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
 * Name:            ACLDialog
 *
 * Description:     ACL Dialog
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
 * ACLDialog.java
 *
 * Created on October 31, 2006, 8:21 AM
 */
package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.client.service.XincoCoreACE;
import com.bluecubs.xinco.client.service.XincoCoreData;
import com.bluecubs.xinco.client.service.XincoCoreGroup;
import com.bluecubs.xinco.client.service.XincoCoreNode;
import com.bluecubs.xinco.core.XincoException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

/**
 * ACL Dialog
 * @author Javier A. Ortiz
 */
public class ACLDialog extends AbstractDialog {

    private XincoExplorer explorer = null;
    private ArrayList tempAcl = null;
    private XincoCoreACE tempAce = null;

    /**
     * Creates new form ACLDialog
     * @param parent Parent of this dialog
     * @param modal Is dialog modal?
     * @param explorer XincoExplorer related to this dialog
     */
    public ACLDialog(java.awt.Frame parent, boolean modal,
            XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        this.explorer = explorer;
        setTitle(explorer.getResourceBundle().getString("window.acl"));
        ACLWarning.setText(explorer.getResourceBundle().getString("window.acl.note"));
        AddACE.setText(explorer.getResourceBundle().getString("window.acl.addace"));
        Admin.setText(explorer.getResourceBundle().getString("general.acl.adminpermission"));
        Close.setText(explorer.getResourceBundle().getString("general.close"));
        Execute.setText(explorer.getResourceBundle().getString("general.acl.executepermission"));
        Read.setText(explorer.getResourceBundle().getString("general.acl.readpermission"));
        RemoveACE.setText(explorer.getResourceBundle().getString("window.acl.removeace"));
        Write.setText(explorer.getResourceBundle().getString("general.acl.writepermission"));
        aclAddLabel.setText(explorer.getResourceBundle().getString("window.acl.grouplabel"));
        aclRemoveLabel.setText(explorer.getResourceBundle().getString("window.acl.removeacelabel"));
        setLocationRelativeTo(null);
        //fill group list
        loadACLGroupListACL();
        //fill ACL
        reloadACLListACL();
    }

    @Override
    public void setToDefaults() {
        super.setToDefaults();
        //fill group list
        loadACLGroupListACL();
        //fill ACL
        reloadACLListACL();
    }

    /**
     * Loads the ACL group list
     */
    protected final void loadACLGroupListACL() {
        String[] list =
                new String[explorer.getSession().getServerGroups().size()];
        for (int i = 0; i
                < explorer.getSession().getServerGroups().size(); i++) {
            list[i] = ((XincoCoreGroup) explorer.getSession().getServerGroups().get(i)).getDesignation();
            try {
                list[i] = explorer.getResourceBundle().getString(list[i]);
            } catch (java.util.MissingResourceException e) {
                //Nothing to translate
            }
        }
        setACLGroupModel(list);
    }

