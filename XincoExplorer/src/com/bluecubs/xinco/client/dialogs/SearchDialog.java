/**
 *Copyright 2007 blueCubs.com
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
 * Name:            SearchDialog
 *
 * Description:     Search Dialog
 *
 * Original Author: Javier A. Ortiz
 * Date:            2007
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *
 *************************************************************
 * SearchDialog.java
 *
 * Created on January 27, 2007, 8:45 AM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.XincoMutableTreeNode;
import com.bluecubs.xinco.client.object.XincoProgressBarThread;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreDataType;
import com.bluecubs.xinco.core.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.XincoCoreLanguage;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author  javydreamercsw
 */
public class SearchDialog extends javax.swing.JDialog {
    private XincoExplorer explorer;
    private ResourceBundle xerb;
    /** Creates new form SearchDialog */
    public SearchDialog(java.awt.Frame parent, boolean modal,XincoExplorer e) {
        super(parent, modal);
        initComponents();
        getRootPane().setDefaultButton(this.searchButton);
        this.explorer=e;
        this.xerb=this.explorer.getResourceBundle();
        setTitle(xerb.getString("window.search"));
        this.queryLabel.setText(xerb.getString("window.search.query") + ":");
        this.languageLabel.setText(xerb.getString("general.language") + ":");
        this.builderLabel.setText(xerb.getString("window.search.querybuilder") + ":");
        this.systemOptionsValueLabel.setText(xerb.getString("window.search.querybuilderhintslabel"));
        this.systemOptionsLabel.setText(xerb.getString("window.search.querybuilderhints"));
        this.searchButton.setText(xerb.getString("window.search.addtoquery"));
        this.allLanguagesCheckBox.setText(xerb.getString("window.search.alllanguages"));
        this.searchButton.setText(xerb.getString("window.search"));
        this.goToSelectionButton.setText(xerb.getString("window.search.gotoselection"));
        this.addToQueryButton.setText(xerb.getString("window.search.addtoquery"));
        this.resetButton.setText(xerb.getString("general.reset"));
        String[] cn = {xerb.getString("window.search.table.designation"),xerb.getString("window.search.table.path")};
        resultTable.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{xerb.getString("window.search.table.designation"),
                xerb.getString("window.search.table.path")}) {
            boolean[] canEdit = new boolean[]{false, false};
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        this.languageList.setEnabled(!this.allLanguagesCheckBox.isSelected());
        //processing independent of creation
        int i = 0;
        int j = 0;
        String text = "";
        int selection = -1;
        int alt_selection = 0;
        ListModel dlm;
        XincoCoreDataType xcdt = null;
        this.operatorComboBox.setSelectedIndex(0);
        //load fields
        this.optionsComboBox.removeAllItems();
        this.optionsComboBox.addItem(" ");
        this.optionsComboBox.addItem(xerb.getString("window.search.filecontent") + " (file)");
        text = "";
        for (i=0;i<explorer.getSession().server_datatypes.size();i++) {
            xcdt = (XincoCoreDataType)explorer.getSession().server_datatypes.elementAt(i);
            for (j=0;j<xcdt.getXinco_core_data_type_attributes().size();j++) {
                text = ((XincoCoreDataTypeAttribute)xcdt.getXinco_core_data_type_attributes().elementAt(j)).getDesignation();
                this.optionsComboBox.addItem(text);
            }
        }
        this.optionsComboBox.setSelectedIndex(0);
        //load languages
        dlm = this.languageList.getModel();
        this.languageList.removeAll();
        final Vector list=new Vector();
        selection = -1;
        alt_selection = 0;
        text = "";
        for (i=0;i<explorer.getSession().server_languages.size();i++) {
            text = ((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getDesignation() + " (" + ((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getSign() + ")";
            list.add(text);
            if (((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getSign().toLowerCase().compareTo(Locale.getDefault().getLanguage().toLowerCase()) == 0) {
                selection = i;
            }
            if (((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getId() == 1) {
                alt_selection = i;
            }
        }
        if (selection == -1) {
            selection = alt_selection;
        }
        this.languageList.setModel(new javax.swing.AbstractListModel() {
            Vector strings = list;
            public int getSize() { return strings.size(); }
            public Object getElementAt(int i) { return strings.get(i); }
        });
        this.languageList.setSelectedIndex(selection);
        this.languageList.ensureIndexIsVisible(this.languageList.getSelectedIndex());
        this.setBounds(0, 0, (new Double(getToolkit().getScreenSize().getWidth())).intValue()-100,
                (new Double(getToolkit().getScreenSize().getHeight())).intValue()-75);
    }
    
    public void clearResults(){
        this.resultTable.removeAll();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        builderLabel = new javax.swing.JLabel();
        operatorComboBox = new javax.swing.JComboBox();
        optionsComboBox = new javax.swing.JComboBox();
        variableField = new javax.swing.JTextField();
        addToQueryButton = new javax.swing.JButton();
        systemOptionsLabel = new javax.swing.JLabel();
        systemOptionsValueLabel = new javax.swing.JLabel();
        queryLabel = new javax.swing.JLabel();
        queryValueField = new javax.swing.JTextField();
        resetButton = new javax.swing.JButton();
        languageLabel = new javax.swing.JLabel();
        allLanguagesCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        languageList = new javax.swing.JList();
        searchButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
        goToSelectionButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        builderLabel.setText("jLabel1");

        operatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "AND", "OR", "NOT", "+", "-" }));
        operatorComboBox.setAutoscrolls(true);

        optionsComboBox.setAutoscrolls(true);

        addToQueryButton.setText("jButton1");
        addToQueryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToQueryButtonActionPerformed(evt);
            }
        });

        systemOptionsLabel.setText("jLabel1");

        systemOptionsValueLabel.setText("jLabel1");

        queryLabel.setText("jLabel1");

        resetButton.setText("jButton1");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        languageLabel.setText("jLabel1");

        allLanguagesCheckBox.setSelected(true);
        allLanguagesCheckBox.setText("jCheckBox1");
        allLanguagesCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allLanguagesCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        allLanguagesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                allLanguagesCheckBoxItemStateChanged(evt);
            }
        });

