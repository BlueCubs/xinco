/*
 * XincoMenuRepository.java
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
public class XincoMenuRepository extends JPopupMenu{
    public JMenuItem tmi = null;
    public XincoExplorer explorer;
    public JMenuItem RepositoryAddData,AddDataStructure,
            ViewURL,EmailContact,
            EditFolderData,CheckoutData,
            UndoCheckoutData,CheckinData,
            PublishData,LockData,
            DownloadRevision,Refresh,
            AddFolder,ViewEditAddAttributes,
            EditFolderDataACL,MoveFolderData,
            InsertFolderData,ViewData,
            CommentData;
    public ResourceBundle xerb;
    
    
    /** Default Constructor XincoMenuRepository */
    public XincoMenuRepository() {  
    }
    
    /** Creates a new instance of XincoMenuRepository */
    public XincoMenuRepository(XincoExplorer explorer) {
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
        this.explorer=explorer;
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryRefresh().getText());
        tmi.setEnabled(getJMenuItemRepositoryRefresh().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryRefresh().doClick();
            }
        });
        add(tmi);
        //add item
        addSeparator();
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryAddFolder().getText());
        tmi.setEnabled(getJMenuItemRepositoryAddFolder().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryAddFolder().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryAddData().getText());
        tmi.setEnabled(getJMenuItemRepositoryAddData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryAddData().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryAddDataStructure().getText());
        tmi.setEnabled(getJMenuItemRepositoryAddDataStructure().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryAddDataStructure().doClick();
            }
        });
        add(tmi);
        //add item
        addSeparator();
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryEditFolderData().getText());
        tmi.setEnabled(getJMenuItemRepositoryEditFolderData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryEditFolderData().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryViewEditAddAttributes().getText());
        tmi.setEnabled(getJMenuItemRepositoryViewEditAddAttributes().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryViewEditAddAttributes().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryEditFolderDataACL().getText());
        tmi.setEnabled(getJMenuItemRepositoryEditFolderDataACL().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryEditFolderDataACL().doClick();
            }
        });
        add(tmi);
        //add item
        addSeparator();
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryMoveFolderData().getText());
        tmi.setEnabled(getJMenuItemRepositoryMoveFolderData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryMoveFolderData().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryInsertFolderData().getText());
        tmi.setEnabled(getJMenuItemRepositoryInsertFolderData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryInsertFolderData().doClick();
            }
        });
        add(tmi);
        //add item
        addSeparator();
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryViewURL().getText());
        tmi.setEnabled(getJMenuItemRepositoryViewURL().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryViewURL().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryEmailContact().getText());
        tmi.setEnabled(getJMenuItemRepositoryEmailContact().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryEmailContact().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryViewData().getText());
        tmi.setEnabled(getJMenuItemRepositoryViewData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryViewData().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryCheckoutData().getText());
        tmi.setEnabled(getJMenuItemRepositoryCheckoutData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryCheckoutData().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryUndoCheckoutData().getText());
        tmi.setEnabled(getJMenuItemRepositoryUndoCheckoutData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryUndoCheckoutData().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryCheckinData().getText());
        tmi.setEnabled(getJMenuItemRepositoryCheckinData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryCheckinData().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryPublishData().getText());
        tmi.setEnabled(getJMenuItemRepositoryPublishData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryPublishData().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryLockData().getText());
        tmi.setEnabled(getJMenuItemRepositoryLockData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryLockData().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryDownloadRevision().getText());
        tmi.setEnabled(getJMenuItemRepositoryDownloadRevision().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryDownloadRevision().doClick();
            }
        });
        add(tmi);
        //add item
        tmi = new JMenuItem();
        tmi.setText(getJMenuItemRepositoryCommentData().getText());
        tmi.setEnabled(getJMenuItemRepositoryCommentData().isEnabled());
        tmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                getJMenuItemRepositoryCommentData().doClick();
            }
        });
        add(tmi);
    }
    /**
     * This method initializes RepositoryAddData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryAddData() {
        if(RepositoryAddData == null) {
            RepositoryAddData = new javax.swing.JMenuItem();
            RepositoryAddData.setText(xerb.getString("menu.repository.adddata"));
            RepositoryAddData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.KeyEvent.ALT_MASK));
            RepositoryAddData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //data wizard -> add new data object
                    explorer.doDataWizard(1);
                }
            });
        }
        return RepositoryAddData;
    }
    /**
     * This method initializes AddDataStructure
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public JMenuItem getJMenuItemRepositoryAddDataStructure() {
        if (AddDataStructure == null) {
            AddDataStructure = new JMenuItem();
            AddDataStructure.setText(xerb.getString("menu.repository.adddatastructure"));
            AddDataStructure.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.KeyEvent.ALT_MASK));
            AddDataStructure.addActionListener(new java.awt.event.ActionListener() {
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
        }
        return AddDataStructure;
    }
    /**
     * This method initializes ViewURL
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public JMenuItem getJMenuItemRepositoryViewURL() {
        if (ViewURL == null) {
            ViewURL = new JMenuItem();
            ViewURL.setText(xerb.getString("menu.repository.viewurl"));
            ViewURL.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.KeyEvent.ALT_MASK));
            ViewURL.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(8);
                }
            });
        }
        return ViewURL;
    }
    /**
     * This method initializes EmailContact
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public JMenuItem getJMenuItemRepositoryEmailContact() {
        if (EmailContact == null) {
            EmailContact = new JMenuItem();
            EmailContact.setText(xerb.getString("menu.repository.emailcontact"));
            EmailContact.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.KeyEvent.ALT_MASK));
            EmailContact.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(9);
                }
            });
        }
        return EmailContact;
    }
    /**
     * This method initializes EditFolderData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryEditFolderData() {
        if(EditFolderData == null) {
            EditFolderData = new javax.swing.JMenuItem();
            EditFolderData.setText(xerb.getString("menu.edit.folderdata"));
            EditFolderData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.KeyEvent.ALT_MASK));
            EditFolderData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (explorer.getSession().currentTreeNodeSelection != null) {
                        if (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                            //open folder dialog
                            explorer.getJDialogFolder();
//                            jDialogFolder.setVisible(true);
                        }
                        if (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                            //data wizard -> edit data object
                            explorer.doDataWizard(2);
                        }
                    }
                }
            });
        }
        return EditFolderData;
    }
    /**
     * This method initializes CheckoutData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryCheckoutData() {
        if(CheckoutData == null) {
            CheckoutData = new javax.swing.JMenuItem();
            CheckoutData.setText(xerb.getString("menu.edit.checkoutfile"));
            CheckoutData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.KeyEvent.ALT_MASK));
            CheckoutData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(4);
                }
            });
        }
        return CheckoutData;
    }
    /**
     * This method initializes UndoCheckoutData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryUndoCheckoutData() {
        if(UndoCheckoutData == null) {
            UndoCheckoutData = new javax.swing.JMenuItem();
            UndoCheckoutData.setText(xerb.getString("menu.edit.undocheckout"));
            UndoCheckoutData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.KeyEvent.ALT_MASK));
            UndoCheckoutData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(5);
                }
            });
        }
        return UndoCheckoutData;
    }
    /**
     * This method initializes CheckinData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryCheckinData() {
        if(CheckinData == null) {
            CheckinData = new javax.swing.JMenuItem();
            CheckinData.setText(xerb.getString("menu.edit.checkinfile"));
            CheckinData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.KeyEvent.ALT_MASK));
            CheckinData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(6);
                }
            });
        }
        return CheckinData;
    }
    /**
     * This method initializes PublishData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryPublishData() {
        if(PublishData == null) {
            PublishData = new javax.swing.JMenuItem();
            PublishData.setText(xerb.getString("menu.edit.publishdata"));
            PublishData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.KeyEvent.ALT_MASK));
            PublishData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(10);
                }
            });
        }
        return PublishData;
    }
    /**
     * This method initializes LockData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryLockData() {
        if(LockData == null) {
            LockData = new javax.swing.JMenuItem();
            LockData.setText(xerb.getString("menu.edit.lockdata"));
            LockData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.KeyEvent.ALT_MASK));
            LockData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(12);
                }
            });
        }
        return LockData;
    }
    /**
     * This method initializes DownloadRevision
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryDownloadRevision() {
        if(DownloadRevision == null) {
            DownloadRevision = new javax.swing.JMenuItem();
            DownloadRevision.setText(xerb.getString("menu.edit.downloadrevision"));
            DownloadRevision.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.KeyEvent.ALT_MASK));
            DownloadRevision.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(11);
                }
            });
        }
        return DownloadRevision;
    }
    /**
     * This method initializes Refresh
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryRefresh() {
        if(Refresh == null) {
            Refresh = new javax.swing.JMenuItem();
            Refresh.setText(xerb.getString("menu.repository.refresh"));
            Refresh.setName("Refresh");
            Refresh.setEnabled(true);
            Refresh.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
            Refresh.addActionListener(new java.awt.event.ActionListener() {
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
        }
        return Refresh;
    }
    /**
     * This method initializes AddFolder
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryAddFolder() {
        if(AddFolder == null) {
            AddFolder = new javax.swing.JMenuItem();
            AddFolder.setText(xerb.getString("menu.repository.addfolder"));
            AddFolder.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.KeyEvent.ALT_MASK));
            AddFolder.addActionListener(new java.awt.event.ActionListener() {
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
//                            jDialogFolder.setVisible(true);
                            //update treemodel
                            explorer.getSession().xincoClientRepository.treemodel.reload(explorer.getSession().currentTreeNodeSelection);
                            explorer.getSession().xincoClientRepository.treemodel.nodeChanged(explorer.getSession().currentTreeNodeSelection);
                        }
                    }
                }
            });
        }
        return AddFolder;
    }
     /**
     * This method initializes ViewEditAddAttributes
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryViewEditAddAttributes() {
        if(ViewEditAddAttributes == null) {
            ViewEditAddAttributes = new javax.swing.JMenuItem();
            ViewEditAddAttributes.setText(xerb.getString("menu.repository.vieweditaddattributes"));
            ViewEditAddAttributes.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.KeyEvent.ALT_MASK));
            ViewEditAddAttributes.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (explorer.getSession().currentTreeNodeSelection != null) {
                        if (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreData.class) {
                            //data wizard -> edit add attributes
                            explorer.doDataWizard(3);
                        }
                    }
                }
            });
        }
        return ViewEditAddAttributes;
    }
       /**
     * This method initializes EditFolderDataACL
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryEditFolderDataACL() {
        if(EditFolderDataACL == null) {
            EditFolderDataACL = new javax.swing.JMenuItem();
            EditFolderDataACL.setText(xerb.getString("menu.edit.acl"));
            EditFolderDataACL.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.KeyEvent.ALT_MASK));
            EditFolderDataACL.addActionListener(new java.awt.event.ActionListener() {
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
        }
        return EditFolderDataACL;
    }
    /**
     * This method initializes MoveFolderData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryMoveFolderData() {
        if(MoveFolderData == null) {
            MoveFolderData = new javax.swing.JMenuItem();
            MoveFolderData.setText(xerb.getString("menu.edit.movefolderdatatoclipboard"));
            MoveFolderData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.KeyEvent.ALT_MASK));
            MoveFolderData.addActionListener(new java.awt.event.ActionListener() {
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
        }
        return MoveFolderData;
    }
    /**
     * This method initializes InsertFolderData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryInsertFolderData() {
        if(InsertFolderData == null) {
            InsertFolderData = new javax.swing.JMenuItem();
            InsertFolderData.setText(xerb.getString("menu.edit.insertfolderdata"));
            InsertFolderData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.KeyEvent.ALT_MASK));
            InsertFolderData.addActionListener(new java.awt.event.ActionListener() {
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
        }
        return InsertFolderData;
    }
    /**
     * This method initializes ViewData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public javax.swing.JMenuItem getJMenuItemRepositoryViewData() {
        if(ViewData == null) {
            ViewData = new javax.swing.JMenuItem();
            ViewData.setText(xerb.getString("menu.repository.downloadfile"));
            ViewData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.KeyEvent.ALT_MASK));
            ViewData.setEnabled(false);
            ViewData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(7);
                }
            });
        }
        return ViewData;
    }
     /**
     * This method initializes CommentData
     * 
     * 
     * @return javax.swing.JMenuItem
     */
    public JMenuItem getJMenuItemRepositoryCommentData() {
        if (CommentData == null) {
            CommentData = new JMenuItem();
            CommentData.setText(xerb.getString("menu.edit.commentdata"));
            CommentData.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.KeyEvent.ALT_MASK));
            CommentData.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    explorer.doDataWizard(13);
                }
            });
        }
        return CommentData;
    }
}
