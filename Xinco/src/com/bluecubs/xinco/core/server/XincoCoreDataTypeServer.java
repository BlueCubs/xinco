/**
 *Copyright 2010 blueCubs.com
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
 * Name:            XincoCoreDataTypeServer
 *
 * Description:     data type 
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

import java.util.Vector;
import java.sql.*;

import com.bluecubs.xinco.core.*;

public class XincoCoreDataTypeServer extends XincoCoreDataType {
    
    //create data type object for data structures
    public XincoCoreDataTypeServer(int attrID, XincoDBManager DBM) throws XincoException {
        
        try {
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_data_type WHERE id=" + attrID);

			//throw exception if no result found
			int RowCount = 0;
            while (rs.next()) {
				RowCount++;
                setId(rs.getInt("id"));
                setDesignation(rs.getString("designation"));
                setDescription(rs.getString("description"));
				setXinco_core_data_type_attributes(XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(getId(), DBM));
            }
			if (RowCount < 1) {
				throw new XincoException();
			}

            stmt.close();
        } catch (Exception e) {
        	throw new XincoException();
        }
        
    }

    //create data type object for data structures
    public XincoCoreDataTypeServer(int attrID, String attrD, String attrDESC, Vector attrA) throws XincoException {
        
        setId(attrID);
        setDesignation(attrD);
        setDescription(attrDESC);
		setXinco_core_data_type_attributes(attrA);
        
    }
    
    //create complete list of data types
    public static Vector getXincoCoreDataTypes(XincoDBManager DBM) {
        
        Vector coreDataTypes = new Vector();
        
        try {
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_data_type ORDER BY designation");

            while (rs.next()) {
                coreDataTypes.addElement(new XincoCoreDataTypeServer(rs.getInt("id"), rs.getString("designation"), rs.getString("description"), XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(rs.getInt("id"), DBM)));
            }

            stmt.close();
        } catch (Exception e) {
            coreDataTypes.removeAllElements();
        }

        return coreDataTypes;
    }

}
