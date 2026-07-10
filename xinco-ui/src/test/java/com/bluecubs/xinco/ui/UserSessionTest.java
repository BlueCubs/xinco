package com.bluecubs.xinco.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import org.junit.jupiter.api.Test;

class UserSessionTest {

  @Test
  void isLoggedIn_false_initially() {
    assertFalse(new UserSession().isLoggedIn());
  }

  @Test
  void getUser_null_initially() {
    assertNull(new UserSession().getUser());
  }

  @Test
  void setUser_isLoggedIn_true() {
    XincoCoreUserServer mockUser = mock(XincoCoreUserServer.class);
    UserSession session = new UserSession();
    session.setUser(mockUser);
    assertTrue(session.isLoggedIn());
    assertEquals(mockUser, session.getUser());
  }

  @Test
  void logout_clearsUser() {
    XincoCoreUserServer mockUser = mock(XincoCoreUserServer.class);
    UserSession session = new UserSession();
    session.setUser(mockUser);
    session.logout();
    assertFalse(session.isLoggedIn());
    assertNull(session.getUser());
  }
}
