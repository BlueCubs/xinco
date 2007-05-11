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
import java.util.Calendar;
import java.util.GregorianCalendar;

public class XincoCoreDataTypeAttributeServer extends XincoCoreDataTypeAttribute {
    private static int changer=0;
    //create data type attribute object for data structures
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
            try {
                DBM.finalize();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
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
            XincoCoreAuditTrail audit= new XincoCoreAuditTrail();
            if(getAttribute_id()==0){
                DBM.executeUpdate("INSERT INTO xinco_core_data_type_attribute VALUES (" +
                        getXinco_core_data_type_id() + ", " + getAttribute_id() + ", '" +
                        getDesignation() + "', '" + getData_type() + "', " + getSize() +
                        ")");
                audit.updateAuditTrail("xinco_core_data_type_attribute",new String [] {"xinco_core_data_type_id=" + getXinco_core_data_type_id(),
                "attribute_id =" +getAttribute_id()},
                        DBM,"audit.general.create",this.getChangerID());
                Calendar cal = new GregorianCalendar().getInstance();
                cal.setTime(new Timestamp(System.currentTimeMillis()));
                XincoAddAttributeServer xaas = new XincoAddAttributeServer(getXinco_core_data_type_id(),
                        getAttribute_id(),0,0,0,"","",cal);
                xaas.write2DB(DBM);
                try {
                    DBM.finalize();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }else{
                DBM.executeUpdate("Update xinco_core_data_type_attribute set xinco_core_data_type_id=" +
                        getXinco_core_data_type_id() + ", attribute_id=" + getAttribute_id() + ", designation='" +
                        getDesignation() + "', data_type='" + getData_type() + "', size=" + getSize() +
                        " where xinco_core_data_type_id=" +getXinco_core_data_type_id() +
                        " and attribute_id=" + getAttribute_id());
                audit.updateAuditTrail("xinco_core_data_type_attribute",new String [] {"xinco_core_data_type_id=" + getXinco_core_data_type_id(),
                "attribute_id =" +getAttribute_id()},
                        DBM,"audit.general.modified",this.getChangerID());
            }
            DBM.getCon().commit();
            
        } catch (Exception e) {
            try {
                DBM.getCon().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        
        return 0;
        
    }
    
    //delete from db
    public static int removeFromDB(XincoCoreDataTypeAttribute attrCDTA, XincoDBManager DBM, int userID) throws XincoException { 
        try {
            XincoCoreAuditTrail audit= new XincoCoreAuditTrail();
            //Remove all related add attributes from all related data
            XincoAddAttributeServer xaas = new XincoAddAttributeServer();
            xaas.removeAllFromDBForDataType(attrCDTA.getXinco_core_data_type_id(),
                    attrCDTA.getAttribute_id(),userID,DBM);
//            DBM.executeUpdate("DELETE FROM xinco_add_attribute WHERE xinco_add_attribute.attribute_id=" +
//                     + " AND xinco_add_attribute.xinco_core_data_id IN (SELECT id FROM xinco_core_data WHERE xinco_core_data.xinco_core_data_type_id=" +
//                    attrCDTA.getXinco_core_data_type_id() + ")");
//            DBM.getCon().commit();
            
            audit.updateAuditTrail("xinco_core_data_type_attribute",
                    new String [] {"xinco_core_data_type_id=" + 
                            attrCDTA.getXinco_core_data_type_id(),
                    "attribute_id=" + attrCDTA.getAttribute_id()},
                    DBM,"audit.general.delete",userID);
            DBM.getCon().commit();
            DBM.executeUpdate("DELETE FROM xinco_core_data_type_attribute WHERE " +
                    "xinco_core_data_type_id=" + attrCDTA.getXinco_core_data_type_id() +
                    " AND attribute_id=" + attrCDTA.getAttribute_id());
            DBM.getCon().commit();
            try {
                DBM.finalize();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            try {
                DBM.getCon().rollback();
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
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_data_type_attribute WHERE xinco_core_data_type_id =" + attrID + " ORDER BY attribute_id");
            while (rs.next()) {
                coreDataTypeAttributes.addElement(new XincoCoreDataTypeAttributeServer(rs.getInt("xinco_core_data_type_id"), rs.getInt("attribute_id"), rs.getString("designation"), rs.getString("data_type"), rs.getInt("size")));
            }
            try {
                DBM.finalize();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            coreDataTypeAttributes.removeAllElements();
        }
        
        return coreDataTypeAttributes;
    }
    
}
