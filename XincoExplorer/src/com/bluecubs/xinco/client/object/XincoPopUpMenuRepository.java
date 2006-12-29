/*
 * XincoMenuPopUpRepository.java
 *
 * Created on December 11, 2006, 2:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
import java.io.File;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author ortizbj
 */
public class XincoPopUpMenuRepository extends JPopupMenu{
    public JMenuItem tmi = null;
    public XincoExplorer explorer=null;
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
    private JMenuItem [] items = new JMenuItem[19];
    public ResourceBundle xerb;
    
    public void itemSetEnable(int number,boolean enable){
        items[number].setEnabled(enable);
    }
    
    /**
     * Creates a new instance of XincoMenuPopUpRepository
     */
    public XincoPopUpMenuRepository(final XincoExplorer explorer) {
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
        this.explorer=explorer;
        //add item
        this.items[0] = new JMenuItem();
        this.items[0].setText(xerb.getString("menu.repository.refresh"));
        this.items[0].setEnabled(true);
        this.items[0].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        this.items[0].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    //get root
                    XincoCoreNode xnode = new XincoCoreNode();
                    xnode.setId(1);
                    xnode = explorer.getSession().xinco.getXincoCoreNode(xnode, explorer.getSession().user);
                    explorer.getSession().xincoClientRepository.assignObject2TreeNode((XincoMutableTreeNode)((DefaultTreeModel)explorer.getSession().xincoClientRepository.treemodel).getRoot(), xnode, explorer.getSession().xinco, explorer.getSession().user, 2);
                    explorer.jTreeRepository.expandPath(new TreePath(explorer.getSession().xincoClientRepository.treemodel.getPathToRoot(((XincoMutableTreeNode)((DefaultTreeModel)explorer.getSession().xincoClientRepository.treemodel).getRoot()))));
                } catch (Exception rmie) {
                }
            }
        });
        add(this.items[0]);
        
        //add item
        addSeparator();
        
        //add item
        this.items[1] = new JMenuItem();
        this.items[1].setText(xerb.getString("menu.repository.addfolder"));
        this.items[1].setEnabled(true);
        this.items[1].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.KeyEvent.ALT_MASK));
        this.items[1].addActionListener(new java.awt.event.ActionListener() {
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
        add(this.items[1]);
        
        //add item
        this.items[2] = new JMenuItem();
        this.items[2].setText(xerb.getString("menu.repository.adddata"));
        this.items[2].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.KeyEvent.ALT_MASK));
        this.items[2].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //data wizard -> add new data object
                explorer.doDataWizard(1);
            }
        });
        add(this.items[2]);
        
        //add item
        this.items[3] = new JMenuItem();
        this.items[3].setText(xerb.getString("menu.repository.adddatastructure"));
        this.items[3].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.KeyEvent.ALT_MASK));
        this.items[3].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //import data structure
                if (explorer.getSession().currentTreeNodeSelection != null) {
                    if (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                        JOptionPane.showMessageDialog(explorer, xerb.getString("window.massiveimport.info"), xerb.getString("window.massiveimport"), JOptionPane.INFORMATION_MESSAGE);
                        try {
                            JFileChooser fc = new JFileChooser();
                            fc.setCurrentDirectory(new File(explorer.current_path));
                            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                            //show dialog
                            int result = fc.showOpenDialog(explorer);
                            if(result == JFileChooser.APPROVE_OPTION) {
                                explorer.setCurrentPath(fc.getSelectedFile().toString());
                            } else {
                                throw new XincoException(xerb.getString("datawizard.updatecancel"));
                            }
                            //update transaction info
                            JOptionPane.showMessageDialog(explorer, xerb.getString("window.massiveimport.progress"), xerb.getString("window.massiveimport"), JOptionPane.INFORMATION_MESSAGE);
                            explorer.jLabelInternalFrameInformationText.setText(xerb.getString("window.massiveimport.progress"));
                            explorer.importContentOfFolder((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject(), new File(explorer.current_path));
                            //select current path
                            explorer.jTreeRepository.setSelectionPath(new TreePath(explorer.getSession().currentTreeNodeSelection.getPath()));
                            //update transaction info
                            explorer.jLabelInternalFrameInformationText.setText(xerb.getString("window.massiveimport.importsuccess"));
                        } catch (Exception ie) {
                            JOptionPane.showMessageDialog(explorer, xerb.getString("window.massiveimport.importfailed") +
                                    " " + xerb.getString("general.reason") + ": " + ie.toString(), xerb.getString("general.error"),
                                    JOptionPane.WARNING_MESSAGE);
                            explorer.jLabelInternalFrameInformationText.setText("");
                        }
                    }
                }
            }
        });
        add(this.items[3]);
        
        //add item
        addSeparator();
        
        //add item
        this.items[4] = new JMenuItem();
        this.items[4].setText(xerb.getString("menu.edit.folderdata"));
        this.items[4].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.KeyEvent.ALT_MASK));
        this.items[4].addActionListener(
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
        add(this.items[4]);
        
        //add item
        this.items[5] = new JMenuItem();
        this.items[5].setText(xerb.getString("menu.repository.vieweditaddattributes"));
        this.items[5].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.KeyEvent.ALT_MASK));
        this.items[5].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //data wizard -> edit add attributes
                explorer.doDataWizard(3);
            }
        });
        add(this.items[5]);
        
        //add item
        this.items[6] = new JMenuItem();
        this.items[6].setText(xerb.getString("menu.edit.acl"));
        this.items[6].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.KeyEvent.ALT_MASK));
        this.items[6].addActionListener(new java.awt.event.ActionListener() {
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
        add(this.items[6]);
        
        //add item
        addSeparator();
        
        //add item
        this.items[7] = new JMenuItem();
        this.items[7].setText(xerb.getString("menu.edit.movefolderdatatoclipboard"));
        this.items[7].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.KeyEvent.ALT_MASK));
        this.items[7].addActionListener(new java.awt.event.ActionListener() {
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
        add(this.items[7]);
        
        //add item
        this.items[8] = new JMenuItem();
        this.items[8].setText(xerb.getString("menu.edit.insertfolderdata"));
        this.items[8].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.KeyEvent.ALT_MASK));
        this.items[8].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if ((explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) && (explorer.getSession().clipboardTreeNodeSelection != null)) {
                    int old_parent_node_id = 0;
                    int i;
                    int cb_size;
                    XincoMutableTreeNode temp_node;
                    cb_size = explorer.getSession().clipboardTreeNodeSelection.size();
                    for (i=0;i<cb_size;i++) {
                        temp_node = (XincoMutableTreeNode)explorer.getSession().clipboardTreeNodeSelection.elementAt(0);
                        if (explorer.getSession().currentTreeNodeSelection != temp_node) {
                            //paste node
                            if (temp_node.getUserObject().getClass() == XincoCoreNode.class) {
                                //modify moved node
                                old_parent_node_id = ((XincoCoreNode)temp_node.getUserObject()).getXinco_core_node_id();
                                ((XincoCoreNode)temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId());
                                try {
                                    //modify treemodel
                                    explorer.getSession().xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
                                    explorer.getSession().xincoClientRepository.treemodel.insertNodeInto(temp_node, explorer.getSession().currentTreeNodeSelection, explorer.getSession().currentTreeNodeSelection.getChildCount());
                                    //optimize node size
                                    ((XincoCoreNode)temp_node.getUserObject()).setXinco_core_nodes(new Vector());
                                    ((XincoCoreNode)temp_node.getUserObject()).setXinco_core_data(new Vector());
                                    //invoke web-service
                                    if (explorer.getSession().xinco.setXincoCoreNode((XincoCoreNode)temp_node.getUserObject(), explorer.getSession().user) != null) {
                                        //update transaction info
                                        explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movefoldersuccess"));
                                    } else {
                                        throw new XincoException(xerb.getString("error.nowritepermission"));
                                    }
                                } catch (Exception rmie) {
                                    //undo modification
                                    ((XincoCoreNode)temp_node.getUserObject()).setXinco_core_node_id(old_parent_node_id);
                                    JOptionPane.showMessageDialog(explorer,
                                            xerb.getString("error.movefolderfailed") + " " +
                                            xerb.getString("general.reason") + ": " + rmie.toString(),
                                            xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                                    break;
                                }
                            }
                            //paste data
                            if (temp_node.getUserObject().getClass() == XincoCoreData.class) {
                                //modify moved data
                                old_parent_node_id = ((XincoCoreData)temp_node.getUserObject()).getXinco_core_node_id();
                                ((XincoCoreData)temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId());
                                try {
                                    //modify treemodel
                                    explorer.getSession().xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
                                    explorer.getSession().xincoClientRepository.treemodel.insertNodeInto(temp_node, explorer.getSession().currentTreeNodeSelection, explorer.getSession().currentTreeNodeSelection.getChildCount());
                                    //invoke web-service
                                    if (explorer.getSession().xinco.setXincoCoreData((XincoCoreData)temp_node.getUserObject(), explorer.getSession().user) != null) {
                                        //insert log
                                        try {
                                            XincoCoreLog newlog = new XincoCoreLog();
                                            Vector oldlogs = ((XincoCoreData)temp_node.getUserObject()).getXinco_core_logs();
                                            newlog.setXinco_core_data_id(((XincoCoreData)temp_node.getUserObject()).getId());
                                            newlog.setXinco_core_user_id(explorer.getSession().user.getId());
                                            newlog.setOp_code(2);
                                            newlog.setOp_description(xerb.getString("menu.edit.movedtofolder") + " " + ((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getDesignation() + " (" + ((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject()).getId() + ") " + xerb.getString("general.by") + " " + explorer.getSession().user.getUsername());
                                            newlog.setOp_datetime(null);
                                            newlog.setVersion(((XincoCoreLog)oldlogs.elementAt(oldlogs.size()-1)).getVersion());
                                            explorer.getSession().xinco.setXincoCoreLog(newlog, explorer.getSession().user);
                                        } catch (Exception loge) {
                                        }
                                        //update transaction info
                                        explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movedatasuccess"));
                                    } else {
                                        throw new XincoException(xerb.getString("error.nowritepermission"));
                                    }
                                } catch (Exception rmie) {
                                    //undo modification
                                    ((XincoCoreData)temp_node.getUserObject()).setXinco_core_node_id(old_parent_node_id);
                                    JOptionPane.showMessageDialog(explorer, xerb.getString("error.movedatafailed") + " " + xerb.getString("general.reason") + ": " + rmie.toString(), xerb.getString("general.error"), JOptionPane.WARNING_MESSAGE);
                                    break;
                                }
                            }
                        }
                        //remove moved element from clipboard
                        explorer.getSession().clipboardTreeNodeSelection.removeElementAt(0);
                    }
                }
            }
        });
        add(this.items[8]);
        
        //add item
        addSeparator();
        
        //add item
        this.items[9] = new JMenuItem();
        this.items[9].setText(xerb.getString("menu.repository.viewurl"));
        this.items[9].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.KeyEvent.ALT_MASK));
        this.items[9].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(8);
            }
        });
        add(this.items[9]);
        
        //add item
        this.items[10] = new JMenuItem();
        this.items[10].setText(xerb.getString("menu.repository.emailcontact"));
        this.items[10].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.KeyEvent.ALT_MASK));
        this.items[10].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(9);
            }
        });
        add(this.items[10]);
        
        //add item
        this.items[11] = new JMenuItem();
        this.items[11].setText(xerb.getString("menu.repository.downloadfile"));
        this.items[11].setEnabled(false);
        this.items[11].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.KeyEvent.ALT_MASK));
        this.items[11].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(7);
            }
        });
        add(this.items[11]);
        
        //add item
        this.items[12] = new JMenuItem();
        this.items[12].setText(xerb.getString("menu.edit.checkoutfile"));
        this.items[12].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.KeyEvent.ALT_MASK));
        this.items[12].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(4);
            }
        });
        add(this.items[12]);
        
        //add item
        this.items[13] = new JMenuItem();
        this.items[13].setText(xerb.getString("menu.edit.undocheckout"));
        this.items[13].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.KeyEvent.ALT_MASK));
        this.items[13].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(5);
            }
        });
        add(this.items[13]);
        
        //add item
        this.items[14] = new JMenuItem();
        this.items[14].setText(xerb.getString("menu.edit.checkinfile"));
        this.items[14].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.KeyEvent.ALT_MASK));
        this.items[14].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(6);
            }
        });
        add(this.items[14]);
        
        //add item
        this.items[15] = new JMenuItem();
        this.items[15].setText(xerb.getString("menu.edit.publishdata"));
        this.items[15].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.KeyEvent.ALT_MASK));
        this.items[15].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(10);
            }
        });
        add(this.items[15]);
        
        //add item
        this.items[16] = new JMenuItem();
        this.items[16].setText(xerb.getString("menu.edit.lockdata"));
        this.items[16].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.KeyEvent.ALT_MASK));
        this.items[16].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(12);
            }
        });
        add(this.items[16]);
        
        //add item
        this.items[17] = new JMenuItem();
        this.items[17].setText(xerb.getString("menu.edit.downloadrevision"));
        this.items[17].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.KeyEvent.ALT_MASK));
        this.items[17].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(11);
            }
        });
        add(this.items[17]);
        
        //add item
        this.items[18] = new JMenuItem();
        this.items[18].setText(xerb.getString("menu.edit.commentdata"));
        this.items[18].setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.KeyEvent.ALT_MASK));
        this.items[18].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                explorer.doDataWizard(13);
            }
        });
        add(this.items[18]);
        
    }
    public void resetItems(){
        for(int i=1;i<this.items.length;i++){
            if(this.items[i]!=null)
                this.itemSetEnable(i,false);
        }
    }
}
