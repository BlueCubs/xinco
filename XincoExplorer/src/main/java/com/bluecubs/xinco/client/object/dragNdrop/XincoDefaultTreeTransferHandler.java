/**
 * Copyright 2010 blueCubs.com
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>************************************************************ This project supports the
 * blueCubs vision of giving back to the community in exchange for free software! More information
 * on: http://www.bluecubs.org ************************************************************
 *
 * <p>Name: XincoDefaultTreeTransferHandler
 *
 * <p>Description: XincoDefaultTreeTransferHandler
 *
 * <p>Original Author: Javier A. Ortiz Date: May 18, 2010, 2:24 PM
 *
 * <p>Modifications:
 *
 * <p>Who? When? What?
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.client.object.dragNdrop;

import com.bluecubs.xinco.client.object.XincoJTree;
import com.bluecubs.xinco.core.XincoCoreData;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Javier A. Ortiz
 */
public class XincoDefaultTreeTransferHandler extends XincoAbstractTreeTransferHandler {

  public XincoDefaultTreeTransferHandler(XincoJTree tree, int action) {
    super(tree, action, true);
  }

  public boolean canPerformAction(
      XincoJTree target, DefaultMutableTreeNode draggedNode, int action, Point location) {
    TreePath pathTarget = target.getPathForLocation(location.x, location.y);
    if (pathTarget == null) {
      target.setSelectionPath(null);
      return (false);
    }
    target.setSelectionPath(pathTarget);
    if (action == DnDConstants.ACTION_COPY) {
      return true;
    } else if (action == DnDConstants.ACTION_MOVE) {
      DefaultMutableTreeNode parentNode =
          (DefaultMutableTreeNode) pathTarget.getLastPathComponent();
      if (draggedNode.isRoot()
          || parentNode == draggedNode.getParent()
          || draggedNode.isNodeDescendant(parentNode)
          || parentNode.getUserObject().getClass() == XincoCoreData.class) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  public boolean executeDrop(
      XincoJTree target,
      DefaultMutableTreeNode draggedNode,
      DefaultMutableTreeNode newParentNode,
      int action) {
    if (action == DnDConstants.ACTION_COPY) {
      DefaultMutableTreeNode newNode = XincoJTree.makeDeepCopy(draggedNode);
      ((DefaultTreeModel) target.getModel())
          .insertNodeInto(newNode, newParentNode, newParentNode.getChildCount());
      TreePath treePath = new TreePath(newNode.getPath());
      target.scrollPathToVisible(treePath);
      target.setSelectionPath(treePath);
      return (true);
    }
    if (action == DnDConstants.ACTION_MOVE) {
      draggedNode.removeFromParent();
      ((DefaultTreeModel) target.getModel())
          .insertNodeInto(draggedNode, newParentNode, newParentNode.getChildCount());
      TreePath treePath = new TreePath(draggedNode.getPath());
      target.scrollPathToVisible(treePath);
      target.setSelectionPath(treePath);
      return (true);
    }
    return (false);
  }
}
