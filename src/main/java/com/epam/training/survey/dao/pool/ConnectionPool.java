package com.epam.training.survey.dao.pool;

import com.epam.training.survey.dao.exception.ConnectionPoolException;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
public class ConnectionPool {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/sociological_survey";
    private static final String USER = "root";
    private static final String PASSWORD = "titanquest2";
    private static final String POOL_SIZE = "10";
    private static final ConnectionPool instance = new ConnectionPool();
    private static ReentrantLock lock = new ReentrantLock();
    
    private BlockingQueue<Connection> freeConnections;
    private BlockingQueue<Connection> busyConnections;
    private volatile boolean isInit = false;

    private ConnectionPool(){}

    public static ConnectionPool getInstance() {
        return instance;
    }

    public void init() throws ConnectionPoolException {
        try {
            lock.lock();
            if(!isInit){
                int poolSize = Integer.parseInt(POOL_SIZE);
                freeConnections = new ArrayBlockingQueue<>(poolSize);
                busyConnections = new ArrayBlockingQueue<>(poolSize);

                try {
                    Class.forName(DRIVER);
                    for(int i = 0; i < poolSize; i++){
                        Connection connection = DriverManager.getConnection(
                                URL,
                                USER,
                                PASSWORD
                        );
                        connection.setAutoCommit(false);
                        freeConnections.add(connection);
                    }
                    isInit = true;
                    log.info("Connection pool was successfully initialized");
                } catch(ClassNotFoundException e){
                    throw new ConnectionPoolException("Not found atabase driver: ", e);
                } catch(SQLException e){
                    throw new ConnectionPoolException("Connection pool initialization fail: ", e);
                }
            } else {
                throw new ConnectionPoolException("Try to inititialize initialized connection pool");
            }
        } finally {
            lock.unlock();
        }
    }

    public Connection getConnection() throws ConnectionPoolException {
        try {
            Connection connection = freeConnections.take();
            busyConnections.add(connection);
            return connection;
        } catch(InterruptedException e){
            throw new ConnectionPoolException("Taking interrupted exception while getting connection", e);
        }
    }

    public void returnConnection(Connection connection) throws ConnectionPoolException {
        if(busyConnections.contains(connection)){
            try {
                busyConnections.remove(connection);
                freeConnections.put(connection);
            } catch(InterruptedException e){
                throw new ConnectionPoolException("Taking interrupted exception while returning connection", e);
            }
        } else {
            throw new ConnectionPoolException("Take to close not a pool connection");
        }
    }

    public void dispose() throws ConnectionPoolException {
        try {
            lock.lock();
            if(isInit){
                try {
                    for(Connection connection : freeConnections){
                        connection.close();
                    }
                    for(Connection connection : busyConnections){
                        connection.close();
                    }
                    isInit = false;

                    log.info("Connection pool was successfully destroyed");
                } catch(SQLException e){
                    throw new ConnectionPoolException("Connection pool dispose fail", e);
                }
            } else {
                throw new ConnectionPoolException("Try to dispose not initialized pool");
            }
        } finally {
            lock.unlock();
        }
    }
}