/*
 * LockDialog.java
 *
 * Created on March 12, 2007, 11:09 AM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreUser;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author  Javier A. Ortiz
 */
public class LockDialog extends javax.swing.JDialog {
    private XincoExplorer explorer=null;
    /**
     * Creates new form LockDialog
     */
    public LockDialog(java.awt.Frame parent, boolean modal,XincoExplorer e) {
        super(parent, modal);
        this.explorer=e;
        setLocationRelativeTo(null);
        initComponents();
        this.passwordLabel.setText(explorer.getResourceBundle().getString("general.password"));
        this.disconnect.setText(explorer.getResourceBundle().getString("menu.disconnect"));
        this.password.setText("");
        this.connect.setText(explorer.getResourceBundle().getString("menu.connection.connect"));
        this.setTitle(explorer.getResourceBundle().getString("general.login"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel2 = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        connect = new javax.swing.JButton();
        disconnect = new javax.swing.JButton();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        passwordLabel.setText("jLabel1");

        connect.setText("jButton1");
        connect.setSelected(true);
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });

        disconnect.setText("jButton2");
        disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(connect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(disconnect))
                    .addComponent(passwordLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(passwordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connect)
                    .addComponent(disconnect))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
//        try {
//            User might be locked by any user attempting to log in incorrectly
//            XincoCoreUser temp=null;
//            if ((temp = this.explorer.getSession().xinco.getCurrentXincoCoreUser(this.explorer.getSession().user.getUsername(),
//                    new String(this.password.getPassword()))) == null){
        if(!new String(this.password.getPassword()).equals(this.explorer.getSession().user.getUserpassword())){
                JOptionPane.showMessageDialog(this.explorer,
                        explorer.getResourceBundle().getString("menu.connection.error.user"),
                        explorer.getResourceBundle().getString("password.login.fail"),
                        JOptionPane.INFORMATION_MESSAGE);
            } else{
                this.explorer.setLock(false);
                this.setVisible(false);
            }
//        } catch (RemoteException ex) {
//        }
        this.password.setText("");
    }//GEN-LAST:event_connectActionPerformed
    
    private void disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectActionPerformed
        this.setVisible(false);
        this.explorer.setLock(false);
        this.explorer.resetTimer();
        this.explorer.getSession().status=0;
        this.explorer.markConnectionStatus();
        this.explorer.collapseAllNodes();
        this.password.setText("");
    }//GEN-LAST:event_disconnectActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connect;
    private javax.swing.JButton disconnect;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel passwordLabel;
    // End of variables declaration//GEN-END:variables
    
}
