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

/**
 *
 * @author ortizbj
 */
public class XincoWorkflowStepForkServer extends XincoWorkflowStepFork{
    private boolean isChange;
    /** Creates a new instance of XincoWorkflowStepForkServer */
    public XincoWorkflowStepForkServer() {
    }
    
    public XincoWorkflowStepForkServer(int id, XincoDBManager dbm) {
        ResultSet rs =null;
        if(id>0){
            //Fork exists
            try {
                rs = dbm.getCon().createStatement().executeQuery("select * from xinco_step_fork where id="+id);
                rs.next();
                setYesStep(rs.getInt("yesStep"));
                setNoStep(rs.getInt("noStep"));
                setId(rs.getInt("id"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else{
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
    
    public XincoWorkflowStepForkServer(int id,int yesStep,int noStep) {
        setId(id);
        setYesStep(yesStep);
        setNoStep(noStep);
        try {
            write2DB(new XincoDBManager());
        } catch (XincoException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isIsChange() {
        return isChange;
    }
    
    public void setIsChange(boolean isChange) {
        this.isChange = isChange;
    }
    
    public void write2DB(XincoDBManager dbm) throws XincoException{
        
    }
    
    public String toString(){
        String s="";
        s+="ID: "+getId()+"\n";
        s+="Yes: "+getYesStep()+"\n";
        s+="No: "+getNoStep()+"\n";
        return s;
    }
}
