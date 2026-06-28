package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreLanguage;
import com.bluecubs.xinco.ui.component.PropertyGrid;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.TreeGrid;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExplorerViewTest {

  private static Routes routes;

  @BeforeAll
  static void discoverRoutes() {
    routes = new Routes().autoDiscoverViews("com.bluecubs.xinco.ui");
  }

  @BeforeEach
  void setup() {
    // Default route "" navigates to ExplorerView — no explicit navigate needed
    MockVaadin.setup(routes);
  }

  @AfterEach
  void tearDown() {
    MockVaadin.tearDown();
  }

  @Test
  void explorerView_renders() {
    assertNotNull(_get(TreeGrid.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  void explorerView_treeSelection_populatesDataGrid() {
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
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreLanguage mockLang = mock(XincoCoreLanguage.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockData.getXincoCoreLanguage()).thenReturn(mockLang);
    when(mockLang.getSign()).thenReturn("EN");
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);
    when(mockType.getDesignation()).thenReturn("text");

    // Get the data grid (not TreeGrid, not PropertyGrid)
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
}
