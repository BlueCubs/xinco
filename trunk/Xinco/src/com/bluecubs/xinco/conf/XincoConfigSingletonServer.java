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
import com.bluecubs.xinco.core.server.XincoSettingServer;
import java.util.Vector;
import javax.naming.InitialContext;
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
    public int FileIndexerCount = 0;
    public Vector IndexFileTypesClass = null;
    public Vector IndexFileTypesExt = null;
    public String[] IndexNoIndex = null;
    public String JNDIDB = null;
    public int MaxSearchResult = 0;
    private static XincoConfigSingletonServer  instance = null;
    
    public static XincoConfigSingletonServer getInstance() {
        if (instance == null) {
            instance = new XincoConfigSingletonServer();
        }
        return instance;
    }
    
    //private constructor to avoid instance generation with new-operator!
    private XincoConfigSingletonServer() {
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
            if (!(FileRepositoryPath.substring(FileRepositoryPath.length()-1).equals(System.getProperty("file.separator")))) {
                FileRepositoryPath = FileRepositoryPath + System.getProperty("file.separator");
            }
            MaxSearchResult=xss.getSetting("xinco/MaxSearchResult").getInt_value();
            FileIndexPath=xss.getSetting("xinco/FileIndexPath").getString_value();
            if(FileIndexPath=="")
                FileIndexPath = FileRepositoryPath + "index";
            if (!(FileIndexPath.substring(FileIndexPath.length()-1).equals(System.getProperty("file.separator")))) {
                FileIndexPath += System.getProperty("file.separator");
            }
            FileArchivePath=xss.getSetting("xinco/FileArchivePath").getString_value();
            if (!(FileArchivePath.substring(FileArchivePath.length()-1).equals(System.getProperty("file.separator")))) {
                FileArchivePath += System.getProperty("file.separator");
            }
            FileArchivePeriod=xss.getSetting("xinco/FileArchivePeriod").getLong_value();
            Vector s=xss.getXinco_settings();
            for(int i=0;i<s.size();i++){
                if(((XincoSetting)s.get(i)).getDescription().startsWith("xinco/FileIndexer") &&
                        ((XincoSetting)s.get(i)).getDescription().endsWith("Class") ){
                    if(IndexFileTypesClass ==null)
                        IndexFileTypesClass = new Vector();
                    IndexFileTypesClass.add(((XincoSetting)s.get(i)).getString_value());
                }
                if(((XincoSetting)s.get(i)).getDescription().startsWith("xinco/FileIndexer") &&
                        ((XincoSetting)s.get(i)).getDescription().endsWith("Ext") ){
                    if(IndexFileTypesExt ==null)
                        IndexFileTypesExt = new Vector();
                    IndexFileTypesExt.add(((XincoSetting)s.get(i)).getString_value());
                }
            }
            IndexFileTypesExt.add(xss.getSetting("xinco/IndexNoIndex").getString_value());
        } catch (Exception ex) {
            ex.printStackTrace();
            //Default values
            FileRepositoryPath = "";
            FileIndexPath = "";
            FileArchivePath = "";
            FileArchivePeriod = 14400000;
            FileIndexerCount = 4;
            IndexFileTypesClass = new Vector();
            IndexFileTypesExt = new Vector();
            String[] tsa = null;
            IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF");
            tsa = null;
            tsa[0] = "pdf";
            IndexFileTypesExt.add(tsa);
            IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexMSWord");
            tsa = null;
            tsa[0] = "doc";
            IndexFileTypesExt.add(tsa);
            IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexMSExcel");
            tsa = null;
            tsa[0] = "xls";
            IndexFileTypesExt.add(tsa);
            IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexHTML");
            tsa = null;
            tsa[0] = "htm";
            tsa[1] = "html";
            tsa[2] = "php";
            tsa[3] = "jsp";
            IndexFileTypesExt.add(tsa);
            IndexNoIndex[0] = "";
            IndexNoIndex[1] = "com";
            IndexNoIndex[2] = "exe";
            JNDIDB = "java:comp/env/jdbc/XincoDB";
            MaxSearchResult = 30;
        }
    }
}
