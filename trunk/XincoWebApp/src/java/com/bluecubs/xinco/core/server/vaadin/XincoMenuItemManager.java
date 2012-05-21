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
 * @author Javier A. Ortiz Bultron<javier.ortiz.78@gmail.com>
 */
public class XincoMenuItemManager {

    private static TreeMap<Integer, XincoMenuItem> items =
            new TreeMap<Integer, XincoMenuItem>();
    private static TreeMap<String, MenuItem> groups = new TreeMap<String, MenuItem>();

    public static XincoMenuItem addItem(XincoMenuItem item) {
        return items.put(item.getIndex(), item);
    }

    public static void updateMenuBar(MenuBar menu) {
        menu.removeItems();
        groups.clear();
        for (Iterator<Integer> it = items.keySet().iterator(); it.hasNext();) {
            //TreeMap has keys already sorted
            XincoMenuItem item = items.get(it.next());
            if (!groups.containsKey(item.getGroupName())) {
                //Group not in the menu bar yet
                groups.put(item.getGroupName(), menu.addItem(item.getGroupName(), null));
            }
            if (canAdd(item)) {
                groups.get(item.getGroupName()).addItem(item.getName(), item.getCommand());
            }
        }
    }

    private static boolean canAdd(XincoMenuItem item) {
        String selection = Xinco.getInstance().getXincoTree().getValue() == null ? null : Xinco.getInstance().getXincoTree().getValue().toString();
        boolean add = true;
        boolean hasAccess = false;
        XincoCoreACE tempAce = null;
        if (Xinco.getInstance().getLoggedUser() != null && selection != null && selection.startsWith("node")) {
            XincoCoreNodeServer node = null;
            try {
                node = new XincoCoreNodeServer(Integer.valueOf(selection.substring(selection.indexOf('-') + 1)));
            } catch (XincoException ex) {
                Logger.getLogger(XincoMenuItemManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
                add = false;
            }

            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(Xinco.getInstance().getLoggedUser().getId()),
                        (ArrayList) node.getXincoCoreAcl());
            } catch (XincoException ex) {
                Logger.getLogger(XincoMenuItemManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            }
        } else if (Xinco.getInstance().getLoggedUser() != null && selection != null && selection.startsWith("data")) {
            XincoCoreDataServer data = null;
            try {
                data = new XincoCoreDataServer(Integer.valueOf(selection.substring(selection.indexOf('-') + 1)));
            } catch (XincoException ex) {
                Logger.getLogger(XincoMenuItemManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
                add = false;
            }
            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(Xinco.getInstance().getLoggedUser().getId()),
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
            add = false;
        }

        if ((item.isNodeOnly() && selection == null)
                || (item.isNodeOnly() && !selection.startsWith("node"))) {
            add = false;
        }

        if (item.isLoggedIn() && Xinco.getInstance().getLoggedUser() == null) {
            add = false;
        }

        if (item.isLoggedIn() && Xinco.getInstance().getLoggedUser() != null && !hasAccess) {
            add = false;
        }

        if (item.isSelected() && selection == null) {
            add = false;
        }

        if (selection != null && selection.startsWith("data")
                && item.getDataTypes() != null) {
            XincoCoreDataServer temp = null;
            try {
                temp = new XincoCoreDataServer(Integer.valueOf(selection.substring(selection.indexOf('-') + 1)));
            } catch (XincoException ex) {
                Logger.getLogger(XincoMenuItemManager.class.getName()).log(Level.SEVERE, null, ex);
                add = false;
            }
            boolean found = false;
            for (int x : item.getDataTypes()) {
                if (x == temp.getXincoCoreDataType().getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                add = false;
            }
        }

        if (selection != null && selection.startsWith("data")
                && item.getStatuses() != null) {
            XincoCoreDataServer temp = null;
            try {
                temp = new XincoCoreDataServer(Integer.valueOf(selection.substring(selection.indexOf('-') + 1)));
            } catch (XincoException ex) {
                Logger.getLogger(XincoMenuItemManager.class.getName()).log(Level.SEVERE, null, ex);
                add = false;
            }
            boolean found = false;
            for (int x : item.getStatuses()) {
                if (x == temp.getStatusNumber()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                add = false;
            }
        }

        return add;
    }

    private XincoMenuItemManager() {
    }
}
