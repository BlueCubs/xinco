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

import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreAceJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLanguageJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreNodeJpaController;
import com.bluecubs.xinco.index.XincoIndexer;
import com.bluecubs.xinco.server.service.XincoCoreNode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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
                    setXincoCoreNodeId(xcn.getXincoCoreNodeId().getId());
                }
                setXincoCoreLanguage(new XincoCoreLanguageServer(xcn.getXincoCoreLanguageId()));
                setDesignation(xcn.getDesignation());
                setStatusNumber(xcn.getStatusNumber());
                getXincoCoreNodes().clear();
                getXincoCoreData().clear();
                //load acl for this object
                getXincoCoreAcl().clear();
                getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(xcn.getId(), "xincoCoreNodeId.id"));
                fillXincoCoreData();
                fillXincoCoreNodes();
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            setXincoCoreLanguage(null);
            getXincoCoreNodes().clear();
            getXincoCoreData().clear();
            getXincoCoreData().clear();
            throw new XincoException(e.getMessage());
        }
    }

    //create node object for data structures
    public XincoCoreNodeServer(int attrID, int attrCNID, int attrLID, String attrD, int attrSN) throws XincoException {
        try {
            setId(attrID);
            setXincoCoreNodeId(attrCNID);
            setXincoCoreLanguage(new XincoCoreLanguageServer(attrLID));
            setDesignation(attrD);
            setStatusNumber(attrSN);
            getXincoCoreNodes().clear();
            getXincoCoreData().clear();
            //load acl for this object
            getXincoCoreAcl().clear();
            getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreNodeId.id"));
        } catch (Exception e) {
            setXincoCoreLanguage(null);
            ((ArrayList) getXincoCoreAcl()).clear();
            ((ArrayList) getXincoCoreNodes()).clear();
            ((ArrayList) getXincoCoreData()).clear();
            throw new XincoException(e.getMessage());
        }
    }

    private XincoCoreNodeServer(com.bluecubs.xinco.core.server.persistence.XincoCoreNode xcn) {
        setId(xcn.getId());
        setXincoCoreNodeId(xcn.getXincoCoreNodeId() == null ? 0 : xcn.getXincoCoreNodeId().getId());
        setXincoCoreLanguage(new XincoCoreLanguageServer(xcn.getXincoCoreLanguageId()));
        setDesignation(xcn.getDesignation());
        setStatusNumber(xcn.getStatusNumber());
        getXincoCoreNodes().clear();
        getXincoCoreData().clear();
        //load acl for this object
        getXincoCoreAcl().clear();
        getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(xcn.getId(), "xincoCoreNodeId.id"));
        fillXincoCoreData();
        fillXincoCoreNodes();
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            XincoCoreNodeJpaController controller = new XincoCoreNodeJpaController(XincoDBManager.getEntityManagerFactory());
            com.bluecubs.xinco.core.server.persistence.XincoCoreNode xcn;
            if (getId() > 0) {
                xcn = controller.findXincoCoreNode(getId());
                if (getXincoCoreNodeId() == 0) {
                    xcn.setXincoCoreNodeId(controller.findXincoCoreNode(getXincoCoreNodeId()));
                }
                xcn.setXincoCoreLanguageId(new XincoCoreLanguageJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreLanguage(getXincoCoreLanguage().getId()));
                xcn.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcn.setStatusNumber(getStatusNumber());
                xcn.setModificationReason("audit.general.modified");
                xcn.setModifierId(getChangerID());
                xcn.setModificationTime(new Timestamp(new Date().getTime()));
                controller.edit(xcn);
            } else {
                setId(XincoDBManager.getNewID("xincoCoreNode"));
                xcn = new com.bluecubs.xinco.core.server.persistence.XincoCoreNode(getId());
                if (getXincoCoreNodeId() == 0) {
                    xcn.setXincoCoreNodeId(controller.findXincoCoreNode(getXincoCoreNodeId()));
                }
                xcn.setXincoCoreLanguageId(new XincoCoreLanguageJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreLanguage(getXincoCoreLanguage().getId()));
                xcn.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcn.setStatusNumber(getStatusNumber());
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
                    new XincoCoreAceJpaController(XincoDBManager.getEntityManagerFactory()).destroy(xca.getId());
                }
                //Controller handles all linked data and child nodes
                new XincoCoreNodeJpaController(XincoDBManager.getEntityManagerFactory()).destroy(getId());
            } else {
                //Special handling for removing children only
                //fill nodes and data
                fillXincoCoreNodes();
                fillXincoCoreData();
                //start recursive deletion
                for (i = 0; i < ((ArrayList) getXincoCoreNodes()).size(); i++) {
                    ((XincoCoreNodeServer) ((ArrayList) getXincoCoreNodes()).get(i)).deleteFromDB(true, userID);
                }
                for (i = 0; i < ((ArrayList) getXincoCoreData()).size(); i++) {
                    XincoIndexer.removeXincoCoreData((XincoCoreDataServer) ((ArrayList) getXincoCoreData()).get(i));
                    XincoCoreDataServer.removeFromDB(userID,
                            ((XincoCoreDataServer) ((ArrayList) getXincoCoreData()).get(i)).getId());
                    ((XincoCoreDataServer) ((ArrayList) getXincoCoreData()).get(i)).setChangerID(userID);
                    ((XincoCoreDataServer) ((ArrayList) getXincoCoreData()).get(i)).deleteFromDB();
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
                ((ArrayList) getXincoCoreNodes()).add(new XincoCoreNodeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o));
            }
        } catch (Exception e) {
            ((ArrayList) getXincoCoreNodes()).clear();
        }

    }

    public void fillXincoCoreData() {
        try {
            result = XincoDBManager.createdQuery("SELECT xcd FROM XincoCoreData xcd WHERE xcd.xincoCoreNodeId.id = " + getId() + " ORDER BY xcd.designation");
            for (Object o : result) {
                ((ArrayList) getXincoCoreData()).add(new XincoCoreDataServer((com.bluecubs.xinco.core.server.persistence.XincoCoreData) o));
            }
        } catch (Exception e) {
            ((ArrayList) getXincoCoreData()).clear();
        }
    }

    public static ArrayList findXincoCoreNodes(String attrS, int attrLID) {
        ArrayList nodes = new ArrayList();
        try {
            result = XincoDBManager.createdQuery("SELECT xcn FROM XincoCoreNode xcn "
                    + "WHERE xcn.xincoCoreLanguageId.id = " + attrLID + " AND "
                    + "xcn.designation LIKE '" + attrS + "%' ORDER BY xcn.designation, xcn.xincoCoreLanguageId.id");
            int i = 0;
            for (Object o : result) {
                nodes.add(new XincoCoreNodeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o));
                i++;
                if (i >= XincoDBManager.config.MaxSearchResult) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            nodes.clear();
        }
        return nodes;
    }

    public static ArrayList getXincoCoreNodeParents(int attrID) {
        ArrayList nodes = new ArrayList();
        int id;
        try {
            id = attrID;
            while (id > 0) {
                parameters.clear();
                parameters.put("id", id);
                result = XincoDBManager.namedQuery("XincoCoreNode.findById", parameters);
                for (Object o : result) {
                    nodes.add(new XincoCoreNodeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o));
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
            nodes.clear();
        }
        return nodes;
    }
}
