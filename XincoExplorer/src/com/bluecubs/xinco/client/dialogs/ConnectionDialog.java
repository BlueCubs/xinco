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
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * Connection Dialog
 * @author ortizbj
 */
public class ConnectionDialog extends javax.swing.JDialog {
    private XincoExplorer explorer=null;
    private int sel,finalSelection;
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
        this.savePasswordLabel.setText(explorer.getResourceBundle().getString("window.connection.savepassword"));
        DefaultListModel dlm = new DefaultListModel();
        this.profileList.setModel(dlm);
        this.profileList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        dlm = (DefaultListModel)this.profileList.getModel();
        for (int i=0;i<((Vector)explorer.getConfig().elementAt(0)).size();i++) {
            dlm.addElement(new String(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(i)).toString()));
        }
        getRootPane().setDefaultButton(connect);
    }
    
    public void updateProfile(){
        //update profile
        if(finalSelection >=0){
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(finalSelection)).profile_name = this.profileName.getText();
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(finalSelection)).service_endpoint = this.endpoint.getText();
            ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(finalSelection)).username = this.username.getText();
            if(!this.explorer.getSettings()[8].isBool_value()){
                ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(finalSelection)).password = "";
                ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(finalSelection)).save_password=false;
            }else {
                ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(finalSelection)).password = new String(this.password.getPassword());
                ((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(finalSelection)).save_password=this.savePassword.isSelected();
            }
        }
        //save profiles
        explorer.saveConfig();
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
        connect = new javax.swing.JButton();
        deleteProfile = new javax.swing.JButton();
        password = new javax.swing.JPasswordField();
        savePasswordLabel = new javax.swing.JLabel();
        savePassword = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("title");
        setAlwaysOnTop(true);
        setModal(true);
        profileLabel.setText("Perfil");

        profileList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                profileListValueChanged(evt);
            }
        });

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

        savePasswordLabel.setText("jLabel1");

        savePassword.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        savePassword.setMargin(new java.awt.Insets(0, 0, 0, 0));

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
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 91, Short.MAX_VALUE)
                                .add(deleteProfile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(usernameLabel)
                            .add(endpointLabel)
                            .add(profileNameLabel)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(savePasswordLabel)
                                .add(passwordLabel)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(savePassword)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                    .add(connect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 79, Short.MAX_VALUE)
                                    .add(Cancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(password, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                                .add(username, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                                .add(endpoint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                                .add(profileName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)))))
                .add(57, 57, 57))
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(profileNameLabel)
                    .add(profileName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(endpointLabel)
                    .add(endpoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(9, 9, 9)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(usernameLabel)
                    .add(username, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(9, 9, 9)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(passwordLabel)
                    .add(password, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(savePasswordLabel)
                    .add(savePassword))
                .add(38, 38, 38)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(Cancel)
                    .add(connect))
                .add(38, 38, 38))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void profileListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_profileListValueChanged
        sel = profileList.getSelectedIndex();
        if (sel >= 0) {
            profileName.setText(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).profile_name);
            endpoint.setText(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).service_endpoint);
            username.setText(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).username);
            if(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).save_password)
               password.setText(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).password);
            savePassword.setSelected(((XincoClientConnectionProfile)((Vector)explorer.getConfig().elementAt(0)).elementAt(sel)).save_password);
            finalSelection =sel;
        }
    }//GEN-LAST:event_profileListValueChanged
    
    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_CancelActionPerformed
    
    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        //save session info
        explorer.getSession().service_endpoint = this.endpoint.getText();
        explorer.getSession().user.setUsername(this.username.getText());
        explorer.getSession().user.setUserpassword(new String(this.password.getPassword()));
        explorer.getSession().status = 1;
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
    private javax.swing.JCheckBox savePassword;
    private javax.swing.JLabel savePasswordLabel;
    private javax.swing.JTextField username;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
    
}