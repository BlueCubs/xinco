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
 * Name:            XincoWorkflowServer
 *
 * Description:     XincoWorkflowServer
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
import com.bluecubs.xinco.workflow.XincoWorkflow;
import com.bluecubs.xinco.workflow.XincoWorkflowStep;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author ortizbj
 */
public class XincoWorkflowServer extends XincoWorkflow{
    private boolean change=false;
    private int changerID;
    private XincoWorkflowStep currentStep=null;
    /** Creates a new instance of XincoWorkflowServer */
    public XincoWorkflowServer(int id,XincoDBManager dbm) {
        ResultSet rs =null;
        if(id>0){
            //Workflow exists
            try {
                rs = dbm.getCon().createStatement().executeQuery("select * from " +
                        "xinco_workflow where id="+id);
                rs.next();
                setDescription(rs.getString("designation"));
                setId(rs.getInt("id"));
                    try {
                        //Load steps
                        setXinco_workflow_steps(new XincoWorkflowStepServer().getSteps(getId(),new XincoDBManager()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public XincoWorkflowServer(String description,
            Vector xinco_workflow_steps, XincoDBManager dbm) {
        try {
            setId(dbm.getNewID("xinco_workflow"));
            setXinco_workflow_steps(xinco_workflow_steps);
            setDescription(description);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
            //Workflow already exists
            try {
                dbm.getCon().createStatement().executeUpdate("update xinco_workflow set id="+getId()+
                        ", designation='"+getDescription()+"'");
                if(isChange()){
                    XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
                    audit.updateAuditTrail("xinco_workflow",
                            new String [] {"id ="+getId()},
                            dbm,"audit.workflow.change",this.getChangerID());
                    //Update steps
                    for(int i=0;i<getXinco_workflow_steps().size();i++){
                        //Mark as changed
                        ((XincoWorkflowStepServer)getXinco_workflow_steps().get(i)).setChange(true);
                        //Set user making the change
                        ((XincoWorkflowStepServer)getXinco_workflow_steps().get(i)).setChangerID(getChangerID());
                        //Write changes to the Database
                        ((XincoWorkflowStepServer)getXinco_workflow_steps().get(i)).write2DB(dbm);
                    }
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
                setId(dbm.getNewID("xinco_workflow"));
                stmt.execute("insert into xinco_workflow values("+
                        getId()+",'"+getDescription()+"')");
                audit.updateAuditTrail("xinco_workflow",new String [] {"id ="+getId()},
                        dbm,"audit.general.create",this.getChangerID());
                stmt.close();
                dbm.getCon().commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
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
    
    public String toString() {
        String s= null;
        try {
            s="Workflow:\n";
            s+="ID: "+getId()+"\n";
            s+="Description: "+getDescription()+"\n";
            s+="----------------------------"+"\n";
            s+="Steps: "+"\n";
            s+="----------------------------"+"\n";
            for(int i=0;i<getXinco_workflow_steps().size();i++){
                s+=(i+1)+". "+((XincoWorkflowStepServer)getXinco_workflow_steps().get(i)).toString();
                s+="----------------------------\n";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }
    
}
