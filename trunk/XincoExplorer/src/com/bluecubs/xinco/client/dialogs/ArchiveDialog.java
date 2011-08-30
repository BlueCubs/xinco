/**
 *Copyright 2011 blueCubs.com
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
 * 
 *
 *************************************************************
 * ArchiveDialog.java
 *
 * Created on January 5, 2007, 9:17 AM
 */
package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.client.service.XincoAddAttribute;
import com.bluecubs.xinco.client.service.XincoCoreData;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author  Javier A. Ortiz
 */
public class ArchiveDialog extends AbstractDialog {

    private XincoExplorer explorer;
    private ResourceBundle xerb;

    /** Creates new form ArchiveDialog
     * @param parent
     * @param modal
     * @param explorer
     */
    public ArchiveDialog(java.awt.Frame parent, boolean modal, final XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.explorer = explorer;
        xerb = this.explorer.getResourceBundle();
        revisionModelLabel.setText(xerb.getString("window.archive.revisionmodel") + ":");
        revisionModelCheckbox.setSelected(true);
        archiveModelLabel.setText(xerb.getString("window.archive.archivingmodel") + ":");
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        archiveModelDropDown.setModel(dcbm);
        archiveModelDropDown.setBounds(120, 40, 250, 20);
        archiveModelDropDown.setEditable(false);
        dateLabel.setText(xerb.getString("window.archive.archivedate") + ":");
        dayAmount.setText(xerb.getString("window.archive.archivedays") + ":");
        okButton.setText(xerb.getString("general.continue"));
        cancelButton.setText(xerb.getString("general.cancel"));
    }

