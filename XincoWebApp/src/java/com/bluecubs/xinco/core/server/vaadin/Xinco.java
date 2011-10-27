package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.core.server.service.*;
import com.bluecubs.xinco.tools.XincoFileIconManager;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.*;
import com.vaadin.ui.Window;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class Xinco extends Application implements Property.ValueChangeListener {

    //client version
    private XincoVersion xincoClientVersion = null;
    //TODO: use selected language
    private ResourceBundle xerb =
            ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", Locale.getDefault());
    private XincoCoreUser loggedUser;
    private XincoFileIconManager xfm = new XincoFileIconManager();
    //Table linking displayed item with it's id
    private Tree xincoTree = null;
    private Table xincoTable = null;
    private com.vaadin.ui.MenuBar menuBar = new com.vaadin.ui.MenuBar();
    private Item root;
    private Xinco_Service service;

    @Override
    public void init() {
        //Default user (System)
        try {
            loggedUser = new XincoCoreUserServer(1);
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
        xincoClientVersion = new XincoVersion();
        try {
            xincoClientVersion.setVersionHigh(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.high").getIntValue());
            xincoClientVersion.setVersionMid(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.mid").getIntValue());
            xincoClientVersion.setVersionLow(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.low").getIntValue());
            xincoClientVersion.setVersionPostfix(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.postfix").getStringValue());
        } catch (com.bluecubs.xinco.core.server.XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
        setMainWindow(new Window("Xinco"));
        showXincoExplorer();
    }

    protected ThemeResource getIcon(String extension) throws IOException {
        WebApplicationContext context = (WebApplicationContext) getContext();
        File iconsFolder = new File(context.getHttpSession().getServletContext().getRealPath("/WEB-INF") + System.getProperty("file.separator") + "icons");
        if (!iconsFolder.exists()) {
            //Create it
            iconsFolder.mkdirs();
        }
        Image image = iconToImage(xfm.getIcon(extension));
        BufferedImage buffered = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffered.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        if (extension.indexOf('.') > -1) {
            extension = extension.substring(extension.lastIndexOf('.') + 1,
                    extension.length());
        }
        File icon = new File(iconsFolder.getAbsolutePath()
                + System.getProperty("file.separator") + extension + ".png");
        ImageIO.write(buffered, "PNG", icon);
        ThemeResource resource = new ThemeResource(".." + System.getProperty("file.separator")
                + ".." + System.getProperty("file.separator")
                + "icons" + System.getProperty("file.separator") + extension + ".png");
        getMainWindow().showNotification(resource.getResourceId());
        return resource;
    }

    static Image iconToImage(Icon icon) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        Graphics2D g = image.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return image;
    }

    /**
     * @return the Resource Bundle
     */
    public ResourceBundle getResource() {
        return xerb;
    }

    /**
     * @return the loggedUser
     */
    public XincoCoreUser getLoggedUser() {
        return loggedUser;
    }

    private void showXincoExplorer() {
        getMainWindow().removeAllComponents();
        try {
            getMainWindow().setCaption(getResource().getString("general.clienttitle") + " - "
                    + getResource().getString("general.version") + " "
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.high").getIntValue() + "."
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.mid").getIntValue() + "."
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.low").getIntValue() + " "
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.postfix").getStringValue());
            HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
            // Put two components in the container.
            splitPanel.setFirstComponent(getXincoTree());
            splitPanel.setSecondComponent(getDetailTable());
            splitPanel.setHeight(500, Sizeable.UNITS_PIXELS);
            splitPanel.setSplitPosition(25, Sizeable.UNITS_PERCENTAGE);
            getMainWindow().addComponent(menuBar);
            //hide it since is empty at start
            menuBar.setVisible(false);
            getMainWindow().addComponent(splitPanel);
            updateMenu();
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Tree getXincoTree() {
        if (xincoTree == null) {
            try {
                xincoTree = new Tree(getResource().getString("menu.repository"));
                refresh();
                xincoTree.addListener(this);
                xincoTree.setImmediate(true);
                //Add icon support
                xincoTree.addContainerProperty("icon", Resource.class, null);
                xincoTree.setItemIconPropertyId("icon");
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
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
            xincoTable.addContainerProperty(getResource().getString("window.repository.table.attribute"),
                    String.class, null);
            xincoTable.addContainerProperty(getResource().getString("window.repository.table.details"),
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

    private void addChildren(XincoCoreNodeProperty parent) throws XincoException {
        addChildrenNodes(parent);
        addChildrenData(parent);
        xincoTree.expandItem(root);
    }

    private void addChildrenNodes(XincoCoreNodeProperty parent) throws XincoException {
        XincoCoreNodeServer xcns = new XincoCoreNodeServer(((XincoCoreNode) parent.getValue()).getId());
        for (Iterator<XincoCoreNode> it = xcns.getXincoCoreNodes().iterator(); it.hasNext();) {
            XincoCoreNode subnode = it.next();
            XincoCoreNodeProperty childProperty = new XincoCoreNodeProperty(subnode);
            xincoTree.addItem(childProperty);
            // Set it to be a child.
            xincoTree.setParent(childProperty, parent);
            //Allow to have children
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
//                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }

    protected void updateMenu() {
        //Build Menu
        menuBar.removeItems();
        com.vaadin.ui.MenuBar.MenuItem repo = menuBar.addItem(getResource().getString("menu.repository"), null);
        //Refresh Menu is always on
        repo.addItem(getResource().getString("menu.repository.refresh"),
                null,//Icon 
                new com.vaadin.ui.MenuBar.Command() {

            @Override
            public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                try {
                    refresh();
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        if (xincoTree.getValue() instanceof XincoCoreNodeProperty) {

            repo.addItem(getResource().getString("menu.repository.addfolder"),
                    null,//Icon 
                    new com.vaadin.ui.MenuBar.Command() {

                @Override
                public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                    //Show the Data Folder Dialog window
                    showDataFolderDialog(true);
                }
            });
        }
        if (xincoTree.getValue() instanceof XincoCoreDataProperty) {
        }
        //Hide it if empty
        menuBar.setVisible(!menuBar.getItems().isEmpty());
    }

    private void showDataFolderDialog(final boolean newFolder) {
        XincoCoreNodeProperty nodeProp = (XincoCoreNodeProperty) xincoTree.getValue();
        final XincoCoreNode node = ((XincoCoreNode) (nodeProp).getValue());
        final Window dataFolderDialog = new Window();
        final Form form = new Form();
        form.setCaption(getResource().getString("window.datadetails"));
        //ID
        com.vaadin.ui.TextField idField = new com.vaadin.ui.TextField(getResource().getString("general.id") + ":");
        form.addField("id", idField);
        if (newFolder) {
            idField.setValue("0");
        } else {
            idField.setValue(node.getId());
        }
        //Not editable
        idField.setEnabled(false);
        //Designation
        com.vaadin.ui.TextField designationField = new com.vaadin.ui.TextField(getResource().getString("general.designation") + ":");
        form.addField("designation", designationField);
        form.getField("designation").setRequired(true);
        form.getField("designation").setRequiredError(getResource().getString("message.missing.designation"));
        if (!newFolder) {
            idField.setValue(node.getDesignation());
        }
        //Language selection
        final Select languages = new Select(getResource().getString("general.language") + ":");
        int i = 0;
        for (Object language : XincoCoreLanguageServer.getXincoCoreLanguages()) {
            String designation = ((XincoCoreLanguageServer) language).getDesignation();
            if (getResource().containsKey(designation)) {
                String value = getResource().getString(designation);
                languages.addItem(i);
                languages.setItemCaption(i, value);
                if (newFolder && ((XincoCoreLanguageServer) language).getSign().equals("en")) //Select by default
                {
                    languages.setValue(i);
                }
                i++;
            } else {
                Logger.getLogger(Xinco.class.getName()).log(Level.WARNING,
                        "{0} not defined in com.bluecubs.xinco.messages.XincoMessagesLocale",
                        "Locale." + designation);
            }
            if (node.getXincoCoreLanguage().getSign().equals(designation)) {
                languages.setValue("Locale." + designation);
            }
        }
        form.addField("lang", languages);
        form.getField("lang").setRequired(true);
        form.getField("lang").setRequiredError(getResource().getString("message.missing.language"));
        //Status
        com.vaadin.ui.TextField statusField = new com.vaadin.ui.TextField(getResource().getString("general.status") + ":");
        //Not editable
        statusField.setEnabled(false);
        String text = "";
        int status = 1;
        if (newFolder || node.getStatusNumber() == 1) {
            text = getResource().getString("general.status.open");
        }
        if (node.getStatusNumber() == 2) {
            text = getResource().getString("general.status.locked") + " (-)";
            status = node.getStatusNumber();
        }
        if (node.getStatusNumber() == 3) {
            text = getResource().getString("general.status.archived") + " (->)";
            status = node.getStatusNumber();
        }
        statusField.setValue(text);
        form.addField("status", statusField);
        form.setFooter(new HorizontalLayout());
        //Used for validation purposes
        com.vaadin.ui.Button commit = new com.vaadin.ui.Button(getResource().getString("general.save"), form, "commit");
        final int finalStatus = status;
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    //Disable data fields, make sure nothing gets modified after clicking save
                    form.getField("lang").setEnabled(false);
                    form.getField("designation").setEnabled(false);
                    //Process the data
                    XincoCoreNodeServer newNode = new XincoCoreNodeServer(
                            Integer.valueOf(form.getField("id").getValue().toString()),
                            (newFolder ? node.getId() : null),
                            ((XincoCoreLanguage) XincoCoreLanguageServer.getXincoCoreLanguages().get(Integer.valueOf(languages.getValue().toString()))).getId(),
                            form.getField("designation").getValue().toString(),
                            finalStatus);
                    newNode.setChangerID(getLoggedUser().getId());
                    try {
                        //Call the web service
                        getService().getXincoPort().setXincoCoreNode(newNode, loggedUser);
                    } catch (MalformedURLException ex) {
                        throw new XincoException(ex);
                    }
                    getMainWindow().removeWindow(dataFolderDialog);
                    refresh();
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        form.getFooter().setSizeFull();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        getMainWindow().removeWindow(dataFolderDialog);
                    }
                }));
        dataFolderDialog.addComponent(form);
        dataFolderDialog.setModal(true);
        dataFolderDialog.center();
        dataFolderDialog.setWidth(25, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(dataFolderDialog);
    }

    private void refresh() throws XincoException {
        XincoCoreNodeServer xcns = null;
        try {
            xcns = new XincoCoreNodeServer(1);
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
        XincoCoreNodeProperty rootProperty = new XincoCoreNodeProperty(xcns);
        //Clear tree and start over
        xincoTree.removeAllItems();
        root = xincoTree.addItem(rootProperty);
        addChildren(rootProperty);
        //Expand root
        xincoTree.expandItem(root);
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        updateMenu();
        XincoCoreACE tempAce = new XincoCoreACE();
        //Clear table
        xincoTable.removeAllItems();
        int i = 1;
        String value = "", header;
        if (xincoTree.getValue() instanceof XincoCoreNodeProperty) {
            XincoCoreNodeProperty xincoCoreNodeProperty = (XincoCoreNodeProperty) xincoTree.getValue();
            // get ace
            XincoCoreNode node = ((XincoCoreNode) (xincoCoreNodeProperty).getValue());
            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(getLoggedUser().getId()),
                        (ArrayList) (node).getXincoCoreAcl());
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
            // only nodes have children
            // check for children only if none have been found yet
            XincoCoreNodeServer xcns = null;
            try {
                xcns = new XincoCoreNodeServer(1);
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
            java.util.List<XincoCoreNode> xincoCoreNodes = xcns.getXincoCoreNodes();
            if (xincoCoreNodes.isEmpty() && node.getXincoCoreData().isEmpty() && tempAce.isReadPermission()) {
                try {
                    //Add the children nodes and data
                    XincoCoreNodeServer parent = null;
                    try {
                        parent = new XincoCoreNodeServer(node.getId());
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Populate children
                    parent.fillXincoCoreData();
                    parent.fillXincoCoreNodes();
                    addChildren(new XincoCoreNodeProperty(parent));
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                getMainWindow().showNotification(getResource().getString("error.accessdenied"),
                        getResource().getString("error.folder.sufficientrights"),
                        Window.Notification.TYPE_WARNING_MESSAGE);
            }
            // update details table
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{getResource().getString("general.id"),
                        node.getId()}, i++);
            xincoTable.addItem(new Object[]{getResource().getString("general.designation"),
                        node.getDesignation()}, i++);
            xincoTable.addItem(new Object[]{getResource().getString("general.language"),
                        getResource().getString(node.getXincoCoreLanguage().getDesignation()) + " ("
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
            xincoTable.addItem(new Object[]{getResource().getString("general.accessrights"), value}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            value = "";
            if (node.getStatusNumber() == 1) {
                value = getResource().getString("general.status.open") + "";
            }
            if (node.getStatusNumber() == 2) {
                value = getResource().getString("general.status.locked") + " (-)";
            }
            if (node.getStatusNumber() == 3) {
                value = getResource().getString("general.status.archived") + " (->)";
            }
            xincoTable.addItem(new Object[]{getResource().getString("general.status"), value}, i++);
        } else if (xincoTree.getValue() instanceof XincoCoreDataProperty) {
            // get ace
            XincoCoreData data = ((XincoCoreData) ((XincoCoreDataProperty) xincoTree.getValue()).getValue());
            try {
                tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(1),
                        (ArrayList) (data).getXincoCoreAcl());
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
            xincoTable.addItem(new Object[]{
                        getResource().getString("general.id"), data.getId()}, i++);
            xincoTable.addItem(new Object[]{
                        getResource().getString("general.designation"), data.getDesignation()}, i++);
            xincoTable.addItem(new Object[]{
                        getResource().getString("general.language"), data.getXincoCoreLanguage().getDesignation()
                        + " ("
                        + data.getXincoCoreLanguage().getSign()
                        + ")"}, i++);
            xincoTable.addItem(new Object[]{
                        getResource().getString("general.datatype"), getResource().getString(data.getXincoCoreDataType().getDesignation())
                        + " ("
                        + getResource().getString(data.getXincoCoreDataType().getDescription())
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
                        getResource().getString("general.accessrights"),
                        "[" + value + "]"}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            switch (data.getStatusNumber()) {
                case 1:
                    value = getResource().getString("general.status.open");
                    break;
                case 2:
                    value = getResource().getString("general.status.locked");
                    break;
                case 3:
                    value = getResource().getString("general.status.archived");
                    break;
                case 4:
                    value = getResource().getString("general.status.checkedout");
                    break;
                case 5:
                    value = getResource().getString("general.status.published");
                    break;
            }
            xincoTable.addItem(new Object[]{
                        getResource().getString("general.status"), value}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{
                        getResource().getString("general.typespecificattributes"), ""}, i++);
            // get add attributes of Core Data, if access granted
            java.util.List<XincoAddAttribute> attributes =
                    data.getXincoAddAttributes();
            java.util.List<XincoCoreDataTypeAttribute> dataTypeAttributes =
                    data.getXincoCoreDataType().getXincoCoreDataTypeAttributes();
            if (!attributes.isEmpty()) {
                for (int j = 0; j < attributes.size(); j++) {
                    header = getResource().getString(dataTypeAttributes.get(j).getDesignation());
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
                header = getResource().getString("error.accessdenied");
                value = getResource().getString("error.content.sufficientrights");
                xincoTable.addItem(new Object[]{header, value}, i++);
            }
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
            header = getResource().getString("general.logslastfirst");
            value = "";
            xincoTable.addItem(new Object[]{header, value}, i++);
            Calendar cal;
            XMLGregorianCalendar realcal;
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
                        Logger.getLogger(Xinco.class.getSimpleName()).log(Level.SEVERE, null, ce);
                    }
                }
                value = "("
                        + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getOpCode()
                        + ") "
                        + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getOpDescription();
                xincoTable.addItem(new Object[]{header, value}, i++);
                header = "";
                try {
                    value = getResource().getString("general.version")
                            + " "
                            + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getVersion().getVersionHigh()
                            + "."
                            + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getVersion().getVersionMid()
                            + "."
                            + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getVersion().getVersionLow()
                            + ""
                            + ((XincoCoreLog) data.getXincoCoreLogs().get(j)).getVersion().getVersionPostfix();
                    xincoTable.addItem(new Object[]{header, value}, i++);
                } catch (Exception ex) {
                    Logger.getLogger(Xinco.class.getSimpleName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        xincoTable.setPageLength(i - 1);
        xincoTable.requestRepaintAll();
    }

    private String getEndpoint() {
        String fullURL = getMainWindow().getURL().toString();
        String key = "xinco";
        String endpoint = fullURL.substring(0, fullURL.indexOf(key) + key.length()) + "/Xinco";
        return endpoint;
    }

    private Xinco_Service getService() throws MalformedURLException {
        if (service == null) {
            service = new Xinco_Service(new java.net.URL(getEndpoint()),
                    new QName("http://service.server.core.xinco.bluecubs.com/", "Xinco"));
        }
        return service;
    }
}
