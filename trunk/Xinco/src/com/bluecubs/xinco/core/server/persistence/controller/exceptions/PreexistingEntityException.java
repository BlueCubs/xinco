package com.bluecubs.xinco.core.server.persistence.controller.exceptions;

public class PreexistingEntityException extends Error {

    private static final long serialVersionUID = 1L;

    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreexistingEntityException(String message) {
        super(message);
    }
}
