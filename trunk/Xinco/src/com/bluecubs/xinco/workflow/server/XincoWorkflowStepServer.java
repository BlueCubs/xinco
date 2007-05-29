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
 * Name:            XincoWorkflowStepServer
 *
 * Description:     XincoWorkflowStepServer
 *
 * Original Author: Javier Ortiz
 * Date:            May 2, 2007, 4:28 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreAuditTrail;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.XincoWorkflowStep;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author ortizbj
 */
public class XincoWorkflowStepServer extends XincoWorkflowStep{
    private boolean change=false;
    private int changerID;
    private int workflow_id;
    private XincoWorkflowStepForkServer forks;
    /**
     * Creates a new instance of XincoWorkflowStepServer
     * @param step_id
     * @param workflow_id
     * @param designation
     */
    public XincoWorkflowStepServer(int step_id, String designation,int workflow_id) {
        setId(step_id);
        setWorkflow_id(workflow_id);
        setDesignation(designation);
        try {
            write2DB(new XincoDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public XincoWorkflowStepServer(){
    }
    
    public Vector getSteps(int id, XincoDBManager dbm){
        ResultSet rs =null,rs2=null;
        Vector steps=null,substeps=null;
        XincoWorkflowServer subWorkflow=null;
        Statement stmt2=null;
        if(id>0){
            //Workflow exists
            steps = new Vector();
            try {
                rs = dbm.getCon().createStatement().executeQuery("select id from xinco_workflow_has_xinco_workflow_step where xinco_workflow_id ="+id);
                while(rs.next()){
                    try {
                        //Check if step has fork options
                        setFork(new XincoWorkflowStepForkServer(rs.getInt("id"),getWorkflow_id(),new XincoDBManager()));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    //A step has a workflow as sub steps
                    if(getWorkflow_id()>0){
                        try {
                            //Get new sub workflow
                            subWorkflow = new XincoWorkflowServer(getWorkflow_id(),new XincoDBManager());
                            //Extract workflow steps
                            substeps = subWorkflow.getXinco_workflow_steps();
                            //Add steps to main workflow
                            //This also will get any changes in the sub workflow
                            for(int i=0;i<substeps.size();i++)
                                steps.add(substeps.get(i));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return steps;
    }
    
    public void write2DB(XincoDBManager dbm) throws XincoException{
        ResultSet rs =null,rs2=null;
        if(getId()>0){
            //Step already exists
            try {
                dbm.getCon().createStatement().executeUpdate("update xinco_workflow_step set id="+getId()+
                        ", designation='"+getDesignation()+"', xinco_workflow_id="+
                        (getWorkflow_id()<1 ? 0:getWorkflow_id())+" where id="+getId());
                if(isChange()){
                    XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
                    audit.updateAuditTrail("xinco_workflow_step",new String [] {"id ="+getId()},
                            dbm,"audit.workflowstep.change",this.getChangerID());
                }
                dbm.getCon().commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
                try {
                    dbm.getCon().rollback();
                } catch (Exception erollback) {
                }
                throw new XincoException();
            }
        } else{
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            try {
                rs=dbm.getCon().createStatement().executeQuery("select count(*) from " +
                        "xinco_workflow_has_xinco_workflow_step where " +
                        "xinco_workflow_id="+getWorkflow_id());
                rs.next();
                setId(rs.getInt(1)+1);
                dbm.getCon().createStatement().executeUpdate("insert into xinco_workflow_step values("+
                        getId()+",'"+getDesignation()+"', "+getWorkflow_id()+")");
                dbm.getCon().createStatement().executeUpdate("insert into xinco_workflow_has_xinco_workflow_step values("+
                        getWorkflow_id()+", "+getId()+")");
                audit.updateAuditTrail("xinco_workflow_step",new String [] {"id ="+getId()},
                        dbm,"audit.general.create",this.getChangerID());
                dbm.getCon().commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public boolean isChange() {
        return change;
    }
    
    public void setChange(boolean change) {
        this.change = change;
    }
    
    public int getChangerID() {
        return changerID;
    }
    
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }
    
    public int getWorkflow_id() {
        return workflow_id;
    }
    
    public void setWorkflow_id(int workflow_id) {
        this.workflow_id = workflow_id;
    }
    
    public String toString(){
        String s="";
        s+="ID: "+getId()+"\n";
        s+="Description: "+getDesignation()+"\n";
        if(getFork()!=null){
            s+="Forks: \n";
            s+=getFork().toString();
        }
        return s;
    }

    private void setFork(XincoWorkflowStepForkServer xincoWorkflowStepForkServer) {
        this.forks=xincoWorkflowStepForkServer;
    }

    public XincoWorkflowStepForkServer getFork() {
        return forks;
    }
}
