package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.XincoCoreACEServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoException;
import com.bluecubs.xinco.core.server.service.*;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window.Notification;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoExplorer extends XincoComponent implements ValueChangeListener {

    //Table linking displayed item with it's id
    private Tree xincoTree = null;
    private Table xincoTable = null;
    private final Xinco xinco;
    private com.bluecubs.xinco.core.server.service.client.Xinco_Service service = new com.bluecubs.xinco.core.server.service.client.Xinco_Service();
    private com.bluecubs.xinco.core.server.service.client.Xinco port = service.getXincoPort();

    public XincoExplorer(Xinco xinco) {
        this.xinco = xinco;
        menubar = new MenuBar();
        //TODO: use selected language
        HorizontalSplitPanel splitpanel = new HorizontalSplitPanel();
        // Put two components in the container.
        splitpanel.setFirstComponent(getXincoTree());
        splitpanel.setSecondComponent(getDetailTable());
        splitpanel.setHeight(500, Sizeable.UNITS_PIXELS);
        splitpanel.setSplitPosition(25, Sizeable.UNITS_PERCENTAGE);
        // The composition root MUST be set
        setCompositionRoot(splitpanel);
    }

    private Tree getXincoTree() {
        if (xincoTree == null) {
            try {
                xincoTree = new Tree(xinco.getResource().getString("menu.repository"));
                XincoCoreNodeServer xcns = null;
                try {
                    xcns = new XincoCoreNodeServer(1);
                } catch (XincoException ex) {
                    Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
                }
                XincoCoreNodeProperty rootProperty = new XincoCoreNodeProperty(xcns);
                Item root = xincoTree.addItem(rootProperty);
                xincoTree.select(rootProperty);
                addChildren(rootProperty);
                xincoTree.expandItem(root);
                xincoTree.addListener(XincoExplorer.this);
                xincoTree.setImmediate(true);
                //Add icon support
                xincoTree.addContainerProperty("icon", Resource.class, null);
                xincoTree.setItemIconPropertyId("icon");
            } catch (XincoException ex) {
                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            xincoTable.addContainerProperty(xinco.getResource().getString("window.repository.table.attribute"),
                    String.class, null);
            xincoTable.addContainerProperty(xinco.getResource().getString("window.repository.table.details"),
                    String.class, null);
            // Send changes in selection immediately to server.
            xincoTable.setImmediate(true);
            //Disable sorting
            xincoTable.setSortDisabled(true);
            xincoTable.setPageLength(20);
            xincoTable.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        }
        return xincoTable;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        buildMenu();
        XincoCoreACE tempAce = new XincoCoreACE();
        //Clear table
        xincoTable.removeAllItems();
        int i = 1;
        String value = "", header = "";
        if (xincoTree.getValue() instanceof XincoCoreNodeProperty) {
            XincoCoreNodeProperty xincoCoreNodeProperty = (XincoCoreNodeProperty) xincoTree.getValue();
            // get ace
            XincoCoreNode node = ((XincoCoreNode) (xincoCoreNodeProperty).getValue());
            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(xinco.getLoggedUser().getId()),
                        (ArrayList) (node).getXincoCoreAcl());
            } catch (XincoException ex) {
                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
            }
            // only nodes have children
            // check for children only if none have been found yet
            XincoCoreNodeServer xcns = null;
            try {
                xcns = new XincoCoreNodeServer(1);
            } catch (XincoException ex) {
                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
            }
            java.util.List<XincoCoreNode> xincoCoreNodes = xcns.getXincoCoreNodes();
            if (xincoCoreNodes.isEmpty() && node.getXincoCoreData().isEmpty() && tempAce.isReadPermission()) {
                try {
                    //Add the children nodes and data
                    XincoCoreNodeServer parent = null;
                    try {
                        parent = new XincoCoreNodeServer(node.getId());
                    } catch (XincoException ex) {
                        Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Populate children
                    parent.fillXincoCoreData();
                    parent.fillXincoCoreNodes();
                    addChildren(new XincoCoreNodeProperty(parent));
                } catch (XincoException ex) {
                    Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                xinco.getMainWindow().showNotification(xinco.getResource().getString("error.accessdenied"),
                        xinco.getResource().getString("error.folder.sufficientrights"),
                        Notification.TYPE_WARNING_MESSAGE);
            }
            // update details table
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{xinco.getResource().getString("general.id"),
                        node.getId()}, i++);
            xincoTable.addItem(new Object[]{xinco.getResource().getString("general.designation"),
                        node.getDesignation()}, i++);
            xincoTable.addItem(new Object[]{xinco.getResource().getString("general.language"),
                        xinco.getResource().getString(node.getXincoCoreLanguage().getDesignation()) + " ("
                        + node.getXincoCoreLanguage().getSign()
                        + ")"}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            value = "[";
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
                value = value + "X";
            } else {
                value += "-";
            }
            if (tempAce.isAdminPermission()) {
                value = value + "A";
            } else {
                value += "-";
            }
            value = value + "]";
            xincoTable.addItem(new Object[]{xinco.getResource().getString("general.accessrights"), value}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            value = "";
            if (node.getStatusNumber() == 1) {
                value = xinco.getResource().getString("general.status.open") + "";
            }
            if (node.getStatusNumber() == 2) {
                value = xinco.getResource().getString("general.status.locked") + " (-)";
            }
            if (node.getStatusNumber() == 3) {
                value = xinco.getResource().getString("general.status.archived") + " (->)";
            }
            xincoTable.addItem(new Object[]{xinco.getResource().getString("general.status"), value}, i++);
        } else if (xincoTree.getValue() instanceof XincoCoreDataProperty) {
            // get ace
            XincoCoreData data = ((XincoCoreData) ((XincoCoreDataProperty) xincoTree.getValue()).getValue());
            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(1),
                        (ArrayList) (data).getXincoCoreAcl());
            } catch (XincoException ex) {
                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
            }
            xincoTable.addItem(new Object[]{
                        xinco.getResource().getString("general.id"), data.getId()}, i++);
            xincoTable.addItem(new Object[]{
                        xinco.getResource().getString("general.designation"), data.getDesignation()}, i++);
            xincoTable.addItem(new Object[]{
                        xinco.getResource().getString("general.language"), data.getXincoCoreLanguage().getDesignation()
                        + " ("
                        + data.getXincoCoreLanguage().getSign()
                        + ")"}, i++);
            xincoTable.addItem(new Object[]{
                        xinco.getResource().getString("general.datatype"), xinco.getResource().getString(data.getXincoCoreDataType().getDesignation())
                        + " ("
                        + xinco.getResource().getString(data.getXincoCoreDataType().getDescription())
                        + ")"}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
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
                        xinco.getResource().getString("general.accessrights"),
                        "[" + value + "]"}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            switch (data.getStatusNumber()) {
                case 1:
                    value = xinco.getResource().getString("general.status.open");
                    break;
                case 2:
                    value = xinco.getResource().getString("general.status.locked");
                    break;
                case 3:
                    value = xinco.getResource().getString("general.status.archived");
                    break;
                case 4:
                    value = xinco.getResource().getString("general.status.checkedout");
                    break;
                case 5:
                    value = xinco.getResource().getString("general.status.published");
                    break;
            }
            xincoTable.addItem(new Object[]{
                        xinco.getResource().getString("general.status"), value}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{
                        xinco.getResource().getString("general.typespecificattributes"), ""}, i++);
            // get add attributes of Core Data, if access granted
            java.util.List<XincoAddAttribute> attributes =
                    data.getXincoAddAttributes();
            java.util.List<XincoCoreDataTypeAttribute> dataTypeAttributes =
                    data.getXincoCoreDataType().getXincoCoreDataTypeAttributes();
            if (!attributes.isEmpty()) {
                for (int j = 0; j < attributes.size(); j++) {
                    header = xinco.getResource().getString(dataTypeAttributes.get(j).getDesignation());
                    if (dataTypeAttributes.get(j).getDataType().equalsIgnoreCase("int")) {
                        value = ""
                                + attributes.get(j).getAttribInt();
                    }
                    if (dataTypeAttributes.get(j).getDataType().equalsIgnoreCase("unsignedint")) {
                        value = ""
                                + (attributes.get(j)).getAttribUnsignedint();
                    }
                    if (dataTypeAttributes.get(j).getDataType().equalsIgnoreCase("double")) {
                        value = ""
                                + (attributes.get(j)).getAttribDouble();
                    }
                    if (dataTypeAttributes.get(j).getDataType().equalsIgnoreCase("varchar")) {
                        value = ""
                                + (attributes.get(j)).getAttribVarchar();
                    }
                    if (dataTypeAttributes.get(j).getDataType().equalsIgnoreCase("text")) {
                        value = ""
                                + (attributes.get(j)).getAttribText();
                    }
                    if (dataTypeAttributes.get(j).getDataType().equalsIgnoreCase("datetime")) {
                        Date time = ((XincoAddAttribute) data.getXincoAddAttributes().get(j)).getAttribDatetime().toGregorianCalendar().getTime();
                        Calendar cal = new GregorianCalendar();
                        cal.setTime(time);
                        value = (cal.get(Calendar.MONTH) == 11
                                && cal.get(Calendar.YEAR) == 2
                                && cal.get(Calendar.DAY_OF_MONTH) == 31) ? "" : "" + time;
                    }
                    xincoTable.addItem(new Object[]{header, value}, i++);
                }
            } else {
                header = xinco.getResource().getString("error.accessdenied");
                value = xinco.getResource().getString("error.content.sufficientrights");
                xincoTable.addItem(new Object[]{header, value}, i++);
            }
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            header = xinco.getResource().getString("general.logslastfirst");
            value = "";
            xincoTable.addItem(new Object[]{header, value}, i++);
            Calendar cal;
            XMLGregorianCalendar realcal = null;
            Calendar ngc = new GregorianCalendar();
            for (int j = data.getXincoCoreLogs().size() - 1; j >= 0; j--) {
                if (((XincoCoreLog) data.getXincoCoreLogs().get(j)).getOpDatetime() == null) {
                    header = "???";
                } else {
                    try {
                        // convert clone from remote time to local time
                        cal = ((XMLGregorianCalendar) ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getOpDatetime().clone()).toGregorianCalendar();
                        realcal = (((XincoCoreLog) data.getXincoCoreLogs().get(j)).getOpDatetime());
                        cal.add(Calendar.MILLISECOND,
                                (ngc.get(Calendar.ZONE_OFFSET)
                                - realcal.toGregorianCalendar().get(Calendar.ZONE_OFFSET))
                                - (ngc.get(Calendar.DST_OFFSET)
                                + realcal.toGregorianCalendar().get(Calendar.DST_OFFSET)));
                        header = ""
                                + (cal.get(Calendar.MONTH)
                                + 1)
                                + " / "
                                + cal.get(Calendar.DAY_OF_MONTH)
                                + " / "
                                + cal.get(Calendar.YEAR)
                                + " ";
                        if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
                            header += "0" + cal.get(Calendar.HOUR_OF_DAY) + ":";
                        } else {
                            header += cal.get(Calendar.HOUR_OF_DAY) + ":";
                        }
                        if (cal.get(Calendar.MINUTE) < 10) {
                            header += "0" + cal.get(Calendar.MINUTE) + ":";
                        } else {
                            header += cal.get(Calendar.MINUTE) + ":";
                        }
                        if (cal.get(Calendar.SECOND) < 10) {
                            header += "0" + cal.get(Calendar.SECOND);
                        } else {
                            header += cal.get(Calendar.SECOND);
                        }
                    } catch (Exception ce) {
                        Logger.getLogger(XincoExplorer.class.getSimpleName()).log(Level.SEVERE, null, ce);
                    }
                }
                value = "("
                        + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getOpCode()
                        + ") "
                        + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getOpDescription();
                xincoTable.addItem(new Object[]{header, value}, i++);
                header = "";
                try {
                    value = xinco.getResource().getString("general.version")
                            + " "
                            + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getVersion().getVersionHigh()
                            + "."
                            + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getVersion().getVersionMid()
                            + "."
                            + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getVersion().getVersionLow()
                            + ""
                            + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getVersion().getVersionPostfix();
                } catch (Exception ex) {
                    Logger.getLogger(XincoExplorer.class.getSimpleName()).log(Level.SEVERE, null, ex);
                }
                xincoTable.addItem(new Object[]{header, value}, i++);
            }
        }
        xincoTable.setPageLength(i - 1);
        xincoTable.requestRepaintAll();
    }

    public static XincoCoreNode getXincoCoreNode(XincoCoreNode in0, XincoCoreUser in1) {
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            XincoCoreNodeServer node = new XincoCoreNodeServer(in0.getId());
            XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, (ArrayList) node.getXincoCoreAcl());
            if (ace.isReadPermission()) {
                boolean showChildren = false;
                // show content of TRASH to admins ONLY!
                if (node.getId() == 2) {
                    for (int i = 0; i < user.getXincoCoreGroups().size(); i++) {
                        if (((XincoCoreGroup) user.getXincoCoreGroups().get(i)).getId() == 1) {
                            showChildren = true;
                            break;
                        }
                    }
                } else {
                    showChildren = true;
                }
                if (showChildren) {
                    node.fillXincoCoreNodes();
                    node.fillXincoCoreData();
                }
                return (XincoCoreNode) node;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    private void addChildrenNodes(XincoCoreNodeProperty parent) throws XincoException {
        XincoCoreNodeServer xcns = new XincoCoreNodeServer(((XincoCoreNode) parent.getValue()).getId());
        for (Iterator<XincoCoreNode> it = xcns.getXincoCoreNodes().iterator(); it.hasNext();) {
            XincoCoreNode subnode = it.next();
            XincoCoreNodeProperty childProperty = new XincoCoreNodeProperty(subnode);
            xincoTree.addItem(childProperty);
            // Set it to be a child.
            xincoTree.setParent(childProperty, parent);
            xincoTree.setChildrenAllowed(childProperty, true);
        }
    }

    private void addChildrenData(XincoCoreNodeProperty parent) throws XincoException {
        XincoCoreNodeServer xcns = new XincoCoreNodeServer(((XincoCoreNode) parent.getValue()).getId());
        for (Iterator<XincoCoreData> it = xcns.getXincoCoreData().iterator(); it.hasNext();) {
            XincoCoreData data = it.next();
            XincoCoreDataProperty dataProperty = new XincoCoreDataProperty(data);
            //Add childen data
//            Item item = 
            xincoTree.addItem(dataProperty);
            // Set it to be a child.
            xincoTree.setParent(dataProperty, parent);
            // Set as leaves
            xincoTree.setChildrenAllowed(dataProperty, false);
//            try {
//                if (data.getDesignation().contains(".")
//                        && data.getDesignation().substring(data.getDesignation().lastIndexOf(".") + 1,
//                        data.getDesignation().length()).length() == 3 
//                        //                        && data.getXincoCoreDataType().getDesignation().equals("general.data.type.file")
//                        ) {
//                    //Set Icon
//                    item.getItemProperty("icon").setValue(
//                            xinco.getIcon(data.getDesignation().substring(data.getDesignation().lastIndexOf(".") + 1,
//                            data.getDesignation().length())));
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }

    private void addChildren(XincoCoreNodeProperty parent) throws XincoException {
        addChildrenNodes(parent);
        addChildrenData(parent);
    }

    private void buildMenu() {
        //Build Menu
        menubar.removeItems();
        if (xincoTree.getValue() instanceof XincoCoreNodeProperty) {
            MenuItem repo = menubar.addItem(xinco.getResource().getString("menu.repository"), null);
            repo.addItem(xinco.getResource().getString("menu.repository.addfolder"),
                    null,//Icon 
                    new MenuBar.Command() {

                @Override
                public void menuSelected(MenuItem selectedItem) {
                    com.bluecubs.xinco.core.server.service.client.XincoCoreNode node =
                            new com.bluecubs.xinco.core.server.service.client.XincoCoreNode();
                    com.bluecubs.xinco.core.server.service.client.XincoCoreUser user =
                            new com.bluecubs.xinco.core.server.service.client.XincoCoreUser();
                    port.setXincoCoreNode(node, user);
                }
            });
        }
        if (xincoTree.getValue() instanceof XincoCoreDataProperty) {
            xinco.getMainWindow().showNotification("Data- no menus");
        }
    }
}
