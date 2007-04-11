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
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * This class starts document indexing in a separate thread
 */
public class XincoIndexThread extends Thread {
    private Vector nodes=null;
    private boolean index_content = false, index_directory_deleted = false,index_result = false;
    private XincoDBManager dbm = null;
    /**
     * Run method
     */
    public void run() {
        while(nodes.size()>0){
            XincoIndexer.indexXincoCoreData(((XincoCoreData)nodes.firstElement()), index_content, dbm);
            nodes.remove(0);
            try {
                sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                try {
                    dbm.con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            dbm.con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    
    /**
     * Constructor
     * @param index_content Should index content?
     * @param dbm XincoDatabaseManger
     */
    public XincoIndexThread(boolean index_content, XincoDBManager dbm) {
        this.index_content = index_content;
        this.dbm = dbm;
    }
    
    /**
     * Constructor
     */
    public XincoIndexThread() {
    }
    
    /**
     * Allows for adding a data element to the queue if there's a thread already running
     * @param d XincoCoreData
     */
    public void addData(XincoCoreData d){
        if(nodes==null)
            nodes = new Vector();
        nodes.add(d);
    }
    
    /**
     * Sets the nodes vector of files to be indexed
     * @param n Vector containing de nodes to be indexed
     */
    public void setNodes(Vector n){
        this.nodes=n;
    }
    
    public XincoDBManager getDbm() {
        return dbm;
    }
    
    public synchronized void deleteIndex(XincoDBManager dbm){
        File indexDirectory = null;
        File indexDirectoryFile = null;
        String[] indexDirectoryFileList = null;
        indexDirectory = new File(dbm.config.FileIndexPath);
        if (indexDirectory.exists()) {
            indexDirectoryFileList = indexDirectory.list();
            for (int i=0;i<indexDirectoryFileList.length;i++) {
                indexDirectoryFile = new File(dbm.config.FileIndexPath + indexDirectoryFileList[i]);
                indexDirectoryFile.delete();
            }
            index_directory_deleted=indexDirectory.delete();
        }
    }
    
    public Vector getNodes() {
        return nodes;
    }
    
    public synchronized boolean buildIndex(XincoDBManager dbm){
        //select all data
        XincoCoreDataServer xdata_temp = null;
        try {
            Statement stmt = dbm.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM xinco_core_data ORDER BY designation");
            while (rs.next()) {
                xdata_temp = new XincoCoreDataServer(rs.getInt("id"), dbm);
                index_result = XincoIndexer.indexXincoCoreData(xdata_temp, true, dbm);
            }
            stmt.close();
        } catch (XincoException ex) {
            ex.printStackTrace();
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public synchronized boolean rebuildIndex(XincoDBManager dbm){
        deleteIndex(dbm);
        return buildIndex(dbm);
    }
    
    public boolean isIndex_directory_deleted() {
        return index_directory_deleted;
    }
    
    public boolean isIndex_result() {
        return index_result;
    }
}
