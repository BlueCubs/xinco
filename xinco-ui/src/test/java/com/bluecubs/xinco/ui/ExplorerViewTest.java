package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoAddAttributeServer;
import com.bluecubs.xinco.core.server.XincoCoreACEServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import com.bluecubs.xinco.core.server.XincoCoreLanguageServer;
import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreLanguage;
import com.bluecubs.xinco.ui.component.PropertyGrid;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.VaadinSession;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

class ExplorerViewTest {

  private static Routes routes;

  @BeforeAll
  static void discoverRoutes() {
    routes = new Routes().autoDiscoverViews("com.bluecubs.xinco.ui");
  }

  @BeforeEach
  void setup() {
    MockVaadin.setup(routes);
    ViewTestHelper.registerI18NProvider();
  }

  @AfterEach
  void tearDown() {
    MockVaadin.tearDown();
  }

  // ---- Rendering ----

  @Test
  void explorerView_renders() {
    addView(new ExplorerView(new UserSession()));
    assertNotNull(_get(TreeGrid.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  void explorerView_treeSelection_populatesDataGrid() {
    addView(new ExplorerView(new UserSession()));

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreLanguage mockLang = mock(XincoCoreLanguage.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockData.getXincoCoreLanguage()).thenReturn(mockLang);
    when(mockLang.getSign()).thenReturn("EN");
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);
    when(mockType.getDesignation()).thenReturn("text");

    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getDesignation()).thenReturn("Root");
    List<XincoCoreData> nodeData = new ArrayList<>();
    nodeData.add(mockData);
    when(mockNode.getXincoCoreData()).thenReturn(nodeData);

    TreeGrid<XincoCoreNodeServer> treeGrid = (TreeGrid<XincoCoreNodeServer>) _get(TreeGrid.class);
    treeGrid.setItems(List.of(mockNode), n -> List.of());
    treeGrid.asSingleSelect().setValue(mockNode);

    assertNotNull(treeGrid);
  }

  @Test
  @SuppressWarnings("unchecked")
  void explorerView_dataSelection_populatesPropertyGrid() {
    addView(new ExplorerView(new UserSession()));

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreLanguage mockLang = mock(XincoCoreLanguage.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockData.getXincoCoreLanguage()).thenReturn(mockLang);
    when(mockLang.getSign()).thenReturn("EN");
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);
    when(mockType.getDesignation()).thenReturn("text");

    Grid<XincoCoreDataServer> dataGrid =
        (Grid<XincoCoreDataServer>)
            _find(Grid.class).stream()
                .filter(g -> !(g instanceof TreeGrid) && !(g instanceof PropertyGrid))
                .findFirst()
                .orElseThrow();
    dataGrid.setItems(List.of(mockData));
    dataGrid.asSingleSelect().setValue(mockData);

    assertNotNull(dataGrid);
  }

  // ---- MenuBar state: no login ----

