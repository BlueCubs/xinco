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
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
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

    @Override
    public void init() {
        //Switch to Xinco theme
        setTheme("xinco");
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
                xincoTree.setDebugId("xinco_tree");
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
            XincoCoreData temp = it.next();
            XincoCoreDataProperty dataProperty = new XincoCoreDataProperty(temp);
            //Add childen data
//            Item item = 
            xincoTree.addItem(dataProperty);
            // Set it to be a child.
            xincoTree.setParent(dataProperty, parent);
            // Set as leaves
            xincoTree.setChildrenAllowed(dataProperty, false);
            //TODO: Add icons to files, not a critical feature
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
        //Menus always enabled
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
        //Exclusive menus for Nodes
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

            repo.addItem(getResource().getString("menu.repository.adddata"),
                    null,//Icon 
                    new com.vaadin.ui.MenuBar.Command() {

                @Override
                public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                    //Show the Data Folder Dialog window
                    showDataDialog(true);
                }
            });

            repo.addItem(getResource().getString("menu.repository.adddatastructure"),
                    null,//Icon 
                    new com.vaadin.ui.MenuBar.Command() {

                @Override
                public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                    //Show the Data Structure Dialog window
                    final Window w = new Window("Mass import");
                    MultiFileUpload fileUpload = new MultiFileUpload() {

                        @Override
                        protected void handleFile(File file, String fileName,
                                String mimeType, long length) {
                            getMainWindow().showNotification(
                                    getResource().getString("window.massiveimport.progress"),
                                    Notification.TYPE_WARNING_MESSAGE);
                            fileToLoad = file;
                            getMainWindow().removeWindow(w);
                        }
                    };
                    fileUpload.setWidth("600px");
                    w.addComponent(fileUpload);
                    w.setModal(true);
                    w.center();
                    getMainWindow().addWindow(w);
                }
            });
        }
        //Exclusive menus for Data
        if (xincoTree.getValue() instanceof XincoCoreDataProperty) {
        }
        //Hide it if empty
        menuBar.setVisible(!menuBar.getItems().isEmpty());
    }

    private void loadFile(File file) throws XincoException, MalformedURLException, IOException {
        loadFile(file, file.getName());
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
        data.setXincoCoreNodeId(((XincoCoreNode) ((XincoCoreNodeProperty) xincoTree.getValue()).getValue()).getId());
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
        if (loggedUser == null) {
            //TODO: Prompt for login
        } else if (xat != null) {
            xat.getActivityTimer().restart();
        }
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
                    data.setXincoCoreNodeId(((XincoCoreNode) ((XincoCoreNodeProperty) xincoTree.getValue()).getValue()).getId());
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
        ddManager = new DataDialogManager();
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
                //Cancelled so roll back everything done in database
                HashMap parameters = new HashMap();
                parameters.put("id", data.getId());
                if (data != null) {
                    XincoCoreDataServer.removeFromDB(loggedUser.getId(), data.getId());
                }
                //Remove the file from the repository as well
                closeWizard();
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void finishWizard() {
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
            try {
                getMainWindow().removeWindow(wizardWindow);
                getMainWindow().setCaption(getResource().getString("general.clienttitle") + " - "
                        + getResource().getString("general.version") + " "
                        + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.high").getIntValue() + "."
                        + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.mid").getIntValue() + "."
                        + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.low").getIntValue() + " "
                        + XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.postfix").getStringValue());
                discard();
                try {
                    //Show changes in tree
                    refresh();
                } catch (XincoException ex) {
                    Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (XincoException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(getResource().getString("general.save"), form, "commit");
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
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
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
            XincoCoreData temp = ((XincoCoreData) ((XincoCoreDataProperty) xincoTree.getValue()).getValue());
            try {
                //Load from database
                temp = getService().getXincoPort().getXincoCoreData(temp, loggedUser);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
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
            xincoTable.addItem(new Object[]{"", ""}, i++);
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
            xincoTable.addItem(new Object[]{"", ""}, i++);
            xincoTable.addItem(new Object[]{"", ""}, i++);
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
