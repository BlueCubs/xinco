/*
 * XincoWorkflowServer.java
 *
 * Created on May 2, 2007, 4:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
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
    /**
     * Creates a new instance of XincoWorkflowStepServer
     * @param step_id
     * @param workflow_id
     * @param designation
     */
    public XincoWorkflowStepServer(int step_id,int workflow_id, String designation) {
        setId(step_id);
        setWorkflow_id(workflow_id);
        setDescription(designation);
        try {
            write2DB(new XincoDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public XincoWorkflowStepServer(){
    }
    
    public Vector getSteps(int id, XincoDBManager dbm){
        Statement stmt=null,stmt2=null;
        ResultSet rs =null,rs2=null;
        Vector steps=null;
        if(id>0){
            //Workflow exists
            steps = new Vector();
            try {
                String sql="select id from xinco_workflow_has_xinco_workflow_step where xinco_workflow_id ="+id;
                System.out.println(sql);
                stmt = dbm.getCon().createStatement();
                rs = stmt.executeQuery(sql);
                int c=0;
                while(rs.next()){
                    stmt2 = dbm.getCon().createStatement();
                    System.out.println("Loop #: "+(++c));
                    sql="select * from xinco_workflow_step where id ="+rs.getInt("id");
                    System.out.println(sql);
                    rs2=stmt.executeQuery(sql);
                    rs2.next();
                    steps.add(new XincoWorkflowStep(rs2.getInt("id"),rs2.getString("designation")));
                    stmt2.close();
                }
                stmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return steps;
    }
    
    public void write2DB(XincoDBManager dbm) throws XincoException{
        Statement stmt=null;
        ResultSet rs =null,rs2=null;
        try {
            dbm = new XincoDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(getId()>0){
            //Step already exists
            try {
                stmt = dbm.getCon().createStatement();
                stmt.executeUpdate("update xinco_workflow_step set id="+getId()+
                        ", designation='"+getDescription()+"'");
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
                stmt = dbm.getCon().createStatement();
                rs=stmt.executeQuery("select count(*) from " +
                        "xinco_workflow_has_xinco_workflow_step where " +
                        "xinco_workflow_id="+getWorkflow_id());
                rs.next();
                setId(rs.getInt(1)+1);
                stmt.execute("insert into xinco_workflow_step values("+
                        getId()+",'"+getDescription()+"')");
                stmt.execute("insert into xinco_workflow_has_xinco_workflow_step values("+
                        getWorkflow_id()+", "+getId()+")");
                audit.updateAuditTrail("xinco_workflow_step",new String [] {"id ="+getId()},
                        dbm,"audit.general.create",this.getChangerID());
                stmt.close();
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
}
