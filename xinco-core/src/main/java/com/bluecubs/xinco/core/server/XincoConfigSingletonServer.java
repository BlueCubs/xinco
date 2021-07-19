/**
 * Copyright 2012 blueCubs.com
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>************************************************************ This project supports the
 * blueCubs vision of giving back to the community in exchange for free software! More information
 * on: http://www.bluecubs.org ************************************************************
 *
 * <p>Name: XincoConfigSingletonServer
 *
 * <p>Description: configuration class on server side
 *
 * <p>Original Author: Alexander Manes Date: 2004
 *
 * <p>$Author$: $Date$:
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import static com.bluecubs.xinco.core.server.XincoSettingServer.getSetting;
import static com.bluecubs.xinco.core.server.index.XincoIndexer.optimizeIndex;
import static java.lang.System.getProperty;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;

import com.bluecubs.xinco.core.server.persistence.XincoSetting;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/** This class handles the server configuration of xinco. Edit values in database */
public final class XincoConfigSingletonServer {

  public String fileRepositoryPath = null;
  public String fileIndexPath = null;
  public String fileArchivePath = null;
  public long fileArchivePeriod = 0;
  private long fileIndexOptimizerPeriod = 0;
  private long fileIndexerCount = 0;
  private ArrayList indexFileTypesClass = null;
  private ArrayList indexFileTypesExt = null;
  private String[] indexNoIndex = null;
  private String JNDIDB = null;
  private int maxSearchResult = 0;
  private int OOPort = 0;
  private boolean allowOutsideLinks = true, allowPublisherList = true, guessLanguage = false;
  private static XincoConfigSingletonServer instance = null;
  private boolean loadDefault;
  private static final Logger LOG = getLogger(XincoConfigSingletonServer.class.getSimpleName());

  public static XincoConfigSingletonServer getInstance() {
    if (instance == null) {
      instance = new XincoConfigSingletonServer();
    }
    return instance;
  }

  // private constructor to avoid instance generation with new-operator!
  @SuppressWarnings("unchecked")
  private XincoConfigSingletonServer() {
    try {
      JNDIDB = (String) (new InitialContext()).lookup("java:comp/env/xinco/JNDIDB");
    } catch (NamingException e) {
      JNDIDB = "XincoPUJNDI";
    }
  }

  private String formatPath(String p) {
    if (!p.isEmpty() && !(p.substring(p.length() - 1).equals(getProperty("file.separator")))) {
      p += getProperty("file.separator");
    }
    return p;
  }

