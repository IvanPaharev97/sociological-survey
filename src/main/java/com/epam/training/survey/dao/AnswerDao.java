package com.epam.training.survey.dao;

import java.util.List;

import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.entity.Answer;

public interface AnswerDao {
    void persist(Answer answer) throws DaoException;
    int update(Answer answer) throws DaoException;
    int delete(Answer answer) throws DaoException;
    Answer get(int survey_id, int question_id, int user_id) throws DaoException;
    List<Answer> getAll() throws DaoException;
    List<Answer> getUserAnswers(int user_id) throws DaoException;
}
