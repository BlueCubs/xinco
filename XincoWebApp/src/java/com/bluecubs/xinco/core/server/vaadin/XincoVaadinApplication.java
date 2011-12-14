package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.service.XincoCoreUser;
import com.bluecubs.xinco.core.server.service.Xinco_Service;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.ResizeListener;
import java.net.MalformedURLException;
import java.util.ResourceBundle;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public interface XincoVaadinApplication extends ResizeListener {

    /**
     * @return the loggedUser
     */
    XincoCoreUser getLoggedUser();

    /**
     * @return the Resource Bundle
     */
    ResourceBundle getResource();

    /**
     * Reset activity timer
     */
    void resetTimer();
    
    /**
     * Get the tree of the application
     * @return Tree
     */
    Tree getXincoTree();

    /**
     * Get main window
     * @return Main Window
     */
    public Window getMainWindow();
    
    /**
     * Get service
     * @return Xinco Service
     * @throws MalformedURLException 
     */
    public Xinco_Service getService() throws MalformedURLException;
}
