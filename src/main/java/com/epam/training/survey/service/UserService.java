package com.epam.training.survey.service;

import com.epam.training.survey.dao.UserDao;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.impl.MySqlUserDao;
import com.epam.training.survey.entity.User;
import com.epam.training.survey.service.exception.ServiceException;

import lombok.extern.log4j.Log4j;

@Log4j
public class UserService {
    private UserDao userDao;

    public UserService() {
        super();
        this.userDao = new MySqlUserDao();
    }
    
    public User login(String email, String password) throws ServiceException {
        User user;
        try {
            user = userDao.getUserByEmail(email);
            if (user != null) {
                userDao.loadUserRoles(user);
            }
        } catch (DaoException e) {
            log.error("Exception on user login process: ", e);
            throw new ServiceException("Exception on user login process: ", e);
        }
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }
    
}
