package com.epam.training.survey.service.exception;

public class ServiceException extends Exception {
    private static final long serialVersionUID = -7558373872909674084L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    } 
}