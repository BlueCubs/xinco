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

public class XincoAddAttributeServer extends XincoAddAttribute {
    //create add attribute object for data structures
    public XincoAddAttributeServer(){
        
    }
    public XincoAddAttributeServer(int attrID1, int attrID2, XincoDBManager DBM) throws XincoException {
        Statement stmt=null;
        try {
            stmt = DBM.getCon().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_add_attribute " +
                    "WHERE xinco_core_data_id=" + attrID1 + " AND attribute_id=" + attrID2);
            if(rs.next()) {
                setXinco_core_data_id(rs.getInt("xinco_core_data_id"));
                setAttribute_id(rs.getInt("attribute_id"));
                setAttrib_int(rs.getInt("attrib_int"));
                setAttrib_unsignedint(rs.getInt("attrib_unsignedint"));
                setAttrib_double(rs.getInt("attrib_double"));
                setAttrib_varchar(rs.getString("attrib_varchar"));
                setAttrib_text(rs.getString("attrib_text"));
                setAttrib_datetime(new GregorianCalendar());
                getAttrib_datetime().setTime(rs.getTimestamp("attrib_datetime"));
                write2DB(new XincoDBManager());
            }
            stmt.close();
        } catch (Exception e) {
            if(stmt!=null){
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new XincoException();
        }
    }
    
    //create add attribute object for data structures
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
    
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {
        
        try {
            XincoCoreAuditTrail audit= new XincoCoreAuditTrail();
            Statement stmt;
            String attrT = "";
            String attrVC = "";
            String attrDT = "";
            if (getAttrib_text() != null) {
                attrT = getAttrib_text();
                attrT = attrT.replaceAll("'","\\\\'");
            } else {
                attrT = "NULL";
            }
            if (getAttrib_varchar() != null) {
                attrVC = getAttrib_varchar();
                attrVC = attrVC.replaceAll("'","\\\\'");
            } else {
                attrVC = "NULL";
            }
            if (getAttrib_datetime() != null) {
                //convert clone from remote time to local time
                Calendar cal = (Calendar)getAttrib_datetime().clone();
                Calendar ngc = new GregorianCalendar();
                cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - getAttrib_datetime().get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + getAttrib_datetime().get(Calendar.DST_OFFSET)) );
                attrDT = "" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
            } else {
                attrDT = "NULL";
            }
            
            stmt = DBM.getCon().createStatement();
            String sql="";
            //get existing attributes for this data
            ResultSet rs = stmt.executeQuery("select attribute_id from xinco_add_attribute" +
                    " where xinco_core_data_id="+ getXinco_core_data_id() +
                    " and attribute_id=" + getAttribute_id());
            //Check if this attribute already exists to decide if it should be updated or inserted
            boolean isNew=true;
            while(rs.next()){
                if(rs.getInt("attribute_id")==getAttribute_id())
                    isNew=false;
            }
            if(!isNew){
                sql="update xinco_add_attribute set xinco_core_data_id=" +
                        getXinco_core_data_id() + ", attribute_id=" + getAttribute_id() +
                        ",attrib_int= " + getAttrib_int() + ", attrib_unsignedint=" + getAttrib_unsignedint() +
                        ", attrib_double=" + getAttrib_double() + ", attrib_varchar='" + attrVC + "', " +
                        "attrib_text= '" + attrT + "', attrib_datetime ='" + attrDT + "' where xinco_core_data_id="+
                        getXinco_core_data_id() + " and attribute_id=" + getAttribute_id();
                stmt.executeUpdate(sql);
                if(isChange())
                    audit.updateAuditTrail("xinco_add_attribute",new String [] {"attribute_id ="+getAttribute_id(),
                    "xinco_core_data_id="+getXinco_core_data_id()},
                            DBM,"audit.addattribute.change",this.getChangerID());
            } else{
                //Attributes already comes with attribute id assigned. If not...
                if(getAttribute_id()==0){
                    rs = stmt.executeQuery("select count(attribute_id) from xinco_add_attribute" +
                    " where xinco_core_data_id="+ getXinco_core_data_id() +
                    " and attribute_id=" + getAttribute_id());
                    setAttribute_id(rs.getInt(1));
                }
                sql="INSERT INTO xinco_add_attribute VALUES (" + getXinco_core_data_id() +
                        ", " + getAttribute_id() + ", " + getAttrib_int() + ", " +
                        getAttrib_unsignedint() + ", " + getAttrib_double() + ", '" +
                        attrVC + "', '" + attrT + "', '" + attrDT + "')";
                System.out.println(sql);
                stmt.executeUpdate(sql);
                audit.updateAuditTrail("xinco_add_attribute",new String [] {"attribute_id ="+getAttribute_id(),
                "xinco_core_data_id="+getXinco_core_data_id()},
                        DBM,"audit.general.create",getChangerID());
            }
            stmt.close();
            DBM.getCon().commit();
        } catch (Exception e) {
            //no commit or rollback -> CoreData manages exceptions!
            e.printStackTrace();
            throw new XincoException();
        }
        return 1;
    }
    //create complete list of add attributes
    public static Vector getXincoAddAttributes(int attrID, XincoDBManager DBM) {
        Vector addAttributes = new Vector();
        try {
            Statement stmt = DBM.getCon().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_add_attribute WHERE xinco_core_data_id =" + attrID + " ORDER BY attribute_id");
            
            GregorianCalendar cal;
            while (rs.next()) {
                cal = new GregorianCalendar();
                if (rs.getDate("attrib_datetime") != null) {
                    cal.setTime(rs.getTimestamp("attrib_datetime"));
                }
                addAttributes.addElement(new XincoAddAttributeServer(rs.getInt("xinco_core_data_id"),
                        rs.getInt("attribute_id"), rs.getInt("attrib_int"),
                        rs.getLong("attrib_unsignedint"), rs.getDouble("attrib_double"),
                        rs.getString("attrib_varchar"), rs.getString("attrib_text"), cal));
            }
            
            stmt.close();
        } catch (Exception e) {
            addAttributes.removeAllElements();
            e.printStackTrace();
        }
        
        return addAttributes;
    }
    
    public static int deleteFromDB(int xinco_core_data_id, int attribute_id, XincoDBManager DBM, int userID) throws XincoException {
        Statement stmt = null;
        XincoCoreAuditTrail audit= new XincoCoreAuditTrail();
        try {
            stmt = DBM.getCon().createStatement();
            stmt.executeUpdate("DELETE FROM xinco_add_attribute WHERE xinco_add_attribute.attribute_id=" +
                    attribute_id + " AND xinco_add_attribute.xinco_core_data_id = "+xinco_core_data_id);
            audit.updateAuditTrail("xinco_add_attribute",new String [] {"xinco_add_attribute.attribute_id=" + attribute_id,
            "xinco_add_attribute.xinco_core_data_id ="+xinco_core_data_id},
                    DBM,"audit.general.delete",userID);
            stmt.close();
            
            DBM.getCon().commit();
        } catch (SQLException ex) {
            try {
                DBM.getCon().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        return 0;
    }
}
