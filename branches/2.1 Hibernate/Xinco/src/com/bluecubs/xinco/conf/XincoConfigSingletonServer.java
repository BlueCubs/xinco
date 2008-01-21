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
 * Name:            XincoConfigSingletonServer
 *
 * Description:     configuration class on server side
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
package com.bluecubs.xinco.conf;

import com.bluecubs.xinco.core.persistence.XincoSetting;
import com.bluecubs.xinco.core.server.persistence.XincoSettingServer;
import java.math.BigInteger;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This class handles the server configuration of xinco.
 * Edit values in context.xml
 */
public class XincoConfigSingletonServer {

    private String FileRepositoryPath = null;
    private String FileIndexPath = null;
    private String FileArchivePath = null;
    private BigInteger FileArchivePeriod;
    private int FileIndexerCount = 0;
    private Vector IndexFileTypesClass = null;
    private Vector IndexFileTypesExt = null;
    private String[] IndexNoIndex = null;
    private String JNDIDB = null;
    private int MaxSearchResult = 0;
    private boolean allowOutsideLinks;
    private static XincoConfigSingletonServer instance = null;

    /**
     * Gets a singleton instance. Creates one if needed.
     * @return
     */
    public static XincoConfigSingletonServer getInstance() {
        if (instance == null) {
            instance = new XincoConfigSingletonServer();
        }
        instance.init();
        return instance;
    }

    /**
     * private constructor to avoid instance generation with new-operator!
     */
    protected XincoConfigSingletonServer() {
        try {
            JNDIDB = (String) (new InitialContext()).lookup("xinco/JNDIDB");
        } catch (NamingException ex) {
            JNDIDB = "java:comp/env/jdbc/XincoDB";
        }
    }

