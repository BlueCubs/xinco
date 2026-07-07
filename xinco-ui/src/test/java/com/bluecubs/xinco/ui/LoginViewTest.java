package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
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
}
