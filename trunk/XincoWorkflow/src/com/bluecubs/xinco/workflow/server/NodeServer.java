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
import com.bluecubs.xinco.workflow.Transaction;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class NodeServer extends Node{
    private ResultSet rs=null;
    private boolean completed=false;
    private boolean instanceReady=false;
    private int instance_template_id,instance_id;
    private WorkflowAuditTrail wat;
    private TransactionServer [] myTransactions;
    
    /** Creates a new instance of NodeServer */
    public NodeServer() {
    }
    
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
                System.out.println("Done!\n");
        }
    }
    
    private void loadActivities(WorkflowDBManager DBM){
        Vector values = new Vector();
        try {
            rs=DBM.getStatement().executeQuery("select * from node_has_activity where node_id="+getId());
            values.removeAllElements();
            while(rs.next()){
                try {
                    values.addElement(new ActivityServer(rs.getInt("activity_id"),DBM));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            setActivities(values);
        } catch (SQLException ex) {
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
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
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                ex.printStackTrace();
        }
    }
    
    public NodeServer instanceSetup(int instance_id,int instance_template_id, WorkflowDBManager DBM){
        try {
            String sql="select * from workflow_instance_has_node " +
                    "where node_id="+getId()+" and id="+instance_id;
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println(sql);
            ResultSet rs2=DBM.getStatement().executeQuery(sql);
            rs2.next();
            setStartNode(rs2.getBoolean("isStartNode"));
            setEndNode(rs2.getBoolean("isEndNode"));
            setCompleted(rs2.getBoolean("completed"));
            setInstance_template_id(instance_template_id);
            setInstance_id(instance_id);
            loadTransactions(instance_id,instance_template_id,DBM);
            setInstanceReady(true);
        } catch (SQLException ex) {
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                ex.printStackTrace();
        }
        return this;
    }
    
    public NodeServer switchToNodeServer(Node n){
        NodeServer temp=null;
        try {
            temp = new NodeServer(n.getId(), new WorkflowDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }
    
    private void loadTransactions(int instance_id,int instance_template_id,WorkflowDBManager DBM){
        String sql ="select * from Workflow_Instance_has_Transaction where workflow_template_id = "+
                instance_template_id+" and node_id="+getId();
        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
            System.out.println(sql);
        ResultSet rs2=null;
        try {
            rs2 = DBM.getStatement().executeQuery(sql);
            while(rs2.next()){
                if(getMyTransactions()==null)
                    setMyTransactions(new TransactionServer[1]);
                else{
                    TransactionServer [] temp = new TransactionServer[getMyTransactions().length+1];
                    for(int i=0;i<getMyTransactions().length;i++)
                        temp[i]=getMyTransactions()[i];
                    setMyTransactions(temp);
                }
                getMyTransactions()[getMyTransactions().length-1]=new TransactionServer(rs2.getInt("transaction_id"),DBM);
                getMyTransactions()[getMyTransactions().length-1].instanceSetup(instance_id,DBM);
            }
        } catch (SQLException ex) {
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                ex.printStackTrace();
        }
    }
    
    public void resetTransactions(){
        if(getMyTransactions()!=null){
            for(int i=0;i<getMyTransactions().length;i++)
                getMyTransactions()[i].setCompleted(false);
        }
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public void write2DB(WorkflowDBManager DBM){
        wat= new WorkflowAuditTrail();
        String sql="";
        try {
            if(getId()>0){
                //Update Node
                sql="update node set id="+
                        getId()+", description='"+getDescription()+"' where id ="+getId();
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                wat.updateAuditTrail("node",new String [] {"id="+getId()},DBM,"audit.general.modified",getChangerID());
                instanceWrite2DB(DBM);
                DBM.getConnection().commit();
            } else{
                setId(DBM.getNewID("node"));
                sql="INSERT INTO Node (id, description) VALUES("+
                        getId()+", '"+getDescription()+"')";
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                instanceWrite2DB(DBM);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isInstanceReady() {
        return instanceReady;
    }
    
    public void setInstanceReady(boolean instanceReady) {
        this.instanceReady = instanceReady;
    }
    
    private void instanceWrite2DB(WorkflowDBManager DBM){
        if(isInstanceReady()){
            String sql="update Workflow_Instance_has_Node set completed="+(isCompleted() ? 1:0)+
                    ",  isStartNode="+(isStartNode() ? 1:0)+", isEndNode="+(isEndNode() ? 1:0)+" where id="+
                    getInstance_id()+" and Node_id="+getId()+" and workflow_template_id="+
                    getInstance_template_id();
            try {
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                DBM.getConnection().commit();
            } catch (SQLException ex) {
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    ex.printStackTrace();
            }
        }else{
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Not instance ready...");
        }
    }
    
    public Vector loadNodes(int instance_id,int template_id,WorkflowDBManager DBM){
        Vector nodes = new Vector();
        try {
            ResultSet rs= DBM.getConnection().createStatement().executeQuery("select node_id from " +
                    "workflow_instance_has_node where id="+instance_id);
            NodeServer temp=null;
            while(rs.next()){
                nodes.add(new NodeServer(rs.getInt("node_id"),DBM).instanceSetup(instance_id,template_id,DBM));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nodes;
    }
    
    public int getInstance_template_id() {
        return instance_template_id;
    }
    
    public void setInstance_template_id(int instance_template_id) {
        this.instance_template_id = instance_template_id;
    }
    
    public int getInstance_id() {
        return instance_id;
    }
    
    public void setInstance_id(int instance_id) {
        this.instance_id = instance_id;
    }
    
    public TransactionServer[] getMyTransactions() {
        return myTransactions;
    }
    
    public void setMyTransactions(TransactionServer[] myTransactions) {
        this.myTransactions = myTransactions;
    }
}