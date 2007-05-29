/*
 * XincoTransferableNode.java
 *
 * Created on May 18, 2007, 12:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.client.object;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Arrays;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author ortizbj
 */
public class XincoTransferableNode implements Transferable {
	static final DataFlavor NODE_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, "Node");
	private DefaultMutableTreeNode node;
	private DataFlavor[] flavors = { NODE_FLAVOR };
 
	public XincoTransferableNode(DefaultMutableTreeNode nd) {
		node = nd;
	}  
 
	public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (flavor == NODE_FLAVOR) {
			return node;
		}
		else {
			throw new UnsupportedFlavorException(flavor);	
		}			
	}
 
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}
 
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return Arrays.asList(flavors).contains(flavor);
	}
}
