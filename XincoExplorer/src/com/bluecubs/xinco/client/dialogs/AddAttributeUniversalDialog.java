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
 * Name:            AddAttributeUniversalDialog
 *
 * Description:     Add Attribute Universal Dialog
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
 * AddAttributeUniversalDialog.java
 *
 * Created on November 22, 2006, 9:27 AM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreDataTypeAttribute;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader; 

/**
 * Add Attribute Universal Dialog
 * @author ortizbj
 */
public class AddAttributeUniversalDialog extends javax.swing.JDialog {
    private XincoExplorer explorer=null;
    /**
     * Creates new form AddAttributeUniversalDialog
     * @param parent Dialog's parent
     * @param modal Is modal?
     * @param explorer Related XincoExplorer.
     */
    public AddAttributeUniversalDialog(java.awt.Frame parent, boolean modal,XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        this.explorer=explorer;
        setTitle(explorer.getResourceBundle().getString("window.addattributesuniversal"));
        setBounds(200, 200, 600, 540);
        setLocationRelativeTo(null);
        this.cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        this.save.setText(explorer.getResourceBundle().getString("general.save")+"!");
        
        String[] cn = {explorer.getResourceBundle().getString("general.attribute"),explorer.getResourceBundle().getString("general.details")};
        int i=0,j=0, start = 0;
        //reset selection
        table.editCellAt(-1, -1);
        //processing independent of creation
        if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getStatus_number() == 1) {
            this.save.setEnabled(true);
        } else {
            this.save.setEnabled(false);
        }
        DefaultTableModel dtm = new DefaultTableModel(cn, 0);
        j = dtm.getRowCount();
        for (i=0;i<j;i++) {
            dtm.removeRow(0);
        }
        String[] rdata = {"",""};
        //prevent editing of fixed attributes for certain data types
        start = 0;
        //file = 1
        if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getId() == 1) {
            start = 8;
        }
        //text = 2
        if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getId() == 2) {
            start = 1;
        }
        for (i=start;i<((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().size();i++) {
            rdata[0] = ((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getDesignation();
            rdata[1] = "";
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equals(new String("datetime"))) {
                rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_datetime().getTime().toString();
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equals(new String("double"))) {
                rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_double();
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equals(new String("int"))) {
                rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_int();
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equals(new String("text"))) {
                rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_text();
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equals(new String("unsignedint"))) {
                rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_unsignedint();
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equals(new String("varchar"))) {
                rdata[1] = "" + ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i)).getAttrib_varchar();
            }
            dtm.addRow(rdata);
        }
        //set selection
        table.editCellAt(0, 1);
        table.setModel(dtm);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        cancel = new javax.swing.JButton();
        save = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Description", null},
                {"keyword_1", null},
                {"keyword_2", null},
                {"keyword_3", null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        table.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(table);

        cancel.setText("jButton1");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        save.setText("jButton1");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(15, 15, 15)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(save)
                        .add(22, 22, 22)
                        .add(cancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 275, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancel)
                    .add(save))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed
    
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        int i=0, start=0;
        String text = "";
        DefaultTableModel dtm;
        Date attr_dt = new Date(0);
        int attr_i = 0;
        long attr_l = 0;
        double attr_d = 0;
        
        //make sure changes are committed
        table.editCellAt(-1, -1);
        //prevent editing of fixed attributes for certain data types
        start = 0;
        //file = 1
        if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getId() == 1) {
            start = 8;
        }
        if (((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getId() == 2) {
            start = 1;
        }
        //update add attributes
        for (i=0;i<((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().size()-start;i++) {
            dtm = (DefaultTableModel)table.getModel();
            try {
                text = (String)dtm.getValueAt(i, 1);
            } catch (Exception dtme) {
                text = "";
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i+start)).getData_type().equals(new String("datetime"))) {
                try {
                    DateFormat df = DateFormat.getInstance();
                    attr_dt = df.parse(text);
                } catch (Exception pe) {
                    attr_dt = new Date(0);
                }
                ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i+start)).getAttrib_datetime().setTime(attr_dt);
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i+start)).getData_type().equals(new String("double"))) {
                try {
                    attr_d = Double.parseDouble(text);
                } catch (Exception pe) {
                    attr_d = 0.0;
                }
                ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i+start)).setAttrib_double(attr_d);
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i+start)).getData_type().equals(new String("int"))) {
                try {
                    attr_i = Integer.parseInt(text);
                } catch (Exception pe) {
                    attr_i = 0;
                }
                ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i+start)).setAttrib_int(attr_i);
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i+start)).getData_type().equals(new String("text"))) {
                ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i+start)).setAttrib_text(text);
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i+start)).getData_type().equals(new String("unsignedint"))) {
                try {
                    attr_l = Long.parseLong(text);
                } catch (Exception pe) {
                    attr_l = 0;
                }
                ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i+start)).setAttrib_unsignedint(attr_l);
            }
            if (((XincoCoreDataTypeAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i+start)).getData_type().equals(new String("varchar"))) {
                ((XincoAddAttribute)((XincoCoreData)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_add_attributes().elementAt(i+start)).setAttrib_varchar(text);
            }
        }
        explorer.set_global_dialog_return_value(1);
        setVisible(false);
    }//GEN-LAST:event_saveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton save;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
    
}
