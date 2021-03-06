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
import com.epam.training.survey.dao.query.Query;
import com.epam.training.survey.dao.query.constant.QueryPart;
import com.epam.training.survey.dao.query.constant.QuestionTable;
import com.epam.training.survey.dao.query.constant.TopicTable;
import com.epam.training.survey.entity.Topic;

import lombok.extern.log4j.Log4j;

@Log4j
public class MySqlTopicDao extends MySqlBaseDao<Topic> implements TopicDao {
    
    @Override
    public Topic getQuestionTopic(int question_id) throws DaoException {
        Topic topic = null;
        Connection connection = null;
        Query query = new Query(QueryPart.SELECT_START, TopicTable.TABLE_NAME);
        query
            .joinTable(TopicTable.JOIN_QUESTION_TABLE)
            .whereEquals(QuestionTable.ID_FIELD);
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query.getResultQuery());
            statement.setInt(1, question_id);
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
            log.error("Error while executing SQL query: " + query.getResultQuery() + ", exception: ", e);
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
        Query query = new Query(QueryPart.SELECT_START, TopicTable.TABLE_NAME);
        return query.getResultQuery();
    }
    
    @Override
    protected String getSelectByIdQuery() {
        Query query = new Query(QueryPart.SELECT_START, TopicTable.TABLE_NAME);
        query.whereEquals(TopicTable.ID_FIELD);
        return query.getResultQuery();
    }

    @Override
    protected String getInsertQuery() {
        Query query = new Query(QueryPart.INSERT_START, TopicTable.TABLE_NAME);
        query.setCreateOrUpdateBody(TopicTable.INSERT_BODY);
        return query.getResultQuery();
    }

    @Override
    protected String getUpdateQuery() {
        Query query = new Query(QueryPart.UPDATE_START, TopicTable.TABLE_NAME);
        query.setCreateOrUpdateBody(TopicTable.UPDATE_BODY);
        query.whereEquals(TopicTable.ID_FIELD);
        return query.getResultQuery();
    }
    
    @Override
    protected String getDeleteQuery() {
        Query query = new Query(QueryPart.DELETE_START, TopicTable.TABLE_NAME);
        query.whereEquals(TopicTable.ID_FIELD);
        return query.getResultQuery();
    }
}
