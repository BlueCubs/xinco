/**
 *Copyright 2009 blueCubs.com
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
 * Name:            DataTypeDialog
 *
 * Description:     Data Type Dialog
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
 * DataTypeDialog.java
 *
 * Created on November 22, 2006, 8:44 AM
 */
package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreDataType;
import javax.swing.DefaultListModel;

/**
 * Data Type Dialog
 * @author Javier A. Ortiz
 */
public class DataTypeDialog extends AbstractDialog {

    private XincoExplorer explorer = null;

    /**
     * Creates new form DataTypeDialog
     * @param parent Dialog's parent.
     * @param modal Is modal?
     * @param explorer Related XincoExplorer.
     */
    public DataTypeDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.explorer = explorer;
        setTitle(explorer.getResourceBundle().getString("window.datatype"));
        this.continueButton.setText(explorer.getResourceBundle().getString("general.continue"));
        this.cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        this.dataTypeLabel.setText(explorer.getResourceBundle().getString("window.datatype.datatype") + ":");
        int i = 0;
        String text = "";
        if (explorer.getSession().getCurrentTreeNodeSelection().getUserObject() != null) {
            DefaultListModel dlm = new DefaultListModel();
            dlm.removeAllElements();
            for (i = 0; i < explorer.getSession().getServerDatatypes().size(); i++) {
                text = ((XincoCoreDataType) explorer.getSession().getServerDatatypes().elementAt(i)).getDesignation() + " (" + ((XincoCoreDataType) explorer.getSession().getServerDatatypes().elementAt(i)).getDescription() + ")";
                dlm.addElement(text);
                if (((XincoCoreDataType) explorer.getSession().getServerDatatypes().elementAt(i)).getId() == ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_data_type().getId()) {
                    this.dataType.setSelectedIndex(i);
                }
            }
            this.dataType.setModel(dlm);
        }
        setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        dataTypeLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataType = new javax.swing.JList();
        continueButton = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModal(true);
        dataTypeLabel.setText("jLabel1");

        jScrollPane1.setViewportView(dataType);

        continueButton.setText("jButton2");
        continueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueButtonActionPerformed(evt);
            }
        });

        cancel.setText("jButton1");
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
                        .add(dataTypeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(continueButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 86, Short.MAX_VALUE)
                        .add(cancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1)
                    .add(dataTypeLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(continueButton)
                    .add(cancel))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed

    private void continueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueButtonActionPerformed
        ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).setXinco_core_data_type((XincoCoreDataType) explorer.getSession().getServerDatatypes().elementAt(this.dataType.getSelectedIndex()));
        explorer.set_global_dialog_return_value(1);
        setVisible(false);
    }//GEN-LAST:event_continueButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JButton continueButton;
    private javax.swing.JList dataType;
    private javax.swing.JLabel dataTypeLabel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
