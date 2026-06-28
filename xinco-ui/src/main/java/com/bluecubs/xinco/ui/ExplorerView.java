package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.ui.component.PropertyGrid;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.ArrayList;
import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Explorer — Xinco DMS")
@AnonymousAllowed
public class ExplorerView extends HorizontalLayout {

  private final TreeGrid<XincoCoreNodeServer> nodeTree = new TreeGrid<>();
  private final Grid<XincoCoreDataServer> dataGrid = new Grid<>(XincoCoreDataServer.class, false);
  private final PropertyGrid propertyGrid = new PropertyGrid();

  public ExplorerView() {
    this(new UserSession());
  }

  public ExplorerView(UserSession session) {
    setSizeFull();
    setPadding(false);

    buildNodeTree();
    buildDataGrid();

    VerticalLayout left = new VerticalLayout(new Span("Nodes"), nodeTree);
    left.setWidth("30%");
    left.setPadding(false);

    VerticalLayout center = new VerticalLayout(new Span("Data items"), dataGrid);
    center.setWidth("40%");
    center.setPadding(false);

    VerticalLayout right = new VerticalLayout(new Span("Properties"), propertyGrid);
    right.setWidth("30%");
    right.setPadding(false);

    add(left, center, right);

    loadRootNodes();
  }

  private void buildNodeTree() {
    nodeTree.addHierarchyColumn(XincoCoreNodeServer::getDesignation).setHeader("Folder");
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
                    }));
  }

  private void buildDataGrid() {
    dataGrid.addColumn(XincoCoreDataServer::getDesignation).setHeader("Name");
    dataGrid.addColumn(d -> d.getXincoCoreLanguage().getSign()).setHeader("Language");
    dataGrid.setSizeFull();
    dataGrid.addSelectionListener(
        e ->
            e.getFirstSelectedItem()
                .ifPresent(
                    data -> {
                      data.loadAddAttributes();
                      propertyGrid.setData(data);
                    }));
  }

  private void loadRootNodes() {
    try {
      XincoCoreNodeServer root = new XincoCoreNodeServer(1);
      root.fillXincoCoreNodes();
      List<XincoCoreNodeServer> roots = new ArrayList<>();
      roots.add(root);
      nodeTree.setItems(roots, this::getChildNodes);
    } catch (Throwable e) {
      Notification.show("Failed to load nodes: " + e.getMessage());
    }
  }

  private List<XincoCoreNodeServer> getChildNodes(XincoCoreNodeServer parent) {
    parent.fillXincoCoreNodes();
    return parent.getXincoCoreNodes().stream()
        .filter(o -> o instanceof XincoCoreNodeServer)
        .map(o -> (XincoCoreNodeServer) o)
        .toList();
  }
}
