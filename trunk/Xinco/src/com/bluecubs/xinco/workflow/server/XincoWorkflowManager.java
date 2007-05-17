/**
 *Copyright 2007 blueCubs.com
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
 * Name:            XincoWorkflowManagerServer
 *
 * Description:     Xinco Workflow Manager Server
 *
 * Original Author: Javier Ortiz
 * Date:            May 2, 2007, 9:54 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.XincoDBManager;

/**
 *
 * @author ortizbj
 */
public class XincoWorkflowManager {
    private XincoDBManager DBM= null;
    /**
     * Creates a new instance of XincoWorkflowManager
     */
    public XincoWorkflowManager() {
        try {
            DBM=new XincoDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
