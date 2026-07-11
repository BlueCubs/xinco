package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@VaadinSessionScope
public class UserSession {

  @Getter @Setter private XincoCoreUserServer user;

  public boolean isLoggedIn() {
    return user != null;
  }

  public void logout() {
    user = null;
  }
}
