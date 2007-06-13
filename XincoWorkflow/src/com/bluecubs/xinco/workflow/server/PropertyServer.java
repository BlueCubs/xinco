/**
 *Copyright June 8, 2007 blueCubs.com
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
 * Name:            PropertyServer.java
 *
 * Description:     PropertyServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 8, 2007, 2:17 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.workflow.Property;
import com.bluecubs.xinco.workflow.server.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PropertyServer extends Property{
    private ResultSet rs;
    private final int NODE=1,TRANSACTION=2,ACTIVITY=3;
    /** Creates a new instance of PropertyServer */
    public PropertyServer(int id, WorkflowDBManager DBM) {
        if(id>0){
            try {
                rs=DBM.getStatement().executeQuery("select * from " +
                        "property where id="+id);
                rs.next();
                setId(rs.getInt("id"));
                setActivityId(rs.getInt("activity_id"));
                setNodeId(rs.getInt("node_id"));
                setTransactionId(rs.getInt("transaction_id"));
                setDescription(rs.getString("description"));
                setStringProperty(rs.getString("propertyString"));
                setBoolProperty(rs.getBoolean("propertyBool"));
                setIntProperty(rs.getInt("propertyInt"));
                setLongProperty(rs.getLong("'propertyLong"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public PropertyServer(){
        
    }
    
    public Vector getPropertiesForNode(int id, WorkflowDBManager DBM){
        return getProperties(id,this.NODE,DBM);
    }
    
    public Vector getPropertiesForTransaction(int id, WorkflowDBManager DBM){
        return getProperties(id,this.TRANSACTION,DBM);
    }
    
    public Vector getPropertiesForActivity(int id, WorkflowDBManager DBM){
        return getProperties(id,this.ACTIVITY,DBM);
    }
    
    private Vector getProperties(int id,int type, WorkflowDBManager DBM){
        Vector properties = new Vector();
        String sql = "select property_id from ";
        switch(type){
            case NODE:sql+="node_has_property where node_id="+id;
            break;
            case TRANSACTION:sql+="transaction_has_property where transaction_id="+id;
            break;
            case ACTIVITY:sql+="activity_has_property where activity_id="+id;
            break;
        }
        try {
            rs=DBM.getStatement().executeQuery(sql);
            while(rs.next()){
                properties.add(new PropertyServer(rs.getInt("id"),DBM));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
}
