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
 * Name:            WorkflowServer.java
 *
 * Description:     WorkflowServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 8, 2007, 10:23 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.WorkflowInstance;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkflowInstanceServer extends WorkflowInstance{
    private ResultSet rs;
    private WorkflowTemplateServer template;
    /**
     * Creates a new instance of WorkflowInstanceServer
     */
    public WorkflowInstanceServer(int id, XincoDBManager DBM) {
        if(id>0){
            try {
                rs=DBM.getConnection().createStatement().executeQuery("select * from " +
                        "workflow_instance where id ="+id);
                rs.next();
                setId(rs.getInt("id"));
                setWorkflowTemplateId(rs.getInt("workflow_template_id"));
                setCreationTime(rs.getTimestamp("creationtime"));
                template =new WorkflowTemplateServer(getWorkflowTemplateId(),DBM);
                setNodes(template.getNodes());
                setTransactions(template.getTransactions());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
