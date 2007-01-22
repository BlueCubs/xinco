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
 * Name:            XincoClientRepository
 *
 * Description:     repository on client side 
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

import javax.swing.tree.*;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.service.*;

/**
 * XincoClientRepository
 */
public class XincoClientRepository {
	
    /**
     * DefaultTreeModel
     */
	public DefaultTreeModel treemodel = null;

    /**
     * XincoClientRepository
     */
	public XincoClientRepository() {
		treemodel = new DefaultTreeModel(new XincoMutableTreeNode("root"));
	}
	
    /**
     * Assign object to tree node
     * @param node Node to assign object to.
     * @param object Object to be assigned to node.
     * @param service Xinco service involved.
     * @param user User involved
     * @param depth Node depth.
     */
	public void assignObject2TreeNode(XincoMutableTreeNode node, XincoCoreNode object, Xinco service, XincoCoreUser user, int depth) {
		int i = 0;
		depth--;
		//if node = root -> create new tree
		//if (node.isRoot()) {
			node.removeAllChildren();
			//treemodel.reload(node);
			//treemodel.nodeChanged(node);
		//} else {
		//}
		node.setUserObject(object);
                int size=object.getXinco_core_nodes().size();
		for (i=0;i<size;i++) {
			XincoMutableTreeNode temp_xmtn = new XincoMutableTreeNode(object.getXinco_core_nodes().elementAt(i));
			treemodel.insertNodeInto(temp_xmtn, node, node.getChildCount());
			//expand one more level
			//check for children only if none have been found yet
			if (depth > 0) {
				try {
					XincoCoreNode xnode = service.getXincoCoreNode((XincoCoreNode)object.getXinco_core_nodes().elementAt(i), user);
					if (xnode != null) {
						this.assignObject2TreeNode(temp_xmtn, xnode, service, user, depth);
					} else {
					}
				} catch (Exception rmie) {
				}
			}
		}
		for (i=0;i<object.getXinco_core_data().size();i++) {
			treemodel.insertNodeInto(new XincoMutableTreeNode(object.getXinco_core_data().elementAt(i)), node, node.getChildCount());
		}
		treemodel.reload(node);
		treemodel.nodeChanged(node);
	}

}
