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
import com.bluecubs.xinco.ui.component.PropertyGrid;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
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
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
  private com.vaadin.flow.component.contextmenu.MenuItem miManageAcl;

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
    var repoMenu = menuBar.addItem(getTranslation("menu.repository"));
    var repoSub = repoMenu.getSubMenu();
    miNewFolder = repoSub.addItem(getTranslation("general.newfolder"), e -> openNewFolderDialog());
    miAddData =
        repoSub.addItem(getTranslation("menu.repository.adddata") + "…", e -> openAddDataDialog());
    repoSub.addItem(getTranslation("menu.repository.refresh"), e -> loadRootNodes());

    // Edit menu
    var editMenu = menuBar.addItem("Edit");
    var editSub = editMenu.getSubMenu();
    miDelete = editSub.addItem(getTranslation("general.delete"), e -> confirmDelete());
    editSub.add(new com.vaadin.flow.component.html.Hr());
    miManageAcl = editSub.addItem("Manage ACL…", e -> openAclDialog());

    // File menu
    var fileMenu = menuBar.addItem("File");
    var fileSub = fileMenu.getSubMenu();
    miDownload =
        fileSub.addItem(getTranslation("menu.repository.downloadfile"), e -> downloadSelected());
    fileSub.add(new com.vaadin.flow.component.html.Hr());
    miCheckOut = fileSub.addItem(getTranslation("menu.edit.checkoutfile"), e -> checkoutSelected());
    miCheckIn =
        fileSub.addItem(getTranslation("menu.edit.checkinfile") + "…", e -> openCheckinDialog());
    miUndoCheckOut =
        fileSub.addItem(getTranslation("menu.edit.undocheckout"), e -> undoCheckoutSelected());
    fileSub.add(new com.vaadin.flow.component.html.Hr());
    miLock = fileSub.addItem(getTranslation("menu.edit.lockdata"), e -> lockSelected());
    miPublish = fileSub.addItem(getTranslation("menu.edit.publishdata"), e -> publishSelected());

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
        .addHierarchyColumn(XincoCoreNodeServer::getDesignation)
        .setHeader(getTranslation("general.folder"));
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
    dataGrid
        .addColumn(XincoCoreDataServer::getDesignation)
        .setHeader(getTranslation("general.filename"));
    dataGrid
        .addColumn(d -> d.getXincoCoreLanguage().getSign())
        .setHeader(getTranslation("general.language"));
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
    switch (status) {
      case 1:
        return "Active";
      case 2:
        return "Locked";
      case 4:
        return "Checked Out";
      case 5:
        return "Published";
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
      // Add a hidden anchor and programmatically click it to trigger download
      Anchor anchor = new Anchor(resource, "");
      anchor.getElement().setAttribute("download", true);
      anchor.setVisible(false);
      add(anchor);
      anchor.getElement().callJsFunction("click");
      // Clean up anchor after a short delay so it doesn't accumulate
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

    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setMaxFiles(1);
    upload.setWidthFull();

    TextField designationField = new TextField(getTranslation("general.filename"));
    designationField.setWidthFull();
    designationField.setRequired(true);

    upload.addSucceededListener(
        e -> {
          if (designationField.isEmpty()) {
            designationField.setValue(e.getFileName());
          }
        });

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

    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(getTranslation("menu.repository.adddata"));
    dialog.setWidth("480px");
    dialog.add(new VerticalLayout(upload, designationField, langSelect));

    final List<XincoCoreLanguageServer> finalLanguages = languages;
    Button addBtn =
        new Button(
            "Add",
            e -> {
              String name = designationField.getValue().trim();
              if (name.isEmpty()) {
                designationField.setErrorMessage("Name is required");
                designationField.setInvalid(true);
                return;
              }
              if (buffer.getFileName() == null || buffer.getFileName().isEmpty()) {
                error("Please upload a file first.");
                return;
              }
              XincoCoreLanguageServer lang =
                  langSelect.getValue() != null && !finalLanguages.isEmpty()
                      ? langSelect.getValue()
                      : null;
              if (lang == null) {
                error("Please select a language.");
                return;
              }
              try {
                // 1. Persist the data record
                XincoCoreDataServer newData =
                    new XincoCoreDataServer(0, selectedNode.getId(), lang.getId(), 1, name, 1);
                newData.write2DB();
                int dataId = newData.getId();

                // 2. Create CREATION log entry (versionHigh=1, versionMid=0 for major version)
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

                // 3. Write file bytes to repository path {id}-{logId}
                String repoPath =
                    XincoCoreDataServer.getXincoCoreDataPath(
                        XincoDBManager.CONFIG.fileRepositoryPath, dataId, dataId + "-" + logId);
                File repoFile = new File(repoPath);
                repoFile.getParentFile().mkdirs();
                try (InputStream in = buffer.getInputStream()) {
                  Files.copy(in, repoFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                // Also write to base path {id} so checkout/checkin operations have a working copy
                String basePath =
                    XincoCoreDataServer.getXincoCoreDataPath(
                        XincoDBManager.CONFIG.fileRepositoryPath, dataId, "" + dataId);
                Files.copy(
                    repoFile.toPath(),
                    new File(basePath).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

                // 4. Write add attributes for data type 1 (12 attributes)
                XMLGregorianCalendar now =
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
                String filename = buffer.getFileName();
                long filesize = repoFile.length();
                // attr 1: filename (varchar)
                new XincoAddAttributeServer(dataId, 1, 0, 0L, 0.0, filename, "", now).write2DB();
                // attr 2: filesize (unsignedint)
                new XincoAddAttributeServer(dataId, 2, 0, filesize, 0.0, "", "", now).write2DB();
                // attr 3: checksum (varchar, empty)
                new XincoAddAttributeServer(dataId, 3, 0, 0L, 0.0, "", "", now).write2DB();
                // attr 4: revision model = 1
                new XincoAddAttributeServer(dataId, 4, 0, 1L, 0.0, "", "", now).write2DB();
                // attr 5-12: defaults
                for (int i = 5; i <= 12; i++) {
                  new XincoAddAttributeServer(dataId, i, 0, 0L, 0.0, "", "", now).write2DB();
                }

                dialog.close();
                // Refresh data grid for current node
                selectedNode.fillXincoCoreData();
                dataGrid.setItems(
                    selectedNode.getXincoCoreData().stream()
                        .filter(o -> o instanceof XincoCoreDataServer)
                        .map(o -> (XincoCoreDataServer) o)
                        .toList());
                Notification.show("'" + name + "' added.")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
              } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Add data failed", ex);
                error("Failed to add data: " + ex.getMessage());
              }
            });

    dialog
        .getFooter()
        .add(new Button(getTranslation("general.cancel"), e -> dialog.close()), addBtn);
    dialog.open();
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
            new Button(
                getTranslation("general.create"),
                e -> {
                  String name = nameField.getValue().trim();
                  if (name.isEmpty()) {
                    nameField.setErrorMessage("Name is required");
                    nameField.setInvalid(true);
                    return;
                  }
                  try {
                    XincoCoreNodeServer newNode =
                        new XincoCoreNodeServer(
                            0,
                            selectedNode.getId(),
                            selectedNode.getXincoCoreLanguage().getId(),
                            name,
                            1);
                    newNode.write2DB();
                    dialog.close();
                    loadRootNodes();
                    Notification.show("Folder '" + name + "' created.")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
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
    confirm.addConfirmListener(
        e -> {
          try {
            if (selectedData != null) {
              if (selectedData.getStatusNumber() == 4) {
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

  private void checkoutSelected() {
    if (selectedData == null) return;
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
              .setOperationDescription(
                  getTranslation("menu.edit.checkoutfile")
                      + " (user: "
                      + session.getUser().getUsername()
                      + ")")
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
  }

  private void openCheckinDialog() {
    if (selectedData == null) return;

    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setMaxFiles(1);
    upload.setWidthFull();

    TextField descField = new TextField("Change description");
    descField.setWidthFull();

    Dialog dialog = new Dialog();
    dialog.setHeaderTitle(getTranslation("menu.edit.checkinfile"));
    dialog.setWidth("480px");
    dialog.add(new VerticalLayout(upload, descField));

    dialog
        .getFooter()
        .add(
            new Button(getTranslation("general.cancel"), e -> dialog.close()),
            new Button(
                getTranslation("menu.edit.checkinfile"),
                e -> {
                  if (buffer.getFileName() == null || buffer.getFileName().isEmpty()) {
                    error("Please upload the revised file first.");
                    return;
                  }
                  try {
                    XincoCoreDataServer data = new XincoCoreDataServer(selectedData.getId());
                    XincoCoreLogServer lastLog =
                        data.getXincoCoreLogs().isEmpty()
                            ? null
                            : (XincoCoreLogServer)
                                data.getXincoCoreLogs().get(data.getXincoCoreLogs().size() - 1);
                    int vh = lastLog != null ? lastLog.getVersion().getVersionHigh() + 1 : 2;
                    String desc =
                        descField.getValue().trim().isEmpty()
                            ? getTranslation("menu.edit.checkinfile")
                            : descField.getValue().trim();

                    var log =
                        new XincoCoreLogServerBuilder()
                            .setXincoCoreDataId(data.getId())
                            .setXincoCoreUserId(session.getUser().getId())
                            .setOpCode(OPCode.CHECKIN.ordinal() + 1)
                            .setOperationDescription(desc)
                            .setVersionHigh(vh)
                            .setVersionMid(0)
                            .setVersionLow(0)
                            .setVersionPostFix("")
                            .createXincoCoreLogServer();
                    log.write2DB();
                    int logId = log.getId();

                    // Write new bytes to base path {id}
                    String base =
                        XincoCoreDataServer.getXincoCoreDataPath(
                            XincoDBManager.CONFIG.fileRepositoryPath,
                            data.getId(),
                            "" + data.getId());
                    File baseFile = new File(base);
                    baseFile.getParentFile().mkdirs();
                    try (InputStream in = buffer.getInputStream()) {
                      Files.copy(in, baseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    // Archive versioned copy {id}-{logId}
                    String versionPath =
                        XincoCoreDataServer.getXincoCoreDataPath(
                            XincoDBManager.CONFIG.fileRepositoryPath,
                            data.getId(),
                            data.getId() + "-" + logId);
                    Files.copy(
                        baseFile.toPath(),
                        new File(versionPath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                    // Update filename and filesize add attributes
                    XMLGregorianCalendar now =
                        DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(new GregorianCalendar());
                    new XincoAddAttributeServer(
                            data.getId(), 1, 0, 0L, 0.0, buffer.getFileName(), "", now)
                        .write2DB();
                    new XincoAddAttributeServer(
                            data.getId(), 2, 0, baseFile.length(), 0.0, "", "", now)
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
                }));
    dialog.open();
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
    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader(getTranslation("menu.edit.lockdata") + "?");
    confirm.setCancelable(true);
    confirm.setConfirmText(getTranslation("menu.edit.lockdata"));
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
                    .setOpCode(OPCode.LOCK_COMMENT.ordinal() + 1)
                    .setOperationDescription(getTranslation("menu.edit.lockdata"))
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
    confirm.open();
  }

  private void publishSelected() {
    if (selectedData == null) return;
    ConfirmDialog confirm = new ConfirmDialog();
    confirm.setHeader(getTranslation("menu.edit.publishdata") + "?");
    confirm.setCancelable(true);
    confirm.setConfirmText(getTranslation("menu.edit.publishdata"));
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
                    .setOpCode(OPCode.PUBLISH_COMMENT.ordinal() + 1)
                    .setOperationDescription(getTranslation("menu.edit.publishdata"))
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
    confirm.open();
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
    dialog.setHeaderTitle("ACL — " + designation);
    dialog.setWidth("700px");

    for (XincoCoreACEServer ace : new ArrayList<>(acl)) {
      String name;
      if (ace.getXincoCoreUserId() > 0) {
        int uid = ace.getXincoCoreUserId();
        name =
            "User: "
                + allUsers.stream()
                    .filter(u -> u.getId() == uid)
                    .map(XincoCoreUserServer::getUsername)
                    .findFirst()
                    .orElse("#" + uid);
      } else {
        int gid = ace.getXincoCoreGroupId();
        name =
            "Group: "
                + allGroups.stream()
                    .filter(g -> g.getId() == gid)
                    .map(XincoCoreGroupServer::getDesignation)
                    .findFirst()
                    .orElse("#" + gid);
      }

      Checkbox cbAdmin = new Checkbox("Admin", ace.isAdminPermission());
      cbAdmin.addValueChangeListener(ev -> ace.setAdminPermission(ev.getValue()));
      Checkbox cbRead = new Checkbox("Read", ace.isReadPermission());
      cbRead.addValueChangeListener(ev -> ace.setReadPermission(ev.getValue()));
      Checkbox cbWrite = new Checkbox("Write", ace.isWritePermission());
      cbWrite.addValueChangeListener(ev -> ace.setWritePermission(ev.getValue()));
      Checkbox cbExec = new Checkbox("Execute", ace.isExecutePermission());
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
        String label = "User: " + u.getUsername();
        subjects.add(label);
        subjectIds.put(label, new int[] {uid, 0});
      }
    }
    for (XincoCoreGroupServer g : allGroups) {
      int gid = g.getId();
      if (acl.stream().noneMatch(a -> a.getXincoCoreGroupId() == gid)) {
        String label = "Group: " + g.getDesignation();
        subjects.add(label);
        subjectIds.put(label, new int[] {0, gid});
      }
    }

    ComboBox<String> subjectBox = new ComboBox<>("Add entry");
    subjectBox.setItems(subjects);
    subjectBox.setWidth("220px");
    Checkbox newAdmin = new Checkbox("Admin");
    Checkbox newRead = new Checkbox("Read", true);
    Checkbox newWrite = new Checkbox("Write", true);
    Checkbox newExec = new Checkbox("Execute");

    Button addBtn = new Button("Add");
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

            Checkbox r1 = new Checkbox("Admin", newAce.isAdminPermission());
            r1.addValueChangeListener(e2 -> newAce.setAdminPermission(e2.getValue()));
            Checkbox r2 = new Checkbox("Read", newAce.isReadPermission());
            r2.addValueChangeListener(e2 -> newAce.setReadPermission(e2.getValue()));
            Checkbox r3 = new Checkbox("Write", newAce.isWritePermission());
            r3.addValueChangeListener(e2 -> newAce.setWritePermission(e2.getValue()));
            Checkbox r4 = new Checkbox("Execute", newAce.isExecutePermission());
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
        new Button(
            "Save",
            ev -> {
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
                Notification.show("ACL saved.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
              } catch (Exception ex) {
                LOG.log(Level.SEVERE, "ACL save failed", ex);
                error("ACL save failed: " + ex.getMessage());
              }
            });
    dialog.getFooter().add(new Button("Close", ev -> dialog.close()), saveBtn);
    dialog.open();
  }

  private void error(String msg) {
    Notification.show(msg).addThemeVariants(NotificationVariant.LUMO_ERROR);
  }
}
