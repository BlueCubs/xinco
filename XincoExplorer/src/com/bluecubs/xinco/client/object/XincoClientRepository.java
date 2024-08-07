/**
 *Copyright 2010 blueCubs.com
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
package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;
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
    private XincoExplorer explorer;

    /**
     * XincoClientRepository
     * @param e
     */
    public XincoClientRepository(XincoExplorer e) {
        explorer = e;
        treemodel = new DefaultTreeModel(new XincoMutableTreeNode("root", getExplorer()));
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
        node.removeAllChildren();
        node.setUserObject(object);
        int size = object.getXinco_core_nodes().size();
        for (i = 0; i < size; i++) {
            XincoMutableTreeNode temp_xmtn = new XincoMutableTreeNode(object.getXinco_core_nodes().elementAt(i), getExplorer());
            treemodel.insertNodeInto(temp_xmtn, node, node.getChildCount());
        }
        for (i = 0; i < object.getXinco_core_data().size(); i++) {
            treemodel.insertNodeInto(new XincoMutableTreeNode(object.getXinco_core_data().elementAt(i), getExplorer()), node, node.getChildCount());
        }
        treemodel.reload(node);
        treemodel.nodeChanged(node);
    }

    /**
     * @return the explorer
     */
    public XincoExplorer getExplorer() {
        return explorer;
    }
}
