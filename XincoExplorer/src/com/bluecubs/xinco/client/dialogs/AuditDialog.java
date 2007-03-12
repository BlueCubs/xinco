/*
 * AuditDialog.java
 *
 * Created on February 22, 2007, 9:41 AM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.XincoMutableTreeNode;
import com.bluecubs.xinco.core.XincoCoreAudit;
import com.bluecubs.xinco.core.XincoCoreAuditType;
import com.bluecubs.xinco.core.XincoCoreData;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 *
 * @author  Javier A. Ortiz
 */
public class AuditDialog extends javax.swing.JDialog {
    private XincoExplorer explorer=null;
    private ResourceBundle xerb;
    private Vector types=null;
    private XincoCoreData data=null;
    private XincoCoreAudit xca=null;
    private XincoCoreAuditType xcat=null;
    private XincoMutableTreeNode node=null;
    
    /** Creates new form AuditDialog */
    public AuditDialog(java.awt.Frame parent, boolean modal,XincoExplorer e) {
        super(parent, modal);
        this.explorer=e;
        addMouseListener(this.explorer);
        this.xerb=this.explorer.getResourceBundle();
        node =this.explorer.getSession().currentTreeNodeSelection;
        this.data=((XincoCoreData) node.getUserObject());
        initComponents();
        this.auditCheckbox.setText(xerb.getString("window.archive.audit"));
        this.auditTypeLabel.setText(xerb.getString("window.archive.audit.type"));
        this.auditFlexibilityLabel.setText(xerb.getString("window.archive.audit.flexibility"));
        this.auditDateLabel.setText(xerb.getString("window.archive.audit.date"));
        this.cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        this.ok.setText(explorer.getResourceBundle().getString("general.ok"));
        this.auditTypeList.setEnabled(false);
        this.due_same_day.setText(explorer.getResourceBundle().getString("window.audit.duesameday"));
        this.due_same_week.setText(explorer.getResourceBundle().getString("window.audit.duesameweek"));
        this.due_same_month.setText(explorer.getResourceBundle().getString("window.audit.duesamemonth"));
        this.auditSetting.add(this.due_same_day);
        this.auditSetting.add(this.due_same_week);
        this.auditSetting.add(this.due_same_month);
        this.auditDateLabel.setVisible(false);
        this.auditDate.setVisible(false);
        this.auditDate.setDate(new Date(System.currentTimeMillis()));
        //Inititalize the audit type list
        this.auditTypeList.removeAllItems();
        this.auditTypeList.addItem("");
        try {
            types=this.explorer.getSession().xinco.getXincoCoreAuditTypes(this.explorer.getSession().user);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        for(int i=0;i<types.size();i++){
            this.auditTypeList.addItem(this.explorer.getResourceBundle().getString(((XincoCoreAuditType)types.get(i)).getDescription()));
        }
        if(this.data!=null)
            this.getAuditData();
        setLocationRelativeTo(null);
    }
    
    private void getAuditData(){
        try {
            xca=this.explorer.getSession().xinco.getXincoCoreAudit(this.data,
                    this.explorer.getSession().user,0);
            if(xca==null)
                return;
            xcat=this.explorer.getSession().xinco.getXincoCoreAuditType(this.explorer.getSession().user,
                    xca.getSchedule_type_id());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        this.enableFileAudit.setSelected(true);
        this.auditTypeList.setSelectedIndex(xca.getSchedule_type_id());
        this.auditDate.setDate(xca.getScheduled_date());
        this.due_same_day.setSelected(xcat.isDue_same_day());
        this.due_same_week.setSelected(xcat.isDue_same_week());
        this.due_same_month.setSelected(xcat.isDue_same_month());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        auditSetting = new javax.swing.ButtonGroup();
        auditCheckbox = new javax.swing.JLabel();
        auditTypeLabel = new javax.swing.JLabel();
        auditFlexibilityLabel = new javax.swing.JLabel();
        auditDateLabel = new javax.swing.JLabel();
        auditDate = new com.toedter.calendar.JDateChooser();
        auditTypeList = new javax.swing.JComboBox();
        enableFileAudit = new javax.swing.JCheckBox();
        ok = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        due_same_day = new javax.swing.JRadioButton();
        due_same_week = new javax.swing.JRadioButton();
        due_same_month = new javax.swing.JRadioButton();

        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusCycleRoot(false);
        setModal(true);
        auditCheckbox.setText("jLabel1");

        auditTypeLabel.setText("jLabel1");

        auditFlexibilityLabel.setText("jLabel1");

        auditDateLabel.setText("jLabel1");

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

        due_same_day.setSelected(true);
        due_same_day.setText("jRadioButton1");
        due_same_day.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        due_same_day.setEnabled(false);
        due_same_day.setMargin(new java.awt.Insets(0, 0, 0, 0));

        due_same_week.setText("jRadioButton2");
        due_same_week.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        due_same_week.setEnabled(false);
        due_same_week.setMargin(new java.awt.Insets(0, 0, 0, 0));

        due_same_month.setText("jRadioButton3");
        due_same_month.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        due_same_month.setEnabled(false);
        due_same_month.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(auditDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(auditCheckbox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                                    .addComponent(auditTypeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(enableFileAudit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(auditTypeList, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(due_same_day)
                                        .addComponent(due_same_week)
                                        .addComponent(due_same_month)
                                        .addComponent(auditDate, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(ok)
                        .addGap(69, 69, 69)
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88))
                    .addComponent(auditFlexibilityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {auditCheckbox, auditDateLabel, auditFlexibilityLabel, auditTypeLabel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(enableFileAudit, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(auditCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(auditTypeLabel)
                    .addComponent(auditTypeList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(auditDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(auditDateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(due_same_day)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(due_same_week)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(due_same_month))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(auditFlexibilityLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ok)
                            .addComponent(cancel))))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed
    
    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        if(this.xca==null) {
            System.out.println("Creating XincoCoreAudit");
            this.xca=new XincoCoreAudit(0,this.data.getId(),this.auditTypeList.getSelectedIndex()+1,
                    this.auditDate.getDate(),new Date(System.currentTimeMillis()),-1);
            try {
                this.explorer.getSession().xinco.setXincoCoreAudit(this.xca,
                        this.explorer.getSession().user);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                explorer.set_global_dialog_return_value(2);
            }
            //Add audit attributes
            int start = ((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().size();
            //Audit Type
            Calendar cal = Calendar.getInstance();
            Vector auditTypes=null;
            try {
                auditTypes = explorer.getSession().xinco.getXincoCoreAuditTypes(explorer.getSession().user);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                explorer.set_global_dialog_return_value(2);
            }
            ((XincoCoreData)explorer.getSession().currentTreeNodeSelection.
                    getUserObject()).getXinco_add_attributes().addElement(new XincoAddAttribute());
            ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.
                    getUserObject()).getXinco_add_attributes().elementAt(start++)).
                    setAttrib_varchar(((XincoCoreAuditType)auditTypes.elementAt(this.auditTypeList.getSelectedIndex())).getDescription());
            
            //Audit Next Scheduled Date
            ((XincoCoreData)explorer.getSession().currentTreeNodeSelection.
                    getUserObject()).getXinco_add_attributes().addElement(new XincoAddAttribute());
            cal.setTime(this.auditDate.getDate());
            System.out.println("Before: "+this.auditDate.getDate().toString()+", After: "+cal.getTime().toString());
            ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.
                    getUserObject()).getXinco_add_attributes().elementAt(start++)).setAttrib_datetime(cal);
        }
        else
            System.out.println("XincoCoreAudit not created");
        explorer.set_global_dialog_return_value(1);
        this.setVisible(false);
    }//GEN-LAST:event_okActionPerformed
    
    private void enableFileAuditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableFileAuditActionPerformed
        this.auditTypeList.setEnabled(this.enableFileAudit.isSelected());
        this.auditDateLabel.setVisible(this.enableFileAudit.isSelected());
        this.due_same_day.setEnabled(this.enableFileAudit.isSelected());
        this.due_same_week.setEnabled(this.enableFileAudit.isSelected());
        this.due_same_month.setEnabled(this.enableFileAudit.isSelected());
        this.auditDate.setVisible(this.enableFileAudit.isSelected());
    }//GEN-LAST:event_enableFileAuditActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel auditCheckbox;
    private com.toedter.calendar.JDateChooser auditDate;
    private javax.swing.JLabel auditDateLabel;
    private javax.swing.JLabel auditFlexibilityLabel;
    private javax.swing.ButtonGroup auditSetting;
    private javax.swing.JLabel auditTypeLabel;
    private javax.swing.JComboBox auditTypeList;
    private javax.swing.JButton cancel;
    private javax.swing.JRadioButton due_same_day;
    private javax.swing.JRadioButton due_same_month;
    private javax.swing.JRadioButton due_same_week;
    private javax.swing.JCheckBox enableFileAudit;
    private javax.swing.JButton ok;
    // End of variables declaration//GEN-END:variables
    
}