    @Override
    public void setToDefaults() {
        super.setToDefaults();
        XincoCoreData data = ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject());
        List<XincoAddAttribute> attributes;
        if (data.getId() == 0) {
            // Is a new data, there's nothing yet in the database.
            // Load local values.
            attributes = data.getXincoAddAttributes();
        } else {
            attributes = explorer.getSession().getXinco().getXincoAddAttributes(data,
                    explorer.getSession().getUser());
        }
        //processing independent of creation
        if (attributes.get(3).getAttribUnsignedint() == 0) {
            revisionModelCheckbox.setSelected(false);
        } else {
            revisionModelCheckbox.setSelected(true);
        }
        //fill archiving model list
        ((DefaultComboBoxModel) archiveModelDropDown.getModel()).removeAllElements();
        ((DefaultComboBoxModel) archiveModelDropDown.getModel()).addElement(xerb.getString("window.archive.archivingmodel.none"));
        ((DefaultComboBoxModel) archiveModelDropDown.getModel()).addElement(xerb.getString("window.archive.archivingmodel.archivedate"));
        ((DefaultComboBoxModel) archiveModelDropDown.getModel()).addElement(xerb.getString("window.archive.archivingmodel.archivedays"));
        archiveModelDropDown.setSelectedIndex((int) ((XincoAddAttribute) attributes.get(4)).getAttribUnsignedint());
        //set date / days
        //convert clone from remote time to local time
        Calendar cal = (Calendar) ((XMLGregorianCalendar) (attributes.get(5)).getAttribDatetime()).toGregorianCalendar().clone();
        Calendar realcal = ((XMLGregorianCalendar) (attributes.get(5)).getAttribDatetime()).toGregorianCalendar();
        Calendar ngc = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - realcal.get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + realcal.get(Calendar.DST_OFFSET)));
        yearTextBox.setText("" + cal.get(Calendar.YEAR));
        monthTextBox.setText("" + (cal.get(Calendar.MONTH) + 1));
        dayTextBox.setText("" + cal.get(Calendar.DAY_OF_MONTH));
        dayAmountTextBox.setText("" + ((XincoAddAttribute) attributes.get(6)).getAttribUnsignedint());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        revisionModelLabel = new javax.swing.JLabel();
        revisionModelCheckbox = new javax.swing.JCheckBox();
        archiveModelLabel = new javax.swing.JLabel();
        archiveModelDropDown = new javax.swing.JComboBox();
        dateLabel = new javax.swing.JLabel();
        yearTextBox = new javax.swing.JTextField();
        monthTextBox = new javax.swing.JTextField();
        dayTextBox = new javax.swing.JTextField();
        dayAmount = new javax.swing.JLabel();
        dayAmountTextBox = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        revisionModelLabel.setText("jLabel1");

        revisionModelCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        revisionModelCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        archiveModelLabel.setText("jLabel1");

        archiveModelDropDown.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        archiveModelDropDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archiveModelDropDownActionPerformed(evt);
            }
        });

        dateLabel.setText("jLabel1");

        yearTextBox.setColumns(5);
        yearTextBox.setText("jTextField1");
        yearTextBox.setEnabled(false);

        monthTextBox.setColumns(4);
        monthTextBox.setText("jTextField1");
        monthTextBox.setEnabled(false);

        dayTextBox.setColumns(4);
        dayTextBox.setText("jTextField1");
        dayTextBox.setEnabled(false);

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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(dateLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 190, Short.MAX_VALUE))
                            .add(revisionModelLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .add(archiveModelLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
                        .add(layout.createSequentialGroup()
                            .add(dayAmount)
                            .add(157, 157, 157)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okButton)
                        .add(21, 21, 21)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cancelButton)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(dayAmountTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(layout.createSequentialGroup()
                            .add(monthTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(dayTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(yearTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(revisionModelCheckbox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                        .add(archiveModelDropDown, 0, 223, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(revisionModelLabel)
                    .add(revisionModelCheckbox))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(archiveModelLabel)
                    .add(archiveModelDropDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dateLabel)
                    .add(monthTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dayTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(yearTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dayAmount)
                    .add(dayAmountTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(17, 17, 17)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(okButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void archiveModelDropDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveModelDropDownActionPerformed
        XincoCoreData data = ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject());
        //update add attributes
        List<XincoAddAttribute> attributes;
        if (data.getId() == 0) {
            // Is a new data, there's nothing yet in the database.
            // Load local values.
            attributes = data.getXincoAddAttributes();
        } else {
            attributes = explorer.getSession().getXinco().getXincoAddAttributes(data,
                    explorer.getSession().getUser());
        }
        //update archiving options of selected data
        if (revisionModelCheckbox.isSelected()) {
            ((XincoAddAttribute) attributes.get(3)).setAttribUnsignedint(1);
        } else {
            ((XincoAddAttribute) attributes.get(3)).setAttribUnsignedint(0);
        }
        ((XincoAddAttribute) attributes.get(4)).setAttribUnsignedint(archiveModelDropDown.getSelectedIndex());
        int temp_year_int = 0;
        int temp_month_int = 0;
        int tempDay_int = 0;
        int tempDays_int = 0;
        try {
            temp_year_int = Integer.parseInt(yearTextBox.getText());
            temp_month_int = Integer.parseInt(monthTextBox.getText());
            tempDay_int = Integer.parseInt(dayTextBox.getText());
            //set FIXED date: GMT, no DST
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cal.set(Calendar.DST_OFFSET, 0);
            cal.set(Calendar.ZONE_OFFSET, 0);
            cal.set(Calendar.YEAR,
                    archiveModelDropDown.getSelectedIndex() == 1
                    ? temp_year_int : 0);
            cal.set(Calendar.MONTH,
                    archiveModelDropDown.getSelectedIndex() == 1
                    ? (temp_month_int - 1) : 0);
            cal.set(Calendar.DAY_OF_MONTH,
                    archiveModelDropDown.getSelectedIndex() == 1
                    ? tempDay_int : 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(cal.getTime());
            DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            ((XincoAddAttribute) attributes.get(5)).setAttribDatetime(
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));

            tempDays_int = Integer.parseInt(dayAmountTextBox.getText());
            ((XincoAddAttribute) attributes.get(6)).setAttribUnsignedint(tempDays_int);
            //close dialog
            explorer.setGlobalDialogReturnValue(1);
            setVisible(false);
        } catch (Exception parseex) {
            Logger.getLogger(ArchiveDialog.class.getSimpleName()).log(Level.SEVERE, null, parseex);
        }
    }//GEN-LAST:event_archiveModelDropDownActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        XincoCoreData data = ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject());
        //update add attributes
        List<XincoAddAttribute> attributes;
        if (data.getId() == 0) {
            // Is a new data, there's nothing yet in the database.
            // Load local values.
            attributes = data.getXincoAddAttributes();
        } else {
            attributes = explorer.getSession().getXinco().getXincoAddAttributes(data,
                    explorer.getSession().getUser());
        }
        //update archiving options of selected data
        if (revisionModelCheckbox.isSelected()) {
            ((XincoAddAttribute) attributes.get(3)).setAttribUnsignedint(1);
        } else {
            ((XincoAddAttribute) attributes.get(3)).setAttribUnsignedint(0);
        }
        ((XincoAddAttribute) attributes.get(4)).setAttribUnsignedint(archiveModelDropDown.getSelectedIndex());
        int temp_year_int = 0;
        int temp_month_int = 0;
        int tempDay_int = 0;
        int tempDays_int = 0;
        try {
            temp_year_int = Integer.parseInt(yearTextBox.getText());
            temp_month_int = Integer.parseInt(monthTextBox.getText());
            tempDay_int = Integer.parseInt(dayTextBox.getText());
            //set FIXED date: GMT, no DST
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cal.set(Calendar.DST_OFFSET, 0);
            cal.set(Calendar.YEAR, temp_year_int);
            cal.set(Calendar.MONTH, (temp_month_int - 1));
            cal.set(Calendar.DAY_OF_MONTH, tempDay_int);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(cal.getTime());
            DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            ((XincoAddAttribute) attributes.get(5)).setAttribDatetime(
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));

            tempDays_int = Integer.parseInt(dayAmountTextBox.getText());
            ((XincoAddAttribute) attributes.get(6)).setAttribUnsignedint(tempDays_int);
            //close dialog
            explorer.setGlobalDialogReturnValue(1);
            setVisible(false);
        } catch (Exception parseex) {
            Logger.getLogger(ArchiveDialog.class.getSimpleName()).log(Level.SEVERE, null, parseex);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox archiveModelDropDown;
    private javax.swing.JLabel archiveModelLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel dayAmount;
    private javax.swing.JTextField dayAmountTextBox;
    private javax.swing.JTextField dayTextBox;
    private javax.swing.JTextField monthTextBox;
    private javax.swing.JButton okButton;
    private javax.swing.JCheckBox revisionModelCheckbox;
    private javax.swing.JLabel revisionModelLabel;
    private javax.swing.JTextField yearTextBox;
    // End of variables declaration//GEN-END:variables
}
