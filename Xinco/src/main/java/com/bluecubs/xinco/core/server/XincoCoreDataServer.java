/**
 * Copyright 2012 blueCubs.com
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
 * Name: XincoCoreDataServer
 *
 * Description: data object
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.XincoAddAttributeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreAceJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLogJpaController;
import com.bluecubs.xinco.core.server.service.XincoAddAttribute;
import com.bluecubs.xinco.core.server.service.XincoCoreData;
import com.bluecubs.xinco.core.server.service.XincoCoreLog;
import com.bluecubs.xinco.core.server.service.XincoVersion;
import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class XincoCoreDataServer extends XincoCoreData {

    private static HashMap<String, Object> parameters =
            new HashMap<String, Object>();
    private static List<Object> result;
    private static final Logger LOG = 
            Logger.getLogger(XincoCoreDataServer.class.getName());
    //create data object for data structures

    /**
     * This will get the latest major revision. Basically the last version with
     * a '0' as a mid version
     *
     * @param xincoCoreDataId
     * @return Path to last major version
     * @throws SQLException
     * @throws XincoException
     */
    public static String getLastMajorVersionDataPath(int xincoCoreDataId) throws SQLException, XincoException {
        XincoCoreDataServer temp = new XincoCoreDataServer(xincoCoreDataId);
        temp.loadLogs();
        for (Iterator<Object> it = temp.getXincoCoreLogs().iterator(); it.hasNext();) {
            XincoCoreLog log = (XincoCoreLog) it.next();
            if (log.getVersion().getVersionMid() == 0) {
                return XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.fileRepositoryPath, xincoCoreDataId, xincoCoreDataId + "-" + log.getId());
            }
        }
        throw new XincoException("No major log history for XincoCoreData with id: " + xincoCoreDataId);
    }

    public XincoCoreDataServer(int attrID) throws XincoException {
        try {
            XincoCoreDataJpaController controller = new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory());
            com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd = controller.findXincoCoreData(attrID);
            //throw exception if no result found
            if (xcd != null) {
                setId(xcd.getId());
                setXincoCoreNodeId(xcd.getXincoCoreNode().getId());
                setXincoCoreLanguage(new XincoCoreLanguageServer(xcd.getXincoCoreLanguage().getId()));
                setXincoCoreDataType(new XincoCoreDataTypeServer(xcd.getXincoCoreDataType().getId()));
                //load logs
                loadLogs();
                //load add attributes
                loadAddAttributes();
                setDesignation(xcd.getDesignation());
                setStatusNumber(xcd.getStatusNumber());
                //load acl for this object
                loadACL();
            } else {
                throw new XincoException("Unable to find XincoCoreData with id: " + attrID);
            }
        } catch (Exception e) {
            getXincoCoreAcl().clear();
            throw new XincoException();
        }
    }
    public static XincoVersion getCurrentVersion(int xincoCoreDataId) {
        try {
            XincoCoreDataServer temp = new XincoCoreDataServer(xincoCoreDataId);
            temp.loadLogs();
            if (!temp.getXincoCoreLogs().isEmpty()) {
                return ((XincoCoreLog) temp.getXincoCoreLogs().get(temp.getXincoCoreLogs().size() - 1)).getVersion();
            } else {
                return null;
            }
        } catch (XincoException e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    //create data object for data structures
    public XincoCoreDataServer(int attrID, int attrCNID, int attrLID, int attrDTID, String attrD, int attrSN) throws XincoException {
        setId(attrID);
        setXincoCoreNodeId(attrCNID);
        setXincoCoreLanguage(new XincoCoreLanguageServer(attrLID));
        setXincoCoreDataType(new XincoCoreDataTypeServer(attrDTID));
        //load logs
        getXincoCoreLogs().clear();
        getXincoCoreLogs().addAll(XincoCoreLogServer.getXincoCoreLogs(attrID));
        //security: don't load add attribute, force direct access to data including check of access rights!
        getXincoAddAttributes().clear();
        setDesignation(attrD);
        setStatusNumber(attrSN);
        //load acl for this object
        getXincoCoreAcl().clear();
        getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreData.id"));
    }
    public void loadAddAttributes() {
        getXincoAddAttributes().clear();
        getXincoAddAttributes().addAll(XincoAddAttributeServer.getXincoAddAttributes(getId()));
    }

    public static XincoVersion getLastMajorVersion(int xincoCoreDataId) throws XincoException {
        XincoCoreDataServer temp = new XincoCoreDataServer(xincoCoreDataId);
        temp.loadLogs();
        for (Iterator<Object> it = temp.getXincoCoreLogs().iterator(); it.hasNext();) {
            XincoCoreLog log = (XincoCoreLog) it.next();
            if (log.getVersion().getVersionMid() == 0) {
                return log.getVersion();
            }
        }
        return null;
    }
    public XincoCoreDataServer(com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd) throws XincoException {
        try {
            setId(xcd.getId());
            setXincoCoreNodeId(xcd.getXincoCoreNode().getId());
            setXincoCoreLanguage(new XincoCoreLanguageServer(xcd.getXincoCoreLanguage().getId()));
            setXincoCoreDataType(new XincoCoreDataTypeServer(xcd.getXincoCoreDataType().getId()));
            //load logs
            getXincoCoreLogs().clear();
            getXincoCoreLogs().addAll(XincoCoreLogServer.getXincoCoreLogs(xcd.getId()));
            //load add attributes
            getXincoAddAttributes().clear();
            getXincoAddAttributes().addAll(XincoAddAttributeServer.getXincoAddAttributes(xcd.getId()));
            setDesignation(xcd.getDesignation());
            setStatusNumber(xcd.getStatusNumber());
            //load acl for this object
            getXincoCoreAcl().clear();
            getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(xcd.getId(), "xincoCoreData.id"));
        } catch (XincoException ex) {
            getXincoCoreAcl().clear();
            throw new XincoException();
        }
    }

    public void loadLogs() {
        getXincoCoreLogs().clear();
        getXincoCoreLogs().addAll(XincoCoreLogServer.getXincoCoreLogs(getId()));
    }

    public static void removeFromDB(int userID, int id) throws XincoException {
        if (!XincoDBManager.createdQuery("SELECT x FROM XincoCoreData x WHERE x.id = " + id).isEmpty()) {
            try {
                //Related logs
                result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreLog x WHERE x.xincoCoreData.id=" + id);
                for (Iterator it = result.iterator(); it.hasNext();) {
                    com.bluecubs.xinco.core.server.persistence.XincoCoreLog log =
                            (com.bluecubs.xinco.core.server.persistence.XincoCoreLog) it.next();
                    new XincoCoreLogJpaController(XincoDBManager.getEntityManagerFactory()).destroy(log.getId());
                }
                //Related ACEs
                result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreAce x WHERE x.xincoCoreData.id=" + id);
                for (Iterator it = result.iterator(); it.hasNext();) {
                    com.bluecubs.xinco.core.server.persistence.XincoCoreAce ace =
                            (com.bluecubs.xinco.core.server.persistence.XincoCoreAce) it.next();
                    new XincoCoreAceJpaController(XincoDBManager.getEntityManagerFactory()).destroy(ace.getId());
                }
                //Related attributes
                result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE x.xincoCoreData.id=" + id);
                for (Iterator it = result.iterator(); it.hasNext();) {
                    com.bluecubs.xinco.core.server.persistence.XincoAddAttribute attr =
                            (com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) it.next();
                    new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).destroy(attr.getXincoAddAttributePK());
                }
                for (com.bluecubs.xinco.core.server.persistence.XincoCoreData next : 
                        XincoCoreDataHasDependencyServer.getRenderings(id)) {
                    removeFromDB(userID, next.getId());
                }
                //Data itself
                new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory()).destroy(id);
            } catch (Exception e) {
                Logger.getLogger(XincoCoreDataServer.class.getSimpleName()).log(Level.SEVERE, null, e);
                throw new XincoException(e.getLocalizedMessage());
            }
        }
    }
    public void loadACL() {
        getXincoCoreAcl().clear();
        getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreData.id"));
    }

    public static List<XincoCoreData> findXincoCoreData(String attrS, int attrLID, boolean attrSA, boolean attrSFD) {
        ArrayList<XincoCoreData> data = new ArrayList<XincoCoreData>();
        try {
            String lang = "";
            if (attrLID != 0) {
                lang = "AND (x.xincoCoreData.xincoCoreLanguage.id = " + attrLID + ")";
            }
            if (attrSA) {
                result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE (x.xincoCoreData.designation LIKE '"
                        + attrS + "%' or " + "x.attribVarchar  LIKE '" + attrS + "%' or x.attribText LIKE '" + attrS + "') "
                        + lang + "order by x.xincoCoreData.designation, x.xincoCoreData.xincoCoreLanguage.id");
            } else {
                result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE x.xincoCoreData.designation LIKE '"
                        + attrS + "%' " + lang + "order by x.xincoCoreData.designation, x.xincoCoreData.xincoCoreLanguage.id");
            }
            int i = 0;
            for (Object o : result) {
                data.add(new XincoCoreDataServer(((com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) o).getXincoCoreData().getId()));
                i++;
                if (i >= XincoDBManager.config.getMaxSearchResult()) {
                    break;
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            data.clear();
        }
        return data;
    }

    //write to db
    @SuppressWarnings("unchecked")
    public int write2DB() throws XincoException {
        boolean create;
        com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd;
        XincoCoreDataJpaController controller = new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory());
        try {
            if (getId() > 0) {
                xcd = controller.findXincoCoreData(getId());
                parameters.clear();
                parameters.put("id", getXincoCoreNodeId());
                xcd.setXincoCoreNode((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) XincoDBManager.namedQuery("XincoCoreNode.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXincoCoreDataType().getId());
                xcd.setXincoCoreDataType((com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) XincoDBManager.namedQuery("XincoCoreDataType.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXincoCoreLanguage().getId());
                xcd.setXincoCoreLanguage((com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage) XincoDBManager.namedQuery("XincoCoreLanguage.findById", parameters).get(0));
                xcd.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcd.setStatusNumber(getStatusNumber());
                xcd.setModificationReason("audit.data.change");
                xcd.setModifierId(getChangerID());
                xcd.setModificationTime(new Timestamp(new Date().getTime()));
                controller.edit(xcd);
            } else {
                xcd = new com.bluecubs.xinco.core.server.persistence.XincoCoreData();
                parameters.clear();
                parameters.put("id", this.getXincoCoreNodeId());
                xcd.setXincoCoreNode((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) XincoDBManager.namedQuery("XincoCoreNode.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXincoCoreDataType().getId());
                xcd.setXincoCoreDataType((com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) XincoDBManager.namedQuery("XincoCoreDataType.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXincoCoreLanguage().getId());
                xcd.setXincoCoreLanguage((com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage) XincoDBManager.namedQuery("XincoCoreLanguage.findById", parameters).get(0));
                xcd.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcd.setStatusNumber(getStatusNumber());
                xcd.setModificationReason("audit.general.create");
                xcd.setModifierId(getChangerID());
                xcd.setModificationTime(new Timestamp(new Date().getTime()));
                controller.create(xcd);
                setId(xcd.getId());
            }
            //Update add attributes
            for (Iterator<XincoAddAttribute> it = getXincoAddAttributes().iterator(); it.hasNext();) {
                /**
                 * Copy fields from XincoAddAttribute to XincoAddAttribute
                 * (Persistence) The delete and create approach doesn't go well
                 * with persistence.
                 */
                com.bluecubs.xinco.core.server.service.XincoAddAttribute temp = it.next();
                com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa;
                //Try to retrieve from database
                xaa = new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).findXincoAddAttribute(new XincoAddAttributePK(getId(),
                        temp.getAttributeId()));
                create = xaa == null;
                if (create) {
                    //Not in the database yet, get ready to create it.
                    xaa = new com.bluecubs.xinco.core.server.persistence.XincoAddAttribute();
                    xaa.setXincoAddAttributePK(new XincoAddAttributePK(xcd.getId(), temp.getAttributeId()));
                }
                xaa.setAttribInt(temp.getAttribInt());
                xaa.setAttribUnsignedint(temp.getAttribUnsignedint());
                xaa.setAttribDouble(temp.getAttribDouble());
                xaa.setAttribVarchar(temp.getAttribVarchar());
                xaa.setAttribText(temp.getAttribText());
                xaa.setAttribDatetime(temp.getAttribDatetime().toGregorianCalendar().getTime());
                xaa.setXincoCoreData(xcd);
                if (create) {
                    new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).create(xaa);
                } else {
                    new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).edit(xaa);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    public static String getXincoCoreDataPath(String attrRP, int attrID, String attrFN) {
        String path;
        // convert ID to String
        String path4Id = "" + attrID;
        // fill ID String with zeros
        while (path4Id.length() < 10) {
            path4Id = "0" + path4Id;
        }
        // shorten to 7 chars
        path4Id = path4Id.substring(0, 7);
        // add seperator
        for (int i = 0; i < 7; i++) {
            path4Id = path4Id.substring(0, (i * 2 + 1)) + System.getProperty("file.separator") + path4Id.substring((i * 2 + 1));
        }
        // create path if neccessary
        (new File(attrRP + path4Id)).mkdirs();
        // check if file exists at NEW location (>= xinco DMS 2.0)
        if ((new File(attrRP + path4Id + attrFN)).exists()) {
            // output NEW location
            path = attrRP + path4Id + attrFN;
        } else {
            // check if file exists at OLD location (pre xinco DMS 2.0)
            if ((new File(attrRP + attrFN)).exists()) {
                // output OLD location
                path = attrRP + attrFN;
            } else {
                // output NEW location for NEW file
                path = attrRP + path4Id + attrFN;
            }
        }
        return path;
    }
    public int deleteFromDB() throws XincoException {
        try {
            //delete file / file = 1
            if (getXincoCoreDataType().getId() == 1) {
                try {
                    (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.fileRepositoryPath, getId(), "" + getId()))).delete();
                } catch (Exception dfe) {
                    Logger.getLogger(XincoCoreDataServer.class.getSimpleName()).log(Level.WARNING, null, dfe);
                    // continue, file might not exist
                }
                // delete revisions created upon creation or checkin
                for (int i = 0; i < getXincoCoreLogs().size(); i++) {
                    if ((((XincoCoreLog) getXincoCoreLogs().get(i)).getOpCode() == 1) || (((XincoCoreLog) getXincoCoreLogs().get(i)).getOpCode() == 5)) {
                        try {
                            (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.fileRepositoryPath, getId(), getId() + "-" + ((XincoCoreLog) getXincoCoreLogs().get(i)).getId()))).delete();
                        } catch (Exception drfe) {
                            Logger.getLogger(XincoCoreDataServer.class.getSimpleName()).log(Level.WARNING, null, drfe);
                            // continue, delete next revision
                        }
                    }
                }
            }
            //Delete related attributes
            loadAddAttributes();
            for (XincoAddAttribute attr : getXincoAddAttributes()) {
                new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).destroy(new XincoAddAttributePK(attr.getXincoCoreDataId(), attr.getAttributeId()));
            }
            //Delete related logs
            loadLogs();
            for (Iterator<Object> it = getXincoCoreLogs().iterator(); it.hasNext();) {
                XincoCoreLog log = (XincoCoreLog) it.next();
                new XincoCoreLogJpaController(XincoDBManager.getEntityManagerFactory()).destroy(log.getId());
            }
            new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory()).destroy(getId());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            throw new XincoException(e.getLocalizedMessage());
        }
        return 0;
    }

    public static boolean isArchived(int id) {
        return isArchived(new XincoCoreDataServer(id));
    }

    public XincoAddAttributeServer getAttribute(int id) {
        XincoAddAttributeServer xaas = null;
        for (Iterator<XincoAddAttribute> it = getXincoAddAttributes().iterator(); it.hasNext();) {
            XincoAddAttribute attr = it.next();
            if (attr.getAttributeId() == id) {
                try {
                    xaas = new XincoAddAttributeServer(attr.getXincoCoreDataId(), attr.getAttributeId());
                    break;
                } catch (XincoException ex) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return xaas;
    }

    public static boolean isArchived(XincoCoreDataServer xcds) {
        boolean res = false;
        //Make sure the logs are filled
        for (Iterator<Object> it = xcds.getXincoCoreLogs().iterator(); it.hasNext();) {
            XincoCoreLog log = (XincoCoreLog) it.next();
            if (log.getOpCode() == OPCode.ARCHIVED.ordinal() + 1) {
                res = true;
                break;
            }
        }
        return res;
    }
}
