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

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.Node;
import com.bluecubs.xinco.workflow.Transaction;
import com.bluecubs.xinco.workflow.WorkflowTemplate;
import com.bluecubs.xinco.workflow.server.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WorkflowTemplateServer extends WorkflowTemplate{
    ResultSet rs;
    /** Creates a new instance of WorkflowTemplateServer */
    public WorkflowTemplateServer(int id, XincoDBManager DBM){
        if(id>0){
            try {
                rs=DBM.getConnection().createStatement().executeQuery("select * from " +
                        "workflow_template where id ="+id);
                rs.next();
                setId(rs.getInt("id"));
                setDescription(rs.getString("description"));
                rs.close();
                loadNodes(DBM);
                loadTransactions(DBM);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private Node [] loadNodes(XincoDBManager DBM) throws SQLException{
        rs=DBM.getConnection().createStatement().executeQuery("select node_id from " +
                "Workflow_Template_has_Node where id="+getId());
        setNodes(null);
        int counter=0;
        while(rs.next()){
            setNodes(counter,new NodeServer(rs.getInt("node_id"),DBM));
            counter++;
        }
        return getNodes();
    }
    
    private Transaction [] loadTransactions(XincoDBManager DBM) throws SQLException{
        rs=DBM.getConnection().createStatement().executeQuery("select transaction_id from " +
                "Workflow_Template_has_Transaction where id="+getId());
        setTransactions(null);
        int counter=0;
        while(rs.next()){
            setTransactions(counter,new TransactionServer(rs.getInt("transaction_id"),DBM));
            counter ++;
        }
        return getTransactions();
    }
}
