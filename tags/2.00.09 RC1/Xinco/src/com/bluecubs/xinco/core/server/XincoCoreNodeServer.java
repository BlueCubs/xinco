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
 * Name:            XincoCoreNodeServer
 *
 * Description:     node object
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
import com.bluecubs.xinco.index.*;

public class XincoCoreNodeServer extends XincoCoreNode {
    
    //create node object for data structures
    public XincoCoreNodeServer(int attrID, XincoDBManager DBM) throws XincoException {
        
        try {
            
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_node WHERE id=" + attrID);
            
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setXinco_core_node_id(rs.getInt("xinco_core_node_id"));
                setXinco_core_language(new XincoCoreLanguageServer(rs.getInt("xinco_core_language_id"), DBM));
                setDesignation(rs.getString("designation"));
                setStatus_number(rs.getInt("status_number"));
                setXinco_core_nodes(new Vector());
                setXinco_core_data(new Vector());
                //load acl for this object
                setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(rs.getInt("id"), "xinco_core_node_id", DBM));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
            
            stmt.close();
            
        } catch (Exception e) {
            setXinco_core_language(null);
            getXinco_core_acl().removeAllElements();
            getXinco_core_nodes().removeAllElements();
            getXinco_core_data().removeAllElements();
            throw new XincoException();
        }
        
    }
    
    //create node object for data structures
    public XincoCoreNodeServer(int attrID, int attrCNID, int attrLID, String attrD, int attrSN, XincoDBManager DBM) throws XincoException {
        
        try {
            setId(attrID);
            setXinco_core_node_id(attrCNID);
            setXinco_core_language(new XincoCoreLanguageServer(attrLID, DBM));
            setDesignation(attrD);
            setStatus_number(attrSN);
            setXinco_core_nodes(new Vector());
            setXinco_core_data(new Vector());
            //load acl for this object
            setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(getId(), "xinco_core_node_id", DBM));
        } catch (Exception e) {
            setXinco_core_language(null);
            getXinco_core_acl().removeAllElements();
            getXinco_core_nodes().removeAllElements();
            getXinco_core_data().removeAllElements();
            throw new XincoException();
        }
        
    }
    
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {
        
        try {
            
            Statement stmt;
            String xcnid = "";
            
            if (getId() > 0) {
                stmt = DBM.con.createStatement();
                //set values of nullable attributes
                if (getXinco_core_node_id() == 0) {
                    xcnid = "NULL";
                } else {
                    xcnid = "" + getXinco_core_node_id();
                }
                XincoCoreAuditServer audit= new XincoCoreAuditServer();
                audit.updateAuditTrail("xinco_core_node",new String [] {"id ="+getId()},
                        DBM,"audit.corenode.change",this.getChangerID());
                stmt.executeUpdate("UPDATE xinco_core_node SET xinco_core_node_id=" + xcnid + ", xinco_core_language_id=" + getXinco_core_language().getId() + ", designation='" + getDesignation().replaceAll("'","\\\\'") + "', status_number=" + getStatus_number() + " WHERE id=" + getId());
                stmt.close();
            } else {
                setId(DBM.getNewID("xinco_core_node"));
                
                stmt = DBM.con.createStatement();
                //set values of nullable attributes
                if (getXinco_core_node_id() == 0) {
                    xcnid = "NULL";
                } else {
                    xcnid = "" + getXinco_core_node_id();
                }
                stmt.executeUpdate("INSERT INTO xinco_core_node VALUES (" + getId() + ", " + xcnid + ", " + getXinco_core_language().getId() + ", '" + getDesignation().replaceAll("'","\\\\'") + "', " + getStatus_number() + ")");
                stmt.close();
            }
            
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
    
    //delete from db
    public void deleteFromDB(boolean delete_this, XincoDBManager DBM,int userID) throws XincoException {
        
        int i=0;
        
        try {
            
            Statement stmt;
            
            //fill nodes and data
            fillXincoCoreNodes(DBM);
            fillXincoCoreData(DBM);
            //start recursive deletion
            for (i=0;i<getXinco_core_nodes().size();i++) {
                ((XincoCoreNodeServer)getXinco_core_nodes().elementAt(i)).deleteFromDB(true, DBM,userID);
            }
            for (i=0;i<getXinco_core_data().size();i++) {
                XincoIndexer.removeXincoCoreData((XincoCoreDataServer)getXinco_core_data().elementAt(i), DBM);
                XincoCoreDataServer.removeFromDB(DBM,userID,
                        ((XincoCoreDataServer)getXinco_core_data().elementAt(i)).getId());
                ((XincoCoreDataServer)getXinco_core_data().elementAt(i)).setChangerID(userID);
                ((XincoCoreDataServer)getXinco_core_data().elementAt(i)).deleteFromDB(DBM);
            }
            if (delete_this) {
                XincoCoreAuditServer audit= new XincoCoreAuditServer();
                /*
                 * Aduit Trail Table (*_t) cannot handle multiple row changes!!!
                audit.updateAuditTrail("xinco_core_ace",new String [] {"id ="+getId()},
                        DBM,"audit.general.delete",userID);
                */
                stmt = DBM.con.createStatement();
                stmt.executeUpdate("DELETE FROM xinco_core_ace WHERE xinco_core_node_id=" + getId());
                stmt.close();
                audit.updateAuditTrail("xinco_core_node",new String [] {"id ="+getId()},
                        DBM,"audit.general.delete",userID);
                stmt = DBM.con.createStatement();
                stmt.executeUpdate("DELETE FROM xinco_core_node WHERE id=" + getId());
                stmt.close();
            }
            DBM.con.commit();
        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        
    }
    
    public void fillXincoCoreNodes(XincoDBManager DBM) {
        
        try {
            
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_node WHERE xinco_core_node_id = " + getId() + " ORDER BY designation");
            
            while (rs.next()) {
                getXinco_core_nodes().addElement(new XincoCoreNodeServer(rs.getInt("id"), rs.getInt("xinco_core_node_id"), rs.getInt("xinco_core_language_id"), rs.getString("designation"), rs.getInt("status_number"), DBM));
            }
            
            stmt.close();
            
        } catch (Exception e) {
            getXinco_core_nodes().removeAllElements();
        }
        
    }
    
    public void fillXincoCoreData(XincoDBManager DBM) {
        
        try {
            
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_data WHERE xinco_core_node_id = " + getId() + " ORDER BY designation");
            
            while (rs.next()) {
                getXinco_core_data().addElement(new XincoCoreDataServer(rs.getInt("id"), rs.getInt("xinco_core_node_id"), rs.getInt("xinco_core_language_id"), rs.getInt("xinco_core_data_type_id"), rs.getString("designation"), rs.getInt("status_number"), DBM));
            }
            
            stmt.close();
            
        } catch (Exception e) {
            getXinco_core_data().removeAllElements();
        }
        
    }
    
    public static Vector findXincoCoreNodes(String attrS, int attrLID, XincoDBManager DBM) {
        
        Vector nodes = null;
        
        try {
            
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_node WHERE xinco_core_language_id = " + attrLID + " AND designation LIKE '" + attrS + "%' ORDER BY designation, xinco_core_language_id");
            
            int i = 0;
            while (rs.next()) {
                nodes.addElement(new XincoCoreNodeServer(rs.getInt("id"), rs.getInt("xinco_core_node_id"), rs.getInt("xinco_core_language_id"), rs.getString("designation"), rs.getInt("status_number"), DBM));
                i++;
                if (i >= DBM.config.MaxSearchResult) {
                    break;
                }
            }
            
            stmt.close();
            
        } catch (Exception e) {
            nodes.removeAllElements();
        }
        
        return nodes;
        
    }
    
    public static Vector getXincoCoreNodeParents(int attrID, XincoDBManager DBM) {
        
        Vector nodes = new Vector();
        int id;
        
        try {
            
            id = attrID;
            
            Statement stmt;
            ResultSet rs;
            
            while (id > 0) {
                stmt = DBM.con.createStatement();
                rs = stmt.executeQuery("SELECT * FROM xinco_core_node WHERE id = " + id);
                while (rs.next()) {
                    nodes.addElement(new XincoCoreNodeServer(rs.getInt("id"), DBM));
                    if (id > 1) {
                        id = rs.getInt("xinco_core_node_id");
                    } else {
                        id = 0;
                    }
                }
                stmt.close();
            }
            
        } catch (Exception e) {
            nodes.removeAllElements();
        }
        
        return nodes;
        
    }
    
}
