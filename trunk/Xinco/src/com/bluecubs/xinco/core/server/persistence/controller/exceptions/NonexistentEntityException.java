package com.bluecubs.xinco.core.server.persistence.controller.exceptions;

public class NonexistentEntityException extends Error {

    private static final long serialVersionUID = 1L;

    public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonexistentEntityException(String message) {
        super(message);
    }
}
