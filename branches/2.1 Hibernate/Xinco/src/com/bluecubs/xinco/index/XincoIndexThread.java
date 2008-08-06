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

import com.bluecubs.xinco.core.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import com.dreamer.Hibernate.HibernateConfigurationParams;
import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class starts document indexing in a separate thread
 */
public class XincoIndexThread extends Thread {

    private XincoCoreDataServer d = null;
    private List result;
    private boolean index_content = false,  index_result = false;
    private static boolean index_directory_deleted = false;
    private Vector nodes= new Vector();

    @Override
    public void run() {
        XincoIndexer.indexXincoCoreData(d, index_content);
    }

    public XincoIndexThread(XincoCoreDataServer d, boolean index_content) {
        this.d = d;
        this.index_content = index_content;
    }



    /**
     * Delete Lucene Index
     * @return boolean
     */
    public synchronized static boolean deleteIndex() {
        try {
            File indexDirectory = null;
            File indexDirectoryFile = null;
            String[] indexDirectoryFileList = null;
            indexDirectory = new File(XincoDBManager.config.getFileIndexPath());
            if (indexDirectory.exists()) {
                indexDirectoryFileList = indexDirectory.list();
                for (int i = 0; i < indexDirectoryFileList.length; i++) {
                    if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                        System.out.println("Deleting index file: " + XincoDBManager.config.getFileIndexPath() + indexDirectoryFileList[i]);
                    }
                    indexDirectoryFile = new File(XincoDBManager.config.getFileIndexPath() + indexDirectoryFileList[i]);
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
     * @return boolean
     */
    public synchronized boolean buildIndex() {
        //select all data
        try {
            XincoDBManager pm = new XincoDBManager(HibernateConfigurationParams.CONTEXT);
            result = pm.executeQuery("SELECT x FROM XincoCoreData x ORDER BY x.designation");
            nodes = null;
            while (!result.isEmpty()) {
                addData((XincoCoreDataServer)result.get(0));
                result.remove(0);
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
     * Rebuild Index
     * @return boolean
     */
    public synchronized boolean rebuildIndex() {
        boolean r = false;
        try {
            deleteIndex();
            r = buildIndex();
        } catch (Throwable ex) {
            Logger.getLogger(XincoIndexThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
}
