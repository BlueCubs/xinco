/**
 *Copyright 2011 blueCubs.com
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
 * Name:            XincoIndexOptimizeThread
 *
 * Description:     handle optimizing in thread 
 *
 * Original Author: Alexander Manes
 * Date:            2005/01/19
 *
 * Modifications:
 * 
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.index;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bluecubs.xinco.core.server.XincoDBManager;

/**
 * This class runs index optimizing in a separate thread
 * (only one thread is allowed)
 */
public class XincoIndexOptimizeThread extends Thread {

    public static XincoIndexOptimizeThread instance = null;
    public Calendar firstRun = null;
    public Calendar lastRun = null;
    public long index_period = 604800000; //Weekly

    @Override
    @SuppressWarnings("static-access")
    public void run() {
        firstRun = new GregorianCalendar();
        while (true) {
            try {
                XincoDBManager DBM = null;
                DBM = XincoDBManager.get();
                index_period = DBM.config.getFileIndexOptimizerPeriod();
                //exit indexer if period = 0
                if (index_period == 0) {
                    break;
                }
                XincoIndexer.optimizeIndex();
                lastRun = new GregorianCalendar();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sleep(index_period);
            } catch (Exception se) {
                break;
            }
        }
    }

    public static XincoIndexOptimizeThread getInstance() {
        if (instance == null) {
            instance = new XincoIndexOptimizeThread();
        }
        return instance;
    }

    private XincoIndexOptimizeThread() {
    }
}
