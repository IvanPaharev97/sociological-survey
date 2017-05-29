package com.epam.training.survey.dao.test;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.training.survey.dao.UserDao;
import com.epam.training.survey.dao.exception.ConnectionPoolException;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.impl.MySqlUserDao;
import com.epam.training.survey.dao.pool.ConnectionPool;
import com.epam.training.survey.entity.User;

import static org.junit.Assert.*;

public class MySqlUserDaoTest {
    
    private UserDao userDao = new MySqlUserDao();
    private static int insertedId;
    
    @BeforeClass
    public static void init() {
        try {
            ConnectionPool.getInstance().init();
        } catch (ConnectionPoolException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetAndGetAll() {
        try {
            List<User> users = userDao.getAll();
            assertNotNull(users);
            User user = userDao.get(users.get(0).getId());
            assertNotNull(user);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testPersist() {
        try {
            User user = userDao.persist(new User("test", "test", "test", "test", "test", "test"));
            insertedId = user.getId();
            assertNotNull(user.getId());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpdate() {
        try {
            String newUserEmail = "new email";
            User user = userDao.get(userDao.getAll().get(0).getId());
            user.setEmail(newUserEmail);
            userDao.update(user);
            assertEquals(newUserEmail, userDao.get(user.getId()).getEmail());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDelete() {
        try {
            userDao.delete(insertedId);
            assertNull(userDao.get(insertedId));
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    @Test 
    public void testLoadRolesAndAnswers() {
        try {
            User user = userDao.getAll().get(0);
            userDao.loadUserAnswers(user);
            userDao.loadUserRoles(user);
            assertNotNull(user.getAnswers());
            assertNotNull(user.getRoles());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    @Test 
    public void testGetUsersWithRole() {
        try {
            assertNotNull(userDao.getUsersWithRole(2));
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
