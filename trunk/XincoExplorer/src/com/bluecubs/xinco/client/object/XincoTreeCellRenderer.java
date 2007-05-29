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
 * Original Author: Javier Ortiz
 * Date:            May 25, 2007, 10:01 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.client.XincoMutableTreeNode;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.tools.XincoFileIconManager;
import java.awt.Component;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author javydreamercsw
 */
public class XincoTreeCellRenderer extends DefaultTreeCellRenderer{
    private XincoFileIconManager xfm=null;
    /** Creates a new instance of XincoTreeCellRenderer */
    public XincoTreeCellRenderer() {
        xfm = new XincoFileIconManager();
    }
    
    public Component getTreeCellRendererComponent(JTree tree,
            Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);
        if (leaf) {
            System.out.println("Leaf: "+value.toString());
//            XincoMutableTreeNode node=(XincoMutableTreeNode)value;
//            Vector attributes=((XincoCoreData)node.getUserObject()).getXinco_add_attributes();
//            String fileName="";
//            for(int i=0;i<attributes.size();i++){
//                if(((XincoAddAttribute)attributes.get(i)).getAttribute_id()==1){
//                    fileName=((XincoAddAttribute)attributes.get(i)).getAttrib_varchar();
//                    break;
//                }
//            }
//            setIcon(xfm.getIcon16(fileName.substring(fileName.indexOf("."))));
        } else {
            setToolTipText(null); //no tool tip
        }
        return this;
    }
}
