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
 * Name:            XincoMutableTreeNode
 *
 * Description:     tree node on client side
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
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreNode;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Alexander Manes
 */
public class XincoMutableTreeNode extends DefaultMutableTreeNode {

    private XincoExplorer explorer;

    /**
     * Constructor
     * @param o
     * @param e
     */
    public XincoMutableTreeNode(Object o, XincoExplorer e) {
        super(o);
        explorer = e;
    }

    @Override
    public String toString() {
        String s = null;
        String status = null;
        if (getUserObject() != null) {
            if (getUserObject().getClass() == XincoCoreNode.class) {
                s = ((XincoCoreNode) getUserObject()).getDesignation();
                if (s == null) {
                    s = super.toString();
                }
                status = new String("");
                if (((XincoCoreNode) getUserObject()).getStatus_number() == 2) {
                    status = new String(" | -");
                }
                if (((XincoCoreNode) getUserObject()).getStatus_number() == 3) {
                    status = new String(" | ->");
                }
                return "" + s + " (" + ((XincoCoreNode) getUserObject()).getXinco_core_language().getSign() + status + ")";
            }
            if (getUserObject().getClass() == XincoCoreData.class) {
                s = ((XincoCoreData) getUserObject()).getDesignation();
                if (s == null) {
                    s = super.toString();
                }
                status = new String("");
                if (((XincoCoreData) getUserObject()).getStatus_number() == 2) {
                    status = new String(" | -");
                }
                if (((XincoCoreData) getUserObject()).getStatus_number() == 3) {
                    status = new String(" | ->");
                }
                if (((XincoCoreData) getUserObject()).getStatus_number() == 4) {
                    status = new String(" | X");
                }
                if (((XincoCoreData) getUserObject()).getStatus_number() == 5) {
                    status = new String(" | WWW");
                }
                return "" + s + " (" + explorer.getResourceBundle().getString(((XincoCoreData) getUserObject()).getXinco_core_data_type().getDesignation()) +
                        " | " + ((XincoCoreData) getUserObject()).getXinco_core_language().getSign() + status + ")";
            }
        }
        return super.toString();
    }
}
