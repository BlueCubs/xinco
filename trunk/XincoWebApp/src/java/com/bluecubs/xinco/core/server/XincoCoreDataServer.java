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
 * Name:            XincoCoreDataServer
 *
 * Description:     data object
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

import java.util.ArrayList;
import java.io.File;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.XincoAddAttributeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreAceJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLogJpaController;
import com.bluecubs.xinco.server.service.XincoAddAttribute;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreLog;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class XincoCoreDataServer extends XincoCoreData {

    private XincoCoreUserServer user;
    private static HashMap parameters = new HashMap();
    private static List result;
    //create data object for data structures

    public XincoCoreDataServer(int attrID) throws XincoException {
        try {
            XincoCoreDataJpaController controller = new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory());
            com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd = controller.findXincoCoreData(attrID);
            //throw exception if no result found
            if (xcd != null) {
                setId(xcd.getId());
                setXincoCoreNodeId(xcd.getXincoCoreNodeId().getId());
                setXincoCoreLanguage(new XincoCoreLanguageServer(xcd.getXincoCoreLanguageId().getId()));
                setXincoCoreDataType(new XincoCoreDataTypeServer(xcd.getXincoCoreDataTypeId().getId()));
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
                getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(xcd.getId(), "xincoCoreDataId.id"));
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            getXincoCoreAcl().clear();
            throw new XincoException();
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
        getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreDataId.id"));
    }

    public XincoCoreDataServer(com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd) throws XincoException {
        try {
            setId(xcd.getId());
            setXincoCoreNodeId(xcd.getXincoCoreNodeId().getId());
            setXincoCoreLanguage(new XincoCoreLanguageServer(xcd.getXincoCoreLanguageId().getId()));
            setXincoCoreDataType(new XincoCoreDataTypeServer(xcd.getXincoCoreDataTypeId().getId()));
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
            getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(xcd.getId(), "xincoCoreDataId.id"));
        } catch (XincoException ex) {
            getXincoCoreAcl().clear();
            throw new XincoException();
        }
    }

    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }

    /**
     * This will get the latest major revision. Basically the last version
     * with a '0' as a mid version
     * @param xincoCoreDataId
     * @return Path to last major version
     * @throws SQLException
     * @throws XincoException
     */
    public static String getLastMajorVersionDataPath(int xincoCoreDataId) throws SQLException, XincoException {
        parameters.clear();
        parameters.put("xcdi", xincoCoreDataId);
        parameters.put("opcode", OPCode.CHECKIN.ordinal() + 1);
        result = XincoDBManager.createdQuery("select xcl from XincoCoreLog xcl"
                + "where xcl.xincoCoreDataId.id =:xcdi and xcl.versionMid='0' "
                + "and xcl.opCode=:opcode order by xcl.id desc");
        if (result.size() > 0) {
            com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd = (com.bluecubs.xinco.core.server.persistence.XincoCoreData) result.get(0);
            XincoCoreLogServer log = new XincoCoreLogServer(xcd.getId());
            return XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, xincoCoreDataId, xincoCoreDataId + "-" + log.getId());
        } else {
            throw new XincoException("No major log history for XincoCoreData with id: " + xincoCoreDataId);
        }
    }

    //write to db
    public int write2DB() throws XincoException {
        int i = 0;
        XincoCoreDataJpaController controller = new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory());
        try {
            if (getId() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd = controller.findXincoCoreData(getId());
                parameters.clear();
                parameters.put("id", getXincoCoreNodeId());
                xcd.setXincoCoreNodeId((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) XincoDBManager.namedQuery("XincoCoreNode.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXincoCoreDataType().getId());
                xcd.setXincoCoreLanguageId((com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage) XincoDBManager.namedQuery("XincoCoreLanguage.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXincoCoreLanguage().getId());
                xcd.setXincoCoreDataTypeId((com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) XincoDBManager.namedQuery("XincoCoreDataType.findById", parameters).get(0));
                xcd.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcd.setStatusNumber(getStatusNumber());
                xcd.setModificationReason("audit.data.change");
                xcd.setModifierId(getChangerID());
                xcd.setModificationTime(new Timestamp(new Date().getTime()));
                controller.edit(xcd);
            } else {
                setId(XincoDBManager.getNewID("xincoCoreData"));
                com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd = new com.bluecubs.xinco.core.server.persistence.XincoCoreData();
                xcd.setId(getId());
                parameters.clear();
                parameters.put("id", this.getXincoCoreNodeId());
                xcd.setXincoCoreNodeId((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) XincoDBManager.namedQuery("XincoCoreNode.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXincoCoreDataType().getId());
                xcd.setXincoCoreLanguageId((com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage) XincoDBManager.namedQuery("XincoCoreLanguage.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXincoCoreLanguage().getId());
                xcd.setXincoCoreDataTypeId((com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) XincoDBManager.namedQuery("XincoCoreDataType.findById", parameters).get(0));
                xcd.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcd.setStatusNumber(getStatusNumber());
                xcd.setModificationReason("audit.general.create");
                xcd.setModifierId(getChangerID());
                xcd.setModificationTime(new Timestamp(new Date().getTime()));
                controller.create(xcd);
            }
            //Update add attributes
            for (i = 0; i < getXincoAddAttributes().size(); i++) {
                /**
                 * Copy fields from XincoAddAttribute to XincoAddAttribute (Persistence)
                 * The delete and create approach doesn't go well with persistence.
                 */
                com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                        new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).findXincoAddAttribute(new XincoAddAttributePK(getId(),
                        ((XincoAddAttribute) getXincoAddAttributes().get(i)).getAttributeId()));
                xaa.setAttribInt(((XincoAddAttribute) getXincoAddAttributes().get(i)).getAttribInt());
                xaa.setAttribUnsignedint(((XincoAddAttribute) getXincoAddAttributes().get(i)).getAttribUnsignedint());
                xaa.setAttribDouble(((XincoAddAttribute) getXincoAddAttributes().get(i)).getAttribDouble());
                xaa.setAttribVarchar(((XincoAddAttribute) getXincoAddAttributes().get(i)).getAttribVarchar());
                xaa.setAttribText(((XincoAddAttribute) getXincoAddAttributes().get(i)).getAttribText());
                xaa.setAttribDatetime(((XincoAddAttribute) getXincoAddAttributes().get(i)).getAttribDatetime().toGregorianCalendar().getTime());
                new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).edit(xaa);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    public static void removeFromDB(int userID, int id) throws XincoException {
        try {
            result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreLog x WHERE x.xincoCoreDataId=" + id);
            while (!result.isEmpty()) {
                new XincoCoreLogJpaController(XincoDBManager.getEntityManagerFactory()).destroy(((com.bluecubs.xinco.core.server.persistence.XincoCoreLog) result.get(0)).getId());
            }
            result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreAce x WHERE x.xincoCoreDataId=" + id);
            while (!result.isEmpty()) {
                new XincoCoreAceJpaController(XincoDBManager.getEntityManagerFactory()).destroy(((com.bluecubs.xinco.core.server.persistence.XincoCoreAce) result.get(0)).getId());
            }
            result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE x.xincoCoreData.id=" + id);
            while (!result.isEmpty()) {
                new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).destroy(((com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) result.get(0)).getXincoAddAttributePK());
            }
        } catch (Exception e) {
            throw new XincoException();
        }
    }

    //delete from db
    public int deleteFromDB() throws XincoException {
        int i = 0;
        try {
            //delete file / file = 1
            if (getXincoCoreDataType().getId() == 1) {
                try {
                    (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, getId(), "" + getId()))).delete();
                } catch (Exception dfe) {
                    // continue, file might not exist
                }
                // delete revisions created upon creation or checkin
                for (i = 0; i < getXincoCoreLogs().size(); i++) {
                    if ((((XincoCoreLog) getXincoCoreLogs().get(i)).getOpCode() == 1) || (((XincoCoreLog) getXincoCoreLogs().get(i)).getOpCode() == 5)) {
                        try {
                            (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, getId(), getId() + "-" + ((XincoCoreLog) getXincoCoreLogs().get(i)).getId()))).delete();
                        } catch (Exception drfe) {
                            // continue, delete next revision
                        }
                    }
                }
            }
            new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory()).destroy(getId());
        } catch (Exception e) {
            throw new XincoException();
        }
        return 0;
    }

    public static byte[] loadBinaryData(XincoCoreData attrCD) {

        byte[] binaryData = null;

        return binaryData;

    }

    public static int saveBinaryData(XincoCoreData attrCD, byte[] attrBD) {

        return 0;

    }

    public static ArrayList findXincoCoreData(String attrS, int attrLID, boolean attrSA, boolean attrSFD) {
        ArrayList data = new ArrayList();
        try {
            String lang = "";
            if (attrLID != 0) {
                lang = "AND (x.xincoCoreData.xincoCoreLanguageId.id = " + attrLID + ")";
            }
            if (attrSA) {
                result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE (x.xincoCoreData.designation LIKE '"
                        + attrS + "%' or " + "x.attribVarchar  LIKE '" + attrS + "%' or x.attribText LIKE '" + attrS + "') "
                        + lang + "order by x.xincoCoreData.designation, x.xincoCoreData.xincoCoreLanguageId.id");
            } else {
                result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE x.xincoCoreData.designation LIKE '"
                        + attrS + "%' " + lang + "order by x.xincoCoreData.designation, x.xincoCoreData.xincoCoreLanguageId.id");
            }
            int i = 0;
            for (Object o : result) {
                data.add(new XincoCoreDataServer(((com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) o).getXincoCoreData().getId()));
                i++;
                if (i >= XincoDBManager.config.MaxSearchResult) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.clear();
        }
        return data;
    }

    public static String getXincoCoreDataPath(String attrRP, int attrID, String attrFN) {
        String path = null;
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
}
