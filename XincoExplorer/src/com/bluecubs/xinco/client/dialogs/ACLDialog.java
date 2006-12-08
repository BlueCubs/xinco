/*
 * ACLDialog.java
 *
 * Created on October 31, 2006, 8:21 AM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreACE;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

/**
 * ACL Dialog
 * @author ortizbj
 */
public class ACLDialog extends javax.swing.JDialog {
    private XincoExplorer explorer=null;
    /**
     * Boolean value that checks if the dialog is done.
     */
    public boolean done=false;
    private Vector temp_acl = null;
    private XincoCoreACE temp_ace=null;
    /**
     * Creates new form ACLDialog
     * @param parent Parent of this dialog
     * @param modal Is dialog modal?
     * @param explorer XincoExplorer related to this dialog
     */
    public ACLDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        this.explorer=explorer;
        setTitle(explorer.getResourceBundle().getString("window.acl"));
        this.ACLWarning.setText(explorer.getResourceBundle().getString("window.acl.note"));
        this.AddACE.setText(explorer.getResourceBundle().getString("window.acl.addace"));
        this.Admin.setText(explorer.getResourceBundle().getString("general.acl.adminpermission"));
        this.Close.setText(explorer.getResourceBundle().getString("general.close"));
        this.Execute.setText(explorer.getResourceBundle().getString("general.acl.executepermission"));
        this.Read.setText(explorer.getResourceBundle().getString("general.acl.readpermission"));
        this.RemoveACE.setText(explorer.getResourceBundle().getString("window.acl.removeace"));
        this.Write.setText(explorer.getResourceBundle().getString("general.acl.writepermission"));
        this.aclAddLabel.setText(explorer.getResourceBundle().getString("window.acl.grouplabel"));
        this.aclRemoveLabel.setText(explorer.getResourceBundle().getString("window.acl.removeacelabel"));
        setLocationRelativeTo(null);
        //fill group list
        loadACLGroupListACL();
        //fill ACL
        reloadACLListACL();
    }
    
    /**
     * Loads the ACL group list
     */
    public void loadACLGroupListACL() {
        String[] list = new String[this.explorer.getSession().server_groups.size()];
        for (int i=0;i<this.explorer.getSession().server_groups.size();i++) {
            list[i]=new String(((XincoCoreGroup)this.explorer.getSession().server_groups.elementAt(i)).getDesignation());
        }
        setACLGroupModel(list);
    }
    
    /**
     * Reloads ACL list
     */
    public void reloadACLListACL() {
        int i = 0, j = 0;
        String temp_string = "";
        Vector temp_vector = new Vector();
        
        if (this.explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
            temp_vector = ((XincoCoreNode)this.explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_acl();
        }
        if (this.explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
            temp_vector = ((XincoCoreData)this.explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_acl();
        }
        if(temp_acl!=null){
            temp_vector=temp_acl;
        }
        String[] list = new String[temp_vector.size()];
        for (i=0;i<temp_vector.size();i++) {
            temp_ace = (XincoCoreACE)temp_vector.elementAt(i);
            if (temp_ace.getXinco_core_user_id() > 0) {
                temp_string = this.explorer.getResourceBundle().getString("general.user") +
                        ": " + this.explorer.getResourceBundle().getString("general.id") +
                        "=" + temp_ace.getXinco_core_user_id();
            }
            if (temp_ace.getXinco_core_group_id() > 0) {
                for (j=0;j<this.explorer.getSession().server_groups.size();j++) {
                    if (((XincoCoreGroup)this.explorer.getSession().server_groups.elementAt(j)).getId() == temp_ace.getXinco_core_group_id()) {
                        temp_string = this.explorer.getResourceBundle().getString("general.group") +
                                ": " + ((XincoCoreGroup)this.explorer.getSession().server_groups.elementAt(j)).getDesignation();
                        break;
                    }
                }
            }
            temp_string = temp_string + " [";
            if (temp_ace.isRead_permission()) {
                temp_string = temp_string + "R";
            } else {
                temp_string = temp_string + "-";
            }
            if (temp_ace.isWrite_permission()) {
                temp_string = temp_string + "W";
            } else {
                temp_string = temp_string + "-";
            }
            if (temp_ace.isExecute_permission()) {
                temp_string = temp_string + "X";
            } else {
                temp_string = temp_string + "-";
            }
            if (temp_ace.isAdmin_permission()) {
                temp_string = temp_string + "A";
            } else {
                temp_string = temp_string + "-";
            }
            temp_string = temp_string + "]";
            list[i]=new String(temp_string);
        }
        setACLListModel(list);
    }
    
    /**
     * Sets ACL group model.
     * @param list String array containing the list.
     */
    public void setACLGroupModel(final String [] list){
        groupList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = list;
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
    }
    
    /**
     * Sets ACL list model.
     * @param list String array containing the list.
     */
    public void setACLListModel(final String [] list){
        currentACLList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = list;
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
    }
    
    /**
     * Get ACL group model.
     * @return Group list model.
     */
    public ListModel getACLGroupModel(){
        return this.groupList.getModel();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
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

        groupList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "test" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
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
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(Read, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(aclAddLabel)
                            .add(Execute, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(177, 177, 177)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(Admin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(Write, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(67, 67, 67))))
            .add(layout.createSequentialGroup()
                .add(158, 158, 158)
                .add(AddACE)
                .addContainerGap(167, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(aclRemoveLabel)
                .addContainerGap(356, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(ACLWarning)
                .addContainerGap(356, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(163, 163, 163)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(Close, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(162, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(RemoveACE)
                        .addContainerGap(162, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(aclAddLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(Read)
                    .add(Write))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(Execute)
                    .add(Admin))
                .add(23, 23, 23)
                .add(AddACE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(aclRemoveLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(RemoveACE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ACLWarning)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 28, Short.MAX_VALUE)
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
                temp_ace = new XincoCoreACE();
                if (this.explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                    temp_acl = ((XincoCoreNode)this.explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_acl();
                    temp_ace = (XincoCoreACE)temp_acl.elementAt(this.currentACLList.getSelectedIndex());
                }
                if (this.explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                    temp_acl = ((XincoCoreData)this.explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_acl();
                    temp_ace = (XincoCoreACE)temp_acl.elementAt(this.currentACLList.getSelectedIndex());
                }
                if (temp_ace.getXinco_core_user_id() > 0) {
                    throw new XincoException(this.explorer.getResourceBundle().getString("window.acl.cannotremoveowner"));
                }
                if (!this.explorer.getSession().xinco.removeXincoCoreACE(temp_ace, this.explorer.getSession().user)) {
                    throw new XincoException(this.explorer.getResourceBundle().getString("error.noadminpermission"));
                }
                //remove ACE from ACL and reload
                temp_acl.removeElementAt(this.currentACLList.getSelectedIndex());
                reloadACLListACL();
            } catch (Exception xe) {
                JOptionPane.showMessageDialog(this, this.explorer.getResourceBundle().getString("window.acl.removefailed") +
                        " " + this.explorer.getResourceBundle().getString("general.reason") +
                        ": " + xe.toString(), this.explorer.getResourceBundle().getString("general.error"),
                        JOptionPane.WARNING_MESSAGE);
                xe.printStackTrace();
            }
        }
    }//GEN-LAST:event_RemoveACEActionPerformed
    
    private void AddACEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddACEActionPerformed
        int i = 0;
        temp_acl = new Vector();
        if (this.groupList.getSelectedIndex() >= 0) {
            try {
                if (this.explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                    temp_acl = ((XincoCoreNode)this.explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_acl();
                }
                if (this.explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                    temp_acl = ((XincoCoreData)this.explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_acl();
                }
                //check if an ACE already exists for selected group
                for (i=0;i<temp_acl.size();i++) {
                    if (((XincoCoreACE)temp_acl.elementAt(i)).getXinco_core_group_id() == ((XincoCoreGroup)this.explorer.getSession().server_groups.elementAt(this.groupList.getSelectedIndex())).getId()) {
                        throw new XincoException(this.explorer.getResourceBundle().getString("window.acl.groupexists"));
                    }
                }
                //create new ACE
                XincoCoreACE newace = new XincoCoreACE();
                newace.setXinco_core_group_id(((XincoCoreGroup)this.explorer.getSession().server_groups.elementAt(this.groupList.getSelectedIndex())).getId());
                if (this.explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                    newace.setXinco_core_node_id(((XincoCoreNode)this.explorer.getSession().currentTreeNodeSelection.getUserObject()).getId());
                }
                if (this.explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                    newace.setXinco_core_data_id(((XincoCoreData)this.explorer.getSession().currentTreeNodeSelection.getUserObject()).getId());
                }
                newace.setRead_permission(this.Read.isSelected());
                newace.setWrite_permission(this.Write.isSelected());
                newace.setExecute_permission(this.Execute.isSelected());
                newace.setAdmin_permission(this.Admin.isSelected());
                if ((newace = this.explorer.getSession().xinco.setXincoCoreACE(newace, this.explorer.getSession().user)) == null) {
                    throw new XincoException(this.explorer.getResourceBundle().getString("error.noadminpermission"));
                }
                //add ACE to ACL and reload
                temp_acl.add(newace);
                reloadACLListACL();
            } catch (Exception xe) {
                JOptionPane.showMessageDialog(this, this.explorer.getResourceBundle().getString("window.acl.addacefailed") +
                        " " + this.explorer.getResourceBundle().getString("general.reason") +
                        ": " + xe.toString(), this.explorer.getResourceBundle().getString("general.error"), JOptionPane.WARNING_MESSAGE);
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
