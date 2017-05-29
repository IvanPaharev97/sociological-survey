package com.epam.training.survey.dao.pool;

import com.epam.training.survey.dao.exception.ConnectionPoolException;

import lombok.extern.log4j.Log4j;

@Log4j
public class ConnectionPoolManager {

    public void init() {
        try {
            ConnectionPool.getInstance().init();
        } catch(ConnectionPoolException e){
            log.error("Connection pool initialization exception: ", e);
        }
    }

    public void destroy() {
        try {
            ConnectionPool.getInstance().dispose();
        } catch(ConnectionPoolException e){
            log.error("Connection pool initialization exception: ", e);
        }
    }
}
