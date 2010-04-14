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

import com.bluecubs.xinco.add.server.XincoAddAttributeServer;
import com.bluecubs.xinco.add.XincoAddAttribute;
import java.util.Vector;
import java.io.File;
import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.XincoAddAttributeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreAceJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLogJpaController;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class XincoCoreDataServer extends XincoCoreData {

    private static final long serialVersionUID = 1L;
    private XincoCoreUserServer user;
    private static HashMap parameters = new HashMap();
    private static List result;
    //create data object for data structures

    public XincoCoreDataServer(int attrID) throws XincoException {
        try {
            XincoCoreDataJpaController controller = new XincoCoreDataJpaController();
            com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd = controller.findXincoCoreData(attrID);
            //throw exception if no result found
            if (xcd != null) {
                setId(xcd.getId());
                setXinco_core_node_id(xcd.getXincoCoreNodeId().getId());
                setXinco_core_language(new XincoCoreLanguageServer(xcd.getXincoCoreLanguageId().getId()));
                setXinco_core_data_type(new XincoCoreDataTypeServer(xcd.getXincoCoreDataTypeId().getId()));
                //load logs
                setXinco_core_logs(XincoCoreLogServer.getXincoCoreLogs(xcd.getId()));
                //load add attributes
                setXinco_add_attributes(XincoAddAttributeServer.getXincoAddAttributes(xcd.getId()));
                setDesignation(xcd.getDesignation());
                setStatus_number(xcd.getStatusNumber());
                //load acl for this object
                setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(xcd.getId(), "xincoCoreDataId.id"));
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            getXinco_core_acl().removeAllElements();
            throw new XincoException();
        }
    }

    //create data object for data structures
    public XincoCoreDataServer(int attrID, int attrCNID, int attrLID, int attrDTID, String attrD, int attrSN) throws XincoException {
        setId(attrID);
        setXinco_core_node_id(attrCNID);
        setXinco_core_language(new XincoCoreLanguageServer(attrLID));
        setXinco_core_data_type(new XincoCoreDataTypeServer(attrDTID));
        //load logs
        setXinco_core_logs(XincoCoreLogServer.getXincoCoreLogs(attrID));
        //security: don't load add attribute, force direct access to data including check of access rights!
        setXinco_add_attributes(new Vector());
        setDesignation(attrD);
        setStatus_number(attrSN);
        //load acl for this object
        setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreDataId.id"));
    }

    public XincoCoreDataServer(com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd) throws XincoException {
        try {
            setId(xcd.getId());
            setXinco_core_node_id(xcd.getXincoCoreNodeId().getId());
            setXinco_core_language(new XincoCoreLanguageServer(xcd.getXincoCoreLanguageId().getId()));
            setXinco_core_data_type(new XincoCoreDataTypeServer(xcd.getXincoCoreDataTypeId().getId()));
            //load logs
            setXinco_core_logs(XincoCoreLogServer.getXincoCoreLogs(xcd.getId()));
            //load add attributes
            setXinco_add_attributes(XincoAddAttributeServer.getXincoAddAttributes(xcd.getId()));
            setDesignation(xcd.getDesignation());
            setStatus_number(xcd.getStatusNumber());
            //load acl for this object
            setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(xcd.getId(), "xincoCoreDataId.id"));
        } catch (XincoException ex) {
            getXinco_core_acl().removeAllElements();
            throw new XincoException();
        }
    }

    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }

    /**
     * This will get the latest major revision. Basically the last version
     * with a '0' as a mid version
     * @param xinco_core_data_id 
     * @return Path to last major version
     * @throws SQLException
     * @throws XincoException
     */
    public static String getLastMajorVersionDataPath(int xinco_core_data_id) throws SQLException, XincoException {
        parameters.clear();
        parameters.put("xcdi", xinco_core_data_id);
        parameters.put("opcode", OPCode.CHECKIN.ordinal() + 1);
        result = XincoDBManager.createdQuery("select xcl from XincoCoreLog xcl"
                + "where xcl.xincoCoreDataId.id =:xcdi and xcl.versionMid='0' "
                + "and xcl.opCode=:opcode order by xcl.id desc");
        if (result.size() > 0) {
            com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd = (com.bluecubs.xinco.core.server.persistence.XincoCoreData) result.get(0);
            XincoCoreLogServer log = new XincoCoreLogServer(xcd.getId());
            return XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, xinco_core_data_id, xinco_core_data_id + "-" + log.getId());
        } else {
            throw new XincoException("No major log history for XincoCoreData with id: " + xinco_core_data_id);
        }
    }

    //write to db
    public int write2DB() throws XincoException {
        int i = 0;
        XincoCoreDataJpaController controller = new XincoCoreDataJpaController();
        try {
            if (getId() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd = controller.findXincoCoreData(getId());
                parameters.clear();
                parameters.put("id", getXinco_core_node_id());
                xcd.setXincoCoreNodeId((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) XincoDBManager.namedQuery("XincoCoreNode.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXinco_core_data_type().getId());
                xcd.setXincoCoreLanguageId((com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage) XincoDBManager.namedQuery("XincoCoreLanguage.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXinco_core_language().getId());
                xcd.setXincoCoreDataTypeId((com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) XincoDBManager.namedQuery("XincoCoreDataType.findById", parameters).get(0));
                xcd.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcd.setStatusNumber(getStatus_number());
                xcd.setModificationReason("audit.data.change");
                xcd.setModifierId(getChangerID());
                xcd.setModificationTime(new Timestamp(new Date().getTime()));
                controller.edit(xcd);
            } else {
                com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd = new com.bluecubs.xinco.core.server.persistence.XincoCoreData();
                parameters.clear();
                parameters.put("id", this.getXinco_core_node_id());
                xcd.setXincoCoreNodeId((com.bluecubs.xinco.core.server.persistence.XincoCoreNode) XincoDBManager.namedQuery("XincoCoreNode.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXinco_core_data_type().getId());
                xcd.setXincoCoreLanguageId((com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage) XincoDBManager.namedQuery("XincoCoreLanguage.findById", parameters).get(0));
                parameters.clear();
                parameters.put("id", getXinco_core_language().getId());
                xcd.setXincoCoreDataTypeId((com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) XincoDBManager.namedQuery("XincoCoreDataType.findById", parameters).get(0));
                xcd.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcd.setStatusNumber(getStatus_number());
                xcd.setModificationReason("audit.general.create");
                xcd.setModifierId(getChangerID());
                xcd.setModificationTime(new Timestamp(new Date().getTime()));
                controller.create(xcd);
                setId(xcd.getId());
            }
            //Update add attributes
            for (i = 0; i < getXinco_add_attributes().size(); i++) {
                /**
                 * Copy fields from XincoAddAttribute to XincoAddAttribute (Persistence)
                 * The delete and create approach doesn't go well with persistence.
                 */
                com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                        new XincoAddAttributeJpaController().findXincoAddAttribute(new XincoAddAttributePK(getId(),
                        ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttribute_id()));
                xaa.setAttribInt(((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_int());
                xaa.setAttribUnsignedint(((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_unsignedint());
                xaa.setAttribDouble(((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_double());
                xaa.setAttribVarchar(((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_varchar());
                xaa.setAttribText(((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_text());
                xaa.setAttribDatetime(((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_datetime().getTime());
                new XincoAddAttributeJpaController().edit(xaa);
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
                new XincoCoreLogJpaController().destroy(((com.bluecubs.xinco.core.server.persistence.XincoCoreLog) result.get(0)).getId());
            }
            result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreAce x WHERE x.xincoCoreDataId=" + id);
            while (!result.isEmpty()) {
                new XincoCoreAceJpaController().destroy(((com.bluecubs.xinco.core.server.persistence.XincoCoreAce) result.get(0)).getId());
            }
            result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE x.xincoCoreData.id=" + id);
            while (!result.isEmpty()) {
                new XincoAddAttributeJpaController().destroy(((com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) result.get(0)).getXincoAddAttributePK());
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
            if (getXinco_core_data_type().getId() == 1) {
                try {
                    (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, getId(), "" + getId()))).delete();
                } catch (Exception dfe) {
                    // continue, file might not exist
                }
                // delete revisions created upon creation or checkin
                for (i = 0; i < getXinco_core_logs().size(); i++) {
                    if ((((XincoCoreLog) getXinco_core_logs().elementAt(i)).getOp_code() == 1) || (((XincoCoreLog) getXinco_core_logs().elementAt(i)).getOp_code() == 5)) {
                        try {
                            (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, getId(), getId() + "-" + ((XincoCoreLog) getXinco_core_logs().elementAt(i)).getId()))).delete();
                        } catch (Exception drfe) {
                            // continue, delete next revision
                        }
                    }
                }
            }
            new XincoCoreDataJpaController().destroy(getId());
        } catch (Exception e) {
            throw new XincoException();
        }
        return 0;
    }

    public static byte[] loadBinaryData(XincoCoreData attrCD) {

        byte[] binary_data = null;

        return binary_data;

    }

    public static int saveBinaryData(XincoCoreData attrCD, byte[] attrBD) {

        return 0;

    }

    public static Vector findXincoCoreData(String attrS, int attrLID, boolean attrSA, boolean attrSFD) {
        Vector data = new Vector();
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
                result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE x.xincoCoreData.designation LIKE '%"
                        + attrS + "%' " + lang + "order by x.xincoCoreData.designation, x.xincoCoreData.xincoCoreLanguageId.id");
            }
            int i = 0;
            for (Object o : result) {
                data.addElement(new XincoCoreDataServer(((com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) o).getXincoCoreData().getId()));
                i++;
                if (i >= XincoDBManager.config.MaxSearchResult) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.removeAllElements();
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