    /**
     * Reloads ACL list
     */
    public final void reloadACLListACL() {
        int i = 0, j = 0;
        StringBuffer temp_string = new StringBuffer();
        ArrayList temp_ArrayList = new ArrayList();

        if (explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
            temp_ArrayList.addAll(((XincoCoreNode) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXincoCoreAcl());
        }
        if (this.explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreData.class) {
            temp_ArrayList.addAll(((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXincoCoreAcl());
        }
        if (tempAcl != null) {
            temp_ArrayList = tempAcl;
        }
        String[] list = new String[temp_ArrayList.size()];
        for (i = 0; i < temp_ArrayList.size(); i++) {
            temp_string.setLength(0);
            tempAce = (XincoCoreACE) temp_ArrayList.get(i);
            if (tempAce.getXincoCoreUserId() > 0) {
                temp_string.append(explorer.getResourceBundle().getString("general.user")).append(": ").append(explorer.getResourceBundle().getString("general.id")).append("=").append(tempAce.getXincoCoreUserId());
            }
            if (tempAce.getXincoCoreGroupId() > 0) {
                for (j = 0; j
                        < this.explorer.getSession().getServerGroups().size();
                        j++) {
                    if (((XincoCoreGroup) explorer.getSession().getServerGroups().get(j)).getId() == tempAce.getXincoCoreGroupId()) {
                        temp_string.append(explorer.getResourceBundle().getString("general.group")).append(
                                ": ");
                        String group = ((XincoCoreGroup) explorer.getSession().getServerGroups().get(j)).getDesignation();
                        try {
                            group = explorer.getResourceBundle().getString(group);
                        } catch (java.util.MissingResourceException e) {
                            //Nothing to translate
                        }
                        temp_string.append(group);
                        break;
                    }
                }
            }
            temp_string.append(" [");
            if (tempAce.isReadPermission()) {
                temp_string.append("R");
            } else {
                temp_string.append("-");
            }
            if (tempAce.isWritePermission()) {
                temp_string.append("W");
            } else {
                temp_string.append("-");
            }
            if (tempAce.isExecutePermission()) {
                temp_string.append("X");
            } else {
                temp_string.append("-");
            }
            if (tempAce.isAdminPermission()) {
                temp_string.append("A");
            } else {
                temp_string.append("-");
            }
            temp_string.append("]");
            list[i] = temp_string.toString();
        }
        setACLListModel(list);
    }

    /**
     * Sets ACL group model.
     * @param list String array containing the list.
     */
    public void setACLGroupModel(final String[] list) {
        groupList.setModel(new javax.swing.AbstractListModel() {

            String[] strings = list;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }

    /**
     * Sets ACL list model.
     * @param list String array containing the list.
     */
    protected void setACLListModel(final String[] list) {
        currentACLList.setModel(new javax.swing.AbstractListModel() {

            String[] strings = list;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }

    /**
     * Get ACL group model.
     * @return Group list model.
     */
    public ListModel getACLGroupModel() {
        return this.groupList.getModel();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aclAddLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        groupList = new javax.swing.JList();
        Read = new javax.swing.JCheckBox();
        Execute = new javax.swing.JCheckBox();
        Write = new javax.swing.JCheckBox();
        Admin = new javax.swing.JCheckBox();
        AddACE = new javax.swing.JButton();
        aclRemoveLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        currentACLList = new javax.swing.JList();
        RemoveACE = new javax.swing.JButton();
        ACLWarning = new javax.swing.JLabel();
        Close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        aclAddLabel.setText("jLabel1");

        groupList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(groupList);

        Read.setText("jCheckBox1");
        Read.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Read.setMargin(new java.awt.Insets(0, 0, 0, 0));

        Execute.setText("jCheckBox1");
        Execute.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Execute.setMargin(new java.awt.Insets(0, 0, 0, 0));

        Write.setText("jCheckBox1");
        Write.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Write.setMargin(new java.awt.Insets(0, 0, 0, 0));

        Admin.setText("jCheckBox1");
        Admin.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Admin.setMargin(new java.awt.Insets(0, 0, 0, 0));

        AddACE.setText("jButton1");
        AddACE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddACEActionPerformed(evt);
            }
        });

        aclRemoveLabel.setText("jLabel1");

        currentACLList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(currentACLList);

        RemoveACE.setText("jButton1");
        RemoveACE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveACEActionPerformed(evt);
            }
        });

        ACLWarning.setText("jLabel1");

        Close.setText("jButton1");
        Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(ACLWarning, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(Admin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(125, 125, 125)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(RemoveACE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .add(Close, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                        .add(120, 120, 120))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(aclRemoveLabel))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(123, 123, 123)
                        .add(AddACE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .add(122, 122, 122))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(Write, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(aclAddLabel)
                                .add(Read, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(Execute, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(aclAddLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Read)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Execute)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Write)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Admin)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(aclRemoveLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(AddACE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(22, 22, 22)))
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(RemoveACE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(ACLWarning, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Close))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_CloseActionPerformed

    private void RemoveACEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveACEActionPerformed
        if (this.currentACLList.getSelectedIndex() >= 0) {
            try {
                tempAce = new XincoCoreACE();
                if (this.explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
                    tempAcl.addAll(((XincoCoreNode) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXincoCoreAcl());
                    tempAce = (XincoCoreACE) tempAcl.get(this.currentACLList.getSelectedIndex());
                }
                if (this.explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreData.class) {
                    tempAcl.addAll(((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXincoCoreAcl());
                    tempAce = (XincoCoreACE) tempAcl.get(this.currentACLList.getSelectedIndex());
                }
                if (tempAce.getXincoCoreUserId() > 0) {
                    throw new XincoException(explorer.getResourceBundle().getString("window.acl.cannotremoveowner"));
                }
                if (!this.explorer.getSession().getXinco().removeXincoCoreACE(tempAce, explorer.getSession().getUser())) {
                    throw new XincoException(explorer.getResourceBundle().getString("error.noadminpermission"));
                }
                //remove ACE from ACL and reload
                tempAcl.get(currentACLList.getSelectedIndex());
                reloadACLListACL();
            } catch (Exception xe) {
                JOptionPane.showMessageDialog(this, this.explorer.getResourceBundle().getString("window.acl.removefailed")
                        + " " + this.explorer.getResourceBundle().getString("general.reason")
                        + ": " + xe.toString(), this.explorer.getResourceBundle().getString("general.error"),
                        JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(ACLDialog.class.getSimpleName()).log(Level.SEVERE, null, xe);
            }
        }
    }//GEN-LAST:event_RemoveACEActionPerformed

    private void AddACEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddACEActionPerformed
        int i = 0;
        tempAcl = new ArrayList();
        if (this.groupList.getSelectedIndex() >= 0) {
            try {
                if (this.explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
                    tempAcl.addAll(((XincoCoreNode) this.explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXincoCoreAcl());
                }
                if (this.explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreData.class) {
                    tempAcl.addAll(((XincoCoreData) this.explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXincoCoreAcl());
                }
                //check if an ACE already exists for selected group
                for (i = 0; i < tempAcl.size(); i++) {
                    if (((XincoCoreACE) tempAcl.get(i)).getXincoCoreGroupId() == ((XincoCoreGroup) this.explorer.getSession().getServerGroups().get(this.groupList.getSelectedIndex())).getId()) {
                        throw new XincoException(this.explorer.getResourceBundle().getString("window.acl.groupexists"));
                    }
                }
                //create new ACE
                XincoCoreACE newace = new XincoCoreACE();
                newace.setXincoCoreGroupId(((XincoCoreGroup) explorer.getSession().getServerGroups().get(this.groupList.getSelectedIndex())).getId());
                if (this.explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
                    newace.setXincoCoreNodeId(((XincoCoreNode) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getId());
                }
                if (this.explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreData.class) {
                    newace.setXincoCoreDataId(((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getId());
                }
                newace.setReadPermission(this.Read.isSelected());
                newace.setWritePermission(this.Write.isSelected());
                newace.setExecutePermission(this.Execute.isSelected());
                newace.setAdminPermission(this.Admin.isSelected());
                if ((newace = this.explorer.getSession().getXinco().setXincoCoreACE(newace, this.explorer.getSession().getUser())) == null) {
                    throw new XincoException(this.explorer.getResourceBundle().getString("error.noadminpermission"));
                }
                //add ACE to ACL and reload
                tempAcl.add(newace);
                reloadACLListACL();
            } catch (Exception xe) {
                JOptionPane.showMessageDialog(this, this.explorer.getResourceBundle().getString("window.acl.addacefailed")
                        + " " + this.explorer.getResourceBundle().getString("general.reason")
                        + ": " + xe.toString(), this.explorer.getResourceBundle().getString("general.error"), JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_AddACEActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ACLWarning;
    private javax.swing.JButton AddACE;
    private javax.swing.JCheckBox Admin;
    private javax.swing.JButton Close;
    private javax.swing.JCheckBox Execute;
    private javax.swing.JCheckBox Read;
    private javax.swing.JButton RemoveACE;
    private javax.swing.JCheckBox Write;
    private javax.swing.JLabel aclAddLabel;
    private javax.swing.JLabel aclRemoveLabel;
    private javax.swing.JList currentACLList;
    private javax.swing.JList groupList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
