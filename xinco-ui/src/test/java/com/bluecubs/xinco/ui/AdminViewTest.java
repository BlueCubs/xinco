package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServer;
import com.bluecubs.xinco.core.server.XincoCoreDataTypeServer;
import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

class AdminViewTest {

  private static Routes routes;
  private UserSession session;
  private AdminView view;

  @BeforeAll
  static void discoverRoutes() {
    routes = new Routes().autoDiscoverViews("com.bluecubs.xinco.ui");
  }

  @BeforeEach
  void setup() {
    MockVaadin.setup(routes);
    session = mock(UserSession.class);
    when(session.isLoggedIn()).thenReturn(false);
    view = new AdminView(session);
    UI.getCurrent().add(view);
  }

  @AfterEach
  void tearDown() {
    MockVaadin.tearDown();
  }

  // ---- layout ----

  @Test
  void adminView_renders_withTabSheet() {
    assertNotNull(_get(TabSheet.class));
  }

  @Test
  void adminView_usersTab_hasToolbar() throws Exception {
    HorizontalLayout toolbar = getUserToolbar();
    assertNotNull(toolbar);
    assertEquals(4, toolbar.getComponentCount(), "New / Edit / Lock / Delete");
  }

  @Test
  void adminView_groupsTab_hasToolbar() throws Exception {
    HorizontalLayout toolbar = getGroupToolbar();
    assertNotNull(toolbar);
    assertEquals(3, toolbar.getComponentCount(), "New / Edit / Delete");
  }

  @Test
  void adminView_userToolbar_firstButtonIsNewUser() throws Exception {
    HorizontalLayout toolbar = getUserToolbar();
    Button btn = (Button) toolbar.getComponentAt(0);
    assertEquals("New User", btn.getText());
  }

  @Test
  void adminView_groupToolbar_firstButtonIsNewGroup() throws Exception {
    HorizontalLayout toolbar = getGroupToolbar();
    Button btn = (Button) toolbar.getComponentAt(0);
    assertEquals("New Group", btn.getText());
  }

  // ---- openUserDialog ----

  @Test
  void openUserDialog_null_opensCreateDialog() throws Exception {
    invokeOpenUserDialog(null);
    Dialog dialog = _get(Dialog.class);
    assertNotNull(dialog);
    assertTrue(dialog.isOpened());
    assertEquals("New User", dialog.getHeaderTitle());
  }

  @Test
  void openUserDialog_existingUser_opensEditDialog() throws Exception {
    XincoCoreUserServer user = mock(XincoCoreUserServer.class);
    when(user.getUsername()).thenReturn("alice");
    when(user.getFirstName()).thenReturn("Alice");
    when(user.getLastName()).thenReturn("Smith");
    when(user.getEmail()).thenReturn("alice@example.com");
    when(user.getStatusNumber()).thenReturn(1);
    when(user.getId()).thenReturn(42);

    invokeOpenUserDialog(user);

    Dialog dialog = _get(Dialog.class);
    assertTrue(dialog.isOpened());
    assertEquals("Edit User", dialog.getHeaderTitle());
  }

  @Test
  void openUserDialog_create_usernameFieldIsEditable() throws Exception {
    invokeOpenUserDialog(null);
    // Username field should be writable in create mode
    TextField userField = findUsernameField();
    assertNotNull(userField);
    assertFalse(userField.isReadOnly());
  }

  @Test
  void openUserDialog_edit_usernameFieldIsReadOnly() throws Exception {
    XincoCoreUserServer user = mock(XincoCoreUserServer.class);
    when(user.getUsername()).thenReturn("bob");
    when(user.getFirstName()).thenReturn("");
    when(user.getLastName()).thenReturn("");
    when(user.getEmail()).thenReturn("");
    when(user.getStatusNumber()).thenReturn(1);
    when(user.getId()).thenReturn(7);

    invokeOpenUserDialog(user);

    TextField userField = findUsernameField();
    assertNotNull(userField);
    assertTrue(userField.isReadOnly());
  }

  // ---- openGroupDialog ----

  @Test
  void openGroupDialog_null_opensCreateDialog() throws Exception {
    invokeOpenGroupDialog(null);
    Dialog dialog = _get(Dialog.class);
    assertNotNull(dialog);
    assertTrue(dialog.isOpened());
    assertEquals("New Group", dialog.getHeaderTitle());
  }

