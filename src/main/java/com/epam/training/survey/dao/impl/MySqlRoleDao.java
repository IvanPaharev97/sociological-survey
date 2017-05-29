package com.epam.training.survey.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.survey.dao.RoleDao;
import com.epam.training.survey.dao.UserDao;
import com.epam.training.survey.dao.exception.ConnectionPoolException;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.pool.ConnectionPool;
import com.epam.training.survey.entity.Role;

import lombok.extern.log4j.Log4j;

@Log4j
public class MySqlRoleDao extends MySqlBaseDao<Role> implements RoleDao {
    private static final String INSERT_ROLE = "INSERT INTO role(name) VALUES(?);";
    private static final String UPDATE_ROLE = "UPDATE role SET name = ? WHERE id = ?;";
    private static final String DELETE_ROLE = "DELETE FROM role WHERE id = ?;";
    private static final String SELECT_ALL_ROLES = "SELECT * FROM role";
    private static final String SELECT_ROLE_BY_ID = "SELECT * FROM role WHERE id = ?;";    
    private static final String SELECT_ROLE_BY_NAME = "SELECT * FROM role WHERE name = ?;";    
    private static final String SELECT_USER_ROLES = "SELECT * FROM role "
            + "JOIN user_has_role ON role.id = user_has_role.role_id "
            + "WHERE user_has_role.user_id = ?;";
    
    @Override
    public List<Role> getUserRoles(int user_id) throws DaoException {
        List<Role> roles = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_ROLES);
            statement.setInt(1, user_id);
            roles = parseResultSet(statement.executeQuery());
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + SELECT_USER_ROLES + ", exception: ", e);
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
    public Role getRoleByName(String name) throws DaoException {
        Role role = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ROLE_BY_NAME);
            statement.setString(1, name);
            List<Role> roles= parseResultSet(statement.executeQuery());
            role = roles.get(0);
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + SELECT_ROLE_BY_NAME + ", exception: ", e);
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
        return role;
    }

    @Override
    public void loadRoleUsers(Role role) throws DaoException {
        UserDao userDao = new MySqlUserDao();
        role.setUsers(userDao.getUsersWithRole(role.getId()));
    }

    protected void prepareInsert(PreparedStatement statement, Role role) throws SQLException {
        statement.setString(1, role.getName());
    }
   
    @Override
    protected void prepareUpdate(PreparedStatement statement, Role role) throws SQLException {
        statement.setString(1, role.getName());
        statement.setInt(2, role.getId());
    }
    
    @Override
    protected List<Role> parseResultSet(ResultSet rs) throws DaoException {
        List<Role> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                result.add(role);
            }
        } catch (SQLException e) {
            log.error("Exception while parsing result set: ", e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_ROLES;
    }
    
    @Override
    protected String getSelectByIdQuery() {
        return SELECT_ROLE_BY_ID;
    }

    @Override
    protected String getInsertQuery() {
        return INSERT_ROLE;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_ROLE;
    }
    
    @Override
    protected String getDeleteQuery() {
        return DELETE_ROLE;
    }
    
}
