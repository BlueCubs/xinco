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
 * Name: XincoConfigSingletonServer
 *
 * Description: configuration class on server side
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.XincoSetting;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;

/**
 * This class handles the server configuration of xinco. Edit values in database
 */
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
    private boolean allowOutsideLinks = true, allowPublisherList = true,
            guessLanguage = false;
    private static XincoConfigSingletonServer instance = null;
    private boolean loadDefault;
    private static final Logger logger = Logger.getLogger(XincoConfigSingletonServer.class.getSimpleName());

    public static XincoConfigSingletonServer getInstance() {
        if (instance == null) {
            instance = new XincoConfigSingletonServer();
        }
        return instance;
    }

    //private constructor to avoid instance generation with new-operator!
    @SuppressWarnings("unchecked")
    private XincoConfigSingletonServer() {
        try {
            JNDIDB = (String) (new InitialContext()).lookup("java:comp/env/xinco/JNDIDB");
        } catch (Exception e) {
            JNDIDB = "XincoPUJNDI";
        }
    }

    public void loadSettings() {
        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        try {
            fileRepositoryPath = XincoSettingServer.getSetting("xinco/FileRepositoryPath").getStringValue();
            if (!fileRepositoryPath.isEmpty()
                    && !(fileRepositoryPath.substring(fileRepositoryPath.length() - 1).equals(System.getProperty("file.separator")))) {
                fileRepositoryPath += System.getProperty("file.separator");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            fileRepositoryPath = "";
            exceptions.add(e);
            loadDefault = true;
        }
        //optional: fileIndexPath
        try {
            fileIndexPath = XincoSettingServer.getSetting("xinco/FileIndexPath").getStringValue();
        } catch (Exception e) {
            fileIndexPath = fileRepositoryPath + "index";
        }
        if (!(fileIndexPath.substring(fileIndexPath.length() - 1).equals(System.getProperty("file.separator")))) {
            fileIndexPath += System.getProperty("file.separator");
        }
        try {
            fileArchivePath = XincoSettingServer.getSetting("xinco/FileArchivePath").getStringValue();
            if (!fileArchivePath.isEmpty()
                    && !(fileArchivePath.substring(fileArchivePath.length() - 1).equals(System.getProperty("file.separator")))) {
                fileArchivePath += System.getProperty("file.separator");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            fileArchivePath = "";
            exceptions.add(e);
            loadDefault = true;
        }
        try {
            fileArchivePeriod = XincoSettingServer.getSetting("xinco/FileArchivePeriod").getLongValue();
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            fileArchivePeriod = 14400000;
            exceptions.add(e);
            loadDefault = true;
        }

        try {
            fileIndexOptimizerPeriod = XincoSettingServer.getSetting("xinco/FileIndexOptimizerPeriod").getLongValue();
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            fileIndexOptimizerPeriod = 14400000;
            exceptions.add(e);
            loadDefault = true;
        }

        try {
            fileIndexerCount = ((Long) XincoDBManager.createdQuery("select count(s) from XincoSetting s where s.description like 'xinco/FileIndexer_%_Class'").get(0));
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            fileIndexerCount = 4;
            exceptions.add(e);
            loadDefault = true;
        }
        indexFileTypesClass = new ArrayList();
        indexFileTypesExt = new ArrayList();
        try {
            for (int i = 0; i < getFileIndexerCount(); i++) {
                getIndexFileTypesClass().add(XincoSettingServer.getSetting("xinco/FileIndexer_" + (i + 1) + "_Class").getStringValue());
                getIndexFileTypesExt().add(XincoSettingServer.getSetting("xinco/FileIndexer_" + (i + 1) + "_Ext").getStringValue().split(";"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            String[] tsa;
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF");
            tsa = new String[1];
            tsa[0] = "pdf";
            getIndexFileTypesExt().add(tsa);
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexMSWord");
            tsa = new String[1];
            tsa[0] = "doc";
            getIndexFileTypesExt().add(tsa);
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexMSExcel");
            tsa = new String[1];
            tsa[0] = "xls";
            getIndexFileTypesExt().add(tsa);
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexHTML");
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
            indexNoIndex = XincoSettingServer.getSetting("xinco/IndexNoIndex").getStringValue().split(";");
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            indexNoIndex = new String[3];
            indexNoIndex[0] = "";
            indexNoIndex[1] = "com";
            indexNoIndex[2] = "exe";
            exceptions.add(e);
            loadDefault = true;
        }
        try {
            allowOutsideLinks = XincoSettingServer.getSetting("setting.allowoutsidelinks").isBoolValue();
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            allowOutsideLinks = true;
            exceptions.add(e);
            loadDefault = true;
        }
        try {
            allowPublisherList = XincoSettingServer.getSetting("setting.allowpublisherlist").isBoolValue();
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            allowPublisherList = true;
            exceptions.add(e);
            loadDefault = true;
        }
        try {
            guessLanguage = XincoSettingServer.getSetting("setting.guessLanguage").isBoolValue();
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            guessLanguage = false;
            exceptions.add(e);
            loadDefault = true;
        }
        try {
            maxSearchResult = XincoSettingServer.getSetting("xinco/MaxSearchResult").getIntValue();
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            maxSearchResult = 30;
            exceptions.add(e);
            loadDefault = true;
        }
        try {
            OOPort = XincoSettingServer.getSetting("setting.OOPort").getIntValue();
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            OOPort = 8100;
            exceptions.add(e);
            loadDefault = true;
        }

        if (loadDefault) {
            StringBuilder sb = new StringBuilder("Error loading configuration! Using default value for the ones not found.");
            for (Exception ex : exceptions) {
                sb.append("\n").append(ex.getLocalizedMessage());
            }
            logger.log(Level.SEVERE, sb.toString());
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

    /**
     * @return the guessLanguage
     */
    public boolean isGuessLanguage() {
        return guessLanguage;
    }

    /**
     * @return the OOPort
     */
    public int getOOPort() {
        return OOPort;
    }

    /**
     * @return the fileIndexerCount
     */
    public long getFileIndexerCount() {
        return fileIndexerCount;
    }

    /**
     * @return the indexFileTypesClass
     */
    public ArrayList getIndexFileTypesClass() {
        return indexFileTypesClass;
    }

    /**
     * @return the indexFileTypesExt
     */
    public ArrayList getIndexFileTypesExt() {
        return indexFileTypesExt;
    }

    /**
     * @return the indexNoIndex
     */
    public String[] getIndexNoIndex() {
        return indexNoIndex;
    }

    /**
     * @return the JNDIDB
     */
    public String getJNDIDB() {
        return JNDIDB;
    }

    /**
     * @return the maxSearchResult
     */
    public int getMaxSearchResult() {
        return maxSearchResult;
    }

    private void showSettings() {
        for (Object o : XincoDBManager.namedQuery("XincoSetting.findAll")) {
            XincoSetting setting = (XincoSetting) o;
            logger.log(Level.FINE, "-------------{0}-------------",
                    setting.getDescription());
            logger.log(Level.FINE, "id: {0}", setting.getId());
            logger.log(Level.FINE, "int: {0}", setting.getIntValue());
            logger.log(Level.FINE, "long: {0}", setting.getLongValue());
            logger.log(Level.FINE, "string: {0}", setting.getStringValue());
            logger.log(Level.FINE, "string: {0}", setting.getBoolValue());
            logger.fine("--------------------------");
        }
    }
}
