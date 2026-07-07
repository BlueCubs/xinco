package com.bluecubs.xinco.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

  public MainLayout() {
    this(sessionFromVaadin());
  }

  private static UserSession sessionFromVaadin() {
    if (VaadinSession.getCurrent() == null) {
      return new UserSession();
    }
    UserSession s = VaadinSession.getCurrent().getAttribute(UserSession.class);
    return s != null ? s : new UserSession();
  }

  public MainLayout(UserSession session) {
    DrawerToggle toggle = new DrawerToggle();
    H1 title = new H1(getTranslation("general.clienttitle"));
    title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

    HorizontalLayout header = new HorizontalLayout(toggle, title);
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.setWidthFull();
    addToNavbar(header);

    SideNav nav = new SideNav();
    nav.addItem(
        new SideNavItem(
            getTranslation("menu.repository"),
            ExplorerView.class,
            VaadinIcon.FOLDER_OPEN.create()));

    if (session.isLoggedIn()) {
      Span userLabel =
          new Span(getTranslation("general.user") + ": " + session.getUser().getUsername());
      userLabel
          .getStyle()
          .set("font-size", "var(--lumo-font-size-s)")
          .set("padding", "var(--lumo-space-s)");
      nav.addItem(
          new SideNavItem(
              getTranslation("general.group.admin"), AdminView.class, VaadinIcon.COG.create()));
      nav.addItem(
          new SideNavItem(
              getTranslation("general.logout"), LoginView.class, VaadinIcon.SIGN_OUT.create()));
      addToDrawer(nav, userLabel);
    } else {
      addToDrawer(nav);
    }
  }
}
