package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreACEServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.service.XincoCoreACE;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoMenuItemManager {

    private static TreeMap<Integer, XincoMenuItem> items =
            new TreeMap<Integer, XincoMenuItem>();
    private static TreeMap<String, MenuItem> groups = new TreeMap<String, MenuItem>();

    public static XincoMenuItem addItem(XincoMenuItem item) {
        return items.put(item.getIndex(), item);
    }

    public static void updateMenuBar(Xinco xinco, MenuBar menu) {
        menu.removeItems();
        groups.clear();
        for (Iterator<Integer> it = items.keySet().iterator(); it.hasNext();) {
            //TreeMap has keys already sorted
            XincoMenuItem item = items.get(it.next());
            if (!groups.containsKey(item.getGroupName())) {
                //Group not in the menu bar yet
                groups.put(item.getGroupName(), menu.addItem(item.getGroupName(), null));
            }
            if (canAdd(xinco, item)) {
                groups.get(item.getGroupName()).addItem(item.getName(), item.getCommand());
            }
        }
    }

    private static boolean canAdd(Xinco xinco, XincoMenuItem item) {
        String selection = xinco.getXincoTree().getValue() == null ? null : xinco.getXincoTree().getValue().toString();
        boolean hasAccess = false;
        XincoCoreACE tempAce = null;
        if (xinco.getLoggedUser() != null && selection != null && selection.startsWith("node")) {
            XincoCoreNodeServer node;
            try {
                node = new XincoCoreNodeServer(Integer.valueOf(selection.substring(selection.indexOf('-') + 1)));
            } catch (XincoException ex) {
                Logger.getLogger(XincoMenuItemManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
                return false;
            }

            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(xinco.getLoggedUser().getId()),
                        (ArrayList) node.getXincoCoreAcl());
            } catch (XincoException ex) {
                Logger.getLogger(XincoMenuItemManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            }
        } else if (xinco.getLoggedUser() != null && selection != null && selection.startsWith("data")) {
            XincoCoreDataServer data;
            try {
                data = new XincoCoreDataServer(Integer.valueOf(selection.substring(selection.indexOf('-') + 1)));
            } catch (XincoException ex) {
                Logger.getLogger(XincoMenuItemManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
                return false;
            }
            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(xinco.getLoggedUser().getId()),
                        (ArrayList) data.getXincoCoreAcl());
            } catch (XincoException ex) {
                Logger.getLogger(XincoMenuItemManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            }
        }

        if (tempAce != null) {
            hasAccess = tempAce.isWritePermission();
        }

        if ((item.isDataOnly() && selection == null)
                || (item.isDataOnly() && !selection.startsWith("data"))) {
            return false;
        }

        if ((item.isNodeOnly() && selection == null)
                || (item.isNodeOnly() && !selection.startsWith("node"))) {
            return false;
        }

        if (item.isLoggedIn() && xinco.getLoggedUser() == null) {
            return false;
        }
        
        if (item.isLoggedIn() && xinco.getLoggedUser() != null) {
            return hasAccess;
        }
        return true;
    }
}
