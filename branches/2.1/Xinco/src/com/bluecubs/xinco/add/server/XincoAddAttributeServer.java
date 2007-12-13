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
 * Name:            XincoAddAttributeServer
 *
 * Description:     additional attributes of a data object 
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
package com.bluecubs.xinco.add.server;

import com.bluecubs.xinco.add.XincoAddAttribute;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.*;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.server.*;

/**
 * Create add attribute object for data structures
 * @author Alexander Manes
 */
public class XincoAddAttributeServer extends XincoAddAttribute {

    /**
     * Create add attribute object for data structures
     * @param attrID1 xinco_core_data_id
     * @param attrID2 attribute_id
     * @param DBM XincoDBManager
     * @throws com.bluecubs.xinco.core.XincoException
     */
    private static int changerID;

    public XincoAddAttributeServer(int attrID1, int attrID2, XincoDBManager DBM) throws XincoException {
        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_add_attribute " +
                    "WHERE xinco_core_data_id=" + attrID1 + " AND attribute_id=" + attrID2);
            while (rs.next()) {
                setXinco_core_data_id(rs.getInt("xinco_core_data_id"));
                setAttribute_id(rs.getInt("attribute_id"));
                setAttrib_int(rs.getInt("attrib_int"));
                setAttrib_unsignedint(rs.getInt("attrib_unsignedint"));
                setAttrib_double(rs.getInt("attrib_double"));
                setAttrib_varchar(rs.getString("attrib_varchar"));
                setAttrib_text(rs.getString("attrib_text"));
                setAttrib_datetime(new GregorianCalendar());
                getAttrib_datetime().setTime(rs.getDate("attrib_datetime"));
            }
            stmt.close();
        } catch (Throwable e) {
            throw new XincoException();
        }
    }

    /**
     * Create add attribute object for data structures
     * @param attrID1 xinco_core_data_id
     * @param attrID2 attribute_id
     * @param attrI int attribute
     * @param attrUI long attribute
     * @param attrD double attribute
     * @param attrVC varchar attribute
     * @param attrT string attribute
     * @param attrDT date time attribute
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoAddAttributeServer(int attrID1, int attrID2, int attrI, long attrUI, double attrD, String attrVC, String attrT, Calendar attrDT) throws XincoException {
        setXinco_core_data_id(attrID1);
        setAttribute_id(attrID2);
        setAttrib_int(attrI);
        setAttrib_unsignedint(attrUI);
        setAttrib_double(attrD);
        setAttrib_varchar(attrVC);
        setAttrib_text(attrT);
        setAttrib_datetime(attrDT);
    }

    /**
     * Persists object to database
     * @param DBM
     * @return int
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public int write2DB(XincoDBManager DBM) throws XincoException {
        try {
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            String attrT = "";
            String attrVC = "";
            String attrDT = "";
            if (getAttrib_text() != null) {
                attrT = getAttrib_text();
                attrT = attrT.replaceAll("'", "\\\\'");
            } else {
                attrT = "NULL";
            }
            if (getAttrib_varchar() != null) {
                attrVC = getAttrib_varchar();
                attrVC = attrVC.replaceAll("'", "\\\\'");
            } else {
                attrVC = "NULL";
            }
            if (getAttrib_datetime() != null) {
                //convert clone from remote time to local time
                Calendar cal = (Calendar) getAttrib_datetime().clone();
                Calendar ngc = new GregorianCalendar();
                cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - getAttrib_datetime().get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + getAttrib_datetime().get(Calendar.DST_OFFSET)));
                attrDT = "" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
            } else {
                attrDT = "NULL";
            }
            audit.updateAuditTrail("xinco_add_attribute", new String[]{"attribute_id =" + getAttribute_id(), "xinco_core_data_id=" + getXinco_core_data_id()},
                    DBM, "audit.general.create", getChangerID());
            DBM.executeUpdate("INSERT INTO xinco_add_attribute VALUES (" + getXinco_core_data_id() + ", " + getAttribute_id() + ", " + getAttrib_int() + ", " + getAttrib_unsignedint() + ", " + getAttrib_double() + ", '" + attrVC + "', '" + attrT + "', '" + attrDT + "')");
        } catch (Throwable e) {
            //no commit or rollback -> CoreData manages exceptions!
            throw new XincoException();
        }
        return 1;
    }

    /**
     * Remove object from DB
     * @param id 
     * @param data_id 
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException 
     */
    public static void removeFromDB(int id, int data_id, XincoDBManager DBM) throws XincoException {
        try {
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            audit.updateAuditTrail("xinco_add_attribute", new String[]{"attribute_id =" + id, "xinco_core_data_id=" + data_id},
                    DBM, "audit.general.delete", getChangerID());
            DBM.executeUpdate("DELETE FROM xinco_add_attribute WHERE attribute_id=" + id + " and xinco_core_data_id=" + data_id);
        } catch (Throwable e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            e.printStackTrace();
            throw new XincoException();
        }
    }

    /**
     * create complete list of add attributes
     * @param attrID xinco_core_data_id
     * @param DBM XincoDBManager
     * @return vector containing attributes
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoAddAttributes(int attrID, XincoDBManager DBM) {
        Vector addAttributes = new Vector();
        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_add_attribute WHERE xinco_core_data_id =" + attrID + " ORDER BY attribute_id");
            GregorianCalendar cal;
            while (rs.next()) {
                cal = new GregorianCalendar();
                if (rs.getDate("attrib_datetime") != null) {
                    cal.setTime(rs.getDate("attrib_datetime"));
                }
                addAttributes.addElement(new XincoAddAttributeServer(rs.getInt("xinco_core_data_id"), rs.getInt("attribute_id"), rs.getInt("attrib_int"), rs.getLong("attrib_unsignedint"), rs.getDouble("attrib_double"), rs.getString("attrib_varchar"), rs.getString("attrib_text"), cal));
            }
            stmt.close();
        } catch (Throwable e) {
            addAttributes.removeAllElements();
        }
        return addAttributes;
    }

    /**
     * Get changerID
     * @return int
     */
    public static int getChangerID() {
        return changerID;
    }

    /**
     * Set changerID
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }
}
