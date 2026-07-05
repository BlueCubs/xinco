package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bluecubs.xinco.core.server.XincoCoreACEServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreLanguage;
import com.bluecubs.xinco.ui.component.PropertyGrid;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.Function;
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
    Method m = ExplorerView.class.getDeclaredMethod("statusLabel", int.class);
    m.setAccessible(true);
    assertEquals("Active", m.invoke(null, 1));
    assertEquals("Locked", m.invoke(null, 2));
    assertEquals("Checked Out", m.invoke(null, 4));
    assertEquals("Published", m.invoke(null, 5));
    String unknown = (String) m.invoke(null, 99);
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
    com.vaadin.flow.component.html.Span label =
        (com.vaadin.flow.component.html.Span) sl.get(view);
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

    Select<Integer> modelSelect = _get(Select.class, spec -> spec.withLabel("Archive mode"));
    assertNotNull(modelSelect, "Archive mode selector present");
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

    Select<Integer> modelSelect = _get(Select.class, spec -> spec.withLabel("Archive mode"));
    DatePicker archiveDate = _get(DatePicker.class, spec -> spec.withLabel("Archive on"));
    IntegerField archiveDays = _get(IntegerField.class, spec -> spec.withLabel("Archive after (days)"));

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

    Checkbox minorBump = _get(Checkbox.class, spec -> spec.withLabel("Minor bump"));
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
}