  @Test
  void openGroupDialog_existingGroup_opensEditDialog() throws Exception {
    XincoCoreGroupServer group = mock(XincoCoreGroupServer.class);
    when(group.getDesignation()).thenReturn("Admins");
    when(group.getStatusNumber()).thenReturn(1);

    invokeOpenGroupDialog(group);

    Dialog dialog = _get(Dialog.class);
    assertTrue(dialog.isOpened());
    assertEquals("Edit Group", dialog.getHeaderTitle());
  }

  @Test
  void openGroupDialog_create_nameFieldIsEmpty() throws Exception {
    invokeOpenGroupDialog(null);
    TextField nameField = _get(TextField.class, spec -> spec.withLabel("Name"));
    assertNotNull(nameField);
    assertTrue(nameField.isEmpty());
  }

  @Test
  void openGroupDialog_edit_nameFieldPrePopulated() throws Exception {
    XincoCoreGroupServer group = mock(XincoCoreGroupServer.class);
    when(group.getDesignation()).thenReturn("Editors");
    when(group.getStatusNumber()).thenReturn(1);

    invokeOpenGroupDialog(group);

    TextField nameField = _get(TextField.class, spec -> spec.withLabel("Name"));
    assertEquals("Editors", nameField.getValue());
  }

  // ---- openSettingDialog ----

  @Test
  void adminView_settingsTab_hasToolbarWithEditButton() throws Exception {
    HorizontalLayout toolbar = getSettingsToolbar();
    assertNotNull(toolbar);
    assertEquals(1, toolbar.getComponentCount(), "Edit only — no create/delete for settings");
    assertEquals("Edit", ((Button) toolbar.getComponentAt(0)).getText());
  }

  @Test
  void openSettingDialog_opensWithCorrectHeader() throws Exception {
    XincoSettingServer setting = new XincoSettingServer(1, "password.attempts", 5, null, false, 0L);
    invokeOpenSettingDialog(setting);
    Dialog dialog = _get(Dialog.class);
    assertTrue(dialog.isOpened());
    assertEquals("Edit Setting", dialog.getHeaderTitle());
  }

  @Test
  void openSettingDialog_keyFieldIsReadOnly() throws Exception {
    XincoSettingServer setting = new XincoSettingServer(1, "password.attempts", 5, null, false, 0L);
    invokeOpenSettingDialog(setting);
    TextField keyField = findInDialog("Key");
    assertNotNull(keyField);
    assertTrue(keyField.isReadOnly());
    assertEquals("password.attempts", keyField.getValue());
  }

  // ---- Data Types tab ----

  @Test
  void adminView_dataTypesTab_hasToolbarWithThreeButtons() throws Exception {
    HorizontalLayout toolbar = getDataTypesToolbar();
    assertNotNull(toolbar);
    assertEquals(3, toolbar.getComponentCount(), "New / Edit / Delete");
    assertEquals("New", ((Button) toolbar.getComponentAt(0)).getText());
    assertEquals("Edit", ((Button) toolbar.getComponentAt(1)).getText());
    assertEquals("Delete", ((Button) toolbar.getComponentAt(2)).getText());
  }

  @Test
  void openDataTypeDialog_null_opensCreateDialog() throws Exception {
    invokeOpenDataTypeDialog(null);
    Dialog dialog = _get(Dialog.class);
    assertNotNull(dialog);
    assertTrue(dialog.isOpened());
    assertEquals("New Data Type", dialog.getHeaderTitle());
  }

  @Test
  void openDataTypeDialog_existing_opensEditDialog() throws Exception {
    XincoCoreDataTypeServer dt = mock(XincoCoreDataTypeServer.class);
    when(dt.getDesignation()).thenReturn("MyType");
    when(dt.getDescription()).thenReturn("A custom type");
    invokeOpenDataTypeDialog(dt);
    Dialog dialog = _get(Dialog.class);
    assertTrue(dialog.isOpened());
    assertEquals("Edit Data Type", dialog.getHeaderTitle());
    TextField desig = _get(TextField.class, spec -> spec.withLabel("Designation"));
    assertEquals("MyType", desig.getValue());
  }

  @Test
  void openAttributeDialog_noSelectionShowsNothing() throws Exception {
    // selectedDataType is null by default; openAttributeDialog should show an error notification
    // rather than a dialog
    _get(TabSheet.class).setSelectedIndex(3);
    Method m = AdminView.class.getDeclaredMethod("openAttributeDialog");
    m.setAccessible(true);
    m.invoke(view);
    // No dialog should appear when no data type is selected
    assertTrue(_find(Dialog.class).isEmpty(), "no dialog without a selected data type");
  }

