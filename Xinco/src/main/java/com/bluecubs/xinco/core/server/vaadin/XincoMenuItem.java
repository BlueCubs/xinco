package com.bluecubs.xinco.core.server.vaadin;

import static com.bluecubs.xinco.core.server.vaadin.Xinco.getInstance;

import com.vaadin.terminal.ThemeResource;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a menu item in the Xinco DMS menu bar.
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoMenuItem {

  @Getter private final int index;
  private final String groupName;
  private final String name;
  @Getter private final ThemeResource icon;
  @Getter private final com.vaadin.ui.MenuBar.Command command;
  @Getter private final boolean loggedIn;
  @Getter private final boolean dataOnly;
  @Getter private final boolean nodeOnly;
  @Getter @Setter private int[] dataTypes, statuses;
  @Getter private final boolean selected;

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
   * Returns the item name.
   *
   * @return the item name
   */
  public String getName() {
    return getInstance().getResource().getString(name);
  }

  /**
   * Returns the group name.
   *
   * @return the group name
   */
  public String getGroupName() {
    return getInstance().getResource().getString(groupName);
  }
}
