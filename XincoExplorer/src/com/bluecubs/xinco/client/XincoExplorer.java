/**
 *Copyright 2010 blueCubs.com
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
 *
 *************************************************************
 */
package com.bluecubs.xinco.client;

import com.bluecubs.xinco.client.object.WindowClosingAdapter;
import com.bluecubs.xinco.client.object.XincoTreeCellRenderer;
import com.bluecubs.xinco.client.object.XincoClientConnectionProfile;
import com.bluecubs.xinco.client.object.XincoClientSession;
import com.bluecubs.xinco.client.object.XincoMutableTreeNode;
import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.client.dialogs.ACLDialog;
import com.bluecubs.xinco.client.dialogs.AddAttributeText;
import com.bluecubs.xinco.client.dialogs.AddAttributeUniversalDialog;
import com.bluecubs.xinco.client.dialogs.ArchiveDialog;
import com.bluecubs.xinco.client.dialogs.ConnectionDialog;
import com.bluecubs.xinco.client.dialogs.DataDialog;
import com.bluecubs.xinco.client.dialogs.DataFolderDialog;
import com.bluecubs.xinco.client.dialogs.DataTypeDialog;
import com.bluecubs.xinco.client.dialogs.LockDialog;
import com.bluecubs.xinco.client.dialogs.LogDialog;
import com.bluecubs.xinco.client.dialogs.SearchDialog;
import com.bluecubs.xinco.client.dialogs.UserDialog;
import com.bluecubs.xinco.client.object.XincoJTree;
import com.bluecubs.xinco.client.object.XincoRepositoryActionHandler;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.client.object.menu.XincoMenuRepository;
import com.bluecubs.xinco.client.object.menu.XincoPopUpMenuRepository;
import com.bluecubs.xinco.client.object.thread.XincoProgressBarThread;
import com.bluecubs.xinco.client.object.timer.XincoActivityTimer;
import com.bluecubs.xinco.core.OPCode;
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
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    public XincoJTree jTreeRepository = null;
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
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleJTatoo = null;
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
    String previous_fullpath = "";
    //global dialog return value
    private int global_dialog_return_value = 0;
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
    private AbstractDialog AbstractDialogAddAttributesText = null;
    private javax.swing.JPanel jContentPaneDialogAddAttributesText = null;
    private javax.swing.JTextArea jTextAreaDialogAddAttributesText = null;
    private javax.swing.JButton jButtonDialogAddAttributesTextSave = null;
    private javax.swing.JButton jButtonDialogAddAttributesTextCancel = null;
    private javax.swing.JScrollPane jScrollPaneDialogAddAttributesText = null;
    private javax.swing.JPanel jContentPaneDialogTransactionInfo = null;
    private javax.swing.JLabel jLabelDialogTransactionInfoText = null;
    private javax.swing.JPanel jContentPaneInformation = null;
    public javax.swing.JTextArea jLabelInternalFrameInformationText = null;
    private XincoPopUpMenuRepository jPopupMenuRepository = null;
    private JPanel jContentPaneDialogLocale = null;
    private JDialog JDialogLocale = null;
    private JScrollPane jScrollPaneDialogLocale = null;
    private JList jListDialogLocale = null;
    private JButton jButtonDialogLocaleOk = null;
    public XincoProgressBarThread progressBar;
    private ConnectionDialog dialogConnection = null;
    private UserDialog userDialog = null;
    private JInternalFrame jInternalFrameInformation = null;
    private JMenuItem jMenuItemConnectionExit = null;
    private String status_string_1 = "", status_string_2 = "";
    private XincoCoreUser temp;
    private final XincoCoreUser newuser = new XincoCoreUser();
    private loginThread loginT;
    private int wizard_type;
    private XincoMutableTreeNode newnode, previousnode;
    private byte[] byte_array;
    private XincoCoreData xdata;
    private XincoCoreLog newlog;
    private InputStream in = null;
    private final XincoExplorer explorer = this;
    private SearchDialog search;
    private XincoRepositoryActionHandler actionHandler = null;
    //Size of menu actions
    private int actionSize = 19;
    private XincoExplorer.refreshThread rThread;
    private Vector<AbstractDialog> dialogs = null;
    private LockDialog lockDialog = null;
    //Status of the explorer: lock = true - idle time limit exceeded, user must log cin again to continue use
    //lock = false - work normally
    private boolean lock = false;
    private XincoActivityTimer xat = null;

    /**
     * This is the default constructor
     */
    public XincoExplorer() {
        super();
        try {
            setIconImage((new ImageIcon(XincoExplorer.class.getResource("blueCubsIcon.gif"))).getImage());
        } catch (Exception icone) {
            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, icone);
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
        } catch (Exception e) {
            loc = Locale.getDefault();
        }
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
        xerb.getLocale();
        progressBar = new XincoProgressBarThread(this);
        initialize();
        //Windows-Listener
        addWindowListener(new WindowClosingAdapter(true));
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
     * @return XincoPopUpMenuRepository
     */
    public XincoPopUpMenuRepository getJPopupMenuRepository() {
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
     * This method initializes AbstractDialogLocale
     *
     * @return AbstractDialog
     */
    private JDialog getJDialogLocale() {
        if (JDialogLocale == null) {
            JDialogLocale = new JDialog();
            JDialogLocale.setContentPane(getJContentPaneDialogLocale());
            JDialogLocale.setTitle("XincoExplorer");
            JDialogLocale.setBounds(400, 400, 300, 200);
            JDialogLocale.setResizable(false);
            JDialogLocale.setModal(true);
            JDialogLocale.setAlwaysOnTop(true);
            JDialogLocale.getRootPane().setDefaultButton(getJButtonDialogLocaleOk());
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
        dlm = (DefaultListModel) jListDialogLocale.getModel();
        dlm.removeAllElements();
        selection = -1;
        alt_selection = 0;
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
                alt_selection = i;
            }
        }
        if (selection == -1) {
            selection = alt_selection;
        }
        jListDialogLocale.setSelectedIndex(selection);
        jListDialogLocale.ensureIndexIsVisible(jListDialogLocale.getSelectedIndex());
        return JDialogLocale;
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
                        JDialogLocale.setVisible(false);
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
        ResourceBundle settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
        //init session
        xincoClientSession = new XincoClientSession(this);
        //set client version
        xincoClientVersion = new XincoVersion();
        xincoClientVersion.setVersion_high(Integer.parseInt(settings.getString("version.high")));
        xincoClientVersion.setVersion_mid(Integer.parseInt(settings.getString("version.mid")));
        xincoClientVersion.setVersion_low(Integer.parseInt(settings.getString("version.low")));
        xincoClientVersion.setVersion_postfix(settings.getString("version.postfix"));
        switchPLAF((String) xincoClientConfig.elementAt(1));
        this.setBounds(0, 0, (new Double(getToolkit().getScreenSize().getWidth())).intValue() - 100,
                (new Double(getToolkit().getScreenSize().getHeight())).intValue() - 75);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setName("XincoExplorer");
        this.setTitle(xerb.getString("general.clienttitle") + " - "
                + xerb.getString("general.version") + " "
                + xincoClientVersion.getVersion_high() + "."
                + xincoClientVersion.getVersion_mid() + "."
                + xincoClientVersion.getVersion_low() + " "
                + xincoClientVersion.getVersion_postfix());
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
                    String message_string = "";
                    message_string = message_string + xerb.getString("window.aboutxinco.clienttitle") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.clientversion") + ": "
                            + xincoClientVersion.getVersion_high() + "." + xincoClientVersion.getVersion_mid() + "."
                            + xincoClientVersion.getVersion_low() + " " + xincoClientVersion.getVersion_postfix() + "\n";
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

    private void expandAllNodes() {
        getJTreeRepository();
        int row = 0;
        while (row < jTreeRepository.getRowCount()) {
            jTreeRepository.expandRow(row);
            row++;
        }
    }

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
            jMenuView.add(getJRadioButtonMenuItemViewStyleMotif());
            jMenuView.add(getJRadioButtonMenuItemViewStyleNapkin());
            jMenuView.add(getJRadioButtonMenuItemViewStyleSubstance());
            jMenuView.add(getJRadioButtonMenuItemViewStyleLiquid());
            jMenuView.add(getJRadioButtonMenuItemViewStyleJTatoo());
            bgwindowstyle.add(jMenuView.getItem(0));
            bgwindowstyle.add(jMenuView.getItem(1));
            bgwindowstyle.add(jMenuView.getItem(2));
            bgwindowstyle.add(jMenuView.getItem(3));
            bgwindowstyle.add(jMenuView.getItem(4));
            bgwindowstyle.add(jMenuView.getItem(5));
            bgwindowstyle.add(jMenuView.getItem(6));
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
     * This method initializes jRadioButtonMenuItemViewStyleLiquid
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
     * This method initializes jRadioButtonMenuItemViewStyleLiquid
     *
     * @return javax.swing.JRadioButtonMenuItem
     */
    private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItemViewStyleJTatoo() {
        if (jRadioButtonMenuItemViewStyleJTatoo == null) {
            jRadioButtonMenuItemViewStyleJTatoo = new javax.swing.JRadioButtonMenuItem();
            if (((String) xincoClientConfig.elementAt(1)).equals(new String("com.jtattoo.plaf.smart.SmartLookAndFeel"))) {
                jRadioButtonMenuItemViewStyleJTatoo.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleJTatoo.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleJTatoo.setText(xerb.getString("menu.view.jtatoo"));
            jRadioButtonMenuItemViewStyleJTatoo.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("com.jtattoo.plaf.smart.SmartLookAndFeel");
                    xincoClientConfig.setElementAt(new String("com.jtattoo.plaf.smart.SmartLookAndFeel"), 1);
                    saveConfig();
                }
            });
        }
        return jRadioButtonMenuItemViewStyleJTatoo;
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
        status_string_1 = "";
        status_string_2 = "";
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
                            xincoClientSession.setServeVersion(xincoClientSession.getXinco().getXincoServerVersion());
                            //check if client and server versions match (high AND mid must match!)
                            if ((xincoClientVersion.getVersion_high() != xincoClientSession.getServerVersion().getVersion_high()) || (xincoClientVersion.getVersion_mid() != xincoClientSession.getServerVersion().getVersion_mid())) {
                                throw new XincoException(xerb.getString("menu.connection.error.serverversion") + " " + xincoClientSession.getServerVersion().getVersion_high() + "." + xincoClientSession.getServerVersion().getVersion_mid() + ".x");
                            }
                            if ((temp = xincoClientSession.getXinco().getCurrentXincoCoreUser(xincoClientSession.getUser().getUsername(), xincoClientSession.getUser().getUserpassword())) == null) {
                                throw new XincoException(xerb.getString("menu.connection.error.user"));
                            }
                            temp.setUserpassword(xincoClientSession.getUser().getUserpassword());
                            newuser.setEmail(temp.getEmail());
                            newuser.setFirstname(temp.getFirstname());
                            newuser.setId(temp.getId());
                            newuser.setName(temp.getName());
                            newuser.setStatus_number(temp.getStatus_number());
                            newuser.setUsername(temp.getUsername());
                            newuser.setUserpassword(temp.getUserpassword());
                            xincoClientSession.setUser(xincoClientSession.getXinco().getCurrentXincoCoreUser(newuser.getUsername(), newuser.getUserpassword()));
                            progressBar.run();
                            xincoClientSession.setServerDatatypes(xincoClientSession.getXinco().getAllXincoCoreDataTypes(xincoClientSession.getUser()));
                            xincoClientSession.setServeGroups(xincoClientSession.getXinco().getAllXincoCoreGroups(xincoClientSession.getUser()));
                            xincoClientSession.setServerLanguages(xincoClientSession.getXinco().getAllXincoCoreLanguages(xincoClientSession.getUser()));
                            for (i = 0; i < xincoClientSession.getUser().getXinco_core_groups().size(); i++) {
                                String label = ((XincoCoreGroup) xincoClientSession.getUser().getXinco_core_groups().elementAt(i)).getDesignation();
                                try {
                                    label = explorer.getResourceBundle().getString(label);
                                } catch (java.util.MissingResourceException ex) {
                                    //Nothing to translate
                                }
                                status_string_1 = status_string_1 + "      + " + label + "\n";
                            }
                            for (i = 0; i < xincoClientSession.getServerDatatypes().size(); i++) {
                                status_string_2 += "      + " + xerb.getString(((XincoCoreDataType) xincoClientSession.getServerDatatypes().elementAt(i)).getDesignation()) + "\n";
                            }
                            loginT = new loginThread();
                            loginT.start();
                        } catch (Exception cone) {
                            xincoClientSession.setStatus(0);
                            cone.printStackTrace();
                            markConnectionStatus();
                            JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("menu.connection.failed") + " " + xerb.getString("general.reason") + ": " + cone.toString(), xerb.getString("menu.connection.failed"), JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            });
        }
        return jMenuItemConnectionConnect;
    }

    public XincoCoreUser getUser() {
        return this.newuser;
    }

    /**
     * @return the global_dialog_return_value
     */
    public int getGlobal_dialog_return_value() {
        return global_dialog_return_value;
    }

    /**
     * @param global_dialog_return_value the global_dialog_return_value to set
     */
    public void setGlobal_dialog_return_value(int global_dialog_return_value) {
        this.global_dialog_return_value = global_dialog_return_value;
    }

    /**
     * @param lock the lock to set
     */
    public void setLock(boolean lock) {
        this.lock = lock;
    }

    private class loginThread extends Thread {

        @Override
        public void run() {
            try {
                String status_string = "";
                temp = xincoClientSession.getXinco().getCurrentXincoCoreUser(xincoClientSession.getUser().getUsername(), xincoClientSession.getUser().getUserpassword());
                status_string += xerb.getString("menu.connection.connectedto") + ": " + xincoClientSession.getServiceEndpoint() + "\n";
                status_string += xerb.getString("general.serverversion") + ": ";
                status_string += xincoClientSession.getServerVersion().getVersion_high() + ".";
                status_string += xincoClientSession.getServerVersion().getVersion_mid() + ".";
                status_string += xincoClientSession.getServerVersion().getVersion_low() + " ";
                status_string += xincoClientSession.getServerVersion().getVersion_postfix() + "\n";
                status_string += "\n";
                status_string += xerb.getString("general.user") + ": " + xincoClientSession.getUser().getFirstname() + " " + xincoClientSession.getUser().getName() + " <" + xincoClientSession.getUser().getEmail() + ">\n";
                status_string += xerb.getString("general.memberof") + ":\n";
                status_string += status_string_1 + "\n";
                status_string += xerb.getString("general.groupsonserver") + ": " + xincoClientSession.getServerGroups().size() + "\n";
                status_string += xerb.getString("general.languagesonserver") + ": " + xincoClientSession.getServerLanguages().size() + "\n";
                status_string += xerb.getString("general.datatypesonserver") + ": " + xincoClientSession.getServerDatatypes().size() + "\n";
                status_string += status_string_2 + "\n";
                xincoClientSession.setCurrentSearchResult(new Vector());
                xincoClientSession.setStatus(2);
                JOptionPane.showMessageDialog(XincoExplorer.this, status_string, xerb.getString("menu.connection.established"), JOptionPane.INFORMATION_MESSAGE);
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
                if (temp.getStatus_number() == 3) {
                    jLabelInternalFrameInformationText.setText(xerb.getString("password.aged"));
                    getAbstractDialogUser(true);
                }
                progressBar.hide();
            } catch (Exception cone) {
                xincoClientSession.setStatus(0);
                cone.printStackTrace();
                markConnectionStatus();
                JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("menu.connection.failed") + " " + xerb.getString("general.reason") + ": " + cone.toString(), xerb.getString("menu.connection.failed"), JOptionPane.WARNING_MESSAGE);
                progressBar.hide();
            }
        }

        public void resetStrings() {
            status_string_1 = "";
            status_string_2 = "";
        }
    }

    /**
     * This method initializes jContentPaneDialogConnection
     *
     * @return javax.swing.JPanel
     */
    private void getJContentPaneDialogConnection() {
        if (this.dialogConnection == null) {
            this.dialogConnection = new ConnectionDialog(new javax.swing.JFrame(),
                    true, this);
        }
    }

    /**
     * This method initializes AbstractDialogConnection
     *
     * @return AbstractDialog
     */
    private AbstractDialog getAbstractDialogConnection() {
        if (dialogConnection == null) {
            dialogConnection = new ConnectionDialog(new javax.swing.JFrame(),
                    true, this);
            addDialog(dialogConnection);
        }
        dialogConnection.setVisible(true);
        return dialogConnection;
    }

    public ResourceBundle getResourceBundle() {
        return this.xerb;
    }

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
            jPopupMenuRepository.resetItems();
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
    private JInternalFrame getJInternalFrameInformation() {
        if (jInternalFrameInformation == null) {
            jInternalFrameInformation = new JInternalFrame();
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
            //TODO: Convert to forms
            if (JDialogLocale != null) {
                SwingUtilities.updateComponentTreeUI(JDialogLocale);
            }
            if (jDialogRevision != null) {
                SwingUtilities.updateComponentTreeUI(jDialogRevision);
            }
        } catch (Exception plafe) {
            Logger.getLogger(XincoExplorer.class.getName()).log(Level.SEVERE, null, plafe);
            resetExplorer();
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
     * This method initializes AbstractDialogFolder
     *
     * @return AbstractDialog
     */
    public AbstractDialog getAbstractDialogFolder() {
        if (AbstractDialogFolder == null) {
            AbstractDialogFolder = new DataFolderDialog(null, true, this);
            addDialog(AbstractDialogFolder);
        }
        //Issue #2972311
        AbstractDialogFolder.setVisible(true);
        return AbstractDialogFolder;
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
        //Issue #2972311
        AbstractDialogACL.setVisible(true);
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
            for (i = 0; i < ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().size(); i++) {
                if ((((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getOp_code() == 1)
                        || (((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getOp_code() == 5)) {
                    //convert clone from remote time to local time
                    cal = (Calendar) ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getOp_datetime().clone();
                    realcal = (((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getOp_datetime());
                    cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - realcal.get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + realcal.get(Calendar.DST_OFFSET)));
                    text = "" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH);
                    text = text + " - " + xerb.getString("general.version") + " " + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_high()
                            + "." + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_mid() + "."
                            + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_low() + " "
                            + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_postfix();
                    text = text + " - " + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getOp_description();

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
                        for (i = 0; i < ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().size(); i++) {
                            if ((((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getOp_code() == 1) || (((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getOp_code() == 5)) {
                                RealLogIndex++;
                            }
                            if (RealLogIndex == jListDialogRevision.getSelectedIndex()) {
                                setGlobal_dialog_return_value(((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(i)).getId());
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
        AbstractDialogData.setVisible(true);
        return AbstractDialogData;
    }

    /**
     * This method initializes AbstractDialogLog
     *
     * @return AbstractDialog
     */
    private AbstractDialog getAbstractDialogLog(boolean editableVersion) {
        if (AbstractDialogLog == null) {
            AbstractDialogLog = new LogDialog(null, true, this, editableVersion);
            addDialog(AbstractDialogLog);
        }
        AbstractDialogLog.setVisible(true);
        return AbstractDialogLog;
    }

    /**
     * This method imports files + subfolders of a folder into node
     *
     * @param node XincoCoreNode
     * @param folder File
     * @throws java.lang.Exception 
     */
    @SuppressWarnings("unchecked")
    public void importContentOfFolder(XincoCoreNode node, File folder) throws Exception {
        int i = 0;
        int j = 0;
        File[] folder_list = null;
        folder_list = folder.listFiles();
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
        int alt_selection = 0;
        for (j = 0; j < xincoClientSession.getServerLanguages().size(); j++) {
            if (((XincoCoreLanguage) xincoClientSession.getServerLanguages().elementAt(j)).getSign().toLowerCase().compareTo(Locale.getDefault().getLanguage().toLowerCase()) == 0) {
                selection = j;
                break;
            }
            if (((XincoCoreLanguage) xincoClientSession.getServerLanguages().elementAt(j)).getId() == 1) {
                alt_selection = j;
            }
        }
        if (selection == -1) {
            selection = alt_selection;
        }
        xcl1 = (XincoCoreLanguage) xincoClientSession.getServerLanguages().elementAt(selection);
        //process files
        progressBar.setTitle(xerb.getString("datawizard.fileuploadinfo"));
        progressBar.show();
        for (i = 0; i < folder_list.length; i++) {
            if (folder_list[i].isFile()) {
                // set current node to new one
                newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
                // set data attributes
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_node_id(node.getId());
                ((XincoCoreData) newnode.getUserObject()).setDesignation(folder_list[i].getName());
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_data_type(xcdt1);
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_language(xcl1);
                ((XincoCoreData) newnode.getUserObject()).setXinco_add_attributes(new Vector());
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_acl(new Vector());
                ((XincoCoreData) newnode.getUserObject()).setXinco_core_logs(new Vector());
                ((XincoCoreData) newnode.getUserObject()).setStatus_number(1);
                xincoClientSession.getXincoClientRepository().treemodel.insertNodeInto(newnode,
                        xincoClientSession.getCurrentTreeNodeSelection(), xincoClientSession.getCurrentTreeNodeSelection().getChildCount());
                xincoClientSession.setCurrentTreeNodeSelection(newnode);
                // add specific attributes
                XincoAddAttribute xaa;

                for (j = 0; j
                        < ((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().size(); j++) {
                    xaa = new XincoAddAttribute();
                    // xaa.setAttribute_id(j+1); // bug => attribute_ids might be missing cin between
                    xaa.setAttribute_id(((XincoCoreDataTypeAttribute) xcdt1.getXinco_core_data_type_attributes().elementAt(j)).getAttribute_id());
                    xaa.setAttrib_varchar("");
                    xaa.setAttrib_text("");
                    xaa.setAttrib_datetime(new GregorianCalendar());
                    ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().addElement(xaa);
                }
                // add log
                newlog = new XincoCoreLog();
                newlog.setOp_code(OPCode.CREATION.ordinal() + 1);
                newlog.setOp_description(xerb.getString(OPCode.getOPCode(newlog.getOp_code()).getName())
                        + "!" + " ("
                        + xerb.getString("general.user") + ": "
                        + xincoClientSession.getUser().getUsername()
                        + ")");
                newlog.setXinco_core_user_id(xincoClientSession.getUser().getId());
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

                if (((xincoClientSession.getServerVersion().getVersion_high() == 1)
                        && (xincoClientSession.getServerVersion().getVersion_mid() >= 9))
                        || (xincoClientSession.getServerVersion().getVersion_high() > 1)) {
                    useSAAJ = true;
                } else {
                    useSAAJ = false;
                }
                CheckedInputStream cin = null;
                ByteArrayOutputStream out = null;

                byte_array = null;
                try {
                    cin = new CheckedInputStream(new FileInputStream(folder_list[i]),
                            new CRC32());
                    if (useSAAJ) {
                        total_len = folder_list[i].length();
                    } else {
                        out = new ByteArrayOutputStream();
                        byte[] buf = new byte[4096];
                        int len = 0;

                        total_len = 0;
                        while ((len = cin.read(buf)) > 0) {
                            out.write(buf, 0, len);
                            total_len = total_len + len;
                        }
                        byte_array = out.toByteArray();
                        out.close();
                    }
                    // update attributes
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).setAttrib_varchar(folder_list[i].getName());
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(1)).setAttrib_unsignedint(total_len);
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(2)).setAttrib_varchar(""
                            + cin.getChecksum().getValue());
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(3)).setAttrib_unsignedint(1);
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(4)).setAttrib_unsignedint(0);
                    if (!useSAAJ) {
                        cin.close();
                    }
                } catch (Exception fe) {
                    throw new XincoException(xerb.getString("datawizard.unabletoloadfile"));
                }
                // save data to server
                xdata = xincoClientSession.getXinco().setXincoCoreData((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser());
                if (xdata == null) {
                    throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
                }
                newnode.setUserObject(xdata);
                // update id cin log
                newlog.setXinco_core_data_id(((XincoCoreData) newnode.getUserObject()).getId());
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
                // upload file
                if (xincoClientSession.getXinco().uploadXincoCoreData((XincoCoreData) newnode.getUserObject(),
                        byte_array, xincoClientSession.getUser())
                        != total_len) {
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
                xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) newnode.getParent());
            }
        }
        //process directories
        for (i = 0; i < folder_list.length; i++) {
            if (folder_list[i].isDirectory()) {
                // set current node to new one
                newnode = new XincoMutableTreeNode(new XincoCoreNode(), this);
                // set node attributes
                ((XincoCoreNode) newnode.getUserObject()).setXinco_core_node_id(node.getId());
                ((XincoCoreNode) newnode.getUserObject()).setDesignation(folder_list[i].getName());
                ((XincoCoreNode) newnode.getUserObject()).setXinco_core_language(xcl1);
                ((XincoCoreNode) newnode.getUserObject()).setStatus_number(1);
                // Bug fix by cmichl for Import Data Structure java.lang.NullPointerException (http://www.bluecubs.com/viewtopic.php?xincoCoreUser=500)
                if (xincoClientSession.getCurrentTreeNodeSelection() == null) {
                    xincoClientSession.setCurrentTreeNodeSelection(previousnode);
                }
                //End bug fix
                xincoClientSession.getXincoClientRepository().treemodel.insertNodeInto(newnode,
                        xincoClientSession.getCurrentTreeNodeSelection(), xincoClientSession.getCurrentTreeNodeSelection().getChildCount());
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
                        folder_list[i]);
                // select parent of new node
                // Bug fix by cmichl for Import Data Structure java.lang.NullPointerException (http://www.bluecubs.com/viewtopic.php?xincoCoreUser=500)
                if (newnode.getParent() != null) {
                    xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) newnode.getParent());
                }
            }
        }
        refreshJTree();
        progressBar.hide();
    }

    /**
     * This method download files + subnodes of a node into folder
     *
     * @param node XincoCoreNode
     * @param folder File
     * @throws java.lang.Exception 
     */
    @SuppressWarnings("unchecked")
    public void downloadContentOfNode(XincoCoreNode node, File folder) throws Exception {
        int i = 0;
        XincoMutableTreeNode currentNode = null;

        if (xincoClientSession.getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
            currentNode = xincoClientSession.getCurrentTreeNodeSelection();

            // only nodes have children
            if (currentNode.getUserObject().getClass() == XincoCoreNode.class) {
                // check for children only if none have been found yet
                if ((((XincoCoreNode) currentNode.getUserObject()).getXinco_core_nodes().size() == 0)
                        && (((XincoCoreNode) currentNode.getUserObject()).getXinco_core_data().size() == 0)) {
                    try {
                        XincoCoreNode xnode = xincoClientSession.getXinco().getXincoCoreNode((XincoCoreNode) currentNode.getUserObject(), xincoClientSession.getUser());

                        if (xnode != null) {
                            xincoClientSession.getXincoClientRepository().assignObject2TreeNode(currentNode,
                                    xnode, xincoClientSession.getXinco(), xincoClientSession.getUser(),
                                    2);
                        }
                    } catch (Exception rmie) {
                    }
                }
            }

            // download files of node
            for (i = 0; i < currentNode.getChildCount(); i++) {
                if (((XincoMutableTreeNode) currentNode.getChildAt(i)).getUserObject().getClass() == XincoCoreData.class) {
                    if (((XincoCoreData) ((XincoMutableTreeNode) currentNode.getChildAt(i)).getUserObject()).getXinco_core_data_type().getId() == 1) {
                        xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) currentNode.getChildAt(i));
                        // load full data
                        try {
                            XincoCoreData tempXdata = xincoClientSession.getXinco().getXincoCoreData(((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()), xincoClientSession.getUser());
                            if (tempXdata != null) {
                                xincoClientSession.getCurrentTreeNodeSelection().setUserObject(tempXdata);
                                xincoClientSession.getXincoClientRepository().treemodel.nodeChanged(xincoClientSession.getCurrentTreeNodeSelection());
                                // download file
                                setCurrentPathFilename(folder.getAbsolutePath() + System.getProperty("file.separator") + ((XincoAddAttribute) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_varchar());
                                doDataWizard(15);
                            }
                        } catch (Exception rmie) {
                        }
                    }
                }
            }

            // process subnodes
            for (i = 0; i < currentNode.getChildCount(); i++) {
                if (((XincoMutableTreeNode) currentNode.getChildAt(i)).getUserObject().getClass() == XincoCoreNode.class) {
                    File newfolder = null;
                    newfolder = new File(folder.getAbsolutePath() + System.getProperty("file.separator") + ((XincoCoreNode) ((XincoMutableTreeNode) currentNode.getChildAt(i)).getUserObject()).getDesignation());
                    if (newfolder.mkdirs() || newfolder.isDirectory()) {
                        xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) currentNode.getChildAt(i));
                        downloadContentOfNode((XincoCoreNode) xincoClientSession.getCurrentTreeNodeSelection().getUserObject(), newfolder);
                    }
                }
            }

        }

    }

    /**
     * This method leads through data adding/editing
     *
     * @param wizard_type 
     */
    @SuppressWarnings("unchecked")
    public void doDataWizard(final int wizard_type) {
        this.wizard_type = wizard_type;
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
        = 15 = download file with predefined name
         */
        int i = 0, j = 0;
        newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
        xdata = null;
        newlog = new XincoCoreLog();

        in = null;
        byte_array = null;
        if (xincoClientSession.getCurrentTreeNodeSelection() != null) {
            //execute wizard as a whole
            try {
                //add new data
                if ((wizard_type == 1)
                        && (xincoClientSession.getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class)) {

                    //set current node to new one
                    newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
                    //set data attributes
                    ((XincoCoreData) newnode.getUserObject()).setXinco_core_node_id(((XincoCoreNode) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getId());
                    ((XincoCoreData) newnode.getUserObject()).setDesignation(xerb.getString("datawizard.newdata"));
                    ((XincoCoreData) newnode.getUserObject()).setXinco_core_data_type((XincoCoreDataType) xincoClientSession.getServerDatatypes().elementAt(0));
                    ((XincoCoreData) newnode.getUserObject()).setXinco_core_language((XincoCoreLanguage) xincoClientSession.getServerLanguages().elementAt(0));
                    ((XincoCoreData) newnode.getUserObject()).setXinco_add_attributes(new Vector());
                    ((XincoCoreData) newnode.getUserObject()).setXinco_core_acl(new Vector());
                    ((XincoCoreData) newnode.getUserObject()).setXinco_core_logs(new Vector());
                    ((XincoCoreData) newnode.getUserObject()).setStatus_number(1);
                    xincoClientSession.getXincoClientRepository().treemodel.insertNodeInto(newnode, xincoClientSession.getCurrentTreeNodeSelection(), xincoClientSession.getCurrentTreeNodeSelection().getChildCount());
                    xincoClientSession.setCurrentTreeNodeSelection(newnode);

                    //step 1: select data type
                    AbstractDialogDataType = new DataTypeDialog(null, true, this);
                    setGlobal_dialog_return_value(0);
                    AbstractDialogDataType.setVisible(true);
                    if (getGlobal_dialog_return_value() == 0) {
                        this.progressBar.hide();
                        throw new XincoException(xerb.getString("datawizard.updatecancel"));
                    }

                    //add specific attributes
                    XincoAddAttribute xaa;
                    for (i = 0; i < ((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().size(); i++) {
                        xaa = new XincoAddAttribute();
                        xaa.setAttribute_id(((XincoCoreDataTypeAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getAttribute_id());
                        xaa.setAttrib_varchar("");
                        xaa.setAttrib_text("");
                        xaa.setAttrib_datetime(new GregorianCalendar());
                        ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().addElement(xaa);
                    }

                    //initialize specific attributes:
                    //files
                    if (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 1) {
                        ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(3)).setAttrib_unsignedint(1); //revision model
                        ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(4)).setAttrib_unsignedint(0); //archiving model
                    }

                }

                if (xincoClientSession.getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreData.class) {

                    newnode = xincoClientSession.getCurrentTreeNodeSelection();

                    //check file attribute count
                    //file = 1
                    if ((wizard_type == 3)
                            && ((((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 1)
                            && (((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().size() <= 3))) {
                        this.progressBar.hide();
                        throw new XincoException(xerb.getString("datawizard.noaddattributes"));
                    }

                    //edit add attributes
                    if ((wizard_type == 1) || (wizard_type == 3)) {

                        //step 2: edit add attributes
                        //for files -> show filechooser
                        //file = 1
                        if ((wizard_type == 1)
                                && (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 1)) {
                            JFileChooser fc = new JFileChooser();

                            fc.setCurrentDirectory(new File(current_path));
                            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            // show dialog
                            int result = fc.showOpenDialog(XincoExplorer.this);

                            if (result != JFileChooser.APPROVE_OPTION) {
                                this.progressBar.hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            setCurrentPathFilename(fc.getSelectedFile().getPath());
                            ((XincoCoreData) newnode.getUserObject()).setDesignation(current_filename);
                        }
                        //for text -> show text editing dialog
                        //text = 2
                        if (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 2) {
                            setGlobal_dialog_return_value(0);
                            AbstractDialogAddAttributesText = getAbstractDialogAddAttributesText();
                            if (getGlobal_dialog_return_value() == 0) {
                                this.progressBar.hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }
                        //show dialog for all additional attributes and custom data types
                        //file = 1 / text = 2
                        if ((((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId()
                                != 1
                                || ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().size()
                                > 8)
                                && (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId()
                                != 2
                                || ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().size()
                                > 1)) {
                            //for other data type -> show universal add attribute dialog
                            AbstractDialogAddAttributesUniversal = new AddAttributeUniversalDialog(null, true, this);
                            setGlobal_dialog_return_value(0);
                            AbstractDialogAddAttributesUniversal.setVisible(true);
                            if (getGlobal_dialog_return_value() == 0) {
                                this.progressBar.hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }

                    }

                    //edit logging
                    //step 3: edit logging (creation!)
                    if (wizard_type == 1) {
                        newlog = new XincoCoreLog();
                        newlog.setOp_code(OPCode.CREATION.ordinal() + 1);
                        newlog.setOp_description(xerb.getString(OPCode.getOPCode(newlog.getOp_code()).getName()) + "!");
                        newlog.setXinco_core_user_id(xincoClientSession.getUser().getId());
                        newlog.setXinco_core_data_id(((XincoCoreData) newnode.getUserObject()).getId()); //update to new id later!
                        newlog.setVersion(new XincoVersion());
                        newlog.getVersion().setVersion_high(0);
                        newlog.getVersion().setVersion_mid(0);
                        newlog.getVersion().setVersion_low(0);
                        newlog.getVersion().setVersion_postfix("");
                        ((XincoCoreData) newnode.getUserObject()).setXinco_core_logs(new Vector());
                        ((XincoCoreData) newnode.getUserObject()).getXinco_core_logs().addElement(newlog);
                        setGlobal_dialog_return_value(0);
                        AbstractDialogLog = getAbstractDialogLog(true);
                        if (getGlobal_dialog_return_value() == 0) {
                            this.progressBar.hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
                        newlog.setOp_description(newlog.getOp_description() + " (" + xerb.getString("general.user") + ": " + xincoClientSession.getUser().getUsername() + ")");
                    } else {
                        if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) && (wizard_type != 14) && (wizard_type != 15)) {
                            newlog = new XincoCoreLog();
                            if (wizard_type <= 3) {
                                newlog.setOp_code(OPCode.MODIFICATION.ordinal() + 1);
                                newlog.setOp_description(xerb.getString(OPCode.getOPCode(newlog.getOp_code()).getName()) + "!");
                            }
                            if (wizard_type == 4) {
                                newlog.setOp_code(OPCode.CHECKOUT.ordinal() + 1);
                                newlog.setOp_description(xerb.getString(OPCode.getOPCode(newlog.getOp_code()).getName()));
                            }
                            if (wizard_type == 5) {
                                newlog.setOp_code(OPCode.CHECKOUT_UNDONE.ordinal() + 1);
                                newlog.setOp_description(xerb.getString(OPCode.getOPCode(newlog.getOp_code()).getName()));
                            }
                            if (wizard_type == 6) {
                                newlog.setOp_code(OPCode.CHECKIN.ordinal() + 1);
                                newlog.setOp_description(xerb.getString(OPCode.getOPCode(newlog.getOp_code()).getName()));
                            }
                            if (wizard_type == 10) {
                                newlog.setOp_code(OPCode.PUBLISH_COMMENT.ordinal() + 1);
                                newlog.setOp_description(xerb.getString(OPCode.getOPCode(newlog.getOp_code()).getName()));
                            }
                            if (wizard_type == 12) {
                                newlog.setOp_code(OPCode.LOCK_COMMENT.ordinal() + 1);
                                newlog.setOp_description(xerb.getString(OPCode.getOPCode(newlog.getOp_code()).getName()));
                            }
                            if (wizard_type == 13) {
                                newlog.setOp_code(OPCode.COMMENT_COMMENT.ordinal() + 1);
                                newlog.setOp_description(xerb.getString(OPCode.getOPCode(newlog.getOp_code()).getName()));
                            }
                            newlog.setXinco_core_user_id(xincoClientSession.getUser().getId());
                            newlog.setXinco_core_data_id(((XincoCoreData) newnode.getUserObject()).getId());
                            newlog.setVersion(((XincoCoreLog) ((XincoCoreData) newnode.getUserObject()).getXinco_core_logs().elementAt(((XincoCoreData) newnode.getUserObject()).getXinco_core_logs().size() - 1)).getVersion());
                            ((XincoCoreData) newnode.getUserObject()).getXinco_core_logs().addElement(newlog);
                            //Nothing to do in log dialog for checkout...
                            if (wizard_type != 4) {
                                setGlobal_dialog_return_value(0);
                                AbstractDialogLog = getAbstractDialogLog(false);
                                if (getGlobal_dialog_return_value() == 0) {
                                    this.progressBar.hide();
                                    throw new XincoException(xerb.getString("datawizard.updatecancel"));
                                }
                            }
                            newlog.setOp_description(newlog.getOp_description() + " (" + xerb.getString("general.user") + ": " + xincoClientSession.getUser().getUsername() + ")");
                        }
                    }

                    //choose filename for checkout/checkin/download/preview
                    if ((wizard_type == 4) || (wizard_type == 6) || (wizard_type == 7) || (wizard_type == 11)) {
                        JFileChooser fc = new JFileChooser();

                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fc.setSelectedFile(new File(current_path
                                + ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_varchar()));
                        // show dialog
                        int result;

                        if ((wizard_type == 4) || (wizard_type == 7)
                                || (wizard_type == 11)) {
                            result = fc.showSaveDialog(XincoExplorer.this);
                        } else {
                            result = fc.showOpenDialog(XincoExplorer.this);
                        }
                        if (result != JFileChooser.APPROVE_OPTION) {
                            this.progressBar.hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
                        setCurrentPathFilename(fc.getSelectedFile().getPath());
                    }
                    if (wizard_type == 14) {
                        setCurrentPathFilename(File.createTempFile("xinco_", "_" + ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_varchar()).getPath());
                    }

                    //edit data details
                    if ((wizard_type == 1) || (wizard_type == 2)) {

                        //step 4: edit data details
                        setGlobal_dialog_return_value(0);
                        AbstractDialogData = getAbstractDialogData();
                        if (getGlobal_dialog_return_value() == 0) {
                            this.progressBar.hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }

                        //step 4b: edit archiving options of files
                        if (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 1) {
                            setGlobal_dialog_return_value(0);
                            AbstractDialogArchive = getAbstractDialogArchive();
                            if (getGlobal_dialog_return_value() == 0) {
                                this.progressBar.hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }

                    }
                    //set status = published
                    if (wizard_type == 10) {
                        ((XincoCoreData) newnode.getUserObject()).setStatus_number(5);
                    }
                    //set status = locked
                    if (wizard_type == 12) {
                        ((XincoCoreData) newnode.getUserObject()).setStatus_number(2);
                    }

                    //invoke web service (update data / (upload file) / add log)
                    //load file (new / checkin)
                    long total_len = 0;
                    boolean useSAAJ = false;
                    if (((xincoClientSession.getServerVersion().getVersion_high() == 1) && (xincoClientSession.getServerVersion().getVersion_mid() >= 9)) || (xincoClientSession.getServerVersion().getVersion_high() > 1)) {
                        useSAAJ = true;
                    } else {
                        useSAAJ = false;
                    }
                    ByteArrayOutputStream out = null;
                    //file = 1
                    if (((wizard_type == 1) || (wizard_type == 6)) && (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 1)) {
                        try {
                            //update transaction info
                            progressBar.setTitle(xerb.getString("datawizard.fileuploadinfo"));
                            progressBar.show();
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
                            ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).setAttrib_varchar(current_filename);
                            ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(1)).setAttrib_unsignedint(total_len);
                            ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(2)).setAttrib_varchar("" + ((CheckedInputStream) in).getChecksum().getValue());
                            if (!useSAAJ) {
                                in.close();
                            }
                        } catch (Exception fe) {
                            progressBar.hide();
                            throw new XincoException(xerb.getString("datawizard.unabletoloadfile"));
                        }
                    }
                    //save data to server
                    if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) && (wizard_type != 14) && (wizard_type != 15)) {
                        if ((wizard_type >= 4) && (wizard_type <= 6)) {
                            if (wizard_type == 4) {
                                xdata = xincoClientSession.getXinco().doXincoCoreDataCheckout((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser());
                            } else {
                                if (wizard_type == 5) {
                                    xdata = xincoClientSession.getXinco().undoXincoCoreDataCheckout((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser());
                                } else {
                                    xdata = xincoClientSession.getXinco().doXincoCoreDataCheckin((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser());
                                }
                            }
                        } else {
                            xdata = xincoClientSession.getXinco().setXincoCoreData((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser());
                        }
                        if (xdata == null) {
                            throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
                        }
                        newnode.setUserObject(xdata);
                    }
                    if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) && (wizard_type != 14) && (wizard_type != 15)) {
                        //update id cin log
                        newlog.setXinco_core_data_id(((XincoCoreData) newnode.getUserObject()).getId());
                        //save log to server
                        newlog = xincoClientSession.getXinco().setXincoCoreLog(newlog, xincoClientSession.getUser());
                        if (newlog == null) {
                        } else {
                            ((XincoCoreData) newnode.getUserObject()).getXinco_core_logs().addElement(newlog);
                        }
                    }
                    //upload file (new / checkin)
                    //file = 1
                    if (((wizard_type == 1) || (wizard_type == 6)) && (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 1)) {
                        //attach file to SOAP message
                        if (useSAAJ) {
                            AttachmentPart ap = null;
                            ap = new AttachmentPart();
                            ap.setContent(in, "unknown/unknown");
                            ((XincoSoapBindingStub) xincoClientSession.getXinco()).addAttachment(ap);
                        }

                        if (xincoClientSession.getXinco().uploadXincoCoreData((XincoCoreData) newnode.getUserObject(), byte_array, xincoClientSession.getUser()) != total_len) {
                            ((XincoSoapBindingStub) xincoClientSession.getXinco()).clearAttachments();
                            in.close();
                            JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.fileuploadfailed"), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                        }
                        ((XincoSoapBindingStub) xincoClientSession.getXinco()).clearAttachments();
                        in.close();
                        //update transaction info
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.fileuploadsuccess"));
                        progressBar.hide();
                    }
                    //download file
                    //file = 1
                    if (((wizard_type == 4) || (wizard_type == 7) || (wizard_type == 11)
                            || (wizard_type == 14) || (wizard_type == 15))
                            && (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 1)) {
                        //determine requested revision and set log vector
                        if (wizard_type != 15) {
                            progressBar.setTitle(xerb.getString("datawizard.filedownloadinfo"));
                            progressBar.show();
                        }
                        Vector DataLogVector = null;
                        if (wizard_type == 11) {
                            jDialogRevision = getJDialogRevision();
                            setGlobal_dialog_return_value(-1);
                            jDialogRevision.setVisible(true);
                            if (getGlobal_dialog_return_value() == -1) {
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            DataLogVector = ((XincoCoreData) newnode.getUserObject()).getXinco_core_logs();
                            XincoCoreLog RevLog = null;
                            for (i = 0; i < ((XincoCoreData) newnode.getUserObject()).getXinco_core_logs().size(); i++) {
                                if (((XincoCoreLog) ((XincoCoreData) newnode.getUserObject()).getXinco_core_logs().elementAt(i)).getId() == getGlobal_dialog_return_value()) {
                                    RevLog = (XincoCoreLog) ((XincoCoreData) newnode.getUserObject()).getXinco_core_logs().elementAt(i);
                                    break;
                                }
                            }
                            Vector RevLogVector = new Vector();
                            RevLogVector.add(RevLog);
                            ((XincoCoreData) newnode.getUserObject()).setXinco_core_logs(RevLogVector);
                        }

                        //update transaction info
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.filedownloadinfo"));
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
                            byte_array = (byte[]) call.invoke(objp);
                            //get file from SOAP message or byte array
                            mc = call.getMessageContext();
                            m = mc.getResponseMessage();
                            if (m.getAttachments().hasNext()) {
                                ap = (AttachmentPart) m.getAttachments().next();
                                in = (InputStream) ap.getContent();
                            } else {
                                in = new ByteArrayInputStream(byte_array);
                            }
                        } catch (Exception ce) {
                            //reassign log vector
                            if (wizard_type == 11) {
                                ((XincoCoreData) newnode.getUserObject()).setXinco_core_logs(DataLogVector);
                            }
                            JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.filedownloadfailed"), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                            this.progressBar.hide();
                            throw (ce);
                        }

                        //reassign log vector
                        if (wizard_type == 11) {
                            ((XincoCoreData) newnode.getUserObject()).setXinco_core_logs(DataLogVector);
                        }

                        //ByteArrayInputStream cin = new ByteArrayInputStream(byte_array);
                        CheckedOutputStream couts = new CheckedOutputStream(new FileOutputStream(current_fullpath), new CRC32());
                        byte[] buf = new byte[4096];
                        int len = 0;
                        total_len = 0;
                        while ((len = in.read(buf)) > 0) {
                            couts.write(buf, 0, len);
                            total_len = total_len + len;
                        }
                        in.close();
                        ((XincoSoapBindingStub) xincoClientSession.getXinco()).clearAttachments();
                        //check correctness of data
                        if (wizard_type != 11) {
                            //if ((((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(1)).getAttrib_unsignedint() != total_len) || (((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(2)).getAttrib_varchar().equals(new String("" + couts.getChecksum().getValue())))) {
                            if (((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(1)).getAttrib_unsignedint() != total_len) {
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
                        //open file cin default application
                        if (wizard_type != 15) {
                            Process process = null;
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
                                    } catch (Throwable t) {
                                    }
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
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                        if (wizard_type != 15) {
                            progressBar.hide();
                        }
                    }
                    //Open cin Browser
                    //URL = 3
                    if ((wizard_type == 8) && (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 3)) {
                        //open URL cin default browser
                        Process process = null;
                        String temp_url = ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_varchar();
                        if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                            try {
                                String[] cmd = {"open", temp_url};
                                process = Runtime.getRuntime().exec(cmd);
                            } catch (Throwable t) {
                            }
                        } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                            try {
                                String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + temp_url + "\"";
                                process = Runtime.getRuntime().exec(cmd);
                            } catch (Throwable t) {
                            }
                        }
                    }
                    //Open cin Email Client
                    //contact = 4
                    if ((wizard_type == 9) && (((XincoCoreData) newnode.getUserObject()).getXinco_core_data_type().getId() == 4)) {
                        //open URL cin default browser
                        Process process = null;
                        String temp_email = ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXinco_add_attributes().elementAt(9)).getAttrib_varchar();
                        if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                            try {
                                String[] cmd = {"open", "mailto:" + temp_email};
                                process = Runtime.getRuntime().exec(cmd);
                            } catch (Throwable t) {
                            }
                        } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                            try {
                                String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + "mailto:" + temp_email + "\"";
                                process = Runtime.getRuntime().exec(cmd);
                            } catch (Throwable t) {
                            }
                        }
                    }

                    if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) && (wizard_type != 14) && (wizard_type != 15)) {
                        //update treemodel
                        xincoClientSession.getXincoClientRepository().treemodel.reload(newnode);
                        xincoClientSession.getXincoClientRepository().treemodel.nodeChanged(newnode);
                        jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.updatesuccess"));
                        if (wizard_type == 10) {
                            String temp_url = "";
                            //file = 1
                            if (xdata.getXinco_core_data_type().getId() == 1) {
                                temp_url = ((XincoAddAttribute) xdata.getXinco_add_attributes().elementAt(0)).getAttrib_varchar();
                            } else {
                                temp_url = xdata.getDesignation();
                            }
                            jLabelInternalFrameInformationText.setText(xerb.getString("datawizard.updatesuccess.publisherinfo") + "\nhttp://[server_name]:[port]/xinco/XincoPublisher/" + xdata.getId() + "/" + temp_url);
                        }
                        jTreeRepository.setSelectionPath(new TreePath(((XincoMutableTreeNode) xincoClientSession.getCurrentTreeNodeSelection().getParent()).getPath()));
                        jTreeRepository.setSelectionPath(new TreePath(xincoClientSession.getCurrentTreeNodeSelection().getPath()));
                    }

                }
            } catch (Exception we) {
                //update transaction info
                jLabelInternalFrameInformationText.setText("");
                //remove new data cin case off error
                if (wizard_type == 1) {
                    xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) xincoClientSession.getCurrentTreeNodeSelection().getParent());
                    xincoClientSession.getXincoClientRepository().treemodel.removeNodeFromParent(newnode);
                    jTreeRepository.setSelectionPath(new TreePath(xincoClientSession.getCurrentTreeNodeSelection().getPath()));
                }
                if (wizard_type != 3 || getGlobal_dialog_return_value() != 0) {
                    JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.updatefailed")
                            + " " + xerb.getString("general.reason") + ": "
                            + we.toString(), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                }
                this.progressBar.hide();
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
                previous_fullpath = s;
                i = s.lastIndexOf(System.getProperty("file.separator"));
                /*j = s.lastIndexOf("\\");
                //select i as index wanted
                if (j>i) {
                i = j;
                }*/
                previous_filename = s.substring(i + 1);
                if (i > 0) {
                    previous_path = s.substring(0, i + 1);
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
     * @param path String representing path
     */
    public void setCurrentPath(String path) {
        if (!(path.substring(path.length() - 1).equals(System.getProperty("file.separator")))) {
            path = path + System.getProperty("file.separator");
        }
        current_filename = "";
        current_path = path;
        current_fullpath = path;
    }

    /**
     * This method initializes AbstractDialogUser
     *
     * @return AbstractDialog
     */
    private void getAbstractDialogUser(boolean aged) {
        if (userDialog == null) {
            userDialog = new UserDialog(null, true, this, aged);
            addDialog(userDialog);
        }
        userDialog.setVisible(true);
    }

    /**
     * This method initializes AbstractDialogAddAttributesText
     *
     * @return AbstractDialog
     */
    public AbstractDialog getAbstractDialogAddAttributesText() {
        if (AbstractDialogAddAttributesText == null) {
            AbstractDialogAddAttributesText = new AddAttributeText(null, true, false, this);
            addDialog(AbstractDialogAddAttributesText);
        }
        //Issue #2972311
        AbstractDialogAddAttributesText.setVisible(true);
        return AbstractDialogAddAttributesText;
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
                    ((XincoAddAttribute) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXinco_add_attributes().elementAt(0)).setAttrib_text(jTextAreaDialogAddAttributesText.getText());
                    setGlobal_dialog_return_value(1);
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
            java.io.FileOutputStream fout =
                    new java.io.FileOutputStream(System.getProperty("user.home")
                    + System.getProperty("file.separator") + "xincoClientConfig.dat");
            java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(fout);
            os.writeObject(xincoClientConfig);
            os.close();
            fout.close();
        } catch (Exception ioe) {
            //error handling
            System.out.println("Unable to write Profiles-File!");
        }
    }

    /**
     * This method loads the configuration
     *
     * @return void
     */
    @SuppressWarnings("unchecked")
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
                    while ((tmp_vector = (Vector) ois.readObject()) != null) {
                        tmp_vector_old = tmp_vector;
                    }
                } catch (Exception ioe3) {
                }
                ois.close();
                fin.close();
            } catch (Exception ioe2) {
                tmp_vector_old = null;
            }
            fin = new FileInputStream(System.getProperty("user.home") + System.getProperty("file.separator") + "xincoClientConfig.dat");
            ois = new ObjectInputStream(fin);
            try {
                while ((tmp_vector = (Vector) ois.readObject()) != null) {
                    xincoClientConfig = tmp_vector;
                }
            } catch (Exception ioe3) {
            }
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
                createDefaultConfiguration(true);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void createDefaultConfiguration(boolean modifyProfiles) {
        xincoClientConfig = new Vector();
        xincoClientConfig.add(new Vector());
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
        //Issue #2972311
        AbstractDialogArchive.setVisible(true);
        return AbstractDialogArchive;
    }

    public void set_global_dialog_return_value(int v) {
        this.setGlobal_dialog_return_value(v);
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
     * Refresh JTree
     */
    public void refreshJTree() {
        rThread = null;
        rThread = new refreshThread();
        rThread.start();
    }

    /**
     *
     * @return XincoProgressBarThread
     */
    public XincoProgressBarThread getProgressBar() {
        return progressBar;
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
                getProgressBar().hide();
                getJTreeRepository().setEnabled(true);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addDialog(AbstractDialog dialog) {
        getDialogs().add(dialog);
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
        if (isLock()) {
            getAbstractDialogLock();
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
     * @param xincoCoreUser
     */
    public void setTemp(XincoCoreUser xincoCoreUser) {
        temp = xincoCoreUser;
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
}
