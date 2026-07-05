package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bluecubs.xinco.core.server.XincoCoreDataTypeServer;
import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import java.lang.reflect.Method;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    return findToolbarWithCount(view, 4);
  }

  private HorizontalLayout getGroupToolbar() {
    // Groups tab is lazy-rendered; select it first so its content appears in the tree.
    _get(TabSheet.class).setSelectedIndex(1);
    return findToolbarWithCount(view, 3);
  }

  private HorizontalLayout getSettingsToolbar() {
    _get(TabSheet.class).setSelectedIndex(2);
    return findToolbarWithCount(view, 1);
  }

  private HorizontalLayout getDataTypesToolbar() {
    _get(TabSheet.class).setSelectedIndex(3);
    return findToolbarWithCount(view, 3);
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
}
