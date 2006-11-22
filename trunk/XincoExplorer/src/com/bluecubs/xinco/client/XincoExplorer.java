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
 * Javier Ortiz     Aug-Dec 2006      Remove dialogs and windows from main code
 *
 *************************************************************
 */

package com.bluecubs.xinco.client;

import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.client.dialogs.ACLDialog;
import com.bluecubs.xinco.client.dialogs.AddAttributeUniversalDialog;
import com.bluecubs.xinco.client.dialogs.ConnectionDialog;
import com.bluecubs.xinco.client.dialogs.DataDialog;
import com.bluecubs.xinco.client.dialogs.DataFolderDialog;
import com.bluecubs.xinco.client.dialogs.DataTypeDialog;
import com.bluecubs.xinco.client.dialogs.LogDialog;
import com.bluecubs.xinco.client.dialogs.UserDialog;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import java.util.Vector;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.Locale;
import java.io.*;
import java.util.zip.*;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.client.*;
import com.bluecubs.xinco.service.*;

import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputListener;

import java.awt.event.MouseEvent;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;
import javax.xml.namespace.QName;
import java.net.URL;

import javax.swing.JDialog;
public class XincoExplorer extends JFrame {
    
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
    public javax.swing.JTree jTreeRepository = null;
    private javax.swing.JTable jTableRepository = null;
    private javax.swing.JMenuItem jMenuItemRepositoryRefresh = null;
    private javax.swing.JMenu jMenuSearch = null;
    private javax.swing.JMenuItem jMenuItemSearchRepository = null;
    private javax.swing.JMenuItem jMenuItemRepositoryAddFolder = null;
    private javax.swing.JMenuItem jMenuItemRepositoryAddData = null;
    private javax.swing.JMenu jMenuView = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleWindows = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleJava = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleMotif = null;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemViewStyleNapkin = null;
    private javax.swing.ButtonGroup bgwindowstyle;
    //client version
    private XincoVersion xincoClientVersion = null;
    //session object
    private XincoClientSession xincoClientSession = null;
    //connection profiles
    private Vector xincoClientConfig = null;
    //current path and filename
    private String current_filename = "";
    private String current_path = "";
    private String current_fullpath = "";
    //global dialog return value
    private int global_dialog_return_value = 0;
    private javax.swing.JMenuItem jMenuItemConnectionConnect = null;
    private javax.swing.JPanel jContentPaneDialogConnection = null;
    private javax.swing.JDialog jDialogConnection = null;
    private javax.swing.JLabel jLabelDialogConnectionServerEndpoint = null;
    private javax.swing.JTextField jTextFieldDialogConnectionUsername = null;
    private javax.swing.JTextField jTextFieldDialogConnectionServerEndpoint = null;
    private javax.swing.JPasswordField jPasswordFieldDialogConnectionPassword = null;
    private javax.swing.JLabel jLabelDialogConnectionUsername = null;
    private javax.swing.JLabel jLabelDialogConnectionPassword = null;
    private javax.swing.JButton jButtonDialogConnectionConnect = null;
    private javax.swing.JButton jButtonDialogConnectionCancel = null;
    private javax.swing.JLabel jLabelDialogConnectionProfile = null;
    private javax.swing.JList jListDialogConnectionProfile = null;
    private javax.swing.JLabel jLabelDialogConnectionProfileName = null;
    private javax.swing.JTextField jTextFieldDialogConnectionProfileName = null;
    private javax.swing.JButton jButtonDialogConnectionNewProfile = null;
    private javax.swing.JButton jButtonDialogConnectionDeleteProfile = null;
    private javax.swing.JLabel jLabelDialogConnectionSavePassword = null;
    private javax.swing.JCheckBox jCheckBoxDialogConnectionSavePassword = null;
    private javax.swing.JScrollPane jScrollPaneDialogConnectionProfile = null;
    private javax.swing.JMenuItem jMenuItemRepositoryEditFolderData = null;
    private javax.swing.JMenuItem jMenuItemRepositoryCheckoutData = null;
    private javax.swing.JMenuItem jMenuItemRepositoryUndoCheckoutData = null;
    private javax.swing.JMenuItem jMenuItemRepositoryCheckinData = null;
    private javax.swing.JMenuItem jMenuItemRepositoryPublishData = null;
    private javax.swing.JMenuItem jMenuItemRepositoryLockData = null;
    private javax.swing.JMenuItem jMenuItemRepositoryDownloadRevision = null;
    private javax.swing.JDialog jDialogFolder = null;
    private javax.swing.JPanel jContentPaneDialogFolder = null;
    private javax.swing.JLabel jLabelDialogFolderID = null;
    private javax.swing.JLabel jLabelDialogFolderDesignation = null;
    private javax.swing.JLabel jLabelDialogFolderLanguage = null;
    private javax.swing.JLabel jLabelDialogFolderStatus = null;
    private javax.swing.JButton jButtonDialogFolderSave = null;
    private javax.swing.JButton jButtonDialogFolderCancel = null;
    private javax.swing.JTextField jTextFieldDialogFolderID = null;
    private javax.swing.JTextField jTextFieldDialogFolderDesignation = null;
    private javax.swing.JTextField jTextFieldDialogFolderStatus = null;
    private javax.swing.JScrollPane jScrollPaneDialogFolderLanguage = null;
    private javax.swing.JList jListDialogFolderLanguage = null;
    private javax.swing.JMenuItem jMenuItemRepositoryEditFolderDataACL = null;
    private javax.swing.JMenuItem jMenuItemRepositoryMoveFolderData = null;
    private javax.swing.JMenuItem jMenuItemRepositoryInsertFolderData = null;
    private javax.swing.JDialog jDialogACL = null;
    private javax.swing.JMenuItem jMenuItemRepositoryViewEditAddAttributes = null;
    private javax.swing.JDialog jDialogDataType = null;
    private javax.swing.JPanel jContentPaneDialogDataType = null;
    private javax.swing.JLabel jLabelDialogDataType = null;
    private javax.swing.JScrollPane jScrollPaneDialogDataType = null;
    private javax.swing.JList jListDialogDataType = null;
    private javax.swing.JButton jButtonDialogDataTypeContinue = null;
    private javax.swing.JButton jButtonDialogDataTypeCancel = null;
    private javax.swing.JDialog jDialogRevision = null;
    private javax.swing.JPanel jContentPaneDialogRevision = null;
    private javax.swing.JLabel jLabelDialogRevision = null;
    private javax.swing.JScrollPane jScrollPaneDialogRevision = null;
    private javax.swing.JList jListDialogRevision = null;
    private javax.swing.JButton jButtonDialogRevisionContinue = null;
    private javax.swing.JButton jButtonDialogRevisionCancel = null;
    private javax.swing.JDialog jDialogData = null;
    private javax.swing.JPanel jContentPaneDialogData = null;
    private javax.swing.JLabel jLabelDialogDataID = null;
    private javax.swing.JLabel jLabelDialogDataDesignation = null;
    private javax.swing.JLabel jLabelDialogDataLanguage = null;
    private javax.swing.JLabel jLabelDialogDataStatus = null;
    private javax.swing.JTextField jTextFieldDialogDataID = null;
    private javax.swing.JTextField jTextFieldDialogDataDesignation = null;
    private javax.swing.JTextField jTextFieldDialogDataStatus = null;
    private javax.swing.JScrollPane jScrollPaneDialogDataLanguage = null;
    private javax.swing.JButton jButtonDialogDataSave = null;
    private javax.swing.JButton jButtonDialogDataCancel = null;
    private javax.swing.JList jListDialogDataLanguage = null;
    private javax.swing.JDialog jDialogArchive = null;
    private javax.swing.JPanel jContentPaneDialogArchive = null;
    private javax.swing.JLabel jLabelDialogArchiveRevisionModel = null;
    private JCheckBox jCheckBoxDialogArchiveRevisionModel = null;
    private javax.swing.JLabel jLabelDialogArchiveArchivingModel = null;
    private JComboBox jComboBoxDialogArchiveArchivingModel = null;
    private javax.swing.JLabel jLabelDialogArchiveDate = null;
    private javax.swing.JTextField jTextFieldDialogArchiveDateYear = null;
    private javax.swing.JTextField jTextFieldDialogArchiveDateMonth = null;
    private javax.swing.JTextField jTextFieldDialogArchiveDateDay = null;
    private javax.swing.JLabel jLabelDialogArchiveDays = null;
    private javax.swing.JTextField jTextFieldDialogArchiveDays = null;
    private javax.swing.JButton jButtonDialogArchiveContinue = null;
    private javax.swing.JButton jButtonDialogArchiveCancel = null;
    private javax.swing.JDialog jDialogLog = null;
    private javax.swing.JPanel jContentPaneDialogLog = null;
    private javax.swing.JLabel jLabelDialogLogDescription = null;
    private javax.swing.JLabel jLabelDialogLogVersion = null;
    private javax.swing.JTextField jTextFieldDialogLogDescription = null;
    private javax.swing.JTextField jTextFieldDialogLogVersionHigh = null;
    private javax.swing.JTextField jTextFieldDialogLogVersionMid = null;
    private javax.swing.JTextField jTextFieldDialogLogVersionLow = null;
    private javax.swing.JTextField jTextFieldDialogLogVersionPostfix = null;
    private javax.swing.JButton jButtonDialogLogContinue = null;
    private javax.swing.JButton jButtonDialogLogCancel = null;
    private javax.swing.JLabel jLabelDialogLogVersionPostfix = null;
    private javax.swing.JLabel jLabelDialogLogVersionPostfixExplanation = null;
    private javax.swing.JLabel jLabelDialogLogVersionDot1 = null;
    private javax.swing.JLabel jLabelDialogLogVersionDot2 = null;
    private javax.swing.JDialog jDialogAddAttributesUniversal = null;
    private javax.swing.JPanel jContentPaneDialogAddAttributesUniversal = null;
    private javax.swing.JScrollPane jScrollPaneDialogAddAttributesUniversal = null;
    private javax.swing.JButton jButtonDialogAddAttributesUniversalSave = null;
    private javax.swing.JButton jButtonDialogAddAttributesUniversalCancel = null;
    private javax.swing.JTable jTableDialogAddAttributesUniversal = null;
    private javax.swing.JMenu jMenuPreferences = null;
    private javax.swing.JMenuItem jMenuItemPreferencesEditUser = null;
    private javax.swing.JMenuItem jMenuItemRepositoryViewData = null;
    private javax.swing.JDialog jDialogUser = null;
    private javax.swing.JPanel jContentPaneDialogUser = null;
    private javax.swing.JLabel jLabelDialogUserID = null;
    private javax.swing.JLabel jLabelDialogUserUsername = null;
    private javax.swing.JLabel jLabelDialogUserPassword = null;
    private javax.swing.JLabel jLabeDialogUserVerifyPassword = null;
    private javax.swing.JLabel jLabelDialogUserFirstname = null;
    private javax.swing.JLabel jLabelDialogUserLastname = null;
    private javax.swing.JLabel jLabelDialogUserEmail = null;
    private javax.swing.JTextField jTextFieldDialogUserID = null;
    private javax.swing.JTextField jTextFieldDialogUserUsername = null;
    private javax.swing.JLabel jLabelDialogUserStatus = null;
    private javax.swing.JPasswordField jPasswordFieldDialogUserPassword = null;
    private javax.swing.JPasswordField jPasswordFieldDialogUserVerifyPassword = null;
    private javax.swing.JTextField jTextFieldDialogUserFirstname = null;
    private javax.swing.JTextField jTextFieldDialogUserLastname = null;
    private javax.swing.JTextField jTextFieldDialogUserEmail = null;
    private javax.swing.JTextField jTextFieldDialogUserStatus = null;
    private javax.swing.JButton jButtonDialogUserSave = null;
    private javax.swing.JButton jButtonDialogUserCancel = null;
    private javax.swing.JDialog jDialogAddAttributesText = null;
    private javax.swing.JPanel jContentPaneDialogAddAttributesText = null;
    private javax.swing.JTextArea jTextAreaDialogAddAttributesText = null;
    private javax.swing.JButton jButtonDialogAddAttributesTextSave = null;
    private javax.swing.JButton jButtonDialogAddAttributesTextCancel = null;
    private javax.swing.JScrollPane jScrollPaneDialogAddAttributesText = null;
    private javax.swing.JDialog jDialogTransactionInfo = null;
    private javax.swing.JPanel jContentPaneDialogTransactionInfo = null;
    private javax.swing.JLabel jLabelDialogTransactionInfoText = null;
    private javax.swing.JPanel jContentPaneInformation = null;
    public javax.swing.JTextArea jLabelInternalFrameInformationText = null;
    private JPanel jContentPaneSearch = null;
    private JInternalFrame jInternalFrameSearch = null;
    private JTable jTableSearchResult = null;
    private JScrollPane jScrollPaneSearchResult = null;
    private JLabel jLabelSearchQuery = null;
    private JLabel jLabelSearchLanguage = null;
    private JLabel jLabelSearchQueryBuilder = null;
    private JLabel jLabelSearchQueryBuilderHintsLabel = null;
    private JLabel jLabelSearchQueryBuilderHints = null;
    private JTextField jTextFieldSearchQuery = null;
    private JScrollPane jScrollPaneSearchLanguage = null;
    private JList jListSearchLanguage = null;
    private JButton jButtonSearch = null;
    private JButton jButtonSearchGoToSelection = null;
    private JCheckBox jCheckBoxSearchAllLanguages = null;
    private JMenuItem jMenuItemRepositoryAddDataStructure = null;
    private JMenuItem jMenuItemRepositoryViewURL = null;
    private JMenuItem jMenuItemRepositoryEmailContact = null;
    private JComboBox jComboBoxSearchOperator = null;
    private JComboBox jComboBoxSearchField = null;
    private JTextField jTextFieldSearchKeyword = null;
    private JButton jButtonSearchAddToQuery = null;
    private JButton jButtonSearchResetQuery = null;
    private JMenuItem jMenuItemRepositoryCommentData = null;
    private JPopupMenu jPopupMenuRepository = null;
    private JPanel jContentPaneDialogLocale = null;
    private JDialog jDialogLocale = null;
    private JScrollPane jScrollPaneDialogLocale = null;
    private JList jListDialogLocale = null;
    private JButton jButtonDialogLocaleOk = null;
    
