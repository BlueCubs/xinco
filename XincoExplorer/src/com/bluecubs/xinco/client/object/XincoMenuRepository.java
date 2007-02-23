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
 * Name:            XincoMenuRepository
 *
 * Description:     XincoMenuRepository
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
 * XincoMenuRepository.java
 *
 * Created on December 11, 2006, 2:19 PM
 */

package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.XincoMutableTreeNode;
import com.bluecubs.xinco.client.dialogs.ACLDialog;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreLanguage;
import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoException;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author ortizbj
 */
public class XincoMenuRepository extends JMenu{
    private JMenuItem tmi = null;
    private XincoExplorer explorer;
    public JMenuItem AddData=null,AddDataStructure=null,
            ViewURL=null,EmailContact=null,
            EditFolderData=null,CheckoutData=null,
            UndoCheckoutData=null,CheckinData=null,
            PublishData=null,LockData=null,
            DownloadRevision=null,Refresh=null,
            AddFolder=null,ViewEditAddAttributes=null,
            EditFolderDataACL=null,MoveFolderData=null,
            InsertFolderData=null,ViewData=null,
            CommentData=null;
    private JMenuItem [] items = new JMenuItem[20];
    public ResourceBundle xerb;
    private int counter=0;
    private XincoActionHandler handler= null;
    
