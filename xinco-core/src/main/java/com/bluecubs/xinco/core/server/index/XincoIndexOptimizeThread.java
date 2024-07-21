/**
 * Copyright 2012 blueCubs.com
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>************************************************************ This project supports the
 * blueCubs vision of giving back to the community in exchange for free software! More information
 * on: http://www.bluecubs.org ************************************************************
 *
 * <p>Name: XincoIndexOptimizeThread
 *
 * <p>Description: handle optimizing in thread
 *
 * <p>Original Author: Alexander Manes Date: 2005/01/19
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.core.server.index;

import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import static com.bluecubs.xinco.core.server.index.XincoIndexer.optimizeIndex;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;

import java.util.Calendar;
import java.util.GregorianCalendar;

/** This class runs index optimizing in a separate thread (only one thread is allowed) */
public class XincoIndexOptimizeThread extends Thread {

  public static XincoIndexOptimizeThread instance = null;
  public Calendar firstRun = null;
  public Calendar lastRun = null;
  public long index_period = 604_800_000; // Weekly

  @Override
  @SuppressWarnings("static-access")
  public void run() {
    firstRun = new GregorianCalendar();
    while (true) {
      try {
        index_period = CONFIG.getFileIndexOptimizerPeriod();
        // exit indexer if period = 0
        if (index_period > 0) {
          optimizeIndex();
          lastRun = new GregorianCalendar();
        }
      } catch (Exception e) {
        getLogger(XincoIndexOptimizeThread.class.getSimpleName()).log(WARNING, null, e);
      }
      try {
        sleep(index_period);
      } catch (InterruptedException se) {
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

  private XincoIndexOptimizeThread() {}
}
