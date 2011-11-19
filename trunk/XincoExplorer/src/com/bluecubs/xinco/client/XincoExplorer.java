/**
 * Copyright 2011 blueCubs.com
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
 * Name: XincoExplorer
 *
 * Description: client of the xinco DMS
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who? When? What? Javier Ortiz Aug-Dec 2006 1. Remove dialogs and windows from
 * main code 2. Incorporate 21 CFR regulatory features
 *
 *************************************************************
 */
package com.bluecubs.xinco.client;

import com.bluecubs.xinco.client.dialogs.*;
import com.bluecubs.xinco.client.object.*;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.client.object.menu.XincoMenuRepository;
import com.bluecubs.xinco.client.object.menu.XincoPopUpMenuRepository;
import com.bluecubs.xinco.client.object.thread.XincoProgressBarThread;
import com.bluecubs.xinco.client.object.timer.XincoActivityTimer;
import com.bluecubs.xinco.client.service.*;
import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.XincoException;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * XincoExplorer
 */
public final class XincoExplorer extends JFrame implements ActionListener, MouseListener {

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
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleJTatoo = null;
    private javax.swing.ButtonGroup bgwindowstyle;
    //client version
    private XincoVersion xincoClientVersion = null;
    //session object
    private XincoClientSession xincoClientSession = null;
    //connection profiles
    private ArrayList xincoClientConfig = null;
    //current path and filename
    private String current_filename = "";
    private String currentPath = "";
    private String current_fullpath = "";
    //previous path and filename
    private String previous_filename = "";
    private String previousPath = "";
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
    private AbstractDialog AbstractDialogAddAttributesText = null;
    private javax.swing.JPanel jContentPaneInformation = null;
    private javax.swing.JTextArea jLabelInternalFrameInformationText = null;
    private XincoPopUpMenuRepository jPopupMenuRepository = null;
    private JPanel jContentPaneDialogLocale = null;
    private JDialog JDialogLocale = null;
    private JScrollPane jScrollPaneDialogLocale = null;
    private JList jListDialogLocale = null;
    private JButton jButtonDialogLocaleOk = null;
    private XincoProgressBarThread progressBar;
    private ConnectionDialog dialogConnection = null;
    private UserDialog userDialog = null;
    private JInternalFrame jInternalFrameInformation = null;
    private JMenuItem jMenuItemConnectionExit = null;
    private String status_string_1 = "", status_string_2 = "";
    private XincoCoreUser temp;
    private final XincoCoreUser newuser = new XincoCoreUser();
    private loginThread loginT;
    private int wizardType;
    private XincoMutableTreeNode newnode, previousnode;
    private byte[] byteArray;
    private XincoCoreData xdata;
    private XincoCoreLog newlog;
    private InputStream in = null;
    private final XincoExplorer explorer = this;
    private SearchDialog search;
    private XincoRepositoryActionHandler actionHandler = null;
    //Size of menu actions
    private int actionSize = 19;
    private XincoExplorer.RefreshThread rThread;
    private ArrayList<AbstractDialog> dialogs = null;
    private LockDialog lockDialog = null;
    //Status of the explorer: lock = true - idle time limit exceeded, user must log in again to continue use
    //lock = false - work normally
    private boolean lock = false;
    private XincoActivityTimer xat = null;
    public static final int ConfigFileVersion = 1;
    private boolean recoverFromBackup = false;
    public static final String CONFIG_NAME = "xincoClientConfig.dat";

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
            switchPLAF((String) xincoClientConfig.get(ConfigElement.LAF.ordinal()));
        } catch (Throwable e) {
            createDefaultConfiguration(true);
            switchPLAF((String) xincoClientConfig.get(ConfigElement.LAF.ordinal()));
        }
        //choose language
        getJDialogLocale().setVisible(true);
        Locale loc;
        try {
            String list = ((Locale) xincoClientConfig.get(ConfigElement.LOCALE.ordinal())).toString();
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
            setInternalFrameInformationText(new JTextArea());
            getjInternalFrameInformationText().setBounds(5, 5, 380, 130);
            getjInternalFrameInformationText().setAutoscrolls(true);
            getjInternalFrameInformationText().setLineWrap(true);
            getjInternalFrameInformationText().setWrapStyleWord(true);
            getjInternalFrameInformationText().setEditable(false);
            jContentPaneInformation = new JPanel();
            jContentPaneInformation.setLayout(null);
            getjInternalFrameInformationText().setText("");
            jContentPaneInformation.add(getjInternalFrameInformationText(), null);
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
        int i;
        ResourceBundle lrb;
        String[] locales;
        String text = "";
        int selection;
        int alt_selection;
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
            if ((locales[i].compareTo(((Locale) xincoClientConfig.get(ConfigElement.LOCALE.ordinal())).toString()) == 0)
                    || (locales[i].compareTo(((Locale) xincoClientConfig.get(ConfigElement.LOCALE.ordinal())).getLanguage()) == 0)) {
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
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //get locale
                    if (jListDialogLocale.getSelectedIndex() >= 0) {
                        ResourceBundle lrb;
                        String[] locales;
                        lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
                        locales = lrb.getString("AvailableLocales").split(",");
                        xincoClientConfig.remove(ConfigElement.LOCALE.ordinal());
                        xincoClientConfig.add(ConfigElement.LOCALE.ordinal(), new Locale(locales[jListDialogLocale.getSelectedIndex()]));
                        saveConfig();
                        JDialogLocale.setVisible(false);
                    }
                }
            });
        }
        return jButtonDialogLocaleOk;
    }

    public static void main(String[] args) {
        new XincoExplorer();
    }

    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        if (show) {
            showConnectionDialog();
        }
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
        xincoClientVersion.setVersionHigh(Integer.parseInt(settings.getString("version.high")));
        xincoClientVersion.setVersionMid(Integer.parseInt(settings.getString("version.mid")));
        xincoClientVersion.setVersionLow(Integer.parseInt(settings.getString("version.low")));
        xincoClientVersion.setVersionPostfix(settings.getString("version.postfix"));
        switchPLAF((String) xincoClientConfig.get(ConfigElement.LAF.ordinal()));
        this.setBounds(0, 0, (new Double(getToolkit().getScreenSize().getWidth())).intValue() - 100,
                (new Double(getToolkit().getScreenSize().getHeight())).intValue() - 75);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setName("XincoExplorer");
        this.setTitle(xerb.getString("general.clienttitle") + " - "
                + xerb.getString("general.version") + " "
                + xincoClientVersion.getVersionHigh() + "."
                + xincoClientVersion.getVersionMid() + "."
                + xincoClientVersion.getVersionLow() + " "
                + xincoClientVersion.getVersionPostfix());
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

                @Override
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

                @Override
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

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String message_string = "";
                    message_string = message_string + xerb.getString("window.aboutxinco.clienttitle") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.clientversion") + ": "
                            + xincoClientVersion.getVersionHigh()
                            + "." + xincoClientVersion.getVersionMid() + "."
                            + xincoClientVersion.getVersionLow() + " "
                            + xincoClientVersion.getVersionPostfix() + "\n";
                    message_string += "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.partof") + ":\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.softwaretitle") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.softwaresubtitle") + "\n";
                    message_string += "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.vision1") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.vision2") + "\n";
                    message_string += "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.moreinfo") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.xinco_org") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.bluecubsCom") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.bluecubs_org") + "\n";
                    message_string += "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.thanks") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.apache") + "\n";
                    message_string += "\n";
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
        if (getjTreeRepository() == null) {
            jTreeRepository = new XincoJTree(this);
            getjTreeRepository().setModel(
                    xincoClientSession.getXincoClientRepository().treemodel);
            //enable tool tips
            ToolTipManager.sharedInstance().registerComponent(getjTreeRepository());
            getjTreeRepository().setCellRenderer(new XincoTreeCellRenderer(this));
            getjTreeRepository().setRootVisible(true);
            getjTreeRepository().setEditable(false);
            DefaultTreeSelectionModel dtsm = new DefaultTreeSelectionModel();
            dtsm.setSelectionMode(
                    TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            getjTreeRepository().setSelectionModel(dtsm);
        }
        return getjTreeRepository();
    }

    private void expandAllNodes() {
        getJTreeRepository();
        int row = 0;
        while (row < getjTreeRepository().getRowCount()) {
            getjTreeRepository().expandRow(row);
            row++;
        }
    }

    public void collapseAllNodes() {
        getJTreeRepository();
        int row = getjTreeRepository().getRowCount() - 1;
        while (row >= 0) {
            getjTreeRepository().collapseRow(row);
            row--;
        }
        getjTreeRepository().expandRow(0);
    }

    /**
     * This method initializes jTableRepository
     *
     * @return javax.swing.JTable
     */
    public javax.swing.JTable getJTableRepository() {
        if (jTableRepository == null) {
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

                @Override
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
            if (((String) xincoClientConfig.get(ConfigElement.LAF.ordinal())).equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) {
                jRadioButtonMenuItemViewStyleWindows.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleWindows.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleWindows.setText(xerb.getString("menu.view.windows"));
            jRadioButtonMenuItemViewStyleWindows.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                @Override
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    xincoClientConfig.add(ConfigElement.LAF.ordinal(), "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
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
            if (((String) xincoClientConfig.get(ConfigElement.LAF.ordinal())).equals("net.sourceforge.napkinlaf.NapkinLookAndFeel")) {
                jRadioButtonMenuItemViewStyleNapkin.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleNapkin.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleNapkin.setText(xerb.getString("menu.view.napkin"));
            jRadioButtonMenuItemViewStyleNapkin.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                @Override
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("net.sourceforge.napkinlaf.NapkinLookAndFeel");
                    xincoClientConfig.add(ConfigElement.LAF.ordinal(), "net.sourceforge.napkinlaf.NapkinLookAndFeel");
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
            if (((String) xincoClientConfig.get(ConfigElement.LAF.ordinal())).equals("com.birosoft.liquid.LiquidLookAndFeel")) {
                jRadioButtonMenuItemViewStyleLiquid.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleLiquid.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleLiquid.setText(xerb.getString("menu.view.liquid"));
            jRadioButtonMenuItemViewStyleLiquid.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                @Override
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("com.birosoft.liquid.LiquidLookAndFeel");
                    xincoClientConfig.add(ConfigElement.LAF.ordinal(), "com.birosoft.liquid.LiquidLookAndFeel");
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
            if (((String) xincoClientConfig.get(ConfigElement.LAF.ordinal())).equals("com.jtattoo.plaf.smart.SmartLookAndFeel")) {
                jRadioButtonMenuItemViewStyleJTatoo.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleJTatoo.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleJTatoo.setText(xerb.getString("menu.view.jtatoo"));
            jRadioButtonMenuItemViewStyleJTatoo.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                @Override
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("com.jtattoo.plaf.smart.SmartLookAndFeel");
                    xincoClientConfig.add(ConfigElement.LAF.ordinal(), "com.jtattoo.plaf.smart.SmartLookAndFeel");
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
            if (((String) xincoClientConfig.get(ConfigElement.LAF.ordinal())).equals("org.jvnet.substance.SubstanceLookAndFeel")) {
                jRadioButtonMenuItemViewStyleSubstance.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleSubstance.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleSubstance.setText(xerb.getString("menu.view.substance"));
            jRadioButtonMenuItemViewStyleSubstance.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                @Override
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("org.jvnet.substance.SubstanceLookAndFeel");
                    xincoClientConfig.add(ConfigElement.LAF.ordinal(), "org.jvnet.substance.SubstanceLookAndFeel");
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
            if (((String) xincoClientConfig.get(ConfigElement.LAF.ordinal())).equals("javax.swing.plaf.metal.MetalLookAndFeel")) {
                jRadioButtonMenuItemViewStyleJava.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleJava.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleJava.setText(xerb.getString("menu.view.java"));
            jRadioButtonMenuItemViewStyleJava.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                @Override
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("javax.swing.plaf.metal.MetalLookAndFeel");
                    xincoClientConfig.add(ConfigElement.LAF.ordinal(), "javax.swing.plaf.metal.MetalLookAndFeel");
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
            if (((String) xincoClientConfig.get(ConfigElement.LAF.ordinal())).equals("com.sun.java.swing.plaf.motif.MotifLookAndFeel")) {
                jRadioButtonMenuItemViewStyleMotif.setSelected(true);
            } else {
                jRadioButtonMenuItemViewStyleMotif.setSelected(false);
            }
            jRadioButtonMenuItemViewStyleMotif.setText(xerb.getString("menu.view.motif"));
            jRadioButtonMenuItemViewStyleMotif.addItemListener(new java.awt.event.ItemListener() {

                @SuppressWarnings("unchecked")
                @Override
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    switchPLAF("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    xincoClientConfig.add(ConfigElement.LAF.ordinal(), "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    saveConfig();
                }
            });
        }
        return jRadioButtonMenuItemViewStyleMotif;
    }

    private void showConnectionDialog() {
        int i;
        //init session
        xincoClientSession = new XincoClientSession(XincoExplorer.this);
        getJTreeRepository().setModel(xincoClientSession.getXincoClientRepository().treemodel);
        xincoClientSession.setStatus(0);
        //open connection dialog
        getAbstractDialogConnection();
        DefaultListModel dlm = (DefaultListModel) dialogConnection.getProfileList().getModel();
        dlm.removeAllElements();
        for (i = 0; i < ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size(); i++) {
            dlm.addElement(((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(i)).toString());
        }
        //establish connection and login
        if (xincoClientSession.getStatus() == 1) {
            try {
                xincoClientSession.setXincoService(new Xinco_Service(new java.net.URL(xincoClientSession.getServiceEndpoint())));
                xincoClientSession.setXinco(xincoClientSession.getXincoService().getXincoPort());
                xincoClientSession.setServerVersion(xincoClientSession.getXinco().getXincoServerVersion());
                //check if client and server versions match (high AND mid must match!)
                if ((xincoClientVersion.getVersionHigh() != xincoClientSession.getServerVersion().getVersionHigh()) || (xincoClientVersion.getVersionMid() != xincoClientSession.getServerVersion().getVersionMid())) {
                    //Let the user know!
                    String mess = xerb.getString("menu.connection.error.serverversion") + " " + xincoClientSession.getServerVersion().getVersionHigh() + "." + xincoClientSession.getServerVersion().getVersionMid() + ".x";
                    mess = mess.replaceAll("%v", xincoClientVersion.getVersionHigh() + "." + xincoClientVersion.getVersionMid() + "." + xincoClientVersion.getVersionLow());
                    JOptionPane.showMessageDialog(XincoExplorer.this, mess, mess, JOptionPane.WARNING_MESSAGE);
                    throw new XincoException(mess);
                }
                try {
                    temp = xincoClientSession.getXinco().getCurrentXincoCoreUser(xincoClientSession.getUser().getUsername(), xincoClientSession.getUser().getUserpassword());
                } catch (Exception ex) {
                    throw new Exception(xerb.getString("menu.connection.error.user"));
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
                if (getProgressBar().isInitialized()) {
                    getProgressBar().show();
                } else {
                    getProgressBar().start();
                }
                xincoClientSession.setServerDatatypes(xincoClientSession.getXinco().getAllXincoCoreDataTypes(xincoClientSession.getUser()));
                xincoClientSession.setServerGroups(xincoClientSession.getXinco().getAllXincoCoreGroups(xincoClientSession.getUser()));
                xincoClientSession.setServerLanguages(xincoClientSession.getXinco().getAllXincoCoreLanguages(xincoClientSession.getUser()));
                for (i = 0; i < xincoClientSession.getUser().getXincoCoreGroups().size(); i++) {
                    String label = ((XincoCoreGroup) xincoClientSession.getUser().getXincoCoreGroups().get(i)).getDesignation();
                    try {
                        label = explorer.getResourceBundle().getString(label);
                    } catch (java.util.MissingResourceException ex) {
                        //Nothing to translate
                    }
                    status_string_1 = status_string_1 + "      + " + label + "\n";
                }
                for (i = 0; i < xincoClientSession.getServerDatatypes().size(); i++) {
                    status_string_2 += "      + " + xerb.getString(((XincoCoreDataType) xincoClientSession.getServerDatatypes().get(i)).getDesignation()) + "\n";
                }
                loginT = new loginThread();
                loginT.start();
            } catch (Exception cone) {
                Logger.getLogger(
                        ConnectionDialog.class.getSimpleName()).log(
                        Level.SEVERE, "Connection error", cone);
                xincoClientSession.setStatus(0);
                markConnectionStatus();
                JOptionPane.showMessageDialog(XincoExplorer.this,
                        xerb.getString("menu.connection.failed")
                        + " " + xerb.getString("general.reason") + ": "
                        + cone.toString(),
                        xerb.getString("menu.connection.failed"),
                        JOptionPane.WARNING_MESSAGE);
            }
        }
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

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showConnectionDialog();
                }
            });
        }
        return jMenuItemConnectionConnect;
    }

    public XincoCoreUser getUser() {
        return this.newuser;
    }

    /**
     * @return the globalDialog_returnValue
     */
    public int getGlobalDialog_returnValue() {
        return globalDialog_returnValue;
    }

    /**
     * @param globalDialog_returnValue the globalDialog_returnValue to set
     */
    public void setGlobalDialogReturnValue(int globalDialog_returnValue) {
        this.globalDialog_returnValue = globalDialog_returnValue;
    }

    /**
     * @param lock the lock to set
     */
    public void setLock(boolean lock) {
        this.lock = lock;
    }

    /**
     * @return the currentPath
     */
    public String getCurrentPath() {
        return currentPath;
    }

    /**
     * @return the jLabelInternalFrameInformationText
     */
    public javax.swing.JTextArea getjInternalFrameInformationText() {
        return jLabelInternalFrameInformationText;
    }

    /**
     * @param jLabelInternalFrameInformationText the
     * jLabelInternalFrameInformationText to set
     */
    public void setInternalFrameInformationText(javax.swing.JTextArea jLabelInternalFrameInformationText) {
        this.jLabelInternalFrameInformationText = jLabelInternalFrameInformationText;
    }

    /**
     * @return the jTreeRepository
     */
    public XincoJTree getjTreeRepository() {
        return jTreeRepository;
    }

    /**
     * @return the current_filename
     */
    public String getCurrentFileName() {
        return current_filename;
    }

    /**
     * @param current_filename the current_filename to set
     */
    private void setCurrentFilename(String current_filename) {
        this.current_filename = current_filename;
    }

    /**
     * @return the current_fullpath
     */
    public String getCurrentFullPath() {
        return current_fullpath;
    }

    /**
     * @param current_fullpath the current_fullpath to set
     */
    public void setCurrentFullPath(String current_fullpath) {
        this.current_fullpath = current_fullpath;
    }

    private class loginThread extends Thread {

        @Override
        public void run() {
            try {
                StringBuilder status_string = new StringBuilder();
                temp = xincoClientSession.getXinco().getCurrentXincoCoreUser(xincoClientSession.getUser().getUsername(), xincoClientSession.getUser().getUserpassword());
                status_string.append(xerb.getString("menu.connection.connectedto")).append(": ").append(xincoClientSession.getServiceEndpoint()).append("\n");
                status_string.append(xerb.getString("general.serverversion")).append(": ");
                status_string.append(xincoClientSession.getServerVersion().getVersionHigh()).append(".");
                status_string.append(xincoClientSession.getServerVersion().getVersionMid()).append(".");
                status_string.append(xincoClientSession.getServerVersion().getVersionLow()).append(" ");
                status_string.append(xincoClientSession.getServerVersion().getVersionPostfix()).append("\n");
                status_string.append("\n");
                status_string.append(xerb.getString("general.user")).append(": ").append(xincoClientSession.getUser().getFirstname()).append(" ").append(xincoClientSession.getUser().getName()).append(" <").append(xincoClientSession.getUser().getEmail()).append(">\n");
                status_string.append(xerb.getString("general.memberof")).append(":\n");
                status_string.append(status_string_1).append("\n");
                status_string.append(xerb.getString("general.groupsonserver")).append(": ").append(xincoClientSession.getServerGroups().size()).append("\n");
                status_string.append(xerb.getString("general.languagesonserver")).append(": ").append(xincoClientSession.getServerLanguages().size()).append("\n");
                status_string.append(xerb.getString("general.datatypesonserver")).append(": ").append(xincoClientSession.getServerDatatypes().size()).append("\n");
                status_string.append(status_string_2).append("\n");
                xincoClientSession.setCurrentSearchResult(new ArrayList());
                xincoClientSession.setStatus(2);
                JOptionPane.showMessageDialog(XincoExplorer.this, status_string.toString(), xerb.getString("menu.connection.established"), JOptionPane.INFORMATION_MESSAGE);
                getjInternalFrameInformationText().setText(xerb.getString("menu.connection.established"));
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
                    getjInternalFrameInformationText().setText(xerb.getString("password.aged"));
                    getAbstractDialogUser(true);
                }
                getProgressBar().hide();
            } catch (Exception cone) {
                xincoClientSession.setStatus(0);
                markConnectionStatus();
                JOptionPane.showMessageDialog(XincoExplorer.this,
                        xerb.getString("menu.connection.failed") + " "
                        + xerb.getString("general.reason") + ": "
                        + cone.toString(),
                        xerb.getString("menu.connection.failed"),
                        JOptionPane.WARNING_MESSAGE);
                getProgressBar().hide();
            }
        }

        public void resetStrings() {
            status_string_1 = "";
            status_string_2 = "";
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

    public ArrayList getConfig() {
        return xincoClientConfig;
    }

    /**
     * This method marks menus, etc. according to connection status
     *
     */
    public void markConnectionStatus() {
        int i, j;
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
            xincoClientSession.setClipboardTreeNodeSelection(new ArrayList());
            xincoClientSession.setCurrentSearchResult(new ArrayList());
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
        int i;
        String text;
        if (xincoClientSession.getCurrentTreeNodeSelection().getUserObject() != null) {
            DefaultListModel dlm = (DefaultListModel) jListDialogRevision.getModel();
            dlm.removeAllElements();
            Calendar cal;
            XMLGregorianCalendar realcal;
            Calendar ngc = new GregorianCalendar();
            for (i = 0; i < ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().size(); i++) {
                if ((((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getOpCode() == 1)
                        || (((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getOpCode() == 5)) {
                    //convert clone from remote time to local time
                    cal = (Calendar) ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getOpDatetime().clone();
                    realcal = (((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getOpDatetime());
                    cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET)
                            - realcal.toGregorianCalendar().get(Calendar.ZONE_OFFSET))
                            - (ngc.get(Calendar.DST_OFFSET)
                            + realcal.toGregorianCalendar().get(Calendar.DST_OFFSET)));
                    text = "" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH);
                    text = text + " - " + xerb.getString("general.version") + " " + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getVersion().getVersionHigh()
                            + "." + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getVersion().getVersionMid() + "."
                            + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getVersion().getVersionLow() + " "
                            + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getVersion().getVersionPostfix();
                    text = text + " - " + ((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getOpDescription();

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

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int i;
                    int RealLogIndex = -1;
                    if (jListDialogRevision.getSelectedIndex() >= 0) {
                        for (i = 0; i < ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().size(); i++) {
                            if ((((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getOpCode() == 1) || (((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getOpCode() == 5)) {
                                RealLogIndex++;
                            }
                            if (RealLogIndex == jListDialogRevision.getSelectedIndex()) {
                                setGlobalDialogReturnValue(((XincoCoreLog) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoCoreLogs().get(i)).getId());
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

                @Override
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
        int i;
        int j;
        File[] folderList;
        folderList = folder.listFiles();
        newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
        XincoCoreNode xnode;
        newlog = new XincoCoreLog();
        XincoCoreDataType xcdt1 = null;
        //find data type = 1
        for (j = 0; j < xincoClientSession.getServerDatatypes().size(); j++) {
            if (((XincoCoreDataType) xincoClientSession.getServerDatatypes().get(j)).getId() == 1) {
                xcdt1 = (XincoCoreDataType) xincoClientSession.getServerDatatypes().get(j);
                break;
            }
        }
        //find default language
        XincoCoreLanguage xcl1;
        int selection = -1;
        int alt_selection = 0;
        for (j = 0; j < xincoClientSession.getServerLanguages().size(); j++) {
            if (((XincoCoreLanguage) xincoClientSession.getServerLanguages().get(j)).getSign().toLowerCase().compareTo(Locale.getDefault().getLanguage().toLowerCase()) == 0) {
                selection = j;
                break;
            }
            if (((XincoCoreLanguage) xincoClientSession.getServerLanguages().get(j)).getId() == 1) {
                alt_selection = j;
            }
        }
        if (selection == -1) {
            selection = alt_selection;
        }
        xcl1 = (XincoCoreLanguage) xincoClientSession.getServerLanguages().get(selection);
        //process files
        getProgressBar().setTitle(xerb.getString("datawizard.fileuploadinfo"));
        getProgressBar().show();
        for (i = 0; i < folderList.length; i++) {
            if (folderList[i].isFile()) {
                // set current node to new one
                newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
                // set data attributes
                ((XincoCoreData) newnode.getUserObject()).setXincoCoreNodeId(node.getId());
                ((XincoCoreData) newnode.getUserObject()).setDesignation(folderList[i].getName());
                ((XincoCoreData) newnode.getUserObject()).setXincoCoreDataType(xcdt1);
                ((XincoCoreData) newnode.getUserObject()).setXincoCoreLanguage(xcl1);
                ((XincoCoreData) newnode.getUserObject()).setStatusNumber(1);
                xincoClientSession.getXincoClientRepository().treemodel.insertNodeInto(newnode,
                        xincoClientSession.getCurrentTreeNodeSelection(), xincoClientSession.getCurrentTreeNodeSelection().getChildCount());
                xincoClientSession.setCurrentTreeNodeSelection(newnode);
                // add specific attributes
                XincoAddAttribute xaa;

                for (j = 0; j
                        < ((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getXincoCoreDataTypeAttributes().size(); j++) {
                    xaa = new XincoAddAttribute();
                    // xaa.setAttributeId(j+1); // bug => attributeIds might be missing cin between
                    xaa.setAttributeId(((XincoCoreDataTypeAttribute) xcdt1.getXincoCoreDataTypeAttributes().get(j)).getAttributeId());
                    xaa.setAttribVarchar("");
                    xaa.setAttribText("");
                    ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().add(xaa);
                }
                // add log
                newlog = new XincoCoreLog();
                newlog.setOpCode(OPCode.CREATION.ordinal() + 1);
                newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName())
                        + "!" + " ("
                        + xerb.getString("general.user") + ": "
                        + xincoClientSession.getUser().getUsername()
                        + ")");
                newlog.setXincoCoreUserId(xincoClientSession.getUser().getId());
                newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId());
                newlog.setVersion(new XincoVersion());
                newlog.getVersion().setVersionHigh(1);
                newlog.getVersion().setVersionMid(0);
                newlog.getVersion().setVersionLow(0);
                newlog.getVersion().setVersionPostfix("");
                ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().add(newlog);
                // invoke web service (update data / upload file / add log)
                // load file
                long totalLen = 0;
                CheckedInputStream cin = null;
                ByteArrayOutputStream out;

                byteArray = null;
                try {
                    cin = new CheckedInputStream(new FileInputStream(folderList[i]),
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
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(0)).setAttribVarchar(folderList[i].getName());
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(1)).setAttribUnsignedint(totalLen);
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(2)).setAttribVarchar(""
                            + cin.getChecksum().getValue());
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(3)).setAttribUnsignedint(1);
                    ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(4)).setAttribUnsignedint(0);
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
                newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId());
                // save log to server
                newlog = xincoClientSession.getXinco().setXincoCoreLog(newlog, xincoClientSession.getUser());
                if (newlog == null) {
                }
                // upload file
                if (xincoClientSession.getXinco().uploadXincoCoreData((XincoCoreData) newnode.getUserObject(),
                        byteArray, xincoClientSession.getUser())
                        != totalLen) {
                    cin.close();
                    throw new XincoException(xerb.getString("datawizard.fileuploadfailed"));
                }
                cin.close();
                // update treemodel
                xincoClientSession.getXincoClientRepository().treemodel.reload(newnode);
                xincoClientSession.getXincoClientRepository().treemodel.nodeChanged(newnode);
                // select parent of new node
                xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) newnode.getParent());
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
                        folderList[i]);
                // select parent of new node
                // Bug fix by cmichl for Import Data Structure java.lang.NullPointerException (http://www.bluecubs.com/viewtopic.php?xincoCoreUser=500)
                if (newnode.getParent() != null) {
                    xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) newnode.getParent());
                }
            }
        }
        refreshJTree();
        getProgressBar().hide();
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
        int i;
        XincoMutableTreeNode currentNode;

        if (xincoClientSession.getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
            currentNode = xincoClientSession.getCurrentTreeNodeSelection();

            // only nodes have children
            if (currentNode.getUserObject().getClass() == XincoCoreNode.class) {
                // check for children only if none have been found yet
                if ((((XincoCoreNode) currentNode.getUserObject()).getXincoCoreNodes().isEmpty())
                        && (((XincoCoreNode) currentNode.getUserObject()).getXincoCoreData().isEmpty())) {
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
                    if (((XincoCoreData) ((XincoMutableTreeNode) currentNode.getChildAt(i)).getUserObject()).getXincoCoreDataType().getId() == 1) {
                        xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) currentNode.getChildAt(i));
                        // load full data
                        try {
                            XincoCoreData tempXdata = xincoClientSession.getXinco().getXincoCoreData(((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()), xincoClientSession.getUser());
                            if (tempXdata != null) {
                                xincoClientSession.getCurrentTreeNodeSelection().setUserObject(tempXdata);
                                xincoClientSession.getXincoClientRepository().treemodel.nodeChanged(xincoClientSession.getCurrentTreeNodeSelection());
                                // download file
                                setCurrentPathFilename(folder.getAbsolutePath() + System.getProperty("file.separator") + ((XincoAddAttribute) ((XincoCoreData) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getXincoAddAttributes().get(0)).getAttribVarchar());
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
                    File newfolder;
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
     * @param w_type
     */
    @SuppressWarnings("unchecked")
    public void doDataWizard(final int w_type) {
        this.wizardType = w_type;
        /*
         * wizard type 
         * = 1 = add new data 
         * = 2 = edit data object 
         * = 3 = edit add attributes 
         * = 4 = checkout data 
         * = 5 = undo checkout 
         * = 6 = checkin data
         * = 7 = download data 
         * = 8 = open URL cin browser 
         * = 9 = open email client with contact information 
         * = 10 = publish data 
         * = 11 = download previous revision 
         * = 12 = lock data 
         * = 13 = comment data 
         * = 14 = preview data 
         * = 15 = download file with predefined name
         */
        int i;
        newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
        xdata = null;
        newlog = new XincoCoreLog();

        in = null;
        byteArray = null;
        if (xincoClientSession.getCurrentTreeNodeSelection() != null) {
            //execute wizard as a whole
            try {
                //add new data
                if ((wizardType == 1)
                        && (xincoClientSession.getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class)) {

                    //set current node to new one
                    newnode = new XincoMutableTreeNode(new XincoCoreData(), this);
                    //set data attributes
                    ((XincoCoreData) newnode.getUserObject()).setXincoCoreNodeId(((XincoCoreNode) xincoClientSession.getCurrentTreeNodeSelection().getUserObject()).getId());
                    ((XincoCoreData) newnode.getUserObject()).setDesignation(xerb.getString("datawizard.newdata"));
                    ((XincoCoreData) newnode.getUserObject()).setXincoCoreDataType((XincoCoreDataType) xincoClientSession.getServerDatatypes().get(0));
                    ((XincoCoreData) newnode.getUserObject()).setXincoCoreLanguage((XincoCoreLanguage) xincoClientSession.getServerLanguages().get(0));
                    ((XincoCoreData) newnode.getUserObject()).setStatusNumber(1);
                    xincoClientSession.getXincoClientRepository().treemodel.insertNodeInto(newnode,
                            xincoClientSession.getCurrentTreeNodeSelection(),
                            xincoClientSession.getCurrentTreeNodeSelection().getChildCount());
                    xincoClientSession.setCurrentTreeNodeSelection(newnode);

                    //step 1: select data type
                    AbstractDialogDataType = new DataTypeDialog(null, true, this);
                    setGlobalDialogReturnValue(0);
                    AbstractDialogDataType.setVisible(true);
                    if (getGlobalDialog_returnValue() == 0) {
                        getProgressBar().hide();
                        throw new XincoException(xerb.getString("datawizard.updatecancel"));
                    }

                    //add specific attributes
                    XincoAddAttribute xaa;
                    for (i = 0; i < ((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getXincoCoreDataTypeAttributes().size(); i++) {
                        xaa = new XincoAddAttribute();
                        xaa.setAttributeId(((XincoCoreDataTypeAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(i)).getAttributeId());
                        xaa.setAttribVarchar("");
                        xaa.setAttribText("");
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(new Date());
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
                        xaa.setAttribDatetime(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
                        ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().add(xaa);
                    }

                    //initialize specific attributes:
                    //files
                    if (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1) {
                        ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(3)).setAttribUnsignedint(1); //revision model
                        ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(4)).setAttribUnsignedint(0); //archiving model
                    }
                }

                if (xincoClientSession.getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreData.class) {

                    newnode = xincoClientSession.getCurrentTreeNodeSelection();

                    //check file attribute count
                    //file = 1
                    if ((wizardType == 3)
                            && ((((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1)
                            && (((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().size() <= 3))) {
                        getProgressBar().hide();
                        throw new XincoException(xerb.getString("datawizard.noaddattributes"));
                    }

                    //edit add attributes
                    if ((wizardType == 1) || (wizardType == 3)) {

                        //step 2: edit add attributes
                        //for files -> show filechooser
                        //file = 1
                        if ((wizardType == 1)
                                && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1)) {
                            JFileChooser fc = new JFileChooser();

                            fc.setCurrentDirectory(new File(getCurrentPath()));
                            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            // show dialog
                            int result = fc.showOpenDialog(XincoExplorer.this);

                            if (result != JFileChooser.APPROVE_OPTION) {
                                getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            setCurrentPathFilename(fc.getSelectedFile().getPath());
                            ((XincoCoreData) newnode.getUserObject()).setDesignation(getCurrentFileName());
                        }

                        //for text -> show text editing dialog
                        //text = 2
                        if (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 2) {
                            setGlobalDialogReturnValue(0);
                            AbstractDialogAddAttributesText = getAbstractDialogAddAttributesText(false);
                            if (getGlobalDialog_returnValue() == 0) {
                                getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }
                        //show dialog for all additional attributes and custom data types
                        //file = 1 / text = 2
                        if ((((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId()
                                != 1
                                || ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().size()
                                > 8)
                                && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId()
                                != 2
                                || ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().size()
                                > 1)) {
                            //for other data type -> show universal add attribute dialog
                            AbstractDialogAddAttributesUniversal = new AddAttributeUniversalDialog(null, true, this);
                            setGlobalDialogReturnValue(0);
                            AbstractDialogAddAttributesUniversal.setVisible(true);
                            if (getGlobalDialog_returnValue() == 0) {
                                getProgressBar().hide();
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }

                    }

                    //edit logging
                    //step 3: edit logging (creation!)
                    if (wizardType == 1) {
                        newlog = new XincoCoreLog();
                        newlog.setOpCode(OPCode.CREATION.ordinal() + 1);
                        newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()) + "!");
                        newlog.setXincoCoreUserId(xincoClientSession.getUser().getId());
                        newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId()); //update to new id later!
                        newlog.setVersion(new XincoVersion());
                        newlog.getVersion().setVersionHigh(0);
                        newlog.getVersion().setVersionMid(0);
                        newlog.getVersion().setVersionLow(0);
                        newlog.getVersion().setVersionPostfix("");
                        ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().add(newlog);
                        newlog.setOpDescription(newlog.getOpDescription() + " (" + xerb.getString("general.user") + ": " + xincoClientSession.getUser().getUsername() + ")");
                    } else {
                        if ((wizardType != 7) && (wizardType != 8) && (wizardType != 9) && (wizardType != 11) && (wizardType != 14) && (wizardType != 15)) {
                            newlog = new XincoCoreLog();
                            if (wizardType <= 3) {
                                newlog.setOpCode(OPCode.MODIFICATION.ordinal() + 1);
                                newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()) + "!");
                            }
                            if (wizardType == 4) {
                                newlog.setOpCode(OPCode.CHECKOUT.ordinal() + 1);
                                newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()));
                            }
                            if (wizardType == 5) {
                                newlog.setOpCode(OPCode.CHECKOUT_UNDONE.ordinal() + 1);
                                newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()));
                            }
                            if (wizardType == 6) {
                                newlog.setOpCode(OPCode.CHECKIN.ordinal() + 1);
                                newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()));
                            }
                            if (wizardType == 10) {
                                newlog.setOpCode(OPCode.PUBLISH_COMMENT.ordinal() + 1);
                                newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()));
                            }
                            if (wizardType == 12) {
                                newlog.setOpCode(OPCode.LOCK_COMMENT.ordinal() + 1);
                                newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()));
                            }
                            if (wizardType == 13) {
                                newlog.setOpCode(OPCode.COMMENT_COMMENT.ordinal() + 1);
                                newlog.setOpDescription(xerb.getString(OPCode.getOPCode(newlog.getOpCode()).getName()));
                            }
                            newlog.setXincoCoreUserId(xincoClientSession.getUser().getId());
                            newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId());
                            newlog.setVersion(((XincoCoreLog) ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().get(((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().size() - 1)).getVersion());
                            ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().add(newlog);
                            //Nothing to do in log dialog for checkout...
                            if (wizardType != 4) {
                                setGlobalDialogReturnValue(0);
                                AbstractDialogLog = getAbstractDialogLog(false);
                                if (getGlobalDialog_returnValue() == 0) {
                                    getProgressBar().hide();
                                    throw new XincoException(xerb.getString("datawizard.updatecancel"));
                                }
                            }
                            newlog.setOpDescription(newlog.getOpDescription() + " (" + xerb.getString("general.user") + ": " + xincoClientSession.getUser().getUsername() + ")");
                        }
                    }

                    //choose filename for checkout/checkin/download/preview
                    if ((wizardType == 4) || (wizardType == 6) || (wizardType == 7) || (wizardType == 11)) {
                        JFileChooser fc = new JFileChooser();

                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fc.setSelectedFile(new File(getCurrentPath()
                                + ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(0)).getAttribVarchar()));
                        // show dialog
                        int result;

                        if ((wizardType == 4) || (wizardType == 7)
                                || (wizardType == 11)) {
                            result = fc.showSaveDialog(XincoExplorer.this);
                        } else {
                            result = fc.showOpenDialog(XincoExplorer.this);
                        }
                        if (result != JFileChooser.APPROVE_OPTION) {
                            getProgressBar().hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
                        setCurrentPathFilename(fc.getSelectedFile().getPath());
                    }
                    if (wizardType == 14) {
                        setCurrentPathFilename(File.createTempFile("xinco_", "_" + ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(0)).getAttribVarchar()).getPath());
                    }

                    //edit data details
                    if ((wizardType == 1) || (wizardType == 2)) {

                        //step 4: edit data details
                        setGlobalDialogReturnValue(0);
                        AbstractDialogData = getAbstractDialogData();
                        if (getGlobalDialog_returnValue() == 0) {
                            getProgressBar().hide();
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }

                        //step 4b: edit archiving options of files
                        if (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1) {
                            setGlobalDialogReturnValue(0);
                            AbstractDialogArchive = getAbstractDialogArchive();
                            if (getGlobalDialog_returnValue() == 0) {
                                getProgressBar().hide();
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
                    ByteArrayOutputStream out;
                    //file = 1
                    if (((wizardType == 1) || (wizardType == 6)) && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1)) {
                        try {
                            //update transaction info
                            getProgressBar().setTitle(xerb.getString("datawizard.fileuploadinfo"));
                            getProgressBar().show();
                            getjInternalFrameInformationText().setText(xerb.getString("datawizard.fileuploadinfo"));
                            in = new CheckedInputStream(new FileInputStream(getCurrentFullPath()), new CRC32());
                            out = new ByteArrayOutputStream();
                            byte[] buf = new byte[4096];
                            int len;
                            totalLen = 0;
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                                totalLen += len;
                            }
                            byteArray = out.toByteArray();
                            out.close();
                            //update attributes
                            ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(0)).setAttribVarchar(getCurrentFileName());
                            ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(1)).setAttribUnsignedint(totalLen);
                            ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(2)).setAttribVarchar("" + ((CheckedInputStream) in).getChecksum().getValue());
                        } catch (Exception fe) {
                            Logger.getLogger(XincoExplorer.class.getSimpleName()).log(Level.SEVERE, null, fe);
                            getProgressBar().hide();
                            throw new XincoException(xerb.getString("datawizard.unabletoloadfile") + "\n" + fe.getLocalizedMessage());
                        }
                    }
                    //save data to server
                    if ((wizardType != 7) && (wizardType != 8) && (wizardType != 9) && (wizardType != 11) && (wizardType != 14) && (wizardType != 15)) {
                        if ((wizardType >= 4) && (wizardType <= 6)) {
                            if (wizardType == 4) {
                                xdata = xincoClientSession.getXinco().doXincoCoreDataCheckout((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser());
                            } else {
                                if (wizardType == 5) {
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
                    if ((wizardType != 7) && (wizardType != 8) && (wizardType != 9) && (wizardType != 11) && (wizardType != 14) && (wizardType != 15)) {
                        //update id cin log
                        newlog.setXincoCoreDataId(((XincoCoreData) newnode.getUserObject()).getId());
                        //save log to server
                        newlog = xincoClientSession.getXinco().setXincoCoreLog(newlog, xincoClientSession.getUser());
                        if (newlog != null) {
                            ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().add(newlog);
                        }
                    }
                    //upload file (new / checkin)
                    //file = 1
                    if (((wizardType == 1) || (wizardType == 6)) && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1)) {

                        if (xincoClientSession.getXinco().uploadXincoCoreData((XincoCoreData) newnode.getUserObject(), byteArray, xincoClientSession.getUser()) != totalLen) {
                            in.close();
                            JOptionPane.showMessageDialog(XincoExplorer.this,
                                    xerb.getString("datawizard.fileuploadfailed"),
                                    xerb.getString("general.error"),
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        in.close();
                        //update transaction info
                        getjInternalFrameInformationText().setText(xerb.getString("datawizard.fileuploadsuccess"));
                        getProgressBar().hide();
                    }
                    //download file
                    //file = 1
                    if (((wizardType == 4) || (wizardType == 7) || (wizardType == 11)
                            || (wizardType == 14) || (wizardType == 15))
                            && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 1)) {
                        //determine requested revision and set log ArrayList
                        if (wizardType != 15) {
                            getProgressBar().setTitle(xerb.getString("datawizard.filedownloadinfo"));
                            getProgressBar().show();
                        }
                        ArrayList dataLogArrayList = null;
                        if (wizardType == 11) {
                            jDialogRevision = getJDialogRevision();
                            setGlobalDialogReturnValue(-1);
                            jDialogRevision.setVisible(true);
                            if (getGlobalDialog_returnValue() == -1) {
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            dataLogArrayList = new ArrayList();
                            dataLogArrayList.addAll(((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs());
                            XincoCoreLog RevLog = null;
                            for (i = 0; i < ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().size(); i++) {
                                if (((XincoCoreLog) ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().get(i)).getId() == getGlobalDialog_returnValue()) {
                                    RevLog = (XincoCoreLog) ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().get(i);
                                    break;
                                }
                            }
                            ArrayList revLogArrayList = new ArrayList();
                            revLogArrayList.add(RevLog);
                            ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().clear();
                            ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().addAll(revLogArrayList);
                        }

                        //update transaction info
                        getjInternalFrameInformationText().setText(xerb.getString("datawizard.filedownloadinfo"));
                        try {
                            //invoke actual call
                            byteArray = (byte[]) xincoClientSession.getXinco().downloadXincoCoreData((XincoCoreData) newnode.getUserObject(), xincoClientSession.getUser());
                            in = new ByteArrayInputStream(byteArray);
                        } catch (Exception ce) {
                            //reassign log ArrayList
                            if (wizardType == 11) {
                                ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().clear();
                                ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().addAll(dataLogArrayList);
                            }
                            JOptionPane.showMessageDialog(XincoExplorer.this,
                                    xerb.getString("datawizard.filedownloadfailed"),
                                    xerb.getString("general.error"),
                                    JOptionPane.WARNING_MESSAGE);
                            getProgressBar().hide();
                            throw (ce);
                        }

                        //reassign log ArrayList
                        if (wizardType == 11) {
                            ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().clear();
                            ((XincoCoreData) newnode.getUserObject()).getXincoCoreLogs().addAll(dataLogArrayList);
                        }

                        //ByteArrayInputStream cin = new ByteArrayInputStream(byteArray);
                        CheckedOutputStream couts = new CheckedOutputStream(new FileOutputStream(getCurrentFullPath()), new CRC32());
                        byte[] buf = new byte[4096];
                        int len;
                        totalLen = 0;
                        while ((len = in.read(buf)) > 0) {
                            couts.write(buf, 0, len);
                            totalLen += len;
                        }
                        in.close();
                        //check correctness of data
                        if (wizardType != 11) {
                            if (((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(1)).getAttribUnsignedint() != totalLen) {
                                JOptionPane.showMessageDialog(XincoExplorer.this,
                                        xerb.getString("datawizard.filedownloadcorrupted"),
                                        xerb.getString("general.error"),
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        couts.close();
                        //make sure temp. file is deleted on exit
                        if (wizardType == 14) {
                            (new File(getCurrentFullPath())).deleteOnExit();
                        }
                        //update transaction info
                        getjInternalFrameInformationText().setText(xerb.getString("datawizard.filedownloadsuccess"));
                        //open file cin default application
                        if (wizardType != 15) {
                            boolean open_file = false;
                            if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                                if (wizardType == 14) {
                                    open_file = true;
                                } else {
                                    if (JOptionPane.showConfirmDialog(XincoExplorer.this,
                                            xerb.getString("datawizard.opendataindefaultapplication"),
                                            xerb.getString("general.question"),
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                        open_file = true;
                                    }
                                }
                                if (open_file) {
                                    try {
                                        String[] cmd = {"open", getCurrentFullPath()};
                                        Runtime.getRuntime().exec(cmd);
                                    } catch (Throwable t) {
                                        Logger.getLogger(XincoExplorer.class.getSimpleName()).log(
                                                Level.SEVERE, null, t);
                                    }
                                }
                            } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                                if (wizardType == 14) {
                                    open_file = true;
                                } else {
                                    if (JOptionPane.showConfirmDialog(XincoExplorer.this,
                                            xerb.getString("datawizard.opendataindefaultapplication"),
                                            xerb.getString("general.question"),
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                        open_file = true;
                                    }
                                }
                                if (open_file) {
                                    try {
                                        String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + getCurrentFullPath() + "\"";
                                        Runtime.getRuntime().exec(cmd);
                                    } catch (Throwable t) {
                                        Logger.getLogger(XincoExplorer.class.getSimpleName()).log(
                                                Level.SEVERE, null, t);
                                    }
                                }
                            } else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
                                if (wizardType == 14) {
                                    open_file = true;
                                } else {
                                    if (JOptionPane.showConfirmDialog(XincoExplorer.this, xerb.getString("datawizard.opendataindefaultapplication"), xerb.getString("general.question"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                        open_file = true;
                                    }
                                }
                                if (open_file) {
                                    try {
                                        String[] cmd = {"xdg-open", getCurrentFullPath()};
                                        Runtime.getRuntime().exec(cmd);
                                    } catch (Throwable t) {
                                        Logger.getLogger(XincoExplorer.class.getSimpleName()).log(
                                                Level.SEVERE, null, t);
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString(
                                        "This Operative System is unsuported for this functionality. Please upgrade to Java 6+")
                                        + " " + xerb.getString("general.reason") + ": ", xerb.getString("general.error"),
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        if (wizardType != 15) {
                            getProgressBar().hide();
                        }
                    }
                    //Open cin Browser
                    //URL = 3
                    if ((wizardType == 8) && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 3)) {
                        //open URL cin default browser
                        Desktop desktop;
                        // Before more Desktop API is used, first check
                        // whether the API is supported by this particular
                        // virtual machine (VM) on this particular host.
                        if (Desktop.isDesktopSupported()) {
                            desktop = Desktop.getDesktop();
                            String temp_url = ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(0)).getAttribVarchar();
                            java.net.URI uri = new java.net.URI(temp_url);
                            desktop.browse(uri);
                        } else {
                            String tempUrl = ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(0)).getAttribVarchar();
                            if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                                try {
                                    String[] cmd = {"open", tempUrl};
                                    Runtime.getRuntime().exec(cmd);
                                } catch (Throwable t) {
                                    Logger.getLogger(XincoExplorer.class.getSimpleName()).log(
                                            Level.SEVERE, null, t);
                                }
                            } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                                try {
                                    String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + tempUrl + "\"";
                                    Runtime.getRuntime().exec(cmd);
                                } catch (Throwable t) {
                                    Logger.getLogger(XincoExplorer.class.getSimpleName()).log(
                                            Level.SEVERE, null, t);
                                }
                            } else {
                                JOptionPane.showMessageDialog(XincoExplorer.this,
                                        xerb.getString("message.warning.operation.notsupported")
                                        + " " + xerb.getString("general.reason")
                                        + ": ", xerb.getString("general.error"),
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                    //Open cin Email Client
                    //contact = 4
                    if ((wizardType == 9) && (((XincoCoreData) newnode.getUserObject()).getXincoCoreDataType().getId() == 4)) {
                        //open Contact cin default mail application
                        Desktop desktop;
                        // Before more Desktop API is used, first check
                        // whether the API is supported by this particular
                        // virtual machine (VM) on this particular host.
                        if (Desktop.isDesktopSupported()) {
                            System.out.println("Supported");
                            desktop = Desktop.getDesktop();
                            String temp_email = ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(9)).getAttribVarchar();
                            URI uriMailTo;
                            uriMailTo = new URI("mailto", temp_email, null);
                            desktop.mail(uriMailTo);
                        } else {
                            String temp_email = ((XincoAddAttribute) ((XincoCoreData) newnode.getUserObject()).getXincoAddAttributes().get(9)).getAttribVarchar();
                            if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
                                try {
                                    String[] cmd = {"open", "mailto:" + temp_email};
                                    Runtime.getRuntime().exec(cmd);
                                } catch (Throwable t) {
                                    Logger.getLogger(XincoExplorer.class.getSimpleName()).log(
                                            Level.SEVERE, null, t);
                                }
                            } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                                try {
                                    String cmd = "rundll32 url.dll,FileProtocolHandler" + " \"" + "mailto:" + temp_email + "\"";
                                    Runtime.getRuntime().exec(cmd);
                                } catch (Throwable t) {
                                    Logger.getLogger(XincoExplorer.class.getSimpleName()).log(
                                            Level.SEVERE, null, t);
                                }
                            } else {
                                JOptionPane.showMessageDialog(XincoExplorer.this, 
                                        xerb.getString("message.warning.operation.notsupported")
                                        + " " + xerb.getString("general.reason") + ": ", xerb.getString("general.error"),
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }

                    if ((wizardType != 7) && (wizardType != 8) && (wizardType != 9) && (wizardType != 11) && (wizardType != 14) && (wizardType != 15)) {
                        //update treemodel
                        xincoClientSession.getXincoClientRepository().treemodel.reload(newnode);
                        xincoClientSession.getXincoClientRepository().treemodel.nodeChanged(newnode);
                        getjInternalFrameInformationText().setText(xerb.getString("datawizard.updatesuccess"));
                        if (wizardType == 10) {
                            String tempUrl;
                            //file = 1
                            if (xdata.getXincoCoreDataType().getId() == 1) {
                                tempUrl = ((XincoAddAttribute) xdata.getXincoAddAttributes().get(0)).getAttribVarchar();
                            } else {
                                tempUrl = xdata.getDesignation();
                            }
                            getjInternalFrameInformationText().setText(xerb.getString("datawizard.updatesuccess.publisherinfo") + "\nhttp://[serverName]:[port]/xinco/XincoPublisher/" + xdata.getId() + "/" + tempUrl);
                        }
                        TreePath currentTreePath = new TreePath(xincoClientSession.getCurrentTreeNodeSelection().getPath());
                        getjTreeRepository().setSelectionPath(new TreePath(((XincoMutableTreeNode) xincoClientSession.getCurrentTreeNodeSelection().getParent()).getPath()));
                        getjTreeRepository().setSelectionPath(currentTreePath);
                    }

                }
            } catch (Exception we) {
                Logger.getLogger(XincoExplorer.class.getSimpleName()).log(Level.SEVERE, null, we);
                //update transaction info
                getjInternalFrameInformationText().setText("");
                //remove new data cin case off error
                if (wizardType == 1) {
                    xincoClientSession.setCurrentTreeNodeSelection((XincoMutableTreeNode) xincoClientSession.getCurrentTreeNodeSelection().getParent());
                    xincoClientSession.getXincoClientRepository().treemodel.removeNodeFromParent(newnode);
                    getjTreeRepository().setSelectionPath(new TreePath(xincoClientSession.getCurrentTreeNodeSelection().getPath()));
                }
                if (wizardType != 3 || getGlobalDialog_returnValue() != 0) {
                    JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.updatefailed")
                            + " " + xerb.getString("general.reason") + ": "
                            + we.toString(), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                }
                getProgressBar().hide();
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

                @Override
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
        int i;
        if (s != null) {
            try {
                setPreviousFullpath(s);
                i = s.lastIndexOf(System.getProperty("file.separator"));
                /*
                 * j = s.lastIndexOf("\\"); //select i as index wanted if (j>i)
                 * { i = j; }
                 */
                previous_filename = s.substring(i + 1);
                if (i > 0) {
                    previousPath = s.substring(0, i + 1);
                } else {
                    previousPath = "";
                }
            } catch (Exception e) {
                previous_filename = "";
                previousPath = "";
                setPreviousFullpath("");
            }
        } else {
            previous_filename = "";
            previousPath = "";
            setPreviousFullpath("");
        }
    }

    /**
     * This method sets current path and filename
     *
     * @param s
     */
    public void setCurrentPathFilename(String s) {
        int i;
        if (s != null) {
            try {
                setPreviousPathFilename(getCurrentFullPath());
                setCurrentFullPath(s);
                i = s.lastIndexOf(System.getProperty("file.separator"));
                setCurrentFilename(s.substring(i + 1));
                if (i > 0) {
                    setCurrentPath(s.substring(0, i + 1));
                } else {
                    setCurrentPath("");
                }
            } catch (Exception e) {
                setCurrentFilename("");
                setCurrentPath("");
                setCurrentFullPath("");
            }
        } else {
            setCurrentFilename("");
            setCurrentPath("");
            setCurrentFullPath("");
        }
    }

    /**
     * This method sets current path and filename
     *
     * @param path String representing path
     */
    public void setCurrentPath(String path) {
        if (!(path.substring(path.length() - 1).equals(System.getProperty("file.separator")))) {
            path += System.getProperty("file.separator");
        }
        setCurrentFilename("");
        currentPath = path;
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
        } else {
            userDialog.setIsAged(aged);
        }
        userDialog.setVisible(true);
    }

    /**
     * This method initializes AbstractDialogAddAttributesText
     *
     * @param viewonly
     * @return AbstractDialog
     */
    public AbstractDialog getAbstractDialogAddAttributesText(boolean viewonly) {
        if (AbstractDialogAddAttributesText == null) {
            AbstractDialogAddAttributesText = new AddAttributeText(null, true, viewonly, this);
            addDialog(AbstractDialogAddAttributesText);
        }
        //Issue #2972311
        AbstractDialogAddAttributesText.setVisible(true);
        ((AddAttributeText) AbstractDialogAddAttributesText).setViewOnly(viewonly);
        return AbstractDialogAddAttributesText;
    }

    /**
     * This method saves the configuration
     *
     */
    public void saveConfig() {
        try {
            //Save configuration
            java.io.FileOutputStream fout =
                    new java.io.FileOutputStream(System.getProperty("user.home")
                    + System.getProperty("file.separator") + "xincoClientConfig.dat");
            java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(fout);
            os.writeObject(xincoClientConfig);
            os.close();
            fout.close();
            //Feature Request # 3024502
            //Now save a backup
            fout = new java.io.FileOutputStream(System.getProperty("user.home")
                    + System.getProperty("file.separator") + "xincoClientConfig.dat.bak");
            os = new java.io.ObjectOutputStream(fout);
            os.writeObject(xincoClientConfig);
            os.close();
            fout.close();
        } catch (Exception ioe) {
            //error handling
            Logger.getLogger(ConnectionDialog.class.getSimpleName()).log(Level.SEVERE, "Unable to write Profiles-File!", ioe);
        }
    }

    /**
     * This method loads the configuration
     *
     * @return void
     */
    @SuppressWarnings("unchecked")
    private void loadConfig(String file) {
        ArrayList tmp_ArrayList_old = new ArrayList();
        try {
            ArrayList tmp_ArrayList;
            FileInputStream fin;
            ObjectInputStream ois;
            Object object;
            //get old settings
            try {
                fin = new FileInputStream("xincoClientConnectionProfiles.dat");
                ois = new ObjectInputStream(fin);
                try {
                    while ((tmp_ArrayList = (ArrayList) ois.readObject()) != null) {
                        tmp_ArrayList_old = tmp_ArrayList;
                    }
                    while ((object = ois.readObject()) != null) {
                        if (object instanceof ArrayList) {
                            tmp_ArrayList_old = (ArrayList) object;
                        } else if (object instanceof ArrayList) {
                            tmp_ArrayList_old = new ArrayList();
                            tmp_ArrayList_old.addAll((ArrayList) object);
                        }
                    }
                } catch (Exception ioe3) {
                    Logger.getLogger(XincoExplorer.class.getSimpleName()).log(Level.FINE, "No old settings.", ioe3);
                }
                ois.close();
                fin.close();
            } catch (Exception ioe2) {
                tmp_ArrayList_old = null;
                Logger.getLogger(XincoExplorer.class.getSimpleName()).log(Level.FINE, "No old settings.", ioe2);
            }
            fin = new FileInputStream(System.getProperty("user.home") + System.getProperty("file.separator") + file);
            ois = new ObjectInputStream(fin);
            try {
                while ((object = ois.readObject()) != null) {
                    if (xincoClientConfig == null) {
                        xincoClientConfig = new ArrayList();
                    } else {
                        xincoClientConfig.clear();
                    }
                    if (object instanceof ArrayList) {
                        tmp_ArrayList = (ArrayList) object;
                        xincoClientConfig.addAll(tmp_ArrayList);
                    } else if (object instanceof ArrayList) {
                        xincoClientConfig.addAll((ArrayList) object);
                    }
                }
            } catch (Exception ioe3) {
                Logger.getLogger(ConnectionDialog.class.getSimpleName()).log(Level.FINE, null, ioe3);
            }
            ois.close();
            fin.close();
            //insert old settings
            if (tmp_ArrayList_old != null) {
                xincoClientConfig.add(ConfigElement.CONNECTION_PROFILE.ordinal(), tmp_ArrayList_old);
            }
            //delete old settings
            (new File(CONFIG_NAME)).delete();
            //Check version
            if (xincoClientConfig.size() >= ConfigElement.values().length) {
                //Version element is present
                if ((Integer) xincoClientConfig.get(ConfigElement.VERSION.ordinal()) > ConfigFileVersion) //We're handling a newer version of the config file.
                {
                    JOptionPane.showMessageDialog(this,
                            xerb.getString("error.configversion").replaceAll("%c", ""
                            + ConfigFileVersion).replaceAll("%n", ""
                            + (Integer) xincoClientConfig.get(ConfigElement.VERSION.ordinal())),
                            xerb.getString("general.warning"),
                            JOptionPane.WARNING_MESSAGE);
                    //Add version to old files
                    xincoClientConfig.add(ConfigFileVersion, ConfigElement.VERSION.ordinal());
                    //Remove newer items as we don't know how to handle them.
                    while (xincoClientConfig.size() > ConfigElement.values().length) {
                        xincoClientConfig.remove(xincoClientConfig.size() - 1);
                    }
                }
            } else if (xincoClientConfig.size() < ConfigElement.values().length) {
                //Add version to old files
                xincoClientConfig.add(ConfigFileVersion);
            }
            if (recoverFromBackup) {
                Logger.getLogger(ConnectionDialog.class.getSimpleName()).log(Level.FINE, "Recovered from backup!");
            }
        } catch (Exception ioe) {
            //Feature Request # 3024502
            Logger.getLogger(ConnectionDialog.class.getSimpleName()).log(Level.FINE, ioe.getLocalizedMessage(), ioe);
            if (!recoverFromBackup) {
                Logger.getLogger(ConnectionDialog.class.getSimpleName()).log(Level.FINE, "Trying to retrieve from backup");
                File backup = new File(System.getProperty("user.home")
                        + System.getProperty("file.separator") + "xincoClientConfig.dat.bak");
                if (backup.exists()) {
                    Logger.getLogger(ConnectionDialog.class.getSimpleName()).log(Level.FINE, "Backup file found!");
                    //Delete old one, is not working anyways
                    (new File(System.getProperty("user.home")
                            + System.getProperty("file.separator") + "xincoClientConfig.dat")).delete();
                    backup.renameTo(new File(System.getProperty("user.home")
                            + System.getProperty("file.separator") + "xincoClientConfig.dat"));
                } else {
                    Logger.getLogger(ConnectionDialog.class.getSimpleName()).log(Level.FINE, "No backup file found!");
                }
                recoverFromBackup = true;
                //Now retry with the recovered file...
                loadConfig(file);
                return;
            }
            //error handling
            Logger.getLogger(ConnectionDialog.class.getSimpleName()).log(Level.FINE, "Unable to recover from backup, create a new file...");
            //create config
            xincoClientConfig = new ArrayList();
            //add connection profiles
            xincoClientConfig.add(new ArrayList());
            //insert old settings
            if (tmp_ArrayList_old != null) {
                xincoClientConfig.add(ConfigElement.CONNECTION_PROFILE.ordinal(), tmp_ArrayList_old);
                //delete old settings
                (new File(CONFIG_NAME)).delete();
            } else {
                createDefaultConfiguration(true);
            }
        }
    }

    //Feature Request # 3024502
    /**
     * This method loads the configuration
     *
     * @return void
     */
    @SuppressWarnings("unchecked")
    private void loadConfig() {
        loadConfig(CONFIG_NAME);
    }

    @SuppressWarnings("unchecked")
    protected void createDefaultConfiguration(boolean modifyProfiles) {
        xincoClientConfig = new ArrayList();
        xincoClientConfig.add(new ArrayList());
        if (modifyProfiles) {
            //insert default connection profiles
            ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).add(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).profile_name = "xinco Demo User";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).service_endpoint = "http://xinco.org:8080/xincoDemo/Xinco";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).username = "user";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).password = "user";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).save_password = true;
            ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).add(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).profile_name = "xinco Demo Admin";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).service_endpoint = "http://xinco.org:8080/xincoDemo/Xinco";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).username = "admin";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).password = "admin";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).save_password = true;
            ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).add(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).profile_name = "Template Profile";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).service_endpoint = "http://[serverDomain]:8080/xinco/Xinco";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).username = "yourUsername";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).password = "yourPassword";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).save_password = true;
            ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).add(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).profile_name = "Admin (localhost)";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).service_endpoint = "http://localhost:8080/xinco/Xinco";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).username = "admin";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).password = "admin";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).save_password = true;
            ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).add(new XincoClientConnectionProfile());
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).profile_name = "User (localhost)";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).service_endpoint = "http://localhost:8080/xinco/Xinco";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).username = "user";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).password = "user";
            ((XincoClientConnectionProfile) ((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).get(((ArrayList) xincoClientConfig.get(ConfigElement.CONNECTION_PROFILE.ordinal())).size() - 1)).save_password = true;
        }
        //add Pluggable Look and Feel
        xincoClientConfig.add("javax.swing.plaf.metal.MetalLookAndFeel");
        //add locale
        xincoClientConfig.add(Locale.getDefault());
        //add version
        xincoClientConfig.add(ConfigFileVersion);
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

    public void set_globalDialog_returnValue(int v) {
        this.setGlobalDialogReturnValue(v);
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
        rThread = new RefreshThread();
        rThread.start();
    }

    /**
     *
     * @return XincoProgressBarThread
     */
    public XincoProgressBarThread getProgressBar() {
        return progressBar;
    }

    private class RefreshThread extends Thread {

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
                getjTreeRepository().expandPath(new TreePath(getSession().getXincoClientRepository().treemodel.getPathToRoot((XincoMutableTreeNode) (getSession().getXincoClientRepository().treemodel).getRoot())));
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
     *
     * @return ArrayList containing XincoExplorer's dialogs
     */
    public ArrayList getDialogs() {
        if (dialogs == null) {
            dialogs = new ArrayList();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        resetTimer();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        resetTimer();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        resetTimer();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        resetTimer();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        resetTimer();
    }

    @Override
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