        languageList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "x" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(languageList);

        searchButton.setText("jButton1");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setDoubleBuffered(true);
        resultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(resultTable);

        goToSelectionButton.setText("jButton1");
        goToSelectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToSelectionButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, systemOptionsValueLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, builderLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 532, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, systemOptionsLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(queryLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(queryValueField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 407, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(resetButton))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(languageLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(allLanguagesCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 222, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 105, Short.MAX_VALUE)
                                .add(searchButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                            .add(goToSelectionButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, variableField)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(operatorComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(optionsComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 391, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(addToQueryButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(builderLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(operatorComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(optionsComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(variableField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(addToQueryButton))
                .add(12, 12, 12)
                .add(systemOptionsLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(systemOptionsValueLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(queryLabel)
                    .add(queryValueField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(resetButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(languageLabel)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(searchButton)
                    .add(allLanguagesCheckBox))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(goToSelectionButton)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void goToSelectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToSelectionButtonActionPerformed
        if (this.resultTable.getSelectedRow() < 0) {
            return;
        }
        Vector v = (Vector)explorer.getSession().currentSearchResult.elementAt(this.resultTable.getSelectedRow());
        int i = 0;
        int j = 0;
        int k = 0;
        TreePath tp = null;
        try {
            //expand tree to selected result (check root items first, then check all sub-folders)
            XincoMutableTreeNode xmtn = (XincoMutableTreeNode)explorer.getSession().xincoClientRepository.treemodel.getRoot();
            if (xmtn.getUserObject().getClass() == XincoCoreNode.class) {
                if (((XincoCoreNode)xmtn.getUserObject()).getId() == ((XincoCoreNode)v.elementAt(1)).getId()) {
                    tp = new TreePath(xmtn.getPath());
                    this.explorer.jTreeRepository.setSelectionPath(tp);
                    this.explorer.jTreeRepository.expandPath(tp);
                    j = -1;
                    //select data
                    if (1 == (v.size()-1)) {
                        for (k=0;k<xmtn.getChildCount();k++) {
                            if (((XincoMutableTreeNode)xmtn.getChildAt(k)).getUserObject().getClass() == XincoCoreData.class) {
                                if (((XincoCoreData)((XincoMutableTreeNode)xmtn.getChildAt(k)).getUserObject()).getId() == ((XincoCoreData)v.elementAt(0)).getId()) {
                                    tp = new TreePath(((XincoMutableTreeNode)xmtn.getChildAt(k)).getPath());
                                    this.explorer.jTreeRepository.setSelectionPath(tp);
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
                            this.explorer.jTreeRepository.setSelectionPath(tp);
                            this.explorer.jTreeRepository.expandPath(tp);
                            xmtn = (XincoMutableTreeNode)xmtn.getChildAt(j);
                            j = -1;
                            //select data
                            if (i == (v.size()-1)) {
                                for (k=0;k<xmtn.getChildCount();k++) {
                                    if (((XincoMutableTreeNode)xmtn.getChildAt(k)).getUserObject().getClass() == XincoCoreData.class) {
                                        if (((XincoCoreData)((XincoMutableTreeNode)xmtn.getChildAt(k)).getUserObject()).getId() == ((XincoCoreData)v.elementAt(0)).getId()) {
                                            tp = new TreePath(((XincoMutableTreeNode)xmtn.getChildAt(k)).getPath());
                                            this.explorer.jTreeRepository.setSelectionPath(tp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception tee) {
            tee.printStackTrace();
        }
        this.setVisible(false);
    }//GEN-LAST:event_goToSelectionButtonActionPerformed
    
    private void allLanguagesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_allLanguagesCheckBoxItemStateChanged
        this.languageList.setEnabled(!this.allLanguagesCheckBox.isSelected());
    }//GEN-LAST:event_allLanguagesCheckBoxItemStateChanged
    
    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        this.queryValueField.setText("");
    }//GEN-LAST:event_resetButtonActionPerformed
    
    private void addToQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToQueryButtonActionPerformed
        String operator = "";
        String field = "";
        //create operator string
        if (this.operatorComboBox.getSelectedIndex() != 0) {
            operator = (String)this.operatorComboBox.getSelectedItem();
            operator = operator + " ";
        } else {
            operator = "";
        }
        //create field string
        if (this.optionsComboBox.getSelectedIndex() > 1) {
            field = ((String)this.optionsComboBox.getSelectedItem()) + ":";
        } else if (this.optionsComboBox.getSelectedIndex() == 1) {
            field = "file:";
        } else {
            field = "";
        }
        // append to query
        this.queryValueField.setText(this.queryValueField.getText() + operator + field + this.variableField.getText() + " ");
        this.variableField.setText("");
    }//GEN-LAST:event_addToQueryButtonActionPerformed
    
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        searchThread search = new searchThread();
        search.start();
    }//GEN-LAST:event_searchButtonActionPerformed
    private class searchThread extends Thread{
        @Override
        public void run() {
            XincoProgressBarThread progressBar = new XincoProgressBarThread(explorer);
            progressBar.run();
            progressBar.setTitle(xerb.getString("message.search.progressbar"));
            progressBar.show();
            int i=0, j=0;
            //select language OR all languages!
            XincoCoreLanguage lid = new XincoCoreLanguage();
            lid.setId(0);
            if ((!allLanguagesCheckBox.isSelected()) && (languageList.getSelectedIndex() >= 0)) {
                lid = (XincoCoreLanguage)explorer.getSession().server_languages.elementAt(languageList.getSelectedIndex());
            }
            try {
                if ((explorer.getSession().currentSearchResult = explorer.getSession().xinco.findXincoCoreData(queryValueField.getText(), lid, explorer.getSession().user)) == null) {
                    throw new XincoException();
                }
            } catch (Exception rme) {
                explorer.getSession().currentSearchResult = new Vector();
            }
            //update search result
            String[] rdata = {"", ""};
            DefaultTableModel dtm = (DefaultTableModel)resultTable.getModel();
            j = dtm.getRowCount();
            for (i=0;i<j;i++) {
                dtm.removeRow(0);
            }
            for (i=0;i<explorer.getSession().currentSearchResult.size();i++) {
                rdata[0] = ((XincoCoreData)(((Vector)explorer.getSession().currentSearchResult.elementAt(i)).elementAt(0))).getDesignation() + " (" + ((XincoCoreData)(((Vector)explorer.getSession().currentSearchResult.elementAt(i)).elementAt(0))).getXinco_core_data_type().getDesignation() + " | " + ((XincoCoreData)(((Vector)explorer.getSession().currentSearchResult.elementAt(i)).elementAt(0))).getXinco_core_language().getSign() + ")";
                rdata[1] = new String("");
                for (j=1;j<((Vector)explorer.getSession().currentSearchResult.elementAt(i)).size();j++) {
                    rdata[1] = rdata[1] + ((XincoCoreNode)(((Vector)explorer.getSession().currentSearchResult.elementAt(i)).elementAt(j))).getDesignation() + " / ";
                }
                dtm.addRow(rdata);
            }
            resultTable.repaint();
            progressBar.hide();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToQueryButton;
    private javax.swing.JCheckBox allLanguagesCheckBox;
    private javax.swing.JLabel builderLabel;
    private javax.swing.JButton goToSelectionButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JList languageList;
    private javax.swing.JComboBox operatorComboBox;
    private javax.swing.JComboBox optionsComboBox;
    private javax.swing.JLabel queryLabel;
    private javax.swing.JTextField queryValueField;
    private javax.swing.JButton resetButton;
    private javax.swing.JTable resultTable;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel systemOptionsLabel;
    private javax.swing.JLabel systemOptionsValueLabel;
    private javax.swing.JTextField variableField;
    // End of variables declaration//GEN-END:variables
    
}
