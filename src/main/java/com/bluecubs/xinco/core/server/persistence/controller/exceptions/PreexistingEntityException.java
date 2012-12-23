package com.bluecubs.xinco.core.server.persistence.controller.exceptions;

public class PreexistingEntityException extends Error {

    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreexistingEntityException(String message) {
        super(message);
    }
}
