/**
 *Copyright June 12, 2007 blueCubs.com
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
 * Name:            WorkflowSettingServer.java
 *
 * Description:     WorkflowSettingServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 12, 2007, 4:07 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.WorkflowSetting;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class WorkflowSettingServer extends WorkflowSetting{
    private Vector workflow_settings=null;
    /** Creates a new instance of WorkflowSettingServer */
    public WorkflowSettingServer() {
    }
    
    public WorkflowSettingServer(int id,WorkflowDBManager DBM)throws XincoException{
        try {
            ResultSet rs= DBM.getConnection().createStatement().executeQuery("select * from xinco_setting where id="+getId());
            rs.next();
            this.setId(id);
            this.setDescription(rs.getString("description"));
            this.setInt_value(rs.getInt("int_value"));
            this.setString_value(rs.getString("string_value"));
            this.setBool_value(rs.getBoolean("bool_value"));
            this.setLong_value(rs.getInt("long_value"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public Vector getWorkflow_settings() {
        if(workflow_settings==null)
            try {
                setWorkflow_settings(new WorkflowDBManager().getWorkflowSettingServer().getWorkflow_settings());
                super.setWorkflow_settings(workflow_settings);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        return workflow_settings;
    }
    
     public WorkflowSetting getSetting(int i){
        return (WorkflowSetting)getWorkflow_settings().get(i);
    }
    
    public WorkflowSetting getSetting(String s){
        for(int i=0;i<getWorkflow_settings().size();i++){
            if(((WorkflowSetting)getWorkflow_settings().get(i)).getDescription().equals(s))
                return (WorkflowSetting)getWorkflow_settings().get(i);
        }
        return null;
    }

    public void setWorkflow_settings(Vector workflow_settings) {
        this.workflow_settings = workflow_settings;
    }
}
