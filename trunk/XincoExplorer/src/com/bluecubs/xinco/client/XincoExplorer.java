/**
 *Copyright 2005 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
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
 * Javier Ortiz     Aug-Dec 2006      1. Remove dialogs and windows from main code
 *                                    2. Incorporate 21 CFR regulatory features
 * Javier Ortiz     Jan-Dec 2007      1. Add new audit data feature
 *************************************************************
 */

package com.bluecubs.xinco.client;

import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.add.holders.XincoAddAttributeHolder;
import com.bluecubs.xinco.client.dialogs.ACLDialog;
import com.bluecubs.xinco.client.dialogs.AddAttributeUniversalDialog;
import com.bluecubs.xinco.client.dialogs.ArchiveDialog;
import com.bluecubs.xinco.client.dialogs.AuditDialog;
import com.bluecubs.xinco.client.dialogs.ConnectionDialog;
import com.bluecubs.xinco.client.dialogs.DataDialog;
import com.bluecubs.xinco.client.dialogs.DataFolderDialog;
import com.bluecubs.xinco.client.dialogs.DataTypeDialog;
import com.bluecubs.xinco.client.dialogs.LockDialog;
import com.bluecubs.xinco.client.dialogs.LogDialog;
import com.bluecubs.xinco.client.dialogs.SearchDialog;
import com.bluecubs.xinco.client.dialogs.UserDialog;
import com.bluecubs.xinco.client.object.XincoActivityTimer;
import com.bluecubs.xinco.client.object.XincoJTree;
import com.bluecubs.xinco.client.object.XincoMenuRepository;
import com.bluecubs.xinco.client.object.XincoPopUpMenuRepository;
import com.bluecubs.xinco.client.object.XincoProgressBarThread;
import com.bluecubs.xinco.client.object.XincoRepositoryActionHandler;
import com.bluecubs.xinco.client.object.XincoTreeCellRenderer;
import com.bluecubs.xinco.client.object.XincoWindowClosingAdapter;
import com.bluecubs.xinco.client.pane.XincoAuditPanel;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreDataType;
import com.bluecubs.xinco.core.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreLanguage;
import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoVersion;
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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
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
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
public class XincoExplorer extends JFrame implements ActionListener, MouseListener{
    
    //language resources, XincoExplorerResourceBundle
    private ResourceBundle xerb = null,xesettings=null;
    private XincoClientSetting settings=null;
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
    public XincoJTree jTreeRepository = null;
    private javax.swing.JTable jTableRepository = null;
    private javax.swing.JTable jTableAudit =null;
    private javax.swing.JMenu jMenuSearch = null;
    private javax.swing.JMenuItem jMenuItemSearchRepository = null;
    private javax.swing.JMenu jMenuView = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleWindows = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleJava = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleMotif = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleNapkin = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleSubstance = null;
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
    private String previous_filename = "";
    private String previous_path = "";
    public String previous_fullpath = "";
    //global dialog return value
    private int global_dialog_return_value = 0;
    private javax.swing.JMenuItem jMenuItemConnectionConnect = null;
    private javax.swing.JDialog jDialogConnection = null;
    private javax.swing.JDialog jDialogFolder = null;
    private javax.swing.JDialog jDialogACL = null;
    private javax.swing.JDialog jDialogDataType = null;
    private javax.swing.JDialog jDialogRevision = null;
    private javax.swing.JPanel jContentPaneDialogRevision = null;
    private javax.swing.JLabel jLabelDialogRevision = null;
    private javax.swing.JScrollPane jScrollPaneDialogRevision = null;
    private javax.swing.JList jListDialogRevision = null;
    private javax.swing.JButton jButtonDialogRevisionContinue = null;
    private javax.swing.JButton jButtonDialogRevisionCancel = null;
    private javax.swing.JDialog jDialogData = null;
    private javax.swing.JDialog jDialogArchive = null;
    private javax.swing.JDialog jDialogLog = null;
    private javax.swing.JDialog jDialogAddAttributesUniversal = null;
    private javax.swing.JMenu jMenuPreferences = null;
    private javax.swing.JMenuItem jMenuItemPreferencesEditUser = null;
    private javax.swing.JDialog jDialogUser = null;
    private javax.swing.JDialog jDialogAddAttributesText = null;
    private javax.swing.JPanel jContentPaneDialogAddAttributesText = null;
    private javax.swing.JTextArea jTextAreaDialogAddAttributesText = null;
    private javax.swing.JButton jButtonDialogAddAttributesTextSave = null;
    private javax.swing.JButton jButtonDialogAddAttributesTextCancel = null;
    private javax.swing.JScrollPane jScrollPaneDialogAddAttributesText = null;
    private javax.swing.JDialog jDialogTransactionInfo = null;
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
    private ConnectionDialog dialogConnection=null;
    private UserDialog userDialog=null;
    private JInternalFrame jInternalFrameInformation=null;
    private JMenuItem jMenuItemConnectionExit=null;
    private String status_string_1="",status_string_2="";
    private XincoCoreUser temp;
    private final XincoCoreUser newuser= new XincoCoreUser();
    private loginThread loginT=null;
    private refreshThread rThread=null;
    private int wizard_type;
    private XincoMutableTreeNode newnode,previousnode;
    private byte[] byte_array;
    private XincoCoreData xdata;
    private XincoCoreLog newlog;
    private final XincoExplorer explorer=this;
    private SearchDialog search;
    private Process process = null;
    private XincoAuditPanel xincoAuditPanel=null;
    private AuditDialog auditDialog=null;
    private XincoRepositoryActionHandler actionHandler=null;
    //Size of menu actions
    private int actionSize=20;
    private Vector settingsVector=null;
    private XincoAddAttributeHolder [] xaah=null;
    //Status of the explorer: lock = true - idle time limit exceeded, user must log in again to continue use
    //lock = false - work normally
    private boolean lock=false;
    private LockDialog lockDialog= null;
    private XincoActivityTimer xat = null;
    private JDialog [] dialogs=null;
    //Vector of XincoCoreData to be indexed
    private Vector filesToBeIndexed=null;
    private Vector audits = null;
    
