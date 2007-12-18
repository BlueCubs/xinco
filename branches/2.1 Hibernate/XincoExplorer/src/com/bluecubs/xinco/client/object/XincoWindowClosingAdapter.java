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
 * Name:            WindowClosingAdapter
 *
 * Description:     exits the program correctly 
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
package com.bluecubs.xinco.client.object;

import java.awt.event.*;

/**
 * 
 * @author Alexander Manes
 */
public class XincoWindowClosingAdapter
        extends WindowAdapter {

    private boolean exitSystem;

    /**
     * 
     * @param exitSystem
     */
    public XincoWindowClosingAdapter(boolean exitSystem) {
        this.exitSystem = exitSystem;
    }

    /**
     * 
     */
    public XincoWindowClosingAdapter() {
        this(false);
    }

    @Override
    public void windowClosing(WindowEvent event) {
        event.getWindow().setVisible(false);
        event.getWindow().dispose();
        if (exitSystem) {
            System.exit(0);
        }
    }
}
