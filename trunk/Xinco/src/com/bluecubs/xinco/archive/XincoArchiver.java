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
import com.bluecubs.xinco.core.server.OPCode;
import com.bluecubs.xinco.core.server.XincoException;
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
import java.util.ResourceBundle;
import java.util.ArrayList;

/**
 * This class handles document archiving for xinco.
 * Edit configuration values in context.xml
 */
public class XincoArchiver {

    static XincoCoreDataServer xdata_temp = null;
    static XincoCoreLogServer xlog_temp = null;
    static ArrayList<XincoCoreNodeServer> xnode_temp_ArrayList = null;
    static String ArchiveBaseDir;
    static String ArchiveFileDir = null;
    static ArrayList OrgFileNames = new ArrayList();
    static ArrayList OrgFileIDs = new ArrayList();
    static String FileName = null;
    static String ArchiveName;
    static int i = 0;
    static int j = 0;
    static int k = 0;
    static int len = 0;
    static FileInputStream fcis = null;
    static FileOutputStream fcos = null;
    static byte[] fcbuf = null;
    static ResourceBundle rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");

    private XincoArchiver() {
    }

    @SuppressWarnings("unchecked")
    public static boolean archiveData(XincoCoreDataServer d, ArrayList v) throws FileNotFoundException, IOException, XincoException {
        xdata_temp = d;
        xnode_temp_ArrayList = v;
        Calendar ngc = new GregorianCalendar();
        ArchiveName = ngc.get(Calendar.YEAR) + "-" + (ngc.get(Calendar.MONTH) + 1) + "-" + ngc.get(Calendar.DAY_OF_MONTH);
        ArchiveBaseDir = XincoDBManager.config.FileArchivePath + ArchiveName;
        //archive data
        ArchiveFileDir = "";
        for (i = xnode_temp_ArrayList.size() - 1; i >= 0; i--) {
            ArchiveFileDir = ArchiveFileDir + System.getProperty("file.separator") + ((XincoCoreNodeServer) xnode_temp_ArrayList.get(i)).getDesignation();
        }
        (new File(ArchiveBaseDir + ArchiveFileDir)).mkdirs();
        //copy file + revisions
        //build file list
        OrgFileNames.add(("" + xdata_temp.getId()));
        OrgFileIDs.add(new Integer(xdata_temp.getId()));
        for (i = 0; i < (xdata_temp.getXinco_core_logs()).size(); i++) {
            xlog_temp = ((XincoCoreLogServer) (xdata_temp.getXinco_core_logs()).get(i));
            if ((xlog_temp.getOp_code() == 1) || (xlog_temp.getOp_code() == 5)) {
                OrgFileNames.add(("" + xdata_temp.getId() + "-" + xlog_temp.getId()));
                OrgFileIDs.add(new Integer(xdata_temp.getId()));
            }
        }
        //copy + delete files
        for (k = 0; k < OrgFileNames.size(); k++) {
            FileName = ((String) OrgFileNames.get(k)) + "_" + ((XincoAddAttribute) (xdata_temp.getXinco_add_attributes()).get(0)).getAttrib_varchar();
            if ((new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, ((Integer) OrgFileIDs.get(k)).intValue(), ((String) OrgFileNames.get(k))))).exists()) {
                fcis = new FileInputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, ((Integer) OrgFileIDs.get(k)).intValue(), ((String) OrgFileNames.get(k)))));
                fcos = new FileOutputStream(new File(ArchiveBaseDir + ArchiveFileDir + System.getProperty("file.separator") + FileName));
                fcbuf = new byte[4096];
                len = 0;
                while ((len = fcis.read(fcbuf)) != -1) {
                    fcos.write(fcbuf, 0, len);
                }
                fcis.close();
                fcos.close();
                //delete
                (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, ((Integer) OrgFileIDs.get(k)).intValue(), ((String) OrgFileNames.get(k))))).delete();
            }
        }
        //update data + log
        xdata_temp.setStatus_number(3);
        ((XincoAddAttribute) (xdata_temp.getXinco_add_attributes()).get(7)).setAttrib_text("[" + ArchiveName + "]" + ArchiveFileDir.replace('\\', '/') + "/" + FileName);
        xdata_temp.write2DB();
        if ((xdata_temp.getXinco_core_logs()).size() > 0) {
            xlog_temp = ((XincoCoreLogServer) (xdata_temp.getXinco_core_logs()).get((xdata_temp.getXinco_core_logs()).size() - 1));
            if (xlog_temp != null) {
                xlog_temp.setId(0);
                xlog_temp.setOp_code(OPCode.ARCHIVED.ordinal() + 1);
                xlog_temp.setOp_description(rb.getString(OPCode.getOPCode(xlog_temp.getOp_code()).getName() + "!"));
                xlog_temp.setXinco_core_user_id(1);
                xlog_temp.write2DB();
            }
        }
        return true;
    }
}
