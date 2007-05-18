/*
 * AbstractTreeTransferHandler.java
 *
 * Created on May 18, 2007, 2:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoMutableTreeNode;
import com.bluecubs.xinco.core.XincoCoreACE;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreNode;
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
 * @author ortizbj
 */
public abstract class AbstractTreeTransferHandler implements DragGestureListener, DragSourceListener, DropTargetListener {
    
    private XincoJTree tree;
    private DragSource dragSource; // dragsource
    private DropTarget dropTarget; //droptarget
    private static DefaultMutableTreeNode draggedNode;
    private DefaultMutableTreeNode draggedNodeParent;
    private static BufferedImage image = null; //buff image
    private Rectangle rect2D = new Rectangle();
    private boolean drawImage;
    
    protected AbstractTreeTransferHandler(XincoJTree tree, int action, boolean drawIcon) {
        this.tree = tree;
        drawImage = drawIcon;
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(tree, action, this);
        dropTarget = new DropTarget(tree, action, this);
    }
    
    /* Methods for DragSourceListener */
    public void dragDropEnd(DragSourceDropEvent dsde) {
        System.out.println("dragDropEnd");
        if (dsde.getDropSuccess() && dsde.getDropAction()==DnDConstants.ACTION_MOVE && draggedNodeParent != null) {
            XincoCoreACE temp_ace = new XincoCoreACE();
            // get ace
            if (draggedNode.getUserObject().getClass() == XincoCoreNode.class) {
                temp_ace = XincoCoreACEClient.checkAccess(getTree().getExplorer().getSession().user,
                        ((XincoCoreNode) draggedNode.getUserObject()).getXinco_core_acl());
            }
            if (draggedNode.getUserObject().getClass() == XincoCoreData.class) {
                temp_ace = XincoCoreACEClient.checkAccess(getTree().getExplorer().getSession().user,
                        ((XincoCoreData) draggedNode.getUserObject()).getXinco_core_acl());
            }
            //Drop only if you have write permissions
            if (temp_ace.isWrite_permission()) {
                getTree().getExplorer().getSession().currentTreeNodeSelection=(XincoMutableTreeNode)draggedNodeParent;
                System.out.println("Have permission to write");
                ((DefaultTreeModel)getTree().getModel()).nodeStructureChanged(draggedNodeParent);
                getTree().expandPath(new TreePath(draggedNodeParent.getPath()));
                getTree().expandPath(new TreePath(draggedNode.getPath()));
                getTree().getExplorer().getActionHandler().moveToClipboard();
                getTree().getExplorer().getActionHandler().insertNode(true);
                if(draggedNode.getUserObject().getClass() == XincoCoreNode.class)
                    System.out.println("Dragging Node: "+((XincoCoreNode) draggedNode.getUserObject()).getDesignation());
                if(draggedNode.getUserObject().getClass() == XincoCoreData.class)
                    System.out.println("Dragging Data: "+((XincoCoreData) draggedNode.getUserObject()).getDesignation());
            } else
                System.err.println("Don't have permission to write");
        }
    }
    public final void dragEnter(DragSourceDragEvent dsde)  {
        System.out.println("dragEnter");
        int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY)  {
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
        System.out.println("dragOver");
        int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY) {
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        } else  {
            if (action == DnDConstants.ACTION_MOVE) {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            } else  {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
            }
        }
    }
    public final void dropActionChanged(DragSourceDragEvent dsde)  {
        System.out.println("dropActionChanged");
        int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY) {
            System.out.println("Copy");
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        } else  {
            System.out.println("Move");
            if (action == DnDConstants.ACTION_MOVE) {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            } else {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
            }
        }
    }
    public final void dragExit(DragSourceEvent dse) {
        System.out.println("dragExit");
        dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }
    
    /* Methods for DragGestureListener */
    public final void dragGestureRecognized(DragGestureEvent dge) {
        System.out.println("Drag gesture detected!");
        TreePath path = getTree().getSelectionPath();
        if (path != null) {
            draggedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
            if(draggedNode.getUserObject().getClass() == XincoCoreNode.class)
                System.out.println("Dragging "+((XincoCoreNode) draggedNode.getUserObject()).getDesignation());
            if(draggedNode.getUserObject().getClass() == XincoCoreData.class)
                System.out.println("Dragging "+((XincoCoreData) draggedNode.getUserObject()).getDesignation());
            draggedNodeParent = (DefaultMutableTreeNode)draggedNode.getParent();
            if (drawImage) {
                Rectangle pathBounds = getTree().getPathBounds(path); //getpathbounds of selectionpath
                JComponent lbl = (JComponent)getTree().getCellRenderer().getTreeCellRendererComponent(getTree(), draggedNode, false , getTree().isExpanded(path),((DefaultTreeModel)getTree().getModel()).isLeaf(path.getLastPathComponent()), 0,false);//returning the label
                lbl.setBounds(pathBounds);//setting bounds to lbl
                image = new BufferedImage(lbl.getWidth(), lbl.getHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE);//buffered image reference passing the label's ht and width
                Graphics2D graphics = image.createGraphics();//creating the graphics for buffered image
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));	//Sets the Composite for the Graphics2D context
                lbl.setOpaque(false);
                lbl.paint(graphics); //painting the graphics to label
                graphics.dispose();
            }
            dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop , image, new Point(0,0), new TransferableNode(draggedNode), this);
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
            if (transferable.isDataFlavorSupported(TransferableNode.NODE_FLAVOR) && canPerformAction(getTree(), draggedNode, action, pt)) {
                TreePath pathTarget = getTree().getPathForLocation(pt.x, pt.y);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) transferable.getTransferData(TransferableNode.NODE_FLAVOR);
                DefaultMutableTreeNode newParentNode =(DefaultMutableTreeNode)pathTarget.getLastPathComponent();
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
    
    private final void paintImage(Point pt) {
        getTree().paintImmediately(rect2D.getBounds());
        rect2D.setRect((int) pt.getX(),(int) pt.getY(),image.getWidth(),image.getHeight());
        getTree().getGraphics().drawImage(image,(int) pt.getX(),(int) pt.getY(), getTree());
    }
    
    private final void clearImage() {
        getTree().paintImmediately(rect2D.getBounds());
    }
    
    public abstract boolean canPerformAction(XincoJTree target, DefaultMutableTreeNode draggedNode, int action, Point location);
    
    public abstract boolean executeDrop(XincoJTree tree, DefaultMutableTreeNode draggedNode, DefaultMutableTreeNode newParentNode, int action);
    
    public XincoJTree getTree() {
        return tree;
    }
}

