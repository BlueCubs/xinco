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

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.Node;
import com.bluecubs.xinco.workflow.server.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NodeServer extends Node{
    private ResultSet rs=null;
    /** Creates a new instance of NodeServer */
    public NodeServer(int id, XincoDBManager DBM) {
        if(id > 0){
            try {
                //Node exists
                rs =DBM.getConnection().createStatement().executeQuery("select * from node where id="+id);
                rs.next();
                setId(rs.getInt("id"));
                setDescription(rs.getString("description"));
                //Load adtivities related to this node
                loadActivities(DBM);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void loadActivities(XincoDBManager DBM){
        setActivities(null);
        try {
            rs=DBM.getConnection().createStatement().executeQuery("select activity_id from node_has_activity where node_id="+getId());
            int counter=0;
            while(rs.next()){
                setActivities(counter,new ActivityServer(rs.getInt("activity_id"),DBM));
                counter++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
}
