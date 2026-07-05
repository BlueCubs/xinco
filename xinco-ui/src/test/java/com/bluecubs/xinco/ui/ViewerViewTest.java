package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.*;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import java.lang.reflect.Field;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewerViewTest {

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
  void viewerView_renders_withSplitLayout() {
    addView(new ViewerView(new UserSession()));
    assertNotNull(_get(SplitLayout.class));
  }

  @Test
  void viewerView_hasExplorerMenuItem() {
    addView(new ViewerView(new UserSession()));
    MenuBar menuBar = _get(MenuBar.class);
    assertTrue(
        menuBar.getItems().stream().anyMatch(item -> "Explorer".equals(item.getText())),
        "MenuBar should have an 'Explorer' item");
  }

  @Test
  void viewerView_previewPane_showsPlaceholderOnLoad() throws Exception {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    Field f = ViewerView.class.getDeclaredField("viewerPane");
    f.setAccessible(true);
    Div viewerPane = (Div) f.get(view);

    boolean hasPlaceholder =
        viewerPane
            .getChildren()
            .filter(c -> c instanceof Span)
            .map(c -> (Span) c)
            .anyMatch(s -> s.getText().contains("Select a file"));
    assertTrue(hasPlaceholder, "viewerPane should show placeholder text on load");
  }

  private static void addView(ViewerView view) {
    UI.getCurrent().add(view);
  }
}
