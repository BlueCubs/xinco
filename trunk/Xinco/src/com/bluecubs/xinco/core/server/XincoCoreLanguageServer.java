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
 * Name:            XincoCoreLanguageServer
 *
 * Description:     language
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
import com.bluecubs.xinco.general.AuditTrail;
import java.util.ResourceBundle;

public class XincoCoreLanguageServer extends XincoCoreLanguage {
    
    //create language object for data structures
    public XincoCoreLanguageServer(int attrID, XincoDBManager DBM) throws XincoException {
        
        try {
            
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_language WHERE id=" + attrID);
            
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setSign(rs.getString("sign"));
                setDesignation(rs.getString("designation"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
            
            stmt.close();
            
        } catch (Exception e) {
            throw new XincoException();
        }
        
    }
    
    //create language object for data structures
    public XincoCoreLanguageServer(int attrID, String attrS, String attrD) throws XincoException {
        
        setId(attrID);
        setSign(attrS);
        setDesignation(attrD);
        
    }
    
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {
        
        try {
            
            Statement stmt;
            
            if (getId() > 0) {
                stmt = DBM.getConnection().createStatement();
                AuditTrail audit= new AuditTrail();
                ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
                audit.updateAuditTrail("xinco_core_language",new String [] {"id ="+getId()},
                        DBM,xerb.getString("audit.language.change"),this.getChangerID());
                stmt.executeUpdate("UPDATE xinco_core_language SET sign='" + getSign().replaceAll("'","\\\\'") + "', designation='" + getDesignation().replaceAll("'","\\\\'") + "' WHERE id=" + getId());
                stmt.close();
            } else {
                setId(DBM.getNewID("xinco_core_language"));
                
                stmt = DBM.getConnection().createStatement();
                stmt.executeUpdate("INSERT INTO xinco_core_language VALUES (" + getId() + ", '" + getSign().replaceAll("'","\\\\'") + "', '" + getDesignation().replaceAll("'","\\\\'") + "')");
                stmt.close();
            }
            
            DBM.getConnection().commit();
            
        } catch (Exception e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        
        return getId();
        
    }
    
    //delete from db
    public static int deleteFromDB(XincoCoreLanguage attrCL, XincoDBManager DBM,int userID) throws XincoException {
        
        try {
            
            Statement stmt = null;
            
            stmt = DBM.getConnection().createStatement();
            AuditTrail audit= new AuditTrail();
            audit.updateAuditTrail("xinco_core_language",new String [] {"id ="+attrCL.getId()},
                    DBM,"audit.general.delete",userID);
            stmt.executeUpdate("DELETE FROM xinco_core_language WHERE id=" + attrCL.getId());
            stmt.close();
            
            DBM.getConnection().commit();
            
        } catch (Exception e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        
        return 0;
        
    }
    
    //create complete list of languages
    public static Vector getXincoCoreLanguages(XincoDBManager DBM) {
        
        Vector coreLanguages = new Vector();
        
        try {
            
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_language ORDER BY designation");
            
            while (rs.next()) {
                coreLanguages.addElement(new XincoCoreLanguageServer(rs.getInt("id"), rs.getString("sign"), rs.getString("designation")));
            }
            
            stmt.close();
            
        } catch (Exception e) {
            coreLanguages.removeAllElements();
        }
        
        return coreLanguages;
    }
    
    //check if language is in use by other objects
    public static boolean isLanguageUsed(XincoCoreLanguage xcl, XincoDBManager DBM) {
        
        boolean is_used = false;
        
        try {
            
            Statement stmt = null;
            ResultSet rs = null;
            
            stmt = DBM.getConnection().createStatement();
            rs = stmt.executeQuery("SELECT 1 FROM xinco_core_node WHERE xinco_core_language_id = " + xcl.getId());
            while (rs.next()) {
                is_used = true;
            }
            stmt.close();
            
            if (!is_used) {
                stmt = DBM.getConnection().createStatement();
                rs = stmt.executeQuery("SELECT 1 FROM xinco_core_data WHERE xinco_core_language_id = " + xcl.getId());
                while (rs.next()) {
                    is_used = true;
                }
                stmt.close();
            }
            
        } catch (Exception e) {
            is_used = true; // rather lock language in case of error!
        }
        
        return is_used;
    }
    
}
