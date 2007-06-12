/**
 *Copyright June 11, 2007 blueCubs.com
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
 * Name:            XincoWorkflowConfigSingletonServer.java
 *
 * Description:     XincoWorkflowConfigSingletonServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 11, 2007, 4:28 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.conf;

import com.bluecubs.xinco.workflow.server.WorkflowSettingServer;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class XincoWorkflowConfigSingletonServer{
    private static XincoWorkflowConfigSingletonServer  instance = null;
    private String JNDIDB = null;
    /** Creates a new instance of XincoWorkflowConfigSingletonServer */
    protected XincoWorkflowConfigSingletonServer() {
        try {
            JNDIDB = (String)(new InitialContext()).lookup("java:comp/env/xincoWorkflow/JNDIDB");
        } catch (NamingException ex) {
            JNDIDB = "java:comp/env/jdbc/XincoWorkflowDB";
        }
    }
    
    public static XincoWorkflowConfigSingletonServer getInstance() {
        if (instance == null) {
            instance = new XincoWorkflowConfigSingletonServer();
        }
        return instance;
    }
    
    public void init(WorkflowSettingServer xss){
//        try{
//            
//        }
    }
    
     public String getJNDIDB() {
        return JNDIDB;
    }
}