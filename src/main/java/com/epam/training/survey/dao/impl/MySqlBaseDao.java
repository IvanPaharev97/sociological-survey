package com.epam.training.survey.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.epam.training.survey.dao.BaseDao;
import com.epam.training.survey.dao.exception.ConnectionPoolException;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.pool.ConnectionPool;
import lombok.extern.log4j.Log4j;

@Log4j
public abstract class MySqlBaseDao<E> implements BaseDao<E> {
   
    protected abstract List<E> parseResultSet(ResultSet rs) throws DaoException;
    
    protected abstract void prepareInsert(PreparedStatement statement, E e) throws SQLException;
    
    protected abstract void prepareUpdate(PreparedStatement statement, E e) throws SQLException;
    
    protected abstract String getSelectAllQuery();
    
    protected abstract String getSelectByIdQuery();
    
    protected abstract String getInsertQuery();
    
    protected abstract String getUpdateQuery();
    
    protected abstract String getDeleteQuery();
    
    @Override
    public E get(int id) throws DaoException {
        E entity = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(getSelectByIdQuery()); 
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            List<E> list = parseResultSet(rs);
            if ((list != null) && (list.size() == 1)) {
                entity = list.iterator().next();
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + getSelectByIdQuery() + ", exception: ", e);
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
        return entity;
    }
    
    @Override
    public List<E> getAll() throws DaoException {
        List<E> result = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(getSelectAllQuery());
            result = parseResultSet(statement.executeQuery());
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + getSelectAllQuery() + ", exception: ", e);
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
        return result;
    }
  
    @Override
    public E persist(E entity) throws DaoException {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(getInsertQuery());
            prepareInsert(statement, entity);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new DaoException("On persist modify more then 1 record: " + count);
            }
            connection.commit();
            statement = connection.prepareStatement(getSelectAllQuery().replaceAll(";", "") + " WHERE id = last_insert_id();"); 
            ResultSet rs = statement.executeQuery();
            List<E> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {
                throw new DaoException("Exception on findByPK new persist data.");
            }
            entity = list.iterator().next();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + getInsertQuery() + ", exception: ", e);
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
        return entity;
    }

    @Override
    public int update(E entity) throws DaoException {
        Connection connection = null;
        int result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(getUpdateQuery());
            prepareUpdate(statement, entity);
            result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("On update modify more then 1 record: " + result);
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + getUpdateQuery() + ", exception: ", e);
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
        return result;
    }

    @Override
    public int delete(int id) throws DaoException {
        Connection connection = null;
        int result;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(getDeleteQuery());
            statement.setInt(1, id);
            result = statement.executeUpdate();
            if (result != 1) {
                throw new DaoException("On delete modify more or less then 1 record: " + result);
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Error while executing SQL query: " + getDeleteQuery() + ", exception: ", e);
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
        return result;
    }

}
