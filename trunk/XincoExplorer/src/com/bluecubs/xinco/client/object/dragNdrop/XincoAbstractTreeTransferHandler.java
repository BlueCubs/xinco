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
 * Name:            XincoAbstractTreeTransferHandler
 *
 * Description:     XincoAbstractTreeTransferHandler
 *
 * Original Author: Javier A. Ortiz
 * Date:            May 18, 2007, 2:21 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */
package com.bluecubs.xinco.client.object.dragNdrop;

import com.bluecubs.xinco.client.object.XincoMutableTreeNode;
import com.bluecubs.xinco.client.object.XincoJTree;
import com.bluecubs.xinco.client.service.XincoCoreACE;
import com.bluecubs.xinco.client.service.XincoCoreData;
import com.bluecubs.xinco.client.service.XincoCoreNode;
import com.bluecubs.xinco.core.client.XincoCoreACEClient;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Javier A. Ortiz
 */
public abstract class XincoAbstractTreeTransferHandler implements DragGestureListener, DragSourceListener, DropTargetListener {

    private XincoJTree tree;
    private DragSource dragSource; // dragsource
    private DropTarget dropTarget; //droptarget
    private static DefaultMutableTreeNode draggedNode;
    private DefaultMutableTreeNode draggedNodeParent;
    private static BufferedImage image = null; //buff image
    private Rectangle rect2D = new Rectangle();
    private boolean drawImage;

    protected XincoAbstractTreeTransferHandler(XincoJTree tree, int action, boolean drawIcon) {
        this.tree = tree;
        drawImage = drawIcon;
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(tree, action, this);
        dropTarget = new DropTarget(tree, action, this);
    }

    /* Methods for DragSourceListener */
    public void dragDropEnd(DragSourceDropEvent dsde) {
        if (dsde.getDropSuccess() && dsde.getDropAction() == DnDConstants.ACTION_MOVE && draggedNodeParent != null) {
            XincoCoreACE tempAce = new XincoCoreACE();
            // get ace
            if (draggedNode.getUserObject().getClass() == XincoCoreNode.class) {
                tempAce = XincoCoreACEClient.checkAccess(getTree().getExplorer().getSession().getUser(),
                        ((XincoCoreNode) draggedNode.getUserObject()).getXincoCoreAcl());
            }
            if (draggedNode.getUserObject().getClass() == XincoCoreData.class) {
                tempAce = XincoCoreACEClient.checkAccess(getTree().getExplorer().getSession().getUser(),
                        ((XincoCoreData) draggedNode.getUserObject()).getXincoCoreAcl());
            }
            //Drop only if you have write permissions
            if (tempAce.isWritePermission()) {
                getTree().getExplorer().getSession().setCurrentTreeNodeSelection((XincoMutableTreeNode) draggedNodeParent);
                ((DefaultTreeModel) getTree().getModel()).nodeStructureChanged(draggedNodeParent);
                getTree().expandPath(new TreePath(draggedNodeParent.getPath()));
                getTree().expandPath(new TreePath(draggedNode.getPath()));
                getTree().getExplorer().getActionHandler().insertNode(true);
                getTree().getExplorer().getjInternalFrameInformationText().setText(getTree().getExplorer().getResourceBundle().getString("menu.edit.movefoldersuccess"));
            }
        }
    }

