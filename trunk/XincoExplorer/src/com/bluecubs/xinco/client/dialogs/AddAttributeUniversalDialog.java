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

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.client.service.XincoAddAttribute;
import com.bluecubs.xinco.client.service.XincoCoreData;
import com.bluecubs.xinco.client.service.XincoCoreDataTypeAttribute;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Add Attribute Universal Dialog
 * @author Javier A. Ortiz
 */
public class AddAttributeUniversalDialog extends AbstractDialog {

    private XincoExplorer explorer = null;

    /**
     * Creates new form AddAttributeUniversalDialog
     * @param parent Dialog's parent
     * @param modal Is modal?
     * @param explorer Related XincoExplorer.
     */
    public AddAttributeUniversalDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        this.explorer = explorer;
        setTitle(explorer.getResourceBundle().getString("window.addattributesuniversal"));
        setBounds(200, 200, 600, 540);
        setLocationRelativeTo(null);
        cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        save.setText(explorer.getResourceBundle().getString("general.save") + "!");
    }

    @Override
    public void setToDefaults() {
        super.setToDefaults();
        XincoCoreData data = ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject());
        //processing independent of creation
        String[] cn = {explorer.getResourceBundle().getString("general.attribute"), explorer.getResourceBundle().getString("general.details")};
        int i = 0, j = 0, start = 0;
        //reset selection
        table.editCellAt(-1, -1);
        save.setEnabled(data.getStatusNumber() == 1);
        DefaultTableModel dtm = new DefaultTableModel(cn, 0);
        j = dtm.getRowCount();
        for (i = 0; i < j; i++) {
            dtm.removeRow(0);
        }
        String[] rdata = {"", ""};
        //prevent editing of fixed attributes for certain data types
        start = 0;
        //file = 1
        if (data.getXincoCoreDataType().getId() == 1) {
            start = 8;
        }
        //text = 2
        if (data.getXincoCoreDataType().getId() == 2) {
            start = 1;
        }
        List<XincoAddAttribute> attributes;
        if (data.getId() == 0) {
            // Is a new data, there's nothing yet in the database.
            // Load local values.
            attributes = data.getXincoAddAttributes();
        } else {
            attributes = explorer.getSession().getXinco().getXincoAddAttributes(data,
                    explorer.getSession().getUser());
        }
        List<XincoCoreDataTypeAttribute> dataTypeAttributes = explorer.getSession().getXinco().getXincoCoreDataTypeAttribute(data.getXincoCoreDataType(),
                explorer.getSession().getUser());
        for (i = start; i < attributes.size(); i++) {
            rdata[0] = dataTypeAttributes.get(i).getDesignation();
            rdata[1] = "";
            if (dataTypeAttributes.get(i).getDataType().equals("datetime")) {
                rdata[1] = "" + ((XMLGregorianCalendar) (attributes.get(i)).getAttribDatetime()).toGregorianCalendar().getTime().toString();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("double")) {
                rdata[1] = "" + (attributes.get(i)).getAttribDouble();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("int")) {
                rdata[1] = "" + (attributes.get(i)).getAttribInt();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("text")) {
                rdata[1] = "" + (attributes.get(i)).getAttribText();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("unsignedint")) {
                rdata[1] = "" + (attributes.get(i)).getAttribUnsignedint();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("varchar")) {
                rdata[1] = "" + (attributes.get(i)).getAttribVarchar();
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
        XincoCoreData data = ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject());
        int i = 0, start = 0;
        String text = "";
        DefaultTableModel dtm;
        Date attrDt = new Date(0);
        int attrI = 0;
        long attr_l = 0;
        double attrD = 0;

        //make sure changes are committed
        table.editCellAt(-1, -1);
        //prevent editing of fixed attributes for certain data types
        start = 0;
        //file = 1
        if (data.getXincoCoreDataType().getId() == 1) {
            start = 8;
        }
        if (data.getXincoCoreDataType().getId() == 2) {
            start = 1;
        }
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
        List<XincoCoreDataTypeAttribute> dataTypeAttributes =
                explorer.getSession().getXinco().getXincoCoreDataTypeAttribute(
                data.getXincoCoreDataType(), explorer.getSession().getUser());
        for (i = 0; i < attributes.size() - start; i++) {
            dtm = (DefaultTableModel) table.getModel();
            try {
                text = (String) dtm.getValueAt(i, 1);
            } catch (Exception dtme) {
                text = "";
            }
            if ((dataTypeAttributes.get(i + start)).getDataType().equals("datetime")) {
                try {
                    DateFormat df = DateFormat.getInstance();
                    attrDt = df.parse(text);
                } catch (Exception pe) {
                    attrDt = new Date(0);
                }
                ((XMLGregorianCalendar) (attributes.get(i + start)).getAttribDatetime()).toGregorianCalendar().setTime(attrDt);
            }
            if ((dataTypeAttributes.get(i + start)).getDataType().equals("double")) {
                try {
                    attrD = Double.parseDouble(text);
                } catch (Exception pe) {
                    attrD = 0.0;
                }
                (attributes.get(i + start)).setAttribDouble(attrD);
            }
            if ((dataTypeAttributes.get(i + start)).getDataType().equals("int")) {
                try {
                    attrI = Integer.parseInt(text);
                } catch (Exception pe) {
                    attrI = 0;
                }
                (attributes.get(i + start)).setAttribInt(attrI);
            }
            if ((dataTypeAttributes.get(i + start)).getDataType().equals("text")) {
                (attributes.get(i + start)).setAttribText(text);
            }
            if ((dataTypeAttributes.get(i + start)).getDataType().equals("unsignedint")) {
                try {
                    attr_l = Long.parseLong(text);
                } catch (Exception pe) {
                    attr_l = 0;
                }
                (attributes.get(i + start)).setAttribUnsignedint(attr_l);
            }
            if ((dataTypeAttributes.get(i + start)).getDataType().equals("varchar")) {
                (attributes.get(i + start)).setAttribVarchar(text);
            }
        }
        explorer.setGlobalDialogReturnValue(1);
        setVisible(false);
    }//GEN-LAST:event_saveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton save;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
