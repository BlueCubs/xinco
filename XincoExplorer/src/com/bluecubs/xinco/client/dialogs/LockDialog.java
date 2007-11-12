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
 * Name:            LockDialog
 *
 * Description:     Lock Dialog
 *
 * Original Author: Javier Ortiz
 * Date:            March 12, 2007, 11:09 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.XincoMutableTreeNode;
import com.bluecubs.xinco.core.XincoCoreNode;
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
        this.connect.setText(explorer.getResourceBundle().getString("menu.connection.connect"));
        this.setTitle(explorer.getResourceBundle().getString("general.login"));
        this.passwordLabel.setText(explorer.getResourceBundle().getString("general.password"));
        this.usernameLabel.setText(explorer.getResourceBundle().getString("general.username"));
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
        usernameLabel = new javax.swing.JLabel();
        username = new javax.swing.JTextField();

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

        usernameLabel.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(connect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(disconnect))
                    .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(passwordLabel)
                    .addComponent(usernameLabel)
                    .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usernameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(passwordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connect)
                    .addComponent(disconnect))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        try {
            //User might be locked by any user attempting to log in incorrectly
            XincoCoreUser temp=temp = this.explorer.getSession().getXinco().getCurrentXincoCoreUser(this.username.getText(),
                    new String(this.password.getPassword()));
            if (temp == null){
                JOptionPane.showMessageDialog(this.explorer,
                        explorer.getResourceBundle().getString("menu.connection.error.user"),
                        explorer.getResourceBundle().getString("password.login.fail"),
                        JOptionPane.INFORMATION_MESSAGE);
            } else{
                if(!temp.getUsername().equals(this.explorer.getSession().getUser().getUsername())) {
                    JOptionPane.showMessageDialog(this.explorer,
                            explorer.getResourceBundle().getString("password.unlock.differentuser"),
                            explorer.getResourceBundle().getString("password.unlock.differentuser.detail"),
                            JOptionPane.INFORMATION_MESSAGE);
                    this.explorer.resetTimer();
                    //reconnect with new user info
                    this.explorer.getSession().setStatus(1);
                    temp.setUsername(this.username.getText());
                    temp.setUserpassword(new String(this.password.getPassword()));
                    this.explorer.getSession().setUser(temp);
                    this.explorer.setTemp(temp);
                    XincoCoreNode xnode = new XincoCoreNode();
                    xnode.setId(1);
                    xnode = this.explorer.getSession().getXinco().getXincoCoreNode(xnode, this.explorer.getSession().getUser());
                    this.explorer.getSession().getXincoClientRepository().assignObject2TreeNode((XincoMutableTreeNode) (this.explorer.getSession().getXincoClientRepository().treemodel).getRoot(),
                            xnode,
this.explorer.getSession().getXinco(), this.explorer.getSession().getUser(),                             2);
                    this.explorer.refreshJTree();
                    this.explorer.markConnectionStatus();
                    this.setVisible(false);
                }
                this.explorer.setLock(false);
            }
        } catch (RemoteException ex) {
        }
        this.password.setText("");
        this.username.setText("");
        this.setVisible(false);
    }//GEN-LAST:event_connectActionPerformed
    
    private void disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectActionPerformed
        this.setVisible(false);
        this.explorer.setLock(false);
        this.explorer.resetTimer();
        this.explorer.getSession().setStatus(0);
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
    private javax.swing.JTextField username;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
    
}
