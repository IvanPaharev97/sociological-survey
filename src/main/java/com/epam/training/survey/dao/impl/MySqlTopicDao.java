package com.epam.training.survey.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.survey.dao.TopicDao;
import com.epam.training.survey.dao.exception.ConnectionPoolException;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.pool.ConnectionPool;
import com.epam.training.survey.entity.Topic;

import lombok.extern.log4j.Log4j;

@Log4j
public class MySqlTopicDao extends MySqlBaseDao<Topic> implements TopicDao {
    private static final String INSERT_TOPIC = "INSERT INTO topic(name, description) VALUES(?, ?);";
    private static final String UPDATE_TOPIC = "UPDATE topic SET name = ?, description = ? WHERE id = ?;";
    private static final String DELETE_TOPIC = "DELETE FROM topic WHERE id = ?;";
    private static final String SELECT_ALL_TOPICS = "SELECT * FROM topic";
    private static final String SELECT_TOPIC_BY_ID = "SELECT * FROM topic WHERE id = ?;";
    private static final String SELECT_QUESTION_TOPIC = "SELECT * FROM topic "
            + "JOIN question ON topic.id = question.topic_id "
            + "WHERE question.id = ?;";
    
    @Override
    public Topic getQuestionTopic(int topic_id) throws DaoException {
        Topic topic = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_QUESTION_TOPIC);
            statement.setInt(1, topic_id);
            ResultSet rs = statement.executeQuery();
            int count = 0;
            while (rs.next()) {
                if (count > 1) {
                    throw new DaoException("On selecting topic of question select more than 1 topic");
                }
                topic = new Topic();
                topic.setId(rs.getInt("id"));
                topic.setName(rs.getString("name"));
                topic.setDescription(rs.getString("description"));
                count++;
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + SELECT_QUESTION_TOPIC + ", exception: ", e);
            throw new DaoException(e);
        } catch (ConnectionPoolException e) {
            log.error("Exception while getting connection: ", e);
            throw new DaoException(e);
        } finally {
            if (connection != null) {
                try {
                    ConnectionPool.getInstance().returnConnection(connection);
                } catch (ConnectionPoolException e) {
                    log.error("Exception while returning connection: ", e);
                }
            }
        }
        return topic;
    }
    
    @Override
    protected void prepareInsert(PreparedStatement statement, Topic topic) throws SQLException {
        statement.setString(1, topic.getName());
        statement.setString(2, topic.getDescription());
    }
   
    @Override
    protected void prepareUpdate(PreparedStatement statement, Topic topic) throws SQLException {
        statement.setString(1, topic.getName());
        statement.setString(2, topic.getDescription());
        statement.setInt(3, topic.getId());
    }
    
    @Override
    protected List<Topic> parseResultSet(ResultSet rs) throws DaoException {
        List<Topic> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Topic topic = new Topic();
                topic.setId(rs.getInt("id"));
                topic.setName(rs.getString("name"));
                topic.setDescription(rs.getString("description"));
                result.add(topic);
            }
        } catch (SQLException e) {
            log.error("Exception while parsing result set: ", e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_TOPICS;
    }
    
    @Override
    protected String getSelectByIdQuery() {
        return SELECT_TOPIC_BY_ID;
    }

    @Override
    protected String getInsertQuery() {
        return INSERT_TOPIC;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_TOPIC;
    }
    
    @Override
    protected String getDeleteQuery() {
        return DELETE_TOPIC;
    }
}
