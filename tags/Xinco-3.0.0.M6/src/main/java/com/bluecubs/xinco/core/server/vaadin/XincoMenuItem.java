package com.bluecubs.xinco.core.server.vaadin;

import com.vaadin.terminal.ThemeResource;

/**
 *
 * @author Javier A. Ortiz Bultron<javier.ortiz.78@gmail.com>
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
     */
    protected XincoMenuItem(int index, String groupName, String name,
            ThemeResource icon, com.vaadin.ui.MenuBar.Command command,
            boolean loggedIn, boolean dataOnly, boolean nodeOnly,
            boolean selected, int... validDataTypes) {
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
     * @return the pos
     */
    /**
     * @return the pos
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the name
     */
    /**
     * @return the name
     */
    public String getName() {
        return Xinco.getInstance().getResource().getString(name);
    }

    /**
     * @return the icon
     */
    /**
     * @return the icon
     */
    public ThemeResource getIcon() {
        return icon;
    }

    /**
     * @return the command
     */
    /**
     * @return the command
     */
    public com.vaadin.ui.MenuBar.Command getCommand() {
        return command;
    }

    /**
     * @return the loggedIn
     */
    /**
     * @return the loggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @return the dataOnly
     */
    /**
     * @return the dataOnly
     */
    public boolean isDataOnly() {
        return dataOnly;
    }

    /**
     * @return the nodeOnly
     */
    /**
     * @return the nodeOnly
     */
    public boolean isNodeOnly() {
        return nodeOnly;
    }

    /**
     * @return the dataTypes
     */
    /**
     * @return the dataTypes
     */
    public int[] getDataTypes() {
        return dataTypes;
    }

    /**
     * @param dataTypes the data types to set. Only meaning full for non node
     * objects
     */
    public void setDataTypes(int[] dataTypes) {
        this.dataTypes = dataTypes;
    }

    /**
     * @return the groupName
     */
    /**
     * @return the groupName
     */
    public String getGroupName() {
        return Xinco.getInstance().getResource().getString(groupName);
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @return the statuses
     */
    public int[] getStatuses() {
        return statuses;
    }

    /**
     * @param statuses the statuses to set
     */
    public void setStatuses(int[] statuses) {
        this.statuses = statuses;
    }
}
