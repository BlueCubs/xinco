package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

@Component
@VaadinSessionScope
public class UserSession {

  private XincoCoreUserServer user;

  public XincoCoreUserServer getUser() {
    return user;
  }

  public void setUser(XincoCoreUserServer user) {
    this.user = user;
  }

  public boolean isLoggedIn() {
    return user != null;
  }

  public void logout() {
    user = null;
  }
}
