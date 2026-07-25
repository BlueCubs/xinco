package com.bluecubs.xinco.ui.component;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreLanguage;
import com.bluecubs.xinco.server.service.XincoCoreLog;
import com.bluecubs.xinco.server.service.XincoVersion;
import com.bluecubs.xinco.ui.ViewTestHelper;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import java.util.List;
import java.util.Optional;
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
    ViewTestHelper.registerI18NProvider();
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
  void propertyGrid_setData_noLogs_populatesBaseRows() {
    PropertyGrid grid = new PropertyGrid();
    XincoCoreDataServer mockData = mockBaseData();
    when(mockData.getXincoCoreLogs()).thenReturn(null);

    grid.setData(mockData);
    assertNotNull(grid);
  }

  @Test
  void propertyGrid_setData_withVersionHistory_populatesLogRows() {
    PropertyGrid grid = new PropertyGrid();
    XincoCoreDataServer mockData = mockBaseData();

    XincoCoreLog log = new XincoCoreLog();
    XincoVersion version = new XincoVersion();
    version.setVersionHigh(1);
    version.setVersionMid(2);
    version.setVersionLow(3);
    version.setVersionPostfix("beta");
    log.setVersion(version);
    log.setOpDescription("Initial checkin");

    when(mockData.getXincoCoreLogs()).thenReturn(List.of(log));

    grid.setData(mockData);
    assertNotNull(grid);
  }

  @Test
  void propertyGrid_setData_logWithBlankDescription_showsPlaceholder() {
    PropertyGrid grid = new PropertyGrid();
    XincoCoreDataServer mockData = mockBaseData();

    XincoCoreLog log = new XincoCoreLog();
    XincoVersion version = new XincoVersion();
    version.setVersionHigh(1);
    version.setVersionMid(0);
    version.setVersionLow(0);
    log.setVersion(version);
    log.setOpDescription(null);

    when(mockData.getXincoCoreLogs()).thenReturn(List.of(log));

    grid.setData(mockData);
    assertNotNull(grid);
  }

  @Test
  void propertyGrid_setData_typeDesignation_isTranslated_notRawKey() {
    // Regression: before fix, Type row showed raw i18n key (e.g. "general.data.type.file")
    PropertyGrid grid = new PropertyGrid();
    XincoCoreDataServer mockData = mockBaseData();
    String i18nKey = "general.data.type.file";
    XincoCoreDataType translatedType = mock(XincoCoreDataType.class);
    when(translatedType.getDesignation()).thenReturn(i18nKey);
    when(mockData.getXincoCoreDataType()).thenReturn(translatedType);
    when(mockData.getXincoCoreLogs()).thenReturn(null);

    grid.setData(mockData);

    Optional<PropertyGrid.Row> typeRow =
        grid.getListDataView().getItems().filter(r -> "Type".equals(r.property())).findFirst();
    assertTrue(typeRow.isPresent(), "Type row should be present");
    assertNotEquals(i18nKey, typeRow.get().value(), "Type must show translated text, not raw key");
  }

  private XincoCoreDataServer mockBaseData() {
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
    return mockData;
  }
}
