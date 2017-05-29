package com.epam.training.survey.dao;

import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.entity.Survey;

public interface SurveyDao extends BaseDao<Survey> {
    public void loadSurveyQuestions(Survey survey) throws DaoException;
}