    private ConnectionDialog dialogConnection=null;
    private UserDialog userDialog=null;
    private JInternalFrame jInternalFrameInformation=null;
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
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", (Locale)xincoClientConfig.elementAt(2));
        xerb.getLocale();
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
     * This method initializes jContentPaneSearch
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPaneSearch() {
        if (jContentPaneSearch == null) {
            jLabelSearchLanguage = new JLabel();
            jLabelSearchQuery = new JLabel();
            jLabelSearchQueryBuilder = new JLabel();
            jLabelSearchQueryBuilderHintsLabel = new JLabel();
            jLabelSearchQueryBuilderHints = new JLabel();
            jContentPaneSearch = new JPanel();
            jContentPaneSearch.setLayout(null);
            jLabelSearchQuery.setBounds(10, 70, 100, 20);
            jLabelSearchQuery.setText(xerb.getString("window.search.query") + ":");
            jLabelSearchLanguage.setBounds(10, 100, 100, 20);
            jLabelSearchLanguage.setText(xerb.getString("general.language") + ":");
            jLabelSearchQueryBuilder.setBounds(10, 10, 100, 20);
            jLabelSearchQueryBuilder.setText(xerb.getString("window.search.querybuilder") + ":");
            jLabelSearchQueryBuilderHintsLabel.setBounds(10, 40, 100, 20);
            jLabelSearchQueryBuilderHintsLabel.setText(xerb.getString("window.search.querybuilderhintslabel"));
            jLabelSearchQueryBuilderHints.setBounds(120, 40, 550, 20);
            jLabelSearchQueryBuilderHints.setText(xerb.getString("window.search.querybuilderhints"));
            jContentPaneSearch.add(jLabelSearchQueryBuilder, null);
            jContentPaneSearch.add(getJComboBoxSearchOperator(), null);
            jContentPaneSearch.add(getJComboBoxSearchField(), null);
            jContentPaneSearch.add(getJTextFieldSearchKeyword(), null);
            jContentPaneSearch.add(getJButtonSearchAddToQuery(), null);
            jContentPaneSearch.add(jLabelSearchQueryBuilderHintsLabel, null);
            jContentPaneSearch.add(jLabelSearchQueryBuilderHints, null);
            jContentPaneSearch.add(jLabelSearchQuery, null);
            jContentPaneSearch.add(getJTextFieldSearchQuery(), null);
            jContentPaneSearch.add(getJButtonSearchResetQuery(), null);
            jContentPaneSearch.add(getJScrollPaneSearchLanguage(), null);
            jContentPaneSearch.add(jLabelSearchLanguage, null);
            jContentPaneSearch.add(getJCheckBoxSearchAllLanguages(), null);
            jContentPaneSearch.add(getJButtonSearch(), null);
            jContentPaneSearch.add(getJScrollPaneSearchResult(), null);
            jContentPaneSearch.add(getJButtonSearchGoToSelection(), null);
        }
        return jContentPaneSearch;
    }
    /**
     * This method initializes jInternalFrameSearch
     *
     * @return javax.swing.JInternalFrame
     */
    private JInternalFrame getJInternalFrameSearch() {
        if (jInternalFrameSearch == null) {
            jInternalFrameSearch = new JInternalFrame();
            //jInternalFrameSearch.setBounds(450, 50, 500, 400);
            jInternalFrameSearch.setBounds(this.getWidth()-750, this.getHeight()-710, 700, 460);
            jInternalFrameSearch.setContentPane(getJContentPaneSearch());
            jInternalFrameSearch.setIconifiable(true);
            jInternalFrameSearch.setClosable(true);
            jInternalFrameSearch.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
            jInternalFrameSearch.setTitle(xerb.getString("window.search"));
            jInternalFrameSearch.getRootPane().setDefaultButton(getJButtonSearch());
        }
        //processing independent of creation
        int i = 0;
        int j = 0;
        String text = "";
        int selection = -1;
        int alt_selection = 0;
        DefaultComboBoxModel dcbm;
        DefaultListModel dlm;
        XincoCoreDataType xcdt = null;
        //fill operator list
        dcbm = (DefaultComboBoxModel)jComboBoxSearchOperator.getModel();
        dcbm.removeAllElements();
        dcbm.addElement(" ");
        dcbm.addElement("AND");
        dcbm.addElement("OR");
        dcbm.addElement("NOT");
        dcbm.addElement("+");
        dcbm.addElement("-");
        jComboBoxSearchOperator.setSelectedIndex(0);
        //load fields
        dcbm = (DefaultComboBoxModel)jComboBoxSearchField.getModel();
        dcbm.removeAllElements();
        dcbm.addElement(" ");
        dcbm.addElement(xerb.getString("window.search.filecontent") + " (file)");
        text = "";
        for (i=0;i<xincoClientSession.server_datatypes.size();i++) {
            xcdt = (XincoCoreDataType)xincoClientSession.server_datatypes.elementAt(i);
            for (j=0;j<xcdt.getXinco_core_data_type_attributes().size();j++) {
                text = ((XincoCoreDataTypeAttribute)xcdt.getXinco_core_data_type_attributes().elementAt(j)).getDesignation();
                dcbm.addElement(text);
            }
        }
        jComboBoxSearchField.setSelectedIndex(0);
        //load languages
        dlm = (DefaultListModel)jListSearchLanguage.getModel();
        dlm.removeAllElements();
        selection = -1;
        alt_selection = 0;
        text = "";
        for (i=0;i<xincoClientSession.server_languages.size();i++) {
            text = ((XincoCoreLanguage)xincoClientSession.server_languages.elementAt(i)).getDesignation() + " (" + ((XincoCoreLanguage)xincoClientSession.server_languages.elementAt(i)).getSign() + ")";
            dlm.addElement(text);
            if (((XincoCoreLanguage)xincoClientSession.server_languages.elementAt(i)).getSign().toLowerCase().compareTo(Locale.getDefault().getLanguage().toLowerCase()) == 0) {
                selection = i;
            }
            if (((XincoCoreLanguage)xincoClientSession.server_languages.elementAt(i)).getId() == 1) {
                alt_selection = i;
            }
        }
        if (selection == -1) {
            selection = alt_selection;
        }
        jListSearchLanguage.setSelectedIndex(selection);
        jListSearchLanguage.ensureIndexIsVisible(jListSearchLanguage.getSelectedIndex());
        return jInternalFrameSearch;
    }
    /**
     * This method initializes jTable
     *
     * @return javax.swing.JTable
     */
    private JTable getJTableSearchResult() {
        if (jTableSearchResult == null) {
            String[] cn = {xerb.getString("window.search.table.designation"),xerb.getString("window.search.table.path")};
            DefaultTableModel dtm = new DefaultTableModel(cn, 0);
            jTableSearchResult = new JTable();
            jTableSearchResult.setModel(dtm);
            jTableSearchResult.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jTableSearchResult.setCellSelectionEnabled(false);
            jTableSearchResult.setColumnSelectionAllowed(false);
            jTableSearchResult.setRowSelectionAllowed(true);
            jTableSearchResult.setAutoscrolls(true);
            jTableSearchResult.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        return jTableSearchResult;
    }
    /**
     * This method initializes jScrollPaneSearchResult
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneSearchResult() {
        if (jScrollPaneSearchResult == null) {
            jScrollPaneSearchResult = new JScrollPane();
            jScrollPaneSearchResult.setBounds(10, 210, 660, 150);
            jScrollPaneSearchResult.setViewportView(getJTableSearchResult());
            jScrollPaneSearchResult.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPaneSearchResult.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        }
        return jScrollPaneSearchResult;
    }
    /**
     * This method initializes jTextFieldSearchQuery
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldSearchQuery() {
        if (jTextFieldSearchQuery == null) {
            jTextFieldSearchQuery = new JTextField();
            jTextFieldSearchQuery.setBounds(120, 70, 430, 20);
        }
        return jTextFieldSearchQuery;
    }
    /**
     * This method initializes jScrollPaneSearchLanguage
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPaneSearchLanguage() {
        if (jScrollPaneSearchLanguage == null) {
            jScrollPaneSearchLanguage = new JScrollPane();
            jScrollPaneSearchLanguage.setBounds(120, 100, 550, 60);
            jScrollPaneSearchLanguage.setViewportView(getJListSearchLanguage());
            jScrollPaneSearchLanguage.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        return jScrollPaneSearchLanguage;
    }
    /**
     * This method initializes jListSearchLanguage
     *
     * @return javax.swing.JList
     */
    private JList getJListSearchLanguage() {
        if (jListSearchLanguage == null) {
            DefaultListModel dlm = new DefaultListModel();
            jListSearchLanguage = new JList();
            jListSearchLanguage.setModel(dlm);
            jListSearchLanguage.setEnabled(false);
        }
        return jListSearchLanguage;
    }
    /**
     * This method initializes jButtonSearch
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonSearch() {
        if (jButtonSearch == null) {
            jButtonSearch = new JButton();
            jButtonSearch.setBounds(450, 170, 220, 30);
            jButtonSearch.setText(xerb.getString("window.search"));
            jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int i=0, j=0;
                    //select language OR all languages!
                    XincoCoreLanguage lid = new XincoCoreLanguage();
                    lid.setId(0);
                    if ((!jCheckBoxSearchAllLanguages.isSelected()) && (jListSearchLanguage.getSelectedIndex() >= 0)) {
                        lid = (XincoCoreLanguage)xincoClientSession.server_languages.elementAt(jListSearchLanguage.getSelectedIndex());
                    }
                    try {
                        if ((xincoClientSession.currentSearchResult = xincoClientSession.xinco.findXincoCoreData(jTextFieldSearchQuery.getText(), lid, xincoClientSession.user)) == null) {
                            throw new XincoException();
                        }
                    } catch (Exception rme) {
                        xincoClientSession.currentSearchResult = new Vector();
                    }
                    //update search result
                    String[] rdata = {"", ""};
                    DefaultTableModel dtm = (DefaultTableModel)jTableSearchResult.getModel();
                    j = dtm.getRowCount();
                    for (i=0;i<j;i++) {
                        dtm.removeRow(0);
                    }
                    for (i=0;i<xincoClientSession.currentSearchResult.size();i++) {
                        rdata[0] = ((XincoCoreData)(((Vector)xincoClientSession.currentSearchResult.elementAt(i)).elementAt(0))).getDesignation() + " (" + ((XincoCoreData)(((Vector)xincoClientSession.currentSearchResult.elementAt(i)).elementAt(0))).getXinco_core_data_type().getDesignation() + " | " + ((XincoCoreData)(((Vector)xincoClientSession.currentSearchResult.elementAt(i)).elementAt(0))).getXinco_core_language().getSign() + ")";
                        rdata[1] = new String("");
                        for (j=1;j<((Vector)xincoClientSession.currentSearchResult.elementAt(i)).size();j++) {
                            rdata[1] = rdata[1] + ((XincoCoreNode)(((Vector)xincoClientSession.currentSearchResult.elementAt(i)).elementAt(j))).getDesignation() + " / ";
                        }
                        dtm.addRow(rdata);
                    }
                }
            });
        }
        return jButtonSearch;
    }
    /**
     * This method initializes jButtonSearchGoToSelection
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonSearchGoToSelection() {
        if (jButtonSearchGoToSelection == null) {
            jButtonSearchGoToSelection = new JButton();
            jButtonSearchGoToSelection.setBounds(450, 380, 220, 30);
            jButtonSearchGoToSelection.setText(xerb.getString("window.search.gotoselection"));
            jButtonSearchGoToSelection.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!(jTableSearchResult.getSelectedRow() >= 0)) {
                        return;
                    }
                    Vector v = (Vector)xincoClientSession.currentSearchResult.elementAt(jTableSearchResult.getSelectedRow());
                    int i = 0;
                    int j = 0;
                    int k = 0;
                    TreePath tp = null;
                    try {
                        //expand tree to selected result (check root items first, then check all sub-folders)
                        XincoMutableTreeNode xmtn = (XincoMutableTreeNode)xincoClientSession.xincoClientRepository.treemodel.getRoot();
                        if (xmtn.getUserObject().getClass() == XincoCoreNode.class) {
                            if (((XincoCoreNode)xmtn.getUserObject()).getId() == ((XincoCoreNode)v.elementAt(1)).getId()) {
                                tp = new TreePath(xmtn.getPath());
                                jTreeRepository.setSelectionPath(tp);
                                jTreeRepository.expandPath(tp);
                                j = -1;
                                //select data
                                if (1 == (v.size()-1)) {
                                    for (k=0;k<xmtn.getChildCount();k++) {
                                        if (((XincoMutableTreeNode)xmtn.getChildAt(k)).getUserObject().getClass() == XincoCoreData.class) {
                                            if (((XincoCoreData)((XincoMutableTreeNode)xmtn.getChildAt(k)).getUserObject()).getId() == ((XincoCoreData)v.elementAt(0)).getId()) {
                                                tp = new TreePath(((XincoMutableTreeNode)xmtn.getChildAt(k)).getPath());
                                                jTreeRepository.setSelectionPath(tp);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        for (i=2;i<v.size();i++) {
                            for (j=0;j<xmtn.getChildCount();j++) {
                                if (((XincoMutableTreeNode)xmtn.getChildAt(j)).getUserObject().getClass() == XincoCoreNode.class) {
                                    if (((XincoCoreNode)((XincoMutableTreeNode)xmtn.getChildAt(j)).getUserObject()).getId() == ((XincoCoreNode)v.elementAt(i)).getId()) {
                                        tp = new TreePath(((XincoMutableTreeNode)xmtn.getChildAt(j)).getPath());
                                        jTreeRepository.setSelectionPath(tp);
                                        jTreeRepository.expandPath(tp);
                                        xmtn = (XincoMutableTreeNode)xmtn.getChildAt(j);
                                        j = -1;
                                        //select data
                                        if (i == (v.size()-1)) {
                                            for (k=0;k<xmtn.getChildCount();k++) {
                                                if (((XincoMutableTreeNode)xmtn.getChildAt(k)).getUserObject().getClass() == XincoCoreData.class) {
                                                    if (((XincoCoreData)((XincoMutableTreeNode)xmtn.getChildAt(k)).getUserObject()).getId() == ((XincoCoreData)v.elementAt(0)).getId()) {
                                                        tp = new TreePath(((XincoMutableTreeNode)xmtn.getChildAt(k)).getPath());
                                                        jTreeRepository.setSelectionPath(tp);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception tee) {
                    }
                }
            });
        }
        return jButtonSearchGoToSelection;
    }
    /**
     * This method initializes jCheckBoxSearchAllLanguages
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getJCheckBoxSearchAllLanguages() {
        if (jCheckBoxSearchAllLanguages == null) {
            jCheckBoxSearchAllLanguages = new JCheckBox();
            jCheckBoxSearchAllLanguages.setBounds(120, 170, 120, 20);
            jCheckBoxSearchAllLanguages.setText(xerb.getString("window.search.alllanguages"));
            jCheckBoxSearchAllLanguages.setSelected(true);
            jCheckBoxSearchAllLanguages.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    jListSearchLanguage.setEnabled(!jCheckBoxSearchAllLanguages.isSelected());
                }
            });
        }
        return jCheckBoxSearchAllLanguages;
    }
    /**
     * This method initializes jMenuItemRepositoryAddDataStructure
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemRepositoryAddDataStructure() {
        if (jMenuItemRepositoryAddDataStructure == null) {
            jMenuItemRepositoryAddDataStructure = new JMenuItem();
            jMenuItemRepositoryAddDataStructure.setText(xerb.getString("menu.repository.adddatastructure"));
            jMenuItemRepositoryAddDataStructure.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryAddDataStructure.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //import data structure
                    if (xincoClientSession.currentTreeNodeSelection != null) {
                        if (xincoClientSession.currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                            JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("window.massiveimport.info"), xerb.getString("window.massiveimport"), JOptionPane.INFORMATION_MESSAGE);
                            try {
                                JFileChooser fc = new JFileChooser();
                                fc.setCurrentDirectory(new File(current_path));
                                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                                //show dialog
                                int result = fc.showOpenDialog(XincoExplorer.this);
                                if(result == JFileChooser.APPROVE_OPTION) {
                                    setCurrentPath(fc.getSelectedFile().toString());
                                } else {
                                    throw new XincoException(xerb.getString("datawizard.updatecancel"));
                                }
                                //update transaction info
                                JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("window.massiveimport.progress"), xerb.getString("window.massiveimport"), JOptionPane.INFORMATION_MESSAGE);
                                jLabelInternalFrameInformationText.setText(xerb.getString("window.massiveimport.progress"));
                                importContentOfFolder((XincoCoreNode)xincoClientSession.currentTreeNodeSelection.getUserObject(), new File(current_path));
                                //select current path
                                jTreeRepository.setSelectionPath(new TreePath(xincoClientSession.currentTreeNodeSelection.getPath()));
                                //update transaction info
                                jLabelInternalFrameInformationText.setText(xerb.getString("window.massiveimport.importsuccess"));
                            } catch (Exception ie) {
                                JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("window.massiveimport.importfailed") + " " + xerb.getString("general.reason") + ": " + ie.toString(), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                                jLabelInternalFrameInformationText.setText("");
                            }
                        }
                    }
                }
            });
        }
        return jMenuItemRepositoryAddDataStructure;
    }
    /**
     * This method initializes jMenuItemRepositoryViewURL
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemRepositoryViewURL() {
        if (jMenuItemRepositoryViewURL == null) {
            jMenuItemRepositoryViewURL = new JMenuItem();
            jMenuItemRepositoryViewURL.setText(xerb.getString("menu.repository.viewurl"));
            jMenuItemRepositoryViewURL.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryViewURL.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(8);
                }
            });
        }
        return jMenuItemRepositoryViewURL;
    }
    /**
     * This method initializes jMenuItemRepositoryEmailContact
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemRepositoryEmailContact() {
        if (jMenuItemRepositoryEmailContact == null) {
            jMenuItemRepositoryEmailContact = new JMenuItem();
            jMenuItemRepositoryEmailContact.setText(xerb.getString("menu.repository.emailcontact"));
            jMenuItemRepositoryEmailContact.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryEmailContact.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(9);
                }
            });
        }
        return jMenuItemRepositoryEmailContact;
    }
    /**
     * This method initializes jComboBoxSearchOperator
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBoxSearchOperator() {
        if (jComboBoxSearchOperator == null) {
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
            jComboBoxSearchOperator = new JComboBox();
            jComboBoxSearchOperator.setModel(dcbm);
            jComboBoxSearchOperator.setBounds(120, 10, 50, 20);
            jComboBoxSearchOperator.setEditable(false);
        }
        return jComboBoxSearchOperator;
    }
    /**
     * This method initializes jComboBoxSearchField
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBoxSearchField() {
        if (jComboBoxSearchField == null) {
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
            jComboBoxSearchField = new JComboBox();
            jComboBoxSearchField.setModel(dcbm);
            jComboBoxSearchField.setBounds(180, 10, 150, 20);
            jComboBoxSearchField.setEditable(false);
        }
        return jComboBoxSearchField;
    }
    /**
     * This method initializes jTextFieldSearchKeyword
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldSearchKeyword() {
        if (jTextFieldSearchKeyword == null) {
            jTextFieldSearchKeyword = new JTextField();
            jTextFieldSearchKeyword.setBounds(340, 10, 210, 20);
        }
        return jTextFieldSearchKeyword;
    }
    /**
     * This method initializes jButtonSearchAddToQuery
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonSearchAddToQuery() {
        if (jButtonSearchAddToQuery == null) {
            jButtonSearchAddToQuery = new JButton();
            jButtonSearchAddToQuery.setBounds(560, 10, 110, 20);
            jButtonSearchAddToQuery.setText(xerb.getString("window.search.addtoquery"));
            jButtonSearchAddToQuery.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String operator = "";
                    String field = "";
                    //create operator string
                    if (jComboBoxSearchOperator.getSelectedIndex() != 0) {
                        operator = (String)jComboBoxSearchOperator.getSelectedItem();
                        operator = operator + " ";
                    } else {
                        operator = "";
                    }
                    //create field string
                    if (jComboBoxSearchField.getSelectedIndex() > 1) {
                        field = ((String)jComboBoxSearchField.getSelectedItem()) + ":";
                    } else if (jComboBoxSearchField.getSelectedIndex() == 1) {
                        field = "file:";
                    } else {
                        field = "";
                    }
                    // append to query
                    jTextFieldSearchQuery.setText(jTextFieldSearchQuery.getText() + operator + field + jTextFieldSearchKeyword.getText() + " ");
                    jTextFieldSearchKeyword.setText("");
                }
            });
        }
        return jButtonSearchAddToQuery;
    }
    /**
     * This method initializes jButtonSearchResetQuery
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonSearchResetQuery() {
        if (jButtonSearchResetQuery == null) {
            jButtonSearchResetQuery = new JButton();
            jButtonSearchResetQuery.setBounds(560, 70, 110, 20);
            jButtonSearchResetQuery.setText(xerb.getString("general.reset"));
            jButtonSearchResetQuery.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jTextFieldSearchQuery.setText("");
                }
            });
        }
        return jButtonSearchResetQuery;
    }
    /**
     * This method initializes jMenuItemRepositoryCommentData
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemRepositoryCommentData() {
        if (jMenuItemRepositoryCommentData == null) {
            jMenuItemRepositoryCommentData = new JMenuItem();
            jMenuItemRepositoryCommentData.setText(xerb.getString("menu.edit.commentdata"));
            jMenuItemRepositoryCommentData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryCommentData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(13);
                }
            });
        }
        return jMenuItemRepositoryCommentData;
    }
    /**
     * This method initializes jPopupMenuRepository
     *
     * @return javax.swing.JPopupMenu
     */
    private JPopupMenu getJPopupMenuRepository() {
        JMenuItem tmi = null;
        jPopupMenuRepository = new JPopupMenu();
        jPopupMenuRepository.setEnabled(jMenuRepository.isEnabled());
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryRefresh().getText());
        tmi.setEnabled(getJMenuItemRepositoryRefresh().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryRefresh().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        jPopupMenuRepository.addSeparator();
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryAddFolder().getText());
        tmi.setEnabled(getJMenuItemRepositoryAddFolder().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryAddFolder().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryAddData().getText());
        tmi.setEnabled(getJMenuItemRepositoryAddData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryAddData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryAddDataStructure().getText());
        tmi.setEnabled(getJMenuItemRepositoryAddDataStructure().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryAddDataStructure().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        jPopupMenuRepository.addSeparator();
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryEditFolderData().getText());
        tmi.setEnabled(getJMenuItemRepositoryEditFolderData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryEditFolderData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryViewEditAddAttributes().getText());
        tmi.setEnabled(getJMenuItemRepositoryViewEditAddAttributes().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryViewEditAddAttributes().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryEditFolderDataACL().getText());
        tmi.setEnabled(getJMenuItemRepositoryEditFolderDataACL().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryEditFolderDataACL().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        jPopupMenuRepository.addSeparator();
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryMoveFolderData().getText());
        tmi.setEnabled(getJMenuItemRepositoryMoveFolderData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryMoveFolderData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryInsertFolderData().getText());
        tmi.setEnabled(getJMenuItemRepositoryInsertFolderData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryInsertFolderData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        jPopupMenuRepository.addSeparator();
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryViewURL().getText());
        tmi.setEnabled(getJMenuItemRepositoryViewURL().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryViewURL().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryEmailContact().getText());
        tmi.setEnabled(getJMenuItemRepositoryEmailContact().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryEmailContact().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryViewData().getText());
        tmi.setEnabled(getJMenuItemRepositoryViewData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryViewData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryCheckoutData().getText());
        tmi.setEnabled(getJMenuItemRepositoryCheckoutData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryCheckoutData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryUndoCheckoutData().getText());
        tmi.setEnabled(getJMenuItemRepositoryUndoCheckoutData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryUndoCheckoutData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryCheckinData().getText());
        tmi.setEnabled(getJMenuItemRepositoryCheckinData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryCheckinData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryPublishData().getText());
        tmi.setEnabled(getJMenuItemRepositoryPublishData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryPublishData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryLockData().getText());
        tmi.setEnabled(getJMenuItemRepositoryLockData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryLockData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryDownloadRevision().getText());
        tmi.setEnabled(getJMenuItemRepositoryDownloadRevision().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryDownloadRevision().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryCommentData().getText());
        tmi.setEnabled(getJMenuItemRepositoryCommentData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryCommentData().doClick();
            }
        });
        jPopupMenuRepository.add(tmi);
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
            jDialogLocale.getRootPane().setDefaultButton(getJButtonDialogLocaleOk());
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
        xincoClientVersion.setVersion_high(2);
        xincoClientVersion.setVersion_mid(0);
        xincoClientVersion.setVersion_low(0);
        xincoClientVersion.setVersion_postfix(" Beta");
        switchPLAF((String)xincoClientConfig.elementAt(1));
        this.setBounds(0, 0, (new Double(getToolkit().getScreenSize().getWidth())).intValue()-100,
                (new Double(getToolkit().getScreenSize().getHeight())).intValue()-75);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setName("XincoExplorer");
        this.setTitle(xerb.getString("general.clienttitle") + " - " +
                xerb.getString("general.version") + " " +
                xincoClientVersion.getVersion_high() + "." +
                xincoClientVersion.getVersion_mid() + "." +
                xincoClientVersion.getVersion_low() + "" +
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
            jMenuConnection.setName("Connection");
            jMenuConnection.setText(xerb.getString("menu.connection"));
        }
        return jMenuConnection;
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
    private javax.swing.JMenu getJMenuRepository() {
        if(jMenuRepository == null) {
            jMenuRepository = new javax.swing.JMenu();
            jMenuRepository.add(getJMenuItemRepositoryRefresh());
            jMenuRepository.addSeparator();
            jMenuRepository.add(getJMenuItemRepositoryAddFolder());
            jMenuRepository.add(getJMenuItemRepositoryAddData());
            jMenuRepository.add(getJMenuItemRepositoryAddDataStructure());
            jMenuRepository.addSeparator();
            jMenuRepository.add(getJMenuItemRepositoryEditFolderData());
            jMenuRepository.add(getJMenuItemRepositoryViewEditAddAttributes());
            jMenuRepository.add(getJMenuItemRepositoryEditFolderDataACL());
            jMenuRepository.addSeparator();
            jMenuRepository.add(getJMenuItemRepositoryMoveFolderData());
            jMenuRepository.add(getJMenuItemRepositoryInsertFolderData());
            jMenuRepository.addSeparator();
            jMenuRepository.add(getJMenuItemRepositoryViewURL());
            jMenuRepository.add(getJMenuItemRepositoryEmailContact());
            jMenuRepository.add(getJMenuItemRepositoryViewData());
            jMenuRepository.add(getJMenuItemRepositoryCheckoutData());
            jMenuRepository.add(getJMenuItemRepositoryUndoCheckoutData());
            jMenuRepository.add(getJMenuItemRepositoryCheckinData());
            jMenuRepository.add(getJMenuItemRepositoryPublishData());
            jMenuRepository.add(getJMenuItemRepositoryLockData());
            jMenuRepository.add(getJMenuItemRepositoryDownloadRevision());
            jMenuRepository.add(getJMenuItemRepositoryCommentData());
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
            jMenuItemConnectionDisconnect = new javax.swing.JMenuItem();
            jMenuItemConnectionDisconnect.setName("Disconnect");
            jMenuItemConnectionDisconnect.setText(xerb.getString("menu.disconnect"));
            jMenuItemConnectionDisconnect.setEnabled(false);
            jMenuItemConnectionDisconnect.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    xincoClientSession.status = 0;
                    markConnectionStatus();
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
            jMenuItemAboutAboutXinco = new javax.swing.JMenuItem();
            jMenuItemAboutAboutXinco.setName("AboutXinco");
            jMenuItemAboutAboutXinco.setText(xerb.getString("menu.aboutxinco"));
            jMenuItemAboutAboutXinco.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
            jMenuItemAboutAboutXinco.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String message_string = "";
                    message_string = message_string + xerb.getString("window.aboutxinco.clienttitle") + "\n";
                    message_string = message_string + xerb.getString("window.aboutxinco.clientversion") + ": " + xincoClientVersion.getVersion_high() + "." +xincoClientVersion.getVersion_mid() + "." + xincoClientVersion.getVersion_low() + xincoClientVersion.getVersion_postfix() + "\n";
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
            jDesktopPane.add(getJInternalFrameSearch(), null);
            jDesktopPane.add(getJInternalFrameInformation(), null);
            jDesktopPane.setLayer(getJInternalFrameRepository(), 0);
            jDesktopPane.setLayer(getJInternalFrameSearch(), 1);
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
            //jInternalFrameRepository.setBounds(5, 5, 900, 550);
            jInternalFrameRepository.setBounds(5, 5, this.getWidth()-100, this.getHeight()-150);
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
        if(jSplitPaneRepository == null) {
            jSplitPaneRepository = new javax.swing.JSplitPane();
            jSplitPaneRepository.setLeftComponent(getJScrollPaneRepositoryTree());
            jSplitPaneRepository.setRightComponent(getJScrollPaneRepositoryTable());
            jSplitPaneRepository.setDividerLocation(2.0/3.0);
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
    private javax.swing.JScrollPane getJScrollPaneRepositoryTable() {
        if(jScrollPaneRepositoryTable == null) {
            jScrollPaneRepositoryTable = new javax.swing.JScrollPane();
            jScrollPaneRepositoryTable.setViewportView(getJTableRepository());
        }
        return jScrollPaneRepositoryTable;
    }
    /**
     * This method initializes jTreeRepository
     *
     * @return javax.swing.JTree
     */
    public javax.swing.JTree getJTreeRepository() {
        if(jTreeRepository == null) {
            jTreeRepository = new javax.swing.JTree();
            jTreeRepository.setModel(xincoClientSession.xincoClientRepository.treemodel);
            //enable tool tips.
            ToolTipManager.sharedInstance().registerComponent(jTreeRepository);
            //set custom cell tree renderer
            jTreeRepository.setCellRenderer(new XincoTreeCellRenderer());
            jTreeRepository.setRootVisible(true);
            jTreeRepository.setEditable(false);
            DefaultTreeSelectionModel dtsm = new DefaultTreeSelectionModel();
            dtsm.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            jTreeRepository.setSelectionModel(dtsm);
            jTreeRepository.addMouseListener(new MouseInputListener() {
                public void mouseMoved(MouseEvent event) {
                }
                public void mouseDragged(MouseEvent event) {
                }
                public void mouseEntered(MouseEvent event) {
                }
                public void mouseExited(MouseEvent event) {
                }
                public void mousePressed(MouseEvent event) {
                    if (event.isPopupTrigger()) {
                        getJPopupMenuRepository();
                        jPopupMenuRepository.show(event.getComponent(), event.getX(), event.getY());
                    }
                }
                public void mouseClicked(MouseEvent event) {
                }
                public void mouseReleased(MouseEvent event) {
                    if (event.isPopupTrigger()) {
                        getJPopupMenuRepository();
                        jPopupMenuRepository.show(event.getComponent(), event.getX(), event.getY());
                    }
                }
            }
            );
            jTreeRepository.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
                public void treeExpanded(javax.swing.event.TreeExpansionEvent e) {
                    //node expanded
                }
                public void treeCollapsed(javax.swing.event.TreeExpansionEvent e) {}
            });
            jTreeRepository.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
                public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
                    int i = 0, j = 0;
                    XincoCoreACE temp_ace = new XincoCoreACE();
                    TreePath tp = e.getPath();
                    //get node selected
                    XincoMutableTreeNode node = (XincoMutableTreeNode)tp.getLastPathComponent();
                    //set current node of session
                    xincoClientSession.currentTreeNodeSelection = node;
                    
                    //get ace
                    if (node.getUserObject().getClass() == XincoCoreNode.class) {
                        temp_ace = XincoCoreACEClient.checkAccess(xincoClientSession.user, ((XincoCoreNode)node.getUserObject()).getXinco_core_acl());
                    }
                    if (node.getUserObject().getClass() == XincoCoreData.class) {
                        temp_ace = XincoCoreACEClient.checkAccess(xincoClientSession.user, ((XincoCoreData)node.getUserObject()).getXinco_core_acl());
                    }
                    
                    //intelligent menu
                    //reset menus
                    jMenuItemRepositoryAddFolder.setEnabled(false);
                    jMenuItemRepositoryAddData.setEnabled(false);
                    jMenuItemRepositoryAddDataStructure.setEnabled(false);
                    jMenuItemRepositoryEditFolderData.setEnabled(false);
                    jMenuItemRepositoryViewEditAddAttributes.setEnabled(false);
                    jMenuItemRepositoryEditFolderDataACL.setEnabled(false);
                    jMenuItemRepositoryMoveFolderData.setEnabled(false);
                    jMenuItemRepositoryInsertFolderData.setEnabled(false);
                    jMenuItemRepositoryViewData.setEnabled(false);
                    jMenuItemRepositoryViewURL.setEnabled(false);
                    jMenuItemRepositoryEmailContact.setEnabled(false);
                    jMenuItemRepositoryCheckoutData.setEnabled(false);
                    jMenuItemRepositoryUndoCheckoutData.setEnabled(false);
                    jMenuItemRepositoryCheckinData.setEnabled(false);
                    jMenuItemRepositoryPublishData.setEnabled(false);
                    jMenuItemRepositoryLockData.setEnabled(false);
                    jMenuItemRepositoryDownloadRevision.setEnabled(false);
                    jMenuItemRepositoryCommentData.setEnabled(false);
                    //dynamic enabling
                    if (temp_ace.isWrite_permission()) {
                        //jMenuItemRepositoryEditFolderData.setEnabled(true);
                        jMenuItemRepositoryMoveFolderData.setEnabled(true);
                    }
                    if (temp_ace.isAdmin_permission()) {
                        jMenuItemRepositoryEditFolderDataACL.setEnabled(true);
                    }
                    if (node.getUserObject().getClass() == XincoCoreNode.class) {
                        if (temp_ace.isWrite_permission()) {
                            if (((XincoCoreNode)node.getUserObject()).getStatus_number() == 1) {
                                jMenuItemRepositoryEditFolderData.setEnabled(true);
                            }
                            jMenuItemRepositoryAddFolder.setEnabled(true);
                            jMenuItemRepositoryAddData.setEnabled(true);
                            jMenuItemRepositoryAddDataStructure.setEnabled(true);
                            if (xincoClientSession.clipboardTreeNodeSelection.size() > 0) {
                                jMenuItemRepositoryInsertFolderData.setEnabled(true);
                            }
                        }
                    }
                    if (node.getUserObject().getClass() == XincoCoreData.class) {
                        //file = 1
                        if (((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getId() == 1) {
                            if (temp_ace.isRead_permission()) {
                                if (((XincoCoreData)node.getUserObject()).getStatus_number() != 3) {
                                    jMenuItemRepositoryViewData.setEnabled(true);
                                    jMenuItemRepositoryDownloadRevision.setEnabled(true);
                                }
                            }
                            if (temp_ace.isWrite_permission()) {
                                if(((XincoCoreData)node.getUserObject()).getStatus_number() == 1) {
                                    jMenuItemRepositoryCheckoutData.setEnabled(true);
                                    jMenuItemRepositoryUndoCheckoutData.setEnabled(false);
                                    jMenuItemRepositoryCheckinData.setEnabled(false);
                                }
                                if (((XincoCoreData)node.getUserObject()).getStatus_number() == 4) {
                                    jMenuItemRepositoryCheckoutData.setEnabled(false);
                                    jMenuItemRepositoryUndoCheckoutData.setEnabled(true);
                                    jMenuItemRepositoryCheckinData.setEnabled(true);
                                }
                            }
                        }
                        //URL = 3
                        if (((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getId() == 3) {
                            if (temp_ace.isRead_permission()) {
                                jMenuItemRepositoryViewURL.setEnabled(true);
                            }
                        }
                        //contact = 4
                        if (((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getId() == 4) {
                            if (temp_ace.isRead_permission()) {
                                jMenuItemRepositoryEmailContact.setEnabled(true);
                            }
                        }
                        if (temp_ace.isRead_permission()) {
                            jMenuItemRepositoryViewEditAddAttributes.setEnabled(true);
                        }
                        if (temp_ace.isWrite_permission()) {
                            jMenuItemRepositoryCommentData.setEnabled(true);
                            if (((XincoCoreData)node.getUserObject()).getStatus_number() == 1) {
                                jMenuItemRepositoryEditFolderData.setEnabled(true);
                            }
                        }
                        if (temp_ace.isAdmin_permission()) {
                            if ((((XincoCoreData)node.getUserObject()).getStatus_number() != 3) && (((XincoCoreData)node.getUserObject()).getStatus_number() != 4)) {
                                jMenuItemRepositoryPublishData.setEnabled(true);
                            }
                            if ((((XincoCoreData)node.getUserObject()).getStatus_number() != 2) && (((XincoCoreData)node.getUserObject()).getStatus_number() != 3)) {
                                jMenuItemRepositoryLockData.setEnabled(true);
                            }
                        }
                    }
                    
                    //only nodes have children
                    if (node.getUserObject().getClass() == XincoCoreNode.class) {
                        //check for children only if none have been found yet
                        if ((((XincoCoreNode)node.getUserObject()).getXinco_core_nodes().size() == 0) && (((XincoCoreNode)node.getUserObject()).getXinco_core_data().size() == 0)) {
                            try {
                                XincoCoreNode xnode = xincoClientSession.xinco.getXincoCoreNode((XincoCoreNode)node.getUserObject(), xincoClientSession.user);
                                if (xnode != null) {
                                    xincoClientSession.xincoClientRepository.assignObject2TreeNode(node, xnode, xincoClientSession.xinco, xincoClientSession.user, 2);
                                    //jTreeRepository.expandPath(new TreePath(xincoClientSession.xincoClientRepository.treemodel.getPathToRoot(node)));
                                } else {
                                    JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("error.folder.sufficientrights"), xerb.getString("error.accessdenied"), JOptionPane.WARNING_MESSAGE);
                                }
                            } catch (Exception rmie) {
                            }
                        }
                    }
                    
                    //load full data
                    if (node.getUserObject().getClass() == XincoCoreData.class) {
                        try {
                            XincoCoreData xdata = xincoClientSession.xinco.getXincoCoreData((XincoCoreData)node.getUserObject(), xincoClientSession.user);
                            if (xdata != null) {
                                node.setUserObject(xdata);
                                xincoClientSession.xincoClientRepository.treemodel.nodeChanged(node);
                            } else {
                                JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("error.data.sufficientrights"), xerb.getString("error.accessdenied"), JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (Exception rmie) {
                        }
                    }
                    
                    //update details table
                    if (node.getUserObject().getClass() == XincoCoreNode.class) {
                        DefaultTableModel dtm = (DefaultTableModel)jTableRepository.getModel();
                        j = dtm.getRowCount();
                        for (i=0;i<j;i++) {
                            dtm.removeRow(0);
                        }
                        String[] rdata = {"",""};
                        rdata[0] = xerb.getString("general.id");
                        rdata[1] = "" + ((XincoCoreNode)node.getUserObject()).getId();
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.designation");
                        rdata[1] = ((XincoCoreNode)node.getUserObject()).getDesignation();
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.language");
                        rdata[1] = ((XincoCoreNode)node.getUserObject()).getXinco_core_language().getDesignation() + " (" + ((XincoCoreNode)node.getUserObject()).getXinco_core_language().getSign() + ")";
                        dtm.addRow(rdata);
                        rdata[0] = "";
                        rdata[1] = "";
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.accessrights");
                        rdata[1] = "";
                        rdata[1] = rdata[1] + "[";
                        if (temp_ace.isRead_permission()) {
                            rdata[1] = rdata[1] + "R";
                        } else {
                            rdata[1] = rdata[1] + "-";
                        }
                        if (temp_ace.isWrite_permission()) {
                            rdata[1] = rdata[1] + "W";
                        } else {
                            rdata[1] = rdata[1] + "-";
                        }
                        if (temp_ace.isExecute_permission()) {
                            rdata[1] = rdata[1] + "X";
                        } else {
                            rdata[1] = rdata[1] + "-";
                        }
                        if (temp_ace.isAdmin_permission()) {
                            rdata[1] = rdata[1] + "A";
                        } else {
                            rdata[1] = rdata[1] + "-";
                        }
                        rdata[1] = rdata[1] + "]";
                        dtm.addRow(rdata);
                        rdata[0] = "";
                        rdata[1] = "";
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.status");
                        rdata[1] = "";
                        if (((XincoCoreNode)node.getUserObject()).getStatus_number() == 1) {
                            rdata[1] = xerb.getString("general.status.open") + "";
                        }
                        if (((XincoCoreNode)node.getUserObject()).getStatus_number() == 2) {
                            rdata[1] = xerb.getString("general.status.locked") + " (-)";
                        }
                        if (((XincoCoreNode)node.getUserObject()).getStatus_number() == 3) {
                            rdata[1] = xerb.getString("general.status.archived") + " (->)";
                        }
                        //rdata[1] = rdata[1] + "(" + ((XincoCoreNode)node.getUserObject()).getStatus_number() + ")";
                        dtm.addRow(rdata);
                    }
                    if (node.getUserObject().getClass() == XincoCoreData.class) {
                        DefaultTableModel dtm = (DefaultTableModel)jTableRepository.getModel();
                        j = dtm.getRowCount();
                        for (i=0;i<j;i++) {
                            dtm.removeRow(0);
                        }
                        String[] rdata = {"",""};
                        rdata[0] = xerb.getString("general.id");
                        rdata[1] = "" + ((XincoCoreData)node.getUserObject()).getId();
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.designation");
                        rdata[1] = ((XincoCoreData)node.getUserObject()).getDesignation();
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.language");
                        rdata[1] = ((XincoCoreData)node.getUserObject()).getXinco_core_language().getDesignation() + " (" + ((XincoCoreData)node.getUserObject()).getXinco_core_language().getSign() + ")";
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.datatype");
                        rdata[1] = ((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getDesignation() + " (" + ((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getDescription() + ")";
                        dtm.addRow(rdata);
                        
                        rdata[0] = "";
                        rdata[1] = "";
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.accessrights");
                        rdata[1] = "";
                        rdata[1] = rdata[1] + "[";
                        if (temp_ace.isRead_permission()) {
                            rdata[1] = rdata[1] + "R";
                        } else {
                            rdata[1] = rdata[1] + "-";
                        }
                        if (temp_ace.isWrite_permission()) {
                            rdata[1] = rdata[1] + "W";
                        } else {
                            rdata[1] = rdata[1] + "-";
                        }
                        if (temp_ace.isExecute_permission()) {
                            rdata[1] = rdata[1] + "X";
                        } else {
                            rdata[1] = rdata[1] + "-";
                        }
                        if (temp_ace.isAdmin_permission()) {
                            rdata[1] = rdata[1] + "A";
                        } else {
                            rdata[1] = rdata[1] + "-";
                        }
                        rdata[1] = rdata[1] + "]";
                        dtm.addRow(rdata);
                        rdata[0] = "";
                        rdata[1] = "";
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.status");
                        rdata[1] = "";
                        if (((XincoCoreData)node.getUserObject()).getStatus_number() == 1) {
                            rdata[1] = xerb.getString("general.status.open") + "";
                        }
                        if (((XincoCoreData)node.getUserObject()).getStatus_number() == 2) {
                            rdata[1] = xerb.getString("general.status.locked") + " (-)";
                        }
                        if (((XincoCoreData)node.getUserObject()).getStatus_number() == 3) {
                            rdata[1] = xerb.getString("general.status.archived") + " (->)";
                        }
                        if (((XincoCoreData)node.getUserObject()).getStatus_number() == 4) {
                            rdata[1] = xerb.getString("general.status.checkedout") + " (X)";
                        }
                        if (((XincoCoreData)node.getUserObject()).getStatus_number() == 5) {
                            rdata[1] = xerb.getString("general.status.published") + " (WWW)";
                        }
                        //rdata[1] = rdata[1] + "(" + ((XincoCoreData)node.getUserObject()).getStatus_number() + ")";
                        dtm.addRow(rdata);
                        
                        rdata[0] = "";
                        rdata[1] = "";
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.typespecificattributes");
                        rdata[1] = "";
                        dtm.addRow(rdata);
                        
                        //get add. attributes of CoreData, if access granted
                        if (((XincoCoreData)node.getUserObject()).getXinco_add_attributes().size() > 0) {
                            for (i=0;i<((XincoCoreData)node.getUserObject()).getXinco_add_attributes().size();i++) {
                                rdata[0] = ((XincoCoreDataTypeAttribute)((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getDesignation();
                                if (((XincoCoreDataTypeAttribute)((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("int")) {
                                    rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)node.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_int();
                                }
                                if (((XincoCoreDataTypeAttribute)((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("unsignedint")) {
                                    rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)node.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_unsignedint();
                                }
                                if (((XincoCoreDataTypeAttribute)((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("double")) {
                                    rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)node.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_double();
                                }
                                if (((XincoCoreDataTypeAttribute)((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("varchar")) {
                                    rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)node.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_varchar();
                                }
                                if (((XincoCoreDataTypeAttribute)((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("text")) {
                                    rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)node.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_text();
                                }
                                if (((XincoCoreDataTypeAttribute)((XincoCoreData)node.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("datetime")) {
                                    rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)node.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_datetime().getTime();
                                }
                                dtm.addRow(rdata);
                            }
                        } else {
                            rdata[0] = xerb.getString("error.accessdenied");
                            rdata[1] = xerb.getString("error.content.sufficientrights");
                            dtm.addRow(rdata);
                        }
                        
                        rdata[0] = "";
                        rdata[1] = "";
                        dtm.addRow(rdata);
                        rdata[0] = "";
                        rdata[1] = "";
                        dtm.addRow(rdata);
                        rdata[0] = xerb.getString("general.logslastfirst");
                        rdata[1] = "";
                        dtm.addRow(rdata);
                        Calendar cal;
                        Calendar realcal;
                        Calendar ngc = new GregorianCalendar();
                        for (i=((XincoCoreData)node.getUserObject()).getXinco_core_logs().size()-1;i>=0;i--) {
                            if (((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_datetime() != null) {
                                try {
                                    //convert clone from remote time to local time
                                    cal = (Calendar)(Calendar)((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_datetime().clone();
                                    realcal = ((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_datetime();
                                    cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - realcal.get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + realcal.get(Calendar.DST_OFFSET)) );
                                    rdata[0] = "" + cal.get(Calendar.YEAR) + " / "  + (cal.get(Calendar.MONTH) + 1) + " / "  + cal.get(Calendar.DAY_OF_MONTH);
                                } catch (Exception ce) {
                                }
                            } else {
                                rdata[0] = "???";
                            }
                            rdata[1] = "(" + ((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_code() + ") " + ((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getOp_description();
                            dtm.addRow(rdata);
                            //rdata[0] = "";
                            //rdata[1] = "UserID = " + ((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getXinco_core_user_id();
                            //dtm.addRow(rdata);
                            rdata[0] = "";
                            try{
                                rdata[1] = xerb.getString("general.version") + " " + ((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_high() + "."
                                        + ((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_mid() + "."
                                        + ((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_low() + ""
                                        + ((XincoCoreLog)((XincoCoreData)node.getUserObject()).getXinco_core_logs().elementAt(i)).getVersion().getVersion_postfix();
                            } catch(Exception ex){
                                ex.printStackTrace();
                            }
                            dtm.addRow(rdata);
                        }
                    }
                }
            });
            java.awt.event.MouseListener ml = new java.awt.event.MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int selRow = jTreeRepository.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = jTreeRepository.getPathForLocation(e.getX(), e.getY());
                    if(selRow != -1) {
                        if(e.getClickCount() == 1) {
                            //value change handled in seperate listener
                        } else if(e.getClickCount() == 2) {
                            //double-click -> preview file
                            //jTreeRepository.setSelectionPath(selPath);
                            if (xincoClientSession.currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                                //file = 1
                                if (((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getId() == 1) {
                                    doDataWizard(14);
                                }
                            }
                        }
                    }
                }
            };
            jTreeRepository.addMouseListener(ml);
        }
        return jTreeRepository;
    }
    /**
     * This method initializes jTableRepository
     *
     * @return javax.swing.JTable
     */
    private javax.swing.JTable getJTableRepository() {
        if(jTableRepository == null) {
            String[] cn = {xerb.getString("window.repository.table.attribute"),xerb.getString("window.repository.table.details")};
            DefaultTableModel dtm = new DefaultTableModel(cn, 0);
            jTableRepository = new javax.swing.JTable();
            jTableRepository.setModel(dtm);
            jTableRepository.setColumnSelectionAllowed(false);
            jTableRepository.setRowSelectionAllowed(false);
            jTableRepository.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jTableRepository.setCellSelectionEnabled(false);
            jTableRepository.setEnabled(true);
        }
        return jTableRepository;
    }
    /**
     * This method initializes jMenuItemRepositoryRefresh
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryRefresh() {
        if(jMenuItemRepositoryRefresh == null) {
            jMenuItemRepositoryRefresh = new javax.swing.JMenuItem();
            jMenuItemRepositoryRefresh.setText(xerb.getString("menu.repository.refresh"));
            jMenuItemRepositoryRefresh.setName("Refresh");
            jMenuItemRepositoryRefresh.setEnabled(true);
            jMenuItemRepositoryRefresh.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
            jMenuItemRepositoryRefresh.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        //get root
                        XincoCoreNode xnode = new XincoCoreNode();
                        xnode.setId(1);
                        xnode = xincoClientSession.xinco.getXincoCoreNode(xnode, xincoClientSession.user);
                        xincoClientSession.xincoClientRepository.assignObject2TreeNode((XincoMutableTreeNode)((DefaultTreeModel)xincoClientSession.xincoClientRepository.treemodel).getRoot(), xnode, xincoClientSession.xinco, xincoClientSession.user, 2);
                        jTreeRepository.expandPath(new TreePath(xincoClientSession.xincoClientRepository.treemodel.getPathToRoot(((XincoMutableTreeNode)((DefaultTreeModel)xincoClientSession.xincoClientRepository.treemodel).getRoot()))));
                    } catch (Exception rmie) {
                    }
                }
            });
        }
        return jMenuItemRepositoryRefresh;
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
            jMenuItemSearchRepository = new javax.swing.JMenuItem();
            jMenuItemSearchRepository.setText(xerb.getString("menu.search.search_repository"));
            jMenuItemSearchRepository.setName("SearchRepository");
            jMenuItemSearchRepository.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.KeyEvent.CTRL_MASK));
            jMenuItemSearchRepository.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jInternalFrameSearch = getJInternalFrameSearch();
                    jInternalFrameSearch.setVisible(true);
                }
            });
        }
        return jMenuItemSearchRepository;
    }
    /**
     * This method initializes jMenuItemRepositoryAddFolder
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryAddFolder() {
        if(jMenuItemRepositoryAddFolder == null) {
            jMenuItemRepositoryAddFolder = new javax.swing.JMenuItem();
            jMenuItemRepositoryAddFolder.setText(xerb.getString("menu.repository.addfolder"));
            jMenuItemRepositoryAddFolder.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryAddFolder.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    XincoMutableTreeNode newnode;
                    
                    //open folder dialog
                    if (xincoClientSession.currentTreeNodeSelection != null) {
                        if (xincoClientSession.currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                            //set current node to new one
                            newnode = new XincoMutableTreeNode(new XincoCoreNode());
                            //set node attributes
                            ((XincoCoreNode)newnode.getUserObject()).setXinco_core_node_id(((XincoCoreNode)xincoClientSession.currentTreeNodeSelection.getUserObject()).getId());
                            ((XincoCoreNode)newnode.getUserObject()).setDesignation(xerb.getString("general.newfolder"));
                            ((XincoCoreNode)newnode.getUserObject()).setXinco_core_language((XincoCoreLanguage)xincoClientSession.server_languages.elementAt(0));
                            ((XincoCoreNode)newnode.getUserObject()).setStatus_number(1);
                            xincoClientSession.xincoClientRepository.treemodel.insertNodeInto(newnode, xincoClientSession.currentTreeNodeSelection, xincoClientSession.currentTreeNodeSelection.getChildCount());
                            xincoClientSession.currentTreeNodeSelection = newnode;
                            jDialogFolder = getJDialogFolder();
                            jDialogFolder.setVisible(true);
                            //update treemodel
                            xincoClientSession.xincoClientRepository.treemodel.reload(xincoClientSession.currentTreeNodeSelection);
                            xincoClientSession.xincoClientRepository.treemodel.nodeChanged(xincoClientSession.currentTreeNodeSelection);
                        }
                    }
                }
            });
        }
        return jMenuItemRepositoryAddFolder;
    }
    /**
     * This method initializes jMenuItemRepositoryAddData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryAddData() {
        if(jMenuItemRepositoryAddData == null) {
            jMenuItemRepositoryAddData = new javax.swing.JMenuItem();
            jMenuItemRepositoryAddData.setText(xerb.getString("menu.repository.adddata"));
            jMenuItemRepositoryAddData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryAddData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //data wizard -> add new data object
                    doDataWizard(1);
                }
            });
        }
        return jMenuItemRepositoryAddData;
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
            bgwindowstyle.add(jMenuView.getItem(0));
            bgwindowstyle.add(jMenuView.getItem(1));
            bgwindowstyle.add(jMenuView.getItem(2));
            bgwindowstyle.add(jMenuView.getItem(3));
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
        if(jMenuItemConnectionConnect == null) {
            jMenuItemConnectionConnect = new javax.swing.JMenuItem();
            jMenuItemConnectionConnect.setText(xerb.getString("menu.connection.connect"));
            jMenuItemConnectionConnect.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int i = 0;
                    xincoClientSession.status = 0;
                    //open connection dialog
                    getJDialogConnection();
                    DefaultListModel dlm = (DefaultListModel)dialogConnection.getProfileList().getModel();
                    dlm.removeAllElements();
                    for (i=0;i<((Vector)xincoClientConfig.elementAt(0)).size();i++) {
                        dlm.addElement(new String(((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(i)).toString()));
                    }
                    //establish connection and login
                    if (xincoClientSession.status == 1) {
                        try {
                            String status_string = "";
                            xincoClientSession.xinco_service = new XincoServiceLocator();
                            xincoClientSession.xinco = xincoClientSession.xinco_service.getXinco(new java.net.URL(xincoClientSession.service_endpoint));
                            status_string += xerb.getString("menu.connection.connectedto") + ": " + xincoClientSession.service_endpoint + "\n";
                            xincoClientSession.server_version = xincoClientSession.xinco.getXincoServerVersion();
                            status_string += xerb.getString("general.serverversion") + ": ";
                            status_string += xincoClientSession.server_version.getVersion_high() + "." ;
                            status_string += xincoClientSession.server_version.getVersion_mid() + "." ;
                            status_string += xincoClientSession.server_version.getVersion_low();
                            status_string += xincoClientSession.server_version.getVersion_postfix() + "\n";
                            status_string += "\n";
                            //check if client and server versions match
                            if (xincoClientVersion.getVersion_high() != xincoClientSession.server_version.getVersion_high()) {
                                throw new XincoException(xerb.getString("menu.connection.error.serverversion") + " " + xincoClientSession.server_version.getVersion_high() + ".x");
                            }
                            XincoCoreUser newuser;
                            if ((newuser = xincoClientSession.xinco.getCurrentXincoCoreUser(xincoClientSession.user.getUsername(), xincoClientSession.user.getUserpassword())) == null) {
                                throw new XincoException(xerb.getString("menu.connection.error.user"));
                            }
                            newuser.setUserpassword(xincoClientSession.user.getUserpassword());
                            xincoClientSession.user = newuser;
                            status_string = status_string + xerb.getString("general.user") + ": " + xincoClientSession.user.getFirstname() + " " + xincoClientSession.user.getName() + " <" + xincoClientSession.user.getEmail() + ">\n";
                            status_string = status_string + xerb.getString("general.memberof") + ":\n";
                            for (i=0;i<xincoClientSession.user.getXinco_core_groups().size();i++) {
                                status_string = status_string + "      + " + ((XincoCoreGroup)xincoClientSession.user.getXinco_core_groups().elementAt(i)).getDesignation() + "\n";
                            }
                            status_string = status_string + "\n";
                            xincoClientSession.server_groups = xincoClientSession.xinco.getAllXincoCoreGroups(xincoClientSession.user);
                            status_string = status_string + xerb.getString("general.groupsonserver") + ": " + xincoClientSession.server_groups.size() + "\n";
                            
                            xincoClientSession.server_languages = xincoClientSession.xinco.getAllXincoCoreLanguages(xincoClientSession.user);
                            status_string = status_string + xerb.getString("general.languagesonserver") + ": " + xincoClientSession.server_languages.size() + "\n";
                            
                            xincoClientSession.server_datatypes = xincoClientSession.xinco.getAllXincoCoreDataTypes(xincoClientSession.user);
                            status_string = status_string + xerb.getString("general.datatypesonserver") + ": " + xincoClientSession.server_datatypes.size() + "\n";
                            for (i=0;i<xincoClientSession.server_datatypes.size();i++) {
                                status_string = status_string + "      + " + ((XincoCoreDataType)xincoClientSession.server_datatypes.elementAt(i)).getDesignation() + "\n";
                            }
                            
                            xincoClientSession.currentSearchResult = new Vector();
                            xincoClientSession.status = 2;
                            JOptionPane.showMessageDialog(XincoExplorer.this, status_string, xerb.getString("menu.connection.established"), JOptionPane.INFORMATION_MESSAGE);
                            jLabelInternalFrameInformationText.setText(xerb.getString("menu.connection.established"));
                            
                            //get root
                            XincoCoreNode xnode = new XincoCoreNode();
                            xnode.setId(1);
                            xnode = xincoClientSession.xinco.getXincoCoreNode(xnode, xincoClientSession.user);
                            xincoClientSession.xincoClientRepository.assignObject2TreeNode((XincoMutableTreeNode)((DefaultTreeModel)xincoClientSession.xincoClientRepository.treemodel).getRoot(), xnode, xincoClientSession.xinco, xincoClientSession.user, 2);
                            jTreeRepository.expandPath(new TreePath(xincoClientSession.xincoClientRepository.treemodel.getPathToRoot(((XincoMutableTreeNode)((DefaultTreeModel)xincoClientSession.xincoClientRepository.treemodel).getRoot()))));
                            
                            markConnectionStatus();
                            
                            if(newuser.getStatus_number()==3){
                                jLabelInternalFrameInformationText.setText(xerb.getString("password.aged"));
                                getJDialogUser(true);
                                newuser.setUserpassword(xincoClientSession.user.getUserpassword());
                            }
                        } catch (Exception cone) {
                            xincoClientSession.status = 0;
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
    /**
     * This method initializes jContentPaneDialogConnection
     *
     * @return javax.swing.JPanel
     */
    private void getJContentPaneDialogConnection() {
        if(this.dialogConnection == null) {
            this.dialogConnection=new ConnectionDialog(new javax.swing.JFrame(),
                    true,this);
        }
    }
    
    /**
     * This method initializes jDialogConnection
     *
     * @return javax.swing.JDialog
     */
    private void getJDialogConnection() {
        if(this.dialogConnection == null) {
            this.dialogConnection=new ConnectionDialog(new javax.swing.JFrame(),
                    true,this);
        }
            this.dialogConnection.setVisible(true);
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
    private void markConnectionStatus() {
        int i=0, j=0;
        if(xincoClientSession != null) {
            //do general processing
            DefaultTableModel dtm;
            dtm = (DefaultTableModel)jTableRepository.getModel();
            j = dtm.getRowCount();
            for (i=0;i<j;i++) {
                dtm.removeRow(0);
            }
            dtm = (DefaultTableModel)jTableSearchResult.getModel();
            j = dtm.getRowCount();
            for (i=0;i<j;i++) {
                dtm.removeRow(0);
            }
            //reset selection
            xincoClientSession.currentTreeNodeSelection = null;
            xincoClientSession.clipboardTreeNodeSelection = new Vector();
            xincoClientSession.currentSearchResult = new Vector();
            //reset menus
            jMenuItemRepositoryAddFolder.setEnabled(false);
            jMenuItemRepositoryAddData.setEnabled(false);
            jMenuItemRepositoryAddDataStructure.setEnabled(false);
            jMenuItemRepositoryEditFolderData.setEnabled(false);
            jMenuItemRepositoryViewEditAddAttributes.setEnabled(false);
            jMenuItemRepositoryEditFolderDataACL.setEnabled(false);
            jMenuItemRepositoryMoveFolderData.setEnabled(false);
            jMenuItemRepositoryInsertFolderData.setEnabled(false);
            jMenuItemRepositoryViewData.setEnabled(false);
            jMenuItemRepositoryViewURL.setEnabled(false);
            jMenuItemRepositoryEmailContact.setEnabled(false);
            jMenuItemRepositoryCheckoutData.setEnabled(false);
            jMenuItemRepositoryUndoCheckoutData.setEnabled(false);
            jMenuItemRepositoryCheckinData.setEnabled(false);
            jMenuItemRepositoryPublishData.setEnabled(false);
            jMenuItemRepositoryLockData.setEnabled(false);
            jMenuItemRepositoryDownloadRevision.setEnabled(false);
            jMenuItemRepositoryCommentData.setEnabled(false);
            //status = disconnected
            if (xincoClientSession.status == 0) {
                jMenuRepository.setEnabled(false);
                jMenuSearch.setEnabled(false);
                jMenuItemSearchRepository.setEnabled(false);
                jMenuPreferences.setEnabled(false);
                jMenuItemConnectionConnect.setEnabled(true);
                jMenuItemConnectionDisconnect.setEnabled(false);
                jInternalFrameRepository.setVisible(false);
                jInternalFrameSearch.setVisible(false);
                jInternalFrameInformation.setVisible(false);
            }
            //status = connected
            if (xincoClientSession.status == 2) {
                jMenuRepository.setEnabled(true);
                jMenuSearch.setEnabled(true);
                jMenuItemSearchRepository.setEnabled(true);
                jMenuPreferences.setEnabled(true);
                jMenuItemConnectionConnect.setEnabled(false);
                jMenuItemConnectionDisconnect.setEnabled(true);
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
            jInternalFrameInformation.setBounds(this.getWidth()-450, this.getHeight()-220, 400, 150);
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
        } catch (Exception plafe) {
            //System.err.println(plafe.toString());
        }
    }
    
    /**
     * This method initializes jMenuItemRepositoryEditFolderData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryEditFolderData() {
        if(jMenuItemRepositoryEditFolderData == null) {
            jMenuItemRepositoryEditFolderData = new javax.swing.JMenuItem();
            jMenuItemRepositoryEditFolderData.setText(xerb.getString("menu.edit.folderdata"));
            jMenuItemRepositoryEditFolderData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryEditFolderData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (xincoClientSession.currentTreeNodeSelection != null) {
                        if (xincoClientSession.currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                            //open folder dialog
                            jDialogFolder = getJDialogFolder();
                            jDialogFolder.setVisible(true);
                        }
                        if (xincoClientSession.currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                            //data wizard -> edit data object
                            doDataWizard(2);
                        }
                    }
                }
            });
        }
        return jMenuItemRepositoryEditFolderData;
    }
    /**
     * This method initializes jMenuItemRepositoryCheckoutData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryCheckoutData() {
        if(jMenuItemRepositoryCheckoutData == null) {
            jMenuItemRepositoryCheckoutData = new javax.swing.JMenuItem();
            jMenuItemRepositoryCheckoutData.setText(xerb.getString("menu.edit.checkoutfile"));
            jMenuItemRepositoryCheckoutData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryCheckoutData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(4);
                }
            });
        }
        return jMenuItemRepositoryCheckoutData;
    }
    /**
     * This method initializes jMenuItemRepositoryUndoCheckoutData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryUndoCheckoutData() {
        if(jMenuItemRepositoryUndoCheckoutData == null) {
            jMenuItemRepositoryUndoCheckoutData = new javax.swing.JMenuItem();
            jMenuItemRepositoryUndoCheckoutData.setText(xerb.getString("menu.edit.undocheckout"));
            jMenuItemRepositoryUndoCheckoutData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryUndoCheckoutData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(5);
                }
            });
        }
        return jMenuItemRepositoryUndoCheckoutData;
    }
    /**
     * This method initializes jMenuItemRepositoryCheckinData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryCheckinData() {
        if(jMenuItemRepositoryCheckinData == null) {
            jMenuItemRepositoryCheckinData = new javax.swing.JMenuItem();
            jMenuItemRepositoryCheckinData.setText(xerb.getString("menu.edit.checkinfile"));
            jMenuItemRepositoryCheckinData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryCheckinData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(6);
                }
            });
        }
        return jMenuItemRepositoryCheckinData;
    }
    /**
     * This method initializes jMenuItemRepositoryPublishData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryPublishData() {
        if(jMenuItemRepositoryPublishData == null) {
            jMenuItemRepositoryPublishData = new javax.swing.JMenuItem();
            jMenuItemRepositoryPublishData.setText(xerb.getString("menu.edit.publishdata"));
            jMenuItemRepositoryPublishData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryPublishData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(10);
                }
            });
        }
        return jMenuItemRepositoryPublishData;
    }
    /**
     * This method initializes jMenuItemRepositoryLockData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryLockData() {
        if(jMenuItemRepositoryLockData == null) {
            jMenuItemRepositoryLockData = new javax.swing.JMenuItem();
            jMenuItemRepositoryLockData.setText(xerb.getString("menu.edit.lockdata"));
            jMenuItemRepositoryLockData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryLockData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(12);
                }
            });
        }
        return jMenuItemRepositoryLockData;
    }
    /**
     * This method initializes jMenuItemRepositoryDownloadRevision
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryDownloadRevision() {
        if(jMenuItemRepositoryDownloadRevision == null) {
            jMenuItemRepositoryDownloadRevision = new javax.swing.JMenuItem();
            jMenuItemRepositoryDownloadRevision.setText(xerb.getString("menu.edit.downloadrevision"));
            jMenuItemRepositoryDownloadRevision.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryDownloadRevision.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(11);
                }
            });
        }
        return jMenuItemRepositoryDownloadRevision;
    }
    
    /**
     * This method initializes jDialogFolder
     *
     * @return javax.swing.JDialog
     */
    private javax.swing.JDialog getJDialogFolder() {
        if(jDialogFolder == null)
            jDialogFolder = new DataFolderDialog(null, true, this);
        return jDialogFolder;
    }
    
    /**
     * This method initializes jMenuItemRepositoryEditFolderDataACL
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryEditFolderDataACL() {
        if(jMenuItemRepositoryEditFolderDataACL == null) {
            jMenuItemRepositoryEditFolderDataACL = new javax.swing.JMenuItem();
            jMenuItemRepositoryEditFolderDataACL.setText(xerb.getString("menu.edit.acl"));
            jMenuItemRepositoryEditFolderDataACL.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryEditFolderDataACL.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int i = 0, j = 0;
                    ListModel dlm;
                    if (xincoClientSession.currentTreeNodeSelection != null) {
                        //open ACL dialog
                        jDialogACL = getJDialogACL();
                        //fill group list
                        dlm = (((ACLDialog)jDialogACL).getACLGroupModel());
                        String[] list = new String[xincoClientSession.server_groups.size()];
                        for (i=0;i<xincoClientSession.server_groups.size();i++) {
                            list[i]=(new String(((XincoCoreGroup)xincoClientSession.server_groups.elementAt(i)).getDesignation()));
                        }
                        ((ACLDialog)jDialogACL).setACLGroupModel(list);
                        //fill ACL
                        ((ACLDialog)jDialogACL).reloadACLListACL();
                        jDialogACL.setVisible(true);
                    }
                }
            });
        }
        return jMenuItemRepositoryEditFolderDataACL;
    }
    /**
     * This method initializes jMenuItemRepositoryMoveFolderData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryMoveFolderData() {
        if(jMenuItemRepositoryMoveFolderData == null) {
            jMenuItemRepositoryMoveFolderData = new javax.swing.JMenuItem();
            jMenuItemRepositoryMoveFolderData.setText(xerb.getString("menu.edit.movefolderdatatoclipboard"));
            jMenuItemRepositoryMoveFolderData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryMoveFolderData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int i;
                    TreePath[] tps;
                    //cut node
                    tps =jTreeRepository.getSelectionPaths();
                    xincoClientSession.clipboardTreeNodeSelection.removeAllElements();
                    for (i=0;i<jTreeRepository.getSelectionCount();i++) {
                        xincoClientSession.clipboardTreeNodeSelection.addElement(tps[i].getLastPathComponent());
                    }
                    //update transaction info
                    jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movemessage"));
                }
            });
        }
        return jMenuItemRepositoryMoveFolderData;
    }
    /**
     * This method initializes jMenuItemRepositoryInsertFolderData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryInsertFolderData() {
        if(jMenuItemRepositoryInsertFolderData == null) {
            jMenuItemRepositoryInsertFolderData = new javax.swing.JMenuItem();
            jMenuItemRepositoryInsertFolderData.setText(xerb.getString("menu.edit.insertfolderdata"));
            jMenuItemRepositoryInsertFolderData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryInsertFolderData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if ((xincoClientSession.currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) && (xincoClientSession.clipboardTreeNodeSelection != null)) {
                        int old_parent_node_id = 0;
                        int i;
                        int cb_size;
                        XincoMutableTreeNode temp_node;
                        cb_size = xincoClientSession.clipboardTreeNodeSelection.size();
                        for (i=0;i<cb_size;i++) {
                            temp_node = (XincoMutableTreeNode)xincoClientSession.clipboardTreeNodeSelection.elementAt(0);
                            if (xincoClientSession.currentTreeNodeSelection != temp_node) {
                                //paste node
                                if (temp_node.getUserObject().getClass() == XincoCoreNode.class) {
                                    //modify moved node
                                    old_parent_node_id = ((XincoCoreNode)temp_node.getUserObject()).getXinco_core_node_id();
                                    ((XincoCoreNode)temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode)xincoClientSession.currentTreeNodeSelection.getUserObject()).getId());
                                    try {
                                        //modify treemodel
                                        xincoClientSession.xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
                                        xincoClientSession.xincoClientRepository.treemodel.insertNodeInto(temp_node, xincoClientSession.currentTreeNodeSelection, xincoClientSession.currentTreeNodeSelection.getChildCount());
                                        //optimize node size
                                        ((XincoCoreNode)temp_node.getUserObject()).setXinco_core_nodes(new Vector());
                                        ((XincoCoreNode)temp_node.getUserObject()).setXinco_core_data(new Vector());
                                        //invoke web-service
                                        if (xincoClientSession.xinco.setXincoCoreNode((XincoCoreNode)temp_node.getUserObject(), xincoClientSession.user) != null) {
                                            //update transaction info
                                            jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movefoldersuccess"));
                                        } else {
                                            throw new XincoException(xerb.getString("error.nowritepermission"));
                                        }
                                    } catch (Exception rmie) {
                                        //undo modification
                                        ((XincoCoreNode)temp_node.getUserObject()).setXinco_core_node_id(old_parent_node_id);
                                        JOptionPane.showMessageDialog(XincoExplorer.this, 
                                                xerb.getString("error.movefolderfailed") + " " +
                                                xerb.getString("general.reason") + ": " + rmie.toString(),
                                                xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                                        break;
                                    }
                                }
                                //paste data
                                if (temp_node.getUserObject().getClass() == XincoCoreData.class) {
                                    //modify moved data
                                    old_parent_node_id = ((XincoCoreData)temp_node.getUserObject()).getXinco_core_node_id();
                                    ((XincoCoreData)temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode)xincoClientSession.currentTreeNodeSelection.getUserObject()).getId());
                                    try {
                                        //modify treemodel
                                        xincoClientSession.xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
                                        xincoClientSession.xincoClientRepository.treemodel.insertNodeInto(temp_node, xincoClientSession.currentTreeNodeSelection, xincoClientSession.currentTreeNodeSelection.getChildCount());
                                        //invoke web-service
                                        if (xincoClientSession.xinco.setXincoCoreData((XincoCoreData)temp_node.getUserObject(), xincoClientSession.user) != null) {
                                            //insert log
                                            try {
                                                XincoCoreLog newlog = new XincoCoreLog();
                                                Vector oldlogs = ((XincoCoreData)temp_node.getUserObject()).getXinco_core_logs();
                                                newlog.setXinco_core_data_id(((XincoCoreData)temp_node.getUserObject()).getId());
                                                newlog.setXinco_core_user_id(xincoClientSession.user.getId());
                                                newlog.setOp_code(2);
                                                newlog.setOp_description(xerb.getString("menu.edit.movedtofolder") + " " + ((XincoCoreNode)xincoClientSession.currentTreeNodeSelection.getUserObject()).getDesignation() + " (" + ((XincoCoreNode)xincoClientSession.currentTreeNodeSelection.getUserObject()).getId() + ") " + xerb.getString("general.by") + " " + xincoClientSession.user.getUsername());
                                                newlog.setOp_datetime(null);
                                                newlog.setVersion(((XincoCoreLog)oldlogs.elementAt(oldlogs.size()-1)).getVersion());
                                                xincoClientSession.xinco.setXincoCoreLog(newlog, xincoClientSession.user);
                                            } catch (Exception loge) {
                                            }
                                            //update transaction info
                                            jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movedatasuccess"));
                                        } else {
                                            throw new XincoException(xerb.getString("error.nowritepermission"));
                                        }
                                    } catch (Exception rmie) {
                                        //undo modification
                                        ((XincoCoreData)temp_node.getUserObject()).setXinco_core_node_id(old_parent_node_id);
                                        JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("error.movedatafailed") + " " + xerb.getString("general.reason") + ": " + rmie.toString(), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                                        break;
                                    }
                                }
                            }
                            //remove moved element from clipboard
                            xincoClientSession.clipboardTreeNodeSelection.removeElementAt(0);
                        }
                    }
                }
            });
        }
        return jMenuItemRepositoryInsertFolderData;
    }
    /**
     * This method initializes jDialogACL
     *
     * @return javax.swing.JDialog
     */
    private javax.swing.JDialog getJDialogACL() {
        jDialogACL = new ACLDialog(new javax.swing.JFrame(),true,this);
        return jDialogACL;
    }
    /**
     * This method initializes jMenuItemRepositoryViewEditAddAttributes
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryViewEditAddAttributes() {
        if(jMenuItemRepositoryViewEditAddAttributes == null) {
            jMenuItemRepositoryViewEditAddAttributes = new javax.swing.JMenuItem();
            jMenuItemRepositoryViewEditAddAttributes.setText(xerb.getString("menu.repository.vieweditaddattributes"));
            jMenuItemRepositoryViewEditAddAttributes.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryViewEditAddAttributes.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (xincoClientSession.currentTreeNodeSelection != null) {
                        if (xincoClientSession.currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                            //data wizard -> edit add attributes
                            doDataWizard(3);
                        }
                    }
                }
            });
        }
        return jMenuItemRepositoryViewEditAddAttributes;
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
        }
        //processing independent of creation
        int i = 0;
        String text = "";
        if (!(xincoClientSession.currentTreeNodeSelection.getUserObject() == null)) {
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
        if(jDialogData == null)
            jDialogData = new DataDialog(null, true, this);
        return jDialogData;
    }
    /**
     * This method initializes jDialogLog
     *
     * @return javax.swing.JDialog
     */
    private javax.swing.JDialog getJDialogLog() {
//        if(jDialogLog == null) {
        jDialogLog = new LogDialog(null,true,this);
//        }
        return jDialogLog;
    }
    
    /**
     * This method imports files + subfolders of a folder into node
     *
     * @return void
     */
    private void importContentOfFolder(XincoCoreNode node, File folder) throws Exception {
        int i=0;
        int j=0;
        File[] folder_list = null;
        folder_list = folder.listFiles();
        XincoMutableTreeNode newnode = new XincoMutableTreeNode(new XincoCoreData());
        XincoCoreNode xnode;
        XincoCoreData xdata;
        XincoCoreLog newlog = new XincoCoreLog();
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
        for (i=0;i<folder_list.length;i++) {
            if (folder_list[i].isFile()) {
                //set current node to new one
                newnode = new XincoMutableTreeNode(new XincoCoreData());
                //set data attributes
                ((XincoCoreData)newnode.getUserObject()).setXinco_core_node_id(node.getId());
                ((XincoCoreData)newnode.getUserObject()).setDesignation(folder_list[i].getName());
                ((XincoCoreData)newnode.getUserObject()).setXinco_core_data_type(xcdt1);
                ((XincoCoreData)newnode.getUserObject()).setXinco_core_language(xcl1);
                ((XincoCoreData)newnode.getUserObject()).setXinco_add_attributes(new Vector());
                ((XincoCoreData)newnode.getUserObject()).setXinco_core_acl(new Vector());
                ((XincoCoreData)newnode.getUserObject()).setXinco_core_logs(new Vector());
                ((XincoCoreData)newnode.getUserObject()).setStatus_number(1);
                xincoClientSession.xincoClientRepository.treemodel.insertNodeInto(newnode, xincoClientSession.currentTreeNodeSelection, xincoClientSession.currentTreeNodeSelection.getChildCount());
                xincoClientSession.currentTreeNodeSelection = newnode;
                //add specific attributes
                XincoAddAttribute xaa;
                for (j=0;j<((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().size();j++) {
                    xaa = new XincoAddAttribute();
                    //xaa.setAttribute_id(j+1); // bug => attribute_ids might be missing in between
                    xaa.setAttribute_id(((XincoCoreDataTypeAttribute)xcdt1.getXinco_core_data_type_attributes().elementAt(j)).getAttribute_id());
                    xaa.setAttrib_varchar("");
                    xaa.setAttrib_text("");
                    xaa.setAttrib_datetime(new GregorianCalendar());
                    ((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().addElement(xaa);
                }
                //add log
                newlog = new XincoCoreLog();
                newlog.setOp_code(1);
                newlog.setOp_description(xerb.getString("datawizard.logging.creation") + "!" + " (" + xerb.getString("general.user") + ": " + xincoClientSession.user.getUsername() + ")");
                newlog.setXinco_core_user_id(xincoClientSession.user.getId());
                newlog.setXinco_core_data_id(((XincoCoreData)newnode.getUserObject()).getId()); //update to new id later!
                newlog.setVersion(new XincoVersion());
                newlog.getVersion().setVersion_high(1);
                newlog.getVersion().setVersion_mid(0);
                newlog.getVersion().setVersion_low(0);
                newlog.getVersion().setVersion_postfix("");
                ((XincoCoreData)newnode.getUserObject()).setXinco_core_logs(new Vector());
                ((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().addElement(newlog);
                //invoke web service (update data / upload file / add log)
                //load file
                long total_len = 0;
                boolean useSAAJ = false;
                if (((xincoClientSession.server_version.getVersion_high() == 1) && (xincoClientSession.server_version.getVersion_mid() >= 9)) || (xincoClientSession.server_version.getVersion_high() > 1)) {
                    useSAAJ = true;
                } else {
                    useSAAJ = false;
                }
                CheckedInputStream in = null;
                ByteArrayOutputStream out = null;
                byte[] byte_array = null;
                try {
                    in = new CheckedInputStream(new FileInputStream(folder_list[i]), new CRC32());
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
                    //update attributes
                    ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).setAttrib_varchar(folder_list[i].getName());
                    ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(1)).setAttrib_unsignedint(total_len);
                    ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(2)).setAttrib_varchar("" + in.getChecksum().getValue());
                    ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(3)).setAttrib_unsignedint(1); //revision model
                    ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(4)).setAttrib_unsignedint(0); //archiving model
                    if (!useSAAJ) {
                        in.close();
                    }
                } catch (Exception fe) {
                    throw new XincoException(xerb.getString("datawizard.unabletoloadfile"));
                }
                //save data to server
                xdata = xincoClientSession.xinco.setXincoCoreData((XincoCoreData)newnode.getUserObject(), xincoClientSession.user);
                if (xdata == null) {
                    throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
                } else {
                    newnode.setUserObject(xdata);
                }
                //update id in log
                newlog.setXinco_core_data_id(((XincoCoreData)newnode.getUserObject()).getId());
                //save log to server
                newlog = xincoClientSession.xinco.setXincoCoreLog(newlog, xincoClientSession.user);
                if (newlog == null) {
                    //System.out.println(xerb.getString("datawizard.savelogfailed"));
                }
                //attach file to SOAP message
                if (useSAAJ) {
                    AttachmentPart ap = null;
                    ap = new AttachmentPart();
                    ap.setContent(in, "unknown/unknown");
                    ((XincoSoapBindingStub)xincoClientSession.xinco).addAttachment(ap);
                }
                //upload file
                if (xincoClientSession.xinco.uploadXincoCoreData((XincoCoreData)newnode.getUserObject(), byte_array, xincoClientSession.user) != total_len) {
                    ((XincoSoapBindingStub)xincoClientSession.xinco).clearAttachments();
                    in.close();
                    throw new XincoException(xerb.getString("datawizard.fileuploadfailed"));
                }
                ((XincoSoapBindingStub)xincoClientSession.xinco).clearAttachments();
                in.close();
                //update treemodel
                xincoClientSession.xincoClientRepository.treemodel.reload(newnode);
                xincoClientSession.xincoClientRepository.treemodel.nodeChanged(newnode);
                //select parent of new node
                xincoClientSession.currentTreeNodeSelection = (XincoMutableTreeNode)newnode.getParent();
                //jTreeRepository.setSelectionPath(new TreePath(xincoClientSession.currentTreeNodeSelection.getPath()));
            }
        }
        //process directories
        for (i=0;i<folder_list.length;i++) {
            if (folder_list[i].isDirectory()) {
                //set current node to new one
                newnode = new XincoMutableTreeNode(new XincoCoreNode());
                //set node attributes
                ((XincoCoreNode)newnode.getUserObject()).setXinco_core_node_id(node.getId());
                ((XincoCoreNode)newnode.getUserObject()).setDesignation(folder_list[i].getName());
                ((XincoCoreNode)newnode.getUserObject()).setXinco_core_language(xcl1);
                ((XincoCoreNode)newnode.getUserObject()).setStatus_number(1);
                xincoClientSession.xincoClientRepository.treemodel.insertNodeInto(newnode, xincoClientSession.currentTreeNodeSelection, xincoClientSession.currentTreeNodeSelection.getChildCount());
                xincoClientSession.currentTreeNodeSelection = newnode;
                //save node to server
                xnode = xincoClientSession.xinco.setXincoCoreNode((XincoCoreNode)newnode.getUserObject(), xincoClientSession.user);
                if (xnode == null) {
                    throw new XincoException(xerb.getString("window.folder.updatefailed"));
                } else {
                    newnode.setUserObject(xnode);
                }
                //update treemodel
                xincoClientSession.xincoClientRepository.treemodel.reload(newnode);
                xincoClientSession.xincoClientRepository.treemodel.nodeChanged(newnode);
                //start recursion
                importContentOfFolder((XincoCoreNode)newnode.getUserObject(), folder_list[i]);
                //select parent of new node
                xincoClientSession.currentTreeNodeSelection = (XincoMutableTreeNode)newnode.getParent();
                //jTreeRepository.setSelectionPath(new TreePath(xincoClientSession.currentTreeNodeSelection.getPath()));
            }
        }
    }
    /**
     * This method leads through data adding/editing
     *
     * @return void
     */
    private void doDataWizard(final int wizard_type) {
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
                 */
        int i=0, j=0;
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
                    
                    //check file attribute count
                    //file = 1
                    if ((wizard_type == 3) &&
                            ((((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1) &&
                            (((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().size() <= 3))) {
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
                            //show dialog
                            int result = fc.showOpenDialog(XincoExplorer.this);
                            if(result == JFileChooser.APPROVE_OPTION) {
                                setCurrentPathFilename(fc.getSelectedFile().getPath());
                                ((XincoCoreData)newnode.getUserObject()).setDesignation(current_filename);
                            } else {
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }
                        //for text -> show text editing dialog
                        //text = 2
                        if (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 2) {
                            jDialogAddAttributesText = getJDialogAddAttributesText();
                            global_dialog_return_value = 0;
                            jDialogAddAttributesText.setVisible(true);
                            if (global_dialog_return_value == 0) {
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                        }
                        //show dialog for all additional attributes and custom data types
                        //file = 1 / text = 2
                        if (!(((((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1) &&
                                (((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().size() <= 8)) ||
                                ((((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 2) &&
                                (((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().size() <= 1)))) {
                            //for other data type -> show universal add attribute dialog
                            jDialogAddAttributesUniversal=new AddAttributeUniversalDialog(null,true,this);
                            global_dialog_return_value = 0;
                            jDialogAddAttributesUniversal.setVisible(true);
                            if (global_dialog_return_value == 0) {
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
                            newlog.setXinco_core_user_id(xincoClientSession.user.getId());
                            newlog.setXinco_core_data_id(((XincoCoreData)newnode.getUserObject()).getId());
                            newlog.setVersion(((XincoCoreLog)((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().elementAt(((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().size()-1)).getVersion());
                            ((XincoCoreData)newnode.getUserObject()).getXinco_core_logs().addElement(newlog);
                            jDialogLog = getJDialogLog();
                            global_dialog_return_value = 0;
                            jDialogLog.setVisible(true);
                            if (global_dialog_return_value == 0) {
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            newlog.setOp_description(newlog.getOp_description() + " (" + xerb.getString("general.user") + ": " + xincoClientSession.user.getUsername() + ")");
                        }
                    }
                    
                    //choose filename for checkout/checkin/download/preview
                    if ((wizard_type == 4) || (wizard_type == 6) || (wizard_type == 7) || (wizard_type == 11)) {
                        JFileChooser fc = new JFileChooser();
                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        //fc.setCurrentDirectory(new File(current_path + ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_varchar()));
                        //fc.setCurrentDirectory(new File(current_path));
                        fc.setSelectedFile(new File(current_path + ((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(0)).getAttrib_varchar()));
                        //show dialog
                        int result;
                        if ((wizard_type == 4) || (wizard_type == 7) || (wizard_type == 11)) {
                            result = fc.showSaveDialog(XincoExplorer.this);
                        } else {
                            result = fc.showOpenDialog(XincoExplorer.this);
                        }
                        if(result == JFileChooser.APPROVE_OPTION) {
                            setCurrentPathFilename(fc.getSelectedFile().getPath());
                        } else {
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
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
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
                        
                        //step 4b: edit archiving options of files
                        if (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1) {
                            jDialogArchive = getJDialogArchive();
                            global_dialog_return_value = 0;
                            jDialogArchive.setVisible(true);
                            if (global_dialog_return_value == 0) {
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
                            throw new XincoException(xerb.getString("datawizard.unabletoloadfile"));
                        }
                    }
                    //save data to server
                    if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) && (wizard_type != 14)) {
                        if ((wizard_type >= 4) && (wizard_type <= 6)) {
                            if (wizard_type == 4){
                                xdata = xincoClientSession.xinco.doXincoCoreDataCheckout((XincoCoreData)newnode.getUserObject(), xincoClientSession.user);
                            } else {
                                if (wizard_type == 5){
                                    xdata = xincoClientSession.xinco.undoXincoCoreDataCheckout((XincoCoreData)newnode.getUserObject(), xincoClientSession.user);
                                } else {
                                    xdata = xincoClientSession.xinco.doXincoCoreDataCheckin((XincoCoreData)newnode.getUserObject(), xincoClientSession.user);
                                }
                            }
                        } else {
                            xdata = xincoClientSession.xinco.setXincoCoreData((XincoCoreData)newnode.getUserObject(), xincoClientSession.user);
                        }
                        if (xdata == null) {
                            throw new XincoException(xerb.getString("datawizard.unabletosavedatatoserver"));
                        } else {
                            newnode.setUserObject(xdata);
                        }
                    }
                    if ((wizard_type != 7) && (wizard_type != 8) && (wizard_type != 9) && (wizard_type != 11) && (wizard_type != 14)) {
                        //update id in log
                        newlog.setXinco_core_data_id(((XincoCoreData)newnode.getUserObject()).getId());
                        //save log to server
                        newlog = xincoClientSession.xinco.setXincoCoreLog(newlog, xincoClientSession.user);
                        if (newlog == null) {
                            //System.out.println(xerb.getString("datawizard.savelogfailed"));
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
                    }
                    //download file
                    //file = 1
                    if (((wizard_type == 4) || (wizard_type == 7) || (wizard_type == 11) || (wizard_type == 14)) && (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 1)) {
                        //determine requested revision and set log vector
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
                                                /*if ((byte_array = xincoClientSession.xinco.downloadXincoCoreData((XincoCoreData)newnode.getUserObject(), xincoClientSession.user)) == null) {
                                                        //reassign log vector
                                                        if (wizard_type == 11) {
                                                                ((XincoCoreData)newnode.getUserObject()).setXinco_core_logs(DataLogVector);
                                                        }
                                                        JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.filedownloadfailed"), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                                                }*/
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
                            //if ((((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(1)).getAttrib_unsignedint() != total_len) || (((XincoAddAttribute)((XincoCoreData)newnode.getUserObject()).getXinco_add_attributes().elementAt(2)).getAttrib_varchar().equals(new String("" + couts.getChecksum().getValue())))) {
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
                    }
                    //Open in Browser
                    //URL = 3
                    if ((wizard_type == 8) && (((XincoCoreData)newnode.getUserObject()).getXinco_core_data_type().getId() == 3)) {
                        //open URL in default browser
                        Process process = null;
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
                        Process process = null;
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
                if (!((wizard_type == 3) && (global_dialog_return_value == 0))) {
                    JOptionPane.showMessageDialog(XincoExplorer.this, xerb.getString("datawizard.updatefailed") +
                            " " + xerb.getString("general.reason") + ": " +
                            we.toString(), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                }
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
            jMenuItemPreferencesEditUser = new javax.swing.JMenuItem();
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
     * This method sets current path and filename
     *
     * @return void
     */
    private void setCurrentPathFilename(String s) {
        int i=0,j=0;
        if(s != null) {
            try {
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
    private void setCurrentPath(String s) {
        if (!(s.substring(s.length()-1).equals(System.getProperty("file.separator")))) {
            s = s + System.getProperty("file.separator");
        }
        current_filename = "";
        current_path = s;
        current_fullpath = s;
    }
    /**
     * This method initializes jMenuItemRepositoryViewData
     *
     * @return javax.swing.JMenuItem
     */
    private javax.swing.JMenuItem getJMenuItemRepositoryViewData() {
        if(jMenuItemRepositoryViewData == null) {
            jMenuItemRepositoryViewData = new javax.swing.JMenuItem();
            jMenuItemRepositoryViewData.setText(xerb.getString("menu.repository.downloadfile"));
            jMenuItemRepositoryViewData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.KeyEvent.ALT_MASK));
            jMenuItemRepositoryViewData.setEnabled(false);
            jMenuItemRepositoryViewData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doDataWizard(7);
                }
            });
        }
        return jMenuItemRepositoryViewData;
    }
    /**
     * This method initializes jContentPaneDialogUser
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneDialogUser() {
        if(jContentPaneDialogUser == null) {
            jContentPaneDialogUser = new javax.swing.JPanel();
            jContentPaneDialogUser.setLayout(null);
            getJDialogUser(false);
        }
        return jContentPaneDialogUser;
    }
    /**
     * This method initializes jDialogUser
     *
     * @return javax.swing.JDialog
     */
    private void getJDialogUser(boolean aged) {
        if(this.userDialog == null)
            this.userDialog= new UserDialog(new javax.swing.JFrame(), true,this,aged);
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
     * This method initializes jContentPaneDialogTransactionInfo
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneDialogTransactionInfo() {
        if(jContentPaneDialogTransactionInfo == null) {
            jContentPaneDialogTransactionInfo = new javax.swing.JPanel();
            jContentPaneDialogTransactionInfo.setLayout(null);
            jContentPaneDialogTransactionInfo.add(getJLabelDialogTransactionInfoText(), null);
        }
        return jContentPaneDialogTransactionInfo;
    }
    /**
     * This method initializes jDialogTransactionInfo
     *
     * @return javax.swing.JDialog
     */
    private javax.swing.JDialog getJDialogTransactionInfo() {
        if(jDialogTransactionInfo == null) {
            jDialogTransactionInfo = new javax.swing.JDialog();
            jDialogTransactionInfo.setContentPane(getJContentPaneDialogTransactionInfo());
            jDialogTransactionInfo.setBounds(600, 200, 400, 150);
            jDialogTransactionInfo.setTitle(xerb.getString("window.transactioninfo"));
            jDialogTransactionInfo.setResizable(false);
            jDialogTransactionInfo.setModal(false);
        }
        return jDialogTransactionInfo;
    }
    /**
     * This method initializes jLabelDialogTransactionInfoText
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabelDialogTransactionInfoText() {
        if(jLabelDialogTransactionInfoText == null) {
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
     * @return void
     */
    public void saveConfig() {
        try {
            java.io.FileOutputStream fout = new java.io.FileOutputStream("xincoClientConfig.dat");
            java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(fout);
            os.writeObject(xincoClientConfig);
            os.close();
            fout.close();
        } catch (Exception ioe) {
            //error handling
            //System.out.println("Unable to write Profiles-File!");
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
            
            fin = new FileInputStream("xincoClientConfig.dat");
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
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = true;
                ((Vector)xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).profile_name = "xinco Demo Admin";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).service_endpoint = "http://xinco.org:8080/xinco_demo/services/Xinco";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).username = "admin";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).password = "admin";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = true;
                ((Vector)xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).profile_name = "Template Profile";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).service_endpoint = "http://[server_domain]:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).username = "your_username";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).password = "your_password";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = true;
                ((Vector)xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).profile_name = "Admin (localhost)";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).username = "admin";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).password = "admin";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = true;
                ((Vector)xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).profile_name = "User (localhost)";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).username = "user";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).password = "user";
                ((XincoClientConnectionProfile)((Vector)xincoClientConfig.elementAt(0)).elementAt(((Vector)xincoClientConfig.elementAt(0)).size()-1)).save_password = true;
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
            jDialogArchive = new javax.swing.JDialog();
            jDialogArchive.setContentPane(getJContentPaneDialogArchive());
            jDialogArchive.setBounds(200, 200, 400, 220);
            jDialogArchive.setTitle(xerb.getString("window.archive"));
            jDialogArchive.setModal(true);
            jDialogArchive.setResizable(false);
            jDialogArchive.getRootPane().setDefaultButton(getJButtonDialogArchiveContinue());
        }
        //processing independent of creation
        if (((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(3)).getAttrib_unsignedint() == 0) {
            jCheckBoxDialogArchiveRevisionModel.setSelected(false);
        } else {
            jCheckBoxDialogArchiveRevisionModel.setSelected(true);
        }
        DefaultComboBoxModel dcbm;
        //fill archiving model list
        dcbm = (DefaultComboBoxModel)jComboBoxDialogArchiveArchivingModel.getModel();
        dcbm.removeAllElements();
        dcbm.addElement(xerb.getString("window.archive.archivingmodel.none"));
        dcbm.addElement(xerb.getString("window.archive.archivingmodel.archivedate"));
        dcbm.addElement(xerb.getString("window.archive.archivingmodel.archivedays"));
        jComboBoxDialogArchiveArchivingModel.setSelectedIndex((int)((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(4)).getAttrib_unsignedint());
        //set date / days
        //convert clone from remote time to local time
        Calendar cal = (Calendar)((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(5)).getAttrib_datetime().clone();
        Calendar realcal = ((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(5)).getAttrib_datetime();
        Calendar ngc = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - realcal.get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + realcal.get(Calendar.DST_OFFSET)) );
        jTextFieldDialogArchiveDateYear.setText("" + cal.get(Calendar.YEAR));
        jTextFieldDialogArchiveDateMonth.setText("" + (cal.get(Calendar.MONTH) + 1));
        jTextFieldDialogArchiveDateDay.setText("" + cal.get(Calendar.DAY_OF_MONTH));
        jTextFieldDialogArchiveDays.setText("" + ((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(6)).getAttrib_unsignedint());
        return jDialogArchive;
    }
    /**
     * This method initializes jContentPaneDialogArchive
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPaneDialogArchive() {
        if(jContentPaneDialogArchive == null) {
            jContentPaneDialogArchive = new javax.swing.JPanel();
            jContentPaneDialogArchive.setLayout(null);
            jContentPaneDialogArchive.add(getJLabelDialogArchiveRevisionModel(), null);
            jContentPaneDialogArchive.add(getJCheckBoxDialogArchiveRevisionModel(), null);
            jContentPaneDialogArchive.add(getJLabelDialogArchiveArchivingModel(), null);
            jContentPaneDialogArchive.add(getJComboBoxDialogArchiveArchivingModel(), null);
            jContentPaneDialogArchive.add(getJLabelDialogArchiveDate(), null);
            jContentPaneDialogArchive.add(getJTextFieldDialogArchiveDateYear(), null);
            jContentPaneDialogArchive.add(getJTextFieldDialogArchiveDateMonth(), null);
            jContentPaneDialogArchive.add(getJTextFieldDialogArchiveDateDay(), null);
            jContentPaneDialogArchive.add(getJLabelDialogArchiveDays(), null);
            jContentPaneDialogArchive.add(getJTextFieldDialogArchiveDays(), null);
            jContentPaneDialogArchive.add(getJButtonDialogArchiveContinue(), null);
            jContentPaneDialogArchive.add(getJButtonDialogArchiveCancel(), null);
        }
        return jContentPaneDialogArchive;
    }
    /**
     * This method initializes jLabelDialogArchiveRevisionModel
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabelDialogArchiveRevisionModel() {
        if(jLabelDialogArchiveRevisionModel == null) {
            jLabelDialogArchiveRevisionModel = new javax.swing.JLabel();
            jLabelDialogArchiveRevisionModel.setBounds(10, 10, 100, 20);
            jLabelDialogArchiveRevisionModel.setText(xerb.getString("window.archive.revisionmodel") + ":");
        }
        return jLabelDialogArchiveRevisionModel;
    }
    /**
     * This method initializes jCheckBoxDialogArchiveRevisionModel
     *
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxDialogArchiveRevisionModel() {
        if(jCheckBoxDialogArchiveRevisionModel == null) {
            jCheckBoxDialogArchiveRevisionModel = new javax.swing.JCheckBox();
            jCheckBoxDialogArchiveRevisionModel.setBounds(120, 10, 250, 20);
        }
        return jCheckBoxDialogArchiveRevisionModel;
    }
    /**
     * This method initializes jLabelDialogArchiveArchivingModel
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabelDialogArchiveArchivingModel() {
        if(jLabelDialogArchiveArchivingModel == null) {
            jLabelDialogArchiveArchivingModel = new javax.swing.JLabel();
            jLabelDialogArchiveArchivingModel.setBounds(10, 40, 100, 20);
            jLabelDialogArchiveArchivingModel.setText(xerb.getString("window.archive.archivingmodel") + ":");
        }
        return jLabelDialogArchiveArchivingModel;
    }
    /**
     * This method initializes jComboBoxDialogArchiveArchivingModel
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBoxDialogArchiveArchivingModel() {
        if (jComboBoxDialogArchiveArchivingModel == null) {
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
            jComboBoxDialogArchiveArchivingModel = new JComboBox();
            jComboBoxDialogArchiveArchivingModel.setModel(dcbm);
            jComboBoxDialogArchiveArchivingModel.setBounds(120, 40, 250, 20);
            jComboBoxDialogArchiveArchivingModel.setEditable(false);
            jComboBoxDialogArchiveArchivingModel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (jComboBoxDialogArchiveArchivingModel.getSelectedIndex() == 1) {
                        jTextFieldDialogArchiveDateYear.setEnabled(true);
                        jTextFieldDialogArchiveDateMonth.setEnabled(true);
                        jTextFieldDialogArchiveDateDay.setEnabled(true);
                        jTextFieldDialogArchiveDays.setEnabled(false);
                    } else if (jComboBoxDialogArchiveArchivingModel.getSelectedIndex() == 2) {
                        jTextFieldDialogArchiveDateYear.setEnabled(false);
                        jTextFieldDialogArchiveDateMonth.setEnabled(false);
                        jTextFieldDialogArchiveDateDay.setEnabled(false);
                        jTextFieldDialogArchiveDays.setEnabled(true);
                    } else {
                        jTextFieldDialogArchiveDateYear.setEnabled(false);
                        jTextFieldDialogArchiveDateMonth.setEnabled(false);
                        jTextFieldDialogArchiveDateDay.setEnabled(false);
                        jTextFieldDialogArchiveDays.setEnabled(false);
                    }
                }
            });
        }
        return jComboBoxDialogArchiveArchivingModel;
    }
    /**
     * This method initializes jLabelDialogArchiveDate
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabelDialogArchiveDate() {
        if(jLabelDialogArchiveDate == null) {
            jLabelDialogArchiveDate = new javax.swing.JLabel();
            jLabelDialogArchiveDate.setBounds(10, 70, 100, 20);
            jLabelDialogArchiveDate.setText(xerb.getString("window.archive.archivedate") + ":");
        }
        return jLabelDialogArchiveDate;
    }
    /**
     * This method initializes jTextFieldDialogArchiveDateYear
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDialogArchiveDateYear() {
        if (jTextFieldDialogArchiveDateYear == null) {
            jTextFieldDialogArchiveDateYear = new JTextField();
            jTextFieldDialogArchiveDateYear.setBounds(120, 70, 100, 20);
        }
        return jTextFieldDialogArchiveDateYear;
    }
    /**
     * This method initializes jTextFieldDialogArchiveDateMonth
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDialogArchiveDateMonth() {
        if (jTextFieldDialogArchiveDateMonth == null) {
            jTextFieldDialogArchiveDateMonth = new JTextField();
            jTextFieldDialogArchiveDateMonth.setBounds(230, 70, 50, 20);
        }
        return jTextFieldDialogArchiveDateMonth;
    }
    /**
     * This method initializes jTextFieldDialogArchiveDateDay
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDialogArchiveDateDay() {
        if (jTextFieldDialogArchiveDateDay == null) {
            jTextFieldDialogArchiveDateDay = new JTextField();
            jTextFieldDialogArchiveDateDay.setBounds(290, 70, 50, 20);
        }
        return jTextFieldDialogArchiveDateDay;
    }
    /**
     * This method initializes jLabelDialogArchiveDays
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabelDialogArchiveDays() {
        if(jLabelDialogArchiveDays == null) {
            jLabelDialogArchiveDays = new javax.swing.JLabel();
            jLabelDialogArchiveDays.setBounds(10, 100, 100, 20);
            jLabelDialogArchiveDays.setText(xerb.getString("window.archive.archivedays") + ":");
        }
        return jLabelDialogArchiveDays;
    }
    /**
     * This method initializes jTextFieldDialogArchiveDays
     *
     * @return javax.swing.JTextField
     */
    private JTextField getJTextFieldDialogArchiveDays() {
        if (jTextFieldDialogArchiveDays == null) {
            jTextFieldDialogArchiveDays = new JTextField();
            jTextFieldDialogArchiveDays.setBounds(120, 100, 100, 20);
        }
        return jTextFieldDialogArchiveDays;
    }
    /**
     * This method initializes jButtonDialogArchiveContinue
     *
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonDialogArchiveContinue() {
        if(jButtonDialogArchiveContinue == null) {
            jButtonDialogArchiveContinue = new javax.swing.JButton();
            jButtonDialogArchiveContinue.setBounds(120, 130, 100, 30);
            jButtonDialogArchiveContinue.setText(xerb.getString("general.continue"));
            jButtonDialogArchiveContinue.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //update archiving options of selected data
                    if (jCheckBoxDialogArchiveRevisionModel.isSelected()) {
                        ((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(3)).setAttrib_unsignedint(1);
                    } else {
                        ((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(3)).setAttrib_unsignedint(0);
                    }
                    ((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(4)).setAttrib_unsignedint(jComboBoxDialogArchiveArchivingModel.getSelectedIndex());
                    int temp_year_int = 0;
                    int temp_month_int = 0;
                    int temp_day_int = 0;
                    int temp_days_int = 0;
                    try {
                        temp_year_int = Integer.parseInt(jTextFieldDialogArchiveDateYear.getText());
                        temp_month_int = Integer.parseInt(jTextFieldDialogArchiveDateMonth.getText());
                        temp_day_int = Integer.parseInt(jTextFieldDialogArchiveDateDay.getText());
                        //set FIXED date: GMT, no DST
                        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                        cal.set(Calendar.DST_OFFSET, 0);
                        cal.set(Calendar.YEAR, temp_year_int);
                        cal.set(Calendar.MONTH, (temp_month_int-1));
                        cal.set(Calendar.DAY_OF_MONTH, temp_day_int);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        ((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(5)).setAttrib_datetime(cal);
                        temp_days_int = Integer.parseInt(jTextFieldDialogArchiveDays.getText());
                        ((XincoAddAttribute)((XincoCoreData)xincoClientSession.currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(6)).setAttrib_unsignedint(temp_days_int);
                        //close dialog
                        global_dialog_return_value = 1;
                        jDialogArchive.setVisible(false);
                    } catch (Exception parseex) {
                    }
                }
            });
        }
        return jButtonDialogArchiveContinue;
    }
    /**
     * This method initializes jButtonDialogArchiveCancel
     *
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonDialogArchiveCancel() {
        if(jButtonDialogArchiveCancel == null) {
            jButtonDialogArchiveCancel = new javax.swing.JButton();
            jButtonDialogArchiveCancel.setBounds(240, 130, 100, 30);
            jButtonDialogArchiveCancel.setText(xerb.getString("general.cancel"));
            jButtonDialogArchiveCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jDialogArchive.setVisible(false);
                }
            });
        }
        return jButtonDialogArchiveCancel;
    }
    
    public int get_global_dialog_return_value(){
        return this.global_dialog_return_value;
    }
    
    public void set_global_dialog_return_value(int v){
        this.global_dialog_return_value=v;
    }
}

