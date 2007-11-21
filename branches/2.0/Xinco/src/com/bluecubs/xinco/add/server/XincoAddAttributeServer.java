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

import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.*;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.server.*;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class XincoAddAttributeServer extends XincoAddAttribute {

    private DatatypeFactory df;
    //create add attribute object for data structures
    public XincoAddAttributeServer(int attrID1, int attrID2, XincoDBManager DBM) throws XincoException {
        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_add_attribute " +
                    "WHERE xinco_core_data_id=" + attrID1 + " AND attribute_id=" + attrID2);
            while (rs.next()) {
                setXincoCoreDataId(rs.getInt("xinco_core_data_id"));
                setAttributeId(rs.getInt("attribute_id"));
                setAttribInt(rs.getInt("attrib_int"));
                setAttribUnsignedint(rs.getInt("attrib_unsignedint"));
                setAttribDouble(rs.getInt("attrib_double"));
                setAttribVarchar(rs.getString("attrib_varchar"));
                setAttribText(rs.getString("attrib_text"));
                df = DatatypeFactory.newInstance();
                Calendar cal = (Calendar) rs.getDate("attrib_datetime").clone();
                XMLGregorianCalendar xgc = df.newXMLGregorianCalendar();
                xgc.setYear(cal.get(Calendar.YEAR) - 1900);
                xgc.setMonth(cal.get(Calendar.MONTH));
                xgc.setDay(cal.get(Calendar.DAY_OF_YEAR));
                xgc.setHour(cal.get(Calendar.HOUR_OF_DAY));
                xgc.setMinute(cal.get(Calendar.MINUTE));
                xgc.setSecond(cal.get(Calendar.SECOND));
                xgc.setMillisecond(cal.get(Calendar.MILLISECOND));
                setAttribDatetime(xgc);
            }
            stmt.close();
        } catch (Exception e) {
            throw new XincoException();
        }
    }

    //create add attribute object for data structures
    public XincoAddAttributeServer(int attrID1, int attrID2, int attrI, long attrUI, double attrD, String attrVC, String attrT, Calendar cal) throws XincoException {
        setXincoCoreDataId(attrID1);
        setAttributeId(attrID2);
        setAttribInt(attrI);
        setAttribUnsignedint(attrUI);
        setAttribDouble(attrD);
        setAttribVarchar(attrVC);
        setAttribText(attrT);
        XMLGregorianCalendar xgc = df.newXMLGregorianCalendar();
        xgc.setYear(cal.get(Calendar.YEAR) - 1900);
        xgc.setMonth(cal.get(Calendar.MONTH));
        xgc.setDay(cal.get(Calendar.DAY_OF_YEAR));
        xgc.setHour(cal.get(Calendar.HOUR_OF_DAY));
        xgc.setMinute(cal.get(Calendar.MINUTE));
        xgc.setSecond(cal.get(Calendar.SECOND));
        xgc.setMillisecond(cal.get(Calendar.MILLISECOND));
        setAttribDatetime(xgc);
    }

    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {

        try {

            Statement stmt;
            String attrT = "";
            String attrVC = "";
            String attrDT = "";
            if (getAttribText() != null) {
                attrT = getAttribText();
                attrT = attrT.replaceAll("'", "\\\\'");
            } else {
                attrT = "NULL";
            }
            if (getAttribVarchar() != null) {
                attrVC = getAttribVarchar();
                attrVC = attrVC.replaceAll("'", "\\\\'");
            } else {
                attrVC = "NULL";
            }
            if (getAttribDatetime() != null) {
                //convert clone from remote time to local time
                Calendar cal = (Calendar) getAttribDatetime().clone();
                Calendar ngc = new GregorianCalendar();
                cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - getAttribDatetime().getTimezone()) - (ngc.get(Calendar.DST_OFFSET)));
                attrDT = "" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
            } else {
                attrDT = "NULL";
            }

            stmt = DBM.getConnection().createStatement();
            stmt.executeUpdate("INSERT INTO xinco_add_attribute VALUES (" + 
                    getXincoCoreDataId() + ", " + getAttributeId() + ", " + 
                    getAttribInt() + ", " + getAttribUnsignedint() + ", " + 
                    getAttribDouble() + ", '" + attrVC + "', '" + attrT + "', '" + 
                    attrDT + "')");
            stmt.close();
        } catch (Exception e) {
            //no commit or rollback -> CoreData manages exceptions!
            throw new XincoException();
        }
        return 1;
    }
    //create complete list of add attributes
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
        } catch (Exception e) {
            addAttributes.removeAllElements();
        }

        return addAttributes;
    }
}
