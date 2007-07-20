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
 * Name:            ActivityServer
 *
 * Description:     ActivityServer
 *
 * Original Author: Javier Ortiz
 * Date:            June 8, 2007, 9:09 AM
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
import com.bluecubs.xinco.workflow.Activity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author javydreamercsw
 */
public class ActivityServer extends Activity{
    private int nodeID;
    private ResultSet rs;
    private WorkflowAuditTrail wat;
    /** Creates a new instance of ActivityServer */
    public ActivityServer(int id,WorkflowDBManager DBM) {
        if(id>0){
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Retrieving activity with id: "+id);
            try {
                rs=DBM.getStatement().executeQuery("select * from activity where id="+id);
                rs.next();
                setId(rs.getInt("id"));
                setDescription(rs.getString("description"));
                setClassname(rs.getString("className"));
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Retrieving activity done!");
        }
    }
    
    public ActivityServer(){
        
    }
    
    public int getNodeID() {
        return nodeID;
    }
    
    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public boolean run() {
        return false;
    }
    
    public boolean write2DB(WorkflowDBManager DBM){
        boolean completed=false;
        wat= new WorkflowAuditTrail();
        if(getId()>0){
            // Existant activity
            if(((WorkflowDBManager)DBM).getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Updating activity with id: "+getId());
            try {
                DBM.getStatement().executeUpdate("update Activity set id="+
                        getId()+", description='"+getDescription()+"', className='"+getClassname()+"' where id="+
                        getId());
                DBM.getConnection().commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if(((WorkflowDBManager)DBM).getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Updating activity done!");
        }else{
            // New activity
            try {
                setId(DBM.getNewID("activity"));
                if(((WorkflowDBManager)DBM).getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Updating activity with id: "+getId());
            try {
                DBM.getStatement().executeUpdate("INSERT INTO Activity (id, description, className) VALUES("+
                        getId()+",'"+getDescription()+"', '"+getClassname()+"')");
                wat.updateAuditTrail("node",new String [] {"id="+getId()},DBM,"audit.general.create",getChangerID());
                DBM.getConnection().commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if(((WorkflowDBManager)DBM).getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Updating activity done!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return completed;
    }
    
    public Vector getDefaultPropertiesForType(WorkflowDBManager DBM){
        Vector set = new Vector();
        try {
            ResultSet rs = DBM.getConnection().createStatement().executeQuery("select activity_type_id from activity_type where id ="+getId());
            rs.next();
            int atid=rs.getInt("activity_type_id");
            rs = DBM.getConnection().createStatement().executeQuery("select activity_type_id from activity_type where id ="+getId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return set;
    }
}
