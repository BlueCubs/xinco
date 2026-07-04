package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Admin — Xinco DMS")
@AnonymousAllowed
public class AdminView extends VerticalLayout {

  private static final Logger logger = Logger.getLogger(AdminView.class.getName());

  private final UserSession session;
  private Grid<XincoCoreUserServer> userGrid;
  private Grid<XincoCoreGroupServer> groupGrid;

  public AdminView(UserSession session) {
    this.session = session;
    setSizeFull();
    setPadding(false);

    TabSheet tabs = new TabSheet();
    tabs.setSizeFull();
    tabs.add("Users", buildUsersTab());
    tabs.add("Groups", buildGroupsTab());

    add(tabs);
  }

  private VerticalLayout buildUsersTab() {
    userGrid = new Grid<>();
    userGrid.setSizeFull();
    userGrid.addColumn(XincoCoreUserServer::getId).setHeader("ID").setWidth("60px").setFlexGrow(0);
    userGrid.addColumn(XincoCoreUserServer::getUsername).setHeader("Username");
    userGrid.addColumn(u -> u.getFirstName() + " " + u.getLastName()).setHeader("Name");
    userGrid.addColumn(XincoCoreUserServer::getEmail).setHeader("Email");
    userGrid.addColumn(u -> u.getStatusNumber() == 1 ? "Active" : "Locked").setHeader("Status");
    userGrid.setItems(loadUsers());

    Button btnNew = new Button("New User", e -> openUserDialog(null));
    Button btnEdit =
        new Button(
            "Edit",
            e -> userGrid.asSingleSelect().getOptionalValue().ifPresent(this::openUserDialog));
    Button btnLock =
        new Button(
            "Lock/Unlock",
            e -> userGrid.asSingleSelect().getOptionalValue().ifPresent(this::toggleUserLock));
    Button btnDelete =
        new Button(
            "Delete",
            e -> userGrid.asSingleSelect().getOptionalValue().ifPresent(this::confirmDeleteUser));
    btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    HorizontalLayout toolbar = new HorizontalLayout(btnNew, btnEdit, btnLock, btnDelete);
    toolbar.setPadding(true);

    VerticalLayout layout = new VerticalLayout(toolbar, userGrid);
    layout.setSizeFull();
    layout.setFlexGrow(1, userGrid);
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  private VerticalLayout buildGroupsTab() {
    groupGrid = new Grid<>();
    groupGrid.setSizeFull();
    groupGrid
        .addColumn(XincoCoreGroupServer::getId)
        .setHeader("ID")
        .setWidth("60px")
        .setFlexGrow(0);
    groupGrid.addColumn(XincoCoreGroupServer::getDesignation).setHeader("Name");
    groupGrid.setItems(loadGroups());

    Button btnNew = new Button("New Group", e -> openGroupDialog(null));
    Button btnEdit =
        new Button(
            "Edit",
            e -> groupGrid.asSingleSelect().getOptionalValue().ifPresent(this::openGroupDialog));
    Button btnDelete =
        new Button(
            "Delete",
            e ->
                groupGrid.asSingleSelect().getOptionalValue().ifPresent(this::confirmDeleteGroup));
    btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    HorizontalLayout toolbar = new HorizontalLayout(btnNew, btnEdit, btnDelete);
    toolbar.setPadding(true);

    VerticalLayout layout = new VerticalLayout(toolbar, groupGrid);
    layout.setSizeFull();
    layout.setFlexGrow(1, groupGrid);
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  void openUserDialog(XincoCoreUserServer existing) {
    boolean isNew = existing == null;
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(isNew ? "New User" : "Edit User");
    dialog.setWidth("500px");

    TextField username = new TextField("Username");
    username.setRequired(true);
    username.setReadOnly(!isNew);

    PasswordField password = new PasswordField(isNew ? "Password" : "Password (blank = keep)");
    password.setRequired(isNew);

    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");
    TextField email = new TextField("Email");

    Select<Integer> status = new Select<>();
    status.setLabel("Status");
    status.setItems(1, 2);
    status.setItemLabelGenerator(s -> s == 1 ? "Active" : "Locked");

    List<XincoCoreGroupServer> allGroups = loadGroups();
    MultiSelectListBox<XincoCoreGroupServer> groupBox = new MultiSelectListBox<>();
    groupBox.setItems(allGroups);
    groupBox.setItemLabelGenerator(XincoCoreGroupServer::getDesignation);

    if (!isNew) {
      username.setValue(nvl(existing.getUsername()));
      firstName.setValue(nvl(existing.getFirstName()));
      lastName.setValue(nvl(existing.getLastName()));
      email.setValue(nvl(existing.getEmail()));
      status.setValue(existing.getStatusNumber());
      Set<XincoCoreGroupServer> selected = matchByGroupId(loadUserGroups(existing.getId()), allGroups);
      groupBox.setValue(selected);
    } else {
      status.setValue(1);
    }

    FormLayout form = new FormLayout(username, password, firstName, lastName, email, status);
    form.setColspan(username, 2);
    form.setColspan(password, 2);
    form.setColspan(email, 2);

    VerticalLayout content = new VerticalLayout(form, new Span("Groups:"), groupBox);
    content.setPadding(false);
    dialog.add(content);

    Button save =
        new Button(
            "Save",
            e -> {
              if (username.isEmpty()) {
                username.setErrorMessage("Required");
                username.setInvalid(true);
                return;
              }
              if (isNew && password.isEmpty()) {
                password.setErrorMessage("Required");
                password.setInvalid(true);
                return;
              }
              try {
                XincoCoreUserServer user;
                if (isNew) {
                  user =
                      new XincoCoreUserServer(
                          0,
                          username.getValue().trim(),
                          password.getValue(),
                          lastName.getValue().trim(),
                          firstName.getValue().trim(),
                          email.getValue().trim(),
                          status.getValue(),
                          0,
                          new Timestamp(System.currentTimeMillis()));
                  user.setHashPassword(true);
                } else {
                  user = existing;
                  user.setFirstName(firstName.getValue().trim());
                  user.setLastName(lastName.getValue().trim());
                  user.setEmail(email.getValue().trim());
                  user.setStatusNumber(status.getValue());
                  if (!password.isEmpty()) {
                    user.setUserpassword(password.getValue());
                    user.setHashPassword(true);
                  }
                }
                user.setChange(true);
                user.setChangerID(adminId());
                user.setWriteGroups(false);
                user.write2DB();
                saveUserGroups(user.getId(), groupBox.getSelectedItems(), allGroups);
                dialog.close();
                refreshUsers();
                showSuccess(isNew ? "User created." : "User updated.");
              } catch (Exception ex) {
                logger.log(Level.SEVERE, "Save user failed", ex);
                showError("Save failed: " + ex.getMessage());
              }
            });
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button cancel = new Button("Cancel", e -> dialog.close());
    dialog.getFooter().add(cancel, save);
    dialog.open();
  }

  private void toggleUserLock(XincoCoreUserServer user) {
    int newStatus = user.getStatusNumber() == 1 ? 2 : 1;
    try {
      user.setStatusNumber(newStatus);
      user.setChange(true);
      user.setChangerID(adminId());
      user.write2DB();
      refreshUsers();
      showSuccess(newStatus == 2 ? "User locked." : "User unlocked.");
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Lock/unlock failed", e);
      showError("Operation failed: " + e.getMessage());
    }
  }

  private void confirmDeleteUser(XincoCoreUserServer user) {
    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader("Delete User");
    confirm.setText("Delete user \"" + user.getUsername() + "\"? This cannot be undone.");
    confirm.setCancelable(true);
    confirm.setConfirmText("Delete");
    confirm.setConfirmButtonTheme("error primary");
    confirm.addConfirmListener(
        e -> {
          try {
            XincoCoreUserServer.deleteFromDB(user.getId());
            refreshUsers();
            showSuccess("User deleted.");
          } catch (Exception ex) {
            logger.log(Level.SEVERE, "Delete user failed", ex);
            showError("Cannot delete: " + ex.getMessage());
          }
        });
    confirm.open();
  }

  void openGroupDialog(XincoCoreGroupServer existing) {
    boolean isNew = existing == null;
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(isNew ? "New Group" : "Edit Group");

    TextField name = new TextField("Name");
    name.setRequired(true);

    Select<Integer> status = new Select<>();
    status.setLabel("Status");
    status.setItems(1, 2);
    status.setItemLabelGenerator(s -> s == 1 ? "Active" : "Inactive");

    if (!isNew) {
      name.setValue(nvl(existing.getDesignation()));
      status.setValue(existing.getStatusNumber());
    } else {
      status.setValue(1);
    }

    dialog.add(new FormLayout(name, status));

    Button save =
        new Button(
            "Save",
            e -> {
              if (name.isEmpty()) {
                name.setErrorMessage("Required");
                name.setInvalid(true);
                return;
              }
              try {
                XincoCoreGroupServer group =
                    isNew
                        ? new XincoCoreGroupServer(0, name.getValue().trim(), status.getValue())
                        : existing;
                if (!isNew) {
                  group.setDesignation(name.getValue().trim());
                  group.setStatusNumber(status.getValue());
                }
                group.setChangerID(adminId());
                group.write2DB();
                dialog.close();
                refreshGroups();
                showSuccess(isNew ? "Group created." : "Group updated.");
              } catch (Exception ex) {
                logger.log(Level.SEVERE, "Save group failed", ex);
                showError("Save failed: " + ex.getMessage());
              }
            });
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button cancel = new Button("Cancel", e -> dialog.close());
    dialog.getFooter().add(cancel, save);
    dialog.open();
  }

  private void confirmDeleteGroup(XincoCoreGroupServer group) {
    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader("Delete Group");
    confirm.setText("Delete group \"" + group.getDesignation() + "\"?");
    confirm.setCancelable(true);
    confirm.setConfirmText("Delete");
    confirm.setConfirmButtonTheme("error primary");
    confirm.addConfirmListener(
        e -> {
          int result = XincoCoreGroupServer.deleteFromDB(group);
          if (result == 0) {
            refreshGroups();
            showSuccess("Group deleted.");
          } else {
            showError("Cannot delete group.");
          }
        });
    confirm.open();
  }

  private void refreshUsers() {
    userGrid.setItems(loadUsers());
  }

  private void refreshGroups() {
    groupGrid.setItems(loadGroups());
  }

  @SuppressWarnings("unchecked")
  private List<XincoCoreUserServer> loadUsers() {
    try {
      ArrayList<XincoCoreUserServer> users = XincoCoreUserServer.getXincoCoreUsers();
      return users != null ? users : List.of();
    } catch (Throwable t) {
      return List.of();
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private List<XincoCoreGroupServer> loadGroups() {
    try {
      ArrayList raw = XincoCoreGroupServer.getXincoCoreGroups();
      if (raw == null) return List.of();
      List<XincoCoreGroupServer> groups = new ArrayList<>();
      for (Object obj : raw) {
        if (obj instanceof XincoCoreGroupServer g) groups.add(g);
      }
      return groups;
    } catch (Throwable t) {
      return List.of();
    }
  }

  private List<XincoCoreGroupServer> loadUserGroups(int userId) {
    try {
      return XincoCoreGroupServer.getGroupsOfUser(userId);
    } catch (Throwable t) {
      return List.of();
    }
  }

  private Set<XincoCoreGroupServer> matchByGroupId(
      List<XincoCoreGroupServer> userGroups, List<XincoCoreGroupServer> allGroups) {
    Set<Integer> ids = new HashSet<>();
    for (XincoCoreGroupServer g : userGroups) ids.add(g.getId());
    Set<XincoCoreGroupServer> matched = new HashSet<>();
    for (XincoCoreGroupServer g : allGroups) {
      if (ids.contains(g.getId())) matched.add(g);
    }
    return matched;
  }

  private void saveUserGroups(
      int userId, Set<XincoCoreGroupServer> selected, List<XincoCoreGroupServer> allGroups)
      throws Exception {
    List<Integer> groupIds = new ArrayList<>();
    for (XincoCoreGroupServer g : selected) groupIds.add(g.getId());
    XincoCoreUserServer.saveUserGroups(userId, groupIds);
  }

  private int adminId() {
    return session.isLoggedIn() ? session.getUser().getId() : 1;
  }

  private static String nvl(String s) {
    return s == null ? "" : s;
  }

  private void showSuccess(String msg) {
    Notification n = Notification.show(msg, 3000, Notification.Position.BOTTOM_END);
    n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  private void showError(String msg) {
    Notification n = Notification.show(msg, 5000, Notification.Position.BOTTOM_END);
    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
  }
}