    /**
     * This is the default constructor
     */
    public XincoExplorer() {
        super();
        try {
            setIconImage((new ImageIcon(XincoExplorer.class.getResource("blueCubsIcon.gif"))).getImage());
        } catch (Exception icone) {}
        //load config
        loadConfig();
        saveConfig();
        //Apply LAF to all screens including de Locale Dialog
        switchPLAF((String)xincoClientConfig.elementAt(1));
        //choose language
        getJDialogLocale().setVisible(true);
        //load language data
        Locale loc= null;
        try {
            if(((Locale)xincoClientConfig.elementAt(2)).toString().indexOf("_")==-1)
                loc = new Locale(((Locale)xincoClientConfig.elementAt(2)).toString());
            else
                loc = new Locale(((Locale)xincoClientConfig.elementAt(2)).toString().substring(0,((Locale)xincoClientConfig.elementAt(2)).toString().indexOf("_")),
                        ((Locale)xincoClientConfig.elementAt(2)).toString().substring(((Locale)xincoClientConfig.elementAt(2)).toString().indexOf("_")+1,
                        ((Locale)xincoClientConfig.elementAt(2)).toString().length()));
        } catch (Exception e) {
            loc = Locale.getDefault();
        }
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
        xerb.getLocale();
        //get Xinco Explorer settings
        xesettings=ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
        progressBar=new XincoProgressBarThread(this);
        initialize();
        //Windows-Listener
        addWindowListener(new XincoWindowClosingAdapter(true));
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
     * This method initializes jPopupMenuRepository
     *
     * @return javax.swing.JPopupMenu
     */
    public JPopupMenu getJPopupMenuRepository() {
        if(jPopupMenuRepository==null)
            jPopupMenuRepository = new XincoPopUpMenuRepository(this);
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
     * @return javax.swing.JDialog
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
            this.addDialog(jDialogLocale);
        }
        //processing independent of creation
        int i = 0;
        ResourceBundle lrb = null;
        String[] locales;
        String text = "";
        int selection = -1;
        int alt_selection = 0;
        DefaultListModel dlm;
        //load locales
        dlm = (DefaultListModel)jListDialogLocale.getModel();
        dlm.removeAllElements();
        selection = -1;
        alt_selection = 0;
        lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
        locales = lrb.getString("AvailableLocales").split(",");
        for (i=0;i<locales.length;i++) {
            try {
                text = locales[i];
                if (text.compareTo("") != 0) {
                    text = " (" + text + ")";
                }
                text = lrb.getString("Locale." + locales[i]) + text;
            } catch (Exception le) {
            }
            dlm.addElement(text);
            if ((locales[i].compareTo(((Locale)xincoClientConfig.elementAt(2)).toString()) == 0) || (locales[i].compareTo(((Locale)xincoClientConfig.elementAt(2)).getLanguage()) == 0)) {
                selection = i;
            }
            if (locales[i].compareTo("en") == 0) {
                alt_selection = i;
            }
        }
        if (selection == -1) {
            selection = alt_selection;
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
        //init session
        xincoClientSession = new XincoClientSession();
        //set client version
        xincoClientVersion = new XincoVersion();
        xincoClientVersion.setVersion_high(Integer.parseInt(xesettings.getString("version.high")));
        xincoClientVersion.setVersion_mid(Integer.parseInt(xesettings.getString("version.mid")));
        xincoClientVersion.setVersion_low(Integer.parseInt(xesettings.getString("version.low")));
        xincoClientVersion.setVersion_postfix(xesettings.getString("version.postfix"));
        //init XincoActionHandler
        getActionHandler();
        switchPLAF((String)xincoClientConfig.elementAt(1));
        this.setBounds(0, 0, (new Double(getToolkit().getScreenSize().getWidth())).intValue()-100,
                (new Double(getToolkit().getScreenSize().getHeight())).intValue()-75);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setName("XincoExplorer");
        this.setTitle(xerb.getString("general.clienttitle") + " - " +
                xerb.getString("general.version") + " " +
                xincoClientVersion.getVersion_high() + "." +
                (xincoClientVersion.getVersion_mid() < 9 ? "0"+
                xincoClientVersion.getVersion_mid() : xincoClientVersion.getVersion_mid()) + "." +
                (xincoClientVersion.getVersion_low() < 9 ? "0"+
                xincoClientVersion.getVersion_low() : xincoClientVersion.getVersion_low()) + " " +
                xincoClientVersion.getVersion_postfix());
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
        if(jJMenuBar == null) {
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
        if(jMenuConnection == null) {
            jMenuConnection = new javax.swing.JMenu();
            jMenuConnection.add(getJMenuItemConnectionConnect());
            jMenuConnection.add(getJMenuItemConnectionDisconnect());
            jMenuConnection.add(getJMenuItemConnectionExit());
            jMenuConnection.setName("Connection");
            jMenuConnection.setText(xerb.getString("menu.connection"));
        }
        return jMenuConnection;
    }
    private JMenuItem getJMenuItemConnectionExit(){
        if(jMenuItemConnectionExit == null) {
            jMenuItemConnectionExit = new javax.swing.JMenuItem(getXincoIcon());
            jMenuItemConnectionExit.setName("Exit");
            jMenuItemConnectionExit.setText(xerb.getString("menu.connection.exit"));
            jMenuItemConnectionExit.setEnabled(true);
            jMenuItemConnectionExit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    xincoClientSession.status = 0;
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
        if(jMenuAbout == null) {
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
        if(jMenuRepository == null) {
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
        if(jMenuItemConnectionDisconnect == null) {
            jMenuItemConnectionDisconnect = new javax.swing.JMenuItem(getXincoIcon());
            jMenuItemConnectionDisconnect.setName("Disconnect");
            jMenuItemConnectionDisconnect.setText(xerb.getString("menu.disconnect"));
            jMenuItemConnectionDisconnect.setEnabled(false);
            jMenuItemConnectionDisconnect.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    xincoClientSession.status = 0;
                    markConnectionStatus();
                    getLoginT().resetStrings();
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
        if(jMenuItemAboutAboutXinco == null) {
            jMenuItemAboutAboutXinco =
                    new javax.swing.JMenuItem(getXincoIcon());
            jMenuItemAboutAboutXinco.setName("AboutXinco");
            jMenuItemAboutAboutXinco.setText(xerb.getString("menu.aboutxinco"));
            jMenuItemAboutAboutXinco.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
            jMenuItemAboutAboutXinco.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String message_string = "";
                    message_string = message_string + xerb.getString("window.aboutxinco.clienttitle") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.clientversion") + ": " +
                            xincoClientVersion.getVersion_high() + "." +(xincoClientVersion.getVersion_mid() < 9 ? "0"+
                            xincoClientVersion.getVersion_mid() : xincoClientVersion.getVersion_mid()) + "." +
                            (xincoClientVersion.getVersion_low() < 9 ? "0"+ xincoClientVersion.getVersion_low() : xincoClientVersion.getVersion_low())
                            +" "+ xincoClientVersion.getVersion_postfix() + "\n";
                    message_string = message_string + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.partof") + ":\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.softwaretitle") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.softwaresubtitle") + "\n";
                    message_string = message_string + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.vision1") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.vision2") + "\n";
                    message_string = message_string + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.moreinfo") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.xinco_org") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.bluecubs_com") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.bluecubs_org") + "\n";
                    message_string = message_string + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.thanks") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.apache") + "\n";
                    message_string = message_string + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.copyright") + "\n";
                    JOptionPane.showMessageDialog(XincoExplorer.this, message_string, xerb.getString("window.aboutxinco"), JOptionPane.INFORMATION_MESSAGE);
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
        if(jDesktopPane == null) {
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
        if(jContentPaneRepository == null) {
            jContentPaneRepository = new javax.swing.JPanel();
            jContentPaneRepository.addMouseListener(explorer);
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
        if(jInternalFrameRepository == null) {
            jInternalFrameRepository = new javax.swing.JInternalFrame();
            jInternalFrameRepository.setBounds(5, 5, this.getWidth()-100, this.getHeight()-150);
            jInternalFrameRepository.setContentPane(getJContentPaneRepository());
            jInternalFrameRepository.setVisible(false);
            jInternalFrameRepository.setResizable(true);
            jInternalFrameRepository.setIconifiable(true);
            jInternalFrameRepository.setMaximizable(true);
            jInternalFrameRepository.setName("Repository");
            jInternalFrameRepository.setTitle(xerb.getString("window.repository"));
            jInternalFrameRepository.setFrameIcon(getXincoIcon());
        }
        return jInternalFrameRepository;
    }
    /**
     * This method initializes jSplitPaneRepository
     *
     * @return javax.swing.JSplitPane
     */
    private javax.swing.JSplitPane getJSplitPaneRepository() {
        if(jSplitPaneRepository == null) {
            jSplitPaneRepository = new javax.swing.JSplitPane();
            jSplitPaneRepository.setLeftComponent(getJScrollPaneRepositoryTree());
            jSplitPaneRepository.setDividerLocation(2.0/3.0);
            jSplitPaneRepository.setResizeWeight(1);
            jSplitPaneRepository.setContinuousLayout(true);
            JTabbedPane tabbedPane = new JTabbedPane();
            xincoAuditPanel=new XincoAuditPanel(this.explorer);
            ImageIcon icon = createImageIcon("blueCubsIcon16x16.GIF");
            tabbedPane.addTab(xerb.getString("window.audit.tablepanelabel"), icon,
                    getJScrollPaneRepositoryTable());
            tabbedPane.addTab(xerb.getString("window.audit.auditpanelabel"), icon,
                    getXincoAuditPanel().getPane());
            jSplitPaneRepository.setRightComponent(tabbedPane);
        }
        return jSplitPaneRepository;
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = XincoExplorer.class.getResource(path);
        
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        System.err.println("Couldn\'t find file: " + path);
        return null;
    }
    /**
     * This method initializes jScrollPaneRepositoryTree
     *
     * @return javax.swing.JScrollPane
     */
    protected javax.swing.JScrollPane getJScrollPaneRepositoryTree() {
        if(jScrollPaneRepositoryTree == null) {
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
    protected javax.swing.JScrollPane getJScrollPaneRepositoryTable() {
        if(jScrollPaneRepositoryTable == null) {
            jScrollPaneRepositoryTable = new javax.swing.JScrollPane();
            jScrollPaneRepositoryTable.setViewportView(getJTableRepository());
            jScrollPaneRepositoryTable.setHorizontalScrollBarPolicy(jScrollPaneRepositoryTable.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return jScrollPaneRepositoryTable;
    }
    
    /**
     * This method initializes jTreeRepository
     *
     * @return javax.swing.JTree
     */
    public XincoJTree getJTreeRepository() {
        if(jTreeRepository == null) {
            jTreeRepository = new XincoJTree(this);
            jTreeRepository.setModel(xincoClientSession.xincoClientRepository.treemodel);
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
    
    public void collapseAllNodes() {
        getJTreeRepository().collapseAllNodes();
    }
    /**
     * This method initializes jTableRepository
     *
     * @return javax.swing.JTable
     */
    public javax.swing.JTable getJTableRepository() {
        if(jTableRepository == null) {
            String[] cn = {xerb.getString("window.repository.table.attribute"),
            xerb.getString("window.repository.table.details")};
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
            jTableRepository.addMouseListener(this.explorer);
        }
        return jTableRepository;
    }
    /**
     * This method initializes jMenuSearch
     *
     * @return javax.swing.JMenu
     */
    private javax.swing.JMenu getJMenuSearch() {
        if(jMenuSearch == null) {
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
        if(jMenuItemSearchRepository == null) {
            jMenuItemSearchRepository = new javax.swing.JMenuItem(getXincoIcon());
            jMenuItemSearchRepository.setText(xerb.getString("menu.search.search_repository"));
            jMenuItemSearchRepository.setName("SearchRepository");
            jMenuItemSearchRepository.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.KeyEvent.CTRL_MASK));
            jMenuItemSearchRepository.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if(search==null)
                        search = new SearchDialog(null,true,explorer);
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
        if(jMenuView == null) {
            jMenuView = new javax.swing.JMenu();
            bgwindowstyle = new ButtonGroup();
            jMenuView.add(getJRadioButtonMenuItemViewStyleWindows());
            jMenuView.add(getJRadioButtonMenuItemViewStyleJava());
            jMenuView.add(getJRadioButtonMenuItemViewStyleMotif());
            jMenuView.add(getJRadioButtonMenuItemViewStyleNapkin());
            jMenuView.add(getJRadioButtonMenuItemViewStyleSubstance());
            bgwindowstyle.add(jMenuView.getItem(0));
            bgwindowstyle.add(jMenuView.getItem(1));
            bgwindowstyle.add(jMenuView.getItem(2));
            bgwindowstyle.add(jMenuView.getItem(3));
            bgwindowstyle.add(jMenuView.getItem(4));
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
        if(jRadioButtonMenuItemViewStyleWindows == null) {
            jRadioButtonMenuItemViewStyleWindows = new javax.swing.JRadioButtonMenuItem();
            if (((String)xincoClientConfig.elementAt(1)).equals(new String("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleWindows.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleWindows.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleWindows.setText(xerb.getString("menu.view.windows"));
            jRadioButtonMenuItemViewStyleWindows.addItemListener(new java.awt.event.ItemListener() {
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
     * This method initializes jRadioButtonMenuItemViewStyleNapkin
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleNapkin() {
        if(jRadioButtonMenuItemViewStyleNapkin == null) {
            jRadioButtonMenuItemViewStyleNapkin = new javax.swing.JRadioButtonMenuItem();
            if (((String)xincoClientConfig.elementAt(1)).equals(new String("net.sourceforge.napkinlaf.NapkinLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleNapkin.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleNapkin.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleNapkin.setText(xerb.getString("menu.view.napkin"));
            jRadioButtonMenuItemViewStyleNapkin.addItemListener(new java.awt.event.ItemListener() {
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
        if(jRadioButtonMenuItemViewStyleSubstance == null) {
            jRadioButtonMenuItemViewStyleSubstance = new javax.swing.JRadioButtonMenuItem();
            if (((String)xincoClientConfig.elementAt(1)).equals(new String("org.jvnet.substance.SubstanceLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleSubstance.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleSubstance.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleSubstance.setText(xerb.getString("menu.view.substance"));
            jRadioButtonMenuItemViewStyleSubstance.addItemListener(new java.awt.event.ItemListener() {
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
        if(jRadioButtonMenuItemViewStyleJava == null) {
            jRadioButtonMenuItemViewStyleJava = new javax.swing.JRadioButtonMenuItem();
            if (((String)xincoClientConfig.elementAt(1)).equals(new String("javax.swing.plaf.metal.MetalLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleJava.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleJava.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleJava.setText(xerb.getString("menu.view.java"));
            jRadioButtonMenuItemViewStyleJava.addItemListener(new java.awt.event.ItemListener() {
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
     * This method initializes jRadioButtonMenuItemViewStyleMotif
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleMotif() {
        if(jRadioButtonMenuItemViewStyleMotif == null) {
            jRadioButtonMenuItemViewStyleMotif = new javax.swing.JRadioButtonMenuItem();
            if (((String)xincoClientConfig.elementAt(1)).equals(new String("com.sun.java.swing.plaf.motif.MotifLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleMotif.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleMotif.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleMotif.setText(xerb.getString("menu.view.motif"));
            jRadioButtonMenuItemViewStyleMotif.addItemListener(new java.awt.event.ItemListener() {
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
        status_string_1="";
        status_string_2="";
        if(jMenuItemConnectionConnect == null) {
            jMenuItemConnectionConnect = new javax.swing.JMenuItem(getXincoIcon());
            jMenuItemConnectionConnect.setText(xerb.getString("menu.connection.connect"));
            jMenuItemConnectionConnect.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    connect(true);
                }});
        }
        return jMenuItemConnectionConnect;
    }
    
    public XincoCoreUser getUser(){
        return this.newuser;
    }
    
    private class loginThread extends Thread {
        @Override
        public void run() {
            try {
                getProgressBar().run();
                String status_string = "";
                temp=xincoClientSession.xinco.getCurrentXincoCoreUser(xincoClientSession.user.getUsername(), xincoClientSession.user.getUserpassword());
                status_string += xerb.getString("menu.connection.connectedto") + ": " + xincoClientSession.service_endpoint + "\n";
                status_string += xerb.getString("general.serverversion") + ": ";
                status_string += xincoClientSession.server_version.getVersion_high() + "." ;
                status_string += (xincoClientSession.server_version.getVersion_mid() < 9 ? "0"+
                        xincoClientSession.server_version.getVersion_mid() : xincoClientSession.server_version.getVersion_mid())+ "." ;
                status_string += (xincoClientSession.server_version.getVersion_low() < 9 ? "0"+
                        xincoClientSession.server_version.getVersion_low() : xincoClientSession.server_version.getVersion_low());
                status_string += " " +xincoClientSession.server_version.getVersion_postfix() + "\n";
                status_string += "\n";
                status_string += xerb.getString("general.user") + ": " + xincoClientSession.user.getFirstname() + " " + xincoClientSession.user.getName() + " <" + xincoClientSession.user.getEmail() + ">\n";
                status_string += xerb.getString("general.memberof") + ":\n";
                status_string += status_string_1 + "\n";
                status_string += xerb.getString("general.groupsonserver") + ": " + xincoClientSession.server_groups.size() + "\n";
                status_string += xerb.getString("general.languagesonserver") + ": " + xincoClientSession.server_languages.size() + "\n";
                status_string += xerb.getString("general.datatypesonserver") + ": " + xincoClientSession.server_datatypes.size() + "\n";
                status_string += status_string_2 + "\n";
                xincoClientSession.currentSearchResult = new Vector();
                xincoClientSession.status = 2;
                JOptionPane.showMessageDialog(XincoExplorer.this, status_string, xerb.getString("menu.connection.established"), JOptionPane.INFORMATION_MESSAGE);
                jLabelInternalFrameInformationText.setText(xerb.getString("menu.connection.established"));
                //get root
                XincoCoreNode xnode = new XincoCoreNode();
                xnode.setId(1);
                xnode = xincoClientSession.xinco.getXincoCoreNode(xnode, xincoClientSession.user);
                xincoClientSession.xincoClientRepository.assignObject2TreeNode((XincoMutableTreeNode) (xincoClientSession.xincoClientRepository.treemodel).getRoot(),
                        xnode,
                        xincoClientSession.xinco,
                        xincoClientSession.user,
                        2);
                getJTreeRepository().expandPath(new TreePath(xincoClientSession.xincoClientRepository.treemodel.getPathToRoot((XincoMutableTreeNode) (xincoClientSession.xincoClientRepository.treemodel).getRoot())));
                markConnectionStatus();
                if(temp.getStatus_number()==3){
                    jLabelInternalFrameInformationText.setText(xerb.getString("password.aged"));
                    getJDialogUser(true);
                }
                getProgressBar().hide();
                resetStrings();
            } catch (Exception cone) {
                xincoClientSession.status = 0;
                cone.printStackTrace();
                markConnectionStatus();
                JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("menu.connection.failed") + " " + xerb.getString("general.reason") + ": " + cone.toString(), xerb.getString("menu.connection.failed"), JOptionPane.WARNING_MESSAGE);
                getProgressBar().hide();
                resetStrings();
                return;
            }
        }
        public void resetStrings(){
            status_string_1="";
            status_string_2="";
        }
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
                xnode = getSession().xinco.getXincoCoreNode(xnode,
                        getSession().user);
                getSession().xincoClientRepository.assignObject2TreeNode((XincoMutableTreeNode) (explorer.getSession().xincoClientRepository.treemodel).getRoot(),
                        xnode,
                        explorer.getSession().xinco,
                        explorer.getSession().user,
                        2);
                jTreeRepository.expandPath(new TreePath(getSession().xincoClientRepository.treemodel.getPathToRoot((XincoMutableTreeNode) (getSession().xincoClientRepository.treemodel).getRoot())));
                collapseAllNodes();
                getProgressBar().hide();
                getJTreeRepository().setEnabled(true);
            } catch (Exception rmie) {
                rmie.printStackTrace();
                getProgressBar().hide();
            }
        }
    }
    
    public void refreshJTree(){
        rThread=null;
        rThread= new refreshThread();
        rThread.start();
    }
    /**
     * This method initializes jDialogConnection
     *
     * @return javax.swing.JDialog
     */
    private ConnectionDialog getJDialogConnection() {
        if(this.dialogConnection == null) {
            dialogConnection=new ConnectionDialog(new javax.swing.JFrame(),
                    true,this);
            this.addDialog(dialogConnection);
        }
        return dialogConnection;
    }
    
    public ResourceBundle getResourceBundle(){
        return this.xerb;
    }
    
    public Vector getConfig(){
        return xincoClientConfig;
    }
    /**
     * This method marks menues, etc. according to connection status
     *
     * @return void
     */
    public void markConnectionStatus() {
        int i=0, j=0;
        if(xincoClientSession != null) {
            //do general processing
            DefaultTableModel dtm;
            dtm = (DefaultTableModel)getJTableRepository().getModel();
            j = dtm.getRowCount();
            for (i=0;i<j;i++) {
                dtm.removeRow(0);
            }
            if(search!=null)
                this.search.clearResults();
            //reset selection
            xincoClientSession.currentTreeNodeSelection = null;
            xincoClientSession.clipboardTreeNodeSelection = new Vector();
            xincoClientSession.currentSearchResult = new Vector();
            //reset menus
            getJPopupMenuRepository();
            ((XincoPopUpMenuRepository) jPopupMenuRepository).resetItems();
            ((XincoMenuRepository) jMenuRepository).resetItems();
            //status = disconnected
            if (xincoClientSession.status == 0) {
                // set menus
                jMenuRepository.setEnabled(false);
                jMenuSearch.setEnabled(false);
                jMenuItemSearchRepository.setEnabled(false);
                jMenuPreferences.setEnabled(false);
                jMenuItemConnectionConnect.setEnabled(true);
                jMenuItemConnectionDisconnect.setEnabled(false);
                // set frames
                jInternalFrameRepository.setVisible(false);
                if(search!=null)
                    search.setVisible(false);
                jInternalFrameInformation.setVisible(false);
                //init session
                xincoClientSession = new XincoClientSession();
                getJTreeRepository().setModel(xincoClientSession.xincoClientRepository.treemodel);
            }
            //status = connected
            if (xincoClientSession.status == 2) {
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
    protected JInternalFrame getJInternalFrameInformation() {
        if (jInternalFrameInformation == null) {
            jInternalFrameInformation = new JInternalFrame();
            jInternalFrameInformation.setContentPane(getJContentPaneInformation());
            jInternalFrameInformation.setTitle(xerb.getString("window.information"));
            jInternalFrameInformation.setBounds(this.getWidth()-450, this.getHeight()-220, 400, 150);
            jInternalFrameInformation.setFrameIcon(getXincoIcon());
        }
        return jInternalFrameInformation;
    }
    
    /**
     * This method switches the plugable look and feel
     *
     * @return void
     */
    private void switchPLAF(String plaf_string) {
        try {
            //set LAF
            UIManager.setLookAndFeel(plaf_string);
            //update EACH window
            SwingUtilities.updateComponentTreeUI(XincoExplorer.this);
            //Log/Folder/Data/ACL/DataType/AddAttributesUniversal/Connection/User/TransactionInfo
            if (jDialogLog != null) {
                SwingUtilities.updateComponentTreeUI(jDialogLog);
            }
            if (jDialogFolder != null) {
                SwingUtilities.updateComponentTreeUI(jDialogFolder);
            }
            if (jDialogData != null) {
                SwingUtilities.updateComponentTreeUI(jDialogData);
            }
            if (jDialogArchive != null) {
                SwingUtilities.updateComponentTreeUI(jDialogArchive);
            }
            if (jDialogACL != null) {
                SwingUtilities.updateComponentTreeUI(jDialogACL);
            }
            if (jDialogDataType != null) {
                SwingUtilities.updateComponentTreeUI(jDialogDataType);
            }
            if (jDialogRevision != null) {
                SwingUtilities.updateComponentTreeUI(jDialogRevision);
            }
            if (jDialogAddAttributesUniversal != null) {
                SwingUtilities.updateComponentTreeUI(jDialogAddAttributesUniversal);
            }
            if (jDialogConnection != null) {
                SwingUtilities.updateComponentTreeUI(jDialogConnection);
            }
            if (jDialogUser != null) {
                SwingUtilities.updateComponentTreeUI(jDialogUser);
            }
            if (jDialogTransactionInfo != null) {
                SwingUtilities.updateComponentTreeUI(jDialogTransactionInfo);
            }
            if(jTreeRepository != null){
                SwingUtilities.updateComponentTreeUI(jTreeRepository);
            }
        } catch (Exception plafe) {
            //System.err.println(plafe.toString());
        }
    }
    /**
     * This method initializes jDialogFolder
     *
     * @return javax.swing.JDialog
     */
    public javax.swing.JDialog getJDialogFolder() {
        if(jDialogFolder==null){
            jDialogFolder = new DataFolderDialog(null, true, this);
            this.addDialog(jDialogFolder);
        }
        return jDialogFolder;
    }
    /**
     * This method initializes jDialogACL
     *
     * @return javax.swing.JDialog
     */
    public javax.swing.JDialog getJDialogACL() {
        if(jDialogACL==null){
            jDialogACL = new ACLDialog(new javax.swing.JFrame(),true,this);
            this.addDialog(jDialogACL);
        }
        return jDialogACL;
    }
    /**
     * This method initializes jContentPaneDialogRevision
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneDialogRevision() {
        if(jContentPaneDialogRevision == null) {
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
     * @return javax.swing.JDialog
     */
    private javax.swing.JDialog getJDialogRevision() {
        if(jDialogRevision == null) {
            jDialogRevision = new javax.swing.JDialog();
            jDialogRevision.setContentPane(getJContentPaneDialogRevision());
            jDialogRevision.setBounds(200, 200, 400, 220);
            jDialogRevision.setTitle(xerb.getString("window.revision"));
            jDialogRevision.setModal(true);
            jDialogRevision.setResizable(false);
            jDialogRevision.getRootPane().setDefaultButton(getJButtonDialogRevisionContinue());
            this.addDialog(jDialogRevision);
        }
        //processing independent of creation
        int i = 0;
        String text = "";
        if (xincoClientSession.currentTreeNodeSelection.getUserObject() != null) {
            DefaultListModel dlm = (DefaultListModel)jListDialogRevision.getModel();
            dlm.removeAllElements();
            Calendar cal;
            Calendar realcal;
            Calendar ngc = new GregorianCalendar();
            for (i=0;i<((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().size();i++) {
                if ((((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_code() == 1) || (((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_code() == 5)) {
                    //convert clone from remote time to local time
                    cal = (Calendar)((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_datetime().clone();
                    realcal = ((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_datetime();
                    cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - realcal.get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + realcal.get(Calendar.DST_OFFSET)) );
                    text = "" + cal.get(Calendar.YEAR) + "/"  + (cal.get(Calendar.MONTH) + 1) + "/"  + cal.get(Calendar.DAY_OF_MONTH);
                    text = text + " - " + xerb.getString("general.version") + " " + ((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_high() + "."
                            + ((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_mid() + "."
                            + ((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_low() + ""
                            + ((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_postfix();
                    text = text + " - " + ((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_description();
                    
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
        if(jLabelDialogRevision == null) {
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
        if(jScrollPaneDialogRevision == null) {
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
        if(jListDialogRevision == null) {
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
        if(jButtonDialogRevisionContinue == null) {
            jButtonDialogRevisionContinue = new javax.swing.JButton();
            jButtonDialogRevisionContinue.setBounds(120, 130, 100, 30);
            jButtonDialogRevisionContinue.setText(xerb.getString("general.continue"));
            jButtonDialogRevisionContinue.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int i = 0;
                    int RealLogIndex = -1;
                    if (jListDialogRevision.getSelectedIndex() >= 0) {
                        for (i=0;i<((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().size();i++) {
                            if ((((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_code() == 1) || (((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_code() == 5)) {
                                RealLogIndex++;
                            }
                            if (RealLogIndex == jListDialogRevision.getSelectedIndex()) {
                                global_dialog_return_value = ((XincoCoreLog)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(i)).getId();
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
        if(jButtonDialogRevisionCancel == null) {
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
     * This method initializes jDialogData
     *
     * @return javax.swing.JDialog
     */
    private javax.swing.JDialog getJDialogData() {
        if(jDialogData==null){
            jDialogData = new DataDialog(null, true, this);
            this.addDialog(jDialogData);
        }
        return jDialogData;
    }
    /**
     * This method initializes jDialogLog
     *
     * @return javax.swing.JDialog
     */
    private javax.swing.JDialog getJDialogLog() {
        if(jDialogLog==null){
            jDialogLog = new LogDialog(null,true,this);
            this.addDialog(jDialogLog);
        }
        return jDialogLog;
    }
    
    /**
     * This method imports files + subfolders of a folder into node
     *
     * @return void
     */
    public void importContentOfFolder(XincoCoreNode node, File folder) throws Exception {
        int i=0;
        int j=0;
        File[] folder_list = null;
        folder_list = folder.listFiles();
        newnode = new XincoMutableTreeNode(new XincoCoreData());
        XincoCoreNode xnode;
        newlog = new XincoCoreLog();
        XincoCoreDataType xcdt1 = null;
        //find data type = 1
        for (j=0;j<xincoClientSession.server_datatypes.size();j++) {
            if (((XincoCoreDataType)xincoClientSession.server_datatypes.elementAt(j)).getId() == 1) {
                xcdt1 = (XincoCoreDataType)xincoClientSession.server_datatypes.elementAt(j);
                break;
            }
        }
        //find default language
        XincoCoreLanguage xcl1 = null;
        int selection = -1;
        int alt_selection = 0;
        for (j=0;j<xincoClientSession.server_languages.size();j++) {
            if (((XincoCoreLanguage)xincoClientSession.server_languages.elementAt(j)).getSign().toLowerCase().compareTo(Locale.getDefault().getLanguage().toLowerCase()) == 0) {
                selection = j;
                break;
            }
            if (((XincoCoreLanguage)xincoClientSession.server_languages.elementAt(j)).getId() == 1) {
                alt_selection = j;
            }
        }
        if (selection == -1) {
            selection = alt_selection;
        }
        xcl1 = (XincoCoreLanguage)xincoClientSession.server_languages.elementAt(selection);
        //process files
        getProgressBar().setTitle(xerb.getString("datawizard.fileuploadinfo"));
        getProgressBar().show();
        for (i = 0; i < folder_list.length; i++) {
            if (folder_list[i].isFile()) {
                System.out.println("Processing file: "+folder_list[i].getName()+"("+(i+1)+"/"+folder_list.length+")");
                // set current node to new one
                newnode = new XincoMutableTreeNode(new XincoCoreData());
                // set data attributes
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_node_id(node.getId());
                ((XincoCoreData) newnode.getUserObject()).setDesignation(folder_list[i].getName());
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_data_type(xcdt1);
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_language(xcl1);
                ((XincoCoreData) newnode.getUserObject()).setXinco_add_attributes(new Vector());
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_acl(new Vector());
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_logs(new Vector());
                ((XincoCoreData) newnode.getUserObject()).setStatus_number(1);
                xincoClientSession.xincoClientRepository.treemodel.insertNodeInto(newnode,
                        xincoClientSession.currentTreeNodeSelection,
                        xincoClientSession.currentTreeNodeSelection.getChildCount());
                xincoClientSession.currentTreeNodeSelection = newnode;
                // add specific attributes
                XincoAddAttribute xaa;
                
                for (j = 0; j <
                        ((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().size(); j++) {
                    xaa = new XincoAddAttribute();
                    // xaa.setAttribute_id(j+1); // bug => attribute_ids might be missing in between
                    xaa.setAttribute_id(((XincoCoreDataTypeAttribute) xcdt1.getXinco_core_data_type_attributes().elementAt(j)).getAttribute_id());
                    xaa.setAttrib_varchar("");
                    xaa.setAttrib_text("");
                    xaa.setAttrib_datetime(new GregorianCalendar());
                    ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().addElement(xaa);
                }
                // add log
                newlog = new XincoCoreLog();
                newlog.setOp_code(1);
                newlog.setOp_description(xerb.getString("datawizard.logging.creation") +
                        "!" + " (" +
                        xerb.getString("general.user") + ": " +
                        xincoClientSession.user.getUsername() +
                        ")");
                newlog.setXinco_core_user_id(xincoClientSession.user.getId());
                newlog.setXinco_core_data_id(((XincoCoreData) newnode.getUserObject()).getId());
                newlog.setVersion(new XincoVersion());
                newlog.getVersion().setVersion_high(1);
                newlog.getVersion().setVersion_mid(0);
                newlog.getVersion().setVersion_low(0);
                newlog.getVersion().setVersion_postfix("");
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_logs(new Vector());
                ((XincoCoreData) newnode.getUserObject()).getXinco_core_logs().addElement(newlog);
                // invoke web service (update data / upload file / add log)
                // load file
                long total_len = 0;
                boolean useSAAJ = false;
                
                if (((xincoClientSession.server_version.getVersion_high() == 1) &&
                        (xincoClientSession.server_version.getVersion_mid() >= 9)) ||
                        (xincoClientSession.server_version.getVersion_high() > 1)) {
                    useSAAJ = true;
                } else {
                    useSAAJ = false;
                }
                CheckedInputStream in = null;
                ByteArrayOutputStream out = null;
                
                byte_array = null;
                try {
                    in = new CheckedInputStream(new FileInputStream(folder_list[i]),
                            new CRC32());
                    if (useSAAJ) {
                        total_len = folder_list[i].length();
                    } else {
                        out = new ByteArrayOutputStream();
                        byte[] buf = new byte[4096];
                        int len = 0;
                        
                        total_len = 0;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                            total_len = total_len + len;
                        }
                        byte_array = out.toByteArray();
                        out.close();
                    }
                    // update attributes
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).setAttrib_varchar(folder_list[i].getName());
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(1)).setAttrib_unsignedint(total_len);
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(2)).setAttrib_varchar("" +
                            in.getChecksum().getValue());
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(3)).setAttrib_unsignedint(1);
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(4)).setAttrib_unsignedint(0);
                    if (!useSAAJ) {
                        in.close();
                    }
                } catch (Exception fe) {
                    throw new XincoException(xerb.getString("datawizard.unabletoloadfile"));
                }
                // save data to server
                setXdata(xincoClientSession.xinco.setXincoCoreData((XincoCoreData) newnode.getUserObject(),
                        xincoClientSession.user));
                if (getXdata() == null) {
                    throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
                }
                newnode.setUserObject(getXdata());
                // update id in log
                newlog.setXinco_core_data_id(((XincoCoreData) newnode.getUserObject()).getId());
                // save log to server
                newlog = xincoClientSession.xinco.setXincoCoreLog(newlog,
                        xincoClientSession.user);
                if (newlog == null) {
                }
                // attach file to SOAP message
                if (useSAAJ) {
                    AttachmentPart ap = null;
                    ap = new AttachmentPart();
                    ap.setContent(in, "unknown/unknown");
                    ((XincoSoapBindingStub) xincoClientSession.xinco).addAttachment(ap);
                }
                if(this.getFilesToBeIndexed()==null)
                    this.setFilesToBeIndexed(new Vector());
                this.getFilesToBeIndexed().add((XincoCoreData) newnode.getUserObject());
                // upload file
                if (xincoClientSession.xinco.uploadXincoCoreData((XincoCoreData) newnode.getUserObject(),
                        byte_array,
                        xincoClientSession.user) !=total_len) {
                    ((XincoSoapBindingStub) xincoClientSession.xinco).clearAttachments();
                    in.close();
                    throw new XincoException(xerb.getString("datawizard.fileuploadfailed"));
                }
                ((XincoSoapBindingStub) xincoClientSession.xinco).clearAttachments();
                in.close();
                // update treemodel
                xincoClientSession.xincoClientRepository.treemodel.reload(newnode);
                xincoClientSession.xincoClientRepository.treemodel.nodeChanged(newnode);
                // select parent of new node
                xincoClientSession.currentTreeNodeSelection = (XincoMutableTreeNode) newnode.getParent();
            }
        }
        //process directories
        for (i = 0; i < folder_list.length; i++) {
            if (folder_list[i].isDirectory()) {
                // set current node to new one
                newnode = new XincoMutableTreeNode(new XincoCoreNode());
                // set node attributes
                ((XincoCoreNode) newnode.getUserObject()).setXinco_core_node_id(node.getId());
                ((XincoCoreNode) newnode.getUserObject()).setDesignation(folder_list[i].getName());
                ((XincoCoreNode) newnode.getUserObject()).setXinco_core_language(xcl1);
                ((XincoCoreNode) newnode.getUserObject()).setStatus_number(1);
                System.out.println("Node Info:");
                System.out.println(((XincoCoreNode) newnode.getUserObject()).getXinco_core_node_id());
                System.out.println(((XincoCoreNode) newnode.getUserObject()).getDesignation());
                System.out.println(((XincoCoreNode) newnode.getUserObject()).getXinco_core_language().getChangerID());
                System.out.println(((XincoCoreNode) newnode.getUserObject()).getStatus_number());
                System.out.println("------------------------");
                System.out.println("Node Selected: "+xincoClientSession.currentTreeNodeSelection);
                //Recovery code
                if(xincoClientSession.currentTreeNodeSelection==null){
                    xincoClientSession.currentTreeNodeSelection=previousnode;
                }
                //-------
                System.out.println("Child Count: "+xincoClientSession.currentTreeNodeSelection.getChildCount());
                System.out.println("New node: "+newnode);
                xincoClientSession.xincoClientRepository.treemodel.insertNodeInto(newnode,
                        xincoClientSession.currentTreeNodeSelection,
                        xincoClientSession.currentTreeNodeSelection.getChildCount());
                previousnode=xincoClientSession.currentTreeNodeSelection;
                xincoClientSession.currentTreeNodeSelection = newnode;
                // save node to server
                xnode = xincoClientSession.xinco.setXincoCoreNode((XincoCoreNode) newnode.getUserObject(),
                        xincoClientSession.user);
                if (xnode == null) {
                    throw new XincoException(xerb.getString("window.folder.updatefailed"));
                }
                newnode.setUserObject(xnode);
                // update treemodel
                xincoClientSession.xincoClientRepository.treemodel.reload(newnode);
                xincoClientSession.xincoClientRepository.treemodel.nodeChanged(newnode);
                // start recursion
                importContentOfFolder((XincoCoreNode) newnode.getUserObject(),
                        folder_list[i]);
                // select parent of new node
                xincoClientSession.currentTreeNodeSelection = (XincoMutableTreeNode) newnode.getParent();
            }
        }
        getProgressBar().hide();
    }
    /**
     * This method leads through data adding/editing
     *
     * @return void
     */
    public void doDataWizard(final int wizard_type) {
        this.wizard_type=wizard_type;
                /*
                     wizard type	= 1  = add new data
                                        = 2  = edit data object
                                        = 3  = edit add attributes
                                        = 4  = checkout data
                                        = 5  = undo checkout
                                        = 6  = checkin data
                                        = 7  = download data
                                        = 8  = open URL in browser
                                        = 9  = open email client with contact information
                                        = 10 = publish data
                                        = 11 = download previous revision
                                        = 12 = lock data
                                        = 13 = comment data
                                        = 14 = preview data
                                        = 15 = Add/Edit Audit Data
                 */
        int i=0;
        XincoMutableTreeNode newnode = new XincoMutableTreeNode(new XincoCoreData());
        XincoCoreData xdata = null;
        XincoCoreLog newlog = new XincoCoreLog();
        
        InputStream in = null;
        byte[] byte_array = null;
        if (xincoClientSession.currentTreeNodeSelection != null) {
            //execute wizard as a whole
            try {
                //add new data
                if ((wizard_type == 1) &&
                        (xincoClientSession.currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class)) {
                    
                    //set current node to new one
                    newnode = new XincoMutableTreeNode(new XincoCoreData());
                    //set data attributes
                    ((XincoCoreData)newnode.getUserObject()).setXinco_core_node_id(((XincoCoreNode)xincoClientSession.currentTreeNodeSelection.getUserObject()).getId());
                    ((XincoCoreData)newnode.getUserObject()).setDesignation(xerb.getString("datawizard.newdata"));
                    ((XincoCoreData)newnode.getUserObject()).setXinco_core_data_type((XincoCoreDataType)xincoClientSession.server_datatypes.elementAt(0));
                    ((XincoCoreData)newnode.getUserObject()).setXinco_core_language((XincoCoreLanguage)xincoClientSession.server_languages.elementAt(0));
                    ((XincoCoreData)newnode.getUserObject()).setXinco_add_attributes(new Vector());
                    ((XincoCoreData)newnode.getUserObject()).setXinco_core_acl(new Vector());
                    ((XincoCoreData)newnode.getUserObject()).setXinco_core_logs(new Vector());
                    ((XincoCoreData)newnode.getUserObject()).setStatus_number(1);
                    xincoClientSession.xincoClientRepository.treemodel.insertNodeInto(newnode, xincoClientSession.currentTreeNodeSelection, xincoClientSession.currentTreeNodeSelection.getChildCount());
                    xincoClientSession.currentTreeNodeSelection = newnode;
                    
                    //step 1: select data type
                    jDialogDataType= new DataTypeDialog(null,true,this);
                    global_dialog_return_value = 0;
                    jDialogDataType.setVisible(true);
                    if (global_dialog_return_value == 0) {
                        this.getProgressBar().hide();
                        throw new XincoException(xerb.getString("datawizard.updatecancel"));
                    }
                    
                    //add specific attributes
                    XincoAddAttribute xaa;
                    for (i=0;i<((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().size();i++) {
                        xaa = new XincoAddAttribute();
                        xaa.setAttribute_id(((XincoCoreDataTypeAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getAttribute_id());
                        xaa.setAttrib_varchar("");
                        xaa.setAttrib_text("");
                        xaa.setAttrib_datetime(new GregorianCalendar());
                        ((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().addElement(xaa);
                    }
                    
                    //initialize specific attributes:
                    //files
                    if (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1) {
                        ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(3)).setAttrib_unsignedint(1); //revision model
                        ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(4)).setAttrib_unsignedint(0); //archiving model
                    }
                }
                if (xincoClientSession.currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                    newnode = xincoClientSession.currentTreeNodeSelection;
                    //Add/Edit audit data
                    if(wizard_type == 15){
                        this.set_global_dialog_return_value(0);
                        getJDialogAudit().setVisible(true);
                        //Make sure that the XincoCoreAudit object is set to null when cancelled
                        switch(this.get_global_dialog_return_value()){
                            case 0: getJDialogAudit().setXca(null);
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            case 2: getJDialogAudit().setXca(null);
                            throw new XincoException(xerb.getString("datawizard.auditcreationerror"));
                        }
                    }
                    //check file attribute count
                    //file = 1
                    if ((wizard_type == 3) &&
                            ((((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1) &&
                            (((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().size() <= 3))) {
                        this.getProgressBar().hide();
                        throw new XincoException(xerb.getString("datawizard.noaddattributes"));
                    }
                    //edit add attributes
                    if ((wizard_type == 1) || (wizard_type == 3)) {
                        //step 2: edit add attributes
                        //for files -> show filechooser
                        //file = 1
                        if ((wizard_type == 1) &&
                                (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1)) {
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
                        if (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 2) {
                            jDialogAddAttributesText = getJDialogAddAttributesText();
                            global_dialog_return_value = 0;
                            jDialogAddAttributesText.setVisible(true);
                            if (global_dialog_return_value == 0) {
                                this.getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }
                        //show dialog for all additional attributes and custom data types
                        //file = 1 / text = 2
                        if ((((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() !=1 ||
                                ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().size() >8) &&
                                (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() !=2 ||
                                ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().size() >1)) {
                            //for other data type -> show universal add attribute dialog
                            jDialogAddAttributesUniversal=new AddAttributeUniversalDialog(null,true,this);
                            global_dialog_return_value = 0;
                            jDialogAddAttributesUniversal.setVisible(true);
                            if (global_dialog_return_value == 0) {
                                this.getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }
                    }
                    //edit logging
                    //step 3: edit logging (creation!)
                    if (wizard_type == 1) {
                        newlog = new XincoCoreLog();
                        newlog.setOp_code(1);
                        newlog.setOp_description(xerb.getString("datawizard.logging.creation") + "!");
                        newlog.setXinco_core_user_id(xincoClientSession.user.getId());
                        newlog.setXinco_core_data_id(((XincoCoreData)newnode.getUserObject()).getId()); //update to new id later!
                        newlog.setVersion(new XincoVersion());
                        newlog.getVersion().setVersion_high(1);
                        newlog.getVersion().setVersion_mid(0);
                        newlog.getVersion().setVersion_low(0);
                        newlog.getVersion().setVersion_postfix("");
                        ((XincoCoreData)newnode.getUserObject()).setXinco_core_logs(new Vector());
                        ((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().addElement(newlog);
                        jDialogLog = getJDialogLog();
                        global_dialog_return_value = 0;
                        jDialogLog.setVisible(true);
                        if (global_dialog_return_value == 0) {
                            this.getProgressBar().hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
                        newlog.setOp_description(newlog.getOp_description() + " (" + xerb.getString("general.user") + ": " + xincoClientSession.user.getUsername() + ")");
                    } else {
                        if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) && (wizard_type != 14)) {
                            newlog = new XincoCoreLog();
                            if (wizard_type <= 3) {
                                newlog.setOp_code(2);
                                newlog.setOp_description(xerb.getString("datawizard.logging.modification") + "!");
                            }
                            if (wizard_type == 4) {
                                newlog.setOp_code(3);
                                newlog.setOp_description(xerb.getString("datawizard.logging.checkoutchangesplanned"));
                            }
                            if (wizard_type == 5) {
                                newlog.setOp_code(4);
                                newlog.setOp_description(xerb.getString("datawizard.logging.checkoutundone"));
                            }
                            if (wizard_type == 6) {
                                newlog.setOp_code(5);
                                newlog.setOp_description(xerb.getString("datawizard.logging.checkinchangesmade"));
                            }
                            if (wizard_type == 10) {
                                newlog.setOp_code(6);
                                newlog.setOp_description(xerb.getString("datawizard.logging.publishcomment"));
                            }
                            if (wizard_type == 12) {
                                newlog.setOp_code(7);
                                newlog.setOp_description(xerb.getString("datawizard.logging.lockcomment"));
                            }
                            if (wizard_type == 13) {
                                newlog.setOp_code(9);
                                newlog.setOp_description(xerb.getString("datawizard.logging.commentcomment"));
                            }
                            if (wizard_type == 15) {
                                newlog.setOp_code(10);
                                newlog.setOp_description(xerb.getString("datawizard.logging.addauditdata"));
                                //update transaction info
                                jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.auditupdated"));
                            }
                            if (wizard_type == 16) {
                                newlog.setOp_code(11);
                                newlog.setOp_description(xerb.getString("datawizard.logging.editauditdata"));
                                //update transaction info
                                jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.auditupdated"));
                            }
                            newlog.setXinco_core_user_id(xincoClientSession.user.getId());
                            newlog.setXinco_core_data_id(((XincoCoreData)newnode.getUserObject()).getId());
                            newlog.setVersion(((XincoCoreLog)((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().elementAt(((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().size()-1)).getVersion());
                            ((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().addElement(newlog);
                            jDialogLog = getJDialogLog();
                            global_dialog_return_value = 0;
                            jDialogLog.setVisible(true);
                            if (global_dialog_return_value == 0) {
                                this.getProgressBar().hide();
                                //Make sure that the XincoCoreAudit object is set to null when cancelled
                                if(wizard_type == 15 && auditDialog!=null){
                                    getJDialogAudit().setXca(null);
                                }
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            newlog.setOp_description(newlog.getOp_description() +
                                    " (" + xerb.getString("general.user") + ": " +
                                    xincoClientSession.user.getUsername() + ")");
                            //Process the XincoAudit creation/edit after the log entry have been successfully made
                            if(wizard_type == 15){
                                getJDialogAudit().setXincoAudit();
                            }
                        }
                    }
                    
                    //choose filename for checkout/checkin/download/preview
                    if ((wizard_type == 4) || (wizard_type == 6) || (wizard_type == 7) || (wizard_type == 11)) {
                        JFileChooser fc = new JFileChooser();
                        
                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fc.setSelectedFile(new File(current_path +
                                ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_varchar()));
                        // show dialog
                        int result;
                        
                        if ((wizard_type == 4) || (wizard_type == 7) ||
                                (wizard_type == 11)) {
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
                    if (wizard_type == 14) {
                        setCurrentPathFilename(File.createTempFile("xinco_", "_" + ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_varchar()) .getPath());
                    }
                    
                    //edit data details
                    if ((wizard_type == 1) || (wizard_type == 2)) {
                        
                        //step 4: edit data details
                        jDialogData = getJDialogData();
                        global_dialog_return_value = 0;
                        jDialogData.setVisible(true);
                        if (global_dialog_return_value == 0) {
                            this.getProgressBar().hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
                        
                        //step 4b: edit archiving options of files
                        if (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1) {
                            jDialogArchive = getJDialogArchive();
                            global_dialog_return_value = 0;
                            jDialogArchive.setVisible(true);
                            if (global_dialog_return_value == 0) {
                                this.getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }
                    }
                    //set status = published
                    if (wizard_type == 10) {
                        ((XincoCoreData)newnode.getUserObject()).setStatus_number(5);
                    }
                    //set status = locked
                    if (wizard_type == 12) {
                        ((XincoCoreData)newnode.getUserObject()).setStatus_number(2);
                    }
                    
                    //invoke web service (update data / (upload file) / add log)
                    //load file (new / checkin)
                    long total_len = 0;
                    boolean useSAAJ = false;
                    if (((xincoClientSession.server_version.getVersion_high() == 1) && (xincoClientSession.server_version.getVersion_mid() >= 9)) || (xincoClientSession.server_version.getVersion_high() > 1)) {
                        useSAAJ = true;
                    } else {
                        useSAAJ = false;
                    }
                    ByteArrayOutputStream out = null;
                    //file = 1
                    if (((wizard_type == 1) || (wizard_type == 6)) && (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1)) {
                        try {
                            //update transaction info
                            getProgressBar().setTitle(xerb.getString("datawizard.fileuploadinfo"));
                            getProgressBar().show();
                            jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.fileuploadinfo"));
                            in = new CheckedInputStream(new FileInputStream(current_fullpath), new CRC32());
                            if (useSAAJ) {
                                total_len = (new File(current_fullpath)).length();
                            } else {
                                out = new ByteArrayOutputStream();
                                byte[] buf = new byte[4096];
                                int len = 0;
                                total_len = 0;
                                while ((len = in.read(buf)) > 0) {
                                    out.write(buf, 0, len);
                                    total_len = total_len + len;
                                }
                                byte_array = out.toByteArray();
                                out.close();
                            }
                            //update attributes
                            ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).setAttrib_varchar(current_filename);
                            ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(1)).setAttrib_unsignedint(total_len);
                            ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(2)).setAttrib_varchar("" + ((CheckedInputStream)in).getChecksum().getValue());
                            if (!useSAAJ) {
                                in.close();
                            }
                        } catch (Exception fe) {
                            getProgressBar().hide();
                            throw new XincoException(xerb.getString("datawizard.unabletoloadfile"));
                        }
                    }
                    //Add the created attributes to the holder array
                    xaah=new XincoAddAttributeHolder[((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().size()];
                    for(int j=0;j<((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().size();j++) {
                        xaah[j]= new XincoAddAttributeHolder((XincoAddAttribute)(((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(j)));
                    }
                    //save data to server
                    if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) &&
                            (wizard_type != 14) && (wizard_type != 15) && (wizard_type != 16)) {
                        if ((wizard_type >= 4) && (wizard_type <= 6)) {
                            if (wizard_type == 4) {
                                xdata = xincoClientSession.xinco.doXincoCoreDataCheckout((XincoCoreData) newnode.getUserObject(),
                                        xincoClientSession.user);
                            } else {
                                if (wizard_type == 5) {
                                    xdata = xincoClientSession.xinco.undoXincoCoreDataCheckout((XincoCoreData) newnode.getUserObject(),
                                            xincoClientSession.user);
                                } else {
                                    xdata = xincoClientSession.xinco.doXincoCoreDataCheckin((XincoCoreData) newnode.getUserObject(),
                                            xincoClientSession.user);
                                }
                            }
                        } else {
                            xdata = xincoClientSession.xinco.setXincoCoreData((XincoCoreData) newnode.getUserObject(),
                                    xincoClientSession.user);
                        }
                        if (xdata == null && wizard_type != 15) {
                            throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
                        }
                        newnode.setUserObject(xdata);
                        //edit data details
                        if ((wizard_type == 1) || (wizard_type == 2)) {
                            //step 4c: edit audit options of files
                            if (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1) {
                                auditDialog = getJDialogAudit();
                                global_dialog_return_value = 0;
                                auditDialog.setVisible(true);
                                if (global_dialog_return_value == 0) {
                                    this.getProgressBar().hide();
                                    throw new XincoException(xerb.getString("datawizard.updatecancel"));
                                }
                            }
                        }
                    }
                    if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) && (wizard_type != 14)) {
                        //update id in log
                        newlog.setXinco_core_data_id(((XincoCoreData)newnode.getUserObject()).getId());
                        //save log to server
                        newlog = xincoClientSession.xinco.setXincoCoreLog(newlog, xincoClientSession.user);
                        if (newlog == null) {
                        } else {
                            ((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().addElement(newlog);
                        }
                    }
                    //upload file (new / checkin)
                    //file = 1
                    if (((wizard_type == 1) || (wizard_type == 6)) && (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1)) {
                        //attach file to SOAP message
                        if (useSAAJ) {
                            AttachmentPart ap = null;
                            ap = new AttachmentPart();
                            ap.setContent(in, "unknown/unknown");
                            ((XincoSoapBindingStub)xincoClientSession.xinco).addAttachment(ap);
                        }
                        
                        if (xincoClientSession.xinco.uploadXincoCoreData((XincoCoreData)newnode.getUserObject(), byte_array, xincoClientSession.user) != total_len) {
                            ((XincoSoapBindingStub)xincoClientSession.xinco).clearAttachments();
                            in.close();
                            JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.fileuploadfailed"), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                        }
                        ((XincoSoapBindingStub)xincoClientSession.xinco).clearAttachments();
                        in.close();
                        //update transaction info
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.fileuploadsuccess"));
                        getProgressBar().hide();
                    }
                    //download file
                    //file = 1
                    if (((wizard_type == 4) || (wizard_type == 7) || (wizard_type == 11) ||
                            (wizard_type == 14)) &&
                            (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1)) {
                        //determine requested revision and set log vector
                        getProgressBar().setTitle(xerb.getString("datawizard.filedownloadinfo"));
                        getProgressBar().show();
                        Vector DataLogVector = null;
                        if (wizard_type == 11) {
                            jDialogRevision = getJDialogRevision();
                            global_dialog_return_value = -1;
                            jDialogRevision.setVisible(true);
                            if (global_dialog_return_value == -1) {
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            DataLogVector = ((XincoCoreData)newnode.getUserObject()).getXinco_core_logs();
                            XincoCoreLog RevLog = null;
                            for (i=0;i<((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().size();i++) {
                                if (((XincoCoreLog)((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().elementAt(i)).getId() == global_dialog_return_value) {
                                    RevLog = (XincoCoreLog)((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().elementAt(i);
                                    break;
                                }
                            }
                            Vector RevLogVector = new Vector();
                            RevLogVector.add(RevLog);
                            ((XincoCoreData)newnode.getUserObject()).setXinco_core_logs(RevLogVector);
                        }
                        
                        //update transaction info
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.filedownloadinfo"));
                        //call service
                        try {
                            Message m = null;
                            MessageContext mc = null;
                            AttachmentPart ap = null;
                            Call call = (Call)xincoClientSession.xinco_service.createCall();
                            call.setTargetEndpointAddress(new URL(xincoClientSession.service_endpoint));
                            call.setOperationName(new QName("urn:Xinco", "downloadXincoCoreData"));
                            Object[] objp = new Object[2];
                            objp[0] = (XincoCoreData)newnode.getUserObject();
                            objp[1] = xincoClientSession.user;
                            //tell server to send file as attachment
                            //(keep backward compatibility to earlier versions)
                            ap = new AttachmentPart();
                            ap.setContent(new String("SAAJ"), "text/string");
                            call.addAttachmentPart(ap);
                            //invoke actual call
                            byte_array = (byte[])call.invoke(objp);
                            //get file from SOAP message or byte array
                            mc = call.getMessageContext();
                            m = mc.getResponseMessage();
                            if (m.getAttachments().hasNext()) {
                                ap = (AttachmentPart)m.getAttachments().next();
                                in = (InputStream)ap.getContent();
                            } else {
                                in = new ByteArrayInputStream(byte_array);
                            }
                        } catch (Exception ce) {
                            //reassign log vector
                            if (wizard_type == 11) {
                                ((XincoCoreData)newnode.getUserObject()).setXinco_core_logs(DataLogVector);
                            }
                            JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.filedownloadfailed"), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                            this.getProgressBar().hide();
                            throw(ce);
                        }
                        
                        //reassign log vector
                        if (wizard_type == 11) {
                            ((XincoCoreData)newnode.getUserObject()).setXinco_core_logs(DataLogVector);
                        }
                        
                        //ByteArrayInputStream in = new ByteArrayInputStream(byte_array);
                        CheckedOutputStream couts = new CheckedOutputStream(new FileOutputStream(current_fullpath), new CRC32());
                        byte[] buf = new byte[4096];
                        int len = 0;
                        total_len = 0;
                        while ((len = in.read(buf)) > 0) {
                            couts.write(buf, 0, len);
                            total_len = total_len + len;
                        }
                        in.close();
                        ((XincoSoapBindingStub)xincoClientSession.xinco).clearAttachments();
                        //check correctness of data
                        if (wizard_type != 11) {
                            if (((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(1)).getAttrib_unsignedint() != total_len) {
                                JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.filedownloadcorrupted"), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        couts.close();
                        //make sure temp. file is deleted on exit
                        if (wizard_type == 14) {
                            (new File(current_fullpath)).deleteOnExit();
                        }
                        //update transaction info
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.filedownloadsuccess"));
                        //open file in default application
                        boolean open_file = false;
                        if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                            if (wizard_type == 14) {
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
                                } catch(Throwable t) {}
                            }
                        } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                            if (wizard_type == 14) {
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
                                } catch(Throwable t) {}
                            }
                        }
                        getProgressBar().hide();
                    }
                    //Open in Browser
                    //URL = 3
                    if ((wizard_type == 8) && (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 3)) {
                        //open URL in default browser
                        process = null;
                        String temp_url = ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_varchar();
                        if(System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                            try {
                                String[] cmd = {"open", temp_url};
                                process = Runtime.getRuntime().exec(cmd);
                            } catch(Throwable t) {}
                        } else if(System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                            try {
                                String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + temp_url + "\"";
                                process = Runtime.getRuntime().exec(cmd);
                            } catch(Throwable t) {}
                        }
                    }
                    //Open in Email Client
                    //contact = 4
                    if ((wizard_type == 9) && (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 4)) {
                        //open URL in default browser
                        process = null;
                        String temp_email = ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(9)).getAttrib_varchar();
                        if(System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                            try {
                                String[] cmd = {"open", "mailto:" + temp_email};
                                process = Runtime.getRuntime().exec(cmd);
                            } catch(Throwable t) {}
                        } else if(System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                            try {
                                String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + "mailto:" + temp_email + "\"";
                                process = Runtime.getRuntime().exec(cmd);
                            } catch(Throwable t) {}
                        }
                    }
                    
                    if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) && (wizard_type != 14)) {
                        //update treemodel
                        xincoClientSession.xincoClientRepository.treemodel.reload(newnode);
                        xincoClientSession.xincoClientRepository.treemodel.nodeChanged(newnode);
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.updatesuccess"));
                        if (wizard_type == 10) {
                            String temp_url = "";
                            //file = 1
                            if (xdata.getXinco_core_data_type().getId() == 1) {
                                temp_url = ((XincoAddAttribute)xdata.getXinco_add_attributes().elementAt(0)).getAttrib_varchar();
                            } else {
                                temp_url = xdata.getDesignation();
                            }
                            jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.updatesuccess.publisherinfo") + "\nhttp://[server_name]:[port]/xinco/XincoPublisher/" + xdata.getId() + "/" + temp_url);
                        }
                        jTreeRepository.setSelectionPath(new TreePath(((XincoMutableTreeNode)xincoClientSession.currentTreeNodeSelection.getParent()).getPath()));
                        jTreeRepository.setSelectionPath(new TreePath(xincoClientSession.currentTreeNodeSelection.getPath()));
                    }
                    
                }
            } catch (Exception we) {
                //update transaction info
                jLabelInternalFrameInformationText.setText("");
                //remove new data in case off error
                if (wizard_type == 1) {
                    xincoClientSession.currentTreeNodeSelection = (XincoMutableTreeNode)xincoClientSession.currentTreeNodeSelection.getParent();
                    xincoClientSession.xincoClientRepository.treemodel.removeNodeFromParent(newnode);
                    jTreeRepository.setSelectionPath(new TreePath(xincoClientSession.currentTreeNodeSelection.getPath()));
                }
                if (wizard_type != 3 || global_dialog_return_value != 0) {
                    JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.updatefailed") +
                            " " + xerb.getString("general.reason") + ": " +
                            we.toString(), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                }
                this.getProgressBar().hide();
                we.printStackTrace();
            }
        }
    }
    /**
     * This method initializes jMenuPreferences
     *
     * @return javax.swing.JMenu
     */
    private javax.swing.JMenu getJMenuPreferences() {
        if(jMenuPreferences == null) {
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
        if(jMenuItemPreferencesEditUser == null) {
            jMenuItemPreferencesEditUser = new javax.swing.JMenuItem(getXincoIcon());
            jMenuItemPreferencesEditUser.setText(xerb.getString("menu.preferences.edituserinfo"));
            jMenuItemPreferencesEditUser.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getJDialogUser(false);
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
        int i=0;
        if(s != null) {
            try {
                previous_fullpath = s;
                i = s.lastIndexOf(System.getProperty("file.separator"));
                                /*j = s.lastIndexOf("\\");
                                //select i as index wanted
                                if (j>i) {
                                        i = j;
                                }*/
                previous_filename = s.substring(i+1);
                if (i > 0) {
                    previous_path = s.substring(0, i+1);
                } else {
                    previous_path = "";
                }
            } catch (Exception e) {
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
     * @return void
     */
    public void setCurrentPathFilename(String s) {
        int i=0;
        if(s != null) {
            try {
                setPreviousPathFilename(current_fullpath);
                current_fullpath = s;
                i = s.lastIndexOf(System.getProperty("file.separator"));
                                /*j = s.lastIndexOf("\\");
                                //select i as index wanted
                                if (j>i) {
                                        i = j;
                                }*/
                current_filename = s.substring(i+1);
                if (i > 0) {
                    current_path = s.substring(0, i+1);
                } else {
                    current_path = "";
                }
            } catch (Exception e) {
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
     * @return void
     */
    public void setCurrentPath(String s) {
        if (!(s.substring(s.length()-1).equals(System.getProperty("file.separator")))) {
            s = s + System.getProperty("file.separator");
        }
        current_filename = "";
        current_path = s;
        current_fullpath = s;
    }
    /**
     * This method initializes jDialogUser
     *
     * @return javax.swing.JDialog
     */
    private void getJDialogUser(boolean aged) {
        if(this.userDialog == null){
            this.userDialog= new UserDialog(new javax.swing.JFrame(), true,this,aged);
            this.addDialog(userDialog);
        }
        this.userDialog.setVisible(true);
    }
    
    /**
     * This method initializes jContentPaneDialogAddAttributesText
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneDialogAddAttributesText() {
        if(jContentPaneDialogAddAttributesText == null) {
            jContentPaneDialogAddAttributesText = new javax.swing.JPanel();
            jContentPaneDialogAddAttributesText.setLayout(null);
            jContentPaneDialogAddAttributesText.add(getJScrollPaneDialogAddAttributesText(), null);
            jContentPaneDialogAddAttributesText.add(getJButtonDialogAddAttributesTextSave(), null);
            jContentPaneDialogAddAttributesText.add(getJButtonDialogAddAttributesTextCancel(), null);
        }
        return jContentPaneDialogAddAttributesText;
    }
    /**
     * This method initializes jDialogAddAttributesText
     *
     * @return javax.swing.JDialog
     */
    private javax.swing.JDialog getJDialogAddAttributesText() {
        if(jDialogAddAttributesText == null) {
            jDialogAddAttributesText = new javax.swing.JDialog();
            jDialogAddAttributesText.setContentPane(getJContentPaneDialogAddAttributesText());
            jDialogAddAttributesText.setBounds(200, 200, 600, 540);
            jDialogAddAttributesText.setResizable(false);
            jDialogAddAttributesText.setModal(true);
            jDialogAddAttributesText.setTitle(xerb.getString("window.addattributestext"));
            jDialogAddAttributesText.getRootPane().setDefaultButton(getJButtonDialogAddAttributesTextSave());
            this.addDialog(jDialogAddAttributesText);
        }
        //processing independent of creation
        if (((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getStatus_number() == 1) {
            jButtonDialogAddAttributesTextSave.setEnabled(true);
        } else {
            jButtonDialogAddAttributesTextSave.setEnabled(false);
        }
        jTextAreaDialogAddAttributesText.setText(((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_text());
        return jDialogAddAttributesText;
    }
    /**
     * This method initializes jTextAreaDialogAddAttributesText
     *
     * @return javax.swing.JTextArea
     */
    private javax.swing.JTextArea getJTextAreaDialogAddAttributesText() {
        if(jTextAreaDialogAddAttributesText == null) {
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
        if(jButtonDialogAddAttributesTextSave == null) {
            jButtonDialogAddAttributesTextSave = new javax.swing.JButton();
            jButtonDialogAddAttributesTextSave.setBounds(350, 450, 100, 30);
            jButtonDialogAddAttributesTextSave.setText(xerb.getString("general.save") + "!");
            jButtonDialogAddAttributesTextSave.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(0)).setAttrib_text(jTextAreaDialogAddAttributesText.getText());
                    global_dialog_return_value = 1;
                    jDialogAddAttributesText.setVisible(false);
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
        if(jButtonDialogAddAttributesTextCancel == null) {
            jButtonDialogAddAttributesTextCancel = new javax.swing.JButton();
            jButtonDialogAddAttributesTextCancel.setBounds(470, 450, 100, 30);
            jButtonDialogAddAttributesTextCancel.setText(xerb.getString("general.cancel"));
            jButtonDialogAddAttributesTextCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jDialogAddAttributesText.setVisible(false);
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
        if(jScrollPaneDialogAddAttributesText == null) {
            jScrollPaneDialogAddAttributesText = new javax.swing.JScrollPane();
            jScrollPaneDialogAddAttributesText.setViewportView(getJTextAreaDialogAddAttributesText());
            jScrollPaneDialogAddAttributesText.setBounds(10, 10, 560, 420);
            jScrollPaneDialogAddAttributesText.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPaneDialogAddAttributesText.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        }
        return jScrollPaneDialogAddAttributesText;
    }
    /**
     * This method saves the configuration
     *
     */
    public void saveConfig() {
        try {
            java.io.FileOutputStream fout = new java.io.FileOutputStream(System.getProperty("user.home")+
                    System.getProperty("file.separator")+"xincoClientConfig.dat");
            java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(fout);
            os.writeObject(xincoClientConfig);
            os.close();
            fout.close();
        } catch (Exception ioe) {
        }
    }
    /**
     * This method loads the configuration
     *
     * @return void
     */
    private void loadConfig() {
        Vector tmp_vector_old = new Vector();
        try {
            Vector tmp_vector;
            FileInputStream fin;
            ObjectInputStream ois;
            
            //get old settings
            try {
                fin = new FileInputStream("xincoClientConnectionProfiles.dat");
                ois = new ObjectInputStream(fin);
                try {
                    while ((tmp_vector = (Vector)ois.readObject()) != null) {
                        tmp_vector_old = tmp_vector;
                    }
                } catch (Exception ioe3) {}
                ois.close();
                fin.close();
            } catch (Exception ioe2) {
                tmp_vector_old = null;
            }
            
            fin = new FileInputStream(System.getProperty("user.home")+
                    System.getProperty("file.separator")+"xincoClientConfig.dat");
            ois = new ObjectInputStream(fin);
            
            try {
                while ((tmp_vector = (Vector)ois.readObject()) != null) {
                    xincoClientConfig = tmp_vector;
                }
            } catch (Exception ioe3) {}
            
            ois.close();
            fin.close();
            
            //insert old settings
            if (tmp_vector_old != null) {
                xincoClientConfig.setElementAt(tmp_vector_old, 0);
            }
            //delete old settings
            (new File("xincoClientConnectionProfiles.dat")).delete();
            
        } catch (Exception ioe) {
            //error handling
            //create config
            xincoClientConfig = new Vector();
            //add connection profiles
            xincoClientConfig.addElement(new Vector());
            //insert old settings
            if (tmp_vector_old != null) {
                xincoClientConfig.setElementAt(tmp_vector_old, 0);
                //delete old settings
                (new File("xincoClientConnectionProfiles.dat")).delete();
            } else {
                //insert connection profiles
                ((Vector)xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).profile_name = "xinco Demo User";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).service_endpoint = "http://xinco.org:8080/xinco_demo/services/Xinco";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).username = "user";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).password = "user";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = false;
                ((Vector)xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).profile_name = "xinco Demo Admin";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).service_endpoint = "http://xinco.org:8080/xinco_demo/services/Xinco";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).username = "admin";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).password = "admin";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = false;
                ((Vector)xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).profile_name = "Template Profile";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).service_endpoint = "http://[server_domain]:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).username = "your_username";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).password = "";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = false;
                ((Vector)xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).profile_name = "Admin (localhost)";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).username = "admin";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).password = "";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = false;
                ((Vector)xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).profile_name = "User (localhost)";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).username = "user";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).password = "";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = false;
            }
            //add Pluggable Look and Feel
            xincoClientConfig.addElement(new String("javax.swing.plaf.metal.MetalLookAndFeel"));
            //add locale
            xincoClientConfig.addElement(Locale.getDefault());
        }
    }
    /**
     * This method initializes jDialogArchive
     *
     * @return javax.swing.JDialog
     */
    private javax.swing.JDialog getJDialogArchive() {
        if(jDialogArchive == null) {
            jDialogArchive = new ArchiveDialog(null,true,this);
            jDialogArchive.setTitle(xerb.getString("window.archive"));
            jDialogArchive.setResizable(false);
            this.addDialog(jDialogArchive);
        }
        return jDialogArchive;
    }
    /**
     * This method initializes jDialogArchive
     *
     * @return javax.swing.JDialog
     */
    protected AuditDialog getJDialogAudit() {
        if(auditDialog == null) {
            auditDialog = new AuditDialog(null,true,this);
            auditDialog.setTitle(xerb.getString("window.audit"));
            auditDialog.setResizable(false);
            this.addDialog(auditDialog);
        }
        return auditDialog;
    }
    private int get_global_dialog_return_value(){
        return this.global_dialog_return_value;
    }
    
    public void set_global_dialog_return_value(int v){
        this.global_dialog_return_value=v;
    }
    
    public void setJTableAudit(javax.swing.JTable jTableAudit) {
        this.jTableAudit = jTableAudit;
    }
    
    public XincoClientSetting getSettings() {
        //Catch any recent setting change in the DB
        updateSettings();
        return settings;
    }
    
    public XincoRepositoryActionHandler getActionHandler() {
        if(this.actionHandler==null)
            this.actionHandler=new XincoRepositoryActionHandler(this);
        return actionHandler;
    }
    
    private void updateSettings(){
        settingsVector=null;
        try {
            settingsVector = xincoClientSession.xinco.getXincoSetting(new XincoCoreUser());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        settings= new XincoClientSetting();
        settings.setXinco_settings(settingsVector);
    }
    
    public int getActionSize() {
        return actionSize;
    }
    
    public Icon getXincoIcon(){
        return new javax.swing.ImageIcon(XincoExplorer.class.getResource("blueCubsIcon16x16.GIF"));
    }
    
    protected boolean isLock() {
        return lock;
    }
    
    public void setLock(boolean lock) {
        this.lock = lock;
        if(!this.lock)
            this.xat.getActivityTimer().restart();
    }
    
    public loginThread getLoginT() {
        return loginT;
    }
    
    protected LockDialog getJDialogLock() {
        if(lockDialog == null){
            lockDialog = new LockDialog(null,true,this);
            this.addDialog(lockDialog);
        }
        lockDialog.setVisible(true);
        return lockDialog;
    }
    
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
    
    public void resetTimer(){
        if(this.isLock()){
            this.getJDialogLock();
        } else if(getXat()!=null)
            this.getXat().getActivityTimer().restart();
    }
    
    public void connect(boolean prompt){
        int i = 0;
        if(prompt){
            //init session
            xincoClientSession = new XincoClientSession();
            getJTreeRepository().setModel(xincoClientSession.xincoClientRepository.treemodel);
            xincoClientSession.status = 0;
            //open connection dialog
            getJDialogConnection().setVisible(true);
            DefaultListModel dlm = (DefaultListModel)dialogConnection.getProfileList().getModel();
            dlm.removeAllElements();
            for (i=0;i<((Vector)xincoClientConfig.elementAt(0)).size();i++) {
                dlm.addElement(new String(((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(i)).toString()));
            }
        }
        //establish connection and login
        if (xincoClientSession.status == 1) {
            try {
                xincoClientSession.xinco_service = new XincoServiceLocator();
                xincoClientSession.xinco = xincoClientSession.xinco_service.getXinco(new java.net.URL(xincoClientSession.service_endpoint));
                xincoClientSession.server_version = xincoClientSession.xinco.getXincoServerVersion();
                //check if client and server versions match (high AND mid must match!)
                if ((xincoClientVersion.getVersion_high() != xincoClientSession.server_version.getVersion_high()) || (xincoClientVersion.getVersion_mid() != xincoClientSession.server_version.getVersion_mid())) {
                    throw new XincoException(xerb.getString("menu.connection.error.serverversion") + " " + xincoClientSession.server_version.getVersion_high() + "." + xincoClientSession.server_version.getVersion_mid() + ".x");
                }
                if ((temp = xincoClientSession.xinco.getCurrentXincoCoreUser(xincoClientSession.user.getUsername(), xincoClientSession.user.getUserpassword())) == null) {
                    throw new XincoException(xerb.getString("menu.connection.error.user"));
                }
                updateSettings();
                if(getSettings().getSetting("general.setting.enable.lockidle").isBool_value())
                    xat=new XincoActivityTimer(XincoExplorer.this,
                            getSettings().getSetting("general.setting.enable.lockidle").getInt_value());
                getJDialogConnection().updateProfile();
                temp.setUserpassword(xincoClientSession.user.getUserpassword());
                newuser.setEmail(temp.getEmail());
                newuser.setFirstname(temp.getFirstname());
                newuser.setId(temp.getId());
                newuser.setName(temp.getName());
                newuser.setStatus_number(temp.getStatus_number());
                newuser.setUsername(temp.getUsername());
                newuser.setUserpassword(temp.getUserpassword());
                xincoClientSession.user = xincoClientSession.xinco.getCurrentXincoCoreUser(newuser.getUsername(), newuser.getUserpassword());
                xincoClientSession.server_datatypes = xincoClientSession.xinco.getAllXincoCoreDataTypes(xincoClientSession.user);
                xincoClientSession.server_groups = xincoClientSession.xinco.getAllXincoCoreGroups(xincoClientSession.user);
                xincoClientSession.server_languages = xincoClientSession.xinco.getAllXincoCoreLanguages(xincoClientSession.user);
                for (i=0;i<xincoClientSession.user.getXinco_core_groups().size();i++) {
                    status_string_1 += "      + " + ((XincoCoreGroup)xincoClientSession.user.getXinco_core_groups().elementAt(i)).getDesignation() + "\n";
                }
                for (i=0;i<xincoClientSession.server_datatypes.size();i++) {
                    status_string_2 += "      + " + ((XincoCoreDataType)xincoClientSession.server_datatypes.elementAt(i)).getDesignation() + "\n";
                }
                loginT= new loginThread();
                getLoginT().start();
            }catch (java.rmi.RemoteException cone) {
                if(getProgressBar()!=null)
                    getProgressBar().hide();
                xincoClientSession.status = 0;
                cone.printStackTrace();
                markConnectionStatus();
                JOptionPane.showMessageDialog(XincoExplorer.this,
                        xerb.getString("menu.connection.failed") + " " +
                        xerb.getString("general.reason") + ": " +
                        cone.toString() +" "+xerb.getString("error.connection.incorrect.deployment"),
                        xerb.getString("menu.connection.failed"),
                        JOptionPane.WARNING_MESSAGE);
            }catch (Exception cone) {
                this.getProgressBar().hide();
                xincoClientSession.status = 0;
                cone.printStackTrace();
                markConnectionStatus();
                String exception="";
                if(cone.toString().equals("java.lang"))
                    exception=xerb.getString("error.connection.incorrect.deployment");
                else
                    exception=cone.toString();
                JOptionPane.showMessageDialog(XincoExplorer.this,
                        xerb.getString("menu.connection.failed") + " " +
                        xerb.getString("general.reason") + ": " +
                        cone.toString(),
                        xerb.getString("menu.connection.failed"),
                        JOptionPane.WARNING_MESSAGE);
            }
            //Test workflow
            try {
                getSession().xinco.getWorkflow(2,getSession().user).toString();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void addDialog(JDialog dialog){
        if(this.dialogs==null)
            this.dialogs=new JDialog[1];
        else{
            JDialog [] temp = new JDialog[this.dialogs.length+1];
            for(int i=0;i<this.dialogs.length;i++)
                temp[i]=this.dialogs[i];
            this.dialogs=temp;
        }
        this.dialogs[this.dialogs.length-1]=dialog;
    }
    
    public String getSelectedNodeDesignation(){
        String nodeName="";
        XincoMutableTreeNode node=getSession().currentTreeNodeSelection;
        if(node.getUserObject().getClass() == XincoCoreNode.class)
            nodeName= ((XincoCoreNode) node.getUserObject()).getDesignation();
        if(node.getUserObject().getClass() == XincoCoreData.class)
            nodeName= ((XincoCoreData) node.getUserObject()).getDesignation();
        return nodeName;
    }
    
    public Vector getFilesToBeIndexed() {
        return filesToBeIndexed;
    }
    
    public void setFilesToBeIndexed(Vector filesToBeIndexed) {
        this.filesToBeIndexed = filesToBeIndexed;
    }
    
    public XincoCoreData getXdata() {
        return xdata;
    }
    
    public void setXdata(XincoCoreData xdata) {
        this.xdata = xdata;
    }
    
    public Vector getAudits() {
        return audits;
    }
    
    public void setAudits(Vector audits) {
        this.audits = audits;
    }
    
    public XincoAuditPanel getXincoAuditPanel() {
        return xincoAuditPanel;
    }
    
    public XincoProgressBarThread getProgressBar() {
        return progressBar;
    }
}
