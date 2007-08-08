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

import com.bluecubs.xinco.core.XincoSetting;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import java.util.Vector;
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
    private long FileArchivePeriod = 0;
    private int FileIndexerCount = 0;
    private Vector IndexFileTypesClass = null;
    private Vector IndexFileTypesExt = null;
    private String[] IndexNoIndex = null;
    private String JNDIDB = null;
    private int MaxSearchResult = 0;
    private static XincoConfigSingletonServer  instance = null;
    
    public static XincoConfigSingletonServer getInstance() {
        if (instance == null) {
            instance = new XincoConfigSingletonServer();
        }
        return instance;
    }
    
    //private constructor to avoid instance generation with new-operator!
    protected XincoConfigSingletonServer() {
        try {
            JNDIDB = (String)(new InitialContext()).lookup("java:comp/env/xinco/JNDIDB");
        } catch (NamingException ex) {
            JNDIDB = "java:comp/env/jdbc/XincoDB";
        }
    }
    
    public String getFileArchivePath() {
        return FileArchivePath;
    }
    
    public void init(XincoSettingServer xss){
        try{
            FileRepositoryPath=xss.getSetting("xinco/FileRepositoryPath").getString_value();
            if (!(getFileRepositoryPath().substring(getFileRepositoryPath().length()-1).equals(System.getProperty("file.separator")))) {
                FileRepositoryPath = getFileRepositoryPath() + System.getProperty("file.separator");
            }
            MaxSearchResult=xss.getSetting("xinco/MaxSearchResult").getInt_value();
            FileIndexPath=xss.getSetting("xinco/FileIndexPath").getString_value();
            if(getFileIndexPath()=="")
                FileIndexPath = getFileRepositoryPath() + "index";
            if (!(getFileIndexPath().substring(getFileIndexPath().length()-1).equals(System.getProperty("file.separator")))) {
                FileIndexPath += System.getProperty("file.separator");
            }
            FileArchivePath=xss.getSetting("xinco/FileArchivePath").getString_value();
            if (!(getFileArchivePath().substring(getFileArchivePath().length()-1).equals(System.getProperty("file.separator")))) {
                FileArchivePath += System.getProperty("file.separator");
            }
            FileArchivePeriod=xss.getSetting("xinco/FileArchivePeriod").getLong_value();
            Vector s=xss.getXinco_settings();
            for(int i=0;i<s.size();i++){
                if(((XincoSetting)s.get(i)).getDescription().startsWith("xinco/FileIndexer") &&
                        ((XincoSetting)s.get(i)).getDescription().endsWith("Class") ){
                    if(getIndexFileTypesClass() ==null)
                        IndexFileTypesClass = new Vector();
                    getIndexFileTypesClass().add(((XincoSetting)s.get(i)).getString_value());
                }
                if(((XincoSetting)s.get(i)).getDescription().startsWith("xinco/FileIndexer") &&
                        ((XincoSetting)s.get(i)).getDescription().endsWith("Ext") ){
                    if(getIndexFileTypesExt() ==null)
                        IndexFileTypesExt = new Vector();
                    getIndexFileTypesExt().add(((XincoSetting)s.get(i)).getString_value());
                }
            }
            getIndexFileTypesExt().add(xss.getSetting("xinco/IndexNoIndex").getString_value());
        } catch (Exception ex) {
            try {
                if(new XincoDBManager().getXincoSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Default values
            FileRepositoryPath = "";
            FileIndexPath = "";
            FileArchivePath = "";
            FileArchivePeriod = 14400000;
            FileIndexerCount = 4;
            IndexFileTypesClass = new Vector();
            IndexFileTypesExt = new Vector();
            String[] tsa = null;
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF");
            tsa = null;
            tsa[0] = "pdf";
            getIndexFileTypesExt().add(tsa);
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexMSWord");
            tsa = null;
            tsa[0] = "doc";
            getIndexFileTypesExt().add(tsa);
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexMSExcel");
            tsa = null;
            tsa[0] = "xls";
            getIndexFileTypesExt().add(tsa);
            getIndexFileTypesClass().add("com.bluecubs.xinco.index.filetypes.XincoIndexHTML");
            tsa = null;
            tsa[0] = "htm";
            tsa[1] = "html";
            tsa[2] = "php";
            tsa[3] = "jsp";
            getIndexFileTypesExt().add(tsa);
            getIndexNoIndex()[0] = "";
            getIndexNoIndex()[1] = "com";
            getIndexNoIndex()[2] = "exe";
            JNDIDB = "java:comp/env/jdbc/XincoDB";
            MaxSearchResult = 30;
        }
    }

    public String getFileRepositoryPath() {
        return FileRepositoryPath;
    }

    public String getFileIndexPath() {
        return FileIndexPath;
    }

    public long getFileArchivePeriod() {
        return FileArchivePeriod;
    }

    public int getFileIndexerCount() {
        return FileIndexerCount;
    }

    public Vector getIndexFileTypesClass() {
        return IndexFileTypesClass;
    }

    public Vector getIndexFileTypesExt() {
        return IndexFileTypesExt;
    }

    public String[] getIndexNoIndex() {
        return IndexNoIndex;
    }

    public String getJNDIDB() {
        return JNDIDB;
    }

    public int getMaxSearchResult() {
        return MaxSearchResult;
    }
}
