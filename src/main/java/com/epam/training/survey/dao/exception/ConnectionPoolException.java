package com.epam.training.survey.dao.exception;

public class ConnectionPoolException extends Exception {
    private static final long serialVersionUID = -8504230107867152346L;

    public ConnectionPoolException() {
        super();
    }

    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionPoolException(Throwable cause) {
        super(cause);
    }
}
