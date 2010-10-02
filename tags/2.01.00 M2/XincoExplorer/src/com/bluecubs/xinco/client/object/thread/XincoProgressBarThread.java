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
 * Name:            XincoProgressBarThread
 *
 * Description:     XincoProgressBarThread
 *
 * Original Author: Javier A. Ortiz
 * Date:            2007
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *
 *************************************************************
 * XincoProgressBarThread.java
 *
 * Created on January 22, 2007, 3:31 PM
 */
package com.bluecubs.xinco.client.object.thread;

import com.bluecubs.xinco.client.object.*;
import com.bluecubs.xinco.client.XincoExplorer;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoProgressBarThread extends Thread {

    private XincoExplorer explorer;
    private XincoProgressBar progressBar;
    private boolean initialized=false;

    @Override
    public void run() {
        progressBar = new XincoProgressBar(explorer);
        progressBar.pack();
        progressBar.setVisible(true);
        setInitialized(true);
    }

    /** Creates a new instance of XincoProgressBarThread */
    public XincoProgressBarThread(XincoExplorer e) {
        this.explorer = e;
    }

    public void hide() {
        progressBar.setVisible(false);
    }

    public void show() {
        progressBar.setVisible(true);
        progressBar.repaint();
    }

    public void setTitle(String t) {
        progressBar.setTitle(t);
    }

    public boolean isVisible() {
        return progressBar.isVisible();
    }

    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
