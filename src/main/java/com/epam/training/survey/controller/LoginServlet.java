package com.epam.training.survey.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.survey.entity.User;
import com.epam.training.survey.service.RoleService;
import com.epam.training.survey.service.UserService;
import com.epam.training.survey.service.exception.ServiceException;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 4286305586373975184L;

    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            User user = userService.login(login, password);
            if (user != null) {
                if (!user.getRoles().contains(roleService.getRoleByName("admin"))) {
                    req.getRequestDispatcher("/WEB-INF/user.jsp").forward(req, resp);
                } else {
                    req.getRequestDispatcher("/WEB-INF/admin.jsp").forward(req, resp);
                }
            } else {
                req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
            }
        } catch (ServiceException e) {
            //TODO process exception
            e.printStackTrace();
        }
    }
    
}
