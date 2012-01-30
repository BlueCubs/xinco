package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.core.server.persistence.controller.*;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.service.*;
import com.bluecubs.xinco.core.server.vaadin.custom.VersionSelector;
import com.bluecubs.xinco.core.server.vaadin.setting.SettingAdminWindow;
import com.bluecubs.xinco.core.server.vaadin.wizard.WizardStep;
import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;
import com.bluecubs.xinco.core.server.vaadin.wizard.event.*;
import com.bluecubs.xinco.index.XincoIndexer;
import com.bluecubs.xinco.tools.Tool;
import com.bluecubs.xinco.tools.XincoFileIconManager;
import com.vaadin.Application;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.Or;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.terminal.*;
import com.vaadin.terminal.gwt.client.ui.dd.VerticalDropLocation;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.AbstractSelect.VerticalLocationIs;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.Tree.TreeTargetDetails;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.Window.ResizeEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.Map.Entry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import javax.imageio.ImageIO;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EntityType;
import javax.swing.Icon;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.vaadin.easyuploads.MultiFileUpload;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class Xinco extends Application {

    //client version
    private XincoVersion xincoClientVersion = null;
    private ResourceBundle xerb =
            ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", Locale.getDefault());
    private XincoCoreUserServer loggedUser = null;
    private XincoFileIconManager xfm = new XincoFileIconManager();
    //Table linking displayed item with it's id
    private Tree xincoTree = null;
    private Table xincoTable = null;
    private com.vaadin.ui.MenuBar menuBar = new com.vaadin.ui.MenuBar();
    private Item root;
    private XincoWebService service;
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
    private boolean renderingSupportEnabled = false;
    private String version;
    private HorizontalSplitPanel xeSplitPanel;
    private com.vaadin.ui.Button login;
    private com.vaadin.ui.Button logout;
    private com.vaadin.ui.Button profile;
    private WizardStep fileStep;
    private ThemeResource smallIcon = new ThemeResource("img/blueCubsIcon16x16.GIF");
    private HierarchicalContainer xincoTreeContainer;
    private com.vaadin.ui.Panel adminPanel;
    private Embedded icon;

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void init() {
        try {
            XincoDBManager.reload();
            XincoDBManager.getEntityManagerFactory();
            XincoDBManager.updateDBState();
            //Switch to Xinco theme
            setTheme("xinco");
            setMainWindow(new Window("Xinco"));
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
            xincoClientVersion = new XincoVersion();
            try {
                xincoClientVersion.setVersionHigh(XincoSettingServer.getSetting(
                        new XincoCoreUserServer(1), "version.high").getIntValue());
                xincoClientVersion.setVersionMid(XincoSettingServer.getSetting(
                        new XincoCoreUserServer(1), "version.mid").getIntValue());
                xincoClientVersion.setVersionLow(XincoSettingServer.getSetting(
                        new XincoCoreUserServer(1), "version.low").getIntValue());
                xincoClientVersion.setVersionPostfix(XincoSettingServer.getSetting(
                        new XincoCoreUserServer(1), "version.postfix").getStringValue());
            } catch (com.bluecubs.xinco.core.XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
            version = XincoSettingServer.getSetting(new XincoCoreUserServer(1),
                    "version.high").getIntValue() + "."
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1),
                    "version.mid").getIntValue() + "."
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1),
                    "version.low").getIntValue() + " "
                    + XincoSettingServer.getSetting(new XincoCoreUserServer(1),
                    "version.postfix").getStringValue();
            icon = new Embedded("Xinco - "
                    + getResource().getString("general.version") + " "
                    + version, new ThemeResource("img/blueCubsSmall.gif"));
            icon.setType(Embedded.TYPE_IMAGE);
            if (XincoDBManager.config.isGuessLanguage()) {
                // Use the locale from the request as default.
                // Login uses this setting later.
                setLocale(((WebApplicationContext) getContext()).getBrowser().getLocale());
                showMainWindow();
            } else {
                showLanguageSelection();
            }
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", getLocale());
        login = new com.vaadin.ui.Button(
                getResource().getString("general.login"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        showLoginDialog();
                    }
                });
        logout = new com.vaadin.ui.Button(
                getResource().getString("general.logout"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        loggedUser = null;
                        try {
                            updateMenu();
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
        profile = new com.vaadin.ui.Button(
                getResource().getString("message.admin.userProfile"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        showUserAdminWindow(false);
                    }
                });
        fileStep = new WizardStep() {

            final UploadManager um = new UploadManager(com.bluecubs.xinco.core.server.vaadin.Xinco.this);
            final Upload upload = new Upload(getResource().getString("general.file.select"), um);
            final Upload.SucceededListener listener = new Upload.SucceededListener() {

                @Override
                public void uploadSucceeded(SucceededEvent event) {
                    if (upload != null) {
                        getMainWindow().showNotification(
                                getResource().getString("datawizard.fileuploadsuccess"),
                                Notification.TYPE_HUMANIZED_MESSAGE);
                    }
                }
            };

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
                if (!um.isSuccess()) {
                    getMainWindow().showNotification(
                            getResource().getString("message.missing.file"),
                            Notification.TYPE_ERROR_MESSAGE);
                } else {
                    getXincoCoreData().setDesignation(getFileName());
                }
                return um.isSuccess();
            }

            @Override
            public boolean onBack() {
                return true;
            }

            @Override
            public String toString() {
                return getCaption();
            }
        };
        initMenuItems();
    }

    private void addHeader() {
        final Select languages = getLanguageOptions();
        languages.setImmediate(true);
        languages.addListener(new ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                Locale loc;
                try {
                    String list = languages.getValue().toString();
                    String[] locales;
                    locales = list.split("_");
                    switch (locales.length) {
                        case 1:
                            loc = new Locale(locales[0]);
                            break;
                        case 2:
                            loc = new Locale(locales[0], locales[1]);
                            break;
                        case 3:
                            loc = new Locale(locales[0], locales[1], locales[2]);
                            break;
                        default:
                            loc = Locale.getDefault();
                    }
                } catch (Exception e) {
                    loc = Locale.getDefault();
                }
                setLocale(loc);
                refresh();
            }
        });
        languages.setCaption(getResource().getString("general.language") + ":");
        getMainWindow().addComponent(languages);
        getMainWindow().addComponent(new SimplifiedSearchComponent(this));
    }

    private void showMainWindow() {
        getMainWindow().removeAllComponents();
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        // Put two components in the container.
        splitPanel.setFirstComponent(getSideMenu());
        splitPanel.setSecondComponent(getXincoExplorer());
        splitPanel.setHeight(700, Sizeable.UNITS_PIXELS);
        splitPanel.setSplitPosition(20, Sizeable.UNITS_PERCENTAGE);
        addHeader();
        getMainWindow().addComponent(splitPanel);
    }

    private FileResource getFlagIcon(String code) throws IOException {
        if (code == null || code.isEmpty()) {
            return null;
        }
        //Format if it has '_' underscore
        if (code.contains("_")) {
            String newCode = code.substring(code.lastIndexOf('_') + 1);
            Logger.getLogger(Xinco.class.getSimpleName()).log(Level.FINE,
                    "Converting code from: {0} to {1}!", new Object[]{code, newCode});
            code = newCode;
        }
        Logger.getLogger(Xinco.class.getSimpleName()).log(Level.FINE,
                "Requested icon for code: {0}", code);
        WebApplicationContext context = (WebApplicationContext) getContext();
        File iconsFolder = new File(context.getHttpSession().getServletContext().getRealPath(
                "/VAADIN/themes/xinco") + System.getProperty("file.separator") + "icons"
                + System.getProperty("file.separator") + "flags");
        File tempIcon = new File(iconsFolder.getAbsolutePath()
                + System.getProperty("file.separator") + code.toLowerCase() + ".png");
        FileResource resource = null;
        if (tempIcon.exists()) {
            Logger.getLogger(Xinco.class.getSimpleName()).log(Level.FINE,
                    "Found icon for code: {0}!", code);
            resource = new FileResource(tempIcon, Xinco.this);
        } else {
            Logger.getLogger(Xinco.class.getSimpleName()).log(Level.FINE,
                    "Unable to find icon for: {0}", code);
        }
        return resource;
    }

    protected FileResource getIcon(String extension) throws IOException {
        WebApplicationContext context = (WebApplicationContext) getContext();
        File iconsFolder = new File(context.getHttpSession().getServletContext().getRealPath(
                "/VAADIN/themes/xinco") + System.getProperty("file.separator") + "icons");
        if (!iconsFolder.exists()) {
            //Create it
            iconsFolder.mkdirs();
        }
        File tempIconFile = new File(iconsFolder.getAbsolutePath()
                + System.getProperty("file.separator") + extension + ".png");
        if (!tempIconFile.exists()) {
            Icon tempIcon = xfm.getIcon(extension);
            if (tempIcon != null) {
                Image image = iconToImage(tempIcon);
                BufferedImage buffered = new BufferedImage(
                        image.getWidth(null),
                        image.getHeight(null),
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = buffered.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                ImageIO.write(buffered, "PNG", tempIconFile);
            } else {
                return null;
            }
        }
        FileResource resource =
                new FileResource(tempIconFile, Xinco.this);
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
        getXincoTree().setSizeFull();
        getXincoTable().setSizeFull();
        xeSplitPanel.setFirstComponent(getXincoTree());
        xeSplitPanel.setSecondComponent(getXincoTable());
        xeSplitPanel.setHeight(700, Sizeable.UNITS_PIXELS);
        xeSplitPanel.setSplitPosition(100, Sizeable.UNITS_PERCENTAGE);
        //Hide details by default, user needs to log in for some features.
        getXincoTable().setVisible(false);
        panel.addComponent(menuBar);
        menuBar.setSizeFull();
        panel.addComponent(xeSplitPanel);
        try {
            updateMenu();
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
        return panel;
    }

    public Tree getXincoTree() {
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
                        //Populate the children if you have access
                        processTreeSelection(event.getItemId().toString());
                        //Now go with the expansion
                        xincoTree.expandItem(event.getItemId().toString());
                    }
                });
                xincoTree.addListener(new ItemClickListener() {

                    @Override
                    public void itemClick(ItemClickEvent event) {
                        if (event.getButton() == ItemClickEvent.BUTTON_LEFT
                                && event.isDoubleClick()
                                && xincoTree.getValue() != null
                                && xincoTree.getValue().toString().startsWith("data")) {
                            try {
                                downloadFile();
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (XincoException ex) {
                                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (xincoTree.getValue() != null) {
                            processTreeSelection(xincoTree.getValue().toString());
                        }
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
                        if (loggedUser == null) {
                            return;
                        }

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

                        XincoCoreNodeServer targetN =
                                new XincoCoreNodeServer(Integer.valueOf(
                                targetItemId.toString().substring(
                                targetItemId.toString().indexOf('-') + 1)));

                        // On which side of the target the item was dropped 
                        VerticalDropLocation location = target.getDropLocation();

                        // Drop right on an item -> make it a child
                        if (XincoCoreACEServer.checkAccess(loggedUser,
                                (ArrayList) (targetN).getXincoCoreAcl()).isWritePermission()
                                && location == VerticalDropLocation.MIDDLE) {
                            try {
                                //Now update things in the database
                                if (sourceItemId.toString().startsWith("data")
                                        && targetItemId.toString().startsWith("node")) {
                                    XincoCoreDataServer source =
                                            new XincoCoreDataServer(Integer.valueOf(sourceItemId.toString().substring(sourceItemId.toString().indexOf('-') + 1)));
                                    // Get a reason for the move:
                                    source.setXincoCoreNodeId(targetN.getId());
                                    showCommentDataDialog(source, OPCode.DATA_MOVE);
                                }
                                if (sourceItemId.toString().startsWith("node")
                                        && targetItemId.toString().startsWith("node")) {
                                    XincoCoreNodeServer source =
                                            new XincoCoreNodeServer(Integer.valueOf(sourceItemId.toString().substring(sourceItemId.toString().indexOf('-') + 1)));
                                    source.setXincoCoreNodeId(targetN.getId());
                                    source.write2DB();
                                }
                                xincoTree.setParent(sourceItemId, targetItemId);
                                try {
                                    refresh();
                                } catch (XincoException ex) {
                                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (XincoException ex) {
                                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    @Override
                    public AcceptCriterion getAcceptCriterion() {
                        //Accepts drops only on nodes that allow children, but between all nodes
                        return new Or(Tree.TargetItemAllowsChildren.get(),
                                new Not(VerticalLocationIs.MIDDLE));
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
                    com.vaadin.ui.Component.class, null);
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
        XincoCoreNodeServer xcns = new XincoCoreNodeServer(Integer.valueOf(
                parent.substring(parent.indexOf('-') + 1)));
        for (Iterator<XincoCoreData> it = xcns.getXincoCoreData().iterator(); it.hasNext();) {
            XincoCoreData temp = it.next();
            //Only show files that are not renderings
            if (!XincoCoreDataHasDependencyServer.isRendering(temp.getId())) {
                //Add childen data
                String id = "data-" + temp.getId();
                Item item = xincoTreeContainer.addItem(id);
                item.getItemProperty("caption").setValue(temp.getDesignation());
                // Set it to be a child.
                xincoTreeContainer.setParent(id, parent);
                // Set as leaves
                xincoTreeContainer.setChildrenAllowed(id, false);
                if (!temp.getXincoAddAttributes().isEmpty()) {
                    String name = temp.getXincoAddAttributes().get(0).getAttribVarchar();
                    try {
                        FileResource icon1 = null;
                        //Set Icon
                        switch (temp.getXincoCoreDataType().getId()) {
                            case 1://File
                            //Fall through
                            case 5://Rendering
                                if (name != null
                                        && name.contains(".")
                                        && name.substring(name.lastIndexOf('.') + 1,
                                        name.length()).length() >= 3) {
                                    icon1 = getIcon(name.substring(name.lastIndexOf('.') + 1,
                                            name.length()));
                                }
                                break;
                            case 2://Text
                                icon1 = getIcon("txt");

                                break;
                            case 3://URL
                                icon1 = getIcon("html");
                                break;
                            case 4://Contact
                                item.getItemProperty("icon").setValue(new ThemeResource("icons/contact.gif"));
                                break;
                        }
                        if (icon1 != null) {
                            item.getItemProperty("icon").setValue(icon1);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    protected void updateMenu() throws XincoException {
        XincoMenuItemManager.updateMenuBar(this, menuBar);
        //Hide it if empty
        menuBar.setVisible(!menuBar.getItems().isEmpty());
        //Update the side menu
        updateSideMenu();
    }

    private void checkinFile() {
        final XincoCoreLog newlog = new XincoCoreLog();
        try {
            final Form form = new Form();
            final XincoWizard wizard = new XincoWizard(getLocale());
            final UploadManager um = new UploadManager(this);
            final Upload upload = new Upload(getResource().getString("general.file.select"), um);
            XincoCoreDataServer temp = new XincoCoreDataServer(
                    Integer.valueOf(xincoTree.getValue().toString().substring(
                    xincoTree.getValue().toString().indexOf('-') + 1)));
            //Check in file
            newlog.setOpCode(OPCode.CHECKIN.ordinal() + 1);
            newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()));
            newlog.setXincoCoreUserId(loggedUser.getId());
            newlog.setXincoCoreDataId(temp.getId());
            newlog.setVersion(((XincoCoreLog) temp.getXincoCoreLogs().get(
                    temp.getXincoCoreLogs().size() - 1)).getVersion());
            newlog.setOpDescription(newlog.getOpDescription() + " ("
                    + xerb.getString("general.user") + ": " + loggedUser.getUsername() + ")");
            //save log to server
            XincoCoreLog tempLog = getService().setXincoCoreLog(newlog, loggedUser);
            if (tempLog != null) {
                temp.getXincoCoreLogs().add(tempLog);
            }
            final int log_index = temp.getXincoCoreLogs().size() - 1;
            final VersionSelector versionSelector = new VersionSelector(getResource().getString("general.version"),
                    temp.getXincoCoreLogs().get(log_index).getVersion());
            versionSelector.increaseHigh();
            final Upload.SucceededListener listener = new Upload.SucceededListener() {

                @Override
                public void uploadSucceeded(SucceededEvent event) {
                    if (upload != null) {
                        upload.setEnabled(false);
                        getMainWindow().showNotification(
                                getResource().getString("datawizard.fileuploadsuccess"),
                                Notification.TYPE_HUMANIZED_MESSAGE);
                    }
                }
            };
            wizard.addStep(new WizardStep() {

                @Override
                public String getCaption() {
                    return getResource().getString("window.loggingdetails");
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    try {
                        XincoCoreDataServer temp = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                        buildLogDialog(temp, form, versionSelector);
                        return form;
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                    }
                }

                @Override
                public boolean onAdvance() {
                    if (form.getField("reason").isEnabled()
                            && form.getField("reason").getValue().toString().isEmpty()) {
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public boolean onBack() {
                    return false;
                }
            });
            wizard.addStep(new WizardStep() {

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
                    if (!um.isSuccess()) {
                        getMainWindow().showNotification(
                                getResource().getString("message.missing.file"),
                                Notification.TYPE_ERROR_MESSAGE);
                    }
                    return um.isSuccess();
                }

                @Override
                public boolean onBack() {
                    return true;
                }
            });

            wizardWindow.removeAllComponents();
            wizardWindow.addComponent(wizard);
            //Disable closing with the 'X' in the window
            wizardWindow.setReadOnly(true);
            wizard.setSizeFull();
            wizard.addListener(new WizardProgressListener() {

                @Override
                public void activeStepChanged(WizardStepActivationEvent event) {
                    //Nothing to do
                }

                @Override
                public void stepSetChanged(WizardStepSetChangedEvent event) {
                    //Nothing to do
                }

                @Override
                public void wizardCompleted(WizardCompletedEvent event) {
                    try {
                        XincoCoreDataServer temp = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                        //Check in file
                        if (getService().doXincoCoreDataCheckin(temp, loggedUser) == null) {
                            getMainWindow().showNotification(
                                    getResource().getString("datawizard.unabletoloadfile"),
                                    Notification.TYPE_ERROR_MESSAGE);
                        }
                        try {
                            data = new XincoCoreDataServer(temp.getId());
                            //Now load the file
                            loadFile(getFileToLoad(), getFileName());
                            //Update log version
                            XincoCoreLogServer log = new XincoCoreLogServer(getXincoCoreData().getXincoCoreLogs().get(getXincoCoreData().getXincoCoreLogs().size() - 1).getId());
                            log.setVersion(versionSelector.getVersion());
                            log.setChangerID(1);
                            log.write2DB();
                            refresh();
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    getMainWindow().removeWindow(wizardWindow);
                }

                @Override
                public void wizardCancelled(WizardCancelledEvent event) {
                    getMainWindow().removeWindow(wizardWindow);
                }
            });
            wizardWindow.setModal(true);
            wizardWindow.setWidth(40, Sizeable.UNITS_PERCENTAGE);
            // add the wizard to a layout
            getMainWindow().addWindow(wizardWindow);
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            if (newlog != null) {
                try {
                    XincoCoreLogServer log = new XincoCoreLogServer(newlog.getId());
                    if (log != null) {
                        new XincoCoreLogJpaController(XincoDBManager.getEntityManagerFactory()).destroy(log.getId());
                    }
                } catch (NonexistentEntityException ex1) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex1);
                } catch (XincoException ex1) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

    private void buildLogDialog(XincoCoreDataServer temp, Form form, VersionSelector versionSelector) throws XincoException {
        form.addField("action", new com.vaadin.ui.TextField(
                getResource().getString("window.loggingdetails.action")
                + ":"));
        final int log_index = temp.getXincoCoreLogs().size() - 1;
        form.getField("action").setValue(temp.getXincoCoreLogs().get(log_index).getOpDescription());
        form.getField("action").setEnabled(false);
        form.addField("reason", new com.vaadin.ui.TextArea());
        versionSelector.setMinorEnabled(false);
        versionSelector.setCaption(getResource().getString("general.version"));
        versionSelector.setVersion(temp.getXincoCoreLogs().get(log_index).getVersion());
        OPCode code = OPCode.getOPCode(temp.getXincoCoreLogs().get(log_index).getOpCode());
        switch (code) {
            case COMMENT:
            case CHECKIN:
                versionSelector.setMinorEnabled(code == OPCode.CHECKIN);
                versionSelector.setVersionEnabled(false);
            case MODIFICATION:
            case LOCK_COMMENT:
            case PUBLISH_COMMENT:
            case CHECKOUT_UNDONE:
            case DATA_MOVE:
                form.getField("reason").setEnabled(true);
                break;
            default:
                form.getField("reason").setEnabled(false);
        }
        versionSelector.setEnabled(code != OPCode.DATA_MOVE);
        form.getField("reason").setRequired(form.getField("reason").isEnabled());
        form.getField("reason").setRequiredError(getResource().getString("message.warning.reason"));
        form.getLayout().addComponent(versionSelector);
    }

    private void undoCheckoutFile() {
        if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("data")) {
            try {
                XincoCoreDataServer temp = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                //Undo check out
                XincoCoreLog newlog = new XincoCoreLog();
                newlog.setOpCode(OPCode.CHECKOUT_UNDONE.ordinal() + 1);
                newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()));
                newlog.setXincoCoreUserId(loggedUser.getId());
                newlog.setXincoCoreDataId(temp.getId());
                newlog.setVersion(((XincoCoreLog) temp.getXincoCoreLogs().get(temp.getXincoCoreLogs().size() - 1)).getVersion());
                newlog.setOpDescription(newlog.getOpDescription() + " (" + xerb.getString("general.user") + ": " + loggedUser.getUsername() + ")");
                //save log to server
                newlog = getService().setXincoCoreLog(newlog, loggedUser);
                if (newlog != null) {
                    temp.getXincoCoreLogs().add(newlog);
                }
                getService().undoXincoCoreDataCheckout(temp, loggedUser);
                refresh();
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void checkoutFile() {
        try {
            if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("data")) {
                XincoCoreDataServer temp = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                //Set as checked out
                try {
                    XincoCoreLog newlog = new XincoCoreLog();
                    newlog.setOpCode(OPCode.CHECKOUT.ordinal() + 1);
                    newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()));
                    newlog.setXincoCoreUserId(loggedUser.getId());
                    newlog.setXincoCoreDataId(temp.getId());
                    newlog.setVersion(((XincoCoreLog) temp.getXincoCoreLogs().get(temp.getXincoCoreLogs().size() - 1)).getVersion());
                    newlog.setOpDescription(newlog.getOpDescription() + " (" + xerb.getString("general.user") + ": " + loggedUser.getUsername() + ")");
                    //save log to server
                    newlog = getService().setXincoCoreLog(newlog, loggedUser);
                    if (newlog != null) {
                        temp.getXincoCoreLogs().add(newlog);
                    }
                    if (getService().doXincoCoreDataCheckout(temp, loggedUser) != null) {
                        //Download the file
                        downloadFile(false);
                        xincoTree.setValue("node-1");
                        refresh();
                    } else {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE,
                                "Unable to check out file: {0} with user: {1}",
                                new Object[]{temp.getDesignation(), loggedUser});
                        getMainWindow().showNotification(
                                getResource().getString("general.error"),
                                Notification.TYPE_WARNING_MESSAGE);
                    }
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void downloadFile(final XincoCoreData data) throws XincoException, MalformedURLException {
        boolean temporaryAccess = false;
        if (loggedUser == null) {
            loggedUser = new XincoCoreUserServer(1);
            temporaryAccess = true;
        }
        StreamSource ss = new StreamSource() {

            byte[] bytes = getService().downloadXincoCoreData(
                    data, loggedUser);
            InputStream is = new ByteArrayInputStream(bytes);

            @Override
            public InputStream getStream() {
                return is;
            }
        };
        StreamResource sr = new StreamResource(ss, data.getXincoAddAttributes().get(0).getAttribVarchar(), this);
        getMainWindow().open(sr, "_blank");
        if (temporaryAccess) {
            loggedUser = null;
        }
    }

    private void downloadFile() throws XincoException, MalformedURLException {
        downloadFile(true);
    }

    private void downloadFile(boolean useRendering) throws XincoException, MalformedURLException {
        XincoCoreDataServer temp = new XincoCoreDataServer(Integer.valueOf(
                xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
        //Check for available renderings
        java.util.List<com.bluecubs.xinco.core.server.persistence.XincoCoreData> renderings =
                XincoCoreDataHasDependencyServer.getRenderings(temp.getId());
        if (useRendering && !renderings.isEmpty()) {
            for (Iterator<com.bluecubs.xinco.core.server.persistence.XincoCoreData> it = renderings.iterator(); it.hasNext();) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreData rendering = it.next();
                if (!rendering.getXincoAddAttributeList().isEmpty()
                        && rendering.getXincoAddAttributeList().get(0).getAttribVarchar().endsWith(".pdf")) {
                    //Download the PDF rendering instead
                    temp = new XincoCoreDataServer(rendering.getId());
                }
            }
        }
        downloadFile(temp);
    }

    private void loadFile(File file, String fileName) {
        try {
            String path_to_file = file.getAbsolutePath();
            File temp = null;
            if (!file.getName().equals(fileName)) {
                //Different files, probably stored as a temp file. Need to rename it
                //Create a temp file to hide the transaction
                temp = new File(file.getParentFile().getAbsolutePath()
                        + System.getProperty("file.separator") + UUID.randomUUID().toString());
                temp.mkdirs();
                path_to_file = temp.getAbsolutePath() + System.getProperty("file.separator") + fileName;
                if (!file.renameTo(new File(path_to_file))) {
                    throw new RuntimeException("Unable to rename file from "
                            + file.getAbsolutePath() + " to " + path_to_file);
                }
                file.deleteOnExit();
                temp.deleteOnExit();
            }
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
                ((XincoAddAttribute) getXincoCoreData().getXincoAddAttributes().get(0)).setAttribVarchar(fileName);
                getXincoCoreData().setDesignation(fileName);
                ((XincoAddAttribute) getXincoCoreData().getXincoAddAttributes().get(1)).setAttribUnsignedint(totalLen);
                ((XincoAddAttribute) getXincoCoreData().getXincoAddAttributes().get(2)).setAttribVarchar(""
                        + cin.getChecksum().getValue());
                ((XincoAddAttribute) getXincoCoreData().getXincoAddAttributes().get(3)).setAttribUnsignedint(1);
                ((XincoAddAttribute) getXincoCoreData().getXincoAddAttributes().get(4)).setAttribUnsignedint(0);
            } catch (Exception fe) {
                Logger.getLogger(Xinco.class.getSimpleName()).log(Level.SEVERE, null, fe);
                getMainWindow().showNotification(
                        xerb.getString("datawizard.unabletoloadfile"),
                        Notification.TYPE_ERROR_MESSAGE);
            }
            // save data to server
            data = getService().setXincoCoreData(getXincoCoreData(), loggedUser);
            if (getXincoCoreData() == null) {
                getMainWindow().showNotification(
                        xerb.getString("datawizard.unabletosavedatatoserver"),
                        Notification.TYPE_ERROR_MESSAGE);
            }
            // upload file
            if (getService().uploadXincoCoreData(getXincoCoreData(), byteArray, loggedUser) != totalLen) {
                cin.close();
                removeDirectory(temp);
                getMainWindow().showNotification(
                        xerb.getString("datawizard.fileuploadfailed"),
                        Notification.TYPE_ERROR_MESSAGE);
            }
            cin.close();
            removeDirectory(temp);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            getMainWindow().showNotification(
                    xerb.getString("datawizard.fileuploadfailed"),
                    Notification.TYPE_ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            getMainWindow().showNotification(
                    xerb.getString("datawizard.fileuploadfailed"),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

    public static boolean removeDirectory(File directory) {

        if (directory == null) {
            return false;
        }
        if (!directory.exists()) {
            return true;
        }
        if (!directory.isDirectory()) {
            return false;
        }

        String[] list = directory.list();

        // Some JVMs return null for File.list() when the
        // directory is empty.
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File entry = new File(directory, list[i]);

                //        System.out.println("\tremoving entry " + entry);

                if (entry.isDirectory()) {
                    if (!removeDirectory(entry)) {
                        return false;
                    }
                } else {
                    if (!entry.delete()) {
                        return false;
                    }
                }
            }
        }
        return directory.delete();
    }

    public void setLock() {
        //Don't do anything if no one is logged in.
        if (loggedUser != null) {
            //Give user a chance to login
            showLoginDialog();
        }
        resetTimer();
    }

    /**
     * Reset activity timer
     */
    public void resetTimer() {
        if (xat != null) {
            xat.getActivityTimer().restart();
        }
    }

    public void windowResized(ResizeEvent e) {
        for (Window w : getMainWindow().getChildWindows()) {
            //Center sub window in new screen size
            w.center();
        }
    }
//TODO: Rendering support

    private void showRenderingDialog() throws XincoException {
        if (renderingSupportEnabled) {
            final Window renderWindow = new Window();
            final Form form = new Form();
            form.setCaption(getResource().getString("general.data.type.rendering"));
            final ArrayList<com.bluecubs.xinco.core.server.persistence.XincoCoreData> renderings =
                    (ArrayList<com.bluecubs.xinco.core.server.persistence.XincoCoreData>) XincoCoreDataHasDependencyServer.getRenderings(
                    Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
            final Table table = new Table();
            table.addStyleName("striped");
            table.addContainerProperty(
                    getResource().getString("general.name"),
                    com.vaadin.ui.Label.class, null);
            table.addContainerProperty(
                    getResource().getString("general.version"),
                    com.vaadin.ui.Label.class, null);
            table.addContainerProperty(
                    getResource().getString("general.extension"),
                    com.vaadin.ui.Label.class, null);
            form.getLayout().addComponent(table);
            for (com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd : renderings) {
                XincoCoreDataServer xcds = new XincoCoreDataServer(xcd.getId());
                String name = xcds.getXincoAddAttributes().get(0).getAttribVarchar();
                XincoVersion xVersion = XincoCoreDataServer.getCurrentVersion(xcds.getId());
                table.addItem(new Object[]{new com.vaadin.ui.Label(xcds.getDesignation()),
                            new com.vaadin.ui.Label(xVersion.getVersionHigh() + "." + xVersion.getVersionMid()
                            + "." + xVersion.getVersionLow() + " " + xVersion.getVersionPostfix()),
                            new com.vaadin.ui.Label(name.substring(name.lastIndexOf(".") + 1, name.length()))},
                        xcds.getId());
            }
            form.setFooter(new HorizontalLayout());
            //Used for validation purposes
            final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                    getResource().getString("general.add"), form, "commit");
            final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                    getResource().getString("general.cancel"),
                    new com.vaadin.ui.Button.ClickListener() {

                        @Override
                        public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                            getMainWindow().removeWindow(renderWindow);
                        }
                    });
            commit.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        final XincoCoreDataServer parent = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                        //initialize the data object
                        data = new XincoCoreData();
                        getXincoCoreData().setXincoCoreDataType(new XincoCoreDataTypeServer(parent.getXincoCoreDataType().getId()));
                        getXincoCoreData().setStatusNumber(parent.getStatusNumber());
                        getXincoCoreData().setXincoCoreLanguage(parent.getXincoCoreLanguage());
                        getXincoCoreData().setXincoCoreNodeId(parent.getXincoCoreNodeId());
                        Tool.addDefaultAddAttributes(getXincoCoreData());
                        commit.setEnabled(false);
                        cancel.setEnabled(false);
                        XincoWizard wizard = new XincoWizard(getLocale());
                        wizard.addStep(fileStep);
                        getMainWindow().removeWindow(renderWindow);
                        final Window addRenderingWindow = new Window();
                        addRenderingWindow.addComponent(wizard);
                        //Add the DataDialog manager to handle the adding data part
                        ddManager = new DataDialogManager(true);
                        wizard.setSizeFull();
                        wizard.addListener(ddManager);
                        //Add a custom listener to act when the wizard is done
                        wizard.addListener(new WizardProgressListener() {

                            @Override
                            public void activeStepChanged(WizardStepActivationEvent event) {
                                //Do nothing
                            }

                            @Override
                            public void stepSetChanged(WizardStepSetChangedEvent event) {
                                //Do nothing
                            }

                            @Override
                            public void wizardCompleted(WizardCompletedEvent event) {
                                try {
                                    //The data is in the data variable, now link to this data as rendering
                                    XincoCoreDataJpaController controller = new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory());
                                    XincoCoreDataHasDependencyServer dep = new XincoCoreDataHasDependencyServer(
                                            controller.findXincoCoreData(parent.getId()),
                                            controller.findXincoCoreData(getXincoCoreData().getId()),
                                            new XincoDependencyTypeServer(5));//Rendering
                                    dep.write2DB();
                                } catch (XincoException ex) {
                                    getMainWindow().showNotification(getResource().getString("general.error"),
                                            getResource().getString("message.error.association.exists"),
                                            Notification.TYPE_ERROR_MESSAGE);
                                }
                                getMainWindow().removeWindow(addRenderingWindow);
                                try {
                                    showRenderingDialog();
                                } catch (XincoException ex) {
                                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                            @Override
                            public void wizardCancelled(WizardCancelledEvent event) {
                                getMainWindow().removeWindow(addRenderingWindow);
                            }
                        });
                        getMainWindow().addWindow(addRenderingWindow);
                        addRenderingWindow.center();
                        addRenderingWindow.setModal(true);
                        addRenderingWindow.setWidth(35, Sizeable.UNITS_PERCENTAGE);
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            table.setWidth(100, Sizeable.UNITS_INCH);
            renderWindow.addComponent(form);
            form.getFooter().setSizeUndefined();
            form.getFooter().addComponent(commit);
            form.getFooter().addComponent(cancel);
            form.setSizeFull();
            renderWindow.center();
            renderWindow.setModal(true);
            renderWindow.setWidth(50, Sizeable.UNITS_PERCENTAGE);
            getMainWindow().addWindow(renderWindow);
        }
    }

    private void lockData() throws XincoException, MalformedURLException {
        XincoCoreDataServer xdata = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
        xdata.setStatusNumber(2);
        addLog(xdata, OPCode.LOCK_COMMENT);
        xdata.write2DB();
        refresh();
    }

    private void publishData() throws XincoException, MalformedURLException {
        XincoCoreDataServer xdata = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
        xdata.setStatusNumber(5);
        addLog(xdata, OPCode.PUBLISH_COMMENT);
        xdata.write2DB();
        refresh();
        String tempUrl;
        if (xdata.getXincoCoreDataType().getId() == 1) {
            tempUrl = ((XincoAddAttribute) xdata.getXincoAddAttributes().get(0)).getAttribVarchar();
        } else {
            tempUrl = xdata.getDesignation();
        }
        getMainWindow().showNotification("",
                xerb.getString("datawizard.updatesuccess.publisherinfo") + "\nhttp://[serverName]:[port]/xinco/XincoPublisher/" + xdata.getId() + "/" + tempUrl,
                Notification.TYPE_TRAY_NOTIFICATION);
    }

    private void showACLDialog() {
        final Window aclWindow = new Window();
        final Form form = new Form();
        form.setCaption(getResource().getString("window.acl"));
        final HashMap<String, XincoCoreACEServer> aceList = new HashMap<String, XincoCoreACEServer>();
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
            String userLabel = user.getFirstName() + " "
                    + user.getLastName() + " (" + user.getUsername() + ")";
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
                        try {
                            updateMenu();
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
                            showCommentDataDialog(new XincoCoreDataServer(getXincoCoreData().getId()), OPCode.MODIFICATION);
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
                getResource().getString("general.login"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        try {
                            //Make sure to log out anyone previously logged in
                            loggedUser = null;
                            updateMenu();
                            //Select root node
                            xincoTree.setValue("node-1");
                            getMainWindow().removeWindow(loginWindow);
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
                    String username = ((com.vaadin.ui.TextField) form.getField("username")).getValue().toString();
                    //Wrong password or username
                    java.util.List result = XincoDBManager.createdQuery(
                            "SELECT x FROM XincoCoreUser x WHERE x.username='"
                            + username + "' AND x.statusNumber <> 2");
                    //Check if the username is correct if not just throw the wrong login message
                    if (result.isEmpty()) {
                        getMainWindow().showNotification("Login "
                                + xerb.getString("general.fail")
                                + " Username and/or Password may be incorrect!",
                                Notification.TYPE_WARNING_MESSAGE);
                    } else {
                        result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreUser x WHERE x.username='"
                                + username + "'");
                        if (result.size() > 0) {
                            XincoCoreUserServer temp_user = new XincoCoreUserServer((com.bluecubs.xinco.core.server.persistence.XincoCoreUser) result.get(0));
                            long attempts = XincoSettingServer.getSetting(new XincoCoreUserServer(1),
                                    "password.attempts").getLongValue();
                            //If user exists increase the atempt tries in the db. If limit reached lock account
                            if (temp_user.getAttempts() >= attempts && temp_user.getId() != 1) {
                                //The logged in admin does the locking
                                int adminId = 1;
                                temp_user.setChangerID(adminId);
                                temp_user.setWriteGroups(true);
                                //Register change in audit trail
                                temp_user.setChange(true);
                                //Reason for change
                                temp_user.setReason(xerb.getString("password.attempt.limitReached"));
                                //the password retrieved when you logon is already hashed...
                                temp_user.setHashPassword(false);
                                temp_user.setIncreaseAttempts(true);
                                temp_user.write2DB();
                                getMainWindow().showNotification(xerb.getString("password.attempt.limitReached"),
                                        Notification.TYPE_WARNING_MESSAGE);
                            } else {
                                getMainWindow().showNotification(xerb.getString("password.login.fail"),
                                        Notification.TYPE_WARNING_MESSAGE);
                            }
                        }
                    }
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
                        if (loggedUser.getStatusNumber() == 3) {
                            //Password aging
                            showChangePasswordDialog();
                        }
                        updateMenu();
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                getMainWindow().removeWindow(loginWindow);
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        form.setSizeFull();
        loginWindow.addComponent(form);
        loginWindow.center();
        loginWindow.setModal(true);
        loginWindow.setWidth(30, Sizeable.UNITS_PERCENTAGE);
        loginWindow.setReadOnly(true);
        getMainWindow().addWindow(loginWindow);
    }

    private void refreshGroupTable(final Table table) {
        ArrayList allgroups = XincoCoreGroupServer.getXincoCoreGroups();
        table.removeAllItems();
        for (Iterator<XincoCoreGroupServer> it = allgroups.iterator(); it.hasNext();) {
            XincoCoreGroupServer group = it.next();
            final com.vaadin.ui.Button edit = new com.vaadin.ui.Button(getResource().getString("general.edit"));
            edit.setData(group.getId());
            edit.addStyleName("link");
            edit.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    showEditSingleGroupWindow((Integer) edit.getData());
                }
            });
            table.addItem(new Object[]{group.getId(),
                        getResource().containsKey(group.getDesignation())
                        ? getResource().getString(group.getDesignation())
                        : group.getDesignation(),
                        edit}, group.getId());
        }
        table.sort();
    }

    private void showEditSingleGroupWindow(final Integer groupId) {
        final Window group = new Window();
        final Form form = new Form();
        refreshGroupContentsTables(form, groupId);
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        getMainWindow().removeWindow(group);
                    }
                });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(cancel);
        group.addComponent(form);
        group.setModal(true);
        group.center();
        group.setWidth(70, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(group);
    }

    private void showGroupAdminWindow() {
        final Window group = new Window();
        final Form form = new Form();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getResource().getString("general.id"),
                Integer.class, null);
        table.addContainerProperty(getResource().getString("general.name"),
                String.class, null);
        table.addContainerProperty(getResource().getString("general.edit"),
                com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getResource().getString("general.id"));
        form.getLayout().addComponent(table);
        refreshGroupTable(table);
        form.addField("name", new com.vaadin.ui.TextField(getResource().getString("general.name") + ":"));
        form.getField("name").setRequired(true);
        form.getField("name").setRequiredError(getResource().getString("message.missing.groupname"));
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getResource().getString("general.add.group"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        getMainWindow().removeWindow(group);
                    }
                });
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    commit.setEnabled(false);
                    cancel.setEnabled(false);
                    //Process the data
                    new XincoCoreGroupServer(0,
                            form.getField("name").getValue().toString(),
                            1).write2DB();
                    refreshGroupTable(table);
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        group.addComponent(form);
        group.setModal(true);
        group.center();
        group.setWidth(25, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(group);
    }

    private void showDataTypeAttrAdminWindow(final int dataTypeId) {
        final Window attr = new Window();
        final Form form = new Form();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getResource().getString("general.position"),
                Integer.class, null);
        table.addContainerProperty(getResource().getString("general.designation"),
                String.class, null);
        table.addContainerProperty(getResource().getString("general.datatype"),
                String.class, null);
        table.addContainerProperty(getResource().getString("general.size"),
                String.class, null);
        table.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getResource().getString("general.position"));
        form.getLayout().addComponent(table);
        refreshFileTypeAttrTable(table, dataTypeId);
        form.addField("position", new com.vaadin.ui.TextField(getResource().getString("general.position") + ":"));
        form.getField("position").setRequired(true);
        form.getField("position").setRequiredError(getResource().getString("message.missing.position"));
        form.addField("designation", new com.vaadin.ui.TextField(getResource().getString("general.designation") + ":"));
        form.getField("designation").setRequired(true);
        form.getField("designation").setRequiredError(getResource().getString("message.missing.designation"));
        Select types = new Select(getResource().getString("general.datatype") + ":");
        HashMap<String, String> typeDefs = new HashMap<String, String>();
        typeDefs.put("int", "int (Integer)");
        typeDefs.put("unsignedint", "unsignedint (Unsigned Integer)");
        typeDefs.put("double", "double (Floating Point Number)");
        typeDefs.put("varchar", "varchar (String)");
        typeDefs.put("text", "text (Text)");
        typeDefs.put("datetime", "datetime (Date + Time)");
        for (Entry<String, String> entry : typeDefs.entrySet()) {
            types.addItem(entry.getKey());
            types.setItemCaption(entry.getKey(), entry.getValue());
        }
        form.addField("type", types);
        form.getField("type").setRequired(true);
        form.getField("type").setRequiredError(getResource().getString("message.missing.datatype"));
        form.addField("size", new com.vaadin.ui.TextField(getResource().getString("general.size") + ":"));
        form.getField("size").setRequired(true);
        form.getField("size").setRequiredError(getResource().getString("message.missing.size"));
        form.getField("size").setDescription(getResource().getString("message.admin.attribute.req4string"));
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getResource().getString("general.add.attribute"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        getMainWindow().removeWindow(attr);
                    }
                });
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    commit.setEnabled(false);
                    cancel.setEnabled(false);
                    form.getField("position").setEnabled(false);
                    form.getField("designation").setEnabled(false);
                    form.getField("type").setEnabled(false);
                    form.getField("size").setEnabled(false);
                    XincoCoreDataTypeAttributeServer tempAttribute = new XincoCoreDataTypeAttributeServer(dataTypeId,
                            Integer.valueOf(form.getField("position").getValue().toString()),
                            form.getField("designation").getValue().toString(),
                            form.getField("type").getValue().toString(),
                            Integer.valueOf(form.getField("size").getValue().toString()));
                    tempAttribute.setChangerID(loggedUser.getId());
                    tempAttribute.write2DB();
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    refreshFileTypeAttrTable(table, dataTypeId);
                    form.getField("position").setEnabled(true);
                    form.getField("designation").setEnabled(true);
                    form.getField("type").setEnabled(true);
                    form.getField("size").setEnabled(true);
                    commit.setEnabled(true);
                    cancel.setEnabled(true);
                }
            }
        });

        form.getLayout().addComponent(new com.vaadin.ui.Label(
                getResource().getString("message.warning.attribute.remove")));
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        attr.addComponent(form);
        attr.setModal(true);
        attr.center();
        attr.setWidth(50, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(attr);
    }

    private void showChangePasswordDialog() {
        final Window pass = new Window();
        final Form form = new Form();
        form.getLayout().addComponent(new com.vaadin.ui.Label(xerb.getString("password.aged")));
        form.addField("password", new PasswordField(getResource().getString("general.password")));
        form.addField("confirm", new PasswordField(getResource().getString("general.verifypassword")));
        form.getField("password").setRequired(true);
        form.getField("password").setRequiredError(getResource().getString("message.missing.password"));
        form.getField("confirm").setRequired(true);
        form.getField("confirm").setRequiredError(getResource().getString("message.missing.designation"));
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button("Submit",
                form, "commit");
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                if (!form.getField("password").getValue().equals(form.getField("confirm").getValue())) {
                    getMainWindow().showNotification(
                            getResource().getString("window.userinfo.passwordmismatch"),
                            Notification.TYPE_WARNING_MESSAGE);
                } else {
                    boolean passwordIsUsable = loggedUser.isPasswordUsable(form.getField("password").getValue().toString());
                    if (passwordIsUsable) {
                        try {
                            XincoCoreUserServer temp_user = new XincoCoreUserServer(loggedUser.getId());
                            temp_user.setUserpassword(form.getField("password").getValue().toString());
                            temp_user.setLastModified(new Timestamp(System.currentTimeMillis()));
                            temp_user.setChangerID(loggedUser.getId());
                            temp_user.setWriteGroups(true);
                            //Register change in audit trail
                            temp_user.setChange(true);
                            //Reason for change
                            temp_user.setReason("audit.user.account.password.change");
                            temp_user.setHashPassword(true);
                            temp_user.write2DB();
                            getMainWindow().showNotification(xerb.getString("password.changed"),
                                    Notification.TYPE_TRAY_NOTIFICATION);
                            getMainWindow().removeWindow(pass);
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        getMainWindow().showNotification(xerb.getString("password.unusable"),
                                Notification.TYPE_WARNING_MESSAGE);
                    }
                }
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        pass.addComponent(form);
        pass.setModal(true);
        pass.center();
        pass.setWidth(25, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(pass);
    }

    private Select getLanguageOptions() {
        final Select languages = new Select();
        ArrayList<String> locales = new ArrayList<String>();
        ResourceBundle lrb = ResourceBundle.getBundle(
                "com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
        locales.addAll(Arrays.asList(lrb.getString("AvailableLocales").split(",")));
        for (Iterator<String> it = locales.iterator(); it.hasNext();) {
            String locale = it.next();
            languages.addItem(locale);
            languages.setItemCaption(locale, lrb.getString("Locale." + locale));
            try {
                FileResource flagIcon = getFlagIcon(locale.isEmpty() ? "us" : locale);
                languages.setItemIcon(locale, flagIcon);
            } catch (IOException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return languages;
    }

    private void showLanguageSelection() {
        final Window lang = new Window();
        lang.setReadOnly(true);
        final Form form = new Form();
        Embedded logo = new Embedded("", new ThemeResource("img/xinco_logo.gif"));
        logo.setType(Embedded.TYPE_IMAGE);
        form.getLayout().addComponent(logo);
        form.getLayout().addComponent(new com.vaadin.ui.Label(
                "Please choose a language:"));
        final Select languages = getLanguageOptions();
        form.addField("type", languages);
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button("Submit",
                form, "commit");
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                Locale loc;
                try {
                    String list = languages.getValue().toString();
                    String[] locales;
                    locales = list.split("_");
                    switch (locales.length) {
                        case 1:
                            loc = new Locale(locales[0]);
                            break;
                        case 2:
                            loc = new Locale(locales[0], locales[1]);
                            break;
                        case 3:
                            loc = new Locale(locales[0], locales[1], locales[2]);
                            break;
                        default:
                            loc = Locale.getDefault();
                    }
                } catch (Exception e) {
                    loc = Locale.getDefault();
                }
                setLocale(loc);
                getMainWindow().removeWindow(lang);
                showMainWindow();
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        lang.addComponent(form);
        lang.setModal(true);
        lang.center();
        WebApplicationContext context = (WebApplicationContext) getContext();
        lang.setWidth(Tool.getImageDim(context.getHttpSession().getServletContext().getRealPath(
                "/VAADIN/themes/xinco") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "xinco_logo.gif").width + 30,
                logo.getWidthUnits());
        getMainWindow().addWindow(lang);
    }

    private void showSettingAdminWindow() {
        Window setting = new Window();
        setting.setContent(new SettingAdminWindow());
        setting.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setting.center();
        setting.setModal(true);
        getMainWindow().addWindow(setting);
    }

    private void showAuditWindow() throws XincoException {
        final Window audit = new Window();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getResource().getString("general.table"),
                String.class, null);
        table.addContainerProperty(getResource().getString("general.audit.action"),
                com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getResource().getString("general.table"));
        Set<EntityType<?>> entities = XincoDBManager.getEntityManagerFactory().getMetamodel().getEntities();
        for (EntityType type : entities) {
            String name = type.getName();
            if (type.getJavaType().getSuperclass() == XincoAuditedObject.class) {
                java.util.List<Object> result = XincoDBManager.createdQuery("select distinct x from "
                        + type.getJavaType().getSimpleName() + "T x");
                if (!result.isEmpty()) {
                    final com.vaadin.ui.Button cont = new com.vaadin.ui.Button(xerb.getString("general.continue"));
                    cont.setData(type);
                    cont.addListener(new com.vaadin.ui.Button.ClickListener() {

                        @Override
                        public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                            getMainWindow().removeWindow(audit);
                            showAuditDetails((EntityType) cont.getData());
                        }
                    });
                    table.addItem(new Object[]{name, cont}, name);
                }
            }
        }
        table.sort();
        audit.addComponent(table);
        audit.setModal(true);
        audit.center();
        audit.setWidth(35, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(audit);
    }

    private Table showEntitiesInTable(java.util.List entities) throws XincoException {
        //Create the table
        final Table table = new Table();
        table.addStyleName("striped");
        table.setSizeFull();
        if (!entities.isEmpty()) {
            Class<? extends Object> entityClass = entities.get(0).getClass();
            EntityType entityType =
                    XincoDBManager.getEntityManagerFactory().getMetamodel().entity(entityClass);
            LinkedHashMap<String, PersistentAttributeType> typeMap = new LinkedHashMap<String, PersistentAttributeType>();
            for (Iterator it = entityType.getAttributes().iterator(); it.hasNext();) {
                Attribute attr = (Attribute) it.next();
                table.addContainerProperty(attr.getName(), com.vaadin.ui.Component.class, null);
                typeMap.put(attr.getName(), attr.getPersistentAttributeType());
            }
            //Now add the audit fields
            table.addContainerProperty(getResource().getString("general.reason"), com.vaadin.ui.Component.class, null);
            table.addContainerProperty(getResource().getString("general.audit.modtime"), com.vaadin.ui.Component.class, null);
            table.addContainerProperty(getResource().getString("general.user"), com.vaadin.ui.Component.class, null);
            int index = 0;
            for (Iterator it = entities.iterator(); it.hasNext();) {
                int recordId = 0;
                Object o = it.next();
                ArrayList values = new ArrayList();
                int i = 0;
                for (Iterator<Entry<String, PersistentAttributeType>> it2 = typeMap.entrySet().iterator(); it2.hasNext();) {
                    Entry<String, PersistentAttributeType> entry = it2.next();
                    try {

                        java.lang.reflect.Method method = o.getClass().getMethod("get"
                                + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1));
                        if (entry.getKey().equals("recordId")) {
                            recordId = Integer.valueOf(method.invoke(o).toString());
                        }
                        String value = method.invoke(o).toString();
                        switch (entry.getValue()) {
                            case BASIC:
                                values.add(new com.vaadin.ui.Label(entry.getKey().contains("password")
                                        ? "**********" : (getResource().containsKey(value)
                                        ? getResource().getString(value) : value)));
                                break;
                            default:
                                throw new XincoException(entry.getValue().name() + " not supported yet!");
                        }
                        i++;
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        throw new XincoException(ex.getLocalizedMessage());
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        throw new XincoException(ex.getLocalizedMessage());
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        throw new XincoException(ex.getLocalizedMessage());
                    } catch (NoSuchMethodException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        throw new XincoException(ex.getLocalizedMessage());
                    } catch (SecurityException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        throw new XincoException(ex.getLocalizedMessage());
                    }
                }
                //Now add the audit fields
                HashMap parameters = new HashMap();
                parameters.put("recordId", recordId);
                XincoCoreUserModifiedRecord record = (XincoCoreUserModifiedRecord) XincoDBManager.namedQuery("XincoCoreUserModifiedRecord.findByRecordId", parameters).get(0);
                values.add(new com.vaadin.ui.Label(getResource().containsKey(record.getModReason())
                        ? getResource().getString(record.getModReason())
                        : record.getModReason()));
                values.add(new com.vaadin.ui.Label(record.getModTime().toString()));
                values.add(new com.vaadin.ui.Label(record.getXincoCoreUser().getFirstName()
                        + " " + record.getXincoCoreUser().getLastName()));
                table.addItem(values.toArray(), index++);
            }
        }
        return table;
    }

    private void showAuditDetails(final EntityType entity) {
        try {
            final Window audit = new Window();
            final com.vaadin.ui.TextField tf = new com.vaadin.ui.TextField(
                    entity.getId(entity.getIdType().getJavaType()).getName());
            tf.focus();
            final Table table = showEntitiesInTable(XincoDBManager.createdQuery("select distinct x from "
                    + entity.getJavaType().getSimpleName() + "T x"));
            table.setSortDisabled(false);
            tf.addListener(new TextChangeListener() {

                SimpleStringFilter filter = null;

                @Override
                public void textChange(TextChangeEvent event) {
                    Filterable f = (Filterable) table.getContainerDataSource();

                    // Remove old filter
                    if (filter != null) {
                        f.removeContainerFilter(filter);
                    }
                    // Set new filter for the "key" column
                    filter = new SimpleStringFilter(
                            entity.getId(entity.getIdType().getJavaType()).getName(),
                            event.getText(),
                            true, false);
                    f.addContainerFilter(filter);
                }
            });
            if (entity.getId(entity.getIdType().getJavaType()).getPersistentAttributeType() == PersistentAttributeType.BASIC) {
                audit.addComponent(tf);
            }
            audit.addComponent(table);
            audit.setModal(true);
            audit.center();
            audit.setWidth(90, Sizeable.UNITS_PERCENTAGE);
            getMainWindow().addWindow(audit);
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showAttrAdminWindow() {
        final Window attr = new Window();
        final Form form = new Form();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getResource().getString("general.id"),
                Integer.class, null);
        table.addContainerProperty(getResource().getString("general.designation"),
                String.class, null);
        table.addContainerProperty(getResource().getString("general.description"),
                String.class, null);
        table.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getResource().getString("general.id"));
        form.getLayout().addComponent(table);
        refreshFileTypeTable(table);
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        getMainWindow().removeWindow(attr);
                    }
                });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(cancel);
        attr.addComponent(form);
        attr.setModal(true);
        attr.center();
        attr.setWidth(50, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(attr);
    }

    private void showLangAdminWindow() {
        final Window lang = new Window();
        final Form form = new Form();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getResource().getString("general.id"),
                Integer.class, null);
        table.addContainerProperty(getResource().getString("general.name"),
                String.class, null);
        table.addContainerProperty(getResource().getString("general.designation"),
                String.class, null);
        table.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getResource().getString("general.id"));
        form.getLayout().addComponent(table);
        refreshLanguageTable(table);
        form.addField("designation", new com.vaadin.ui.TextField(getResource().getString("general.designation") + ":"));
        form.getField("designation").setRequired(true);
        form.getField("designation").setRequiredError(getResource().getString("message.missing.designation"));
        form.addField("sign", new com.vaadin.ui.TextField(getResource().getString("general.sign") + ":"));
        form.getField("sign").setRequired(true);
        form.getField("sign").setRequiredError(getResource().getString("message.missing.sign"));
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getResource().getString("general.add.language"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        getMainWindow().removeWindow(lang);
                    }
                });
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    commit.setEnabled(false);
                    cancel.setEnabled(false);
                    form.getField("sign").setEnabled(false);
                    form.getField("designation").setEnabled(false);
                    /**
                     * Check for duplicate designation. This is done at DB level
                     * but won't work for default designation since those are
                     * translated.
                     */
                    for (XincoCoreLanguageServer lang : XincoCoreLanguageServer.getXincoCoreLanguages()) {
                        if (getResource().containsKey(lang.getDesignation())
                                && getResource().getString(lang.getDesignation()).equals(form.getField("designation").getValue().toString())) {
                            //Duplicate designation
                            throw new XincoException();
                        }
                    }
                    //Process the data
                    XincoCoreLanguageServer lang = new XincoCoreLanguageServer(0,
                            form.getField("sign").getValue().toString(),
                            form.getField("designation").getValue().toString());
                    lang.setChangerID(loggedUser.getId());
                    lang.write2DB();
                } catch (XincoException ex) {
                    getMainWindow().showNotification(getResource().getString("general.error"),
                            getResource().getString("error.language.add.duplicate"),
                            Notification.TYPE_WARNING_MESSAGE);
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    refreshLanguageTable(table);
                    form.getField("sign").setValue("");
                    form.getField("designation").setValue("");
                    form.getField("sign").setEnabled(true);
                    form.getField("designation").setEnabled(true);
                    commit.setEnabled(true);
                    cancel.setEnabled(true);
                }
            }
        });
        form.getLayout().addComponent(new com.vaadin.ui.Label(
                getResource().getString("error.language.delete.referenced")));
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        lang.addComponent(form);
        lang.setModal(true);
        lang.center();
        lang.setWidth(50, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(lang);
    }

    private void refreshLanguageTable(final Table table) {
        ArrayList allLanguages = XincoCoreLanguageServer.getXincoCoreLanguages();
        boolean is_used;
        table.removeAllItems();
        for (Iterator<XincoCoreLanguageServer> it = allLanguages.iterator(); it.hasNext();) {
            XincoCoreLanguageServer lang = it.next();
            is_used = XincoCoreLanguageServer.isLanguageUsed(lang);
            final com.vaadin.ui.Button delete = new com.vaadin.ui.Button(getResource().getString("general.delete"));
            delete.setData(lang.getId());
            delete.addStyleName("link");
            delete.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreLanguageServer.deleteFromDB(new XincoCoreLanguageServer((Integer) delete.getData()), loggedUser.getId());
                        refreshLanguageTable(table);
                    } catch (Exception e) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            table.addItem(new Object[]{lang.getId(),
                        getResource().containsKey(lang.getDesignation())
                        ? getResource().getString(lang.getDesignation())
                        : lang.getDesignation(),
                        lang.getSign(),
                        is_used ? null : delete}, lang.getId());
        }
        table.sort();
    }

    private void showUserAdminWindow(final boolean userAdmin) {
        final Window user = new Window();
        final Form form = new Form();
        final Table table = new Table();
        if (userAdmin) {
            table.addStyleName("striped");
            table.addContainerProperty(getResource().getString("general.id"),
                    Integer.class, null);
            table.addContainerProperty(getResource().getString("general.username"),
                    String.class, null);
            table.addContainerProperty(getResource().getString("general.firstname"),
                    String.class, null);
            table.addContainerProperty(getResource().getString("general.lastname"),
                    String.class, null);
            table.addContainerProperty(getResource().getString("general.email"),
                    String.class, null);
            table.addContainerProperty(getResource().getString("general.lock") + "/"
                    + getResource().getString("general.unlock"),
                    com.vaadin.ui.Component.class, null);
            table.addContainerProperty(getResource().getString("general.password.reset") + "*",
                    com.vaadin.ui.Component.class, null);
            refreshUserTable(table);
            table.setSortContainerPropertyId(getResource().getString("general.id"));
            form.getLayout().addComponent(table);
        }
        form.addField("username", new com.vaadin.ui.TextField(getResource().getString("general.username") + ":"));
        form.getField("username").setRequired(userAdmin);
        form.getField("username").setRequiredError(getResource().getString("message.missing.username"));
        if (!userAdmin) {
            form.getField("username").setValue(loggedUser.getUsername());
        }
        form.addField("pass", new com.vaadin.ui.PasswordField(getResource().getString("general.password") + ":"));
        form.getField("pass").setRequired(userAdmin);
        form.getField("pass").setRequiredError(getResource().getString("message.missing.password"));
        if (!userAdmin) {
            form.addField("verify", new com.vaadin.ui.PasswordField(getResource().getString("general.verifypassword") + ":"));
            form.getField("verify").setRequired(userAdmin);
            form.getField("verify").setRequiredError(getResource().getString("message.missing.password"));
        }
        form.addField("firstname", new com.vaadin.ui.TextField(getResource().getString("general.firstname") + ":"));
        form.getField("firstname").setRequired(userAdmin);
        form.getField("firstname").setRequiredError(getResource().getString("message.missing.firstname"));
        if (!userAdmin) {
            form.getField("firstname").setValue(loggedUser.getFirstName());
        }
        form.addField("lastname", new com.vaadin.ui.TextField(getResource().getString("general.lastname") + ":"));
        form.getField("lastname").setRequired(userAdmin);
        form.getField("lastname").setRequiredError(getResource().getString("message.missing.lastname"));
        if (!userAdmin) {
            form.getField("lastname").setValue(loggedUser.getLastName());
        }
        form.addField("email", new com.vaadin.ui.TextField(getResource().getString("general.email") + ":"));
        form.getField("email").setRequired(userAdmin);
        form.getField("email").setRequiredError(getResource().getString("message.missing.email"));
        if (!userAdmin) {
            form.getField("email").setValue(loggedUser.getEmail());
        }
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                userAdmin ? getResource().getString("general.add.user")
                : getResource().getString("general.save"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        getMainWindow().removeWindow(user);
                    }
                });
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    commit.setEnabled(false);
                    cancel.setEnabled(false);
                    //Process the data
                    boolean changed = false;
                    XincoCoreUserServer temp_user;
                    if (userAdmin) {
                        temp_user = new XincoCoreUserServer(0,
                                form.getField("username").getValue().toString(),
                                form.getField("pass").getValue().toString(),
                                form.getField("lastname").getValue().toString(),
                                form.getField("firstname").getValue().toString(),
                                form.getField("email").getValue().toString(),
                                1, 0, new Timestamp(System.currentTimeMillis()));
                        temp_user.getXincoCoreGroups().add(new XincoCoreGroupServer(2));
                        temp_user.setWriteGroups(true);
                        temp_user.setHashPassword(true);
                        //Reason for change
                        temp_user.setReason("audit.user.account.create");
                        changed = true;
                    } else {
                        temp_user = new XincoCoreUserServer(loggedUser.getId());
                        if (!form.getField("username").getValue().toString().equals(loggedUser.getUsername())) {
                            loggedUser.setUsername(form.getField("username").getValue().toString());
                            changed = true;
                        }
                        if (!form.getField("pass").getValue().toString().isEmpty()
                                && !form.getField("pass").getValue().toString().equals(loggedUser.getUserpassword())) {
                            if (!form.getField("pass").getValue().toString().equals(form.getField("verify").getValue().toString())) {
                                getMainWindow().showNotification("window.userinfo.passwordmismatch", Notification.TYPE_WARNING_MESSAGE);
                                return;
                            } else {
                                loggedUser.setUserpassword(form.getField("pass").getValue().toString());
                                loggedUser.setHashPassword(true);
                                changed = true;
                            }
                        }
                        if (!form.getField("username").getValue().toString().equals(loggedUser.getUsername())) {
                            loggedUser.setUsername(form.getField("username").getValue().toString());
                            changed = true;
                        }
                        if (!form.getField("lastname").getValue().toString().equals(loggedUser.getLastName())) {
                            loggedUser.setLastName(form.getField("lastname").getValue().toString());
                            changed = true;
                        }
                        if (!form.getField("firstname").getValue().toString().equals(loggedUser.getLastName())) {
                            loggedUser.setFirstName(form.getField("firstname").getValue().toString());
                            changed = true;
                        }
                        if (changed) {
                            loggedUser.setReason("audit.user.account.modified");
                        }
                    }
                    //The logged in admin does the locking
                    temp_user.setChangerID(loggedUser.getId());
                    //Register change in audit trail
                    temp_user.setChange(true);
                    boolean passwordIsUsable = loggedUser.isPasswordUsable(form.getField("pass").getValue().toString());
                    if (!passwordIsUsable) {
                        getMainWindow().showNotification(xerb.getString("password.unusable"),
                                Notification.TYPE_WARNING_MESSAGE);
                        changed = false;
                    }
                    if (changed) {
                        temp_user.write2DB();
                        loggedUser = temp_user;
                    }
                    if (userAdmin) {
                        refreshUserTable(table);
                    } else {
                        getMainWindow().removeWindow(user);
                    }
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        user.addComponent(form);
        user.setModal(true);
        user.center();
        user.setWidth(25, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(user);
    }

    private com.vaadin.ui.Component getSideMenu() throws XincoException {
        com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel();
        panel.addComponent(icon);
        Accordion menu = new Accordion();
        menu.setSizeFull();
        com.vaadin.ui.Panel accountPanel = new com.vaadin.ui.Panel();
        accountPanel.setContent(new VerticalLayout());
        accountPanel.addComponent(login);
        accountPanel.addComponent(profile);
        accountPanel.addComponent(logout);
        login.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        profile.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        logout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        menu.addTab(accountPanel, getResource().getString("window.connection.profile"), null);
        adminPanel = new com.vaadin.ui.Panel();
        com.vaadin.ui.Button userAdmin = new com.vaadin.ui.Button(xerb.getString("message.admin.userAdmin"));
        userAdmin.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        userAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                showUserAdminWindow(true);
            }
        });
        adminPanel.addComponent(userAdmin);
        com.vaadin.ui.Button groupAdmin = new com.vaadin.ui.Button(xerb.getString("message.admin.groupAdmin"));
        groupAdmin.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        groupAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                showGroupAdminWindow();
            }
        });
        adminPanel.addComponent(groupAdmin);
        com.vaadin.ui.Button langAdmin = new com.vaadin.ui.Button(xerb.getString("message.admin.language"));
        langAdmin.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        langAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                showLangAdminWindow();
            }
        });
        adminPanel.addComponent(langAdmin);
        com.vaadin.ui.Button attrAdmin = new com.vaadin.ui.Button(xerb.getString("message.admin.attribute"));
        attrAdmin.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        attrAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                showAttrAdminWindow();
            }
        });
        adminPanel.addComponent(attrAdmin);
        com.vaadin.ui.Button trashAdmin = new com.vaadin.ui.Button(xerb.getString("message.admin.trash"));
        trashAdmin.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        trashAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    (new XincoCoreNodeServer(2)).deleteFromDB(false, loggedUser.getId());
                    refresh();
                } catch (Exception e) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        });
        adminPanel.addComponent(trashAdmin);
        com.vaadin.ui.Button indexAdmin = new com.vaadin.ui.Button(xerb.getString("message.admin.index"));
        indexAdmin.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        indexAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {

            Table table = new Table();
            final ProgressIndicator indicator = new ProgressIndicator(new Float(0.0));
            com.vaadin.ui.Button ok = new com.vaadin.ui.Button(xerb.getString("general.ok"));

            class IndexRebuilder extends Thread {

                java.util.List result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreData x ORDER BY x.designation");

                @Override
                public void run() {
                    //Delete existing index
                    File indexDirectory;
                    File indexDirectoryFile;
                    String[] indexDirectoryFileList;
                    indexDirectory = new File(XincoDBManager.config.fileIndexPath);
                    int work_units = result.size() + (indexDirectory.exists()
                            ? indexDirectory.list().length + 1 : 0) + 1;
                    int index = 0;
                    int count = 0;
                    if (indexDirectory.exists()) {
                        indexDirectoryFileList = indexDirectory.list();
                        for (int i = 0; i < indexDirectoryFileList.length; i++) {
                            indexDirectoryFile = new File(XincoDBManager.config.fileIndexPath + indexDirectoryFileList[i]);
                            indexDirectoryFile.delete();
                            count++;
                        }
                        boolean indexDirectoryDeleted = indexDirectory.delete();
                        count++;
                        table.addItem(new Object[]{new com.vaadin.ui.Label(
                                    xerb.getString("message.index.delete")),
                                    new com.vaadin.ui.Label(indexDirectoryDeleted
                                    ? xerb.getString("general.ok") + "!" : xerb.getString("general.fail"))}, index++);
                    }
                    XincoCoreDataServer xdataTemp;
                    boolean index_result;
                    for (Object o : result) {
                        xdataTemp = new XincoCoreDataServer((com.bluecubs.xinco.core.server.persistence.XincoCoreData) o);
                        if (!XincoCoreDataHasDependencyServer.isRendering(xdataTemp.getId())) {
                            index_result = XincoIndexer.indexXincoCoreData(xdataTemp, true);
                            table.addItem(new Object[]{new com.vaadin.ui.Label(xdataTemp.getDesignation()),
                                        new com.vaadin.ui.Label(index_result
                                        ? xerb.getString("general.ok") + "!" : xerb.getString("general.fail"))}, index++);
                            count++;
                            indicator.setValue(new Float(count) / new Float(work_units));
                        }
                    }
                    index_result = XincoIndexer.optimizeIndex();
                    //Optimize index
                    table.addItem(new Object[]{new com.vaadin.ui.Label(xerb.getString("message.index.optimize")),
                                new com.vaadin.ui.Label(index_result
                                ? xerb.getString("general.ok") + "!" : xerb.getString("general.fail"))}, index++);
                    count++;
                    indicator.setValue(new Float(1.0));
                    ok.setEnabled(true);
                }
            }

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    final Window progress = new Window();
                    progress.addComponent(indicator);
                    // Set polling frequency to 0.5 seconds.
                    indicator.setPollingInterval(500);
                    progress.addComponent(new com.vaadin.ui.Label(xerb.getString("message.index.rebuild")));
                    progress.addComponent(new com.vaadin.ui.Label(xerb.getString("message.warning.index.rebuild")));
                    table.addStyleName("striped");
                    table.addContainerProperty(xerb.getString("message.data.sort.designation"),
                            com.vaadin.ui.Label.class, null);
                    table.addContainerProperty(xerb.getString("message.indexing.status"),
                            com.vaadin.ui.Label.class, null);
                    progress.addComponent(table);
                    ok.addListener(new com.vaadin.ui.Button.ClickListener() {

                        @Override
                        public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                            getMainWindow().removeWindow(progress);
                        }
                    });
                    progress.addComponent(ok);
                    progress.setWidth(50, Sizeable.UNITS_PERCENTAGE);
                    ok.setEnabled(false);
                    getMainWindow().addWindow(progress);
                    progress.center();
                    new IndexRebuilder().start();
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    ok.setEnabled(true);
                }
            }
        });
        adminPanel.addComponent(indexAdmin);
        com.vaadin.ui.Button auditAdmin = new com.vaadin.ui.Button(xerb.getString("general.audit.menu"));
        auditAdmin.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        auditAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    showAuditWindow();
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        adminPanel.addComponent(auditAdmin);
        com.vaadin.ui.Button settingAdmin = new com.vaadin.ui.Button(xerb.getString("message.admin.settingAdmin"));
        settingAdmin.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        settingAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                showSettingAdminWindow();
            }
        });
        adminPanel.addComponent(settingAdmin);
        menu.addTab(adminPanel, "XincoAdmin", null);
        panel.addComponent(menu);
        return panel;
    }

    private void updateSideMenu() {
        login.setEnabled(loggedUser == null);
        logout.setEnabled(loggedUser != null);
        profile.setEnabled(loggedUser != null);
        boolean isAdmin = false;
        if (loggedUser != null) {
            for (XincoCoreGroup xcg : loggedUser.getXincoCoreGroups()) {
                if (xcg.getId() == 1) {
                    isAdmin = true;
                    break;
                }
            }
        }
        adminPanel.setVisible(isAdmin);
    }

    private void refreshUserTable(final Table table) {
        table.removeAllItems();
        ArrayList<XincoCoreUserServer> allusers = XincoCoreUserServer.getXincoCoreUsers();
        for (Iterator<XincoCoreUserServer> it = allusers.iterator(); it.hasNext();) {
            XincoCoreUserServer tempUser = it.next();
            com.vaadin.ui.Button lock = new com.vaadin.ui.Button(getResource().getString("general.lock"));
            lock.setData(tempUser.getId());
            lock.addStyleName("link");
            lock.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreUserServer temp_user = new XincoCoreUserServer((Integer) event.getButton().getData());
                        temp_user.setStatusNumber(2);
                        //The logged in admin does the locking
                        temp_user.setChangerID(loggedUser.getId());
                        temp_user.setWriteGroups(true);
                        //Register change in audit trail
                        temp_user.setChange(true);
                        //Reason for change
                        temp_user.setReason("audit.user.account.lock");
                        temp_user.write2DB();
                        refreshUserTable(table);
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            com.vaadin.ui.Button unlock = new com.vaadin.ui.Button(getResource().getString("general.unlock"));
            unlock.setData(tempUser.getId());
            unlock.addStyleName("link");
            unlock.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreUserServer temp_user = new XincoCoreUserServer((Integer) event.getButton().getData());
                        temp_user.setStatusNumber(1);
                        //Reset login attempts
                        temp_user.setAttempts(0);
                        //The logged in admin does the locking
                        temp_user.setChangerID(loggedUser.getId());
                        temp_user.setWriteGroups(true);
                        //Register change in audit trail
                        temp_user.setChange(true);
                        //Reason for change
                        temp_user.setReason("audit.user.account.unlock");
                        temp_user.write2DB();
                        refreshUserTable(table);
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            com.vaadin.ui.Button reset = new com.vaadin.ui.Button(getResource().getString("general.password.reset"));
            reset.setData(tempUser.getId());
            reset.addStyleName("link");
            reset.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreUserServer temp_user = new XincoCoreUserServer((Integer) event.getButton().getData());
                        temp_user.setUserpassword("123456");
                        //The logged in admin does the locking
                        temp_user.setChangerID(loggedUser.getId());
                        temp_user.setWriteGroups(true);
                        //Register change in audit trail
                        temp_user.setChange(true);
                        //Reason for change
                        temp_user.setReason("audit.user.account.password.reset");
                        temp_user.write2DB();
                        refreshUserTable(table);
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            table.addItem(new Object[]{tempUser.getId(),
                        tempUser.getUsername(),
                        tempUser.getFirstName(),
                        tempUser.getLastName(),
                        tempUser.getEmail(),
                        tempUser.getStatusNumber() == 1 ? lock : unlock,
                        reset}, tempUser.getId());
        }
        table.sort();
    }

    private void refreshGroupContentsTables(final Form form, final int groupId) {
        final Table table1 = new Table();
        table1.addStyleName("striped");
        table1.addContainerProperty(getResource().getString("general.id"),
                Integer.class, null);
        table1.addContainerProperty(getResource().getString("general.username"),
                String.class, null);
        table1.addContainerProperty(getResource().getString("general.firstname"),
                String.class, null);
        table1.addContainerProperty(getResource().getString("general.lastname"),
                String.class, null);
        table1.addContainerProperty(getResource().getString("general.email"),
                String.class, null);
        table1.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table1.setSortContainerPropertyId(getResource().getString("general.id"));
        table1.setPageLength(10);
        final Table table2 = new Table();
        table2.addStyleName("striped");
        table2.addContainerProperty(getResource().getString("general.id"),
                Integer.class, null);
        table2.addContainerProperty(getResource().getString("general.username"),
                String.class, null);
        table2.addContainerProperty(getResource().getString("general.firstname"),
                String.class, null);
        table2.addContainerProperty(getResource().getString("general.lastname"),
                String.class, null);
        table2.addContainerProperty(getResource().getString("general.email"),
                String.class, null);
        table2.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table2.setSortContainerPropertyId(getResource().getString("general.id"));
        table1.setPageLength(2);
        ArrayList<XincoCoreUserServer> allusers = XincoCoreUserServer.getXincoCoreUsers();
        boolean member_ofGroup;
        form.getLayout().removeAllComponents();
        for (Iterator<XincoCoreUserServer> it = allusers.iterator(); it.hasNext();) {
            XincoCoreUserServer tempUser = it.next();
            member_ofGroup = false;
            for (Iterator<XincoCoreGroup> it2 = tempUser.getXincoCoreGroups().iterator(); it2.hasNext();) {
                if (it2.next().getId() == groupId) {
                    member_ofGroup = true;
                    break;
                }
            }
            final com.vaadin.ui.Button remove = new com.vaadin.ui.Button(getResource().getString("general.group.removeuser"));
            remove.setData(tempUser.getId());
            remove.addStyleName("link");
            remove.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    //main admin always is admin and everyone is a regular user
                    if (!(((groupId == 1) && ((Integer) remove.getData() == 1)) || (groupId == 2))) {
                        try {
                            java.util.List results = XincoDBManager.createdQuery("SELECT x FROM XincoCoreUserHasXincoCoreGroup x "
                                    + "WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = " + (Integer) remove.getData()
                                    + " and x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = " + groupId);
                            for (Object o : results) {
                                new XincoCoreUserHasXincoCoreGroupJpaController(XincoDBManager.getEntityManagerFactory()).destroy(((XincoCoreUserHasXincoCoreGroup) o).getXincoCoreUserHasXincoCoreGroupPK());
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        getMainWindow().showNotification(getResource().getString("error.user.remove.mainUserGroup"),
                                Notification.TYPE_WARNING_MESSAGE);
                    }
                    refreshGroupContentsTables(form, groupId);
                }
            });
            final com.vaadin.ui.Button add = new com.vaadin.ui.Button(getResource().getString("general.group.adduser"));
            add.setData(tempUser.getId());
            add.addStyleName("link");
            add.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup(
                                (Integer) add.getData(), groupId);
                        uhg.setXincoCoreGroup(new XincoCoreGroupJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreGroup(groupId));
                        uhg.setXincoCoreUser(new XincoCoreUserJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreUser((Integer) add.getData()));
                        new XincoCoreUserHasXincoCoreGroupJpaController(XincoDBManager.getEntityManagerFactory()).create(uhg);
                    } catch (Exception ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    refreshGroupContentsTables(form, groupId);
                }
            });
            if (member_ofGroup) {
                table1.addItem(new Object[]{tempUser.getId(),
                            tempUser.getUsername(),
                            tempUser.getFirstName(),
                            tempUser.getLastName(),
                            tempUser.getEmail(),
                            remove}, tempUser.getId());
            } else {
                table2.addItem(new Object[]{tempUser.getId(),
                            tempUser.getUsername(),
                            tempUser.getFirstName(),
                            tempUser.getLastName(),
                            tempUser.getEmail(),
                            add}, tempUser.getId());
            }
        }
        form.getLayout().addComponent(new com.vaadin.ui.Label(getResource().getString("general.group.member")));
        form.getLayout().addComponent(table1);
        form.getLayout().addComponent(new com.vaadin.ui.Label(getResource().getString("general.group.notmember")));
        form.getLayout().addComponent(table2);
    }

    private void refreshFileTypeAttrTable(final Table table, final int dataTypeId) {
        ArrayList<XincoCoreDataTypeAttribute> alldatatypes = (ArrayList<XincoCoreDataTypeAttribute>) XincoCoreDataTypeServer.getXincoCoreDataType(dataTypeId).getXincoCoreDataTypeAttributes();
        table.removeAllItems();
        for (Iterator<XincoCoreDataTypeAttribute> it = alldatatypes.iterator(); it.hasNext();) {
            XincoCoreDataTypeAttribute type = it.next();
            final com.vaadin.ui.Button delete = new com.vaadin.ui.Button(getResource().getString("general.delete") + "*");
            delete.setData(type.getAttributeId());
            delete.addStyleName("link");
            delete.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreDataTypeAttributeServer tempAttribute = new XincoCoreDataTypeAttributeServer(dataTypeId, (Integer) delete.getData());
                        XincoCoreDataTypeAttributeServer.deleteFromDB(tempAttribute, loggedUser.getId());
                        refreshFileTypeAttrTable(table, dataTypeId);
                    } catch (Exception e) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            table.addItem(new Object[]{type.getAttributeId(),
                        getResource().containsKey(type.getDesignation())
                        ? getResource().getString(type.getDesignation())
                        : type.getDesignation(),
                        type.getDataType(),
                        type.getSize(),
                        ((dataTypeId == 1)
                        && (type.getAttributeId() <= 8))
                        || ((dataTypeId == 2)
                        && (type.getAttributeId() <= 1))
                        || ((dataTypeId == 3)
                        && (type.getAttributeId() <= 1)) ? null : delete},
                    type.getAttributeId());
        }
        table.sort();
    }

    private void refreshFileTypeTable(Table table) {
        ArrayList alldatatypes = XincoCoreDataTypeServer.getXincoCoreDataTypes();
        table.removeAllItems();
        for (Iterator<XincoCoreDataTypeServer> it = alldatatypes.iterator(); it.hasNext();) {
            XincoCoreDataTypeServer type = it.next();
            final com.vaadin.ui.Button edit = new com.vaadin.ui.Button(getResource().getString("general.edit"));
            edit.setData(type.getId());
            edit.addStyleName("link");
            edit.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        showDataTypeAttrAdminWindow((Integer) edit.getData());
                    } catch (Exception e) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            table.addItem(new Object[]{type.getId(),
                        getResource().containsKey(type.getDesignation())
                        ? getResource().getString(type.getDesignation())
                        : type.getDesignation(),
                        getResource().containsKey(type.getDescription())
                        ? getResource().getString(type.getDescription())
                        : type.getDescription(),
                        edit}, type.getId());
        }
        table.sort();
    }

    private void initMenuItems() {
        XincoMenuItem item;
        int i = 0;
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.repository.refresh"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            refresh();
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                },
                false, //Need to be logged in
                false, //Data only
                false, //Node only
                false);
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.repository.addfolder"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //Show the Data Folder Dialog window
                        showDataFolderDialog(true);
                    }
                },
                true, //Need to be logged in
                false, //Data only
                true, //Node only
                true //Something selected
                );
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.repository.adddata"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //Show the Data Folder Dialog window
                        showDataDialog(true);
                    }
                },
                true, //Need to be logged in
                false, //Data only
                true, //Node only
                true //Something selected
                );
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.repository.adddatastructure"),
                smallIcon,
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
                                    data = new XincoCoreData();
                                    getXincoCoreData().setXincoCoreDataType(new XincoCoreDataTypeServer(1));
                                    getXincoCoreData().setStatusNumber(1);
                                    getXincoCoreData().setXincoCoreLanguage(new XincoCoreLanguageServer(2));
                                    getXincoCoreData().setXincoCoreNodeId(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                                    Tool.addDefaultAddAttributes(getXincoCoreData());
                                    loadFile(file, fileName);
                                }
                            }
                        };
                        fileUpload.setWidth("600px");
                        w.addComponent(fileUpload);
                        w.setModal(true);
                        w.center();
                        getMainWindow().addWindow(w);
                    }
                },
                true, //Need to be logged in
                false, //Data only
                true, //Node only
                true //Something selected
                );
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.edit.folderdata"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        if (xincoTree.getValue().toString().startsWith("node")) {
                            showDataFolderDialog(false);
                        } else if (xincoTree.getValue().toString().startsWith("data")) {
                            showDataDialog(false);
                        }
                    }
                },
                true, //Need to be logged in
                false, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{1, 4});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.repository.vieweditaddattributes"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //Show the Data Folder Dialog window
                        showDataDialog(false);
                    }
                },
                true, //Need to be logged in
                true, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{1, 4});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.edit.acl"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        showACLDialog();
                    }
                },
                true, //Need to be logged in
                false, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{1, 4});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.repository.downloadfile"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            downloadFile();
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                },
                false, //Need to be logged in
                false, //Data only
                false, //Node only
                true //Something selected
                );
        item.setDataTypes(new int[]{1});
        XincoMenuItemManager.addItem(item);
        //TODO: Enable Rendering support
        if (renderingSupportEnabled) {
            XincoMenuItemManager.addItem(item);
            item = new XincoMenuItem(i += 1000,
                    getResource().getString("menu.repository"),
                    getResource().getString("menu.repository.addrendering"),
                    smallIcon,
                    new com.vaadin.ui.MenuBar.Command() {

                        @Override
                        public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                            try {
                                showRenderingDialog();
                            } catch (XincoException ex) {
                                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    },
                    true, //Need to be logged in
                    true, //Data only
                    false, //Node only
                    true //Something selected
                    );
            item.setDataTypes(new int[]{1});
            XincoMenuItemManager.addItem(item);
        }
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.edit.checkoutfile"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        checkoutFile();
                    }
                },
                true, //Need to be logged in
                true, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{1});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.edit.undocheckout"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        undoCheckoutFile();
                    }
                },
                true, //Need to be logged in
                true, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{4});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.edit.checkinfile"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        checkinFile();
                    }
                },
                true, //Need to be logged in
                true, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{4});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.edit.publishdata"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            publishData();
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                },
                true, //Need to be logged in
                true, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{1});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.edit.lockdata"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            lockData();
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                },
                true, //Need to be logged in
                true, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{1});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.edit.downloadrevision"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        showDownloadRevDialog();
                    }
                },
                true, //Need to be logged in
                true, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{1});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.repository"),
                getResource().getString("menu.edit.commentdata"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            final XincoCoreDataServer xdata = new XincoCoreDataServer(
                                    Integer.valueOf(xincoTree.getValue().toString().substring(
                                    xincoTree.getValue().toString().indexOf('-') + 1)));
                            showCommentDataDialog(xdata, OPCode.COMMENT);
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                },
                true, //Need to be logged in
                true, //Data only
                false, //Node only
                true //Something selected
                );
        item.setStatuses(new int[]{1});
        XincoMenuItemManager.addItem(item);
        item = new XincoMenuItem(i += 1000,
                getResource().getString("menu.search"),
                getResource().getString("menu.search.search_repository"),
                smallIcon,
                new com.vaadin.ui.MenuBar.Command() {

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            showLuceneSearchWindow();
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                },
                false, //Need to be logged in
                false, //Data only
                false, //Node only
                false //Something selected
                );
        XincoMenuItemManager.addItem(item);
    }

    public boolean selectNode(String nodeId) {
        if (getXincoTree() == null) {
            return false;
        }
        return getXincoTree().expandItem(nodeId);
    }

    public boolean expandTreeNodes(java.util.List<Integer> parents) {
        if (getXincoTree() == null) {
            return false;
        }
        for (Integer id : parents) {
            if (!getXincoTree().expandItem("node-" + id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the data
     */
    protected XincoCoreData getXincoCoreData() {
        return data;
    }

    private void showCommentDataDialog(final XincoCoreDataServer data, final OPCode opcode) throws XincoException {
        final Form form = new Form();
        final VersionSelector versionSelector = new VersionSelector();
        buildLogDialog(data, form, versionSelector);
        final Window comment = new Window();
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getResource().getString("general.continue"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        getMainWindow().removeWindow(comment);
                    }
                });
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    commit.setEnabled(false);
                    cancel.setEnabled(false);
                    //Process the data
                    int log_index;
                    String text;
                    addLog(data, opcode);
                    log_index = data.getXincoCoreLogs().size() - 1;
                    XincoCoreLogServer newLog = new XincoCoreLogServer(((XincoCoreLog) data.getXincoCoreLogs().get(log_index)).getId());
                    //Reason really needed only for checkin
                    text = newLog.getOpDescription() + " "
                            + (((XincoCoreLog) data.getXincoCoreLogs().get(log_index)).getOpCode() == 3 ? getResource().getString("general.status.checkedout") : form.getField("reason").getValue());
                    newLog.setOpDescription(text);
                    text = "" + versionSelector.getVersion().getVersionHigh();
                    try {
                        newLog.getVersion().setVersionHigh(Integer.parseInt(text));
                    } catch (Exception nfe) {
                        newLog.getVersion().setVersionHigh(0);
                    }
                    text = "" + versionSelector.getVersion().getVersionMid();
                    try {
                        newLog.getVersion().setVersionMid(Integer.parseInt(text));
                    } catch (Exception nfe) {
                        newLog.getVersion().setVersionMid(0);
                    }
                    text = "" + versionSelector.getVersion().getVersionLow();
                    try {
                        newLog.getVersion().setVersionLow(Integer.parseInt(text));
                    } catch (Exception nfe) {
                        newLog.getVersion().setVersionLow(0);
                    }
                    text = versionSelector.getVersion().getVersionPostfix() == null ? "" : versionSelector.getVersion().getVersionPostfix();
                    newLog.getVersion().setVersionPostfix(text);
                    newLog.write2DB();
                    data.write2DB();
                    getMainWindow().removeWindow(comment);
                    refresh();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        comment.addComponent(form);
        comment.setModal(true);
        comment.center();
        comment.setWidth(35, Sizeable.UNITS_PERCENTAGE);
        getMainWindow().addWindow(comment);
    }

    private void showDownloadRevDialog() {
        try {
            final Window revWindow = new Window(xerb.getString("window.revision"));
            final Form form = new Form();
            form.getLayout().addComponent(new com.vaadin.ui.Label(xerb.getString("window.revision")));
            Select rev = new Select();
            int i;
            String text;
            Calendar cal;
            XMLGregorianCalendar realcal;
            Calendar ngc = new GregorianCalendar();
            final XincoCoreDataServer xdata = new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
            for (i = 0; i < xdata.getXincoCoreLogs().size(); i++) {
                if ((((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getOpCode() == 1)
                        || (((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getOpCode() == 5)) {
                    //convert clone from remote time to local time
                    Date time = ((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getOpDatetime().toGregorianCalendar().getTime();
                    cal = new GregorianCalendar();
                    cal.setTime(time);
                    realcal = (((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getOpDatetime());
                    cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET)
                            - realcal.toGregorianCalendar().get(Calendar.ZONE_OFFSET))
                            - (ngc.get(Calendar.DST_OFFSET)
                            + realcal.toGregorianCalendar().get(Calendar.DST_OFFSET)));
                    text = "" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH);
                    text = text + " - " + xerb.getString("general.version") + " " + ((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getVersion().getVersionHigh()
                            + "." + ((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getVersion().getVersionMid() + "."
                            + ((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getVersion().getVersionLow() + " "
                            + ((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getVersion().getVersionPostfix();
                    text = text + " - " + ((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getOpDescription();
                    int id = ((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getId();
                    rev.addItem(id);
                    rev.setItemCaption(id, text);
                }
            }
            form.addField("rev", rev);
            form.getField("rev").setRequired(true);
            form.getField("rev").setRequiredError(getResource().getString("message.missing.rev"));
            form.setFooter(new HorizontalLayout());
            //Used for validation purposes
            final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                    getResource().getString("general.continue"), form, "commit");
            final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                    getResource().getString("general.cancel"),
                    new com.vaadin.ui.Button.ClickListener() {

                        @Override
                        public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                            getMainWindow().removeWindow(revWindow);
                        }
                    });
            commit.addListener(new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        //Disable data fields, make sure nothing gets modified after clicking save
                        form.getField("rev").setEnabled(false);
                        commit.setEnabled(false);
                        cancel.setEnabled(false);
                        //Process the data
                        ArrayList dataLogArrayList = new ArrayList();
                        dataLogArrayList.addAll(xdata.getXincoCoreLogs());
                        XincoCoreLog revLog = null;
                        for (int i = 0; i < xdata.getXincoCoreLogs().size(); i++) {
                            if (((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getId()
                                    == Integer.valueOf(form.getField("rev").getValue().toString())) {
                                revLog = (XincoCoreLog) xdata.getXincoCoreLogs().get(i);
                                break;
                            }
                        }
                        ArrayList revLogArrayList = new ArrayList();
                        revLogArrayList.add(revLog);
                        xdata.getXincoCoreLogs().clear();
                        xdata.getXincoCoreLogs().addAll(revLogArrayList);
                        //Download file
                        try {
                            downloadFile(xdata);
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        getMainWindow().removeWindow(revWindow);
                        refresh();
                    } catch (XincoException ex) {
                        Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            form.getFooter().setSizeUndefined();
            form.getFooter().addComponent(commit);
            form.getFooter().addComponent(cancel);
            revWindow.addComponent(form);
            revWindow.setModal(true);
            revWindow.center();
            revWindow.setWidth(35, Sizeable.UNITS_PERCENTAGE);
            getMainWindow().addWindow(revWindow);
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showDataDialog(final boolean newData) {
        final XincoWizard wizard = new XincoWizard(getLocale());
        final WizardStep dataDetails = new WizardStep() {

            @Override
            public String getCaption() {
                return getResource().getString("window.datadetails");
            }

            @Override
            public com.vaadin.ui.Component getContent() {
                if (dataDialog == null) {
                    dataDialog = new DataDialog(newData, com.bluecubs.xinco.core.server.vaadin.Xinco.this);
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
                    getXincoCoreData().setDesignation(dataDialog.getDesignationField().getValue().toString());
                    getXincoCoreData().setXincoCoreLanguage((XincoCoreLanguage) XincoCoreLanguageServer.getXincoCoreLanguages().get(Integer.valueOf(dataDialog.getLanguages().getValue().toString())));
                }
                return value;
            }

            @Override
            public boolean onBack() {
                return true;
            }

            @Override
            public String toString() {
                return getCaption();
            }
        };
        if (newData) {
            final WizardStep attrStep = new WizardStep() {

                @Override
                public String getCaption() {
                    return getResource().getString("window.addattributesuniversal");
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    if (attrDialog == null) {
                        attrDialog = new AddAttributeDialog(getXincoCoreData(), com.bluecubs.xinco.core.server.vaadin.Xinco.this);
                        attrDialog.setSizeFull();
                    }
                    return attrDialog;
                }

                @Override
                public boolean onAdvance() {
                    //Process the data
                    attrDialog.updateAttributes();
                    //True if there are more steps after this one
                    return wizard.getSteps().size() > wizard.getLastCompleted() + 1;
                }

                @Override
                public boolean onBack() {
                    return true;
                }

                @Override
                public String toString() {
                    return getCaption();
                }
            };
            wizard.addStep(new WizardStep() {

                @Override
                public String getCaption() {
                    return getResource().getString("window.datatype");
                }

                @Override
                public String toString() {
                    return getCaption();
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    if (dataTypeDialog == null) {
                        dataTypeDialog = new DataTypeDialog(com.bluecubs.xinco.core.server.vaadin.Xinco.this);
                        dataTypeDialog.setSizeFull();
                        dataTypeDialog.getTypes().addListener(new ValueChangeListener() {

                            ArrayList<WizardStep> temp = new ArrayList<WizardStep>();

                            @Override
                            public void valueChange(ValueChangeEvent event) {
                                data = new XincoCoreData();
                                try {
                                    //Process data
                                    getXincoCoreData().setXincoCoreDataType(new XincoCoreDataTypeServer(Integer.valueOf(dataTypeDialog.getTypes().getValue().toString())));
                                } catch (XincoException ex) {
                                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //Set the parent id to the current selected node
                                getXincoCoreData().setXincoCoreNodeId(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                                Tool.addDefaultAddAttributes(getXincoCoreData());
                                //wizard.getLastCompleted() is the previous step, 
                                //the current is wizard.getLastCompleted() + 1, 
                                //the next step wizard.getLastCompleted() + 2
                                switch (getXincoCoreData().getXincoCoreDataType().getId()) {
                                    //File = 1
                                    case 1:
                                        clearTempSteps();
                                        addAttributeStep();
                                        temp.add(fileStep);
                                        wizard.addStep(temp.get(temp.size() - 1), wizard.getLastCompleted() + 1);
                                        getXincoCoreData().getXincoAddAttributes().get(3).setAttribUnsignedint(1); //revision model
                                        getXincoCoreData().getXincoAddAttributes().get(4).setAttribUnsignedint(0); //archiving model
                                        //Is a file, show archiving dialog
                                        temp.add(new WizardStep() {

                                            @Override
                                            public String getCaption() {
                                                return getResource().getString("window.archive");
                                            }

                                            @Override
                                            public com.vaadin.ui.Component getContent() {
                                                if (archDialog == null) {
                                                    archDialog = new ArchiveDialog(com.bluecubs.xinco.core.server.vaadin.Xinco.this);
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

                                            @Override
                                            public String toString() {
                                                return getCaption();
                                            }
                                        });
                                        wizard.addStep(temp.get(temp.size() - 1));
                                        break;
                                    //Text data
                                    case 2:
                                        clearTempSteps();
                                        addAttributeStep();
                                        final ExpandingTextArea tArea = new ExpandingTextArea();
                                        temp.add(new WizardStep() {

                                            @Override
                                            public String getCaption() {
                                                return getResource().getString("window.addattributestext");
                                            }

                                            @Override
                                            public com.vaadin.ui.Component getContent() {
                                                return tArea;
                                            }

                                            @Override
                                            public boolean onAdvance() {
                                                if (tArea.getValue().toString().isEmpty()) {
                                                    getMainWindow().showNotification(
                                                            getResource().getString("general.error"),
                                                            getResource().getString("message.missing.text"),
                                                            Notification.TYPE_WARNING_MESSAGE);
                                                    return false;
                                                } else {
                                                    //Process the data
                                                    getXincoCoreData().getXincoAddAttributes().get(0).setAttribText(tArea.getValue().toString());
                                                    return true;
                                                }
                                            }

                                            @Override
                                            public boolean onBack() {
                                                return true;
                                            }

                                            @Override
                                            public String toString() {
                                                return getCaption();
                                            }
                                        });
                                        wizard.addStep(temp.get(temp.size() - 1), wizard.getLastCompleted() + 1);
                                        break;
                                    default:
                                        clearTempSteps();
                                        //Only show the attribute screen
                                        addAttributeStep();
                                        break;
                                }
                            }

                            private void addAttributeStep() {
                                if ((getXincoCoreData().getXincoCoreDataType().getId() != 1
                                        || getXincoCoreData().getXincoAddAttributes().size() > 8)
                                        && (getXincoCoreData().getXincoCoreDataType().getId() != 2
                                        || getXincoCoreData().getXincoAddAttributes().size() > 1)) {
                                    temp.add(attrStep);
                                    wizard.addStep(temp.get(temp.size() - 1), wizard.getLastCompleted() + 1);
                                }
                            }

                            private void clearTempSteps() {
                                for (WizardStep ws : temp) {
                                    if (wizard.getSteps().contains(ws)) {
                                        wizard.removeStep(ws);
                                    }
                                }
                                temp.clear();
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
                data = getService().getXincoCoreData(
                        new XincoCoreDataServer(Integer.valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1))),
                        loggedUser);
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        wizard.addStep(dataDetails);
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

    /**
     * @return the fileToLoad
     */
    protected File getFileToLoad() {
        return fileToLoad;
    }

    /**
     * @return the fileName
     */
    protected String getFileName() {
        return fileName;
    }

    /**
     * @param fileToLoad the fileToLoad to set
     */
    protected void setFileToLoad(File fileToLoad) {
        this.fileToLoad = fileToLoad;
    }

    /**
     * @param fileName the fileName to set
     */
    protected void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private class DataDialogManager implements WizardProgressListener {

        private final boolean newData;

        DataDialogManager(boolean newData) {
            this.newData = newData;
        }

        @Override
        public void activeStepChanged(WizardStepActivationEvent event) {
            //Nothing to do
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
                if (newData && getXincoCoreData() != null) {
                    //Cancelled so roll back everything done in database
                    HashMap parameters = new HashMap();
                    parameters.put("id", getXincoCoreData().getId());
                    XincoCoreDataServer.removeFromDB(loggedUser.getId(), getXincoCoreData().getId());
                }
                //Remove the file from the repository as well
                closeWizard();
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void finishWizard() {
            if (newData && getXincoCoreData() != null) {
                try {
                    if (getXincoCoreData().getXincoCoreLanguage() == null) {
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
                        getXincoCoreData().setXincoCoreLanguage(xcl1);
                    }
                    getXincoCoreData().setId(0);
                    // set data attributes
                    getXincoCoreData().setStatusNumber(1);
                    switch (getXincoCoreData().getXincoCoreDataType().getId()) {
                        case 1:
                            //Now load the file
                            loadFile(getFileToLoad(), getFileName());
                            break;
                        default:
                            // save data to server
                            data = getService().setXincoCoreData(getXincoCoreData(), loggedUser);
                            if (getXincoCoreData() == null) {
                                throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
                            }
                            break;
                    }
                    addLog(getXincoCoreData(), getXincoCoreData().getXincoCoreLogs().isEmpty() ? OPCode.CREATION : OPCode.CHECKIN);
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //Update data only
                getService().setXincoCoreData(getXincoCoreData(), loggedUser);
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
            setFileToLoad(null);
            setFileName(null);
        }

        private void closeWizard() {
            getMainWindow().removeWindow(wizardWindow);
            discard();
            try {
                //Show changes in tree
                refresh();
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void addLog(XincoCoreData data, OPCode code) throws XincoException, MalformedURLException {
        //Add log
        XincoCoreLog newlog = new XincoCoreLog();
        newlog.setOpCode(code.ordinal() + 1);
        newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName())
                + "!" + " ("
                + xerb.getString("general.user") + ": "
                + loggedUser.getUsername()
                + ")");
        newlog.setXincoCoreUserId(loggedUser.getId());
        newlog.setXincoCoreDataId(data.getId());
        if (XincoCoreDataServer.getCurrentVersion(data.getId()) == null) {
            newlog.setVersion(new XincoVersion());
            newlog.getVersion().setVersionHigh(1);
            newlog.getVersion().setVersionMid(0);
            newlog.getVersion().setVersionLow(0);
            newlog.getVersion().setVersionPostfix("");
        } else {
            newlog.setVersion(XincoCoreDataServer.getCurrentVersion(data.getId()));
        }
        try {
            DatatypeFactory factory = DatatypeFactory.newInstance();
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            newlog.setOpDatetime(factory.newXMLGregorianCalendar(cal));
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
        // save log to server
        newlog = getService().setXincoCoreLog(newlog, loggedUser);
        if (newlog == null) {
            Logger.getLogger(Xinco.class.getSimpleName()).severe("Unable to create new log entry!");
        } else {
            data.getXincoCoreLogs().add(newlog);
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
                        //Call the web service
                        if (getService().setXincoCoreNode(tempNode, loggedUser) == null) {
                            getMainWindow().showNotification(getResource().getString("error.accessdenied"),
                                    getResource().getString("error.folder.sufficientrights"),
                                    Window.Notification.TYPE_ERROR_MESSAGE);
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
        //Refresh and highlight current selection
        String selectedId = xincoTree.getValue() == null ? null : xincoTree.getValue().toString();
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
        root.getItemProperty("caption").setValue(xcns.getDesignation() == null
                ? "" : xcns.getDesignation());
        addChildren(id);
        //Expand root node
        for (Iterator<?> it = xincoTreeContainer.rootItemIds().iterator(); it.hasNext();) {
            xincoTree.expandItem(it.next());
        }
        if (selectedId != null) {
            xincoTree.setValue(selectedId);
        } else {
            xincoTree.setValue("node-1");
        }
    }

    private void processTreeSelection(String source) {
        try {
            updateMenu();
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
        XincoCoreACE tempAce = new XincoCoreACE();
        getXincoTable().setVisible(getLoggedUser() != null);
        getXincoTable().requestRepaint();
        xeSplitPanel.setSplitPosition(getLoggedUser() != null ? 25 : 100, Sizeable.UNITS_PERCENTAGE);
        if (getXincoTable().isVisible()) {
            //Clear table
            xincoTable.removeAllItems();
            int i = 1;
            String value = "", header;
            if (source != null && source.startsWith("node")) {
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
                xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                xincoTable.addItem(new Object[]{getResource().getString("general.id"),
                            new com.vaadin.ui.Label("" + xcns.getId())}, i++);
                xincoTable.addItem(new Object[]{getResource().getString("general.designation"),
                            new com.vaadin.ui.Label(xcns.getDesignation())}, i++);
                xincoTable.addItem(new Object[]{getResource().getString("general.language"),
                            new com.vaadin.ui.Label(getResource().getString(xcns.getXincoCoreLanguage().getDesignation()) + " ("
                            + xcns.getXincoCoreLanguage().getSign()
                            + ")")}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
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
                    value += "X";
                } else {
                    value += "-";
                }
                if (tempAce.isAdminPermission()) {
                    value += "A";
                } else {
                    value += "-";
                }
                value += "]";
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.accessrights"),
                            new com.vaadin.ui.Label(value)}, i++);
                xincoTable.addItem(new Object[]{"\u00a0",
                            new com.vaadin.ui.Label()}, i++);
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
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.status"),
                            new com.vaadin.ui.Label(value)}, i++);
            } else if (source != null && source.startsWith("data")) {
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
                            getResource().getString("general.id"),
                            new com.vaadin.ui.Label("" + temp.getId())}, i++);
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.designation"),
                            new com.vaadin.ui.Label(temp.getDesignation())}, i++);
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.language"),
                            new com.vaadin.ui.Label(getResource().getString(temp.getXincoCoreLanguage().getDesignation())
                            + " ("
                            + temp.getXincoCoreLanguage().getSign()
                            + ")")}, i++);
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.datatype"),
                            new com.vaadin.ui.Label(getResource().getString(temp.getXincoCoreDataType().getDesignation())
                            + " ("
                            + getResource().getString(temp.getXincoCoreDataType().getDescription())
                            + ")")}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
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
                            new com.vaadin.ui.Label("[" + value + "]")}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
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
                            getResource().getString("general.status"),
                            new com.vaadin.ui.Label(value)}, i++);
                xincoTable.addItem(new Object[]{"\u00a0",
                            new com.vaadin.ui.Label()}, i++);
                xincoTable.addItem(new Object[]{
                            getResource().getString("general.typespecificattributes"),
                            new com.vaadin.ui.Label()}, i++);
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
                        Link link = new Link(value, new ExternalResource(value));
                        link.setTargetName("_blank");
                        link.setTargetBorder(Link.TARGET_BORDER_NONE);
                        try {
                            xincoTable.addItem(new Object[]{header, header.equals("URL")
                                        && XincoSettingServer.getSetting(loggedUser, "setting.allowoutsidelinks", true).isBoolValue()
                                        ? link : new com.vaadin.ui.Label(value)}, i++);
                        } catch (XincoException ex) {
                            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    header = getResource().getString("error.accessdenied");
                    value = getResource().getString("error.content.sufficientrights");
                    xincoTable.addItem(new Object[]{header, new com.vaadin.ui.Label(value)}, i++);
                }
                xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                header = getResource().getString("general.logslastfirst");
                value = "";
                xincoTable.addItem(new Object[]{header, new com.vaadin.ui.Label(value)}, i++);
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
                    xincoTable.addItem(new Object[]{header, new com.vaadin.ui.Label(value)}, i++);
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
                        xincoTable.addItem(new Object[]{header, new com.vaadin.ui.Label(value)}, i++);
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

    public XincoWebService getService() {
        if (service == null) {
            service = new XincoWebService();
        }
        return service;
    }

    private void showLuceneSearchWindow() {
        LuceneSearchWindow search = new LuceneSearchWindow(this);
        search.center();
        getMainWindow().addWindow(search);
    }
}
