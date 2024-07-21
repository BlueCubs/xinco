package com.bluecubs.xinco.about;

import com.bluecubs.xinco.MainLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Version;

@Route(value = "About", layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends HorizontalLayout
{

  public static final String VIEW_NAME = "About";
  private static final long serialVersionUID = 118645218340246757L;

  public AboutView()
  {
    add(VaadinIcon.INFO_CIRCLE.create());
    add(new Span(" This application is using Vaadin Flow "
            + Version.getFullVersion() + "."));

    setSizeFull();
    setJustifyContentMode(JustifyContentMode.CENTER);
    setAlignItems(Alignment.CENTER);
  }
}