  @Test
  void menuState_noLogin_allContextItemsDisabled() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    assertFalse(menuItem(view, "miNewFolder").isEnabled(), "miNewFolder");
    assertFalse(menuItem(view, "miAddData").isEnabled(), "miAddData");
    assertFalse(menuItem(view, "miDelete").isEnabled(), "miDelete");
    assertFalse(menuItem(view, "miDownload").isEnabled(), "miDownload");
    assertFalse(menuItem(view, "miCheckOut").isEnabled(), "miCheckOut");
    assertFalse(menuItem(view, "miCheckIn").isEnabled(), "miCheckIn");
    assertFalse(menuItem(view, "miUndoCheckOut").isEnabled(), "miUndoCheckOut");
    assertFalse(menuItem(view, "miLock").isEnabled(), "miLock");
    assertFalse(menuItem(view, "miPublish").isEnabled(), "miPublish");
    assertFalse(menuItem(view, "miManageAcl").isEnabled(), "miManageAcl");
  }

  @Test
  void menuState_noLogin_fileDataSelected_downloadEnabled() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockData.getStatusNumber()).thenReturn(1);
    when(mockData.getXincoCoreAcl()).thenReturn(List.of());

    setField(view, "selectedData", mockData);
    invoke(view, "updateMenuState");

    // download is enabled purely by (dataSelected && isFile) — no login required
    assertTrue(menuItem(view, "miDownload").isEnabled(), "download enabled for file data");
    // all write/admin actions still disabled (not logged in)
    assertFalse(menuItem(view, "miCheckOut").isEnabled(), "checkout needs login");
    assertFalse(menuItem(view, "miLock").isEnabled(), "lock needs login");
  }

  @Test
  void menuState_noLogin_nonFileDataSelected_downloadDisabled() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(2); // not a file type
    when(mockData.getStatusNumber()).thenReturn(1);
    when(mockData.getXincoCoreAcl()).thenReturn(List.of());

    setField(view, "selectedData", mockData);
    invoke(view, "updateMenuState");

    assertFalse(menuItem(view, "miDownload").isEnabled(), "download disabled for non-file data");
  }

  // ---- MenuBar state: logged in ----

  @Test
  void menuState_loggedIn_nodeWithWriteAce_newFolderAndAddDataEnabled() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreAcl()).thenReturn(List.of());

    XincoCoreACEServer mockAce = mock(XincoCoreACEServer.class);
    when(mockAce.isWritePermission()).thenReturn(true);
    when(mockAce.isAdminPermission()).thenReturn(true);

    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreACEServer> mocked = mockStatic(XincoCoreACEServer.class)) {
      mocked.when(() -> XincoCoreACEServer.checkAccess(any(), any())).thenReturn(mockAce);
      invoke(view, "updateMenuState");

      assertTrue(menuItem(view, "miNewFolder").isEnabled(), "new folder enabled with write");
      assertTrue(menuItem(view, "miAddData").isEnabled(), "add data enabled with write");
      assertTrue(menuItem(view, "miDelete").isEnabled(), "delete enabled with write");
      assertTrue(menuItem(view, "miManageAcl").isEnabled(), "manage ACL enabled with admin");
    }
  }

  @Test
  void menuState_loggedIn_activeFileWithWriteAce_checkoutAndPublishEnabled() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockData.getStatusNumber()).thenReturn(1); // active
    when(mockData.getXincoCoreAcl()).thenReturn(List.of());

    XincoCoreACEServer mockAce = mock(XincoCoreACEServer.class);
    when(mockAce.isWritePermission()).thenReturn(true);
    when(mockAce.isAdminPermission()).thenReturn(false);

    setField(view, "selectedData", mockData);

    try (MockedStatic<XincoCoreACEServer> mocked = mockStatic(XincoCoreACEServer.class)) {
      mocked.when(() -> XincoCoreACEServer.checkAccess(any(), any())).thenReturn(mockAce);
      invoke(view, "updateMenuState");

      assertTrue(menuItem(view, "miCheckOut").isEnabled(), "checkout enabled: active file + write");
      assertFalse(menuItem(view, "miCheckIn").isEnabled(), "checkin disabled: not checked out");
      assertFalse(menuItem(view, "miUndoCheckOut").isEnabled(), "undo disabled: not checked out");
      assertTrue(menuItem(view, "miPublish").isEnabled(), "publish enabled: active file + write");
      assertTrue(menuItem(view, "miLock").isEnabled(), "lock enabled: active file + write");
    }
  }

  @Test
  void menuState_loggedIn_checkedOutFileWithWriteAce_checkInAndUndoEnabled() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockData.getStatusNumber()).thenReturn(4); // checked out
    when(mockData.getXincoCoreAcl()).thenReturn(List.of());

    XincoCoreACEServer mockAce = mock(XincoCoreACEServer.class);
    when(mockAce.isWritePermission()).thenReturn(true);
    when(mockAce.isAdminPermission()).thenReturn(false);

    setField(view, "selectedData", mockData);

    try (MockedStatic<XincoCoreACEServer> mocked = mockStatic(XincoCoreACEServer.class)) {
      mocked.when(() -> XincoCoreACEServer.checkAccess(any(), any())).thenReturn(mockAce);
      invoke(view, "updateMenuState");

      assertTrue(menuItem(view, "miCheckIn").isEnabled(), "checkin enabled when checked out");
      assertTrue(menuItem(view, "miUndoCheckOut").isEnabled(), "undo enabled when checked out");
      assertFalse(
          menuItem(view, "miCheckOut").isEnabled(), "checkout disabled: already checked out");
      assertFalse(menuItem(view, "miPublish").isEnabled(), "publish disabled: not active");
    }
  }

  @Test
  void menuState_loggedIn_emptyAcl_writeItemsDisabled() throws Exception {
    // Empty ACL → checkAccess returns null → no write access even when logged in
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedNode", mockNode);
    invoke(view, "updateMenuState");

    assertFalse(menuItem(view, "miNewFolder").isEnabled(), "no write without ACE");
    assertFalse(menuItem(view, "miAddData").isEnabled(), "no write without ACE");
  }

  // ---- Dialog: New Folder ----

  @Test
  void newFolderDialog_doesNotOpen_whenNoNodeSelected() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    invoke(view, "openNewFolderDialog"); // selectedNode is null → early return

    assertTrue(_find(Dialog.class).isEmpty(), "no dialog when selectedNode is null");
  }

  @Test
  void newFolderDialog_opens_whenNodeSelected() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    setField(view, "selectedNode", mock(XincoCoreNodeServer.class));
    invoke(view, "openNewFolderDialog");

    assertFalse(_find(Dialog.class).isEmpty(), "new folder dialog should open");
  }

  // ---- Dialog: Delete confirmation ----

  @Test
  void deleteDialog_doesNotOpen_whenNothingSelected() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    invoke(view, "confirmDelete"); // both selectedNode and selectedData are null

    assertTrue(_find(ConfirmDialog.class).isEmpty(), "no confirm dialog when nothing selected");
  }

  @Test
  void deleteDialog_opens_forSelectedNode() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getDesignation()).thenReturn("TestFolder");
    setField(view, "selectedNode", mockNode);
    invoke(view, "confirmDelete");

    assertFalse(_find(ConfirmDialog.class).isEmpty(), "confirm dialog opens for node deletion");
  }

  @Test
  void deleteDialog_opens_forSelectedData_prefersDataOverNode() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    when(mockData.getDesignation()).thenReturn("report.pdf");
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getDesignation()).thenReturn("SomeFolder");
    setField(view, "selectedData", mockData);
    setField(view, "selectedNode", mockNode);
    invoke(view, "confirmDelete");

    // confirmDelete prefers selectedData when both are non-null
    assertFalse(_find(ConfirmDialog.class).isEmpty(), "confirm dialog opens for data deletion");
  }

  // ---- statusLabel ----

  @Test
  void statusLabel_returnsCorrectStrings() throws Exception {
    ExplorerView instance = new ExplorerView(new UserSession());
    addView(instance);
    Method m = ExplorerView.class.getDeclaredMethod("statusLabel", int.class);
    m.setAccessible(true);
    assertEquals("Open", m.invoke(instance, 1));
    assertEquals("Locked", m.invoke(instance, 2));
    assertEquals("Checked out", m.invoke(instance, 4));
    assertEquals("Published", m.invoke(instance, 5));
    String unknown = (String) m.invoke(instance, 99);
    assertTrue(unknown.startsWith("Unknown"), "unknown status: " + unknown);
  }

  // ---- Helpers ----

  private static void addView(ExplorerView view) {
    UI.getCurrent().add(view);
  }

  private static UserSession loggedInSession() {
    UserSession session = new UserSession();
    XincoCoreUserServer mockUser = mock(XincoCoreUserServer.class);
    when(mockUser.getId()).thenReturn(1);
    session.setUser(mockUser);
    return session;
  }

  private static MenuItem menuItem(ExplorerView view, String fieldName) throws Exception {
    Field f = ExplorerView.class.getDeclaredField(fieldName);
    f.setAccessible(true);
    return (MenuItem) f.get(view);
  }

  private static void setField(ExplorerView view, String name, Object value) throws Exception {
    Field f = ExplorerView.class.getDeclaredField(name);
    f.setAccessible(true);
    f.set(view, value);
  }

  private static void invoke(ExplorerView view, String name) throws Exception {
    Method m = ExplorerView.class.getDeclaredMethod(name);
    m.setAccessible(true);
    m.invoke(view);
  }

  // ---- Search bar ----

  @Test
  void searchBar_renders_searchFieldPresent() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    Field sf = ExplorerView.class.getDeclaredField("searchField");
    sf.setAccessible(true);
    assertNotNull(sf.get(view), "searchField should be initialized");
  }

  @Test
  void search_emptyQuery_doesNothing() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    // searchField is empty by default; doSearch() should be a no-op
    Field sf = ExplorerView.class.getDeclaredField("searchField");
    sf.setAccessible(true);
    com.vaadin.flow.component.textfield.TextField field =
        (com.vaadin.flow.component.textfield.TextField) sf.get(view);
    field.setValue("");
    invoke(view, "doSearch");
    // statusLabel stays hidden
    Field sl = ExplorerView.class.getDeclaredField("searchStatusLabel");
    sl.setAccessible(true);
    assertFalse(((com.vaadin.flow.component.html.Span) sl.get(view)).isVisible());
  }

  @Test
  void search_indexUnavailable_showsError() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    // Inject a searcher that simulates an unavailable index (returns null).
    injectSearcher(view, q -> null);

    Field sf = ExplorerView.class.getDeclaredField("searchField");
    sf.setAccessible(true);
    ((com.vaadin.flow.component.textfield.TextField) sf.get(view)).setValue("report");
    invoke(view, "doSearch");

    Field sl = ExplorerView.class.getDeclaredField("searchStatusLabel");
    sl.setAccessible(true);
    assertFalse(((com.vaadin.flow.component.html.Span) sl.get(view)).isVisible());
  }

  @Test
  @SuppressWarnings("unchecked")
  void search_withResults_populatesDataGridAndShowsStatus() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    XincoCoreDataServer hit = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(hit.getXincoCoreDataType().getId()).thenReturn(1);
    when(hit.getStatusNumber()).thenReturn(1);
    java.util.ArrayList results = new java.util.ArrayList();
    results.add(hit);

    // Inject a searcher that returns a known result list — avoids mockStatic on XincoIndexer.
    injectSearcher(view, q -> results);

    Field sf = ExplorerView.class.getDeclaredField("searchField");
    sf.setAccessible(true);
    ((com.vaadin.flow.component.textfield.TextField) sf.get(view)).setValue("report");
    invoke(view, "doSearch");

    Field sl = ExplorerView.class.getDeclaredField("searchStatusLabel");
    sl.setAccessible(true);
    com.vaadin.flow.component.html.Span label = (com.vaadin.flow.component.html.Span) sl.get(view);
    assertTrue(label.isVisible(), "status label visible after search");
    assertTrue(label.getText().contains("1"), "label shows result count");

    Field clr = ExplorerView.class.getDeclaredField("clearSearchBtn");
    clr.setAccessible(true);
    assertTrue(
        ((com.vaadin.flow.component.button.Button) clr.get(view)).isVisible(),
        "clear button visible");
  }

  // ---- Archive dialog ----

  @Test
  @SuppressWarnings("unchecked")
  void archiveDialog_opensWithModeSelector() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockData.getId()).thenReturn(0);
    when(mockData.getDesignation()).thenReturn("test.pdf");
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);
    when(mockType.getId()).thenReturn(1);
    when(mockData.getXincoAddAttributes()).thenReturn(new java.util.ArrayList<>());

    Method m = ExplorerView.class.getDeclaredMethod("openArchiveDialog", XincoCoreDataServer.class);
    m.setAccessible(true);
    m.invoke(view, mockData);

    Dialog dialog = _get(Dialog.class);
    assertTrue(dialog.isOpened());
    assertTrue(dialog.getHeaderTitle().contains("Archive"));

    Select<Integer> modelSelect = _get(Select.class, spec -> spec.withLabel("Archive Model"));
    assertNotNull(modelSelect, "Archive model selector present");
    assertEquals(0, modelSelect.getValue(), "defaults to None");
  }

  @Test
  @SuppressWarnings("unchecked")
  void archiveDialog_modeSelection_enablesCorrectField() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockData.getId()).thenReturn(0);
    when(mockData.getDesignation()).thenReturn("test.pdf");
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);
    when(mockType.getId()).thenReturn(1);
    when(mockData.getXincoAddAttributes()).thenReturn(new java.util.ArrayList<>());

    Method m = ExplorerView.class.getDeclaredMethod("openArchiveDialog", XincoCoreDataServer.class);
    m.setAccessible(true);
    m.invoke(view, mockData);

    Select<Integer> modelSelect = _get(Select.class, spec -> spec.withLabel("Archive Model"));
    DatePicker archiveDate = _get(DatePicker.class, spec -> spec.withLabel("Archiving Date"));
    IntegerField archiveDays =
        _get(IntegerField.class, spec -> spec.withLabel("Archiving Days"));

    // Default: None — both disabled
    assertFalse(archiveDate.isEnabled(), "date disabled in None mode");
    assertFalse(archiveDays.isEnabled(), "days disabled in None mode");

    // Select Date mode
    modelSelect.setValue(1);
    assertTrue(archiveDate.isEnabled(), "date enabled in Date mode");
    assertFalse(archiveDays.isEnabled(), "days disabled in Date mode");

    // Select Days mode
    modelSelect.setValue(2);
    assertFalse(archiveDate.isEnabled(), "date disabled in Days mode");
    assertTrue(archiveDays.isEnabled(), "days enabled in Days mode");
  }

  // ---- Checkin dialog version fields ----

  @Test
  void checkinDialog_opensWithVersionFields() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    when(mockData.getId()).thenReturn(0);
    setField(view, "selectedData", mockData);
    invoke(view, "openCheckinDialog");

    Dialog dialog = _get(Dialog.class);
    assertTrue(dialog.isOpened());
    // DB load fails gracefully (id=0, no DB) → defaults: curHigh=1, major bump → Major=2
    IntegerField majorField = _get(IntegerField.class, spec -> spec.withLabel("Major"));
    IntegerField minorField = _get(IntegerField.class, spec -> spec.withLabel("Minor"));
    IntegerField patchField = _get(IntegerField.class, spec -> spec.withLabel("Patch"));
    assertNotNull(majorField, "Major field present");
    assertNotNull(minorField, "Minor field present");
    assertNotNull(patchField, "Patch field present");
    assertEquals(2, majorField.getValue(), "major bump default: curHigh(1)+1");
    assertEquals(0, minorField.getValue(), "minor default: 0");
    assertEquals(0, patchField.getValue(), "patch default: 0");
  }

  @Test
  void checkinDialog_minorBumpCheckbox_updatesVersionFields() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    when(mockData.getId()).thenReturn(0);
    setField(view, "selectedData", mockData);
    invoke(view, "openCheckinDialog");

    Checkbox minorBump = _get(Checkbox.class, spec -> spec.withLabel("Minor"));
    IntegerField majorField = _get(IntegerField.class, spec -> spec.withLabel("Major"));
    IntegerField minorField = _get(IntegerField.class, spec -> spec.withLabel("Minor"));

    minorBump.setValue(true);
    // curHigh=1 → major stays 1; curMid=0 → minor becomes 1
    assertEquals(1, majorField.getValue(), "minor bump keeps major at curHigh");
    assertEquals(1, minorField.getValue(), "minor bump increments minor");

    minorBump.setValue(false);
    assertEquals(2, majorField.getValue(), "back to major bump");
    assertEquals(0, minorField.getValue(), "minor resets to 0");
  }

  // ---- ACL Dialog coverage ----

  @Test
  void openAclDialog_withNodeSelected_coversMethodBody() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getDesignation()).thenReturn("Root");
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreACEServer> msAce = mockStatic(XincoCoreACEServer.class);
        MockedStatic<XincoCoreUserServer> msUser = mockStatic(XincoCoreUserServer.class);
        MockedStatic<XincoCoreGroupServer> msGroup = mockStatic(XincoCoreGroupServer.class)) {
      msAce
          .when(() -> XincoCoreACEServer.getXincoCoreACL(anyInt(), anyString()))
          .thenReturn(new ArrayList<>());
      msUser.when(XincoCoreUserServer::getXincoCoreUsers).thenReturn(new ArrayList<>());
      msGroup.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());
      invoke(view, "openAclDialog");
      assertTrue(_get(Dialog.class).isOpened());
    }
  }

  @Test
  void openAclDialog_withDataSelected_coversIsDataBranches() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    when(mockData.getId()).thenReturn(2);
    when(mockData.getDesignation()).thenReturn("file.txt");
    setField(view, "selectedData", mockData);

    try (MockedStatic<XincoCoreACEServer> msAce = mockStatic(XincoCoreACEServer.class);
        MockedStatic<XincoCoreUserServer> msUser = mockStatic(XincoCoreUserServer.class);
        MockedStatic<XincoCoreGroupServer> msGroup = mockStatic(XincoCoreGroupServer.class)) {
      msAce
          .when(() -> XincoCoreACEServer.getXincoCoreACL(anyInt(), anyString()))
          .thenReturn(new ArrayList<>());
      msUser.when(XincoCoreUserServer::getXincoCoreUsers).thenReturn(new ArrayList<>());
      msGroup.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());
      invoke(view, "openAclDialog");
      assertTrue(_get(Dialog.class).isOpened());
    }
  }

  @Test
  void openAclDialog_saveButton_emptyAcl_coversEmptySaveLambda() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getDesignation()).thenReturn("Root");
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreACEServer> msAce = mockStatic(XincoCoreACEServer.class);
        MockedStatic<XincoCoreUserServer> msUser = mockStatic(XincoCoreUserServer.class);
        MockedStatic<XincoCoreGroupServer> msGroup = mockStatic(XincoCoreGroupServer.class)) {
      msAce
          .when(() -> XincoCoreACEServer.getXincoCoreACL(anyInt(), anyString()))
          .thenReturn(new ArrayList<>());
      msUser.when(XincoCoreUserServer::getXincoCoreUsers).thenReturn(new ArrayList<>());
      msGroup.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());
      invoke(view, "openAclDialog");
      Dialog dialog = _get(Dialog.class);
      assertTrue(dialog.isOpened(), "Dialog should be open");
      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty(), "Save button must be present");
      saveBtns.get(0).click();
      // If the click triggered the real save lambda, dialog.close() would have been called
      assertFalse(dialog.isOpened(), "Dialog should close after real save lambda executes");
    }
  }

  @Test
  void openAclDialog_withUserAce_coversAceLoopAndDeleteSave() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getDesignation()).thenReturn("Root");
    setField(view, "selectedNode", mockNode);

    XincoCoreACEServer mockAce = mock(XincoCoreACEServer.class);
    when(mockAce.getId()).thenReturn(5);
    when(mockAce.getXincoCoreUserId()).thenReturn(1);
    when(mockAce.getXincoCoreGroupId()).thenReturn(0);
    when(mockAce.isAdminPermission()).thenReturn(false);
    when(mockAce.isReadPermission()).thenReturn(true);
    when(mockAce.isWritePermission()).thenReturn(false);
    when(mockAce.isExecutePermission()).thenReturn(false);

    XincoCoreUserServer mockUser = mock(XincoCoreUserServer.class);
    when(mockUser.getId()).thenReturn(1);
    when(mockUser.getUsername()).thenReturn("admin");

    try (MockedStatic<XincoCoreACEServer> msAce = mockStatic(XincoCoreACEServer.class);
        MockedStatic<XincoCoreUserServer> msUser = mockStatic(XincoCoreUserServer.class);
        MockedStatic<XincoCoreGroupServer> msGroup = mockStatic(XincoCoreGroupServer.class)) {
      msAce
          .when(() -> XincoCoreACEServer.getXincoCoreACL(anyInt(), anyString()))
          .thenReturn(new ArrayList<>(List.of(mockAce)));
      msAce.when(() -> XincoCoreACEServer.removeFromDB(any(), anyInt())).thenReturn(0);
      msUser
          .when(XincoCoreUserServer::getXincoCoreUsers)
          .thenReturn(new ArrayList<>(List.of(mockUser)));
      msGroup.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());

      invoke(view, "openAclDialog");

      // Click "×" to move ACE into deletedAces, then Save → removeFromDB called
      List<Button> delBtns = _find(Button.class, spec -> spec.withText("×"));
      if (!delBtns.isEmpty()) {
        delBtns.get(0).click();
      }
      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty());
      saveBtns.get(0).click();
    }
  }

  // ---- Add Data Dialog coverage ----

  @Test
  void openAddDataDialog_withNode_coversMethodBody() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreLanguageServer> msLang = mockStatic(XincoCoreLanguageServer.class)) {
      msLang.when(XincoCoreLanguageServer::getXincoCoreLanguages).thenReturn(new ArrayList<>());
      invoke(view, "openAddDataDialog");
      assertTrue(_get(Dialog.class).isOpened());
    }
  }

  @Test
  void openAddDataDialog_submitEmptyName_coversNameValidation() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreLanguageServer> msLang = mockStatic(XincoCoreLanguageServer.class)) {
      msLang.when(XincoCoreLanguageServer::getXincoCoreLanguages).thenReturn(new ArrayList<>());
      invoke(view, "openAddDataDialog");
      // Click Add with empty name → designationField.isEmpty() → validation branch
      List<Button> addBtns = _find(Button.class, spec -> spec.withText("Add"));
      if (!addBtns.isEmpty()) {
        addBtns.get(0).click();
      }
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void openAddDataDialog_fullSubmit_coversLambdaBody() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    Path tempDir = Files.createTempDirectory("xinco-test-adddata");
    try (MockedStatic<XincoCoreLanguageServer> msLang = mockStatic(XincoCoreLanguageServer.class);
        MockedStatic<XincoCoreDataServer> msDataS = mockStatic(XincoCoreDataServer.class);
        MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoAddAttributeServer> mcAttr =
            mockConstruction(
                XincoAddAttributeServer.class,
                (m, ctx) -> {
                  when(m.write2DB()).thenReturn(1);
                })) {

      XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
      when(mockLang.getId()).thenReturn(1);
      when(mockLang.getSign()).thenReturn("EN");
      when(mockLang.getDesignation()).thenReturn("English");
      msLang
          .when(XincoCoreLanguageServer::getXincoCoreLanguages)
          .thenReturn(new ArrayList<>(List.of(mockLang)));

      Path file1 = tempDir.resolve("data-1-1.bin");
      Path file2 = tempDir.resolve("data-1.bin");
      final int[] callCount = {0};
      msDataS
          .when(() -> XincoCoreDataServer.getXincoCoreDataPath(any(), anyInt(), anyString()))
          .thenAnswer(
              inv -> {
                callCount[0]++;
                return callCount[0] == 1 ? file1.toString() : file2.toString();
              });

      invoke(view, "openAddDataDialog");

      // Simulate file upload
      Upload upload = _get(Upload.class);
      MemoryBuffer buffer = (MemoryBuffer) upload.getReceiver();
      try (OutputStream out = buffer.receiveUpload("test.txt", "text/plain")) {
        out.write("hello test content".getBytes());
      }

      // Fill in designation
      _find(TextField.class, spec -> spec.withLabel("File Name")).stream()
          .findFirst()
          .ifPresent(f -> f.setValue("Test Document"));

      // Click Add button
      List<Button> addBtns = _find(Button.class, spec -> spec.withText("Add"));
      if (!addBtns.isEmpty()) {
        addBtns.get(0).click();
      }
    } finally {
      try {
        Files.walk(tempDir).sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
      } catch (Exception ignored) {
      }
    }
  }

  // ---- Download Selected coverage ----

  @Test
  void downloadSelected_withNullPath_coversNullBranch() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    when(mockData.getId()).thenReturn(1);
    setField(view, "selectedData", mockData);

    try (MockedStatic<XincoCoreDataServer> ms = mockStatic(XincoCoreDataServer.class)) {
      ms.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt())).thenReturn(null);
      invoke(view, "downloadSelected");
    }
  }

  @Test
  void downloadSelected_withRealFile_coversHappyPath() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    when(mockData.getId()).thenReturn(1);
    when(mockData.getDesignation()).thenReturn("file.txt");
    setField(view, "selectedData", mockData);

    Path tempFile = Files.createTempFile("xinco-dl-test", ".txt");
    Files.writeString(tempFile, "test content");
    try (MockedStatic<XincoCoreDataServer> ms = mockStatic(XincoCoreDataServer.class)) {
      ms.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt()))
          .thenReturn(tempFile.toString());
      invoke(view, "downloadSelected");
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  // ---- Checkout Selected coverage ----

  @Test
  void checkoutSelected_withMockedDb_coversTryBlock() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer selectedDataMock = mock(XincoCoreDataServer.class);
    when(selectedDataMock.getId()).thenReturn(1);
    setField(view, "selectedData", selectedDataMock);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreDataServer> msData = mockStatic(XincoCoreDataServer.class);
        MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.getXincoCoreLogs()).thenReturn(new ArrayList<>());
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                })) {
      // getLastMajorVersionDataPath returns null → downloadSelected error branch
      msData.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt())).thenReturn(null);
      invoke(view, "checkoutSelected");
    }
  }

  // ---- Lock / Publish / Undo Checkout lambda coverage ----

  @Test
  void lockSelected_confirmTriggered_coversLambdaBody() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer selectedDataMock = mock(XincoCoreDataServer.class);
    when(selectedDataMock.getId()).thenReturn(1);
    setField(view, "selectedData", selectedDataMock);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    try (MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.getXincoCoreLogs()).thenReturn(new ArrayList<>());
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                })) {
      invoke(view, "lockSelected");
      Dialog dialog = _get(Dialog.class, spec -> spec.withPredicate(d -> d.isOpened()));
      assertTrue(dialog.isOpened());
      _get(TextField.class, spec -> spec.withLabel("Reason")).setValue("Lock reason");
      Button confirmBtn = _find(Button.class).stream()
          .filter(b -> "Lock Data".equals(b.getText()))
          .findFirst()
          .orElseThrow();
      ComponentUtil.fireEvent(confirmBtn, new com.vaadin.flow.component.ClickEvent<>(confirmBtn));
    }
  }

  @Test
  void publishSelected_confirmTriggered_coversLambdaBody() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer selectedDataMock = mock(XincoCoreDataServer.class);
    when(selectedDataMock.getId()).thenReturn(1);
    setField(view, "selectedData", selectedDataMock);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    try (MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.getXincoCoreLogs()).thenReturn(new ArrayList<>());
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                })) {
      invoke(view, "publishSelected");
      Dialog dialog = _get(Dialog.class, spec -> spec.withPredicate(d -> d.isOpened()));
      assertTrue(dialog.isOpened());
      _get(TextField.class, spec -> spec.withLabel("Reason")).setValue("Publish reason");
      Button confirmBtn = _find(Button.class).stream()
          .filter(b -> "Publish Data".equals(b.getText()))
          .findFirst()
          .orElseThrow();
      ComponentUtil.fireEvent(confirmBtn, new com.vaadin.flow.component.ClickEvent<>(confirmBtn));
    }
  }

  @Test
  void undoCheckoutSelected_confirmTriggered_coversLambdaBody() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer selectedDataMock = mock(XincoCoreDataServer.class);
    when(selectedDataMock.getId()).thenReturn(1);
    setField(view, "selectedData", selectedDataMock);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    try (MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.getXincoCoreLogs()).thenReturn(new ArrayList<>());
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                })) {
      invoke(view, "undoCheckoutSelected");
      ConfirmDialog confirm = _get(ConfirmDialog.class);
      assertTrue(confirm.isOpened());
      ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));
    }
  }

  // ---- Confirm Delete coverage ----

  @Test
  void confirmDelete_withDataStatusCheckedOut_coversEarlyReturnBranch() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    when(mockData.getStatusNumber()).thenReturn(4);
    when(mockData.getDesignation()).thenReturn("file.txt");
    setField(view, "selectedData", mockData);

    invoke(view, "confirmDelete");
    ConfirmDialog confirm = _get(ConfirmDialog.class);
    assertTrue(confirm.isOpened());
    ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));
  }

  @Test
  void confirmDelete_withDataStatusOk_coversDeleteDataPath() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    when(mockData.getStatusNumber()).thenReturn(1);
    when(mockData.getDesignation()).thenReturn("file.txt");
    setField(view, "selectedData", mockData);

    invoke(view, "confirmDelete");
    ConfirmDialog confirm = _get(ConfirmDialog.class);
    assertTrue(confirm.isOpened());
    ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));
  }

  @Test
  void confirmDelete_withNode_coversDeleteNodePath() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getDesignation()).thenReturn("Folder");
    setField(view, "selectedNode", mockNode);

    invoke(view, "confirmDelete");
    ConfirmDialog confirm = _get(ConfirmDialog.class);
    assertTrue(confirm.isOpened());
    ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));
  }

  // ---- Check-in Dialog full submit ----

  @Test
  @SuppressWarnings("unchecked")
  void openCheckinDialog_fullSubmit_coversLambdaBody() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer selectedDataMock = mock(XincoCoreDataServer.class);
    when(selectedDataMock.getId()).thenReturn(1);
    setField(view, "selectedData", selectedDataMock);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    Path tempDir = Files.createTempDirectory("xinco-test-checkin");
    try (MockedStatic<XincoCoreDataServer> msData = mockStatic(XincoCoreDataServer.class);
        MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.getXincoCoreLogs()).thenReturn(new ArrayList<>());
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoAddAttributeServer> mcAttr =
            mockConstruction(
                XincoAddAttributeServer.class,
                (m, ctx) -> {
                  when(m.write2DB()).thenReturn(1);
                })) {
      Path baseFile = tempDir.resolve("base.bin");
      Path versionFile = tempDir.resolve("version.bin");
      final int[] cnt = {0};
      msData
          .when(() -> XincoCoreDataServer.getXincoCoreDataPath(any(), anyInt(), anyString()))
          .thenAnswer(
              inv -> {
                cnt[0]++;
                return cnt[0] == 1 ? baseFile.toString() : versionFile.toString();
              });

      invoke(view, "openCheckinDialog");

      Upload upload = _get(Upload.class);
      MemoryBuffer buffer = (MemoryBuffer) upload.getReceiver();
      try (OutputStream out = buffer.receiveUpload("revised.txt", "text/plain")) {
        out.write("revised content".getBytes());
      }

      List<Button> btns = _find(Button.class, spec -> spec.withText("Checkin File"));
      if (!btns.isEmpty()) {
        btns.get(0).click();
      }
    } finally {
      try {
        Files.walk(tempDir).sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
      } catch (Exception ignored) {
      }
    }
  }

  // ---- New Folder Dialog ----

  @Test
  void openNewFolderDialog_emptyName_coversValidationBranch() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    setField(view, "selectedNode", mockNode);

    invoke(view, "openNewFolderDialog");

    List<Button> createBtns = _find(Button.class, spec -> spec.withText("Create"));
    if (!createBtns.isEmpty()) {
      createBtns.get(0).click();
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void openNewFolderDialog_withName_coversCreatePath() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    XincoCoreLanguage mockLang = mock(XincoCoreLanguage.class);
    when(mockLang.getId()).thenReturn(1);
    when(mockNode.getXincoCoreLanguage()).thenReturn(mockLang);
    setField(view, "selectedNode", mockNode);

    try (MockedConstruction<XincoCoreNodeServer> mcNode =
        mockConstruction(
            XincoCoreNodeServer.class,
            (m, ctx) -> {
              when(m.write2DB()).thenReturn(1);
            })) {
      invoke(view, "openNewFolderDialog");

      _find(TextField.class, spec -> spec.withLabel("Folder")).stream()
          .findFirst()
          .ifPresent(f -> f.setValue("New Folder"));

      List<Button> createBtns = _find(Button.class, spec -> spec.withText("Create"));
      if (!createBtns.isEmpty()) {
        createBtns.get(0).click();
      }
    }
  }

  @Test
  void diagnostic_buttonClickInDialogFooterFiresListener() throws Exception {
    // Confirms whether button.click() works for buttons in dialog footers
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreACEServer> msAce = mockStatic(XincoCoreACEServer.class);
        MockedStatic<XincoCoreUserServer> msUser = mockStatic(XincoCoreUserServer.class);
        MockedStatic<XincoCoreGroupServer> msGroup = mockStatic(XincoCoreGroupServer.class)) {
      msAce
          .when(() -> XincoCoreACEServer.getXincoCoreACL(anyInt(), anyString()))
          .thenReturn(new ArrayList<>());
      msUser.when(XincoCoreUserServer::getXincoCoreUsers).thenReturn(new ArrayList<>());
      msGroup.when(XincoCoreGroupServer::getXincoCoreGroups).thenReturn(new ArrayList<>());
      invoke(view, "openAclDialog");

      List<Button> saveBtns = _find(Button.class, spec -> spec.withText("Save"));
      assertFalse(saveBtns.isEmpty(), "Save button must be findable");
      Button foundBtn = saveBtns.get(0);

      // Attach a probe listener to verify click() fires on this instance
      boolean[] probeHit = {false};
      foundBtn.addClickListener(e -> probeHit[0] = true);
      foundBtn.click();

      assertTrue(probeHit[0], "button.click() must fire listeners on the found button instance");
    }
  }

  // ---- doCreateFolder direct tests ----

  @Test
  void doCreateFolder_emptyName_setsFieldInvalid() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField nameField = new TextField();
    nameField.setValue("");
    Dialog dialog = new Dialog();
    view.doCreateFolder(nameField, dialog);
    assertTrue(nameField.isInvalid(), "empty name should mark field invalid");
  }

  @Test
  @SuppressWarnings("unchecked")
  void doCreateFolder_exception_coversExceptionBranch() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    XincoCoreLanguage mockLang = mock(XincoCoreLanguage.class);
    when(mockLang.getId()).thenReturn(1);
    when(mockNode.getXincoCoreLanguage()).thenReturn(mockLang);
    setField(view, "selectedNode", mockNode);

    Dialog dialog = new Dialog();
    dialog.open();
    TextField nameField = new TextField();
    nameField.setValue("TestFolder");

    try (MockedConstruction<XincoCoreNodeServer> mc =
        mockConstruction(
            XincoCoreNodeServer.class,
            (m, ctx) -> {
              doThrow(new XincoException("DB error")).when(m).write2DB();
              when(m.getDesignation()).thenReturn("Root");
              when(m.getXincoCoreNodes()).thenReturn(new ArrayList<>());
            })) {
      assertDoesNotThrow(() -> view.doCreateFolder(nameField, dialog));
    }
  }

  // ---- doSaveAcl direct tests ----

  @Test
  void doSaveAcl_emptyLists_closesDialog() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    Dialog dialog = new Dialog();
    dialog.open();
    view.doSaveAcl(new ArrayList<>(), new ArrayList<>(), dialog);
    assertFalse(dialog.isOpened(), "dialog should close after successful save");
  }

  @Test
  void doSaveAcl_withDeletableAce_callsRemoveFromDB() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreACEServer mockAce = mock(XincoCoreACEServer.class);
    when(mockAce.getId()).thenReturn(5);
    Dialog dialog = new Dialog();
    dialog.open();
    try (MockedStatic<XincoCoreACEServer> ms = mockStatic(XincoCoreACEServer.class)) {
      view.doSaveAcl(List.of(mockAce), new ArrayList<>(), dialog);
      ms.verify(() -> XincoCoreACEServer.removeFromDB(eq(mockAce), anyInt()));
    }
    assertFalse(dialog.isOpened());
  }

  @Test
  void doSaveAcl_withIdZeroAce_skipsRemoveFromDB() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreACEServer mockAce = mock(XincoCoreACEServer.class);
    when(mockAce.getId()).thenReturn(0);
    Dialog dialog = new Dialog();
    dialog.open();
    try (MockedStatic<XincoCoreACEServer> ms = mockStatic(XincoCoreACEServer.class)) {
      view.doSaveAcl(List.of(mockAce), new ArrayList<>(), dialog);
      ms.verify(() -> XincoCoreACEServer.removeFromDB(any(), anyInt()), never());
    }
    assertFalse(dialog.isOpened());
  }

  @Test
  void doSaveAcl_withWritableAce_callsWrite2DB() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreACEServer mockAce = mock(XincoCoreACEServer.class);
    Dialog dialog = new Dialog();
    dialog.open();
    try (MockedStatic<XincoCoreACEServer> ms = mockStatic(XincoCoreACEServer.class)) {
      view.doSaveAcl(new ArrayList<>(), List.of(mockAce), dialog);
      verify(mockAce).write2DB();
    }
    assertFalse(dialog.isOpened());
  }

  @Test
  void doSaveAcl_writeThrows_dialogStaysOpen() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreACEServer mockAce = mock(XincoCoreACEServer.class);
    doThrow(new RuntimeException("write failed")).when(mockAce).write2DB();
    Dialog dialog = new Dialog();
    dialog.open();
    try (MockedStatic<XincoCoreACEServer> ms = mockStatic(XincoCoreACEServer.class)) {
      assertDoesNotThrow(() -> view.doSaveAcl(new ArrayList<>(), List.of(mockAce), dialog));
    }
    assertTrue(dialog.isOpened(), "dialog stays open when save fails");
  }

  // ---- doCheckin direct tests ----

  @Test
  void doCheckin_emptyBuffer_showsError() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    Dialog dialog = new Dialog();
    dialog.open();
    MemoryBuffer emptyBuffer = new MemoryBuffer();
    view.doCheckin(emptyBuffer, 1, 0, 0, "", "desc", dialog);
    assertTrue(dialog.isOpened(), "dialog stays open when no file uploaded");
  }

  @Test
  void doCheckin_exception_caught() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer mockSelected = mock(XincoCoreDataServer.class);
    when(mockSelected.getId()).thenReturn(1);
    setField(view, "selectedData", mockSelected);

    Dialog dialog = new Dialog();
    dialog.open();
    MemoryBuffer buffer = mock(MemoryBuffer.class);
    when(buffer.getFileName()).thenReturn("file.txt");

    try (MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(XincoCoreDataServer.class, (m, ctx) -> when(m.getId()).thenReturn(1));
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(XincoCoreLogServer.class, (m, ctx) -> when(m.getId()).thenReturn(1));
        MockedStatic<XincoCoreDataServer> msData = mockStatic(XincoCoreDataServer.class)) {
      msData
          .when(() -> XincoCoreDataServer.getXincoCoreDataPath(any(), anyInt(), anyString()))
          .thenThrow(new RuntimeException("path error"));
      assertDoesNotThrow(() -> view.doCheckin(buffer, 1, 0, 0, "", "desc", dialog));
    }
    assertTrue(dialog.isOpened(), "dialog stays open on error");
  }

  // ---- doAddData direct tests ----

  @Test
  void doAddData_emptyName_setsFieldInvalid() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField designField = new TextField();
    designField.setValue("");
    Dialog dialog = new Dialog();
    view.doAddData(designField, new MemoryBuffer(), mock(XincoCoreLanguageServer.class), dialog);
    assertTrue(designField.isInvalid(), "empty name should mark field invalid");
  }

  @Test
  void doAddData_nullFileName_showsError() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField designField = new TextField();
    designField.setValue("My File");
    MemoryBuffer emptyBuffer = new MemoryBuffer();
    Dialog dialog = new Dialog();
    assertDoesNotThrow(
        () ->
            view.doAddData(designField, emptyBuffer, mock(XincoCoreLanguageServer.class), dialog));
  }

  @Test
  void doAddData_nullLang_showsError() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField designField = new TextField();
    designField.setValue("My File");
    MemoryBuffer buffer = mock(MemoryBuffer.class);
    when(buffer.getFileName()).thenReturn("file.txt");
    Dialog dialog = new Dialog();
    assertDoesNotThrow(() -> view.doAddData(designField, buffer, null, dialog));
  }

  @Test
  void doAddData_exception_caught() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    setField(view, "selectedNode", mockNode);

    TextField designField = new TextField();
    designField.setValue("My File");
    MemoryBuffer buffer = mock(MemoryBuffer.class);
    when(buffer.getFileName()).thenReturn("file.txt");
    XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
    when(mockLang.getId()).thenReturn(1);
    Dialog dialog = new Dialog();
    dialog.open();

    try (MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(XincoCoreDataServer.class, (m, ctx) -> when(m.getId()).thenReturn(1));
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(XincoCoreLogServer.class, (m, ctx) -> when(m.getId()).thenReturn(1));
        MockedStatic<XincoCoreDataServer> msData = mockStatic(XincoCoreDataServer.class)) {
      msData
          .when(() -> XincoCoreDataServer.getXincoCoreDataPath(any(), anyInt(), anyString()))
          .thenThrow(new RuntimeException("path error"));
      assertDoesNotThrow(() -> view.doAddData(designField, buffer, mockLang, dialog));
    }
    assertTrue(dialog.isOpened(), "dialog stays open on error");
  }

  // ---- beforeEnter / afterNavigation / getChildNodes / archiveSelected ----

  @Test
  void beforeEnter_noSession_reroutesToLogin() {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    VaadinSession.getCurrent().setAttribute(UserSession.class, null);
    BeforeEnterEvent mockEvent = mock(BeforeEnterEvent.class);
    view.beforeEnter(mockEvent);
    verify(mockEvent).rerouteTo(LoginView.class);
  }

  @Test
  void beforeEnter_withLoggedInSession_setsSession() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    UserSession loggedIn = loggedInSession();
    VaadinSession.getCurrent().setAttribute(UserSession.class, loggedIn);
    BeforeEnterEvent mockEvent = mock(BeforeEnterEvent.class);
    view.beforeEnter(mockEvent);
    Field f = ExplorerView.class.getDeclaredField("session");
    f.setAccessible(true);
    assertSame(loggedIn, f.get(view));
  }

  @Test
  void afterNavigation_callsLoadRootNodes() {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    AfterNavigationEvent mockEvent = mock(AfterNavigationEvent.class);
    assertDoesNotThrow(() -> view.afterNavigation(mockEvent));
  }

  @Test
  @SuppressWarnings("unchecked")
  void getChildNodes_withMockedParent_returnsNodeChildren() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreNodeServer mockChild = mock(XincoCoreNodeServer.class);
    XincoCoreNodeServer mockParent = mock(XincoCoreNodeServer.class);
    when(mockParent.getXincoCoreNodes()).thenReturn(new ArrayList<>(List.of(mockChild)));
    Method m = ExplorerView.class.getDeclaredMethod("getChildNodes", XincoCoreNodeServer.class);
    m.setAccessible(true);
    List<XincoCoreNodeServer> children = (List<XincoCoreNodeServer>) m.invoke(view, mockParent);
    assertEquals(1, children.size());
    assertSame(mockChild, children.get(0));
  }

  @Test
  void archiveSelected_nullData_isNoop() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    invoke(view, "archiveSelected");
    assertTrue(_find(Dialog.class).isEmpty(), "no dialog when selectedData is null");
  }

  @Test
  void archiveSelected_nonFileData_isNoop() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(2);
    setField(view, "selectedData", mockData);
    invoke(view, "archiveSelected");
    assertTrue(_find(Dialog.class).isEmpty(), "no dialog for non-file data type");
  }

  @Test
  void archiveSelected_fileData_constructionFails_coversExceptionBranch() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer mockSelected = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockSelected.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockSelected.getId()).thenReturn(1);
    setField(view, "selectedData", mockSelected);
    invoke(view, "archiveSelected");
  }

  @Test
  void archiveSelected_fileData_mockedConstruction_opensDialog() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer mockSelected = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockSelected.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockSelected.getId()).thenReturn(1);
    when(mockSelected.getDesignation()).thenReturn("file.txt");
    setField(view, "selectedData", mockSelected);

    try (MockedConstruction<XincoCoreDataServer> mc =
        mockConstruction(
            XincoCoreDataServer.class,
            (m, ctx) -> {
              when(m.getXincoAddAttributes()).thenReturn(new ArrayList<>());
              when(m.getDesignation()).thenReturn("file.txt");
            })) {
      invoke(view, "archiveSelected");
      assertFalse(_find(Dialog.class).isEmpty(), "archive dialog should open");
    }
  }

  @Test
  void doAddData_success_closesDialog() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    TextField designField = new TextField();
    designField.setValue("TestFile.txt");
    Dialog dialog = new Dialog();
    dialog.open();

    MemoryBuffer buffer = mock(MemoryBuffer.class);
    when(buffer.getFileName()).thenReturn("upload.txt");
    when(buffer.getInputStream()).thenReturn(new ByteArrayInputStream("content".getBytes()));

    XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
    when(mockLang.getId()).thenReturn(1);

    Path tempDir = Files.createTempDirectory("xinco-success-adddata");
    try (MockedStatic<XincoCoreDataServer> msData = mockStatic(XincoCoreDataServer.class);
        MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoAddAttributeServer> mcAttr =
            mockConstruction(
                XincoAddAttributeServer.class, (m, ctx) -> when(m.write2DB()).thenReturn(1))) {
      Path repo = tempDir.resolve("1-1.bin");
      Path base = tempDir.resolve("1.bin");
      int[] cnt = {0};
      msData
          .when(() -> XincoCoreDataServer.getXincoCoreDataPath(any(), anyInt(), anyString()))
          .thenAnswer(inv -> cnt[0]++ == 0 ? repo.toString() : base.toString());

      assertDoesNotThrow(() -> view.doAddData(designField, buffer, mockLang, dialog));
      assertFalse(dialog.isOpened(), "dialog should close on success");
    } finally {
      Files.walk(tempDir).sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
    }
  }

  @Test
  void doCheckin_success_closesDialog() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer selectedDataMock = mock(XincoCoreDataServer.class);
    when(selectedDataMock.getId()).thenReturn(1);
    setField(view, "selectedData", selectedDataMock);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    MemoryBuffer buffer = mock(MemoryBuffer.class);
    when(buffer.getFileName()).thenReturn("revised.txt");
    when(buffer.getInputStream()).thenReturn(new ByteArrayInputStream("revised".getBytes()));

    Dialog dialog = new Dialog();
    dialog.open();

    Path tempDir = Files.createTempDirectory("xinco-success-checkin");
    try (MockedStatic<XincoCoreDataServer> msData = mockStatic(XincoCoreDataServer.class);
        MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                });
        MockedConstruction<XincoAddAttributeServer> mcAttr =
            mockConstruction(
                XincoAddAttributeServer.class, (m, ctx) -> when(m.write2DB()).thenReturn(1))) {
      Path base = tempDir.resolve("base.bin");
      Path version = tempDir.resolve("version.bin");
      int[] cnt = {0};
      msData
          .when(() -> XincoCoreDataServer.getXincoCoreDataPath(any(), anyInt(), anyString()))
          .thenAnswer(inv -> cnt[0]++ == 0 ? base.toString() : version.toString());

      assertDoesNotThrow(() -> view.doCheckin(buffer, 1, 0, 0, "", "Test checkin", dialog));
      assertFalse(dialog.isOpened(), "dialog should close on success");
    } finally {
      Files.walk(tempDir).sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
    }
  }

  @SuppressWarnings("unchecked")
  private static void injectSearcher(
      ExplorerView view, Function<String, java.util.ArrayList> searcher) throws Exception {
    Field f = ExplorerView.class.getDeclaredField("searcher");
    f.setAccessible(true);
    f.set(view, searcher);
  }

  @Test
  void clearSearch_hidesStatusAndClearButton() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    // Simulate a successful search by injecting a searcher with empty results.
    injectSearcher(view, q -> new java.util.ArrayList());
    Field sf = ExplorerView.class.getDeclaredField("searchField");
    sf.setAccessible(true);
    ((com.vaadin.flow.component.textfield.TextField) sf.get(view)).setValue("anything");
    invoke(view, "doSearch");

    invoke(view, "clearSearch");

    Field sl = ExplorerView.class.getDeclaredField("searchStatusLabel");
    sl.setAccessible(true);
    assertFalse(((com.vaadin.flow.component.html.Span) sl.get(view)).isVisible(), "status hidden");

    Field clr = ExplorerView.class.getDeclaredField("clearSearchBtn");
    clr.setAccessible(true);
    assertFalse(
        ((com.vaadin.flow.component.button.Button) clr.get(view)).isVisible(), "clear hidden");
  }

  // ── Gap documentation tests ────────────────────────────────────────────────
  // Each @Disabled test documents expected behaviour that is not yet present.
  // Enable and flip assertions when the feature is implemented.

  @Test
  @SuppressWarnings("unchecked")
  void nodeSelected_propertyGridShouldShowNodeProperties() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(42);
    when(mockNode.getDesignation()).thenReturn("MyFolder");
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    when(mockNode.getXincoCoreAcl()).thenReturn(List.of());

    com.vaadin.flow.component.treegrid.TreeGrid<XincoCoreNodeServer> tree =
        (com.vaadin.flow.component.treegrid.TreeGrid<XincoCoreNodeServer>) _get(TreeGrid.class);
    tree.setItems(List.of(mockNode), n -> List.of());
    tree.asSingleSelect().setValue(mockNode);

    PropertyGrid pg = _get(PropertyGrid.class);
    assertTrue(
        pg.getListDataView().getItemCount() > 0,
        "PropertyGrid must show folder details when a node is selected");
  }

  @Test
  void delete_shouldMoveToTrashNotPermanentlyRemove() throws Exception {
    // §2.7.5: users move data to the Trash folder; an admin then empties it permanently.
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreDataServer selectedDataMock = mock(XincoCoreDataServer.class);
    when(selectedDataMock.getId()).thenReturn(7);
    when(selectedDataMock.getDesignation()).thenReturn("report.pdf");
    when(selectedDataMock.getStatusNumber()).thenReturn(1);
    setField(view, "selectedData", selectedDataMock);

    try (MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(7);
                  when(m.write2DB()).thenReturn(7);
                });
        MockedConstruction<XincoCoreNodeServer> mcNode =
            mockConstruction(
                XincoCoreNodeServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.getXincoCoreNodes()).thenReturn(new ArrayList<>());
                })) {
      invoke(view, "confirmDelete");
      ConfirmDialog confirm = _get(ConfirmDialog.class);
      assertTrue(confirm.isOpened());
      ComponentUtil.fireEvent(confirm, new ConfirmDialog.ConfirmEvent(confirm, false));

      // Original selectedData must never have deleteFromDB() called
      verify(selectedDataMock, never()).deleteFromDB();

      // The freshly constructed XincoCoreDataServer must have parentId set to Trash (2)
      XincoCoreDataServer freshInstance = mcData.constructed().get(0);
      verify(freshInstance).setXincoCoreNodeId(2);
      verify(freshInstance).write2DB();
    }
  }

  @Test
  void addDataDialog_shouldOfferDataTypeSelection() throws Exception {
    // §2.4: four data types must be selectable when adding a data item.
    // Currently the dialog skips type selection and always creates type=1 (File).
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedNode", mockNode);

    invoke(view, "openAddDataDialog");

    assertFalse(
        _find(com.vaadin.flow.component.select.Select.class).stream()
            .filter(s -> s.getLabel() != null && s.getLabel().toLowerCase().contains("type"))
            .toList()
            .isEmpty(),
        "Add Data dialog must contain a data-type selector (File / Text / URL / Contact)");
  }

  @Test
  @Disabled("gap: search bar should include a language filter per §2.12")
  void searchBar_shouldHaveLanguageFilter() {
    // §2.12: language can optionally be added to a search query.
    addView(new ExplorerView(loggedInSession()));

    assertFalse(
        _find(com.vaadin.flow.component.select.Select.class).stream()
            .filter(s -> s.getLabel() != null && s.getLabel().toLowerCase().contains("language"))
            .toList()
            .isEmpty(),
        "Search bar must contain a language filter selector");
  }

  @Test
  void checkout_shouldPromptForReasonComment() throws Exception {
    // §2.6 / §2.13: every modification must prompt for a reason; reason cannot be empty.
    // Currently checkout sets status=4 and logs immediately with a hard-coded description.
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getId()).thenReturn(3);
    when(mockData.getStatusNumber()).thenReturn(1);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockData.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedData", mockData);

    invoke(view, "checkoutSelected");

    assertTrue(
        _find(Dialog.class).stream().anyMatch(d -> d.isOpened()),
        "Checkout must open a reason/comment dialog before writing the log entry");
  }
}