    public final void dragEnter(DragSourceDragEvent dsde) {
        int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY) {
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        } else {
            if (action == DnDConstants.ACTION_MOVE) {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            } else {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
            }
        }
    }

    public final void dragOver(DragSourceDragEvent dsde) {
        int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY) {
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        } else {
            if (action == DnDConstants.ACTION_MOVE) {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            } else {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
            }
        }
    }

    public final void dropActionChanged(DragSourceDragEvent dsde) {
        int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY) {
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        } else {
            if (action == DnDConstants.ACTION_MOVE) {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            } else {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
            }
        }
    }

    public final void dragExit(DragSourceEvent dse) {
        dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    /* Methods for DragGestureListener */
    public final void dragGestureRecognized(DragGestureEvent dge) {
        TreePath path = getTree().getSelectionPath();
        if (path != null) {
            draggedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            draggedNodeParent = (DefaultMutableTreeNode) draggedNode.getParent();
            if (drawImage) {
                Rectangle pathBounds = getTree().getPathBounds(path); //getpathbounds of selectionpath
                JComponent lbl = (JComponent) getTree().getCellRenderer().getTreeCellRendererComponent(getTree(), draggedNode, false, getTree().isExpanded(path), ((DefaultTreeModel) getTree().getModel()).isLeaf(path.getLastPathComponent()), 0, false);//returning the label
                lbl.setBounds(pathBounds);//setting bounds to lbl
                image = new BufferedImage(lbl.getWidth(), lbl.getHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE);//buffered image reference passing the label's ht and width
                Graphics2D graphics = image.createGraphics();//creating the graphics for buffered image
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));	//Sets the Composite for the Graphics2D context
                lbl.setOpaque(false);
                lbl.paint(graphics); //painting the graphics to label
                graphics.dispose();
                getTree().getExplorer().getjInternalFrameInformationText().setText(getTree().getExplorer().getResourceBundle().getString("menu.edit.movemessage"));
                getTree().getExplorer().getActionHandler().moveToClipboard();
            }
            dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop, image, new Point(0, 0), new XincoTransferableNode(draggedNode), this);
        }
    }

    /* Methods for DropTargetListener */
    public final void dragEnter(DropTargetDragEvent dtde) {
        Point pt = dtde.getLocation();
        int action = dtde.getDropAction();
        if (drawImage) {
            paintImage(pt);
        }
        if (canPerformAction(getTree(), draggedNode, action, pt)) {
            dtde.acceptDrag(action);
        } else {
            dtde.rejectDrag();
        }
    }

    public final void dragExit(DropTargetEvent dte) {
        if (drawImage) {
            clearImage();
        }
    }

    public final void dragOver(DropTargetDragEvent dtde) {
        Point pt = dtde.getLocation();
        int action = dtde.getDropAction();
        getTree().autoscroll(pt);
        if (drawImage) {
            paintImage(pt);
        }
        if (canPerformAction(getTree(), draggedNode, action, pt)) {
            dtde.acceptDrag(action);
        } else {
            dtde.rejectDrag();
        }
    }

    public final void dropActionChanged(DropTargetDragEvent dtde) {
        Point pt = dtde.getLocation();
        int action = dtde.getDropAction();
        if (drawImage) {
            paintImage(pt);
        }
        if (canPerformAction(getTree(), draggedNode, action, pt)) {
            dtde.acceptDrag(action);
        } else {
            dtde.rejectDrag();
        }
    }

    public final void drop(DropTargetDropEvent dtde) {
        try {
            if (drawImage) {
                clearImage();
            }
            int action = dtde.getDropAction();
            Transferable transferable = dtde.getTransferable();
            Point pt = dtde.getLocation();
            if (transferable.isDataFlavorSupported(XincoTransferableNode.NODE_FLAVOR) && canPerformAction(getTree(), draggedNode, action, pt)) {
                TreePath pathTarget = getTree().getPathForLocation(pt.x, pt.y);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) transferable.getTransferData(XincoTransferableNode.NODE_FLAVOR);
                DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode) pathTarget.getLastPathComponent();
                getTree().setTargetTreeNode((XincoMutableTreeNode) newParentNode);
                if (executeDrop(getTree(), node, newParentNode, action)) {
                    dtde.acceptDrop(action);
                    dtde.dropComplete(true);
                    return;
                }
            }
            dtde.rejectDrop();
            dtde.dropComplete(false);
        } catch (Exception e) {
            System.out.println(e);
            dtde.rejectDrop();
            dtde.dropComplete(false);
        }
    }

    private void paintImage(Point pt) {
        getTree().paintImmediately(rect2D.getBounds());
        rect2D.setRect((int) pt.getX(), (int) pt.getY(), image.getWidth(), image.getHeight());
        getTree().getGraphics().drawImage(image, (int) pt.getX(), (int) pt.getY(), getTree());
    }

    private void clearImage() {
        getTree().paintImmediately(rect2D.getBounds());
    }

    protected abstract boolean canPerformAction(XincoJTree target, DefaultMutableTreeNode draggedNode, int action, Point location);

    protected abstract boolean executeDrop(XincoJTree tree, DefaultMutableTreeNode draggedNode, DefaultMutableTreeNode newParentNode, int action);

    protected XincoJTree getTree() {
        return tree;
    }
}

