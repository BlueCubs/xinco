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
 * Name:            XincoIndexThread
 *
 * Description:     handle document indexing in thread
 *
 * Original Author: Alexander Manes
 * Date:            2004/12/18
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.index;

import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import java.io.File;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class starts document indexing in a separate thread
 */
public class XincoIndexThread extends Thread {

    private Vector nodes = null;
    private boolean index_content = false,  index_result = false;
    private static boolean index_directory_deleted = false;
    private XincoDBManager DBM = null;
    private XincoCoreData d = null;

    /**
     * Run method
     */
    @Override
    public void run() {
        while (!nodes.isEmpty()) {
            try {
                DBM = new XincoDBManager();
                d = (XincoCoreData) nodes.firstElement();
                if (XincoIndexer.indexXincoCoreData(d, index_content, DBM)) {  
                    nodes.remove(0);
                } else {
                    Logger.getLogger(XincoIndexThread.class.getName()).log(Level.INFO, "Couldn't index " + d.getDesignation()+". Verify if there's a indexer for this file type.");
                }
                DBM.finalize();
            } catch (Throwable ex) {
                Logger.getLogger(XincoIndexThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Constructor
     *
     * @param xinco_core_data Data to index
     * @param index_content Should index content?
     */
    @SuppressWarnings("unchecked")
    public XincoIndexThread(XincoCoreData xinco_core_data, boolean index_content) {
        nodes = new Vector();
        nodes.add(xinco_core_data);
        this.index_content = index_content;
    }

    /**
     * Constructor
     */
    public XincoIndexThread() {
    }

    /**
     * Allows for adding a data element to the queue if there's a thread already running
     * @param xinco_core_data XincoCoreData
     */
    @SuppressWarnings("unchecked")
    public void addData(XincoCoreData xinco_core_data) {
        if (nodes == null) {
            nodes = new Vector();
        }
        nodes.add(xinco_core_data);
        if (!this.isAlive()) {
            start();
        }
    }

    /**
     * Sets the nodes vector of files to be indexed
     * @param n Vector containing de nodes to be indexed
     */
    public void setNodes(Vector n) {
        this.nodes = n;
    }

    /**
     * Get XIncoDBManager
     * @return
     */
    public XincoDBManager getXincoDBManager() {
        return DBM;
    }

    /**
     * Delete Lucene Index
     * @param DBM
     * @return boolean
     */
    public synchronized static boolean deleteIndex(XincoDBManager DBM) {
        try {
            DBM = new XincoDBManager();
            File indexDirectory = null;
            File indexDirectoryFile = null;
            String[] indexDirectoryFileList = null;
            indexDirectory = new File(DBM.config.getFileIndexPath());
            if (indexDirectory.exists()) {
                indexDirectoryFileList = indexDirectory.list();
                for (int i = 0; i < indexDirectoryFileList.length; i++) {
                    if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                        System.out.println("Deleting index file: " + DBM.config.getFileIndexPath() + indexDirectoryFileList[i]);
                    }
                    indexDirectoryFile = new File(DBM.config.getFileIndexPath() + indexDirectoryFileList[i]);
                    indexDirectoryFile.delete();
                }
                index_directory_deleted = indexDirectory.delete();
            }
        } catch (Exception ex) {
            Logger.getLogger(XincoIndexThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return index_directory_deleted;
    }

    /**
     * Get nodes
     * @return Vector
     */
    public Vector getNodes() {
        return nodes;
    }

    /**
     * Build Lucene Index
     * @param DBM
     * @return boolean
     */
    public synchronized boolean buildIndex(XincoDBManager DBM) {
        //select all data
        try {
            ResultSet rs = DBM.executeQuery("SELECT id FROM xinco_core_data ORDER BY designation");
            nodes = null;
            while (rs.next()) {
                addData(new XincoCoreDataServer(rs.getInt("id"), DBM));
            }
            if (!this.isAlive()) {
                start();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Rebuild Index
     * @return boolean
     */
    public synchronized boolean rebuildIndex() {
        boolean result = false;
        try {
            DBM = new XincoDBManager();
            deleteIndex(DBM);
            result = buildIndex(DBM);
            DBM.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(XincoIndexThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
