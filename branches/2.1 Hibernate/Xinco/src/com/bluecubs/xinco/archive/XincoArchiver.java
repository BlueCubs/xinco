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
import com.bluecubs.xinco.core.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.persistence.XincoPersistenceManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

/**
 * This class handles document archiving for xinco.
 * Edit configuration values in context.xml
 */
public class XincoArchiver {

    private static XincoPersistenceManager pm;
    private static List result;

    @SuppressWarnings("unchecked")
    public static synchronized boolean archiveData() {
        int i = 0;
        int j = 0;
        int k = 0;
        int len = 0;
        FileInputStream fcis = null;
        FileOutputStream fcos = null;
        byte[] fcbuf = null;
        try {
            XincoCoreDataServer xdataTemp = null;
            XincoCoreLogServer xlogTemp = null;
            Vector xnodeTemp_vector = null;
            Calendar ngc = new GregorianCalendar();
            String ArchiveName = ngc.get(Calendar.YEAR) + "-" + (ngc.get(Calendar.MONTH) + 1) + "-" + ngc.get(Calendar.DAY_OF_MONTH);
            String ArchiveBaseDir = pm.getXincoConfigSingleton().getFileArchivePath() + ArchiveName;
            String ArchiveFileDir = null;
            Vector OrgFileNames = new Vector();
            Vector OrgFileIDs = new Vector();
            String FileName = null;
            int querycount = 0;
            String[] query = new String[2];
            query[querycount] = new String("SELECT DISTINCT xcd.id FROM XincoCoreData xcd, XincoAddAttribute xaa1, XincoAddAttribute xaa2 " +
                    "WHERE xcd.xincoCoreDataTypeId = 1 " +
                    "AND xcd.statusNumber <> 3 " +
                    "AND xcd.id = xaa1.xincoCoreDataId " +
                    "AND xcd.id = xaa2.xincoCoreDataId " +
                    "AND xaa1.attributeId = 5 " +
                    "AND xaa1.attrib_unsignedint = 1 " +
                    "AND xaa2.attributeId = 6 " +
                    "AND xaa2.attribDatetime < now() " +
                    "ORDER BY xcd.id");
            querycount++;
            query[querycount] = new String("SELECT DISTINCT xcd.id FROM XincoCoreData xcd, XincoAddAttribute xaa1, XincoAddAttribute xaa2, XincoCoreLog xcl " +
                    "WHERE xcd.xincoCoreDataTypeId = 1 " +
                    "AND xcd.statusNumber <> 3 " +
                    "AND xcd.id = xaa1.xincoCoreDataId " +
                    "AND xcd.id = xaa2.xincoCoreDataId " +
                    "AND xcd.id = xcl.xincoCoreDataId " +
                    "AND xaa1.attributeId = 5 " +
                    "AND xaa1.attrib_unsignedint = 2 " +
                    "AND xaa2.attributeId = 7 " +
                    "AND ADDDATE(xcl.opDatetime, xaa2.attribUnsignedint) < now() " +
                    "ORDER BY xcd.id");
            querycount++;
            for (j = 0; j < querycount; j++) {
                //select data with expired archiving date
                result = pm.executeQuery(query[j]);
                while (!result.isEmpty()) {
                    XincoCoreData temp = (XincoCoreData)result.get(0);
                    xdataTemp = new XincoCoreDataServer(temp.getId());
                    xnodeTemp_vector = XincoCoreNodeServer.getXincoCoreNodeParents(xdataTemp.getXincoCoreNodeId());
                    //archive data
                    ArchiveFileDir = "";
                    for (i = xnodeTemp_vector.size() - 1; i >= 0; i--) {
                        ArchiveFileDir = ArchiveFileDir + System.getProperty("file.separator") + ((XincoCoreNodeServer) xnodeTemp_vector.elementAt(i)).getDesignation();
                    }
                    (new File(ArchiveBaseDir + ArchiveFileDir)).mkdirs();
                    //copy file + revisions
                    //build file list
                    OrgFileNames.add(new String("" + xdataTemp.getId()));
                    OrgFileIDs.add(new Integer(xdataTemp.getId()));
                    for (i = 0; i < xdataTemp.getXincoCoreLogs().size(); i++) {
                        xlogTemp = ((XincoCoreLogServer) xdataTemp.getXincoCoreLogs().elementAt(i));
                        if ((xlogTemp.getOpCode() == 1) || (xlogTemp.getOpCode() == 5)) {
                            OrgFileNames.add(new String("" + xdataTemp.getId() + "-" + xlogTemp.getId()));
                            OrgFileIDs.add(new Integer(xdataTemp.getId()));
                        }
                    }
                    //copy + delete files
                    for (k = 0; k < OrgFileNames.size(); k++) {
                        FileName = ((String) OrgFileNames.elementAt(k)) + "_" + ((XincoAddAttribute) xdataTemp.getXincoAddAttributes().elementAt(0)).getAttribVarchar();
                        if ((new File(XincoCoreDataServer.getXincoCoreDataPath(pm.getXincoConfigSingleton().getFileRepositoryPath(), ((Integer) OrgFileIDs.elementAt(k)).intValue(), ((String) OrgFileNames.elementAt(k))))).exists()) {
                            fcis = new FileInputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(pm.getXincoConfigSingleton().getFileRepositoryPath(), ((Integer) OrgFileIDs.elementAt(k)).intValue(), ((String) OrgFileNames.elementAt(k)))));
                            fcos = new FileOutputStream(new File(ArchiveBaseDir + ArchiveFileDir + System.getProperty("file.separator") + FileName));
                            fcbuf = new byte[4096];
                            len = 0;
                            while ((len = fcis.read(fcbuf)) != -1) {
                                fcos.write(fcbuf, 0, len);
                            }
                            fcis.close();
                            fcos.close();
                            //delete
                            (new File(XincoCoreDataServer.getXincoCoreDataPath(pm.getXincoConfigSingleton().getFileRepositoryPath(), ((Integer) OrgFileIDs.elementAt(k)).intValue(), ((String) OrgFileNames.elementAt(k))))).delete();
                        }
                    }
                    //update data + log
                    xdataTemp.setStatusNumber(3);
                    ((XincoAddAttribute) xdataTemp.getXincoAddAttributes().elementAt(7)).setAttribText("[" + ArchiveName + "]" + ArchiveFileDir.replace('\\', '/') + "/" + FileName);
                    xdataTemp.write2DB();
                    if (xdataTemp.getXincoCoreLogs().size() > 0) {
                        xlogTemp = ((XincoCoreLogServer) xdataTemp.getXincoCoreLogs().elementAt(xdataTemp.getXincoCoreLogs().size() - 1));
                        if (xlogTemp != null) {
                            xlogTemp.setId(0);
                            xlogTemp.setOpCode(8);
                            xlogTemp.setOpDescription("Archived!");
                            xlogTemp.setXincoCoreUserId(1);
                            xlogTemp.write2DB();
                        }
                    }
                    result.remove(0);
                }
            }
            return true;
        } catch (Throwable e) {
            try {
                if (fcis != null) {
                    fcis.close();
                }
                if (fcos != null) {
                    fcos.close();
                }
            } catch (Exception fe) {
            }
            return false;
        }
    }

    private XincoArchiver() {
    }
}
