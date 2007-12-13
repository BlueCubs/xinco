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
 * Name:            XincoCoreGroupServer
 *
 * Description:     group
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
 * Create group object for data structures
 * @author Alexander Manes
 */
public class XincoCoreGroupServer extends XincoCoreGroup {

    /**
     * Create group object for data structures
     * @param id
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreGroupServer(int id, XincoDBManager DBM) throws XincoException {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_group WHERE id=" + id);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setDesignation(rs.getString("designation"));
                setStatus_number(rs.getInt("status_number"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
        } catch (Throwable e) {
            throw new XincoException(e.getMessage());
        }
    }

    /**
     * Create group object for data structures
     * @param id
     * @param designation
     * @param status
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreGroupServer(int id, String designation, int status) throws XincoException {
        setId(id);
        setDesignation(designation);
        setStatus_number(status);
    }

    /**
     * Persist object
     * @param DBM
     * @return
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public int write2DB(XincoDBManager DBM) throws XincoException {
        try {
            if (getId() > 0) {
                XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
                audit.updateAuditTrail("xinco_core_group", new String[]{"id =" + getId()},
                        DBM, "audit.coregroup.change", this.getChangerID());
                DBM.executeUpdate("UPDATE xinco_core_group SET designation='" + getDesignation().replaceAll("'", "\\\\'") + "', status_number=" + getStatus_number() + " WHERE id=" + getId());
            } else {
                setId(DBM.getNewID("xinco_core_group"));
                DBM.executeUpdate("INSERT INTO xinco_core_group VALUES (" + getId() + ", '" + getDesignation().replaceAll("'", "\\\\'") + "', " + getStatus_number() + ")");
            }
            DBM.getConnection().commit();
        } catch (Throwable e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        return getId();
    }

    /**
     * Create complete list of groups
     * @param DBM
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreGroups(XincoDBManager DBM) {
        Vector coreGroups = new Vector();
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_group ORDER BY designation");
            while (rs.next()) {
                coreGroups.add(new XincoCoreGroupServer(rs.getInt("id"), rs.getString("designation"), rs.getInt("status_number")));
            }
        } catch (Throwable e) {
            coreGroups.removeAllElements();
        }
        return coreGroups;
    }
}
