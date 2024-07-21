/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.vaadin;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.MenuBar.Command;

public class XincoMenuItemBuilder {

  private int index;
  private String groupName;
  private String name;
  private ThemeResource icon;
  private Command command;
  private boolean loggedIn;
  private boolean dataOnly;
  private boolean nodeOnly;
  private boolean selected;
  private int[] validDataTypes = null;

  public XincoMenuItemBuilder() {}

  public XincoMenuItemBuilder setIndex(int index) {
    this.index = index;
    return this;
  }

  public XincoMenuItemBuilder setGroupName(String groupName) {
    this.groupName = groupName;
    return this;
  }

  public XincoMenuItemBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public XincoMenuItemBuilder setIcon(ThemeResource icon) {
    this.icon = icon;
    return this;
  }

  public XincoMenuItemBuilder setCommand(Command command) {
    this.command = command;
    return this;
  }

  public XincoMenuItemBuilder setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
    return this;
  }

  public XincoMenuItemBuilder setDataOnly(boolean dataOnly) {
    this.dataOnly = dataOnly;
    return this;
  }

  public XincoMenuItemBuilder setNodeOnly(boolean nodeOnly) {
    this.nodeOnly = nodeOnly;
    return this;
  }

  public XincoMenuItemBuilder setSelected(boolean selected) {
    this.selected = selected;
    return this;
  }

  public XincoMenuItemBuilder setValidDataTypes(int[] validDataTypes) {
    this.validDataTypes = validDataTypes;
    return this;
  }

  public XincoMenuItem createXincoMenuItem() {
    return new XincoMenuItem(
        index,
        groupName,
        name,
        icon,
        command,
        loggedIn,
        dataOnly,
        nodeOnly,
        selected,
        validDataTypes);
  }
}
