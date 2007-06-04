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
import com.bluecubs.xinco.workflow.XincoWorkflowStep;

/**
 *
 * @author ortizbj
 */
public class XincoWorkflowManager {
    private XincoWorkflowServer workflow=null;
    private XincoWorkflowStepServer current=null;
    private int currentStepNumber=0;
    private boolean isFork=false;
    /**
     * Creates a new instance of XincoWorkflowManager
     */
    public XincoWorkflowManager(XincoDBManager DBM) {
    }

    public XincoWorkflowServer getWorkflow() {
        return workflow;
    }

    public void setWorkflow(XincoWorkflowServer workflow) {
        this.workflow = workflow;
        //Set current step as the first step
        if(getCurrent()==null){
            setCurrent((XincoWorkflowStepServer)this.getWorkflow().getXinco_workflow_steps().elementAt(0));
        }
        if(getCurrent().getFork()!=null)
            setIsFork(true);
    }
    
    public XincoWorkflowStep nextStep(){
        if(isFork()){
            
        }
        else
            this.setCurrentStepNumber(getCurrentStepNumber()+1);
        setCurrent((XincoWorkflowStepServer)this.getWorkflow().getXinco_workflow_steps().elementAt(this.getCurrentStepNumber()));
        return null;
    }

    public XincoWorkflowStepServer getCurrent() {
        return current;
    }

    public void setCurrent(XincoWorkflowStepServer current) {
        this.current = current;
    }

    public boolean isFork() {
        setIsFork(!(current.getFork()==null));
        return isFork;
    }

    public void setIsFork(boolean isFork) {
        this.isFork = isFork;
    }
    
    private void processFork(){
        
    }

    public int getCurrentStepNumber() {
        return currentStepNumber;
    }

    public void setCurrentStepNumber(int currentStepNumber) {
        this.currentStepNumber = currentStepNumber;
    }
}
