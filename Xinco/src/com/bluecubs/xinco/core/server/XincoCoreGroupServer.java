/**
 *Copyright 2010 blueCubs.com
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

public class XincoCoreGroupServer extends XincoCoreGroup {
    
    //create group object for data structures
    public XincoCoreGroupServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_group WHERE id=" + attrID);
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
            stmt.close();
        } catch (XincoException e) {
            throw new XincoException(e.getMessage());
        }
      catch (SQLException e)
      {
        throw new XincoException(e.getMessage());
      }
    }
    
    //create group object for data structures
    public XincoCoreGroupServer(int attrID, String attrD, int attrSN) throws XincoException {
        setId(attrID);
        setDesignation(attrD);
        setStatus_number(attrSN);
    }
    
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException{
        try {
            Statement stmt;
            if (getId() > 0) {
                XincoCoreAuditServer audit= new XincoCoreAuditServer();
                audit.updateAuditTrail("xinco_core_group",new String [] {"id ="+getId()},
                        DBM,"audit.coregroup.change",this.getChangerID());
                stmt = DBM.con.createStatement();
                stmt.executeUpdate("UPDATE xinco_core_group SET designation='" + getDesignation().replaceAll("'","\\\\'") + "', status_number=" + getStatus_number() + " WHERE id=" + getId());
                stmt.close();
            } else {
                setId(DBM.getNewID("xinco_core_group"));
                stmt = DBM.con.createStatement();
                stmt.executeUpdate("INSERT INTO xinco_core_group VALUES (" + getId() + ", '" + getDesignation().replaceAll("'","\\\\'") + "', " + getStatus_number() + ")");
                stmt.close();
            }
            DBM.con.commit();
        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (SQLException erollback) {
            }
            throw new XincoException();
        }
        return getId();
    }
    
    //create complete list of groups
    public static Vector getXincoCoreGroups(XincoDBManager DBM) {
        Vector coreGroups = new Vector();
        try {
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_group ORDER BY designation");
            while (rs.next()) {
                coreGroups.addElement(new XincoCoreGroupServer(rs.getInt("id"), rs.getString("designation"), rs.getInt("status_number")));
            }
            stmt.close();
        } catch (XincoException e) {
            coreGroups.removeAllElements();
        }
      catch (SQLException e)
      {
        coreGroups.removeAllElements();
      }
        return coreGroups;
    }
}
