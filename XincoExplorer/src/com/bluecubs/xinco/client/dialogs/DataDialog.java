/**
 *Copyright 2006 blueCubs.com
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
 * Name:            DataDialog
 *
 * Description:     Data Dialog
 *
 * Original Author: Javier A. Ortiz
 * Date:            2006
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *
 *************************************************************
 * DataDialog.java
 *
 * Created on November 22, 2006, 10:38 AM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreLanguage;
import java.util.Locale;
import javax.swing.DefaultListModel;

/**
 * Data Dialog
 * @author ortizbj
 */
public class DataDialog extends javax.swing.JDialog {
    private XincoExplorer explorer=null;
    /**
     * Creates new form DataDialog
     * @param parent Dialog's parent.
     * @param modal Is modal?
     * @param explorer Related XincoExplorer.
     */
    public DataDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.explorer=explorer;
        setTitle(explorer.getResourceBundle().getString("window.datadetails"));
        this.save.setText(explorer.getResourceBundle().getString("general.save") + "!");
        this.cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        this.idLabel.setText(explorer.getResourceBundle().getString("general.id")+ ":");
        this.designationLabel.setText(explorer.getResourceBundle().getString("general.designation")+ ":");
        this.languageLabel.setText(explorer.getResourceBundle().getString("general.language")+ ":");
        this.statusLabel.setText(explorer.getResourceBundle().getString("general.status")+ ":");
        
        //processing independent of creation
        int i = 0;
        String text = "";
        int selection = -1;
        int alt_selection = 0;
        if (explorer.getSession().currentTreeNodeSelection.getUserObject() != null) {
            text = "" + ((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId();
            this.id.setText(text);
            text = "" + ((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getDesignation();
            this.designation.setText(text);
            this.designation.selectAll();
            DefaultListModel dlm = new DefaultListModel();
            dlm.removeAllElements();
            for (i=0;i<explorer.getSession().server_languages.size();i++) {
                text = ((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getDesignation() + " (" + ((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getSign() + ")";
                dlm.addElement(text);
                if (((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getId() == ((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_language().getId()) {
                    selection=i;
                }
                if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId() == 0) {
                    if (((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getSign().toLowerCase().compareTo(Locale.getDefault().getLanguage().toLowerCase()) == 0) {
                        selection = i;
                    }
                    if (((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getId() == 1) {
                        alt_selection = i;
                    }
                }
            }
            this.language.setModel(dlm);
            if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId() == 0) {
                if (selection == -1) {
                    selection = alt_selection;
                }
            }
            this.language.setSelectedIndex(selection);
            language.ensureIndexIsVisible(language.getSelectedIndex());
            if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getStatus_number() == 1) {
                text = explorer.getResourceBundle().getString("general.status.open") + "";
            }
            if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getStatus_number() == 2) {
                text = explorer.getResourceBundle().getString("general.status.locked") + " (-)";
            }
            if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getStatus_number() == 3) {
                text = explorer.getResourceBundle().getString("general.status.archived") + " (->)";
            }
            if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getStatus_number() == 4) {
                text = explorer.getResourceBundle().getString("general.status.checkedout") + " (X)";
            }
            if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getStatus_number() == 5) {
                text = explorer.getResourceBundle().getString("general.status.published") + " (WWW)";
            }
            this.status.setText(text);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        idLabel = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        designationLabel = new javax.swing.JLabel();
        designation = new javax.swing.JTextField();
        languageLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        language = new javax.swing.JList();
        statusLabel = new javax.swing.JLabel();
        status = new javax.swing.JTextField();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        idLabel.setText("jLabel1");

        id.setEditable(false);
        id.setText("jTextField1");

        designationLabel.setText("jLabel1");

        designation.setText("jTextField1");

        languageLabel.setText("jLabel1");

        jScrollPane1.setViewportView(language);

        statusLabel.setText("jLabel1");

        status.setEditable(false);
        status.setText("jTextField1");

        save.setText("jButton1");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        cancel.setText("jButton2");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(idLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(designationLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                .add(9, 9, 9))
                            .add(layout.createSequentialGroup()
                                .add(languageLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                .add(9, 9, 9))
                            .add(layout.createSequentialGroup()
                                .add(statusLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                .add(9, 9, 9)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(status, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .add(designation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .add(id, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(save, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                        .add(71, 71, 71)
                        .add(cancel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(idLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(id))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(designationLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(designation))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(languageLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(118, 118, 118))
                    .add(jScrollPane1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(statusLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(status))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(save, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cancel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed
    
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
//set altered values
        ((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).setDesignation(this.designation.getText());
        ((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).setXinco_core_language(((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(this.language.getSelectedIndex())));
        explorer.set_global_dialog_return_value(1);
        setVisible(false);
    }//GEN-LAST:event_saveActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JTextField designation;
    private javax.swing.JLabel designationLabel;
    private javax.swing.JTextField id;
    private javax.swing.JLabel idLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList language;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JButton save;
    private javax.swing.JTextField status;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
    
}
