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
 * Name:            ChangeReasonDialog
 *
 * Description:     Change Reason Dialog
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
 * ChangeReasonDialog.java
 *
 * Created on September 26, 2006, 2:11 PM
 */
package com.bluecubs.xinco.client.dialog;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoException;
import javax.swing.JOptionPane;

/**
 * Change Reason Dialog
 * @author Javier A. Ortiz
 */
public class ChangeReasonDialog extends javax.swing.JDialog {

    private XincoCoreUser user;
    private XincoExplorer explorer = null;
    private String reasonS = "";
    /**
     * Is dialog done?
     */
    boolean done = false;

    /**
     * Creates new form ChangeReasonDialog
     * @param parent Dialog's parent
     * @param modal Is modal?
     * @param explorer 
     * @throws com.bluecubs.xinco.core.XincoException XincoException thrown
     */
    public ChangeReasonDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer) throws XincoException {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.user = explorer.getSession().getUser();
        this.explorer = explorer;
        setTitle(explorer.getResourceBundle().getString("window.changereason.title"));
        this.reasonLabel.setText(explorer.getResourceBundle().getString("window.changereason.label"));
        this.save.setText(explorer.getResourceBundle().getString("general.save") + "!");
        this.cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        setLocationRelativeTo(null);
    }

    /**
     * Get the specified reason.
     * @return Specified reason.
     */
    public String getReason() {
        return this.reasonS;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        reason = new javax.swing.JTextArea();
        reasonLabel = new javax.swing.JLabel();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setModal(true);
        reason.setColumns(20);
        reason.setLineWrap(true);
        reason.setRows(5);
        reason.setWrapStyleWord(true);
        jScrollPane1.setViewportView(reason);

        reasonLabel.setText("jLabel1");

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
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
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(reasonLabel)
                    .add(layout.createSequentialGroup()
                        .add(save)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 40, Short.MAX_VALUE)
                        .add(cancel)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(reasonLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(save)
                    .add(cancel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        //Reason can't be empty'
        if (!reason.getText().trim().equals("")) {
            this.user.setReason(reason.getText());
            this.reasonS = reason.getText();
            setVisible(false);
            this.done = true;
        } else {
            JOptionPane.showMessageDialog(this,
                    explorer.getResourceBundle().getString("message.warning.reason"),
                    explorer.getResourceBundle().getString("general.error"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_saveActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        this.done = true;
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea reason;
    private javax.swing.JLabel reasonLabel;
    private javax.swing.JButton save;
    // End of variables declaration//GEN-END:variables
}
