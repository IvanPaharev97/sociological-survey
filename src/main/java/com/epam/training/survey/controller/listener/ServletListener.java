package com.epam.training.survey.controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.epam.training.survey.dao.factory.DaoFactory;

public class ServletListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DaoFactory.getInstance().getPoolManager().destroy();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DaoFactory.getInstance().getPoolManager().init();
    }
    
    
}
