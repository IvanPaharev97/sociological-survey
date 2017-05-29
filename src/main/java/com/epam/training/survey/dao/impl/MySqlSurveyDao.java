package com.epam.training.survey.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.survey.dao.QuestionDao;
import com.epam.training.survey.dao.SurveyDao;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.entity.Survey;

import lombok.extern.log4j.Log4j;

@Log4j
public class MySqlSurveyDao extends MySqlBaseDao<Survey> implements SurveyDao {
    private static final String INSERT_SURVEY = "INSERT INTO survey(name, created_date, completed_date) VALUES(?, ?, ?);";
    private static final String UPDATE_SURVEY = "UPDATE survey SET name = ?, created_date = ?, completed_date = ? WHERE id = ?;";
    private static final String DELETE_SURVEY = "DELETE FROM survey WHERE id = ?;";
    private static final String SELECT_ALL_SURVEYS = "SELECT * FROM survey";
    private static final String SELECT_SURVEY_BY_ID = "SELECT * FROM survey WHERE id = ?;";    
    
    @Override
    public void loadSurveyQuestions(Survey survey) throws DaoException {
        QuestionDao questionDao = new MySqlQuestionDao();
        survey.setQuestions(questionDao.selectSurveyQuestions(survey.getId()));
    }

    protected void prepareInsert(PreparedStatement statement, Survey survey) throws SQLException {
        statement.setString(1, survey.getName());
        statement.setDate(2, survey.getCreatedDate());
        statement.setDate(3, survey.getCompletedDate());
    }
   
    @Override
    protected void prepareUpdate(PreparedStatement statement, Survey survey) throws SQLException {
        statement.setString(1, survey.getName());
        statement.setDate(2, survey.getCreatedDate());
        statement.setDate(3, survey.getCompletedDate());
        statement.setInt(4, survey.getId());
    }
    
    @Override
    protected List<Survey> parseResultSet(ResultSet rs) throws DaoException {
        List<Survey> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Survey survey = new Survey();
                survey.setId(rs.getInt("id"));
                survey.setName(rs.getString("name"));
                survey.setCreatedDate(rs.getDate("created_date"));
                survey.setCompletedDate(rs.getDate("completed_date"));
                result.add(survey);
            }
        } catch (SQLException e) {
            log.error("Exception while parsing result set: ", e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_SURVEYS;
    }
    
    @Override
    protected String getSelectByIdQuery() {
        return SELECT_SURVEY_BY_ID;
    }

    @Override
    protected String getInsertQuery() {
        return INSERT_SURVEY;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_SURVEY;
    }
    
    @Override
    protected String getDeleteQuery() {
        return DELETE_SURVEY;
    }
    
}
