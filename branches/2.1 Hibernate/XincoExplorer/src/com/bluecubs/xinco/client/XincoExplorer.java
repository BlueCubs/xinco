/**
 *Copyright 2005 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except cin compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to cin writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community cin exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            XincoExplorer
 *
 * Description:     client of the xinco DMS
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * Javier A. Ortiz     Aug-Dec 2006   1. Remove dialogs and windows from main code
 *                                    2. Incorporate 21 CFR regulatory features
 *
 *************************************************************
 */
package com.bluecubs.xinco.client;

import com.bluecubs.xinco.client.object.XincoMutableTreeNode;
import com.bluecubs.xinco.client.object.WindowClosingAdapter;
import com.bluecubs.xinco.client.object.XincoClientConnectionProfile;
import com.bluecubs.xinco.client.dialog.ACLDialog;
import com.bluecubs.xinco.client.dialog.AddAttributeText;
import com.bluecubs.xinco.client.dialog.AddAttributeUniversalDialog;
import com.bluecubs.xinco.client.dialog.ArchiveDialog;
import com.bluecubs.xinco.client.dialog.ConnectionDialog;
import com.bluecubs.xinco.client.dialog.DataDialog;
import com.bluecubs.xinco.client.dialog.DataFolderDialog;
import com.bluecubs.xinco.client.dialog.DataTypeDialog;
import com.bluecubs.xinco.client.dialog.LockDialog;
import com.bluecubs.xinco.client.dialog.LogDialog;
import com.bluecubs.xinco.client.dialog.SearchDialog;
import com.bluecubs.xinco.client.dialog.UserDialog;
import com.bluecubs.xinco.client.frames.XincoInformationFrame;
import com.bluecubs.xinco.client.object.XincoClientSession;
import com.bluecubs.xinco.client.object.XincoJTree;
import com.bluecubs.xinco.client.object.XincoRepositoryActionHandler;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.client.object.dragNdrop.XincoTreeCellRenderer;
import com.bluecubs.xinco.client.object.menu.XincoMenuRepository;
import com.bluecubs.xinco.client.object.menu.XincoPopUpMenuRepository;
import com.bluecubs.xinco.client.object.thread.XincoProgressBarThread;
import com.bluecubs.xinco.client.object.timer.XincoActivityTimer;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreDataType;
import com.bluecubs.xinco.core.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreLanguage;
import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.client.XincoClientSetting;
import com.bluecubs.xinco.service.XincoServiceLocator;
import com.bluecubs.xinco.service.XincoSoapBindingStub;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.namespace.QName;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;
import org.apache.axis.utils.ByteArrayOutputStream;

/**
 * XincoExplorer
 */
public class XincoExplorer extends JFrame implements ActionListener, MouseListener {

    //language resources, XincoExplorerResourceBundle
    private ResourceBundle xerb = null;
    private javax.swing.JMenuBar jJMenuBar = null;
    private javax.swing.JMenu jMenuConnection = null;
    private javax.swing.JMenu jMenuAbout = null;
    private javax.swing.JMenu jMenuRepository = null;
    private javax.swing.JMenuItem jMenuItemConnectionDisconnect = null;
    private javax.swing.JMenuItem jMenuItemAboutAboutXinco = null;
    private javax.swing.JDesktopPane jDesktopPane = null;
    private javax.swing.JInternalFrame jInternalFrameRepository = null;
    private javax.swing.JPanel jContentPaneRepository = null;
    private javax.swing.JSplitPane jSplitPaneRepository = null;
    private javax.swing.JScrollPane jScrollPaneRepositoryTree = null;
    private javax.swing.JScrollPane jScrollPaneRepositoryTable = null;
    private XincoJTree jTreeRepository = null;
    private javax.swing.JTable jTableRepository = null;
    private javax.swing.JMenu jMenuSearch = null;
    private javax.swing.JMenuItem jMenuItemSearchRepository = null;
    private javax.swing.JMenu jMenuView = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleWindows = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleJava = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleMotif = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleNapkin = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleSubstance = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleLiquid = null;
    private javax.swing.ButtonGroup bgwindowstyle;
    //client version
    private XincoVersion xincoClientVersion = null;
    //session object
    private XincoClientSession xincoClientSession = null;
    //connection profiles
    private Vector xincoClientConfig = null;
    //current path and filename
    private String current_filename = "";
    public String current_path = "";
    private String current_fullpath = "";
    //previous path and filename
    private String previous_fullpath = "";
    //global dialog return value
    private int globalDialog_returnValue = 0;
    private javax.swing.JMenuItem jMenuItemConnectionConnect = null;
    private AbstractDialog AbstractDialogFolder = null;
    private AbstractDialog AbstractDialogACL = null;
    private AbstractDialog AbstractDialogDataType = null;
    private JDialog jDialogRevision = null;
    private javax.swing.JPanel jContentPaneDialogRevision = null;
    private javax.swing.JLabel jLabelDialogRevision = null;
    private javax.swing.JScrollPane jScrollPaneDialogRevision = null;
    private javax.swing.JList jListDialogRevision = null;
    private javax.swing.JButton jButtonDialogRevisionContinue = null;
    private javax.swing.JButton jButtonDialogRevisionCancel = null;
    private AbstractDialog AbstractDialogData = null;
    private AbstractDialog AbstractDialogArchive = null;
    private AbstractDialog AbstractDialogLog = null;
    private AbstractDialog AbstractDialogAddAttributesUniversal = null;
    private javax.swing.JMenu jMenuPreferences = null;
    private javax.swing.JMenuItem jMenuItemPreferencesEditUser = null;
    private javax.swing.JPanel jContentPaneDialogUser = null;
    private AddAttributeText AbstractDialogAddAttributesText = null;
    private javax.swing.JPanel jContentPaneDialogAddAttributesText = null;
    private javax.swing.JTextArea jTextAreaDialogAddAttributesText = null;
    private javax.swing.JButton jButtonDialogAddAttributesTextSave = null;
    private javax.swing.JButton jButtonDialogAddAttributesTextCancel = null;
    private javax.swing.JScrollPane jScrollPaneDialogAddAttributesText = null;
    private JDialog AbstractDialogTransactionInfo = null;
    private javax.swing.JPanel jContentPaneDialogTransactionInfo = null;
    private javax.swing.JLabel jLabelDialogTransactionInfoText = null;
    private javax.swing.JPanel jContentPaneInformation = null;
    public javax.swing.JTextArea jLabelInternalFrameInformationText = null;
    private JPopupMenu jPopupMenuRepository = null;
    private JPanel jContentPaneDialogLocale = null;
    private JDialog jDialogLocale = null;
    private JScrollPane jScrollPaneDialogLocale = null;
    private JList jListDialogLocale = null;
    private JButton jButtonDialogLocaleOk = null;
    private XincoProgressBarThread progressBar;
    private ConnectionDialog dialogConnection = null;
    private UserDialog userDialog = null;
    private XincoInformationFrame jInternalFrameInformation = null;
    private JMenuItem jMenuItemConnectionExit = null;
    private String statusString_1 = "",  statusString_2 = "";
    private XincoCoreUser temp;
    private final XincoCoreUser newuser = new XincoCoreUser();
    private loginThread loginT;
    private int wizardType;
    private XincoMutableTreeNode newnode,  previousnode;
    private byte[] byteArray;
    private XincoCoreData xdata;
    private XincoCoreLog newlog;
    private Vector DataLogVector = null;
    private InputStream in = null;
    private final XincoExplorer explorer = this;
    private SearchDialog search;
    private XincoClientSetting settings;
    private Vector settingsVector;
    //Status of the explorer: lock = true - idle time limit exceeded, user must log cin again to continue use
    //lock = false - work normally
    private boolean lock = false;
    private LockDialog lockDialog = null;
    private XincoActivityTimer xat = null;
    private Vector dialogs = null;
    private XincoExplorer.refreshThread rThread;
    private String previous_filename;
    private String previous_path;
    private XincoRepositoryActionHandler actionHandler = null;
    //Size of menu actions
    private int actionSize = 19;
    private Vector filesToBeIndexed;
    public boolean viewOnly = false;
    private JRadioButtonMenuItem jRadioButtonMenuItemViewStyleNimbus;

    /**
     * This is the default constructor
     */
    public XincoExplorer() {
        super();
        try {
            setIconImage((new ImageIcon(XincoExplorer.class.getResource("blueCubsIcon.gif"))).getImage());
        } catch (Exception e) {
            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, e);
        }
        //load config
        loadConfig();
        saveConfig();
        //Apply LAF to all screens including de Locale Dialog
        try {
            switchPLAF((String) xincoClientConfig.elementAt(1));
        } catch (Throwable e) {
            createDefaultConfiguration(true);
            switchPLAF((String) xincoClientConfig.elementAt(1));
        }
        //choose language
        getJDialogLocale().setVisible(true);

