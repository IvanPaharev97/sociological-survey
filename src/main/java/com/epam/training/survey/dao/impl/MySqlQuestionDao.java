package com.epam.training.survey.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.survey.dao.QuestionDao;
import com.epam.training.survey.dao.TopicDao;
import com.epam.training.survey.dao.exception.ConnectionPoolException;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.pool.ConnectionPool;
import com.epam.training.survey.entity.Question;

import lombok.extern.log4j.Log4j;

@Log4j
public class MySqlQuestionDao extends MySqlBaseDao<Question> implements QuestionDao {
    private static final String INSERT_QUESTION = "INSERT INTO question(topic_id, question) VALUES(?, ?);";
    private static final String UPDATE_QUESTION = "UPDATE question SET topic_id = ?, question = ? WHERE id = ?;";
    private static final String DELETE_QUESTION = "DELETE FROM question WHERE id = ?;";
    private static final String SELECT_ALL_QUESTIONS = "SELECT * FROM question";
    private static final String SELECT_QUESTION_BY_ID = "SELECT * FROM question WHERE id = ?;";    
    private static final String SELECT_SURVEY_QUESTIONS = "SELECT * FROM question "
            + "JOIN survey_has_question ON question.id = survey_has_question.question_id "
            + "WHERE survey_has_question.survey_id = ?;";
    
    @Override
    public List<Question> selectSurveyQuestions(int survey_id) throws DaoException {
        List<Question> questions = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_SURVEY_QUESTIONS);
            statement.setInt(1, survey_id);
            ResultSet rs = statement.executeQuery();
            questions = new ArrayList<>();
            TopicDao topicDao = new MySqlTopicDao();
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getInt("id"));
                question.setTopic(topicDao.getQuestionTopic(question.getId()));
                question.setQuestion(rs.getString("question"));
                questions.add(question);
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + SELECT_SURVEY_QUESTIONS + ", exception: ", e);
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
        return questions;
    }

    protected void prepareInsert(PreparedStatement statement, Question question) throws SQLException {
        statement.setInt(1, question.getTopic().getId());
        statement.setString(2, question.getQuestion());
    }
   
    @Override
    protected void prepareUpdate(PreparedStatement statement, Question question) throws SQLException {
        statement.setInt(1, question.getTopic().getId());
        statement.setString(2, question.getQuestion());
        statement.setInt(3, question.getId());
    }
    
    @Override
    protected List<Question> parseResultSet(ResultSet rs) throws DaoException {
        List<Question> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getInt("id"));
                question.setQuestion(rs.getString("question"));
                result.add(question);
            }
        } catch (SQLException e) {
            log.error("Exception while parsing result set: ", e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_QUESTIONS;
    }
    
    @Override
    protected String getSelectByIdQuery() {
        return SELECT_QUESTION_BY_ID;
    }

    @Override
    protected String getInsertQuery() {
        return INSERT_QUESTION;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_QUESTION;
    }
    
    @Override
    protected String getDeleteQuery() {
        return DELETE_QUESTION;
    }
    
}
