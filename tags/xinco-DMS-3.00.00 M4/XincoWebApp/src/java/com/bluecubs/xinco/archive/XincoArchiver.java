/**
 * Copyright 2011 blueCubs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 *
 * Name: XincoArchiver
 *
 * Description: handle document archiving
 *
 * Original Author: Alexander Manes Date: 2005/01/16
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.archive;

import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.service.XincoAddAttribute;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * This class handles document archiving for Xinco. Edit configuration values in
 * database
 */
public class XincoArchiver {

    static XincoCoreDataServer xdataTemp = null;
    static XincoCoreLogServer xlogTemp = null;
    static ArrayList xnodeTempArrayList = null;
    static String archiveBaseDir;
    static String archiveFileDir = null;
    static ArrayList orgFileNames = new ArrayList();
    static ArrayList orgFileIDs = new ArrayList();
    static String fileName = null;
    static String archiveName;
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
        xdataTemp = d;
        xnodeTempArrayList = v;
        Calendar ngc = new GregorianCalendar();
        archiveName = ngc.get(Calendar.YEAR) + "-" + (ngc.get(Calendar.MONTH) + 1)
                + "-" + ngc.get(Calendar.DAY_OF_MONTH);
        archiveBaseDir = XincoDBManager.config.fileArchivePath + archiveName;
        //archive data
        archiveFileDir = "";
        for (i = xnodeTempArrayList.size() - 1; i >= 0; i--) {
            archiveFileDir = archiveFileDir + System.getProperty("file.separator")
                    + ((XincoCoreNodeServer) xnodeTempArrayList.get(i)).getDesignation();
        }
        (new File(archiveBaseDir + archiveFileDir)).mkdirs();
        //copy file + revisions
        //build file list
        orgFileNames.add(("" + xdataTemp.getId()));
        orgFileIDs.add(new Integer(xdataTemp.getId()));
        for (i = 0; i < ((ArrayList) xdataTemp.getXincoCoreLogs()).size(); i++) {
            xlogTemp = ((XincoCoreLogServer) ((ArrayList) xdataTemp.getXincoCoreLogs()).get(i));
            if ((xlogTemp.getOpCode() == 1) || (xlogTemp.getOpCode() == 5)) {
                orgFileNames.add(("" + xdataTemp.getId() + "-" + xlogTemp.getId()));
                orgFileIDs.add(new Integer(xdataTemp.getId()));
            }
        }
        //copy + delete files
        for (k = 0; k < orgFileNames.size(); k++) {
            fileName = ((String) orgFileNames.get(k)) + "_"
                    + ((XincoAddAttribute) ((ArrayList) xdataTemp.getXincoAddAttributes()).get(0)).getAttribVarchar();
            if ((new File(XincoCoreDataServer.getXincoCoreDataPath(
                    XincoDBManager.config.fileRepositoryPath, ((Integer) orgFileIDs.get(k)).intValue(),
                    ((String) orgFileNames.get(k))))).exists()) {
                fcis = new FileInputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(
                        XincoDBManager.config.fileRepositoryPath,
                        ((Integer) orgFileIDs.get(k)).intValue(), ((String) orgFileNames.get(k)))));
                fcos = new FileOutputStream(new File(archiveBaseDir + archiveFileDir
                        + System.getProperty("file.separator") + fileName));
                fcbuf = new byte[4096];
                len = 0;
                while ((len = fcis.read(fcbuf)) != -1) {
                    fcos.write(fcbuf, 0, len);
                }
                fcis.close();
                fcos.close();
                //delete
                (new File(XincoCoreDataServer.getXincoCoreDataPath(
                        XincoDBManager.config.fileRepositoryPath,
                        ((Integer) orgFileIDs.get(k)).intValue(),
                        ((String) orgFileNames.get(k))))).delete();
            }
        }
        //update data + log
        xdataTemp.setStatusNumber(3);
        ((XincoAddAttribute) ((ArrayList) xdataTemp.getXincoAddAttributes()).get(7)).setAttribText(
                "[" + archiveName + "]" + archiveFileDir.replace('\\', '/') + "/" + fileName);
        xdataTemp.write2DB();
        if (((ArrayList) xdataTemp.getXincoCoreLogs()).size() > 0) {
            xlogTemp = ((XincoCoreLogServer) ((ArrayList) xdataTemp.getXincoCoreLogs()).get(
                    ((ArrayList) xdataTemp.getXincoCoreLogs()).size() - 1));
            if (xlogTemp != null) {
                xlogTemp.setOpCode(OPCode.ARCHIVED.ordinal() + 1);
                xlogTemp.setOpDescription(rb.getString(OPCode.getOPCode(
                        xlogTemp.getOpCode()).getName()));
                xlogTemp.setXincoCoreUserId(1);
                xlogTemp.write2DB();
            } else {
                Logger.getLogger(XincoArchiver.class.getSimpleName()).error(
                        "XincoCoreData (" + xdataTemp + ") had no logs!");
            }
        }
        return true;
    }
}
