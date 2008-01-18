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
 * Name:            XincoTreeCellRenderer
 *
 * Description:     XincoTreeCellRenderer
 *
 * Original Author: Javier A. Ortiz
 * Date:            May 25, 2007, 10:01 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */
package com.bluecubs.xinco.client.object.dragNdrop;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.client.object.*;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.tools.XincoFileIconManager;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoTreeCellRenderer extends DefaultTreeCellRenderer {

    private XincoFileIconManager xfm = null;
    private XincoExplorer explorer;

    /** Creates a new instance of XincoTreeCellRenderer
     * @param explorer XincoExplorer
     */
    public XincoTreeCellRenderer(XincoExplorer explorer) {
        this.explorer = explorer;
        xfm = new XincoFileIconManager();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
            Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);
        if (leaf) {
            //Only attempt if connected!
            if (explorer.getSession().getXinco() != null) {
                if (!isFolder(value)) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    String ext = value.toString().substring(0, value.toString().indexOf("(") - 1);
                    switch (((XincoCoreData) (node.getUserObject())).getXincoCoreDataType().getId()) {
                        case 1:
                            try {
                                setIcon(xfm.getIcon16(ext));
                            } catch (NullPointerException e) {
                                setIcon(getLeafIcon());
                            }
                            break;
                        case 2:
                            try {
                                setIcon(xfm.getIcon16(".txt"));
                            } catch (NullPointerException e) {
                                setIcon(getLeafIcon());
                            }
                            break;
                        case 3:
                            try {
                                setIcon(xfm.getIcon16(".html"));
                            } catch (NullPointerException e) {
                                setIcon(getLeafIcon());
                            }
                            break;
                        case 4:
                            try {
                                setIcon(new ImageIcon(XincoFileIconManager.class.getResource("contact.gif")));
                            } catch (NullPointerException e) {
                                setIcon(getLeafIcon());
                            }
                            break;
                        default:
                            setIcon(getLeafIcon());
                    }
                    if (this.getIcon() == null) {
                        setIcon(getLeafIcon());
                    }
                }
            }
        } else {
            setToolTipText(null); //no tool tip
            setIcon(getLeafIcon());
        }
        return this;
    }

    protected boolean isFolder(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node != null) {
            if (node.getUserObject().getClass() == XincoCoreNode.class) {
                return true;
            }
        }
        return false;
    }
}
