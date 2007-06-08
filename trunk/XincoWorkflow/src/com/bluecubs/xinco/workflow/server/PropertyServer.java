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

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.Property;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PropertyServer extends Property{
    private ResultSet rs;
    private final int NODE=1,TRANSACTION=2,ACTIVITY=3;
    /** Creates a new instance of PropertyServer */
    public PropertyServer(int id, XincoDBManager DBM) {
        if(id>0){
            try {
                rs=DBM.getConnection().createStatement().executeQuery("select * from " +
                        "property where id="+id);
                rs.next();
                setId(rs.getInt("id"));
                setActivityId(rs.getInt("activity_id"));
                setNodeId(rs.getInt("node_id"));
                setTransactionId(rs.getInt("transaction_id"));
                setDescription(rs.getString("description"));
                setPropertyString(rs.getString("propertyString"));
                setPropertyBool(rs.getBoolean("propertyBool"));
                setPropertyInt(rs.getInt("propertyInt"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public PropertyServer(){
        
    }
    
    public Vector getPropertiesForNode(int id, XincoDBManager DBM){
        return getProperties(id,this.NODE,DBM);
    }
    
    public Vector getPropertiesForTransaction(int id, XincoDBManager DBM){
        return getProperties(id,this.TRANSACTION,DBM);
    }
    
    public Vector getPropertiesForActivity(int id, XincoDBManager DBM){
        return getProperties(id,this.ACTIVITY,DBM);
    }
    
    private Vector getProperties(int id,int type, XincoDBManager DBM){
        Vector properties = new Vector();
        String sql = "select id from property where ";
        switch(type){
            case NODE:sql+="node_id=";
            break;
            case TRANSACTION:sql+="transaction_id=";
            break;
            case ACTIVITY:sql+="activity_id=";
            break;
        }
        sql+=id;
        try {
            rs=DBM.getConnection().createStatement().executeQuery(sql);
            while(rs.next()){
                properties.add(new PropertyServer(rs.getInt("id"),DBM));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
}
