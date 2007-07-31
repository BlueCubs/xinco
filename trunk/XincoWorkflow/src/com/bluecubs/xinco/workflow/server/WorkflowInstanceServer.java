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

import com.bluecubs.xinco.core.server.WorkflowAuditTrail;
import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.workflow.WorkflowException;
import com.bluecubs.xinco.workflow.WorkflowInstance;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Vector;

public class WorkflowInstanceServer extends WorkflowInstance{
    private ResultSet rs;
    private WorkflowTemplateServer template;
    private Vector properties;
    private int changerId=-1;
    private WorkflowDBManager DBM;
    private String sql="";
    /**
     * Creates a new instance of WorkflowInstanceServer
     */
    public WorkflowInstanceServer(int id,int template_id, WorkflowDBManager DBM) throws WorkflowException {
        if(id>0){
            try {
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Loading instance...");
                String sql="select * from workflow_instance where id ="+id;
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                rs=DBM.getStatement().executeQuery(sql);
                rs.next();
                setId(rs.getInt("id"));
                setTemplateId(rs.getInt("workflow_template_id"));
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTimeInMillis(rs.getTimestamp("creationtime").getTime());
                setCreationTime(cal);
                template =new WorkflowTemplateServer(getTemplateId(),DBM);
                setNodes(new NodeServer().loadNodes(getId(),getTemplateId(),DBM));
                initializeNodes(DBM);
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("# of nodes: "+getNodes().size());
                setTransactions(template.getTransactions());
                initializeTransactions(DBM);
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("# of transactions: "+getTransactions().size());
                setCurrentNode(rs.getInt("node_id"));
                loadProperties();
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Loading instance...Done!");
            } catch (SQLException ex) {
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
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
                initializeTransactions(DBM);
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("# of transactions: "+getTransactions().size());
                setCurrentNode(rs.getInt("node_id"));
                loadProperties();
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Creating instance...Done!");
                write2DB(DBM);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void write2DB(WorkflowDBManager DBM){
        WorkflowAuditTrail wat= new WorkflowAuditTrail();
        try {
            if(getId()>0){
                //Update Instance
                sql="update workflow_instance set id="+
                        getId()+", workflow_template_id="+getTemplateId()+", creationTime="+getCreationTime();
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                wat.updateAuditTrail("workflow_instance",new String [] {"id="+getId()},DBM,"audit.general.modified",getChangerID());
                //Update Node status
                for(int i=0;i<getNodes().size();i++){
                    ((NodeServer)getNodes().get(i)).write2DB(DBM);
                    sql="update workflow_instance_has_node " +
                            "set workflow_instance_workflow_template="+getTemplateId()+", id="+getId()+
                            ", node_id="+((NodeServer)getNodes().get(i)).getId()+", completed="+
                            (((NodeServer)getNodes().get(i)).isCompleted() ? 1 : 0)+", isstartnode="+
                            (((NodeServer)getNodes().get(i)).isStartNode() ? 1 : 0)+", isendnode="+
                            (((NodeServer)getNodes().get(i)).isEndNode() ? 1 : 0)+")";
                    if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                        System.out.println(sql);
                    DBM.getConnection().createStatement().executeUpdate(sql);
                }
                //Update Transaction status
                for(int i=0;i<getTransactions().size();i++){
                    ((TransactionServer)getTransactions().get(i)).write2DB(DBM);
                    sql="update workflow_instance_has_transaction " +
                            "set workflow_instance_workflow_template="+getTemplateId()+", id="+getId()+
                            ", transaction_id="+((TransactionServer)getTransactions().get(i)).getId()+", completed="+
                            (((TransactionServer)getTransactions().get(i)).isCompleted() ? 1 : 0)+")";
                    if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                        System.out.println(sql);
                    DBM.getConnection().createStatement().executeUpdate(sql);
                }
                DBM.getConnection().commit();
            }else{
                //Create Instance
                Timestamp ts = new Timestamp(getCreationTime().getTimeInMillis());
                sql="INSERT INTO Workflow_Instance " +
                        "(id, Workflow_Template_id, Node_id, creationTime) VALUES("+
                        getId()+", "+getTemplateId()+", "+ts;
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                wat.updateAuditTrail("workflow_instance",new String [] {"id="+getId()},DBM,"audit.general.modified",getChangerID());
                DBM.getConnection().commit();
                //Update Node status
                for(int i=0;i<getNodes().size();i++){
                    ((NodeServer)getNodes().get(i)).write2DB(DBM);
                }
                //Update Transaction status
                for(int i=0;i<getTransactions().size();i++){
                    ((NodeServer)getTransactions().get(i)).write2DB(DBM);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                DBM.getConnection().rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void loadProperties() throws WorkflowException{
        try {
            setProperties(new InstancePropertyServer().getProperties(getId(),getTemplateId(),new WorkflowDBManager()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WorkflowException();
        }
    }
    
    public void initializeNodes(WorkflowDBManager DBM){
        for(int i=0;i<getNodes().size();i++){
            ((NodeServer)getNodes().get(i)).instanceSetup(getId(),getTemplateId(),DBM);
            ((NodeServer)getNodes().get(i)).setChangerID(getChangerId());
        }
    }
    
    public void initializeTransactions(WorkflowDBManager DBM){
        for(int i=0;i<getTransactions().size();i++){
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Initializing transaction "+((TransactionServer)getTransactions().get(i)).getId());
            ((TransactionServer)getTransactions().get(i)).instanceSetup(getId(),DBM);
            ((TransactionServer)getTransactions().get(i)).setChangerID(getChangerId());
        }
    }
    
    public int getChangerId() {
        return changerId;
    }
    
    public void setChangerId(int changerId) {
        this.changerId = changerId;
    }
    
    private void loadProperties(WorkflowDBManager DBM){
        Vector values = new Vector();
        try {
            rs=DBM.getStatement().executeQuery("select id from instance_property where id="+getId());
            values.removeAllElements();
            while(rs.next()){
                values.addElement(new PropertyServer(rs.getInt("id"),DBM));
            }
            setProperties(values);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public Vector getProperties() {
        try {
            loadProperties();
        } catch (WorkflowException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
    
    public void setProperties(Vector properties) {
        this.properties = properties;
    }
    
    public Vector getTransactionsForNode(NodeServer node){
        try {
            DBM=new WorkflowDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Vector t= new Vector();
        for(int j=0;j<getTransactions().size();j++){
            if(((TransactionServer)getTransactions().get(j)).getFrom().getId()==node.getId()){
                TransactionServer temp=null;
                try {
                    temp = new TransactionServer(((TransactionServer) getTransactions().get(j)).getId(), new WorkflowDBManager());
                    temp.instanceSetup(getId(),DBM);
                    t.add(temp);
                } catch (Exception ex) {
                    if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                        ex.printStackTrace();
                }
            }
        }
        return t;
    }
}