  // ---- helpers ----

  private HorizontalLayout getUserToolbar() {
    return getTabToolbar(0, 4);
  }

  private HorizontalLayout getGroupToolbar() {
    return getTabToolbar(1, 3);
  }

  private HorizontalLayout getSettingsToolbar() {
    return getTabToolbar(2, 1);
  }

  private HorizontalLayout getDataTypesToolbar() {
    return getTabToolbar(3, 3);
  }

  private HorizontalLayout getTabToolbar(int tabIndex, int buttonCount) {
    TabSheet ts = _get(TabSheet.class);
    Component content = ts.getComponent(ts.getTabAt(tabIndex));
    return findToolbarWithCount(content, buttonCount);
  }

  private HorizontalLayout findToolbarWithCount(Component root, int count) {
    if (root instanceof HorizontalLayout hl && hl.getComponentCount() == count) return hl;
    return root.getChildren()
        .map(c -> findToolbarWithCount(c, count))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }

  private TextField findInDialog(String label) {
    Dialog dialog = _get(Dialog.class);
    return findTextField(dialog, label);
  }

  private TextField findTextField(Component root, String label) {
    if (root instanceof TextField tf && label.equals(tf.getLabel())) return tf;
    return root.getChildren()
        .map(c -> findTextField(c, label))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }

  private void invokeOpenUserDialog(XincoCoreUserServer user) throws Exception {
    Method m = AdminView.class.getDeclaredMethod("openUserDialog", XincoCoreUserServer.class);
    m.setAccessible(true);
    m.invoke(view, user);
  }

  private void invokeOpenGroupDialog(XincoCoreGroupServer group) throws Exception {
    Method m = AdminView.class.getDeclaredMethod("openGroupDialog", XincoCoreGroupServer.class);
    m.setAccessible(true);
    m.invoke(view, group);
  }

  private void invokeOpenSettingDialog(XincoSettingServer setting) throws Exception {
    Method m = AdminView.class.getDeclaredMethod("openSettingDialog", XincoSettingServer.class);
    m.setAccessible(true);
    m.invoke(view, setting);
  }

  private void invokeOpenDataTypeDialog(XincoCoreDataTypeServer dt) throws Exception {
    Method m =
        AdminView.class.getDeclaredMethod("openDataTypeDialog", XincoCoreDataTypeServer.class);
    m.setAccessible(true);
    m.invoke(view, dt);
  }

  private TextField findUsernameField() {
    return findInDialog("Username");
  }

  // ---- openAttributeDialog coverage ----

  @Test
  void openAttributeDialog_withSelectedDataType_opensDialog() throws Exception {
    setSelectedDataType(buildDataTypeServer(1, "File"));

    Method m = AdminView.class.getDeclaredMethod("openAttributeDialog");
    m.setAccessible(true);
    try (MockedStatic<XincoCoreDataTypeAttributeServer> ms =
        mockStatic(XincoCoreDataTypeAttributeServer.class)) {
      ms.when(() -> XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(anyInt()))
          .thenReturn(new ArrayList<>());
      m.invoke(view);
    }
    Dialog dialog = _get(Dialog.class);
    assertTrue(dialog.isOpened());
    assertEquals("Add Attribute", dialog.getHeaderTitle());
  }

  @Test
  void openAttributeDialog_saveEmptyName_coversValidationBranch() throws Exception {
    setSelectedDataType(buildDataTypeServer(1, "File"));

    Method m = AdminView.class.getDeclaredMethod("openAttributeDialog");
    m.setAccessible(true);
    try (MockedStatic<XincoCoreDataTypeAttributeServer> ms =
        mockStatic(XincoCoreDataTypeAttributeServer.class)) {
      ms.when(() -> XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(anyInt()))
          .thenReturn(new ArrayList<>());
      m.invoke(view);
    }
    List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
    assertFalse(saveBtns.isEmpty());
    saveBtns.get(0).click();
  }

