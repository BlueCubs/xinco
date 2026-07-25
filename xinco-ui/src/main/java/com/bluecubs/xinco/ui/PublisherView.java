package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Public publisher endpoint serving published content without authentication.
 *
 * <p><b>Route:</b> {@code /publisher/[nodeId]} (e.g. {@code /publisher/123})
 *
 * <p><b>Access:</b> {@link AnonymousAllowed} — no login required. This differs from all other
 * Vaadin views ({@code LoginView}, {@code ExplorerView}, {@code AdminView}) which require
 * authentication. The view is rendered inside {@link MainLayout} for consistent header/branding.
 *
 * <p><b>Config gate:</b> The entire endpoint is guarded by {@link
 * com.bluecubs.xinco.core.server.XincoConfigSingletonServer#isAllowPublisherList()}. When that
 * setting is {@code false}, the view shows an error message and does not serve any data. The
 * setting key is {@code setting.allowpublisherlist} in the Xinco configuration DB.
 *
 * <p><b>Data scope:</b> Only items with {@code statusNumber = 5} ({@link
 * com.bluecubs.xinco.core.XincoDataStatus#PUBLISHED}) are displayed. Download links are provided
 * for file-type data (typeId=1) via {@link StreamResource} and URL links for link-type data
 * (typeId=3).
 */
@Route(value = "publisher", layout = MainLayout.class)
@PageTitle("Publisher — Xinco DMS")
@AnonymousAllowed
public class PublisherView extends VerticalLayout implements HasUrlParameter<Integer> {

  private static final Logger LOG = Logger.getLogger(PublisherView.class.getName());

  private final Span heading = new Span();
  private final Grid<XincoCoreDataServer> dataGrid = new Grid<>(XincoCoreDataServer.class, false);
  private final Div content = new Div();

  public PublisherView() {
    setSizeFull();
    setPadding(false);
    setSpacing(false);

    heading
        .getStyle()
        .set("font-size", "var(--lumo-font-size-xl)")
        .set("font-weight", "bold")
        .set("padding", "var(--lumo-space-m)");

    buildDataGrid();

    content.setSizeFull();
    content.getStyle().set("overflow", "auto").set("padding", "8px");

    add(heading, dataGrid, content);
    expand(dataGrid);
  }

  private void buildDataGrid() {
    dataGrid
        .addColumn(XincoCoreDataServer::getDesignation)
        .setHeader(getTranslation("general.name"))
        .setAutoWidth(true);
    dataGrid
        .addColumn(d -> d.getXincoCoreDataType().getDesignation())
        .setHeader(getTranslation("general.type"))
        .setAutoWidth(true);
    dataGrid
        .addColumn(d -> d.getXincoCoreLanguage().getSign())
        .setHeader(getTranslation("general.language"))
        .setAutoWidth(true);
    dataGrid
        .addColumn(new ComponentRenderer<>(this::buildDownloadLink))
        .setHeader(getTranslation("general.file"))
        .setKey("download");
    dataGrid.setSizeFull();
    dataGrid.setVisible(false);
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter Integer nodeId) {
    if (nodeId == null) {
      showMessage(getTranslation("datawizard.updatefailed"));
      return;
    }
    loadPublishedItems(nodeId);
  }

  private void loadPublishedItems(int nodeId) {
    try {
      if (!XincoConfigSingletonServer.getInstance().isAllowPublisherList()) {
        showMessage(getTranslation("datawizard.updatefailed"));
        return;
      }

      XincoCoreNodeServer node = new XincoCoreNodeServer(nodeId);
      heading.setText(node.getDesignation());

      List<XincoCoreDataServer> publishedItems =
          node.getXincoCoreData().stream()
              .filter(o -> o instanceof XincoCoreDataServer)
              .map(o -> (XincoCoreDataServer) o)
              .filter(d -> d.getStatusNumber() == 5)
              .toList();

      if (publishedItems.isEmpty()) {
        showMessage(getTranslation("datawizard.updatefailed"));
        return;
      }

      content.removeAll();
      dataGrid.setItems(publishedItems);
      dataGrid.setVisible(true);
    } catch (XincoException e) {
      LOG.log(Level.INFO, "Publisher node not found: " + nodeId, e);
      showMessage(getTranslation("datawizard.filedownloadfailed"));
    }
  }

  private Component buildDownloadLink(XincoCoreDataServer data) {
    int typeId = data.getXincoCoreDataType().getId();
    if (typeId == 1) {
      try {
        String path = XincoCoreDataServer.getLastMajorVersionDataPath(data.getId());
        if (path == null) return new Span("-");
        File file = new File(path);
        if (!file.exists()) return new Span("-");

        String filename = getOriginalFilename(data);

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

        String mimeType = detectMimeType(filename, Paths.get(path));
        resource.setContentType(mimeType);

        Anchor anchor = new Anchor(resource, filename);
        anchor.getElement().setAttribute("download", true);
        return anchor;
      } catch (Exception e) {
        LOG.log(Level.SEVERE, "Download link failed", e);
        return new Span("-");
      }
    } else if (typeId == 3) {
      String url =
          data.getXincoAddAttributes().stream()
              .filter(a -> a.getAttributeId() == 1)
              .map(a -> a.getAttribVarchar())
              .filter(v -> v != null && !v.isBlank())
              .findFirst()
              .orElse(null);
      if (url != null) {
        Anchor link = new Anchor(url, getTranslation("general.open"));
        link.setTarget("_blank");
        return link;
      }
      return new Span("-");
    }
    return new Span("-");
  }

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
    } catch (Exception ignored) {
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

  private void showMessage(String msg) {
    content.removeAll();
    content.add(new Span(msg));
    dataGrid.setVisible(false);
  }
}
