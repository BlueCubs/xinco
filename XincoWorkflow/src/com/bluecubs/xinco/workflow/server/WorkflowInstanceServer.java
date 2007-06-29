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
 * Name:            WorkflowServer.java
 *
 * Description:     WorkflowServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 8, 2007, 10:23 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.workflow.WorkflowException;
import com.bluecubs.xinco.workflow.WorkflowInstance;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Vector;

public class WorkflowInstanceServer extends WorkflowInstance{
    private ResultSet rs;
    private WorkflowTemplateServer template;
    private Vector properties;
    /**
     * Creates a new instance of WorkflowInstanceServer
     */
    public WorkflowInstanceServer(int id,int template_id, WorkflowDBManager DBM) throws WorkflowException {
        if(id>0){
            try {
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Loading instance...");
                rs=DBM.getStatement().executeQuery("select * from " +
                        "workflow_instance where id ="+id);
                rs.next();
                setId(rs.getInt("id"));
                setTemplateId(rs.getInt("workflow_template_id"));
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTimeInMillis(rs.getTimestamp("creationtime").getTime());
                setCreationTime(cal);
                template =new WorkflowTemplateServer(getTemplateId(),DBM);
                setNodes(template.getNodes());
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("# of nodes: "+getNodes().size());
                setTransactions(template.getTransactions());
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("# of transactions: "+getTransactions().size());
                setCurrentNode(rs.getInt("node_id"));
                loadProperties();
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Loading instance...Done!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new WorkflowException();
            }
        }else{
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Creating instance...");
            try {
                DBM.getNewID("workflow_instance");
                setId(id);
                setTemplateId(template_id);
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTimeInMillis(System.currentTimeMillis());
                setCreationTime(cal);
                template =new WorkflowTemplateServer(getTemplateId(),DBM);
                setNodes(template.getNodes());
                initializeNodes(DBM);
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("# of nodes: "+getNodes().size());
                setTransactions(template.getTransactions());
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("# of transactions: "+getTransactions().size());
                setCurrentNode(rs.getInt("node_id"));
                loadProperties();
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Creating instance...Done!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void write2DB(){
        
    }
    
    private void loadProperties() throws WorkflowException{
        try {
            properties=new InstancePropertyServer().getProperties(getId(),getTemplateId(),new WorkflowDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WorkflowException();
        }
    }
    
    public void initializeNodes(WorkflowDBManager DBM){
            for(int i=0;i<getNodes().size();i++){
            try {
                rs=DBM.getStatement().executeQuery("select * from workflow_instance_has_node " +
                        "where node_id="+((NodeServer)getNodes().get(i)).getId()+" and workflow_instance_id="+getId());
                rs.next();
                ((NodeServer)getNodes().get(i)).setStartNode(rs.getBoolean("isStartNode"));
                ((NodeServer)getNodes().get(i)).setEndNode(rs.getBoolean("isEndNode"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            }
    }
}
