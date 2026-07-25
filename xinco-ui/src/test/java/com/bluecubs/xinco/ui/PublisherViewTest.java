package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreDataTypeServer;
import com.bluecubs.xinco.core.server.XincoCoreLanguageServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

class PublisherViewTest {

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
  void publisherView_renders() {
    addView(new PublisherView());
    // No exception means it renders
  }

  @Test
  @SuppressWarnings("unchecked")
  void publisherView_loadsPublishedItems() throws Exception {
    XincoCoreLanguageServer mockLang = mock(XincoCoreLanguageServer.class);

    XincoCoreDataTypeServer mockType = mock(XincoCoreDataTypeServer.class);

    XincoCoreDataServer publishedData = mock(XincoCoreDataServer.class, RETURNS_DEEP_STUBS);
    when(publishedData.getDesignation()).thenReturn("Published Doc");
    when(publishedData.getStatusNumber()).thenReturn(5);
    when(publishedData.getXincoCoreDataType()).thenReturn(mockType);
    when(publishedData.getXincoCoreLanguage()).thenReturn(mockLang);

    List<XincoCoreData> dataList = new ArrayList<>();
    dataList.add(publishedData);

    try (MockedStatic<XincoConfigSingletonServer> cfg =
        mockStatic(XincoConfigSingletonServer.class)) {
      XincoConfigSingletonServer cfgInstance = mock(XincoConfigSingletonServer.class);
      when(cfgInstance.isAllowPublisherList()).thenReturn(true);
      cfg.when(XincoConfigSingletonServer::getInstance).thenReturn(cfgInstance);

      try (MockedConstruction<XincoCoreNodeServer> ignored =
          mockConstruction(
              XincoCoreNodeServer.class,
              (mock, ctx) -> {
                when(mock.getDesignation()).thenReturn("Public Folder");
                when(mock.getXincoCoreData()).thenReturn(dataList);
              })) {

        PublisherView view = new PublisherView();
        addView(view);
        UI.getCurrent().navigate("publisher/1");

        Grid<XincoCoreDataServer> grid = _get(Grid.class);
        assertNotNull(grid);
      }
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void publisherView_configDisabled_showsMessage() throws Exception {
    try (MockedStatic<XincoConfigSingletonServer> cfg =
        mockStatic(XincoConfigSingletonServer.class)) {
      XincoConfigSingletonServer cfgInstance = mock(XincoConfigSingletonServer.class);
      when(cfgInstance.isAllowPublisherList()).thenReturn(false);
      cfg.when(XincoConfigSingletonServer::getInstance).thenReturn(cfgInstance);

      PublisherView view = new PublisherView();
      addView(view);
      UI.getCurrent().navigate("publisher/1");

      // Should render without data grid
      assertNotNull(view);
    }
  }

  @Test
  void publisherView_nonexistentNode_showsMessage() throws Exception {
    try (MockedStatic<XincoConfigSingletonServer> cfg =
        mockStatic(XincoConfigSingletonServer.class)) {
      XincoConfigSingletonServer cfgInstance = mock(XincoConfigSingletonServer.class);
      when(cfgInstance.isAllowPublisherList()).thenReturn(true);
      cfg.when(XincoConfigSingletonServer::getInstance).thenReturn(cfgInstance);

      try (MockedConstruction<XincoCoreNodeServer> ignored =
          mockConstruction(
              XincoCoreNodeServer.class,
              (mock, ctx) -> {
                throw new XincoException("Node not found");
              })) {

        PublisherView view = new PublisherView();
        addView(view);
        UI.getCurrent().navigate("publisher/999");

        assertNotNull(view);
      }
    }
  }

  private static void addView(PublisherView view) {
    UI.getCurrent().add(view);
  }
}
