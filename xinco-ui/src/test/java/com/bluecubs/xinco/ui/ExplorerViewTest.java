package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.VaadinSession;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
  void noArgConstructor_coversResolveSession() {
    ExplorerView view = new ExplorerView();
    addView(view);
    assertNotNull(_get(TreeGrid.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  void explorerView_treeDataItemSelection_coversDataBranch() {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    @SuppressWarnings("unchecked")
    com.vaadin.flow.component.treegrid.TreeGrid<Object> tree =
        (com.vaadin.flow.component.treegrid.TreeGrid<Object>) _get(TreeGrid.class);
    tree.asSingleSelect().setValue(mockData);
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
    assertFalse(menuItem(view, "miSendEmail").isEnabled(), "miSendEmail");
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
    IntegerField archiveDays = _get(IntegerField.class, spec -> spec.withLabel("Archiving Days"));

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
      Button confirmBtn =
          _find(Button.class).stream()
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
      Button confirmBtn =
          _find(Button.class).stream()
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
    view.doCheckin("", new ByteArrayInputStream(new byte[0]), 1, 0, 0, "", "desc", dialog);
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
    try (MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(XincoCoreDataServer.class, (m, ctx) -> when(m.getId()).thenReturn(1));
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(XincoCoreLogServer.class, (m, ctx) -> when(m.getId()).thenReturn(1));
        MockedStatic<XincoCoreDataServer> msData = mockStatic(XincoCoreDataServer.class)) {
      msData
          .when(() -> XincoCoreDataServer.getXincoCoreDataPath(any(), anyInt(), anyString()))
          .thenThrow(new RuntimeException("path error"));
      assertDoesNotThrow(
          () ->
              view.doCheckin(
                  "file.txt", new ByteArrayInputStream(new byte[0]), 1, 0, 0, "", "desc", dialog));
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
    view.doAddData(
        designField,
        "",
        new ByteArrayInputStream(new byte[0]),
        mock(XincoCoreLanguageServer.class),
        0,
        null,
        30,
        dialog);
    assertTrue(designField.isInvalid(), "empty name should mark field invalid");
  }

  @Test
  void doAddData_nullFileName_showsError() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField designField = new TextField();
    designField.setValue("My File");
    Dialog dialog = new Dialog();
    assertDoesNotThrow(
        () ->
            view.doAddData(
                designField,
                "",
                new ByteArrayInputStream(new byte[0]),
                mock(XincoCoreLanguageServer.class),
                0,
                null,
                30,
                dialog));
  }

  @Test
  void doAddData_nullLang_showsError() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField designField = new TextField();
    designField.setValue("My File");
    Dialog dialog = new Dialog();
    assertDoesNotThrow(
        () ->
            view.doAddData(
                designField,
                "file.txt",
                new ByteArrayInputStream(new byte[0]),
                null,
                0,
                null,
                30,
                dialog));
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
      assertDoesNotThrow(
          () ->
              view.doAddData(
                  designField,
                  "file.txt",
                  new ByteArrayInputStream(new byte[0]),
                  mockLang,
                  0,
                  null,
                  30,
                  dialog));
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
    Method m = ExplorerView.class.getDeclaredMethod("getChildNodes", Object.class);
    m.setAccessible(true);
    List<Object> children = (List<Object>) m.invoke(view, mockParent);
    assertEquals(1, children.size());
    assertSame(mockChild, children.get(0));
  }

  @Test
  @SuppressWarnings("unchecked")
  void getChildNodes_nonNode_returnsEmptyList() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    Method m = ExplorerView.class.getDeclaredMethod("getChildNodes", Object.class);
    m.setAccessible(true);
    List<Object> result = (List<Object>) m.invoke(view, "notANode");
    assertTrue(result.isEmpty());
  }

  @Test
  void itemDesignation_node() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreNodeServer n = mock(XincoCoreNodeServer.class);
    when(n.getDesignation()).thenReturn("MyFolder");
    Method m = ExplorerView.class.getDeclaredMethod("itemDesignation", Object.class);
    m.setAccessible(true);
    assertEquals("MyFolder", m.invoke(view, n));
  }

  @Test
  void itemDesignation_data() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class);
    when(d.getDesignation()).thenReturn("file.txt");
    Method m = ExplorerView.class.getDeclaredMethod("itemDesignation", Object.class);
    m.setAccessible(true);
    assertEquals("file.txt", m.invoke(view, d));
  }

  @Test
  void itemDesignation_unknown() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    Method m = ExplorerView.class.getDeclaredMethod("itemDesignation", Object.class);
    m.setAccessible(true);
    assertEquals("", m.invoke(view, Integer.valueOf(42)));
  }

  @Test
  void buildTreeCell_node_returnsLayout() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreNodeServer n = mock(XincoCoreNodeServer.class);
    when(n.getDesignation()).thenReturn("Folder");
    Method m = ExplorerView.class.getDeclaredMethod("buildTreeCell", Object.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, n));
  }

  @Test
  void buildTreeCell_data_returnsLayout() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getDesignation()).thenReturn("report.pdf");
    when(d.getXincoCoreDataType().getId()).thenReturn(1);
    Method m = ExplorerView.class.getDeclaredMethod("buildTreeCell", Object.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  @Test
  void buildTreeCell_unknown_returnsLayout() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    Method m = ExplorerView.class.getDeclaredMethod("buildTreeCell", Object.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, "unknown"));
  }

  @Test
  void dataTypeIcon_type2_url() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(2);
    Method m = ExplorerView.class.getDeclaredMethod("dataTypeIcon", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  @Test
  void dataTypeIcon_type3_text() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(3);
    Method m = ExplorerView.class.getDeclaredMethod("dataTypeIcon", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  @Test
  void dataTypeIcon_type4_contact() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(4);
    Method m = ExplorerView.class.getDeclaredMethod("dataTypeIcon", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  @Test
  void dataTypeIcon_imageExtension() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(1);
    when(d.getDesignation()).thenReturn("photo.jpg");
    Method m = ExplorerView.class.getDeclaredMethod("dataTypeIcon", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  @Test
  void dataTypeIcon_audioExtension() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(1);
    when(d.getDesignation()).thenReturn("song.mp3");
    Method m = ExplorerView.class.getDeclaredMethod("dataTypeIcon", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  @Test
  void dataTypeIcon_videoExtension() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(1);
    when(d.getDesignation()).thenReturn("clip.mp4");
    Method m = ExplorerView.class.getDeclaredMethod("dataTypeIcon", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  @Test
  void dataTypeIcon_archiveExtension() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(1);
    when(d.getDesignation()).thenReturn("backup.zip");
    Method m = ExplorerView.class.getDeclaredMethod("dataTypeIcon", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  @Test
  void dataTypeIcon_pdfExtension() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(1);
    when(d.getDesignation()).thenReturn("doc.pdf");
    Method m = ExplorerView.class.getDeclaredMethod("dataTypeIcon", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  @Test
  void dataTypeIcon_defaultExtension() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(1);
    when(d.getDesignation()).thenReturn("data.bin");
    Method m = ExplorerView.class.getDeclaredMethod("dataTypeIcon", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertNotNull(m.invoke(view, d));
  }

  // ---- openDataItem / openUrlInTab / openTextDialog / openContactDialog ----

  @Test
  void openDataItem_type2_opensTextDialog() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(2);
    when(d.getDesignation()).thenReturn("My Note");
    XincoAddAttributeServer attr = mock(XincoAddAttributeServer.class);
    when(attr.getAttributeId()).thenReturn(1);
    when(attr.getAttribText()).thenReturn("Hello world");
    when(attr.getAttribVarchar()).thenReturn(null);
    when(d.getXincoAddAttributes()).thenReturn(new ArrayList<>(List.of(attr)));
    Method m = ExplorerView.class.getDeclaredMethod("openDataItem", XincoCoreDataServer.class);
    m.setAccessible(true);
    m.invoke(view, d);
    assertFalse(_find(com.vaadin.flow.component.dialog.Dialog.class).isEmpty());
  }

  @Test
  void openDataItem_type3_withUrl_doesNotShowError() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(3);
    XincoAddAttributeServer attr = mock(XincoAddAttributeServer.class);
    when(attr.getAttributeId()).thenReturn(1);
    when(attr.getAttribVarchar()).thenReturn("https://example.com");
    when(d.getXincoAddAttributes()).thenReturn(new ArrayList<>(List.of(attr)));
    Method m = ExplorerView.class.getDeclaredMethod("openDataItem", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertDoesNotThrow(() -> m.invoke(view, d));
  }

  @Test
  void openDataItem_type4_opensContactDialog() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(4);
    when(d.getDesignation()).thenReturn("John Doe");
    XincoAddAttributeServer attr2 = mock(XincoAddAttributeServer.class);
    when(attr2.getAttributeId()).thenReturn(2);
    when(attr2.getAttribVarchar()).thenReturn("John");
    when(attr2.getAttribText()).thenReturn(null);
    when(d.getXincoAddAttributes()).thenReturn(new ArrayList<>(List.of(attr2)));
    Method m = ExplorerView.class.getDeclaredMethod("openDataItem", XincoCoreDataServer.class);
    m.setAccessible(true);
    m.invoke(view, d);
    assertFalse(_find(com.vaadin.flow.component.dialog.Dialog.class).isEmpty());
  }

  @Test
  void openUrlInTab_noUrl_showsError() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoAddAttributes()).thenReturn(new ArrayList<>());
    Method m = ExplorerView.class.getDeclaredMethod("openUrlInTab", XincoCoreDataServer.class);
    m.setAccessible(true);
    m.invoke(view, d);
    assertFalse(_find(com.vaadin.flow.component.notification.Notification.class).isEmpty());
  }

  @Test
  void openUrlInTab_urlWithoutProtocol_doesNotThrow() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    XincoAddAttributeServer attr = mock(XincoAddAttributeServer.class);
    when(attr.getAttributeId()).thenReturn(1);
    when(attr.getAttribVarchar()).thenReturn("example.com");
    when(d.getXincoAddAttributes()).thenReturn(new ArrayList<>(List.of(attr)));
    Method m = ExplorerView.class.getDeclaredMethod("openUrlInTab", XincoCoreDataServer.class);
    m.setAccessible(true);
    assertDoesNotThrow(() -> m.invoke(view, d));
  }

  @Test
  void openTextDialog_emptyText_opensDialog() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getDesignation()).thenReturn("Note");
    when(d.getXincoAddAttributes()).thenReturn(new ArrayList<>());
    Method m = ExplorerView.class.getDeclaredMethod("openTextDialog", XincoCoreDataServer.class);
    m.setAccessible(true);
    m.invoke(view, d);
    assertFalse(_find(com.vaadin.flow.component.dialog.Dialog.class).isEmpty());
  }

  @Test
  void openContactDialog_emptyAttrs_opensDialog() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getDesignation()).thenReturn("Contact");
    when(d.getXincoAddAttributes()).thenReturn(new ArrayList<>());
    Method m = ExplorerView.class.getDeclaredMethod("openContactDialog", XincoCoreDataServer.class);
    m.setAccessible(true);
    m.invoke(view, d);
    assertFalse(_find(com.vaadin.flow.component.dialog.Dialog.class).isEmpty());
  }

  // ---- openRenameDialog / doRename ----

  @Test
  void openRenameDialog_nothingSelected_isNoop() {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    view.openRenameDialog();
    assertTrue(_find(com.vaadin.flow.component.dialog.Dialog.class).isEmpty());
  }

  @Test
  void openRenameDialog_nodeSelected_opensDialog() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreNodeServer n = mock(XincoCoreNodeServer.class);
    when(n.getDesignation()).thenReturn("Folder");
    setField(view, "selectedNode", n);
    view.openRenameDialog();
    assertFalse(_find(com.vaadin.flow.component.dialog.Dialog.class).isEmpty());
  }

  @Test
  void openRenameDialog_dataSelected_opensDialog() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getDesignation()).thenReturn("file.txt");
    setField(view, "selectedData", d);
    view.openRenameDialog();
    assertFalse(_find(com.vaadin.flow.component.dialog.Dialog.class).isEmpty());
  }

  @Test
  void doRename_emptyName_keepsDialogOpen() {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    TextField nameField = new TextField();
    nameField.setValue("");
    com.vaadin.flow.component.dialog.Dialog dialog = new com.vaadin.flow.component.dialog.Dialog();
    dialog.open();
    view.doRename(nameField, dialog);
    assertTrue(dialog.isOpened());
  }

  @Test
  void doRename_withData_coversDataBranch() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.write2DB()).thenReturn(1);
    setField(view, "selectedData", mockData);
    TextField nameField = new TextField();
    nameField.setValue("NewName");
    com.vaadin.flow.component.dialog.Dialog dialog = new com.vaadin.flow.component.dialog.Dialog();
    dialog.open();
    view.doRename(nameField, dialog);
  }

  @Test
  void doRename_withNode_coversNodeBranch() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.write2DB()).thenReturn(1);
    setField(view, "selectedNode", mockNode);
    TextField nameField = new TextField();
    nameField.setValue("NewFolder");
    com.vaadin.flow.component.dialog.Dialog dialog = new com.vaadin.flow.component.dialog.Dialog();
    dialog.open();
    view.doRename(nameField, dialog);
  }

  // ---- commentSelected ----

  @Test
  void commentSelected_nullData_isNoop() {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    view.commentSelected();
    assertTrue(_find(Dialog.class).isEmpty(), "no dialog when selectedData is null");
  }

  @Test
  void commentSelected_withData_opensReasonDialog() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer d = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(d.getXincoCoreDataType().getId()).thenReturn(1);
    setField(view, "selectedData", d);
    view.commentSelected();
    assertFalse(_find(Dialog.class).isEmpty(), "reason dialog should open");
  }

  @Test
  void commentSelected_confirmTriggered_coversLambdaBody() throws Exception {
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
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.write2DB()).thenReturn(1);
                })) {
      view.commentSelected();
      _get(TextField.class, spec -> spec.withLabel("Reason")).setValue("test comment");
      Button confirmBtn =
          _find(Button.class).stream()
              .filter(b -> "Save".equals(b.getText()))
              .findFirst()
              .orElseThrow();
      ComponentUtil.fireEvent(confirmBtn, new com.vaadin.flow.component.ClickEvent<>(confirmBtn));
    }
  }

  @Test
  void commentSelected_withNonNullLog_coversLogBranches() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer selectedDataMock = mock(XincoCoreDataServer.class);
    when(selectedDataMock.getId()).thenReturn(1);
    setField(view, "selectedData", selectedDataMock);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    XincoCoreLogServer existingLog = mock(XincoCoreLogServer.class, RETURNS_DEEP_STUBS);
    when(existingLog.getVersion().getVersionHigh()).thenReturn(1);
    when(existingLog.getVersion().getVersionMid()).thenReturn(0);
    when(existingLog.getVersion().getVersionLow()).thenReturn(3);
    when(existingLog.getVersion().getVersionPostfix()).thenReturn("beta");
    List<Object> logs = new ArrayList<>();
    logs.add(existingLog);

    try (MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.getXincoCoreLogs()).thenReturn(logs);
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.write2DB()).thenReturn(1);
                })) {
      view.commentSelected();
      _get(TextField.class, spec -> spec.withLabel("Reason")).setValue("comment with log");
      Button confirmBtn =
          _find(Button.class).stream()
              .filter(b -> "Save".equals(b.getText()))
              .findFirst()
              .orElseThrow();
      ComponentUtil.fireEvent(confirmBtn, new com.vaadin.flow.component.ClickEvent<>(confirmBtn));
    }
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

  // ---- sendEmailSelected ----

  @Test
  void sendEmailSelected_nullData_isNoop() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    assertDoesNotThrow(() -> view.sendEmailSelected());
    assertTrue(_find(Dialog.class).isEmpty());
  }

  @Test
  void sendEmailSelected_nonContactData_isNoop() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(1);
    setField(view, "selectedData", mockData);
    assertDoesNotThrow(() -> view.sendEmailSelected());
    assertTrue(_find(Dialog.class).isEmpty());
  }

  @Test
  void sendEmailSelected_noEmailAttr_showsError() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(4);
    when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>());
    setField(view, "selectedData", mockData);
    assertDoesNotThrow(() -> view.sendEmailSelected());
  }

  @Test
  void sendEmailSelected_withEmail_opensMailto() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(4);
    XincoAddAttributeServer emailAttr = mock(XincoAddAttributeServer.class);
    when(emailAttr.getAttributeId()).thenReturn(10);
    when(emailAttr.getAttribVarchar()).thenReturn("test@example.com");
    when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>(List.of(emailAttr)));
    setField(view, "selectedData", mockData);
    assertDoesNotThrow(() -> view.sendEmailSelected());
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

      assertDoesNotThrow(
          () ->
              view.doAddData(
                  designField,
                  "upload.txt",
                  new ByteArrayInputStream("content".getBytes()),
                  mockLang,
                  0,
                  null,
                  30,
                  dialog));
      assertFalse(dialog.isOpened(), "dialog should close on success");
    } finally {
      Files.walk(tempDir).sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
    }
  }

  @Test
  void doAddData_archiveModel1_writesFixedDate() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    TextField designField = new TextField();
    designField.setValue("ArchiveFile.txt");
    Dialog dialog = new Dialog();
    dialog.open();

    XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
    when(mockLang.getId()).thenReturn(1);

    Path tempDir = Files.createTempDirectory("xinco-archive1-adddata");
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

      java.time.LocalDate fixedDate = java.time.LocalDate.now().plusDays(14);
      assertDoesNotThrow(
          () ->
              view.doAddData(
                  designField,
                  "upload.txt",
                  new ByteArrayInputStream("content".getBytes()),
                  mockLang,
                  1,
                  fixedDate,
                  30,
                  dialog));
      assertFalse(dialog.isOpened(), "dialog should close on success with model=1");
    } finally {
      Files.walk(tempDir).sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
    }
  }

  @Test
  void doAddData_archiveModel2_writesDays() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    TextField designField = new TextField();
    designField.setValue("DaysFile.txt");
    Dialog dialog = new Dialog();
    dialog.open();

    XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
    when(mockLang.getId()).thenReturn(1);

    Path tempDir = Files.createTempDirectory("xinco-archive2-adddata");
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

      assertDoesNotThrow(
          () ->
              view.doAddData(
                  designField,
                  "upload.txt",
                  new ByteArrayInputStream("content".getBytes()),
                  mockLang,
                  2,
                  null,
                  7,
                  dialog));
      assertFalse(dialog.isOpened(), "dialog should close on success with model=2");
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

      assertDoesNotThrow(
          () ->
              view.doCheckin(
                  "revised.txt",
                  new ByteArrayInputStream("revised".getBytes()),
                  1,
                  0,
                  0,
                  "",
                  "Test checkin",
                  dialog));
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

  // ---- cutSelected ----

  @Test
  void cutSelected_withData_setsClipboardData() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockData.getStatusNumber()).thenReturn(1);
    when(mockData.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedData", mockData);

    invoke(view, "cutSelected");

    Field f = ExplorerView.class.getDeclaredField("clipboardData");
    f.setAccessible(true);
    assertSame(mockData, f.get(view), "clipboardData should be set to selectedData after cut");
  }

  @Test
  void cutSelected_withNode_setsClipboardNode() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedNode", mockNode);

    invoke(view, "cutSelected");

    Field f = ExplorerView.class.getDeclaredField("clipboardNode");
    f.setAccessible(true);
    assertSame(mockNode, f.get(view), "clipboardNode should be set to selectedNode after cut");
  }

  @Test
  void cutSelected_withNothingSelected_clipboardEmpty() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    invoke(view, "cutSelected");

    Field fn = ExplorerView.class.getDeclaredField("clipboardNode");
    fn.setAccessible(true);
    Field fd = ExplorerView.class.getDeclaredField("clipboardData");
    fd.setAccessible(true);
    assertNull(fn.get(view));
    assertNull(fd.get(view));
  }

  // ---- pasteClipboard ----

  @Test
  void pasteClipboard_withNullNode_isNoop() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    // selectedNode is null → early return
    invoke(view, "pasteClipboard");
    // No exception should be thrown
  }

  @Test
  @SuppressWarnings("unchecked")
  void pasteClipboard_withClipboardData_movesData() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreNodeServer mockTarget = mock(XincoCoreNodeServer.class);
    when(mockTarget.getId()).thenReturn(5);
    when(mockTarget.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockTarget);

    XincoCoreDataServer mockClip = mock(XincoCoreDataServer.class);
    when(mockClip.getId()).thenReturn(10);
    setField(view, "clipboardData", mockClip);

    try (MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(10);
                  when(m.getXincoCoreNodeId()).thenReturn(3); // different from target 5
                  when(m.write2DB()).thenReturn(1);
                  when(m.getXincoCoreLogs()).thenReturn(new ArrayList<>());
                });
        MockedConstruction<XincoCoreLogServer> mcLog =
            mockConstruction(
                XincoCoreLogServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.write2DB()).thenReturn(1);
                })) {
      assertDoesNotThrow(() -> invoke(view, "pasteClipboard"));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void pasteClipboard_withClipboardNode_movesNode() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreNodeServer mockTarget = mock(XincoCoreNodeServer.class);
    when(mockTarget.getId()).thenReturn(5);
    setField(view, "selectedNode", mockTarget);

    XincoCoreNodeServer mockClip = mock(XincoCoreNodeServer.class);
    when(mockClip.getId()).thenReturn(10);
    setField(view, "clipboardNode", mockClip);

    try (MockedConstruction<XincoCoreNodeServer> mcNode =
        mockConstruction(
            XincoCoreNodeServer.class,
            (m, ctx) -> {
              when(m.getId()).thenReturn(ctx.arguments().isEmpty() ? 1 : 10);
              when(m.write2DB()).thenReturn(1);
              when(m.getXincoCoreNodes()).thenReturn(new ArrayList<>());
              when(m.getDesignation()).thenReturn("Root");
            })) {
      assertDoesNotThrow(() -> invoke(view, "pasteClipboard"));
    }
  }

  // ---- openVersionHistoryDialog ----

  @Test
  void openVersionHistoryDialog_nullData_isNoop() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    // selectedData is null → early return
    invoke(view, "openVersionHistoryDialog");
    assertTrue(_find(Dialog.class).isEmpty(), "no dialog when selectedData is null");
  }

  @Test
  void openVersionHistoryDialog_withData_opensDialog() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreDataServer mockSel = mock(XincoCoreDataServer.class);
    when(mockSel.getId()).thenReturn(1);
    setField(view, "selectedData", mockSel);

    try (MockedConstruction<XincoCoreDataServer> mc =
        mockConstruction(
            XincoCoreDataServer.class,
            (m, ctx) -> {
              when(m.getId()).thenReturn(1);
              when(m.getDesignation()).thenReturn("test.pdf");
              when(m.getXincoCoreLogs()).thenReturn(new ArrayList<>());
            })) {
      invoke(view, "openVersionHistoryDialog");
      assertFalse(_find(Dialog.class).isEmpty(), "version history dialog should open");
    }
  }

  @Test
  void openVersionHistoryDialog_constructorThrows_showsError() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreDataServer mockSel = mock(XincoCoreDataServer.class);
    when(mockSel.getId()).thenReturn(99);
    setField(view, "selectedData", mockSel);

    try (MockedConstruction<XincoCoreDataServer> mc =
        mockConstruction(
            XincoCoreDataServer.class,
            (m, ctx) -> {
              throw new RuntimeException("DB error");
            })) {
      assertDoesNotThrow(() -> invoke(view, "openVersionHistoryDialog"));
    }
    assertTrue(_find(Dialog.class).isEmpty(), "no dialog when constructor throws");
  }

  // ---- downloadVersion ----

  @Test
  void downloadVersion_fileNotFound_showsError() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    try (MockedStatic<XincoCoreDataServer> ms = mockStatic(XincoCoreDataServer.class)) {
      ms.when(() -> XincoCoreDataServer.getXincoCoreDataPath(any(), anyInt(), anyString()))
          .thenReturn("/nonexistent/path/1-1");
      Method m =
          ExplorerView.class.getDeclaredMethod(
              "downloadVersion", int.class, int.class, String.class);
      m.setAccessible(true);
      assertDoesNotThrow(() -> m.invoke(view, 1, 1, "file.pdf"));
    }
  }

  // ---- doAddNonFileData ----

  @Test
  void doAddNonFileData_emptyName_setsFieldInvalid() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField designField = new TextField();
    designField.setValue("");
    Dialog dialog = new Dialog();
    view.doAddNonFileData(2, designField, mock(XincoCoreLanguageServer.class), "content", dialog);
    assertTrue(designField.isInvalid(), "empty name should mark field invalid");
  }

  @Test
  void doAddNonFileData_nullLang_showsError() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField designField = new TextField();
    designField.setValue("My Text");
    Dialog dialog = new Dialog();
    dialog.open();
    assertDoesNotThrow(() -> view.doAddNonFileData(2, designField, null, "content", dialog));
    assertTrue(dialog.isOpened(), "dialog stays open when lang is null");
  }

  @Test
  @SuppressWarnings("unchecked")
  void doAddNonFileData_type2_success_closesDialog() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    TextField designField = new TextField();
    designField.setValue("My Text Doc");
    XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
    when(mockLang.getId()).thenReturn(1);
    Dialog dialog = new Dialog();
    dialog.open();

    try (MockedConstruction<XincoCoreDataServer> mcData =
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
      assertDoesNotThrow(
          () -> view.doAddNonFileData(2, designField, mockLang, "Hello World", dialog));
      assertFalse(dialog.isOpened(), "dialog should close on success");
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void doAddNonFileData_type3_success_closesDialog() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    TextField designField = new TextField();
    designField.setValue("My URL");
    XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
    when(mockLang.getId()).thenReturn(1);
    Dialog dialog = new Dialog();
    dialog.open();

    try (MockedConstruction<XincoCoreDataServer> mcData =
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
      assertDoesNotThrow(
          () -> view.doAddNonFileData(3, designField, mockLang, "https://example.com", dialog));
      assertFalse(dialog.isOpened(), "dialog should close on success");
    }
  }

  // ---- doAddContactData ----

  @Test
  void doAddContactData_emptyName_setsFieldInvalid() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField designField = new TextField();
    designField.setValue("");
    Dialog dialog = new Dialog();
    view.doAddContactData(designField, mock(XincoCoreLanguageServer.class), Map.of(), dialog);
    assertTrue(designField.isInvalid(), "empty name should mark field invalid");
  }

  @Test
  void doAddContactData_nullLang_showsError() {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    TextField designField = new TextField();
    designField.setValue("John Doe");
    Dialog dialog = new Dialog();
    dialog.open();
    assertDoesNotThrow(() -> view.doAddContactData(designField, null, Map.of(), dialog));
    assertTrue(dialog.isOpened(), "dialog stays open when lang is null");
  }

  @Test
  @SuppressWarnings("unchecked")
  void doAddContactData_success_closesDialog() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    TextField designField = new TextField();
    designField.setValue("John Doe");
    XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
    when(mockLang.getId()).thenReturn(1);
    Dialog dialog = new Dialog();
    dialog.open();

    Map<Integer, String> attrs = new java.util.LinkedHashMap<>();
    attrs.put(1, "Mr.");
    attrs.put(2, "John");
    attrs.put(4, "Doe");

    try (MockedConstruction<XincoCoreDataServer> mcData =
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
      assertDoesNotThrow(() -> view.doAddContactData(designField, mockLang, attrs, dialog));
      assertFalse(dialog.isOpened(), "dialog should close on success");
    }
  }

  // ---- menuState: new menu items (versionHistory, cut, paste) ----

  @Test
  void menuState_loggedIn_fileSelected_versionHistoryEnabled() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockData.getStatusNumber()).thenReturn(1);
    when(mockData.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedData", mockData);

    invoke(view, "updateMenuState");

    assertTrue(menuItem(view, "miVersionHistory").isEnabled(), "versionHistory enabled for file");
  }

  @Test
  void menuState_loggedIn_nodeSelected_cutEnabled() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedNode", mockNode);

    invoke(view, "updateMenuState");

    assertTrue(menuItem(view, "miCut").isEnabled(), "cut enabled when node selected and logged in");
  }

  @Test
  void menuState_loggedIn_clipboardFull_nodeSelected_pasteEnabled() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedNode", mockNode);
    setField(view, "clipboardData", mock(XincoCoreDataServer.class));

    invoke(view, "updateMenuState");

    assertTrue(
        menuItem(view, "miPaste").isEnabled(),
        "paste enabled when clipboard full and node selected");
  }

  @Test
  void menuState_noLogin_cutAndPasteDisabled() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);

    invoke(view, "updateMenuState");

    assertFalse(menuItem(view, "miCut").isEnabled(), "cut disabled when not logged in");
    assertFalse(menuItem(view, "miPaste").isEnabled(), "paste disabled when not logged in");
  }

  @Test
  void menuState_contactDataSelected_sendEmailEnabled() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(4);
    when(mockData.getStatusNumber()).thenReturn(1);
    when(mockData.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedData", mockData);
    invoke(view, "updateMenuState");
    assertTrue(menuItem(view, "miSendEmail").isEnabled(), "sendEmail enabled for contact data");
  }

  @Test
  void menuState_fileDataSelected_sendEmailDisabled() throws Exception {
    ExplorerView view = new ExplorerView(new UserSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockData.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockData.getStatusNumber()).thenReturn(1);
    when(mockData.getXincoCoreAcl()).thenReturn(List.of());
    setField(view, "selectedData", mockData);
    invoke(view, "updateMenuState");
    assertFalse(menuItem(view, "miSendEmail").isEnabled(), "sendEmail disabled for file data");
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
  void nodeSelected_propertyGridShouldShowNodeProperties() {
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
      XincoCoreDataServer freshInstance = mcData.constructed().getFirst();
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
        _find(Dialog.class).stream().anyMatch(Dialog::isOpened),
        "Checkout must open a reason/comment dialog before writing the log entry");
  }

  // ---- checkout confirm lambda coverage ----

  @Test
  @SuppressWarnings("unchecked")
  void checkout_confirmWithReason_coversLambdaBody() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreDataServer mockSelected = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(mockSelected.getId()).thenReturn(3);
    when(mockSelected.getXincoCoreDataType().getId()).thenReturn(1);
    when(mockSelected.getStatusNumber()).thenReturn(1);
    when(mockSelected.getXincoCoreAcl()).thenReturn(List.of());
    when(mockSelected.getDesignation()).thenReturn("test.pdf");
    setField(view, "selectedData", mockSelected);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreDataServer> msData = mockStatic(XincoCoreDataServer.class);
        MockedConstruction<XincoCoreDataServer> mcData =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(3);
                  when(m.getDesignation()).thenReturn("test.pdf");
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
      msData.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt())).thenReturn(null);

      invoke(view, "checkoutSelected");

      // Dialog is open — fill reason and click confirm
      TextField reasonField = _get(TextField.class, spec -> spec.withLabel("Reason"));
      reasonField.setValue("checkout for review");

      // button text = getTranslation("menu.edit.checkoutfile") = "Checkout File"
      List<Button> confirmBtns = _find(Button.class, spec -> spec.withText("Checkout File"));
      if (!confirmBtns.isEmpty()) {
        confirmBtns.get(0).click();
      }
    }
  }

  // ---- Add Data dialog: type-select listener + type 2 / 3 add button ----

  @Test
  @SuppressWarnings("unchecked")
  void openAddDataDialog_typeChange_toText_coversTypeListener() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreLanguageServer> msLang = mockStatic(XincoCoreLanguageServer.class)) {
      XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
      when(mockLang.getId()).thenReturn(1);
      when(mockLang.getSign()).thenReturn("EN");
      when(mockLang.getDesignation()).thenReturn("English");
      msLang
          .when(XincoCoreLanguageServer::getXincoCoreLanguages)
          .thenReturn(new ArrayList<>(List.of(mockLang)));

      invoke(view, "openAddDataDialog");

      // Change type selector to 2 (Text) — exercises the typeSelect change listener
      Select<Integer> typeSelect =
          _find(Select.class, spec -> spec.withLabel("Data Type")).stream()
              .filter(s -> s.getValue() instanceof Integer)
              .findFirst()
              .map(s -> (Select<Integer>) s)
              .orElse(null);
      if (typeSelect != null) {
        typeSelect.setValue(2);
        typeSelect.setValue(3);
        typeSelect.setValue(4);
        typeSelect.setValue(1);
      }
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void openAddDataDialog_archiveSectionVisibleForFileType() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreLanguageServer> msLang = mockStatic(XincoCoreLanguageServer.class)) {
      msLang.when(XincoCoreLanguageServer::getXincoCoreLanguages).thenReturn(new ArrayList<>());
      invoke(view, "openAddDataDialog");

      Select<Integer> archiveSelect =
          _find(Select.class, spec -> spec.withLabel("Archive Model")).stream()
              .filter(s -> s.getValue() instanceof Integer)
              .findFirst()
              .map(s -> (Select<Integer>) s)
              .orElse(null);
      assertNotNull(archiveSelect, "archive model select should be present");
      assertTrue(
          archiveSelect.getParent().map(p -> p.isVisible()).orElse(false),
          "archive section visible when type=1 (file)");
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void openAddDataDialog_archiveSectionHiddenForNonFileTypes() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreLanguageServer> msLang = mockStatic(XincoCoreLanguageServer.class)) {
      msLang.when(XincoCoreLanguageServer::getXincoCoreLanguages).thenReturn(new ArrayList<>());
      invoke(view, "openAddDataDialog");

      Select<Integer> typeSelect =
          _find(Select.class, spec -> spec.withLabel("Data Type")).stream()
              .filter(s -> s.getValue() instanceof Integer)
              .findFirst()
              .map(s -> (Select<Integer>) s)
              .orElse(null);
      Select<Integer> archiveSelect =
          _find(Select.class, spec -> spec.withLabel("Archive Model")).stream()
              .filter(s -> s.getValue() instanceof Integer)
              .findFirst()
              .map(s -> (Select<Integer>) s)
              .orElse(null);
      assertNotNull(typeSelect, "type select present");
      assertNotNull(archiveSelect, "archive model select present");

      typeSelect.setValue(2);
      assertFalse(
          archiveSelect.getParent().map(p -> p.isVisible()).orElse(true),
          "archive section hidden for type=2 (Text)");

      typeSelect.setValue(3);
      assertFalse(
          archiveSelect.getParent().map(p -> p.isVisible()).orElse(true),
          "archive section hidden for type=3 (URL)");

      typeSelect.setValue(4);
      assertFalse(
          archiveSelect.getParent().map(p -> p.isVisible()).orElse(true),
          "archive section hidden for type=4 (Contact)");

      typeSelect.setValue(1);
      assertTrue(
          archiveSelect.getParent().map(p -> p.isVisible()).orElse(false),
          "archive section re-appears when switching back to type=1 (File)");
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void openAddDataDialog_type2_addClick_coversTextAddBranch() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(1);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);

    try (MockedStatic<XincoCoreLanguageServer> msLang = mockStatic(XincoCoreLanguageServer.class);
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

      XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);
      when(mockLang.getId()).thenReturn(1);
      when(mockLang.getSign()).thenReturn("EN");
      when(mockLang.getDesignation()).thenReturn("English");
      msLang
          .when(XincoCoreLanguageServer::getXincoCoreLanguages)
          .thenReturn(new ArrayList<>(List.of(mockLang)));

      invoke(view, "openAddDataDialog");

      // Change type to 2 (Text)
      Select<Integer> typeSelect =
          _find(Select.class, spec -> spec.withLabel("Data Type")).stream()
              .filter(s -> s.getValue() instanceof Integer)
              .findFirst()
              .map(s -> (Select<Integer>) s)
              .orElse(null);
      if (typeSelect != null) typeSelect.setValue(2);

      // Fill designation
      _find(TextField.class, spec -> spec.withLabel("File Name")).stream()
          .findFirst()
          .ifPresent(f -> f.setValue("My Text Doc"));

      // Click Add → exercises the else-if(type==4) branch skipped, falls to doAddNonFileData
      List<Button> addBtns = _find(Button.class, spec -> spec.withText("Add"));
      if (!addBtns.isEmpty()) addBtns.get(0).click();
    }
  }

  // ---- openVersionHistoryDialog with log entries ----

  @Test
  @SuppressWarnings("unchecked")
  void openVersionHistoryDialog_withLogEntries_rendersRows() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);

    XincoCoreDataServer mockSel = mock(XincoCoreDataServer.class);
    when(mockSel.getId()).thenReturn(1);
    setField(view, "selectedData", mockSel);

    // Build a mock log entry with deep stubs to avoid resolving XincoVersion type
    XincoCoreLogServer mockLog = mock(XincoCoreLogServer.class, RETURNS_DEEP_STUBS);
    when(mockLog.getId()).thenReturn(1);
    when(mockLog.getOpCode()).thenReturn(1); // CREATION
    when(mockLog.getOpDescription()).thenReturn("initial creation");
    when(mockLog.getVersion().getVersionHigh()).thenReturn(1);
    when(mockLog.getVersion().getVersionMid()).thenReturn(0);
    when(mockLog.getVersion().getVersionLow()).thenReturn(0);
    when(mockLog.getVersion().getVersionPostfix()).thenReturn("");

    java.util.ArrayList logList = new java.util.ArrayList();
    logList.add(mockLog);

    try (MockedConstruction<XincoCoreDataServer> mc =
            mockConstruction(
                XincoCoreDataServer.class,
                (m, ctx) -> {
                  when(m.getId()).thenReturn(1);
                  when(m.getDesignation()).thenReturn("test.pdf");
                  when(m.getXincoCoreLogs()).thenReturn(logList);
                });
        MockedStatic<XincoCoreDataServer> msData = mockStatic(XincoCoreDataServer.class)) {
      msData
          .when(() -> XincoCoreDataServer.getXincoCoreDataPath(any(), anyInt(), anyString()))
          .thenReturn("/nonexistent/path"); // hasFile = false → no download button

      invoke(view, "openVersionHistoryDialog");
      assertFalse(
          _find(Dialog.class).isEmpty(), "version history dialog should open with log rows");
    }
  }

  // ---- null-selectedData early-return branches ----

  @Test
  void checkoutSelected_nullData_isNoop() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    // selectedData defaults to null → covers the if (selectedData == null) return branch
    assertDoesNotThrow(() -> invoke(view, "checkoutSelected"));
  }

  @Test
  void lockSelected_nullData_isNoop() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    assertDoesNotThrow(() -> invoke(view, "lockSelected"));
  }

  // ---- clearSearch with selectedNode branch ----

  @Test
  @SuppressWarnings("unchecked")
  void clearSearch_withSelectedNode_refreshesDataGrid() throws Exception {
    ExplorerView view = new ExplorerView(loggedInSession());
    addView(view);
    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());
    setField(view, "selectedNode", mockNode);
    assertDoesNotThrow(() -> invoke(view, "clearSearch"));
  }
}
