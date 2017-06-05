package com.epam.training.survey.command;

import java.util.HashMap;
import java.util.Map;

import com.epam.training.survey.command.impl.LoginCommand;

import static com.epam.training.survey.command.constant.CommandName.*;

public class CommandManager {
    private static final CommandManager INSTANCE = new CommandManager();
    private Map<String, Command> commands;

    private CommandManager(){
        commands = new HashMap<>();
        commands.put(LOGIN, new LoginCommand());
    }

    public static CommandManager getInstance(){
        return INSTANCE;
    }

    public void addCommand(String commandName, Command command){
        commands.put(commandName, command);
    }

    public Command getCommand(String command){
        return commands.get(command);
    }
}
