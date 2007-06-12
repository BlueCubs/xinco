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
 * Name:            XincoStepManager
 *
 * Description:     XincoStepManager
 *
 * Original Author: Javier Ortiz
 * Date:            May 21, 2007, 4:40 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

/**
 *
 * @author javydreamercsw
 */
public class XincoStepManager {
    private XincoWorkflowServer wfs=null;
    /** Creates a new instance of XincoStepManager */
    public XincoStepManager(XincoWorkflowServer wf) {
        setWorkflow(wf);
    }

    public XincoWorkflowServer getWorkflow() {
        return wfs;
    }

    public void setWorkflow(XincoWorkflowServer wf) {
        this.wfs = wf;
    }
    
}