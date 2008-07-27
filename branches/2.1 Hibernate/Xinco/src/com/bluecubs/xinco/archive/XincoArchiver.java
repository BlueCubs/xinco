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

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * This class handles document archiving for xinco.
 * Edit configuration values in context.xml
 */
public class XincoArchiver {

    static XincoCoreDataServer xdataTemp = null;
    static XincoCoreLogServer xlogTemp = null;
    static Vector xnodeTempVector = null;
    static String ArchiveBaseDir;
    static String ArchiveFileDir = null;
    static Vector OrgFileNames = new Vector();
    static Vector OrgFileIDs = new Vector();
    static String FileName = null;
    static String ArchiveName;
    static int i = 0;
    static int j = 0;
    static int k = 0;
    static int len = 0;
    static FileInputStream fcis = null;
    static FileOutputStream fcos = null;
    static byte[] fcbuf = null;

    private XincoArchiver() {
    }

    @SuppressWarnings("unchecked")
    public static boolean archiveData(XincoCoreDataServer d, Vector v, XincoDBManager DBM) throws FileNotFoundException, IOException, XincoException {
        xdataTemp = d;
        xnodeTempVector = v;
        Calendar ngc = new GregorianCalendar();
        ArchiveName = ngc.get(Calendar.YEAR) + "-" + (ngc.get(Calendar.MONTH) + 1) + "-" + ngc.get(Calendar.DAY_OF_MONTH);
        ArchiveBaseDir = DBM.config.getFileArchivePath() + ArchiveName;
        //archive data
        ArchiveFileDir = "";
        for (i = xnodeTempVector.size() - 1; i >= 0; i--) {
            ArchiveFileDir = ArchiveFileDir + System.getProperty("file.separator") + ((XincoCoreNodeServer) xnodeTempVector.elementAt(i)).getDesignation();
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
            if ((new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), ((Integer) OrgFileIDs.elementAt(k)).intValue(), ((String) OrgFileNames.elementAt(k))))).exists()) {
                fcis = new FileInputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), ((Integer) OrgFileIDs.elementAt(k)).intValue(), ((String) OrgFileNames.elementAt(k)))));
                fcos = new FileOutputStream(new File(ArchiveBaseDir + ArchiveFileDir + System.getProperty("file.separator") + FileName));
                fcbuf = new byte[4096];
                len = 0;
                while ((len = fcis.read(fcbuf)) != -1) {
                    fcos.write(fcbuf, 0, len);
                }
                fcis.close();
                fcos.close();
                //delete
                (new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), ((Integer) OrgFileIDs.elementAt(k)).intValue(), ((String) OrgFileNames.elementAt(k))))).delete();
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
                xlogTemp.write2DB(DBM);
            }
        }
        return true;
    }
}