        Locale loc = null;
        try {
            String list = ((Locale) xincoClientConfig.elementAt(2)).toString();
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
        } catch (Throwable e) {
            loc = Locale.getDefault();
        }
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
        xerb.getLocale();
        progressBar = new XincoProgressBarThread(this);
        initialize();
        //Windows-Listener
        addWindowListener(new WindowClosingAdapter(true));
    }

    @SuppressWarnings("unchecked")
    private void createDefaultConfiguration(boolean modifyProfiles) {
        if (modifyProfiles) {
            //insert default connection profiles
            ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "xinco Demo User";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://xinco.org:8080/xinco_demo/services/Xinco";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "user";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "user";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
            ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "xinco Demo Admin";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://xinco.org:8080/xinco_demo/services/Xinco";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "admin";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "admin";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
            ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "Template Profile";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://[server_domain]:8080/xinco/services/Xinco";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "your_username";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "your_password";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
            ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "Admin (localhost)";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "admin";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "admin";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
            ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "User (localhost)";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "user";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "user";
            ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
        }
        //add Pluggable Look and Feel
        xincoClientConfig.addElement(new String("javax.swing.plaf.metal.MetalLookAndFeel"));
        //add locale
        xincoClientConfig.addElement(Locale.getDefault());
    }

    /**
     * 
     * @return XincoClientSetting
     */
    public XincoClientSetting getSettings() {
        //Catch any recent setting change cin the DB
        updateSettings();
        return settings;
    }

    /**
     * Lock Explorer
     * @param b
     */
    public void setLock(boolean b) {
        this.lock = b;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getFilesToBeIndexed() {
        return filesToBeIndexed;
    }

    /**
     * 
     * @param filesToBeIndexed
     */
    public void setFilesToBeIndexed(Vector filesToBeIndexed) {
        this.filesToBeIndexed = filesToBeIndexed;
    }

    private void updateSettings() {
        try {
            settingsVector = xincoClientSession.getXinco().getXincoSetting(xincoClientSession.getUser());
        } catch (RemoteException ex) {
            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
        }
        settings = new XincoClientSetting();
        settings.setXincoSettings(settingsVector);
    }

    /**
     * This method initializes jContentPaneInformation
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPaneInformation() {
        if (jContentPaneInformation == null) {
            jLabelInternalFrameInformationText = new JTextArea();
            jLabelInternalFrameInformationText.setBounds(5, 5, 380, 130);
            jLabelInternalFrameInformationText.setAutoscrolls(true);
            jLabelInternalFrameInformationText.setLineWrap(true);
            jLabelInternalFrameInformationText.setWrapStyleWord(true);
            jLabelInternalFrameInformationText.setEditable(false);
            jContentPaneInformation = new JPanel();
            jContentPaneInformation.setLayout(null);
            jLabelInternalFrameInformationText.setText("");
            jContentPaneInformation.add(jLabelInternalFrameInformationText, null);
        }
        return jContentPaneInformation;
    }

    /**
     * 
     * @return XincoProgressBarThread
     */
    public XincoProgressBarThread getProgressBar() {
        return progressBar;
    }

    /**
     * 
     * @param progressBar
     */
    public void setProgressBar(XincoProgressBarThread progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * 
     * @return XincoCoreData
     */
    public XincoCoreData getXdata() {
        return xdata;
    }

    /**
     * 
     * @param xdata
     */
    public void setXdata(XincoCoreData xdata) {
        this.xdata = xdata;
    }

    /**
     * 
     * @return String
     */
    public String getPreviousFullpath() {
        return previous_fullpath;
    }

    /**
     * 
     * @param previous_fullpath
     */
    public void setPreviousFullpath(String previous_fullpath) {
        this.previous_fullpath = previous_fullpath;
    }

    private class refreshThread extends Thread {

        @Override
        public void run() {
            try {
                getJTreeRepository().setEnabled(false);
                getProgressBar().setTitle(getResourceBundle().getString("message.progressbar.refresh"));
                getProgressBar().show();
                // get root
                XincoCoreNode xnode = new XincoCoreNode();
                xnode.setId(1);
                xnode = getSession().getXinco().getXincoCoreNode(xnode, getSession().getUser());
                getSession().getXincoClientRepository().assignObject2TreeNode((XincoMutableTreeNode) (explorer.getSession().getXincoClientRepository().treemodel).getRoot(),
                        xnode,
                        explorer.getSession().getXinco(), explorer.getSession().getUser(), 2);
                jTreeRepository.expandPath(new TreePath(getSession().getXincoClientRepository().treemodel.getPathToRoot((XincoMutableTreeNode) (getSession().getXincoClientRepository().treemodel).getRoot())));
                collapseAllNodes();
                getProgressBar().hide();
                getJTreeRepository().setEnabled(true);
            } catch (Exception rmie) {
                if (getSettings().getSetting("setting.enable.developermode").isBoolValue()) {
                    Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, rmie);
                }
                getProgressBar().hide();
                getJTreeRepository().setEnabled(true);
            }
        }
    }

    /**
     * Reset XincoExplorer dialogs
     */
    public void resetExplorer() {
        for (int i = 0; i < getDialogs().size(); i++) {
            ((AbstractDialog) getDialogs().get(i)).clearDialog();
        }
    }

    /**
     * Refresh JTree
     */
    public void refreshJTree() {
        rThread = null;
        rThread = new refreshThread();
        rThread.start();
    }

    /**
     * This method initializes jPopupMenuRepository
     *
     * @return javax.swing.JPopupMenu
     */
    public JPopupMenu getJPopupMenuRepository() {
        JMenuItem tmi = null;
        if (jPopupMenuRepository == null) {
            jPopupMenuRepository = new XincoPopUpMenuRepository(this);
        }
        jPopupMenuRepository.setEnabled(jMenuRepository.isEnabled());
        return jPopupMenuRepository;
    }

    /**
     * This method initializes jContentPaneDialogLocale
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPaneDialogLocale() {
        if (jContentPaneDialogLocale == null) {
            jContentPaneDialogLocale = new JPanel();
            jContentPaneDialogLocale.setLayout(null);
            jContentPaneDialogLocale.add(getJScrollPaneDialogLocale(), null);
            jContentPaneDialogLocale.add(getJButtonDialogLocaleOk(), null);
        }
        return jContentPaneDialogLocale;
    }

    /**
     * This method initializes jDialogLocale
     *
     * @return JDialog
     */
    private JDialog getJDialogLocale() {
        if (jDialogLocale == null) {
            jDialogLocale = new JDialog();
            jDialogLocale.setContentPane(getJContentPaneDialogLocale());
            jDialogLocale.setTitle("XincoExplorer");
            jDialogLocale.setBounds(400, 400, 300, 200);
            jDialogLocale.setResizable(false);
            jDialogLocale.setModal(true);
            jDialogLocale.setAlwaysOnTop(true);
            jDialogLocale.getRootPane().setDefaultButton(getJButtonDialogLocaleOk());
        }
        //processing independent of creation
        int i = 0;
        ResourceBundle lrb = null;
        String[] locales;
        String text = "";
        int selection = -1;
        int altSelection = 0;
        DefaultListModel dlm;
        //load locales
        dlm = (DefaultListModel) jListDialogLocale.getModel();
        dlm.removeAllElements();
        selection = -1;
        altSelection = 0;
        lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
        locales = lrb.getString("AvailableLocales").split(",");
        for (i = 0; i < locales.length; i++) {
            try {
                text = locales[i];
                if (text.compareTo("") != 0) {
                    text = " (" + text + ")";
                }
                text = lrb.getString("Locale." + locales[i]) + text;
            } catch (Exception le) {
            }
            dlm.addElement(text);
            if ((locales[i].compareTo(((Locale) xincoClientConfig.elementAt(2)).toString()) == 0) || (locales[i].compareTo(((Locale) xincoClientConfig.elementAt(2)).getLanguage()) == 0)) {
                selection = i;
            }
            if (locales[i].compareTo("en") == 0) {
                altSelection = i;
            }
        }
        if (selection == -1) {
            selection = altSelection;
        }
        jListDialogLocale.setSelectedIndex(selection);
        jListDialogLocale.ensureIndexIsVisible(jListDialogLocale.getSelectedIndex());
        return jDialogLocale;
    }

    /**
     * This method initializes jScrollPaneDialogLocale
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneDialogLocale() {
        if (jScrollPaneDialogLocale == null) {
            jScrollPaneDialogLocale = new JScrollPane();
            jScrollPaneDialogLocale.setViewportView(getJListDialogLocale());
            jScrollPaneDialogLocale.setBounds(10, 10, 270, 100);
            jScrollPaneDialogLocale.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        return jScrollPaneDialogLocale;
    }

    /**
     * This method initializes jListDialogLocale
     *
     * @return javax.swing.JList
     */
    private JList getJListDialogLocale() {
        if (jListDialogLocale == null) {
            jListDialogLocale = new JList();
            jListDialogLocale.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jListDialogLocale.setModel(new DefaultListModel());
        }
        return jListDialogLocale;
    }

    /**
     * 
     * @return XincoClientSession
     */
    public XincoClientSession getSession() {
        return this.xincoClientSession;
    }

    /**
     * This method initializes jButtonDialogLocaleOk
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonDialogLocaleOk() {
        if (jButtonDialogLocaleOk == null) {
            jButtonDialogLocaleOk = new JButton();
            jButtonDialogLocaleOk.setBounds(200, 120, 70, 30);
            jButtonDialogLocaleOk.setText("Ok");
            jButtonDialogLocaleOk.addActionListener(new java.awt.event.ActionListener() {

                @SuppressWarnings("unchecked")
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //get locale
                    if (jListDialogLocale.getSelectedIndex() >= 0) {
                        ResourceBundle lrb = null;
                        String[] locales;
                        lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
                        locales = lrb.getString("AvailableLocales").split(",");
                        xincoClientConfig.setElementAt(new Locale(locales[jListDialogLocale.getSelectedIndex()]), 2);
                        saveConfig();
                        jDialogLocale.setVisible(false);
                    }
                }
            });
        }
        return jButtonDialogLocaleOk;
    }

    /**
     * Main Method
     * @param args
     */
    public static void main(String[] args) {
        XincoExplorer frame = new XincoExplorer();
        frame.setVisible(true);
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        ResourceBundle settingsRB = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
        //init session
        xincoClientSession = new XincoClientSession(this);
        //set client version
        xincoClientVersion = new XincoVersion();
        xincoClientVersion.setVersionHigh(Integer.parseInt(settingsRB.getString("version.high")));
        xincoClientVersion.setVersionMid(Integer.parseInt(settingsRB.getString("version.mid")));
        xincoClientVersion.setVersionLow(Integer.parseInt(settingsRB.getString("version.low")));
        xincoClientVersion.setVersionPostfix(settingsRB.getString("version.postfix"));
        switchPLAF((String) xincoClientConfig.elementAt(1));
        this.setBounds(0, 0, (new Double(getToolkit().getScreenSize().getWidth())).intValue() - 100,
                (new Double(getToolkit().getScreenSize().getHeight())).intValue() - 75);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setName("XincoExplorer");
        this.setTitle(xerb.getString("general.clienttitle") + " - " +
                xerb.getString("general.version") + " " +
                xincoClientVersion.getVersionHigh() + "." +
                xincoClientVersion.getVersionMid() + "." +
                xincoClientVersion.getVersionLow() + " " +
                xincoClientVersion.getVersionPostfix());
        this.setJMenuBar(getJJMenuBar());
        this.setContentPane(getJDesktopPane());
        this.setVisible(true);
    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private javax.swing.JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new javax.swing.JMenuBar();
            jJMenuBar.add(getJMenuConnection());
            jJMenuBar.add(getJMenuRepository());
            jJMenuBar.add(getJMenuSearch());
            jJMenuBar.add(getJMenuPreferences());
            jJMenuBar.add(getJMenuView());
            jJMenuBar.add(getJMenuAbout());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes jMenuConnection
     *
     * @return javax.swing.JMenu
     */
    private javax.swing.JMenu getJMenuConnection() {
        if (jMenuConnection == null) {
            jMenuConnection = new javax.swing.JMenu();
            jMenuConnection.add(getJMenuItemConnectionConnect());
            jMenuConnection.add(getJMenuItemConnectionDisconnect());
            jMenuConnection.add(getJMenuItemConnectionExit());
            jMenuConnection.setName("Connection");
            jMenuConnection.setText(xerb.getString("menu.connection"));
        }
        return jMenuConnection;
    }

    private JMenuItem getJMenuItemConnectionExit() {
        if (jMenuItemConnectionExit == null) {
            jMenuItemConnectionExit = new javax.swing.JMenuItem();
            jMenuItemConnectionExit.setName("Exit");
            jMenuItemConnectionExit.setText(xerb.getString("menu.connection.exit"));
            jMenuItemConnectionExit.setEnabled(true);
            jMenuItemConnectionExit.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    xincoClientSession.setStatus(0);
                    markConnectionStatus();
                    dispose();
                    System.exit(0);
                }
            });
        }
        return jMenuItemConnectionExit;
    }

    /**
     * This method initializes jMenuAbout
     *
     * @return javax.swing.JMenu
     */
    private javax.swing.JMenu getJMenuAbout() {
        if (jMenuAbout == null) {
            jMenuAbout = new javax.swing.JMenu();
            jMenuAbout.add(getJMenuItemAboutAboutXinco());
            jMenuAbout.setName("About");
            jMenuAbout.setText(xerb.getString("menu.about"));
        }
        return jMenuAbout;
    }

    /**
     * This method initializes jMenuRepository
     *
     * @return javax.swing.JMenu
     */
    public javax.swing.JMenu getJMenuRepository() {
        if (jMenuRepository == null) {
            jMenuRepository = new XincoMenuRepository(this);
            jMenuRepository.setText(xerb.getString("menu.repository"));
            jMenuRepository.setName("Repository");
            jMenuRepository.setEnabled(false);
        }
        return jMenuRepository;
    }

    /**
     * This method initializes jMenuItemConnectionDisconnect
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemConnectionDisconnect() {
        if (jMenuItemConnectionDisconnect == null) {
            jMenuItemConnectionDisconnect = new javax.swing.JMenuItem();
            jMenuItemConnectionDisconnect.setName("Disconnect");
            jMenuItemConnectionDisconnect.setText(xerb.getString("menu.disconnect"));
            jMenuItemConnectionDisconnect.setEnabled(false);
            jMenuItemConnectionDisconnect.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    xincoClientSession.setStatus(0);
                    markConnectionStatus();
                    loginT.resetStrings();
                    collapseAllNodes();
                }
            });
        }
        return jMenuItemConnectionDisconnect;
    }

    /**
     * This method initializes jMenuItemAboutAboutXinco
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemAboutAboutXinco() {
        if (jMenuItemAboutAboutXinco == null) {
            jMenuItemAboutAboutXinco = new javax.swing.JMenuItem();
            jMenuItemAboutAboutXinco.setName("AboutXinco");
            jMenuItemAboutAboutXinco.setText(xerb.getString("menu.aboutxinco"));
            jMenuItemAboutAboutXinco.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
            jMenuItemAboutAboutXinco.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String messageString = "";
                    messageString = messageString + xerb.getString("window.aboutxinco.clienttitle") + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.clientversion") + ": " + xincoClientVersion.getVersionHigh() + "." + xincoClientVersion.getVersionMid() + "." + xincoClientVersion.getVersionLow() + xincoClientVersion.getVersionPostfix() + "\n";
                    messageString = messageString + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.partof") + ":\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.softwaretitle") + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.softwaresubtitle") + "\n";
                    messageString = messageString + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.vision1") + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.vision2") + "\n";
                    messageString = messageString + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.moreinfo") + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.xinco_org") + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.bluecubsCom") + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.bluecubs_org") + "\n";
                    messageString = messageString + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.thanks") + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.apache") + "\n";
                    messageString = messageString + "\n";
                    messageString = messageString + xerb.getString("window.aboutxinco.copyright") + "\n";
                    JOptionPane.showMessageDialog(XincoExplorer.this, messageString, xerb.getString("window.aboutxinco"), JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
        return jMenuItemAboutAboutXinco;
    }

    /**
     * This method initializes jDesktopPane
     *
     * @return javax.swing.JDesktopPane
     */
    private javax.swing.JDesktopPane getJDesktopPane() {
        if (jDesktopPane == null) {
            jDesktopPane = new javax.swing.JDesktopPane();
            jDesktopPane.add(getJInternalFrameRepository(), null);
            jDesktopPane.add(getJInternalFrameInformation(), null);
            jDesktopPane.setLayer(getJInternalFrameRepository(), 0);
            jDesktopPane.setLayer(jInternalFrameInformation, 10);
        }
        return jDesktopPane;
    }

    /**
     * This method initializes jContentPaneRepository
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneRepository() {
        if (jContentPaneRepository == null) {
            jContentPaneRepository = new javax.swing.JPanel();
            jContentPaneRepository.setLayout(new java.awt.BorderLayout());
            jContentPaneRepository.add(getJSplitPaneRepository(), java.awt.BorderLayout.CENTER);
        }
        return jContentPaneRepository;
    }

    /**
     * This method initializes jInternalFrameRepository
     *
     * @return javax.swing.JInternalFrame
     */
    private javax.swing.JInternalFrame getJInternalFrameRepository() {
        if (jInternalFrameRepository == null) {
            jInternalFrameRepository = new javax.swing.JInternalFrame();
            jInternalFrameRepository.setBounds(5, 5, this.getWidth() - 100, this.getHeight() - 150);
            jInternalFrameRepository.setContentPane(getJContentPaneRepository());
            jInternalFrameRepository.setVisible(false);
            jInternalFrameRepository.setResizable(true);
            jInternalFrameRepository.setIconifiable(true);
            jInternalFrameRepository.setMaximizable(true);
            jInternalFrameRepository.setName("Repository");
            jInternalFrameRepository.setTitle(xerb.getString("window.repository"));
        }
        return jInternalFrameRepository;
    }

    /**
     * This method initializes jSplitPaneRepository
     *
     * @return javax.swing.JSplitPane
     */
    private javax.swing.JSplitPane getJSplitPaneRepository() {
        if (jSplitPaneRepository == null) {
            jSplitPaneRepository = new javax.swing.JSplitPane();
            jSplitPaneRepository.setLeftComponent(getJScrollPaneRepositoryTree());
            jSplitPaneRepository.setRightComponent(getJScrollPaneRepositoryTable());
            jSplitPaneRepository.setDividerLocation(2.0 / 3.0);
            jSplitPaneRepository.setResizeWeight(1);
            jSplitPaneRepository.setContinuousLayout(true);
        }
        return jSplitPaneRepository;
    }

    /**
     * This method initializes jScrollPaneRepositoryTree
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPaneRepositoryTree() {
        if (jScrollPaneRepositoryTree == null) {
            jScrollPaneRepositoryTree = new javax.swing.JScrollPane();
            jScrollPaneRepositoryTree.setViewportView(getJTreeRepository());
        }
        return jScrollPaneRepositoryTree;
    }

    /**
     * This method initializes jScrollPaneRepositoryTable
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPaneRepositoryTable() {
        if (jScrollPaneRepositoryTable == null) {
            jScrollPaneRepositoryTable = new javax.swing.JScrollPane();
            jScrollPaneRepositoryTable.setViewportView(getJTableRepository());
            jScrollPaneRepositoryTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return jScrollPaneRepositoryTable;
    }

    /**
     * This method initializes jTreeRepository
     *
     * @return javax.swing.JTree
     */
    public XincoJTree getJTreeRepository() {
        if (jTreeRepository == null) {
            jTreeRepository = new XincoJTree(this);
            jTreeRepository.setModel(xincoClientSession.getXincoClientRepository().treemodel);
            //enable tool tips
            ToolTipManager.sharedInstance().registerComponent(jTreeRepository);
            jTreeRepository.setCellRenderer(new XincoTreeCellRenderer(this));
            jTreeRepository.setRootVisible(true);
            jTreeRepository.setEditable(false);
            DefaultTreeSelectionModel dtsm = new DefaultTreeSelectionModel();
            dtsm.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            jTreeRepository.setSelectionModel(dtsm);
        }
        return jTreeRepository;
    }

    /**
     * 
     * @param xincoCoreUser
     */
    public void setTemp(XincoCoreUser xincoCoreUser) {
        temp = xincoCoreUser;
    }

    /**
     * Collapse all tree nodes
     */
    public void collapseAllNodes() {
        getJTreeRepository();
        int row = jTreeRepository.getRowCount() - 1;
        while (row >= 0) {
            jTreeRepository.collapseRow(row);
            row--;
        }
        jTreeRepository.expandRow(0);
    }

    /**
     * This method initializes jTableRepository
     *
     * @return javax.swing.JTable
     */
    public javax.swing.JTable getJTableRepository() {
        if (jTableRepository == null) {
            String[] cn = {xerb.getString("window.repository.table.attribute"), xerb.getString("window.repository.table.details")};
            DefaultTableModel dtm = new DefaultTableModel(cn, 0) {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            jTableRepository = new javax.swing.JTable();
            jTableRepository.setModel(dtm);
            jTableRepository.setColumnSelectionAllowed(false);
            jTableRepository.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            jTableRepository.setRowSelectionAllowed(false);
            jTableRepository.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jTableRepository.setCellSelectionEnabled(false);
            jTableRepository.setEnabled(true);
        }
        return jTableRepository;
    }

    /**
     * This method initializes jMenuSearch
     *
     * @return javax.swing.JMenu
     */
    private javax.swing.JMenu getJMenuSearch() {
        if (jMenuSearch == null) {
            jMenuSearch = new javax.swing.JMenu();
            jMenuSearch.add(getJMenuItemSearchRepository());
            jMenuSearch.setText(xerb.getString("menu.search"));
            jMenuSearch.setName("Search");
            jMenuSearch.setEnabled(false);
        }
        return jMenuSearch;
    }

    /**
     * This method initializes jMenuItemSearchRepository
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemSearchRepository() {
        if (jMenuItemSearchRepository == null) {
            jMenuItemSearchRepository = new javax.swing.JMenuItem();
            jMenuItemSearchRepository.setText(xerb.getString("menu.search.search_repository"));
            jMenuItemSearchRepository.setName("SearchRepository");
            jMenuItemSearchRepository.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.KeyEvent.CTRL_MASK));
            jMenuItemSearchRepository.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (search == null) {
                        search = new SearchDialog(null, true, explorer);
                    }
                    search.setVisible(true);
                }
            });
        }
        return jMenuItemSearchRepository;
    }

    /**
     * This method initializes jMenuView
     *
     * @return javax.swing.JMenu
     */
    private javax.swing.JMenu getJMenuView() {
        if (jMenuView == null) {
            jMenuView = new javax.swing.JMenu();
            bgwindowstyle = new ButtonGroup();
            jMenuView.add(getJRadioButtonMenuItemViewStyleWindows());
            jMenuView.add(getJRadioButtonMenuItemViewStyleJava());
            jMenuView.add(getJRadioButtonMenuItemViewStyleLiquid());
            jMenuView.add(getJRadioButtonMenuItemViewStyleMotif());
            jMenuView.add(getJRadioButtonMenuItemViewStyleNapkin());
            jMenuView.add(getJRadioButtonMenuItemViewStyleSubstance());
            jMenuView.add(getJRadioButtonMenuItemViewStyleNimbus());
            bgwindowstyle.add(jMenuView.getItem(0));
            bgwindowstyle.add(jMenuView.getItem(1));
            bgwindowstyle.add(jMenuView.getItem(2));
            bgwindowstyle.add(jMenuView.getItem(3));
            bgwindowstyle.add(jMenuView.getItem(4));
            bgwindowstyle.add(jMenuView.getItem(5));
            jMenuView.setText(xerb.getString("menu.view"));
        }
        return jMenuView;
    }

    /**
     * This method initializes jRadioButtonMenuItemViewStyleWindows
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleWindows() {
        if (jRadioButtonMenuItemViewStyleWindows == null) {
            jRadioButtonMenuItemViewStyleWindows = new javax.swing.JRadioButtonMenuItem();
            if (((String) xincoClientConfig.elementAt(1)).equals(new String("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleWindows.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleWindows.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleWindows.setText(xerb.getString("menu.view.windows"));
            jRadioButtonMenuItemViewStyleWindows.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    xincoClientConfig.setElementAt(new String("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"), 1);
                    saveConfig();
                }
            });
        }
        return jRadioButtonMenuItemViewStyleWindows;
    }

    /**
     * This method initializes jRadioButtonMenuItemViewStyleNimbus
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleNimbus() {
        if (jRadioButtonMenuItemViewStyleNimbus == null) {
            jRadioButtonMenuItemViewStyleNimbus = new javax.swing.JRadioButtonMenuItem();
            if (((String) xincoClientConfig.elementAt(1)).equals(new String("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleNimbus.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleNimbus.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleNimbus.setText(xerb.getString("menu.view.nimbus"));
            jRadioButtonMenuItemViewStyleNimbus.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    xincoClientConfig.setElementAt(new String("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"), 1);
                    saveConfig();
                }
            });
        }
        return jRadioButtonMenuItemViewStyleNimbus;
    }

    /**
     * This method initializes jRadioButtonMenuItemViewStyleNapkin
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleNapkin() {
        if (jRadioButtonMenuItemViewStyleNapkin == null) {
            jRadioButtonMenuItemViewStyleNapkin = new javax.swing.JRadioButtonMenuItem();
            if (((String) xincoClientConfig.elementAt(1)).equals(new String("net.sourceforge.napkinlaf.NapkinLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleNapkin.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleNapkin.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleNapkin.setText(xerb.getString("menu.view.napkin"));
            jRadioButtonMenuItemViewStyleNapkin.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("net.sourceforge.napkinlaf.NapkinLookAndFeel");
                    xincoClientConfig.setElementAt(new String("net.sourceforge.napkinlaf.NapkinLookAndFeel"), 1);
                    saveConfig();
                }
            });
        }
        return jRadioButtonMenuItemViewStyleNapkin;
    }

    /**
     * This method initializes jRadioButtonMenuItemViewStyleNapkin
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleSubstance() {
        if (jRadioButtonMenuItemViewStyleSubstance == null) {
            jRadioButtonMenuItemViewStyleSubstance = new javax.swing.JRadioButtonMenuItem();
            if (((String) xincoClientConfig.elementAt(1)).equals(new String("org.jvnet.substance.SubstanceLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleSubstance.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleSubstance.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleSubstance.setText(xerb.getString("menu.view.substance"));
            jRadioButtonMenuItemViewStyleSubstance.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("org.jvnet.substance.SubstanceLookAndFeel");
                    xincoClientConfig.setElementAt(new String("org.jvnet.substance.SubstanceLookAndFeel"), 1);
                    saveConfig();
                }
            });
        }
        return jRadioButtonMenuItemViewStyleSubstance;
    }

    /**
     * This method initializes jRadioButtonMenuItemViewStyleJava
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleJava() {
        if (jRadioButtonMenuItemViewStyleJava == null) {
            jRadioButtonMenuItemViewStyleJava = new javax.swing.JRadioButtonMenuItem();
            if (((String) xincoClientConfig.elementAt(1)).equals(new String("javax.swing.plaf.metal.MetalLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleJava.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleJava.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleJava.setText(xerb.getString("menu.view.java"));
            jRadioButtonMenuItemViewStyleJava.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("javax.swing.plaf.metal.MetalLookAndFeel");
                    xincoClientConfig.setElementAt(new String("javax.swing.plaf.metal.MetalLookAndFeel"), 1);
                    saveConfig();
                }
            });
        }
        return jRadioButtonMenuItemViewStyleJava;
    }

    /**
     * This method initializes jRadioButtonMenuItemViewStyleJava
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleLiquid() {
        if (jRadioButtonMenuItemViewStyleLiquid == null) {
            jRadioButtonMenuItemViewStyleLiquid = new javax.swing.JRadioButtonMenuItem();
            if (((String) xincoClientConfig.elementAt(1)).equals(new String("com.birosoft.liquid.LiquidLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleLiquid.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleLiquid.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleLiquid.setText(xerb.getString("menu.view.liquid"));
            jRadioButtonMenuItemViewStyleLiquid.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("com.birosoft.liquid.LiquidLookAndFeel");
                    xincoClientConfig.setElementAt(new String("com.birosoft.liquid.LiquidLookAndFeel"), 1);
                    saveConfig();
                }
            });
        }
        return jRadioButtonMenuItemViewStyleLiquid;
    }

    /**
     * This method initializes jRadioButtonMenuItemViewStyleMotif
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleMotif() {
        if (jRadioButtonMenuItemViewStyleMotif == null) {
            jRadioButtonMenuItemViewStyleMotif = new javax.swing.JRadioButtonMenuItem();
            if (((String) xincoClientConfig.elementAt(1)).equals(new String("com.sun.java.swing.plaf.motif.MotifLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleMotif.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleMotif.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleMotif.setText(xerb.getString("menu.view.motif"));
            jRadioButtonMenuItemViewStyleMotif.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    xincoClientConfig.setElementAt(new String("com.sun.java.swing.plaf.motif.MotifLookAndFeel"), 1);
                    saveConfig();
                }
            });
        }
        return jRadioButtonMenuItemViewStyleMotif;
    }

    /**
     * This method initializes jMenuItemConnectionConnect
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemConnectionConnect() {
        statusString_1 = "";
        statusString_2 = "";
        if (jMenuItemConnectionConnect == null) {
            jMenuItemConnectionConnect = new javax.swing.JMenuItem();
            jMenuItemConnectionConnect.setText(xerb.getString("menu.connection.connect"));
            jMenuItemConnectionConnect.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int i = 0;
                    //init session
                    xincoClientSession = new XincoClientSession(XincoExplorer.this);
                    getJTreeRepository().setModel(xincoClientSession.getXincoClientRepository().treemodel);
                    xincoClientSession.setStatus(0);
                    //open connection dialog
                    getAbstractDialogConnection();
                    DefaultListModel dlm = (DefaultListModel) dialogConnection.getProfileList().getModel();
                    dlm.removeAllElements();
                    for (i = 0; i < ((Vector) xincoClientConfig.elementAt(0)).size(); i++) {
                        dlm.addElement(new String(((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(i)).toString()));
                    }
                    //establish connection and login
                    if (xincoClientSession.getStatus() == 1) {
                        try {
                            xincoClientSession.setXincoService(new XincoServiceLocator());
                            xincoClientSession.setXinco(xincoClientSession.getXincoService().getXinco(new java.net.URL(xincoClientSession.getServiceEndpoint())));
                            xincoClientSession.setServerVersion(xincoClientSession.getXinco().getXincoServerVersion());
                            //check if client and server versions match (high AND mid must match!)
                            if ((xincoClientVersion.getVersionHigh() != xincoClientSession.getServerVersion().getVersionHigh()) || (xincoClientVersion.getVersionMid() != xincoClientSession.getServerVersion().getVersionMid())) {
                                throw new XincoException(xerb.getString("menu.connection.error.serverversion") + " " + xincoClientSession.getServerVersion().getVersionHigh() + "." + xincoClientSession.getServerVersion().getVersionMid() + ".x");
                            }
                            if ((temp = xincoClientSession.getXinco().getCurrentXincoCoreUser(xincoClientSession.getUser().getUsername(), xincoClientSession.getUser().getUserpassword())).getId() == 0) {
                                throw new XincoException(xerb.getString("menu.connection.error.user"));
                            }
                            temp.setUserpassword(xincoClientSession.getUser().getUserpassword());
                            newuser.setEmail(temp.getEmail());
                            newuser.setFirstname(temp.getFirstname());
                            newuser.setId(temp.getId());
                            newuser.setName(temp.getName());
                            newuser.setStatusNumber(temp.getStatusNumber());
                            newuser.setUsername(temp.getUsername());
                            newuser.setUserpassword(temp.getUserpassword());
                            xincoClientSession.setUser(xincoClientSession.getXinco().getCurrentXincoCoreUser(newuser.getUsername(), newuser.getUserpassword()));
                            getProgressBar().run();
                            xincoClientSession.setServerDatatypes(xincoClientSession.getXinco().getAllXincoCoreDataTypes(xincoClientSession.getUser()));
                            xincoClientSession.setServerGroups(xincoClientSession.getXinco().getAllXincoCoreGroups(xincoClientSession.getUser()));
                            xincoClientSession.setServerLanguages(xincoClientSession.getXinco().getAllXincoCoreLanguages(xincoClientSession.getUser()));
                            for (i = 0; i < xincoClientSession.getUser().getXincoCoreGroups().size(); i++) {
                                statusString_1 = statusString_1 + "      + ";
                                if (((XincoCoreGroup) xincoClientSession.getUser().getXincoCoreGroups().elementAt(i)).getId() <= 1000) {
                                    statusString_1 += xerb.getString(((XincoCoreGroup) xincoClientSession.getUser().getXincoCoreGroups().elementAt(i)).getDesignation()) + "\n";
                                } else {
                                    statusString_1 += ((XincoCoreGroup) xincoClientSession.getUser().getXincoCoreGroups().elementAt(i)).getDesignation() + "\n";
                                }
                            }
                            for (i = 0; i < xincoClientSession.getServerDatatypes().size(); i++) {
                                statusString_2 += "      + " + xerb.getString(((XincoCoreDataType) xincoClientSession.getServerDatatypes().elementAt(i)).getDesignation()) + "\n";
                            }
                            loginT = new loginThread();
                            loginT.start();
                        } catch (Exception cone) {
                            xincoClientSession.setStatus(0);
                            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, cone);
                            markConnectionStatus();
                            JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("menu.connection.failed") + " " +
                                    xerb.getString("general.reason") + ": " + cone.toString(),
                                    xerb.getString("menu.connection.failed"), JOptionPane.WARNING_MESSAGE);
                            resetExplorer();
                        }
                    }
                }
            });
        }
        return jMenuItemConnectionConnect;
    }

    /**
     * 
     * @return XincoCoreUser
     */
    public XincoCoreUser getUser() {
        return this.newuser;
    }

    /**
     * Convenience method
     * @return Vector containing XincoExplorer's dialogs
     */
    public Vector getDialogs() {
        if (dialogs == null) {
            dialogs = new Vector();
        }
        return dialogs;
    }

    /**
     * 
     * @param dialogs
     */
    public void setDialogs(Vector dialogs) {
        this.dialogs = dialogs;
    }

    private class loginThread extends Thread {

        @Override
        public void run() {
            try {
                String statusString = "";
                temp = xincoClientSession.getXinco().getCurrentXincoCoreUser(xincoClientSession.getUser().getUsername(), xincoClientSession.getUser().getUserpassword());
                statusString += xerb.getString("menu.connection.connectedto") + ": " + xincoClientSession.getServiceEndpoint() + "\n";
                statusString += xerb.getString("general.serverversion") + ": ";
                statusString += xincoClientSession.getServerVersion().getVersionHigh() + ".";
                statusString += xincoClientSession.getServerVersion().getVersionMid() + ".";
                statusString += xincoClientSession.getServerVersion().getVersionLow();
                statusString += " " + xincoClientSession.getServerVersion().getVersionPostfix() + "\n";
                statusString += "\n";
                statusString += xerb.getString("general.user") + ": " + xincoClientSession.getUser().getFirstname() + " " + xincoClientSession.getUser().getName() + " <" + xincoClientSession.getUser().getEmail() + ">\n";
                statusString += xerb.getString("general.memberof") + ":\n";
                statusString += statusString_1 + "\n";
                statusString += xerb.getString("general.groupsonserver") + ": " + xincoClientSession.getServerGroups().size() + "\n";
                statusString += xerb.getString("general.languagesonserver") + ": " + xincoClientSession.getServerLanguages().size() + "\n";
                statusString += xerb.getString("general.datatypesonserver") + ": " + xincoClientSession.getServerDatatypes().size() + "\n";
                statusString += statusString_2 + "\n";
                xincoClientSession.setCurrentSearchResult(new Vector());
                xincoClientSession.setStatus(2);
                JOptionPane.showMessageDialog(XincoExplorer.this, statusString, xerb.getString("menu.connection.established"), JOptionPane.INFORMATION_MESSAGE);
                jLabelInternalFrameInformationText.setText(xerb.getString("menu.connection.established"));
                //get root
                XincoCoreNode xnode = new XincoCoreNode();
                xnode.setId(1);
                xnode = xincoClientSession.getXinco().getXincoCoreNode(xnode, xincoClientSession.getUser());
                xincoClientSession.getXincoClientRepository().assignObject2TreeNode((XincoMutableTreeNode) (xincoClientSession.getXincoClientRepository().treemodel).getRoot(),
                        xnode, xincoClientSession.getXinco(), xincoClientSession.getUser(),
                        2);
                getJTreeRepository().expandPath(new TreePath(xincoClientSession.getXincoClientRepository().treemodel.getPathToRoot((XincoMutableTreeNode) (xincoClientSession.getXincoClientRepository().treemodel).getRoot())));
                markConnectionStatus();
                if (temp.getStatusNumber() == 3) {
                    jLabelInternalFrameInformationText.setText(xerb.getString("password.aged"));
                    getAbstractDialogUser(true);
                }
                getProgressBar().hide();
                getJInternalFrameInformation().setVisible(true);
                updateSettings();
                dialogConnection.updateProfile(getSettings().getSetting("setting.enable.savepassword").isBoolValue());
            } catch (Exception cone) {
                xincoClientSession.setStatus(0);
                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, cone);
                markConnectionStatus();
                JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("menu.connection.failed") + " " + xerb.getString("general.reason") + ": " + cone.toString(), xerb.getString("menu.connection.failed"), JOptionPane.WARNING_MESSAGE);
                getProgressBar().hide();
                resetExplorer();
            }
        }

        public void resetStrings() {
            statusString_1 = "";
            statusString_2 = "";
        }
    }

    /**
     * This method gets AbstractDialogConnection
     *
     * @return AbstractDialog
     */
    protected AbstractDialog getAbstractDialogConnection() {
        if (dialogConnection == null) {
            dialogConnection = new ConnectionDialog(new javax.swing.JFrame(),
                    true, this);
            addDialog(dialogConnection);
        }
        dialogConnection.setVisible(true);
        return dialogConnection;
    }

    /**
     * 
     * @return ResourceBundle
     */
    public ResourceBundle getResourceBundle() {
        return this.xerb;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getConfig() {
        return xincoClientConfig;
    }

    /**
     * This method marks menues, etc. according to connection status
     *
     */
    public void markConnectionStatus() {
        int i = 0, j = 0;
        if (xincoClientSession != null) {
            //do general processing
            DefaultTableModel dtm;
            dtm = (DefaultTableModel) jTableRepository.getModel();
            j = dtm.getRowCount();
            for (i = 0; i < j; i++) {
                dtm.removeRow(0);
            }
            if (search != null) {
                this.search.clearResults();
            }
            //reset selection
            xincoClientSession.setCurrentTreeNodeSelection(null);
            xincoClientSession.setClipboardTreeNodeSelection(new Vector());
            xincoClientSession.setCurrentSearchResult(new Vector());
            //reset menus
            getJPopupMenuRepository();
            ((XincoPopUpMenuRepository) jPopupMenuRepository).resetItems();
            ((XincoMenuRepository) jMenuRepository).resetItems();
            //status = disconnected
            if (xincoClientSession.getStatus() == 0) {
                // set menus
                jMenuRepository.setEnabled(false);
                jMenuSearch.setEnabled(false);
                jMenuItemSearchRepository.setEnabled(false);
                jMenuPreferences.setEnabled(false);
                jMenuItemConnectionConnect.setEnabled(true);
                jMenuItemConnectionDisconnect.setEnabled(false);
                // set frames
                jInternalFrameRepository.setVisible(false);
                if (search != null) {
                    search.setVisible(false);
                }
                jInternalFrameInformation.setVisible(false);
                //init session
                xincoClientSession = new XincoClientSession(this);
                getJTreeRepository().setModel(xincoClientSession.getXincoClientRepository().treemodel);
            }
            //status = connected
            if (xincoClientSession.getStatus() == 2) {
                // set menus
                jMenuRepository.setEnabled(true);
                jMenuSearch.setEnabled(true);
                jMenuItemSearchRepository.setEnabled(true);
                jMenuPreferences.setEnabled(true);
                jMenuItemConnectionConnect.setEnabled(false);
                jMenuItemConnectionDisconnect.setEnabled(true);
                // set frames
                jInternalFrameRepository.setVisible(true);
                jInternalFrameInformation.setVisible(true);
            }
        }
    }

    /**
     * This method initializes jInternalFrameInformation
     *
     * @return javax.swing.JInternalFrame
     */
    private XincoInformationFrame getJInternalFrameInformation() {
        if (jInternalFrameInformation == null) {
            jInternalFrameInformation = new XincoInformationFrame(this);
            jInternalFrameInformation.setContentPane(getJContentPaneInformation());
            jInternalFrameInformation.setTitle(xerb.getString("window.information"));
            jInternalFrameInformation.setBounds(this.getWidth() - 450, this.getHeight() - 220, 400, 150);
        }
        return jInternalFrameInformation;
    }

    /**
     * This method switches the plugable look and feel
     *
     * @return void
     */
    private void switchPLAF(String plafString) {
        try {
            //set LAF
            UIManager.setLookAndFeel(plafString);
            //update EACH window
            SwingUtilities.updateComponentTreeUI(XincoExplorer.this);
            if (getDialogs() != null) {
                for (int i = 0; i < getDialogs().size(); i++) {
                    if (getDialogs().get(i) != null) {
                        SwingUtilities.updateComponentTreeUI((AbstractDialog) getDialogs().get(i));
                    }
                }
            }
        } catch (Exception plafe) {
            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, plafe);
            resetExplorer();
        }
    }

    /**
     * This method initializes AbstractDialogFolder
     *
     * @return AbstractDialog
     */
    public AbstractDialog getAbstractDialogFolder() {
        if (AbstractDialogFolder == null) {
            AbstractDialogFolder = new DataFolderDialog(null, true, this);
            addDialog(AbstractDialogFolder);
        }
        return AbstractDialogFolder;
    }

    /**
     * This method initializes AbstractDialogACL
     *
     * @return AbstractDialog
     */
    public AbstractDialog getAbstractDialogACL() {
        if (AbstractDialogACL == null) {
            AbstractDialogACL = new ACLDialog(new javax.swing.JFrame(), true, this);
            addDialog(AbstractDialogACL);
        }
        return AbstractDialogACL;
    }

    /**
     * This method initializes jContentPaneDialogRevision
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneDialogRevision() {
        if (jContentPaneDialogRevision == null) {
            jContentPaneDialogRevision = new javax.swing.JPanel();
            jContentPaneDialogRevision.setLayout(null);
            jContentPaneDialogRevision.add(getJLabelDialogRevision(), null);
            jContentPaneDialogRevision.add(getJScrollPaneDialogRevision(), null);
            jContentPaneDialogRevision.add(getJButtonDialogRevisionContinue(), null);
            jContentPaneDialogRevision.add(getJButtonDialogRevisionCancel(), null);
        }
        return jContentPaneDialogRevision;
    }

    /**
     * This method initializes jDialogRevision
     *
     * @return AbstractDialog
     */
    private JDialog getJDialogRevision() {
        if (jDialogRevision == null) {
            jDialogRevision = new JDialog();
            jDialogRevision.setContentPane(getJContentPaneDialogRevision());
            jDialogRevision.setBounds(200, 200, 400, 220);
            jDialogRevision.setTitle(xerb.getString("window.revision"));
            jDialogRevision.setModal(true);
            jDialogRevision.setResizable(false);
            jDialogRevision.getRootPane().setDefaultButton(getJButtonDialogRevisionContinue());
        }
        //processing independent of creation
        int i = 0;
        String text = "";
        if (xincoClientSession.getCurrentTreeNodeSelection().getUserObject() != null) {
            DefaultListModel dlm = (DefaultListModel) jListDialogRevision.getModel();
            dlm.removeAllElements();
            Calendar cal = null;
            Calendar realcal = null;
            Calendar ngc = new GregorianCalendar();
            for (i = 0; i < ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().size(); i++) {
                if ((((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getOpCode() == 1) || (((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getOpCode() == 5)) {
                    //convert clone from remote time to local time
                    cal = (Calendar) ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getOpDatetime().clone();
                    realcal = (((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getOpDatetime());
                    cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - realcal.get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + realcal.get(Calendar.DST_OFFSET)));
                    text = "" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH);
                    text = text + " - " + xerb.getString("general.version") + " " + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getVersion().getVersionHigh() + "." + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getVersion().getVersionMid() + "." + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getVersion().getVersionLow() + "" + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getVersion().getVersionPostfix();
                    text = text + " - " + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getOpDescription();

                    dlm.addElement(text);
                }
            }
        }
        return jDialogRevision;
    }

    /**
     * This method initializes jLabelDialogRevision
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabelDialogRevision() {
        if (jLabelDialogRevision == null) {
            jLabelDialogRevision = new javax.swing.JLabel();
            jLabelDialogRevision.setBounds(10, 10, 100, 20);
            jLabelDialogRevision.setText(xerb.getString("window.revision.revision") + ":");
        }
        return jLabelDialogRevision;
    }

    /**
     * This method initializes jScrollPaneDialogRevision
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPaneDialogRevision() {
        if (jScrollPaneDialogRevision == null) {
            jScrollPaneDialogRevision = new javax.swing.JScrollPane();
            jScrollPaneDialogRevision.setViewportView(getJListDialogRevision());
            jScrollPaneDialogRevision.setBounds(120, 10, 250, 100);
            jScrollPaneDialogRevision.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        return jScrollPaneDialogRevision;
    }

    /**
     * This method initializes jListDialogRevision
     *
     * @return javax.swing.JList
     */
    private javax.swing.JList getJListDialogRevision() {
        if (jListDialogRevision == null) {
            DefaultListModel dlm = new DefaultListModel();
            jListDialogRevision = new javax.swing.JList();
            jListDialogRevision.setModel(dlm);
            jListDialogRevision.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        }
        return jListDialogRevision;
    }

    /**
     * This method initializes jButtonDialogRevisionContinue
     *
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonDialogRevisionContinue() {
        if (jButtonDialogRevisionContinue == null) {
            jButtonDialogRevisionContinue = new javax.swing.JButton();
            jButtonDialogRevisionContinue.setBounds(120, 130, 100, 30);
            jButtonDialogRevisionContinue.setText(xerb.getString("general.continue"));
            jButtonDialogRevisionContinue.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int i = 0;
                    int RealLogIndex = -1;
                    if (jListDialogRevision.getSelectedIndex() >= 0) {
                        for (i = 0; i < ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().size(); i++) {
                            if ((((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getOpCode() == 1) || (((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getOpCode() == 5)) {
                                RealLogIndex++;
                            }
                            if (RealLogIndex == jListDialogRevision.getSelectedIndex()) {
                                globalDialog_returnValue = ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().elementAt(i)).getId();
                                break;
                            }
                        }
                        jDialogRevision.setVisible(false);
                    }
                }
            });
        }
        return jButtonDialogRevisionContinue;
    }

    /**
     * This method initializes jButtonDialogRevisionCancel
     *
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonDialogRevisionCancel() {
        if (jButtonDialogRevisionCancel == null) {
            jButtonDialogRevisionCancel = new javax.swing.JButton();
            jButtonDialogRevisionCancel.setBounds(240, 130, 100, 30);
            jButtonDialogRevisionCancel.setText(xerb.getString("general.cancel"));
            jButtonDialogRevisionCancel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jDialogRevision.setVisible(false);
                }
            });
        }
        return jButtonDialogRevisionCancel;
    }

    /**
     * This method initializes AbstractDialogData
     *
     * @return AbstractDialog
     */
    private AbstractDialog getAbstractDialogData() {
        if (AbstractDialogData == null) {
            AbstractDialogData = new DataDialog(null, true, this);
            addDialog(AbstractDialogData);
        }
        return AbstractDialogData;
    }

    /**
     * This method initializes AbstractDialogLog
     *
     * @return AbstractDialog
     */
    private AbstractDialog getAbstractDialogLog() {
        if (AbstractDialogLog == null) {
            AbstractDialogLog = new LogDialog(null, true, this);
            addDialog(AbstractDialogLog);
        }
        return AbstractDialogLog;
    }

    /**
     * This method imports files + subfolders of a folder into node
     * @param node
     * @param folder
     * @throws java.lang.Exception 
     */
    @SuppressWarnings("unchecked")
    public void importContentOfFolder(XincoCoreNode node, File folder) throws Exception {
        int i = 0;
        int j = 0;
        File[] folderList = null;
        folderList = folder.listFiles();
        newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
        XincoCoreNode xnode;
        newlog = new XincoCoreLog();
        XincoCoreDataType xcdt1 = null;
        //find data type = 1
        for (j = 0; j < xincoClientSession.getServerDatatypes().size(); j++) {
            if (((XincoCoreDataType) xincoClientSession.getServerDatatypes().elementAt(j)).getId() == 1) {
                xcdt1 = (XincoCoreDataType) xincoClientSession.getServerDatatypes().elementAt(j);
                break;
            }
        }
        //find default language
        XincoCoreLanguage xcl1 = null;
        int selection = -1;
        int altSelection = 0;
        for (j = 0; j < xincoClientSession.getServerLanguages().size(); j++) {
            if (((XincoCoreLanguage) xincoClientSession.getServerLanguages().elementAt(j)).getSign().toLowerCase().compareTo(Locale.getDefault().getLanguage().toLowerCase()) == 0) {
                selection = j;
                break;
            }
            if (((XincoCoreLanguage) xincoClientSession.getServerLanguages().elementAt(j)).getId() == 1) {
                altSelection = j;
            }
        }
        if (selection == -1) {
            selection = altSelection;
        }
        xcl1 = (XincoCoreLanguage) xincoClientSession.getServerLanguages().elementAt(selection);
        //process files
        getProgressBar().setTitle(xerb.getString("datawizard.fileuploadinfo"));
        getProgressBar().show();
        for (i = 0; i < folderList.length; i++) {
            if (folderList.length > 0 && folderList[i].isFile()) {
                if (getSettings().getSetting("setting.enable.developermode").isBoolValue()) {
                    System.out.println("Processing file: " + folderList[i].getName() + "(" + (i + 1) + "/" + folderList.length + ")");
                }
                // set current node to new one
                newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
                // set data attributes
                ((XincoCoreData) newnode.getUserObject()).setXincoCoreNodeId(node.getId());
                ((XincoCoreData) newnode.getUserObject()).setDesignation(folderList[i].getName());
                ((XincoCoreData) newnode.getUserObject()).setXincoCoreDataType(xcdt1);
                ((XincoCoreData) newnode.getUserObject()).setXincoCoreLanguage(xcl1);
                ((XincoCoreData) newnode.getUserObject()).setXincoAddAttributes(new Vector());
                ((XincoCoreData) newnode.getUserObject()).setXincoCoreACL(new Vector());
                ((XincoCoreData) newnode.getUserObject()).setXincoCoreLogs(new Vector());
                ((XincoCoreData) newnode.getUserObject()).setStatusNumber(1);
                if (xincoClientSession.getCurrentTreeNodeSelection() == null) {
                    xincoClientSession.setCurrentTreeNodeSelection(previousnode);
                }
                xincoClientSession.getXincoClientRepository().treemodel.insertNodeInto(newnode,
                        xincoClientSession.getCurrentTreeNodeSelection(),
                        xincoClientSession.getCurrentTreeNodeSelection().getChildCount());
                previousnode = xincoClientSession.getCurrentTreeNodeSelection();
                // add specific attributes
                XincoAddAttribute xaa;

                for (j = 0; j <
                        ((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getXincoCoreDataTypeAttributes().size(); j++) {
                    xaa = new XincoAddAttribute();
                    xaa.setAttributeId(((XincoCoreDataTypeAttribute) xcdt1.getXincoCoreDataTypeAttributes().elementAt(j)).getAttributeId());
                    xaa.setAttribVarchar("");
                    xaa.setAttribText("");
                    xaa.setAttribDatetime(new GregorianCalendar());
                    ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().addElement(xaa);
                }
                // add log
                newlog = new XincoCoreLog();
                newlog.setOpCode(1);
                newlog.setOpDescription(xerb.getString("datawizard.logging.creation") +
                        "!" + " (" +
                        xerb.getString("general.user") + ": " +
                        xincoClientSession.getUser().getUsername() +
                        ")");
                newlog.setXincoCoreUserId(xincoClientSession.getUser().getId());
                newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId());
                newlog.setVersion(new XincoVersion());
                newlog.getVersion().setVersionHigh(1);
                newlog.getVersion().setVersionMid(0);
                newlog.getVersion().setVersionLow(0);
                newlog.getVersion().setVersionPostfix("");
                ((XincoCoreData) newnode.getUserObject()).setXincoCoreLogs(new Vector());
                ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().addElement(newlog);
                // invoke web service (update data / upload file / add log)
                // load file
                long totalLen = 0;
                boolean useSAAJ = false;

                if (((xincoClientSession.getServerVersion().getVersionHigh() == 1) &&
                        (xincoClientSession.getServerVersion().getVersionMid() >= 9)) ||
                        (xincoClientSession.getServerVersion().getVersionHigh() > 1)) {
                    useSAAJ = true;
                } else {
                    useSAAJ = false;
                }
                CheckedInputStream cin = null;
                ByteArrayOutputStream out = null;

                byteArray = null;
                try {
                    cin = new CheckedInputStream(new FileInputStream(folderList[i]),
                            new CRC32());
                    if (useSAAJ) {
                        totalLen = folderList[i].length();
                    } else {
                        out = new ByteArrayOutputStream();
                        byte[] buf = new byte[4096];
                        int len = 0;

                        totalLen = 0;
                        while ((len = cin.read(buf)) > 0) {
                            out.write(buf, 0, len);
                            totalLen = totalLen + len;
                        }
                        byteArray = out.toByteArray();
                        out.close();
                    }
                    // update attributes
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(0)).setAttribVarchar(folderList[i].getName());
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(1)).setAttribUnsignedint(totalLen);
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(2)).setAttribVarchar("" +
                            cin.getChecksum().getValue());
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(3)).setAttribUnsignedint(1);
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(4)).setAttribUnsignedint(0);
                    if (!useSAAJ) {
                        cin.close();
                    }
                } catch (Exception fe) {
                    if (getSettings().getSetting("setting.enable.developermode").isBoolValue()) {
                        Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, fe);
                    }
                    resetExplorer();
                    throw new XincoException(xerb.getString("datawizard.unabletoloadfile"));
                }
                // save data to server
                setXdata(xincoClientSession.getXinco().setXincoCoreData((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser()));
                if (getXdata() == null) {
                    throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
                }
                newnode.setUserObject(getXdata());
                // update id cin log
                newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId());
                // save log to server
                newlog = xincoClientSession.getXinco().setXincoCoreLog(newlog, xincoClientSession.getUser());
                if (newlog == null) {
                }
                // attach file to SOAP message
                if (useSAAJ) {
                    AttachmentPart ap = null;
                    ap = new AttachmentPart();
                    ap.setContent(cin, "unknown/unknown");
                    ((XincoSoapBindingStub) xincoClientSession.getXinco()).addAttachment(ap);
                }
                if (this.getFilesToBeIndexed() == null) {
                    this.setFilesToBeIndexed(new Vector());
                }
                this.getFilesToBeIndexed().add((XincoCoreData) newnode.getUserObject());
                // upload file
                if (xincoClientSession.getXinco().uploadXincoCoreData((XincoCoreData) newnode.getUserObject(),
                        byteArray, xincoClientSession.getUser()) != totalLen) {
                    ((XincoSoapBindingStub) xincoClientSession.getXinco()).clearAttachments();
                    cin.close();
                    throw new XincoException(xerb.getString("datawizard.fileuploadfailed"));
                }
                ((XincoSoapBindingStub) xincoClientSession.getXinco()).clearAttachments();
                cin.close();
                // update treemodel
                xincoClientSession.getXincoClientRepository().treemodel.reload(newnode);
                xincoClientSession.getXincoClientRepository().treemodel.nodeChanged(newnode);
                // select parent of new node
                if (newnode.getParent() != null) {
                    xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) newnode.getParent());
                }
            }
        }
        //process directories
        for (i = 0; i < folderList.length; i++) {
            if (folderList[i].isDirectory()) {
                // set current node to new one
                newnode = new XincoMutableTreeNode(new XincoCoreNode(), this);
                // set node attributes
                ((XincoCoreNode) newnode.getUserObject()).setXincoCoreNodeId(node.getId());
                ((XincoCoreNode) newnode.getUserObject()).setDesignation(folderList[i].getName());
                ((XincoCoreNode) newnode.getUserObject()).setXincoCoreLanguage(xcl1);
                ((XincoCoreNode) newnode.getUserObject()).setStatusNumber(1);
                if (getSettings().getSetting("setting.enable.developermode").isBoolValue()) {
                    System.out.println("Node Info:");
                    System.out.println(((XincoCoreNode) newnode.getUserObject()).getXincoCoreNodeId());
                    System.out.println(((XincoCoreNode) newnode.getUserObject()).getDesignation());
                    System.out.println(((XincoCoreNode) newnode.getUserObject()).getXincoCoreLanguage().getChangerID());
                    System.out.println(((XincoCoreNode) newnode.getUserObject()).getStatusNumber());
                    System.out.println("------------------------");
                    System.out.println("Node Selected: " + xincoClientSession.getCurrentTreeNodeSelection());
                }
                if (getSettings().getSetting("setting.enable.developermode").isBoolValue()) {
                    System.out.println("Child Count: " + xincoClientSession.getCurrentTreeNodeSelection().getChildCount());
                    System.out.println("New node: " + newnode);
                }
                // Bug fix by cmichl for Import Data Structure java.lang.NullPointerException (http://www.bluecubs.com/viewtopic.php?xincoCoreUser=500)
                if (xincoClientSession.getCurrentTreeNodeSelection() == null) {
                    xincoClientSession.setCurrentTreeNodeSelection(previousnode);
                }
                //End bug fix
                xincoClientSession.getXincoClientRepository().treemodel.insertNodeInto(newnode,
                        xincoClientSession.getCurrentTreeNodeSelection(),
                        xincoClientSession.getCurrentTreeNodeSelection().getChildCount());
                previousnode = xincoClientSession.getCurrentTreeNodeSelection();
                xincoClientSession.setCurrentTreeNodeSelection(newnode);
                // save node to server
                xnode = xincoClientSession.getXinco().setXincoCoreNode((XincoCoreNode) newnode.getUserObject(), xincoClientSession.getUser());
                if (xnode == null) {
                    throw new XincoException(xerb.getString("window.folder.updatefailed"));
                }
                newnode.setUserObject(xnode);
                // update treemodel
                xincoClientSession.getXincoClientRepository().treemodel.reload(newnode);
                xincoClientSession.getXincoClientRepository().treemodel.nodeChanged(newnode);
                // start recursion
                importContentOfFolder((XincoCoreNode) newnode.getUserObject(),
                        folderList[i]);
                // select parent of new node
                // Bug fix by cmichl for Import Data Structure java.lang.NullPointerException (http://www.bluecubs.com/viewtopic.php?xincoCoreUser=500)
                if (newnode.getParent() != null) {
                    xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) newnode.getParent());
                }
            //End bug fix
            }
        }
        getProgressBar().hide();
    }

    /**
     * This method leads through data adding/editing
     *
     * @param wizardType 
     */
    @SuppressWarnings("unchecked")
    public void doDataWizard(final int wizardType) {
        this.wizardType = wizardType;
        /*
        wizard type	= 1  = add new data
        = 2  = edit data object
        = 3  = edit add attributes
        = 4  = checkout data
        = 5  = undo checkout
        = 6  = checkin data
        = 7  = download data
        = 8  = open URL cin browser
        = 9  = open email client with contact information
        = 10 = publish data
        = 11 = download previous revision
        = 12 = lock data
        = 13 = comment data
        = 14 = preview data
         */
        int i = 0, j = 0;
        newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
        setXdata(null);
        newlog = new XincoCoreLog();

        in = null;
        byteArray = null;
        if (xincoClientSession.getCurrentTreeNodeSelection() != null) {
            //execute wizard as a whole
            try {
                //add new data
                if ((wizardType == 1) &&
                        (xincoClientSession.getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class)) {

                    //set current node to new one
                    newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
                    //set data attributes
                    ((XincoCoreData) newnode.getUserObject()).setXincoCoreNodeId(((XincoCoreNode) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getId());
                    ((XincoCoreData) newnode.getUserObject()).setDesignation(xerb.getString("datawizard.newdata"));
                    ((XincoCoreData) newnode.getUserObject()).setXincoCoreDataType((XincoCoreDataType) xincoClientSession.getServerDatatypes().elementAt(0));
                    ((XincoCoreData) newnode.getUserObject()).setXincoCoreLanguage((XincoCoreLanguage) xincoClientSession.getServerLanguages().elementAt(0));
                    ((XincoCoreData) newnode.getUserObject()).setXincoAddAttributes(new Vector());
                    ((XincoCoreData) newnode.getUserObject()).setXincoCoreACL(new Vector());
                    ((XincoCoreData) newnode.getUserObject()).setXincoCoreLogs(new Vector());
                    ((XincoCoreData) newnode.getUserObject()).setStatusNumber(1);
                    xincoClientSession.getXincoClientRepository().treemodel.insertNodeInto(newnode, xincoClientSession.getCurrentTreeNodeSelection(), xincoClientSession.getCurrentTreeNodeSelection().getChildCount());
                    xincoClientSession.setCurrentTreeNodeSelection(newnode);

                    //step 1: select data type
                    AbstractDialogDataType = new DataTypeDialog(null, true, this);
                    globalDialog_returnValue = 0;
                    AbstractDialogDataType.setVisible(true);
                    if (globalDialog_returnValue == 0) {
                        this.getProgressBar().hide();
                        throw new XincoException(xerb.getString("datawizard.updatecancel"));
                    }

                    //add specific attributes
                    XincoAddAttribute xaa;
                    for (i = 0; i < ((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getXincoCoreDataTypeAttributes().size(); i++) {
                        xaa = new XincoAddAttribute();
                        xaa.setAttributeId(((XincoCoreDataTypeAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getAttributeId());
                        xaa.setAttribVarchar("");
                        xaa.setAttribText("");
                        xaa.setAttribDatetime(new GregorianCalendar());
                        ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().addElement(xaa);
                    }

                    //initialize specific attributes:
                    //files
                    if (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1) {
                        ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(3)).setAttribUnsignedint(1); //revision model

                        ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(4)).setAttribUnsignedint(0); //archiving model

                    }

                }

                if (xincoClientSession.getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreData.class) {

                    newnode = xincoClientSession.getCurrentTreeNodeSelection();

                    //check file attribute count
                    //file = 1
                    if ((wizardType == 3) &&
                            ((((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1) &&
                            (((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().size() <= 3))) {
                        this.getProgressBar().hide();
                        throw new XincoException(xerb.getString("datawizard.noaddattributes"));
                    }

                    //edit add attributes
                    if ((wizardType == 1) || (wizardType == 3)) {

                        //step 2: edit add attributes
                        //for files -> show filechooser
                        //file = 1
                        if ((wizardType == 1) &&
                                (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1)) {
                            JFileChooser fc = new JFileChooser();

                            fc.setCurrentDirectory(new File(current_path));
                            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            // show dialog
                            int result = fc.showOpenDialog(XincoExplorer.this);

                            if (result != JFileChooser.APPROVE_OPTION) {
                                this.getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            setCurrentPathFilename(fc.getSelectedFile().getPath());
                            ((XincoCoreData) newnode.getUserObject()).setDesignation(current_filename);
                        }
                        //for text -> show text editing dialog
                        //text = 2
                        if (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 2) {
                            AbstractDialogAddAttributesText = getAbstractDialogAddAttributesText();
                            globalDialog_returnValue = 0;
                            if (globalDialog_returnValue == 0) {
                                this.getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }
                        //show dialog for all additional attributes and custom data types
                        //file = 1 / text = 2
                        if ((((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() !=
                                1 ||
                                ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().size() >
                                8) &&
                                (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() !=
                                2 ||
                                ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().size() >
                                1)) {
                            //for other data type -> show universal add attribute dialog
                            AbstractDialogAddAttributesUniversal = new AddAttributeUniversalDialog(null, true, this);
                            globalDialog_returnValue = 0;
                            AbstractDialogAddAttributesUniversal.setVisible(true);
                            if (globalDialog_returnValue == 0) {
                                this.getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }

                    }

                    //edit logging
                    //step 3: edit logging (creation!)
                    if (wizardType == 1) {
                        newlog = new XincoCoreLog();
                        newlog.setOpCode(1);
                        newlog.setOpDescription(xerb.getString("datawizard.logging.creation") + "!");
                        newlog.setXincoCoreUserId(xincoClientSession.getUser().getId());
                        newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId()); //update to new id later!

                        newlog.setVersion(new XincoVersion());
                        newlog.getVersion().setVersionHigh(1);
                        newlog.getVersion().setVersionMid(0);
                        newlog.getVersion().setVersionLow(0);
                        newlog.getVersion().setVersionPostfix("");
                        ((XincoCoreData) newnode.getUserObject()).setXincoCoreLogs(new Vector());
                        ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().addElement(newlog);
                        AbstractDialogLog = getAbstractDialogLog();
                        globalDialog_returnValue = 0;
                        AbstractDialogLog.setVisible(true);
                        if (globalDialog_returnValue == 0) {
                            this.getProgressBar().hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
                        newlog.setOpDescription(newlog.getOpDescription() + " (" + xerb.getString("general.user") + ": " + xincoClientSession.getUser().getUsername() + ")");
                    } else {
                        if ((wizardType != 7) && (wizardType != 8) && (wizardType != 9) && (wizardType != 11) && (wizardType != 14)) {
                            newlog = new XincoCoreLog();
                            if (wizardType <= 3) {
                                newlog.setOpCode(2);
                                newlog.setOpDescription(xerb.getString("datawizard.logging.modification") + "!");
                            }
                            if (wizardType == 4) {
                                newlog.setOpCode(3);
                                newlog.setOpDescription(xerb.getString("datawizard.logging.checkoutchangesplanned"));
                            }
                            if (wizardType == 5) {
                                newlog.setOpCode(4);
                                newlog.setOpDescription(xerb.getString("datawizard.logging.checkoutundone"));
                            }
                            if (wizardType == 6) {
                                newlog.setOpCode(5);
                                newlog.setOpDescription(xerb.getString("datawizard.logging.checkinchangesmade"));
                            }
                            if (wizardType == 10) {
                                newlog.setOpCode(6);
                                newlog.setOpDescription(xerb.getString("datawizard.logging.publishcomment"));
                            }
                            if (wizardType == 12) {
                                newlog.setOpCode(7);
                                newlog.setOpDescription(xerb.getString("datawizard.logging.lockcomment"));
                            }
                            if (wizardType == 13) {
                                newlog.setOpCode(9);
                                newlog.setOpDescription(xerb.getString("datawizard.logging.commentcomment"));
                            }
                            newlog.setXincoCoreUserId(xincoClientSession.getUser().getId());
                            newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId());
                            newlog.setVersion(((XincoCoreLog) ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().elementAt(((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().size() - 1)).getVersion());
                            ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().addElement(newlog);
                            AbstractDialogLog = getAbstractDialogLog();
                            globalDialog_returnValue = 0;
                            AbstractDialogLog.setVisible(true);
                            if (globalDialog_returnValue == 0) {
                                this.getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            newlog.setOpDescription(newlog.getOpDescription() + " (" + xerb.getString("general.user") + ": " + xincoClientSession.getUser().getUsername() + ")");
                        }
                    }

                    //choose filename for checkout/checkin/download/preview
                    if ((wizardType == 4) || (wizardType == 6) || (wizardType == 7) || (wizardType == 11)) {
                        JFileChooser fc = new JFileChooser();

                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fc.setCurrentDirectory(new File(current_path + ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(0)).getAttribVarchar()));
                        fc.setCurrentDirectory(new File(current_path));
                        fc.setSelectedFile(new File(current_path +
                                ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(0)).getAttribVarchar()));
                        // show dialog
                        int result;

                        if ((wizardType == 4) || (wizardType == 7) ||
                                (wizardType == 11)) {
                            result = fc.showSaveDialog(XincoExplorer.this);
                        } else {
                            result = fc.showOpenDialog(XincoExplorer.this);
                        }
                        if (result != JFileChooser.APPROVE_OPTION) {
                            this.getProgressBar().hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
                        setCurrentPathFilename(fc.getSelectedFile().getPath());
                    }
                    if (wizardType == 14) {
                        setCurrentPathFilename(File.createTempFile("xinco_", "_" + ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(0)).getAttribVarchar()).getPath());
                    }

                    //edit data details
                    if ((wizardType == 1) || (wizardType == 2)) {

                        //step 4: edit data details
                        AbstractDialogData = getAbstractDialogData();
                        globalDialog_returnValue = 0;
                        AbstractDialogData.setVisible(true);
                        if (globalDialog_returnValue == 0) {
                            this.getProgressBar().hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }

                        //step 4b: edit archiving options of files
                        if (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1) {
                            AbstractDialogArchive = getAbstractDialogArchive();
                            globalDialog_returnValue = 0;
                            AbstractDialogArchive.setVisible(true);
                            if (globalDialog_returnValue == 0) {
                                this.getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }

                    }
                    //set status = published
                    if (wizardType == 10) {
                        ((XincoCoreData) newnode.getUserObject()).setStatusNumber(5);
                    }
                    //set status = locked
                    if (wizardType == 12) {
                        ((XincoCoreData) newnode.getUserObject()).setStatusNumber(2);
                    }

                    //invoke web service (update data / (upload file) / add log)
                    //load file (new / checkin)
                    long totalLen = 0;
                    boolean useSAAJ = false;
                    if (((xincoClientSession.getServerVersion().getVersionHigh() == 1) && (xincoClientSession.getServerVersion().getVersionMid() >= 9)) || (xincoClientSession.getServerVersion().getVersionHigh() > 1)) {
                        useSAAJ = true;
                    } else {
                        useSAAJ = false;
                    }
                    ByteArrayOutputStream out = null;
                    //file = 1
                    if (((wizardType == 1) || (wizardType == 6)) && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1)) {
                        try {
                            //update transaction info
                            getProgressBar().setTitle(xerb.getString("datawizard.fileuploadinfo"));
                            getProgressBar().show();
                            jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.fileuploadinfo"));
                            in = new CheckedInputStream(new FileInputStream(current_fullpath), new CRC32());
                            if (useSAAJ) {
                                totalLen = (new File(current_fullpath)).length();
                            } else {
                                out = new ByteArrayOutputStream();
                                byte[] buf = new byte[4096];
                                int len = 0;
                                totalLen = 0;
                                while ((len = in.read(buf)) > 0) {
                                    out.write(buf, 0, len);
                                    totalLen = totalLen + len;
                                }
                                byteArray = out.toByteArray();
                                out.close();
                            }
                            //update attributes
                            ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(0)).setAttribVarchar(current_filename);
                            ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(1)).setAttribUnsignedint(totalLen);
                            ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(2)).setAttribVarchar("" + ((CheckedInputStream) in).getChecksum().getValue());
                            if (!useSAAJ) {
                                in.close();
                            }
                        } catch (Exception fe) {
                            getProgressBar().hide();
                            resetExplorer();
                            throw new XincoException(xerb.getString("datawizard.unabletoloadfile"));
                        }
                    }
                    //save data to server
                    if ((wizardType != 7) && (wizardType != 8) && (wizardType != 9) && (wizardType != 11) && (wizardType != 14)) {
                        if ((wizardType >= 4) && (wizardType <= 6)) {
                            if (wizardType == 4) {
                                setXdata(xincoClientSession.getXinco().doXincoCoreDataCheckout((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser()));
                            } else {
                                if (wizardType == 5) {
                                    setXdata(xincoClientSession.getXinco().undoXincoCoreDataCheckout((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser()));
                                } else {
                                    setXdata(xincoClientSession.getXinco().doXincoCoreDataCheckin((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser()));
                                }
                            }
                        } else {
                            setXdata(xincoClientSession.getXinco().setXincoCoreData((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser()));
                        }
                        if (getXdata() == null) {
                            throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
                        }
                        newnode.setUserObject(getXdata());
                    }
                    if ((wizardType != 7) && (wizardType != 8) && (wizardType != 9) && (wizardType != 11) && (wizardType != 14)) {
                        //update id in log
                        newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId());
                        //save log to server
                        newlog = xincoClientSession.getXinco().setXincoCoreLog(newlog, xincoClientSession.getUser());
                        if (newlog == null) {
                            //System.out.println(xerb.getString("datawizard.savelogfailed"));
                        } else {
                            ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().addElement(newlog);
                        }
                    }
                    //upload file (new / checkin)
                    //file = 1
                    if (((wizardType == 1) || (wizardType == 6)) && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1)) {
                        //attach file to SOAP message
                        if (useSAAJ) {
                            AttachmentPart ap = null;
                            ap = new AttachmentPart();
                            ap.setContent(in, "unknown/unknown");
                            ((XincoSoapBindingStub) xincoClientSession.getXinco()).addAttachment(ap);
                        }

                        if (xincoClientSession.getXinco().uploadXincoCoreData((XincoCoreData) newnode.getUserObject(), byteArray, xincoClientSession.getUser()) != totalLen) {
                            ((XincoSoapBindingStub) xincoClientSession.getXinco()).clearAttachments();
                            in.close();
                            JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.fileuploadfailed"), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                        }
                        ((XincoSoapBindingStub) xincoClientSession.getXinco()).clearAttachments();
                        in.close();
                        //update transaction info
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.fileuploadsuccess"));
                        getProgressBar().hide();
                    }
                    //download file
                    //file = 1
                    if (((wizardType == 4) || (wizardType == 7) || (wizardType == 11) ||
                            (wizardType == 14)) &&
                            (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1)) {
                        //determine requested revision and set log vector
                        getProgressBar().setTitle(xerb.getString("datawizard.filedownloadinfo"));
                        getProgressBar().show();
                        DataLogVector = null;
                        if (wizardType == 11) {
                            jDialogRevision = getJDialogRevision();
                            globalDialog_returnValue = -1;
                            jDialogRevision.setVisible(true);
                            if (globalDialog_returnValue == -1) {
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            DataLogVector = ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs();
                            XincoCoreLog RevLog = null;
                            for (i = 0; i < ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().size(); i++) {
                                if (((XincoCoreLog) ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().elementAt(i)).getId() == globalDialog_returnValue) {
                                    RevLog = (XincoCoreLog) (((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().elementAt(i));
                                    break;
                                }
                            }
                            Vector RevLogVector = new Vector();
                            RevLogVector.add(RevLog);
                            ((XincoCoreData) newnode.getUserObject()).setXincoCoreLogs(RevLogVector);
                        }

                        //update transaction info
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.filedownloadinfo"));
                        //call service
                        try {
                            Message m = null;
                            MessageContext mc = null;
                            AttachmentPart ap = null;
                            Call call = (Call) xincoClientSession.getXincoService().createCall();
                            call.setTargetEndpointAddress(new URL(xincoClientSession.getServiceEndpoint()));
                            call.setOperationName(new QName("urn:Xinco", "downloadXincoCoreData"));
                            Object[] objp = new Object[2];
                            objp[0] = (XincoCoreData) newnode.getUserObject();
                            objp[1] = xincoClientSession.getUser();
                            //tell server to send file as attachment
                            //(keep backward compatibility to earlier versions)
                            ap = new AttachmentPart();
                            ap.setContent(new String("SAAJ"), "text/string");
                            call.addAttachmentPart(ap);
                            //invoke actual call
                            byteArray = (byte[]) call.invoke(objp);
                            //get file from SOAP message or byte array
                            mc = call.getMessageContext();
                            m = mc.getResponseMessage();
                            if (m.getAttachments().hasNext()) {
                                ap = (AttachmentPart) m.getAttachments().next();
                                in = (InputStream) ap.getContent();
                            } else {
                                in = new ByteArrayInputStream(byteArray);
                            }
                        } catch (Exception ce) {
                            //reassign log vector
                            if (wizardType == 11) {
                                ((XincoCoreData) newnode.getUserObject()).setXincoCoreLogs(DataLogVector);
                            }
                            JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.filedownloadfailed"), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                            this.getProgressBar().hide();
                            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ce);
                            resetExplorer();
                            throw (ce);
                        }

                        //reassign log vector
                        if (wizardType == 11) {
                            ((XincoCoreData) newnode.getUserObject()).setXincoCoreLogs(DataLogVector);
                        }

                        //ByteArrayInputStream cin = new ByteArrayInputStream(byteArray);
                        CheckedOutputStream couts = new CheckedOutputStream(new FileOutputStream(current_fullpath), new CRC32());
                        byte[] buf = new byte[4096];
                        int len = 0;
                        totalLen = 0;
                        while ((len = in.read(buf)) > 0) {
                            couts.write(buf, 0, len);
                            totalLen = totalLen + len;
                        }
                        in.close();
                        ((XincoSoapBindingStub) xincoClientSession.getXinco()).clearAttachments();
                        //check correctness of data
                        if (wizardType != 11) {
                            if (((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(1)).getAttribUnsignedint() != totalLen) {
                                JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.filedownloadcorrupted"), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        couts.close();
                        //make sure temp. file is deleted on exit
                        if (wizardType == 14) {
                            (new File(current_fullpath)).deleteOnExit();
                        }
                        //update transaction info
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.filedownloadsuccess"));
                        //open file cin default application
                        Process process = null;
                        boolean open_file = false;
                        if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                            if (wizardType == 14) {
                                open_file = true;
                            } else {
                                if (JOptionPane.showConfirmDialog(XincoExplorer.this, xerb.getString("datawizard.opendataindefaultapplication"), xerb.getString("general.question"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                    open_file = true;
                                }
                            }
                            if (open_file) {
                                try {
                                    String[] cmd = {"open", current_fullpath};
                                    process = Runtime.getRuntime().exec(cmd);
                                } catch (Throwable t) {
                                    Logger.getLogger(XincoExplorer.class.getName()).log(Level.INFO, null, t);
                                }
                            }
                        } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                            if (wizardType == 14) {
                                open_file = true;
                            } else {
                                if (JOptionPane.showConfirmDialog(XincoExplorer.this, xerb.getString("datawizard.opendataindefaultapplication"), xerb.getString("general.question"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                    open_file = true;
                                }
                            }
                            if (open_file) {
                                try {
                                    String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + current_fullpath + "\"";
                                    process = Runtime.getRuntime().exec(cmd);
                                } catch (Throwable t) {
                                    Logger.getLogger(XincoExplorer.class.getName()).log(Level.INFO, null, t);
                                }
                            }
                        }
                        getProgressBar().hide();
                    }
                    //Open in Browser
                    //URL = 3
                    if ((wizardType == 8) && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 3)) {
                        //open URL cin default browser
                        Process process = null;
                        String tempUrl = ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(0)).getAttribVarchar();
                        if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                            try {
                                String[] cmd = {"open", tempUrl};
                                process = Runtime.getRuntime().exec(cmd);
                            } catch (Throwable t) {
                                Logger.getLogger(XincoExplorer.class.getName()).log(Level.INFO, null, t);
                            }
                        } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                            try {
                                String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + tempUrl + "\"";
                                process = Runtime.getRuntime().exec(cmd);
                            } catch (Throwable t) {
                                Logger.getLogger(XincoExplorer.class.getName()).log(Level.INFO, null, t);
                            }
                        }
                    }
                    //Open in Email Client
                    //contact = 4
                    if ((wizardType == 9) && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 4)) {
                        //open URL cin default browser
                        Process process = null;
                        String temp_email = ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().elementAt(9)).getAttribVarchar();
                        if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                            try {
                                String[] cmd = {"open", "mailto:" + temp_email};
                                process = Runtime.getRuntime().exec(cmd);
                            } catch (Throwable t) {
                                Logger.getLogger(XincoExplorer.class.getName()).log(Level.INFO, null, t);
                            }
                        } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                            try {
                                String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + "mailto:" + temp_email + "\"";
                                process = Runtime.getRuntime().exec(cmd);
                            } catch (Throwable t) {
                                Logger.getLogger(XincoExplorer.class.getName()).log(Level.INFO, null, t);
                            }
                        }
                    }

                    if ((wizardType != 7) && (wizardType != 8) && (wizardType != 9) && (wizardType != 11) && (wizardType != 14)) {
                        //update treemodel
                        xincoClientSession.getXincoClientRepository().treemodel.reload(newnode);
                        xincoClientSession.getXincoClientRepository().treemodel.nodeChanged(newnode);
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.updatesuccess"));
                        if (wizardType == 10) {
                            String tempUrl = "";
                            //file = 1
                            if (getXdata().getXincoCoreDataType().getId() == 1) {
                                tempUrl = ((XincoAddAttribute) getXdata().getXincoAddAttributes().elementAt(0)).getAttribVarchar();
                            } else {
                                tempUrl = getXdata().getDesignation();
                            }
                            jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.updatesuccess.publisherinfo") + "\nhttp://[serverName]:[port]/xinco/XincoPublisher/" + getXdata().getId() + "/" + tempUrl);
                        }
                        jTreeRepository.setSelectionPath(new TreePath(((XincoMutableTreeNode) xincoClientSession.getCurrentTreeNodeSelection().getParent()).getPath()));
                        jTreeRepository.setSelectionPath(new TreePath(xincoClientSession.getCurrentTreeNodeSelection().getPath()));
                    }

                }
            } catch (Exception we) {
                //update transaction info
                jLabelInternalFrameInformationText.setText("");
                //remove new data cin case off error
                if (wizardType == 1) {
                    xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) xincoClientSession.getCurrentTreeNodeSelection().getParent());
                    xincoClientSession.getXincoClientRepository().treemodel.removeNodeFromParent(newnode);
                    jTreeRepository.setSelectionPath(new TreePath(xincoClientSession.getCurrentTreeNodeSelection().getPath()));
                }
                if (wizardType != 3 || globalDialog_returnValue != 0) {
                    JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.updatefailed") +
                            " " + xerb.getString("general.reason") + ": " +
                            we.toString(), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                }
                getProgressBar().hide();
                resetExplorer();
                Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, we);
            }

        }
    }

    /**
     * This method initializes jMenuPreferences
     *
     * @return javax.swing.JMenu
     */
    private javax.swing.JMenu getJMenuPreferences() {
        if (jMenuPreferences == null) {
            jMenuPreferences = new javax.swing.JMenu();
            jMenuPreferences.add(getJMenuItemPreferencesEditUser());
            jMenuPreferences.setText(xerb.getString("menu.preferences"));
            jMenuPreferences.setEnabled(false);
        }
        return jMenuPreferences;
    }

    /**
     * This method initializes jMenuItemPreferencesEditUser
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemPreferencesEditUser() {
        if (jMenuItemPreferencesEditUser == null) {
            jMenuItemPreferencesEditUser = new javax.swing.JMenuItem();
            jMenuItemPreferencesEditUser.setText(xerb.getString("menu.preferences.edituserinfo"));
            jMenuItemPreferencesEditUser.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getAbstractDialogUser(false);
                }
            });
        }
        return jMenuItemPreferencesEditUser;
    }

    /**
     * This method sets previous path and filename
     *
     * @return void
     */
    private void setPreviousPathFilename(String s) {
        int i = 0, j = 0;
        if (s != null) {
            try {
                setPreviousFullpath(s);
                i = s.lastIndexOf(System.getProperty("file.separator"));
                previous_filename = s.substring(i + 1);
                if (i > 0) {
                    previous_path = s.substring(0, i + 1);
                } else {
                    previous_path = "";
                }
            } catch (Throwable e) {
                previous_filename = "";
                previous_path = "";
                previous_fullpath = "";
            }
        } else {
            previous_filename = "";
            previous_path = "";
            previous_fullpath = "";
        }
    }

    /**
     * This method sets current path and filename
     *
     * @param s 
     */
    public void setCurrentPathFilename(String s) {
        int i = 0, j = 0;
        if (s != null) {
            try {
                setPreviousPathFilename(current_fullpath);
                current_fullpath = s;
                i = s.lastIndexOf(System.getProperty("file.separator"));
                current_filename = s.substring(i + 1);
                if (i > 0) {
                    current_path = s.substring(0, i + 1);
                } else {
                    current_path = "";
                }
            } catch (Throwable e) {
                current_filename = "";
                current_path = "";
                current_fullpath = "";
            }
        } else {
            current_filename = "";
            current_path = "";
            current_fullpath = "";
        }
    }

    /**
     * This method sets current path and filename
     *
     * @param s 
     */
    public void setCurrentPath(String s) {
        if (!(s.substring(s.length() - 1).equals(System.getProperty("file.separator")))) {
            s = s + System.getProperty("file.separator");
        }
        current_filename = "";
        current_path = s;
        current_fullpath = s;
    }

    /**
     * This method initializes jContentPaneDialogUser
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneDialogUser() {
        if (jContentPaneDialogUser == null) {
            jContentPaneDialogUser = new javax.swing.JPanel();
            jContentPaneDialogUser.setLayout(null);
            getAbstractDialogUser(false);
        }
        return jContentPaneDialogUser;
    }

    /**
     * This method initializes AbstractDialogUser
     *
     * @return AbstractDialog
     */
    private void getAbstractDialogUser(boolean aged) {
        if (userDialog == null) {
            userDialog = new UserDialog(new javax.swing.JFrame(), true, this, aged);
            addDialog(userDialog);
        }
        userDialog.setVisible(true);
    }

    /**
     * This method initializes jContentPaneDialogAddAttributesText
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneDialogAddAttributesText() {
        if (jContentPaneDialogAddAttributesText == null) {
            jContentPaneDialogAddAttributesText = new javax.swing.JPanel();
            jContentPaneDialogAddAttributesText.setLayout(null);
            jContentPaneDialogAddAttributesText.add(getJScrollPaneDialogAddAttributesText(), null);
            jContentPaneDialogAddAttributesText.add(getJButtonDialogAddAttributesTextSave(), null);
            jContentPaneDialogAddAttributesText.add(getJButtonDialogAddAttributesTextCancel(), null);
        }
        return jContentPaneDialogAddAttributesText;
    }

    /**
     * This method initializes AbstractDialogAddAttributesText
     * @return AbstractDialog
     */
    public AddAttributeText getAbstractDialogAddAttributesText() {
        if (AbstractDialogAddAttributesText == null) {
            AbstractDialogAddAttributesText = new AddAttributeText(null, true, viewOnly, this);
            addDialog(AbstractDialogAddAttributesText);
        }
        viewOnly = false;
        return AbstractDialogAddAttributesText;
    }

    /**
     * 
     * @return int
     */
    public int getActionSize() {
        return actionSize;
    }

    /**
     * 
     * @return Icon
     */
    public Icon getXincoIcon() {
        return new javax.swing.ImageIcon(XincoExplorer.class.getResource("blueCubsIcon16x16.GIF"));
    }

    /**
     * 
     * @return String
     */
    public String getSelectedNodeDesignation() {
        String nodeName = "";
        XincoMutableTreeNode node = getSession().getCurrentTreeNodeSelection();
        if (node.getUserObject().getClass() == XincoCoreNode.class) {
            nodeName = ((XincoCoreNode) node.getUserObject()).getDesignation();
        }
        if (node.getUserObject().getClass() == XincoCoreData.class) {
            nodeName = ((XincoCoreData) node.getUserObject()).getDesignation();
        }
        return nodeName;
    }

    /**
     * This method initializes jTextAreaDialogAddAttributesText
     *
     * @return javax.swing.JTextArea
     */
    private javax.swing.JTextArea getJTextAreaDialogAddAttributesText() {
        if (jTextAreaDialogAddAttributesText == null) {
            jTextAreaDialogAddAttributesText = new javax.swing.JTextArea();
        }
        return jTextAreaDialogAddAttributesText;
    }

    /**
     * This method initializes jButtonDialogAddAttributesTextSave
     *
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonDialogAddAttributesTextSave() {
        if (jButtonDialogAddAttributesTextSave == null) {
            jButtonDialogAddAttributesTextSave = new javax.swing.JButton();
            jButtonDialogAddAttributesTextSave.setBounds(350, 450, 100, 30);
            jButtonDialogAddAttributesTextSave.setText(xerb.getString("general.save") + "!");
            jButtonDialogAddAttributesTextSave.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ((XincoAddAttribute) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoAddAttributes().elementAt(0)).setAttribText(jTextAreaDialogAddAttributesText.getText());
                    globalDialog_returnValue = 1;
                    AbstractDialogAddAttributesText.setVisible(false);
                }
            });
        }
        return jButtonDialogAddAttributesTextSave;
    }

    /**
     * This method initializes jButtonDialogAddAttributesTextCancel
     *
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonDialogAddAttributesTextCancel() {
        if (jButtonDialogAddAttributesTextCancel == null) {
            jButtonDialogAddAttributesTextCancel = new javax.swing.JButton();
            jButtonDialogAddAttributesTextCancel.setBounds(470, 450, 100, 30);
            jButtonDialogAddAttributesTextCancel.setText(xerb.getString("general.cancel"));
            jButtonDialogAddAttributesTextCancel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    AbstractDialogAddAttributesText.setVisible(false);
                }
            });
        }
        return jButtonDialogAddAttributesTextCancel;
    }

    /**
     * This method initializes jScrollPaneDialogAddAttributesText
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPaneDialogAddAttributesText() {
        if (jScrollPaneDialogAddAttributesText == null) {
            jScrollPaneDialogAddAttributesText = new javax.swing.JScrollPane();
            jScrollPaneDialogAddAttributesText.setViewportView(getJTextAreaDialogAddAttributesText());
            jScrollPaneDialogAddAttributesText.setBounds(10, 10, 560, 420);
            jScrollPaneDialogAddAttributesText.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPaneDialogAddAttributesText.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        }
        return jScrollPaneDialogAddAttributesText;
    }

    /**
     * This method initializes jContentPaneDialogTransactionInfo
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneDialogTransactionInfo() {
        if (jContentPaneDialogTransactionInfo == null) {
            jContentPaneDialogTransactionInfo = new javax.swing.JPanel();
            jContentPaneDialogTransactionInfo.setLayout(null);
            jContentPaneDialogTransactionInfo.add(getJLabelDialogTransactionInfoText(), null);
        }
        return jContentPaneDialogTransactionInfo;
    }

    /**
     * This method initializes AbstractDialogTransactionInfo
     *
     * @return AbstractDialog
     */
    private JDialog getJDialogTransactionInfo() {
        if (AbstractDialogTransactionInfo == null) {
            AbstractDialogTransactionInfo = new JDialog();
            AbstractDialogTransactionInfo.setContentPane(getJContentPaneDialogTransactionInfo());
            AbstractDialogTransactionInfo.setBounds(600, 200, 400, 150);
            AbstractDialogTransactionInfo.setTitle(xerb.getString("window.transactioninfo"));
            AbstractDialogTransactionInfo.setResizable(false);
            AbstractDialogTransactionInfo.setModal(false);
        }
        return AbstractDialogTransactionInfo;
    }

    /**
     * This method initializes jLabelDialogTransactionInfoText
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabelDialogTransactionInfoText() {
        if (jLabelDialogTransactionInfoText == null) {
            jLabelDialogTransactionInfoText = new javax.swing.JLabel();
            jLabelDialogTransactionInfoText.setBounds(10, 10, 370, 80);
        }
        //independent of creation
        jLabelDialogTransactionInfoText.setText("");
        return jLabelDialogTransactionInfoText;
    }

    /**
     * This method saves the configuration
     *
     */
    public void saveConfig() {
        try {
            java.io.FileOutputStream fout = new java.io.FileOutputStream(System.getProperty("user.home") + System.getProperty("file.separator") + "xincoClientConfig.dat");
            java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(fout);
            os.writeObject(xincoClientConfig);
            os.close();
            fout.close();
        } catch (IOException ex) {
            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method loads the configuration
     *
     * @return void
     */
    @SuppressWarnings("unchecked")
    private void loadConfig() {
        Vector tmpVector_old = new Vector();
        try {
            Vector tmpVector;
            FileInputStream fin;
            ObjectInputStream ois;

            //get old settingsRB
            try {
                fin = new FileInputStream("xincoClientConnectionProfiles.dat");
                ois = new ObjectInputStream(fin);
                try {
                    while ((tmpVector = (Vector) ois.readObject()) != null) {
                        tmpVector_old = tmpVector;
                    }
                } catch (Exception ioe3) {
                }
                ois.close();
                fin.close();
            } catch (Exception ioe2) {
                tmpVector_old = null;
            }

            fin = new FileInputStream(System.getProperty("user.home") + System.getProperty("file.separator") + "xincoClientConfig.dat");
            ois = new ObjectInputStream(fin);

            try {
                while ((tmpVector = (Vector) ois.readObject()) != null) {
                    xincoClientConfig = tmpVector;
                }
            } catch (Exception ioe3) {
            }

            ois.close();
            fin.close();

            //insert old settingsRB
            if (tmpVector_old != null) {
                xincoClientConfig.setElementAt(tmpVector_old, 0);
            }
            //delete old settingsRB
            (new File("xincoClientConnectionProfiles.dat")).delete();

        } catch (Exception ioe) {
            //error handling
            //create config
            xincoClientConfig = new Vector();
            //add connection profiles
            xincoClientConfig.addElement(new Vector());
            //insert old settingsRB
            if (tmpVector_old != null) {
                xincoClientConfig.setElementAt(tmpVector_old, 0);
                //delete old settings
                (new File("xincoClientConnectionProfiles.dat")).delete();
            } else {
                //insert connection profiles
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "xinco Demo User";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://xinco.org:8080/xincoDemo/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "user";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "user";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "xinco Demo Admin";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://xinco.org:8080/xincoDemo/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "admin";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "admin";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "Template Profile";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://[serverDomain]:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "yourUsername";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "your_password";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "Admin (localhost)";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "admin";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "admin";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "User (localhost)";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "user";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "user";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
            }
            //add Pluggable Look and Feel
            xincoClientConfig.addElement(new String("javax.swing.plaf.metal.MetalLookAndFeel"));
            //add locale
            xincoClientConfig.addElement(Locale.getDefault());
        }
    }

    /**
     * This method initializes AbstractDialogArchive
     *
     * @return AbstractDialog
     */
    private AbstractDialog getAbstractDialogArchive() {
        if (AbstractDialogArchive == null) {
            AbstractDialogArchive = new ArchiveDialog(null, true, this);
            AbstractDialogArchive.setTitle(xerb.getString("window.archive"));
            AbstractDialogArchive.setResizable(false);
            addDialog(AbstractDialogArchive);
        }
        return AbstractDialogArchive;
    }

    /**
     * 
     * @param v
     */
    public void setGlobalDialogReturnValue(int v) {
        this.globalDialog_returnValue = v;
    }

    /**
     * 
     * @return AbstractDialog
     */
    protected AbstractDialog getAbstractDialogLock() {
        if (lockDialog == null) {
            lockDialog = new LockDialog(null, true, this);
            addDialog(lockDialog);
        }
        lockDialog.setVisible(true);
        return lockDialog;
    }

    @SuppressWarnings("unchecked")
    private void addDialog(AbstractDialog dialog) {
        getDialogs().add(dialog);
    }

    /**
     * 
     * @return XincoActivityTimer
     */
    protected XincoActivityTimer getXat() {
        return xat;
    }

    public void actionPerformed(ActionEvent e) {
        resetTimer();
    }

    public void mouseClicked(MouseEvent e) {
        resetTimer();
    }

    public void mousePressed(MouseEvent e) {
        resetTimer();
    }

    public void mouseReleased(MouseEvent e) {
        resetTimer();
    }

    public void mouseEntered(MouseEvent e) {
        resetTimer();
    }

    public void mouseExited(MouseEvent e) {
        resetTimer();
    }

    /**
     * Reset activity timer
     */
    public void resetTimer() {
        if (this.isLock()) {
            this.getAbstractDialogLock();
        } else if (getXat() != null) {
            getXat().getActivityTimer().restart();
        }
    }

    /**
     * 
     * @return boolean
     */
    protected boolean isLock() {
        return lock;
    }

    /**
     * 
     * @return XincoRepositoryActionHandler
     */
    public XincoRepositoryActionHandler getActionHandler() {
        if (this.actionHandler == null) {
            this.actionHandler = new XincoRepositoryActionHandler(this);
        }
        return actionHandler;
    }
}
