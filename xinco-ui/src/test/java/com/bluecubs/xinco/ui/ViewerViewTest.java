package com.bluecubs.xinco.ui;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreLanguage;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.VaadinSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

class ViewerViewTest {

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
  void viewerView_renders_withSplitLayout() {
    addView(new ViewerView(new UserSession()));
    assertNotNull(_get(SplitLayout.class));
  }

  @Test
  void viewerView_hasExplorerMenuItem() {
    addView(new ViewerView(new UserSession()));
    MenuBar menuBar = _get(MenuBar.class);
    assertTrue(
        menuBar.getItems().stream().anyMatch(item -> "Repository".equals(item.getText())),
        "MenuBar should have a 'Repository' item");
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
            .anyMatch(s -> s.getText().contains("Select file"));
    assertTrue(hasPlaceholder, "viewerPane should show placeholder text on load");
  }

  // ---- showPreview coverage ----

  @Test
  void showPreview_typeNot1_coversPropertyGridBranch() {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockType.getId()).thenReturn(2);
    when(mockType.getDesignation()).thenReturn("Text");
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);
    XincoCoreLanguage mockLang = mock(XincoCoreLanguage.class);
    when(mockLang.getSign()).thenReturn("EN");
    when(mockData.getXincoCoreLanguage()).thenReturn(mockLang);
    when(mockData.getDesignation()).thenReturn("note.txt");
    when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>());
    when(mockData.getXincoCoreLogs()).thenReturn(new ArrayList<>());

    view.showPreview(mockData);

    // Non-null check: PropertyGrid should be added to viewerPane
    Field f;
    try {
      f = ViewerView.class.getDeclaredField("viewerPane");
      f.setAccessible(true);
      Div pane = (Div) f.get(view);
      assertFalse(pane.getChildren().findAny().isEmpty(), "viewerPane should have content");
    } catch (Exception e) {
      fail("reflection failed: " + e.getMessage());
    }
  }

  @Test
  void showPreview_type1_nullPath_coversNullBranch() throws Exception {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockType.getId()).thenReturn(1);
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);
    when(mockData.getId()).thenReturn(1);
    when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>());
    when(mockData.getDesignation()).thenReturn("file.txt");

    try (MockedStatic<XincoCoreDataServer> ms = mockStatic(XincoCoreDataServer.class)) {
      ms.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt())).thenReturn(null);
      view.showPreview(mockData);
    }

    Field f = ViewerView.class.getDeclaredField("viewerPane");
    f.setAccessible(true);
    Div pane = (Div) f.get(view);
    boolean hasNoVersionSpan =
        pane.getChildren()
            .filter(c -> c instanceof Span)
            .map(c -> (Span) c)
            .anyMatch(s -> s.getText().contains("Updating/Accessing"));
    assertTrue(hasNoVersionSpan, "Should show update-failed message when path is null");
  }

  @Test
  void showPreview_type1_fileNotFound_coversFileNotFoundBranch() throws Exception {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockType.getId()).thenReturn(1);
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);
    when(mockData.getId()).thenReturn(1);
    when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>());
    when(mockData.getDesignation()).thenReturn("missing.txt");

    try (MockedStatic<XincoCoreDataServer> ms = mockStatic(XincoCoreDataServer.class)) {
      ms.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt()))
          .thenReturn("/nonexistent/path/that/does/not/exist.txt");
      view.showPreview(mockData);
    }

    Field f = ViewerView.class.getDeclaredField("viewerPane");
    f.setAccessible(true);
    Div pane = (Div) f.get(view);
    boolean hasNotFoundSpan =
        pane.getChildren()
            .filter(c -> c instanceof Span)
            .map(c -> (Span) c)
            .anyMatch(s -> s.getText().contains("Downloading file"));
    assertTrue(hasNotFoundSpan, "Should show download-failed message when file does not exist");
  }

  @Test
  void showPreview_type1_imageFile_coversImageRenderBranch() throws Exception {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    Path tempFile = Files.createTempFile("xinco-viewer-test", ".png");
    Files.write(tempFile, new byte[] {(byte) 0x89, 0x50, 0x4e, 0x47}); // PNG header bytes
    try {
      XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
      XincoCoreDataType mockType = mock(XincoCoreDataType.class);
      when(mockType.getId()).thenReturn(1);
      when(mockData.getXincoCoreDataType()).thenReturn(mockType);
      when(mockData.getId()).thenReturn(1);
      when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>());
      when(mockData.getDesignation()).thenReturn("image.png");

      try (MockedStatic<XincoCoreDataServer> ms = mockStatic(XincoCoreDataServer.class)) {
        ms.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt()))
            .thenReturn(tempFile.toString());
        view.showPreview(mockData);
      }

      Field f = ViewerView.class.getDeclaredField("viewerPane");
      f.setAccessible(true);
      Div pane = (Div) f.get(view);
      boolean hasImage = pane.getChildren().anyMatch(c -> c instanceof Image);
      assertTrue(hasImage, "viewerPane should contain an Image component for PNG files");
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  void showPreview_type1_pdfFile_coversIframeBranch() throws Exception {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    Path tempFile = Files.createTempFile("xinco-viewer-test", ".pdf");
    Files.writeString(tempFile, "%PDF-1.4 test");
    try {
      XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
      XincoCoreDataType mockType = mock(XincoCoreDataType.class);
      when(mockType.getId()).thenReturn(1);
      when(mockData.getXincoCoreDataType()).thenReturn(mockType);
      when(mockData.getId()).thenReturn(1);
      when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>());
      when(mockData.getDesignation()).thenReturn("document.pdf");

      try (MockedStatic<XincoCoreDataServer> ms = mockStatic(XincoCoreDataServer.class)) {
        ms.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt()))
            .thenReturn(tempFile.toString());
        view.showPreview(mockData);
      }

      Field f = ViewerView.class.getDeclaredField("viewerPane");
      f.setAccessible(true);
      Div pane = (Div) f.get(view);
      // PDF → iframe path → element added to pane's element, not as a direct Vaadin child
      // The pane should not be empty
      assertFalse(
          pane.getChildren().findAny().isEmpty() && pane.getElement().getChildCount() == 0,
          "viewerPane should have content for PDF file");
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  // ---- detectMimeType coverage ----

  @Test
  void detectMimeType_pdf_returnsApplicationPdf() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.pdf");
    assertEquals("application/pdf", m.invoke(null, "file.pdf", p));
  }

  @Test
  void detectMimeType_png_returnsImagePng() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.png");
    assertEquals("image/png", m.invoke(null, "img.png", p));
  }

  @Test
  void detectMimeType_jpg_returnsImageJpeg() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.jpg");
    assertEquals("image/jpeg", m.invoke(null, "photo.jpg", p));
  }

  @Test
  void detectMimeType_jpeg_returnsImageJpeg() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.jpeg");
    assertEquals("image/jpeg", m.invoke(null, "photo.jpeg", p));
  }

  @Test
  void detectMimeType_gif_returnsImageGif() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.gif");
    assertEquals("image/gif", m.invoke(null, "anim.gif", p));
  }

  @Test
  void detectMimeType_svg_returnsImageSvg() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.svg");
    assertEquals("image/svg+xml", m.invoke(null, "diagram.svg", p));
  }

  @Test
  void detectMimeType_txt_returnsTextPlain() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.txt");
    assertEquals("text/plain", m.invoke(null, "readme.txt", p));
  }

  @Test
  void detectMimeType_html_returnsTextHtml() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.html");
    assertEquals("text/html", m.invoke(null, "page.html", p));
  }

  @Test
  void detectMimeType_htm_returnsTextHtml() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.htm");
    assertEquals("text/html", m.invoke(null, "page.htm", p));
  }

  @Test
  void detectMimeType_unknown_returnsOctetStream() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/dummy.xncounknown");
    assertEquals("application/octet-stream", m.invoke(null, "data.xncounknown", p));
  }

  @Test
  void detectMimeType_noExtension_returnsOctetStream() throws Exception {
    Method m = ViewerView.class.getDeclaredMethod("detectMimeType", String.class, Path.class);
    m.setAccessible(true);
    Path p = Path.of("/tmp/noext");
    assertEquals("application/octet-stream", m.invoke(null, "noext", p));
  }

  // ---- loadRootNodes coverage ----

  @Test
  void loadRootNodes_nodeConstructorThrows_coversErrorBranch() throws Exception {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    // XincoCoreNodeServer(1) will throw without a DB — this exercises the catch block
    Method m = ViewerView.class.getDeclaredMethod("loadRootNodes");
    m.setAccessible(true);
    assertDoesNotThrow(() -> m.invoke(view), "loadRootNodes should swallow the exception");
  }

  @Test
  void loadRootNodes_withMockedNode_coversSuccessPath() throws Exception {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    try (MockedConstruction<XincoCoreNodeServer> mc =
        mockConstruction(
            XincoCoreNodeServer.class,
            (m, ctx) -> {
              when(m.getDesignation()).thenReturn("Root");
              when(m.getXincoCoreNodes()).thenReturn(new ArrayList<>());
              doNothing().when(m).fillXincoCoreNodes();
            })) {
      Method m = ViewerView.class.getDeclaredMethod("loadRootNodes");
      m.setAccessible(true);
      assertDoesNotThrow(() -> m.invoke(view));
    }
  }

  // ---- no-arg constructor / resolveSession ----

  @Test
  void viewerView_noArgConstructor_createsView() {
    // exercises resolveSession() and the no-arg <init>
    ViewerView view = new ViewerView();
    assertDoesNotThrow(() -> UI.getCurrent().add(view));
  }

  // ---- beforeEnter coverage ----

  @Test
  void beforeEnter_noSession_reroutesToLogin() {
    VaadinSession.getCurrent().setAttribute(UserSession.class, null);
    ViewerView view = new ViewerView(new UserSession());
    addView(view);
    BeforeEnterEvent event = mock(BeforeEnterEvent.class);
    view.beforeEnter(event);
    verify(event).rerouteTo(LoginView.class);
  }

  @Test
  void beforeEnter_loggedInSession_updatesSession() throws Exception {
    UserSession loggedIn = new UserSession();
    loggedIn.setUser(mock(com.bluecubs.xinco.core.server.XincoCoreUserServer.class));
    VaadinSession.getCurrent().setAttribute(UserSession.class, loggedIn);
    ViewerView view = new ViewerView(new UserSession());
    addView(view);
    BeforeEnterEvent event = mock(BeforeEnterEvent.class);
    view.beforeEnter(event);
    java.lang.reflect.Field f = ViewerView.class.getDeclaredField("session");
    f.setAccessible(true);
    assertSame(loggedIn, f.get(view));
  }

  // ---- afterNavigation coverage ----

  @Test
  void afterNavigation_callsLoadRootNodes() {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);
    AfterNavigationEvent event = mock(AfterNavigationEvent.class);
    assertDoesNotThrow(() -> view.afterNavigation(event));
  }

  // ---- openInTab coverage ----

  @Test
  void openInTab_nullPath_returns() throws Exception {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);
    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    when(mockData.getId()).thenReturn(1);
    try (MockedStatic<XincoCoreDataServer> ms = mockStatic(XincoCoreDataServer.class)) {
      ms.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt())).thenReturn(null);
      Method m = ViewerView.class.getDeclaredMethod("openInTab", XincoCoreDataServer.class);
      m.setAccessible(true);
      assertDoesNotThrow(() -> m.invoke(view, mockData));
    }
  }

  @Test
  void openInTab_existingFile_coversSuccessPath() throws Exception {
    ViewerView view = new ViewerView(new UserSession());
    addView(view);
    Path tempFile = Files.createTempFile("xinco-tab-test", ".txt");
    Files.writeString(tempFile, "test content");
    try {
      XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
      when(mockData.getId()).thenReturn(1);
      when(mockData.getDesignation()).thenReturn("test.txt");
      when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>());
      try (MockedStatic<XincoCoreDataServer> ms = mockStatic(XincoCoreDataServer.class)) {
        ms.when(() -> XincoCoreDataServer.getLastMajorVersionDataPath(anyInt()))
            .thenReturn(tempFile.toString());
        Method m = ViewerView.class.getDeclaredMethod("openInTab", XincoCoreDataServer.class);
        m.setAccessible(true);
        assertDoesNotThrow(() -> m.invoke(view, mockData));
      }
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  private static void addView(ViewerView view) {
    UI.getCurrent().add(view);
  }

  // ── Gap documentation tests ────────────────────────────────────────────────

  @Test
  @SuppressWarnings("unchecked")
  void nodeSelected_viewerPaneShouldShowFolderProperties() throws Exception {
    // §2.3: "A table displaying details about the currently selected object (right)."
    // Currently selecting a node fills only the data grid; the right viewerPane stays
    // as the placeholder Span "Select a file to preview it here."
    UserSession session = mock(UserSession.class);
    when(session.isLoggedIn()).thenReturn(true);
    ViewerView view = new ViewerView(session);
    addView(view);

    XincoCoreNodeServer mockNode = mock(XincoCoreNodeServer.class);
    when(mockNode.getId()).thenReturn(5);
    when(mockNode.getDesignation()).thenReturn("Reports");
    when(mockNode.getXincoCoreData()).thenReturn(new ArrayList<>());

    com.vaadin.flow.component.treegrid.TreeGrid<XincoCoreNodeServer> tree =
        (com.vaadin.flow.component.treegrid.TreeGrid<XincoCoreNodeServer>) _get(TreeGrid.class);
    tree.setItems(List.of(mockNode), n -> List.of());
    tree.asSingleSelect().setValue(mockNode);

    Field f = ViewerView.class.getDeclaredField("viewerPane");
    f.setAccessible(true);
    Div viewerPane = (Div) f.get(view);

    // After node selection viewerPane should contain folder info — not the static placeholder.
    boolean hasOnlyPlaceholder =
        viewerPane.getComponentCount() == 1
            && viewerPane.getComponentAt(0) instanceof Span sp
            && sp.getText().contains("Select a file");
    assertFalse(hasOnlyPlaceholder, "viewerPane must show folder details when a node is selected");
  }

  @Test
  void showPreview_type2_showsTextArea() {
    // §2.9: text items store attribText (attribute_id=1); viewer shows content inline.
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockType.getId()).thenReturn(2);
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);

    com.bluecubs.xinco.server.service.XincoAddAttribute attr =
        mock(com.bluecubs.xinco.server.service.XincoAddAttribute.class);
    when(attr.getAttributeId()).thenReturn(1);
    when(attr.getAttribText()).thenReturn("Hello, world!");
    when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>(List.of(attr)));

    view.showPreview(mockData);

    assertFalse(
        _find(com.vaadin.flow.component.textfield.TextArea.class).isEmpty(),
        "viewerPane must show a TextArea for type=2 text items");
  }

  @Test
  void urlDataItem_shouldOpenInBrowser() throws Exception {
    // §2.10: selecting a URL data item renders an Anchor in viewerPane (type=3, attrib id=1).
    ViewerView view = new ViewerView(new UserSession());
    addView(view);

    XincoCoreDataServer mockData = mock(XincoCoreDataServer.class);
    XincoCoreDataType mockType = mock(XincoCoreDataType.class);
    when(mockType.getId()).thenReturn(3);
    when(mockData.getXincoCoreDataType()).thenReturn(mockType);

    com.bluecubs.xinco.server.service.XincoAddAttribute attr =
        mock(com.bluecubs.xinco.server.service.XincoAddAttribute.class);
    when(attr.getAttributeId()).thenReturn(1);
    when(attr.getAttribVarchar()).thenReturn("https://example.com");
    when(mockData.getXincoAddAttributes()).thenReturn(new ArrayList<>(List.of(attr)));

    view.showPreview(mockData);

    assertFalse(
        _find(com.vaadin.flow.component.html.Anchor.class).isEmpty(),
        "viewerPane must contain a clickable Anchor when a URL data item is selected");
  }
}
