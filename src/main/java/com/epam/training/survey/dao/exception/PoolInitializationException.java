package com.epam.training.survey.dao.exception;

public class PoolInitializationException extends RuntimeException {
    private static final long serialVersionUID = 5473787717176100646L;

    public PoolInitializationException() {
        super();
    }

    public PoolInitializationException(String message) {
        super(message);
    }

    public PoolInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PoolInitializationException(Throwable cause) {
        super(cause);
    }
}
