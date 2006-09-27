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
 * Name:            XincoCoreUserServer
 *
 * Description:     user
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

import java.sql.*;
import java.util.Vector;

import com.bluecubs.xinco.core.*;

public class XincoCoreUserServer extends XincoCoreUser {
    
    private void fillXincoCoreGroups(XincoDBManager DBM) throws XincoException {

		setXinco_core_groups(new Vector());

        try {
	        Statement stmt = DBM.con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + getId());
	        while (rs.next()) {
				getXinco_core_groups().addElement(new XincoCoreGroupServer(rs.getInt("xinco_core_group_id"), DBM));
	        }
	        stmt.close();
        } catch (Exception e) {
        	getXinco_core_groups().removeAllElements();
        	throw new XincoException();
        }

    }
    
    private void writeXincoCoreGroups(XincoDBManager DBM) throws XincoException {

        Statement stmt;

        try {
	        stmt = DBM.con.createStatement();
	        stmt.executeUpdate("DELETE FROM xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + getId());
	        stmt.close();
	
	        for (int i=0; i<getXinco_core_groups().size(); i++) {
	            stmt = DBM.con.createStatement();
	            stmt.executeUpdate("INSERT INTO xinco_core_user_has_xinco_core_group VALUES (" + getId() + ", " + ((XincoCoreGroupServer)getXinco_core_groups().elementAt(i)).getId() + ", " + 1 + ")");
	            stmt.close();
	        }
        } catch (Exception e) {
			throw new XincoException();
        }
        
    }
    
    //create user object and login
    public XincoCoreUserServer(String attrUN, String attrUPW, XincoDBManager DBM) throws XincoException {

        try {
            
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_user WHERE username='" + attrUN + "' AND userpassword=MD5('" + attrUPW + "') AND status_number=1");

			//throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
            	RowCount++;
                setId(rs.getInt("id"));
                setUsername(rs.getString("username"));
				setUserpassword(rs.getString("userpassword"));
                setName(rs.getString("name"));
                setFirstname(rs.getString("firstname"));
                setEmail(rs.getString("email"));
                setStatus_number(rs.getInt("status_number"));
            }
			if (RowCount < 1) {
				throw new XincoException();
			}

            stmt.close();
            
            fillXincoCoreGroups(DBM);

        } catch (Exception e) {
        	if (getXinco_core_groups() != null) {
        		getXinco_core_groups().removeAllElements();
        	}
			throw new XincoException();
        }
        
    }
    
    //create user object for data structures
    public XincoCoreUserServer(int attrID, XincoDBManager DBM) throws XincoException {

        try {
            
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_user WHERE id=" + attrID);

			//throw exception if no result found
			int RowCount = 0;
            while (rs.next()) {
				RowCount++;
                setId(rs.getInt("id"));
                setUsername(rs.getString("username"));
                setUserpassword(rs.getString("userpassword"));
                setName(rs.getString("name"));
                setFirstname(rs.getString("firstname"));
                setEmail(rs.getString("email"));
                setStatus_number(rs.getInt("status_number"));
            }
			if (RowCount < 1) {
				throw new XincoException();
			}

            stmt.close();
            
            fillXincoCoreGroups(DBM);

        } catch (Exception e) {
            getXinco_core_groups().removeAllElements();
            throw new XincoException();
        }
        
    }
    
	//create user object
	public XincoCoreUserServer(int attrID, String attrUN, String attrUPW, String attrN, String attrFN, String attrE, int attrSN, XincoDBManager DBM) throws XincoException {

		try {
            
			setId(attrID);
			setUsername(attrUN);
			setUserpassword(attrUPW);
			setName(attrN);
			setFirstname(attrFN);
			setEmail(attrE);
			setStatus_number(attrSN);

			fillXincoCoreGroups(DBM);

		} catch (Exception e) {
			getXinco_core_groups().removeAllElements();
			throw new XincoException();
		}
        
	}
    
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {

        try {
            
            Statement stmt;
            
            if (getId() > 0) {
				stmt = DBM.con.createStatement();
				stmt.executeUpdate("UPDATE xinco_core_user SET username='" + getUsername().replaceAll("'","\\\\'") + "', userpassword=MD5('" + getUserpassword().replaceAll("'","\\\\'") + "'), name='" + getName().replaceAll("'","\\\\'") + "', firstname='" + getFirstname().replaceAll("'","\\\\'") + "', email='" + getEmail().replaceAll("'","\\\\'") + "', status_number=" + getStatus_number() + " WHERE id=" + getId());
				stmt.close();
            } else {
				setId(DBM.getNewID("xinco_core_user"));

				stmt = DBM.con.createStatement();
				stmt.executeUpdate("INSERT INTO xinco_core_user VALUES (" + getId() + ", '" + getUsername().replaceAll("'","\\\\'") + "', MD5('" + getUserpassword().replaceAll("'","\\\\'") + "'), '" + getName().replaceAll("'","\\\\'") + "', '" + getFirstname().replaceAll("'","\\\\'") + "', '" + getEmail().replaceAll("'","\\\\'") + "', " + getStatus_number() + ")");
				stmt.close();
            }
            
            writeXincoCoreGroups(DBM);
            
            DBM.con.commit();
            
        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        
        return getId();
        
    }
    
	//create complete list of users
	public static Vector getXincoCoreUsers(XincoDBManager DBM) {
        
		Vector coreUsers = new Vector();
        
		try {
            
			Statement stmt = DBM.con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_user ORDER BY username");

			while (rs.next()) {
				coreUsers.addElement(new XincoCoreUserServer(rs.getInt("id"), rs.getString("username"), rs.getString("userpassword"), rs.getString("name"), rs.getString("firstname"), rs.getString("email"), rs.getInt("status_number"), DBM));
			}

			stmt.close();
            
		} catch (Exception e) {
			coreUsers.removeAllElements();
		}

		return coreUsers;
	}

}
