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
 * Name:            XincoJTree
 *
 * Description:     Xinco JTree
 *
 * Original Author: Javier A. Ortiz
 * Date:            May 18, 2010, 9:34 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */
package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.object.dragNdrop.XincoDefaultTreeTransferHandler;
import com.bluecubs.xinco.client.object.menu.XincoMenuRepository;
import com.bluecubs.xinco.client.object.menu.XincoPopUpMenuRepository;
import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.client.dialogs.AddAttributeText;
import com.bluecubs.xinco.core.XincoCoreACE;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoDataStatus;
import com.bluecubs.xinco.core.client.XincoCoreACEClient;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Javier A. ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public final class XincoJTree extends JTree {

    private XincoExplorer explorer = null;
    private XincoMutableTreeNode previousTreeNodeSelection = null, targetTreeNode = null;
    //Drag and Drop variables
    private Insets autoscrollInsets = new Insets(20, 20, 20, 20); // insets

    /** Creates a new instance of XincoJTree
     * @param explorer
     */
    public XincoJTree(XincoExplorer explorer) {
        setExplorer(explorer);
        new XincoDefaultTreeTransferHandler(this, DnDConstants.ACTION_COPY_OR_MOVE);
        this.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseMoved(MouseEvent event) {
                getExplorer().resetTimer();
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                getExplorer().resetTimer();
            }

            @Override
            public void mouseEntered(MouseEvent event) {
                getExplorer().resetTimer();
            }

            @Override
            public void mouseExited(MouseEvent event) {
                getExplorer().resetTimer();
            }

            public void mousePressed(MouseEvent event) {
                getExplorer().resetTimer();
                //Change selection even with right click
                TreePath path = getPathForLocation(event.getX(), event.getY());
                if (path != null) {
                    setSelectionPath(path);
                }
                if (event.isPopupTrigger()) {
                    getExplorer().getJPopupMenuRepository().show(event.getComponent(), event.getX(), event.getY());
                }
            }

            public void mouseClicked(MouseEvent event) {
                getExplorer().resetTimer();
            }

            public void mouseReleased(MouseEvent event) {
                getExplorer().resetTimer();
                if (event.isPopupTrigger()) {
                    getExplorer().getJPopupMenuRepository().show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });
        addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {

            @Override
            public void treeExpanded(javax.swing.event.TreeExpansionEvent e) {
            }

            @Override
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent e) {
            }
        });
        addTreeSelectionListener((javax.swing.event.TreeSelectionEvent e) -> {
          getExplorer().getProgressBar().setTitle(getExplorer().getResourceBundle().getString("message.explorer.verifyingcredentials"));
          getExplorer().getProgressBar().show();
          getExplorer().resetTimer();
          int i;
          int j;
          XincoCoreACE tempAce = new XincoCoreACE();
          TreePath tp = e.getPath();
          // get node selected
          XincoMutableTreeNode node = (XincoMutableTreeNode) tp.getLastPathComponent();
          // set current node of session
          setCurrentTreeNodeSelection(node);
          // get ace
          if (node.getUserObject().getClass() == XincoCoreNode.class) {
            tempAce = XincoCoreACEClient.checkAccess(getExplorer().getSession().getUser(), ((XincoCoreNode) node.getUserObject()).getXinco_core_acl());
          }
          if (node.getUserObject().getClass() == XincoCoreData.class) {
            tempAce = XincoCoreACEClient.checkAccess(getExplorer().getSession().getUser(), ((XincoCoreData) node.getUserObject()).getXinco_core_acl());
          }
          // Intelligent menu
          // reset menus
          ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).resetItems();
          ((XincoMenuRepository) getExplorer().getJMenuRepository()).resetItems();
          // dynamic enabling
          if (tempAce.isWrite_permission()) {
            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(4, true);
            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(4, true);
            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(7, true);
            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(7, true);
          }
          if (tempAce.isAdmin_permission()) {
            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(4, true);
            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(4, true);
            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(6, true);
            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(6, true);
            ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(7, true);
            ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(7, true);
          }
          if (node.getUserObject().getClass() == XincoCoreNode.class) {
            if (tempAce.isWrite_permission()) {
              if (((XincoCoreNode) node.getUserObject()).getStatus_number() == 1) {
                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(4, true);
                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(4, true);
              }
              ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(1, true);
              ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(2, true);
              ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(3, true);
              ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(1, true);
              ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(2, true);
              ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(3, true);
              if (!getExplorer().getSession().getClipboardTreeNodeSelection().isEmpty()) {
                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(8, true);
                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(8, true);
              }
            }
            if (tempAce.isRead_permission()) {
              ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(11, true);
              ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(11, true);
            }
          }
          if (node.getUserObject().getClass() == XincoCoreData.class) {
            // file = 1
            if (((XincoCoreData) node.getUserObject()).getXinco_core_data_type().getId() == 1) {
              ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(17, true);
              ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(17, true);
              if (tempAce.isRead_permission()) {
                if (((XincoCoreData) node.getUserObject()).getStatus_number() != XincoDataStatus.ARCHIVED.ordinal() + 1) {
                  ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(5, true);
                  ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(11, true);
                  ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(5, true);
                  ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(11, true);
                }
              }
              if (tempAce.isWrite_permission()) {
                if (((XincoCoreData) node.getUserObject()).getStatus_number() == XincoDataStatus.OPEN.ordinal() + 1) {
                  ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(12, true);
                  ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(13, false);
                  ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(14, false);
                  ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(12, true);
                  ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(13, false);
                  ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(14, false);
                }
                if (((XincoCoreData) node.getUserObject()).getStatus_number() == XincoDataStatus.CHECKED_OUT.ordinal() + 1) {
                  ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(12, false);
                  ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(13, true);
                  ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(14, true);
                  ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(12, false);
                  ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(13, true);
                  ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(14, true);
                }
              }
            }
            // URL = 3
            if (((XincoCoreData) node.getUserObject()).getXinco_core_data_type().getId() == 3) {
              if (tempAce.isRead_permission()) {
                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(9, true);
                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(9, true);
              }
            }
            // contact = 4
            if (((XincoCoreData) node.getUserObject()).getXinco_core_data_type().getId() == 4) {
              if (tempAce.isRead_permission()) {
                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(10, true);
                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(10, true);
              }
            }
            if (tempAce.isRead_permission()) {
              ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(5, true);
              ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(5, true);
            }
            if (tempAce.isWrite_permission()) {
              ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(18, true);
              ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(18, true);
              if (((XincoCoreData) node.getUserObject()).getStatus_number() == XincoDataStatus.OPEN.ordinal() + 1) {
                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(4, true);
                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(4, true);
              }
            }
            if (tempAce.isAdmin_permission()) {
              if ((((XincoCoreData) node.getUserObject()).getStatus_number() != XincoDataStatus.ARCHIVED.ordinal() + 1)
                  && (((XincoCoreData) node.getUserObject()).getStatus_number() != XincoDataStatus.CHECKED_OUT.ordinal() + 1)) {
                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(15, true);
                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(15, true);
              }
              if ((((XincoCoreData) node.getUserObject()).getStatus_number() != XincoDataStatus.LOCKED.ordinal() + 1)
                  && (((XincoCoreData) node.getUserObject()).getStatus_number() != XincoDataStatus.ARCHIVED.ordinal() + 1)) {
                ((XincoMenuRepository) getExplorer().getJMenuRepository()).itemSetEnable(16, true);
                ((XincoPopUpMenuRepository) getExplorer().getJPopupMenuRepository()).itemSetEnable(16, true);
              }
            }
          }
          // only nodes have children
          if (node.getUserObject().getClass() == XincoCoreNode.class) {
            // check for children only if none have been found yet
            if ((((XincoCoreNode) node.getUserObject()).getXinco_core_nodes().isEmpty())
                && (((XincoCoreNode) node.getUserObject()).getXinco_core_data().isEmpty())) {
              try {
                XincoCoreNode xnode = getExplorer().getSession().getXinco().getXincoCoreNode((XincoCoreNode) node.getUserObject(), getExplorer().getSession().getUser());

                if (xnode != null) {
                  getExplorer().getSession().getXincoClientRepository().assignObject2TreeNode(node,
                      xnode,
                      getExplorer().getSession().getXinco(), getExplorer().getSession().getUser(), 2);
                } else {
                  JOptionPane.showMessageDialog(getExplorer(),
                      getExplorer().getResourceBundle().getString("error.folder.sufficientrights"),
                      getExplorer().getResourceBundle().getString("error.accessdenied"),
                      JOptionPane.WARNING_MESSAGE);
                  getExplorer().getProgressBar().hide();
                }
              } catch (Exception rmie) {
                Logger.getLogger(XincoJTree.class.getSimpleName()).log(
                    Level.SEVERE, null, rmie);
              }
            }
          }
          // load full data
          if (node.getUserObject().getClass() == XincoCoreData.class) {
            try {
              getExplorer().setXdata(getExplorer().getSession().getXinco().getXincoCoreData((XincoCoreData) node.getUserObject(), getExplorer().getSession().getUser()));
              if (getExplorer().getXdata() != null) {
                node.setUserObject(getExplorer().getXdata());
                getExplorer().getSession().getXincoClientRepository().treemodel.nodeChanged(node);
              } else {
                JOptionPane.showMessageDialog(getExplorer(),
                    getExplorer().getResourceBundle().getString("error.data.sufficientrights"),
                    getExplorer().getResourceBundle().getString("error.accessdenied"),
                    JOptionPane.WARNING_MESSAGE);
                getExplorer().getProgressBar().hide();
              }
            } catch (Exception rmie) {
            }
          }
          // update details table
          if (node.getUserObject().getClass() == XincoCoreNode.class) {
            DefaultTableModel dtm = (DefaultTableModel) getExplorer().getJTableRepository().getModel();

            j = dtm.getRowCount();
            for (i = 0; i < j; i++) {
              dtm.removeRow(0);
            }
            String[] rdata = {"", ""};

            rdata[0] = getExplorer().getResourceBundle().getString("general.id");
            rdata[1] = "" + ((XincoCoreNode) node.getUserObject()).getId();
            dtm.addRow(rdata);
            rdata[0] = getExplorer().getResourceBundle().getString("general.designation");
            rdata[1] = ((XincoCoreNode) node.getUserObject()).getDesignation();
            dtm.addRow(rdata);
            rdata[0] = getExplorer().getResourceBundle().getString("general.language");
            rdata[1] = ((XincoCoreNode) node.getUserObject()).getXinco_core_language().getDesignation()
                + " ("
                + ((XincoCoreNode) node.getUserObject()).getXinco_core_language().getSign()
                + ")";
            dtm.addRow(rdata);
            rdata[0] = "";
            rdata[1] = "";
            dtm.addRow(rdata);
            rdata[0] = getExplorer().getResourceBundle().getString("general.accessrights");
            rdata[1] = "";
            rdata[1] = rdata[1]
                + "[";
            if (tempAce.isRead_permission()) {
              rdata[1] = rdata[1]
                  + "R";
            } else {
              rdata[1] = rdata[1]
                  + "-";
            }
            if (tempAce.isWrite_permission()) {
              rdata[1] = rdata[1]
                  + "W";
            } else {
              rdata[1] = rdata[1]
                  + "-";
            }
            if (tempAce.isExecute_permission()) {
              rdata[1] = rdata[1]
                  + "X";
            } else {
              rdata[1] = rdata[1]
                  + "-";
            }
            if (tempAce.isAdmin_permission()) {
              rdata[1] = rdata[1]
                  + "A";
            } else {
              rdata[1] = rdata[1]
                  + "-";
            }
            rdata[1] = rdata[1]
                + "]";
            dtm.addRow(rdata);
            rdata[0] = "";
            rdata[1] = "";
            dtm.addRow(rdata);
            rdata[0] = getExplorer().getResourceBundle().getString("general.status");
            rdata[1] = "";
            if (((XincoCoreNode) node.getUserObject()).getStatus_number()
                == 1) {
              rdata[1] = getExplorer().getResourceBundle().getString("general.status.open")
                  + "";
            }
            if (((XincoCoreNode) node.getUserObject()).getStatus_number()
                == 2) {
              rdata[1] = getExplorer().getResourceBundle().getString("general.status.locked")
                  + " (-)";
            }
            if (((XincoCoreNode) node.getUserObject()).getStatus_number()
                == 3) {
              rdata[1] = getExplorer().getResourceBundle().getString("general.status.archived")
                  + " (->)";
            }
            dtm.addRow(rdata);
          }
          if (node.getUserObject().getClass() == XincoCoreData.class) {
            DefaultTableModel dtm = (DefaultTableModel) getExplorer().getJTableRepository().getModel();
            j = dtm.getRowCount();
            for (i = 0; i < j; i++) {
              dtm.removeRow(0);
            }
            String[] rdata = {"", ""};
            if (getExplorer().getXdata() != null) {
              rdata[0] = getExplorer().getResourceBundle().getString("general.id");
              rdata[1] = "" + getExplorer().getXdata().getId();
              dtm.addRow(rdata);
              rdata[0] = getExplorer().getResourceBundle().getString("general.designation");
              rdata[1] = getExplorer().getXdata().getDesignation();
              dtm.addRow(rdata);
              rdata[0] = getExplorer().getResourceBundle().getString("general.language");
              rdata[1] = getExplorer().getXdata().getXinco_core_language().getDesignation()
                  + " ("
                  + getExplorer().getXdata().getXinco_core_language().getSign()
                  + ")";
            }
            dtm.addRow(rdata);
            rdata[0] = getExplorer().getResourceBundle().getString("general.datatype");
            rdata[1] = getExplorer().getResourceBundle().getString(getExplorer().getXdata().getXinco_core_data_type().getDesignation())
                + " (" + getExplorer().getResourceBundle().getString(getExplorer().getXdata().getXinco_core_data_type().getDescription())
                + ")";
            dtm.addRow(rdata);
            rdata[0] = "";
            rdata[1] = "";
            dtm.addRow(rdata);
            rdata[0] = getExplorer().getResourceBundle().getString("general.accessrights");
            rdata[1] = "";
            rdata[1] = rdata[1]
                + "[";
            if (tempAce.isRead_permission()) {
              rdata[1] = rdata[1]
                  + "R";
            } else {
              rdata[1] = rdata[1]
                  + "-";
            }
            if (tempAce.isWrite_permission()) {
              rdata[1] = rdata[1]
                  + "W";
            } else {
              rdata[1] = rdata[1]
                  + "-";
            }
            if (tempAce.isExecute_permission()) {
              rdata[1] = rdata[1]
                  + "X";
            } else {
              rdata[1] = rdata[1]
                  + "-";
            }
            if (tempAce.isAdmin_permission()) {
              rdata[1] = rdata[1]
                  + "A";
            } else {
              rdata[1] = rdata[1]
                  + "-";
            }
            rdata[1] = rdata[1]
                + "]";
            dtm.addRow(rdata);
            rdata[0] = "";
            rdata[1] = "";
            dtm.addRow(rdata);
            rdata[0] = getExplorer().getResourceBundle().getString("general.status");
            rdata[1] = "";
            if (getExplorer().getXdata().getStatus_number() == XincoDataStatus.OPEN.ordinal() + 1) {
              rdata[1] = getExplorer().getResourceBundle().getString("general.status.open")
                  + "";
            }
            if (getExplorer().getXdata().getStatus_number() == XincoDataStatus.LOCKED.ordinal() + 1) {
              rdata[1] = getExplorer().getResourceBundle().getString("general.status.locked")
                  + " (-)";
            }
            if (getExplorer().getXdata().getStatus_number() == XincoDataStatus.ARCHIVED.ordinal() + 1) {
              rdata[1] = getExplorer().getResourceBundle().getString("general.status.archived")
                  + " (->)";
            }
            if (getExplorer().getXdata().getStatus_number() == XincoDataStatus.CHECKED_OUT.ordinal() + 1) {
              rdata[1] = getExplorer().getResourceBundle().getString("general.status.checkedout")
                  + " (X)";
            }
            if (getExplorer().getXdata().getStatus_number() == XincoDataStatus.PUBLISHED.ordinal() + 1) {
              rdata[1] = getExplorer().getResourceBundle().getString("general.status.published")
                  + " (WWW)";
            }
            dtm.addRow(rdata);
            rdata[0] = "";
            rdata[1] = "";
            dtm.addRow(rdata);
            rdata[0] = getExplorer().getResourceBundle().getString("general.typespecificattributes");
            rdata[1] = "";
            dtm.addRow(rdata);
            // get add attributes of CoreData, if access granted
            if (!getExplorer().getXdata().getXinco_add_attributes().isEmpty()) {
              for (i = 0; i < getExplorer().getXdata().getXinco_add_attributes().size(); i++) {
                if (i < getExplorer().getXdata().getXinco_core_data_type().getXinco_core_data_type_attributes().size()) {
                  rdata[0] = ((XincoCoreDataTypeAttribute) getExplorer().getXdata().getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getDesignation();
                  if (((XincoCoreDataTypeAttribute) getExplorer().getXdata().getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("int")) {
                    rdata[1] = ""
                        + ((XincoAddAttribute) getExplorer().getXdata().getXinco_add_attributes().elementAt(i)).getAttrib_int();
                  }
                  if (((XincoCoreDataTypeAttribute) getExplorer().getXdata().getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("unsignedint")) {
                    rdata[1] = ""
                        + ((XincoAddAttribute) getExplorer().getXdata().getXinco_add_attributes().elementAt(i)).getAttrib_unsignedint();
                  }
                  if (((XincoCoreDataTypeAttribute) getExplorer().getXdata().getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("double")) {
                    rdata[1] = ""
                        + ((XincoAddAttribute) getExplorer().getXdata().getXinco_add_attributes().elementAt(i)).getAttrib_double();
                  }
                  if (((XincoCoreDataTypeAttribute) getExplorer().getXdata().getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("varchar")) {
                    rdata[1] = ""
                        + ((XincoAddAttribute) getExplorer().getXdata().getXinco_add_attributes().elementAt(i)).getAttrib_varchar();
                  }
                  if (((XincoCoreDataTypeAttribute) getExplorer().getXdata().getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("text")) {
                    rdata[1] = ""
                        + ((XincoAddAttribute) getExplorer().getXdata().getXinco_add_attributes().elementAt(i)).getAttrib_text();
                  }
                  if (((XincoCoreDataTypeAttribute) getExplorer().getXdata().getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().equalsIgnoreCase("datetime")) {
                    Date time = ((XincoAddAttribute) getExplorer().getXdata().getXinco_add_attributes().elementAt(i)).getAttrib_datetime().getTime();
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(time);
                    rdata[1] = (cal.get(Calendar.MONTH) == 11
                        && cal.get(Calendar.YEAR) == 2
                        && cal.get(Calendar.DAY_OF_MONTH) == 31) ? "" : "" + time;
                  }
                  dtm.addRow(rdata);
                }
              }
            } else {
              rdata[0] = getExplorer().getResourceBundle().getString("error.accessdenied");
              rdata[1] = getExplorer().getResourceBundle().getString("error.content.sufficientrights");
              dtm.addRow(rdata);
            }
            rdata[0] = "";
            rdata[1] = "";
            dtm.addRow(rdata);
            rdata[0] = "";
            rdata[1] = "";
            dtm.addRow(rdata);
            rdata[0] = getExplorer().getResourceBundle().getString("general.logslastfirst");
            rdata[1] = "";
            dtm.addRow(rdata);
            Calendar cal;
            Calendar realcal;
            Calendar ngc = new GregorianCalendar();
            for (i = getExplorer().getXdata().getXinco_core_logs().size() - 1; i >= 0; i--) {
              if (((XincoCoreLog) getExplorer().getXdata().getXinco_core_logs().elementAt(i)).getOp_datetime()
                  != null) {
                try {
                  // convert clone from remote time to local time
                  cal = (Calendar) ((XincoCoreLog) getExplorer().getXdata().getXinco_core_logs().elementAt(i)).getOp_datetime().clone();
                  realcal = (((XincoCoreLog) getExplorer().getXdata().getXinco_core_logs().elementAt(i)).getOp_datetime());
                  cal.add(Calendar.MILLISECOND,
                      (ngc.get(Calendar.ZONE_OFFSET)
                          - realcal.get(Calendar.ZONE_OFFSET))
                          - (ngc.get(Calendar.DST_OFFSET)
                              + realcal.get(Calendar.DST_OFFSET)));
                  rdata[0] = ""
                      + (cal.get(Calendar.MONTH)
                      + 1)
                      + " / "
                      + cal.get(Calendar.DAY_OF_MONTH)
                      + " / "
                      + cal.get(Calendar.YEAR)
                      + " ";
                  if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
                    rdata[0] += "0" + cal.get(Calendar.HOUR_OF_DAY) + ":";
                  } else {
                    rdata[0] += cal.get(Calendar.HOUR_OF_DAY) + ":";
                  }
                  if (cal.get(Calendar.MINUTE) < 10) {
                    rdata[0] += "0" + cal.get(Calendar.MINUTE) + ":";
                  } else {
                    rdata[0] += cal.get(Calendar.MINUTE) + ":";
                  }
                  if (cal.get(Calendar.SECOND) < 10) {
                    rdata[0] += "0" + cal.get(Calendar.SECOND);
                  } else {
                    rdata[0] += cal.get(Calendar.SECOND);
                  }
                } catch (Exception ce) {
                  Logger.getLogger(XincoJTree.class.getSimpleName()).log(
                      Level.SEVERE, null, ce);
                }
              } else {
                rdata[0] = "???";
              }
              rdata[1] = "("
                  + ((XincoCoreLog) getExplorer().getXdata().getXinco_core_logs().elementAt(i)).getOp_code()
                  + ") "
                  + ((XincoCoreLog) getExplorer().getXdata().getXinco_core_logs().elementAt(i)).getOp_description();
              dtm.addRow(rdata);
              rdata[0] = "";
              try {
                rdata[1] = getExplorer().getResourceBundle().getString("general.version")
                    + " "
                    + ((XincoCoreLog) getExplorer().getXdata().getXinco_core_logs().elementAt(i)).getVersion().getVersion_high()
                    + "."
                    + ((XincoCoreLog) getExplorer().getXdata().getXinco_core_logs().elementAt(i)).getVersion().getVersion_mid()
                    + "."
                    + ((XincoCoreLog) getExplorer().getXdata().getXinco_core_logs().elementAt(i)).getVersion().getVersion_low()
                    + ""
                    + ((XincoCoreLog) getExplorer().getXdata().getXinco_core_logs().elementAt(i)).getVersion().getVersion_postfix();
              } catch (Exception ex) {
                Logger.getLogger(XincoJTree.class.getSimpleName()).log(
                    Level.SEVERE, null, ex);
                getExplorer().getProgressBar().hide();
              }
              dtm.addRow(rdata);
            }
          }
          XincoAutofitTableColumns.autoResizeTable(getExplorer().getJTableRepository(), true);
          getExplorer().getProgressBar().hide();
        });
        java.awt.event.MouseListener ml = new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                getExplorer().resetTimer();
                int selRow = getRowForLocation(e.getX(),
                        e.getY());
                if (selRow != -1) {
                    if (e.getClickCount() == 1) {
                    } else if (e.getClickCount() == 2) {
                        // double-click -> preview file
                        // setSelectionPath(selPath);
                        // Only if not archieved
                        if (getExplorer().getSession().getCurrentTreeNodeSelection().getUserObject().getClass()
                                == XincoCoreData.class) {
                            if (((XincoCoreData) getExplorer().getSession().getCurrentTreeNodeSelection().getUserObject()).getStatus_number()
                                    == XincoDataStatus.ARCHIVED.ordinal() + 1) {
                                JOptionPane.showMessageDialog(getExplorer(),
                                        "This operation is not available for archived data.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            // file = 1
                            if (((XincoCoreData) getExplorer().getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_data_type().getId() == 1) {
                                getExplorer().doDataWizard(14);
                                getExplorer().setCurrentPathFilename(getExplorer().getPreviousFullpath());
                            }
                            // text = 2
                            if (((XincoCoreData) getExplorer().getSession().getCurrentTreeNodeSelection().getUserObject()).getXinco_core_data_type().getId() == 2) {
                                getExplorer().getAbstractDialogAddAttributesText(true);
                            }
                        }
                    }
                }
            }
        };
        addMouseListener(ml);
    }

    public XincoExplorer getExplorer() {
        return explorer;
    }

    protected void setExplorer(XincoExplorer explorer) {
        this.explorer = explorer;
    }

    public void collapseAllNodes() {
        int row = getRowCount() - 1;
        while (row >= 0) {
            collapseRow(row);
            row--;
        }
        expandRow(0);
    }

    //Drag and Drop functionality methods
    public void autoscroll(Point cursorLocation) {
        Insets insets = getAutoscrollInsets();
        Rectangle outer = getVisibleRect();
        Rectangle inner = new Rectangle(outer.x + insets.left, outer.y + insets.top, outer.width - (insets.left + insets.right), outer.height - (insets.top + insets.bottom));
        if (!inner.contains(cursorLocation)) {
            Rectangle scrollRect = new Rectangle(cursorLocation.x - insets.left, cursorLocation.y - insets.top, insets.left + insets.right, insets.top + insets.bottom);
            scrollRectToVisible(scrollRect);
        }
    }

    protected Insets getAutoscrollInsets() {
        return (autoscrollInsets);
    }

    public static DefaultMutableTreeNode makeDeepCopy(DefaultMutableTreeNode node) {
        DefaultMutableTreeNode copy = new DefaultMutableTreeNode(node.getUserObject());
        for (Enumeration e = node.children(); e.hasMoreElements();) {
            copy.add(makeDeepCopy((DefaultMutableTreeNode) e.nextElement()));
        }
        return (copy);
    }

    public XincoMutableTreeNode getPreviousTreeNodeSelection() {
        return previousTreeNodeSelection;
    }

    private void setCurrentTreeNodeSelection(XincoMutableTreeNode current) {
        setPreviousTreeNodeSelection(getExplorer().getSession().getCurrentTreeNodeSelection());
        getExplorer().getSession().setCurrentTreeNodeSelection(current);
    }

    protected void setPreviousTreeNodeSelection(XincoMutableTreeNode previousTreeNodeSelection) {
        this.previousTreeNodeSelection = previousTreeNodeSelection;
    }

    public XincoMutableTreeNode getTargetTreeNode() {
        return targetTreeNode;
    }

    public void setTargetTreeNode(XincoMutableTreeNode targetTreeNode) {
        this.targetTreeNode = targetTreeNode;
    }
}
