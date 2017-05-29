package com.epam.training.survey.dao;

import java.util.List;

import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.entity.User;

public interface UserDao extends BaseDao<User> {
    void loadUserRoles(User user) throws DaoException;
    void loadUserAnswers(User user) throws DaoException;
    List<User> getUsersWithRole(int role_id) throws DaoException;
    User getUserByEmail(String email) throws DaoException;
}
