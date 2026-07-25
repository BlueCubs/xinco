package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
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
    langSelect.setLabel(getTranslation("general.language"));
    langSelect.setItems(XincoI18NProvider.PROVIDED_LOCALES);
    langSelect.setItemLabelGenerator(l -> l.getDisplayName(l));
    langSelect.setWidth("300px");

    // Default to current UI locale, fall back to English
    Locale current = getLocale();
    Locale defaultLocale =
        XincoI18NProvider.PROVIDED_LOCALES.contains(current) ? current : Locale.ENGLISH;
    langSelect.setValue(defaultLocale);

    langSelect.addValueChangeListener(e -> getUI().ifPresent(ui -> ui.setLocale(e.getValue())));

    TextField username = new TextField(getTranslation("general.username"));
    username.setWidth("300px");
    PasswordField password = new PasswordField(getTranslation("general.password"));
    password.setWidth("300px");
    Button login = new Button(getTranslation("general.login"));
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
                        ui.navigate(ViewerView.class);
                      });
            } else if (user.getStatusNumber() == 3) {
              showPasswordChangeDialog(user, session, langSelect.getValue());
            } else {
              Notification.show(
                  "Account locked or password expired. Status: " + user.getStatusNumber());
            }
          } catch (Throwable ex) {
            String msg =
                ex.getMessage() != null ? ex.getMessage() : "Invalid username or password.";
            Notification.show("Login failed: " + msg);
          }
        });

    add(new H2(getTranslation("general.clienttitle")), langSelect, username, password, login);
  }

  private void showPasswordChangeDialog(
      XincoCoreUserServer user, UserSession session, Locale locale) {
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(getTranslation("general.password.reset"));
    dialog.setCloseOnOutsideClick(false);

    PasswordField newPassword = new PasswordField(getTranslation("general.password"));
    PasswordField confirmPassword = new PasswordField(getTranslation("general.verifypassword"));

    Button cancel = new Button(getTranslation("general.cancel"), e -> dialog.close());
    Button save = new Button(getTranslation("general.password.reset"));
    save.addClickListener(
        e -> {
          if (newPassword.getValue().isEmpty() || confirmPassword.getValue().isEmpty()) {
            Notification.show(getTranslation("message.missing.password"));
            return;
          }
          if (!newPassword.getValue().equals(confirmPassword.getValue())) {
            Notification.show(getTranslation("password.noMatch"));
            return;
          }
          try {
            user.setUserpassword(newPassword.getValue());
            user.setHashPassword(true);
            user.setStatusNumber(4);
            user.setChange(true);
            user.setChangerID(user.getId());
            user.write2DB();
            session.setUser(user);
            if (VaadinSession.getCurrent() != null) {
              VaadinSession.getCurrent().setAttribute(UserSession.class, session);
            }
            dialog.close();
            getUI()
                .ifPresent(
                    ui -> {
                      ui.setLocale(locale);
                      ui.navigate(ViewerView.class);
                    });
          } catch (Throwable ex) {
            Notification.show(getTranslation("general.error") + ": " + ex.getMessage());
          }
        });

    dialog.add(new VerticalLayout(newPassword, confirmPassword));
    dialog.getFooter().add(cancel, save);
    dialog.open();
  }
}
