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
 * Name:            WorkflowTemplateServer.java
 *
 * Description:     WorkflowTemplateServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 8, 2007, 10:38 AM
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
import com.bluecubs.xinco.workflow.Transaction;
import com.bluecubs.xinco.workflow.WorkflowTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class WorkflowTemplateServer extends WorkflowTemplate{
    private ResultSet rs=null,rs2=null;
    private Vector tNodes=null,tTransactions=null;
    /** Creates a new instance of WorkflowTemplateServer */
    public WorkflowTemplateServer(int id, WorkflowDBManager DBM){
        if(id>0){
            try {
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Loading template..."+id);
                rs=DBM.getStatement().executeQuery("select * from " +
                        "workflow_template where id ="+id);
                rs.next();
                setId(rs.getInt("id"));
                setDescription(rs.getString("description"));
                loadNodes(DBM);
                loadTransactions(DBM);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private Vector loadNodes(WorkflowDBManager DBM){
        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
            System.out.println("Loading Nodes...");
        Vector temp = new Vector();
        try {
            String sql="select node_id from " +
                    "Workflow_Template_has_Node where id="+getId();
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println(sql);
            rs=DBM.getStatement().executeQuery(sql);
            temp.removeAllElements();
            while(rs.next()){
                NodeServer tempNode=new NodeServer(rs.getInt("node_id"),DBM);
                rs2=DBM.getStatement().executeQuery("select * from workflow_template_has_node " +
                        "where node_id="+rs.getInt("node_id")+" and id="+getId());
                rs2.next();
                tempNode.setStartNode(rs2.getBoolean("isStartNode"));
                tempNode.setEndNode(rs2.getBoolean("isEndNode"));
                temp.addElement(tempNode);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            temp.removeAllElements();
        }
        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
            System.out.println("Vector size: "+temp.size());
        setNodes(temp);
        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
            System.out.println("Done!\n");
        return getNodes();
    }
    
    private Vector loadTransactions(WorkflowDBManager DBM){
        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
            System.out.println("Loading Transactions...");
        Vector values = new Vector();
        try{
            String sql="select transaction_id from " +
                    "Workflow_Template_has_Transaction where id="+getId();
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println(sql);
            rs=DBM.getStatement().executeQuery(sql);
            values.removeAllElements();
            while(rs.next()){
                values.addElement(new TransactionServer(rs.getInt("transaction_id"),DBM));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            values.removeAllElements();
        }
        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
            System.out.println("Vector size: "+values.size());
        setTransactions(values);
        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
            System.out.println("Done!\n");
        return getTransactions();
    }
    
    public Vector getTNodes() {
        return tNodes;
    }
    
    public void setTNodes(Vector tNodes) {
        this.tNodes = tNodes;
    }
    
    public Vector getTTransactions() {
        return tTransactions;
    }
    
    public void setTTransactions(Vector tTransactions) {
        this.tTransactions = tTransactions;
    }
    
    public NodeServer getRoot(){
        if(getTNodes()!= null) {
            for(int i=0;i<getTNodes().size();i++) {
                if(((NodeServer)getTNodes().get(i)).isStartNode()){
                    return ((NodeServer)getTNodes().get(i));
                }
            }
        }
        return null;
    }
}