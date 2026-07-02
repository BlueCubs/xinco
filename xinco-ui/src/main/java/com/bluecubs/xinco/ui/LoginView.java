package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.Locale;

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

    Select<Locale> langSelect = new Select<>();
    langSelect.setLabel("Language");
    langSelect.setItems(XincoI18NProvider.PROVIDED_LOCALES);
    langSelect.setItemLabelGenerator(l -> l.getDisplayName(l));
    langSelect.setWidth("300px");

    // Default to current UI locale, fall back to English
    Locale current = getLocale();
    Locale defaultLocale =
        XincoI18NProvider.PROVIDED_LOCALES.contains(current) ? current : Locale.ENGLISH;
    langSelect.setValue(defaultLocale);

    langSelect.addValueChangeListener(e -> getUI().ifPresent(ui -> ui.setLocale(e.getValue())));

    TextField username = new TextField("Username");
    username.setWidth("300px");
    PasswordField password = new PasswordField("Password");
    password.setWidth("300px");
    Button login = new Button("Login");
    login.setWidth("300px");

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
              getUI()
                  .ifPresent(
                      ui -> {
                        ui.setLocale(langSelect.getValue());
                        ui.navigate(ExplorerView.class);
                      });
            } else {
              Notification.show(
                  "Account locked or password expired. Status: " + user.getStatusNumber());
            }
          } catch (Throwable ex) {
            Notification.show("Login failed: " + ex.getMessage());
          }
        });

    add(new H2("Xinco DMS"), langSelect, username, password, login);
  }
}
