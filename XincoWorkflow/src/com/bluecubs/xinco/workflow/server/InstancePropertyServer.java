/**
 *Copyright June 13, 2007 blueCubs.com
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
 * Name:            InstancePropertyServer.java
 *
 * Description:     InstancePropertyServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 13, 2007, 2:47 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.workflow.InstanceProperty;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class InstancePropertyServer extends InstanceProperty{
    private ResultSet rs;
    private Vector properties;
    /** Creates a new instance of InstancePropertyServer */
    public InstancePropertyServer(int id, WorkflowDBManager DBM) {
        if(id>0){
            try {
                ResultSet rs= DBM.getConnection().createStatement().executeQuery("select * from instanceproperty where id="+getId());
                rs.next();
                setId(id);
                setDescription(rs.getString("description"));
                setIntProperty(rs.getInt("propertyint"));
                setStringProperty(rs.getString("propertystring"));
                setBoolProperty(rs.getBoolean("propertybool"));
                setLongProperty(rs.getInt("propertylong"));
                setWorkflow_instance_id(rs.getInt("workflow_instance_id"));
                setWorkflow_template_id(rs.getInt("workflow_instance_workflow_template_id"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public InstancePropertyServer(){
        
    }
    
    public Vector getProperties(int instance_id, int template_id,WorkflowDBManager DBM){
        properties=new Vector();
        try {
            rs=DBM.getStatement().executeQuery("select id from " +
                    "instance_property where Workflow_Instance_Workflow_Template_id ="+
                    getWorkflow_template_id()+" and Workflow_Instance_id="+getWorkflow_instance_id());
            while(rs.next())
                properties.addElement(new InstancePropertyServer(rs.getInt("id"),DBM));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
}
