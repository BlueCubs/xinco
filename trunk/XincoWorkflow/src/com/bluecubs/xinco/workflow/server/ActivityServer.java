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
 * Name:            ActivityServer
 *
 * Description:     ActivityServer
 *
 * Original Author: Javier Ortiz
 * Date:            June 8, 2007, 9:09 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.Activity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author javydreamercsw
 */
public class ActivityServer extends Activity{
    private int nodeID;
    private ResultSet rs;
    /** Creates a new instance of ActivityServer */
    public ActivityServer(int id,Properties props) {
        this.setId(id);
        this.setProperties(props);
    }
    
    public ActivityServer(int id,XincoDBManager DBM) {
        try {
            rs=DBM.getConnection().createStatement().executeQuery("select * from activity where id="+id);
            rs.next();
            setId(rs.getInt("id"));
            setDescription(rs.getString("description"));
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }
    
    
}
