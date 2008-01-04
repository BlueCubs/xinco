/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoCoreLanguage;
import com.bluecubs.xinco.core.persistance.XincoCoreNode;
import com.bluecubs.xinco.core.server.XincoCoreAuditTrail;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import com.bluecubs.xinco.index.XincoIndexer;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoCoreNodeServer extends XincoCoreNode implements XincoAuditableDAO, XincoPersistanceServerObject {

    private static XincoPersistanceManager pm = new XincoPersistanceManager();
    private static List result;
    private static HashMap parameters;
    private Vector xincoCoreNodes,  xincoCoreData,  xincoCoreACL;
    private XincoCoreLanguage xincoCoreLanguage;

    /**
     * Create node object for data structures
     * @param attrID
     * @throws com.bluecubs.xinco.core.XincoException
     */
    @SuppressWarnings("unchecked")
    public XincoCoreNodeServer(int attrID) throws XincoException {
        try {
            parameters = new HashMap();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreNode.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreNode temp = (XincoCoreNode) result.get(0);
                setId(temp.getId());
                setXincoCoreNodeId(temp.getXincoCoreNodeId());
                setXincoCoreLanguage(new XincoCoreLanguageServer(temp.getXincoCoreLanguageId()));
                setDesignation(temp.getDesignation());
                setStatusNumber(temp.getStatusNumber());
                setXincoCoreNodes(new Vector());
                setXincoCoreData(new Vector());
                //load acl for this object
                setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(temp.getId(), "xincoCoreNodeId"));
            } else {
                throw new XincoException();
            }
        } catch (Throwable e) {
            setXincoCoreLanguage(null);
            getXincoCoreACL().removeAllElements();
            getXincoCoreNodes().removeAllElements();
            getXincoCoreData().removeAllElements();
            if (new XincoSettingServer().getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, "Error creating node.", e);
            }
            throw new XincoException();
        }
    }

    /**
     * Create node object for data structures
     * @param attrID
     * @param attrCNID
     * @param attrLID 
     * @param attrD 
     * @param attrSN
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreNodeServer(int attrID, int attrCNID, int attrLID, String attrD, int attrSN, XincoDBManager DBM) throws XincoException {
        try {
            setId(attrID);
            setXincoCoreNodeId(attrCNID);
            setXincoCoreLanguage(new XincoCoreLanguageServer(attrLID));
            setDesignation(attrD);
            setStatusNumber(attrSN);
            setXincoCoreNodes(new Vector());
            setXincoCoreData(new Vector());
            //load acl for this object
            setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCorenode_id"));
        } catch (Throwable e) {
            setXincoCoreLanguage(null);
            getXincoCoreACL().removeAllElements();
            getXincoCoreNodes().removeAllElements();
            getXincoCoreData().removeAllElements();
            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, "Error creating node.", e);
            }
            throw new XincoException();
        }
    }

    /**
     * Fill XIncoCoreNodes vector for this Node
     * @param DBM
     */
    @SuppressWarnings("unchecked")
    public void fillXincoCoreNodes() {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xincoCorenode WHERE xincoCorenode_id = " + getId() + " ORDER BY designation");
            while (rs.next()) {
                getXincoCoreNodes().add(new XincoCoreNodeServer(rs.getInt("id"), rs.getInt("xincoCorenode_id"), rs.getInt("xincoCorelanguage_id"), rs.getString("designation"), rs.getInt("StatusNumber"), DBM));
            }
        } catch (Throwable e) {
            getXincoCoreNodes().removeAllElements();
        }
    }

    /**
     * Fill XIncoCoreData vector for this Node
     * @param DBM
     */
    @SuppressWarnings("unchecked")
    public void fillXincoCoreData() {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xincoCoredata WHERE xincoCorenode_id = " + getId() + " ORDER BY designation");
            while (rs.next()) {
                getXincoCoreData().add(new XincoCoreDataServer(rs.getInt("id"), rs.getInt("xincoCorenode_id"), rs.getInt("xincoCorelanguage_id"), rs.getInt("xincoCoredata_type_id"), rs.getString("designation"), rs.getInt("StatusNumber"), DBM));
            }
        } catch (Throwable e) {
            getXincoCoreData().removeAllElements();
        }
    }

    /**
     * Find XincoCoreNodes based on parameters
     * @param designation
     * @param language_id
     * @param DBM
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector findXincoCoreNodes(String designation, int language_id, XincoDBManager DBM) {
        Vector nodes = null;
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xincoCorenode WHERE xincoCorelanguage_id = " + language_id + " AND designation LIKE '" + designation + "%' ORDER BY designation, xincoCorelanguage_id");
            int i = 0;
            while (rs.next()) {
                nodes.add(new XincoCoreNodeServer(rs.getInt("id"), rs.getInt("xincoCorenode_id"), rs.getInt("xincoCorelanguage_id"), rs.getString("designation"), rs.getInt("StatusNumber"), DBM));
                i++;
                if (i >= DBM.config.getMaxSearchResult()) {
                    break;
                }
            }
        } catch (Throwable e) {
            nodes.removeAllElements();
        }
        return nodes;
    }

    /**
     * Get parents of this node
     * @param id 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreNodeParents(int id) {
        Vector nodes = new Vector();
//        try {
//            ResultSet rs = null;
//            while (id > 0) {
//                rs = DBM.executeQuery("SELECT * FROM xincoCorenode WHERE id = " + id);
//                while (rs.next()) {
//                    nodes.addElement(new XincoCoreNodeServer(rs.getInt("id")));
//                    if (id > 1) {
//                        id = rs.getInt("xincoCorenode_id");
//                    } else {
//                        id = 0;
//                    }
//                }
//            }
//        } catch (Throwable e) {
//            nodes.removeAllElements();
//        }
        return nodes;
    }

    public Vector getXincoCoreNodes() {
        return xincoCoreNodes;
    }

    public void setXincoCoreNodes(Vector xincoCoreNodes) {
        this.xincoCoreNodes = xincoCoreNodes;
    }

    public Vector getXincoCoreData() {
        return xincoCoreData;
    }

    public void setXincoCoreData(Vector xincoCoreData) {
        this.xincoCoreData = xincoCoreData;
    }

    public Vector getXincoCoreACL() {
        return xincoCoreACL;
    }

    public void setXincoCoreACL(Vector xincoCoreACL) {
        this.xincoCoreACL = xincoCoreACL;
    }

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
        result = pm.namedQuery("XincoCoreNode.findById", parameters);
        XincoCoreNode temp = (XincoCoreNode) result.get(0);
        temp.setTransactionTime(getTransactionTime());
        temp.setChangerID(getChangerID());
        return temp;
    }

    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreNode x WHERE ";
        if (parameters.containsKey("xincoCoreNodeId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by xincoCoreNodeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreNodeId = :xincoCoreNodeId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreLanguageId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by xincoCoreLanguageId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreLanguageId = :xincoCoreLanguageId";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("statusNumber")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by statusNumber");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.statusNumber = :statusNumber";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreNode temp[] = new XincoCoreNode[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreNode) result.get(0);
                temp[i].setTransactionTime(getTransactionTime());
                i++;
                result.remove(0);
            }
            return temp;
        } else {
            return null;
        }
    }

    public XincoAbstractAuditableObject create(XincoAbstractAuditableObject value) {
        XincoCoreNode temp, newValue = new XincoCoreNode();
        temp = (XincoCoreNode) value;
        if (!value.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setId(getNewID());
        }
        if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Creating with new id: " + newValue.getId());
        }
        newValue.setStatusNumber(temp.getStatusNumber());
        newValue.setStatusNumber(temp.getStatusNumber());
        newValue.setDesignation(temp.getDesignation());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        return newValue;
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        int i = 0;
        try {
            //fill nodes and data
            fillXincoCoreNodes();
            fillXincoCoreData();
            //start recursive deletion
            for (i = 0; i < getXincoCoreNodes().size(); i++) {
                ((XincoCoreNodeServer) getXincoCoreNodes().elementAt(i)).deleteFromDB();
            }
            for (i = 0; i < getXincoCoreData().size(); i++) {
                XincoIndexer.removeXincoCoreData((XincoCoreData) getXincoCoreData().elementAt(i), DBM);
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).setChangerID(userID);
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).removeFromDB(DBM, userID,
                        ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).getId());
            }
            if (delete_this) {
                XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
                ResultSet rs = DBM.executeQuery("select id FROM xincoCoreace WHERE xincoCorenode_id=" + getId());
                while (rs.next()) {
                    XincoCoreACEServer temp = new XincoCoreACEServer(rs.getInt(1), DBM);
                    temp.removeFromDB(temp, DBM, userID);
                }
                audit.updateAuditTrail("xincoCorenode", new String[]{"id =" + getId()},
                        DBM, "audit.general.delete", userID);
                DBM.executeUpdate("DELETE FROM xincoCorenode WHERE id=" + getId());
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new XincoException();
        }
    }

    public HashMap getParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNewID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean deleteFromDB() throws XincoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean write2DB() throws XincoException {
//        try {
//            String xcnid = "";
//            if (getId() > 0) {
//                //set values of nullable attributes
//                if (getXincoCoreNodeId() == 0) {
//                    xcnid = "NULL";
//                } else {
//                    xcnid = "" + getXincoCoreNodeId();
//                }
//                XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
//                audit.updateAuditTrail("xincoCorenode", new String[]{"id =" + getId()},
//                        DBM, "audit.corenode.change", this.getChangerID());
//                DBM.executeUpdate("UPDATE xincoCorenode SET xincoCorenode_id=" + xcnid + ", xincoCorelanguage_id=" + getXincoCoreLanguage().getId() + ", designation='" + getDesignation().replaceAll("'", "\\\\'") + "', StatusNumber=" + getStatusNumber() + " WHERE id=" + getId());
//            } else {
//                setId(DBM.getNewID("xincoCorenode"));
//                //set values of nullable attributes
//                if (getXincoCoreNodeId() == 0) {
//                    xcnid = "NULL";
//                } else {
//                    xcnid = "" + getXincoCoreNodeId();
//                }
//                DBM.executeUpdate("INSERT INTO xincoCorenode VALUES (" + getId() + ", " + getXincoCoreNodeId() + ", " + getXincoCoreLanguage().getId() + ", '" + getDesignation().replaceAll("'", "\\\\'") + "', " + getStatusNumber() + ")");
//            }
//        } catch (Throwable e) {
//            throw new XincoException();
//        }
//        return getId();
        return true;
    }

    public XincoCoreLanguage getXincoCoreLanguage() {
        return xincoCoreLanguage;
    }

    public void setXincoCoreLanguage(XincoCoreLanguage xincoCoreLanguage) {
        this.xincoCoreLanguage = xincoCoreLanguage;
    }
}
