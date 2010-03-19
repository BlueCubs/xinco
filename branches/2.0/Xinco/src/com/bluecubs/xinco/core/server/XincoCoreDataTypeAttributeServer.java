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

import java.util.Vector;
import java.sql.*;

import com.bluecubs.xinco.core.*;

public class XincoCoreDataTypeAttributeServer extends XincoCoreDataTypeAttribute {

    private static int changer = 0;
    //create data type attribute object for data structures

    public XincoCoreDataTypeAttributeServer(int attrID1, int attrID2, XincoDBManager DBM) throws XincoException {

        try {
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_data_type_attribute WHERE xinco_core_data_type_id=" + attrID1 + " AND attribute_id=" + attrID2);

            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setXinco_core_data_type_id(rs.getInt("xinco_core_data_type_id"));
                setAttribute_id(rs.getInt("attribute_id"));
                setDesignation(rs.getString("designation"));
                setData_type(rs.getString("data_type"));
                setSize(rs.getInt("attr_size"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }

            stmt.close();
        } catch (Exception e) {
            throw new XincoException();
        }

    }

    //create data type attribute object for data structures
    public XincoCoreDataTypeAttributeServer(int attrID1, int attrID2, String attrD, String attrDT, int attrS) throws XincoException {

        setXinco_core_data_type_id(attrID1);
        setAttribute_id(attrID2);
        setDesignation(attrD);
        setData_type(attrDT);
        setSize(attrS);

    }

    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {

        try {

            Statement stmt = null;

            stmt = DBM.con.createStatement();
            stmt.executeUpdate("INSERT INTO xinco_core_data_type_attribute VALUES (" + getXinco_core_data_type_id() + ", " + getAttribute_id() + ", '" + getDesignation() + "', '" + getData_type() + "', " + getSize() + ")");
            stmt.close();

            /*
             * Aduit Trail Table (*_t) cannot handle multiple row changes!!!
            XincoCoreAuditServer audit= new XincoCoreAuditServer();
            audit.updateAuditTrail("xinco_add_attribute",new String [] {"xinco_add_attribute.attribute_id=" + getAttribute_id(),
            "xinco_add_attribute.xinco_core_data_id IN (SELECT id FROM xinco_core_data WHERE xinco_core_data.xinco_core_data_type_id=" +
            getXinco_core_data_type_id()+ ")"},
            DBM,"audit.datatype.attribute.change",this.getChangerID());
             */

            stmt = DBM.con.createStatement();
            stmt.executeUpdate("INSERT INTO xinco_add_attribute SELECT id, " + getAttribute_id() + ", 0, 0, 0, '', '', now() FROM xinco_core_data WHERE xinco_core_data_type_id = " + getXinco_core_data_type_id());
            stmt.close();

            DBM.con.commit();

        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }

        return 0;

    }

    //delete from db
    public static int deleteFromDB(XincoCoreDataTypeAttribute attrCDTA, XincoDBManager DBM, int userID) throws XincoException {

        try {

            Statement stmt = null;

            stmt = DBM.con.createStatement();
            XincoCoreAuditServer audit = new XincoCoreAuditServer();
            /*
             * Aduit Trail Table (*_t) cannot handle multiple row changes!!!
            audit.updateAuditTrail("xinco_add_attribute",new String [] {"xinco_add_attribute.attribute_id=" + attrCDTA.getAttribute_id(),
            "xinco_add_attribute.xinco_core_data_id IN (SELECT id FROM xinco_core_data WHERE xinco_core_data.xinco_core_data_type_id=" +
            attrCDTA.getXinco_core_data_type_id()+ ")"},
            DBM,"audit.general.delete",userID);
             */
            stmt.executeUpdate("DELETE FROM xinco_add_attribute WHERE xinco_add_attribute.attribute_id="
                    + attrCDTA.getAttribute_id() + " AND xinco_add_attribute.xinco_core_data_id IN (SELECT id FROM xinco_core_data WHERE xinco_core_data.xinco_core_data_type_id="
                    + attrCDTA.getXinco_core_data_type_id() + ")");
            stmt.close();

            stmt = DBM.con.createStatement();
            audit.updateAuditTrail("xinco_core_data_type_attribute", new String[]{"xinco_core_data_type_id=" + attrCDTA.getXinco_core_data_type_id(), "attribute_id=" + attrCDTA.getAttribute_id()},
                    DBM, "audit.general.delete", userID);
            stmt.executeUpdate("DELETE FROM xinco_core_data_type_attribute WHERE xinco_core_data_type_id=" + attrCDTA.getXinco_core_data_type_id() + " AND attribute_id=" + attrCDTA.getAttribute_id());
            stmt.close();

            DBM.con.commit();

        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        return 0;
    }

    //create complete list of data type attributes
    public static Vector getXincoCoreDataTypeAttributes(int attrID, XincoDBManager DBM) {
        Vector coreDataTypeAttributes = new Vector();
        try {
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_data_type_attribute WHERE xinco_core_data_type_id =" + attrID + " ORDER BY attribute_id");
            while (rs.next()) {
                coreDataTypeAttributes.addElement(new XincoCoreDataTypeAttributeServer(rs.getInt("xinco_core_data_type_id"), rs.getInt("attribute_id"), rs.getString("designation"), rs.getString("data_type"), rs.getInt("attr_size")));
            }
            stmt.close();
        } catch (Exception e) {
            coreDataTypeAttributes.removeAllElements();
        }
        return coreDataTypeAttributes;
    }
}
