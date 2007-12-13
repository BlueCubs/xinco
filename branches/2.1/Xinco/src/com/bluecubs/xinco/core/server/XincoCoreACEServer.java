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
 * Name:            XincoCoreACEServer
 *
 * Description:     access control entry
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

import java.util.Vector;
import java.sql.*;

import com.bluecubs.xinco.core.*;

/**
 * Single ace object for data structures
 * @author Alexander Manes
 */
public class XincoCoreACEServer extends XincoCoreACE {

    private int userID = 1;

    /**
     * create single ace object for data structures
     * @param attrID xinco_core_ace id
     * @param DBM XincoDBManager
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreACEServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_ace WHERE id=" + attrID);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setXinco_core_user_id(rs.getInt("xinco_core_user_id"));
                setXinco_core_group_id(rs.getInt("xinco_core_group_id"));
                setXinco_core_node_id(rs.getInt("xinco_core_node_id"));
                setXinco_core_data_id(rs.getInt("xinco_core_data_id"));
                setRead_permission(rs.getBoolean("read_permission"));
                setWrite_permission(rs.getBoolean("write_permission"));
                setExecute_permission(rs.getBoolean("execute_permission"));
                setAdmin_permission(rs.getBoolean("admin_permission"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
        } catch (Throwable e) {
            throw new XincoException();
        }
    }

    /**
     * create single ace object for data structures
     * @param attrID Xinco_core_user_id
     * @param attrUID Attribute id
     * @param attrGID Xinco_core_group_id
     * @param attrNID Xinco_core_node_id
     * @param attrDID Xinco_core_data_id
     * @param attrRP Read_permission
     * @param attrWP Write_permission
     * @param attrEP Execute_permission
     * @param attrAP Admin_permission
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreACEServer(int attrID, int attrUID, int attrGID, int attrNID, int attrDID, boolean attrRP, boolean attrWP, boolean attrEP, boolean attrAP) throws XincoException {
        setId(attrID);
        setXinco_core_user_id(attrUID);
        setXinco_core_group_id(attrGID);
        setXinco_core_node_id(attrNID);
        setXinco_core_data_id(attrDID);
        setRead_permission(attrRP);
        setWrite_permission(attrWP);
        setExecute_permission(attrEP);
        setAdmin_permission(attrAP);
    }

    /**
     * Persist Object in DB
     * @param DBM
     * @return int
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public int write2DB(XincoDBManager DBM) throws XincoException {
        try {
            String xcuid = "";
            String xcgid = "";
            String xcnid = "";
            String xcdid = "";
            int rp = 0;
            int wp = 0;
            int xp = 0;
            int ap = 0;
            int op = 0;
            //set values of nullable attributes
            if (getXinco_core_user_id() == 0) {
                xcuid = "NULL";
            } else {
                xcuid = "" + getXinco_core_user_id();
            }
            if (getXinco_core_group_id() == 0) {
                xcgid = "NULL";
            } else {
                xcgid = "" + getXinco_core_group_id();
            }
            if (getXinco_core_node_id() == 0) {
                xcnid = "NULL";
            } else {
                xcnid = "" + getXinco_core_node_id();
            }
            if (getXinco_core_data_id() == 0) {
                xcdid = "NULL";
            } else {
                xcdid = "" + getXinco_core_data_id();
            }
            //convert boolean to 0/1
            if (isRead_permission()) {
                rp = 1;
            }
            if (isWrite_permission()) {
                wp = 1;
            }
            if (isExecute_permission()) {
                xp = 1;
            }
            if (isAdmin_permission()) {
                ap = 1;
            }
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            if (getId() > 0) {
                audit.updateAuditTrail("xinco_core_ace", new String[]{"id =" + getId()},
                        DBM, "window.acl", this.getChangerID());
                DBM.executeUpdate("UPDATE xinco_core_ace SET xinco_core_user_id=" + xcuid +
                        ", xinco_core_group_id=" + xcgid + ", xinco_core_node_id=" + xcnid +
                        ", xinco_core_data_id=" + xcdid + ", read_permission=" + rp +
                        ", write_permission=" + wp + ", execute_permission=" + xp +
                        ", admin_permission=" + ap + " WHERE id=" + getId());
            } else {
                setId(DBM.getNewID("xinco_core_ace"));
                DBM.executeUpdate("INSERT INTO xinco_core_ace VALUES (" + getId() + ", " + xcuid + ", " + xcgid + ", " + xcnid + ", " + xcdid + ", " + rp + ", " + wp + ", " + xp + ", " + ap + ")");
            }
            DBM.getConnection().commit();
        } catch (Throwable e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            e.printStackTrace();
            throw new XincoException();
        }
        return getId();
    }

    /**
     * Remove from DB
     * @param attrCACE XincoCoreACE
     * @param DBM XincoDBManager
     * @param userID User ID
     * @return int
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public static int removeFromDB(XincoCoreACE attrCACE, XincoDBManager DBM, int userID) throws XincoException {
        try {
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            audit.updateAuditTrail("xinco_core_ace", new String[]{"id =" + attrCACE.getId()},
                    DBM, "audit.general.delete", userID);
            DBM.executeUpdate("DELETE FROM xinco_core_ace WHERE id=" + attrCACE.getId());
        } catch (Throwable e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            e.printStackTrace();
            throw new XincoException();
        }
        return 0;
    }

    /**
     * create complete ACL for node or data
     * @param attrID Attribute id
     * @param attrT Attribute name (String)
     * @param DBM XincoDBManager
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreACL(int attrID, String attrT, XincoDBManager DBM) {
        Vector core_acl = new Vector();
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_ace WHERE " + attrT + "=" + attrID + " ORDER BY xinco_core_user_id, xinco_core_group_id, xinco_core_node_id, xinco_core_data_id");
            while (rs.next()) {
                core_acl.addElement(new XincoCoreACEServer(rs.getInt("id"), rs.getInt("xinco_core_user_id"), rs.getInt("xinco_core_group_id"), rs.getInt("xinco_core_node_id"), rs.getInt("xinco_core_data_id"), rs.getBoolean("read_permission"), rs.getBoolean("write_permission"), rs.getBoolean("execute_permission"), rs.getBoolean("admin_permission")));
            }
        } catch (Throwable e) {
            core_acl.removeAllElements();
        }
        return core_acl;
    }

    /**
     * check access by comparing user / user groups to ACL and return permissions
     * @param attrU XincoCoreUser
     * @param attrACL ACL
     * @return
     */
    public static XincoCoreACE checkAccess(XincoCoreUser attrU, Vector attrACL) {
        int i = 0;
        int j = 0;
        boolean match_ace = false;
        XincoCoreACE core_ace = new XincoCoreACE();
        for (i = 0; i < attrACL.size(); i++) {
            //reset match_ace
            match_ace = false;
            //check if user is mentioned in ACE
            if (((XincoCoreACE) attrACL.elementAt(i)).getXinco_core_user_id() == attrU.getId()) {
                match_ace = true;
            }
            //check if group of user is mentioned in ACE
            if (!match_ace) {
                for (j = 0; j < attrU.getXinco_core_groups().size(); j++) {
                    if (((XincoCoreACE) attrACL.elementAt(i)).getXinco_core_group_id() == ((XincoCoreGroup) attrU.getXinco_core_groups().elementAt(j)).getId()) {
                        match_ace = true;
                        break;
                    }
                }
            }
            //add to rights
            if (match_ace) {
                //modify read permission
                if (!core_ace.isRead_permission()) {
                    core_ace.setRead_permission(((XincoCoreACE) attrACL.elementAt(i)).isRead_permission());
                }
                //modify write permission
                if (!core_ace.isWrite_permission()) {
                    core_ace.setWrite_permission(((XincoCoreACE) attrACL.elementAt(i)).isWrite_permission());
                }
                //modify execute permission
                if (!core_ace.isExecute_permission()) {
                    core_ace.setExecute_permission(((XincoCoreACE) attrACL.elementAt(i)).isExecute_permission());
                }
                //modify admin permission
                if (!core_ace.isAdmin_permission()) {
                    core_ace.setAdmin_permission(((XincoCoreACE) attrACL.elementAt(i)).isAdmin_permission());
                }
            }
        }
        return core_ace;
    }

    /**
     * Set user id
     * @param i
     */
    public void setUserId(int i) {
        this.userID = i;
    }
}
