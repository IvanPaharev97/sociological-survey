package com.epam.training.survey.dao.factory;

import com.epam.training.survey.dao.AnswerDao;
import com.epam.training.survey.dao.QuestionDao;
import com.epam.training.survey.dao.RoleDao;
import com.epam.training.survey.dao.SurveyDao;
import com.epam.training.survey.dao.TopicDao;
import com.epam.training.survey.dao.UserDao;
import com.epam.training.survey.dao.impl.MySqlAnswerDao;
import com.epam.training.survey.dao.impl.MySqlQuestionDao;
import com.epam.training.survey.dao.impl.MySqlRoleDao;
import com.epam.training.survey.dao.impl.MySqlSurveyDao;
import com.epam.training.survey.dao.impl.MySqlTopicDao;
import com.epam.training.survey.dao.impl.MySqlUserDao;
import com.epam.training.survey.dao.pool.ConnectionPoolManager;

import lombok.Getter;

@Getter
public class DaoFactory {
    private static final DaoFactory INSTANCE = new DaoFactory();

    private UserDao userDao;
    private AnswerDao answerDao;
    private TopicDao topicDao;
    private QuestionDao questionDao;
    private RoleDao roleDao;
    private SurveyDao surveyDao;
    private ConnectionPoolManager poolManager;

    private DaoFactory(){
        userDao = new MySqlUserDao();
        answerDao = new MySqlAnswerDao();
        topicDao = new MySqlTopicDao();
        questionDao = new MySqlQuestionDao();
        roleDao = new MySqlRoleDao();
        surveyDao = new MySqlSurveyDao();
        poolManager = new ConnectionPoolManager();
    }

    public static DaoFactory getInstance(){
        return INSTANCE;
    }
    
}
