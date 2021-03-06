/**
 * Copyright 2012 blueCubs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 *
 * Name: XincoDBManager
 *
 * Description: server-side database manager
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * $Date$
 * $Author$
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.OPCode;
import static com.bluecubs.xinco.core.OPCode.CHECKIN;
import static com.bluecubs.xinco.core.OPCode.CHECKOUT;
import static com.bluecubs.xinco.core.OPCode.CHECKOUT_UNDONE;
import static com.bluecubs.xinco.core.OPCode.COMMENT;
import static com.bluecubs.xinco.core.OPCode.CREATION;
import static com.bluecubs.xinco.core.OPCode.DATA_MOVE;
import static com.bluecubs.xinco.core.OPCode.LOCK_COMMENT;
import static com.bluecubs.xinco.core.OPCode.PUBLISH_COMMENT;
import static com.bluecubs.xinco.core.OPCode.getOPCode;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import com.bluecubs.xinco.core.server.XincoCoreACEServer;
import static com.bluecubs.xinco.core.server.XincoCoreACEServer.checkAccess;
import com.bluecubs.xinco.core.server.XincoCoreDataHasDependencyServer;
import static com.bluecubs.xinco.core.server.XincoCoreDataHasDependencyServer.getRenderings;
import static com.bluecubs.xinco.core.server.XincoCoreDataHasDependencyServer.isRendering;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.getCurrentVersion;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.getOwnerID;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.removeFromDB;
import com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServer;
import static com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServer.deleteFromDB;
import com.bluecubs.xinco.core.server.XincoCoreDataTypeServer;
import static com.bluecubs.xinco.core.server.XincoCoreDataTypeServer.getXincoCoreDataType;
import static com.bluecubs.xinco.core.server.XincoCoreDataTypeServer.getXincoCoreDataTypes;
import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import static com.bluecubs.xinco.core.server.XincoCoreGroupServer.getXincoCoreGroups;
import com.bluecubs.xinco.core.server.XincoCoreLanguageServer;
import static com.bluecubs.xinco.core.server.XincoCoreLanguageServer.deleteFromDB;
import static com.bluecubs.xinco.core.server.XincoCoreLanguageServer.getXincoCoreLanguages;
import static com.bluecubs.xinco.core.server.XincoCoreLanguageServer.isLanguageUsed;
import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import static com.bluecubs.xinco.core.server.XincoCoreUserServer.getXincoCoreUsers;
import static com.bluecubs.xinco.core.server.XincoCoreUserServer.validCredentials;
import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.displayDBStatus;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.getState;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.reload;
import com.bluecubs.xinco.core.server.XincoDependencyTypeServer;
import static com.bluecubs.xinco.core.server.XincoSettingServer.getSetting;
import static com.bluecubs.xinco.core.server.db.DBState.VALID;
import static com.bluecubs.xinco.core.server.index.XincoIndexer.indexXincoCoreData;
import static com.bluecubs.xinco.core.server.index.XincoIndexer.optimizeIndex;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreGroupJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLogJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreUserHasXincoCoreGroupJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreUserJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.service.XincoAddAttribute;
import com.bluecubs.xinco.core.server.service.XincoCoreACE;
import com.bluecubs.xinco.core.server.service.XincoCoreData;
import com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.server.service.XincoCoreGroup;
import com.bluecubs.xinco.core.server.service.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.service.XincoCoreLog;
import com.bluecubs.xinco.core.server.service.XincoCoreNode;
import com.bluecubs.xinco.core.server.service.XincoCoreUser;
import com.bluecubs.xinco.core.server.service.XincoVersion;
import com.bluecubs.xinco.core.server.service.XincoWebService;
import static com.bluecubs.xinco.core.server.vaadin.XincoMenuItemManager.addItem;
import static com.bluecubs.xinco.core.server.vaadin.XincoMenuItemManager.updateMenuBar;
import com.bluecubs.xinco.core.server.vaadin.custom.VersionSelector;
import com.bluecubs.xinco.core.server.vaadin.setting.SettingAdminWindow;
import com.bluecubs.xinco.core.server.vaadin.wizard.WizardStep;
import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;
import com.bluecubs.xinco.core.server.vaadin.wizard.event.WizardCancelledEvent;
import com.bluecubs.xinco.core.server.vaadin.wizard.event.WizardCompletedEvent;
import com.bluecubs.xinco.core.server.vaadin.wizard.event.WizardProgressListener;
import com.bluecubs.xinco.core.server.vaadin.wizard.event.WizardStepActivationEvent;
import com.bluecubs.xinco.core.server.vaadin.wizard.event.WizardStepSetChangedEvent;
import static com.bluecubs.xinco.tools.Tool.addDefaultAddAttributes;
import static com.bluecubs.xinco.tools.Tool.getImageDim;
import com.bluecubs.xinco.tools.XincoFileIconManager;
import com.vaadin.Application;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import static com.vaadin.event.MouseEvents.ClickEvent.BUTTON_LEFT;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.Or;
import com.vaadin.terminal.*;
import static com.vaadin.terminal.Sizeable.UNITS_INCH;
import static com.vaadin.terminal.Sizeable.UNITS_PERCENTAGE;
import static com.vaadin.terminal.Sizeable.UNITS_PIXELS;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.terminal.gwt.client.ui.dd.VerticalDropLocation;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.*;
import static com.vaadin.ui.AbstractSelect.ITEM_CAPTION_MODE_PROPERTY;
import com.vaadin.ui.AbstractSelect.VerticalLocationIs;
import static com.vaadin.ui.Embedded.TYPE_IMAGE;
import static com.vaadin.ui.Link.TARGET_BORDER_NONE;
import com.vaadin.ui.Tree.ExpandEvent;
import static com.vaadin.ui.Tree.TreeDragMode.NODE;
import com.vaadin.ui.Tree.TreeTargetDetails;
import com.vaadin.ui.Upload.SucceededEvent;
import static com.vaadin.ui.Window.Notification.TYPE_ERROR_MESSAGE;
import static com.vaadin.ui.Window.Notification.TYPE_HUMANIZED_MESSAGE;
import static com.vaadin.ui.Window.Notification.TYPE_TRAY_NOTIFICATION;
import static com.vaadin.ui.Window.Notification.TYPE_WARNING_MESSAGE;
import com.vaadin.ui.Window.ResizeEvent;
import java.awt.*;
import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.io.*;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static java.lang.System.currentTimeMillis;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.*;
import static java.util.Arrays.asList;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DST_OFFSET;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.ZONE_OFFSET;
import static java.util.Collections.addAll;
import static java.util.Locale.getDefault;
import java.util.Map.Entry;
import static java.util.ResourceBundle.getBundle;
import static java.util.UUID.randomUUID;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import static javax.imageio.ImageIO.write;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import static javax.persistence.metamodel.Attribute.PersistentAttributeType.BASIC;
import javax.persistence.metamodel.EntityType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.Icon;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import static javax.xml.datatype.DatatypeFactory.newInstance;
import javax.xml.datatype.XMLGregorianCalendar;
import org.vaadin.easyuploads.MultiFileUpload;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea;

/**
 *
 * @author Javier A. Ortiz Bultronjavier.ortiz.78@gmail.com
 */
public class Xinco extends Application implements HttpServletRequestListener {

    private static final Logger LOG = getLogger(Xinco.class.getName());
    private static final long serialVersionUID = 1L;
    private static final ThreadLocal<Xinco> LOCAL_THREAD = new ThreadLocal<Xinco>();
    private XincoVersion xincoVersion = null;
    private ResourceBundle xerb
            = getBundle("com.bluecubs.xinco.messages.XincoMessages", getDefault());
    private XincoCoreUserServer loggedUser = null;
    private final XincoFileIconManager xfm = new XincoFileIconManager();
    //Table linking displayed item with it's id
    private Tree xincoTree = null;
    private Table xincoTable = null;
    private final com.vaadin.ui.MenuBar menuBar = new com.vaadin.ui.MenuBar();
    private Item root;
    private XincoWebService service;
    private final com.vaadin.ui.Window wizardWindow = new com.vaadin.ui.Window();
    private DataDialog dataDialog;
    private DataTypeDialog dataTypeDialog;
    private AddAttributeDialog attrDialog;
    private DataDialogManager ddManager;
    private ArchiveDialog archDialog;
    private XincoCoreData data = new XincoCoreData();
    private XincoActivityTimer xat = null;
    private File fileToLoad;
    private String fileName;                    // Original file name
    private final boolean renderingSupportEnabled = false;
    private String version;
    private HorizontalSplitPanel xeSplitPanel;
    private com.vaadin.ui.Button login;
    private com.vaadin.ui.Button logout;
    private com.vaadin.ui.Button profile;
    private WizardStep fileStep;
    private final ThemeResource smallIcon
            = new ThemeResource("img/blueCubsIcon16x16.GIF");
    private HierarchicalContainer xincoTreeContainer;
    private com.vaadin.ui.Panel adminPanel;
    private Embedded icon;
    private Locale locale = getDefault();
    private com.vaadin.ui.Button userAdmin;
    private com.vaadin.ui.Button groupAdmin;
    private com.vaadin.ui.Button langAdmin;
    private com.vaadin.ui.Button attrAdmin;
    private com.vaadin.ui.Button trashAdmin;
    private com.vaadin.ui.Button indexAdmin;
    private com.vaadin.ui.Button auditAdmin;
    private com.vaadin.ui.Button settingAdmin;
    private com.vaadin.ui.Panel accountPanel;
    private Accordion menu;

    @Override
    public String getVersion() {
        return version;
    }

    // @return the current application instance
    public static Xinco getInstance() {
        return LOCAL_THREAD.get();
    }

    // Set the current application instance
    public static void setInstance(Xinco application) {
        LOCAL_THREAD.set(application);
    }

