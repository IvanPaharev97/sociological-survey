package com.epam.training.survey.dao.test;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.training.survey.dao.TopicDao;
import com.epam.training.survey.dao.exception.ConnectionPoolException;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.impl.MySqlTopicDao;
import com.epam.training.survey.dao.pool.ConnectionPool;
import com.epam.training.survey.entity.Topic;

import static org.junit.Assert.*;

public class MySqlTopicDaoTest {
    
    private TopicDao topicDao = new MySqlTopicDao();
    private static int insertedId;
    
    @BeforeClass
    public static void init() {
        try {
            ConnectionPool.getInstance().init();
        } catch (ConnectionPoolException e) {
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
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpdate() {
        try {
            String newTopicName = "new topic";
            Topic topic = topicDao.get(topicDao.getAll().get(0).getId());
            topic.setName(newTopicName);
            topicDao.update(topic);
            assertEquals(newTopicName, topicDao.get(topic.getId()).getName());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDelete() {
        try {
            topicDao.delete(insertedId);
            assertNull(topicDao.get(insertedId));
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
