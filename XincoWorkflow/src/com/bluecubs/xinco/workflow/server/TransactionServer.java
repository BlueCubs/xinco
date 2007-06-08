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
 * Name:            TransactionServer.java
 *
 * Description:     TransactionServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 8, 2007, 10:21 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.Transaction;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TransactionServer extends Transaction{
    private ResultSet rs;
    /** Creates a new instance of TransactionServer */
    public TransactionServer(int id, XincoDBManager DBM) {
        if(id>0){
            try {
                rs=DBM.getConnection().createStatement().executeQuery("select * from transaction where id="+id);
                rs.next();
                setId(rs.getInt("id"));
                setDescription(rs.getString("description"));
                loadActivities(DBM);
                loadProperties(DBM);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void loadActivities(XincoDBManager DBM) throws SQLException{
        getActivities().clear();
        rs=DBM.getConnection().createStatement().executeQuery("select activity_id from " +
                "Transaction_has_Activity where transaction_id="+getId());
        while(rs.next())
            getActivities().add(new ActivityServer(rs.getInt("activity_id"),DBM));
    }
    
    private void loadProperties(XincoDBManager DBM) throws SQLException{
        getProperties().clear();
        Vector temp=new PropertyServer().getPropertiesForTransaction(getId(),DBM);
        for(int i=0;i<temp.size();i++){
            getProperties().add((PropertyServer)temp.get(i));
        }
    }
}
