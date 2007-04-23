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

import java.sql.ResultSet;
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
    
    public void init(ResultSet rs){
        try {
            while(rs.next()){
                if(rs.getString("description").equals("xinco/FileRepositoryPath")){
                    FileRepositoryPath=rs.getString("string_value");
                    if (!(FileRepositoryPath.substring(FileRepositoryPath.length()-1).equals(System.getProperty("file.separator")))) {
                        FileRepositoryPath = FileRepositoryPath + System.getProperty("file.separator");
                    }
                    if(rs.getString("description").equals("xinco/MaxSearchResult"))
                        MaxSearchResult=rs.getInt("int_value");
                    if(rs.getString("description").equals("xinco/FileIndexPath")){
                        FileIndexPath=rs.getString("string_value");
                        if(FileIndexPath=="")
                            FileIndexPath = FileRepositoryPath + "index";
                        if (!(FileIndexPath.substring(FileIndexPath.length()-1).equals(System.getProperty("file.separator")))) {
                            FileIndexPath += System.getProperty("file.separator");
                        }
                    }
                    if(rs.getString("description").equals("xinco/FileArchivePath")){
                        FileArchivePath=rs.getString("string_value");
                        if (!(FileArchivePath.substring(FileArchivePath.length()-1).equals(System.getProperty("file.separator")))) {
                            FileArchivePath += System.getProperty("file.separator");
                        }
                    }
                    if(rs.getString("description").equals("xinco/FileArchivePeriod"))
                        FileArchivePeriod=rs.getLong("long_value");
                    if(rs.getString("description").startsWith("xinco/FileIndexer") &&
                            rs.getString("description").endsWith("Class") ){
                        if(IndexFileTypesClass ==null)
                            IndexFileTypesClass = new Vector();
                        IndexFileTypesClass.add(rs.getString("string_value"));
                    }
                    if(rs.getString("description").startsWith("xinco/FileIndexer") &&
                            rs.getString("description").endsWith("Ext") ){
                        if(IndexFileTypesClass ==null)
                            IndexFileTypesClass = new Vector();
                        IndexFileTypesExt.add(rs.getString("string_value"));
                    }
                    if(rs.getString("description").equals("xinco/IndexNoIndex"))
                        IndexFileTypesExt.add(rs.getString("string_value"));
                }
            }
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
