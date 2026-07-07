package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServer;
import com.bluecubs.xinco.core.server.XincoCoreDataTypeServer;
import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.component.textfield.IntegerField;
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
  private Grid<XincoSettingServer> settingGrid;
  private Grid<XincoCoreDataTypeServer> dataTypeGrid;
  private Grid<XincoCoreDataTypeAttributeServer> attrGrid;
  private XincoCoreDataTypeServer selectedDataType;

  public AdminView(UserSession session) {
    this.session = session;
    setSizeFull();
    setPadding(false);

    TabSheet tabs = new TabSheet();
    tabs.setSizeFull();
    tabs.add(getTranslation("general.user"), buildUsersTab());
    tabs.add(getTranslation("general.group"), buildGroupsTab());
    tabs.add(getTranslation("menu.preferences"), buildSettingsTab());
    tabs.add(getTranslation("general.datatype"), buildDataTypesTab());

    add(tabs);
  }

  private VerticalLayout buildUsersTab() {
    userGrid = new Grid<>();
    userGrid.setSizeFull();
    userGrid
        .addColumn(XincoCoreUserServer::getId)
        .setHeader(getTranslation("general.id"))
        .setWidth("60px")
        .setFlexGrow(0);
    userGrid
        .addColumn(XincoCoreUserServer::getUsername)
        .setHeader(getTranslation("general.username"));
    userGrid
        .addColumn(u -> u.getFirstName() + " " + u.getLastName())
        .setHeader(getTranslation("general.name"));
    userGrid.addColumn(XincoCoreUserServer::getEmail).setHeader(getTranslation("general.email"));
    userGrid
        .addColumn(
            u ->
                u.getStatusNumber() == 1
                    ? getTranslation("general.status.open")
                    : getTranslation("general.status.locked"))
        .setHeader(getTranslation("general.status"));
    userGrid.setItems(loadUsers());

    Button btnNew =
        new Button(getTranslation("message.admin.adminuser.add"), e -> openUserDialog(null));
    Button btnEdit =
        new Button(
            getTranslation("general.edit"),
            e -> userGrid.asSingleSelect().getOptionalValue().ifPresent(this::openUserDialog));
    Button btnLock =
        new Button(
            getTranslation("general.lock"),
            e -> userGrid.asSingleSelect().getOptionalValue().ifPresent(this::toggleUserLock));
    Button btnDelete =
        new Button(
            getTranslation("general.delete"),
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
        .setHeader(getTranslation("general.id"))
        .setWidth("60px")
        .setFlexGrow(0);
    groupGrid
        .addColumn(XincoCoreGroupServer::getDesignation)
        .setHeader(getTranslation("general.name"));
    groupGrid.setItems(loadGroups());

    Button btnNew =
        new Button(
            getTranslation("general.add") + " " + getTranslation("general.group"),
            e -> openGroupDialog(null));
    Button btnEdit =
        new Button(
            getTranslation("general.edit"),
            e -> groupGrid.asSingleSelect().getOptionalValue().ifPresent(this::openGroupDialog));
    Button btnDelete =
        new Button(
            getTranslation("general.delete"),
            e -> groupGrid.asSingleSelect().getOptionalValue().ifPresent(this::confirmDeleteGroup));
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
    dialog.setHeaderTitle(
        isNew
            ? getTranslation("message.admin.adminuser.add")
            : getTranslation("general.edit") + " " + getTranslation("general.user"));
    dialog.setWidth("500px");

    TextField username = new TextField(getTranslation("general.username"));
    username.setRequired(true);
    username.setReadOnly(!isNew);

    PasswordField password = new PasswordField(getTranslation("general.password"));
    password.setRequired(isNew);

    TextField firstName = new TextField(getTranslation("general.firstname"));
    TextField lastName = new TextField(getTranslation("general.lastname"));
    TextField email = new TextField(getTranslation("general.email"));

    Select<Integer> status = new Select<>();
    status.setLabel(getTranslation("general.status"));
    status.setItems(1, 2);
    status.setItemLabelGenerator(
        s ->
            s == 1
                ? getTranslation("general.status.open")
                : getTranslation("general.status.locked"));

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
      Set<XincoCoreGroupServer> selected =
          matchByGroupId(loadUserGroups(existing.getId()), allGroups);
      groupBox.setValue(selected);
    } else {
      status.setValue(1);
    }

    FormLayout form = new FormLayout(username, password, firstName, lastName, email, status);
    form.setColspan(username, 2);
    form.setColspan(password, 2);
    form.setColspan(email, 2);

    VerticalLayout content =
        new VerticalLayout(form, new Span(getTranslation("general.group") + ":"), groupBox);
    content.setPadding(false);
    dialog.add(content);

    Button save =
        new Button(
            getTranslation("general.save"),
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
    Button cancel = new Button(getTranslation("general.cancel"), e -> dialog.close());
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
    confirm.setHeader(getTranslation("general.delete") + " " + getTranslation("general.user"));
    confirm.setText("Delete user \"" + user.getUsername() + "\"? This cannot be undone.");
    confirm.setCancelable(true);
    confirm.setConfirmText(getTranslation("general.delete"));
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
    dialog.setHeaderTitle(
        isNew
            ? getTranslation("general.add") + " " + getTranslation("general.group")
            : getTranslation("general.edit") + " " + getTranslation("general.group"));

    TextField name = new TextField(getTranslation("general.name"));
    name.setRequired(true);

    Select<Integer> status = new Select<>();
    status.setLabel(getTranslation("general.status"));
    status.setItems(1, 2);
    status.setItemLabelGenerator(
        s ->
            s == 1
                ? getTranslation("general.status.open")
                : getTranslation("general.status.locked"));

    if (!isNew) {
      name.setValue(nvl(existing.getDesignation()));
      status.setValue(existing.getStatusNumber());
    } else {
      status.setValue(1);
    }

    dialog.add(new FormLayout(name, status));

    Button save =
        new Button(
            getTranslation("general.save"),
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
    Button cancel = new Button(getTranslation("general.cancel"), e -> dialog.close());
    dialog.getFooter().add(cancel, save);
    dialog.open();
  }

  private void confirmDeleteGroup(XincoCoreGroupServer group) {
    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader(getTranslation("general.delete") + " " + getTranslation("general.group"));
    confirm.setText("Delete group \"" + group.getDesignation() + "\"?");
    confirm.setCancelable(true);
    confirm.setConfirmText(getTranslation("general.delete"));
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

  private VerticalLayout buildSettingsTab() {
    settingGrid = new Grid<>();
    settingGrid.setSizeFull();
    settingGrid
        .addColumn(XincoSettingServer::getDescription)
        .setHeader(getTranslation("general.keyword"))
        .setFlexGrow(2);
    settingGrid
        .addColumn(this::settingType)
        .setHeader(getTranslation("general.type"))
        .setWidth("80px")
        .setFlexGrow(0);
    settingGrid
        .addColumn(this::settingValue)
        .setHeader(getTranslation("general.details"))
        .setFlexGrow(1);
    settingGrid.setItems(loadSettings());

    Button btnEdit =
        new Button(
            getTranslation("general.edit"),
            e ->
                settingGrid.asSingleSelect().getOptionalValue().ifPresent(this::openSettingDialog));
    HorizontalLayout toolbar = new HorizontalLayout(btnEdit);
    toolbar.setPadding(true);

    VerticalLayout layout = new VerticalLayout(toolbar, settingGrid);
    layout.setSizeFull();
    layout.setFlexGrow(1, settingGrid);
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  void openSettingDialog(XincoSettingServer setting) {
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(
        getTranslation("general.edit") + " " + getTranslation("menu.preferences"));
    dialog.setWidth("400px");

    TextField keyField = new TextField(getTranslation("general.keyword"));
    keyField.setValue(nvl(setting.getDescription()));
    keyField.setReadOnly(true);

    String type = settingType(setting);
    FormLayout form = new FormLayout(keyField);

    Checkbox boolField = new Checkbox(getTranslation("general.details"));
    IntegerField intField = new IntegerField(getTranslation("general.details"));
    TextField strField = new TextField(getTranslation("general.details"));

    if (XincoSettingServer.TYPE_BOOL.equals(type)) {
      boolField.setValue(setting.isBoolValue());
      form.add(boolField);
    } else if (XincoSettingServer.TYPE_INT.equals(type)) {
      intField.setValue(setting.getIntValue());
      form.add(intField);
    } else {
      strField.setValue(nvl(setting.getStringValue()));
      form.add(strField);
    }

    dialog.add(form);

    Button save =
        new Button(
            getTranslation("general.save"),
            e -> {
              try {
                if (XincoSettingServer.TYPE_BOOL.equals(type)) {
                  setting.setBoolValue(boolField.getValue());
                } else if (XincoSettingServer.TYPE_INT.equals(type)) {
                  Integer val = intField.getValue();
                  if (val != null) setting.setIntValue(val);
                } else {
                  setting.setStringValue(strField.getValue().trim());
                }
                setting.write2DB();
                dialog.close();
                refreshSettings();
                showSuccess("Setting saved.");
              } catch (Exception ex) {
                logger.log(Level.SEVERE, "Save setting failed", ex);
                showError("Save failed: " + ex.getMessage());
              }
            });
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button cancel = new Button(getTranslation("general.cancel"), e -> dialog.close());
    dialog.getFooter().add(cancel, save);
    dialog.open();
  }

  private String settingType(XincoSettingServer s) {
    return s.getSettingType();
  }

  private String settingValue(XincoSettingServer s) {
    return switch (s.getSettingType()) {
      case XincoSettingServer.TYPE_INT -> String.valueOf(s.getIntValue());
      case XincoSettingServer.TYPE_STRING -> s.getStringValue() != null ? s.getStringValue() : "";
      default -> String.valueOf(s.isBoolValue());
    };
  }

  private void refreshSettings() {
    settingGrid.setItems(loadSettings());
  }

  private List<XincoSettingServer> loadSettings() {
    try {
      return XincoSettingServer.getAllSettings();
    } catch (Throwable t) {
      return List.of();
    }
  }

  private VerticalLayout buildDataTypesTab() {
    dataTypeGrid = new Grid<>();
    dataTypeGrid.setSizeFull();
    dataTypeGrid
        .addColumn(XincoCoreDataTypeServer::getId)
        .setHeader(getTranslation("general.id"))
        .setWidth("60px")
        .setFlexGrow(0);
    dataTypeGrid
        .addColumn(XincoCoreDataTypeServer::getDesignation)
        .setHeader(getTranslation("general.designation"));
    dataTypeGrid
        .addColumn(XincoCoreDataTypeServer::getDescription)
        .setHeader(getTranslation("general.description"));
    dataTypeGrid.setItems(loadDataTypes());
    dataTypeGrid.addSelectionListener(
        e -> {
          selectedDataType = e.getFirstSelectedItem().orElse(null);
          if (selectedDataType != null) {
            attrGrid.setItems(loadAttributes(selectedDataType.getId()));
          } else {
            attrGrid.setItems(List.of());
          }
        });

    Button btnNew = new Button(getTranslation("general.create"), e -> openDataTypeDialog(null));
    Button btnEdit =
        new Button(
            getTranslation("general.edit"),
            e ->
                dataTypeGrid
                    .asSingleSelect()
                    .getOptionalValue()
                    .ifPresent(this::openDataTypeDialog));
    Button btnDelete =
        new Button(
            getTranslation("general.delete"),
            e ->
                dataTypeGrid
                    .asSingleSelect()
                    .getOptionalValue()
                    .ifPresent(this::confirmDeleteDataType));
    btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    HorizontalLayout dtToolbar = new HorizontalLayout(btnNew, btnEdit, btnDelete);
    dtToolbar.setPadding(true);

    attrGrid = new Grid<>();
    attrGrid.setSizeFull();
    attrGrid
        .addColumn(XincoCoreDataTypeAttributeServer::getAttributeId)
        .setHeader("#")
        .setWidth("50px")
        .setFlexGrow(0);
    attrGrid
        .addColumn(XincoCoreDataTypeAttributeServer::getDesignation)
        .setHeader(getTranslation("general.name"));
    attrGrid
        .addColumn(XincoCoreDataTypeAttributeServer::getDataType)
        .setHeader(getTranslation("general.datatype"))
        .setWidth("120px")
        .setFlexGrow(0);
    attrGrid
        .addColumn(XincoCoreDataTypeAttributeServer::getSize)
        .setHeader(getTranslation("general.size"))
        .setWidth("80px")
        .setFlexGrow(0);

    Button btnAddAttr =
        new Button(
            getTranslation("general.add") + " " + getTranslation("general.attribute"),
            e -> openAttributeDialog());
    Button btnRemoveAttr =
        new Button(
            getTranslation("general.delete"),
            e ->
                attrGrid
                    .asSingleSelect()
                    .getOptionalValue()
                    .ifPresent(this::confirmRemoveAttribute));
    btnRemoveAttr.addThemeVariants(ButtonVariant.LUMO_ERROR);
    HorizontalLayout attrToolbar = new HorizontalLayout(btnAddAttr, btnRemoveAttr);
    attrToolbar.setPadding(true);

    VerticalLayout layout =
        new VerticalLayout(
            dtToolbar,
            dataTypeGrid,
            new Span(getTranslation("general.attribute")),
            attrToolbar,
            attrGrid);
    layout.setSizeFull();
    layout.setFlexGrow(2, dataTypeGrid);
    layout.setFlexGrow(1, attrGrid);
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  void openDataTypeDialog(XincoCoreDataTypeServer existing) {
    boolean isNew = existing == null;
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(
        isNew
            ? getTranslation("general.create") + " " + getTranslation("general.datatype")
            : getTranslation("general.edit") + " " + getTranslation("general.datatype"));
    dialog.setWidth("420px");

    TextField designation = new TextField(getTranslation("general.designation"));
    designation.setRequired(true);
    TextField description = new TextField(getTranslation("general.description"));

    if (!isNew) {
      designation.setValue(nvl(existing.getDesignation()));
      description.setValue(nvl(existing.getDescription()));
    }

    dialog.add(new FormLayout(designation, description));

    Button save =
        new Button(
            getTranslation("general.save"),
            e -> {
              if (designation.isEmpty()) {
                designation.setErrorMessage("Required");
                designation.setInvalid(true);
                return;
              }
              try {
                XincoCoreDataTypeServer dt =
                    isNew
                        ? new XincoCoreDataTypeServer(
                            0,
                            designation.getValue().trim(),
                            description.getValue().trim(),
                            new ArrayList<>())
                        : existing;
                if (!isNew) {
                  dt.setDesignation(designation.getValue().trim());
                  dt.setDescription(description.getValue().trim());
                }
                dt.setChangerID(adminId());
                dt.write2DB();
                dialog.close();
                refreshDataTypes();
                showSuccess(isNew ? "Data type created." : "Data type updated.");
              } catch (Exception ex) {
                logger.log(Level.SEVERE, "Save data type failed", ex);
                showError("Save failed: " + ex.getMessage());
              }
            });
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button cancel = new Button(getTranslation("general.cancel"), e -> dialog.close());
    dialog.getFooter().add(cancel, save);
    dialog.open();
  }

  private void confirmDeleteDataType(XincoCoreDataTypeServer dt) {
    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader(getTranslation("general.delete") + " " + getTranslation("general.datatype"));
    confirm.setText(
        "Delete data type \""
            + dt.getDesignation()
            + "\"? This will fail if any data items use this type.");
    confirm.setCancelable(true);
    confirm.setConfirmText(getTranslation("general.delete"));
    confirm.setConfirmButtonTheme("error primary");
    confirm.addConfirmListener(
        e -> {
          int result = XincoCoreDataTypeServer.deleteFromDB(dt);
          if (result == 0) {
            selectedDataType = null;
            attrGrid.setItems(List.of());
            refreshDataTypes();
            showSuccess("Data type deleted.");
          } else {
            showError("Cannot delete: data items may exist for this type.");
          }
        });
    confirm.open();
  }

  void openAttributeDialog() {
    if (selectedDataType == null) {
      showError("Select a data type first.");
      return;
    }
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(
        getTranslation("general.add") + " " + getTranslation("general.attribute"));
    dialog.setWidth("400px");

    TextField name = new TextField(getTranslation("general.name"));
    name.setRequired(true);

    Select<String> dataType = new Select<>();
    dataType.setLabel(getTranslation("general.datatype"));
    dataType.setItems("varchar", "int", "unsigned int", "double", "text", "date");
    dataType.setValue("varchar");

    IntegerField size = new IntegerField(getTranslation("general.size"));
    size.setValue(255);
    size.setMin(0);

    dialog.add(new FormLayout(name, dataType, size));

    Button save =
        new Button(
            getTranslation("general.save"),
            e -> {
              if (name.isEmpty()) {
                name.setErrorMessage("Required");
                name.setInvalid(true);
                return;
              }
              try {
                List<XincoCoreDataTypeAttributeServer> existing =
                    loadAttributes(selectedDataType.getId());
                int nextId =
                    existing.stream()
                            .mapToInt(XincoCoreDataTypeAttributeServer::getAttributeId)
                            .max()
                            .orElse(0)
                        + 1;
                XincoCoreDataTypeAttributeServer attr =
                    new XincoCoreDataTypeAttributeServer(
                        selectedDataType.getId(),
                        nextId,
                        name.getValue().trim(),
                        dataType.getValue(),
                        size.getValue() != null ? size.getValue() : 0);
                attr.setChangerID(adminId());
                attr.write2DB();
                dialog.close();
                refreshAttributes();
                showSuccess("Attribute added.");
              } catch (Exception ex) {
                logger.log(Level.SEVERE, "Add attribute failed", ex);
                showError("Save failed: " + ex.getMessage());
              }
            });
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button cancel = new Button(getTranslation("general.cancel"), e -> dialog.close());
    dialog.getFooter().add(cancel, save);
    dialog.open();
  }

  private void confirmRemoveAttribute(XincoCoreDataTypeAttributeServer attr) {
    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader(getTranslation("general.delete") + " " + getTranslation("general.attribute"));
    confirm.setText(
        "Remove attribute \""
            + attr.getDesignation()
            + "\"? All stored values for this attribute will also be deleted.");
    confirm.setCancelable(true);
    confirm.setConfirmText(getTranslation("general.delete"));
    confirm.setConfirmButtonTheme("error primary");
    confirm.addConfirmListener(
        e -> {
          try {
            XincoCoreDataTypeAttributeServer.deleteFromDB(attr, adminId());
            refreshAttributes();
            showSuccess("Attribute removed.");
          } catch (Exception ex) {
            logger.log(Level.SEVERE, "Remove attribute failed", ex);
            showError("Cannot remove: " + ex.getMessage());
          }
        });
    confirm.open();
  }

  private void refreshDataTypes() {
    dataTypeGrid.setItems(loadDataTypes());
  }

  private void refreshAttributes() {
    if (selectedDataType != null) {
      attrGrid.setItems(loadAttributes(selectedDataType.getId()));
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private List<XincoCoreDataTypeServer> loadDataTypes() {
    try {
      ArrayList raw = XincoCoreDataTypeServer.getXincoCoreDataTypes();
      if (raw == null) return List.of();
      List<XincoCoreDataTypeServer> types = new ArrayList<>();
      for (Object obj : raw) {
        if (obj instanceof XincoCoreDataTypeServer s) types.add(s);
      }
      return types;
    } catch (Throwable t) {
      return List.of();
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private List<XincoCoreDataTypeAttributeServer> loadAttributes(int dataTypeId) {
    try {
      ArrayList raw = XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(dataTypeId);
      if (raw == null) return List.of();
      List<XincoCoreDataTypeAttributeServer> attrs = new ArrayList<>();
      for (Object obj : raw) {
        if (obj instanceof XincoCoreDataTypeAttributeServer a) attrs.add(a);
      }
      return attrs;
    } catch (Throwable t) {
      return List.of();
    }
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
        if (obj instanceof XincoCoreGroupServer) groups.add((XincoCoreGroupServer) obj);
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
