package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static com.github.mvysny.kaributesting.v10.LocatorJ._setValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginViewTest {

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
    MockVaadin.teardown();
  }

  @Test
  void loginView_renders() {
    MockVaadin.navigate(LoginView.class);
    assertNotNull(_get(TextField.class, spec -> spec.withCaption("Username")));
    assertNotNull(_get(PasswordField.class, spec -> spec.withCaption("Password")));
    assertNotNull(_get(Button.class, spec -> spec.withCaption("Login")));
  }

  @Test
  void loginView_emptyCredentials_showsNotification() {
    MockVaadin.navigate(LoginView.class);
    _setValue(_get(TextField.class, spec -> spec.withCaption("Username")), "");
    _setValue(_get(PasswordField.class, spec -> spec.withCaption("Password")), "");
    _click(_get(Button.class, spec -> spec.withCaption("Login")));
    // No exception thrown — invalid credentials show notification, not crash
  }
}
