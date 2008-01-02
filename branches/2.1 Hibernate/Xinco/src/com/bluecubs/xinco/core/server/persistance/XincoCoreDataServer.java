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
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.add.server.XincoAddAttributeServer;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoAddAttribute;
import com.bluecubs.xinco.core.persistance.XincoCoreACE;
import com.bluecubs.xinco.core.persistance.XincoCoreData;
import com.bluecubs.xinco.core.persistance.XincoCoreLog;
import com.bluecubs.xinco.core.server.XincoCoreAuditTrail;
import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import java.io.File;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Create data object for data structures
 * @author Alexander Manes
 * Statuses:
 * 1 => Open
 * 2 => Locked
 * 3 => Archived
 * 4 => Checked Out
 * 5 => Published
 */
public class XincoCoreDataServer extends XincoCoreData implements XincoAuditableDAO, XincoPersistanceServerObject {

    private XincoCoreUserServer user;
    private static XincoPersistanceManager pm = new XincoPersistanceManager();
    private static List result;
    private static HashMap parameters;
    private Vector xinco_core_logs;

    /**
     * create data object for data structures
     * @param attrID Data id
     * @throws com.bluecubs.xinco.core.XincoException 
     */
    @SuppressWarnings("unchecked")
    public XincoCoreDataServer(int attrID) throws XincoException {
        try {
            parameters = new HashMap();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreData.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreData temp = (XincoCoreData) result.get(0);
                setId(temp.getId());
                setXincoCoreNodeId(temp.getXincoCoreNodeId());
                setXincoCoreLanguageId(temp.getXincoCoreLanguageId());
                setXincoCoreDataTypeId(temp.getXincoCoreDataTypeId());
                //load logs
//                setXincoCoreLogs(XincoCoreLogServer.getXincoCoreLogs(getId()));
                //load add attributes
//                setXincoAddAttributes(XincoAddAttributeServer.getXincoAddAttributes(getId(), DBM));
//                setDesignation(rs.getString("designation"));
//                setStatusNumber(rs.getInt("statusNumber"));
                //load acl for this object
//                setXincoCoreacl(XincoCoreACEServer.getXincoCoreACL(rs.getInt("id"), "xincoCoredataId", DBM));
            }

            else {
                throw new XincoException();
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, ex);
//            getXincoCoreacl().removeAllElements();
            throw new XincoException();
        }
    }

    /**
     * create data object for data structures
     * @param attrID ID
     * @param attrCNID XincoCoreNodeId
     * @param attrLID XincoCorelanguageId
     * @param attrDTID XincoCoreDataTypeId
     * @param attrD Designation
     * @param attrSN Status number
     * @param DBM XincoDBManager
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreDataServer(int attrID, int attrCNID, int attrLID, int attrDTID, String attrD, int attrSN, XincoDBManager DBM) throws XincoException {
        setId(attrID);
        setXincoCoreNodeId(attrCNID);
//        setXincoCorelanguage(new XincoCoreLanguageServer(attrLID, DBM));
//        setXincoCoreDataType(new XincoCoreDataTypeServer(attrDTID, DBM));
        //load logs
//        setXincoCorelogs(XincoCoreLogServer.getXincoCoreLogs(attrID, DBM));
        //load add attributes
        //security: don't load add attribute, force direct access to data including check of access rights!
//        setXincoAddAttributes(new Vector());
        setDesignation(attrD);
        setStatusNumber(attrSN);
        //load acl for this object
//        setXincoCoreacl(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoredataId", DBM));
    }

    /**
     * Set XincoCoreuser
     * @param user
     */
    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }

    /**
     * Load binay data from XincoCoreData
     * @param xincoCoredata
     * @return
     */
    public static byte[] loadBinaryData(XincoCoreData xincoCoredata) {
        byte[] binary_data = null;
        return binary_data;
    }

    /**
     * Find XincoCoreData
     * @param designation
     * @param attrLID
     * @param attrSA
     * @param attrSFD
     * @param DBM
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector findXincoCoreData(String designation, int attrLID, boolean attrSA, boolean attrSFD, XincoDBManager DBM) {
        Vector data = new Vector();
        try {
            ResultSet rs;
            String lang = "";
            if (attrLID != 0) {
                lang = "AND (xincoCorelanguageId = " + attrLID + ")";
            }
            if (attrSA) {
                rs = DBM.executeQuery("SELECT DISTINCT xincoCoredata.* FROM xincoCoredata, xincoAddAttribute WHERE (xincoCoredata.id = xincoAddAttribute.xincoCoredataId) AND (xincoCoredata.designation LIKE '" + designation + "%' OR xincoAddAttribute.attrib_varchar LIKE '" + designation + "' OR xincoAddAttribute.attrib_text LIKE '" + designation + "') " + lang + " ORDER BY xincoCoredata.designation, xincoCoredata.xincoCorelanguageId");
            } else {
                rs = DBM.executeQuery("SELECT DISTINCT xincoCoredata.* FROM xincoCoredata WHERE designation LIKE '" + designation + "' " + lang + " ORDER BY designation, xincoCorelanguageId");
            }
            int i = 0;
            while (rs.next()) {
//                data.add(new XincoCoreDataServer(rs.getInt("id"), DBM));
                i++;
                if (i >= DBM.config.getMaxSearchResult()) {
                    break;
                }
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, ex);
            data.removeAllElements();
        }
        return data;
    }

    /**
     * Get XincoCOreData path
     * @param attrRP
     * @param attrID
     * @param attrFN
     * @return String
     */
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

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XincoAbstractAuditableObject create(XincoAbstractAuditableObject value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public HashMap getParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNewID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean deleteFromDB() throws XincoException {
//        try {
//            ResultSet rs;
//            Vector objects = new Vector();
//            XincoCoreLogServer temp;
//            // Get related logs
//            rs = DBM.executeQuery("select id FROM xincoCorelog WHERE xincoCoredataId=" + id);
//            while (rs.next()) {
//                temp = new XincoCoreLogServer(rs.getInt(1), DBM);
//                temp.setChangerID(userID);
//                objects.add(temp);
//            }
//            while (!objects.isEmpty()) {
//                ((XincoCoreLogServer) objects.get(0)).removeFromDB(DBM);
//                objects.remove(0);
//            }
//            objects.removeAllElements();
//            XincoCoreACEServer tempACE;
//            // Get related ACE's
//            rs = DBM.executeQuery("select id FROM xincoCoreace WHERE xincoCoredataId=" + id);
//            while (rs.next()) {
//                tempACE = new XincoCoreACEServer(rs.getInt(1), DBM);
//                tempACE.setChangerID(userID);
//                objects.add(tempACE);
//            }
//            while (!objects.isEmpty()) {
//                XincoCoreACEServer.removeFromDB(((XincoCoreACE) objects.get(0)), DBM, userID);
//                objects.remove(0);
//            }
//            objects.removeAllElements();
//            XincoAddAttributeServer tempAdd;
//            // Get related ACE's
//            rs = DBM.executeQuery("select attributeId FROM xincoAddAttribute WHERE xincoCoredataId=" + id);
//            while (rs.next()) {
//                tempAdd = new XincoAddAttributeServer(id, rs.getInt(1), DBM);
//                tempAdd.setChangerID(userID);
//                objects.add(tempAdd);
//            }
//            while (!objects.isEmpty()) {
//                XincoAddAttributeServer.removeFromDB(((XincoAddAttributeServer) objects.get(0)).getAttributeId(),
//                        ((XincoAddAttributeServer) objects.get(0)).getXincoCoredataId(), DBM);
//                objects.remove(0);
//            }
//            objects.removeAllElements();
//            //Now delete the data
//            int i = 0;
//            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
//            //delete file / file = 1
//            if (getXincoCoreDataType().getId() == 1) {
//                try {
//                    (new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), getId(), "" + getId()))).delete();
//                } catch (Exception dfe) {
//                // continue, file might not exists
//                }
//                // delete revisions created upon creation or checkin
//                for (i = 0; i < this.getXincoCorelogs().size(); i++) {
//                    if ((((XincoCoreLog) getXincoCorelogs().elementAt(i)).getOp_code() == 1) || (((XincoCoreLog) getXincoCorelogs().elementAt(i)).getOp_code() == 5)) {
//                        try {
//                            (new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), getId(), getId() + "-" + ((XincoCoreLog) getXincoCorelogs().elementAt(i)).getId()))).delete();
//                        } catch (Exception drfe) {
//                        // continue, delete next revision
//                        }
//                    }
//                }
//            }
//            audit.updateAuditTrail("xincoCoredata", new String[]{"id =" + getId()},
//                    DBM, "audit.general.delete", this.getChangerID());
//            DBM.executeUpdate("DELETE FROM xincoCoredata WHERE id=" + getId());
//        } catch (Throwable e) {
//            try {
//                DBM.getConnection().rollback();
//            } catch (Exception erollback) {
//            }
//            throw new XincoException();
//        }
        return true;
    }

    public boolean write2DB() throws XincoException {
//        int i = 0;
//        try {
//            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
//            if (getId() > 0) {
//                audit.updateAuditTrail("xincoCoredata", new String[]{"id =" + getId()},
//                        DBM, "audit.data.change", this.getChangerID());
//                DBM.executeUpdate("UPDATE xincoCoredata SET xincoCoreNodeId=" +
//                        getXincoCoreNodeId() + ", xincoCorelanguageId=" +
//                        getXincoCorelanguage().getId() + ", xincoCoreDataTypeId=" +
//                        getXincoCoreDataType().getId() + ", designation='" +
//                        getDesignation().replaceAll("'", "\\\\'") + "', statusNumber=" +
//                        getStatusNumber() + " WHERE id =" + getId());
//            } else {
//                setId(DBM.getNewID("xincoCoredata"));
//                DBM.executeUpdate("INSERT INTO xincoCoredata VALUES (" + getId() + ", " + getXincoCoreNodeId() + ", " + getXincoCorelanguage().getId() + ", " + getXincoCoreDataType().getId() + ", '" + getDesignation().replaceAll("'", "\\\\'") + "', " + getStatusNumber() + ")");
//                audit.updateAuditTrail("xincoCoredata", new String[]{"id =" + getId()},
//                        DBM, "audit.data.create", this.getChangerID());
//            }
//            //write add attributes
//            XincoAddAttributeServer xaas;
//            for (i = 0; i < getXincoAddAttributes().size(); i++) {
//                ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).setXincoCoredataId(getId());
//                //copy fields from XincoAddAttribute to XincoAddAttributeServer
//                xaas = new XincoAddAttributeServer(((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getXincoCoredataId(), ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttributeId(), ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribInt(), ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttrib_unsignedint(), ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttrib_double(), ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttrib_varchar(), ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttrib_text(), ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttrib_datetime());
//                xaas.write2DB(DBM);
//            }
//            DBM.getConnection().commit();
//        } catch (Throwable e) {
//            try {
//                DBM.getConnection().rollback();
//            } catch (Exception erollback) {
//            }
//            throw new XincoException();
//        }
        return true;
    }

    public Vector getXincoCoreLogs() {
        return xinco_core_logs;
    }

    public void setXincoCoreLogs(Vector xinco_core_logs) {
        this.xinco_core_logs = xinco_core_logs;
    }
}