    /**
     * Initializes this singleton from a XincoSettingServer object
     * @return True if initialization was successfull
     */
    @SuppressWarnings("unchecked")
    public boolean init() {
        try {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "Retrieving settings from Database...");
            }
            FileRepositoryPath = XincoSettingServer.getSetting("xinco/FileRepositoryPath").getStringValue();
            if (!(getFileRepositoryPath().substring(getFileRepositoryPath().length() - 1).equals(System.getProperty("file.separator")))) {
                FileRepositoryPath = getFileRepositoryPath() + System.getProperty("file.separator");
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "FileRepositoryPath: " + FileRepositoryPath);
            }
            MaxSearchResult = XincoSettingServer.getSetting("xinco/MaxSearchResult").getIntValue();
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "MaxSearchResult: " + MaxSearchResult);
            }
            FileIndexPath = XincoSettingServer.getSetting("xinco/FileIndexPath").getStringValue();
            if (getFileIndexPath().equals("")) {
                FileIndexPath = getFileRepositoryPath() + "index";
            }
            if (!(getFileIndexPath().substring(getFileIndexPath().length() - 1).equals(System.getProperty("file.separator")))) {
                FileIndexPath += System.getProperty("file.separator");
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "FileIndexPath: " + FileIndexPath);
            }
            FileArchivePath = XincoSettingServer.getSetting("xinco/FileArchivePath").getStringValue();
            if (!(getFileArchivePath().substring(getFileArchivePath().length() - 1).equals(System.getProperty("file.separator")))) {
                FileArchivePath += System.getProperty("file.separator");
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "FileArchivePath: " + FileArchivePath);
            }
            FileArchivePeriod = XincoSettingServer.getSetting("xinco/FileArchivePeriod").getLongValue();
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "FileArchivePeriod: " + FileArchivePeriod);
            }
            Vector s = new XincoSettingServer().getXincoSettings();
            StringTokenizer st;
            String[] temp;
            for (int i = 0; i < s.size(); i++) {
                if (((XincoSetting) s.get(i)).getDescription().startsWith("xinco/FileIndexer") &&
                        ((XincoSetting) s.get(i)).getDescription().endsWith("Class")) {
                    if (getIndexFileTypesClass() == null) {
                        IndexFileTypesClass = new Vector();
                    }
                    getIndexFileTypesClass().add(((XincoSetting) s.get(i)).getStringValue());
                    FileIndexerCount++;
                }
                if (((XincoSetting) s.get(i)).getDescription().startsWith("xinco/FileIndexer") &&
                        ((XincoSetting) s.get(i)).getDescription().endsWith("Ext")) {
                    if (getIndexFileTypesExt() == null) {
                        IndexFileTypesExt = new Vector();
                    }
                    getIndexFileTypesExt().add(((XincoSetting) s.get(i)).getStringValue());
                }
                if (((XincoSetting) s.get(i)).getDescription().startsWith("xinco/IndexNoIndex")) {
                    st = new StringTokenizer(((XincoSetting) s.get(i)).getStringValue(), ";");
                    temp = new String[st.countTokens()];
                    int j = 0;
                    while (st.hasMoreTokens()) {
                        temp[j] = st.nextToken();
                        j++;
                    }
                    setIndexNoIndex(temp);
                }
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "IndexFileTypesClass: " + IndexFileTypesClass);
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "IndexFileTypesExt: " + IndexFileTypesExt);
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "IndexNoIndex: " + IndexNoIndex);
            }
            setAllowOutsideLinks(XincoSettingServer.getSetting("setting.allowoutsidelinks").getBoolValue());
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, "AllowOutsideLinks: " + allowOutsideLinks);
            }
        } catch (Exception ex) {
            //Default values
            setFileRepositoryPath("");
            setFileIndexPath("");
            setFileArchivePath("");
            setFileArchivePeriod(BigInteger.valueOf(14400000));
            setFileIndexerCount(4);
            setAllowOutsideLinks(false);
            setIndexFileTypesClass(new Vector());
            setIndexFileTypesExt(new Vector());
            setIndexNoIndex(new String[3]);
            String[] tsa = new String[1];
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF");
            tsa[0] = "pdf";
            getIndexFileTypesExt().add(tsa);
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexMSWord");
            tsa[0] = "doc";
            getIndexFileTypesExt().add(tsa);
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexMSExcel");
            tsa[0] = "xls";
            getIndexFileTypesExt().add(tsa);
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexHTML");
            tsa = new String[4];
            tsa[0] = "htm";
            tsa[1] = "html";
            tsa[2] = "php";
            tsa[3] = "jsp";
            getIndexFileTypesExt().add(tsa);
            getIndexNoIndex()[0] = "";
            getIndexNoIndex()[1] = "com";
            getIndexNoIndex()[2] = "exe";
            setJNDIDB("java:comp/env/jdbc/XincoDB");
            setMaxSearchResult(30);
            return true;
        }
        return true;
    }

    /**
     * Get FileArchivePath
     * @return String
     */
    public String getFileArchivePath() {
        return FileArchivePath;
    }

    /**
     * Get FileRepositoryPath
     * @return String
     */
    public String getFileRepositoryPath() {
        return FileRepositoryPath;
    }

    /**
     * Set FileRepositoryPath
     * @param FileRepositoryPath
     */
    public void setFileRepositoryPath(String FileRepositoryPath) {
        this.FileRepositoryPath = FileRepositoryPath;
    }

    /**
     * Get FileIndexPath
     * @return String
     */
    public String getFileIndexPath() {
        return FileIndexPath;
    }

    /**
     * Set FileIndexPath
     * @param FileIndexPath
     */
    public void setFileIndexPath(String FileIndexPath) {
        this.FileIndexPath = FileIndexPath;
    }

    /**
     * Set FileArchivePath
     * @param FileArchivePath
     */
    public void setFileArchivePath(String FileArchivePath) {
        this.FileArchivePath = FileArchivePath;
    }

    /**
     * Get FileArchivePeriod
     * @return long
     */
    public BigInteger getFileArchivePeriod() {
        return FileArchivePeriod;
    }

    /**
     * Set FileArchivePeriod
     * @param FileArchivePeriod
     */
    public void setFileArchivePeriod(BigInteger FileArchivePeriod) {
        this.FileArchivePeriod = FileArchivePeriod;
    }

    /**
     * Get FileIndexerCount
     * @return int
     */
    public int getFileIndexerCount() {
        return FileIndexerCount;
    }

    /**
     * Set FileIndexerCount
     * @param FileIndexerCount
     */
    public void setFileIndexerCount(int FileIndexerCount) {
        this.FileIndexerCount = FileIndexerCount;
    }

    /**
     * Get IndexFileTypesClass
     * @return Vector
     */
    public Vector getIndexFileTypesClass() {
        return IndexFileTypesClass;
    }

    /**
     * Set IndexFileTypesClass
     * @param IndexFileTypesClass
     */
    public void setIndexFileTypesClass(Vector IndexFileTypesClass) {
        this.IndexFileTypesClass = IndexFileTypesClass;
    }

    /**
     * Get IndexFileTypesExt
     * @return Vector
     */
    public Vector getIndexFileTypesExt() {
        return IndexFileTypesExt;
    }

    /**
     * Set IndexFileTypesExt
     * @param IndexFileTypesExt
     */
    public void setIndexFileTypesExt(Vector IndexFileTypesExt) {
        this.IndexFileTypesExt = IndexFileTypesExt;
    }

    /**
     * Get IndexNoIndex
     * @return String[]
     */
    public String[] getIndexNoIndex() {
        return IndexNoIndex;
    }

    /**
     * Set IndexNoIndex
     * @param IndexNoIndex
     */
    public void setIndexNoIndex(String[] IndexNoIndex) {
        this.IndexNoIndex = IndexNoIndex;
    }

    /**
     * Get JNDIDB
     * @return String
     */
    public String getJNDIDB() {
        return JNDIDB;
    }

    /**
     * Set JNDIDB
     * @param JNDIDB
     */
    public void setJNDIDB(String JNDIDB) {
        this.JNDIDB = JNDIDB;
    }

    /**
     * Get MaxSearchResult
     * @return int
     */
    public int getMaxSearchResult() {
        return MaxSearchResult;
    }

    /**
     * Set MaxSearchResult
     * @param MaxSearchResult
     */
    public void setMaxSearchResult(int MaxSearchResult) {
        this.MaxSearchResult = MaxSearchResult;
    }

    /**
     * Is outside links allowed?
     * @return boolean
     */
    public boolean isAllowOutsideLinks() {
        return allowOutsideLinks;
    }

    /**
     * Set allowOutsideLinks
     * @param allowOutsideLinks
     */
    public void setAllowOutsideLinks(boolean allowOutsideLinks) {
        this.allowOutsideLinks = allowOutsideLinks;
    }
}
