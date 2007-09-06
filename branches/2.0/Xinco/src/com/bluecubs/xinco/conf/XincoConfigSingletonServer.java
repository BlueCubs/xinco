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

import javax.naming.InitialContext;
import java.util.Vector;

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
        private boolean allowOutsideLinks;
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
			setFileRepositoryPath((String)(new InitialContext()).lookup("java:comp/env/xinco/FileRepositoryPath"));
			if (!(getFileRepositoryPath().substring(getFileRepositoryPath().length()-1).equals(System.getProperty("file.separator")))) {
				setFileRepositoryPath(getFileRepositoryPath() + System.getProperty("file.separator"));
			}
			//optional: FileIndexPath
			try {
				setFileIndexPath((String)(new InitialContext()).lookup("java:comp/env/xinco/FileIndexPath"));
			} catch (Exception ce) {
				setFileIndexPath(getFileRepositoryPath() + "index");
			}
			if (!(getFileIndexPath().substring(getFileIndexPath().length()-1).equals(System.getProperty("file.separator")))) {
				setFileIndexPath(getFileIndexPath() + System.getProperty("file.separator"));
			}
			setFileArchivePath((String)(new InitialContext()).lookup("java:comp/env/xinco/FileArchivePath"));
			if (!(getFileArchivePath().substring(getFileArchivePath().length()-1).equals(System.getProperty("file.separator")))) {
				setFileArchivePath(getFileArchivePath() + System.getProperty("file.separator"));
			}
			setFileArchivePeriod(((Long)(new InitialContext()).lookup("java:comp/env/xinco/FileArchivePeriod")).longValue());
			
			setFileIndexerCount(((Integer)(new InitialContext()).lookup("java:comp/env/xinco/FileIndexerCount")).intValue());
			setIndexFileTypesClass(new Vector());
			setIndexFileTypesExt(new Vector());
			for (int i=0;i<getFileIndexerCount();i++) {
				getIndexFileTypesClass().add((String)(new InitialContext()).lookup("java:comp/env/xinco/FileIndexer_" + (i+1) + "_Class"));
				getIndexFileTypesExt().add(((String)(new InitialContext()).lookup("java:comp/env/xinco/FileIndexer_" + (i+1) + "_Ext")).split(";"));
			}
			setIndexNoIndex(((String)(new InitialContext()).lookup("java:comp/env/xinco/IndexNoIndex")).split(";"));
			
			setJNDIDB((String)(new InitialContext()).lookup("java:comp/env/xinco/JNDIDB"));
			setMaxSearchResult(((Integer)(new InitialContext()).lookup("java:comp/env/xinco/MaxSearchResult")).intValue());
                        
                        String temp =(String)(new InitialContext()).lookup("general.setting.allowoutsidelinks");
                        if(temp.equals("true"))
                            setAllowOutsideLinks(true);
                        else
                            setAllowOutsideLinks(false);
		} catch (Exception e) {
			setFileRepositoryPath("");
			setFileIndexPath("");
			setFileArchivePath("");
			setFileArchivePeriod(14400000);
			setFileIndexerCount(4);
                        setAllowOutsideLinks(false);
			setIndexFileTypesClass(new Vector());
			setIndexFileTypesExt(new Vector());
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
		}
	}

    public String getFileRepositoryPath() {
        return FileRepositoryPath;
    }

    public void setFileRepositoryPath(String FileRepositoryPath) {
        this.FileRepositoryPath = FileRepositoryPath;
    }

    public String getFileIndexPath() {
        return FileIndexPath;
    }

    public void setFileIndexPath(String FileIndexPath) {
        this.FileIndexPath = FileIndexPath;
    }

    public String getFileArchivePath() {
        return FileArchivePath;
    }

    public void setFileArchivePath(String FileArchivePath) {
        this.FileArchivePath = FileArchivePath;
    }

    public long getFileArchivePeriod() {
        return FileArchivePeriod;
    }

    public void setFileArchivePeriod(long FileArchivePeriod) {
        this.FileArchivePeriod = FileArchivePeriod;
    }

    public int getFileIndexerCount() {
        return FileIndexerCount;
    }

    public void setFileIndexerCount(int FileIndexerCount) {
        this.FileIndexerCount = FileIndexerCount;
    }

    public Vector getIndexFileTypesClass() {
        return IndexFileTypesClass;
    }

    public void setIndexFileTypesClass(Vector IndexFileTypesClass) {
        this.IndexFileTypesClass = IndexFileTypesClass;
    }

    public Vector getIndexFileTypesExt() {
        return IndexFileTypesExt;
    }

    public void setIndexFileTypesExt(Vector IndexFileTypesExt) {
        this.IndexFileTypesExt = IndexFileTypesExt;
    }

    public String[] getIndexNoIndex() {
        return IndexNoIndex;
    }

    public void setIndexNoIndex(String[] IndexNoIndex) {
        this.IndexNoIndex = IndexNoIndex;
    }

    public String getJNDIDB() {
        return JNDIDB;
    }

    public void setJNDIDB(String JNDIDB) {
        this.JNDIDB = JNDIDB;
    }

    public int getMaxSearchResult() {
        return MaxSearchResult;
    }

    public void setMaxSearchResult(int MaxSearchResult) {
        this.MaxSearchResult = MaxSearchResult;
    }

    public boolean isAllowOutsideLinks() {
        return allowOutsideLinks;
    }

    public void setAllowOutsideLinks(boolean allowOutsideLinks) {
        this.allowOutsideLinks = allowOutsideLinks;
    }

}
