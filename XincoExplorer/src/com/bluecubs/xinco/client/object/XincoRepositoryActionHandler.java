/**
 *Copyright 2010 blueCubs.com
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
 * Date:            February 23, 2010, 3:44 PM
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
import com.bluecubs.xinco.client.dialogs.ACLDialog;
import com.bluecubs.xinco.client.object.abstractObject.AbstractDialog;
import com.bluecubs.xinco.client.object.thread.XincoDownloadThread;
import com.bluecubs.xinco.client.object.thread.XincoImportThread;
import com.bluecubs.xinco.core.XincoCoreData;
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
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoRepositoryActionHandler {

    private ResourceBundle xerb;
    private XincoExplorer explorer = null;
    private int count = 0;
    private Action[] actions = null;

    /** Creates a new instance of XincoActionHandler
     * @param e 
     */
    public XincoRepositoryActionHandler(XincoExplorer e) {
        this.explorer = e;
        this.xerb = e.getResourceBundle();
        //Same size as menu Items
        actions = new Action[e.getActionSize()];
        //0
        actions[count] = new Refresh(xerb.getString("menu.repository.refresh"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.refresh"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        increaseCount();
        //1
        actions[count] = new AddFolder(xerb.getString("menu.repository.addfolder"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.addfolder"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //2
        actions[count] = new AddData(xerb.getString("menu.repository.adddata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.adddata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //3
        actions[count] = new AddDataStructure(xerb.getString("menu.repository.adddatastructure"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.adddatastructure"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //4
        actions[count] = new EditFolderData(xerb.getString("menu.edit.folderdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.folderdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //5
        actions[count] = new ViewEditDataAttributes(xerb.getString("menu.repository.vieweditaddattributes"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.vieweditaddattributes"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //6
        actions[count] = new EditACL(xerb.getString("menu.edit.acl"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.acl"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //7
        actions[count] = new MoveToClipboard(xerb.getString("menu.edit.movefolderdatatoclipboard"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.movefolderdatatoclipboard"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //8
        actions[count] = new InsertFolderData(xerb.getString("menu.edit.insertfolderdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.insertfolderdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //9
        actions[count] = new ViewURL(xerb.getString("menu.repository.viewurl"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.viewurl"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //10
        actions[count] = new EmailContact(xerb.getString("menu.repository.emailcontact"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.emailcontact"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //11
        actions[count] = new DownloadFile(xerb.getString("menu.repository.downloadfile"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.repository.downloadfile"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //12
        actions[count] = new CheckoutFile(xerb.getString("menu.edit.checkoutfile"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.checkoutfile"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //13
        actions[count] = new UndoCheckoutFile(xerb.getString("menu.edit.undocheckout"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.undocheckout"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //14
        actions[count] = new CheckinFile(xerb.getString("menu.edit.checkinfile"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.checkinfile"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //15
        actions[count] = new PublishData(xerb.getString("menu.edit.publishdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.publishdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //16
        actions[count] = new LockData(xerb.getString("menu.edit.lockdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.lockdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //17
        actions[count] = new DownloadRevision(xerb.getString("menu.edit.downloadrevision"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.downloadrevision"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.KeyEvent.ALT_MASK));
        increaseCount();
        //18
        actions[count] = new CommentData(xerb.getString("menu.edit.commentdata"),
                this.explorer.getXincoIcon(),
                xerb.getString("menu.edit.commentdata"),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.KeyEvent.ALT_MASK));
    }

    private void increaseCount() {
        count++;
    }

    public class Refresh extends AbstractAction {

        public Refresh(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
        }

        public void actionPerformed(ActionEvent e) {
            XincoMutableTreeNode newnode;
            //open folder dialog
            if (explorer.getSession().getCurrentTreeNodeSelection() != null) {
                if (explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
                    //set current node to new one
                    newnode = new XincoMutableTreeNode(new XincoCoreNode(), explorer);
                    //set node attributes
                    ((XincoCoreNode) newnode.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getId());
                    ((XincoCoreNode) newnode.getUserObject()).setDesignation(xerb.getString("general.newfolder"));
                    ((XincoCoreNode) newnode.getUserObject()).setXinco_core_language((XincoCoreLanguage) explorer.getSession().getServerLanguages().elementAt(0));
                    ((XincoCoreNode) newnode.getUserObject()).setStatus_number(1);
                    explorer.getSession().getXincoClientRepository().treemodel.insertNodeInto(newnode, explorer.getSession().getCurrentTreeNodeSelection(), explorer.getSession().getCurrentTreeNodeSelection().getChildCount());
                    explorer.getSession().setCurrentTreeNodeSelection(newnode);
                    explorer.getAbstractDialogFolder();
                    //update treemodel
                    explorer.getSession().getXincoClientRepository().treemodel.reload(explorer.getSession().getCurrentTreeNodeSelection());
                    explorer.getSession().getXincoClientRepository().treemodel.nodeChanged(explorer.getSession().getCurrentTreeNodeSelection());
                }
            }
        }
    }

    public class AddData extends AbstractAction {

        public AddData(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
        }

        public void actionPerformed(ActionEvent e) {
            if (explorer.getSession().getCurrentTreeNodeSelection() != null) {
                if (explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
                    //open folder dialog
                    explorer.getAbstractDialogFolder();
                }
                if (explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreData.class) {
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
        }

        public void actionPerformed(ActionEvent e) {
            if (explorer.getSession().getCurrentTreeNodeSelection() != null) {
                //open ACL dialog
                AbstractDialog AbstractDialogACL = explorer.getAbstractDialogACL();
                //fill ACL
                ((ACLDialog) AbstractDialogACL).reloadACLListACL();
            }
        }
    }

    public class MoveToClipboard extends AbstractAction {

        public MoveToClipboard(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
        }

        public void actionPerformed(ActionEvent e) {
            if (explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
                XincoDownloadThread downloadT = new XincoDownloadThread();
                downloadT.setXincoExplorer(explorer);
                downloadT.start();
            } else {
                explorer.doDataWizard(7);
            }
        }
    }

    public class CheckoutFile extends AbstractAction {

        public CheckoutFile(String text, Icon icon,
                String desc, KeyStroke key) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
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
            putValue(ACCELERATOR_KEY, key);
        }

        public void actionPerformed(ActionEvent e) {
            explorer.doDataWizard(15);
        }
    }

    public Action[] getActions() {
        return actions;
    }

    @SuppressWarnings("unchecked")
    public void moveToClipboard() {
        int i;
        TreePath[] tps;
        //cut node
        tps = explorer.getJTreeRepository().getSelectionPaths();
        explorer.getSession().getClipboardTreeNodeSelection().removeAllElements();
        for (i = 0; i < explorer.getJTreeRepository().getSelectionCount(); i++) {
            explorer.getSession().getClipboardTreeNodeSelection().addElement(tps[i].getLastPathComponent());
        }
    }

    /**
     * Insert Node
     * @param dragNdrop
     */
    public void insertNode(boolean dragNdrop) {
        if (((explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class)
                && !dragNdrop)
                || ((explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class)
                && dragNdrop)
                && (explorer.getSession().getClipboardTreeNodeSelection() != null)) {
            int old_parentNodeId = 0;
            int i;
            int cb_size;
            XincoMutableTreeNode tempNode;
            cb_size = explorer.getSession().getClipboardTreeNodeSelection().size();
            for (i = 0; i < cb_size; i++) {
                tempNode = (XincoMutableTreeNode) explorer.getSession().getClipboardTreeNodeSelection().elementAt(0);
                if ((explorer.getSession().getCurrentTreeNodeSelection() != tempNode && !dragNdrop) || dragNdrop) {
                    // paste node
                    if (tempNode.getUserObject().getClass() == XincoCoreNode.class) {
                        // modify moved node
                        old_parentNodeId = ((XincoCoreNode) tempNode.getUserObject()).getXinco_core_node_id();
                        try {
                            // modify treemodel
                            explorer.getSession().getXincoClientRepository().treemodel.removeNodeFromParent(tempNode);
                            //For some reason removing the node from parent changes the xinco core node id to was it was. Modify node afterwards.
                            if (!dragNdrop && (explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class)) {
                                ((XincoCoreNode) tempNode.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getId());
                            } else if (dragNdrop && (explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class)) {
                                ((XincoCoreNode) tempNode.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getId());
                            }
                            if (!dragNdrop) {
                                explorer.getSession().getXincoClientRepository().treemodel.insertNodeInto(tempNode,
                                        explorer.getSession().getCurrentTreeNodeSelection(), explorer.getSession().getCurrentTreeNodeSelection().getChildCount());
                            } else {
                                explorer.getSession().getXincoClientRepository().treemodel.insertNodeInto(tempNode,
                                        explorer.getJTreeRepository().getTargetTreeNode(),
                                        explorer.getJTreeRepository().getTargetTreeNode().getChildCount());
                            }
                            // optimize node size
                            ((XincoCoreNode) tempNode.getUserObject()).setXinco_core_nodes(new Vector());
                            ((XincoCoreNode) tempNode.getUserObject()).setXinco_core_data(new Vector());
                            if (explorer.getSession().getXinco().setXincoCoreNode((XincoCoreNode) tempNode.getUserObject(), explorer.getSession().getUser()) == null) {
                                throw new XincoException(xerb.getString("error.nowritepermission"));
                            }
                            // update transaction info
                            explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movefoldersuccess"));
                        } catch (Exception rmie) {
                            // undo modification
                            ((XincoCoreNode) tempNode.getUserObject()).setXinco_core_node_id(old_parentNodeId);
                            JOptionPane.showMessageDialog(explorer,
                                    xerb.getString("error.movefolderfailed")
                                    + " "
                                    + xerb.getString("general.reason")
                                    + ": "
                                    + rmie.toString(),
                                    xerb.getString("general.error"),
                                    JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                    }
                    // paste data
                    if (tempNode.getUserObject().getClass() == XincoCoreData.class) {
                        // modify moved data
                        if (!dragNdrop) {
                            old_parentNodeId = ((XincoCoreData) tempNode.getUserObject()).getXinco_core_node_id();
                        }
                        if (dragNdrop) {
                            if (explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject().getClass() == XincoCoreData.class) {
                                old_parentNodeId = ((XincoCoreData) explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject()).getXinco_core_node_id();
                            }
                            if (explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject().getClass() == XincoCoreNode.class) {
                                old_parentNodeId = ((XincoCoreNode) explorer.getJTreeRepository().getPreviousTreeNodeSelection().getUserObject()).getXinco_core_node_id();
                            }
                        }
                        if (!dragNdrop) {
                            ((XincoCoreData) tempNode.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getId());
                        }
                        if (dragNdrop) {
                            ((XincoCoreData) tempNode.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getId());
                        }
                        try {
                            // modify treemodel
                            explorer.getSession().getXincoClientRepository().treemodel.removeNodeFromParent(tempNode);
                            if (!dragNdrop) {
                                explorer.getSession().getXincoClientRepository().treemodel.insertNodeInto(tempNode,
                                        explorer.getSession().getCurrentTreeNodeSelection(), explorer.getSession().getCurrentTreeNodeSelection().getChildCount());
                            }
                            if (dragNdrop) {
                                //For some reason removing the node from parent changes the xinco core node id to was it was. Modify node afterwards.
                                ((XincoCoreData) tempNode.getUserObject()).setXinco_core_node_id(((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getId());
                                explorer.getSession().getXincoClientRepository().treemodel.insertNodeInto(tempNode,
                                        explorer.getJTreeRepository().getTargetTreeNode(),
                                        explorer.getJTreeRepository().getTargetTreeNode().getChildCount());
                            }
                            if (explorer.getSession().getXinco().setXincoCoreData((XincoCoreData) tempNode.getUserObject(), explorer.getSession().getUser()) == null) {
                                throw new XincoException(xerb.getString("error.nowritepermission"));
                            }
                            // insert log
                            try {
                                XincoCoreLog newlog = new XincoCoreLog();
                                Vector oldlogs = ((XincoCoreData) tempNode.getUserObject()).getXinco_core_logs();

                                newlog.setXinco_core_data_id(((XincoCoreData) tempNode.getUserObject()).getId());
                                newlog.setXinco_core_user_id(explorer.getSession().getUser().getId());
                                newlog.setOp_code(2);
                                if (!dragNdrop) {
                                    newlog.setOp_description(xerb.getString("menu.edit.movedtofolder") + " "
                                            + ((XincoCoreNode) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getDesignation() + " ("
                                            + ((XincoCoreNode) explorer.getSession().getCurrentTreeNodeSelection().getUserObject()).getId() + ") "
                                            + xerb.getString("general.by") + " "
                                            + explorer.getSession().getUser().getUsername());
                                } else {
                                    newlog.setOp_description(xerb.getString("menu.edit.movedtofolder") + " "
                                            + ((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getDesignation() + " ("
                                            + ((XincoCoreNode) explorer.getJTreeRepository().getTargetTreeNode().getUserObject()).getId() + ") "
                                            + xerb.getString("general.by") + " "
                                            + explorer.getSession().getUser().getUsername());
                                }
                                newlog.setOp_datetime(null);
                                newlog.setVersion(((XincoCoreLog) oldlogs.elementAt(oldlogs.size() - 1)).getVersion());
                                explorer.getSession().getXinco().setXincoCoreLog(newlog, explorer.getSession().getUser());
                            } catch (Exception loge) {
                            }
                            // update transaction info
                            explorer.jLabelInternalFrameInformationText.setText(xerb.getString("menu.edit.movedatasuccess"));
                        } catch (Exception rmie) {

                            // undo modification
                            ((XincoCoreData) tempNode.getUserObject()).setXinco_core_node_id(old_parentNodeId);
                            JOptionPane.showMessageDialog(explorer,
                                    xerb.getString("error.movedatafailed")
                                    + " "
                                    + xerb.getString("general.reason")
                                    + ": "
                                    + rmie.toString(),
                                    xerb.getString("general.error"),
                                    JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                    }
                }
                // remove moved element from clipboard
                explorer.getSession().getClipboardTreeNodeSelection().removeElementAt(0);
            }
        }
    }
}
