/*
 * ChangeReasonDialog.java
 *
 * Created on September 26, 2006, 2:11 PM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoException;
import java.awt.HeadlessException;
import javax.swing.JOptionPane; 

/**
 * Change Reason Dialog
 * @author ortizbj
 */
public class ChangeReasonDialog extends javax.swing.JDialog {
    private XincoCoreUser user;
    private XincoExplorer explorer=null;
    private String reasonS="";
    /**
     * Is dialog done?
     */
    public boolean done=false;
    /**
     * Creates new form ChangeReasonDialog
     * @param parent Dialog's parent
     * @param modal Is modal?
     * @param user User making the change.
     * @param explorer Related XincoExplorer.
     * @throws com.bluecubs.xinco.core.XincoException XincoException thrown
     */
    public ChangeReasonDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer) throws XincoException{
        super(parent, modal);
        initComponents();
        this.user=explorer.getSession().user;
        this.explorer=explorer;
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
        cancel.setSelected(true);
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
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 38, Short.MAX_VALUE)
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
        if(!reason.getText().trim().equals("")){
            this.user.setReason(reason.getText());
            this.reasonS=reason.getText();
            setVisible(false);
            this.done=true;
        }
        else
            JOptionPane.showMessageDialog(this, 
                    explorer.getResourceBundle().getString("message.warning.reason"),
                    explorer.getResourceBundle().getString("general.error"), 
                    JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_saveActionPerformed
    
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        this.done=true;
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