  public void loadSettings() {
    ArrayList<Exception> exceptions = new ArrayList<>();
    File temp;
    try {
      fileRepositoryPath = getSetting("xinco/FileRepositoryPath").getStringValue();
      if (fileRepositoryPath == null) {
        if (IS_OS_WINDOWS) {
          fileRepositoryPath = "C:" + getProperty("file.separator") + "Temp";
        } else {
          fileRepositoryPath = getProperty("file.separator") + "tmp";
        }
        fileRepositoryPath +=
            getProperty("file.separator")
                + "xinco"
                + getProperty("file.separator")
                + "file_repository";
        fileRepositoryPath = formatPath(fileRepositoryPath);
        temp = new File(fileRepositoryPath);
        LOG.log(INFO, "Using {0} as repository!", temp.getAbsolutePath());
        temp.mkdirs();
      }
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      fileRepositoryPath = "";
      exceptions.add(e);
      loadDefault = true;
    }
    // optional: fileIndexPath
    try {
      fileIndexPath = getSetting("xinco/FileIndexPath").getStringValue();
      if (fileIndexPath == null) {
        fileIndexPath = fileRepositoryPath + "index";
      }
    } catch (Exception e) {
      fileIndexPath = fileRepositoryPath + "index";
    }
    fileIndexPath = formatPath(fileIndexPath);
    temp = new File(fileIndexPath);
    LOG.log(INFO, "Using {0} as index!", temp.getAbsolutePath());
    temp.mkdirs();
    // Initialize the index
    optimizeIndex();
    try {
      fileArchivePath = getSetting("xinco/FileArchivePath").getStringValue();
      if (fileArchivePath == null) {

        fileArchivePath = fileRepositoryPath + "archive";
      }
      fileArchivePath = formatPath(fileArchivePath);
      temp = new File(fileArchivePath);
      LOG.log(INFO, "Using {0} as archieve!", temp.getAbsolutePath());
      temp.mkdirs();
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      fileArchivePath = "";
      exceptions.add(e);
      loadDefault = true;
    }
    try {
      fileArchivePeriod = getSetting("xinco/FileArchivePeriod").getLongValue();
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      fileArchivePeriod = 14_400_000;
      exceptions.add(e);
      loadDefault = true;
    }

    try {
      fileIndexOptimizerPeriod = getSetting("xinco/FileIndexOptimizerPeriod").getLongValue();
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      fileIndexOptimizerPeriod = 14_400_000;
      exceptions.add(e);
      loadDefault = true;
    }

    try {
      fileIndexerCount =
          ((Long)
              createdQuery(
                      "select count(s) from "
                          + "XincoSetting s where s.description like "
                          + "'FileIndexer_%_Class'")
                  .get(0));
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      fileIndexerCount = 4;
      exceptions.add(e);
      loadDefault = true;
    }
    indexFileTypesClass = new ArrayList();
    indexFileTypesExt = new ArrayList();
    try {
      for (int i = 0; i < getFileIndexerCount(); i++) {
        getIndexFileTypesClass()
            .add(getSetting("FileIndexer_" + (i + 1) + "_Class").getStringValue());
        getIndexFileTypesExt()
            .add(getSetting("FileIndexer_" + (i + 1) + "_Ext").getStringValue().split(";"));
      }
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      String[] tsa;
      getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes." + "XincoIndexAdobePDF");
      tsa = new String[1];
      tsa[0] = "pdf";
      getIndexFileTypesExt().add(tsa);
      getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes." + "XincoIndexMSWord");
      tsa = new String[1];
      tsa[0] = "doc";
      getIndexFileTypesExt().add(tsa);
      getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes." + "XincoIndexMSExcel");
      tsa = new String[1];
      tsa[0] = "xls";
      getIndexFileTypesExt().add(tsa);
      getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes." + "XincoIndexHTML");
      tsa = new String[4];
      tsa[0] = "htm";
      tsa[1] = "html";
      tsa[2] = "php";
      tsa[3] = "jsp";
      getIndexFileTypesExt().add(tsa);
      exceptions.add(e);
      loadDefault = true;
    }
    try {
      indexNoIndex = getSetting("xinco/IndexNoIndex").getStringValue().split(";");
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      indexNoIndex = new String[3];
      indexNoIndex[0] = "";
      indexNoIndex[1] = "com";
      indexNoIndex[2] = "exe";
      exceptions.add(e);
      loadDefault = true;
    }
    try {
      allowOutsideLinks = getSetting("setting.allowoutsidelinks").isBoolValue();
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      allowOutsideLinks = true;
      exceptions.add(e);
      loadDefault = true;
    }
    try {
      allowPublisherList = getSetting("setting.allowpublisherlist").isBoolValue();
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      allowPublisherList = true;
      exceptions.add(e);
      loadDefault = true;
    }
    try {
      guessLanguage = getSetting("setting.guessLanguage").isBoolValue();
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      guessLanguage = false;
      exceptions.add(e);
      loadDefault = true;
    }
    try {
      maxSearchResult = getSetting("xinco/MaxSearchResult").getIntValue();
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      maxSearchResult = 30;
      exceptions.add(e);
      loadDefault = true;
    }
    try {
      OOPort = getSetting("setting.OOPort").getIntValue();
    } catch (Exception e) {
      LOG.log(SEVERE, null, e);
      OOPort = 8_100;
      exceptions.add(e);
      loadDefault = true;
    }

    if (loadDefault) {
      StringBuilder sb =
          new StringBuilder(
              "Error loading configuration!" + " Using default value for the ones not found.");
      exceptions.forEach(
          (ex) -> {
            sb.append("\n").append(ex.getLocalizedMessage());
          });
      LOG.log(SEVERE, sb.toString());
    }
    showSettings();
  }

  public boolean isAllowOutsideLinks() {
    return allowOutsideLinks;
  }

  public long getFileIndexOptimizerPeriod() {
    return fileIndexOptimizerPeriod;
  }

  public void setFileIndexOptimizerPeriod(long FileIndexOptimizerPeriod) {
    this.fileIndexOptimizerPeriod = FileIndexOptimizerPeriod;
  }

  public boolean isAllowPublisherList() {
    return allowPublisherList;
  }

  /** @return the guessLanguage */
  public boolean isGuessLanguage() {
    return guessLanguage;
  }

  /** @return the OOPort */
  public int getOOPort() {
    return OOPort;
  }

  /** @return the fileIndexerCount */
  public long getFileIndexerCount() {
    return fileIndexerCount;
  }

  /** @return the indexFileTypesClass */
  public ArrayList getIndexFileTypesClass() {
    return indexFileTypesClass;
  }

  /** @return the indexFileTypesExt */
  public ArrayList getIndexFileTypesExt() {
    return indexFileTypesExt;
  }

  /** @return the indexNoIndex */
  public String[] getIndexNoIndex() {
    return indexNoIndex;
  }

  /** @return the JNDIDB */
  public String getJNDIDB() {
    return JNDIDB;
  }

  /** @return the maxSearchResult */
  public int getMaxSearchResult() {
    return maxSearchResult;
  }

  private void showSettings() {
    for (Object o : namedQuery("XincoSetting.findAll")) {
      XincoSetting setting = (XincoSetting) o;
      LOG.log(FINE, "-------------{0}-------------", setting.getDescription());
      LOG.log(FINE, "id: {0}", setting.getId());
      LOG.log(FINE, "int: {0}", setting.getIntValue());
      LOG.log(FINE, "long: {0}", setting.getLongValue());
      LOG.log(FINE, "string: {0}", setting.getStringValue());
      LOG.log(FINE, "string: {0}", setting.getBoolValue());
      LOG.fine("--------------------------");
    }
  }
}
