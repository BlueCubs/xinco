package com.bluecubs.xinco.workflow.persistence.controller.exceptions;

import java.util.ArrayList;
import java.util.List;

public class IllegalOrphanException extends Exception {

    private static final long serialVersionUID = 1L;
    private List<String> messages;

    public IllegalOrphanException(List<String> messages) {
        super((messages != null && messages.size() > 0 ? messages.get(0) : null));
        if (messages == null) {
            this.messages = new ArrayList<String>();
        } else {
            this.messages = messages;
        }
    }

    public List<String> getMessages() {
        return messages;
    }
}