    static Image iconToImage(Icon icon) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        GraphicsEnvironment ge
                = getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        Graphics2D g = image.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return image;
    }

    public static boolean removeDirectory(File directory) {
        boolean result = false;
        if (directory == null) {
            result = false;
        } else if (!directory.exists()) {
            result = true;
        } else if (!directory.isDirectory()) {
            result = false;
        }

        if (directory != null) {
            String[] list = directory.list();

            // Some JVMs return null for File.list() when the
            // directory is empty.
            if (list != null) {
                for (String list1 : list) {
                    File entry = new File(directory, list1);
                    if (entry.isDirectory()) {
                        if (!removeDirectory(entry)) {
                            result = false;
                            break;
                        }
                    } else {
                        if (!entry.delete()) {
                            result = false;
                            break;
                        }
                    }
                }
            }
            result = result && directory.delete();
        }
        return result;
    }

    @Override
    public void onRequestStart(HttpServletRequest request,
            HttpServletResponse response) {
        setInstance(this);
    }

    @Override
    public void onRequestEnd(HttpServletRequest request,
            HttpServletResponse response) {
        LOCAL_THREAD.remove();
    }

    @Override
    public void init() {
        //Switch to Xinco theme
        setTheme("xinco");
        setInstance(this);
        try {
            reload();
            com.vaadin.ui.Window window = new com.vaadin.ui.Window("Xinco");
            setMainWindow(window);
            //Build the window
            getMainWindow().removeAllComponents();
            if (xat == null) {
                //5 mins
                xat = new XincoActivityTimer(5);
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
            xincoVersion = new XincoVersion();
            if (getState().equals(VALID)) {
                try {
                    xincoVersion.setVersionHigh(getSetting(
                            new XincoCoreUserServer(1),
                            "version.high").getIntValue());
                    xincoVersion.setVersionMid(getSetting(
                            new XincoCoreUserServer(1),
                            "version.mid").getIntValue());
                    xincoVersion.setVersionLow(getSetting(
                            new XincoCoreUserServer(1),
                            "version.low").getIntValue());
                    xincoVersion.setVersionPostfix(getSetting(
                            new XincoCoreUserServer(1),
                            "version.postfix").getStringValue());
                } catch (com.bluecubs.xinco.core.XincoException ex) {
                    LOG.log(SEVERE, null, ex);
                }
                version = getSetting(
                        new XincoCoreUserServer(1),
                        "version.high").getIntValue() + "."
                        + getSetting(
                                new XincoCoreUserServer(1),
                                "version.mid").getIntValue() + "."
                        + getSetting(
                                new XincoCoreUserServer(1),
                                "version.low").getIntValue() + " "
                        + getSetting(
                                new XincoCoreUserServer(1),
                                "version.postfix").getStringValue();
                icon = new Embedded("Xinco - "
                        + getInstance().getResource()
                                .getString("general.version") + " "
                        + version, new ThemeResource("img/blueCubsSmall.gif"));
                icon.setType(TYPE_IMAGE);
                login = new com.vaadin.ui.Button(
                        getInstance().getResource().getString("general.login"),
                        new com.vaadin.ui.Button.ClickListener() {
                    @Override
                    public void buttonClick(
                            com.vaadin.ui.Button.ClickEvent event) {
                        showLoginDialog();
                    }
                });
                login.setDebugId("login");
                logout = new com.vaadin.ui.Button(
                        getInstance().getResource().getString("general.logout"),
                        new com.vaadin.ui.Button.ClickListener() {
                    @Override
                    public void buttonClick(
                            com.vaadin.ui.Button.ClickEvent event) {
                        loggedUser = null;
                        try {
                            showMainWindow();
                            refresh();
                        } catch (XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                });
                profile = new com.vaadin.ui.Button(
                        getInstance().getResource().getString(
                                "message.admin.userProfile"),
                        new com.vaadin.ui.Button.ClickListener() {
                    @Override
                    public void buttonClick(
                            com.vaadin.ui.Button.ClickEvent event) {
                        showUserAdminWindow(false);
                    }
                });
                if (CONFIG.isGuessLanguage()) {
                    // Use the locale from the request as default.
                    // Login uses this setting later.
                    setLocale(((WebApplicationContext) getContext())
                            .getBrowser().getLocale());
                    showMainWindow();
                } else {
                    showLanguageSelection();
                }
            } else {
                //Trigger the error window below
                throw new XincoException("Database is not in valid state! "
                        + "Unable to proceed.");
            }
            //Add parameter handling for parameter parsing
            window.addParameterHandler(new ParameterHandler() {
                @Override
                public void handleParameters(Map<String, String[]> parameters) {
                    if (!parameters.isEmpty()) {
                        LOG.info("Parameters:");
                        for (Entry<String, String[]> e : parameters.entrySet()) {
                            LOG.log(FINE, "Name: {0}, Values:", e.getKey());
                            for (String val : e.getValue()) {
                                LOG.log(FINE, "{0}", val);
                            }
                            if (e.getKey().equals("dataId")) {
                                //A link to a specific data was requested.
                                //Download the file.
                                Integer dataId = valueOf(e.getValue()[0]);
                                XincoCoreDataServer data;
                                try {
                                    data = new XincoCoreDataServer(dataId);
                                    switch (data.getXincoCoreDataType().getId()) {
                                        case 1:
                                            try {
                                                //Download a file
                                                downloadFile(data);
                                                setMainWindow(new com.vaadin.ui.Window("Xinco"));
                                            } catch (MalformedURLException ex) {
                                                throw new XincoException(ex);
                                            }
                                            break;
                                        default:
                                            getMainWindow().showNotification(getInstance().getResource().getString(
                                                    "general.error") + " Not a file!", TYPE_ERROR_MESSAGE);
                                    }
                                } catch (XincoException xe) {
                                    getMainWindow().showNotification(getInstance().getResource().getString(
                                            "general.error") + xe, TYPE_ERROR_MESSAGE);
                                }
                            } else if (e.getKey().equals("folderId")) {
                                //TODO: Show the path on the tree
                                getMainWindow().showNotification("We should search the folder here!", TYPE_HUMANIZED_MESSAGE);
                            }
                        }
                    } else {
                        LOG.fine("No parameters provided!");
                    }
                }
            });
        } catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
            setMainWindow(new com.vaadin.ui.Window("Error"));
            getMainWindow().addComponent(new com.vaadin.ui.Label(
                    "Unable to start application, please contact your "
                    + "administrator!\n"
                    + displayDBStatus()));
            LOG.log(SEVERE,
                    "An uncaught exception occurred: ", ex);
        }
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
        xerb = getBundle(
                "com.bluecubs.xinco.messages.XincoMessages",
                getInstance().getLocale());
        if (login != null) {
            login.setCaption(getInstance().getResource()
                    .getString("general.login"));
        }
        if (logout != null) {
            logout.setCaption(getInstance().getResource()
                    .getString("general.logout"));
        }
        if (profile != null) {
            profile.setCaption(getInstance().getResource()
                    .getString("message.admin.userProfile"));
        }
        fileStep = new WizardStep() {
            final UploadManager um = new UploadManager();
            final Upload upload = new Upload(getInstance().getResource()
                    .getString("general.file.select"), um);
            final Upload.SucceededListener listener
                    = new Upload.SucceededListener() {
                @Override
                public void uploadSucceeded(SucceededEvent event) {
                    if (upload != null) {
                        getMainWindow().showNotification(getInstance().getResource().getString(
                                "datawizard.fileuploadsuccess"), TYPE_HUMANIZED_MESSAGE);
                    }
                }
            };

            @Override
            public String getCaption() {
                return getInstance().getResource()
                        .getString("general.file.select");
            }

            @Override
            public com.vaadin.ui.Component getContent() {
                upload.addListener((Upload.SucceededListener) um);
                upload.addListener(listener);
                upload.addListener((Upload.FailedListener) um);
                return upload;
            }

            @Override
            public boolean onAdvance() {
                if (!um.isSuccess()) {
                    getMainWindow().showNotification(getInstance().getResource()
                            .getString("message.missing.file"), TYPE_ERROR_MESSAGE);
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
        //Update Side Menu
        ResourceBundle rb = getInstance().getResource();
        if (userAdmin != null) {
            userAdmin.setCaption(rb.getString("message.admin.userAdmin"));
        }
        if (groupAdmin != null) {
            groupAdmin.setCaption(rb.getString("message.admin.groupAdmin"));
        }
        if (langAdmin != null) {
            langAdmin.setCaption(rb.getString("message.admin.language"));
        }
        if (attrAdmin != null) {
            attrAdmin.setCaption(rb.getString("message.admin.attribute"));
        }
        if (trashAdmin != null) {
            trashAdmin.setCaption(rb.getString("message.admin.trash"));
        }
        if (indexAdmin != null) {
            indexAdmin.setCaption(rb.getString("general.audit.menu"));
        }
        if (auditAdmin != null) {
            auditAdmin.setCaption(rb.getString("general.audit.menu"));
        }
        if (settingAdmin != null) {
            settingAdmin.setCaption(rb.getString("message.admin.settingAdmin"));
        }
        if (menu != null && accountPanel != null) {
            menu.getTab(accountPanel).setCaption(
                    rb.getString("window.connection.profile"));
        }
    }

    private void addHeader() {
        final Select languages = getLanguageOptions();
        final SimplifiedSearchComponent ssc = new SimplifiedSearchComponent();
        languages.setImmediate(true);
        languages.addListener(new ValueChangeListener() {
            @Override
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
                            loc = new Locale(locales[0], locales[1],
                                    locales[2]);
                            break;
                        default:
                            loc = getDefault();
                    }
                } catch (Exception e) {
                    loc = getDefault();
                }
                setLocale(loc);
                languages.setCaption(getInstance().getResource()
                        .getString("general.language") + ":");
                ssc.refresh();
            }
        });
        languages.setCaption(getInstance().getResource()
                .getString("general.language") + ":");
        languages.setValue(getLocale().getLanguage());
        com.vaadin.ui.Label title
                = new com.vaadin.ui.Label(getSetting(
                        new XincoCoreUserServer(1),
                        "general.title").getStringValue());
        getMainWindow().addComponent(title);
        getMainWindow().addComponent(languages);
        getMainWindow().addComponent(ssc);
    }

    private void showMainWindow() {
        getMainWindow().removeAllComponents();
        getMainWindow().addListener(new MouseEvents.ClickListener() {
            @Override
            public void click(ClickEvent event) {
                //Do nothing. Based on https://vaadin.com/es/forum/-/message_boards/view_message/359139
            }
        });
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        // Put two components in the container.
        splitPanel.setFirstComponent(getSideMenu());
        splitPanel.setSecondComponent(getXincoExplorer());
        splitPanel.setHeight(700, UNITS_PIXELS);
        splitPanel.setSplitPosition(20, UNITS_PERCENTAGE);
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
            LOG.log(FINE,
                    "Converting code from: {0} to {1}!",
                    new Object[]{code, newCode});
            code = newCode;
        }
        LOG.log(FINE,
                "Requested icon for code: {0}", code);
        WebApplicationContext context = (WebApplicationContext) getContext();
        File iconsFolder = new File(context.getHttpSession().getServletContext()
                .getRealPath(
                        "/VAADIN/themes/xinco") + System.getProperty("file.separator")
                + "icons"
                + System.getProperty("file.separator") + "flags");
        File tempIcon = new File(iconsFolder.getAbsolutePath()
                + System.getProperty("file.separator") + code.toLowerCase()
                + ".png");
        FileResource resource = null;
        if (tempIcon.exists()) {
            LOG.log(FINE,
                    "Found icon for code: {0}!", code);
            resource = new FileResource(tempIcon, Xinco.this);
        } else {
            LOG.log(FINE,
                    "Unable to find icon for: {0}", code);
        }
        return resource;
    }

    protected FileResource getIcon(String extension) throws IOException {
        WebApplicationContext context = (WebApplicationContext) getContext();
        File iconsFolder
                = new File(context.getHttpSession().getServletContext()
                        .getRealPath(
                                "/VAADIN/themes/xinco") + System.getProperty("file.separator")
                        + "icons");
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
                        image.getHeight(null), TYPE_INT_RGB);
                Graphics2D g = buffered.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                write(buffered, "PNG", tempIconFile);
            } else {
                return null;
            }
        }
        FileResource resource
                = new FileResource(tempIconFile, Xinco.this);
        return resource;
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
        xeSplitPanel.setHeight(700, UNITS_PIXELS);
        xeSplitPanel.setSplitPosition(100, UNITS_PERCENTAGE);
        //Hide details by default, user needs to log in for some features.
        getXincoTable().setVisible(false);
        panel.addComponent(menuBar);
        menuBar.setSizeFull();
        panel.addComponent(xeSplitPanel);
        try {
            updateMenu();
        } catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
        }
        return panel;
    }

    public Tree getXincoTree() {
        if (xincoTree == null) {
            try {
                xincoTreeContainer = new HierarchicalContainer();
                xincoTreeContainer.addContainerProperty("caption",
                        String.class, null);
                //Add icon support
                xincoTreeContainer.addContainerProperty("icon",
                        Resource.class, null);
                xincoTree = new Tree("menu.repository");
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
                        if (event.getButton() == BUTTON_LEFT
                                && event.isDoubleClick()
                                && xincoTree.getValue() != null
                                && xincoTree.getValue().toString()
                                        .startsWith("data")) {
                            try {
                                downloadFile();
                            } catch (MalformedURLException | XincoException ex) {
                                LOG.log(SEVERE, null, ex);
                            }
                        }
                        if (xincoTree.getValue() != null) {
                            processTreeSelection(xincoTree.getValue().toString());
                        }
                    }
                });
                xincoTree.setImmediate(true);
                xincoTree.setItemCaptionPropertyId("caption");
                xincoTree.setItemCaptionMode(ITEM_CAPTION_MODE_PROPERTY);
                //Dra and drop support
                //Set the tree in drag source mode
                xincoTree.setDragMode(NODE);
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

                        TreeTargetDetails target
                                = (TreeTargetDetails) event.getTargetDetails();

                        // Get ids of the dragged item and the target item
                        Object sourceItemId = t.getData("itemId");
                        Object targetItemId = target.getItemIdOver();

                        XincoCoreNodeServer targetN
                                = new XincoCoreNodeServer(valueOf(
                                        targetItemId.toString().substring(
                                                targetItemId.toString().indexOf('-') + 1)));

                        // On which side of the target the item was dropped
                        VerticalDropLocation location = target.getDropLocation();

                        // Drop right on an item -> make it a child
                        if (checkAccess(loggedUser,
                                (ArrayList) (targetN).getXincoCoreAcl())
                                .isWritePermission()
                                && location == VerticalDropLocation.MIDDLE) {
                            try {
                                //Now update things in the database
                                if (sourceItemId.toString().startsWith("data")
                                        && targetItemId.toString()
                                                .startsWith("node")) {
                                    XincoCoreDataServer source
                                            = new XincoCoreDataServer(
                                                    valueOf(sourceItemId
                                                            .toString().substring(
                                                                    sourceItemId.toString().indexOf('-')
                                                                    + 1)));
                                    // Get a reason for the move:
                                    source.setXincoCoreNodeId(targetN.getId());
                                    showCommentDataDialog(source, DATA_MOVE);
                                }
                                if (sourceItemId.toString().startsWith("node")
                                        && targetItemId.toString().startsWith("node")) {
                                    XincoCoreNodeServer source
                                            = new XincoCoreNodeServer(
                                                    valueOf(sourceItemId
                                                            .toString().substring(
                                                                    sourceItemId.toString().indexOf('-')
                                                                    + 1)));
                                    source.setXincoCoreNodeId(targetN.getId());
                                    source.write2DB();
                                }
                                xincoTree.setParent(sourceItemId, targetItemId);
                                try {
                                    refresh();
                                } catch (XincoException ex) {
                                    LOG.log(SEVERE, null, ex);
                                }
                            } catch (XincoException ex) {
                                LOG.log(SEVERE, null, ex);
                            }
                        }
                    }

                    @Override
                    public AcceptCriterion getAcceptCriterion() {
                        //Accepts drops only on nodes that allow children,
                        //but between all nodes
                        return new Or(Tree.TargetItemAllowsChildren.get(),
                                new Not(VerticalLocationIs.MIDDLE));
                    }
                });
            } catch (XincoException ex) {
                LOG.log(SEVERE, null, ex);
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
            xincoTable.addContainerProperty(getInstance().getResource()
                    .getString("window.repository.table.attribute"),
                    String.class, null);
            xincoTable.addContainerProperty(getInstance().getResource()
                    .getString("window.repository.table.details"),
                    com.vaadin.ui.Component.class, null);
            // Send changes in selection immediately to server.
            xincoTable.setImmediate(true);
            //Disable sorting
            xincoTable.setSortDisabled(true);
            xincoTable.setPageLength(20);
            xincoTable.setWidth(100, UNITS_PERCENTAGE);
            xincoTable.addStyleName("striped");
        }
        return xincoTable;
    }

    private void addChildren(String parent) throws XincoException {
        addChildrenNodes(parent);
        addChildrenData(parent);
    }

    private void addChildrenNodes(String parent) throws XincoException {
        XincoCoreNodeServer xcns
                = new XincoCoreNodeServer(valueOf(parent
                        .substring(parent.indexOf('-') + 1)));
        for (XincoCoreNode subnode : xcns.getXincoCoreNodes()) {
            LOG.log(FINE, "Checking permissions for: {0}",
                    subnode.getDesignation());
            XincoCoreACE access = checkAccess(loggedUser,
                    subnode.getXincoCoreAcl());
            if (access.isAdminPermission() || access.isReadPermission()) {
                String id = "node-" + subnode.getId();
                Item item = xincoTreeContainer.addItem(id);
                item.getItemProperty("caption").setValue(subnode.getDesignation());
                // Set it to be a child.
                xincoTreeContainer.setParent(id, parent);
                //Allow to have children
                xincoTreeContainer.setChildrenAllowed(id, true);
            }
        }
    }

    private void addChildrenData(String parent) throws XincoException {
        XincoCoreNodeServer xcns = new XincoCoreNodeServer(valueOf(
                parent.substring(parent.indexOf('-') + 1)));
        for (XincoCoreData temp : xcns.getXincoCoreData()) {
            LOG.log(FINE, "Checking permissions for: {0}",
                    temp.getDesignation());
            XincoCoreACE access = checkAccess(loggedUser,
                    temp.getXincoCoreAcl());
            if (access.isAdminPermission() || access.isReadPermission()) {
                //Only show files that are not renderings
                if (!isRendering(temp.getId())) {
                    //Add childen data
                    String id = "data-" + temp.getId();
                    Item item = xincoTreeContainer.addItem(id);
                    item.getItemProperty("caption").setValue(temp.getDesignation());
                    // Set it to be a child.
                    xincoTreeContainer.setParent(id, parent);
                    // Set as leaves
                    xincoTreeContainer.setChildrenAllowed(id, false);
                    if (!temp.getXincoAddAttributes().isEmpty()) {
                        String name = temp.getXincoAddAttributes().get(0)
                                .getAttribVarchar();
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
                                    item.getItemProperty("icon").setValue(
                                            new ThemeResource("icons/contact.gif"));
                                    break;
                                default:
                                    throw new RuntimeException("Invalid Data Type: "
                                            + temp.getXincoCoreDataType().getId());
                            }
                            if (icon1 != null) {
                                item.getItemProperty("icon").setValue(icon1);
                            }
                        } catch (IOException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    protected void updateMenu() throws XincoException {
        updateMenuBar(menuBar);
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
            final UploadManager um = new UploadManager();
            final Upload upload = new Upload(getInstance().getResource()
                    .getString("general.file.select"), um);
            XincoCoreDataServer temp = new XincoCoreDataServer(
                    valueOf(xincoTree.getValue().toString().substring(
                            xincoTree.getValue().toString().indexOf('-') + 1)));
            //Check in file
            newlog.setOpCode(CHECKIN.ordinal() + 1);
            newlog.setOpDescription(getInstance().getResource().getString(getOPCode(newlog.getOpCode()).getName()));
            newlog.setXincoCoreUserId(loggedUser.getId());
            newlog.setXincoCoreDataId(temp.getId());
            newlog.setVersion(((XincoCoreLog) temp.getXincoCoreLogs().get(
                    temp.getXincoCoreLogs().size() - 1)).getVersion());
            newlog.setOpDescription(newlog.getOpDescription() + " ("
                    + getInstance().getResource().getString("general.user")
                    + ": " + loggedUser.getUsername() + ")");
            //save log to server
            XincoCoreLog tempLog = getService().setXincoCoreLog(newlog, loggedUser);
            if (tempLog != null) {
                temp.getXincoCoreLogs().add(tempLog);
            }
            final int log_index = temp.getXincoCoreLogs().size() - 1;
            final VersionSelector versionSelector
                    = new VersionSelector(getInstance().getResource()
                            .getString("general.version"),
                            ((XincoCoreLog) temp.getXincoCoreLogs().get(log_index))
                                    .getVersion());
            versionSelector.increaseHigh();
            final Upload.SucceededListener listener
                    = new Upload.SucceededListener() {
                @Override
                public void uploadSucceeded(SucceededEvent event) {
                    if (upload != null) {
                        upload.setEnabled(false);
                        getMainWindow().showNotification(getInstance().getResource().getString(
                                "datawizard.fileuploadsuccess"), TYPE_HUMANIZED_MESSAGE);
                    }
                }
            };
            wizard.addStep(new WizardStep() {
                @Override
                public String getCaption() {
                    return getInstance().getResource().getString(
                            "window.loggingdetails");
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    try {
                        XincoCoreDataServer temp
                                = new XincoCoreDataServer(
                                        valueOf(xincoTree.getValue().toString()
                                                .substring(xincoTree.getValue().toString()
                                                        .indexOf('-') + 1)));
                        buildLogDialog(temp, form, versionSelector);
                        return form;
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
                        return null;
                    }
                }

                @Override
                public boolean onAdvance() {
                    return !(form.getField("reason").isEnabled()
                            && form.getField("reason").getValue().toString()
                                    .isEmpty());
                }

                @Override
                public boolean onBack() {
                    return false;
                }
            });
            wizard.addStep(new WizardStep() {
                @Override
                public String getCaption() {
                    return getInstance().getResource().getString("general.file.select");
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    upload.addListener((Upload.SucceededListener) um);
                    upload.addListener(listener);
                    upload.addListener((Upload.FailedListener) um);
                    return upload;
                }

                @Override
                public boolean onAdvance() {
                    if (!um.isSuccess()) {
                        getMainWindow().showNotification(getInstance().getResource().getString("message.missing.file"), TYPE_ERROR_MESSAGE);
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
                        XincoCoreDataServer temp = new XincoCoreDataServer(
                                valueOf(xincoTree.getValue()
                                        .toString().substring(xincoTree.getValue()
                                                .toString().indexOf('-') + 1)));
                        //Check in file
                        if (getService().doXincoCoreDataCheckin(temp,
                                loggedUser) == null) {
                            getMainWindow().showNotification(getInstance().getResource()
                                    .getString("datawizard.unabletoloadfile"), TYPE_ERROR_MESSAGE);
                        }
                        try {
                            data = new XincoCoreDataServer(temp.getId());
                            //Now load the file
                            loadFile(getFileToLoad(), getFileName());
                            //Update log version
                            XincoCoreLogServer log = new XincoCoreLogServer(
                                    ((XincoCoreLog) getXincoCoreData()
                                            .getXincoCoreLogs().get(
                                                    getXincoCoreData()
                                                            .getXincoCoreLogs().size() - 1)).getId());
                            log.setVersion(versionSelector.getVersion());
                            log.setChangerID(1);
                            log.write2DB();
                            refresh();
                        } catch (XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
                    }
                    getMainWindow().removeWindow(wizardWindow);
                }

                @Override
                public void wizardCancelled(WizardCancelledEvent event) {
                    getMainWindow().removeWindow(wizardWindow);
                }
            });
            wizardWindow.setModal(true);
            wizardWindow.setWidth(40, UNITS_PERCENTAGE);
            // add the wizard to a layout
            getMainWindow().addWindow(wizardWindow);
        } catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
            try {
                XincoCoreLogServer log
                        = new XincoCoreLogServer(newlog.getId());
                new XincoCoreLogJpaController(
                        getEntityManagerFactory())
                        .destroy(log.getId());
            } catch (NonexistentEntityException | XincoException ex1) {
                LOG.log(SEVERE, null, ex1);
            }
        }
    }

    private void buildLogDialog(XincoCoreDataServer temp, Form form,
            VersionSelector versionSelector)
            throws XincoException {
        buildLogDialog(temp, form, versionSelector, null);
    }

    private void buildLogDialog(XincoCoreDataServer temp, Form form,
            VersionSelector versionSelector, OPCode opcode)
            throws XincoException {
        form.addField("action", new com.vaadin.ui.TextField(
                getInstance().getResource().getString(
                        "window.loggingdetails.action")
                + ":"));
        final int log_index = temp.getXincoCoreLogs().size() - 1;
        form.getField("action").setValue(
                ((XincoCoreLog) temp.getXincoCoreLogs().get(log_index))
                        .getOpDescription());
        form.getField("action").setEnabled(false);
        form.addField("reason", new com.vaadin.ui.TextArea());
        OPCode code;
        if (opcode == null) {
            code = getOPCode(
                    ((XincoCoreLog) temp.getXincoCoreLogs()
                            .get(log_index)).getOpCode());
        } else {
            code = opcode;
        }
        switch (code) {
            case COMMENT:
                versionSelector.increaseLow();
            case CHECKIN:
                versionSelector.setMinorEnabled(code == CHECKIN);
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
        versionSelector.setMinorEnabled(false);
        versionSelector.setCaption(getInstance().getResource()
                .getString("general.version"));
        versionSelector.setVersion(versionSelector.getVersion());
        versionSelector.setEnabled(code != DATA_MOVE);
        form.getField("reason").setRequired(form.getField("reason").isEnabled());
        form.getField("reason").setRequiredError(getInstance().getResource()
                .getString("message.warning.reason"));
        form.getLayout().addComponent(versionSelector);
    }

    private void undoCheckoutFile() {
        if (xincoTree.getValue() != null && xincoTree.getValue().toString()
                .startsWith("data")) {
            try {
                XincoCoreDataServer temp
                        = new XincoCoreDataServer(
                                valueOf(xincoTree.getValue().toString()
                                        .substring(xincoTree.getValue().toString().indexOf('-')
                                                + 1)));
                //Undo check out
                XincoCoreLog newlog = new XincoCoreLog();
                newlog.setOpCode(CHECKOUT_UNDONE.ordinal() + 1);
                newlog.setOpDescription(getInstance().getResource().getString(getOPCode(newlog.getOpCode()).getName()));
                newlog.setXincoCoreUserId(loggedUser.getId());
                newlog.setXincoCoreDataId(temp.getId());
                newlog.setVersion(((XincoCoreLog) temp.getXincoCoreLogs().get(
                        temp.getXincoCoreLogs().size() - 1)).getVersion());
                newlog.setOpDescription(newlog.getOpDescription() + " ("
                        + getInstance().getResource().getString("general.user")
                        + ": " + loggedUser.getUsername() + ")");
                //save log to server
                newlog = getService().setXincoCoreLog(newlog, loggedUser);
                if (newlog != null) {
                    temp.getXincoCoreLogs().add(newlog);
                }
                getService().undoXincoCoreDataCheckout(temp, loggedUser);
                refresh();
            } catch (XincoException ex) {
                LOG.log(SEVERE, null, ex);
            }
        }
    }

    private void checkoutFile() {
        try {
            if (xincoTree.getValue() != null && xincoTree.getValue().toString()
                    .startsWith("data")) {
                XincoCoreDataServer temp
                        = new XincoCoreDataServer(
                                valueOf(xincoTree.getValue().toString()
                                        .substring(xincoTree.getValue().toString().indexOf('-')
                                                + 1)));
                //Only makes sense for data
                if (temp.getXincoCoreDataType().getId() == 1) {
                    //Set as checked out
                    try {
                        XincoCoreLog newlog = new XincoCoreLog();
                        newlog.setOpCode(CHECKOUT.ordinal() + 1);
                        newlog.setOpDescription(getInstance().getResource()
                                .getString(getOPCode(newlog.getOpCode())
                                        .getName()));
                        newlog.setXincoCoreUserId(loggedUser.getId());
                        newlog.setXincoCoreDataId(temp.getId());
                        newlog.setVersion(((XincoCoreLog) temp.getXincoCoreLogs()
                                .get(temp.getXincoCoreLogs().size() - 1)).getVersion());
                        newlog.setOpDescription(newlog.getOpDescription() + " ("
                                + getInstance().getResource()
                                        .getString("general.user") + ": "
                                + loggedUser.getUsername() + ")");
                        //save log to server
                        newlog = getService().setXincoCoreLog(newlog, loggedUser);
                        if (newlog != null) {
                            temp.getXincoCoreLogs().add(newlog);
                        }
                        if (getService().doXincoCoreDataCheckout(temp, loggedUser)
                                != null) {
                            //Download the file
                            downloadFile(false, data);
                            xincoTree.setValue("node-1");
                            refresh();
                        } else {
                            LOG.log(SEVERE,
                                    "Unable to check out file: {0} with user: {1}",
                                    new Object[]{temp.getDesignation(), loggedUser});
                            getMainWindow().showNotification(getInstance().getResource().getString(
                                    "general.error"), TYPE_WARNING_MESSAGE);
                        }
                    } catch (MalformedURLException | XincoException ex) {
                        LOG.log(SEVERE, null, ex);
                    }
                }
            }
        } catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
        }
    }

    private void downloadFile(final XincoCoreData data) throws XincoException,
            MalformedURLException {
        boolean temporaryAccess = false;
        if (loggedUser == null) {
            loggedUser = new XincoCoreUserServer(1);
            temporaryAccess = true;
        }
        StreamSource ss = new StreamSource() {
            byte[] bytes = getService().downloadXincoCoreData(
                    data, loggedUser);
            InputStream is = bytes == null ? null : new ByteArrayInputStream(bytes);

            @Override
            public InputStream getStream() {
                return is;
            }
        };
        StreamResource sr = new StreamResource(ss,
                data.getXincoAddAttributes().get(0).getAttribVarchar(), this);
        getMainWindow().open(sr, "_blank");
        if (temporaryAccess) {
            loggedUser = null;
        }
    }

    private void downloadFile() throws XincoException, MalformedURLException {
        downloadFile(true, data);
    }

    private void downloadFile(boolean useRendering, XincoCoreData xdata)
            throws XincoException, MalformedURLException {
        XincoCoreData temp = xdata;
        //Check for available renderings
        java.util.List<com.bluecubs.xinco.core.server.persistence.XincoCoreData> renderings
                = getRenderings(temp.getId());
        if (useRendering && !renderings.isEmpty()) {
            for (com.bluecubs.xinco.core.server.persistence.XincoCoreData rendering
                    : renderings) {
                if (!rendering.getXincoAddAttributeList().isEmpty()
                        && rendering.getXincoAddAttributeList().get(0)
                                .getAttribVarchar().endsWith(".pdf")) {
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
                        + System.getProperty("file.separator")
                        + randomUUID().toString());
                temp.mkdirs();
                path_to_file = temp.getAbsolutePath()
                        + System.getProperty("file.separator") + fileName;
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
                ((XincoAddAttribute) getXincoCoreData().getXincoAddAttributes()
                        .get(0)).setAttribVarchar(fileName);
                getXincoCoreData().setDesignation(fileName);
                ((XincoAddAttribute) getXincoCoreData().getXincoAddAttributes()
                        .get(1)).setAttribUnsignedint(totalLen);
                ((XincoAddAttribute) getXincoCoreData().getXincoAddAttributes()
                        .get(2)).setAttribVarchar(""
                        + cin.getChecksum().getValue());
                ((XincoAddAttribute) getXincoCoreData().getXincoAddAttributes()
                        .get(3)).setAttribUnsignedint(1);
            } catch (IOException fe) {
                LOG.log(SEVERE, null, fe);
                getMainWindow().showNotification(getInstance().getResource().getString(
                        "datawizard.unabletoloadfile"), TYPE_ERROR_MESSAGE);
            }
            if (cin != null) {
                // save data to server
                data = getService().setXincoCoreData(getXincoCoreData(),
                        loggedUser);
                if (getXincoCoreData() == null) {
                    getMainWindow().showNotification(getInstance().getResource().getString(
                            "datawizard.unabletosavedatatoserver"), TYPE_ERROR_MESSAGE);
                }
                // upload file
                if (getService().uploadXincoCoreData(getXincoCoreData(),
                        byteArray, loggedUser) != totalLen) {
                    cin.close();
                    removeDirectory(temp);
                    getMainWindow().showNotification(getInstance().getResource().getString(
                            "datawizard.fileuploadfailed"), TYPE_ERROR_MESSAGE);
                }
                cin.close();
                removeDirectory(temp);
            }
        } catch (MalformedURLException ex) {
            LOG.log(SEVERE, null, ex);
            getMainWindow().showNotification(getInstance().getResource().getString(
                    "datawizard.fileuploadfailed"), TYPE_ERROR_MESSAGE);
        } catch (IOException ex) {
            LOG.log(SEVERE, null, ex);
            getMainWindow().showNotification(getInstance().getResource().getString(
                    "datawizard.fileuploadfailed"), TYPE_ERROR_MESSAGE);
        }
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
        for (com.vaadin.ui.Window w : getMainWindow().getChildWindows()) {
            //Center sub window in new screen size
            w.center();
        }
    }
//TODO: Rendering support

    private void showRenderingDialog() throws XincoException {
        if (renderingSupportEnabled) {
            final com.vaadin.ui.Window renderWindow
                    = new com.vaadin.ui.Window();
            final Form form = new Form();
            form.setCaption(getInstance().getResource().getString(
                    "general.data.type.rendering"));
            final ArrayList<com.bluecubs.xinco.core.server.persistence.XincoCoreData> renderings
                    = (ArrayList<com.bluecubs.xinco.core.server.persistence.XincoCoreData>) getRenderings(valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
            final Table table = new Table();
            table.addStyleName("striped");
            table.addContainerProperty(
                    getInstance().getResource().getString("general.name"),
                    com.vaadin.ui.Label.class, null);
            table.addContainerProperty(
                    getInstance().getResource().getString("general.version"),
                    com.vaadin.ui.Label.class, null);
            table.addContainerProperty(
                    getInstance().getResource().getString("general.extension"),
                    com.vaadin.ui.Label.class, null);
            form.getLayout().addComponent(table);
            for (com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd : renderings) {
                XincoCoreDataServer xcds = new XincoCoreDataServer(xcd.getId());
                String name = xcds.getXincoAddAttributes().get(0).getAttribVarchar();
                XincoVersion xVersion = getCurrentVersion(xcds.getId());
                table.addItem(new Object[]{new com.vaadin.ui.Label(xcds.getDesignation()),
                    new com.vaadin.ui.Label(xVersion.getVersionHigh()
                    + "." + xVersion.getVersionMid()
                    + "." + xVersion.getVersionLow() + " "
                    + xVersion.getVersionPostfix()),
                    new com.vaadin.ui.Label(
                    name.substring(name.lastIndexOf('.')
                    + 1, name.length()))},
                        xcds.getId());
            }
            form.setFooter(new HorizontalLayout());
            //Used for validation purposes
            final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                    getInstance().getResource().getString("general.add"), form, "commit");
            final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                    getInstance().getResource().getString("general.cancel"),
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
                        final XincoCoreDataServer parent = new XincoCoreDataServer(valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                        //initialize the data object
                        data = new XincoCoreData();
                        getXincoCoreData().setXincoCoreDataType(new XincoCoreDataTypeServer(parent.getXincoCoreDataType().getId()));
                        getXincoCoreData().setStatusNumber(parent.getStatusNumber());
                        getXincoCoreData().setXincoCoreLanguage(parent.getXincoCoreLanguage());
                        getXincoCoreData().setXincoCoreNodeId(parent.getXincoCoreNodeId());
                        addDefaultAddAttributes(getXincoCoreData());
                        commit.setEnabled(false);
                        cancel.setEnabled(false);
                        XincoWizard wizard = new XincoWizard(getLocale());
                        wizard.addStep(fileStep);
                        getMainWindow().removeWindow(renderWindow);
                        final com.vaadin.ui.Window addRenderingWindow = new com.vaadin.ui.Window();
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
                                    XincoCoreDataJpaController controller = new XincoCoreDataJpaController(getEntityManagerFactory());
                                    XincoCoreDataHasDependencyServer dep = new XincoCoreDataHasDependencyServer(
                                            controller.findXincoCoreData(parent.getId()),
                                            controller.findXincoCoreData(getXincoCoreData().getId()),
                                            new XincoDependencyTypeServer(5));//Rendering
                                    dep.write2DB();
                                } catch (XincoException ex) {
                                    getMainWindow().showNotification(getInstance().getResource().getString("general.error"),
                                            getInstance().getResource().getString("message.error.association.exists"), TYPE_ERROR_MESSAGE);
                                }
                                getMainWindow().removeWindow(addRenderingWindow);
                                try {
                                    showRenderingDialog();
                                } catch (XincoException ex) {
                                    LOG.log(SEVERE, null, ex);
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
                        addRenderingWindow.setWidth(35, UNITS_PERCENTAGE);
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
                    }
                }
            });
            table.setWidth(100, UNITS_INCH);
            renderWindow.addComponent(form);
            form.getFooter().setSizeUndefined();
            form.getFooter().addComponent(commit);
            form.getFooter().addComponent(cancel);
            form.setSizeFull();
            renderWindow.center();
            renderWindow.setModal(true);
            renderWindow.setWidth(50, UNITS_PERCENTAGE);
            getMainWindow().addWindow(renderWindow);
        }
    }

    private void lockData() throws XincoException, MalformedURLException {
        XincoCoreDataServer xdata = new XincoCoreDataServer(valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
        xdata.setStatusNumber(2);
        addLog(xdata, LOCK_COMMENT);
        xdata.write2DB();
        refresh();
    }

    private void publishData() throws XincoException, MalformedURLException {
        XincoCoreDataServer xdata = new XincoCoreDataServer(valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
        xdata.setStatusNumber(5);
        addLog(xdata, PUBLISH_COMMENT);
        xdata.write2DB();
        refresh();
    }

    private void showACLDialog() {
        //TODO: Ugly workaround for issue XDMS-38. See XDMS-39
        //For some reason the ACL is updated twice, once with the new values
        //and once with the old values, leaving them as they were.
        final ArrayList<Integer> processed = new ArrayList<>();
        final com.vaadin.ui.Window aclWindow = new com.vaadin.ui.Window();
        final Form form = new Form();
        form.setCaption(getInstance().getResource().getString("window.acl"));
        final HashMap<String, XincoCoreACEServer> aceList = new HashMap<>();
        final Table table = new Table();
        final TwinColSelect acls = new TwinColSelect();
        acls.setImmediate(true);
        acls.setNewItemsAllowed(false);
        acls.setNullSelectionAllowed(true);
        acls.setMultiSelect(true);
        acls.setNewItemsAllowed(false);
        acls.setLeftColumnCaption(getInstance().getResource().getString("general.notincluded"));
        acls.setRightColumnCaption(getInstance().getResource().getString("general.included"));
        table.addStyleName("striped");
        // Preselect values
        HashSet<String> preselected = new HashSet<>();
        /*
         * Define the names and data types of columns. The "default value"
         * parameter is meaningless here.
         */
        table.addContainerProperty(
                getInstance().getResource().getString("general.name"),
                com.vaadin.ui.Label.class, null);
        table.addContainerProperty(
                getInstance().getResource().getString("general.type"),
                com.vaadin.ui.Label.class, null);
        table.addContainerProperty(
                getInstance().getResource().getString("general.acl.adminpermission"),
                CheckBox.class, null);
        table.addContainerProperty(
                getInstance().getResource().getString("general.acl.readpermission"),
                CheckBox.class, null);
        table.addContainerProperty(
                getInstance().getResource().getString("general.acl.writepermission"),
                CheckBox.class, null);
        table.addContainerProperty(
                getInstance().getResource().getString("general.acl.executepermission"),
                CheckBox.class, null);
        table.setPageLength(10);
        com.vaadin.ui.Label name;
        com.vaadin.ui.Label type;
        //Add Groups
        for (Iterator it = getXincoCoreGroups().iterator(); it.hasNext();) {
            XincoCoreGroupServer group = (XincoCoreGroupServer) it.next();
            String itemId = getInstance().getResource().getString("general.group") + "-" + group.getId();
            String designation = group.getDesignation();
            String value = getInstance().getResource().containsKey(designation)
                    ? getInstance().getResource().getString(designation) : designation;
            name = new com.vaadin.ui.Label(value);
            type = new com.vaadin.ui.Label(getInstance().getResource().getString("general.group"));
            final CheckBox admin = new CheckBox(getInstance().getResource().getString("general.acl.adminpermission"));
            admin.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (admin.getData() != null) {
                        aceList.get(admin.getData().toString()).setAdminPermission((Boolean) admin.getValue());
                    }
                }
            });
            admin.setImmediate(true);
            final CheckBox execute = new CheckBox(getInstance().getResource().getString("general.acl.executepermission"));
            execute.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (execute.getData() != null) {
                        aceList.get(execute.getData().toString()).setExecutePermission((Boolean) execute.getValue());
                    }
                }
            });
            execute.setImmediate(true);
            final CheckBox read = new CheckBox(getInstance().getResource().getString("general.acl.readpermission"));
            read.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (read.getData() != null) {
                        aceList.get(read.getData().toString()).setReadPermission((Boolean) read.getValue());
                    }
                }
            });
            read.setImmediate(true);
            final CheckBox write = new CheckBox(getInstance().getResource().getString("general.acl.writepermission"));
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
                    XincoCoreNodeServer tempNode
                            = new XincoCoreNodeServer(valueOf(xincoTree.getValue().toString()
                                    .substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                    try {
                        acl = new XincoCoreACEServer(0, 0, group.getId(),
                                tempNode.getId(), 0, false, false, false, false);
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
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
                    LOG.log(SEVERE, null, ex);
                }
            } else if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("data")) {
                try {
                    XincoCoreDataServer tempData = new XincoCoreDataServer(valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                    try {
                        acl = new XincoCoreACEServer(0, 0, 0, 0, tempData.getId(), false, false, false, false);
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
                    }
                    for (Iterator<Object> it2 = tempData.getXincoCoreAcl().iterator(); it2.hasNext();) {
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
                    LOG.log(SEVERE, null, ex);
                }
            }
            addAll(preselected, value);
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
        for (XincoCoreUserServer user : getXincoCoreUsers()) {
            String itemId = getInstance().getResource().getString("general.user") + "-" + user.getId();
            String userLabel = user.getFirstName() + " "
                    + user.getLastName() + " (" + user.getUsername() + ")";
            name = new com.vaadin.ui.Label(userLabel);
            type = new com.vaadin.ui.Label(getInstance().getResource().getString("general.user"));
            final CheckBox admin = new CheckBox(getInstance().getResource().getString("general.acl.adminpermission"));
            admin.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (admin.getData() != null) {
                        aceList.get(admin.getData().toString()).setAdminPermission((Boolean) admin.getValue());
                    }
                }
            });
            admin.setImmediate(true);
            final CheckBox execute = new CheckBox(getInstance().getResource().getString("general.acl.executepermission"));
            execute.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (execute.getData() != null) {
                        aceList.get(execute.getData().toString()).setExecutePermission((Boolean) execute.getValue());
                    }
                }
            });
            execute.setImmediate(true);
            final CheckBox read = new CheckBox(getInstance().getResource().getString("general.acl.readpermission"));
            read.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (read.getData() != null) {
                        aceList.get(read.getData().toString()).setReadPermission((Boolean) read.getValue());
                    }
                }
            });
            read.setImmediate(true);
            final CheckBox write = new CheckBox(getInstance().getResource().getString("general.acl.writepermission"));
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
                    XincoCoreNodeServer tempNode
                            = new XincoCoreNodeServer(valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                    try {
                        acl = new XincoCoreACEServer(0, loggedUser.getId(), 0, tempNode.getId(), 0, false, false, false, false);
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
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
                    LOG.log(SEVERE, null, ex);
                }
            } else if (xincoTree.getValue() != null && xincoTree.getValue().toString().startsWith("data")) {
                try {
                    XincoCoreDataServer tempData = new XincoCoreDataServer(valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
                    try {
                        acl = new XincoCoreACEServer(0, 0, loggedUser.getId(), 0, tempData.getId(), false, false, false, false);
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
                    }
                    for (Iterator<Object> it2 = tempData.getXincoCoreAcl().iterator(); it2.hasNext();) {
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
                    LOG.log(SEVERE, null, ex);
                }
            }
            addAll(preselected, userLabel);
            admin.setData(itemId);
            read.setData(itemId);
            write.setData(itemId);
            execute.setData(itemId);
            // Create the table row.
            table.addItem(new Object[]{name, type, admin, read, write, execute},
                    itemId);
            aceList.put(itemId, acl);
            acls.addItem(getInstance().getResource().getString("general.user") + ":" + userLabel);
        }
        table.setSizeFull();
        form.getLayout().addComponent(table);
        form.setFooter(new HorizontalLayout());
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.save"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    updateMenu();
                } catch (XincoException ex) {
                    LOG.log(SEVERE, null, ex);
                }
                getMainWindow().removeWindow(aclWindow);
            }
        });
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                for (Iterator<Entry<String, XincoCoreACEServer>> it = aceList.entrySet().iterator(); it.hasNext();) {
                    boolean skip = false;
                    Entry<String, XincoCoreACEServer> e = it.next();
                    XincoCoreACEServer stored = null;
                    XincoCoreACEServer value = e.getValue();
                    int ownerID = -1;
                    if (getXincoTree().getValue().toString().startsWith("data")) {
                        ownerID = getOwnerID(new XincoCoreDataServer(data.getId()));
                    }
                    Integer thisOwner = valueOf(e.getKey().substring(e.getKey().lastIndexOf("-") + 1));
                    //Owner cannot be removed
                    if (ownerID != -1 && e.getKey().startsWith("User-") && thisOwner == ownerID
                            && (!e.getValue().isReadPermission()
                            || !e.getValue().isAdminPermission()
                            || !e.getValue().isExecutePermission()
                            || !e.getValue().isWritePermission())) {
                        getMainWindow().showNotification(getInstance().getResource().getString("window.acl.removefailed"),
                                getInstance().getResource().getString("window.acl.cannotremoveowner"), TYPE_WARNING_MESSAGE);
                        skip = true;
                    }
                    if (!skip) {
                        try {
                            stored = new XincoCoreACEServer(value.getId());
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
                                LOG.log(SEVERE, null, ex1);
                            }
                        }
                        boolean modified = false;
                        if (stored != null && (stored.isAdminPermission() != e.getValue().isAdminPermission()
                                || stored.isExecutePermission() != e.getValue().isExecutePermission()
                                || stored.isReadPermission() != e.getValue().isReadPermission()
                                || stored.isWritePermission() != e.getValue().isWritePermission())) {
                            //Is different so update
                            stored.setReadPermission(e.getValue().isReadPermission());
                            stored.setAdminPermission(e.getValue().isAdminPermission());
                            stored.setExecutePermission(e.getValue().isExecutePermission());
                            stored.setWritePermission(e.getValue().isWritePermission());
                            modified = true;
                            LOG.info("ACL change detected!");
                        }
                        if (stored != null && stored.getXincoCoreUserId() > 0) {
                            getMainWindow().showNotification(getInstance().getResource().getString("general.warning"),
                                    getInstance().getResource().getString("error.noadminpermission"), TYPE_WARNING_MESSAGE);
                            return;
                        }
                        try {
                            if (stored != null && modified
                                    && !processed.contains(e.getValue().getId())) {
                                LOG.info("Writing ACL into database...");
                                LOG.info(e.getValue().toString());
                                processed.add(e.getValue().getId());
                                e.getValue().write2DB();
                            }
                        } catch (XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
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
        aclWindow.setWidth(80, UNITS_PERCENTAGE);
        getMainWindow().addWindow(aclWindow);
    }

    private void showLoginDialog() {
        final com.vaadin.ui.Window loginWindow = new com.vaadin.ui.Window();
        final Form form = new Form();
        form.setCaption(getInstance().getResource().getString("window.connection") + ":");
        com.vaadin.ui.TextField username
                = new com.vaadin.ui.TextField(getInstance().getResource().getString("general.username") + ":");
        PasswordField password
                = new PasswordField(getInstance().getResource().getString("general.password") + ":");
        form.addField("username", username);
        form.addField("password", password);
        form.getField("username").setRequired(true);
        form.getField("username").focus();
        form.getField("username").setRequiredError(getInstance().getResource().getString("message.missing.username"));
        form.getField("password").setRequired(true);
        form.getField("password").setRequiredError(getInstance().getResource().getString("message.missing.password"));
        form.setFooter(new HorizontalLayout());
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.login"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.cancel"),
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
                    LOG.log(SEVERE, null, ex);
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
                if (!validCredentials(
                        ((com.vaadin.ui.TextField) form.getField("username")).getValue().toString(),
                        ((PasswordField) form.getField("password")).getValue().toString(),
                        true)) {
                    getMainWindow().showNotification(getInstance().getResource().getString("menu.connection.error.user"), TYPE_WARNING_MESSAGE);
                    String username = ((com.vaadin.ui.TextField) form.getField("username")).getValue().toString();
                    //Wrong password or username
                    java.util.List result = createdQuery(
                            "SELECT x FROM XincoCoreUser x WHERE x.username='"
                            + username + "' AND x.statusNumber <> 2");
                    //Check if the username is correct if not just throw the wrong login message
                    if (result.isEmpty()) {
                        getMainWindow().showNotification("Login "
                                + getInstance().getResource().getString("general.fail")
                                + " Username and/or Password may be incorrect!", TYPE_WARNING_MESSAGE);
                    } else {
                        result = createdQuery("SELECT x FROM XincoCoreUser x WHERE x.username='"
                                + username + "'");
                        if (result.size() > 0) {
                            XincoCoreUserServer temp_user = new XincoCoreUserServer((com.bluecubs.xinco.core.server.persistence.XincoCoreUser) result.get(0));
                            long attempts = getSetting(new XincoCoreUserServer(1),
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
                                temp_user.setReason(getInstance().getResource().getString("password.attempt.limitReached"));
                                //the password retrieved when you logon is already hashed...
                                temp_user.setHashPassword(false);
                                temp_user.setIncreaseAttempts(true);
                                temp_user.write2DB();
                                getMainWindow().showNotification(getInstance().getResource().getString("password.attempt.limitReached"), TYPE_WARNING_MESSAGE);
                            } else {
                                getMainWindow().showNotification(getInstance().getResource().getString("password.login.fail"), TYPE_WARNING_MESSAGE);
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
                        refresh();
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
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
        loginWindow.setWidth(300, UNITS_PIXELS);
        loginWindow.setReadOnly(true);
        getMainWindow().addWindow(loginWindow);
        refresh();
    }

    private void refreshGroupTable(final Table table) {
        ArrayList allgroups = getXincoCoreGroups();
        table.removeAllItems();
        for (Iterator<XincoCoreGroupServer> it = allgroups.iterator(); it.hasNext();) {
            XincoCoreGroupServer group = it.next();
            final com.vaadin.ui.Button edit = new com.vaadin.ui.Button(getInstance().getResource().getString("general.edit"));
            edit.setData(group.getId());
            edit.addStyleName("link");
            edit.addListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    showEditSingleGroupWindow((Integer) edit.getData());
                }
            });
            table.addItem(new Object[]{group.getId(),
                getInstance().getResource().containsKey(group.getDesignation())
                ? getInstance().getResource().getString(group.getDesignation())
                : group.getDesignation(),
                edit}, group.getId());
        }
        table.sort();
    }

    private void showEditSingleGroupWindow(final Integer groupId) {
        final com.vaadin.ui.Window group = new com.vaadin.ui.Window();
        final Form form = new Form();
        final XincoCoreGroupServer groupS = new XincoCoreGroupServer(groupId);
        refreshGroupContentsTables(form, groupId);
        form.addField("name", new com.vaadin.ui.TextField());
        form.getField("name").setCaption(getInstance().getResource().getString("general.name"));
        form.getField("name").setValue(getInstance().getResource().containsKey(groupS.getDesignation())
                ? getInstance().getResource().getString(groupS.getDesignation())
                : groupS.getDesignation());
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                getMainWindow().removeWindow(group);
            }
        });
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.save"), form, "commit");
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    if (!form.getField("name").getValue().equals(
                            getInstance().getResource().containsKey(groupS.getDesignation())
                            ? getInstance().getResource().getString(groupS.getDesignation())
                            : groupS.getDesignation())) {
                        groupS.setDesignation(form.getField("name").getValue().toString());
                        groupS.write2DB();
                    }
                } catch (XincoException ex) {
                    LOG.log(SEVERE, null, ex);
                }
                getMainWindow().removeWindow(group);
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        group.addComponent(form);
        group.setModal(true);
        group.center();
        group.setWidth(600, UNITS_PIXELS);
        getMainWindow().addWindow(group);
    }

    private void showGroupAdminWindow() {
        final com.vaadin.ui.Window group = new com.vaadin.ui.Window();
        final Form form = new Form();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getInstance().getResource().getString("general.id"),
                Integer.class, null);
        table.addContainerProperty(getInstance().getResource().getString("general.name"),
                String.class, null);
        table.addContainerProperty(getInstance().getResource().getString("general.edit"),
                com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getInstance().getResource().getString("general.id"));
        form.getLayout().addComponent(table);
        refreshGroupTable(table);
        form.addField("name", new com.vaadin.ui.TextField(getInstance().getResource().getString("general.name") + ":"));
        form.getField("name").setRequired(true);
        form.getField("name").setRequiredError(getInstance().getResource().getString("message.missing.groupname"));
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.add.group"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.cancel"),
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
                    LOG.log(SEVERE, null, ex);
                }
            }
        });
        group.addListener(new FocusListener() {
            @Override
            public void focus(FocusEvent event) {
                refreshGroupTable(table);
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        group.addComponent(form);
        group.setModal(true);
        group.center();
        group.setWidth(300, UNITS_PIXELS);
        getMainWindow().addWindow(group);
    }

    private void showDataTypeAttrAdminWindow(final int dataTypeId) {
        final com.vaadin.ui.Window attr = new com.vaadin.ui.Window();
        final Form form = new Form();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getInstance().getResource().getString("general.position"),
                Integer.class, null);
        table.addContainerProperty(getInstance().getResource().getString("general.designation"),
                String.class, null);
        table.addContainerProperty(getInstance().getResource().getString("general.datatype"),
                String.class, null);
        table.addContainerProperty(getInstance().getResource()
                .getString("general.size"),
                String.class, null);
        table.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getInstance().getResource()
                .getString("general.position"));
        form.getLayout().addComponent(table);
        refreshFileTypeAttrTable(table, dataTypeId);
        form.addField("position", new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.position") + ":"));
        form.getField("position").setRequired(true);
        form.getField("position").setRequiredError(getInstance().getResource()
                .getString("message.missing.position"));
        form.addField("designation", new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.designation") + ":"));
        form.getField("designation").setRequired(true);
        form.getField("designation").setRequiredError(getInstance()
                .getResource().getString("message.missing.designation"));
        Select types = new Select(getInstance().getResource()
                .getString("general.datatype") + ":");
        HashMap<String, String> typeDefs = new HashMap<>();
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
        form.getField("type").setRequiredError(getInstance().getResource()
                .getString("message.missing.datatype"));
        form.addField("size", new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.size") + ":"));
        form.getField("size").setRequired(true);
        form.getField("size").setRequiredError(getInstance().getResource()
                .getString("message.missing.size"));
        form.getField("size").setDescription(getInstance().getResource()
                .getString("message.admin.attribute.req4string"));
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.add.attribute"),
                form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.cancel"),
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
                    XincoCoreDataTypeAttributeServer tempAttribute
                            = new XincoCoreDataTypeAttributeServer(dataTypeId,
                                    valueOf(form.getField("position").getValue()
                                            .toString()),
                                    form.getField("designation").getValue().toString(),
                                    form.getField("type").getValue().toString(),
                                    valueOf(form.getField("size").getValue()
                                            .toString()));
                    tempAttribute.setChangerID(loggedUser.getId());
                    tempAttribute.write2DB();
                } catch (XincoException ex) {
                    LOG.log(SEVERE, null, ex);
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
                getInstance().getResource()
                        .getString("message.warning.attribute.remove")));
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        attr.addComponent(form);
        attr.setModal(true);
        attr.center();
        attr.setWidth(50, UNITS_PERCENTAGE);
        getMainWindow().addWindow(attr);
    }

    private void showChangePasswordDialog() {
        final com.vaadin.ui.Window pass = new com.vaadin.ui.Window();
        final Form form = new Form();
        form.getLayout().addComponent(new com.vaadin.ui.Label(getInstance()
                .getResource().getString("password.aged")));
        form.addField("password", new PasswordField(getInstance().getResource()
                .getString("general.password")));
        form.addField("confirm", new PasswordField(getInstance().getResource()
                .getString("general.verifypassword")));
        form.getField("password").setRequired(true);
        form.getField("password").setRequiredError(getInstance().getResource()
                .getString("message.missing.password"));
        form.getField("confirm").setRequired(true);
        form.getField("confirm").setRequiredError(getInstance().getResource()
                .getString("message.missing.designation"));
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button("Submit",
                form, "commit");
        commit.addListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                if (!form.getField("password").getValue().equals(form
                        .getField("confirm").getValue())) {
                    getMainWindow().showNotification(getInstance().getResource()
                            .getString("window.userinfo.passwordmismatch"), TYPE_WARNING_MESSAGE);
                } else {
                    boolean passwordIsUsable = loggedUser.isPasswordUsable(form
                            .getField("password").getValue().toString());
                    if (passwordIsUsable) {
                        try {
                            XincoCoreUserServer temp_user
                                    = new XincoCoreUserServer(loggedUser.getId());
                            temp_user.setUserpassword(form.getField("password")
                                    .getValue().toString());
                            temp_user.setLastModified(new Timestamp(currentTimeMillis()));
                            temp_user.setChangerID(loggedUser.getId());
                            temp_user.setWriteGroups(true);
                            //Register change in audit trail
                            temp_user.setChange(true);
                            //Reason for change
                            temp_user.setReason("audit.user.account.password.change");
                            temp_user.setHashPassword(true);
                            temp_user.write2DB();
                            getMainWindow().showNotification(getInstance()
                                    .getResource().getString("password.changed"), TYPE_TRAY_NOTIFICATION);
                            getMainWindow().removeWindow(pass);
                        } catch (XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    } else {
                        getMainWindow().showNotification(getInstance()
                                .getResource().getString("password.unusable"), TYPE_WARNING_MESSAGE);
                    }
                }
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        pass.addComponent(form);
        pass.setModal(true);
        pass.center();
        pass.setWidth(25, UNITS_PERCENTAGE);
        getMainWindow().addWindow(pass);
    }

    private Select getLanguageOptions() {
        final Select languages = new Select();
        ArrayList<String> locales = new ArrayList<>();
        ResourceBundle lrb = getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale",
                getDefault());
        locales.addAll(asList(lrb.getString("AvailableLocales").split(",")));
        for (String tempLocale : locales) {
            languages.addItem(tempLocale);
            languages.setItemCaption(tempLocale, lrb.getString("Locale."
                    + tempLocale));
            try {
                FileResource flagIcon = getFlagIcon(tempLocale.isEmpty() ? "us"
                        : tempLocale);
                languages.setItemIcon(tempLocale, flagIcon);
            } catch (IOException ex) {
                LOG.log(SEVERE, null, ex);
            }
        }
        languages.setValue(getLocale().getLanguage());
        return languages;
    }

    private void showLanguageSelection() {
        final com.vaadin.ui.Window lang = new com.vaadin.ui.Window();
        lang.setReadOnly(true);
        final Form form = new Form();
        Embedded logo = new Embedded("", new ThemeResource("img/xinco_logo.gif"));
        logo.setType(TYPE_IMAGE);
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
                            loc = getDefault();
                    }
                } catch (Exception e) {
                    loc = getDefault();
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
        lang.setWidth(getImageDim(context.getHttpSession().getServletContext()
                .getRealPath(
                        "/VAADIN/themes/xinco") + System.getProperty("file.separator")
                + "img"
                + System.getProperty("file.separator")
                + "xinco_logo.gif").width + 30,
                logo.getWidthUnits());
        getMainWindow().addWindow(lang);
    }

    private void showSettingAdminWindow() {
        com.vaadin.ui.Window setting = new com.vaadin.ui.Window();
        setting.setContent(new SettingAdminWindow());
        setting.setWidth(100, UNITS_PERCENTAGE);
        setting.center();
        setting.setModal(true);
        getMainWindow().addWindow(setting);
    }

    private void showAuditWindow() throws XincoException {
        final com.vaadin.ui.Window audit = new com.vaadin.ui.Window();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getInstance().getResource()
                .getString("general.table"),
                String.class, null);
        table.addContainerProperty(getInstance().getResource()
                .getString("general.audit.action"),
                com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getInstance().getResource()
                .getString("general.table"));
        Set<EntityType<?>> entities = getEntityManagerFactory()
                .getMetamodel().getEntities();
        for (EntityType type : entities) {
            String name = type.getName();
            if (type.getJavaType().getSuperclass() == XincoAuditedObject.class) {
                java.util.List<Object> result = createdQuery("select distinct x from "
                        + type.getJavaType().getSimpleName() + "T x");
                if (!result.isEmpty()) {
                    final com.vaadin.ui.Button cont
                            = new com.vaadin.ui.Button(getInstance().getResource()
                                    .getString("general.continue"));
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
        audit.setWidth(35, UNITS_PERCENTAGE);
        getMainWindow().addWindow(audit);
    }

    private Table showEntitiesInTable(java.util.List entities) throws XincoException {
        //Create the table
        final Table table = new Table();
        table.addStyleName("striped");
        table.setSizeFull();
        if (!entities.isEmpty()) {
            Class<? extends Object> entityClass = entities.get(0).getClass();
            EntityType entityType
                    = getEntityManagerFactory().getMetamodel()
                            .entity(entityClass);
            LinkedHashMap<String, PersistentAttributeType> typeMap
                    = new LinkedHashMap<>();
            for (Iterator it = entityType.getAttributes().iterator(); it.hasNext();) {
                Attribute attr = (Attribute) it.next();
                table.addContainerProperty(attr.getName(),
                        com.vaadin.ui.Component.class, null);
                typeMap.put(attr.getName(), attr.getPersistentAttributeType());
            }
            //Now add the audit fields
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.reason"),
                    com.vaadin.ui.Component.class, null);
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.audit.modtime"),
                    com.vaadin.ui.Component.class, null);
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.user"),
                    com.vaadin.ui.Component.class, null);
            int index = 0;
            for (Iterator it = entities.iterator(); it.hasNext();) {
                int recordId = 0;
                Object o = it.next();
                ArrayList values = new ArrayList();
                for (Entry<String, PersistentAttributeType> entry
                        : typeMap.entrySet()) {
                    try {

                        java.lang.reflect.Method method = o.getClass().getMethod("get"
                                + entry.getKey().substring(0, 1).toUpperCase()
                                + entry.getKey().substring(1));
                        if (entry.getKey().equals("recordId")) {
                            recordId = valueOf(method.invoke(o).toString());
                        }
                        String value = method.invoke(o).toString();
                        switch (entry.getValue()) {
                            case BASIC:
                                values.add(new com.vaadin.ui.Label(entry.getKey()
                                        .contains("password")
                                        ? "**********" : (getInstance()
                                                .getResource().containsKey(value)
                                        ? getInstance().getResource()
                                                .getString(value) : value)));
                                break;
                            default:
                                throw new XincoException(entry.getValue().name()
                                        + " not supported yet!");
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        LOG.log(SEVERE, null, ex);
                        throw new XincoException(ex.getLocalizedMessage());
                    }
                }
                //Now add the audit fields
                HashMap parameters = new HashMap();
                parameters.put("recordId", recordId);
                XincoCoreUserModifiedRecord record
                        = (XincoCoreUserModifiedRecord) namedQuery("XincoCoreUserModifiedRecord.findByRecordId",
                                parameters).get(0);
                values.add(new com.vaadin.ui.Label(getInstance().getResource()
                        .containsKey(record.getModReason())
                        ? getInstance().getResource().getString(record.getModReason())
                        : record.getModReason()));
                values.add(new com.vaadin.ui.Label(record.getModTime().toString()));
                values.add(new com.vaadin.ui.Label(record.getXincoCoreUser()
                        .getFirstName()
                        + " " + record.getXincoCoreUser().getLastName()));
                table.addItem(values.toArray(), index++);
            }
        }
        return table;
    }

    private void showAuditDetails(final EntityType entity) {
        try {
            final com.vaadin.ui.Window audit = new com.vaadin.ui.Window();
            final com.vaadin.ui.TextField tf = new com.vaadin.ui.TextField(
                    entity.getId(entity.getIdType().getJavaType()).getName());
            tf.focus();
            final Table table = showEntitiesInTable(createdQuery("select distinct x from "
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
            if (entity.getId(entity.getIdType().getJavaType())
                    .getPersistentAttributeType() == BASIC) {
                audit.addComponent(tf);
            }
            audit.addComponent(table);
            audit.setModal(true);
            audit.center();
            audit.setWidth(90, UNITS_PERCENTAGE);
            getMainWindow().addWindow(audit);
        } catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
        }
    }

    private void showAttrAdminWindow() {
        final com.vaadin.ui.Window attr = new com.vaadin.ui.Window();
        final Form form = new Form();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getInstance().getResource()
                .getString("general.id"),
                Integer.class, null);
        table.addContainerProperty(getInstance().getResource()
                .getString("general.designation"),
                String.class, null);
        table.addContainerProperty(getInstance().getResource()
                .getString("general.description"),
                String.class, null);
        table.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getInstance().getResource()
                .getString("general.id"));
        form.getLayout().addComponent(table);
        refreshFileTypeTable(table);
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.cancel"),
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
        attr.setWidth(50, UNITS_PERCENTAGE);
        getMainWindow().addWindow(attr);
    }

    private void showLangAdminWindow() {
        final com.vaadin.ui.Window lang = new com.vaadin.ui.Window();
        final Form form = new Form();
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getInstance().getResource()
                .getString("general.id"),
                Integer.class, null);
        table.addContainerProperty(getInstance().getResource()
                .getString("general.name"),
                String.class, null);
        table.addContainerProperty(getInstance().getResource()
                .getString("general.designation"),
                String.class, null);
        table.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table.setSortContainerPropertyId(getInstance().getResource()
                .getString("general.id"));
        form.getLayout().addComponent(table);
        refreshLanguageTable(table);
        form.addField("designation", new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.designation") + ":"));
        form.getField("designation").setRequired(true);
        form.getField("designation").setRequiredError(getInstance()
                .getResource().getString("message.missing.designation"));
        form.addField("sign", new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.sign") + ":"));
        form.getField("sign").setRequired(true);
        form.getField("sign").setRequiredError(getInstance().getResource()
                .getString("message.missing.sign"));
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.add.language"),
                form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.cancel"),
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
                    for (XincoCoreLanguageServer lang
                            : getXincoCoreLanguages()) {
                        if (getInstance().getResource()
                                .containsKey(lang.getDesignation())
                                && getInstance().getResource()
                                        .getString(lang.getDesignation())
                                        .equals(form.getField("designation")
                                                .getValue().toString())) {
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
                    getMainWindow().showNotification(getInstance()
                            .getResource().getString("general.error"),
                            getInstance().getResource()
                                    .getString("error.language.add.duplicate"), TYPE_WARNING_MESSAGE);
                    LOG.log(SEVERE, null, ex);
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
                getInstance().getResource()
                        .getString("error.language.delete.referenced")));
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        lang.addComponent(form);
        lang.setModal(true);
        lang.center();
        lang.setWidth(50, UNITS_PERCENTAGE);
        getMainWindow().addWindow(lang);
    }

    private void refreshLanguageTable(final Table table) {
        ArrayList allLanguages = getXincoCoreLanguages();
        boolean is_used;
        table.removeAllItems();
        for (Iterator<XincoCoreLanguageServer> it
                = allLanguages.iterator(); it.hasNext();) {
            XincoCoreLanguageServer lang = it.next();
            is_used = isLanguageUsed(lang);
            final com.vaadin.ui.Button delete
                    = new com.vaadin.ui.Button(getInstance().getResource()
                            .getString("general.delete"));
            delete.setData(lang.getId());
            delete.addStyleName("link");
            delete.addListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        deleteFromDB(
                                new XincoCoreLanguageServer((Integer) delete
                                        .getData()), loggedUser.getId());
                        refreshLanguageTable(table);
                    } catch (Exception e) {
                        LOG.log(SEVERE, null, e);
                    }
                }
            });
            table.addItem(new Object[]{lang.getId(),
                getInstance().getResource().containsKey(lang.getDesignation())
                ? getInstance().getResource().getString(lang.getDesignation())
                : lang.getDesignation(),
                lang.getSign(),
                is_used ? null : delete}, lang.getId());
        }
        table.sort();
    }

    private void showUserAdminWindow(final boolean userAdmin) {
        final com.vaadin.ui.Window user = new com.vaadin.ui.Window();
        final Form form = new Form();
        final Table table = new Table();
        if (userAdmin) {
            table.addStyleName("striped");
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.id"),
                    Integer.class, null);
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.username"),
                    String.class, null);
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.firstname"),
                    String.class, null);
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.lastname"),
                    String.class, null);
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.email"),
                    String.class, null);
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.lock") + "/"
                    + getInstance().getResource().getString("general.unlock"),
                    com.vaadin.ui.Component.class, null);
            table.addContainerProperty(getInstance().getResource()
                    .getString("general.password.reset") + "*",
                    com.vaadin.ui.Component.class, null);
            refreshUserTable(table);
            table.setSortContainerPropertyId(getInstance().getResource()
                    .getString("general.id"));
            form.getLayout().addComponent(table);
        }
        form.addField("username", new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.username") + ":"));
        form.getField("username").setRequired(userAdmin);
        form.getField("username").setRequiredError(getInstance().getResource()
                .getString("message.missing.username"));
        if (!userAdmin) {
            form.getField("username").setValue(loggedUser.getUsername());
        }
        form.addField("pass", new com.vaadin.ui.PasswordField(getInstance()
                .getResource().getString("general.password") + ":"));
        form.getField("pass").setRequired(userAdmin);
        form.getField("pass").setRequiredError(getInstance().getResource()
                .getString("message.missing.password"));
        form.addField("verify", new com.vaadin.ui.PasswordField(getInstance()
                .getResource().getString("general.verifypassword") + ":"));
        form.getField("verify").setRequired(userAdmin);
        form.getField("verify").setRequiredError(getInstance().getResource()
                .getString("message.missing.password"));
        form.addField("firstname", new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.firstname") + ":"));
        form.getField("firstname").setRequired(userAdmin);
        form.getField("firstname").setRequiredError(getInstance().getResource()
                .getString("message.missing.firstname"));
        if (!userAdmin) {
            form.getField("firstname").setValue(loggedUser.getFirstName());
        }
        form.addField("lastname", new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.lastname") + ":"));
        form.getField("lastname").setRequired(userAdmin);
        form.getField("lastname").setRequiredError(getInstance().getResource()
                .getString("message.missing.lastname"));
        if (!userAdmin) {
            form.getField("lastname").setValue(loggedUser.getLastName());
        }
        form.addField("email", new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.email") + ":"));
        form.getField("email").setRequired(userAdmin);
        form.getField("email").setRequiredError(getInstance().getResource()
                .getString("message.missing.email"));
        if (!userAdmin) {
            form.getField("email").setValue(loggedUser.getEmail());
        }
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                userAdmin ? getInstance().getResource().getString("general.add.user")
                        : getInstance().getResource().getString("general.save"),
                form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.cancel"),
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
                        if (!form.getField("pass").getValue().toString()
                                .equals(form.getField("verify").getValue().toString())) {
                            getMainWindow().showNotification(getInstance().getResource()
                                    .getString("window.userinfo.passwordmismatch"), TYPE_WARNING_MESSAGE);
                            commit.setEnabled(true);
                            cancel.setEnabled(true);
                            return;
                        }
                        temp_user = new XincoCoreUserServer(0,
                                form.getField("username").getValue().toString(),
                                form.getField("pass").getValue().toString(),
                                form.getField("lastname").getValue().toString(),
                                form.getField("firstname").getValue().toString(),
                                form.getField("email").getValue().toString(),
                                1, 0, new Timestamp(currentTimeMillis()));
                        temp_user.getXincoCoreGroups().add(new XincoCoreGroupServer(2));
                        temp_user.setWriteGroups(true);
                        temp_user.setHashPassword(true);
                        //Reason for change
                        temp_user.setReason("audit.user.account.create");
                        changed = true;
                    } else {
                        temp_user = new XincoCoreUserServer(loggedUser.getId());
                        if (!form.getField("username").getValue().toString()
                                .equals(loggedUser.getUsername())) {
                            loggedUser.setUsername(form.getField("username")
                                    .getValue().toString());
                            changed = true;
                        }
                        if (!form.getField("pass").getValue().toString().isEmpty()
                                && !form.getField("pass").getValue().toString()
                                        .equals(loggedUser.getUserpassword())) {
                            if (!form.getField("pass").getValue().toString()
                                    .equals(form.getField("verify").getValue()
                                            .toString())) {
                                getMainWindow().showNotification("window.userinfo.passwordmismatch", TYPE_WARNING_MESSAGE);
                                return;
                            } else {
                                loggedUser.setUserpassword(form.getField("pass")
                                        .getValue().toString());
                                loggedUser.setHashPassword(true);
                                changed = true;
                            }
                        }
                        if (!form.getField("username").getValue().toString()
                                .equals(loggedUser.getUsername())) {
                            loggedUser.setUsername(form.getField("username")
                                    .getValue().toString());
                            changed = true;
                        }
                        if (!form.getField("lastname").getValue().toString()
                                .equals(loggedUser.getLastName())) {
                            loggedUser.setLastName(form.getField("lastname")
                                    .getValue().toString());
                            changed = true;
                        }
                        if (!form.getField("firstname").getValue().toString()
                                .equals(loggedUser.getLastName())) {
                            loggedUser.setFirstName(form.getField("firstname")
                                    .getValue().toString());
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
                    boolean passwordIsUsable = loggedUser.isPasswordUsable(form
                            .getField("pass").getValue().toString());
                    if (!passwordIsUsable) {
                        getMainWindow().showNotification(getInstance()
                                .getResource().getString("password.unusable"), TYPE_WARNING_MESSAGE);
                        changed = false;
                    }
                    if (changed) {
                        temp_user.write2DB();
                        loggedUser = temp_user;
                    }
                    if (userAdmin) {
                        refreshUserTable(table);
                        commit.setEnabled(true);
                        cancel.setEnabled(true);
                    } else {
                        getMainWindow().removeWindow(user);
                    }
                    clearForm();
                } catch (XincoException ex) {
                    LOG.log(SEVERE, null, ex);
                }
            }

            private void clearForm() {
                for (Iterator<?> it = form.getItemPropertyIds().iterator();
                        it.hasNext();) {
                    String field = (String) it.next();
                    form.getField(field).setValue("");
                }
            }
        });
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        user.addComponent(form);
        user.setModal(true);
        user.center();
        user.setWidth(50, UNITS_PERCENTAGE);
        getMainWindow().addWindow(user);
    }

    private com.vaadin.ui.Component getSideMenu() throws XincoException {
        com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel();
        panel.addComponent(icon);
        menu = new Accordion();
        menu.setSizeFull();
        accountPanel = new com.vaadin.ui.Panel();
        accountPanel.setContent(new VerticalLayout());
        accountPanel.addComponent(login);
        accountPanel.addComponent(profile);
        accountPanel.addComponent(logout);
        if (login != null) {
            login.setWidth(100, UNITS_PERCENTAGE);
        }
        if (profile != null) {
            profile.setWidth(100, UNITS_PERCENTAGE);
        }
        if (logout != null) {
            logout.setWidth(100, UNITS_PERCENTAGE);
        }
        menu.addTab(accountPanel, getInstance().getResource()
                .getString("window.connection.profile"), null);
        adminPanel = new com.vaadin.ui.Panel();
        userAdmin = new com.vaadin.ui.Button(getInstance().getResource()
                .getString("message.admin.userAdmin"));
        userAdmin.setWidth(100, UNITS_PERCENTAGE);
        userAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                showUserAdminWindow(true);
            }
        });
        adminPanel.addComponent(userAdmin);
        groupAdmin = new com.vaadin.ui.Button(getInstance().getResource()
                .getString("message.admin.groupAdmin"));
        groupAdmin.setWidth(100, UNITS_PERCENTAGE);
        groupAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                showGroupAdminWindow();
            }
        });
        adminPanel.addComponent(groupAdmin);
        langAdmin = new com.vaadin.ui.Button(getInstance().getResource()
                .getString("message.admin.language"));
        langAdmin.setWidth(100, UNITS_PERCENTAGE);
        langAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                showLangAdminWindow();
            }
        });
        adminPanel.addComponent(langAdmin);
        attrAdmin = new com.vaadin.ui.Button(getInstance().getResource()
                .getString("message.admin.attribute"));
        attrAdmin.setWidth(100, UNITS_PERCENTAGE);
        attrAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                showAttrAdminWindow();
            }
        });
        adminPanel.addComponent(attrAdmin);
        trashAdmin = new com.vaadin.ui.Button(getInstance().getResource()
                .getString("message.admin.trash"));
        trashAdmin.setWidth(100, UNITS_PERCENTAGE);
        trashAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    (new XincoCoreNodeServer(2)).deleteFromDB(false,
                            loggedUser.getId());
                    refresh();
                } catch (Exception e) {
                    LOG.log(SEVERE, null, e);
                }
            }
        });
        adminPanel.addComponent(trashAdmin);
        indexAdmin = new com.vaadin.ui.Button(getInstance().getResource()
                .getString("message.admin.index"));
        indexAdmin.setWidth(100, UNITS_PERCENTAGE);
        final Table table = new Table();
        table.addStyleName("striped");
        table.addContainerProperty(getInstance().getResource()
                .getString("message.data.sort.designation"),
                com.vaadin.ui.Label.class, null);
        table.addContainerProperty(getInstance().getResource()
                .getString("message.indexing.status"),
                com.vaadin.ui.Label.class, null);
        indexAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {
            final ProgressIndicator indicator = new ProgressIndicator(new Float(0.0));
            com.vaadin.ui.Button ok = new com.vaadin.ui.Button(getInstance()
                    .getResource().getString("general.ok"));

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    java.util.List result
                            = createdQuery("SELECT x FROM "
                                    + "XincoCoreData x ORDER BY x.designation");
                    final com.vaadin.ui.Window progress
                            = new com.vaadin.ui.Window();
                    progress.addComponent(indicator);
                    // Set polling frequency to 0.5 seconds.
                    indicator.setPollingInterval(500);
                    progress.addComponent(new com.vaadin.ui.Label(getInstance()
                            .getResource().getString("message.index.rebuild")));
                    progress.addComponent(new com.vaadin.ui.Label(getInstance()
                            .getResource().getString("message.warning.index.rebuild")));
                    progress.addComponent(table);
                    ok.addListener(new com.vaadin.ui.Button.ClickListener() {
                        @Override
                        public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                            getMainWindow().removeWindow(progress);
                        }
                    });
                    progress.addComponent(ok);
                    progress.setWidth(50, UNITS_PERCENTAGE);
                    ok.setEnabled(false);
                    getMainWindow().addWindow(progress);
                    progress.center();
                    try {
                        //Delete existing index
                        File indexDirectory;
                        File indexDirectoryFile;
                        String[] indexDirectoryFileList;
                        indexDirectory = new File(CONFIG.fileIndexPath);
                        int work_units = result.size() + (indexDirectory.exists()
                                ? indexDirectory.list().length + 1 : 0) + 1;
                        int index = 0;
                        int count = 0;
                        if (indexDirectory.exists()) {
                            indexDirectoryFileList = indexDirectory.list();
                            for (String indexDirectoryFileList1
                                    : indexDirectoryFileList) {
                                indexDirectoryFile = new File(CONFIG.fileIndexPath + indexDirectoryFileList1);
                                indexDirectoryFile.delete();
                                count++;
                            }
                            boolean indexDirectoryDeleted = indexDirectory.delete();
                            count++;
                            table.addItem(new Object[]{new com.vaadin.ui.Label(
                                getInstance().getResource()
                                .getString("message.index.delete")),
                                new com.vaadin.ui.Label(indexDirectoryDeleted
                                ? getInstance().getResource().getString("general.ok") + "!" : getInstance().getResource().getString("general.fail") + "!")}, index++);
                            XincoCoreDataServer xdataTemp;
                            boolean index_result;
                            for (Object o : result) {
                                xdataTemp = new XincoCoreDataServer((com.bluecubs.xinco.core.server.persistence.XincoCoreData) o);
                                if (!isRendering(xdataTemp.getId())) {
                                    index_result = indexXincoCoreData(xdataTemp, true);
                                    table.addItem(new Object[]{new com.vaadin.ui.Label(xdataTemp.getDesignation()),
                                        new com.vaadin.ui.Label(index_result
                                        ? getInstance().getResource().getString("general.ok") + "!" : getInstance().getResource().getString("general.fail"))}, index++);
                                    count++;
                                    indicator.setValue(new Float(count) / new Float(work_units));
                                }
                            }
                            index_result = optimizeIndex();
                            //Optimize index
                            table.addItem(new Object[]{new com.vaadin.ui.Label(getInstance().getResource().getString("message.index.optimize")),
                                new com.vaadin.ui.Label(index_result
                                ? getInstance().getResource().getString("general.ok") + "!" : getInstance().getResource().getString("general.fail"))}, index++);
                            count++;
                        }
                        indicator.setValue(new Float(1.0));
                        ok.setEnabled(true);
                    } catch (UnsupportedOperationException ex) {
                        LOG.log(SEVERE, null, ex);
                    }
                } catch (XincoException ex) {
                    LOG.log(SEVERE, null, ex);
                    ok.setEnabled(true);
                }
            }
        });
        adminPanel.addComponent(indexAdmin);
        auditAdmin = new com.vaadin.ui.Button(getInstance().getResource()
                .getString("general.audit.menu"));
        auditAdmin.setWidth(100, UNITS_PERCENTAGE);
        auditAdmin.addListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                try {
                    showAuditWindow();
                } catch (XincoException ex) {
                    LOG.log(SEVERE, null, ex);
                }
            }
        });
        adminPanel.addComponent(auditAdmin);
        settingAdmin = new com.vaadin.ui.Button(getInstance().getResource()
                .getString("message.admin.settingAdmin"));
        settingAdmin.setWidth(100, UNITS_PERCENTAGE);
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
            for (Iterator<Object> it = loggedUser.getXincoCoreGroups().iterator();
                    it.hasNext();) {
                XincoCoreGroup xcg = (XincoCoreGroup) it.next();
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
        ArrayList<XincoCoreUserServer> allusers = getXincoCoreUsers();
        for (XincoCoreUserServer tempUser : allusers) {
            com.vaadin.ui.Button lock = new com.vaadin.ui.Button(getInstance()
                    .getResource().getString("general.lock"));
            lock.setData(tempUser.getId());
            lock.addStyleName("link");
            lock.addListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreUserServer temp_user
                                = new XincoCoreUserServer((Integer) event
                                        .getButton().getData());
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
                        LOG.log(SEVERE, null, ex);
                    }
                }
            });
            com.vaadin.ui.Button unlock = new com.vaadin.ui.Button(getInstance()
                    .getResource().getString("general.unlock"));
            unlock.setData(tempUser.getId());
            unlock.addStyleName("link");
            unlock.addListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreUserServer temp_user
                                = new XincoCoreUserServer((Integer) event
                                        .getButton().getData());
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
                        LOG.log(SEVERE, null, ex);
                    }
                }
            });
            com.vaadin.ui.Button reset = new com.vaadin.ui.Button(getInstance()
                    .getResource().getString("general.password.reset"));
            reset.setData(tempUser.getId());
            reset.addStyleName("link");
            reset.addListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreUserServer temp_user
                                = new XincoCoreUserServer((Integer) event.getButton()
                                        .getData());
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
                        LOG.log(SEVERE, null, ex);
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
        table1.addContainerProperty(getInstance().getResource().getString("general.id"),
                Integer.class, null);
        table1.addContainerProperty(getInstance().getResource().getString("general.username"),
                String.class, null);
        table1.addContainerProperty(getInstance().getResource().getString("general.firstname"),
                String.class, null);
        table1.addContainerProperty(getInstance().getResource().getString("general.lastname"),
                String.class, null);
        table1.addContainerProperty(getInstance().getResource().getString("general.email"),
                String.class, null);
        table1.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table1.setSortContainerPropertyId(getInstance().getResource().getString("general.id"));
        table1.setPageLength(10);
        final Table table2 = new Table();
        table2.addStyleName("striped");
        table2.addContainerProperty(getInstance().getResource().getString("general.id"),
                Integer.class, null);
        table2.addContainerProperty(getInstance().getResource().getString("general.username"),
                String.class, null);
        table2.addContainerProperty(getInstance().getResource().getString("general.firstname"),
                String.class, null);
        table2.addContainerProperty(getInstance().getResource().getString("general.lastname"),
                String.class, null);
        table2.addContainerProperty(getInstance().getResource().getString("general.email"),
                String.class, null);
        table2.addContainerProperty("", com.vaadin.ui.Component.class, null);
        table2.setSortContainerPropertyId(getInstance().getResource().getString("general.id"));
        table1.setPageLength(2);
        ArrayList<XincoCoreUserServer> allusers = getXincoCoreUsers();
        boolean member_ofGroup;
        form.getLayout().removeAllComponents();
        for (XincoCoreUserServer tempUser : allusers) {
            member_ofGroup = false;
            for (Iterator<Object> it2 = tempUser.getXincoCoreGroups().iterator(); it2.hasNext();) {
                if (((XincoCoreGroup) it2.next()).getId() == groupId) {
                    member_ofGroup = true;
                    break;
                }
            }
            final com.vaadin.ui.Button remove = new com.vaadin.ui.Button(getInstance().getResource().getString("general.group.removeuser"));
            remove.setData(tempUser.getId());
            remove.addStyleName("link");
            remove.addListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    //main admin always is admin and everyone is a regular user
                    if (!(((groupId == 1) && ((Integer) remove.getData() == 1)) || (groupId == 2))) {
                        try {
                            java.util.List results = createdQuery("SELECT x FROM XincoCoreUserHasXincoCoreGroup x "
                                    + "WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = " + (Integer) remove.getData()
                                    + " and x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = " + groupId);
                            for (Object o : results) {
                                new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory()).destroy(((XincoCoreUserHasXincoCoreGroup) o).getXincoCoreUserHasXincoCoreGroupPK());
                            }
                        } catch (NonexistentEntityException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    } else {
                        getMainWindow().showNotification(getInstance().getResource().getString("error.user.remove.mainUserGroup"), TYPE_WARNING_MESSAGE);
                    }
                    refreshGroupContentsTables(form, groupId);
                }
            });
            final com.vaadin.ui.Button add = new com.vaadin.ui.Button(getInstance().getResource().getString("general.group.adduser"));
            add.setData(tempUser.getId());
            add.addStyleName("link");
            add.addListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup(
                                (Integer) add.getData(), groupId);
                        uhg.setXincoCoreGroup(new XincoCoreGroupJpaController(getEntityManagerFactory()).findXincoCoreGroup(groupId));
                        uhg.setXincoCoreUser(new XincoCoreUserJpaController(getEntityManagerFactory()).findXincoCoreUser((Integer) add.getData()));
                        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory()).create(uhg);
                    } catch (Exception ex) {
                        LOG.log(SEVERE, null, ex);
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
        form.getLayout().addComponent(new com.vaadin.ui.Label(getInstance()
                .getResource().getString("general.group.member")));
        form.getLayout().addComponent(table1);
        form.getLayout().addComponent(new com.vaadin.ui.Label(getInstance()
                .getResource().getString("general.group.notmember")));
        form.getLayout().addComponent(table2);
    }

    private void refreshFileTypeAttrTable(final Table table, final int dataTypeId) {
        ArrayList<XincoCoreDataTypeAttribute> alldatatypes
                = (ArrayList<XincoCoreDataTypeAttribute>) getXincoCoreDataType(dataTypeId)
                        .getXincoCoreDataTypeAttributes();
        table.removeAllItems();
        for (XincoCoreDataTypeAttribute type : alldatatypes) {
            final com.vaadin.ui.Button delete
                    = new com.vaadin.ui.Button(getInstance().getResource()
                            .getString("general.delete") + "*");
            delete.setData(type.getAttributeId());
            delete.addStyleName("link");
            delete.addListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        XincoCoreDataTypeAttributeServer tempAttribute
                                = new XincoCoreDataTypeAttributeServer(dataTypeId, (Integer) delete.getData());
                        deleteFromDB(tempAttribute, loggedUser.getId());
                        refreshFileTypeAttrTable(table, dataTypeId);
                    } catch (Exception e) {
                        LOG.log(SEVERE, null, e);
                    }
                }
            });
            table.addItem(new Object[]{type.getAttributeId(),
                getInstance().getResource().containsKey(type.getDesignation())
                ? getInstance().getResource().getString(type.getDesignation())
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
        ArrayList alldatatypes = getXincoCoreDataTypes();
        table.removeAllItems();
        for (Iterator<XincoCoreDataTypeServer> it = alldatatypes.iterator(); it.hasNext();) {
            XincoCoreDataTypeServer type = it.next();
            final com.vaadin.ui.Button edit = new com.vaadin.ui.Button(getInstance().getResource().getString("general.edit"));
            edit.setData(type.getId());
            edit.addStyleName("link");
            edit.addListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    try {
                        showDataTypeAttrAdminWindow((Integer) edit.getData());
                    } catch (Exception e) {
                        LOG.log(SEVERE, null, e);
                    }
                }
            });
            table.addItem(new Object[]{type.getId(),
                getInstance().getResource().containsKey(type.getDesignation())
                ? getInstance().getResource().getString(type.getDesignation())
                : type.getDesignation(),
                getInstance().getResource().containsKey(type.getDescription())
                ? getInstance().getResource().getString(type.getDescription())
                : type.getDescription(),
                edit}, type.getId());
        }
        table.sort();
    }

    private void initMenuItems() {
        XincoMenuItem item;
        int i = 0;
        item = new XincoMenuItemBuilder().setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.repository.refresh")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            refresh();
                        } catch (XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                }).setLoggedIn(false).setDataOnly(false).setNodeOnly(false)
                .setSelected(false).createXincoMenuItem();
        addItem(item);
        item = new XincoMenuItemBuilder().setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.repository.addfolder")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //Show the Data Folder Dialog window
                        showDataFolderDialog(true);
                    }
                }).setLoggedIn(true)
                .setDataOnly(false)
                .setNodeOnly(true)
                .setSelected(true)
                .createXincoMenuItem();
        addItem(item);
        item = new XincoMenuItemBuilder().setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.repository.adddata")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //Show the Data Folder Dialog window
                        showDataDialog(true);
                    }
                }).setLoggedIn(true).setDataOnly(false).setNodeOnly(true)
                .setSelected(true).createXincoMenuItem();
        addItem(item);
        item = new XincoMenuItemBuilder().setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.repository.adddatastructure")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    StringBuilder sb = new StringBuilder();

                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //Show the Data Structure Dialog window
                        final com.vaadin.ui.Window w = new com.vaadin.ui.Window("Mass import");
                        MultiFileUpload fileUpload = new MultiFileUpload() {
                            @Override
                            protected void handleFile(File file, String fileName,
                                    String mimeType, long length) {
                                if (length == 0) {
                                    //Empty file or folder
                                    if (sb.toString().isEmpty()) {
                                        sb.append(getInstance().getResource().getString("window.massiveimport.notsupported"));
                                    }
                                    sb.append("\n").append(fileName);
                                    getMainWindow().showNotification(sb.toString(), TYPE_TRAY_NOTIFICATION);
                                } else {
                                    getMainWindow().showNotification(getInstance().getResource().getString("window.massiveimport.progress"), TYPE_TRAY_NOTIFICATION);
                                    data = new XincoCoreData();
                                    getXincoCoreData().setXincoCoreDataType(
                                            new XincoCoreDataTypeServer(1));
                                    getXincoCoreData().setStatusNumber(1);
                                    getXincoCoreData().setXincoCoreLanguage(
                                            new XincoCoreLanguageServer(2));
                                    getXincoCoreData().setXincoCoreNodeId(valueOf(xincoTree.getValue()
                                            .toString().substring(xincoTree.getValue()
                                                    .toString().indexOf('-') + 1)));
                                    addDefaultAddAttributes(getXincoCoreData());
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
                }).setLoggedIn(true).setDataOnly(false).setNodeOnly(true).setSelected(true).createXincoMenuItem();
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.edit.folderdata")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        if (xincoTree.getValue().toString().startsWith("node")) {
                            showDataFolderDialog(false);
                        } else if (xincoTree.getValue().toString().startsWith("data")) {
                            showDataDialog(false);
                        }
                    }
                }).setLoggedIn(true)
                .setDataOnly(false)
                .setNodeOnly(false)
                .setSelected(true)
                .createXincoMenuItem();
        item.setStatuses(new int[]{1, 4});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.repository.vieweditaddattributes")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        //Show the Data Folder Dialog window
                        showDataDialog(false);
                    }
                }).setLoggedIn(true)
                .setDataOnly(true)
                .setNodeOnly(false)
                .setSelected(true)
                .createXincoMenuItem();
        item.setStatuses(new int[]{1, 4});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.edit.acl")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        showACLDialog();
                    }
                }).setLoggedIn(true)
                .setDataOnly(false)
                .setNodeOnly(false)
                .setSelected(true)
                .createXincoMenuItem();
        item.setStatuses(new int[]{1, 4});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.repository.downloadfile")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            downloadFile();
                        } catch (MalformedURLException | XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                }).setLoggedIn(false).
                setDataOnly(false).
                setNodeOnly(false).
                setSelected(true).
                createXincoMenuItem();
        item.setDataTypes(new int[]{1});
        addItem(item);
        //TODO: Enable Rendering support
        if (renderingSupportEnabled) {
            addItem(item);
            item = new XincoMenuItemBuilder()
                    .setIndex(i += 1000)
                    .setGroupName("menu.repository")
                    .setName("menu.repository.addrendering")
                    .setIcon(smallIcon)
                    .setCommand(new com.vaadin.ui.MenuBar.Command() {
                        @Override
                        public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                            try {
                                showRenderingDialog();
                            } catch (XincoException ex) {
                                LOG.log(SEVERE, null, ex);
                            }
                        }
                    }).setLoggedIn(true)
                    .setDataOnly(true)
                    .setNodeOnly(false)
                    .setSelected(true)
                    .createXincoMenuItem();
            item.setDataTypes(new int[]{1});
            addItem(item);
        }
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.edit.checkoutfile")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        checkoutFile();
                    }
                })
                .setLoggedIn(true)
                .setDataOnly(true)
                .setNodeOnly(false)
                .setSelected(true)
                //Only valid for files
                .setValidDataTypes(new int[]{1})
                .createXincoMenuItem();
        item.setStatuses(new int[]{1});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.edit.undocheckout")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        undoCheckoutFile();
                    }
                }).setLoggedIn(true).setDataOnly(true).setNodeOnly(false).setSelected(true).createXincoMenuItem();
        item.setStatuses(new int[]{4});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.edit.checkinfile")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        checkinFile();
                    }
                }).setLoggedIn(true)
                .setDataOnly(true)
                .setNodeOnly(false)
                .setSelected(true)
                .createXincoMenuItem();
        item.setStatuses(new int[]{4});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.edit.publishdata")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            publishData();
                        } catch (MalformedURLException | XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                }).setLoggedIn(true)
                .setDataOnly(true)
                .setNodeOnly(false)
                .setSelected(true)
                .createXincoMenuItem();
        item.setStatuses(new int[]{1, 2});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.edit.lockdata")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            lockData();
                        } catch (MalformedURLException | XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                }).setLoggedIn(true)
                .setDataOnly(true)
                .setNodeOnly(false)
                .setSelected(true)
                .createXincoMenuItem();
        item.setStatuses(new int[]{1});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.edit.downloadrevision")
                .setIcon(smallIcon).setCommand(new com.vaadin.ui.MenuBar.Command() {
            @Override
            public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                showDownloadRevDialog();
            }
        }).setLoggedIn(true)
                .setDataOnly(true)
                .setNodeOnly(false)
                .setSelected(true)
                .createXincoMenuItem();
        item.setStatuses(new int[]{1});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.repository")
                .setName("menu.edit.commentdata")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            final XincoCoreDataServer xdata = new XincoCoreDataServer(
                                    valueOf(xincoTree.getValue().toString().substring(
                                            xincoTree.getValue().toString().indexOf('-') + 1)));
                            showCommentDataDialog(xdata, COMMENT);
                        } catch (XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                }).setLoggedIn(true)
                .setDataOnly(true)
                .setNodeOnly(false)
                .setSelected(true)
                .createXincoMenuItem();
        item.setStatuses(new int[]{1});
        addItem(item);
        item = new XincoMenuItemBuilder()
                .setIndex(i += 1000)
                .setGroupName("menu.search")
                .setName("menu.search.search_repository")
                .setIcon(smallIcon)
                .setCommand(new com.vaadin.ui.MenuBar.Command() {
                    @Override
                    public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                        try {
                            showLuceneSearchWindow();
                        } catch (XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                }).setLoggedIn(false)
                .setDataOnly(false)
                .setNodeOnly(false)
                .setSelected(false)
                .createXincoMenuItem();
        addItem(item);
    }

    public boolean selectNode(String nodeId) {
        return getXincoTree() == null ? false : getXincoTree().expandItem(nodeId);
    }

    public boolean expandTreeNodes(java.util.List<Integer> parents) {
        boolean result = true;
        if (getXincoTree() == null) {
            result = false;
        }
        for (Integer id : parents) {
            if (!getXincoTree().expandItem("node-" + id)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * @return the data
     */
    protected XincoCoreData getXincoCoreData() {
        return data;
    }

    private void showCommentDataDialog(final XincoCoreDataServer data,
            final OPCode opcode) throws XincoException {
        final Form form = new Form();
        final int log_index = data.getXincoCoreLogs().size() - 1;
        final VersionSelector versionSelector
                = new VersionSelector(getInstance().getResource().getString("general.version"),
                        ((XincoCoreLog) data.getXincoCoreLogs().get(log_index)).getVersion());
        buildLogDialog(data, form, versionSelector, opcode);
        final com.vaadin.ui.Window comment = new com.vaadin.ui.Window();
        //Used for validation purposes
        final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.continue"), form, "commit");
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getInstance().getResource().getString("general.cancel"),
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
                            + (((XincoCoreLog) data.getXincoCoreLogs().get(log_index)).getOpCode() == 3 ? getInstance().getResource().getString("general.status.checkedout") : form.getField("reason").getValue());
                    newLog.setOpDescription(text);
                    text = "" + versionSelector.getVersion().getVersionHigh();
                    try {
                        newLog.getVersion().setVersionHigh(parseInt(text));
                    } catch (NumberFormatException nfe) {
                        newLog.getVersion().setVersionHigh(0);
                    }
                    text = "" + versionSelector.getVersion().getVersionMid();
                    try {
                        newLog.getVersion().setVersionMid(parseInt(text));
                    } catch (NumberFormatException nfe) {
                        newLog.getVersion().setVersionMid(0);
                    }
                    text = "" + versionSelector.getVersion().getVersionLow();
                    try {
                        newLog.getVersion().setVersionLow(parseInt(text));
                    } catch (NumberFormatException nfe) {
                        newLog.getVersion().setVersionLow(0);
                    }
                    text = versionSelector.getVersion().getVersionPostfix() == null
                            ? "" : versionSelector.getVersion().getVersionPostfix();
                    newLog.getVersion().setVersionPostfix(text);
                    newLog.write2DB();
                    data.write2DB();
                    getMainWindow().removeWindow(comment);
                    refresh();
                } catch (MalformedURLException | XincoException ex) {
                    LOG.log(SEVERE, null, ex);
                }
            }
        });
        versionSelector.setEnabled(false);
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(commit);
        form.getFooter().addComponent(cancel);
        comment.setEnabled(true);
        comment.addComponent(form);
        comment.setModal(true);
        comment.center();
        comment.setWidth(35, UNITS_PERCENTAGE);
        getMainWindow().addWindow(comment);
    }

    private void showDownloadRevDialog() {
        try {
            final com.vaadin.ui.Window revWindow = new com.vaadin.ui.Window(getInstance().getResource().getString("window.revision"));
            final Form form = new Form();
            form.getLayout().addComponent(new com.vaadin.ui.Label(getInstance().getResource().getString("window.revision")));
            Select rev = new Select();
            int i;
            String text;
            Calendar cal;
            XMLGregorianCalendar realcal;
            Calendar ngc = new GregorianCalendar();
            final XincoCoreDataServer xdata = new XincoCoreDataServer(valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
            for (i = 0; i < xdata.getXincoCoreLogs().size(); i++) {
                if ((((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getOpCode() == 1)
                        || (((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getOpCode() == 5)) {
                    //convert clone from remote time to local time
                    Date time = ((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getOpDatetime().toGregorianCalendar().getTime();
                    cal = new GregorianCalendar();
                    cal.setTime(time);
                    realcal = (((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getOpDatetime());
                    cal.add(MILLISECOND, (ngc.get(ZONE_OFFSET)
                            - realcal.toGregorianCalendar().get(ZONE_OFFSET))
                            - (ngc.get(DST_OFFSET)
                            + realcal.toGregorianCalendar().get(DST_OFFSET)));
                    text = "" + cal.get(YEAR) + "/" + (cal.get(MONTH) + 1) + "/" + cal.get(DAY_OF_MONTH);
                    text = text + " - " + getInstance().getResource().getString("general.version") + " " + ((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getVersion().getVersionHigh()
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
            form.getField("rev").setRequiredError(getInstance().getResource().getString("message.missing.rev"));
            form.setFooter(new HorizontalLayout());
            //Used for validation purposes
            final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                    getInstance().getResource().getString("general.continue"), form, "commit");
            final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                    getInstance().getResource().getString("general.cancel"),
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
                        XincoCoreLog revLog = null;
                        int idToLook = valueOf(
                                form.getField("rev").getValue().toString());
                        for (int i = 0; i < xdata.getXincoCoreLogs().size(); i++) {
                            if (((XincoCoreLog) xdata.getXincoCoreLogs().get(i)).getId()
                                    == idToLook) {
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
                            //Use rendering if available and enabled
                            downloadFile(true, xdata);
                        } catch (MalformedURLException | XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                        getMainWindow().removeWindow(revWindow);
                        refresh();
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
                    }
                }
            });
            form.getFooter().setSizeUndefined();
            form.getFooter().addComponent(commit);
            form.getFooter().addComponent(cancel);
            revWindow.addComponent(form);
            revWindow.setModal(true);
            revWindow.center();
            revWindow.setWidth(35, UNITS_PERCENTAGE);
            getMainWindow().addWindow(revWindow);
        } catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
        }
    }

    private void showDataDialog(final boolean newData) {
        final XincoWizard wizard = new XincoWizard(getLocale());
        final WizardStep dataDetails = new WizardStep() {
            @Override
            public String getCaption() {
                return getInstance().getResource().getString(
                        "window.datadetails");
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
                if (dataDialog.getDesignationField().getValue()
                        .toString().isEmpty()) {
                    getMainWindow().showNotification(getInstance().getResource().getString(
                            "message.missing.designation"), TYPE_ERROR_MESSAGE);
                    value = false;
                }
                if (dataDialog.getLanguages().getValue() == null) {
                    getMainWindow().showNotification(getInstance().getResource().getString(
                            "message.missing.language"), TYPE_ERROR_MESSAGE);
                    value = false;
                } else {
                    //Process data
                    getXincoCoreData().setDesignation(
                            dataDialog.getDesignationField().getValue()
                                    .toString());
                    getXincoCoreData().setXincoCoreLanguage((XincoCoreLanguage) getXincoCoreLanguages().get(valueOf(dataDialog.getLanguages()
                            .getValue().toString())));
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
                    return getInstance().getResource().getString(
                            "window.addattributesuniversal");
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    if (attrDialog == null) {
                        attrDialog = new AddAttributeDialog(getXincoCoreData());
                        attrDialog.setSizeFull();
                    }
                    return attrDialog;
                }

                @Override
                public boolean onAdvance() {
                    //Process the data
                    attrDialog.updateAttributes();
                    //True if there are more steps after this one
                    return wizard.getSteps().size()
                            > wizard.getLastCompleted() + 1;
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
                    return getInstance().getResource()
                            .getString("window.datatype");
                }

                @Override
                public String toString() {
                    return getCaption();
                }

                @Override
                public com.vaadin.ui.Component getContent() {
                    if (dataTypeDialog == null) {
                        dataTypeDialog = new DataTypeDialog();
                        dataTypeDialog.setSizeFull();
                        dataTypeDialog.getTypes().addListener(new ValueChangeListener() {
                            ArrayList<WizardStep> temp
                                    = new ArrayList<>();

                            @Override
                            public void valueChange(ValueChangeEvent event) {
                                data = new XincoCoreData();
                                try {
                                    //Process data
                                    getXincoCoreData().setXincoCoreDataType(new XincoCoreDataTypeServer(
                                            valueOf(dataTypeDialog
                                                    .getTypes().getValue().toString())));
                                } catch (XincoException ex) {
                                    LOG.log(SEVERE, null, ex);
                                }
                                //Set the parent id to the current selected node
                                getXincoCoreData().setXincoCoreNodeId(valueOf(xincoTree.getValue()
                                        .toString().substring(xincoTree
                                                .getValue().toString().indexOf('-')
                                                + 1)));
                                addDefaultAddAttributes(
                                        getXincoCoreData());
                                //wizard.getLastCompleted() is the previous step,
                                //the current is wizard.getLastCompleted() + 1,
                                //the next step wizard.getLastCompleted() + 2
                                switch (getXincoCoreData()
                                        .getXincoCoreDataType().getId()) {
                                    //File = 1
                                    case 1:
                                        clearTempSteps();
                                        addAttributeStep();
                                        temp.add(fileStep);
                                        wizard.addStep(temp.get(temp.size() - 1),
                                                wizard.getLastCompleted() + 1);
                                        //revision model
                                        getXincoCoreData()
                                                .getXincoAddAttributes().get(3)
                                                .setAttribUnsignedint(1);
                                        //archiving model
                                        getXincoCoreData()
                                                .getXincoAddAttributes().get(4)
                                                .setAttribUnsignedint(0);
                                        //Is a file, show archiving dialog
                                        temp.add(new WizardStep() {
                                            @Override
                                            public String getCaption() {
                                                return getInstance()
                                                        .getResource()
                                                        .getString(
                                                                "window.archive");
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
                                                try {
                                                    //Save values from wizard
                                                    archDialog.updateAttributes();
                                                } catch (DatatypeConfigurationException ex) {
                                                    LOG.log(SEVERE, null, ex);
                                                    throw new XincoException(ex);
                                                }
                                                //True if there are more steps after this one
                                                return wizard.getSteps().size()
                                                        > wizard.getLastCompleted() + 1;
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
                                        final ExpandingTextArea tArea
                                                = new ExpandingTextArea();
                                        temp.add(new WizardStep() {
                                            @Override
                                            public String getCaption() {
                                                return getInstance()
                                                        .getResource()
                                                        .getString(
                                                                "window.addattributestext");
                                            }

                                            @Override
                                            public com.vaadin.ui.Component getContent() {
                                                return tArea;
                                            }

                                            @Override
                                            public boolean onAdvance() {
                                                if (tArea.getValue().toString().isEmpty()) {
                                                    getMainWindow().showNotification(getInstance()
                                                            .getResource()
                                                            .getString(
                                                                    "general.error"),
                                                            getInstance()
                                                                    .getResource()
                                                                    .getString(
                                                                            "message.missing.text"), TYPE_WARNING_MESSAGE);
                                                    return false;
                                                } else {
                                                    //Process the data
                                                    getXincoCoreData()
                                                            .getXincoAddAttributes()
                                                            .get(0).setAttribText(
                                                            tArea.getValue()
                                                                    .toString());
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
                                        wizard.addStep(temp.get(temp.size() - 1),
                                                wizard.getLastCompleted() + 1);
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
                        getMainWindow().showNotification(getInstance().getResource().getString(
                                "message.missing.datatype"), TYPE_ERROR_MESSAGE);
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
                data = getService().getXincoCoreData(new XincoCoreDataServer(
                        valueOf(xincoTree.getValue().toString()
                                .substring(xincoTree.getValue().toString()
                                        .indexOf('-') + 1))),
                        loggedUser);
            } catch (XincoException ex) {
                LOG.log(SEVERE, null, ex);
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
        wizardWindow.setWidth(40, UNITS_PERCENTAGE);
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

    private void addLog(XincoCoreData data, OPCode code) throws XincoException,
            MalformedURLException {
        //Add log
        XincoCoreLog newlog = new XincoCoreLog();
        newlog.setOpCode(code.ordinal() + 1);
        newlog.setOpDescription(getInstance().getResource().getString(getOPCode(newlog.getOpCode()).getName())
                + "!" + " ("
                + getInstance().getResource().getString("general.user") + ": "
                + loggedUser.getUsername()
                + ")");
        newlog.setXincoCoreUserId(loggedUser.getId());
        newlog.setXincoCoreDataId(data.getId());
        if (getCurrentVersion(data.getId()) == null) {
            newlog.setVersion(new XincoVersion());
            newlog.getVersion().setVersionHigh(1);
            newlog.getVersion().setVersionMid(0);
            newlog.getVersion().setVersionLow(0);
            newlog.getVersion().setVersionPostfix("");
        } else {
            newlog.setVersion(getCurrentVersion(data.getId()));
        }
        try {
            DatatypeFactory factory = newInstance();
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            newlog.setOpDatetime(factory.newXMLGregorianCalendar(cal));
        } catch (DatatypeConfigurationException ex) {
            LOG.log(SEVERE, null, ex);
        }
        // save log to server
        newlog = getService().setXincoCoreLog(newlog, loggedUser);
        if (newlog == null) {
            LOG.severe("Unable to create new log entry!");
        } else {
            data.getXincoCoreLogs().add(newlog);
        }
    }

    private void showDataFolderDialog(final boolean newFolder) {
        try {
            final XincoCoreNode node = new XincoCoreNodeServer(valueOf(xincoTree.getValue().toString().substring(xincoTree.getValue().toString().indexOf('-') + 1)));
            final com.vaadin.ui.Window dataFolderDialog = new com.vaadin.ui.Window();
            final Form form = new Form();
            form.setCaption(getInstance().getResource().getString("window.folder"));
            //ID
            com.vaadin.ui.TextField idField = new com.vaadin.ui.TextField(getInstance().getResource().getString("general.id") + ":");
            form.addField("id", idField);
            if (newFolder) {
                idField.setValue("0");
            } else {
                idField.setValue(node.getId());
            }
            //Not editable
            idField.setEnabled(false);
            //Designation
            com.vaadin.ui.TextField designationField = new com.vaadin.ui.TextField(getInstance().getResource().getString("general.designation") + ":");
            form.addField("designation", designationField);
            form.getField("designation").setRequired(true);
            form.getField("designation").setRequiredError(getInstance().getResource().getString("message.missing.designation"));
            if (!newFolder) {
                designationField.setValue(node.getDesignation());
            }
            //Language selection
            final Select languages = new Select(getInstance().getResource().getString("general.language") + ":");
            int i = 0;
            for (Object language : getXincoCoreLanguages()) {
                String designation = ((XincoCoreLanguageServer) language).getDesignation();
                if (getInstance().getResource().containsKey(designation)) {
                    String value = getInstance().getResource().getString(designation);
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
                    LOG.log(WARNING,
                            "{0} not defined in com.bluecubs.xinco.messages.XincoMessagesLocale",
                            "Locale." + designation);
                }
            }
            form.addField("lang", languages);
            form.getField("lang").setRequired(true);
            form.getField("lang").setRequiredError(getInstance().getResource().getString("message.missing.language"));
            //Status
            com.vaadin.ui.TextField statusField = new com.vaadin.ui.TextField(getInstance().getResource().getString("general.status") + ":");
            //Not editable
            statusField.setEnabled(false);
            String text = "";
            int status = 1;
            if (newFolder || node.getStatusNumber() == 1) {
                text = getInstance().getResource().getString("general.status.open");
            }
            if (node.getStatusNumber() == 2) {
                text = getInstance().getResource().getString("general.status.locked") + " (-)";
                status = node.getStatusNumber();
            }
            if (node.getStatusNumber() == 3) {
                text = getInstance().getResource().getString("general.status.archived") + " (->)";
                status = node.getStatusNumber();
            }
            statusField.setValue(text);
            form.addField("status", statusField);
            form.setFooter(new HorizontalLayout());
            //Used for validation purposes
            final com.vaadin.ui.Button commit = new com.vaadin.ui.Button(
                    getInstance().getResource().getString("general.save"), form, "commit");
            final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                    getInstance().getResource().getString("general.cancel"),
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
                                    valueOf(form.getField("id").getValue().toString()),
                                    node.getId(), //Use selected node's id as parent
                                    ((XincoCoreLanguage) getXincoCoreLanguages().get(valueOf(languages.getValue().toString()))).getId(),
                                    form.getField("designation").getValue().toString(),
                                    finalStatus);
                        } else {
                            tempNode = new XincoCoreNodeServer(
                                    valueOf(form.getField("id").getValue().toString()));
                            //Update with changes
                            tempNode.setDesignation(form.getField("designation").getValue().toString());
                            tempNode.setXincoCoreLanguage(((XincoCoreLanguage) getXincoCoreLanguages().get(valueOf(languages.getValue().toString()))));
                        }
                        tempNode.setChangerID(getLoggedUser().getId());
                        //Call the web service
                        if (getService().setXincoCoreNode(tempNode, loggedUser) == null) {
                            getMainWindow().showNotification(getInstance().getResource().getString("error.accessdenied"),
                                    getInstance().getResource().getString("error.folder.sufficientrights"), TYPE_ERROR_MESSAGE);
                        }
                        getMainWindow().removeWindow(dataFolderDialog);
                        refresh();
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
                    }
                }
            });
            form.getFooter().setSizeUndefined();
            form.getFooter().addComponent(commit);
            form.getFooter().addComponent(cancel);
            dataFolderDialog.addComponent(form);
            dataFolderDialog.setModal(true);
            dataFolderDialog.center();
            dataFolderDialog.setWidth(25, UNITS_PERCENTAGE);
            getMainWindow().addWindow(dataFolderDialog);
        } catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
        }
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
                    removeFromDB(loggedUser.getId(),
                            getXincoCoreData().getId());
                }
                //Remove the file from the repository as well
                closeWizard();
            } catch (XincoException ex) {
                LOG.log(SEVERE, null, ex);
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
                        for (int j = 0; j < getXincoCoreLanguages().size(); j++) {
                            if (((XincoCoreLanguage) getXincoCoreLanguages().get(j)).getSign().toLowerCase().compareTo(getDefault().getLanguage().toLowerCase()) == 0) {
                                selection = j;
                                break;
                            }
                            if (((XincoCoreLanguage) getXincoCoreLanguages().get(j)).getId() == 1) {
                                alt_selection = j;
                            }
                        }
                        if (selection == -1) {
                            selection = alt_selection;
                        }
                        xcl1 = (XincoCoreLanguage) getXincoCoreLanguages().get(selection);
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
                                throw new XincoException(
                                        getInstance().getResource()
                                                .getString("datawizard.unabletosavedatatoserver"));
                            }
                            break;
                    }
                    addLog(getXincoCoreData(),
                            getXincoCoreData().getXincoCoreLogs().isEmpty()
                            ? CREATION : CHECKIN);
                } catch (XincoException | IOException ex) {
                    LOG.log(SEVERE, null, ex);
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
                LOG.log(SEVERE, null, ex);
            }
        }
    }

    private void refresh() throws XincoException {
        //Refresh and highlight current selection
        String selectedId = xincoTree.getValue() == null ? null : xincoTree.getValue().toString();
        XincoCoreNodeServer xcns = null;
        try {
            xcns = new XincoCoreNodeServer(1);
        } catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
        }
        if (xcns != null) {
            //Clear tree and start over
            xincoTreeContainer.removeAllItems();
            xincoTree.collapseItemsRecursively("node-1");
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
    }

    private void processTreeSelection(String source) {
        try {
            updateMenu();
        } catch (XincoException ex) {
            LOG.log(SEVERE, null, ex);
        }
        XincoCoreACE tempAce = new XincoCoreACE();
        getXincoTable().setVisible(getLoggedUser() != null);
        getXincoTable().requestRepaint();
        xeSplitPanel.setSplitPosition(getLoggedUser() != null ? 25
                : 100, UNITS_PERCENTAGE);
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
                    xcns = new XincoCoreNodeServer(valueOf(source.substring(source.indexOf('-') + 1)));
                } catch (XincoException ex) {
                    LOG.log(SEVERE, null, ex);
                }
                if (xcns != null) {
                    // Get ACE
                    try {
                        tempAce = checkAccess(new XincoCoreUserServer(getLoggedUser().getId()),
                                (ArrayList) (xcns).getXincoCoreAcl());
                    } catch (XincoException ex) {
                        LOG.log(SEVERE, null, ex);
                    }
                    if (!xincoTree.hasChildren(xincoTree.getValue()) && tempAce.isReadPermission()) {
                        try {
                            //Populate children
                            xcns.fillXincoCoreData();
                            xcns.fillXincoCoreNodes();
                            addChildren("node-" + xcns.getId());
                        } catch (XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    } else if (!tempAce.isReadPermission()) {
                        getMainWindow().showNotification(getInstance().getResource().getString("error.accessdenied"),
                                getInstance().getResource().getString("error.folder.sufficientrights"), TYPE_WARNING_MESSAGE);
                    }
                    // update details table
                    xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                    xincoTable.addItem(new Object[]{getInstance().getResource().getString("general.id"),
                        new com.vaadin.ui.Label("" + xcns.getId())}, i++);
                    xincoTable.addItem(new Object[]{getInstance().getResource().getString("general.designation"),
                        new com.vaadin.ui.Label(xcns.getDesignation())}, i++);
                    xincoTable.addItem(new Object[]{getInstance().getResource().getString("general.language"),
                        new com.vaadin.ui.Label(getInstance().getResource().getString(xcns.getXincoCoreLanguage().getDesignation()) + " ("
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
                        getInstance().getResource().getString("general.accessrights"),
                        new com.vaadin.ui.Label(value)}, i++);
                    xincoTable.addItem(new Object[]{"\u00a0",
                        new com.vaadin.ui.Label()}, i++);
                    value = "";
                    if (xcns.getStatusNumber() == 1) {
                        value = getInstance().getResource().getString("general.status.open") + "";
                    }
                    if (xcns.getStatusNumber() == 2) {
                        value = getInstance().getResource().getString("general.status.locked") + " (-)";
                    }
                    if (xcns.getStatusNumber() == 3) {
                        value = getInstance().getResource().getString("general.status.archived") + " (->)";
                    }
                    xincoTable.addItem(new Object[]{
                        getInstance().getResource().getString("general.status"),
                        new com.vaadin.ui.Label(value)}, i++);
                }
            } else if (source != null && source.startsWith("data")) {
                // get ace
                XincoCoreDataServer temp = null;
                try {
                    temp = new XincoCoreDataServer(valueOf(source.substring(source.indexOf('-') + 1)));
                    tempAce = checkAccess(new XincoCoreUserServer(1),
                            (ArrayList) (temp).getXincoCoreAcl());
                } catch (XincoException ex) {
                    LOG.log(SEVERE, null, ex);
                }
                if (temp != null) {
                    xincoTable.addItem(new Object[]{
                        getInstance().getResource().getString("general.id"),
                        new com.vaadin.ui.Label("" + temp.getId())}, i++);
                    xincoTable.addItem(new Object[]{
                        getInstance().getResource().getString("general.designation"),
                        new com.vaadin.ui.Label(temp.getDesignation())}, i++);
                    xincoTable.addItem(new Object[]{
                        getInstance().getResource().getString("general.language"),
                        new com.vaadin.ui.Label(getInstance().getResource().getString(temp.getXincoCoreLanguage().getDesignation())
                        + " ("
                        + temp.getXincoCoreLanguage().getSign()
                        + ")")}, i++);
                    xincoTable.addItem(new Object[]{
                        getInstance().getResource().getString("general.datatype"),
                        new com.vaadin.ui.Label(getInstance().getResource().getString(temp.getXincoCoreDataType().getDesignation())
                        + " ("
                        + getInstance().getResource().getString(temp.getXincoCoreDataType().getDescription())
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
                        getInstance().getResource().getString("general.accessrights"),
                        new com.vaadin.ui.Label("[" + value + "]")}, i++);
                    xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                    xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                    switch (temp.getStatusNumber()) {
                        case 1:
                            value = getInstance().getResource().getString("general.status.open");
                            break;
                        case 2:
                            value = getInstance().getResource().getString("general.status.locked");
                            break;
                        case 3:
                            value = getInstance().getResource().getString("general.status.archived");
                            break;
                        case 4:
                            value = getInstance().getResource().getString("general.status.checkedout");
                            break;
                        case 5:
                            value = getInstance().getResource().getString("general.status.published");
                            break;
                        default:
                            throw new XincoException("Invalid status: " + temp.getStatusNumber());
                    }
                    xincoTable.addItem(new Object[]{
                        getInstance().getResource().getString("general.status"),
                        new com.vaadin.ui.Label(value)}, i++);
                    xincoTable.addItem(new Object[]{"\u00a0",
                        new com.vaadin.ui.Label()}, i++);
                    xincoTable.addItem(new Object[]{
                        getInstance().getResource().getString("general.typespecificattributes"),
                        new com.vaadin.ui.Label()}, i++);
                    // get add attributes of Core Data, if access granted
                    java.util.List<XincoAddAttribute> attributes
                            = temp.getXincoAddAttributes();
                    java.util.List<XincoCoreDataTypeAttribute> dataTypeAttributes
                            = temp.getXincoCoreDataType().getXincoCoreDataTypeAttributes();
                    if (!attributes.isEmpty()) {
                        for (int j = 0; j < attributes.size(); j++) {
                            header = getInstance().getResource().containsKey(dataTypeAttributes.get(j).getDesignation())
                                    ? getInstance().getResource().getString(dataTypeAttributes.get(j).getDesignation())
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
                                value = (cal.get(MONTH) == 11
                                        && cal.get(YEAR) == 2
                                        && cal.get(DAY_OF_MONTH) == 31) ? "" : "" + time;
                            }
                            Link link = new Link(value, new ExternalResource(value));
                            link.setTargetName("_blank");
                            link.setTargetBorder(TARGET_BORDER_NONE);
                            try {
                                xincoTable.addItem(new Object[]{header, header.equals("URL")
                                    && getSetting(loggedUser, "setting.allowoutsidelinks", true).isBoolValue()
                                    ? link : new com.vaadin.ui.Label(value)}, i++);
                            } catch (XincoException ex) {
                                LOG.log(SEVERE, null, ex);
                            }
                        }
                    } else {
                        header = getInstance().getResource().getString("error.accessdenied");
                        value = getInstance().getResource().getString("error.content.sufficientrights");
                        xincoTable.addItem(new Object[]{header, new com.vaadin.ui.Label(value)}, i++);
                    }
                    //Only for files
                    if (temp.getXincoCoreDataType().getId() == 1) {
                        //Add permanent link
                        value = getInstance().getURL().toString() + "?dataId=" + temp.getId();
                        Link link = new Link(value, new ExternalResource(value));
                        link.setTargetName("_blank");
                        link.setTargetBorder(TARGET_BORDER_NONE);
                        try {
                            header = getInstance().getResource().getString("general.data.type.URL");
                            xincoTable.addItem(new Object[]{header, link}, i++);
                        } catch (XincoException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                    xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                    xincoTable.addItem(new Object[]{"\u00a0", new com.vaadin.ui.Label()}, i++);
                    header = getInstance().getResource().getString("general.logslastfirst");
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
                                cal.add(MILLISECOND,
                                        (ngc.get(ZONE_OFFSET)
                                        - realcal.toGregorianCalendar().get(ZONE_OFFSET))
                                        - (ngc.get(DST_OFFSET)
                                        + realcal.toGregorianCalendar().get(DST_OFFSET)));
                                header = ""
                                        + (cal.get(MONTH)
                                        + 1)
                                        + " / "
                                        + cal.get(DAY_OF_MONTH)
                                        + " / "
                                        + cal.get(YEAR)
                                        + " ";
                                if (cal.get(HOUR_OF_DAY) < 10) {
                                    header += "0" + cal.get(HOUR_OF_DAY) + ":";
                                } else {
                                    header += cal.get(HOUR_OF_DAY) + ":";
                                }
                                if (cal.get(MINUTE) < 10) {
                                    header += "0" + cal.get(MINUTE) + ":";
                                } else {
                                    header += cal.get(MINUTE) + ":";
                                }
                                if (cal.get(SECOND) < 10) {
                                    header += "0" + cal.get(SECOND);
                                } else {
                                    header += cal.get(SECOND);
                                }
                            } catch (Exception ce) {
                                LOG.log(SEVERE, null, ce);
                            }
                        }
                        value = "("
                                + ((XincoCoreLog) temp.getXincoCoreLogs().get(j))
                                        .getOpCode()
                                + ") "
                                + ((XincoCoreLog) temp.getXincoCoreLogs().get(j))
                                        .getOpDescription();
                        xincoTable.addItem(new Object[]{header,
                            new com.vaadin.ui.Label(value)}, i++);
                        header = "";
                        try {
                            value = getInstance().getResource()
                                    .getString("general.version")
                                    + " "
                                    + ((XincoCoreLog) temp.getXincoCoreLogs().get(j))
                                            .getVersion().getVersionHigh()
                                    + "."
                                    + ((XincoCoreLog) temp.getXincoCoreLogs().get(j))
                                            .getVersion().getVersionMid()
                                    + "."
                                    + ((XincoCoreLog) temp.getXincoCoreLogs().get(j))
                                            .getVersion().getVersionLow()
                                    + ""
                                    + ((XincoCoreLog) temp.getXincoCoreLogs().get(j))
                                            .getVersion().getVersionPostfix();
                            xincoTable.addItem(new Object[]{header,
                                new com.vaadin.ui.Label(value)}, i++);
                        } catch (UnsupportedOperationException ex) {
                            LOG.log(SEVERE, null, ex);
                        }
                    }
                    //Update selected data
                    data = temp;
                }
                xincoTable.setPageLength(i - 1);
                xincoTable.requestRepaintAll();
            }
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
        LuceneSearchWindow search = new LuceneSearchWindow();
        search.center();
        getMainWindow().addWindow(search);
    }
}