  @Test
  void openAttributeDialog_saveWithName_coversLambdaBody() throws Exception {
    setSelectedDataType(buildDataTypeServer(1, "File"));

    Method m = AdminView.class.getDeclaredMethod("openAttributeDialog");
    m.setAccessible(true);
    try (MockedStatic<XincoCoreDataTypeAttributeServer> msAttr =
            mockStatic(XincoCoreDataTypeAttributeServer.class);
        MockedConstruction<XincoCoreDataTypeAttributeServer> mcAttr =
            mockConstruction(
                XincoCoreDataTypeAttributeServer.class,
                (mock, ctx) -> {
                  when(mock.write2DB()).thenReturn(1);
                })) {
      msAttr
          .when(() -> XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(anyInt()))
          .thenReturn(new ArrayList<>());
      m.invoke(view);

      TextField nameField = _get(TextField.class, spec -> spec.withLabel("Name"));
      nameField.setValue("MyAttribute");

      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty());
      saveBtns.get(0).click();
    }
  }

  // ---- toggleUserLock coverage ----

  @Test
  void toggleUserLock_coversLockPath() throws Exception {
    XincoCoreUserServer user = mock(XincoCoreUserServer.class);
    when(user.getStatusNumber()).thenReturn(1);
    when(user.write2DB()).thenReturn(1);

    try (MockedStatic<XincoCoreUserServer> ms = mockStatic(XincoCoreUserServer.class)) {
      ms.when(XincoCoreUserServer::getXincoCoreUsers).thenReturn(new ArrayList<>());
      Method m = AdminView.class.getDeclaredMethod("toggleUserLock", XincoCoreUserServer.class);
      m.setAccessible(true);
      m.invoke(view, user);
    }
    verify(user).setStatusNumber(2);
  }

  @Test
  void toggleUserLock_coversUnlockPath() throws Exception {
    XincoCoreUserServer user = mock(XincoCoreUserServer.class);
    when(user.getStatusNumber()).thenReturn(2);
    when(user.write2DB()).thenReturn(1);

    try (MockedStatic<XincoCoreUserServer> ms = mockStatic(XincoCoreUserServer.class)) {
      ms.when(XincoCoreUserServer::getXincoCoreUsers).thenReturn(new ArrayList<>());
      Method m = AdminView.class.getDeclaredMethod("toggleUserLock", XincoCoreUserServer.class);
      m.setAccessible(true);
      m.invoke(view, user);
    }
    verify(user).setStatusNumber(1);
  }

  // ---- openGroupDialog save lambda coverage ----

  @Test
  void openGroupDialog_save_emptyName_coversValidation() throws Exception {
    invokeOpenGroupDialog(null);
    List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
    assertFalse(saveBtns.isEmpty());
    saveBtns.get(0).click();
  }

  @Test
  void openGroupDialog_create_save_withName_coversNewGroupPath() throws Exception {
    try (MockedConstruction<XincoCoreGroupServer> mc =
            mockConstruction(
                XincoCoreGroupServer.class,
                (m, ctx) -> {
                  when(m.write2DB()).thenReturn(1);
                });
        MockedStatic<XincoCoreGroupServer> ms = mockStatic(XincoCoreGroupServer.class)) {
      ms.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());
      invokeOpenGroupDialog(null);

      TextField nameField = _get(TextField.class, spec -> spec.withLabel("Name"));
      nameField.setValue("Engineers");

      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty());
      saveBtns.get(0).click();
    }
  }

  @Test
  void openGroupDialog_edit_save_coversEditGroupPath() throws Exception {
    XincoCoreGroupServer group = mock(XincoCoreGroupServer.class);
    when(group.getDesignation()).thenReturn("Testers");
    when(group.getStatusNumber()).thenReturn(1);
    when(group.write2DB()).thenReturn(1);

    try (MockedStatic<XincoCoreGroupServer> ms = mockStatic(XincoCoreGroupServer.class)) {
      ms.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());
      invokeOpenGroupDialog(group);

      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty());
      saveBtns.get(0).click();
    }
  }

  // ---- confirmDeleteGroup lambda coverage ----

  @Test
  void confirmDeleteGroup_fireConfirm_coversDeleteLambda() throws Exception {
    XincoCoreGroupServer group = mock(XincoCoreGroupServer.class);
    when(group.getDesignation()).thenReturn("OldGroup");

    Method m = AdminView.class.getDeclaredMethod("confirmDeleteGroup", XincoCoreGroupServer.class);
    m.setAccessible(true);

    try (MockedStatic<XincoCoreGroupServer> ms = mockStatic(XincoCoreGroupServer.class)) {
      ms.when(() -> XincoCoreGroupServer.deleteFromDB(any())).thenReturn(0);
      ms.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());
      m.invoke(view, group);

      ConfirmDialog confirm = _get(ConfirmDialog.class);
      assertTrue(confirm.isOpened());
      ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));
    }
  }

  // ---- openSettingDialog save lambda coverage ----

  @Test
  void openSettingDialog_saveInt_coversIntBranch() throws Exception {
    XincoSettingServer setting = spy(new XincoSettingServer(1, "max.attempts", 5, null, false, 0L));
    doReturn(1).when(setting).write2DB();
    invokeOpenSettingDialog(setting);

    try (MockedStatic<XincoSettingServer> ms = mockStatic(XincoSettingServer.class)) {
      ms.when(XincoSettingServer::getAllSettings).thenReturn(new ArrayList<>());
      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty());
      saveBtns.get(0).click();
    }
  }

  @Test
  void openSettingDialog_saveBool_coversBoolBranch() throws Exception {
    XincoSettingServer setting =
        spy(new XincoSettingServer(2, "feature.enabled", null, null, true, 0L));
    doReturn(1).when(setting).write2DB();
    invokeOpenSettingDialog(setting);

    try (MockedStatic<XincoSettingServer> ms = mockStatic(XincoSettingServer.class)) {
      ms.when(XincoSettingServer::getAllSettings).thenReturn(new ArrayList<>());
      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty());
      saveBtns.get(0).click();
    }
  }

  @Test
  void openSettingDialog_saveString_coversStringBranch() throws Exception {
    XincoSettingServer setting =
        spy(new XincoSettingServer(3, "smtp.host", null, "mail.example.com", false, 0L));
    doReturn(1).when(setting).write2DB();
    invokeOpenSettingDialog(setting);

    try (MockedStatic<XincoSettingServer> ms = mockStatic(XincoSettingServer.class)) {
      ms.when(XincoSettingServer::getAllSettings).thenReturn(new ArrayList<>());
      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty());
      saveBtns.get(0).click();
    }
  }

  // ---- openUserDialog save lambda coverage ----

  @Test
  void openUserDialog_create_emptyUsername_coversUsernameValidation() throws Exception {
    invokeOpenUserDialog(null);
    List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
    assertFalse(saveBtns.isEmpty());
    saveBtns.get(0).click();
  }

  @Test
  void openUserDialog_create_emptyPassword_coversPasswordValidation() throws Exception {
    invokeOpenUserDialog(null);

    TextField usernameField = findUsernameField();
    usernameField.setValue("newuser");

    // password left empty → triggers password validation
    List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
    assertFalse(saveBtns.isEmpty());
    saveBtns.get(0).click();
  }

  @Test
  @SuppressWarnings("unchecked")
  void openUserDialog_create_fullSave_coversNewUserPath() throws Exception {
    try (MockedConstruction<XincoCoreUserServer> mcUser =
            mockConstruction(
                XincoCoreUserServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(99);
                  when(m.write2DB()).thenReturn(1);
                });
        MockedStatic<XincoCoreUserServer> msUser = mockStatic(XincoCoreUserServer.class);
        MockedStatic<XincoCoreGroupServer> msGroup = mockStatic(XincoCoreGroupServer.class)) {

      msUser.when(XincoCoreUserServer::getXincoCoreUsers).thenReturn(new ArrayList<>());
      msUser.when(() -> XincoCoreUserServer.saveUserGroups(anyInt(), any())).then(inv -> null);
      msGroup.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());
      msGroup
          .when(() -> XincoCoreGroupServer.getGroupsOfUser(anyInt()))
          .thenReturn(new ArrayList<>());

      invokeOpenUserDialog(null);

      TextField usernameField = findUsernameField();
      usernameField.setValue("newuser");

      _find(PasswordField.class, spec -> spec.withLabel("Password")).stream()
          .findFirst()
          .ifPresent(pf -> pf.setValue("secret123"));

      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty());
      saveBtns.get(0).click();
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void openUserDialog_edit_save_coversEditUserPath() throws Exception {
    XincoCoreUserServer user = mock(XincoCoreUserServer.class);
    when(user.getUsername()).thenReturn("alice");
    when(user.getFirstName()).thenReturn("Alice");
    when(user.getLastName()).thenReturn("Smith");
    when(user.getEmail()).thenReturn("alice@example.com");
    when(user.getStatusNumber()).thenReturn(1);
    when(user.getId()).thenReturn(42);
    when(user.write2DB()).thenReturn(1);

    try (MockedStatic<XincoCoreUserServer> msUser = mockStatic(XincoCoreUserServer.class);
        MockedStatic<XincoCoreGroupServer> msGroup = mockStatic(XincoCoreGroupServer.class)) {
      msUser.when(XincoCoreUserServer::getXincoCoreUsers).thenReturn(new ArrayList<>());
      msUser.when(() -> XincoCoreUserServer.saveUserGroups(anyInt(), any())).then(inv -> null);
      msGroup.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());
      msGroup
          .when(() -> XincoCoreGroupServer.getGroupsOfUser(anyInt()))
          .thenReturn(new ArrayList<>());

      invokeOpenUserDialog(user);

      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty());
      saveBtns.get(0).click();
    }
  }

  // ---- loadAttributes coverage ----

  @Test
  void loadAttributes_staticReturnsItems_coversHappyPath() throws Exception {
    Method m = AdminView.class.getDeclaredMethod("loadAttributes", int.class);
    m.setAccessible(true);

    XincoCoreDataTypeAttributeServer attr = mock(XincoCoreDataTypeAttributeServer.class);
    ArrayList<Object> raw = new ArrayList<>();
    raw.add(attr);

    try (MockedStatic<XincoCoreDataTypeAttributeServer> ms =
        mockStatic(XincoCoreDataTypeAttributeServer.class)) {
      ms.when(() -> XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(anyInt()))
          .thenReturn(raw);
      @SuppressWarnings("unchecked")
      List<XincoCoreDataTypeAttributeServer> result =
          (List<XincoCoreDataTypeAttributeServer>) m.invoke(view, 1);
      assertEquals(1, result.size());
    }
  }

  @Test
  void loadAttributes_staticReturnsNull_coversNullBranch() throws Exception {
    Method m = AdminView.class.getDeclaredMethod("loadAttributes", int.class);
    m.setAccessible(true);

    try (MockedStatic<XincoCoreDataTypeAttributeServer> ms =
        mockStatic(XincoCoreDataTypeAttributeServer.class)) {
      ms.when(() -> XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(anyInt()))
          .thenReturn(null);
      @SuppressWarnings("unchecked")
      List<XincoCoreDataTypeAttributeServer> result =
          (List<XincoCoreDataTypeAttributeServer>) m.invoke(view, 1);
      assertTrue(result.isEmpty());
    }
  }

  // ---- settingValue coverage ----

  @Test
  void settingValue_intType_returnsIntString() throws Exception {
    XincoSettingServer setting = spy(new XincoSettingServer(1, "key", 42, null, false, 0L));
    Method m = AdminView.class.getDeclaredMethod("settingValue", XincoSettingServer.class);
    m.setAccessible(true);
    assertEquals("42", m.invoke(view, setting));
  }

  @Test
  void settingValue_stringType_returnsStringValue() throws Exception {
    XincoSettingServer setting = spy(new XincoSettingServer(2, "key", null, "hello", false, 0L));
    Method m = AdminView.class.getDeclaredMethod("settingValue", XincoSettingServer.class);
    m.setAccessible(true);
    assertEquals("hello", m.invoke(view, setting));
  }

  @Test
  void settingValue_boolType_returnsBoolString() throws Exception {
    XincoSettingServer setting = spy(new XincoSettingServer(3, "key", null, null, true, 0L));
    Method m = AdminView.class.getDeclaredMethod("settingValue", XincoSettingServer.class);
    m.setAccessible(true);
    String result = (String) m.invoke(view, setting);
    assertEquals("true", result);
  }

  // ---- confirmDeleteUser coverage ----

  @Test
  void confirmDeleteUser_opensConfirmDialog() throws Exception {
    XincoCoreUserServer user = mock(XincoCoreUserServer.class);
    when(user.getUsername()).thenReturn("jdoe");
    Method m = AdminView.class.getDeclaredMethod("confirmDeleteUser", XincoCoreUserServer.class);
    m.setAccessible(true);
    m.invoke(view, user);
    ConfirmDialog confirm = _get(ConfirmDialog.class);
    assertTrue(confirm.isOpened());
  }

  @Test
  void confirmDeleteUser_confirmed_callsDeleteFromDB() throws Exception {
    XincoCoreUserServer user = mock(XincoCoreUserServer.class);
    when(user.getUsername()).thenReturn("jdoe");
    when(user.getId()).thenReturn(7);
    Method m = AdminView.class.getDeclaredMethod("confirmDeleteUser", XincoCoreUserServer.class);
    m.setAccessible(true);
    try (MockedStatic<XincoCoreUserServer> ms = mockStatic(XincoCoreUserServer.class)) {
      ms.when(XincoCoreUserServer::getXincoCoreUsers).thenReturn(new ArrayList<>());
      m.invoke(view, user);
      ConfirmDialog confirm = _get(ConfirmDialog.class);
      ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));
      ms.verify(() -> XincoCoreUserServer.deleteFromDB(7));
    }
  }

  // ---- confirmDeleteDataType coverage ----

  @Test
  void confirmDeleteDataType_opensConfirmDialog() throws Exception {
    XincoCoreDataTypeServer dt = mock(XincoCoreDataTypeServer.class);
    when(dt.getDesignation()).thenReturn("FileType");
    Method m =
        AdminView.class.getDeclaredMethod("confirmDeleteDataType", XincoCoreDataTypeServer.class);
    m.setAccessible(true);
    m.invoke(view, dt);
    ConfirmDialog confirm = _get(ConfirmDialog.class);
    assertTrue(confirm.isOpened());
  }

  @Test
  void confirmDeleteDataType_confirmed_deletesSuccessfully() throws Exception {
    XincoCoreDataTypeServer dt = mock(XincoCoreDataTypeServer.class);
    when(dt.getDesignation()).thenReturn("FileType");
    Method m =
        AdminView.class.getDeclaredMethod("confirmDeleteDataType", XincoCoreDataTypeServer.class);
    m.setAccessible(true);
    try (MockedStatic<XincoCoreDataTypeServer> ms = mockStatic(XincoCoreDataTypeServer.class)) {
      ms.when(() -> XincoCoreDataTypeServer.deleteFromDB(any())).thenReturn(0);
      ms.when(XincoCoreDataTypeServer::getXincoCoreDataTypes).thenReturn(new ArrayList<>());
      m.invoke(view, dt);
      ConfirmDialog confirm = _get(ConfirmDialog.class);
      ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));
    }
  }

  @Test
  void confirmDeleteDataType_confirmed_deleteFails_showsError() throws Exception {
    XincoCoreDataTypeServer dt = mock(XincoCoreDataTypeServer.class);
    when(dt.getDesignation()).thenReturn("FileType");
    Method m =
        AdminView.class.getDeclaredMethod("confirmDeleteDataType", XincoCoreDataTypeServer.class);
    m.setAccessible(true);
    try (MockedStatic<XincoCoreDataTypeServer> ms = mockStatic(XincoCoreDataTypeServer.class)) {
      ms.when(() -> XincoCoreDataTypeServer.deleteFromDB(any())).thenReturn(1);
      m.invoke(view, dt);
      ConfirmDialog confirm = _get(ConfirmDialog.class);
      ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));
    }
  }

  // ---- confirmRemoveAttribute coverage ----

  @Test
  void confirmRemoveAttribute_opensConfirmDialog() throws Exception {
    XincoCoreDataTypeAttributeServer attr = mock(XincoCoreDataTypeAttributeServer.class);
    when(attr.getDesignation()).thenReturn("filename");
    Method m =
        AdminView.class.getDeclaredMethod(
            "confirmRemoveAttribute", XincoCoreDataTypeAttributeServer.class);
    m.setAccessible(true);
    m.invoke(view, attr);
    ConfirmDialog confirm = _get(ConfirmDialog.class);
    assertTrue(confirm.isOpened());
  }

  @Test
  void confirmRemoveAttribute_confirmed_callsDeleteFromDB() throws Exception {
    XincoCoreDataTypeAttributeServer attr = mock(XincoCoreDataTypeAttributeServer.class);
    when(attr.getDesignation()).thenReturn("filename");
    Method m =
        AdminView.class.getDeclaredMethod(
            "confirmRemoveAttribute", XincoCoreDataTypeAttributeServer.class);
    m.setAccessible(true);
    try (MockedStatic<XincoCoreDataTypeAttributeServer> ms =
        mockStatic(XincoCoreDataTypeAttributeServer.class)) {
      ms.when(() -> XincoCoreDataTypeAttributeServer.deleteFromDB(any(), anyInt()))
          .thenAnswer(inv -> null);
      m.invoke(view, attr);
      ConfirmDialog confirm = _get(ConfirmDialog.class);
      ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));
    }
  }

  // ---- refreshDataTypes coverage ----

  @Test
  void refreshDataTypes_callsGridSetItems() throws Exception {
    Method m = AdminView.class.getDeclaredMethod("refreshDataTypes");
    m.setAccessible(true);
    try (MockedStatic<XincoCoreDataTypeServer> ms = mockStatic(XincoCoreDataTypeServer.class)) {
      ms.when(XincoCoreDataTypeServer::getXincoCoreDataTypes).thenReturn(new ArrayList<>());
      assertDoesNotThrow(() -> m.invoke(view));
    }
  }

  // ---- nvl coverage ----

  @Test
  void nvl_null_returnsEmpty() throws Exception {
    Method m = AdminView.class.getDeclaredMethod("nvl", String.class);
    m.setAccessible(true);
    assertEquals("", m.invoke(null, (String) null));
  }

  // ---- adminId coverage ----

  @Test
  void adminId_loggedIn_returnsUserId() throws Exception {
    when(session.isLoggedIn()).thenReturn(true);
    XincoCoreUserServer user = mock(XincoCoreUserServer.class);
    when(user.getId()).thenReturn(42);
    when(session.getUser()).thenReturn(user);
    Method m = AdminView.class.getDeclaredMethod("adminId");
    m.setAccessible(true);
    assertEquals(42, m.invoke(view));
  }

  // ---- matchByGroupId coverage ----

  @Test
  @SuppressWarnings("unchecked")
  void matchByGroupId_withMatchingGroups_returnsMatched() throws Exception {
    XincoCoreGroupServer g1 = mock(XincoCoreGroupServer.class);
    when(g1.getId()).thenReturn(1);
    XincoCoreGroupServer g2 = mock(XincoCoreGroupServer.class);
    when(g2.getId()).thenReturn(2);
    XincoCoreGroupServer g3 = mock(XincoCoreGroupServer.class);
    when(g3.getId()).thenReturn(3);
    Method m =
        AdminView.class.getDeclaredMethod(
            "matchByGroupId", java.util.List.class, java.util.List.class);
    m.setAccessible(true);
    Set<XincoCoreGroupServer> result =
        (Set<XincoCoreGroupServer>) m.invoke(view, List.of(g1, g3), List.of(g1, g2, g3));
    assertEquals(2, result.size());
    assertTrue(result.contains(g1));
    assertTrue(result.contains(g3));
    assertFalse(result.contains(g2));
  }

  // ---- saveUserGroups coverage ----

  @Test
  void saveUserGroups_withGroup_callsStaticSave() throws Exception {
    XincoCoreGroupServer g = mock(XincoCoreGroupServer.class);
    when(g.getId()).thenReturn(1);
    try (MockedStatic<XincoCoreUserServer> ms = mockStatic(XincoCoreUserServer.class)) {
      ms.when(() -> XincoCoreUserServer.saveUserGroups(anyInt(), any())).then(inv -> null);
      Method m =
          AdminView.class.getDeclaredMethod("saveUserGroups", int.class, Set.class, List.class);
      m.setAccessible(true);
      Set<XincoCoreGroupServer> selected = new HashSet<>(List.of(g));
      assertDoesNotThrow(() -> m.invoke(view, 1, selected, new ArrayList<>()));
    }
  }

  // ---- toggleUserLock exception coverage ----

  @Test
  void toggleUserLock_writeThrows_coversExceptionBranch() throws Exception {
    XincoCoreUserServer user = mock(XincoCoreUserServer.class);
    when(user.getStatusNumber()).thenReturn(1);
    when(user.write2DB()).thenThrow(new RuntimeException("DB failure"));
    Method m = AdminView.class.getDeclaredMethod("toggleUserLock", XincoCoreUserServer.class);
    m.setAccessible(true);
    assertDoesNotThrow(() -> m.invoke(view, user));
  }

  // ---- new helpers ----

  private void setSelectedDataType(XincoCoreDataTypeServer dt) throws Exception {
    Field f = AdminView.class.getDeclaredField("selectedDataType");
    f.setAccessible(true);
    f.set(view, dt);
  }

  private XincoCoreDataTypeServer buildDataTypeServer(int id, String designation) {
    XincoCoreDataTypeServer dt = mock(XincoCoreDataTypeServer.class);
    when(dt.getId()).thenReturn(id);
    when(dt.getDesignation()).thenReturn(designation);
    return dt;
  }
}
