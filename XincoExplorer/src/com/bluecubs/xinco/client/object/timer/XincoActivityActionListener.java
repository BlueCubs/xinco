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
 * Name:            XincoActivityActionListener
 *
 * Description:     XincoActivityActionListener
 *
 * Original Author: Javier A. Ortiz
 * Date:            March 12, 2010, 11:05 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *
 *************************************************************
 */
package com.bluecubs.xinco.client.object.timer;

import com.bluecubs.xinco.client.XincoExplorer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoActivityActionListener implements ActionListener {

    private XincoExplorer explorer = null;
    private XincoActivityTimer xat = null;

    /** Creates a new instance of XincoActivityActionListener
     * @param e
     * @param xat 
     */
    public XincoActivityActionListener(XincoExplorer e, XincoActivityTimer xat) {
        this.explorer = e;
        this.xat = xat;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            this.explorer.setLock(true);
        } catch (Throwable ex) {
            Logger.getLogger(XincoActivityActionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
