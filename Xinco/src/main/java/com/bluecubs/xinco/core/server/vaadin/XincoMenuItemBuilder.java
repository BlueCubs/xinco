/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.vaadin;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.MenuBar.Command;

/** Builder for constructing XincoMenuItem instances. */
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

  /** Default constructor. */
  public XincoMenuItemBuilder() {}

  /** Sets the display index. */
  public XincoMenuItemBuilder setIndex(int index) {
    this.index = index;
    return this;
  }

  /** Sets the group name. */
  public XincoMenuItemBuilder setGroupName(String groupName) {
    this.groupName = groupName;
    return this;
  }

  /** Sets the display name. */
  public XincoMenuItemBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /** Sets the icon. */
  public XincoMenuItemBuilder setIcon(ThemeResource icon) {
    this.icon = icon;
    return this;
  }

  /** Sets the menu command. */
  public XincoMenuItemBuilder setCommand(Command command) {
    this.command = command;
    return this;
  }

  /** Sets whether item requires the user to be logged in. */
  public XincoMenuItemBuilder setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
    return this;
  }

  /** Sets whether item applies to data nodes only. */
  public XincoMenuItemBuilder setDataOnly(boolean dataOnly) {
    this.dataOnly = dataOnly;
    return this;
  }

  /** Sets whether item applies to folder nodes only. */
  public XincoMenuItemBuilder setNodeOnly(boolean nodeOnly) {
    this.nodeOnly = nodeOnly;
    return this;
  }

  /** Sets whether the item is pre-selected. */
  public XincoMenuItemBuilder setSelected(boolean selected) {
    this.selected = selected;
    return this;
  }

  /** Sets the valid data types for this item. */
  public XincoMenuItemBuilder setValidDataTypes(int[] validDataTypes) {
    this.validDataTypes = validDataTypes;
    return this;
  }

  /** Builds and returns the configured menu item. */
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
