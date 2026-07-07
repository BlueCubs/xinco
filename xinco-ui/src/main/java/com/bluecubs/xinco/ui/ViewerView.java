package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.ui.component.PropertyGrid;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "viewer", layout = MainLayout.class)
@PageTitle("Viewer — Xinco DMS")
@AnonymousAllowed
public class ViewerView extends VerticalLayout
    implements BeforeEnterObserver, AfterNavigationObserver {

  private static final Logger LOG = Logger.getLogger(ViewerView.class.getName());

  private UserSession session;

  private final TreeGrid<XincoCoreNodeServer> nodeTree = new TreeGrid<>();
  private final Grid<XincoCoreDataServer> dataGrid = new Grid<>(XincoCoreDataServer.class, false);
  private final PropertyGrid propertyGrid = new PropertyGrid();
  private final Div viewerPane = new Div();
  private final MenuBar menuBar = new MenuBar();

  public ViewerView() {
    this(resolveSession());
  }

  private static UserSession resolveSession() {
    if (VaadinSession.getCurrent() == null) return new UserSession();
    UserSession s = VaadinSession.getCurrent().getAttribute(UserSession.class);
    return s != null ? s : new UserSession();
  }

  public ViewerView(UserSession session) {
    this.session = session;
    setSizeFull();
    setPadding(false);
    setSpacing(false);

    buildMenuBar();
    buildNodeTree();
    buildDataGrid();

    viewerPane.setSizeFull();
    viewerPane.getStyle().set("overflow", "auto").set("padding", "8px");
    viewerPane.add(new Span(getTranslation("general.file.select")));

    VerticalLayout left = new VerticalLayout(nodeTree, dataGrid);
    left.setSizeFull();
    left.setPadding(false);
    left.setSpacing(false);
    left.setFlexGrow(1, nodeTree);
    left.setFlexGrow(1, dataGrid);

    SplitLayout content = new SplitLayout(left, viewerPane);
    content.setSizeFull();
    content.setSplitterPosition(35);

    add(menuBar, content);
    expand(content);
  }

  // ── Menu ──────────────────────────────────────────────────────────────────

  private void buildMenuBar() {
    menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
    menuBar.setWidthFull();
    menuBar.addItem(
        getTranslation("menu.repository"),
        e -> getUI().ifPresent(ui -> ui.navigate(ExplorerView.class)));
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
                      node.fillXincoCoreData();
                      dataGrid.setItems(
                          node.getXincoCoreData().stream()
                              .filter(o -> o instanceof XincoCoreDataServer)
                              .map(o -> (XincoCoreDataServer) o)
                              .toList());
                      viewerPane.removeAll();
                      propertyGrid.setNode(node);
                      viewerPane.add(propertyGrid);
                    }));
  }

  // ── Data Grid ─────────────────────────────────────────────────────────────

  private void buildDataGrid() {
    dataGrid
        .addColumn(XincoCoreDataServer::getDesignation)
        .setHeader(getTranslation("general.name"));
    dataGrid
        .addColumn(d -> d.getXincoCoreLanguage().getSign())
        .setHeader(getTranslation("general.language"));
    dataGrid
        .addColumn(d -> d.getXincoCoreDataType().getDesignation())
        .setHeader(getTranslation("general.type"));
    dataGrid.setSizeFull();
    dataGrid.addSelectionListener(
        e ->
            e.getFirstSelectedItem()
                .ifPresent(
                    data -> {
                      data.loadAddAttributes();
                      showPreview(data);
                    }));
    // Double-click: open in new browser tab (§2.8.1 "opened directly for preview")
    dataGrid.addItemDoubleClickListener(
        e -> {
          if (e.getItem().getXincoCoreDataType().getId() == 1) {
            openInTab(e.getItem());
          }
        });
  }

  // ── Navigation lifecycle ──────────────────────────────────────────────────

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
    dataGrid.setItems(List.of());
    clearViewer();
    try {
      XincoCoreNodeServer root = new XincoCoreNodeServer(1);
      root.fillXincoCoreNodes();
      List<XincoCoreNodeServer> roots = new ArrayList<>();
      roots.add(root);
      nodeTree.setItems(roots, this::getChildNodes);
    } catch (Throwable e) {
      showError("Failed to load folders: " + e.getMessage());
    }
  }

  private List<XincoCoreNodeServer> getChildNodes(XincoCoreNodeServer parent) {
    parent.fillXincoCoreNodes();
    return parent.getXincoCoreNodes().stream()
        .filter(o -> o instanceof XincoCoreNodeServer)
        .map(o -> (XincoCoreNodeServer) o)
        .toList();
  }

  // ── Preview ───────────────────────────────────────────────────────────────

  private void clearViewer() {
    viewerPane.removeAll();
    viewerPane.add(new Span(getTranslation("general.file.select")));
  }

  void showPreview(XincoCoreDataServer data) {
    viewerPane.removeAll();
    if (data.getXincoCoreDataType().getId() == 1) {
      showFilePreview(data);
    } else {
      propertyGrid.setData(data);
      viewerPane.add(propertyGrid);
    }
  }

  private void showFilePreview(XincoCoreDataServer data) {
    try {
      String path = XincoCoreDataServer.getLastMajorVersionDataPath(data.getId());
      if (path == null) {
        viewerPane.add(new Span(getTranslation("datawizard.updatefailed")));
        return;
      }
      File file = new File(path);
      if (!file.exists()) {
        viewerPane.add(new Span(getTranslation("datawizard.filedownloadfailed")));
        return;
      }
      String filename = getOriginalFilename(data);
      String mimeType = detectMimeType(filename, Paths.get(path));

      StreamResource resource =
          new StreamResource(
              filename,
              () -> {
                try {
                  return new FileInputStream(file);
                } catch (Exception ex) {
                  LOG.log(Level.SEVERE, "Preview stream error", ex);
                  return null;
                }
              });
      resource.setContentType(mimeType);

      if (mimeType.startsWith("image/")) {
        Image img = new Image(resource, filename);
        img.setMaxWidth("100%");
        img.setMaxHeight("100%");
        viewerPane.add(img);
      } else {
        // PDF, text, HTML — embed via iframe using registered StreamResource
        StreamRegistration reg =
            VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
        Element iframe = new Element("iframe");
        iframe.setAttribute("src", reg.getResourceUri().toString());
        iframe.getStyle().set("width", "100%").set("height", "100%").set("border", "none");
        viewerPane.getElement().appendChild(iframe);
      }
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Preview failed", e);
      viewerPane.add(new Span(getTranslation("general.error")));
    }
  }

  private void openInTab(XincoCoreDataServer data) {
    try {
      String path = XincoCoreDataServer.getLastMajorVersionDataPath(data.getId());
      if (path == null) return;
      File file = new File(path);
      if (!file.exists()) return;
      String filename = getOriginalFilename(data);
      String mimeType = detectMimeType(filename, Paths.get(path));

      StreamResource resource =
          new StreamResource(
              filename,
              () -> {
                try {
                  return new FileInputStream(file);
                } catch (Exception ex) {
                  return null;
                }
              });
      resource.setContentType(mimeType);

      // No "download" attribute → browser opens inline in new tab
      Anchor anchor = new Anchor(resource, "");
      anchor.getElement().setAttribute("target", "_blank");
      anchor.setVisible(false);
      add(anchor);
      anchor.getElement().callJsFunction("click");
      UI.getCurrent()
          .getPage()
          .executeJs("setTimeout(() => $0.remove(), 5000)", anchor.getElement());
    } catch (Exception e) {
      showError("Cannot open file: " + e.getMessage());
    }
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  private String getOriginalFilename(XincoCoreDataServer data) {
    try {
      return data.getXincoAddAttributes().stream()
          .filter(a -> a.getAttributeId() == 1)
          .map(a -> a.getAttribVarchar())
          .filter(s -> s != null && !s.isBlank())
          .findFirst()
          .orElse(data.getDesignation());
    } catch (Exception e) {
      return data.getDesignation();
    }
  }

  private static String detectMimeType(String filename, Path filePath) {
    try {
      String probed = Files.probeContentType(filePath);
      if (probed != null) return probed;
    } catch (IOException ignored) {
    }
    int dot = filename.lastIndexOf('.');
    if (dot >= 0) {
      return switch (filename.substring(dot + 1).toLowerCase()) {
        case "pdf" -> "application/pdf";
        case "png" -> "image/png";
        case "jpg", "jpeg" -> "image/jpeg";
        case "gif" -> "image/gif";
        case "svg" -> "image/svg+xml";
        case "txt" -> "text/plain";
        case "html", "htm" -> "text/html";
        default -> "application/octet-stream";
      };
    }
    return "application/octet-stream";
  }

  private void showError(String msg) {
    Notification n = Notification.show(msg, 5000, Notification.Position.BOTTOM_END);
    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
  }
}
