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
 * Name:            ArchiveDialog
 *
 * Description:     Archive Dialog
 *
 * Original Author: Javier A. Ortiz
 * Date:            2007
 *
 * Modifications:
 *
 * Who?             When?             What?
 * Javier A. Ortiz  02/07/2007        Added jcalendar for date selection and scheduled audit support fields
 *
 *************************************************************
 * ArchiveDialog.java
 *
 * Created on January 5, 2007, 9:17 AM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreData;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author  ortizbj
 */
public class ArchiveDialog extends javax.swing.JDialog {
    private XincoExplorer explorer;
    private ResourceBundle xerb;
    /** Creates new form ArchiveDialog */
    public ArchiveDialog(java.awt.Frame parent, boolean modal, final XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.explorer=explorer;
        this.xerb=this.explorer.getResourceBundle();
        this.revisionModelLabel.setText(xerb.getString("window.archive.revisionmodel") + ":");
        this.revisionModelCheckbox.setSelected(true);
        this.archiveModelLabel.setText(xerb.getString("window.archive.archivingmodel") + ":");
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        this.archiveModelDropDown.setModel(dcbm);
        this.archiveModelDropDown.setEditable(false);
        this.archiveModelCalendar.setVisible(false);
        this.dateLabel.setText(xerb.getString("window.archive.archivedate") + ":");
        this.auditDateLabel.setText(xerb.getString("window.schedule.date") + ":");
        this.dayAmount.setText(xerb.getString("window.archive.archivedays") + ":");
        this.scheduleAuditCheckBoxLabel.setText(xerb.getString("window.schedule.checkbox") + ":");
        this.scheduleAuditComboBoxLabel.setText(xerb.getString("window.schedule.type") + ":");
        this.scheduleAuditCalendar.setVisible(false);
        okButton.setText(xerb.getString("general.continue"));
        cancelButton.setText(xerb.getString("general.cancel"));
        //processing independent of creation
        if (((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(3)).getAttrib_unsignedint() == 0) {
            revisionModelCheckbox.setSelected(false);
        } else {
            revisionModelCheckbox.setSelected(true);
        }
        //fill archiving model list
        dcbm.removeAllElements();
        dcbm.addElement(xerb.getString("window.archive.archivingmodel.none"));
        dcbm.addElement(xerb.getString("window.archive.archivingmodel.archivedate"));
        dcbm.addElement(xerb.getString("window.archive.archivingmodel.archivedays"));
        archiveModelDropDown.setSelectedIndex((int)((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(4)).getAttrib_unsignedint());
        //set date / days
        //convert clone from remote time to local time
        Calendar cal = (Calendar)((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(5)).getAttrib_datetime().clone();
        Calendar realcal = ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(5)).getAttrib_datetime();
        Calendar ngc = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - realcal.get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + realcal.get(Calendar.DST_OFFSET)) );
        archiveModelCalendar.setDate(cal.getTime());
        dayAmountTextBox.setText("" + ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(6)).getAttrib_unsignedint());
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        revisionModelLabel = new javax.swing.JLabel();
        revisionModelCheckbox = new javax.swing.JCheckBox();
        archiveModelLabel = new javax.swing.JLabel();
        archiveModelDropDown = new javax.swing.JComboBox();
        dateLabel = new javax.swing.JLabel();
        dayAmount = new javax.swing.JLabel();
        dayAmountTextBox = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        scheduleAuditCheckBoxLabel = new javax.swing.JLabel();
        scheduleAuditCheckBox = new javax.swing.JCheckBox();
        scheduleAuditCalendar = new com.toedter.calendar.JDateChooser();
        archiveModelCalendar = new com.toedter.calendar.JDateChooser();
        scheduleAuditComboBox = new javax.swing.JComboBox();
        scheduleAuditComboBoxLabel = new javax.swing.JLabel();
        auditDateLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        revisionModelLabel.setText("jLabel1");

        revisionModelCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        revisionModelCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        archiveModelLabel.setText("jLabel1");

        archiveModelDropDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archiveModelDropDownActionPerformed(evt);
            }
        });

        dateLabel.setText("jLabel1");

        dayAmount.setText("jLabel1");

        dayAmountTextBox.setColumns(5);
        dayAmountTextBox.setText("jTextField1");
        dayAmountTextBox.setEnabled(false);

        okButton.setText("jButton1");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("jButton2");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        scheduleAuditCheckBoxLabel.setText("jLabel1");

        scheduleAuditCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scheduleAuditCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        scheduleAuditCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scheduleAuditCheckBoxStateChanged(evt);
            }
        });

        archiveModelCalendar.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        archiveModelCalendar.setEnabled(false);

        scheduleAuditComboBox.setEnabled(false);

        scheduleAuditComboBoxLabel.setText("jLabel1");

        auditDateLabel.setText("jLabel1");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                            .add(101, 101, 101)
                                            .add(okButton)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                                        .add(layout.createSequentialGroup()
                                            .addContainerGap()
                                            .add(revisionModelLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                                        .add(layout.createSequentialGroup()
                                            .addContainerGap()
                                            .add(archiveModelLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                                    .add(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .add(dateLabel)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                                .add(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .add(dayAmount)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                            .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(scheduleAuditCheckBoxLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .add(layout.createSequentialGroup()
                            .addContainerGap()
                            .add(scheduleAuditComboBoxLabel)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(auditDateLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(cancelButton)
                        .add(126, 126, 126))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(dayAmountTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 144, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(revisionModelCheckbox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(scheduleAuditCheckBox)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 177, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, scheduleAuditComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, scheduleAuditCalendar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, archiveModelDropDown, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, archiveModelCalendar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(layout.createSequentialGroup()
                        .add(revisionModelLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(archiveModelLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(dateLabel))
                    .add(layout.createSequentialGroup()
                        .add(revisionModelCheckbox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(archiveModelDropDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(archiveModelCalendar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dayAmountTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dayAmount))
                .add(5, 5, 5)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(scheduleAuditCheckBox)
                    .add(scheduleAuditCheckBoxLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(scheduleAuditComboBoxLabel)
                    .add(scheduleAuditComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(auditDateLabel)
                    .add(scheduleAuditCalendar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(okButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cancelButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void scheduleAuditCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_scheduleAuditCheckBoxStateChanged
        if(scheduleAuditCheckBox.isSelected()){
            this.scheduleAuditComboBox.setEnabled(true);
            this.scheduleAuditCalendar.setEnabled(true);
            this.scheduleAuditCalendar.setVisible(true);
            this.auditDateLabel.setVisible(true);
        }
        else{
            this.scheduleAuditComboBox.setEnabled(false);
            this.scheduleAuditCalendar.setEnabled(false);
            this.scheduleAuditCalendar.setVisible(false);
            this.auditDateLabel.setVisible(false);
        }
    }//GEN-LAST:event_scheduleAuditCheckBoxStateChanged
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
//update archiving options of selected data
        if (revisionModelCheckbox.isSelected()) {
            ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(3)).setAttrib_unsignedint(1);
        } else {
            ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(3)).setAttrib_unsignedint(0);
        }
        ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(4)).setAttrib_unsignedint(archiveModelDropDown.getSelectedIndex());
        int temp_days_int = 0;
        try {
            //set FIXED date: GMT, no DST
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cal.set(Calendar.DST_OFFSET, 0);
            cal.setTime(archiveModelCalendar.getDate());
            cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(5)).setAttrib_datetime(cal);
            temp_days_int = Integer.parseInt(dayAmountTextBox.getText());
            ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(6)).setAttrib_unsignedint(temp_days_int);
            //Process scheduled audit fields
            cal= new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cal.set(Calendar.DST_OFFSET, 0);
            cal.setTime(scheduleAuditCalendar.getDate());
            cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            //close dialog
            explorer.set_global_dialog_return_value(1);
            setVisible(false);
        } catch (Exception parseex) {
        }
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void archiveModelDropDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveModelDropDownActionPerformed
        if (archiveModelDropDown.getSelectedIndex() == 1) {
            archiveModelCalendar.setEnabled(true);
            archiveModelCalendar.setVisible(true);
            dateLabel.setVisible(true);
            dayAmountTextBox.setEnabled(false);
        } else if (archiveModelDropDown.getSelectedIndex() == 2) {
            archiveModelCalendar.setEnabled(false);
            archiveModelCalendar.setVisible(false);
            dayAmountTextBox.setEnabled(true);
            dateLabel.setVisible(false);
        } else {
            archiveModelCalendar.setEnabled(false);
            dayAmountTextBox.setEnabled(false);
            archiveModelCalendar.setVisible(false);
            dateLabel.setVisible(false);
        }
    }//GEN-LAST:event_archiveModelDropDownActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser archiveModelCalendar;
    private javax.swing.JComboBox archiveModelDropDown;
    private javax.swing.JLabel archiveModelLabel;
    private javax.swing.JLabel auditDateLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel dayAmount;
    private javax.swing.JTextField dayAmountTextBox;
    private javax.swing.JButton okButton;
    private javax.swing.JCheckBox revisionModelCheckbox;
    private javax.swing.JLabel revisionModelLabel;
    private com.toedter.calendar.JDateChooser scheduleAuditCalendar;
    private javax.swing.JCheckBox scheduleAuditCheckBox;
    private javax.swing.JLabel scheduleAuditCheckBoxLabel;
    private javax.swing.JComboBox scheduleAuditComboBox;
    private javax.swing.JLabel scheduleAuditComboBoxLabel;
    // End of variables declaration//GEN-END:variables
    
}
