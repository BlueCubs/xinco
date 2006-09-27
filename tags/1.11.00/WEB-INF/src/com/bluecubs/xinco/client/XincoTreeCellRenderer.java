/**
 *Copyright 2004 blueCubs.com
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
 * Name:            XincoTreeCellRenderer
 *
 * Description:     cell renderer on client side 
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 * 
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */

package com.bluecubs.xinco.client;

import java.awt.Component;
import javax.swing.*;
import javax.swing.tree.*;

import com.bluecubs.xinco.core.*;

class XincoTreeCellRenderer extends DefaultTreeCellRenderer {

    public XincoTreeCellRenderer() {
    }

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
                        
        if (leaf && isFolder(value)) {
            setIcon(getClosedIcon());
            setToolTipText("");
        } else {
            setToolTipText(null); //no tool tip
        } 

        return this;
    }

    protected boolean isFolder(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        if(node.getUserObject().getClass() == XincoCoreNode.class) {
        	return true;
        } else {
			return false;
        }
    }
}