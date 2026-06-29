package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.ArrayList;
import java.util.List;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Admin — Xinco DMS")
@AnonymousAllowed
public class AdminView extends VerticalLayout {

  public AdminView() {
    setSizeFull();
    setPadding(false);

    TabSheet tabs = new TabSheet();
    tabs.setSizeFull();
    tabs.add("Users", buildUsersGrid());
    tabs.add("Groups", buildGroupsGrid());

    add(tabs);
  }

  private Grid<XincoCoreUserServer> buildUsersGrid() {
    Grid<XincoCoreUserServer> grid = new Grid<>();
    grid.setSizeFull();
    grid.addColumn(XincoCoreUserServer::getId).setHeader("ID").setWidth("60px").setFlexGrow(0);
    grid.addColumn(XincoCoreUserServer::getUsername).setHeader("Username");
    grid.addColumn(u -> u.getFirstName() + " " + u.getLastName()).setHeader("Name");
    grid.addColumn(XincoCoreUserServer::getEmail).setHeader("Email");
    grid.addColumn(u -> u.getStatusNumber() == 1 ? "Active" : "Locked").setHeader("Status");
    grid.setItems(loadUsers());
    return grid;
  }

  private Grid<XincoCoreGroupServer> buildGroupsGrid() {
    Grid<XincoCoreGroupServer> grid = new Grid<>();
    grid.setSizeFull();
    grid.addColumn(XincoCoreGroupServer::getId).setHeader("ID").setWidth("60px").setFlexGrow(0);
    grid.addColumn(XincoCoreGroupServer::getDesignation).setHeader("Name");
    grid.setItems(loadGroups());
    return grid;
  }

  @SuppressWarnings("unchecked")
  private List<XincoCoreUserServer> loadUsers() {
    try {
      ArrayList<XincoCoreUserServer> users = XincoCoreUserServer.getXincoCoreUsers();
      return users != null ? users : List.of();
    } catch (Exception e) {
      return List.of();
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private List<XincoCoreGroupServer> loadGroups() {
    try {
      ArrayList raw = XincoCoreGroupServer.getXincoCoreGroups();
      if (raw == null) return List.of();
      List<XincoCoreGroupServer> groups = new ArrayList<>();
      for (Object obj : raw) {
        if (obj instanceof XincoCoreGroupServer) groups.add((XincoCoreGroupServer) obj);
      }
      return groups;
    } catch (Exception e) {
      return List.of();
    }
  }
}
