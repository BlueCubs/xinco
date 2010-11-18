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

import javax.naming.InitialContext;
import java.util.Vector;

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
            guessLanguage=false;
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
            FileArchivePath = (String) (new InitialContext()).lookup("java:comp/env/xinco/FileArchivePath");
            if (!(FileArchivePath.substring(FileArchivePath.length() - 1).equals(System.getProperty("file.separator")))) {
                FileArchivePath = FileArchivePath + System.getProperty("file.separator");
            }
            FileArchivePeriod = ((Long) (new InitialContext()).lookup("java:comp/env/xinco/FileArchivePeriod")).longValue();

            FileIndexOptimizerPeriod = ((Long) (new InitialContext()).lookup("java:comp/env/xinco/FileIndexOptimizerPeriod")).longValue();

            FileIndexerCount = ((Integer) (new InitialContext()).lookup("java:comp/env/xinco/FileIndexerCount")).intValue();
            IndexFileTypesClass = new Vector();
            IndexFileTypesExt = new Vector();
            for (int i = 0; i < FileIndexerCount; i++) {
                IndexFileTypesClass.add((String) (new InitialContext()).lookup("java:comp/env/xinco/FileIndexer_" + (i + 1) + "_Class"));
                IndexFileTypesExt.add(((String) (new InitialContext()).lookup("java:comp/env/xinco/FileIndexer_" + (i + 1) + "_Ext")).split(";"));
            }
            IndexNoIndex = ((String) (new InitialContext()).lookup("java:comp/env/xinco/IndexNoIndex")).split(";");

            allowOutsideLinks = ((String) (new InitialContext()).lookup("java:comp/env/setting.allowoutsidelinks")).equals("True");
            guessLanguage = ((String) (new InitialContext()).lookup("java:comp/env/setting.guesslanguage")).equals("True");
            allowPublisherList = ((String) (new InitialContext()).lookup("java:comp/env/setting.allowpublisherlist")).equals("True");
            JNDIDB = (String) (new InitialContext()).lookup("java:comp/env/xinco/JNDIDB");
            MaxSearchResult = ((Integer) (new InitialContext()).lookup("java:comp/env/xinco/MaxSearchResult")).intValue();
        } catch (Exception e) {
            FileRepositoryPath = "";
            FileIndexPath = "";
            FileArchivePath = "";
            FileArchivePeriod = 14400000;
            FileIndexerCount = 4;
            IndexFileTypesClass = new Vector();
            IndexFileTypesExt = new Vector();
            String[] tsa = null;
            IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF");
            tsa = new String[1];
            tsa[0] = "pdf";
            IndexFileTypesExt.add(tsa);
            IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexMSWord");
            tsa = new String[1];
            tsa[0] = "doc";
            IndexFileTypesExt.add(tsa);
            IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexMSExcel");
            tsa = new String[1];
            tsa[0] = "xls";
            IndexFileTypesExt.add(tsa);
            IndexFileTypesClass.add("com.bluecubs.xinco.index.filetypes.XincoIndexHTML");
            tsa = new String[4];
            tsa[0] = "htm";
            tsa[1] = "html";
            tsa[2] = "php";
            tsa[3] = "jsp";
            IndexFileTypesExt.add(tsa);
            IndexNoIndex = new String[3];
            IndexNoIndex[0] = "";
            IndexNoIndex[1] = "com";
            IndexNoIndex[2] = "exe";
            allowOutsideLinks = true;
            allowPublisherList = true;
            guessLanguage = false;
            JNDIDB = "java:comp/env/jdbc/XincoDB";
            MaxSearchResult = 30;
            FileIndexOptimizerPeriod = 14400000;
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
