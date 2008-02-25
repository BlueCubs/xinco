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
 * Name:            XincoCoreDataTypeAttributeServer
 *
 * Description:     data type attribute
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
import java.util.Vector;
import java.sql.*;

import com.bluecubs.xinco.core.*;

/**
 * Create data type attribute object for data structures
 * @author Alexander Manes
 */
public class XincoCoreDataTypeAttributeServer extends XincoCoreDataTypeAttribute {

    private static int changer = 0;

    /**
     * Create data type attribute object for data structures
     * @param attrID1
     * @param attrID2
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreDataTypeAttributeServer(int attrID1, int attrID2, XincoDBManager DBM) throws XincoException {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_data_type_attribute WHERE xinco_core_data_type_id=" + attrID1 + " AND attribute_id=" + attrID2);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setXinco_core_data_type_id(rs.getInt("xinco_core_data_type_id"));
                setAttribute_id(rs.getInt("attribute_id"));
                setDesignation(rs.getString("designation"));
                setData_type(rs.getString("data_type"));
                setSize(rs.getInt("size"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
        } catch (Throwable e) {
            throw new XincoException();
        }
    }

    /**
     * Create data type attribute object for data structures
     * @param attrID1
     * @param attrID2
     * @param attrD
     * @param attrDT
     * @param attrS
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreDataTypeAttributeServer(int attrID1, int attrID2, String attrD, String attrDT, int attrS) throws XincoException {
        setXinco_core_data_type_id(attrID1);
        setAttribute_id(attrID2);
        setDesignation(attrD);
        setData_type(attrDT);
        setSize(attrS);
    }

    /**
     * Write to db
     * @param DBM
     * @return
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public int write2DB(XincoDBManager DBM) throws XincoException {
        try {
            DBM.executeUpdate("INSERT INTO xinco_core_data_type_attribute VALUES (" + getXinco_core_data_type_id() + ", " + getAttribute_id() + ", '" + getDesignation() + "', '" + getData_type() + "', " + getSize() + ")");
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            audit.updateAuditTrail("xinco_core_data_type_attribute", new String[]{"xinco_core_data_type_=" + getXinco_core_data_type_id(),
                "attribute_id=" + getAttribute_id()
            }, DBM, "audit.datatype.attribute.change", this.getChangerID());

            DBM.executeUpdate("INSERT INTO xinco_add_attribute SELECT id, " + getAttribute_id() + ", 0, 0, 0, '', '', now() FROM xinco_core_data WHERE xinco_core_data_type_id = " + getXinco_core_data_type_id());
            audit.updateAuditTrail("xinco_add_attribute", new String[]{"xinco_add_attribute.attribute_id=" + getAttribute_id(),
                "xinco_add_attribute.xinco_core_data_id IN (SELECT id FROM xinco_core_data WHERE xinco_core_data.xinco_core_data_type_id=" +
                getXinco_core_data_type_id() + ")"
            },
                    DBM, "audit.datatype.attribute.change", this.getChangerID());
            DBM.getConnection().commit();
        } catch (Throwable e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        return 0;
    }

    /**
     * Remove from DB
     * @param xinco_core_data_type_attribute
     * @param DBM
     * @param userID
     * @return int
     * @throws com.bluecubs.xinco.core.XincoException
     */
    @SuppressWarnings("static-access")
    public static int removeFromDB(XincoCoreDataTypeAttribute xinco_core_data_type_attribute, XincoDBManager DBM, int userID) throws XincoException {
        try {
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            ResultSet rs = DBM.executeQuery("select xinco_core_data_id,attribute_id FROM xinco_add_attribute WHERE xinco_add_attribute.attribute_id=" +
                    xinco_core_data_type_attribute.getAttribute_id() + " AND xinco_add_attribute.xinco_core_data_id IN (SELECT id FROM xinco_core_data WHERE xinco_core_data.xinco_core_data_type_id=" +
                    xinco_core_data_type_attribute.getXinco_core_data_type_id() + ")");
            while (rs.next()) {
                XincoAddAttributeServer temp = new XincoAddAttributeServer(rs.getInt("xinco_core_data_id"),
                        rs.getInt("attribute_id"), DBM);
                temp.removeFromDB(rs.getInt("attribute_id"), userID, DBM);
            }
            audit.updateAuditTrail("xinco_core_data_type_attribute", new String[]{"xinco_core_data_type_id=" + xinco_core_data_type_attribute.getXinco_core_data_type_id(), "attribute_id=" + xinco_core_data_type_attribute.getAttribute_id()},
                    DBM, "audit.general.delete", userID);
            DBM.executeUpdate("DELETE FROM xinco_core_data_type_attribute WHERE xinco_core_data_type_id=" + xinco_core_data_type_attribute.getXinco_core_data_type_id() + " AND attribute_id=" + xinco_core_data_type_attribute.getAttribute_id());
        } catch (Throwable e) {
            throw new XincoException();
        }
        return 0;
    }

    /**
     * Create complete list of data type attributes
     * @param attrID
     * @param DBM
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreDataTypeAttributes(int attrID, XincoDBManager DBM) {
        Vector coreDataTypeAttributes = new Vector();
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_data_type_attribute WHERE xinco_core_data_type_id =" + attrID + " ORDER BY attribute_id");

            while (rs.next()) {
                coreDataTypeAttributes.add(new XincoCoreDataTypeAttributeServer(rs.getInt("xinco_core_data_type_id"), rs.getInt("attribute_id"), rs.getString("designation"), rs.getString("data_type"), rs.getInt("size")));
            }
        } catch (Throwable e) {
            coreDataTypeAttributes.removeAllElements();
        }
        return coreDataTypeAttributes;
    }
}