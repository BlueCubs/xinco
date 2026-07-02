package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreACEServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.ui.component.PropertyGrid;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "explorer", layout = MainLayout.class)
@PageTitle("Explorer — Xinco DMS")
@AnonymousAllowed
public class ExplorerView extends VerticalLayout
    implements BeforeEnterObserver, AfterNavigationObserver {

  private static final Logger LOG = Logger.getLogger(ExplorerView.class.getName());

  // Data state
  private XincoCoreNodeServer selectedNode;
  private XincoCoreDataServer selectedData;
  private UserSession session;

  // Menu items that need enable/disable based on context
  private com.vaadin.flow.component.contextmenu.MenuItem miNewFolder;
  private com.vaadin.flow.component.contextmenu.MenuItem miDelete;
  private com.vaadin.flow.component.contextmenu.MenuItem miDownload;
  private com.vaadin.flow.component.contextmenu.MenuItem miCheckOut;
  private com.vaadin.flow.component.contextmenu.MenuItem miCheckIn;
  private com.vaadin.flow.component.contextmenu.MenuItem miUndoCheckOut;

  // UI components
  private final TreeGrid<XincoCoreNodeServer> nodeTree = new TreeGrid<>();
  private final Grid<XincoCoreDataServer> dataGrid = new Grid<>(XincoCoreDataServer.class, false);
  private final PropertyGrid propertyGrid = new PropertyGrid();
  private final MenuBar menuBar = new MenuBar();

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

    HorizontalLayout content = new HorizontalLayout();
    content.setSizeFull();
    content.setPadding(false);

    buildNodeTree();
    buildDataGrid();

    VerticalLayout left = new VerticalLayout(new Span("Folders"), nodeTree);
    left.setWidth("28%");
    left.setPadding(false);
    left.setSpacing(false);

    VerticalLayout center = new VerticalLayout(new Span("Data items"), dataGrid);
    center.setWidth("40%");
    center.setPadding(false);
    center.setSpacing(false);

    VerticalLayout right = new VerticalLayout(new Span("Properties"), propertyGrid);
    right.setWidth("32%");
    right.setPadding(false);
    right.setSpacing(false);

    content.add(left, center, right);
    add(menuBar, content);
    expand(content);
  }

  // ── Menu ──────────────────────────────────────────────────────────────────

  private void buildMenuBar() {
    menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
    menuBar.setWidthFull();

    // Repository menu
    var repoMenu = menuBar.addItem("Repository");
    var repoSub = repoMenu.getSubMenu();
    miNewFolder = repoSub.addItem("New Folder…", e -> openNewFolderDialog());
    repoSub.addItem("Refresh", e -> loadRootNodes());

    // Edit menu
    var editMenu = menuBar.addItem("Edit");
    var editSub = editMenu.getSubMenu();
    miDelete = editSub.addItem("Delete", e -> confirmDelete());

    // File menu
    var fileMenu = menuBar.addItem("File");
    var fileSub = fileMenu.getSubMenu();
    miDownload = fileSub.addItem("Download", e -> downloadSelected());
    fileSub.add(new com.vaadin.flow.component.html.Hr());
    miCheckOut = fileSub.addItem("Check Out", e -> notImplemented("Check Out"));
    miCheckIn = fileSub.addItem("Check In…", e -> notImplemented("Check In"));
    miUndoCheckOut = fileSub.addItem("Undo Check Out", e -> notImplemented("Undo Check Out"));

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
    // statusNumber: 1=active, 2=locked, 3=checked-out, 5=published
    boolean isCheckedOut = dataStatus == 3;

    miNewFolder.setEnabled(canWriteNode);
    miDelete.setEnabled(canWriteNode || canWriteData);
    miDownload.setEnabled(dataSelected && isFile);
    miCheckOut.setEnabled(canWriteData && isFile && !isCheckedOut);
    miCheckIn.setEnabled(canWriteData && isFile && isCheckedOut);
    miUndoCheckOut.setEnabled(canWriteData && isFile && isCheckedOut);
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

  // ── Tree ──────────────────────────────────────────────────────────────────

  private void buildNodeTree() {
    nodeTree.addHierarchyColumn(XincoCoreNodeServer::getDesignation).setHeader("Folder");
    nodeTree.setSizeFull();
    nodeTree.addSelectionListener(
        e ->
            e.getFirstSelectedItem()
                .ifPresent(
                    node -> {
                      selectedNode = node;
                      selectedData = null;
                      node.fillXincoCoreData();
                      dataGrid.setItems(
                          node.getXincoCoreData().stream()
                              .filter(o -> o instanceof XincoCoreDataServer)
                              .map(o -> (XincoCoreDataServer) o)
                              .toList());
                      propertyGrid.setData(null);
                      updateMenuState();
                    }));
  }

  // ── Data Grid ─────────────────────────────────────────────────────────────

  private void buildDataGrid() {
    dataGrid.addColumn(XincoCoreDataServer::getDesignation).setHeader("Name");
    dataGrid.addColumn(d -> d.getXincoCoreLanguage().getSign()).setHeader("Language");
    dataGrid.addColumn(d -> statusLabel(d.getStatusNumber())).setHeader("Status");
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

  private static String statusLabel(int status) {
    return switch (status) {
      case 1 -> "Active";
      case 2 -> "Locked";
      case 3 -> "Checked Out";
      case 5 -> "Published";
      default -> "Unknown (" + status + ")";
    };
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
      List<XincoCoreNodeServer> roots = new ArrayList<>();
      roots.add(root);
      nodeTree.setItems(roots, this::getChildNodes);
    } catch (Throwable e) {
      error("Failed to load nodes: " + e.getMessage());
    }
  }

  private List<XincoCoreNodeServer> getChildNodes(XincoCoreNodeServer parent) {
    parent.fillXincoCoreNodes();
    return parent.getXincoCoreNodes().stream()
        .filter(o -> o instanceof XincoCoreNodeServer)
        .map(o -> (XincoCoreNodeServer) o)
        .toList();
  }

  // ── Actions ───────────────────────────────────────────────────────────────

  private void downloadSelected() {
    if (selectedData == null) return;
    try {
      String path = XincoCoreDataServer.getLastMajorVersionDataPath(selectedData.getId());
      if (path == null) {
        error("No file version found in repository.");
        return;
      }
      File file = new File(path);
      if (!file.exists()) {
        error("File not found on disk: " + path);
        return;
      }
      String filename = selectedData.getDesignation();
      StreamResource resource = new StreamResource(filename, () -> {
        try {
          return new FileInputStream(file);
        } catch (Exception ex) {
          LOG.log(Level.SEVERE, "Download stream error", ex);
          return null;
        }
      });
      // Add a hidden anchor and programmatically click it to trigger download
      Anchor anchor = new Anchor(resource, "");
      anchor.getElement().setAttribute("download", true);
      anchor.setVisible(false);
      add(anchor);
      anchor.getElement().callJsFunction("click");
      // Clean up anchor after a short delay so it doesn't accumulate
      UI.getCurrent()
          .getPage()
          .executeJs(
              "setTimeout(() => $0.remove(), 5000)", anchor.getElement());
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Download failed", e);
      error("Download failed: " + e.getMessage());
    }
  }

  private void openNewFolderDialog() {
    if (selectedNode == null) return;
    Dialog dialog = new Dialog();
    dialog.setHeaderTitle("New Folder");
    TextField nameField = new TextField("Folder name");
    nameField.setWidthFull();
    nameField.setAutofocus(true);
    dialog.add(nameField);

    dialog.getFooter().add(
        new com.vaadin.flow.component.button.Button("Cancel", e -> dialog.close()),
        new com.vaadin.flow.component.button.Button("Create", e -> {
          String name = nameField.getValue().trim();
          if (name.isEmpty()) {
            nameField.setErrorMessage("Name is required");
            nameField.setInvalid(true);
            return;
          }
          try {
            XincoCoreNodeServer newNode = new XincoCoreNodeServer(
                0,
                selectedNode.getId(),
                selectedNode.getXincoCoreLanguage().getId(),
                name,
                1);
            newNode.write2DB();
            dialog.close();
            loadRootNodes();
            Notification.show("Folder '" + name + "' created.").addThemeVariants(
                NotificationVariant.LUMO_SUCCESS);
          } catch (XincoException ex) {
            LOG.log(Level.SEVERE, "Create folder failed", ex);
            error("Could not create folder: " + ex.getMessage());
          }
        }));
    dialog.open();
  }

  private void confirmDelete() {
    String target =
        selectedData != null
            ? "data item '" + selectedData.getDesignation() + "'"
            : selectedNode != null ? "folder '" + selectedNode.getDesignation() + "'" : null;
    if (target == null) return;

    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader("Delete " + target + "?");
    confirm.setText("This action cannot be undone.");
    confirm.setCancelable(true);
    confirm.setConfirmText("Delete");
    confirm.setConfirmButtonTheme("error primary");
    confirm.addConfirmListener(e -> {
      try {
        if (selectedData != null) {
          if (selectedData.getStatusNumber() == 3) {
            error("Cannot delete a checked-out item.");
            return;
          }
          selectedData.deleteFromDB();
          selectedData = null;
        } else if (selectedNode != null) {
          int userId = session.getUser() != null ? session.getUser().getId() : 0;
          selectedNode.deleteFromDB(true, userId);
          selectedNode = null;
        }
        loadRootNodes();
        Notification.show("Deleted.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      } catch (XincoException ex) {
        LOG.log(Level.SEVERE, "Delete failed", ex);
        error("Delete failed: " + ex.getMessage());
      }
    });
    confirm.open();
  }

  private void notImplemented(String feature) {
    Notification.show(feature + " is not yet implemented in this version.")
        .addThemeVariants(NotificationVariant.LUMO_CONTRAST);
  }

  private void error(String msg) {
    Notification.show(msg).addThemeVariants(NotificationVariant.LUMO_ERROR);
  }
}
