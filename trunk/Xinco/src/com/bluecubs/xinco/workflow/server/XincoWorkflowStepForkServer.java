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
 * Name:            XincoWorkflowStepForkServer
 *
 * Description:     XincoWorkflowStepForkServer
 *
 * Original Author: Javier Ortiz
 * Date:            May 17, 2007, 10:42 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.XincoWorkflowStepFork;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author ortizbj
 */
public class XincoWorkflowStepForkServer extends XincoWorkflowStepFork{
    private boolean change;
    /** Creates a new instance of XincoWorkflowStepForkServer */
    public XincoWorkflowStepForkServer() {
    }
    
    public XincoWorkflowStepForkServer(int step_id,int workflow_id, XincoDBManager dbm) {
        ResultSet rs =null;
        //Fork exists
        try {
            System.out.println("select * from " +
                    "xinco_workflow_step_has_xinco_workflow_fork where xinco_workflow_step_id="+
                    step_id);
            rs = dbm.getCon().createStatement().executeQuery("select * from " +
                    "xinco_workflow_step_has_xinco_workflow_fork where xinco_workflow_step_id="+
                    step_id);
            Vector forks= new Vector();
            while(rs.next()){
                System.err.println("Found fork for step: "+rs.getInt("xinco_workflow_step_id")+", Subworkflow: "+
                        rs.getInt("xinco_workflow_id"));
                try {
                    forks.add(new XincoWorkflowServer(rs.getInt("xinco_workflow_id"),new XincoDBManager()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            setWorkflow_id(workflow_id);
            setStep_id(step_id);
            setForks(forks);
        } catch (SQLException ex) {
            // No forks found
        }
    }
    
    public XincoWorkflowStepForkServer(int xinco_workflow_id,int xinco_workflow_step_id,Vector forks) {
        setWorkflow_id(xinco_workflow_id);
        setStep_id(xinco_workflow_step_id);
        setForks(forks);
        try {
            write2DB(new XincoDBManager());
        } catch (XincoException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isChange() {
        return change;
    }
    
    public void setIsChange(boolean isChange) {
        this.change = isChange;
    }
    
    public void write2DB(XincoDBManager dbm) throws XincoException{
        
    }
    
    public String toString(){
        String s="";
        if(getForks()!=null && getForks().size()>0){
            for(int i=0;i<getForks().size();i++){
                s+="Fork # "+(i+1)+"------------------------------------\n";
                s+=((XincoWorkflowServer)getForks().get(i)).toString();
            }
        }
        return s;
    }
}
