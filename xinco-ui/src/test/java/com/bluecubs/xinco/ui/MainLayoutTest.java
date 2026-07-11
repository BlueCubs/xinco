package com.bluecubs.xinco.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainLayoutTest {

  private static Routes routes;

  @BeforeAll
  static void discoverRoutes() {
    routes = new Routes().autoDiscoverViews("com.bluecubs.xinco.ui");
  }

  @BeforeEach
  void setup() {
    MockVaadin.setup(routes);
  }

  @AfterEach
  void tearDown() {
    MockVaadin.tearDown();
  }

  @Test
  void mainLayout_notLoggedIn_renders() {
    UserSession session = new UserSession();
    assertNotNull(new MainLayout(session));
  }

  @Test
  void mainLayout_loggedIn_showsUserInfo() {
    XincoCoreUserServer mockUser = mock(XincoCoreUserServer.class);
    when(mockUser.getUsername()).thenReturn("admin");
    UserSession session = new UserSession();
    session.setUser(mockUser);
    assertNotNull(new MainLayout(session));
  }
}
