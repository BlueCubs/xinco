package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoAddAttributeServer;
import com.bluecubs.xinco.core.server.XincoCoreACEServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import com.bluecubs.xinco.core.server.XincoCoreLanguageServer;
import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.XincoCoreLogServerBuilder;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.index.XincoIndexer;
import com.bluecubs.xinco.ui.component.PropertyGrid;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

@Route(value = "explorer", layout = MainLayout.class)
@PageTitle("Explorer — Xinco DMS")
@AnonymousAllowed
public class ExplorerView extends VerticalLayout
    implements BeforeEnterObserver, AfterNavigationObserver {

  private static final Logger LOG = Logger.getLogger(ExplorerView.class.getName());
  private static final int TRASH_NODE_ID = 2;

  // Data state
  private XincoCoreNodeServer selectedNode;
  private XincoCoreDataServer selectedData;
  private UserSession session;

  // Menu items that need enable/disable based on context
  private com.vaadin.flow.component.contextmenu.MenuItem miNewFolder;
  private com.vaadin.flow.component.contextmenu.MenuItem miAddData;
  private com.vaadin.flow.component.contextmenu.MenuItem miDelete;
  private com.vaadin.flow.component.contextmenu.MenuItem miDownload;
  private com.vaadin.flow.component.contextmenu.MenuItem miCheckOut;
  private com.vaadin.flow.component.contextmenu.MenuItem miCheckIn;
  private com.vaadin.flow.component.contextmenu.MenuItem miUndoCheckOut;
  private com.vaadin.flow.component.contextmenu.MenuItem miLock;
  private com.vaadin.flow.component.contextmenu.MenuItem miPublish;
  private com.vaadin.flow.component.contextmenu.MenuItem miArchive;
  private com.vaadin.flow.component.contextmenu.MenuItem miVersionHistory;
  private com.vaadin.flow.component.contextmenu.MenuItem miCut;
  private com.vaadin.flow.component.contextmenu.MenuItem miPaste;
  private com.vaadin.flow.component.contextmenu.MenuItem miManageAcl;

  // Clipboard: holds a cut node or data item
  private XincoCoreNodeServer clipboardNode;
  private XincoCoreDataServer clipboardData;

  // UI components
  private final TreeGrid<Object> nodeTree = new TreeGrid<>();
  private final Grid<XincoCoreDataServer> dataGrid = new Grid<>(XincoCoreDataServer.class, false);
  private final PropertyGrid propertyGrid = new PropertyGrid();
  private final MenuBar menuBar = new MenuBar();
  private final TextField searchField = new TextField();
  private Span searchStatusLabel;
  private Button clearSearchBtn;

  private int searchLangId = 0;

  // Package-private: allows tests to inject search results without static mocking XincoIndexer.
  @SuppressWarnings("unchecked")
  Function<String, java.util.ArrayList> searcher =
      query -> XincoIndexer.findXincoCoreData(query, searchLangId);

  public ExplorerView() {
    this(resolveSession());
  }

  private static UserSession resolveSession() {
    if (VaadinSession.getCurrent() == null) return new UserSession();
    UserSession s = VaadinSession.getCurrent().getAttribute(UserSession.class);
    return s != null ? s : new UserSession();
  }

  public ExplorerView(UserSession session) {
    this.session = session;
    setSizeFull();
    setPadding(false);
    setSpacing(false);

    buildMenuBar();
    HorizontalLayout searchBar = buildSearchBar();

    HorizontalLayout content = new HorizontalLayout();
    content.setSizeFull();
    content.setPadding(false);

    buildNodeTree();
    buildDataGrid();

    VerticalLayout left = new VerticalLayout(new Span(getTranslation("general.folder")), nodeTree);
    left.setWidth("28%");
    left.setPadding(false);
    left.setSpacing(false);

    VerticalLayout center = new VerticalLayout(new Span(getTranslation("general.data")), dataGrid);
    center.setWidth("40%");
    center.setPadding(false);
    center.setSpacing(false);

    VerticalLayout right =
        new VerticalLayout(new Span(getTranslation("general.details")), propertyGrid);
    right.setWidth("32%");
    right.setPadding(false);
    right.setSpacing(false);

    content.add(left, center, right);
    add(menuBar, searchBar, content);
    expand(content);
  }

  // ── Menu ──────────────────────────────────────────────────────────────────

  private void buildMenuBar() {
    menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
    menuBar.setWidthFull();

    // Repository menu
    var repoMenu = menuBar.addItem(getTranslation("menu.repository"));
    var repoSub = repoMenu.getSubMenu();
    miNewFolder = repoSub.addItem(getTranslation("general.newfolder"), e -> openNewFolderDialog());
    miAddData =
        repoSub.addItem(getTranslation("menu.repository.adddata") + "…", e -> openAddDataDialog());
    repoSub.addItem(getTranslation("menu.repository.refresh"), e -> loadRootNodes());

    // Edit menu
    var editMenu = menuBar.addItem(getTranslation("general.edit"));
    var editSub = editMenu.getSubMenu();
    miDelete = editSub.addItem(getTranslation("general.delete"), e -> confirmDelete());
    editSub.addSeparator();
    miCut =
        editSub.addItem(getTranslation("menu.edit.movefolderdatatoclipboard"), e -> cutSelected());
    miPaste = editSub.addItem(getTranslation("menu.edit.insertfolderdata"), e -> pasteClipboard());
    editSub.addSeparator();
    miManageAcl = editSub.addItem(getTranslation("menu.edit.acl") + "…", e -> openAclDialog());

    // File menu
    var fileMenu = menuBar.addItem(getTranslation("general.data.type.file"));
    var fileSub = fileMenu.getSubMenu();
    miDownload =
        fileSub.addItem(getTranslation("menu.repository.downloadfile"), e -> downloadSelected());
    fileSub.addSeparator();
    miCheckOut = fileSub.addItem(getTranslation("menu.edit.checkoutfile"), e -> checkoutSelected());
    miCheckIn =
        fileSub.addItem(getTranslation("menu.edit.checkinfile") + "…", e -> openCheckinDialog());
    miUndoCheckOut =
        fileSub.addItem(getTranslation("menu.edit.undocheckout"), e -> undoCheckoutSelected());
    fileSub.addSeparator();
    miLock = fileSub.addItem(getTranslation("menu.edit.lockdata"), e -> lockSelected());
    miPublish = fileSub.addItem(getTranslation("menu.edit.publishdata"), e -> publishSelected());
    fileSub.addSeparator();
    miArchive = fileSub.addItem(getTranslation("window.archive") + "…", e -> archiveSelected());
    fileSub.addSeparator();
    miVersionHistory =
        fileSub.addItem(getTranslation("window.revision") + "…", e -> openVersionHistoryDialog());

    // View menu
    menuBar.addItem(
        getTranslation("menu.view"), e -> getUI().ifPresent(ui -> ui.navigate(ViewerView.class)));

    updateMenuState();
  }

  private void updateMenuState() {
    boolean loggedIn = session != null && session.isLoggedIn();
    boolean nodeSelected = selectedNode != null;
    boolean dataSelected = selectedData != null;
    boolean isFile = dataSelected && selectedData.getXincoCoreDataType().getId() == 1;

    boolean canWriteNode = loggedIn && nodeSelected && hasWriteAccess(selectedNode);
    boolean canWriteData = loggedIn && dataSelected && hasWriteAccess(selectedData);

    int dataStatus = dataSelected ? selectedData.getStatusNumber() : -1;
    // statusNumber: 1=active, 2=locked, 4=checked-out, 5=published
    boolean isCheckedOut = dataStatus == 4;

    miNewFolder.setEnabled(canWriteNode);
    miAddData.setEnabled(canWriteNode);
    miDelete.setEnabled(canWriteNode || canWriteData);
    miDownload.setEnabled(dataSelected && isFile);
    miCheckOut.setEnabled(canWriteData && isFile && dataStatus == 1);
    miCheckIn.setEnabled(canWriteData && isFile && isCheckedOut);
    miUndoCheckOut.setEnabled(canWriteData && isFile && isCheckedOut);
    miLock.setEnabled(canWriteData && (dataStatus == 1 || dataStatus == 5));
    miPublish.setEnabled(canWriteData && dataStatus == 1);
    miArchive.setEnabled(canWriteData && isFile && dataStatus != 3 && dataStatus != 4);
    miVersionHistory.setEnabled(dataSelected && isFile);
    miCut.setEnabled(loggedIn && (nodeSelected || dataSelected));
    boolean hasClipboard = clipboardNode != null || clipboardData != null;
    miPaste.setEnabled(loggedIn && hasClipboard && nodeSelected);
    boolean canAdminNode = loggedIn && nodeSelected && hasAdminAccess(selectedNode);
    boolean canAdminData = loggedIn && dataSelected && hasAdminAccess(selectedData);
    miManageAcl.setEnabled(canAdminNode || canAdminData);
  }

  // ── ACL helpers ───────────────────────────────────────────────────────────

  @SuppressWarnings("unchecked")
  private boolean hasWriteAccess(XincoCoreNodeServer node) {
    try {
      var ace = XincoCoreACEServer.checkAccess(session.getUser(), node.getXincoCoreAcl());
      return ace != null && ace.isWritePermission();
    } catch (Exception e) {
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  private boolean hasWriteAccess(XincoCoreDataServer data) {
    try {
      var ace = XincoCoreACEServer.checkAccess(session.getUser(), data.getXincoCoreAcl());
      return ace != null && ace.isWritePermission();
    } catch (Exception e) {
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  private boolean hasAdminAccess(XincoCoreNodeServer node) {
    try {
      var ace = XincoCoreACEServer.checkAccess(session.getUser(), node.getXincoCoreAcl());
      return ace != null && ace.isAdminPermission();
    } catch (Exception e) {
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  private boolean hasAdminAccess(XincoCoreDataServer data) {
    try {
      var ace = XincoCoreACEServer.checkAccess(session.getUser(), data.getXincoCoreAcl());
      return ace != null && ace.isAdminPermission();
    } catch (Exception e) {
      return false;
    }
  }

  // ── Tree ──────────────────────────────────────────────────────────────────

  private void buildNodeTree() {
    nodeTree
        .addHierarchyColumn(this::itemDesignation)
        .setRenderer(new ComponentRenderer<>(this::buildTreeCell))
        .setHeader(getTranslation("general.folder"));
    nodeTree.setSizeFull();
    nodeTree.addSelectionListener(
        e ->
            e.getFirstSelectedItem()
                .ifPresent(
                    item -> {
                      if (item instanceof XincoCoreNodeServer node) {
                        selectedNode = node;
                        selectedData = null;
                        node.fillXincoCoreData();
                        dataGrid.setItems(
                            node.getXincoCoreData().stream()
                                .filter(o -> o instanceof XincoCoreDataServer)
                                .map(o -> (XincoCoreDataServer) o)
                                .toList());
                        propertyGrid.setNode(node);
                      } else if (item instanceof XincoCoreDataServer data) {
                        selectedData = data;
                        data.loadAddAttributes();
                        propertyGrid.setData(data);
                      }
                      updateMenuState();
                    }));
  }

  private String itemDesignation(Object item) {
    if (item instanceof XincoCoreNodeServer n) return n.getDesignation();
    if (item instanceof XincoCoreDataServer d) return d.getDesignation();
    return "";
  }

  private HorizontalLayout buildTreeCell(Object item) {
    Icon icon;
    String label;
    if (item instanceof XincoCoreNodeServer node) {
      icon = VaadinIcon.FOLDER.create();
      icon.getStyle().set("color", "var(--lumo-primary-color)");
      label = node.getDesignation();
    } else if (item instanceof XincoCoreDataServer data) {
      icon = dataTypeIcon(data);
      label = data.getDesignation();
    } else {
      return new HorizontalLayout(new Span("?"));
    }
    icon.setSize("1em");
    HorizontalLayout row = new HorizontalLayout(icon, new Span(label));
    row.setAlignItems(FlexComponent.Alignment.CENTER);
    row.getStyle().set("gap", "var(--lumo-space-xs)");
    row.setSpacing(false);
    return row;
  }

  private Icon dataTypeIcon(XincoCoreDataServer data) {
    int typeId = data.getXincoCoreDataType() != null ? data.getXincoCoreDataType().getId() : 1;
    if (typeId == 2) return VaadinIcon.GLOBE.create();
    if (typeId == 3) return VaadinIcon.FILE_TEXT.create();
    if (typeId == 4) return VaadinIcon.USER.create();
    String name = data.getDesignation().toLowerCase();
    if (name.matches(".*\\.(png|jpg|jpeg|gif|bmp|webp|svg)")) return VaadinIcon.PICTURE.create();
    if (name.matches(".*\\.(mp3|wav|ogg|flac|m4a|aac)")) return VaadinIcon.MUSIC.create();
    if (name.matches(".*\\.(mp4|avi|mov|mkv|webm|wmv)")) return VaadinIcon.FILM.create();
    if (name.matches(".*\\.(zip|tar|gz|7z|rar|bz2)")) return VaadinIcon.ARCHIVE.create();
    if (name.matches(".*\\.pdf")) return VaadinIcon.FILE_TEXT.create();
    return VaadinIcon.FILE_O.create();
  }

  // ── Data Grid ─────────────────────────────────────────────────────────────

  private void buildDataGrid() {
    dataGrid
        .addColumn(XincoCoreDataServer::getDesignation)
        .setHeader(getTranslation("general.filename"));
    dataGrid
        .addColumn(d -> d.getXincoCoreLanguage().getSign())
        .setHeader(getTranslation("general.language"));
    dataGrid
        .addColumn(d -> statusLabel(d.getStatusNumber()))
        .setHeader(getTranslation("general.status"));
    dataGrid.setSizeFull();
    dataGrid.addSelectionListener(
        e ->
            e.getFirstSelectedItem()
                .ifPresent(
                    data -> {
                      selectedData = data;
                      data.loadAddAttributes();
                      propertyGrid.setData(data);
                      updateMenuState();
                      if (searchStatusLabel.isVisible()) {
                        navigateToNode(data.getXincoCoreNodeId(), data);
                      }
                    }));
    // Double-click to download
    dataGrid.addItemDoubleClickListener(
        e -> {
          selectedData = e.getItem();
          if (selectedData.getXincoCoreDataType().getId() == 1) {
            downloadSelected();
          }
        });
  }

  private String statusLabel(int status) {
    switch (status) {
      case 1:
        return getTranslation("general.status.open");
      case 2:
        return getTranslation("general.status.locked");
      case 3:
        return getTranslation("general.status.archived");
      case 4:
        return getTranslation("general.status.checkedout");
      case 5:
        return getTranslation("general.status.published");
      default:
        return "Unknown (" + status + ")";
    }
  }

  // ── Navigation ────────────────────────────────────────────────────────────

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    UserSession s =
        VaadinSession.getCurrent() != null
            ? VaadinSession.getCurrent().getAttribute(UserSession.class)
            : null;
    if (s == null || !s.isLoggedIn()) {
      event.rerouteTo(LoginView.class);
    } else {
      session = s;
    }
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    loadRootNodes();
  }

  // ── Data loading ──────────────────────────────────────────────────────────

  private void loadRootNodes() {
    selectedNode = null;
    selectedData = null;
    dataGrid.setItems(List.of());
    propertyGrid.setData(null);
    updateMenuState();
    try {
      XincoCoreNodeServer root = new XincoCoreNodeServer(1);
      root.fillXincoCoreNodes();
      List<Object> roots = new ArrayList<>();
      roots.add(root);
      nodeTree.setItems(roots, this::getChildNodes);
    } catch (Throwable e) {
      error("Failed to load nodes: " + e.getMessage());
    }
  }

  private List<Object> getChildNodes(Object item) {
    if (!(item instanceof XincoCoreNodeServer parent)) return List.of();
    parent.fillXincoCoreNodes();
    return new ArrayList<>(parent.getXincoCoreNodes());
  }

  private void navigateToNode(int targetNodeId, XincoCoreDataServer dataToReselect) {
    // Build path of node IDs from root's children down to targetNodeId
    List<Integer> path = new ArrayList<>();
    try {
      int current = targetNodeId;
      while (current > 1) {
        path.add(0, current);
        XincoCoreNodeServer n = new XincoCoreNodeServer(current);
        current = n.getXincoCoreNodeId();
      }
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Failed to build node path", ex);
      return;
    }

    try {
      XincoCoreNodeServer root = new XincoCoreNodeServer(1);
      root.fillXincoCoreNodes();
      nodeTree.setItems(new ArrayList<>(List.<Object>of(root)), this::getChildNodes);

      // Walk path: after expanding a node, getChildNodes populates its getXincoCoreNodes()
      XincoCoreNodeServer current = root;
      for (int stepId : path) {
        nodeTree.expand(List.<Object>of(current));
        XincoCoreNodeServer child =
            current.getXincoCoreNodes().stream()
                .filter(o -> o instanceof XincoCoreNodeServer)
                .map(o -> (XincoCoreNodeServer) o)
                .filter(n -> n.getId() == stepId)
                .findFirst()
                .orElse(null);
        if (child == null) return;
        current = child;
      }

      // Select the target node — triggers tree selection listener which populates dataGrid
      nodeTree.select(current);
      selectedNode = current;

      // Clear search mode now that we've navigated
      searchStatusLabel.setVisible(false);
      clearSearchBtn.setVisible(false);

      // Re-select the originating data item in the now-refreshed data grid
      if (dataToReselect != null) {
        final int dataId = dataToReselect.getId();
        current.getXincoCoreData().stream()
            .filter(o -> o instanceof XincoCoreDataServer)
            .map(o -> (XincoCoreDataServer) o)
            .filter(d -> d.getId() == dataId)
            .findFirst()
            .ifPresent(
                d -> {
                  dataGrid.select(d);
                  selectedData = d;
                  updateMenuState();
                });
      }
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Navigation to node failed", ex);
    }
  }

  // ── Search ────────────────────────────────────────────────────────────────

  private HorizontalLayout buildSearchBar() {
    searchField.setPlaceholder(getTranslation("menu.search"));
    searchField.setClearButtonVisible(true);
    searchField.setWidth("280px");
    searchField.addKeyPressListener(Key.ENTER, e -> doSearch());

    Button searchBtn = new Button(VaadinIcon.SEARCH.create(), e -> doSearch());
    searchBtn.setTooltipText(getTranslation("menu.search"));

    Select<XincoCoreLanguageServer> langFilter = new Select<>();
    langFilter.setLabel(getTranslation("general.language"));
    langFilter.setWidth("160px");
    try {
      List<XincoCoreLanguageServer> langs =
          XincoCoreLanguageServer.getXincoCoreLanguages().stream()
              .filter(o -> o instanceof XincoCoreLanguageServer)
              .map(o -> (XincoCoreLanguageServer) o)
              .toList();
      langFilter.setItems(langs);
      langFilter.setItemLabelGenerator(XincoCoreLanguageServer::getDesignation);
    } catch (Exception ex) {
      LOG.log(Level.WARNING, "Could not load languages for search filter", ex);
    }
    langFilter.addValueChangeListener(
        e -> searchLangId = e.getValue() != null ? e.getValue().getId() : 0);

    clearSearchBtn = new Button(VaadinIcon.CLOSE_SMALL.create(), e -> clearSearch());
    clearSearchBtn.setTooltipText(getTranslation("general.reset"));
    clearSearchBtn.setVisible(false);

    searchStatusLabel = new Span();
    searchStatusLabel
        .getStyle()
        .set("font-size", "var(--lumo-font-size-s)")
        .set("color", "var(--lumo-secondary-text-color)");
    searchStatusLabel.setVisible(false);

    HorizontalLayout bar =
        new HorizontalLayout(searchField, langFilter, searchBtn, clearSearchBtn, searchStatusLabel);
    bar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
    bar.setPadding(false);
    return bar;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void doSearch() {
    String query = searchField.getValue().trim();
    if (query.isEmpty()) return;

    java.util.ArrayList raw = searcher.apply(query);
    if (raw == null) {
      error("Search unavailable — index may not exist yet.");
      return;
    }

    List<XincoCoreDataServer> results =
        raw.stream()
            .filter(o -> o instanceof XincoCoreDataServer)
            .map(o -> (XincoCoreDataServer) o)
            .toList();

    dataGrid.setItems(results);
    searchStatusLabel.setText(results.size() + " result(s) for \"" + query + "\"");
    searchStatusLabel.setVisible(true);
    clearSearchBtn.setVisible(true);
    selectedData = null;
    updateMenuState();
  }

  private void clearSearch() {
    searchField.clear();
    searchStatusLabel.setVisible(false);
    clearSearchBtn.setVisible(false);
    if (selectedNode != null) {
      selectedNode.fillXincoCoreData();
      dataGrid.setItems(
          selectedNode.getXincoCoreData().stream()
              .filter(o -> o instanceof XincoCoreDataServer)
              .map(o -> (XincoCoreDataServer) o)
              .toList());
    } else {
      dataGrid.setItems(List.of());
    }
    selectedData = null;
    updateMenuState();
  }

  // ── Actions ───────────────────────────────────────────────────────────────

  private void openVersionHistoryDialog() {
    if (selectedData == null) return;
    XincoCoreDataServer data;
    try {
      data = new XincoCoreDataServer(selectedData.getId());
    } catch (Throwable ex) {
      error("Could not load data: " + ex.getMessage());
      return;
    }

    record LogRow(
        XincoCoreLogServer log,
        String version,
        String operation,
        String description,
        boolean hasFile) {}

    List<LogRow> rows = new ArrayList<>();
    for (Object obj : data.getXincoCoreLogs()) {
      XincoCoreLogServer log = (XincoCoreLogServer) obj;
      var v = log.getVersion();
      String ver =
          v.getVersionHigh()
              + "."
              + v.getVersionMid()
              + "."
              + v.getVersionLow()
              + (v.getVersionPostfix() != null && !v.getVersionPostfix().isBlank()
                  ? "-" + v.getVersionPostfix()
                  : "");
      String op =
          log.getOpCode() > 0 && log.getOpCode() <= OPCode.values().length
              ? getTranslation(OPCode.values()[log.getOpCode() - 1].getName())
              : String.valueOf(log.getOpCode());
      String path =
          XincoCoreDataServer.getXincoCoreDataPath(
              XincoDBManager.CONFIG.fileRepositoryPath,
              data.getId(),
              data.getId() + "-" + log.getId());
      boolean hasFile = new File(path).exists();
      rows.add(new LogRow(log, ver, op, log.getOpDescription(), hasFile));
    }

    Grid<LogRow> grid = new Grid<>();
    grid.addColumn(LogRow::version).setHeader(getTranslation("general.version")).setAutoWidth(true);
    grid.addColumn(LogRow::operation).setHeader("Op").setAutoWidth(true);
    grid.addColumn(LogRow::description)
        .setHeader(getTranslation("general.description"))
        .setFlexGrow(1);
    grid.addComponentColumn(
            row -> {
              if (!row.hasFile()) return new Span();
              Button btn = new Button(getTranslation("menu.edit.downloadrevision"));
              btn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
              btn.addClickListener(
                  e -> downloadVersion(data.getId(), row.log().getId(), data.getDesignation()));
              return btn;
            })
        .setHeader(getTranslation("menu.repository.downloadfile"))
        .setAutoWidth(true);
    grid.setItems(rows);
    grid.setHeight("320px");

    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(getTranslation("window.revision") + " — " + data.getDesignation());
    dialog.setWidth("680px");
    dialog.add(grid);
    dialog.getFooter().add(new Button(getTranslation("general.cancel"), e -> dialog.close()));
    dialog.open();
  }

  private void downloadVersion(int dataId, int logId, String designation) {
    String path =
        XincoCoreDataServer.getXincoCoreDataPath(
            XincoDBManager.CONFIG.fileRepositoryPath, dataId, dataId + "-" + logId);
    File file = new File(path);
    if (!file.exists()) {
      error("File not found on disk: " + path);
      return;
    }
    StreamResource resource =
        new StreamResource(
            designation,
            () -> {
              try {
                return new FileInputStream(file);
              } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Version download stream error", ex);
                return null;
              }
            });
    Anchor anchor = new Anchor(resource, "");
    anchor.getElement().setAttribute("download", true);
    anchor.getStyle().set("position", "fixed").set("top", "-9999px").set("left", "-9999px");
    add(anchor);
    anchor.getElement().callJsFunction("click");
    UI.getCurrent().getPage().executeJs("setTimeout(() => $0.remove(), 5000)", anchor.getElement());
  }

  private void cutSelected() {
    clipboardNode = null;
    clipboardData = null;
    if (selectedData != null) {
      clipboardData = selectedData;
    } else if (selectedNode != null) {
      clipboardNode = selectedNode;
    }
    if (clipboardNode != null || clipboardData != null) {
      Notification.show(getTranslation("menu.edit.movemessage"))
          .addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }
    updateMenuState();
  }

  private void pasteClipboard() {
    if (selectedNode == null) return;
    int targetNodeId = selectedNode.getId();
    int userId = session.getUser() != null ? session.getUser().getId() : 1;

    if (clipboardData != null) {
      XincoCoreDataServer toMove = clipboardData;
      clipboardData = null;
      try {
        XincoCoreDataServer fresh = new XincoCoreDataServer(toMove.getId());
        if (fresh.getXincoCoreNodeId() == targetNodeId) {
          updateMenuState();
          return;
        }
        fresh.setXincoCoreNodeId(targetNodeId);
        fresh.setChangerID(userId);
        fresh.write2DB();

        XMLGregorianCalendar now =
            DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
        new XincoCoreLogServerBuilder()
            .setXincoCoreDataId(fresh.getId())
            .setXincoCoreUserId(userId)
            .setOpCode(OPCode.DATA_MOVE.ordinal() + 1)
            .setOperationDescription(getTranslation("datawizard.logging.move"))
            .setVersionHigh(
                fresh.getXincoCoreLogs().isEmpty()
                    ? 1
                    : ((XincoCoreLogServer)
                            fresh.getXincoCoreLogs().get(fresh.getXincoCoreLogs().size() - 1))
                        .getVersion()
                        .getVersionHigh())
            .setVersionMid(0)
            .setVersionLow(0)
            .setVersionPostFix("")
            .createXincoCoreLogServer()
            .write2DB();

        refreshDataGrid();
        Notification.show(getTranslation("menu.edit.movedatasuccess"))
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      } catch (Exception ex) {
        LOG.log(Level.SEVERE, "Move data failed", ex);
        error(getTranslation("error.movedatafailed"));
      }
    } else if (clipboardNode != null) {
      XincoCoreNodeServer toMove = clipboardNode;
      clipboardNode = null;
      try {
        if (toMove.getId() == targetNodeId) {
          updateMenuState();
          return;
        }
        XincoCoreNodeServer fresh = new XincoCoreNodeServer(toMove.getId());
        fresh.setXincoCoreNodeId(targetNodeId);
        fresh.setChangerID(userId);
        fresh.write2DB();

        loadRootNodes();
        Notification.show(getTranslation("menu.edit.movefoldersuccess"))
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      } catch (Exception ex) {
        LOG.log(Level.SEVERE, "Move folder failed", ex);
        error(getTranslation("error.movefolderfailed"));
      }
    }
    updateMenuState();
  }

  private void downloadSelected() {
    if (selectedData == null) return;
    try {
      String path = XincoCoreDataServer.getLastMajorVersionDataPath(selectedData.getId());
      File file = new File(path);
      if (!file.exists()) {
        error("File not found on disk: " + path);
        return;
      }
      String filename = selectedData.getDesignation();
      StreamResource resource =
          new StreamResource(
              filename,
              () -> {
                try {
                  return new FileInputStream(file);
                } catch (Exception ex) {
                  LOG.log(Level.SEVERE, "Download stream error", ex);
                  return null;
                }
              });
      Anchor anchor = new Anchor(resource, "");
      anchor.getElement().setAttribute("download", true);
      anchor.getStyle().set("position", "fixed").set("top", "-9999px").set("left", "-9999px");
      add(anchor);
      anchor.getElement().callJsFunction("click");
      UI.getCurrent()
          .getPage()
          .executeJs("setTimeout(() => $0.remove(), 5000)", anchor.getElement());
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Download failed", e);
      error("Download failed: " + e.getMessage());
    }
  }

  private void openAddDataDialog() {
    if (selectedNode == null) return;

    // ── Type selector ────────────────────────────────────────────────────────
    Select<Integer> typeSelect = new Select<>();
    typeSelect.setLabel(getTranslation("general.datatype"));
    typeSelect.setItems(1, 2, 3, 4);
    typeSelect.setItemLabelGenerator(
        id ->
            switch (id) {
              case 1 -> getTranslation("general.data.type.file");
              case 2 -> getTranslation("general.data.type.text");
              case 3 -> getTranslation("general.data.type.URL");
              case 4 -> getTranslation("general.data.type.contact");
              default -> String.valueOf(id);
            });
    typeSelect.setValue(1);
    typeSelect.setWidthFull();

    // ── Shared fields ────────────────────────────────────────────────────────
    TextField designationField = new TextField(getTranslation("general.filename"));
    designationField.setWidthFull();
    designationField.setRequired(true);

    List<XincoCoreLanguageServer> languages;
    try {
      languages = XincoCoreLanguageServer.getXincoCoreLanguages();
    } catch (Exception e) {
      languages = List.of();
    }
    Select<XincoCoreLanguageServer> langSelect = new Select<>();
    langSelect.setLabel(getTranslation("general.language"));
    langSelect.setItems(languages);
    langSelect.setItemLabelGenerator(l -> l.getSign() + " – " + l.getDesignation());
    langSelect.setWidthFull();
    if (!languages.isEmpty()) {
      langSelect.setValue(languages.get(0));
    }

    // ── Type-specific field sets ─────────────────────────────────────────────
    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setMaxFiles(1);
    upload.setWidthFull();
    upload.addSucceededListener(
        e -> {
          if (designationField.isEmpty()) {
            designationField.setValue(e.getFileName());
          }
        });

    com.vaadin.flow.component.textfield.TextArea textContent =
        new com.vaadin.flow.component.textfield.TextArea(getTranslation("general.description"));
    textContent.setWidthFull();
    textContent.setHeight("120px");

    TextField urlField = new TextField(getTranslation("general.data.type.URL"));
    urlField.setWidthFull();
    urlField.setPlaceholder("https://");

    TextField cSalutation = new TextField(getTranslation("general.salutation"));
    TextField cFirstName = new TextField(getTranslation("general.name"));
    TextField cMiddleName = new TextField(getTranslation("general.middle_name"));
    TextField cLastName = new TextField(getTranslation("general.last_name"));
    TextField cAffix = new TextField(getTranslation("general.name_affix"));
    TextField cPhoneBusiness = new TextField(getTranslation("general.phone_business"));
    TextField cPhonePrivate = new TextField(getTranslation("general.phone_private"));
    TextField cPhoneMobile = new TextField(getTranslation("general.phone_mobile"));
    TextField cFax = new TextField(getTranslation("general.fax"));
    TextField cEmail = new TextField(getTranslation("general.email"));
    TextField cWebsite = new TextField(getTranslation("general.website"));
    TextField cStreet = new TextField(getTranslation("general.steet_address"));
    TextField cPostal = new TextField(getTranslation("general.postal_code"));
    TextField cCity = new TextField(getTranslation("general.city"));
    TextField cState = new TextField(getTranslation("general.state_province"));
    TextField cCountry = new TextField(getTranslation("general.country"));
    TextField cCompany = new TextField(getTranslation("general.company_name"));
    TextField cPosition = new TextField(getTranslation("general.position"));

    FormLayout contactForm = new FormLayout();
    contactForm.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("280px", 2));
    contactForm.add(
        cSalutation,
        cFirstName,
        cMiddleName,
        cLastName,
        cAffix,
        cCompany,
        cPosition,
        cPhoneBusiness,
        cPhonePrivate,
        cPhoneMobile,
        cFax);
    contactForm.add(cEmail);
    contactForm.setColspan(cEmail, 2);
    contactForm.add(cWebsite);
    contactForm.setColspan(cWebsite, 2);
    contactForm.add(cStreet);
    contactForm.setColspan(cStreet, 2);
    contactForm.add(cPostal, cCity, cState, cCountry);
    com.vaadin.flow.component.html.Div contactScroll =
        new com.vaadin.flow.component.html.Div(contactForm);
    contactScroll.getStyle().set("max-height", "260px").set("overflow-y", "auto");

    // ── Dynamic section ──────────────────────────────────────────────────────
    VerticalLayout typeFields = new VerticalLayout(upload);
    typeFields.setPadding(false);
    typeFields.setSpacing(false);

    typeSelect.addValueChangeListener(
        e -> {
          typeFields.removeAll();
          switch (e.getValue()) {
            case 1 -> typeFields.add(upload);
            case 2 -> typeFields.add(textContent);
            case 3 -> typeFields.add(urlField);
            case 4 -> typeFields.add(contactScroll);
          }
        });

    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(getTranslation("menu.repository.adddata"));
    dialog.setWidth("480px");
    dialog.add(new VerticalLayout(typeSelect, designationField, langSelect, typeFields));

    final List<XincoCoreLanguageServer> finalLanguages = languages;
    Button addBtn =
        new Button(
            getTranslation("general.add"),
            e -> {
              int type = typeSelect.getValue() != null ? typeSelect.getValue() : 1;
              XincoCoreLanguageServer lang =
                  langSelect.getValue() != null && !finalLanguages.isEmpty()
                      ? langSelect.getValue()
                      : null;
              if (type == 1) {
                doAddData(designationField, buffer, lang, dialog);
              } else if (type == 4) {
                Map<Integer, String> attrs = new LinkedHashMap<>();
                attrs.put(1, cSalutation.getValue());
                attrs.put(2, cFirstName.getValue());
                attrs.put(3, cMiddleName.getValue());
                attrs.put(4, cLastName.getValue());
                attrs.put(5, cAffix.getValue());
                attrs.put(6, cPhoneBusiness.getValue());
                attrs.put(7, cPhonePrivate.getValue());
                attrs.put(8, cPhoneMobile.getValue());
                attrs.put(9, cFax.getValue());
                attrs.put(10, cEmail.getValue());
                attrs.put(11, cWebsite.getValue());
                attrs.put(12, cStreet.getValue());
                attrs.put(13, cPostal.getValue());
                attrs.put(14, cCity.getValue());
                attrs.put(15, cState.getValue());
                attrs.put(16, cCountry.getValue());
                attrs.put(17, cCompany.getValue());
                attrs.put(18, cPosition.getValue());
                doAddContactData(designationField, lang, attrs, dialog);
              } else {
                String content =
                    switch (type) {
                      case 2 -> textContent.getValue();
                      case 3 -> urlField.getValue();
                      default -> "";
                    };
                doAddNonFileData(type, designationField, lang, content, dialog);
              }
            });

    dialog
        .getFooter()
        .add(new Button(getTranslation("general.cancel"), e -> dialog.close()), addBtn);
    dialog.open();
  }

  void doAddNonFileData(
      int typeId,
      TextField designationField,
      XincoCoreLanguageServer lang,
      String typeContent,
      Dialog dialog) {
    String name = designationField.getValue().trim();
    if (name.isEmpty()) {
      designationField.setErrorMessage(getTranslation("message.missing.designation"));
      designationField.setInvalid(true);
      return;
    }
    if (lang == null) {
      error(getTranslation("message.missing.language"));
      return;
    }
    try {
      XincoCoreDataServer newData =
          new XincoCoreDataServer(0, selectedNode.getId(), lang.getId(), typeId, name, 1);
      newData.write2DB();
      int dataId = newData.getId();

      var log =
          new XincoCoreLogServerBuilder()
              .setXincoCoreDataId(dataId)
              .setXincoCoreUserId(session.getUser().getId())
              .setOpCode(OPCode.CREATION.ordinal() + 1)
              .setOperationDescription(getTranslation("general.create"))
              .setVersionHigh(1)
              .setVersionMid(0)
              .setVersionLow(0)
              .setVersionPostFix("")
              .createXincoCoreLogServer();
      log.write2DB();

      XMLGregorianCalendar now =
          DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
      String content = typeContent != null ? typeContent : "";
      // type=2 (Text): attribText; type=3 (URL): attribVarchar; type=4 (Contact): attribVarchar
      if (typeId == 2) {
        new XincoAddAttributeServer(dataId, 1, 0, 0L, 0.0, "", content, now).write2DB();
      } else if (typeId == 3 || typeId == 4) {
        new XincoAddAttributeServer(dataId, 1, 0, 0L, 0.0, content, "", now).write2DB();
      }

      dialog.close();
      refreshDataGrid();
      Notification.show(getTranslation("general.save") + " OK")
          .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Add data failed", ex);
      error("Add data failed: " + ex.getMessage());
    }
  }

  void doAddContactData(
      TextField designationField,
      XincoCoreLanguageServer lang,
      Map<Integer, String> attrs,
      Dialog dialog) {
    String name = designationField.getValue().trim();
    if (name.isEmpty()) {
      designationField.setErrorMessage(getTranslation("message.missing.designation"));
      designationField.setInvalid(true);
      return;
    }
    if (lang == null) {
      error(getTranslation("message.missing.language"));
      return;
    }
    try {
      XincoCoreDataServer newData =
          new XincoCoreDataServer(0, selectedNode.getId(), lang.getId(), 4, name, 1);
      newData.write2DB();
      int dataId = newData.getId();

      var log =
          new XincoCoreLogServerBuilder()
              .setXincoCoreDataId(dataId)
              .setXincoCoreUserId(session.getUser().getId())
              .setOpCode(OPCode.CREATION.ordinal() + 1)
              .setOperationDescription(getTranslation("general.create"))
              .setVersionHigh(1)
              .setVersionMid(0)
              .setVersionLow(0)
              .setVersionPostFix("")
              .createXincoCoreLogServer();
      log.write2DB();

      XMLGregorianCalendar now =
          DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
      for (Map.Entry<Integer, String> entry : attrs.entrySet()) {
        new XincoAddAttributeServer(dataId, entry.getKey(), 0, 0L, 0.0, entry.getValue(), "", now)
            .write2DB();
      }

      dialog.close();
      refreshDataGrid();
      Notification.show(getTranslation("general.save") + " OK")
          .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Add contact data failed", ex);
      error("Add data failed: " + ex.getMessage());
    }
  }

  void doAddData(
      TextField designationField,
      MemoryBuffer buffer,
      XincoCoreLanguageServer lang,
      Dialog dialog) {
    String name = designationField.getValue().trim();
    if (name.isEmpty()) {
      designationField.setErrorMessage(getTranslation("message.missing.designation"));
      designationField.setInvalid(true);
      return;
    }
    if (buffer.getFileName() == null || buffer.getFileName().isEmpty()) {
      error("Please upload a file first.");
      return;
    }
    if (lang == null) {
      error("Please select a language.");
      return;
    }
    try {
      XincoCoreDataServer newData =
          new XincoCoreDataServer(0, selectedNode.getId(), lang.getId(), 1, name, 1);
      newData.write2DB();
      int dataId = newData.getId();

      var log =
          new XincoCoreLogServerBuilder()
              .setXincoCoreDataId(dataId)
              .setXincoCoreUserId(session.getUser().getId())
              .setOpCode(OPCode.CREATION.ordinal() + 1)
              .setOperationDescription("Initial upload")
              .setVersionHigh(1)
              .setVersionMid(0)
              .setVersionLow(0)
              .setVersionPostFix("")
              .createXincoCoreLogServer();
      log.write2DB();
      int logId = log.getId();

      String repoPath =
          XincoCoreDataServer.getXincoCoreDataPath(
              XincoDBManager.CONFIG.fileRepositoryPath, dataId, dataId + "-" + logId);
      File repoFile = new File(repoPath);
      repoFile.getParentFile().mkdirs();
      try (InputStream in = buffer.getInputStream()) {
        Files.copy(in, repoFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
      String basePath =
          XincoCoreDataServer.getXincoCoreDataPath(
              XincoDBManager.CONFIG.fileRepositoryPath, dataId, "" + dataId);
      Files.copy(
          repoFile.toPath(), new File(basePath).toPath(), StandardCopyOption.REPLACE_EXISTING);

      XMLGregorianCalendar now =
          DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
      String filename = buffer.getFileName();
      long filesize = repoFile.length();
      new XincoAddAttributeServer(dataId, 1, 0, 0L, 0.0, filename, "", now).write2DB();
      new XincoAddAttributeServer(dataId, 2, 0, filesize, 0.0, "", "", now).write2DB();
      new XincoAddAttributeServer(dataId, 3, 0, 0L, 0.0, "", "", now).write2DB();
      new XincoAddAttributeServer(dataId, 4, 0, 1L, 0.0, "", "", now).write2DB();
      for (int i = 5; i <= 12; i++) {
        new XincoAddAttributeServer(dataId, i, 0, 0L, 0.0, "", "", now).write2DB();
      }

      dialog.close();
      selectedNode.fillXincoCoreData();
      dataGrid.setItems(
          selectedNode.getXincoCoreData().stream()
              .filter(o -> o instanceof XincoCoreDataServer)
              .map(o -> (XincoCoreDataServer) o)
              .toList());
      nodeTree.collapse(List.<Object>of(selectedNode));
      nodeTree.expand(List.<Object>of(selectedNode));
      Notification.show(getTranslation("datawizard.fileuploadsuccess"))
          .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Add data failed", ex);
      error("Failed to add data: " + ex.getMessage());
    }
  }

  private void openNewFolderDialog() {
    if (selectedNode == null) return;
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(getTranslation("general.newfolder"));
    TextField nameField = new TextField(getTranslation("general.folder"));
    nameField.setWidthFull();
    nameField.setAutofocus(true);
    dialog.add(nameField);

    dialog
        .getFooter()
        .add(
            new Button(getTranslation("general.cancel"), e -> dialog.close()),
            new Button(getTranslation("general.create"), e -> doCreateFolder(nameField, dialog)));
    dialog.open();
  }

  void doCreateFolder(TextField nameField, Dialog dialog) {
    String name = nameField.getValue().trim();
    if (name.isEmpty()) {
      nameField.setErrorMessage(getTranslation("message.missing.designation"));
      nameField.setInvalid(true);
      return;
    }
    try {
      XincoCoreNodeServer newNode =
          new XincoCoreNodeServer(
              0, selectedNode.getId(), selectedNode.getXincoCoreLanguage().getId(), name, 1);
      newNode.write2DB();
      dialog.close();
      loadRootNodes();
      Notification.show(getTranslation("window.folder.updatesuccess"))
          .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    } catch (XincoException ex) {
      LOG.log(Level.SEVERE, "Create folder failed", ex);
      error("Could not create folder: " + ex.getMessage());
    }
  }

  private void confirmDelete() {
    String target =
        selectedData != null
            ? "data item '" + selectedData.getDesignation() + "'"
            : selectedNode != null ? "folder '" + selectedNode.getDesignation() + "'" : null;
    if (target == null) return;

    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader("Move to Trash?");
    confirm.setText("Move " + target + " to the Trash folder?");
    confirm.setCancelable(true);
    confirm.setConfirmText("Move to Trash");
    confirm.setConfirmButtonTheme("error primary");
    XincoCoreDataServer dataToDelete = selectedData;
    XincoCoreNodeServer nodeToDelete = selectedNode;
    confirm.addConfirmListener(
        e -> {
          try {
            if (dataToDelete != null) {
              if (dataToDelete.getStatusNumber() == 4) {
                error("Cannot delete a checked-out item.");
                return;
              }
              moveDataToTrash(dataToDelete);
              selectedData = null;
            } else if (nodeToDelete != null) {
              moveNodeToTrash(nodeToDelete);
              selectedNode = null;
            }
            loadRootNodes();
            Notification.show("Moved to Trash.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
          } catch (XincoException ex) {
            LOG.log(Level.SEVERE, "Move to Trash failed", ex);
            error("Move to Trash failed: " + ex.getMessage());
          }
        });
    confirm.open();
  }

  private void moveDataToTrash(XincoCoreDataServer data) throws XincoException {
    XincoCoreDataServer fresh = new XincoCoreDataServer(data.getId());
    fresh.setXincoCoreNodeId(TRASH_NODE_ID);
    fresh.setChangerID(session.getUser() != null ? session.getUser().getId() : 1);
    fresh.write2DB();
  }

  private void moveNodeToTrash(XincoCoreNodeServer node) throws XincoException {
    XincoCoreNodeServer fresh = new XincoCoreNodeServer(node.getId());
    fresh.setXincoCoreNodeId(TRASH_NODE_ID);
    fresh.setChangerID(session.getUser() != null ? session.getUser().getId() : 1);
    fresh.write2DB();
  }

  private void checkoutSelected() {
    if (selectedData == null) return;
    showReasonDialog(
        getTranslation("menu.edit.checkoutfile"),
        getTranslation("menu.edit.checkoutfile"),
        reason -> {
          try {
            XincoCoreDataServer data = new XincoCoreDataServer(selectedData.getId());
            XincoCoreLogServer lastLog =
                data.getXincoCoreLogs().isEmpty()
                    ? null
                    : (XincoCoreLogServer)
                        data.getXincoCoreLogs().get(data.getXincoCoreLogs().size() - 1);
            int vh = lastLog != null ? lastLog.getVersion().getVersionHigh() : 1;
            int vm = lastLog != null ? lastLog.getVersion().getVersionMid() : 0;
            int vl = lastLog != null ? lastLog.getVersion().getVersionLow() : 0;
            String vp =
                lastLog != null && lastLog.getVersion().getVersionPostfix() != null
                    ? lastLog.getVersion().getVersionPostfix()
                    : "";

            var log =
                new XincoCoreLogServerBuilder()
                    .setXincoCoreDataId(data.getId())
                    .setXincoCoreUserId(session.getUser().getId())
                    .setOpCode(OPCode.CHECKOUT.ordinal() + 1)
                    .setOperationDescription(reason)
                    .setVersionHigh(vh)
                    .setVersionMid(vm)
                    .setVersionLow(vl)
                    .setVersionPostFix(vp)
                    .createXincoCoreLogServer();
            log.write2DB();

            data.setStatusNumber(4);
            data.write2DB();

            selectedData = data;
            downloadSelected();
            refreshDataGrid();
            Notification.show(getTranslation("menu.edit.checkoutfile") + " OK")
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
          } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Checkout failed", ex);
            error("Checkout failed: " + ex.getMessage());
          }
        });
  }

  private void openCheckinDialog() {
    if (selectedData == null) return;

    // Load current version from last log to pre-populate the version fields
    int curHigh = 1, curMid = 0, curLow = 0;
    String curPostfix = "";
    try {
      XincoCoreDataServer snap = new XincoCoreDataServer(selectedData.getId());
      if (!snap.getXincoCoreLogs().isEmpty()) {
        XincoCoreLogServer last =
            (XincoCoreLogServer) snap.getXincoCoreLogs().get(snap.getXincoCoreLogs().size() - 1);
        curHigh = last.getVersion().getVersionHigh();
        curMid = last.getVersion().getVersionMid();
        curLow = last.getVersion().getVersionLow();
        curPostfix =
            last.getVersion().getVersionPostfix() == null
                ? ""
                : last.getVersion().getVersionPostfix();
      }
    } catch (Throwable ignored) {
    }
    final int basHigh = curHigh, basMid = curMid, basLow = curLow;

    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setMaxFiles(1);
    upload.setWidthFull();

    TextField descField = new TextField(getTranslation("general.description"));
    descField.setWidthFull();

    // Version fields — default to major bump (high+1, mid=0, low=0)
    IntegerField majorField = new IntegerField("Major");
    majorField.setValue(basHigh + 1);
    majorField.setMin(0);
    majorField.setStepButtonsVisible(true);
    majorField.setWidth("90px");

    IntegerField minorField = new IntegerField(getTranslation("general.minor"));
    minorField.setValue(0);
    minorField.setMin(0);
    minorField.setStepButtonsVisible(true);
    minorField.setWidth("90px");

    IntegerField patchField = new IntegerField("Patch");
    patchField.setValue(0);
    patchField.setMin(0);
    patchField.setStepButtonsVisible(true);
    patchField.setWidth("90px");

    TextField postfixField = new TextField(getTranslation("general.version.postfix"));
    postfixField.setValue(curPostfix);
    postfixField.setWidth("90px");

    Checkbox minorBump = new Checkbox(getTranslation("general.minor"));
    minorBump.addValueChangeListener(
        ev -> {
          if (ev.getValue()) {
            // minor bump: keep major, increment minor, reset patch
            majorField.setValue(basHigh);
            minorField.setValue(basMid + 1);
            patchField.setValue(0);
          } else {
            // major bump (default)
            majorField.setValue(basHigh + 1);
            minorField.setValue(0);
            patchField.setValue(0);
          }
        });

    HorizontalLayout versionRow =
        new HorizontalLayout(majorField, minorField, patchField, postfixField);
    versionRow.setAlignItems(FlexComponent.Alignment.END);

    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(getTranslation("menu.edit.checkinfile"));
    dialog.setWidth("520px");
    dialog.add(new VerticalLayout(upload, descField, minorBump, versionRow));

    dialog
        .getFooter()
        .add(
            new Button(getTranslation("general.cancel"), e -> dialog.close()),
            new Button(
                getTranslation("menu.edit.checkinfile"),
                e -> {
                  int vh = majorField.getValue() != null ? majorField.getValue() : basHigh + 1;
                  int vm = minorField.getValue() != null ? minorField.getValue() : 0;
                  int vl = patchField.getValue() != null ? patchField.getValue() : 0;
                  String vp = postfixField.getValue().trim();
                  String desc =
                      descField.getValue().trim().isEmpty()
                          ? getTranslation("menu.edit.checkinfile")
                          : descField.getValue().trim();
                  doCheckin(buffer, vh, vm, vl, vp, desc, dialog);
                }));
    dialog.open();
  }

  void doCheckin(
      MemoryBuffer buffer, int vh, int vm, int vl, String vp, String desc, Dialog dialog) {
    if (buffer.getFileName() == null || buffer.getFileName().isEmpty()) {
      error("Please upload the revised file first.");
      return;
    }
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(selectedData.getId());

      var log =
          new XincoCoreLogServerBuilder()
              .setXincoCoreDataId(data.getId())
              .setXincoCoreUserId(session.getUser().getId())
              .setOpCode(OPCode.CHECKIN.ordinal() + 1)
              .setOperationDescription(desc)
              .setVersionHigh(vh)
              .setVersionMid(vm)
              .setVersionLow(vl)
              .setVersionPostFix(vp)
              .createXincoCoreLogServer();
      log.write2DB();
      int logId = log.getId();

      String base =
          XincoCoreDataServer.getXincoCoreDataPath(
              XincoDBManager.CONFIG.fileRepositoryPath, data.getId(), "" + data.getId());
      File baseFile = new File(base);
      baseFile.getParentFile().mkdirs();
      try (InputStream in = buffer.getInputStream()) {
        Files.copy(in, baseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      String versionPath =
          XincoCoreDataServer.getXincoCoreDataPath(
              XincoDBManager.CONFIG.fileRepositoryPath, data.getId(), data.getId() + "-" + logId);
      Files.copy(
          baseFile.toPath(), new File(versionPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

      XMLGregorianCalendar now =
          DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
      new XincoAddAttributeServer(data.getId(), 1, 0, 0L, 0.0, buffer.getFileName(), "", now)
          .write2DB();
      new XincoAddAttributeServer(data.getId(), 2, 0, baseFile.length(), 0.0, "", "", now)
          .write2DB();

      data.setStatusNumber(1);
      data.write2DB();

      dialog.close();
      refreshDataGrid();
      Notification.show(getTranslation("menu.edit.checkinfile") + " OK")
          .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "Checkin failed", ex);
      error("Checkin failed: " + ex.getMessage());
    }
  }

  private void undoCheckoutSelected() {
    if (selectedData == null) return;
    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader(getTranslation("menu.edit.undocheckout") + "?");
    confirm.setText(
        "Local changes will be discarded. The file reverts to the last checked-in version.");
    confirm.setCancelable(true);
    confirm.setConfirmText(getTranslation("menu.edit.undocheckout"));
    confirm.addConfirmListener(
        e -> {
          try {
            XincoCoreDataServer data = new XincoCoreDataServer(selectedData.getId());
            XincoCoreLogServer lastLog =
                data.getXincoCoreLogs().isEmpty()
                    ? null
                    : (XincoCoreLogServer)
                        data.getXincoCoreLogs().get(data.getXincoCoreLogs().size() - 1);
            int vh = lastLog != null ? lastLog.getVersion().getVersionHigh() : 1;
            int vm = lastLog != null ? lastLog.getVersion().getVersionMid() : 0;
            int vl = lastLog != null ? lastLog.getVersion().getVersionLow() : 0;
            String vp =
                lastLog != null && lastLog.getVersion().getVersionPostfix() != null
                    ? lastLog.getVersion().getVersionPostfix()
                    : "";

            var log =
                new XincoCoreLogServerBuilder()
                    .setXincoCoreDataId(data.getId())
                    .setXincoCoreUserId(session.getUser().getId())
                    .setOpCode(OPCode.CHECKOUT_UNDONE.ordinal() + 1)
                    .setOperationDescription(getTranslation("menu.edit.undocheckout"))
                    .setVersionHigh(vh)
                    .setVersionMid(vm)
                    .setVersionLow(vl)
                    .setVersionPostFix(vp)
                    .createXincoCoreLogServer();
            log.write2DB();

            data.setStatusNumber(1);
            data.write2DB();

            refreshDataGrid();
            Notification.show(getTranslation("menu.edit.undocheckout") + " OK")
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
          } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Undo checkout failed", ex);
            error("Undo checkout failed: " + ex.getMessage());
          }
        });
    confirm.open();
  }

  private void lockSelected() {
    if (selectedData == null) return;
    showReasonDialog(
        getTranslation("menu.edit.lockdata"),
        getTranslation("menu.edit.lockdata"),
        reason -> {
          try {
            XincoCoreDataServer data = new XincoCoreDataServer(selectedData.getId());
            XincoCoreLogServer lastLog =
                data.getXincoCoreLogs().isEmpty()
                    ? null
                    : (XincoCoreLogServer)
                        data.getXincoCoreLogs().get(data.getXincoCoreLogs().size() - 1);
            int vh = lastLog != null ? lastLog.getVersion().getVersionHigh() : 1;
            int vm = lastLog != null ? lastLog.getVersion().getVersionMid() : 0;
            int vl = lastLog != null ? lastLog.getVersion().getVersionLow() : 0;
            String vp =
                lastLog != null && lastLog.getVersion().getVersionPostfix() != null
                    ? lastLog.getVersion().getVersionPostfix()
                    : "";

            var log =
                new XincoCoreLogServerBuilder()
                    .setXincoCoreDataId(data.getId())
                    .setXincoCoreUserId(session.getUser().getId())
                    .setOpCode(OPCode.LOCK_COMMENT.ordinal() + 1)
                    .setOperationDescription(reason)
                    .setVersionHigh(vh)
                    .setVersionMid(vm)
                    .setVersionLow(vl)
                    .setVersionPostFix(vp)
                    .createXincoCoreLogServer();
            log.write2DB();

            data.setStatusNumber(2);
            data.write2DB();

            refreshDataGrid();
            Notification.show(getTranslation("menu.edit.lockdata") + " OK")
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
          } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Lock failed", ex);
            error("Lock failed: " + ex.getMessage());
          }
        });
  }

  private void publishSelected() {
    if (selectedData == null) return;
    showReasonDialog(
        getTranslation("menu.edit.publishdata"),
        getTranslation("menu.edit.publishdata"),
        reason -> {
          try {
            XincoCoreDataServer data = new XincoCoreDataServer(selectedData.getId());
            XincoCoreLogServer lastLog =
                data.getXincoCoreLogs().isEmpty()
                    ? null
                    : (XincoCoreLogServer)
                        data.getXincoCoreLogs().get(data.getXincoCoreLogs().size() - 1);
            int vh = lastLog != null ? lastLog.getVersion().getVersionHigh() : 1;
            int vm = lastLog != null ? lastLog.getVersion().getVersionMid() : 0;
            int vl = lastLog != null ? lastLog.getVersion().getVersionLow() : 0;
            String vp =
                lastLog != null && lastLog.getVersion().getVersionPostfix() != null
                    ? lastLog.getVersion().getVersionPostfix()
                    : "";

            var log =
                new XincoCoreLogServerBuilder()
                    .setXincoCoreDataId(data.getId())
                    .setXincoCoreUserId(session.getUser().getId())
                    .setOpCode(OPCode.PUBLISH_COMMENT.ordinal() + 1)
                    .setOperationDescription(reason)
                    .setVersionHigh(vh)
                    .setVersionMid(vm)
                    .setVersionLow(vl)
                    .setVersionPostFix(vp)
                    .createXincoCoreLogServer();
            log.write2DB();

            data.setStatusNumber(5);
            data.write2DB();

            refreshDataGrid();
            Notification.show(getTranslation("menu.edit.publishdata") + " OK")
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
          } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Publish failed", ex);
            error("Publish failed: " + ex.getMessage());
          }
        });
  }

  private void showReasonDialog(String title, String confirmText, Consumer<String> onConfirm) {
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(title);
    TextField reasonField = new TextField(getTranslation("general.reason"));
    reasonField.setWidthFull();
    Button cancel = new Button(getTranslation("general.cancel"), e -> dialog.close());
    Button confirm = new Button(confirmText);
    confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    confirm.addClickListener(
        e -> {
          if (reasonField.getValue().isBlank()) {
            Notification.show(getTranslation("message.warning.reason"));
            return;
          }
          dialog.close();
          onConfirm.accept(reasonField.getValue());
        });
    dialog.add(new VerticalLayout(reasonField));
    dialog.getFooter().add(cancel, confirm);
    dialog.open();
  }

  private void archiveSelected() {
    if (selectedData == null || selectedData.getXincoCoreDataType().getId() != 1) return;
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(selectedData.getId());
      openArchiveDialog(data);
    } catch (Throwable ex) {
      error("Could not load data: " + ex.getMessage());
    }
  }

  void openArchiveDialog(XincoCoreDataServer data) {
    // Pre-populate from existing addAttributes (attributeId 5=model, 6=date, 7=days)
    int currentModel = 0;
    LocalDate currentDate = LocalDate.now().plusDays(30);
    int currentDays = 30;
    for (var attr : data.getXincoAddAttributes()) {
      if (attr.getAttributeId() == 5) currentModel = (int) attr.getAttribUnsignedint();
      if (attr.getAttributeId() == 6 && attr.getAttribDatetime() != null) {
        try {
          currentDate =
              attr.getAttribDatetime().toGregorianCalendar().toZonedDateTime().toLocalDate();
        } catch (Exception ignored) {
        }
      }
      if (attr.getAttributeId() == 7) currentDays = Math.max(1, (int) attr.getAttribUnsignedint());
    }

    Select<Integer> modelSelect = new Select<>();
    modelSelect.setLabel(getTranslation("general.archive.model"));
    modelSelect.setItems(0, 1, 2);
    modelSelect.setItemLabelGenerator(
        m ->
            switch (m) {
              case 1 -> getTranslation("general.archive.date");
              case 2 -> getTranslation("general.archive.days");
              default -> getTranslation("window.archive.archivingmodel.none");
            });
    modelSelect.setValue(currentModel);

    DatePicker archiveDate = new DatePicker(getTranslation("general.archive.date"));
    archiveDate.setValue(currentDate);
    archiveDate.setEnabled(currentModel == 1);

    IntegerField archiveDays = new IntegerField(getTranslation("general.archive.days"));
    archiveDays.setValue(currentDays);
    archiveDays.setMin(1);
    archiveDays.setEnabled(currentModel == 2);

    modelSelect.addValueChangeListener(
        ev -> {
          archiveDate.setEnabled(ev.getValue() == 1);
          archiveDays.setEnabled(ev.getValue() == 2);
        });

    Dialog dialog = new Dialog();
    dialog.setHeaderTitle("Archive — " + data.getDesignation());
    dialog.setWidth("420px");
    dialog.add(new VerticalLayout(modelSelect, archiveDate, archiveDays));

    Button save =
        new Button(
            getTranslation("general.save"),
            e -> {
              try {
                XMLGregorianCalendar now =
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
                int model = modelSelect.getValue();
                new XincoAddAttributeServer(data.getId(), 5, 0, (long) model, 0.0, "", "", now)
                    .write2DB();
                if (model == 1) {
                  LocalDate ld =
                      archiveDate.getValue() != null
                          ? archiveDate.getValue()
                          : LocalDate.now().plusDays(30);
                  GregorianCalendar gc =
                      new GregorianCalendar(
                          ld.getYear(), ld.getMonthValue() - 1, ld.getDayOfMonth());
                  new XincoAddAttributeServer(
                          data.getId(),
                          6,
                          0,
                          0L,
                          0.0,
                          "",
                          "",
                          DatatypeFactory.newInstance().newXMLGregorianCalendar(gc))
                      .write2DB();
                } else if (model == 2) {
                  int days = archiveDays.getValue() != null ? archiveDays.getValue() : 30;
                  new XincoAddAttributeServer(data.getId(), 7, 0, (long) days, 0.0, "", "", now)
                      .write2DB();
                }
                dialog.close();
                String msg =
                    switch (model) {
                      case 1 -> "Archiving scheduled for " + archiveDate.getValue() + ".";
                      case 2 -> "Archiving scheduled after " + archiveDays.getValue() + " days.";
                      default -> "Archive schedule cleared.";
                    };
                Notification.show(msg).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
              } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Archive schedule failed", ex);
                error("Archive failed: " + ex.getMessage());
              }
            });
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button cancel = new Button(getTranslation("general.cancel"), e -> dialog.close());
    dialog.getFooter().add(cancel, save);
    dialog.open();
  }

  private void refreshDataGrid() {
    if (selectedNode != null) {
      selectedNode.fillXincoCoreData();
      dataGrid.setItems(
          selectedNode.getXincoCoreData().stream()
              .filter(o -> o instanceof XincoCoreDataServer)
              .map(o -> (XincoCoreDataServer) o)
              .toList());
    }
    selectedData = null;
    updateMenuState();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void openAclDialog() {
    if (selectedNode == null && selectedData == null) return;

    boolean isData = selectedData != null;
    int targetId = isData ? selectedData.getId() : selectedNode.getId();
    String designation = isData ? selectedData.getDesignation() : selectedNode.getDesignation();
    String aclType = isData ? "xincoCoreData.id" : "xincoCoreNode.id";
    int nodeIdForAce = isData ? 0 : targetId;
    int dataIdForAce = isData ? targetId : 0;

    List<XincoCoreACEServer> acl =
        new ArrayList<>(XincoCoreACEServer.getXincoCoreACL(targetId, aclType));
    List<XincoCoreACEServer> deletedAces = new ArrayList<>();

    List<XincoCoreUserServer> allUsers;
    List<XincoCoreGroupServer> allGroups;
    try {
      allUsers = XincoCoreUserServer.getXincoCoreUsers();
      allGroups = XincoCoreGroupServer.getXincoCoreGroups();
    } catch (Exception e) {
      error("Failed to load subjects: " + e.getMessage());
      return;
    }

    VerticalLayout aceRows = new VerticalLayout();
    aceRows.setPadding(false);
    aceRows.setSpacing(false);
    aceRows.setWidthFull();

    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(getTranslation("window.acl"));
    dialog.setWidth("700px");

    for (XincoCoreACEServer ace : new ArrayList<>(acl)) {
      String name;
      if (ace.getXincoCoreUserId() > 0) {
        int uid = ace.getXincoCoreUserId();
        name =
            getTranslation("general.user")
                + ": "
                + allUsers.stream()
                    .filter(u -> u.getId() == uid)
                    .map(XincoCoreUserServer::getUsername)
                    .findFirst()
                    .orElse("#" + uid);
      } else {
        int gid = ace.getXincoCoreGroupId();
        name =
            getTranslation("general.group")
                + ": "
                + allGroups.stream()
                    .filter(g -> g.getId() == gid)
                    .map(XincoCoreGroupServer::getDesignation)
                    .findFirst()
                    .orElse("#" + gid);
      }

      Checkbox cbAdmin =
          new Checkbox(getTranslation("general.acl.adminpermission"), ace.isAdminPermission());
      cbAdmin.addValueChangeListener(ev -> ace.setAdminPermission(ev.getValue()));
      Checkbox cbRead =
          new Checkbox(getTranslation("general.acl.readpermission"), ace.isReadPermission());
      cbRead.addValueChangeListener(ev -> ace.setReadPermission(ev.getValue()));
      Checkbox cbWrite =
          new Checkbox(getTranslation("general.acl.writepermission"), ace.isWritePermission());
      cbWrite.addValueChangeListener(ev -> ace.setWritePermission(ev.getValue()));
      Checkbox cbExec =
          new Checkbox(getTranslation("general.acl.executepermission"), ace.isExecutePermission());
      cbExec.addValueChangeListener(ev -> ace.setExecutePermission(ev.getValue()));

      HorizontalLayout row = new HorizontalLayout();
      row.setWidthFull();
      row.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
      Span nameSpan = new Span(name);
      nameSpan.getStyle().set("min-width", "180px").set("flex-grow", "1");
      Button delBtn =
          new Button(
              "×",
              ev -> {
                acl.remove(ace);
                deletedAces.add(ace);
                aceRows.remove(row);
              });
      row.add(nameSpan, cbAdmin, cbRead, cbWrite, cbExec, delBtn);
      aceRows.add(row);
    }

    // Build subject list for the add-entry combo
    List<String> subjects = new ArrayList<>();
    Map<String, int[]> subjectIds = new LinkedHashMap<>();
    for (XincoCoreUserServer u : allUsers) {
      int uid = u.getId();
      if (acl.stream().noneMatch(a -> a.getXincoCoreUserId() == uid)) {
        String label = getTranslation("general.user") + ": " + u.getUsername();
        subjects.add(label);
        subjectIds.put(label, new int[] {uid, 0});
      }
    }
    for (XincoCoreGroupServer g : allGroups) {
      int gid = g.getId();
      if (acl.stream().noneMatch(a -> a.getXincoCoreGroupId() == gid)) {
        String label = getTranslation("general.group") + ": " + g.getDesignation();
        subjects.add(label);
        subjectIds.put(label, new int[] {0, gid});
      }
    }

    ComboBox<String> subjectBox = new ComboBox<>(getTranslation("general.add"));
    subjectBox.setItems(subjects);
    subjectBox.setWidth("220px");
    Checkbox newAdmin = new Checkbox(getTranslation("general.acl.adminpermission"));
    Checkbox newRead = new Checkbox(getTranslation("general.acl.readpermission"), true);
    Checkbox newWrite = new Checkbox(getTranslation("general.acl.writepermission"), true);
    Checkbox newExec = new Checkbox(getTranslation("general.acl.executepermission"));

    Button addBtn = new Button(getTranslation("general.add"));
    addBtn.addClickListener(
        ev -> {
          String sel = subjectBox.getValue();
          if (sel == null || !subjectIds.containsKey(sel)) {
            error("Select a subject first.");
            return;
          }
          int[] ids = subjectIds.get(sel);
          try {
            XincoCoreACEServer newAce =
                new XincoCoreACEServer(
                    0,
                    ids[0],
                    ids[1],
                    nodeIdForAce,
                    dataIdForAce,
                    newRead.getValue(),
                    newWrite.getValue(),
                    newExec.getValue(),
                    newAdmin.getValue());
            acl.add(newAce);

            Checkbox r1 =
                new Checkbox(
                    getTranslation("general.acl.adminpermission"), newAce.isAdminPermission());
            r1.addValueChangeListener(e2 -> newAce.setAdminPermission(e2.getValue()));
            Checkbox r2 =
                new Checkbox(
                    getTranslation("general.acl.readpermission"), newAce.isReadPermission());
            r2.addValueChangeListener(e2 -> newAce.setReadPermission(e2.getValue()));
            Checkbox r3 =
                new Checkbox(
                    getTranslation("general.acl.writepermission"), newAce.isWritePermission());
            r3.addValueChangeListener(e2 -> newAce.setWritePermission(e2.getValue()));
            Checkbox r4 =
                new Checkbox(
                    getTranslation("general.acl.executepermission"), newAce.isExecutePermission());
            r4.addValueChangeListener(e2 -> newAce.setExecutePermission(e2.getValue()));

            HorizontalLayout newRow = new HorizontalLayout();
            newRow.setWidthFull();
            newRow.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            Span ns = new Span(sel);
            ns.getStyle().set("min-width", "180px").set("flex-grow", "1");
            Button delNew =
                new Button(
                    "×",
                    e2 -> {
                      acl.remove(newAce);
                      deletedAces.add(newAce);
                      aceRows.remove(newRow);
                      subjects.add(sel);
                      subjectIds.put(sel, ids);
                      subjectBox.setItems(subjects);
                    });
            newRow.add(ns, r1, r2, r3, r4, delNew);
            aceRows.add(newRow);

            subjects.remove(sel);
            subjectBox.setItems(subjects);
            subjectBox.clear();
          } catch (Exception ex) {
            error("Failed to add entry: " + ex.getMessage());
          }
        });

    HorizontalLayout addRow =
        new HorizontalLayout(subjectBox, newAdmin, newRead, newWrite, newExec, addBtn);
    addRow.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
    addRow.setWidthFull();

    VerticalLayout content =
        new VerticalLayout(aceRows, new com.vaadin.flow.component.html.Hr(), addRow);
    content.setPadding(false);
    dialog.add(content);

    Button saveBtn =
        new Button(getTranslation("general.save"), ev -> doSaveAcl(deletedAces, acl, dialog));
    dialog
        .getFooter()
        .add(new Button(getTranslation("general.close"), ev -> dialog.close()), saveBtn);
    dialog.open();
  }

  void doSaveAcl(
      List<XincoCoreACEServer> deletedAces, List<XincoCoreACEServer> acl, Dialog dialog) {
    try {
      for (XincoCoreACEServer d : deletedAces) {
        if (d.getId() > 0) {
          XincoCoreACEServer.removeFromDB(d, session.getUser().getId());
        }
      }
      for (XincoCoreACEServer a : acl) {
        a.write2DB();
      }
      dialog.close();
      Notification.show(getTranslation("datawizard.updatesuccess"))
          .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    } catch (Exception ex) {
      LOG.log(Level.SEVERE, "ACL save failed", ex);
      error("ACL save failed: " + ex.getMessage());
    }
  }

  private void error(String msg) {
    Notification.show(msg).addThemeVariants(NotificationVariant.LUMO_ERROR);
  }
}
