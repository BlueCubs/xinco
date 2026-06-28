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

  /**
   * Sets the display index.
   *
   * @param index the display index
   * @return this builder
   */
  public XincoMenuItemBuilder setIndex(int index) {
    this.index = index;
    return this;
  }

  /**
   * Sets the group name.
   *
   * @param groupName the group name
   * @return this builder
   */
  public XincoMenuItemBuilder setGroupName(String groupName) {
    this.groupName = groupName;
    return this;
  }

  /**
   * Sets the display name.
   *
   * @param name the display name
   * @return this builder
   */
  public XincoMenuItemBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the icon.
   *
   * @param icon the icon
   * @return this builder
   */
  public XincoMenuItemBuilder setIcon(ThemeResource icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Sets the menu command.
   *
   * @param command the menu command
   * @return this builder
   */
  public XincoMenuItemBuilder setCommand(Command command) {
    this.command = command;
    return this;
  }

  /**
   * Sets whether item requires the user to be logged in.
   *
   * @param loggedIn whether login is required
   * @return this builder
   */
  public XincoMenuItemBuilder setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
    return this;
  }

  /**
   * Sets whether item applies to data nodes only.
   *
   * @param dataOnly true if data-nodes only
   * @return this builder
   */
  public XincoMenuItemBuilder setDataOnly(boolean dataOnly) {
    this.dataOnly = dataOnly;
    return this;
  }

  /**
   * Sets whether item applies to folder nodes only.
   *
   * @param nodeOnly true if folder-nodes only
   * @return this builder
   */
  public XincoMenuItemBuilder setNodeOnly(boolean nodeOnly) {
    this.nodeOnly = nodeOnly;
    return this;
  }

  /**
   * Sets whether the item is pre-selected.
   *
   * @param selected true if pre-selected
   * @return this builder
   */
  public XincoMenuItemBuilder setSelected(boolean selected) {
    this.selected = selected;
    return this;
  }

  /**
   * Sets the valid data types for this item.
   *
   * @param validDataTypes the valid data types
   * @return this builder
   */
  public XincoMenuItemBuilder setValidDataTypes(int[] validDataTypes) {
    this.validDataTypes = validDataTypes;
    return this;
  }

  /**
   * Builds and returns the configured menu item.
   *
   * @return the constructed menu item
   */
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
