package com.epam.training.survey.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.survey.dao.AnswerDao;
import com.epam.training.survey.dao.QuestionDao;
import com.epam.training.survey.dao.SurveyDao;
import com.epam.training.survey.dao.UserDao;
import com.epam.training.survey.dao.exception.ConnectionPoolException;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.pool.ConnectionPool;
import com.epam.training.survey.entity.Answer;

import lombok.extern.log4j.Log4j;

@Log4j
public class MySqlAnswerDao implements AnswerDao {
    private static final String INSERT_ANSWER = "INSERT INTO answer(survey_id, question_id, user_id, answer) VALUES(?, ?, ?, ?);";
    private static final String UPDATE_ANSWER = "UPDATE answer SET survey_id = ?, question_id = ?, user_id = ?, answer = ? "
            + "WHERE survey_id = ? AND question_id = ? AND user_id = ?;";
    private static final String DELETE_ANSWER = "DELETE FROM answer WHERE survey_id = ? AND question_id = ? AND user_id = ?;";
    private static final String SELECT_ALL_ANSWERS = "SELECT * FROM answer";
    private static final String SELECT_ANSWER_BY_ID = "SELECT * FROM answer WHERE survey_id = ? AND question_id = ? AND user_id = ?;";    
    private static final String SELECT_USER_ANSWERS = "SELECT * FROM answer "
            + "JOIN user ON answer.user_id = user.id "
            + "WHERE user.id = ?;";
      
    @Override
    public Answer get(int survey_id, int question_id, int user_id) throws DaoException {
        Answer entity = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ANSWER_BY_ID); 
            statement.setInt(1, survey_id);
            statement.setInt(2, question_id);
            statement.setInt(3, user_id);
            ResultSet rs = statement.executeQuery();
            List<Answer> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {
                throw new DaoException("Exception on findByPK data.");
            }
            entity = list.iterator().next();
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + SELECT_ANSWER_BY_ID + ", exception: ", e);
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
        return entity;
    }
    
    @Override
    public List<Answer> getAll() throws DaoException {
        List<Answer> result = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ANSWERS);
            result = parseResultSet(statement.executeQuery());
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + SELECT_ALL_ANSWERS + ", exception: ", e);
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
        return result;
    }
  
    @Override
    public void persist(Answer answer) throws DaoException {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_ANSWER);
            statement.setInt(1, answer.getSurvey().getId());
            statement.setInt(2, answer.getQuestion().getId());
            statement.setInt(3, answer.getUser().getId());
            statement.setString(4, answer.getAnswer());
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new DaoException("On persist modify more then 1 record: " + count);
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + INSERT_ANSWER + ", exception: ", e);
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
    }

    @Override
    public int update(Answer answer) throws DaoException {
        Connection connection = null;
        int result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_ANSWER);
            statement.setInt(1, answer.getSurvey().getId());
            statement.setInt(2, answer.getQuestion().getId());
            statement.setInt(3, answer.getUser().getId());
            statement.setString(4, answer.getAnswer());
            result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("On update modify more then 1 record: " + result);
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + UPDATE_ANSWER + ", exception: ", e);
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
        return result;
    }

    @Override
    public int delete(Answer answer) throws DaoException {
        Connection connection = null;
        int result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_ANSWER);
            statement.setInt(1, answer.getSurvey().getId());
            statement.setInt(2, answer.getQuestion().getId());
            statement.setInt(3, answer.getUser().getId());
            result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("On delete modify more then 1 record: " + result);
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + DELETE_ANSWER + ", exception: ", e);
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
        return result;
    }

    @Override
    public List<Answer> getUserAnswers(int user_id) throws DaoException {
        List<Answer> answers = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_ANSWERS);
            statement.setInt(1, user_id);
            answers = parseResultSet(statement.executeQuery());
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + SELECT_USER_ANSWERS + ", exception: ", e);
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
        return answers;
    }
    
    private List<Answer> parseResultSet(ResultSet rs) throws DaoException {
        List<Answer> result = new ArrayList<>();
        try {
            SurveyDao surveyDao = new MySqlSurveyDao();
            QuestionDao questionDao = new MySqlQuestionDao();
            UserDao userDao = new MySqlUserDao();
            while (rs.next()) {
                Answer answer = new Answer();
                answer.setSurvey(surveyDao.get(rs.getInt("survey_id")));
                answer.setQuestion(questionDao.get(rs.getInt("question_id")));
                answer.setUser(userDao.get(rs.getInt("user_id")));
                answer.setAnswer(rs.getString("answer"));
                result.add(answer);
            }
        } catch (SQLException e) {
            log.error("Exception while parsing result set: ", e);
            throw new DaoException(e);
        }
        return result;
    }
}
