package com.epam.training.survey.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.survey.dao.AnswerDao;
import com.epam.training.survey.dao.RoleDao;
import com.epam.training.survey.dao.UserDao;
import com.epam.training.survey.dao.exception.ConnectionPoolException;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.pool.ConnectionPool;
import com.epam.training.survey.entity.User;

import lombok.extern.log4j.Log4j;

@Log4j
public class MySqlUserDao extends MySqlBaseDao<User> implements UserDao {
    private static final String INSERT_USER = "INSERT INTO user(email, password, firstname, lastname, phone, user_info) "
            + "VALUES(?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE user SET email = ?, password = ?, firstname = ? , "
            + "lastname = ?, phone = ?, user_info = ? WHERE id = ?;";
    private static final String DELETE_USER = "DELETE FROM user WHERE id = ?;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM user";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE id = ?;";    
    private static final String SELECT_ROLE_USERS = "SELECT * FROM user "
            + "JOIN user_has_role ON user.id = user_has_role.user_id "
            + "WHERE user_has_role.role_id = ?;";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM user WHERE email = ?;";
    
    @Override
    public void loadUserRoles(User user) throws DaoException {
        RoleDao roleDao = new MySqlRoleDao();
        user.setRoles(roleDao.getUserRoles(user.getId()));
    }
    
    @Override
    public void loadUserAnswers(User user) throws DaoException {
        AnswerDao answerDao = new MySqlAnswerDao();
        user.setAnswers(answerDao.getUserAnswers(user.getId()));
    }

    @Override
    public List<User> getUsersWithRole(int role_id) throws DaoException {
        List<User> roles = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ROLE_USERS);
            statement.setInt(1, role_id);
            roles = parseResultSet(statement.executeQuery());
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + SELECT_ROLE_USERS + ", exception: ", e);
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
        return roles;
    }

    @Override
    public User getUserByEmail(String email) throws DaoException {
        User user = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_EMAIL);
            statement.setString(1, email);
            List<User> users = parseResultSet(statement.executeQuery());
            user = users.get(0);
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + SELECT_USER_BY_EMAIL + ", exception: ", e);
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
        return user;
    }

    protected void prepareInsert(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstname());
        statement.setString(4, user.getLastname());
        statement.setString(5, user.getPhone());
        statement.setString(6, user.getUserInfo());
    }
   
    @Override
    protected void prepareUpdate(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstname());
        statement.setString(4, user.getLastname());
        statement.setString(5, user.getPhone());
        statement.setString(6, user.getUserInfo());
        statement.setInt(7, user.getId());
    }
    
    @Override
    protected List<User> parseResultSet(ResultSet rs) throws DaoException {
        List<User> result = new ArrayList<>();
        try {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                user.setPhone(rs.getString("phone"));
                user.setUserInfo(rs.getString("user_info"));
                result.add(user);
            }
        } catch (SQLException e) {
            log.error("Exception while parsing result set: ", e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_USERS;
    }
    
    @Override
    protected String getSelectByIdQuery() {
        return SELECT_USER_BY_ID;
    }

    @Override
    protected String getInsertQuery() {
        return INSERT_USER;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_USER;
    }
    
    @Override
    protected String getDeleteQuery() {
        return DELETE_USER;
    }
    
}
