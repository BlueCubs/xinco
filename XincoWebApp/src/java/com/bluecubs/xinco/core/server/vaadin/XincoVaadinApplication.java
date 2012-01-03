package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.service.XincoCoreUser;
import com.bluecubs.xinco.core.server.service.XincoWebService;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.ResizeListener;
import java.net.MalformedURLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public interface XincoVaadinApplication extends ResizeListener {

    /**
     * @return the loggedUser
     */
    public XincoCoreUser getLoggedUser();

    /**
     * @return the Resource Bundle
     */
    public ResourceBundle getResource();

    /**
     * Reset activity timer
     */
    public void resetTimer();

    /**
     * Get the tree of the application
     *
     * @return Tree
     */
    public Tree getXincoTree();

    /**
     * Get main window
     *
     * @return Main Window
     */
    public Window getMainWindow();

    /**
     * Get service
     *
     * @return Xinco Service
     * @throws MalformedURLException
     */
    public XincoWebService getService();

    /**
     * Expand a node in the tree
     *
     * @param parents Node IDs to expand
     * @return true if successful
     */
    public boolean expandTreeNodes(List<Integer> parents);

    /**
     * Select node in tree
     *
     * @param nodeId Id to select (use "node-" for nodes and "data-" for data)
     * @return true if successful
     */
    public boolean selectNode(String nodeId);

    /**
     * Lock the application (requires log in or disconnection)
     */
    public void setLock();

    /**
     * Add the default attributes for this data.
     * Warning: this removes all previous attributes (if any)
     * @param data XincoCoredata to modify
     */
    public void addDefaultAddAttributes(com.bluecubs.xinco.core.server.service.XincoCoreData data);
}
