/*
 * AuditDialog.java
 *
 * Created on February 22, 2007, 9:41 AM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import java.util.ResourceBundle;

/**
 *
 * @author  Javier A. Ortiz
 */
public class AuditDialog extends javax.swing.JDialog {
    private XincoExplorer explorer=null;
    private ResourceBundle xerb;
    /** Creates new form AuditDialog */
    public AuditDialog(java.awt.Frame parent, boolean modal,XincoExplorer e) {
        super(parent, modal);
        this.explorer=e;
        this.xerb=this.explorer.getResourceBundle();
        setLocationRelativeTo(null);
        initComponents();
        this.auditCheckbox.setText(xerb.getString("window.archive.audit"));
        this.auditTypeLabel.setText(xerb.getString("window.archive.audit.type"));
        this.auditFlexibilityLabel.setText(xerb.getString("window.archive.audit.flexibility"));
        this.auditDateLabel.setText(xerb.getString("window.archive.audit.date"));
        this.cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        this.ok.setText(explorer.getResourceBundle().getString("general.ok"));
        this.auditTypeList.setEnabled(false);
        this.auditFlexibility.setEnabled(false);
        this.auditDate.setEnabled(false);
        //Inititalize the audit type list
        this.auditTypeList.removeAllItems();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        auditCheckbox = new javax.swing.JLabel();
        auditTypeLabel = new javax.swing.JLabel();
        auditFlexibilityLabel = new javax.swing.JLabel();
        auditDateLabel = new javax.swing.JLabel();
        auditDate = new com.toedter.calendar.JDateChooser();
        auditFlexibility = new javax.swing.JCheckBox();
        auditTypeList = new javax.swing.JComboBox();
        enableFileAudit = new javax.swing.JCheckBox();
        ok = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        auditCheckbox.setText("jLabel1");

        auditTypeLabel.setText("jLabel1");

        auditFlexibilityLabel.setText("jLabel1");

        auditDateLabel.setText("jLabel1");

        auditFlexibility.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        auditFlexibility.setMargin(new java.awt.Insets(0, 0, 0, 0));

        enableFileAudit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        enableFileAudit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        enableFileAudit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableFileAuditActionPerformed(evt);
            }
        });

        ok.setText("jButton1");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        cancel.setText("jButton2");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(auditCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(auditTypeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(auditFlexibilityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(auditDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(115, 115, 115)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(enableFileAudit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(176, 176, 176))
                            .addComponent(auditTypeList, 0, 189, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(auditFlexibility, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(176, 176, 176))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(auditDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(56, 56, 56))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(ok)
                        .addGap(30, 30, 30)
                        .addComponent(cancel)))
                .addGap(52, 52, 52))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(auditCheckbox)
                    .addComponent(enableFileAudit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(auditTypeList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(auditTypeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(auditFlexibility)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(auditDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(auditFlexibilityLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(auditDateLabel)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok)
                    .addComponent(cancel))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed
    
    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        
        explorer.getSession().status = 1;
    }//GEN-LAST:event_okActionPerformed
    
    private void enableFileAuditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableFileAuditActionPerformed
        this.auditTypeList.setEnabled(this.enableFileAudit.isSelected());
        this.auditFlexibility.setEnabled(this.enableFileAudit.isSelected());
        this.auditDate.setVisible(this.enableFileAudit.isSelected());
    }//GEN-LAST:event_enableFileAuditActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AuditDialog(new javax.swing.JFrame(), true,null).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel auditCheckbox;
    private com.toedter.calendar.JDateChooser auditDate;
    private javax.swing.JLabel auditDateLabel;
    private javax.swing.JCheckBox auditFlexibility;
    private javax.swing.JLabel auditFlexibilityLabel;
    private javax.swing.JLabel auditTypeLabel;
    private javax.swing.JComboBox auditTypeList;
    private javax.swing.JButton cancel;
    private javax.swing.JCheckBox enableFileAudit;
    private javax.swing.JButton ok;
    // End of variables declaration//GEN-END:variables
    
}
