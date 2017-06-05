package com.epam.training.survey.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.training.survey.command.CommandManager;
import lombok.extern.log4j.Log4j;

@Log4j
@WebServlet("/ControllerServlet")
public class ControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 4286305586373975184L;
    private static final String COMMAND = "command";
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        process(req, resp);
    }
    
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String command = request.getParameter(COMMAND);
            CommandManager.getInstance().getCommand(command).execute(request, response);
        } catch (ServletException e) {
            log.error("Exception during processing request: ", e);
            response.sendRedirect("/500.jsp");
        }
    }
    
}
