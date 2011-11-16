package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.core.server.service.*;
import com.bluecubs.xinco.core.server.vaadin.wizard.WizardStep;
import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;
import com.bluecubs.xinco.core.server.vaadin.wizard.event.*;
import com.bluecubs.xinco.tools.XincoFileIconManager;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.Or;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.client.ui.dd.VerticalDropLocation;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.AbstractSelect.VerticalLocationIs;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.Tree.TreeTargetDetails;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.Window.ResizeEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Map.Entry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.vaadin.easyuploads.MultiFileUpload;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class Xinco extends Application implements Window.ResizeListener {

    //client version
    private XincoVersion xincoClientVersion = null;
    //TODO: use selected language
    private ResourceBundle xerb =
            ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", Locale.getDefault());
    private XincoCoreUser loggedUser = null;
    private XincoFileIconManager xfm = new XincoFileIconManager();
    //Table linking displayed item with it's id
    private Tree xincoTree = null;
    private Table xincoTable = null;
    private com.vaadin.ui.MenuBar menuBar = new com.vaadin.ui.MenuBar();
    private Item root;
    private Xinco_Service service;
    private Window wizardWindow = new Window();
    private DataDialog dataDialog;
    private DataTypeDialog dataTypeDialog;
    private AddAttributeDialog attrDialog;
    private DataDialogManager ddManager;
    private ArchiveDialog archDialog;
    private XincoCoreData data = new XincoCoreData();
    private XincoActivityTimer xat = null;
    private File fileToLoad;
    private String fileName;                    // Original file name
    private String defaultName;
    private HorizontalSplitPanel xeSplitPanel;
    private com.vaadin.ui.Button login = new com.vaadin.ui.Button(
            getResource().getString("general.login"),
            new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    showLoginDialog();
                }
            });
    private com.vaadin.ui.Button logout = new com.vaadin.ui.Button(
            getResource().getString("general.logout"),
            new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    loggedUser = null;
                    updateMenu();
                }
            });
    private ThemeResource smallIcon = new ThemeResource("img/blueCubsIcon16x16.GIF");
    private HierarchicalContainer xincoTreeContainer;

    @Override
    public void init() {
        try {
            //Switch to Xinco theme
            setTheme("xinco");
            defaultName = getResource().getString("general.clienttitle");
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
            try {
                XincoIdServer.getIds();
            } catch (XincoException ex) {
                try {
                    Logger.getLogger(Xinco.class.getName()).log(Level.WARNING, null, ex);
                    Logger.getLogger(Xinco.class.getName()).log(Level.INFO, "Creating ids to work around H2 issue...");
                    XincoIdServer temp = new XincoIdServer("xinco_dependency_behavior", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_dependency_behavior", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_core_group", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_core_data_type", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_core_ace", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_core_log", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_dependency_type", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_core_language", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_setting", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_core_user", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_core_node", 999);
                    temp.write2DB();
                    temp = new XincoIdServer("xinco_core_data", 999);
                    temp.write2DB();
                    Logger.getLogger(Xinco.class.getName()).log(Level.INFO, "Done!");
                } catch (XincoException ex1) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex1);
                } finally {
                    try {
                        for (Iterator<XincoIdServer> it = XincoIdServer.getIds().iterator(); it.hasNext();) {
                            XincoIdServer next = it.next();
                            Logger.getLogger(Xinco.class.getName()).log(Level.CONFIG,
                                    "{0}, {1}, {2}", new Object[]{next.getId(),
                                        next.getTablename(), next.getLastId()});
                        }
                    } catch (XincoException ex1) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
            //Build the window
            getMainWindow().removeAllComponents();
            if (xat == null) {
                //5 mins
                xat = new XincoActivityTimer(this, 5);
                xat.getActivityTimer().start();
            } else {
                resetTimer();
            }
            getMainWindow().addListener(new ClickListener() {

                @Override
                public void click(ClickEvent event) {
                    resetTimer();
                }
            });
            Embedded icon;
            icon = new Embedded(getResource().getString("general.clienttitle") + " - "
                    + getResource().getString("general.version") + " "
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.high").getIntValue() + "."
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.mid").getIntValue() + "."
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.low").getIntValue() + " "
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.postfix").getStringValue(), new ThemeResource("img/blueCubsSmall.gif"));
            icon.setType(Embedded.TYPE_IMAGE);
            getMainWindow().addComponent(icon);
            HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
            // Put two components in the container.
            splitPanel.setFirstComponent(getSideMenu());
            splitPanel.setSecondComponent(getXincoExplorer());
            splitPanel.setHeight(500, Sizeable.UNITS_PIXELS);
            splitPanel.setSplitPosition(15, Sizeable.UNITS_PERCENTAGE);
            getMainWindow().addComponent(splitPanel);
            //TODO: Enable?
            //getMainWindow().addComponent(getFooter());
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected FileResource getIcon(String extension) throws IOException {
        WebApplicationContext context = (WebApplicationContext) getContext();
        File iconsFolder = new File(context.getHttpSession().getServletContext().getRealPath("/VAADIN/themes/xinco") + System.getProperty("file.separator") + "icons");
        if (!iconsFolder.exists()) {
            //Create it
            iconsFolder.mkdirs();
        }
        File icon = new File(iconsFolder.getAbsolutePath()
                + System.getProperty("file.separator") + extension + ".png");
        if (!icon.exists()) {
            Image image = iconToImage(xfm.getIcon(extension));
            BufferedImage buffered = new BufferedImage(
                    image.getWidth(null),
                    image.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffered.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            ImageIO.write(buffered, "PNG", icon);
        }
        FileResource resource =
                new FileResource(icon, Xinco.this);
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

    private com.vaadin.ui.Component getXincoExplorer() {
        com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel();
        panel.setContent(new VerticalLayout());
        xeSplitPanel = new HorizontalSplitPanel();
        // Put two components in the container.
        xeSplitPanel.setFirstComponent(getXincoTree());
        xeSplitPanel.setSecondComponent(getXincoTable());
        xeSplitPanel.setHeight(500, Sizeable.UNITS_PIXELS);
        xeSplitPanel.setSplitPosition(100, Sizeable.UNITS_PERCENTAGE);
        //Hide details by default, user needs to log in for some features.
        getXincoTable().setVisible(false);
        panel.addComponent(menuBar);
        menuBar.setSizeFull();
        panel.addComponent(xeSplitPanel);
        updateMenu();
        return panel;
    }

    private Tree getXincoTree() {
        if (xincoTree == null) {
            try {
                xincoTreeContainer = new HierarchicalContainer();
                xincoTreeContainer.addContainerProperty("caption", String.class, null);
                //Add icon support
                xincoTreeContainer.addContainerProperty("icon", Resource.class, null);
                xincoTree = new Tree(getResource().getString("menu.repository"));
                xincoTree.setContainerDataSource(xincoTreeContainer);
                xincoTree.setDebugId("xinco_tree");
                xincoTree.setItemIconPropertyId("icon");
                refresh();
                xincoTree.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        processTreeSelection(event.getProperty().toString());
                    }
                });
                xincoTree.addListener(new Tree.ExpandListener() {

                    @Override
                    public void nodeExpand(ExpandEvent event) {
                        processTreeSelection(event.getItemId().toString());
                    }
                });
                xincoTree.setImmediate(true);
                xincoTree.setItemCaptionPropertyId("caption");
                xincoTree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
                //Dra and drop support
                // Set the tree in drag source mode
                xincoTree.setDragMode(TreeDragMode.NODE);
                xincoTree.setDropHandler(new DropHandler() {

                    @Override
                    public void drop(DragAndDropEvent event) {
                        // Wrapper for the object that is dragged
                        Transferable t = event.getTransferable();

                        // Make sure the drag source is the same tree
                        if (t.getSourceComponent() != xincoTree) {
                            return;
                        }

                        TreeTargetDetails target = (TreeTargetDetails) event.getTargetDetails();

                        // Get ids of the dragged item and the target item
                        Object sourceItemId = t.getData("itemId");
                        Object targetItemId = target.getItemIdOver();

                        // On which side of the target the item was dropped 
                        VerticalDropLocation location = target.getDropLocation();

                        // Drop right on an item -> make it a child
                        if (location == VerticalDropLocation.MIDDLE) {
                            xincoTree.setParent(sourceItemId, targetItemId);
                            //Now update things in the database
                            System.err.println("Source: " + sourceItemId + ", Target: " + targetItemId);
                            try {
                                refresh();
                            } catch (XincoException ex) {
                                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    @Override
                    public AcceptCriterion getAcceptCriterion() {
                        //Accepts drops only on nodes that allow children, but between all nodes
                        return new Or(Tree.TargetItemAllowsChildren.get(), new Not(VerticalLocationIs.MIDDLE));
                    }
                });
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return xincoTree;
    }

    private Table getXincoTable() {
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
            xincoTable.addStyleName("striped");
        }
        return xincoTable;
    }

    private void addChildren(String parent) throws XincoException {
        addChildrenNodes(parent);
        addChildrenData(parent);
    }

    private void addChildrenNodes(String parent) throws XincoException {
        XincoCoreNodeServer xcns =
                new XincoCoreNodeServer(Integer.valueOf(parent.substring(parent.indexOf('-') + 1)));
        for (Iterator<XincoCoreNode> it = xcns.getXincoCoreNodes().iterator(); it.hasNext();) {
            XincoCoreNode subnode = it.next();
            String id = "node-" + subnode.getId();
            Item item = xincoTreeContainer.addItem(id);
            item.getItemProperty("caption").setValue(subnode.getDesignation());
            // Set it to be a child.
            xincoTreeContainer.setParent(id, parent);
            //Allow to have children
            xincoTreeContainer.setChildrenAllowed(id, true);
        }
    }

    private void addChildrenData(String parent) throws XincoException {
        XincoCoreNodeServer xcns = new XincoCoreNodeServer(Integer.valueOf(parent.substring(parent.indexOf('-') + 1)));
        for (Iterator<XincoCoreData> it = xcns.getXincoCoreData().iterator(); it.hasNext();) {
            XincoCoreData temp = it.next();
            //Add childen data
            String id = "data-" + temp.getId();
            Item item = xincoTreeContainer.addItem(id);
            item.getItemProperty("caption").setValue(temp.getDesignation());
            // Set it to be a child.
            xincoTreeContainer.setParent(id, parent);
            // Set as leaves
            xincoTreeContainer.setChildrenAllowed(id, false);
            //TODO: Add icons to files, not a critical feature
//            try {
//                if (temp.getDesignation() != null
//                        && temp.getDesignation().contains(".")
//                        && temp.getDesignation().substring(temp.getDesignation().lastIndexOf(".") + 1,
//                        temp.getDesignation().length()).length() == 3) {
//                    //Set Icon
//                    switch (temp.getXincoCoreDataType().getId()) {
//                        case 1://File
//                            //Fall through
//                        case 5://Rendering
//                            item.getItemProperty("icon").setValue(
//                                    getIcon(temp.getDesignation().substring(temp.getDesignation().lastIndexOf(".") + 1,
//                                    temp.getDesignation().length())));
//                            break;
//                        case 2://Text
//                            break;
//                        case 3://URL
//                            break;
//                        case 4://Contact
//                            item.getItemProperty("icon").setValue(new ThemeResource("icons/contact.gif"));
//                            break;
//                    }
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
        //Menus always enabled
        repo.addItem(getResource().getString("menu.repository.refresh"),
                smallIcon,//Icon 
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
        //Exclusive menus for Nodes
        if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("node")) {
            //Actions user needs to be logged in
            if (getLoggedUser() != null) {
                XincoCoreNodeServer node;
                try {
                    node = new XincoCoreNodeServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
                XincoCoreACE tempAce = null;
                try {
                    tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(getLoggedUser().getId()),
                            (ArrayList) (node).getXincoCoreAcl());
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (tempAce.isWritePermission()) {
                    repo.addItem(getResource().getString("menu.repository.addfolder"),
                            smallIcon,//Icon 
                            new com.vaadin.ui.MenuBar.Command() {

                        @Override
                        public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                            //Show the Data Folder Dialog window
                            showDataFolderDialog(true);
                        }
                    });

                    repo.addItem(getResource().getString("menu.repository.adddata"),
                            smallIcon,//Icon 
                            new com.vaadin.ui.MenuBar.Command() {

                        @Override
                        public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                            //Show the Data Folder Dialog window
                            showDataDialog(true);
                        }
                    });

                    repo.addItem(getResource().getString("menu.repository.adddatastructure"),
                            smallIcon,//Icon 
                            new com.vaadin.ui.MenuBar.Command() {

                        StringBuilder sb = new StringBuilder();

                        @Override
                        public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                            //Show the Data Structure Dialog window
                            final Window w = new Window("Mass import");
                            MultiFileUpload fileUpload = new MultiFileUpload() {

                                @Override
                                protected void handleFile(File file, String fileName,
                                        String mimeType, long length) {
                                    if (length == 0) {
                                        //Empty file or folder
                                        if (sb.toString().isEmpty()) {
                                            sb.append(getResource().getString("window.massiveimport.notsupported"));
                                        }
                                        sb.append("\n").append(fileName);
                                        getMainWindow().showNotification(
                                                sb.toString(),
                                                Notification.TYPE_TRAY_NOTIFICATION);
                                    } else {
                                        getMainWindow().showNotification(
                                                getResource().getString("window.massiveimport.progress"),
                                                Notification.TYPE_TRAY_NOTIFICATION);
                                        try {
                                            loadFile(file, fileName);
                                        } catch (XincoException ex) {
                                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (MalformedURLException ex) {
                                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                                //TODO: Notify the user about any issues
                            };
                            fileUpload.setWidth("600px");
                            w.addComponent(fileUpload);
                            w.setModal(true);
                            w.center();
                            getMainWindow().addWindow(w);
                        }
                    });
                }
            } //Actions user don't needs to be logged in
            else {
            }
        }
        //Exclusive menus for Data
        if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("data")) {
            //Actions user needs to be logged in
            if (getLoggedUser() != null) {
                repo.addItem(getResource().getString("menu.repository.vieweditaddattributes"),
                        smallIcon,//Icon 
                        new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //Show the Data Folder Dialog window
                        showDataDialog(false);
                    }
                });
            }//Actions user don't needs to be logged in
            else {
            }
        }
        //Menus for both data and folders
        if (xincoTree.getValue() != null
                && (xincoTree.getValue().toString().startsWith("data")
                || xincoTree.getValue().toString().startsWith("node"))) {
            //Actions user needs to be logged in
            if (getLoggedUser() != null) {
                repo.addItem(getResource().getString("menu.edit.folderdata"),
                        smallIcon,//Icon 
                        new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        if (xincoTree.getValue().toString().startsWith("node")) {
                            showDataFolderDialog(false);
                        } else if (xincoTree.getValue().toString().startsWith("data")) {
                            showDataDialog(false);
                        }
                    }
                });
                repo.addItem(getResource().getString("menu.edit.acl"),
                        smallIcon,//Icon 
                        new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        showACLDialog();
                    }
                });
                repo.addItem(getResource().getString("menu.edit.movefolderdatatoclipboard"),
                        smallIcon,//Icon 
                        new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //TODO: Implement
                    }
                });
                repo.addItem(getResource().getString("menu.edit.insertfolderdata"),
                        smallIcon,//Icon 
                        new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //TODO: Implement
                    }
                });
            }//Actions user don't needs to be logged in
            else {
            }
        }
        //Hide it if empty
        menuBar.setVisible(!menuBar.getItems().isEmpty());
        //Update the side menu
        updateSideMenu();
    }

    private void loadFile(File file, String fileName) throws XincoException, MalformedURLException, IOException {
        String path_to_file = file.getAbsolutePath();
        File temp = null;
        if (!file.getName().equals(fileName)) {
            //Different files, probably stored as a temp file. Need to rename it
            //Create a temp file to hide the transaction
            temp = new File(file.getParentFile().getAbsolutePath() + System.getProperty("file.separator") + UUID.randomUUID().toString());
            temp.mkdirs();
            path_to_file = temp.getAbsolutePath() + System.getProperty("file.separator") + fileName;
            if (!file.renameTo(new File(path_to_file))) {
                throw new RuntimeException("Unable to rename file!");
            }
            file.deleteOnExit();
            temp.deleteOnExit();
        }
        XincoCoreLog newlog;
        XincoCoreDataType xcdt1 = null;
        //find data type = 1
        for (int j = 0; j < XincoCoreDataTypeServer.getXincoCoreDataTypes().size(); j++) {
            if (((XincoCoreDataType) XincoCoreDataTypeServer.getXincoCoreDataTypes().get(j)).getId() == 1) {
                xcdt1 = (XincoCoreDataType) XincoCoreDataTypeServer.getXincoCoreDataTypes().get(j);
                break;
            }
        }
        //find default language
        XincoCoreLanguage xcl1;
        int selection = -1;
        int alt_selection = 0;
        for (int j = 0; j < XincoCoreLanguageServer.getXincoCoreLanguages().size(); j++) {
            if (((XincoCoreLanguage) XincoCoreLanguageServer.getXincoCoreLanguages().get(j)).getSign().toLowerCase().compareTo(Locale.getDefault().getLanguage().toLowerCase()) == 0) {
                selection = j;
                break;
            }
            if (((XincoCoreLanguage) XincoCoreLanguageServer.getXincoCoreLanguages().get(j)).getId() == 1) {
                alt_selection = j;
            }
        }
        if (selection == -1) {
            selection = alt_selection;
        }
        xcl1 = (XincoCoreLanguage) XincoCoreLanguageServer.getXincoCoreLanguages().get(selection);
        data = new XincoCoreData();
        data.setId(0);
        // set data attributes
        data.setXincoCoreNodeId(Integer.valueOf(fileName));
        data.setDesignation(fileName);
        data.setXincoCoreDataType(xcdt1);
        data.setXincoCoreLanguage(xcl1);
        data.setStatusNumber(1);
        addDefaultAddAttributes();
        // invoke web service (update data / upload file / add log)
        // load file
        long totalLen = 0;
        CheckedInputStream cin = null;
        ByteArrayOutputStream out;

        byte[] byteArray = null;
        try {
            cin = new CheckedInputStream(new FileInputStream(path_to_file),
                    new CRC32());
            out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len;

            totalLen = 0;
            while ((len = cin.read(buf)) > 0) {
                out.write(buf, 0, len);
                totalLen += len;
            }
            byteArray = out.toByteArray();
            out.close();
            // update attributes
            ((XincoAddAttribute) data.getXincoAddAttributes().get(0)).setAttribVarchar(fileName);
            ((XincoAddAttribute) data.getXincoAddAttributes().get(1)).setAttribUnsignedint(totalLen);
            ((XincoAddAttribute) data.getXincoAddAttributes().get(2)).setAttribVarchar(""
                    + cin.getChecksum().getValue());
            ((XincoAddAttribute) data.getXincoAddAttributes().get(3)).setAttribUnsignedint(1);
            ((XincoAddAttribute) data.getXincoAddAttributes().get(4)).setAttribUnsignedint(0);
        } catch (Exception fe) {
            Logger.getLogger(Xinco.class.getSimpleName()).log(Level.SEVERE, null, fe);
            throw new XincoException(xerb.getString("datawizard.unabletoloadfile") + "\n" + fe.getLocalizedMessage());
        }
        // save data to server
        data = getService().getXincoPort().setXincoCoreData(data, loggedUser);
        if (data == null) {
            throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
        }
        // add log
        newlog = new XincoCoreLog();
        newlog.setOpCode(OPCode.CREATION.ordinal() + 1);
        newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName())
                + "!" + " ("
                + xerb.getString("general.user") + ": "
                + loggedUser.getUsername()
                + ")");
        newlog.setXincoCoreUserId(loggedUser.getId());
        newlog.setXincoCoreDataId(data.getId());
        newlog.setVersion(new XincoVersion());
        newlog.getVersion().setVersionHigh(1);
        newlog.getVersion().setVersionMid(0);
        newlog.getVersion().setVersionLow(0);
        newlog.getVersion().setVersionPostfix("");
        try {
            DatatypeFactory factory = DatatypeFactory.newInstance();
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            newlog.setOpDatetime(factory.newXMLGregorianCalendar(cal));
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
        // save log to server
        newlog = getService().getXincoPort().setXincoCoreLog(newlog, loggedUser);
        if (newlog == null) {
            Logger.getLogger(Xinco.class.getSimpleName()).severe("Unable to create new log entry!");
        } else {
            data.getXincoCoreLogs().add(newlog);
        }
        // upload file
        if (getService().getXincoPort().uploadXincoCoreData(data, byteArray, loggedUser) != totalLen) {
            cin.close();
            file.delete();
            if (temp != null) {
                temp.delete();
            }
            throw new XincoException(xerb.getString("datawizard.fileuploadfailed"));
        }
        cin.close();
        file.delete();
        if (temp != null) {
            temp.delete();
        }
    }

    void setLock() {
        //
    }

    /**
     * Reset activity timer
     */
    public void resetTimer() {
        if (xat != null) {
            xat.getActivityTimer().restart();
        }
    }

    @Override
    public void windowResized(ResizeEvent e) {
        for (Window w : getMainWindow().getChildWindows()) {
            //Center sub window in new screen size
            w.center();
        }
    }

    private void showACLDialog() {
        final Window aclWindow = new Window();
        final Form form = new Form();
        form.setCaption(getResource().getString("window.acl"));
        final HashMap<String, XincoCoreACEServer> aceList = new HashMap<String, XincoCoreACEServer>();
        // Create a table and add a style to allow setting the row height in theme.
        final Table table = new Table();
        final TwinColSelect acls = new TwinColSelect();
        acls.setImmediate(true);
        acls.setNewItemsAllowed(false);
        acls.setNullSelectionAllowed(true);
        acls.setMultiSelect(true);
        acls.setNewItemsAllowed(false);
        acls.setLeftColumnCaption(getResource().getString("general.notincluded"));
        acls.setRightColumnCaption(getResource().getString("general.included"));
        table.addStyleName("striped");
        // Preselect values
        HashSet<String> preselected = new HashSet<String>();
        /*
         * Define the names and data types of columns. The "default value"
         * parameter is meaningless here.
         */
        table.addContainerProperty(
                getResource().getString("general.name"),
                com.vaadin.ui.Label.class, null);
        table.addContainerProperty(
                getResource().getString("general.type"), com.vaadin.ui.Label.class, null);
        table.addContainerProperty(
                getResource().getString("general.acl.adminpermission"),
                CheckBox.class, null);
        table.addContainerProperty(
                getResource().getString("general.acl.readpermission"),
                CheckBox.class, null);
        table.addContainerProperty(
                getResource().getString("general.acl.writepermission"),
                CheckBox.class, null);
        table.addContainerProperty(
                getResource().getString("general.acl.executepermission"),
                CheckBox.class, null);
        table.setPageLength(10);
        com.vaadin.ui.Label name;
        com.vaadin.ui.Label type;
        //Add Groups
        for (Iterator it = XincoCoreGroupServer.getXincoCoreGroups().iterator(); it.hasNext();) {
            XincoCoreGroupServer group = (XincoCoreGroupServer) it.next();
            String itemId = getResource().getString("general.group") + "-" + group.getId();
            String designation = ((XincoCoreGroupServer) group).getDesignation();
            String value = getResource().containsKey(designation)
                    ? getResource().getString(designation) : designation;
            name = new com.vaadin.ui.Label(value);
            type = new com.vaadin.ui.Label(getResource().getString("general.group"));
            final CheckBox admin = new CheckBox(getResource().getString("general.acl.adminpermission"));
            admin.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (admin.getData() != null) {
                        aceList.get(admin.getData().toString()).setAdminPermission((Boolean) admin.getValue());
                    }
                }
            });
            admin.setImmediate(true);
            final CheckBox execute = new CheckBox(getResource().getString("general.acl.executepermission"));
            execute.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (execute.getData() != null) {
                        aceList.get(execute.getData().toString()).setExecutePermission((Boolean) execute.getValue());
                    }
                }
            });
            execute.setImmediate(true);
            final CheckBox read = new CheckBox(getResource().getString("general.acl.readpermission"));
            read.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (read.getData() != null) {
                        aceList.get(read.getData().toString()).setReadPermission((Boolean) read.getValue());
                    }
                }
            });
            read.setImmediate(true);
            final CheckBox write = new CheckBox(getResource().getString("general.acl.writepermission"));
            write.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (write.getData() != null) {
                        aceList.get(write.getData().toString()).setWritePermission((Boolean) write.getValue());
                    }
                }
            });
            write.setImmediate(true);
            XincoCoreACEServer acl = null;
            if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("node")) {
                try {
                    XincoCoreNodeServer tempNode = new XincoCoreNodeServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                    try {
                        acl = new XincoCoreACEServer(0, 0, group.getId(), tempNode.getId(), 0, false, false, false, false);
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (Iterator<Object> it2 = tempNode.getXincoCoreAcl().iterator(); it2.hasNext();) {
                        XincoCoreACEServer temp = (XincoCoreACEServer) it2.next();
                        if (temp.getXincoCoreGroupId() == group.getId()) {
                            admin.setValue(temp.isAdminPermission());
                            execute.setValue(temp.isExecutePermission());
                            read.setValue(temp.isReadPermission());
                            write.setValue(temp.isWritePermission());
                            acl = temp;
                            break;
                        }
                    }
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("data")) {
                try {
                    XincoCoreDataServer tempData = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                    try {
                        acl = new XincoCoreACEServer(0, 0, 0, 0, tempData.getId(), false, false, false, false);
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (Iterator<XincoCoreACE> it2 = tempData.getXincoCoreAcl().iterator(); it2.hasNext();) {
                        XincoCoreACEServer temp = (XincoCoreACEServer) it2.next();
                        if (temp.getXincoCoreGroupId() == group.getId()) {
                            admin.setValue(temp.isAdminPermission());
                            execute.setValue(temp.isExecutePermission());
                            read.setValue(temp.isReadPermission());
                            write.setValue(temp.isWritePermission());
                            acl = temp;
                            break;
                        }
                    }
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Collections.addAll(preselected, value);
            aceList.put(itemId, acl);
            // Create the table row.
            admin.setData(itemId);
            read.setData(itemId);
            write.setData(itemId);
            execute.setData(itemId);
            table.addItem(new Object[]{name, type, admin, read, write, execute},
                    itemId);
            acls.addItem(value);
        }
        //Add users
        for (Iterator it = XincoCoreUserServer.getXincoCoreUsers().iterator(); it.hasNext();) {
            XincoCoreUserServer user = (XincoCoreUserServer) it.next();
            String itemId = getResource().getString("general.user") + "-" + user.getId();
            String userLabel = user.getFirstname() + " "
                    + user.getName() + " (" + user.getUsername() + ")";
            name = new com.vaadin.ui.Label(userLabel);
            type = new com.vaadin.ui.Label(getResource().getString("general.user"));
            final CheckBox admin = new CheckBox(getResource().getString("general.acl.adminpermission"));
            admin.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (admin.getData() != null) {
                        aceList.get(admin.getData().toString()).setAdminPermission((Boolean) admin.getValue());
                    }
                }
            });
            admin.setImmediate(true);
            final CheckBox execute = new CheckBox(getResource().getString("general.acl.executepermission"));
            execute.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (execute.getData() != null) {
                        aceList.get(execute.getData().toString()).setExecutePermission((Boolean) execute.getValue());
                    }
                }
            });
            execute.setImmediate(true);
            final CheckBox read = new CheckBox(getResource().getString("general.acl.readpermission"));
            read.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (read.getData() != null) {
                        aceList.get(read.getData().toString()).setReadPermission((Boolean) read.getValue());
                    }
                }
            });
            read.setImmediate(true);
            final CheckBox write = new CheckBox(getResource().getString("general.acl.writepermission"));
            write.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (write.getData() != null) {
                        aceList.get(write.getData().toString()).setWritePermission((Boolean) write.getValue());
                    }
                }
            });
            write.setImmediate(true);
            XincoCoreACEServer acl = null;
            if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("node")) {
                try {
                    XincoCoreNodeServer tempNode = new XincoCoreNodeServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                    try {
                        acl = new XincoCoreACEServer(0, loggedUser.getId(), 0, tempNode.getId(), 0, false, false, false, false);
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (Iterator<Object> it2 = tempNode.getXincoCoreAcl().iterator(); it2.hasNext();) {
                        XincoCoreACEServer temp = (XincoCoreACEServer) it2.next();
                        if (temp.getXincoCoreGroupId() == user.getId()) {
                            admin.setValue(temp.isAdminPermission());
                            execute.setValue(temp.isExecutePermission());
                            read.setValue(temp.isReadPermission());
                            write.setValue(temp.isWritePermission());
                            acl = temp;
                            break;
                        }
                    }
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("data")) {
                try {
                    XincoCoreDataServer tempData = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                    try {
                        acl = new XincoCoreACEServer(0, 0, loggedUser.getId(), 0, tempData.getId(), false, false, false, false);
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (Iterator<XincoCoreACE> it2 = tempData.getXincoCoreAcl().iterator(); it2.hasNext();) {
                        XincoCoreACEServer temp = (XincoCoreACEServer) it2.next();
                        if (temp.getXincoCoreGroupId() == user.getId()) {
                            admin.setValue(temp.isAdminPermission());
                            execute.setValue(temp.isExecutePermission());
                            read.setValue(temp.isReadPermission());
                            write.setValue(temp.isWritePermission());
                            acl = temp;
                            break;
                        }
                    }
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Collections.addAll(preselected, userLabel);
            admin.setData(itemId);
            read.setData(itemId);
            write.setData(itemId);
            execute.setData(itemId);
            // Create the table row.
            table.addItem(new Object[]{name, type, admin, read, write, execute},
                    itemId);
            aceList.put(itemId, acl);
            acls.addItem(getResource().getString("general.user") + ":" + userLabel);
        }
        table.setSizeFull();
        form.getLayout().addComponent(table);
        form.setFooter(new HorizontalLayout());
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getResource().getString("general.save"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        updateMenu();
                        getMainWindow().removeWindow(aclWindow);
                    }
                });
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                for (Iterator<Entry<String, XincoCoreACEServer>> it = aceList.entrySet().iterator(); it.hasNext();) {
                    Entry<String, XincoCoreACEServer> e = it.next();
                    XincoCoreACEServer stored = null;
                    XincoCoreACEServer value = e.getValue();
                    try {
                        stored = new XincoCoreACEServer(e.getValue().getId());
                    } catch (XincoException ex) {
                        try {
                            //Doesn't exist, create a new one
                            stored = new XincoCoreACEServer(0,
                                    value.getXincoCoreUserId(),
                                    value.getXincoCoreGroupId(),
                                    value.getXincoCoreNodeId(),
                                    value.getXincoCoreDataId(),
                                    false, false,
                                    false, false);
                        } catch (XincoException ex1) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }
                    if (stored.isAdminPermission() != e.getValue().isAdminPermission()
                            || stored.isExecutePermission() != e.getValue().isExecutePermission()
                            || stored.isReadPermission() != e.getValue().isReadPermission()
                            || stored.isWritePermission() != e.getValue().isWritePermission()) {
                        //Is different so update
                        stored.setReadPermission(e.getValue().isReadPermission());
                        stored.setAdminPermission(e.getValue().isAdminPermission());
                        stored.setExecutePermission(e.getValue().isExecutePermission());
                        stored.setWritePermission(e.getValue().isWritePermission());
                    }
                    try {
                        if (stored.isAdminPermission()
                                || stored.isExecutePermission()
                                || stored.isReadPermission()
                                || stored.isWritePermission()) {
                            stored.write2DB();
                        }
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                getMainWindow().removeWindow(aclWindow);
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        form.setSizeFull();
        aclWindow.addComponent(form);
        aclWindow.center();
        aclWindow.setModal(true);
        aclWindow.setWidth(80, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(aclWindow);
    }

    private void showLoginDialog() {
        final Window loginWindow = new Window();
        final Form form = new Form();
        form.setCaption(getResource().getString("window.connection") + ":");
        com.vaadin.ui.TextField username =
                new com.vaadin.ui.TextField(getResource().getString("general.username") + ":");
        PasswordField password =
                new PasswordField(getResource().getString("general.password") + ":");
        form.addField("username", username);
        form.addField("password", password);
        form.getField("username").setRequired(true);
        form.getField("username").focus();
        form.getField("username").setRequiredError(getResource().getString("message.missing.username"));
        form.getField("password").setRequired(true);
        form.getField("password").setRequiredError(getResource().getString("message.missing.password"));
        form.setFooter(new HorizontalLayout());
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getResource().getString("general.save"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        updateMenu();
                        getMainWindow().removeWindow(loginWindow);
                    }
                });
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                //Disable data fields, make sure nothing gets modified after clicking save
                form.getField("username").setEnabled(false);
                form.getField("password").setEnabled(false);
                commit.setEnabled(false);
                cancel.setEnabled(false);
                if (!XincoCoreUserServer.validCredentials(
                        ((com.vaadin.ui.TextField) form.getField("username")).getValue().toString(),
                        ((PasswordField) form.getField("password")).getValue().toString(),
                        true)) {
                    getMainWindow().showNotification(
                            getResource().getString("menu.connection.error.user"),
                            Notification.TYPE_WARNING_MESSAGE);
                    //Enable so they can retry
                    form.getField("username").setEnabled(true);
                    form.getField("password").setEnabled(true);
                    commit.setEnabled(true);
                    cancel.setEnabled(true);
                } else {
                    try {
                        //Update logged user
                        loggedUser = new XincoCoreUserServer(((com.vaadin.ui.TextField) form.getField("username")).getValue().toString(),
                                ((PasswordField) form.getField("password")).getValue().toString());
                        updateMenu();
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    getMainWindow().removeWindow(loginWindow);
                }
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        form.setSizeFull();
        loginWindow.addComponent(form);
        loginWindow.center();
        loginWindow.setModal(true);
        loginWindow.setWidth(25, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(loginWindow);
    }

    private com.vaadin.ui.Component getSideMenu() throws XincoException {
        Accordion menu = new Accordion();
        menu.setSizeFull();
        com.vaadin.ui.Panel accountPanel = new com.vaadin.ui.Panel(
                getResource().getString("window.loggingdetails.action"));
        accountPanel.setContent(new VerticalLayout());
        accountPanel.addComponent(login);
        accountPanel.addComponent(logout);
        login.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        logout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        menu.addTab(accountPanel, "Account", null);
        //TODO: Need to add more stuff?
        return menu;
    }

    private com.vaadin.ui.Component getFooter() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateSideMenu() {
        login.setEnabled(loggedUser == null);
        logout.setEnabled(loggedUser != null);
    }

    private class ArchiveDialog extends CustomComponent {

        private final Select archiveModel = new Select(
                getResource().getString("window.archive.archivingmodel") + ":");

        public ArchiveDialog() {
            com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel(
                    getResource().getString("window.datatype"));
            panel.setContent(new VerticalLayout());
            CheckBox revisionModelCheckbox =
                    new CheckBox(
                    getResource().getString("window.archive.revisionmodel"));
            panel.addComponent(revisionModelCheckbox);
            java.util.List<XincoAddAttribute> attributes;
            if (data.getId() == 0) {
                // Is a new data, there's nothing yet in the database.
                // Load local values.
                attributes = data.getXincoAddAttributes();
            } else {
                attributes = XincoAddAttributeServer.getXincoAddAttributes(data.getId());
            }
            int i = 0;
            archiveModel.addItem(i);
            archiveModel.setItemCaption(i, getResource().getString("window.archive.archivingmodel.none"));
            //Set as default
            archiveModel.setValue(i);
            i++;
            archiveModel.addItem(i);
            archiveModel.setItemCaption(i, getResource().getString("window.archive.archivingmodel.archivedate"));
            i++;
            archiveModel.addItem(i);
            archiveModel.setItemCaption(i, getResource().getString("window.archive.archivingmodel.archivedays"));
            i++;
            panel.addComponent(archiveModel);
            //processing independent of creation
            revisionModelCheckbox.setValue(attributes.get(3).getAttribUnsignedint() == 0);
            final DateField date = new DateField();
            // Set the date and time to present
            date.setValue(new Date());
            date.setDateFormat("dd-MM-yyyy");
            date.setCaption(getResource().getString("window.archive.archivedate") + ":");
            panel.addComponent(date);
            //Disabled by default
            date.setEnabled(false);
            //set date / days
            //convert clone from remote time to local time
            Calendar cal = (Calendar) ((XMLGregorianCalendar) (attributes.get(5)).getAttribDatetime()).toGregorianCalendar().clone();
            Calendar realcal = ((XMLGregorianCalendar) (attributes.get(5)).getAttribDatetime()).toGregorianCalendar();
            Calendar ngc = new GregorianCalendar();
            cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - realcal.get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + realcal.get(Calendar.DST_OFFSET)));
            date.setValue(cal.getTime());
            final com.vaadin.ui.TextField days = new com.vaadin.ui.TextField(
                    getResource().getString("window.archive.archivedays") + ":");
            panel.addComponent(days);
            //Disabled by default
            days.setEnabled(false);
            archiveModel.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    switch (Integer.valueOf(event.getProperty().toString())) {
                        case 1:
                            //Enable date
                            date.setEnabled(true);
                            days.setEnabled(false);
                            break;
                        case 2:
                            //Enable days
                            days.setEnabled(true);
                            date.setEnabled(false);
                            break;
                        default:
                            //Disable both
                            date.setEnabled(false);
                            days.setEnabled(false);
                            break;
                    }
                }
            });
            // Set the size as undefined at all levels
            panel.getContent().setSizeUndefined();
            panel.setSizeUndefined();
            setSizeUndefined();
            // The composition root MUST be set
            setCompositionRoot(panel);
        }

        /**
         * @return the archiveModel
         */
        public Select getArchiveModel() {
            return archiveModel;
        }
    }

    private class DataTypeDialog extends CustomComponent {

        private final Select types = new Select(getResource().getString("window.datatype.datatype") + ":");

        public DataTypeDialog() {
            com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel(getResource().getString("window.datatype"));
            panel.setContent(new VerticalLayout());
            //Data Type selection
            int i = 1;
            for (Iterator it = XincoCoreDataTypeServer.getXincoCoreDataTypes().iterator(); it.hasNext();) {
                Object type = it.next();
                String designation = ((XincoCoreDataTypeServer) type).getDesignation();
                if (getResource().containsKey(designation)) {
                    String value = getResource().getString(designation);
                    types.addItem(((XincoCoreDataTypeServer) type).getId());
                    types.setItemCaption(((XincoCoreDataTypeServer) type).getId(), value);
                    i++;
                }
            }
            panel.addComponent(types);
            // Set the size as undefined at all levels
            panel.getContent().setSizeUndefined();
            panel.setSizeUndefined();
            setSizeUndefined();

            // The composition root MUST be set
            setCompositionRoot(panel);
        }

        /**
         * @return the types
         */
        public Select getTypes() {
            return types;
        }
    }

    private class AddAttributeDialog extends CustomComponent {

        public AddAttributeDialog(XincoCoreData data) {
            com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel(getResource().getString("window.addattributesuniversal"));
            panel.setContent(new VerticalLayout());
            Table attrTable = new Table("attributes");
            /*
             * Define the names and data types of columns. The "default value"
             * parameter is meaningless here.
             */
            attrTable.addContainerProperty(getResource().getString("general.attribute"),
                    String.class, null);
            attrTable.addContainerProperty(getResource().getString("general.details"),
                    String.class, null);
            //prevent editing of fixed attributes for certain data types
            int start = 0;
            //file = 1
            if (data.getXincoCoreDataType().getId() == 1) {
                start = 8;
            }
            //text = 2
            if (data.getXincoCoreDataType().getId() == 2) {
                start = 1;
            }
            java.util.List<com.bluecubs.xinco.core.server.service.XincoAddAttribute> attributes;
            if (data.getId() == 0) {
                // Is a new data, there's nothing yet in the database.
                // Load local values.
                attributes = data.getXincoAddAttributes();
            } else {
                attributes = XincoAddAttributeServer.getXincoAddAttributes(data.getId());
            }
            java.util.List<XincoCoreDataTypeAttribute> dataTypeAttributes = data.getXincoCoreDataType().getXincoCoreDataTypeAttributes();
            int count = 0;
            for (int i = start; i < attributes.size(); i++) {
                String value = "";
                if (dataTypeAttributes.get(i).getDataType().equals("datetime")) {
                    value = "" + ((XMLGregorianCalendar) (attributes.get(i)).getAttribDatetime()).toGregorianCalendar().getTime().toString();
                }
                if (dataTypeAttributes.get(i).getDataType().equals("double")) {
                    value = "" + (attributes.get(i)).getAttribDouble();
                }
                if (dataTypeAttributes.get(i).getDataType().equals("int")) {
                    value = "" + (attributes.get(i)).getAttribInt();
                }
                if (dataTypeAttributes.get(i).getDataType().equals("text")) {
                    value = "" + (attributes.get(i)).getAttribText();
                }
                if (dataTypeAttributes.get(i).getDataType().equals("unsignedint")) {
                    value = "" + (attributes.get(i)).getAttribUnsignedint();
                }
                if (dataTypeAttributes.get(i).getDataType().equals("varchar")) {
                    value = "" + (attributes.get(i)).getAttribVarchar();
                }
                attrTable.addItem(new Object[]{
                            dataTypeAttributes.get(i).getDesignation(), value}, count++);
            }
            attrTable.setEditable(true);
            panel.addComponent(attrTable);
            // Set the size as undefined at all levels
            panel.getContent().setSizeUndefined();
            panel.setSizeUndefined();
            setSizeUndefined();

            // The composition root MUST be set
            setCompositionRoot(panel);
        }
    }

    private class DataDialog extends CustomComponent {

        private com.vaadin.ui.TextField idField;
        private com.vaadin.ui.TextField designationField;
        private com.vaadin.ui.TextField statusField;
        private final Select languages = new Select(getResource().getString("general.language") + ":");

        public DataDialog(boolean newData) {
            com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel(getResource().getString("window.datadetails"));
            panel.setContent(new VerticalLayout());
            //ID
            idField = new com.vaadin.ui.TextField(getResource().getString("general.id") + ":");
            panel.addComponent(idField);
            if (newData) {
                idField.setValue("0");
            } else {
                idField.setValue(data.getId());
            }
            //Not editable
            idField.setEnabled(false);
            //Designation
            designationField = new com.vaadin.ui.TextField(getResource().getString("general.designation") + ":");
            panel.addComponent(designationField);
            designationField.setValue(data.getDesignation());
            //Language selection
            int i = 0;
            for (Object language : XincoCoreLanguageServer.getXincoCoreLanguages()) {
                String designation = ((XincoCoreLanguageServer) language).getDesignation();
                if (getResource().containsKey(designation)) {
                    String value = getResource().getString(designation);
                    languages.addItem(((XincoCoreLanguageServer) language).getId());
                    languages.setItemCaption(((XincoCoreLanguageServer) language).getId(), value);
                    if (newData && ((XincoCoreLanguageServer) language).getSign().equals("en")) //Select by default
                    {
                        languages.setValue(((XincoCoreLanguageServer) language).getId());
                    } else if (!newData && ((XincoCoreLanguageServer) language).getId() == data.getXincoCoreLanguage().getId()) {
                        languages.setValue(((XincoCoreLanguageServer) language).getId());
                    }
                    i++;
                } else {
                    Logger.getLogger(Xinco.class.getName()).log(Level.WARNING,
                            "{0} not defined in com.bluecubs.xinco.messages.XincoMessagesLocale",
                            "Locale." + designation);
                }
                if (data != null && data.getXincoCoreLanguage() != null
                        && data.getXincoCoreLanguage().getSign().equals(designation)) {
                    languages.setValue("Locale." + designation);
                }
            }
            panel.addComponent(languages);
            //Status
            statusField = new com.vaadin.ui.TextField(getResource().getString("general.status") + ":");
            //Not editable
            statusField.setEnabled(false);
            String text = "";
            if (newData || data.getStatusNumber() == 1) {
                text = getResource().getString("general.status.open");
            }
            if (data != null && data.getStatusNumber() == 2) {
                text = getResource().getString("general.status.locked") + " (-)";
            }
            if (data != null && data.getStatusNumber() == 3) {
                text = getResource().getString("general.status.archived") + " (->)";
            }
            if (data != null && data.getStatusNumber() == 4) {
                text = getResource().getString("general.status.checkedout") + " (X)";
            }
            if (data != null && data.getStatusNumber() == 5) {
                text = getResource().getString("general.status.published") + " (WWW)";
            }
            statusField.setValue(text);
            panel.addComponent(statusField);
            // Set the size as undefined at all levels
            panel.getContent().setSizeUndefined();
            panel.setSizeUndefined();
            setSizeUndefined();

            // The composition root MUST be set
            setCompositionRoot(panel);
        }

        /**
         * @return the idField
         */
        public com.vaadin.ui.TextField getIdField() {
            return idField;
        }

        /**
         * @return the designationField
         */
        public com.vaadin.ui.TextField getDesignationField() {
            return designationField;
        }

        /**
         * @return the statusField
         */
        public com.vaadin.ui.TextField getStatusField() {
            return statusField;
        }

        /**
         * @return the languages
         */
        public Select getLanguages() {
            return languages;
        }
    }

    private void showDataDialog(final boolean newData) {
        final XincoWizard wizard = new XincoWizard();
        if (newData) {
            final UploadManager um = new UploadManager();
            final Upload upload = new Upload(getResource().getString("general.file.select"), um);
            final Upload.SucceededListener listener = new Upload.SucceededListener() {

                @Override
                public void uploadSucceeded(SucceededEvent event) {
                    if (upload != null) {
                        upload.setEnabled(false);
                    }
                }
            };
            final WizardStep fileStep = new WizardStep() {

                @Override
                public String getCaption() {
                    return getResource().getString("general.file.select");
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    upload.addListener((Upload.SucceededListener) um);
                    upload.addListener((Upload.SucceededListener) listener);
                    upload.addListener((Upload.FailedListener) um);
                    return upload;
                }

                @Override
                public boolean onAdvance() {
                    //TODO: Not critical; Next button is enabled without a file loaded (but gives error message)
                    if (!um.isSuccess()) {
                        getMainWindow().showNotification(
                                getResource().getString("message.missing.file"),
                                Notification.TYPE_ERROR_MESSAGE);
                    } else {
                        data.setDesignation(fileName);
                    }
                    return um.isSuccess();
                }

                @Override
                public boolean onBack() {
                    return true;
                }
            };
            final WizardStep attrStep = new WizardStep() {

                @Override
                public String getCaption() {
                    return getResource().getString("window.addattributesuniversal");
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    if (attrDialog == null) {
                        attrDialog = new AddAttributeDialog(data);
                        attrDialog.setSizeFull();
                    }
                    return attrDialog;
                }

                @Override
                public boolean onAdvance() {
                    //True if there are more steps after this one
                    return wizard.getSteps().size() > wizard.getLastCompleted() + 1;
                }

                @Override
                public boolean onBack() {
                    return true;
                }
            };
            wizard.addStep(new WizardStep() {

                @Override
                public String getCaption() {
                    return getResource().getString("window.datatype");
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    if (dataTypeDialog == null) {
                        dataTypeDialog = new DataTypeDialog();
                        dataTypeDialog.setSizeFull();
                        dataTypeDialog.getTypes().addListener(new ValueChangeListener() {

                            @Override
                            public void valueChange(ValueChangeEvent event) {
                                //File = 1
                                if (Integer.valueOf(event.getProperty().toString()) == 1) {
                                    if (!wizard.getSteps().contains(fileStep)) {
                                        //wizard.getLastCompleted() is the previous step, 
                                        //the current is wizard.getLastCompleted() + 1, 
                                        //the next step wizard.getLastCompleted() + 2
                                        wizard.addStep(fileStep, wizard.getLastCompleted() + 1);
                                    }
                                } else {
                                    wizard.removeStep(fileStep);
                                }
                            }
                        });
                    }
                    return dataTypeDialog;
                }

                @Override
                public boolean onAdvance() {
                    boolean value = true;
                    if (dataTypeDialog.getTypes().getValue() == null) {
                        getMainWindow().showNotification(
                                getResource().getString("message.missing.datatype"),
                                Notification.TYPE_ERROR_MESSAGE);
                        value = false;
                    } else {
                        data = new XincoCoreData();
                        //Process data
                        data.setXincoCoreDataType((XincoCoreDataType) XincoCoreDataTypeServer.getXincoCoreDataTypes().get(Integer.valueOf(dataTypeDialog.getTypes().getValue().toString())));
                        //Set the parent id to the current selected node
                        data.setXincoCoreNodeId(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                        addDefaultAddAttributes();
                        if ((data.getXincoCoreDataType().getId() != 1
                                || data.getXincoAddAttributes().size() > 8)
                                && (data.getXincoCoreDataType().getId() != 2
                                || data.getXincoAddAttributes().size() > 1)) {
                            wizard.addStep(attrStep, wizard.getLastCompleted() + 2);
                        }
                        if (data.getXincoCoreDataType().getId() == 1) {
                            //Is a file, show archiving dialog
                            wizard.addStep(new WizardStep() {

                                @Override
                                public String getCaption() {
                                    return getResource().getString("window.archive");
                                }

                                @Override
                                public com.vaadin.ui.Component getContent() {
                                    if (archDialog == null) {
                                        archDialog = new ArchiveDialog();
                                        archDialog.setSizeFull();
                                    }
                                    return archDialog;
                                }

                                @Override
                                public boolean onAdvance() {
                                    //True if there are more steps after this one
                                    return wizard.getSteps().size() > wizard.getLastCompleted() + 1;
                                }

                                @Override
                                public boolean onBack() {
                                    return true;
                                }
                            });
                        }
                        switch (data.getXincoCoreDataType().getId()) {
                            //Text data
                            case 2:
                                //TODO: Prompt user to enter text
                                break;
                            //TODO handle other cases
                            default:
                        }
                    }
                    return value;
                }

                @Override
                public boolean onBack() {
                    return true;
                }
            });
        } else {
            try {
                //Load from database
                data = getService().getXincoPort().getXincoCoreData(
                        new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1))),
                        loggedUser);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        wizard.addStep(new WizardStep() {

            @Override
            public String getCaption() {
                return getResource().getString("window.datadetails");
            }

            @Override
            public com.vaadin.ui.Component getContent() {
                if (dataDialog == null) {
                    dataDialog = new DataDialog(newData);
                    dataDialog.setSizeFull();
                }
                return dataDialog;
            }

            @Override
            public boolean onAdvance() {
                boolean value = true;
                if (dataDialog.getDesignationField().getValue().toString().isEmpty()) {
                    getMainWindow().showNotification(
                            getResource().getString("message.missing.designation"),
                            Notification.TYPE_ERROR_MESSAGE);
                    value = false;
                }
                if (dataDialog.getLanguages().getValue() == null) {
                    getMainWindow().showNotification(
                            getResource().getString("message.missing.language"),
                            Notification.TYPE_ERROR_MESSAGE);
                    value = false;
                } else {
                    //Process data
                    data.setDesignation(dataDialog.getDesignationField().getValue().toString());
                    data.setXincoCoreLanguage((XincoCoreLanguage) XincoCoreLanguageServer.getXincoCoreLanguages().get(Integer.valueOf(dataDialog.getLanguages().getValue().toString())));
                }
                return value;
            }

            @Override
            public boolean onBack() {
                return false;
            }
        });
        wizardWindow.removeAllComponents();
        wizardWindow.addComponent(wizard);
        //Disable closing with the 'X' in the window
        wizardWindow.setReadOnly(true);
        ddManager = new DataDialogManager(newData);
        wizard.setSizeFull();
        wizard.addListener(ddManager);
        wizardWindow.setModal(true);
        wizardWindow.setWidth(40, Sizeable.UNITS_PERCENTAGE);
        // add the wizard to a layout
        getMainWindow().addWindow(wizardWindow);
    }

    private void addDefaultAddAttributes() {
        //add specific attributes
        data.getXincoAddAttributes().clear();
        XincoAddAttribute xaa;
        for (int i = 0; i < data.getXincoCoreDataType().getXincoCoreDataTypeAttributes().size(); i++) {
            try {
                xaa = new XincoAddAttribute();
                xaa.setAttributeId(((XincoCoreDataTypeAttribute) data.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(i)).getAttributeId());
                xaa.setAttribVarchar("");
                xaa.setAttribText("");
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
                xaa.setAttribDatetime(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
                data.getXincoAddAttributes().add(xaa);
            } catch (DatatypeConfigurationException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class UploadManager extends CustomComponent
            implements Upload.SucceededListener,
            Upload.FailedListener,
            Upload.Receiver {

        private File file;                          // File to write to.
        private boolean success = false;

        // Callback method to begin receiving the upload.
        @Override
        public OutputStream receiveUpload(String filename,
                String MIMEType) {
            FileOutputStream fos; // Output stream to write to
            fileName = filename;
            try {
                //Create upload folder if needed
                File uploads = new File(XincoConfigSingletonServer.getInstance().FileRepositoryPath
                        + System.getProperty("file.separator") + "uploads"
                        + System.getProperty("file.separator"));
                uploads.mkdirs();
                uploads.deleteOnExit();
                file = File.createTempFile("xinco", ".xtf", uploads);
            } catch (IOException ex) {
                return null;
            }
            getFile().deleteOnExit();
            try {
                // Open the file for writing.
                fos = new FileOutputStream(getFile());
            } catch (final java.io.FileNotFoundException e) {
                return null;
            }
            return fos; // Return the output stream to write to
        }

        @Override
        public void uploadSucceeded(SucceededEvent event) {
            fileToLoad = file;
            success = true;
        }

        @Override
        public void uploadFailed(FailedEvent event) {
            getMainWindow().showNotification(
                    getResource().getString("datawizard.unabletoloadfile"),
                    Notification.TYPE_ERROR_MESSAGE);
            file = null;
            fileName = null;
            success = false;
        }

        /**
         * @return the file
         */
        public File getFile() {
            return file;
        }

        /**
         * @return the fileName
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * @return the success
         */
        public boolean isSuccess() {
            return success;
        }
    }

    private class DataDialogManager implements WizardProgressListener {

        private final boolean newData;

        public DataDialogManager(boolean newData) {
            this.newData = newData;
        }

        @Override
        public void activeStepChanged(WizardStepActivationEvent event) {
            // display the step caption as the window title
            getMainWindow().setCaption(event.getActivatedStep().getCaption());
        }

        @Override
        public void stepSetChanged(WizardStepSetChangedEvent event) {
            //Nothing to do
        }

        @Override
        public void wizardCompleted(WizardCompletedEvent event) {
            finishWizard();
        }

        @Override
        public void wizardCancelled(WizardCancelledEvent event) {
            try {
                if (newData) {
                    //Cancelled so roll back everything done in database
                    HashMap parameters = new HashMap();
                    parameters.put("id", data.getId());
                    if (data != null) {
                        XincoCoreDataServer.removeFromDB(loggedUser.getId(), data.getId());
                    }
                }
                //Remove the file from the repository as well
                closeWizard();
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void finishWizard() {
            if (newData) {
                try {
                    //Now load the file
                    loadFile(fileToLoad, fileName);
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    //Update data only
                    getService().getXincoPort().setXincoCoreData(data, loggedUser);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            closeWizard();
            getMainWindow().removeWindow(wizardWindow);
        }

        private void discard() {
            //Clear wizard related stuff
            dataDialog = null;
            dataTypeDialog = null;
            attrDialog = null;
            ddManager = null;
            data = null;
            fileToLoad = null;
            fileName = null;
        }

        private void closeWizard() {
            getMainWindow().removeWindow(wizardWindow);
            getMainWindow().setCaption(defaultName);
            discard();
            try {
                //Show changes in tree
                refresh();
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void showDataFolderDialog(final boolean newFolder) {
        try {
            final XincoCoreNode node = new XincoCoreNodeServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
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
                designationField.setValue(node.getDesignation());
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
                    if ((newFolder && ((XincoCoreLanguageServer) language).getSign().equals("en"))
                            || (!newFolder && node.getXincoCoreLanguage() != null
                            && node.getXincoCoreLanguage().getDesignation().equals(designation))) //Select by default
                    {
                        languages.setValue(i);
                    }
                    i++;
                } else {
                    Logger.getLogger(Xinco.class.getName()).log(Level.WARNING,
                            "{0} not defined in com.bluecubs.xinco.messages.XincoMessagesLocale",
                            "Locale." + designation);
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
            final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                    getResource().getString("general.save"), form, "commit");
            final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                    getResource().getString("general.cancel"),
                    new com.vaadin.ui.Button.ClickListener() {

                        @Override
                        public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                            getMainWindow().removeWindow(dataFolderDialog);
                        }
                    });
            final int finalStatus = status;
            commit.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        //Disable data fields, make sure nothing gets modified after clicking save
                        form.getField("lang").setEnabled(false);
                        form.getField("designation").setEnabled(false);
                        commit.setEnabled(false);
                        cancel.setEnabled(false);
                        //Process the data
                        XincoCoreNodeServer tempNode;
                        if (newFolder) {
                            tempNode = new XincoCoreNodeServer(
                                    Integer.valueOf(form.getField("id").getValue().toString()),
                                    node.getId(), //Use selected node's id as parent
                                    ((XincoCoreLanguage) XincoCoreLanguageServer.getXincoCoreLanguages().get(Integer.valueOf(languages.getValue().toString()))).getId(),
                                    form.getField("designation").getValue().toString(),
                                    finalStatus);
                        } else {
                            tempNode = new XincoCoreNodeServer(
                                    Integer.valueOf(form.getField("id").getValue().toString()));
                            //Update with changes
                            tempNode.setDesignation(form.getField("designation").getValue().toString());
                            tempNode.setXincoCoreLanguage(((XincoCoreLanguage) XincoCoreLanguageServer.getXincoCoreLanguages().get(Integer.valueOf(languages.getValue().toString()))));
                        }
                        tempNode.setChangerID(getLoggedUser().getId());
                        try {
                            //Call the web service
                            if (getService().getXincoPort().setXincoCoreNode(tempNode, loggedUser) == null) {
                                getMainWindow().showNotification(getResource().getString("error.accessdenied"),
                                        getResource().getString("error.folder.sufficientrights"),
                                        Window.Notification.TYPE_ERROR_MESSAGE);
                            }
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
            form.getFooter().setSizeUndefined();
            form.getFooter().addComponent(commit);
            form.getFooter().addComponent(cancel);
            dataFolderDialog.addComponent(form);
            dataFolderDialog.setModal(true);
            dataFolderDialog.center();
            dataFolderDialog.setWidth(25, Sizeable.UNITS_PERCENTAGE);
            getMainWindow().addWindow(dataFolderDialog);
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refresh() throws XincoException {
        XincoCoreNodeServer xcns = null;
        try {
            xcns = new XincoCoreNodeServer(1);
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Clear tree and start over
        xincoTreeContainer.removeAllItems();
        String id = "node-" + xcns.getId();
        root = xincoTreeContainer.addItem(id);
        root.getItemProperty("caption").setValue(xcns.getDesignation());
        addChildren(id);
        //Expand root node
        for (Iterator<?> it = xincoTreeContainer.rootItemIds().iterator(); it.hasNext();) {
            xincoTree.expandItemsRecursively(it.next());
        }
    }

    private void processTreeSelection(String source) {
        updateMenu();
        XincoCoreACE tempAce = new XincoCoreACE();
        getXincoTable().setVisible(getLoggedUser() != null);
        getXincoTable().requestRepaint();
        xeSplitPanel.setSplitPosition(getLoggedUser() != null ? 25 : 100, Sizeable.UNITS_PERCENTAGE);
        if (getXincoTable().isVisible()) {
            //Clear table
            xincoTable.removeAllItems();
            int i = 1;
            String value = "", header;
            if (source.startsWith("node")) {
                // only nodes have children
                // check for children only if none have been found yet
                XincoCoreNodeServer xcns = null;
                try {
                    xcns = new XincoCoreNodeServer(Integer.valueOf(source.substring(source.indexOf('-') + 1)));
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Get ACE
                try {
                    tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(getLoggedUser().getId()),
                            (ArrayList) (xcns).getXincoCoreAcl());
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!xincoTree.hasChildren(xincoTree.getValue()) && tempAce.isReadPermission()) {
                    try {
                        //Populate children
                        xcns.fillXincoCoreData();
                        xcns.fillXincoCoreNodes();
                        addChildren("node-" + xcns.getId());
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (!tempAce.isReadPermission()) {
                    getMainWindow().showNotification(getResource().getString("error.accessdenied"),
                            getResource().getString("error.folder.sufficientrights"),
                            Window.Notification.TYPE_WARNING_MESSAGE);
                }
                // update details table
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
                xincoTable.addItem(new Object[]{getResource().getString("general.id"),
                            xcns.getId()}, i++);
                xincoTable.addItem(new Object[]{getResource().getString("general.designation"),
                            xcns.getDesignation()}, i++);
                xincoTable.addItem(new Object[]{getResource().getString("general.language"),
                            getResource().getString(xcns.getXincoCoreLanguage().getDesignation()) + " ("
                            + xcns.getXincoCoreLanguage().getSign()
                            + ")"}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
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
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
                value = "";
                if (xcns.getStatusNumber() == 1) {
                    value = getResource().getString("general.status.open") + "";
                }
                if (xcns.getStatusNumber() == 2) {
                    value = getResource().getString("general.status.locked") + " (-)";
                }
                if (xcns.getStatusNumber() == 3) {
                    value = getResource().getString("general.status.archived") + " (->)";
                }
                xincoTable.addItem(new Object[]{getResource().getString("general.status"), value}, i++);
            } else if (source.startsWith("data")) {
                // get ace
                XincoCoreDataServer temp = null;
                try {
                    temp = new XincoCoreDataServer(Integer.valueOf(source.substring(source.indexOf('-') + 1)));
                    tempAce = XincoCoreACEServer.checkAccess(new XincoCoreUserServer(1),
                            (ArrayList) (temp).getXincoCoreAcl());
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.id"), temp.getId()}, i++);
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.designation"), temp.getDesignation()}, i++);
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.language"),
                            getResource().getString(temp.getXincoCoreLanguage().getDesignation())
                            + " ("
                            + temp.getXincoCoreLanguage().getSign()
                            + ")"}, i++);
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.datatype"), getResource().getString(temp.getXincoCoreDataType().getDesignation())
                            + " ("
                            + getResource().getString(temp.getXincoCoreDataType().getDescription())
                            + ")"}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
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
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
                switch (temp.getStatusNumber()) {
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
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.typespecificattributes"), ""}, i++);
                // get add attributes of Core Data, if access granted
                java.util.List<XincoAddAttribute> attributes =
                        temp.getXincoAddAttributes();
                java.util.List<XincoCoreDataTypeAttribute> dataTypeAttributes =
                        temp.getXincoCoreDataType().getXincoCoreDataTypeAttributes();
                if (!attributes.isEmpty()) {
                    for (int j = 0; j < attributes.size(); j++) {
                        header = getResource().containsKey(dataTypeAttributes.get(j).getDesignation())
                                ? getResource().getString(dataTypeAttributes.get(j).getDesignation())
                                : dataTypeAttributes.get(j).getDesignation();
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
                            Date time = ((XincoAddAttribute) temp.getXincoAddAttributes().get(j)).getAttribDatetime().toGregorianCalendar().getTime();
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
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", "\u00a0"}, i++);
                header = getResource().getString("general.logslastfirst");
                value = "";
                xincoTable.addItem(new Object[]{header, value}, i++);
                Calendar cal;
                XMLGregorianCalendar realcal;
                Calendar ngc = new GregorianCalendar();
                for (int j = temp.getXincoCoreLogs().size() - 1; j >= 0; j--) {
                    if (((XincoCoreLog) temp.getXincoCoreLogs().get(j)).getOpDatetime() == null) {
                        header = "???";
                    } else {
                        try {
                            // convert clone from remote time to local time
                            cal = ((XMLGregorianCalendar) ((XincoCoreLog) temp.getXincoCoreLogs().get(j)).getOpDatetime().clone()).toGregorianCalendar();
                            realcal = (((XincoCoreLog) temp.getXincoCoreLogs().get(j)).getOpDatetime());
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
                            + ((XincoCoreLog) temp.getXincoCoreLogs().get(j)).getOpCode()
                            + ") "
                            + ((XincoCoreLog) temp.getXincoCoreLogs().get(j)).getOpDescription();
                    xincoTable.addItem(new Object[]{header, value}, i++);
                    header = "";
                    try {
                        value = getResource().getString("general.version")
                                + " "
                                + ((XincoCoreLog) temp.getXincoCoreLogs().get(j)).getVersion().getVersionHigh()
                                + "."
                                + ((XincoCoreLog) temp.getXincoCoreLogs().get(j)).getVersion().getVersionMid()
                                + "."
                                + ((XincoCoreLog) temp.getXincoCoreLogs().get(j)).getVersion().getVersionLow()
                                + ""
                                + ((XincoCoreLog) temp.getXincoCoreLogs().get(j)).getVersion().getVersionPostfix();
                        xincoTable.addItem(new Object[]{header, value}, i++);
                    } catch (Exception ex) {
                        Logger.getLogger(Xinco.class.getSimpleName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            xincoTable.setPageLength(i - 1);
            xincoTable.requestRepaintAll();
        }
        getMainWindow().requestRepaintAll();
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
