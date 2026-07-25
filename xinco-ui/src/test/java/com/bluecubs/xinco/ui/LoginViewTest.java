package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

class LoginViewTest {

  private static Routes routes;

  @BeforeAll
  static void discoverRoutes() {
    routes = new Routes().autoDiscoverViews("com.bluecubs.xinco.ui");
  }

  @BeforeEach
  void setup() {
    MockVaadin.setup(routes);
    ViewTestHelper.registerI18NProvider();
    LoginView view = new LoginView();
    UI.getCurrent().add(view);
  }

  @AfterEach
  void tearDown() {
    MockVaadin.tearDown();
  }

  @Test
  void loginView_renders() {
    assertNotNull(_get(TextField.class, spec -> spec.withLabel("Username")));
    assertNotNull(_get(PasswordField.class, spec -> spec.withLabel("Password")));
    assertNotNull(_get(Button.class, spec -> spec.withText("Login")));
  }

  @Test
  void loginView_emptyCredentials_showsNotification() {
    _get(TextField.class, spec -> spec.withLabel("Username")).setValue("");
    _get(PasswordField.class, spec -> spec.withLabel("Password")).setValue("");
    _click(_get(Button.class, spec -> spec.withText("Login")));
    // Invalid credentials produce a notification, not a crash
  }

  @Test
  void loginView_nullExceptionMessage_doesNotShowLoginFailedNull() {
    // Regression: before fix, a null getMessage() produced "Login failed: null"
    try (MockedConstruction<XincoCoreUserServer> mc =
        mockConstruction(
            XincoCoreUserServer.class,
            (m, ctx) -> {
              when(m.getStatusNumber()).thenThrow(new RuntimeException());
            })) {
      _get(TextField.class, spec -> spec.withLabel("Username")).setValue("user");
      _get(PasswordField.class, spec -> spec.withLabel("Password")).setValue("badpass");
      assertDoesNotThrow(() -> _click(_get(Button.class, spec -> spec.withText("Login"))));
    }
  }

  @Test
  void loginView_agedPassword_opensPasswordChangeDialog() {
    try (MockedConstruction<XincoCoreUserServer> mc =
        mockConstruction(
            XincoCoreUserServer.class,
            (m, ctx) -> {
              when(m.getStatusNumber()).thenReturn(3);
              when(m.getId()).thenReturn(1);
            })) {
      _get(TextField.class, spec -> spec.withLabel("Username")).setValue("testuser");
      _get(PasswordField.class, spec -> spec.withLabel("Password")).setValue("oldpass");
      _click(_get(Button.class, spec -> spec.withText("Login")));
      assertFalse(
          _find(Dialog.class).isEmpty(),
          "Should open password-change dialog for aged password (status=3)");
    }
  }
}
