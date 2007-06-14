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

import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.workflow.Node;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class NodeServer extends Node{
    private ResultSet rs=null;
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
                setEndNode(rs.getBoolean("isEndNode"));
                setStartNode(rs.getBoolean("isStartNode"));
                //Load adtivities related to this node
                loadActivities(DBM);
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
    
    
}
