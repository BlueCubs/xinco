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

/**
 * This class runs index optimizing in a separate thread
 * (only one thread is allowed)
 */
public class XincoIndexOptimizeThread extends Thread {

    public static XincoIndexOptimizeThread instance = null;
    public Calendar firstRun = null;
    public Calendar lastRun = null;

    @Override
    public void run() {
        firstRun = new GregorianCalendar();
        while (true) {
            try {
                XincoIndexer.optimizeIndex();
                lastRun = new GregorianCalendar();
            } catch (Throwable e) {
            //continue, wait and try again...
            }
            try {
                Thread.sleep(14400000); //4 hours
            } catch (Exception se) {
                break;
            }
        }
    }

    /**
     * Get thread instance
     * @return
     */
    public static XincoIndexOptimizeThread getInstance() {
        if (instance == null) {
            instance = new XincoIndexOptimizeThread();
        }
        return instance;
    }

    private XincoIndexOptimizeThread() {
    }
}
