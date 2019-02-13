/**
 * Copyright 2012 blueCubs.com
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
 *************************************************************
 */
package com.bluecubs.xinco.archive;

import static com.bluecubs.xinco.core.OPCode.ARCHIVED;
import static com.bluecubs.xinco.core.OPCode.CREATION;
import static com.bluecubs.xinco.core.OPCode.getOPCode;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.getXincoCoreDataPath;
import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import static java.lang.System.getProperty;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.ResourceBundle.getBundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.server.service.XincoAddAttribute;

/**
 * This class handles document archiving for Xinco. Edit configuration values in
 * database
 */
public final class XincoArchiver
{

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
  static ResourceBundle rb
          = getBundle("com.bluecubs.xinco.messages.XincoMessages");

  private XincoArchiver()
  {
  }

  @SuppressWarnings("unchecked")
  public static boolean archiveData(XincoCoreDataServer d, ArrayList v)
          throws FileNotFoundException, IOException, XincoException
  {
    boolean result = false;
    try
    {
      xdataTemp = d;
      xnodeTempArrayList = v;
      Calendar ngc = new GregorianCalendar();
      archiveName = ngc.get(YEAR) + "-"
              + (ngc.get(MONTH) + 1)
              + "-" + ngc.get(DAY_OF_MONTH);
      archiveBaseDir = CONFIG.fileArchivePath
              + archiveName;
      //archive data
      archiveFileDir = "";
      for (i = xnodeTempArrayList.size() - 1; i >= 0; i--)
      {
        archiveFileDir = archiveFileDir
                + getProperty("file.separator")
                + ((XincoCoreNodeServer) xnodeTempArrayList.get(i))
                        .getDesignation();
      }
      File archive = new File(archiveBaseDir + archiveFileDir);
      if (!archive.exists())
      {
        archive.mkdirs();
      }
      //copy file + revisions
      //build file list
      orgFileNames.add(("" + xdataTemp.getId()));
      orgFileIDs.add(xdataTemp.getId());
      for (i = 0; i
              < ((ArrayList) xdataTemp.getXincoCoreLogs()).size(); i++)
      {
        xlogTemp
                = ((XincoCoreLogServer) ((ArrayList) xdataTemp.getXincoCoreLogs()).get(i));
        if ((xlogTemp.getOpCode() == 1)
                || (xlogTemp.getOpCode() == 5))
        {
          orgFileNames.add(("" + xdataTemp.getId() + "-"
                  + xlogTemp.getId()));
          orgFileIDs.add(xdataTemp.getId());
        }
      }
      //copy + delete files
      for (k = 0; k < orgFileNames.size(); k++)
      {
        fileName = ((String) orgFileNames.get(k))
                + (xdataTemp.getXincoAddAttributes().isEmpty() ? "" : "_"
                + ((XincoAddAttribute) ((ArrayList) xdataTemp.getXincoAddAttributes()).get(0))
                        .getAttribVarchar());
        if ((new File(getXincoCoreDataPath(CONFIG.fileRepositoryPath,
                ((Integer) orgFileIDs.get(k)),
                ((String) orgFileNames.get(k))))).exists())
        {
          fcis = new FileInputStream(
                  new File(getXincoCoreDataPath(CONFIG.fileRepositoryPath,
                          ((Integer) orgFileIDs.get(k)),
                          ((String) orgFileNames.get(k)))));
          fcos = new FileOutputStream(new File(archiveBaseDir
                  + archiveFileDir
                  + getProperty("file.separator") + fileName));
          fcbuf = new byte[4_096];
          len = 0;
          while ((len = fcis.read(fcbuf)) != -1)
          {
            fcos.write(fcbuf, 0, len);
          }
          fcis.close();
          fcos.close();
          //delete
          (new File(getXincoCoreDataPath(CONFIG.fileRepositoryPath,
                  ((Integer) orgFileIDs.get(k)),
                  ((String) orgFileNames.get(k))))).delete();
        }
      }
      if (!xdataTemp.getXincoAddAttributes().isEmpty())
      {
        //update data + log
        xdataTemp.setStatusNumber(3);
        ((XincoAddAttribute) ((ArrayList) xdataTemp.getXincoAddAttributes()).get(7))
                .setAttribText(
                        "[" + archiveName + "]"
                        + archiveFileDir.replace('\\', '/') + "/" + fileName);
        xdataTemp.write2DB();
        if (((ArrayList) xdataTemp.getXincoCoreLogs()).size() > 0)
        {
          xlogTemp = ((XincoCoreLogServer) ((ArrayList) xdataTemp.getXincoCoreLogs()).get(
                  ((ArrayList) xdataTemp.getXincoCoreLogs()).size() - 1));
          if (xlogTemp != null)
          {
            xlogTemp.setOpCode(ARCHIVED.ordinal() + 1);
            xlogTemp.setOpDescription(rb.getString(getOPCode(
                    xlogTemp.getOpCode()).getName()));
            xlogTemp.setXincoCoreUserId(1);
            xlogTemp.write2DB();
            xdataTemp.getXincoCoreLogs().add(xlogTemp);
            xdataTemp.write2DB();
            result = true;
          }
          else
          {
            throw new XincoException(
                    "XincoCoreData (" + xdataTemp + ") had no logs!");
          }
        }
        else
        {
          throw new XincoException(
                  "Not the expected amount of log entries. "
                  + "At least should have " + CREATION.name()
                  + " log entry present");
        }
      }
    }
    catch (IOException e)
    {
      //Do nothing, false by default
    }
    return result;
  }
}
