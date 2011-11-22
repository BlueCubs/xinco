/**
 * Copyright 2011 blueCubs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 *
 * Name: XincoCoreNodeServer
 *
 * Description: node object
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreAceJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLanguageJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreNodeJpaController;
import com.bluecubs.xinco.core.server.service.XincoCoreNode;
import com.bluecubs.xinco.index.XincoIndexer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;

public class XincoCoreNodeServer extends XincoCoreNode {

    private static List result;
    private static HashMap parameters = new HashMap();
    private static final Logger logger = Logger.getLogger(XincoCoreNodeServer.class.getSimpleName());
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
                if (xcn.getXincoCoreNode() != null) {
                    setXincoCoreNodeId(xcn.getXincoCoreNode().getId());
                }
                setXincoCoreLanguage(new XincoCoreLanguageServer(xcn.getXincoCoreLanguage()));
                setDesignation(xcn.getDesignation());
                setStatusNumber(xcn.getStatusNumber());
                getXincoCoreNodes().clear();
                getXincoCoreData().clear();
                //load acl for this object
                getXincoCoreAcl().clear();
                getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(xcn.getId(), "xincoCoreNode.id"));
                fillXincoCoreData();
                fillXincoCoreNodes();
            } else {
                throw new XincoException("Unable to find XincoCoreNode with id: " + attrID);
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
    public XincoCoreNodeServer(int attrID, int attrCNID, int attrLID,
            String attrD, int attrSN) throws XincoException {
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
            getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreNode.id"));
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
        setXincoCoreNodeId(xcn.getXincoCoreNode() == null ? 0 : xcn.getXincoCoreNode().getId());
        setXincoCoreLanguage(new XincoCoreLanguageServer(xcn.getXincoCoreLanguage()));
        setDesignation(xcn.getDesignation());
        setStatusNumber(xcn.getStatusNumber());
        getXincoCoreNodes().clear();
        getXincoCoreData().clear();
        //load acl for this object
        getXincoCoreAcl().clear();
        getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(xcn.getId(), "xincoCoreNode.id"));
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
                if (getXincoCoreNodeId() != 0) {
                    xcn.setXincoCoreNode(controller.findXincoCoreNode(getXincoCoreNodeId()));
                }
                xcn.setXincoCoreLanguage(new XincoCoreLanguageJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreLanguage(getXincoCoreLanguage().getId()));
                xcn.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcn.setStatusNumber(getStatusNumber());
                xcn.setModificationReason("audit.general.modified");
                xcn.setModifierId(getChangerID());
                xcn.setModificationTime(new Timestamp(new Date().getTime()));
                controller.edit(xcn);
            } else {
                xcn = new com.bluecubs.xinco.core.server.persistence.XincoCoreNode(getId());
                if (getXincoCoreNodeId() != 0) {
                    xcn.setXincoCoreNode(controller.findXincoCoreNode(getXincoCoreNodeId()));
                }
                xcn.setXincoCoreLanguage(new XincoCoreLanguageJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreLanguage(getXincoCoreLanguage().getId()));
                xcn.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcn.setStatusNumber(getStatusNumber());
                xcn.setModificationReason("audit.general.create");
                xcn.setModifierId(getChangerID());
                xcn.setModificationTime(new Timestamp(new Date().getTime()));
                controller.create(xcn);
            }
            setId(xcn.getId());
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    //delete from db
    public void deleteFromDB(boolean delete_this, int userID) throws XincoException {
        int i;
        try {
            if (delete_this) {
                result = XincoDBManager.createdQuery("Select xca from XincoCoreAce xca where xca.xincoCoreNode.id=" + getId());
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
            logger.log(Level.SEVERE, null, e);
            throw new XincoException(e.getMessage());
        }
    }

    public final void fillXincoCoreNodes() {
        try {
            getXincoCoreNodes().clear();
            result = XincoDBManager.createdQuery("SELECT xcn FROM XincoCoreNode xcn "
                    + "WHERE xcn.xincoCoreNode.id = " + getId() + " ORDER BY xcn.designation");
            for (Object o : result) {
                ((ArrayList) getXincoCoreNodes()).add(new XincoCoreNodeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            getXincoCoreNodes().clear();
        }

    }

    public final void fillXincoCoreData() {
        try {
            getXincoCoreData().clear();
            result = XincoDBManager.createdQuery("SELECT xcd FROM XincoCoreData xcd WHERE xcd.xincoCoreNode.id = " + getId() + " ORDER BY xcd.designation");
            for (Object o : result) {
                ((ArrayList) getXincoCoreData()).add(new XincoCoreDataServer((com.bluecubs.xinco.core.server.persistence.XincoCoreData) o));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            getXincoCoreData().clear();
        }
    }

    public static ArrayList findXincoCoreNodes(String attrS, int attrLID) {
        ArrayList nodes = new ArrayList();
        try {
            result = XincoDBManager.createdQuery("SELECT xcn FROM XincoCoreNode xcn "
                    + "WHERE xcn.xincoCoreLanguage.id = " + attrLID + " AND "
                    + "xcn.designation LIKE '" + attrS + "%' ORDER BY xcn.designation, xcn.xincoCoreLanguage.id");
            int i = 0;
            for (Object o : result) {
                nodes.add(new XincoCoreNodeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o));
                i++;
                if (i >= Lookup.getDefault().lookup(ConfigurationManager.class).getMaxSearchResult()) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
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
                        if (((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o).getXincoCoreNode() == null) {
                            id = 0;
                        } else {
                            id = ((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) o).getXincoCoreNode().getId();
                        }
                    } else {
                        id = 0;
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            nodes.clear();
        }
        return nodes;
    }
}
