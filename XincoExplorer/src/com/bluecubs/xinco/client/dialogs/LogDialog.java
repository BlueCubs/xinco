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
 * Name:            LogDialog
 *
 * Description:     Log Dialog
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
 * LogDialog.java
 *
 * Created on November 22, 2006, 10:09 AM
 */
package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreLog;
import javax.swing.JOptionPane;

/**
 * 
 * Log Dialog
 * @author Javier A. Ortiz
 */
public class LogDialog extends AbstractDialog {

    private XincoExplorer explorer = null;
    private int log_index = 0;
    private boolean editableVersion;

    /**
     * Creates new form LogDialog
     * @param parent Dialog's parent
     * @param modal Is modal?
     * @param explorer Related XincoExplorer
     * @param editableVersion
     */
    public LogDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer, boolean editableVersion) {
        super(parent, modal);
        this.editableVersion = editableVersion;
        initComponents();
        setLocationRelativeTo(null);
        this.explorer = explorer;
        setTitle(explorer.getResourceBundle().getString("window.loggingdetails"));
        actionLabel.setText(explorer.getResourceBundle().getString("window.loggingdetails.action") + ":");
        versionLabel.setText(explorer.getResourceBundle().getString("general.version") + ":");
        continueButton.setText(explorer.getResourceBundle().getString("general.continue"));
        cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        versionPostfixLabel.setText(explorer.getResourceBundle().getString("general.version.postfix"));
        versionPostfixExplanation.setText(explorer.getResourceBundle().getString("general.version.postfix.explanation"));
        reasonLabel.setText(explorer.getResourceBundle().getString("general.reason"));
        minorChange.setText(explorer.getResourceBundle().getString("general.minor"));
        minorChange.setToolTipText(explorer.getResourceBundle().getString("general.minor.tooltip"));
        //processing independent of creation
        if (explorer.getSession().getCurrentTreeNodeSelection().getUserObject() != null) {
            //For some reason
            log_index = ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().size() - 1;
            //Only enabled for checkin, modify and/or comment
            reason.setEnabled(((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == OPCode.CHECKIN.ordinal() + 1 ||
                    ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == OPCode.COMMENT_COMMENT.ordinal() + 1 ||
                    ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == OPCode.MODIFICATION.ordinal() + 1);
            action.setText(((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_description());
            versionHigh.setText("" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_high()));
            versionMid.setText("" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_mid()));
            versionLow.setText("" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_low()));
            versionPostfix.setText("" + ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_postfix());
        }
        //Enable only for checkin
        minorChange.setEnabled(((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == OPCode.CHECKIN.ordinal() + 1);
        updateVersion();
    }

    /**
     * Updates the version based on the minor change selection. This avoids issues with
     * manually incorrect versions.
     */
    private void updateVersion() {
        versionHigh.setEditable(editableVersion);
        versionMid.setEditable(editableVersion);
        versionLow.setEditable(editableVersion);
        if (minorChange.isEnabled()) {
            if (minorChange.isSelected()) {
                versionHigh.setText("" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_high()));
                versionMid.setText("" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_mid() + 1));
                versionLow.setText("" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_low()));
                //TODO set different postfixes for workflows: draft, review, etc...
                versionPostfix.setText("Draft");
            } else {
                versionHigh.setText("" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_high() + 1));
                versionMid.setText("" + 0);
                versionLow.setText("" + 0);
                versionPostfix.setText("");
            }
        }
        //Increase low after adding a comment or changing metadata
        if (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == OPCode.COMMENT_COMMENT.ordinal() + 1 ||
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == OPCode.MODIFICATION.ordinal() + 1) {
            versionLow.setText("" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_low() + 1));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        actionLabel = new javax.swing.JLabel();
        action = new javax.swing.JTextField();
        versionLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        versionPostfixLabel = new javax.swing.JLabel();
        versionPostfix = new javax.swing.JTextField();
        continueButton = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        versionPostfixExplanation = new javax.swing.JTextField();
        reasonLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        reason = new javax.swing.JTextArea();
        minorChange = new javax.swing.JCheckBox();
        versionHigh = new javax.swing.JFormattedTextField();
        versionMid = new javax.swing.JFormattedTextField();
        versionLow = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        actionLabel.setText("jLabel1");

        action.setEditable(false);
        action.setText("jTextField1");

        versionLabel.setText("jLabel1");

        jLabel1.setText(".");

        jLabel2.setText(".");

        versionPostfixLabel.setText("jLabel3");

        versionPostfix.setText("jTextField1");

        continueButton.setText("jButton1");
        continueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueButtonActionPerformed(evt);
            }
        });

        cancel.setText("jButton2");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        versionPostfixExplanation.setEditable(false);
        versionPostfixExplanation.setText("jTextField1");

        reasonLabel.setText("jLabel3");

        reason.setColumns(20);
        reason.setRows(5);
        jScrollPane1.setViewportView(reason);

        minorChange.setText("jCheckBox1");
        minorChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minorChangeActionPerformed(evt);
            }
        });

        versionHigh.setEditable(false);
        versionHigh.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));

        versionMid.setEditable(false);
        versionMid.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));

        versionLow.setEditable(false);
        versionLow.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(actionLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(action, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(reasonLabel)
                            .add(versionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(versionHigh, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 81, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(versionMid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 81, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 11, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(versionLow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 81, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(12, 12, 12))
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(versionPostfixLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(versionPostfix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(minorChange)
                            .add(continueButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, cancel)
                            .add(versionPostfixExplanation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(actionLabel)
                    .add(action, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(reasonLabel)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(versionLabel)
                    .add(versionHigh, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1)
                    .add(versionMid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2)
                    .add(versionLow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(16, 16, 16)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(versionPostfixLabel)
                    .add(versionPostfix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(versionPostfixExplanation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(minorChange)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 10, Short.MAX_VALUE)
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
        if (this.reason.getText().trim().equals("") && ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == 5) {
            JOptionPane.showMessageDialog(this,
                    explorer.getResourceBundle().getString("message.warning.reason"),
                    explorer.getResourceBundle().getString("general.error"),
                    JOptionPane.WARNING_MESSAGE);
        } else {
            log_index = 0;
            String text = "";
            log_index = ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().size() - 1;
            //Reason really needed only for checkin
            text = action.getText() + " " +
                    (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == 3 ? explorer.getResourceBundle().getString("general.status.checkedout") : this.reason.getText());
            ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).setOp_description(text);
            text = versionHigh.getText();
            try {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_high(Integer.parseInt(text));
            } catch (Exception nfe) {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_high(0);
            }
            text = versionMid.getText();
            try {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_mid(Integer.parseInt(text));
            } catch (Exception nfe) {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_mid(0);
            }
            text = versionLow.getText();
            try {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_low(Integer.parseInt(text));
            } catch (Exception nfe) {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_low(0);
            }
            text = versionPostfix.getText();
            ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_postfix(text);
            explorer.set_global_dialog_return_value(1);
            setVisible(false);
            updateVersion();
        }
    }//GEN-LAST:event_continueButtonActionPerformed

    private void minorChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minorChangeActionPerformed
        updateVersion();
    }//GEN-LAST:event_minorChangeActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField action;
    private javax.swing.JLabel actionLabel;
    private javax.swing.JButton cancel;
    private javax.swing.JButton continueButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox minorChange;
    private javax.swing.JTextArea reason;
    private javax.swing.JLabel reasonLabel;
    private javax.swing.JFormattedTextField versionHigh;
    private javax.swing.JLabel versionLabel;
    private javax.swing.JFormattedTextField versionLow;
    private javax.swing.JFormattedTextField versionMid;
    private javax.swing.JTextField versionPostfix;
    private javax.swing.JTextField versionPostfixExplanation;
    private javax.swing.JLabel versionPostfixLabel;
    // End of variables declaration//GEN-END:variables
}
