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
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.add.server.XincoAddAttributeServer;
import com.bluecubs.xinco.core.XincoException;
import java.util.HashMap;
import java.util.Vector;
import java.io.File;
import com.bluecubs.xinco.core.hibernate.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreData;
import com.bluecubs.xinco.core.persistence.XincoCoreDataT;
import com.bluecubs.xinco.core.persistence.XincoCoreLog;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import com.dreamer.Hibernate.Audit.PersistenceServerUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreDataServer extends XincoCoreData implements XincoAuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = 4801684078586102177L;
    private XincoCoreUserServer user;
    private static List result;
    private Vector xincoCoreLogs,  xincoAddAttributes,  xincoCoreACL;
    //create data object for data structures

    @SuppressWarnings("unchecked")
    public XincoCoreDataServer(int attrID) throws XincoException {

        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreData.findById", parameters);

            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoCoreData xca = ((XincoCoreData) result.get(0));
                setId(xca.getId());
                setXincoCoreNodeId(xca.getXincoCoreNodeId());
                setXincoCoreLanguageId(xca.getXincoCoreLanguageId());
                setXincoCoreDataTypeId(xca.getXincoCoreDataTypeId());
                //load logs
                setXincoCoreLogs(XincoCoreLogServer.getXincoCoreLogs(xca.getId()));
                //load add attributes
                setXincoAddAttributes(XincoAddAttributeServer.getXincoAddAttributes(xca.getId()));
                setDesignation(xca.getDesignation());
                setStatusNumber(xca.getStatusNumber());
                //load acl for this object
                setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(xca.getId(), "xincoCoreDataId"));
            } else {
                throw new XincoException("Record doesn't exist: "+attrID);
            }
        } catch (Exception e) {
            if (getXincoCoreACL() != null) {
                getXincoCoreACL().removeAllElements();
            }
            throw new XincoException(e.getLocalizedMessage());
        }

    }
    //create data object for data structures

    public XincoCoreDataServer(int attrID, int attrCNID, int attrLID, int attrDTID, String attrD, int attrSN) throws XincoException {
        setId(attrID);
        setXincoCoreNodeId(attrCNID);
        setXincoCoreLanguageId(attrLID);
        setXincoCoreDataTypeId(attrDTID);
        //load logs
        setXincoCoreLogs(XincoCoreLogServer.getXincoCoreLogs(attrID));
        //security: don't load add attribute, force direct access to data including check of access rights!
        setXincoAddAttributes(new Vector());
        setDesignation(attrD);
        setStatusNumber(attrSN);
        //load acl for this object
        setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreDataId"));
    }

    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }

    @SuppressWarnings("unchecked")
    public static void removeFromDB(int userID, int id) throws XincoException {
        parameters.clear();
        parameters.put("id", id);
        result = pm.namedQuery("XincoCoreData.findById", parameters);
        if (!result.isEmpty()) {
            try {
                PersistenceServerUtils.removeFromDB((AuditableDAO) result.get(0), userID, getChangerID());
                /*
                 * Aduit Trail Table (*T) cannot handle multiple row changes!!!
                audit.updateAuditTrail("xincoCoreLog",new String [] {"id ="+id},
                pm,"audit.general.delete",userID);
                 */
                parameters.clear();
                parameters.put("xincoCoreDataId", id);
                result = pm.namedQuery("XincoCoreLog.findByXincoCoreDataId", parameters);
                while (!result.isEmpty()) {
                    ((XincoCoreLogServer) result.get(0)).deleteFromDB();
                    result.remove(0);
                }
                /*
                 * Aduit Trail Table (*T) cannot handle multiple row changes!!!
                audit.updateAuditTrail("xincoCoreAce",new String [] {"id ="+id},
                pm,"audit.general.delete",userID);
                 */
                parameters.clear();
                parameters.put("xincoCoreDataId", id);
                result = pm.namedQuery("XincoCoreData.findByXincoCoreDataId", parameters);
                while (!result.isEmpty()) {
                    PersistenceServerUtils.removeFromDB((XincoCoreDataServer) result.get(0), userID, getChangerID());
                    result.remove(0);
                }
                /*
                 * Aduit Trail Table (*T) cannot handle multiple row changes!!!
                audit.updateAuditTrail("xincoAddAttribute",new String [] {"id ="+id},
                pm,"audit.general.delete",userID);
                 */
                parameters.clear();
                parameters.put("xincoCoreDataId", id);
                result = pm.namedQuery("XincoAddAttribute.findByXincoCoreDataId", parameters);
                while (!result.isEmpty()) {
                    PersistenceServerUtils.removeFromDB((XincoCoreDataServer) result.get(0), userID, getChangerID());
                    result.remove(0);
                }
            } catch (Exception e) {
                throw new XincoException(e.getLocalizedMessage());
            }
        }
    }

    public static byte[] loadBinaryData(XincoCoreData attrCD) {
        byte[] binaryData = null;
        return binaryData;
    }

    public static int saveBinaryData(XincoCoreData attrCD, byte[] attrBD) {
        return 0;
    }

    /**
     *
     * @param attrS
     * @param attrLID
     * @param attrSA
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector findXincoCoreData(String attrS, int attrLID, boolean attrSA) {
        Vector data = new Vector();
        try {
            String lang = "";
            if (attrLID != 0) {
                lang = "AND (x.xincoCoreLanguageId = " + attrLID + ")";
            }
            if (attrSA) {
                result = pm.createdQuery("SELECT x FROM XincoCoreData x, " +
                        "XincoAddAttribute a WHERE x.id = a.xincoAddAttributePK.xincoCoreDataId " +
                        "AND (x.designation LIKE '" + attrS + "%' OR a.attribVarchar LIKE '" +
                        attrS + "' OR a.attribText LIKE '" + attrS + "') " + lang +
                        " ORDER BY x.designation, x.xincoCoreLanguageId");
            } else {
                result = pm.createdQuery("SELECT x FROM XincoCoreData x WHERE x.designation LIKE '" + attrS +
                        "%' " + lang + " ORDER BY x.designation, " +
                        "x.xincoCoreLanguageId");
            }
            int i = 0;
            while (!result.isEmpty()) {
                data.addElement((XincoCoreData) result.get(0));
                i++;
                if (i >= XincoDBManager.config.getMaxSearchResult()) {
                    break;
                }
            }
            result.remove(0);
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
            path4Id = path4Id.substring(0, (i * 2 + 1)) +
                    System.getProperty("file.separator") +
                    path4Id.substring((i * 2 + 1));
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

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoCoreData.findById", parameters);
        if (result.size() > 0) {
            XincoCoreData temp = (XincoCoreData) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        result = pm.createdQuery("SELECT x FROM XincoCoreData x WHERE x.id = :id", parameters);
        if (result.size() > 0) {
            XincoCoreData temp[] = new XincoCoreData[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreData) result.get(0);
                temp[i].setTransactionTime(getTransactionTime());
                i++;
                result.remove(0);
            }
            return temp;
        } else {
            return null;
        }
    }

    @SuppressWarnings("static-access")
    public AbstractAuditableObject create(AbstractAuditableObject value) {
        XincoCoreData temp;
        XincoCoreData newValue = new XincoCoreData();
        temp = (XincoCoreData) value;
        newValue.setId(temp.getRecordId());
        newValue.setXincoCoreNodeId(temp.getXincoCoreNodeId());
        newValue.setXincoCoreLanguageId(temp.getXincoCoreLanguageId());
        newValue.setXincoCoreDataTypeId(temp.getXincoCoreDataTypeId());
        newValue.setDesignation(temp.getDesignation());
        newValue.setStatusNumber(temp.getStatusNumber());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreData val = (XincoCoreData) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings({"unchecked", "static-access"})
    public void delete(AbstractAuditableObject value) {
        try {
            XincoCoreData val = (XincoCoreData) value;
            XincoCoreDataT temp = new XincoCoreDataT();
            temp.setRecordId(val.getRecordId());

            temp.setId(val.getId());
            temp.setXincoCoreNodeId(val.getXincoCoreNodeId());
            temp.setXincoCoreLanguageId(val.getXincoCoreLanguageId());
            temp.setXincoCoreDataTypeId(val.getXincoCoreDataTypeId());
            temp.setDesignation(val.getDesignation());
            temp.setStatusNumber(val.getStatusNumber());
            temp.setXincoCoreNodeId(val.getXincoCoreNodeId());

            pm.startTransaction();
            pm.persist(temp, false, false);
            pm.delete(val, false);
            setModifiedRecordDAOObject(value.getModifiedRecordDAOObject());
            //Make sure all audit data is stored properly. If not undo everything
            if (!getModifiedRecordDAOObject().saveAuditData()) {
                throw new XincoException(rb.getString("error.audit_data.invalid"));
            }
            pm.commitAndClose();
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, ex);
            pm.rollback();
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    /**
     * Get a new newID
     * @param a 
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewID(boolean a) {
        return new XincoIDServer("Xinco_Core_Data").getNewTableID(a);
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoCoreData(getId()));
            } else {
                XincoCoreData temp = new XincoCoreData();
                temp.setId(getId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setId(getId());
                temp.setXincoCoreNodeId(getXincoCoreNodeId());
                temp.setXincoCoreLanguageId(getXincoCoreLanguageId());
                temp.setXincoCoreDataTypeId(getXincoCoreDataTypeId());
                temp.setDesignation(getDesignation());
                temp.setStatusNumber(getStatusNumber());
                temp.setXincoCoreNodeId(getXincoCoreNodeId());

                temp = (XincoCoreData) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }

                XincoAddAttributeServer xaas;
                for (int i = 0; i < getXincoAddAttributes().size(); i++) {
                    ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getXincoAddAttributePK().setXincoCoreDataId(getId());
                    //copy fields from XincoAddAttribute to XincoAddAttributeServer
                    xaas = new XincoAddAttributeServer(((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getXincoAddAttributePK().getXincoCoreDataId(),
                            ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getXincoAddAttributePK().getAttributeId(),
                            ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribInt(),
                            ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribUnsignedint(),
                            ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribDouble(),
                            ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribVarchar(),
                            ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribText(), ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribDatetime());
                    xaas.write2DB();
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public boolean deleteFromDB() {
        int i = 0;
        //delete file / file = 1
        if (getXincoCoreDataTypeId() == 1) {
            try {
                (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.getFileRepositoryPath(), getId(), "" + getId()))).delete();
            } catch (Exception dfe) {
                // continue, file might not exists
                }
            // delete revisions created upon creation or checkin
            for (i = 0; i < this.getXincoCoreLogs().size(); i++) {
                if ((((XincoCoreLog) getXincoCoreLogs().elementAt(i)).getOpCode() == 1) || (((XincoCoreLog) getXincoCoreLogs().elementAt(i)).getOpCode() == 5)) {
                    try {
                        (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.getFileRepositoryPath(), getId(), getId() + "-" + ((XincoCoreLog) getXincoCoreLogs().elementAt(i)).getId()))).delete();
                    } catch (Exception drfe) {
                        // continue, delete next revision
                        }
                }
            }
        }
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getId(), getChangerID());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public Vector getXincoCoreLogs() {
        return xincoCoreLogs;
    }

    public void setXincoCoreLogs(Vector xincoCoreLogs) {
        this.xincoCoreLogs = xincoCoreLogs;
    }

    public Vector getXincoAddAttributes() {
        return xincoAddAttributes;
    }

    public void setXincoAddAttributes(Vector xincoAddAttributes) {
        this.xincoAddAttributes = xincoAddAttributes;
    }

    public Vector getXincoCoreACL() {
        return xincoCoreACL;
    }

    public void setXincoCoreACL(Vector xincoCoreACL) {
        this.xincoCoreACL = xincoCoreACL;
    }
}
