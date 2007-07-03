/**
 *Copyright 2007 blueCubs.com
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
 * Name:            NodeServer.java
 *
 * Description:     NodeServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 8, 2007, 9:55 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.WorkflowAuditTrail;
import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.workflow.Node;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class NodeServer extends Node{
    private ResultSet rs=null;
    private boolean completed=false;
    /** Creates a new instance of NodeServer */
    public NodeServer(int id, WorkflowDBManager DBM) {
        if(id > 0){
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Creating node with id: "+id);
            try {
                //Node exists
                rs =DBM.getStatement().executeQuery("select * from node where id="+id);
                rs.next();
                setId(rs.getInt("id"));
                setDescription(rs.getString("description"));
                rs =DBM.getStatement().executeQuery("select * from node where id="+id);
                //Load adtivities related to this node
                loadActivities(DBM);
                //Load properties related to this node
                loadProperties(DBM);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Done!");
        }
    }
    
    private void loadActivities(WorkflowDBManager DBM){
        Vector values = new Vector();
        try {
            rs=DBM.getStatement().executeQuery("select activity_id from node_has_activity where node_id="+getId());
            values.removeAllElements();
            while(rs.next()){
                values.addElement(new ActivityServer(rs.getInt("activity_id"),DBM));
            }
            setActivities(values);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void loadProperties(WorkflowDBManager DBM){
        Vector values = new Vector();
        try {
            rs=DBM.getStatement().executeQuery("select property_id from node_has_property where node_id="+getId());
            values.removeAllElements();
            while(rs.next()){
                values.addElement(new PropertyServer(rs.getInt("property_id"),DBM));
            }
            setProperties(values);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public NodeServer instanceSetup(int instance_id, WorkflowDBManager DBM){
        try {
            ResultSet rs2=DBM.getStatement().executeQuery("select * from workflow_instance_has_node " +
                    "where node_id="+getId()+" and workflow_instance_id="+instance_id);
            rs2.next();
            setStartNode(rs2.getBoolean("isStartNode"));
            setEndNode(rs2.getBoolean("isEndNode"));
            setCompleted(rs2.getBoolean("completed"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public void write2DB(WorkflowDBManager DBM){
        WorkflowAuditTrail wat= new WorkflowAuditTrail();
        try {
            if(getId()>0){
                //Update Node
                DBM.getConnection().createStatement().executeUpdate("update node set id="+
                        getId()+", description='"+getDescription()+"' where id ="+getId());
                wat.updateAuditTrail("node",new String [] {"id="+getId()},DBM,"audit.general.modified",getChangerID());
                DBM.getConnection().commit();
            } else{
                setId(DBM.getNewID("node"));
                DBM.getConnection().createStatement().executeUpdate("INSERT INTO Node (id, description) VALUES("+
                        getId()+", '"+getDescription()+"')");
                wat.updateAuditTrail("node",new String [] {"id="+getId()},DBM,"audit.general.create",getChangerID());
                DBM.getConnection().commit();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                DBM.getConnection().rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
