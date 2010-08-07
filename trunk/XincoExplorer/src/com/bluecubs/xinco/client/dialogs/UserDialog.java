/**
 *Copyright 2010 blueCubs.com
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
 * Name:            UserDialog
 *
 * Description:     User Dialog
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
 * UserDialog.java
 *
 * Created on September 25, 2006, 6:16 PM
 */
package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.core.XincoException;
import javax.swing.JOptionPane;

/**
 * User Dialog
 * @author Javier A. Ortiz
 */
public class UserDialog extends AbstractDialog {

    private XincoExplorer explorer = null;
    private boolean isAged = false;

    /**
     * Creates new form UserDialog
     * @param parent Dialog's parent.
     * @param modal Is modal?
     * @param explorer Related XincoExplorer
     * @param aged Boolean: Is the user's password aged out?
     */
    public UserDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer, boolean aged) {
        super(parent, modal);
        this.explorer = explorer;
        isAged = aged;
        initComponents();
        initialize();
        setLocationRelativeTo(null);
    }

    @Override
    public void setToDefaults() {
        super.setToDefaults();
        //Do not allow to close the window. User MUST change password!
        if (isAged) {
            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            cancel.setEnabled(false);
            name.setEnabled(false);
            lastname.setEnabled(false);
            email.setEnabled(false);
        }
    }

    private void initialize() {
        String text = null;
        setTitle(explorer.getResourceBundle().getString("window.userinfo"));
        //processing independent of creation
        this.id.setText("" + explorer.getSession().getUser().getId());
        this.idLabel.setText(explorer.getResourceBundle().getString("general.id") + ":");
        this.username.setText("" + explorer.getSession().getUser().getUsername());
        this.usernameLabel.setText(explorer.getResourceBundle().getString("general.username") + ":");
        if (this.isAged) {
            this.password.setText("");
        } else {
            this.password.setText("" + explorer.getSession().getUser().getUserpassword());
        }
        this.passwordLabel.setText(explorer.getResourceBundle().getString("general.password") + ":");
        if (this.isAged) {
            this.verification.setText("");
        } else {
            this.verification.setText("" + explorer.getSession().getUser().getUserpassword());
        }
        this.verificationLabel.setText(explorer.getResourceBundle().getString("general.verifypassword") + ":");
        this.name.setText("" + explorer.getSession().getUser().getFirstname());
        this.nameLabel.setText(explorer.getResourceBundle().getString("window.userinfo.firstname") + ":");
        this.lastname.setText("" + explorer.getSession().getUser().getName());
        this.lastnameLabel.setText(explorer.getResourceBundle().getString("window.userinfo.lastname") + ":");
        this.email.setText("" + explorer.getSession().getUser().getEmail());
        this.emailLabel.setText(explorer.getResourceBundle().getString("window.userinfo.email") + ":");
        if (explorer.getSession().getUser().getStatusNumber() == 1) {
            text = explorer.getResourceBundle().getString("general.status.open") + "";
        }
        if (explorer.getSession().getUser().getStatusNumber() == 2) {
            text = explorer.getResourceBundle().getString("general.status.locked") + " (-)";
        }
        this.status.setText(text);
        this.statusLabel.setText(explorer.getResourceBundle().getString("general.status") + ":");
        this.save.setText(explorer.getResourceBundle().getString("general.save") + "!");
        this.cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        setLocationRelativeTo(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        usernameLabel = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        verificationLabel = new javax.swing.JLabel();
        verification = new javax.swing.JPasswordField();
        nameLabel = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        lastnameLabel = new javax.swing.JLabel();
        lastname = new javax.swing.JTextField();
        emailLabel = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        statusLabel = new javax.swing.JLabel();
        status = new javax.swing.JTextField();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        idLabel.setText("jLabel1");

        id.setEditable(false);

        usernameLabel.setText("jLabel1");

        username.setEditable(false);

        passwordLabel.setText("jLabel1");

        password.setText("jPasswordField1");

        verificationLabel.setText("jLabel1");

        verification.setText("jPasswordField1");

        nameLabel.setText("jLabel2");

        lastnameLabel.setText("jLabel2");

        emailLabel.setText("jLabel2");

        statusLabel.setText("jLabel2");

        status.setEditable(false);

        save.setText("Button1");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        cancel.setText("Button2");
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
                        .add(idLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(id, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(usernameLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(username, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(passwordLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(password, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(verificationLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(verification, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(nameLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(name, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(lastnameLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lastname, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(emailLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(email, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(statusLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(save)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 112, Short.MAX_VALUE)
                                .add(cancel))
                            .add(status, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(idLabel)
                    .add(id, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(usernameLabel)
                    .add(username, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordLabel)
                    .add(password, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(verificationLabel)
                    .add(verification, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameLabel)
                    .add(name, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lastnameLabel)
                    .add(lastname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(emailLabel)
                    .add(email, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusLabel)
                    .add(status, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(save)
                    .add(cancel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        //Can't cancel if the password have aged out
        if (!this.isAged) {
            setVisible(false);
        }
    }//GEN-LAST:event_cancelActionPerformed

    @SuppressWarnings("empty-statement")
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        if ((new String(password.getPassword())).equals(new String(verification.getPassword()))) {
            try {
                if (this.isAged
                        && !this.explorer.getSession().getXinco().checkXincoCoreUserNewPassword(new String(password.getPassword()),
                        this.explorer.getSession().getUser(),
                        null)) {
                    this.explorer.getSession().getXinco().checkXincoCoreUserNewPassword(new String(password.getPassword()), this.explorer.getSession().getUser(),
                            null);
                    throw new XincoException(explorer.getResourceBundle().getString("password.unusable"));
                }
                explorer.getUser().setId(explorer.getSession().getUser().getId());
                explorer.getUser().setUsername(explorer.getSession().getUser().getUsername());
                explorer.getUser().setUserpassword(new String(password.getPassword()));
                explorer.getUser().setFirstname(name.getText());
                explorer.getUser().setName(lastname.getText());
                explorer.getUser().setEmail(email.getText());
                explorer.getUser().getXincoCoreGroups().clear();
                explorer.getUser().getXincoCoreGroups().addAll(explorer.getSession().getUser().getXincoCoreGroups());
                explorer.getUser().setStatusNumber(explorer.getSession().getUser().getStatusNumber());
                explorer.getUser().setChange(true);
                explorer.getUser().setUserpassword(new String(password.getPassword()));
                ChangeReasonDialog crd = null;
                boolean enable;
                if (isAged) {
                    enable = false;
                } else {
                    enable = true;
                }
                this.email.setEnabled(enable);
                this.lastname.setEditable(enable);
                this.name.setEditable(enable);
                this.status.setEditable(enable);
                this.username.setEditable(enable);
                // Prompt for change reason
                if (!isAged) {
                    crd = new ChangeReasonDialog(new javax.swing.JFrame(),
                            true, explorer);
                    crd.setVisible(true);
                    while (!crd.done);
                }
                explorer.getUser().setChangerID(explorer.getUser().getId());
                explorer.getUser().setWriteGroups(true);
                explorer.getUser().setChange(true);
                explorer.getUser().setStatusNumber(1);
                if (isAged) {
                    explorer.getUser().setReason("audit.user.account.aged");
                    explorer.getUser().setStatusNumber(4);
                } else {
                    explorer.getUser().setReason(crd.getReason());
                }
                explorer.getSession().setUser(explorer.getSession().getXinco().setXincoCoreUser(explorer.getUser(), explorer.getSession().getUser()));
                // set plain-text password
                explorer.getSession().getUser().setUserpassword(explorer.getUser().getUserpassword());
                // update transaction info
                explorer.jLabelInternalFrameInformationText.setText(explorer.getResourceBundle().getString("window.userinfo.updatesuccess"));
                setVisible(false);
            } catch (Exception ue) {
                JOptionPane.showMessageDialog(this, explorer.getResourceBundle().getString("window.userinfo.updatefailed") + " " + explorer.getResourceBundle().getString("general.reason") + ": " + ue.toString(), explorer.getResourceBundle().getString("general.error"), JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, explorer.getResourceBundle().getString("window.userinfo.passwordmismatch"), explorer.getResourceBundle().getString("general.error"), JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_saveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JTextField email;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField id;
    private javax.swing.JLabel idLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField lastname;
    private javax.swing.JLabel lastnameLabel;
    private javax.swing.JTextField name;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton save;
    private javax.swing.JTextField status;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField username;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JPasswordField verification;
    private javax.swing.JLabel verificationLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * @param isAged the isAged to set
     */
    public void setIsAged(boolean isAged) {
        this.isAged = isAged;
    }
}
