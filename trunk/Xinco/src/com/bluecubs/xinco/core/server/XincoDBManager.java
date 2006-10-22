/**
 *Copyright 2004 blueCubs.com
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
 * Name:            XincoDBManager
 *
 * Description:     server-side database manager
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 * 
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */

package com.bluecubs.xinco.core.server;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import com.bluecubs.xinco.conf.XincoConfigSingletonServer;

public class XincoDBManager {
    
    public Connection con;
	public XincoConfigSingletonServer config;
	public static int count = 0;
    
	public XincoDBManager() throws Exception {
		
		//load compiled configuartion
		config = XincoConfigSingletonServer.getInstance();
		DataSource datasource = (DataSource)(new InitialContext()).lookup(config.JNDIDB);
		con = datasource.getConnection();
		con.setAutoCommit(false);
		count++;
	}

    public int getNewID(String attrTN) throws Exception {

            int newID = 0;
            Statement stmt;
            
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_id WHERE tablename='" + attrTN + "'");
            while (rs.next()) {
                newID = rs.getInt("last_id") + 1;
            }
            stmt.close();

            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE xinco_id SET last_id=last_id+1 WHERE tablename='" + attrTN + "'");
            stmt.close();
            
            return newID;

    }
    
    protected void finalize() throws Throwable {
        try {
            count--;
            con.close();
        } finally {
            if (!con.isClosed()) {
            	count++;
            }
        	super.finalize();
        }
    }
    
}
