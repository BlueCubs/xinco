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
 * Name:            ConnectionDialog
 *
 * Description:     Connection Dialog
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
 * ConnectionDialog.java
 *
 * Created on September 25, 2006, 3:59 PM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.*;
import java.util.Vector;
import javax.swing.DefaultListModel; 
import javax.swing.JList;

/**
 * Connection Dialog
 * @author Javier A. Ortiz
 */
public class ConnectionDialog extends javax.swing.JDialog {
    private XincoExplorer explorer=null;
    /**
     * Creates new form ConnectionDialog
     * @param parent Dialog's parent
     * @param modal Is modal?
     * @param explorer Related XincoExplorer
     */
    public ConnectionDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        this.explorer=explorer;
        initialize();
        setLocationRelativeTo(null);
    }
    
    /**
     * Initialize the dialog.
     */
    protected void initialize(){
        setTitle(explorer.getResourceBundle().getString("window.connection") + ":");
        setName("DialogConnection");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        this.profileLabel.setText(explorer.getResourceBundle().getString("window.connection.profilename") + ":");
        this.connect.setText(explorer.getResourceBundle().getString("window.connection.connect"));
        this.Create.setText(explorer.getResourceBundle().getString("general.create"));
        this.Cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        this.deleteProfile.setText(explorer.getResourceBundle().getString("window.connection.deleteprofile"));
        this.profileNameLabel.setText(explorer.getResourceBundle().getString("window.connection.profile"));
        this.endpointLabel.setText(explorer.getResourceBundle().getString("window.connection.serverendpoint"));
        this.usernameLabel.setText(explorer.getResourceBundle().getString("general.username"));
        this.passwordLabel.setText(explorer.getResourceBundle().getString("general.password"));
        this.savePWLabel.setText(explorer.getResourceBundle().getString("window.connection.savepassword"));
        DefaultListModel dlm = new DefaultListModel();
        this.profileList.setModel(dlm);
        this.profileList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        this.profileList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                int sel;
                sel = profileList.getSelectedIndex();
                if (sel >= 0) {
                    profileName.setText(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).profile_name);
                    endpoint.setText(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).service_endpoint);
                    username.setText(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).username);
                    password.setText(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).password);
                    savePW.setSelected(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).save_password);
                }
            }
        });
        dlm = (DefaultListModel)this.profileList.getModel();
        for (int i=0;i<((Vector)explorer.getConfig().elementAt(0)).size();i++) {
            dlm.addElement(new String(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(i)).toString()));
        }
    }
    
    /**
     * Get profile list.
     * @return Profile list.
     */
    public JList getProfileList(){
        return this.profileList;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        profileLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        profileList = new javax.swing.JList();
        Create = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();
        profileNameLabel = new javax.swing.JLabel();
        profileName = new javax.swing.JTextField();
        endpointLabel = new javax.swing.JLabel();
        endpoint = new javax.swing.JTextField();
        usernameLabel = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        savePWLabel = new javax.swing.JLabel();
        savePW = new javax.swing.JCheckBox();
        connect = new javax.swing.JButton();
        deleteProfile = new javax.swing.JButton();
        password = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("title");
        setAlwaysOnTop(true);
        setModal(true);
        profileLabel.setText("Perfil");

        jScrollPane1.setViewportView(profileList);

        Create.setText("Create");
        Create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateActionPerformed(evt);
            }
        });

        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        profileNameLabel.setText("Profile name:");

        endpointLabel.setText("Endpoint:");

        usernameLabel.setText("Username:");

        passwordLabel.setText("Password:");

        savePWLabel.setText("Save Pasword?");

        savePW.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        savePW.setMargin(new java.awt.Insets(0, 0, 0, 0));

        connect.setText("Connect");
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });

        deleteProfile.setText("Delete");
        deleteProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProfileActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(profileLabel)
                        .add(41, 41, 41)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(Create, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 83, Short.MAX_VALUE)
                                .add(deleteProfile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(usernameLabel)
                            .add(savePWLabel)
                            .add(passwordLabel)
                            .add(endpointLabel)
                            .add(profileNameLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(savePW)
                            .add(password, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .add(username, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .add(endpoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .add(profileName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(24, 24, 24)
                        .add(connect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(72, 72, 72)
                        .add(Cancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(40, 40, 40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(profileLabel)
                    .add(jScrollPane1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(deleteProfile)
                    .add(Create))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(profileNameLabel)
                    .add(profileName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(endpointLabel)
                    .add(endpoint))
                .add(9, 9, 9)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(usernameLabel)
                    .add(username, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .add(9, 9, 9)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordLabel)
                    .add(password, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(savePW)
                    .add(savePWLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(38, 38, 38)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(connect, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(Cancel))
                .add(38, 38, 38))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_CancelActionPerformed
    
    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        //save session info
        explorer.getSession().service_endpoint = this.endpoint.getText();
        explorer.getSession().user.setUsername(this.username.getText());
        explorer.getSession().user.setUserpassword(new String(this.password.getPassword()));
        explorer.getSession().status = 1;
        //update profile
        if (this.profileList.getSelectedIndex() >= 0) {
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).profile_name = this.profileName.getText();
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).service_endpoint = this.endpoint.getText();
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).username = this.username.getText();
            if (this.savePW.isSelected()) {
                ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).password = new String(this.password.getPassword());
            } else {
                ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).password = "";
            }
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).save_password = this.savePW.isSelected();
        }
        //save profiles
        explorer.saveConfig();
        setVisible(false);
    }//GEN-LAST:event_connectActionPerformed
    
    private void deleteProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProfileActionPerformed
        int sel;
        DefaultListModel dlm = (DefaultListModel)this.profileList.getModel();
        sel = this.profileList.getSelectedIndex();
        if (sel >= 0) {
            ((Vector)explorer.getConfig().elementAt(0)).removeElementAt(sel);
            dlm.removeElementAt(sel);
        }
    }//GEN-LAST:event_deleteProfileActionPerformed
    
    private void CreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateActionPerformed
        
        DefaultListModel dlm = (DefaultListModel)this.profileList.getModel();
        //update profile
        if (this.profileList.getSelectedIndex() >= 0) {
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).profile_name = this.profileName.getText();
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).service_endpoint = this.endpoint.getText();
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).username = this.username.getText();
            if (this.savePW.isSelected()) {
                ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).password = new String(this.password.getPassword());
            } else {
                ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).password = "";
            }
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(this.profileList.getSelectedIndex())).save_password = this.savePW.isSelected();
            dlm.setElementAt(new String(this.profileName.getText()), this.profileList.getSelectedIndex());
        }
        XincoClientConnectionProfile ccp = new XincoClientConnectionProfile();
        ccp.profile_name = explorer.getResourceBundle().getString("window.connection.newprofile");
        ((Vector)explorer.getConfig().elementAt(0)).add(ccp);
        dlm.addElement(new String(ccp.toString()));
        this.profileList.setSelectedIndex(((Vector)explorer.getConfig().elementAt(0)).size()-1);
    }//GEN-LAST:event_CreateActionPerformed
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JButton Create;
    private javax.swing.JButton connect;
    private javax.swing.JButton deleteProfile;
    private javax.swing.JTextField endpoint;
    private javax.swing.JLabel endpointLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel profileLabel;
    private javax.swing.JList profileList;
    private javax.swing.JTextField profileName;
    private javax.swing.JLabel profileNameLabel;
    private javax.swing.JCheckBox savePW;
    private javax.swing.JLabel savePWLabel;
    private javax.swing.JTextField username;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
    
}
