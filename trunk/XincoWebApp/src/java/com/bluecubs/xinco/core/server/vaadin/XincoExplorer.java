package com.bluecubs.xinco.core.server.vaadin;

//import com.bluecubs.xinco.tools.XincoFileIconManager;
import com.bluecubs.xinco.core.server.XincoCoreACEServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoException;
import com.bluecubs.xinco.core.server.service.XincoCoreACE;
import com.bluecubs.xinco.core.server.service.XincoCoreData;
import com.bluecubs.xinco.core.server.service.XincoCoreNode;
import com.bluecubs.xinco.core.server.service.XincoVersion;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;
import com.vaadin.ui.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.Icon;
//import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoExplorer extends Application implements ValueChangeListener {

    private ResourceBundle xerb, settings;
    //client version
    private XincoVersion xincoClientVersion = null;
    //Table linking displayed item with it's id
    private Tree xincoTree = null;
    private Table xincoTable = null;
    private Window mainWindow = null;
    // Create the indicator
    final ProgressIndicator indicator =
            new ProgressIndicator(new Float(0.0));

    @Override
    public void init() {
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", Locale.getDefault());//TODO: use selected language
        settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
        xincoClientVersion = new XincoVersion();
        xincoClientVersion.setVersionHigh(Integer.parseInt(settings.getString("version.high")));
        xincoClientVersion.setVersionMid(Integer.parseInt(settings.getString("version.mid")));
        xincoClientVersion.setVersionLow(Integer.parseInt(settings.getString("version.low")));
        xincoClientVersion.setVersionPostfix(settings.getString("version.postfix"));
        mainWindow = new Window(xerb.getString("general.clienttitle") + " - "
                + xerb.getString("general.version") + " "
                + xincoClientVersion.getVersionHigh() + "."
                + xincoClientVersion.getVersionMid() + "."
                + xincoClientVersion.getVersionLow() + " "
                + xincoClientVersion.getVersionPostfix());
        mainWindow.addComponent(indicator);
// Set polling frequency to 0.5 seconds.
        indicator.setPollingInterval(500);
        SplitPanel splitpanel = new SplitPanel();
        // Set the orientation.
        splitpanel.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        // Put two components in the container.
        splitpanel.setFirstComponent(getXincoTree());
        splitpanel.setSecondComponent(getDetailTable());
        mainWindow.addComponent(splitpanel);
        setMainWindow(mainWindow);
    }

    private Tree getXincoTree() {
        if (xincoTree == null) {
//            XincoFileIconManager xfm = new XincoFileIconManager();
            xincoTree = new Tree(xerb.getString("menu.repository"));
//            xincoTree.addContainerProperty("icon", Icon.class, null);
//            xincoTree.setItemIconPropertyId("icon");
            xincoTree.addListener(new ItemClickListener() {

                @Override
                public void itemClick(ItemClickEvent event) {
                    updateTable(event.getItem());
                }
            });
            xincoTree.addListener(new ExpandListener() {

                @Override
                public void nodeExpand(ExpandEvent event) {
                    mainWindow.requestRepaintAll();
                }
            });
            XincoCoreNodeServer xcns = null;
            try {
                xcns = new XincoCoreNodeServer(1);
            } catch (XincoException ex) {
                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
            }
            XincoCoreNodeProperty rootProperty = new XincoCoreNodeProperty(xcns);
            Item root = xincoTree.addItem(rootProperty);
            xincoTree.select(rootProperty);
            for (XincoCoreNode subnode : xcns.getXincoCoreNodes()) {
                //Add childen nodes
                XincoCoreNodeProperty nodeProperty = new XincoCoreNodeProperty(subnode);
//                Item item =
                xincoTree.addItem(nodeProperty);
                // Set it to be a child.
                xincoTree.setParent(nodeProperty, rootProperty);
                xincoTree.setChildrenAllowed(nodeProperty, true);
                //Set Icon
//                DefaultTreeCellRenderer temp = new DefaultTreeCellRenderer();
//                item.getItemProperty("icon").setValue(temp.getDefaultClosedIcon());
            }
            for (XincoCoreData data : xcns.getXincoCoreData()) {
                XincoCoreDataProperty dataProperty = new XincoCoreDataProperty(data);
                //Add childen data
//                Item item =
                xincoTree.addItem(dataProperty);
                // Set it to be a child.
                xincoTree.setParent(dataProperty, rootProperty);
                // Set as leaves
                xincoTree.setChildrenAllowed(dataProperty, false);
                //Set Icon
//                if (data.getDesignation().contains(".")
//                        && data.getDesignation().substring(data.getDesignation().lastIndexOf(".") + 1,
//                        data.getDesignation().length()).length() == 3) {
//                    item.getItemProperty("icon").setValue(xfm.getIcon(
//                            data.getDesignation().substring(data.getDesignation().lastIndexOf(".") + 1,
//                            data.getDesignation().length())));
//                } else {
//                    DefaultTreeCellRenderer temp = new DefaultTreeCellRenderer();
//                    item.getItemProperty("icon").setValue(temp.getDefaultLeafIcon());
//                }
            }
            xincoTree.expandItem(root);
            xincoTree.addListener(this);
        }
        return xincoTree;
    }

    private Table getDetailTable() {
        if (xincoTable == null) {
            /*
             * Create the table with a caption.
             */
            xincoTable = new Table("Properties");
            /*
             * Define the names and data types of columns. The "default value"
             * parameter is meaningless here.
             */
            xincoTable.addContainerProperty(xerb.getString("window.repository.table.attribute"),
                    String.class, null);
            xincoTable.addContainerProperty(xerb.getString("window.repository.table.details"),
                    String.class, null);
            xincoTable.setPageLength(0);
            // Send changes in selection immediately to server.
            xincoTable.setImmediate(true);
        }
        return xincoTable;
    }

    private void updateTable(final Item item) {
        class WorkThread extends Thread {

            @Override
            public void run() {
                //Reset value to zero
                indicator.setValue(0);
                int i = 0;
                int j = 0;
                XincoCoreACE tempAce = new XincoCoreACE();
                // get ace
                try {
                    if (item.getClass().newInstance() instanceof XincoCoreNode) {
                        try {
                            tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(1),
                                    (ArrayList) ((XincoCoreNode) ((XincoCoreNodeProperty) item).getValue()).getXincoCoreAcl());
                        } catch (XincoException ex) {
                            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (InstantiationException ex) {
                    Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (item.getClass().newInstance() instanceof XincoCoreData) {
                        try {
                            tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(1),
                                    (ArrayList) ((XincoCoreData) ((XincoCoreDataProperty) item).getValue()).getXincoCoreAcl());
                        } catch (XincoException ex) {
                            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (InstantiationException ex) {
                    Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Intelligent menu
                // reset menus
//                        ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).resetItems();
//                        ((XincoMenuRepository) getExplorer().getJMenuRepository()).resetItems();
//                        // dynamic enabling
//                        if (tempAce.isWritePermission()) {
//                            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(4, true);
//                            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(4, true);
//                            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(7, true);
//                            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(7, true);
//                        }
//                        if (tempAce.isAdminPermission()) {
//                            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(4, true);
//                            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(4, true);
//                            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(6, true);
//                            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(6, true);
//                            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(7, true);
//                            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(7, true);
//                        }
//                        if (node.getUserObject().getClass() == XincoCoreNode.class) {
//                            if (tempAce.isWritePermission()) {
//                                if (((XincoCoreNode) node.getUserObject()).getStatusNumber() == 1) {
//                                    ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(4, true);
//                                    ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(4, true);
//                                }
//                                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(1, true);
//                                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(2, true);
//                                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(3, true);
//                                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(1, true);
//                                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(2, true);
//                                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(3, true);
//                                if (getExplorer().getSession().getClipboardTreeNodeSelection().size() > 0) {
//                                    ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(8, true);
//                                    ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(8, true);
//                                }
//                            }
//                            if (tempAce.isReadPermission()) {
//                                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(11, true);
//                                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(11, true);
//                            }
//                        }
//                        if (node.getUserObject().getClass() == XincoCoreData.class) {
//                            // file = 1
//                            if (((XincoCoreData) node.getUserObject()).getXincoCoreDataType().getId() == 1) {
//                                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(17, true);
//                                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(17, true);
//                                if (tempAce.isReadPermission()) {
//                                    if (((XincoCoreData) node.getUserObject()).getStatusNumber() != 3) {
//                                        ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(5, true);
//                                        ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(11, true);
//                                        ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(5, true);
//                                        ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(11, true);
//                                    }
//                                }
//                                if (tempAce.isWritePermission()) {
//                                    if (((XincoCoreData) node.getUserObject()).getStatusNumber() == 1) {
//                                        ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(12, true);
//                                        ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(13, false);
//                                        ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(14, false);
//                                        ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(12, true);
//                                        ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(13, false);
//                                        ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(14, false);
//                                    }
//                                    if (((XincoCoreData) node.getUserObject()).getStatusNumber() == 4) {
//                                        ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(12, false);
//                                        ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(13, true);
//                                        ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(14, true);
//                                        ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(12, false);
//                                        ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(13, true);
//                                        ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(14, true);
//                                    }
//                                }
//                            }
//                            // URL = 3
//                            if (((XincoCoreData) node.getUserObject()).getXincoCoreDataType().getId() == 3) {
//                                if (tempAce.isReadPermission()) {
//                                    ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(9, true);
//                                    ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(9, true);
//                                }
//                            }
//                            // contact = 4
//                            if (((XincoCoreData) node.getUserObject()).getXincoCoreDataType().getId() == 4) {
//                                if (tempAce.isReadPermission()) {
//                                    ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(10, true);
//                                    ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(10, true);
//                                }
//                            }
//                            if (tempAce.isReadPermission()) {
//                                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(5, true);
//                                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(5, true);
//                            }
//                            if (tempAce.isWritePermission()) {
//                                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(18, true);
//                                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(18, true);
//                                if (((XincoCoreData) node.getUserObject()).getStatusNumber() == 1) {
//                                    ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(4, true);
//                                    ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(4, true);
//                                }
//                            }
//                            if (tempAce.isAdminPermission()) {
//                                if ((((XincoCoreData) node.getUserObject()).getStatusNumber() != 3)
//                                        && (((XincoCoreData) node.getUserObject()).getStatusNumber() != 4)) {
//                                    ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(15, true);
//                                    ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(15, true);
//                                }
//                                if ((((XincoCoreData) node.getUserObject()).getStatusNumber() != 2)
//                                        && (((XincoCoreData) node.getUserObject()).getStatusNumber() != 3)) {
//                                    ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(16, true);
//                                    ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(16, true);
//                                }
//                            }
//                        }
//                        // only nodes have children
//                        if (node.getUserObject().getClass() == XincoCoreNode.class) {
//                            // check for children only if none have been found yet
//                            List<XincoCoreNode> xincoCoreNodes =
//                                    getExplorer().getSession().getXinco().getXincoCoreNodes((XincoCoreNode) node.getUserObject(), getExplorer().getUser());
//                            if (xincoCoreNodes.isEmpty()
//                                    && (((XincoCoreNode) node.getUserObject()).getXincoCoreData().isEmpty())) {
//                                try {
//                                    XincoCoreNode xnode = getExplorer().getSession().getXinco().getXincoCoreNode((XincoCoreNode) node.getUserObject(), getExplorer().getSession().getUser());
//
//                                    if (xnode != null) {
//                                        getExplorer().getSession().getXincoClientRepository().assignObject2TreeNode(node,
//                                                xnode,
//                                                getExplorer().getSession().getXinco(), getExplorer().getSession().getUser(), 2);
//                                    } else {
//                                        JOptionPane.showMessageDialog(getExplorer(),
//                                                getExplorer().getResourceBundle().getString("error.folder.sufficientrights"),
//                                                getExplorer().getResourceBundle().getString("error.accessdenied"),
//                                                JOptionPane.WARNING_MESSAGE);
//                                        getExplorer().getProgressBar().hide();
//                                    }
//                                } catch (Exception rmie) {
//                                    Logger.getLogger(XincoJTree.class.getSimpleName()).log(Level.SEVERE, null, rmie);
//                                }
//                            }
//                        }
//                        // load full data
//                        if (node.getUserObject().getClass() == XincoCoreData.class) {
//                            try {
//                                getExplorer().setXdata(getExplorer().getSession().getXinco().getXincoCoreData((XincoCoreData) node.getUserObject(), getExplorer().getSession().getUser()));
//                                if (getExplorer().getXdata() != null) {
//                                    node.setUserObject(getExplorer().getXdata());
//                                    getExplorer().getSession().getXincoClientRepository().treemodel.nodeChanged(node);
//                                } else {
//                                    JOptionPane.showMessageDialog(getExplorer(),
//                                            getExplorer().getResourceBundle().getString("error.data.sufficientrights"),
//                                            getExplorer().getResourceBundle().getString("error.accessdenied"),
//                                            JOptionPane.WARNING_MESSAGE);
//                                    getExplorer().getProgressBar().hide();
//                                }
//                            } catch (Exception rmie) {
//                                Logger.getLogger(XincoJTree.class.getSimpleName()).log(Level.SEVERE, null, rmie);
//                            }
//                        }
//                        // update details table
//                        if (node.getUserObject().getClass() == XincoCoreNode.class) {
//                            DefaultTableModel dtm = (DefaultTableModel) getExplorer().getJTableRepository().getModel();
//
//                            j = dtm.getRowCount();
//                            for (i = 0; i < j; i++) {
//                                dtm.removeRow(0);
//                            }
//                            String[] rdata = {"", ""};
//
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.id");
//                            rdata[1] = "" + ((XincoCoreNode) node.getUserObject()).getId();
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.designation");
//                            rdata[1] = ((XincoCoreNode) node.getUserObject()).getDesignation();
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.language");
//                            rdata[1] = ((XincoCoreNode) node.getUserObject()).getXincoCoreLanguage().getDesignation()
//                                    + " ("
//                                    + ((XincoCoreNode) node.getUserObject()).getXincoCoreLanguage().getSign()
//                                    + ")";
//                            dtm.addRow(rdata);
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.accessrights");
//                            rdata[1] = "";
//                            rdata[1] = rdata[1]
//                                    + "[";
//                            if (tempAce.isReadPermission()) {
//                                rdata[1] = rdata[1]
//                                        + "R";
//                            } else {
//                                rdata[1] = rdata[1]
//                                        + "-";
//                            }
//                            if (tempAce.isWritePermission()) {
//                                rdata[1] = rdata[1]
//                                        + "W";
//                            } else {
//                                rdata[1] = rdata[1]
//                                        + "-";
//                            }
//                            if (tempAce.isExecutePermission()) {
//                                rdata[1] = rdata[1]
//                                        + "X";
//                            } else {
//                                rdata[1] = rdata[1]
//                                        + "-";
//                            }
//                            if (tempAce.isAdminPermission()) {
//                                rdata[1] = rdata[1]
//                                        + "A";
//                            } else {
//                                rdata[1] = rdata[1]
//                                        + "-";
//                            }
//                            rdata[1] = rdata[1]
//                                    + "]";
//                            dtm.addRow(rdata);
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.status");
//                            rdata[1] = "";
//                            if (((XincoCoreNode) node.getUserObject()).getStatusNumber()
//                                    == 1) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.open")
//                                        + "";
//                            }
//                            if (((XincoCoreNode) node.getUserObject()).getStatusNumber()
//                                    == 2) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.locked")
//                                        + " (-)";
//                            }
//                            if (((XincoCoreNode) node.getUserObject()).getStatusNumber()
//                                    == 3) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.archived")
//                                        + " (->)";
//                            }
//                            dtm.addRow(rdata);
//                        }
//                        if (node.getUserObject().getClass() == XincoCoreData.class) {
//                            DefaultTableModel dtm = (DefaultTableModel) getExplorer().getJTableRepository().getModel();
//                            j = dtm.getRowCount();
//                            for (i = 0; i < j; i++) {
//                                dtm.removeRow(0);
//                            }
//                            String[] rdata = {"", ""};
//                            if (getExplorer().getXdata() != null) {
//                                rdata[0] = getExplorer().getResourceBundle().getString("general.id");
//                                rdata[1] = "" + getExplorer().getXdata().getId();
//                                dtm.addRow(rdata);
//                                rdata[0] = getExplorer().getResourceBundle().getString("general.designation");
//                                rdata[1] = getExplorer().getXdata().getDesignation();
//                                dtm.addRow(rdata);
//                                rdata[0] = getExplorer().getResourceBundle().getString("general.language");
//                                rdata[1] = getExplorer().getXdata().getXincoCoreLanguage().getDesignation()
//                                        + " ("
//                                        + getExplorer().getXdata().getXincoCoreLanguage().getSign()
//                                        + ")";
//                            }
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.datatype");
//                            rdata[1] = getExplorer().getResourceBundle().getString(getExplorer().getXdata().getXincoCoreDataType().getDesignation())
//                                    + " (" + getExplorer().getResourceBundle().getString(getExplorer().getXdata().getXincoCoreDataType().getDescription())
//                                    + ")";
//                            dtm.addRow(rdata);
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.accessrights");
//                            rdata[1] = "";
//                            rdata[1] = rdata[1]
//                                    + "[";
//                            if (tempAce.isReadPermission()) {
//                                rdata[1] = rdata[1]
//                                        + "R";
//                            } else {
//                                rdata[1] = rdata[1]
//                                        + "-";
//                            }
//                            if (tempAce.isWritePermission()) {
//                                rdata[1] = rdata[1]
//                                        + "W";
//                            } else {
//                                rdata[1] = rdata[1]
//                                        + "-";
//                            }
//                            if (tempAce.isExecutePermission()) {
//                                rdata[1] = rdata[1]
//                                        + "X";
//                            } else {
//                                rdata[1] = rdata[1]
//                                        + "-";
//                            }
//                            if (tempAce.isAdminPermission()) {
//                                rdata[1] = rdata[1]
//                                        + "A";
//                            } else {
//                                rdata[1] = rdata[1]
//                                        + "-";
//                            }
//                            rdata[1] = rdata[1]
//                                    + "]";
//                            dtm.addRow(rdata);
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.status");
//                            rdata[1] = "";
//                            if (getExplorer().getXdata().getStatusNumber() == 1) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.open")
//                                        + "";
//                            }
//                            if (getExplorer().getXdata().getStatusNumber() == 2) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.locked")
//                                        + " (-)";
//                            }
//                            if (getExplorer().getXdata().getStatusNumber() == 3) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.archived")
//                                        + " (->)";
//                            }
//                            if (getExplorer().getXdata().getStatusNumber() == 4) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.checkedout")
//                                        + " (X)";
//                            }
//                            if (getExplorer().getXdata().getStatusNumber() == 5) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.published")
//                                        + " (WWW)";
//                            }
//                            dtm.addRow(rdata);
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.typespecificattributes");
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            // get add attributes of CoreData, if access granted
//                            List<XincoAddAttribute> attributes =
//                                    getExplorer().getSession().getXinco().getXincoAddAttributes(getExplorer().getXdata(), getExplorer().getSession().getUser());
//                            List<XincoCoreDataTypeAttribute> dataTypeAttributes =
//                                    getExplorer().getSession().getXinco().getXincoCoreDataTypeAttribute(getExplorer().getXdata().getXincoCoreDataType(), getExplorer().getSession().getUser());
//                            if (attributes.size() > 0) {
//                                for (i = 0; i < attributes.size(); i++) {
//                                    if (i < dataTypeAttributes.size()) {
//                                        rdata[0] = dataTypeAttributes.get(i).getDesignation();
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("int")) {
//                                            rdata[1] = ""
//                                                    + attributes.get(i).getAttribInt();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("unsignedint")) {
//                                            rdata[1] = ""
//                                                    + (attributes.get(i)).getAttribUnsignedint();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("double")) {
//                                            rdata[1] = ""
//                                                    + (attributes.get(i)).getAttribDouble();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("varchar")) {
//                                            rdata[1] = ""
//                                                    + (attributes.get(i)).getAttribVarchar();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("text")) {
//                                            rdata[1] = ""
//                                                    + (attributes.get(i)).getAttribText();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("datetime")) {
//                                            Date time = ((XincoAddAttribute) getExplorer().getXdata().getXincoAddAttributes().get(i)).getAttribDatetime().toGregorianCalendar().getTime();
//                                            Calendar cal = new GregorianCalendar();
//                                            cal.setTime(time);
//                                            rdata[1] = (cal.get(Calendar.MONTH) == 11
//                                                    && cal.get(Calendar.YEAR) == 2
//                                                    && cal.get(Calendar.DAY_OF_MONTH) == 31) ? "" : "" + time;
//                                        }
//                                        dtm.addRow(rdata);
//                                    }
//                                }
//                            } else {
//                                rdata[0] = getExplorer().getResourceBundle().getString("error.accessdenied");
//                                rdata[1] = getExplorer().getResourceBundle().getString("error.content.sufficientrights");
//                                dtm.addRow(rdata);
//                            }
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.logslastfirst");
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            Calendar cal;
//                            XMLGregorianCalendar realcal = null;
//                            Calendar ngc = new GregorianCalendar();
//                            for (i = getExplorer().getXdata().getXincoCoreLogs().size() - 1; i >= 0; i--) {
//                                if (((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpDatetime()
//                                        == null) {
//                                    rdata[0] = "???";
//                                } else {
//                                    try {
//                                        // convert clone from remote time to local time
//                                        cal = ((XMLGregorianCalendar) ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpDatetime().clone()).toGregorianCalendar();
//                                        realcal = (((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpDatetime());
//                                        cal.add(Calendar.MILLISECOND,
//                                                (ngc.get(Calendar.ZONE_OFFSET)
//                                                - realcal.toGregorianCalendar().get(Calendar.ZONE_OFFSET))
//                                                - (ngc.get(Calendar.DST_OFFSET)
//                                                + realcal.toGregorianCalendar().get(Calendar.DST_OFFSET)));
//                                        rdata[0] = ""
//                                                + (cal.get(Calendar.MONTH)
//                                                + 1)
//                                                + " / "
//                                                + cal.get(Calendar.DAY_OF_MONTH)
//                                                + " / "
//                                                + cal.get(Calendar.YEAR)
//                                                + " ";
//                                        if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
//                                            rdata[0] += "0" + cal.get(Calendar.HOUR_OF_DAY) + ":";
//                                        } else {
//                                            rdata[0] += cal.get(Calendar.HOUR_OF_DAY) + ":";
//                                        }
//                                        if (cal.get(Calendar.MINUTE) < 10) {
//                                            rdata[0] += "0" + cal.get(Calendar.MINUTE) + ":";
//                                        } else {
//                                            rdata[0] += cal.get(Calendar.MINUTE) + ":";
//                                        }
//                                        if (cal.get(Calendar.SECOND) < 10) {
//                                            rdata[0] += "0" + cal.get(Calendar.SECOND);
//                                        } else {
//                                            rdata[0] += cal.get(Calendar.SECOND);
//                                        }
//                                    } catch (Exception ce) {
//                                        Logger.getLogger(XincoJTree.class.getSimpleName()).log(Level.SEVERE, null, ce);
//                                    }
//                                }
//                                rdata[1] = "("
//                                        + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpCode()
//                                        + ") "
//                                        + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpDescription();
//                                dtm.addRow(rdata);
//                                rdata[0] = "";
//                                try {
//                                    rdata[1] = getExplorer().getResourceBundle().getString("general.version")
//                                            + " "
//                                            + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getVersion().getVersionHigh()
//                                            + "."
//                                            + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getVersion().getVersionMid()
//                                            + "."
//                                            + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getVersion().getVersionLow()
//                                            + ""
//                                            + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getVersion().getVersionPostfix();
//                                } catch (Exception ex) {
//                                    Logger.getLogger(XincoJTree.class.getSimpleName()).log(Level.SEVERE, null, ex);
//                                    getExplorer().getProgressBar().hide();
//                                }
//                                dtm.addRow(rdata);
//                            }
//                        }
//                        XincoAutofitTableColumns.autoResizeTable(getExplorer().getJTableRepository(), true);
//                        getExplorer().getProgressBar().hide();
//                    } catch (InstantiationException ex) {
//                        Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (IllegalAccessException ex) {
//                        Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
//                    }
            }
        }
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        XincoCoreACE tempAce = new XincoCoreACE();
        if (xincoTree.getValue() instanceof XincoCoreNodeProperty) {
            // get ace
            XincoCoreNode node = ((XincoCoreNode) ((XincoCoreNodeProperty) xincoTree.getValue()).getValue());
            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(1),
                        (ArrayList) (node).getXincoCoreAcl());
            } catch (XincoException ex) {
                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (xincoTree.getValue() instanceof XincoCoreDataProperty) {
            // get ace
            XincoCoreData data = ((XincoCoreData) ((XincoCoreDataProperty) xincoTree.getValue()).getValue());
            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(1),
                        (ArrayList) (data).getXincoCoreAcl());
            } catch (XincoException ex) {
                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Clear table
            xincoTable.removeAllItems();
            int i = 1;
            xincoTable.addItem(new Object[]{
                        xerb.getString("general.id"), data.getId()}, i++);
            xincoTable.addItem(new Object[]{
                        xerb.getString("general.designation"), data.getDesignation()}, i++);
            xincoTable.addItem(new Object[]{
                        xerb.getString("general.language"), data.getXincoCoreLanguage().getDesignation()
                        + " ("
                        + data.getXincoCoreLanguage().getSign()
                        + ")"}, i++);
            xincoTable.addItem(new Object[]{
                        xerb.getString("general.datatype"), xerb.getString(data.getXincoCoreDataType().getDesignation())
                        + " ("
                        + xerb.getString(data.getXincoCoreDataType().getDescription())
                        + ")"}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            String value = "";
            if (tempAce.isReadPermission()) {
                value += "R";
            } else {
                value += "-";
            }
            if (tempAce.isWritePermission()) {
                value += "W";
            } else {
                value += "-";
            }
            if (tempAce.isExecutePermission()) {
                value += "X";
            } else {
                value += "-";
            }
            if (tempAce.isAdminPermission()) {
                value += "A";
            } else {
                value += "-";
            }
            xincoTable.addItem(new Object[]{
                        xerb.getString("general.accessrights"),
                        "[" + value + "]"}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            switch (data.getStatusNumber()) {
                case 1:
                    value = xerb.getString("general.status.open");
                    break;
                case 2:
                    value = xerb.getString("general.status.locked");
                    break;
                case 3:
                    value = xerb.getString("general.status.archived");
                    break;
                case 4:
                    value = xerb.getString("general.status.checkedout");
                    break;
                case 5:
                    value = xerb.getString("general.status.published");
                    break;
            }
            xincoTable.addItem(new Object[]{
                        xerb.getString("general.status"), value}, i++);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.status");
//                            rdata[1] = "";
//                            if (getExplorer().getXdata().getStatusNumber() == 1) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.open")
//                                        + "";
//                            }
//                            if (getExplorer().getXdata().getStatusNumber() == 2) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.locked")
//                                        + " (-)";
//                            }
//                            if (getExplorer().getXdata().getStatusNumber() == 3) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.archived")
//                                        + " (->)";
//                            }
//                            if (getExplorer().getXdata().getStatusNumber() == 4) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.checkedout")
//                                        + " (X)";
//                            }
//                            if (getExplorer().getXdata().getStatusNumber() == 5) {
//                                rdata[1] = getExplorer().getResourceBundle().getString("general.status.published")
//                                        + " (WWW)";
//                            }
//                            dtm.addRow(rdata);
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.typespecificattributes");
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            // get add attributes of CoreData, if access granted
//                            List<XincoAddAttribute> attributes =
//                                    getExplorer().getSession().getXinco().getXincoAddAttributes(getExplorer().getXdata(), getExplorer().getSession().getUser());
//                            List<XincoCoreDataTypeAttribute> dataTypeAttributes =
//                                    getExplorer().getSession().getXinco().getXincoCoreDataTypeAttribute(getExplorer().getXdata().getXincoCoreDataType(), getExplorer().getSession().getUser());
//                            if (attributes.size() > 0) {
//                                for (i = 0; i < attributes.size(); i++) {
//                                    if (i < dataTypeAttributes.size()) {
//                                        rdata[0] = dataTypeAttributes.get(i).getDesignation();
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("int")) {
//                                            rdata[1] = ""
//                                                    + attributes.get(i).getAttribInt();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("unsignedint")) {
//                                            rdata[1] = ""
//                                                    + (attributes.get(i)).getAttribUnsignedint();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("double")) {
//                                            rdata[1] = ""
//                                                    + (attributes.get(i)).getAttribDouble();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("varchar")) {
//                                            rdata[1] = ""
//                                                    + (attributes.get(i)).getAttribVarchar();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("text")) {
//                                            rdata[1] = ""
//                                                    + (attributes.get(i)).getAttribText();
//                                        }
//                                        if (dataTypeAttributes.get(i).getDataType().equalsIgnoreCase("datetime")) {
//                                            Date time = ((XincoAddAttribute) getExplorer().getXdata().getXincoAddAttributes().get(i)).getAttribDatetime().toGregorianCalendar().getTime();
//                                            Calendar cal = new GregorianCalendar();
//                                            cal.setTime(time);
//                                            rdata[1] = (cal.get(Calendar.MONTH) == 11
//                                                    && cal.get(Calendar.YEAR) == 2
//                                                    && cal.get(Calendar.DAY_OF_MONTH) == 31) ? "" : "" + time;
//                                        }
//                                        dtm.addRow(rdata);
//                                    }
//                                }
//                            } else {
//                                rdata[0] = getExplorer().getResourceBundle().getString("error.accessdenied");
//                                rdata[1] = getExplorer().getResourceBundle().getString("error.content.sufficientrights");
//                                dtm.addRow(rdata);
//                            }
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = "";
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            rdata[0] = getExplorer().getResourceBundle().getString("general.logslastfirst");
//                            rdata[1] = "";
//                            dtm.addRow(rdata);
//                            Calendar cal;
//                            XMLGregorianCalendar realcal = null;
//                            Calendar ngc = new GregorianCalendar();
//                            for (i = getExplorer().getXdata().getXincoCoreLogs().size() - 1; i >= 0; i--) {
//                                if (((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpDatetime()
//                                        == null) {
//                                    rdata[0] = "???";
//                                } else {
//                                    try {
//                                        // convert clone from remote time to local time
//                                        cal = ((XMLGregorianCalendar) ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpDatetime().clone()).toGregorianCalendar();
//                                        realcal = (((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpDatetime());
//                                        cal.add(Calendar.MILLISECOND,
//                                                (ngc.get(Calendar.ZONE_OFFSET)
//                                                - realcal.toGregorianCalendar().get(Calendar.ZONE_OFFSET))
//                                                - (ngc.get(Calendar.DST_OFFSET)
//                                                + realcal.toGregorianCalendar().get(Calendar.DST_OFFSET)));
//                                        rdata[0] = ""
//                                                + (cal.get(Calendar.MONTH)
//                                                + 1)
//                                                + " / "
//                                                + cal.get(Calendar.DAY_OF_MONTH)
//                                                + " / "
//                                                + cal.get(Calendar.YEAR)
//                                                + " ";
//                                        if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
//                                            rdata[0] += "0" + cal.get(Calendar.HOUR_OF_DAY) + ":";
//                                        } else {
//                                            rdata[0] += cal.get(Calendar.HOUR_OF_DAY) + ":";
//                                        }
//                                        if (cal.get(Calendar.MINUTE) < 10) {
//                                            rdata[0] += "0" + cal.get(Calendar.MINUTE) + ":";
//                                        } else {
//                                            rdata[0] += cal.get(Calendar.MINUTE) + ":";
//                                        }
//                                        if (cal.get(Calendar.SECOND) < 10) {
//                                            rdata[0] += "0" + cal.get(Calendar.SECOND);
//                                        } else {
//                                            rdata[0] += cal.get(Calendar.SECOND);
//                                        }
//                                    } catch (Exception ce) {
//                                        Logger.getLogger(XincoJTree.class.getSimpleName()).log(Level.SEVERE, null, ce);
//                                    }
//                                }
//                                rdata[1] = "("
//                                        + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpCode()
//                                        + ") "
//                                        + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getOpDescription();
//                                dtm.addRow(rdata);
//                                rdata[0] = "";
//                                try {
//                                    rdata[1] = getExplorer().getResourceBundle().getString("general.version")
//                                            + " "
//                                            + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getVersion().getVersionHigh()
//                                            + "."
//                                            + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getVersion().getVersionMid()
//                                            + "."
//                                            + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getVersion().getVersionLow()
//                                            + ""
//                                            + ((XincoCoreLog) getExplorer().getXdata().getXincoCoreLogs().get(i)).getVersion().getVersionPostfix();
//                                } catch (Exception ex) {
//                                    Logger.getLogger(XincoJTree.class.getSimpleName()).log(Level.SEVERE, null, ex);
//                                    getExplorer().getProgressBar().hide();
//                                }
//                                dtm.addRow(rdata);
//                            }
        }
        //Autofit to contents
        for (int i = 0; i < xincoTable.getColumnHeaders().length; i++) {
            int max = 0;
            for (int j = 1; j < xincoTable.getItemIds().size(); j++) {
                if (xincoTable.getItem(j).toString().length() > max) {
                    max = xincoTable.getItem(j).toString().length();
                }
            }
            xincoTable.setColumnWidth(xincoTable.getColumnHeaders()[i], max);
        }
    }
}
