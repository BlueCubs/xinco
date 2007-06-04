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
 * Name:            XincoActionHandler
 *
 * Description:     XincoActionHandler
 *
 * Original Author: Javier A. Ortiz
 * Date:            February 23, 2007, 3:44 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *
 *************************************************************
 */

package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.XincoMutableTreeNode;
import com.bluecubs.xinco.client.dialogs.ACLDialog;
import com.bluecubs.xinco.client.object.thread.XincoImportThread;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreLanguage;
import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoException;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoRepositoryActionHandler {
    private ResourceBundle xerb;
    private XincoExplorer explorer=null;
    private int count=0;
    private Action[] actions=null;
    /** Creates a new instance of XincoActionHandler */
    public XincoRepositoryActionHandler(XincoExplorer e) {
        this.explorer=e;
        this.xerb=e.getResourceBundle();
        //Same size as menu Items
        actions=new Action[e.getActionSize()];
        //0
        actions[count]=new Refresh(xerb.getString("menu.repository.refresh"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.refresh"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        increaseCount();
        //1
        actions[count]=new AddFolder(xerb.getString("menu.repository.addfolder"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.addfolder"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //2
        actions[count]=new AddData(xerb.getString("menu.repository.adddata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.adddata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //3
        actions[count]=new AddDataStructure(xerb.getString("menu.repository.adddatastructure"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.adddatastructure"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //4
        actions[count]=new EditFolderData(xerb.getString("menu.edit.folderdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.folderdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //5
        actions[count]=new ViewEditDataAttributes(xerb.getString("menu.repository.vieweditaddattributes"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.vieweditaddattributes"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //6
        actions[count]=new EditACL(xerb.getString("menu.edit.acl"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.acl"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //7
        actions[count]=new MoveToClipboard(xerb.getString("menu.edit.movefolderdatatoclipboard"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.movefolderdatatoclipboard"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //8
        actions[count]=new InsertFolderData(xerb.getString("menu.edit.insertfolderdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.insertfolderdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //9
        actions[count]=new ViewURL(xerb.getString("menu.repository.viewurl"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.viewurl"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //10
        actions[count]=new EmailContact(xerb.getString("menu.repository.emailcontact"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.emailcontact"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //11
        actions[count]=new DownloadFile(xerb.getString("menu.repository.downloadfile"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.downloadfile"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //12
        actions[count]=new CheckoutFile(xerb.getString("menu.edit.checkoutfile"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.checkoutfile"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //13
        actions[count]=new UndoCheckoutFile(xerb.getString("menu.edit.undocheckout"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.undocheckout"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //14
        actions[count]=new CheckinFile(xerb.getString("menu.edit.checkinfile"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.checkinfile"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //15
        actions[count]=new PublishData(xerb.getString("menu.edit.publishdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.publishdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //16
        actions[count]=new LockData(xerb.getString("menu.edit.lockdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.lockdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //17
        actions[count]=new DownloadRevision(xerb.getString("menu.edit.downloadrevision"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.downloadrevision"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //18
        actions[count]=new CommentData(xerb.getString("menu.edit.commentdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.commentdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //19
        actions[count]=new AuditSettings(xerb.getString("menu.repository.audit"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.audit"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.KeyEvent.ALT_MASK));
    }
    
    private void increaseCount(){
        count++;
    }
    
    public class Refresh extends AbstractAction {
        public Refresh(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.refreshJTree();
        }
    }
    
    public class AddFolder extends AbstractAction {
        public AddFolder(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            
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
    }
    public class AddData extends AbstractAction {
        public AddData(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            //data wizard -> add new data object
            explorer.doDataWizard(1);
        }
    }
    public class AddDataStructure extends AbstractAction {
        public AddDataStructure(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            XincoImportThread importT = new XincoImportThread();
            importT.setXincoExplorer(explorer);
            importT.start();
        }
    }
    public class EditFolderData extends AbstractAction {
        public EditFolderData(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
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
    }
    public class ViewEditDataAttributes extends AbstractAction {
        public ViewEditDataAttributes(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            //data wizard -> edit add attributes
            explorer.doDataWizard(3);
        }
    }
    public class EditACL extends AbstractAction {
        public EditACL(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
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
    }
    public class MoveToClipboard extends AbstractAction {
        public MoveToClipboard(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            moveToClipboard();
            //update transaction info
            explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movemessage"));
        }
    }
    public class InsertFolderData extends AbstractAction {
        public InsertFolderData(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            insertNode(false);
        }
    }
    public class ViewURL extends AbstractAction {
        public ViewURL(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(8);
        }
    }
    public class EmailContact extends AbstractAction {
        public EmailContact(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(9);
        }
    }
    public class DownloadFile extends AbstractAction {
        public DownloadFile(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(7);
        }
    }
    public class CheckoutFile extends AbstractAction {
        public CheckoutFile(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(4);
        }
    }
    public class UndoCheckoutFile extends AbstractAction {
        public UndoCheckoutFile(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(5);
        }
    }
    public class CheckinFile extends AbstractAction {
        public CheckinFile(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(6);
        }
    }
    public class PublishData extends AbstractAction {
        public PublishData(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(10);
        }
    }
    public class LockData extends AbstractAction {
        public LockData(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(12);
        }
    }
    public class DownloadRevision extends AbstractAction {
        public DownloadRevision(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(11);
        }
    }
    public class CommentData extends AbstractAction {
        public CommentData(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(13);
        }
    }
    public class AuditSettings extends AbstractAction {
        public AuditSettings(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY,key);
        }
        public void actionPerformed(ActionEvent e){
            explorer.doDataWizard(15);
        }
    }
    
    public Action[] getActions() {
        return actions;
    }
    
    public void moveToClipboard(){
        int i;
        TreePath[] tps;
        //cut node
        tps =explorer.jTreeRepository.getSelectionPaths();
        explorer.getSession().clipboardTreeNodeSelection.removeAllElements();
        for (i=0;i<explorer.jTreeRepository.getSelectionCount();i++) {
            explorer.getSession().clipboardTreeNodeSelection.addElement(tps[i].getLastPathComponent());
        }
        if(explorer.getSettings().getSetting("general.setting.enable.developermode").isBool_value()){
            System.out.println("Items copied to the clip board: "+(i));
        }
    }
    
    public void insertNode(boolean dragNdrop){
        if (((explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) &&
                !dragNdrop) ||
                ((explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) &&
                dragNdrop) &&
                (explorer.getSession().clipboardTreeNodeSelection != null)) {
            int old_parent_node_id = 0;
            int i;
            int cb_size;
            XincoMutableTreeNode temp_node;
            cb_size = explorer.getSession().clipboardTreeNodeSelection.size();
            for (i = 0; i < cb_size; i++) {
                temp_node = (XincoMutableTreeNode) explorer.getSession().clipboardTreeNodeSelection.elementAt(0);
                if (explorer.getSession().currentTreeNodeSelection != temp_node && !dragNdrop || dragNdrop) {
                    // paste node
                    if (temp_node.getUserObject().getClass() == XincoCoreNode.class) {
                        // modify moved node
                        old_parent_node_id = ((XincoCoreNode) temp_node.getUserObject()).getXinco_core_node_id();
                        try {
                            // modify treemodel
                            explorer.getSession().xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
                            //For some reason removing the node from parent changes the xinco core node id to was it was. Modify node afterwards.
                            if(!dragNdrop && (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class)){
                                ((XincoCoreNode) temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getSession().currentTreeNodeSelection.getUserObject()).getId());
                            }else if(dragNdrop && (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class)){
                                ((XincoCoreNode) temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getId());
                            }
                            if(!dragNdrop){
                                explorer.getSession().xincoClientRepository.treemodel.insertNodeInto(temp_node,
                                        explorer.getSession().currentTreeNodeSelection,
                                        explorer.getSession().currentTreeNodeSelection.getChildCount());
                            } else{
                                explorer.getSession().xincoClientRepository.treemodel.insertNodeInto(temp_node,
                                        explorer.getJTreeRepository().getTargetTreeNode(),
                                        explorer.getJTreeRepository().getTargetTreeNode().getChildCount());
                            }
                            // optimize node size
                            ((XincoCoreNode) temp_node.getUserObject()).setXinco_core_nodes(new Vector());
                            ((XincoCoreNode) temp_node.getUserObject()).setXinco_core_data(new Vector());
                            if (explorer.getSession().xinco.setXincoCoreNode((XincoCoreNode) temp_node.getUserObject(),
                                    explorer.getSession().user) == null) {
                                throw new XincoException(xerb.getString("error.nowritepermission"));
                            }
                            // update transaction info
                            explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movefoldersuccess"));
                        } catch (Exception rmie) {
                            if(explorer.getSettings().getSetting("general.setting.enable.developermode").isBool_value())
                                rmie.printStackTrace();
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
                    if (temp_node.getUserObject().getClass() == XincoCoreData.class) {
                        // modify moved data
                        if(!dragNdrop)
                            old_parent_node_id = ((XincoCoreData) temp_node.getUserObject()).getXinco_core_node_id();
                        if(dragNdrop){
                            if(explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject().getClass()== XincoCoreData.class)
                                old_parent_node_id = ((XincoCoreData) explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject()).getXinco_core_node_id();
                            if(explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject().getClass()== XincoCoreNode.class)
                                old_parent_node_id = ((XincoCoreNode) explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject()).getXinco_core_node_id();
                        }
                        if(!dragNdrop)
                            ((XincoCoreData) temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getSession().currentTreeNodeSelection.getUserObject()).getId());
                        if(dragNdrop)
                            ((XincoCoreData) temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getId());
                        try {
                            // modify treemodel
                            explorer.getSession().xincoClientRepository.treemodel.removeNodeFromParent(temp_node);
                            if(!dragNdrop){
                                explorer.getSession().xincoClientRepository.treemodel.insertNodeInto(temp_node,
                                        explorer.getSession().currentTreeNodeSelection,
                                        explorer.getSession().currentTreeNodeSelection.getChildCount());
                            }
                            if(dragNdrop){
                                //For some reason removing the node from parent changes the xinco core node id to was it was. Modify node afterwards.
                                ((XincoCoreData) temp_node.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getId());
                                explorer.getSession().xincoClientRepository.treemodel.insertNodeInto(temp_node,
                                        explorer.getJTreeRepository().getTargetTreeNode(),
                                        explorer.getJTreeRepository().getTargetTreeNode().getChildCount());
                            }
                            if (explorer.getSession().xinco.setXincoCoreData((XincoCoreData) temp_node.getUserObject(),
                                    explorer.getSession().user) == null) {
                                throw new XincoException(xerb.getString("error.nowritepermission"));
                            }
                            // insert log
                            try {
                                XincoCoreLog newlog = new XincoCoreLog();
                                Vector oldlogs = ((XincoCoreData) temp_node.getUserObject()).getXinco_core_logs();
                                
                                newlog.setXinco_core_data_id(((XincoCoreData) temp_node.getUserObject()).getId());
                                newlog.setXinco_core_user_id(explorer.getSession().user.getId());
                                newlog.setOp_code(2);
                                if(!dragNdrop){
                                    newlog.setOp_description(xerb.getString("menu.edit.movedtofolder") +" " +
                                            ((XincoCoreNode) explorer.getSession().currentTreeNodeSelection.getUserObject()).getDesignation() +" (" +
                                            ((XincoCoreNode) explorer.getSession().currentTreeNodeSelection.getUserObject()).getId() +") " +
                                            xerb.getString("general.by") +" " +
                                            explorer.getSession().user.getUsername());
                                } else{
                                    newlog.setOp_description(xerb.getString("menu.edit.movedtofolder") +" " +
                                            ((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getDesignation() +" (" +
                                            ((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getId() +") " +
                                            xerb.getString("general.by") +" " +
                                            explorer.getSession().user.getUsername());
                                }
                                newlog.setOp_datetime(null);
                                newlog.setVersion(((XincoCoreLog) oldlogs.elementAt(oldlogs.size() - 1)).getVersion());
                                explorer.getSession().xinco.setXincoCoreLog(newlog,
                                        explorer.getSession().user);
                            } catch (Exception loge) {
                                if(explorer.getSettings().getSetting("general.setting.enable.developermode").isBool_value())
                                    loge.printStackTrace();
                            }
                            // update transaction info
                            explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movedatasuccess"));
                        } catch (Exception rmie) {
                            if(explorer.getSettings().getSetting("general.setting.enable.developermode").isBool_value())
                                rmie.printStackTrace();
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
            
        }else{
            //Display only if in developer mode!
            if(explorer.getSettings().getSetting("general.setting.enable.developermode").isBool_value()){
                System.err.println("Don't execute change in DB!");
                System.err.println(((explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) &&
                        dragNdrop));
                System.err.println(((explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) &&
                        dragNdrop));
                System.err.println((explorer.getSession().clipboardTreeNodeSelection != null));
            }
        }
    }
}
