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
 * <p>Name: XincoTransferableNode
 *
 * <p>Description: XincoTransferableNode
 *
 * <p>Original Author: Javier A. Ortiz Date: May 18, 2010, 12:01 PM
 *
 * <p>Modifications:
 *
 * <p>Who? When? What?
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.client.object.dragNdrop;

import com.bluecubs.xinco.client.object.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Arrays;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Javier A. Ortiz
 */
public class XincoTransferableNode implements Transferable {
  static final DataFlavor NODE_FLAVOR =
      new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, "Node");
  private DefaultMutableTreeNode node;
  private DataFlavor[] flavors = {NODE_FLAVOR};

  public XincoTransferableNode(DefaultMutableTreeNode nd) {
    node = nd;
  }

  public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
    if (flavor == NODE_FLAVOR) {
      return node;
    } else {
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
