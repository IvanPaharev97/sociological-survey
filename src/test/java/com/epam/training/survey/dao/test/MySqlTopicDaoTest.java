package com.epam.training.survey.dao.test;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.training.survey.dao.QuestionDao;
import com.epam.training.survey.dao.TopicDao;
import com.epam.training.survey.dao.exception.ConnectionPoolException;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.impl.MySqlQuestionDao;
import com.epam.training.survey.dao.impl.MySqlTopicDao;
import com.epam.training.survey.dao.pool.ConnectionPool;
import com.epam.training.survey.entity.Topic;

import lombok.extern.log4j.Log4j;

import static org.junit.Assert.*;

@Log4j
public class MySqlTopicDaoTest {
    
    private TopicDao topicDao = new MySqlTopicDao();
    private QuestionDao questionDao = new MySqlQuestionDao();
    private static int insertedId;
    
    @BeforeClass
    public static void init() {
        try {
            ConnectionPool.getInstance().init();
        } catch (ConnectionPoolException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
    
    @AfterClass
    public static void destroy() {
        try {
            ConnectionPool.getInstance().destroy();
        } catch (ConnectionPoolException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetAndGetAll() {
        try {
            List<Topic> topics = topicDao.getAll();
            assertNotNull(topics);
            Topic topic = topicDao.get(topics.get(0).getId());
            assertNotNull(topic);
        } catch (DaoException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
    
    @Test
    public void testPersist() {
        try {
            Topic topic = topicDao.persist(new Topic("test", "test"));
            insertedId = topic.getId();
            assertNotNull(topic.getId());
        } catch (DaoException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpdate() {
        try {
            String newTopicName = "new topic";
            Topic topic = topicDao.get(topicDao.getAll().get(0).getId());
            String oldTopicName = topic.getName();
            topic.setName(newTopicName);
            topicDao.update(topic);
            assertEquals(newTopicName, topicDao.get(topic.getId()).getName());
            topic.setName(oldTopicName);
            topicDao.update(topic);
        } catch (DaoException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDelete() {
        try {
            topicDao.delete(insertedId);
            assertNull(topicDao.get(insertedId));
        } catch (DaoException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetQuestionTopic() {
        try {
            Topic topic = topicDao.getQuestionTopic(questionDao.getAll().get(0).getId());
            assertNotNull(topic);
        } catch (DaoException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
}
