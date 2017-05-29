package com.epam.training.survey.dao;

import java.util.List;

import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.entity.Question;

public interface QuestionDao extends BaseDao<Question> {
    List<Question> selectSurveyQuestions(int survey_id) throws DaoException;
}
