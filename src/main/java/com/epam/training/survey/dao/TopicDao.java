package com.epam.training.survey.dao;

import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.entity.Topic;

public interface TopicDao extends BaseDao<Topic> {
    Topic getQuestionTopic(int question_id) throws DaoException;    
}
