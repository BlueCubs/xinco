/**
 *Copyright 2005 blueCubs.com
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
 * Name:            XincoArchiver
 *
 * Description:     handle document archiving
 *
 * Original Author: Alexander Manes
 * Date:            2005/01/16
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */

package com.bluecubs.xinco.archive;

import com.bluecubs.xinco.add.XincoAddAttribute;
import java.sql.*;
import java.io.*;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.bluecubs.xinco.core.server.*;

/**
 * This class handles document archiving for xinco.
 * Edit configuration values in context.xml
 */
public class XincoArchiver {
    
    public static synchronized boolean archiveData(XincoDBManager dbm) {
        
        int i = 0;
        int j = 0;
        int k = 0;
        int len = 0;
        FileInputStream fcis = null;
        FileOutputStream fcos = null;
        byte[] fcbuf = null;
        
        try {
            XincoCoreDataServer xdata_temp = null;
            XincoCoreLogServer xlog_temp = null;
            Vector xnode_temp_vector = null;
            Calendar ngc = new GregorianCalendar();
            String ArchiveName = ngc.get(Calendar.YEAR) + "-" + (ngc.get(Calendar.MONTH) + 1) + "-" + ngc.get(Calendar.DAY_OF_MONTH);
            String ArchiveBaseDir = dbm.config.FileArchivePath + ArchiveName;
            String ArchiveFileDir = null;
            Vector OrgFileNames = new Vector();
            Vector OrgFileIDs = new Vector();
            String FileName = null;
            int querycount = 0;
            String[] query = new String[2];
            
            query[querycount] = new String("SELECT DISTINCT xcd.id FROM xinco_core_data xcd, xinco_add_attribute xaa1, xinco_add_attribute xaa2 " +
                    "WHERE xcd.xinco_core_data_type_id = 1 " +
                    "AND xcd.status_number <> 3 " +
                    "AND xcd.id = xaa1.xinco_core_data_id " +
                    "AND xcd.id = xaa2.xinco_core_data_id " +
                    "AND xaa1.attribute_id = 5 " +
                    "AND xaa1.attrib_unsignedint = 1 " +
                    "AND xaa2.attribute_id = 6 " +
                    "AND xaa2.attrib_datetime < now() " +
                    "ORDER BY xcd.id");
            querycount++;
            
            query[querycount] = new String("SELECT DISTINCT xcd.id FROM xinco_core_data xcd, xinco_add_attribute xaa1, xinco_add_attribute xaa2, xinco_core_log xcl " +
                    "WHERE xcd.xinco_core_data_type_id = 1 " +
                    "AND xcd.status_number <> 3 " +
                    "AND xcd.id = xaa1.xinco_core_data_id " +
                    "AND xcd.id = xaa2.xinco_core_data_id " +
                    "AND xcd.id = xcl.xinco_core_data_id " +
                    "AND xaa1.attribute_id = 5 " +
                    "AND xaa1.attrib_unsignedint = 2 " +
                    "AND xaa2.attribute_id = 7 " +
                    "AND ADDDATE(DATE(xcl.op_datetime), xaa2.attrib_unsignedint) < now() " +
                    "ORDER BY xcd.id");
            //"AND xcl.op_datetime < (now()-(xaa2.attrib_unsignedint*3600*24)) " +
            querycount++;
            
            for (j=0;j<querycount;j++) {
                
                Statement stmt = dbm.getConnection().createStatement();
                //select data with expired archiving date
                ResultSet rs = stmt.executeQuery(query[j]);
                while (rs.next()) {
                    xdata_temp = new XincoCoreDataServer(rs.getInt("xcd.id"), dbm);
                    xnode_temp_vector = XincoCoreNodeServer.getXincoCoreNodeParents(xdata_temp.getXinco_core_node_id(), dbm);
                    //archive data
                    ArchiveFileDir = "";
                    for (i=xnode_temp_vector.size()-1;i>=0;i--) {
                        ArchiveFileDir = ArchiveFileDir + System.getProperty("file.separator") + ((XincoCoreNodeServer)xnode_temp_vector.elementAt(i)).getDesignation();
                    }
                    (new File(ArchiveBaseDir + ArchiveFileDir)).mkdirs();
                    //copy file + revisions
                    //build file list
                    OrgFileNames.add(new String("" + xdata_temp.getId()));
                    OrgFileIDs.add(new Integer(xdata_temp.getId()));
                    for (i=0;i<xdata_temp.getXinco_core_logs().size();i++) {
                        xlog_temp = ((XincoCoreLogServer)xdata_temp.getXinco_core_logs().elementAt(i));
                        if ((xlog_temp.getOp_code() == 1) || (xlog_temp.getOp_code() == 5)) {
                            OrgFileNames.add(new String("" + xdata_temp.getId() + "-" + xlog_temp.getId()));
                            OrgFileIDs.add(new Integer(xdata_temp.getId()));
                        }
                    }
                    //copy + delete files
                    for (k=0;k<OrgFileNames.size();k++) {
                        FileName = ((String)OrgFileNames.elementAt(k)) + "_" + ((XincoAddAttribute)xdata_temp.getXinco_add_attributes().elementAt(0)).getAttrib_varchar();
                        if ((new File(XincoCoreDataServer.getXincoCoreDataPath(dbm.config.FileRepositoryPath, ((Integer)OrgFileIDs.elementAt(k)).intValue(), ((String)OrgFileNames.elementAt(k))))).exists()) {
                            fcis  = new FileInputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(dbm.config.FileRepositoryPath, ((Integer)OrgFileIDs.elementAt(k)).intValue(), ((String)OrgFileNames.elementAt(k)))));
                            fcos = new FileOutputStream(new File(ArchiveBaseDir + ArchiveFileDir + System.getProperty("file.separator") + FileName));
                            fcbuf = new byte[4096];
                            len = 0;
                            while((len=fcis.read(fcbuf))!=-1) {
                                fcos.write(fcbuf, 0, len);
                            }
                            fcis.close();
                            fcos.close();
                            //delete
                            (new File(XincoCoreDataServer.getXincoCoreDataPath(dbm.config.FileRepositoryPath, ((Integer)OrgFileIDs.elementAt(k)).intValue(), ((String)OrgFileNames.elementAt(k))))).delete();
                        }
                    }
                    //update data + log
                    xdata_temp.setStatus_number(3);
                    ((XincoAddAttribute)xdata_temp.getXinco_add_attributes().elementAt(7)).setAttrib_text("[" + ArchiveName + "]" + ArchiveFileDir.replace('\\', '/') + "/" + FileName);
                    xdata_temp.write2DB(dbm);
                    if (xdata_temp.getXinco_core_logs().size() > 0) {
                        xlog_temp = ((XincoCoreLogServer)xdata_temp.getXinco_core_logs().elementAt(xdata_temp.getXinco_core_logs().size()-1));
                        if (xlog_temp != null) {
                            xlog_temp.setId(0);
                            xlog_temp.setOp_code(8);
                            xlog_temp.setOp_description("Archived!");
                            xlog_temp.setXinco_core_user_id(1);
                            xlog_temp.write2DB(dbm);
                        }
                    }
                }
                stmt.close();
                
            }
            return true;
        } catch (Exception e) {
            try {
                if (fcis != null) {
                    fcis.close();
                }
                if (fcos != null) {
                    fcos.close();
                }
            } catch (Exception fe) {}
            return false;
        }
        
    }
    
    private XincoArchiver() {
    }
    
}
