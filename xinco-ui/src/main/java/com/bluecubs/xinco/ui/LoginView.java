package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "")
@PageTitle("Login — Xinco DMS")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

  public LoginView() {
    this(new UserSession());
  }

  public LoginView(UserSession session) {
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    TextField username = new TextField("Username");
    PasswordField password = new PasswordField("Password");
    Button login = new Button("Login");

    login.addClickListener(
        e -> {
          try {
            XincoCoreUserServer user =
                new XincoCoreUserServer(username.getValue(), password.getValue());
            if (user.getStatusNumber() == 1) {
              session.setUser(user);
              if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().setAttribute(UserSession.class, session);
              }
              getUI().ifPresent(ui -> ui.navigate(ExplorerView.class));
            } else {
              Notification.show(
                  "Account locked or password expired. Status: " + user.getStatusNumber());
            }
          } catch (Throwable ex) {
            Notification.show("Login failed: " + ex.getMessage());
          }
        });

    add(new H2("Xinco DMS"), username, password, login);
  }
}