    /**
     * Creates a new instance of XincoMenuRepository
     */
    public XincoMenuRepository(final XincoExplorer explorer){
        this.explorer=explorer;
        xerb=this.explorer.getResourceBundle();
        handler= new XincoActionHandler(this.explorer);
        //add item
//        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.repository.refresh"));
        this.items[counter].setEnabled(true);
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    // get root
                    XincoCoreNode xnode = new XincoCoreNode();

                    xnode.setId(1);
                    xnode = explorer.getSession().xinco.getXincoCoreNode(xnode,
                            explorer.getSession().user);
                    explorer.getSession().xincoClientRepository.assignObject2TreeNode((XincoMutableTreeNode) (explorer.getSession().xincoClientRepository.treemodel).getRoot(),
                            xnode,
                            explorer.getSession().xinco,
                            explorer.getSession().user,
                            2);
                    explorer.jTreeRepository.expandPath(new TreePath(explorer.getSession().xincoClientRepository.treemodel.getPathToRoot((XincoMutableTreeNode) (explorer.getSession().xincoClientRepository.treemodel).getRoot())));
                    explorer.collapseAllNodes();
                } catch (Exception rmie) {
                    rmie.printStackTrace();
                }
            }
        });
        add(this.items[counter]);
        //add item
        addSeparator();
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.repository.addfolder"));
        this.items[counter].setEnabled(true);
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                XincoMutableTreeNode newnode;
                
                //open folder dialog
                if (explorer.getSession().currentTreeNodeSelection != null) {
                    if (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                        //set current node to new one
                        newnode = new XincoMutableTreeNode(new XincoCoreNode());
                        //set node attributes
                        ((XincoCoreNode)newnode.getUserObject()).setXinco_core_node_id(((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId());
                        ((XincoCoreNode)newnode.getUserObject()).setDesignation(xerb.getString("general.newfolder"));
                        ((XincoCoreNode)newnode.getUserObject()).setXinco_core_language((XincoCoreLanguage)explorer.getSession().server_languages.elementAt(0));
                        ((XincoCoreNode)newnode.getUserObject()).setStatus_number(1);
                        explorer.getSession().xincoClientRepository.treemodel.insertNodeInto(newnode, explorer.getSession().currentTreeNodeSelection, explorer.getSession().currentTreeNodeSelection.getChildCount());
                        explorer.getSession().currentTreeNodeSelection = newnode;
                        explorer.getJDialogFolder();
                        //update treemodel
                        explorer.getSession().xincoClientRepository.treemodel.reload(explorer.getSession().currentTreeNodeSelection);
                        explorer.getSession().xincoClientRepository.treemodel.nodeChanged(explorer.getSession().currentTreeNodeSelection);
                    }
                }
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.repository.adddata"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //data wizard -> add new data object
                explorer.doDataWizard(1);
            }
        });
        add(this.items[counter]);
        increaseItemNumber();
        //add item
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.repository.adddatastructure"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                XincoImportThread importT = new XincoImportThread();
                importT.setXincoExplorer(explorer);
                importT.start();
            }
        });
        add(this.items[counter]);
        //add item
        addSeparator();
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.folderdata"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(
                new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (explorer.getSession().currentTreeNodeSelection != null) {
                    if (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                        //open folder dialog
                        explorer.getJDialogFolder();
                    }
                    if (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                        //data wizard -> edit data object
                        explorer.doDataWizard(2);
                    }
                }
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.repository.vieweditaddattributes"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //data wizard -> edit add attributes
                explorer.doDataWizard(3);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.acl"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int i = 0, j = 0;
                ListModel dlm;
                if (explorer.getSession().currentTreeNodeSelection != null) {
                    //open ACL dialog
                    JDialog jDialogACL=explorer.getJDialogACL();
                    //fill group list
                    dlm = (((ACLDialog)jDialogACL).getACLGroupModel());
                    String[] list = new String[explorer.getSession().server_groups.size()];
                    for (i=0;i<explorer.getSession().server_groups.size();i++) {
                        list[i]=(new String(((XincoCoreGroup)explorer.getSession().server_groups.elementAt(i)).getDesignation()));
                    }
                    ((ACLDialog)jDialogACL).setACLGroupModel(list);
                    //fill ACL
                    ((ACLDialog)jDialogACL).reloadACLListACL();
                    jDialogACL.setVisible(true);
                }
            }
        });
        add(this.items[counter]);
        //add item
        addSeparator();
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.movefolderdatatoclipboard"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int i;
                TreePath[] tps;
                //cut node
                tps =explorer.jTreeRepository.getSelectionPaths();
                explorer.getSession().clipboardTreeNodeSelection.removeAllElements();
                for (i=0;i<explorer.jTreeRepository.getSelectionCount();i++) {
                    explorer.getSession().clipboardTreeNodeSelection.addElement(tps[i].getLastPathComponent());
                }
                //update transaction info
                explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movemessage"));
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.insertfolderdata"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if ((explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() ==
                        XincoCoreNode.class) &&
                        (explorer.getSession().clipboardTreeNodeSelection !=
                        null)) {
                    int old_parent_node_id = 0;
                    int i;
                    int cb_size;
                    XincoMutableTreeNode temp_node;
                    
                    cb_size = explorer.getSession().clipboardTreeNodeSelection.size();
                    for (i = 0; i < cb_size; i++) {
                        temp_node = (XincoMutableTreeNode) explorer.getSession().clipboardTreeNodeSelection.elementAt(0);
                        if (explorer.getSession().currentTreeNodeSelection !=
                                temp_node) {
                            // paste node
                            if (temp_node.getUserObject().getClass() ==
                                    XincoCoreNode.class) {
                                // modify moved node
                                old_parent_node_id = ((XincoCoreNode) temp_node.getUserObject()).getXinco_core_node_id();
                                ((XincoCoreNode) temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getSession().currentTreeNodeSelection.getUserObject()).getId());
                                try {
                                    // modify treemodel
                                    explorer.getSession().xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
                                    explorer.getSession().xincoClientRepository.treemodel.insertNodeInto(temp_node,
                                            explorer.getSession().currentTreeNodeSelection,
                                            explorer.getSession().currentTreeNodeSelection.getChildCount());
                                    // optimize node size
                                    ((XincoCoreNode) temp_node.getUserObject()).setXinco_core_nodes(new Vector());
                                    ((XincoCoreNode) temp_node.getUserObject()).setXinco_core_data(new Vector());
                                    if (explorer.getSession().xinco.setXincoCoreNode((XincoCoreNode) temp_node.getUserObject(),
                                            explorer.getSession().user) ==
                                            null) {
                                        throw new XincoException(xerb.getString("error.nowritepermission"));
                                    }
                                    // update transaction info
                                    explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movefoldersuccess"));
                                } catch (Exception rmie) {
                                    // undo modification
                                    ((XincoCoreNode) temp_node.getUserObject()).setXinco_core_node_id(old_parent_node_id);
                                    JOptionPane.showMessageDialog(explorer,
                                            xerb.getString("error.movefolderfailed") +
                                            " " +
                                            xerb.getString("general.reason") +
                                            ": " +
                                            rmie.toString(),
                                            xerb.getString("general.error"),
                                            JOptionPane.WARNING_MESSAGE);
                                    break;
                                }
                            }
                            // paste data
                            if (temp_node.getUserObject().getClass() ==
                                    XincoCoreData.class) {
                                // modify moved data
                                old_parent_node_id = ((XincoCoreData) temp_node.getUserObject()).getXinco_core_node_id();
                                ((XincoCoreData) temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getSession().currentTreeNodeSelection.getUserObject()).getId());
                                try {
                                    // modify treemodel
                                    explorer.getSession().xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
                                    explorer.getSession().xincoClientRepository.treemodel.insertNodeInto(temp_node,
                                            explorer.getSession().currentTreeNodeSelection,
                                            explorer.getSession().currentTreeNodeSelection.getChildCount());
                                    if (explorer.getSession().xinco.setXincoCoreData((XincoCoreData) temp_node.getUserObject(),
                                            explorer.getSession().user) ==
                                            null) {
                                        throw new XincoException(xerb.getString("error.nowritepermission"));
                                    }
                                    // insert log
                                    try {
                                        XincoCoreLog newlog = new XincoCoreLog();
                                        Vector oldlogs = ((XincoCoreData) temp_node.getUserObject()).getXinco_core_logs();
                                        
                                        newlog.setXinco_core_data_id(((XincoCoreData) temp_node.getUserObject()).getId());
                                        newlog.setXinco_core_user_id(explorer.getSession().user.getId());
                                        newlog.setOp_code(2);
                                        newlog.setOp_description(xerb.getString("menu.edit.movedtofolder") +
                                                " " +
                                                ((XincoCoreNode) explorer.getSession().currentTreeNodeSelection.getUserObject()).getDesignation() +
                                                " (" +
                                                ((XincoCoreNode) explorer.getSession().currentTreeNodeSelection.getUserObject()).getId() +
                                                ") " +
                                                xerb.getString("general.by") +
                                                " " +
                                                explorer.getSession().user.getUsername());
                                        newlog.setOp_datetime(null);
                                        newlog.setVersion(((XincoCoreLog) oldlogs.elementAt(oldlogs.size() -
                                                1)).getVersion());
                                        explorer.getSession().xinco.setXincoCoreLog(newlog,
                                                explorer.getSession().user);
                                    } catch (Exception loge) {
                                    }
                                    // update transaction info
                                    explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movedatasuccess"));
                                } catch (Exception rmie) {
                                    // undo modification
                                    ((XincoCoreData) temp_node.getUserObject()).setXinco_core_node_id(old_parent_node_id);
                                    JOptionPane.showMessageDialog(explorer,
                                            xerb.getString("error.movedatafailed") +
                                            " " +
                                            xerb.getString("general.reason") +
                                            ": " +
                                            rmie.toString(),
                                            xerb.getString("general.error"),
                                            JOptionPane.WARNING_MESSAGE);
                                    break;
                                }
                            }
                        }
                        // remove moved element from clipboard
                        explorer.getSession().clipboardTreeNodeSelection.removeElementAt(0);
                    }
                }
            }
        });
        add(this.items[counter]);
        //add item
        addSeparator();
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.repository.viewurl"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(8);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.repository.emailcontact"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(9);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.repository.downloadfile"));
        this.items[counter].setEnabled(false);
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(7);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.checkoutfile"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(4);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.undocheckout"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(5);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.checkinfile"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(6);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.publishdata"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(10);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.lockdata"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(12);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.downloadrevision"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(11);
            }
        });
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.edit.commentdata"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(13);
            }
        });
        add(this.items[counter]);
        //add item
        addSeparator();
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem();
        this.items[counter].setText(xerb.getString("menu.repository.audit"));
        this.items[counter].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.KeyEvent.ALT_MASK));
        this.items[counter].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.getJDialogAudit();
            }
        });
        add(this.items[counter]);
    }
    public void resetItems(){
        for(int i=1;i<this.items.length;i++){
            if(this.items[i]!=null)
                this.itemSetEnable(i,false);
        }
    }
    public void itemSetEnable(int number,boolean enable){
        items[number].setEnabled(enable);
    }
    
    private int increaseItemNumber(){
        counter++;
        return counter;
    }
    
    public JMenuItem[] getItems(){
        return this.items;
    }
}
