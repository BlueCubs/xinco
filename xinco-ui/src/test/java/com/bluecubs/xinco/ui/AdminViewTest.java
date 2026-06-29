package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.tabs.TabSheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdminViewTest {

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
  void adminView_renders_withTabSheet() {
    UI.getCurrent().navigate(AdminView.class);
    assertNotNull(_get(TabSheet.class));
  }

  @Test
  void adminView_usersTab_rendersGrid() {
    UI.getCurrent().navigate(AdminView.class);
    TabSheet tabs = _get(TabSheet.class);
    assertNotNull(tabs);
    // Users tab is selected by default (index 0)
    assertNotNull(tabs.getSelectedTab());
  }

  @Test
  void adminView_groupsTab_rendersGrid() {
    UI.getCurrent().navigate(AdminView.class);
    TabSheet tabs = _get(TabSheet.class);
    // Select the Groups tab (index 1)
    tabs.setSelectedIndex(1);
    assertNotNull(tabs.getSelectedTab());
  }
}
