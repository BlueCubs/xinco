/*
 * XincoDefaultTreeTransferHandler.java
 *
 * Created on May 18, 2007, 2:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.client.object.dragNdrop;

import com.bluecubs.xinco.client.object.*;
import com.bluecubs.xinco.core.XincoCoreData;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author ortizbj
 */
public class XincoDefaultTreeTransferHandler extends XincoAbstractTreeTransferHandler {
    public XincoDefaultTreeTransferHandler(XincoJTree tree, int action) {
        super(tree, action, true);
    }
    
    public boolean canPerformAction(XincoJTree target, DefaultMutableTreeNode draggedNode, int action, Point location) {
        TreePath pathTarget = target.getPathForLocation(location.x, location.y);
        if (pathTarget == null) {
            target.setSelectionPath(null);
            return(false);
        }
        target.setSelectionPath(pathTarget);
        if(action == DnDConstants.ACTION_COPY) {
            return true;
        } else
            if(action == DnDConstants.ACTION_MOVE) {
            DefaultMutableTreeNode parentNode =(DefaultMutableTreeNode)pathTarget.getLastPathComponent();
            if (draggedNode.isRoot() || parentNode == draggedNode.getParent() ||
                    draggedNode.isNodeDescendant(parentNode) ||
                    parentNode.getUserObject().getClass() == XincoCoreData.class) {
                return false;
            } else {
                return true;
            }
            } else
                return false;
    }
    
    public boolean executeDrop(XincoJTree target, DefaultMutableTreeNode draggedNode, DefaultMutableTreeNode newParentNode, int action) {
        if (action == DnDConstants.ACTION_COPY) {
            DefaultMutableTreeNode newNode = target.makeDeepCopy(draggedNode);
            ((DefaultTreeModel)target.getModel()).insertNodeInto(newNode,newParentNode,newParentNode.getChildCount());
            TreePath treePath = new TreePath(newNode.getPath());
            target.scrollPathToVisible(treePath);
            target.setSelectionPath(treePath);
            return(true);
        }
        if (action == DnDConstants.ACTION_MOVE) {
            draggedNode.removeFromParent();
            ((DefaultTreeModel)target.getModel()).insertNodeInto(draggedNode,newParentNode,newParentNode.getChildCount());
            TreePath treePath = new TreePath(draggedNode.getPath());
            target.scrollPathToVisible(treePath);
            target.setSelectionPath(treePath);
            return(true);
        }
        return(false);
    }
}

