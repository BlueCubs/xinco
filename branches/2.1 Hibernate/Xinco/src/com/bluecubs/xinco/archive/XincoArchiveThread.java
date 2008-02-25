/**
 *Copyright 2005 blueCubs.com
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
 * Name:            XincoArchiveThread
 *
 * Description:     handle document indexing in thread 
 *
 * Original Author: Alexander Manes
 * Date:            2005/01/16
 *
 * Modifications:
 * 
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.archive;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.server.persistence.XincoPersistenceManager;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.math.BigInteger;

/**
 * This class runs document archiving in a separate thread
 * (only one archiving thread is allowed)
 */
public class XincoArchiveThread extends Thread {

    public static XincoArchiveThread instance = null;
    public Calendar firstRun = null;
    public Calendar lastRun = null;
    private static XincoConfigSingletonServer config = XincoConfigSingletonServer.getInstance();

    @Override
    @SuppressWarnings("static-access")
    public void run() {
        BigInteger archive_period = BigInteger.valueOf(14400000);
        firstRun = new GregorianCalendar();
        while (true) {
            try {
                archive_period = config.getFileArchivePeriod();
                //exit archiver if period = 0
                if (archive_period == BigInteger.valueOf(0)) {
                    break;
                }
                XincoArchiver.archiveData();
                lastRun = new GregorianCalendar();
            } catch (Throwable e) {
                //continue, wait and try again...
                archive_period = BigInteger.valueOf(14400000);
            }
            try {
                Thread.sleep(archive_period.longValue());
            } catch (Exception se) {
                break;
            }
        }
    }

    /**
     * Gets an archive thread instance. Creates one if not existent.
     * @return
     */
    public static XincoArchiveThread getInstance() {
        if (instance == null) {
            instance = new XincoArchiveThread();
        }
        return instance;
    }

    private XincoArchiveThread() {
    }
}