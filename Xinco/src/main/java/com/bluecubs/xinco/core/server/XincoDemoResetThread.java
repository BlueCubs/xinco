/*
 * Copyright 2012 blueCubs.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: XincoDemoResetThread
 * 
 * Description: Thread in charge of resseting the database (for demo purposes only)
 * 
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Dec 16, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.getDemoResetPeriod;
import static com.bluecubs.xinco.core.server.XincoDBManager.isDemo;
import static com.bluecubs.xinco.core.server.XincoDBManager.nativeQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.reload;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
public class XincoDemoResetThread extends Thread {

    public static XincoDemoResetThread instance = null;
    public long reset_period = 86_400_000; //Daily
    private static final Logger logger = getLogger(XincoDemoResetThread.class.getName());

    @Override
    public void run() {
        while (true) {
            reset_period = getDemoResetPeriod();
            if (reset_period > 0) {
                try {
                    sleep(reset_period);
                } catch (InterruptedException se) {
                    break;
                }
                //Check again this is a demo environment, just in case
                if (isDemo()) {
                    logger.warning("Dropping tables...");
                    nativeQuery("DROP ALL OBJECTS");
                    logger.warning("Done!");
                }
                //Reload the database
                logger.warning("Reloading DB...");
                reload(true);
                logger.warning("Done!");
            }
        }
    }

    public static XincoDemoResetThread getInstance() {
        if (instance == null) {
            instance = new XincoDemoResetThread();
        }
        return instance;
    }

    private XincoDemoResetThread() {
    }
}
