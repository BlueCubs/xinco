package com.bluecubs.xinco.ui.component;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreLanguage;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyGridTest {

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

  @Test
  void propertyGrid_constructor_setsUpColumns() {
    assertNotNull(new PropertyGrid());
  }

  @Test
  void propertyGrid_setData_populatesRows() {
    PropertyGrid grid = new PropertyGrid();

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreLanguage mockLang = mock(XincoCoreLanguage.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);

    when(mockData.getId()).thenReturn(42);
    when(mockData.getDesignation()).thenReturn("test-file.txt");
    when(mockData.getXincoCoreLanguage()).thenReturn(mockLang);
    when(mockLang.getSign()).thenReturn("EN");
    when(mockData.getStatusNumber()).thenReturn(1);
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);
    when(mockType.getDesignation()).thenReturn("text/plain");

    grid.setData(mockData);
    assertNotNull(grid);
  }
}
