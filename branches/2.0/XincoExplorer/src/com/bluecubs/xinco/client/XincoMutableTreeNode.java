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

package com.bluecubs.xinco.client;

import javax.swing.tree.DefaultMutableTreeNode;

import com.bluecubs.xinco.core.*;

public class XincoMutableTreeNode extends DefaultMutableTreeNode {
    private XincoExplorer explorer;
    public XincoMutableTreeNode(Object o, XincoExplorer e) {
        super(o);
        explorer=e;
    }
    
    @Override
    public String toString() {
        String s = null;
        String status = null;
        if (this.getUserObject() != null) {
            if (this.getUserObject().getClass() == XincoCoreNode.class) {
                s = ((XincoCoreNode)this.getUserObject()).getDesignation();
                if (s == null) { s = super.toString(); }
                status = new String("");
                if (((XincoCoreNode)this.getUserObject()).getStatusNumber() == 2) {
                    status = new String(" | -");
                }
                if (((XincoCoreNode)this.getUserObject()).getStatusNumber() == 3) {
                    status = new String(" | ->");
                }
                return "" + s + " (" + ((XincoCoreNode)this.getUserObject()).getXincoCoreLanguage().getSign() + status + ")";
            }
            if (this.getUserObject().getClass() == XincoCoreData.class) {
                s = ((XincoCoreData)this.getUserObject()).getDesignation();
                if (s == null) { s = super.toString(); }
                status = new String("");
                if (((XincoCoreData)this.getUserObject()).getStatusNumber() == 2) {
                    status = new String(" | -");
                }
                if (((XincoCoreData)this.getUserObject()).getStatusNumber() == 3) {
                    status = new String(" | ->");
                }
                if (((XincoCoreData)this.getUserObject()).getStatusNumber() == 4) {
                    status = new String(" | X");
                }
                if (((XincoCoreData)this.getUserObject()).getStatusNumber() == 5) {
                    status = new String(" | WWW");
                }
                return "" + s + " (" + explorer.getResourceBundle().getString(((XincoCoreData)this.getUserObject()).getXincoCoreDataType().getDesignation()) +
                        " | " + ((XincoCoreData)this.getUserObject()).getXincoCoreLanguage().getSign() + status + ")";
            }
        }
        return super.toString();
    } 
}
