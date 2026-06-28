package com.bluecubs.xinco.core.server.vaadin;

import static com.bluecubs.xinco.core.server.vaadin.Xinco.getInstance;

import com.vaadin.terminal.ThemeResource;

/**
 * Represents a menu item in the Xinco DMS menu bar.
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoMenuItem {

  private final int index;
  private final String groupName;
  private final String name;
  private final ThemeResource icon;
  private final com.vaadin.ui.MenuBar.Command command;
  private final boolean loggedIn;
  private final boolean dataOnly;
  private final boolean nodeOnly;
  private int[] dataTypes, statuses;
  private final boolean selected;

  /**
   * Creates a menu item.
   *
   * @param index Order of the item
   * @param groupName Name of the item group (Resource Bundle key)
   * @param name Name of the item (Resource Bundle key)
   * @param icon Icon for the item (null for none)
   * @param command Command to be performed
   * @param loggedIn User needs to be logged in to use
   * @param dataOnly Only for data objects
   * @param nodeOnly Only for node objects
   * @param selected Does something needs to be selected to enable this item?
   * @param validDataTypes Valid types
   */
  protected XincoMenuItem(
      int index,
      String groupName,
      String name,
      ThemeResource icon,
      com.vaadin.ui.MenuBar.Command command,
      boolean loggedIn,
      boolean dataOnly,
      boolean nodeOnly,
      boolean selected,
      int... validDataTypes) {
    this.index = index;
    this.groupName = groupName;
    this.name = name;
    this.icon = icon;
    this.command = command;
    this.loggedIn = loggedIn;
    this.dataOnly = dataOnly;
    this.nodeOnly = nodeOnly;
    this.selected = selected;
    this.dataTypes = validDataTypes;
  }

  /**
   * Returns the item index.
   *
   * @return the item index
   */
  public int getIndex() {
    return index;
  }

  /**
   * Returns the item name.
   *
   * @return the item name
   */
  public String getName() {
    return getInstance().getResource().getString(name);
  }

  /**
   * Returns the item icon.
   *
   * @return the item icon
   */
  public ThemeResource getIcon() {
    return icon;
  }

  /**
   * Returns the menu command.
   *
   * @return the menu command
   */
  public com.vaadin.ui.MenuBar.Command getCommand() {
    return command;
  }

  /**
   * Returns whether item requires login.
   *
   * @return true if item requires login
   */
  public boolean isLoggedIn() {
    return loggedIn;
  }

  /**
   * Returns whether item applies to data nodes only.
   *
   * @return true if item applies to data nodes only
   */
  public boolean isDataOnly() {
    return dataOnly;
  }

  /**
   * Returns whether item applies to folder nodes only.
   *
   * @return true if item applies to folder nodes only
   */
  public boolean isNodeOnly() {
    return nodeOnly;
  }

  /**
   * Returns the valid data types for this item.
   *
   * @return the valid data types
   */
  public int[] getDataTypes() {
    return dataTypes;
  }

  /**
   * Sets the valid data types. Only meaningful for non-node objects.
   *
   * @param dataTypes the data types to set
   */
  public void setDataTypes(int[] dataTypes) {
    this.dataTypes = dataTypes;
  }

  /**
   * Returns the group name.
   *
   * @return the group name
   */
  public String getGroupName() {
    return getInstance().getResource().getString(groupName);
  }

  /**
   * Returns whether the item is selected.
   *
   * @return true if the item is selected
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Returns the valid status codes.
   *
   * @return the valid status codes
   */
  public int[] getStatuses() {
    return statuses;
  }

  /**
   * Sets the valid status codes.
   *
   * @param statuses the statuses to set
   */
  public void setStatuses(int[] statuses) {
    this.statuses = statuses;
  }
}
