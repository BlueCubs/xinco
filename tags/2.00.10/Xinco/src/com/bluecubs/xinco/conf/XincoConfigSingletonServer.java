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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import java.util.Vector;
import javax.naming.NamingException;

/**
 * This class handles the server configuration of xinco.
 * Edit values in context.xml
 */
public class XincoConfigSingletonServer {

    public String FileRepositoryPath = null;
    public String FileIndexPath = null;
    public String FileArchivePath = null;
    public long FileArchivePeriod = 0;
    private long FileIndexOptimizerPeriod = 0;
    public int FileIndexerCount = 0;
    public Vector IndexFileTypesClass = null;
    public Vector IndexFileTypesExt = null;
    public String[] IndexNoIndex = null;
    public String JNDIDB = null;
    public int MaxSearchResult = 0;
    private boolean allowOutsideLinks = true, allowPublisherList = true,
            guessLanguage = false;
    private static XincoConfigSingletonServer instance = null;

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
            FileRepositoryPath = (String) (new InitialContext()).lookup("java:comp/env/xinco/FileRepositoryPath");
        } catch (NamingException ex) {
            FileRepositoryPath = "";
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!(FileRepositoryPath.substring(FileRepositoryPath.length() - 1).equals(System.getProperty("file.separator")))) {
            FileRepositoryPath = FileRepositoryPath + System.getProperty("file.separator");
        }
        //optional: FileIndexPath
        try {
            FileIndexPath = (String) (new InitialContext()).lookup("java:comp/env/xinco/FileIndexPath");
        } catch (Exception ce) {
            FileIndexPath = FileRepositoryPath + "index";
        }
        if (!(FileIndexPath.substring(FileIndexPath.length() - 1).equals(System.getProperty("file.separator")))) {
            FileIndexPath = FileIndexPath + System.getProperty("file.separator");
        }
        try {
            FileArchivePath = (String) (new InitialContext()).lookup("java:comp/env/xinco/FileArchivePath");
        } catch (NamingException ex) {
            FileArchivePath = "";
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!(FileArchivePath.substring(FileArchivePath.length() - 1).equals(System.getProperty("file.separator")))) {
            FileArchivePath = FileArchivePath + System.getProperty("file.separator");
        }
        try {
            FileArchivePeriod = ((Long) (new InitialContext()).lookup("java:comp/env/xinco/FileArchivePeriod")).longValue();
        } catch (NamingException ex) {
            FileArchivePeriod = 14400000;
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FileIndexOptimizerPeriod = ((Long) (new InitialContext()).lookup("java:comp/env/xinco/FileIndexOptimizerPeriod")).longValue();
        } catch (NamingException ex) {
            FileIndexOptimizerPeriod = 14400000;
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FileIndexerCount = ((Integer) (new InitialContext()).lookup("java:comp/env/xinco/FileIndexerCount")).intValue();
        } catch (NamingException ex) {
            FileIndexerCount = 4;
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        IndexFileTypesClass = new Vector();
        IndexFileTypesExt = new Vector();
        for (int i = 0; i < FileIndexerCount; i++) {
            try {
                IndexFileTypesClass.add((String) (new InitialContext()).lookup("java:comp/env/xinco/FileIndexer_" + (i + 1) + "_Class"));
            } catch (NamingException ex) {
                IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF");
                IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexMSWord");
                IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexMSExcel");
                IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexHTML");
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                IndexFileTypesExt.add(((String) (new InitialContext()).lookup("java:comp/env/xinco/FileIndexer_" + (i + 1) + "_Ext")).split(";"));
            } catch (NamingException ex) {
                String[] tsa = null;
                tsa = new String[1];
                tsa[0] = "pdf";
                IndexFileTypesExt.add(tsa); 
                tsa = new String[1];
                tsa[0] = "doc";
                IndexFileTypesExt.add(tsa);
                tsa = new String[1];
                tsa[0] = "xls";
                IndexFileTypesExt.add(tsa);
                tsa = new String[4];
                tsa[0] = "htm";
                tsa[1] = "html";
                tsa[2] = "php";
                tsa[3] = "jsp";
                IndexFileTypesExt.add(tsa);
                Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            IndexNoIndex = ((String) (new InitialContext()).lookup("java:comp/env/xinco/IndexNoIndex")).split(";");
        } catch (NamingException ex) {
            IndexNoIndex = new String[3];
        IndexNoIndex[0] = "";
        IndexNoIndex[1] = "com";
        IndexNoIndex[2] = "exe";
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            allowOutsideLinks = ((String) (new InitialContext()).lookup("java:comp/env/setting.allowoutsidelinks")).equals("True");
        } catch (NamingException ex) {
            allowOutsideLinks = true;
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            guessLanguage = ((String) (new InitialContext()).lookup("java:comp/env/setting.guessLanguage")).equals("True");
        } catch (NamingException ex) {
            guessLanguage = false;
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            allowPublisherList = ((String) (new InitialContext()).lookup("java:comp/env/setting.allowpublisherlist")).equals("True");
        } catch (NamingException ex) {
            allowPublisherList = true;
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            JNDIDB = (String) (new InitialContext()).lookup("java:comp/env/xinco/JNDIDB");
        } catch (NamingException ex) {
            JNDIDB = "java:comp/env/jdbc/XincoDB";
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            MaxSearchResult = ((Integer) (new InitialContext()).lookup("java:comp/env/xinco/MaxSearchResult")).intValue();
        } catch (NamingException ex) {
            MaxSearchResult = 30;
            Logger.getLogger(XincoConfigSingletonServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isAllowOutsideLinks() {
        return allowOutsideLinks;
    }

    public long getFileIndexOptimizerPeriod() {
        return FileIndexOptimizerPeriod;
    }

    public void setFileIndexOptimizerPeriod(long FileIndexOptimizerPeriod) {
        this.FileIndexOptimizerPeriod = FileIndexOptimizerPeriod;
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
}
