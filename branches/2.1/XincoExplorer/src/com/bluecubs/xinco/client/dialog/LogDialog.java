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
package com.bluecubs.xinco.client.dialog;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreLog;
import javax.swing.JOptionPane;

/**
 * 
 * Log Dialog
 * @author Javier A. Ortiz
 */
public class LogDialog extends javax.swing.JDialog {

    private XincoExplorer explorer = null;
    private int log_index = 0;

    /**
     * Creates new form LogDialog
     * @param parent Dialog's parent
     * @param modal Is modal?
     * @param explorer Related XincoExplorer
     */
    public LogDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.explorer = explorer;
        setTitle(explorer.getResourceBundle().getString("window.loggingdetails"));
        this.actionLabel.setText(explorer.getResourceBundle().getString("window.loggingdetails.action") + ":");
        this.versionLabel.setText(explorer.getResourceBundle().getString("general.version") + ":");
        this.continueButton.setText(explorer.getResourceBundle().getString("general.continue"));
        this.cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        this.versionPostfixLabel.setText(explorer.getResourceBundle().getString("general.version.postfix"));
        this.versionPostfixExplanation.setText(explorer.getResourceBundle().getString("general.version.postfix.explanation"));
        this.reasonLabel.setText(explorer.getResourceBundle().getString("general.reason"));

        //processing independent of creation
        int i = 0;
        String text = "";
        if (explorer.getSession().getCurrentTreeNodeSelection().getUserObject() != null) {
            //For some reason 
            log_index = ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().size() - 1;
            if (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == 1) {
                this.reason.setEnabled(false);
            } else {
                this.reason.setEnabled(true);
            }
            text = "" + ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_description();
            this.action.setText(text);
            //Increase high after a checkin
            if (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == 5) {
                text = "" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_high() + 1);
                this.versionHigh.setEditable(false);
            } else {
                text = "" + ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_high();
                this.versionHigh.setEditable(true);
            }
            this.versionHigh.setText(text);
            //TODO Increase based on workflow
//            if(((XincoCoreLog)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code()==<review op code>)
//                text = "" + ((XincoCoreLog)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_mid()+1);
//            else
            text = "" + ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_mid();
            this.versionMid.setText(text);
            //Increase low after adding a comment or changing metadata
            if (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() == 9) {
                text = "" + (((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_low() + 1);
                this.versionLow.setEditable(false);
            } else {
                text = "" + ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_low();
                this.versionLow.setEditable(true);
            }
            this.versionLow.setText(text);
            //TODO set different postfixes for workflows: draft, review, etc...
//            if(((XincoCoreLog)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code()==<review op code>)
//                text = "Draft"; //example
//            else
            text = "" + ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().getVersion_postfix();
            this.versionPostfix.setText(text);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        actionLabel = new javax.swing.JLabel();
        action = new javax.swing.JTextField();
        versionLabel = new javax.swing.JLabel();
        versionHigh = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        versionMid = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        versionLow = new javax.swing.JTextField();
        versionPostfixLabel = new javax.swing.JLabel();
        versionPostfix = new javax.swing.JTextField();
        continueButton = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        versionPostfixExplanation = new javax.swing.JTextField();
        reasonLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        reason = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        actionLabel.setText("jLabel1");

        action.setEditable(false);
        action.setText("jTextField1");

        versionLabel.setText("jLabel1");

        versionHigh.setEditable(false);
        versionHigh.setText("jTextField1");

        jLabel1.setText(".");

        versionMid.setEditable(false);
        versionMid.setText("jTextField1");

        jLabel2.setText(".");

        versionLow.setEditable(false);
        versionLow.setText("jTextField1");

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
                        .add(action, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(reasonLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(versionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(versionHigh, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(versionMid, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                        .add(4, 4, 4)
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(versionLow, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(versionPostfixLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(versionPostfix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(versionPostfixExplanation, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(continueButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 180, Short.MAX_VALUE)
                        .add(cancel)))
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(continueButton)
                    .add(cancel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed

    private void continueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueButtonActionPerformed
        if (this.reason.getText().trim().equals("") && ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getOp_code() != 1) {
            JOptionPane.showMessageDialog(this,
                    explorer.getResourceBundle().getString("message.warning.reason"),
                    explorer.getResourceBundle().getString("general.error"),
                    JOptionPane.WARNING_MESSAGE);
        } else {
            log_index = 0;
            String text = "";
            log_index = ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().size() - 1;
            text = this.action.getText() + " " + this.reason.getText();
            ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).setOp_description(text);
            text = this.versionHigh.getText();
            try {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_high(Integer.parseInt(text));
            } catch (Exception nfe) {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_high(0);
            }
            text = this.versionMid.getText();
            try {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_mid(Integer.parseInt(text));
            } catch (Exception nfe) {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_mid(0);
            }
            text = this.versionLow.getText();
            try {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_low(Integer.parseInt(text));
            } catch (Exception nfe) {
                ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_low(0);
            }
            text = this.versionPostfix.getText();
            ((XincoCoreLog) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_logs().elementAt(log_index)).getVersion().setVersion_postfix(text);
            explorer.setGlobalDialogReturnValue(1);
            setVisible(false);
        }
    }//GEN-LAST:event_continueButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField action;
    private javax.swing.JLabel actionLabel;
    private javax.swing.JButton cancel;
    private javax.swing.JButton continueButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea reason;
    private javax.swing.JLabel reasonLabel;
    private javax.swing.JTextField versionHigh;
    private javax.swing.JLabel versionLabel;
    private javax.swing.JTextField versionLow;
    private javax.swing.JTextField versionMid;
    private javax.swing.JTextField versionPostfix;
    private javax.swing.JTextField versionPostfixExplanation;
    private javax.swing.JLabel versionPostfixLabel;
    // End of variables declaration//GEN-END:variables
}
