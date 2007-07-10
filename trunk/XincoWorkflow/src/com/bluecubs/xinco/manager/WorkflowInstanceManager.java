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

package com.bluecubs.xinco.manager;

import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.workflow.Node;
import com.bluecubs.xinco.workflow.Transaction;
import com.bluecubs.xinco.workflow.server.NodeServer;
import com.bluecubs.xinco.workflow.server.PropertyServer;
import com.bluecubs.xinco.workflow.server.TransactionServer;
import com.bluecubs.xinco.workflow.server.WorkflowInstanceServer;
import java.util.Vector;

public class WorkflowInstanceManager {
    private WorkflowInstanceServer currentInstance=null;
    private SimpleWorkflow sw=null;
    private int changerId=3; //By default the changes are made by system.
    /**
     * Creates a new instance of WorkflowInstanceManager
     */
    
    public WorkflowInstanceManager(WorkflowInstanceServer instance) {
        setCurrentInstance(instance);
    }
    
    public WorkflowInstanceServer getCurrentInstance() {
        return currentInstance;
    }
    
    public void setCurrentInstance(WorkflowInstanceServer currentInstance) {
        this.currentInstance = currentInstance;
    }
    
    public void manage(){
        buildWorkflow();
        evaluateCurrentState();
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
                SimpleNode temp= new SimpleNode(((NodeServer)this.currentInstance.getNodes().get(i)).getId());
                temp.setCompleted(((NodeServer)this.currentInstance.getNodes().get(i)).instanceSetup(getCurrentInstance().getId(),DBM).isCompleted());
                sw.addNode(temp);
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println("Root id= "+sw.nodes[0].id);
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
                    String s="("+sw.nodes[j].id+(sw.nodes[j].isCompleted() ? "*":"")+
                            ")--->("+sw.nodes[j].connections[sw.nodes[j].connections.length-1].next.id+(sw.nodes[j].connections[sw.nodes[j].connections.length-1].next.isCompleted() ? "*":"")+")";
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
            setCompleted(false);
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
    
    private boolean evaluateCurrentState(){
        boolean ready=true;
        WorkflowDBManager DBM=null;
        try {
            DBM = new WorkflowDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        NodeServer current=null;
        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
            System.out.println("Starting current state evaluation...");
        for(int i=0;i<sw.nodes.length;i++){
            Vector transactions=new Vector(),
                    transactionProperties=new Vector();
            current=((NodeServer)getCurrentInstance().getNodes().get(i));
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Evaluating node: "+current.getId());
            // This node needs to be evaluated to see if we can get to the next step
            if(!current.isCompleted()){
                if(evaluateNode(current)) {
                    // All properties are fulfilled
                    // this might happen once during the workflow since instance
                    // properties can change later in the workflow.
                    current.setCompleted(true);
                    current.setChangerID(getChangerID());
                    if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                        System.out.println("Changing node status to complete...");
                    try {
                        current.write2DB(new WorkflowDBManager());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            // If the node is completed after evaluation...
            if(current.isCompleted()){
                // Verify the transactions starting after this node's completion
                // to see if we can move fordward.
                transactions= getCurrentInstance().getTransactionsForNode(current);
                for(int j=0;j<transactions.size();j++) {
                    if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                        System.out.println("Evaluating transaction: "+((TransactionServer)transactions.get(j)).getId());
                    transactionProperties=((TransactionServer)transactions.get(j)).getProperties();
                    if(new PropertyServer().compare(transactionProperties,getCurrentInstance().getProperties())){
                        ((TransactionServer)transactions.get(j)).setCompleted(true);
                        ((TransactionServer)transactions.get(j)).setChangerID(getChangerID());
                        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                            System.out.println("Changing transaction status to complete...");
                        try {
                            ((TransactionServer)transactions.get(j)).write2DB(new WorkflowDBManager());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return ready;
    }
    
    private boolean evaluateNode(NodeServer node){
        boolean ready=true,found = false;
        Vector properties=node.getProperties(),
                instanceProperties=getCurrentInstance().getProperties();
        for(int j=0;j<properties.size();j++){
            for(int k=0;k<instanceProperties.size();k++){
                if(((PropertyServer)properties.get(j)).getDescription().equals(((PropertyServer)instanceProperties.get(j)).getDescription())){
                    //Found a matching property.
                    found=true;
                    if(!((PropertyServer)properties.get(j)).compare(((PropertyServer)instanceProperties.get(j)))){
                        ready=false;
                        try {
                            if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                                System.out.println("Properties are diffrent:\n1. \n"+((PropertyServer)properties.get(j)).toString()+"\n2. \n"+
                                        ((PropertyServer)instanceProperties.get(j)).toString());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            }
            if(!found){
                try {
                    if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                        System.out.println("Matching property not found");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
            found=false;
        }
        return ready;
    }
    
    public int getChangerID() {
        return changerId;
    }
    
    public void setChangerID(int changerId) {
        this.changerId = changerId;
    }
}
