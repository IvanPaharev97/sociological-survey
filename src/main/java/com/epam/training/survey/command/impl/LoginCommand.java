package com.epam.training.survey.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.survey.command.Command;
import com.epam.training.survey.entity.User;
import com.epam.training.survey.service.RoleService;
import com.epam.training.survey.service.UserService;
import com.epam.training.survey.service.exception.ServiceException;

import lombok.extern.log4j.Log4j;

@Log4j
public class LoginCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        UserService userService = new UserService();
        RoleService roleService = new RoleService();
        try {
            User user = userService.login(login, password);
            if (user != null) {
                if (!user.getRoles().contains(roleService.getRoleByName("admin"))) {
                    request.getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
                }
            } else {
                request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
            }
        } catch (ServiceException e) {
            log.error("Exception during command execution: ", e);
            response.sendRedirect("/500.jsp");
        }
    }

}
