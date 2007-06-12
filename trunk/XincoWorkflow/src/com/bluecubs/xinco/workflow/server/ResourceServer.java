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
 * Name:            ResourceServer.java
 *
 * Description:     ResourceServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 8, 2007, 10:20 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.workflow.Resource;
import com.bluecubs.xinco.workflow.XincoWorkflowException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourceServer extends Resource{
    private ResultSet rs;
    /** Creates a new instance of ResourceServer */
    public ResourceServer(String username,String password,WorkflowDBManager DBM) throws XincoWorkflowException {
        try {
            rs= DBM.getStatement().executeQuery("select * from " +
                    "resource where username='"+username+"' AND password=MD5('" + password + "')");
            rs.next();
            this.setEmail(rs.getString("email"));
            this.setId(rs.getInt("id"));
            this.setName(rs.getString("name"));
            this.setUserpassword(rs.getString("password"));
            this.setUsername(rs.getString("username"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new XincoWorkflowException();
        }
    }
    
}
