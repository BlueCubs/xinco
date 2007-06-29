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
 * Name:            WorkflowInstanceManager.java
 *
 * Description:     WorkflowInstanceManager
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 13, 2007, 2:26 PM
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
import com.bluecubs.xinco.workflow.WorkflowInstance;
import java.sql.ResultSet;

public class WorkflowInstanceManager {
    private WorkflowInstanceServer currentInstance=null;
    SimpleWorkflow sw=null;
    /**
     * Creates a new instance of WorkflowInstanceManager
     */
    public WorkflowInstanceManager() {
    }
    
    public WorkflowInstanceManager(WorkflowInstanceServer instance) {
        setCurrentInstance(instance);
    }
    
    public WorkflowInstance getCurrentInstance() {
        return currentInstance;
    }
    
    public void setCurrentInstance(WorkflowInstanceServer currentInstance) {
        this.currentInstance = currentInstance;
    }
    
    public void manage(){
        buildWorkflow();
    }
    
    private void buildWorkflow(){
        WorkflowDBManager DBM=null;
        try {
            DBM = new WorkflowDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Find the root node
        sw=new SimpleWorkflow();
        boolean isRoot=false;
        for(int i=0;i<this.currentInstance.getNodes().size();i++){
            for(int j=0;j<this.currentInstance.getTransactions().size();j++){
                if(((Transaction)this.currentInstance.getTransactions().get(j)).getTo().getId()!=
                        ((Node)this.currentInstance.getNodes().get(i)).getId())
                    isRoot=true;
                else
                    isRoot=false;
            }
            if(isRoot){
                //We found the root node!
                SimpleNode temp= new SimpleNode(((Node)this.currentInstance.getNodes().get(i)).getId());
                sw.addNode(temp);
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Root id= "+sw.nodes[0].id);
//                ResultSet rs=DBM.getStatement().executeQuery("select * from workflow_instance_has_node " +
//                        "where node_id="+rs.getInt("node_id")+" and workflow_instance_id="+getId());
//                rs.next();
//                tempNode.setStartNode(rs.getBoolean("isStartNode"));
//                tempNode.setEndNode(rs.getBoolean("isEndNode"));
                break;
            }
        }
        int actual_id=0;
        //Now build the rest of the workflow
        for(int j=0;j<this.currentInstance.getTransactions().size();j++){
            actual_id=sw.nodes[j].id;
            if(((Transaction)this.currentInstance.getTransactions().get(j)).getFrom().getId()==actual_id){
                sw.nodes[j].addConnection(new Connection(((Transaction)this.currentInstance.getTransactions().get(j)).getId()));
                sw.nodes[j].connections[sw.nodes[j].connections.length-1].previous=sw.nodes[j];
                SimpleNode temp= new SimpleNode(((Transaction)this.currentInstance.getTransactions().get(j)).getTo().getId());
                sw.addNode(temp);
                sw.nodes[j].connections[sw.nodes[j].connections.length-1].next=sw.nodes[sw.nodes.length-1];
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                    String s="("+sw.nodes[j].id+")--->("+sw.nodes[j].connections[sw.nodes[j].connections.length-1].next.id+")";
                    
                    System.out.println(s);
                }
            }
        }
    }
    
    private class SimpleWorkflow{
        SimpleNode [] nodes;
        int i=0;
        public int next(){
            
            return 0;
        }
        public int addNode(SimpleNode node){
            if(nodes==null)
                nodes= new SimpleNode[1];
            else {
                SimpleNode[] temp= new SimpleNode[nodes.length+1];
                for(i=0;i<nodes.length;i++){
                    temp[i]=nodes[i];
                    if(node.id==temp[i].id)
                        return node.id;
                }
                nodes=temp;
            }
            nodes[i]=node;
            return i;
        }
    }
    private class SimpleNode{
        int id,i;
        Connection[] connections=null;
        private boolean completed=false;
        public SimpleNode(int id){
            this.id=id;
        }
        public void addConnection(Connection con){
            if(connections==null)
                connections= new Connection[1];
            else {
                Connection[] temp= new Connection[connections.length+1];
                for(i=0;i<connections.length;i++)
                    temp[i]=connections[i];
                connections=temp;
            }
            connections[i]=con;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
    
    private class Connection{
        int id;
        SimpleNode next=null;
        SimpleNode previous=null;
        public Connection(int id){
            this.id=id;
        }
    }
}
