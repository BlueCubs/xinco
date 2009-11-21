/**
 *Copyright 2009 blueCubs.com
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
 * Name:            XincoCoreNodeServer
 *
 * Description:     node object
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
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreAceJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLanguageJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreNodeJpaController;
import com.bluecubs.xinco.index.XincoIndexer;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class XincoCoreNodeServer extends XincoCoreNode {

    private static List result;
    private static HashMap parameters = new HashMap();
    //create node object for data structures

    public XincoCoreNodeServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = XincoDBManager.namedQuery("XincoCoreNode.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreNode xcn =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreNode) result.get(0);
                setId(xcn.getId());
                if (xcn.getXincoCoreNodeId() != null) {
                    setXinco_core_node_id(xcn.getXincoCoreNodeId().getId());
                }
                setXinco_core_language(new XincoCoreLanguageServer(xcn.getXincoCoreLanguageId()));
                setDesignation(xcn.getDesignation());
                setStatus_number(xcn.getStatusNumber());
                setXinco_core_nodes(new Vector());
                setXinco_core_data(new Vector());
                //load acl for this object
                setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(xcn.getId(), "xincoCoreNodeId.id"));
                fillXincoCoreData();
                fillXincoCoreNodes();
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setXinco_core_language(null);
            setXinco_core_acl(null);
            setXinco_core_nodes(null);
            setXinco_core_data(null);
            throw new XincoException(e.getMessage());
        }
    }

    //create node object for data structures
    public XincoCoreNodeServer(int attrID, int attrCNID, int attrLID, String attrD, int attrSN) throws XincoException {
        try {
            setId(attrID);
            setXinco_core_node_id(attrCNID);
            setXinco_core_language(new XincoCoreLanguageServer(attrLID));
            setDesignation(attrD);
            setStatus_number(attrSN);
            setXinco_core_nodes(new Vector());
            setXinco_core_data(new Vector());
            //load acl for this object
            setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreNodeId.id"));
        } catch (Exception e) {
            setXinco_core_language(null);
            ((Vector) getXinco_core_acl()).removeAllElements();
            ((Vector) getXinco_core_nodes()).removeAllElements();
            ((Vector) getXinco_core_data()).removeAllElements();
            throw new XincoException(e.getMessage());
        }
    }

    private XincoCoreNodeServer(com.bluecubs.xinco.core.server.persistence.XincoCoreNode xcn) {
        setId(xcn.getId());
        setXinco_core_node_id(xcn.getXincoCoreNodeId() == null ? 0 : xcn.getXincoCoreNodeId().getId());
        setXinco_core_language(new XincoCoreLanguageServer(xcn.getXincoCoreLanguageId()));
        setDesignation(xcn.getDesignation());
        setStatus_number(xcn.getStatusNumber());
        setXinco_core_nodes(new Vector());
        setXinco_core_data(new Vector());
        //load acl for this object
        setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(xcn.getId(), "xincoCoreNodeId.id"));
        fillXincoCoreData();
        fillXincoCoreNodes();
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            XincoCoreNodeJpaController controller = new XincoCoreNodeJpaController();
            com.bluecubs.xinco.core.server.persistence.XincoCoreNode xcn;
            if (getId() > 0) {
                xcn = controller.findXincoCoreNode(getId());
                if (getXinco_core_node_id() == 0) {
                    xcn.setXincoCoreNodeId(controller.findXincoCoreNode(getXinco_core_node_id()));
                }
                xcn.setXincoCoreLanguageId(new XincoCoreLanguageJpaController().findXincoCoreLanguage(getXinco_core_language().getId()));
                xcn.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcn.setStatusNumber(getStatus_number());
                xcn.setModificationReason("audit.general.modified");
                xcn.setModifierId(getChangerID());
                xcn.setModificationTime(new Timestamp(new Date().getTime()));
                controller.edit(xcn);
            } else {
                setId(XincoDBManager.getNewID("xinco_core_node"));
                xcn = new com.bluecubs.xinco.core.server.persistence.XincoCoreNode(getId());
                if (getXinco_core_node_id() == 0) {
                    xcn.setXincoCoreNodeId(controller.findXincoCoreNode(getXinco_core_node_id()));
                }
                xcn.setXincoCoreLanguageId(new XincoCoreLanguageJpaController().findXincoCoreLanguage(getXinco_core_language().getId()));
                xcn.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcn.setStatusNumber(getStatus_number());
                xcn.setModificationReason("audit.general.create");
                xcn.setModifierId(getChangerID());
                xcn.setModificationTime(new Timestamp(new Date().getTime()));
                controller.create(xcn);
            }
        } catch (Exception e) {
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    //delete from db
    public void deleteFromDB(boolean delete_this, int userID) throws XincoException {
        int i = 0;
        try {
            if (delete_this) {
                result = XincoDBManager.createdQuery("Select xca from XincoCoreAce xca where xca.xincoCoreNodeId.id=" + getId());
                for (Object o : result) {
                    com.bluecubs.xinco.core.server.persistence.XincoCoreAce xca =
                            (com.bluecubs.xinco.core.server.persistence.XincoCoreAce) o;
                    new XincoCoreAceJpaController().destroy(xca.getId());
                }
                //Controller handles all linked data and child nodes
                new XincoCoreNodeJpaController().destroy(getId());
            } else {
                //Special handling for removing children only
                //fill nodes and data
                fillXincoCoreNodes();
                fillXincoCoreData();
                //start recursive deletion
                for (i = 0; i < ((Vector) getXinco_core_nodes()).size(); i++) {
                    ((XincoCoreNodeServer) ((Vector) getXinco_core_nodes()).elementAt(i)).deleteFromDB(true, userID);
                }
                for (i = 0; i < ((Vector) getXinco_core_data()).size(); i++) {
                    XincoIndexer.removeXincoCoreData((XincoCoreDataServer) ((Vector) getXinco_core_data()).elementAt(i));
                    XincoCoreDataServer.removeFromDB(userID,
                            ((XincoCoreDataServer) ((Vector) getXinco_core_data()).elementAt(i)).getId());
                    ((XincoCoreDataServer) ((Vector) getXinco_core_data()).elementAt(i)).setChangerID(userID);
                    ((XincoCoreDataServer) ((Vector) getXinco_core_data()).elementAt(i)).deleteFromDB();
                }
            }
        } catch (Exception e) {
            throw new XincoException(e.getMessage());
        }
    }

    public void fillXincoCoreNodes() {
        try {
            result = XincoDBManager.createdQuery("SELECT xcn FROM XincoCoreNode xcn "
                    + "WHERE xcn.xincoCoreNodeId.id = " + getId() + " ORDER BY xcn.designation");
            for (Object o : result) {
                ((Vector) getXinco_core_nodes()).addElement(new XincoCoreNodeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o));
            }
        } catch (Exception e) {
            ((Vector) getXinco_core_nodes()).removeAllElements();
        }

    }

    public void fillXincoCoreData() {
        try {
            result = XincoDBManager.createdQuery("SELECT xcd FROM XincoCoreData xcd WHERE xcd.xincoCoreNodeId.id = " + getId() + " ORDER BY xcd.designation");
            for (Object o : result) {
                ((Vector) getXinco_core_data()).addElement(new XincoCoreDataServer((com.bluecubs.xinco.core.server.persistence.XincoCoreData) o));
            }
        } catch (Exception e) {
            ((Vector) getXinco_core_data()).removeAllElements();
        }
    }

    public static Vector findXincoCoreNodes(String attrS, int attrLID) {
        Vector nodes = new Vector();
        try {
            result = XincoDBManager.createdQuery("SELECT xcn FROM XincoCoreNode xcn "
                    + "WHERE xcn.xincoCoreLanguageId.id = " + attrLID + " AND "
                    + "xcn.designation LIKE '" + attrS + "%' ORDER BY xcn.designation, xcn.xincoCoreLanguageId.id");
            int i = 0;
            for (Object o : result) {
                nodes.addElement(new XincoCoreNodeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o));
                i++;
                if (i >= XincoDBManager.config.MaxSearchResult) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            nodes.removeAllElements();
        }
        return nodes;
    }

    public static Vector getXincoCoreNodeParents(int attrID) {
        Vector nodes = new Vector();
        int id;
        try {
            id = attrID;
            while (id > 0) {
                parameters.clear();
                parameters.put("id", id);
                result = XincoDBManager.namedQuery("XincoCoreNode.findById", parameters);
                for (Object o : result) {
                    nodes.addElement(new XincoCoreNodeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o));
                    if (id > 1) {
                        if (((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o).getXincoCoreNodeId() == null) {
                            id = 0;
                        } else {
                            id = ((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o).getXincoCoreNodeId().getId();
                        }
                    } else {
                        id = 0;
                    }
                }
            }
        } catch (Exception e) {
            nodes.removeAllElements();
        }
        return nodes;
    }
}
