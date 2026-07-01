package com.bluecubs.xinco.core.server.persistence.controller.exceptions;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class IllegalOrphanException extends Exception {

  @Getter private List<String> messages;

  public IllegalOrphanException(List<String> messages) {
    super(messages != null && messages.size() > 0 ? messages.get(0) : null);
    if (messages == null) {
      this.messages = new ArrayList<>();
    } else {
      this.messages = messages;
    }
  }
}
