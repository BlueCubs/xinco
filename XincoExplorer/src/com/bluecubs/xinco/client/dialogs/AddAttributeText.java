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
 * Name:            AddAttributeText.java
 *
 * Description:     Add Attribute Text
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
 */
package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.object.XincoMutableTreeNode;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.client.service.XincoAddAttribute;
import com.bluecubs.xinco.client.service.XincoCoreData;
import java.util.ArrayList;

/**
 *
 * @author  Javier A. Ortiz
 */
public final class AddAttributeText extends AbstractDialog {

    private XincoExplorer explorer;
    private boolean viewOnly = false;

    /** Creates new form AddAttributeText
     * @param parent
     * @param modal 
     * @param viewOnly 
     * @param explorer
     */
    public AddAttributeText(java.awt.Frame parent, boolean modal, boolean viewOnly,
            XincoExplorer explorer) {
        super(parent, modal);
        setViewOnly(viewOnly);
        this.explorer = explorer;
        initComponents();
        setTitle(explorer.getResourceBundle().getString("window.addattributestext")
                + ": " + this.explorer.getSelectedNodeDesignation());
        save.setText(explorer.getResourceBundle().getString("general.save") + "!");
        cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        setBounds(200, 200, 600, 540);
        setResizable(false);
        text.setEditable(!isViewOnly());
        //processing independent of creation
        if (((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getStatusNumber() == 1) {
            save.setEnabled(!isViewOnly());
        } else {
            save.setEnabled(false);
        }
        addTextArea(text, "");
    }

    /**
     * 
     * @return boolean
     */
    private boolean isViewOnly() {
        return viewOnly;
    }

    /**
     * 
     * @param viewOnly
     */
    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
    }

    @Override
    public void setToDefaults() {
        super.setToDefaults();
        XincoMutableTreeNode node = explorer.getSession().getCurrentTreeNodeSelection();
        ArrayList attr = new ArrayList();
        attr.addAll(((XincoCoreData) node.getUserObject()).getXincoAddAttributes());
        if (((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getStatusNumber() != 2) {
            save.setEnabled(!isViewOnly());
        } else {
            save.setEnabled(false);
        }
        text.setEditable(!isViewOnly());
        if (isViewOnly()) {
            String temp = "";
            for (int i = 0; i < attr.size(); i++) {
                if (((XincoAddAttribute) attr.get(i)).getAttributeId() == 1) {
                    temp += ((XincoAddAttribute) attr.get(i)).getAttribText();
                }
                text.setText(temp);
                save.setEnabled(false);
            }
        } else {
            text.setText(((XincoAddAttribute) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXincoAddAttributes().get(0)).getAttribText());
        }
        jScrollPane1.setLocation(0, 0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        cancel = new javax.swing.JButton();
        save = new javax.swing.JButton();

        text.setColumns(20);
        text.setRows(5);
        jScrollPane1.setViewportView(text);

        cancel.setText("jButton1");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        save.setText("jButton2");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(save)
                        .addGap(60, 60, 60)
                        .addComponent(cancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancel)
                    .addComponent(save))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);

    }//GEN-LAST:event_cancelActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        save.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ((XincoAddAttribute) ((XincoCoreData) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getXincoAddAttributes().get(0)).setAttribText(text.getText());
                explorer.setGlobalDialogReturnValue(1);
                setVisible(false);
            }
        });
    }//GEN-LAST:event_saveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton save;
    private javax.swing.JTextArea text;
    // End of variables declaration//GEN-END:variables
}
