/*
 * DataFolderDialog.java
 *
 * Created on November 21, 2006, 4:23 PM
 */

package com.bluecubs.xinco.client.dialogs;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.XincoMutableTreeNode;
import com.bluecubs.xinco.core.XincoCoreLanguage;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoException; 
import java.util.Locale;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

/**
 * 
 * Data folder Dialog
 * @author ortizbj
 */
public class DataFolderDialog extends javax.swing.JDialog {
    private XincoExplorer explorer=null;
    /**
     * Creates new form DataFolderDialog
     * @param parent Dialog's parent
     * @param modal Is modal?
     * @param explorer Related XincoExplorer.
     */
    public DataFolderDialog(java.awt.Frame parent, boolean modal, XincoExplorer explorer) {
        super(parent, modal);
        initComponents();
        this.explorer=explorer;
        setTitle(explorer.getResourceBundle().getString("window.folder"));
        this.save.setText(explorer.getResourceBundle().getString("general.save")+"!");
        this.cancel.setText(explorer.getResourceBundle().getString("general.cancel"));
        this.designationLabel.setText(explorer.getResourceBundle().getString("general.designation"));
        this.idLabel.setText(explorer.getResourceBundle().getString("general.id"));
        this.languageLabel.setText(explorer.getResourceBundle().getString("general.language"));
        this.stateLabel.setText(explorer.getResourceBundle().getString("general.status"));
        //processing independent of creation
        int i = 0;
        String text = "";
        int selection = -1;
        int alt_selection = 0;
        if (!(explorer.getSession().currentTreeNodeSelection.getUserObject() == null)) {
            text = "" + ((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId();
            this.id.setText(text);
            text = "" + ((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getDesignation();
            this.designation.setText(text);
            this.designation.selectAll();
            DefaultListModel dlm = new DefaultListModel();
            dlm.removeAllElements();
            for (i=0;i<explorer.getSession().server_languages.size();i++) {
                text = ((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getDesignation() + " (" + ((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getSign() + ")";
                dlm.addElement(text);
                if (((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getId() == ((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getXinco_core_language().getId()) {
                    this.language.setSelectedIndex(i);
                }
                if (((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId() == 0) {
                    if (((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getSign().toLowerCase().compareTo(Locale.getDefault().getLanguage().toLowerCase()) == 0) {
                        selection = i;
                    }
                    if (((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(i)).getId() == 1) {
                        alt_selection = i;
                    }
                }
            }
            this.language.setModel(dlm);
            if (((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId() == 0) {
                if (selection == -1) {
                    selection = alt_selection;
                }
                this.language.setSelectedIndex(selection);
            }
            this.language.ensureIndexIsVisible(this.language.getSelectedIndex());
            if (((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getStatus_number() == 1) {
                text = explorer.getResourceBundle().getString("general.status.open") + "";
            }
            if (((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getStatus_number() == 2) {
                text = explorer.getResourceBundle().getString("general.status.locked") + " (-)";
            }
            if (((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getStatus_number() == 3) {
                text = explorer.getResourceBundle().getString("general.status.archived") + " (->)";
            }
            this.state.setText(text);
        }
        setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        idLabel = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        designationLabel = new javax.swing.JLabel();
        designation = new javax.swing.JTextField();
        languageLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        language = new javax.swing.JList();
        stateLabel = new javax.swing.JLabel();
        state = new javax.swing.JTextField();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        idLabel.setText("jLabel1");

        id.setEditable(false);
        id.setText("jTextField1");

        designationLabel.setText("jLabel1");

        designation.setText("jTextField2");

        languageLabel.setText("jLabel1");

        language.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(language);

        stateLabel.setText("jLabel1");

        state.setEditable(false);
        state.setText("jTextField1");

        save.setText("jButton1");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        cancel.setText("jButton2");
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
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(idLabel)
                            .add(designationLabel)
                            .add(languageLabel)
                            .add(stateLabel))
                        .add(53, 53, 53)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(state)
                            .add(jScrollPane1, 0, 0, Short.MAX_VALUE)
                            .add(designation)
                            .add(id, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(save)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 82, Short.MAX_VALUE)
                        .add(cancel)))
                .addContainerGap(36, Short.MAX_VALUE))
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
                    .add(designationLabel)
                    .add(designation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(languageLabel)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(stateLabel)
                    .add(state, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancel)
                    .add(save))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        XincoMutableTreeNode temp_node = null;
        //delete new folder from treemodel if not saved to server yet
        if (((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId() == 0) {
            temp_node = explorer.getSession().currentTreeNodeSelection;
            explorer.getSession().currentTreeNodeSelection = (XincoMutableTreeNode)explorer.getSession().currentTreeNodeSelection.getParent();
            explorer.jTreeRepository.setSelectionPath(new TreePath(explorer.getSession().currentTreeNodeSelection.getPath()));
            explorer.getSession().xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
        }
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed
    
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        boolean insertnewnode = false;
        XincoMutableTreeNode temp_node = null;
        XincoCoreNode newnode = (XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject();
        //check if inserting new node
        if (newnode.getId() <= 0) {
            insertnewnode = true;
        }
        //set altered values
        newnode.setDesignation(this.designation.getText());
        if(this.language.getSelectedIndex()<0)
            this.language.setSelectedIndex(0);
        newnode.setXinco_core_language(((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(this.language.getSelectedIndex())));
        try {
            //optimize node size
            newnode.setXinco_core_nodes(new Vector());
            newnode.setXinco_core_data(new Vector());
            //invoke web-service
            if ((newnode = explorer.getSession().xinco.setXincoCoreNode(newnode, explorer.getSession().user)) != null) {
                //update to modified user object
                explorer.getSession().currentTreeNodeSelection.setUserObject(newnode);
                explorer.getSession().xincoClientRepository.treemodel.nodeChanged((explorer.getSession().currentTreeNodeSelection));
                //select parent of new node
                if (insertnewnode) {
                    explorer.getSession().currentTreeNodeSelection = (XincoMutableTreeNode)explorer.getSession().currentTreeNodeSelection.getParent();
                }
                explorer.jTreeRepository.setSelectionPath(new TreePath(explorer.getSession().currentTreeNodeSelection.getPath()));
                explorer.jLabelInternalFrameInformationText.setText(explorer.getResourceBundle().getString("window.folder.updatesuccess"));
            } else {
                throw new XincoException(explorer.getResourceBundle().getString("error.nowritepermission"));
            }
        } catch (Exception rmie) {
            //remove new node in case off error
            if (insertnewnode) {
                temp_node = explorer.getSession().currentTreeNodeSelection;
                explorer.getSession().currentTreeNodeSelection = 
                        (XincoMutableTreeNode)explorer.getSession().currentTreeNodeSelection.getParent();
                explorer.jTreeRepository.setSelectionPath(new TreePath(explorer.getSession().currentTreeNodeSelection.getPath()));
                explorer.getSession().xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
            }
            JOptionPane.showMessageDialog(explorer, 
                    explorer.getResourceBundle().getString("window.folder.updatefailed") + 
                    " " + explorer.getResourceBundle().getString("general.reason") + ": " + 
                    rmie.toString(), explorer.getResourceBundle().getString("general.error"), 
                    JOptionPane.WARNING_MESSAGE);
        }
        setVisible(false);
    }//GEN-LAST:event_saveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JTextField designation;
    private javax.swing.JLabel designationLabel;
    private javax.swing.JTextField id;
    private javax.swing.JLabel idLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList language;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JButton save;
    private javax.swing.JTextField state;
    private javax.swing.JLabel stateLabel;
    // End of variables declaration//GEN-END:variables
    
}
