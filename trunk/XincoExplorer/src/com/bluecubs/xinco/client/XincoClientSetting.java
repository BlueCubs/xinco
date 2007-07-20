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
 * Name:            XincoClientSetting
 *
 * Description:     XincoClientSetting
 *
 * Original Author: Javier A. Ortiz
 * Date:            April 30, 2007, 5:07 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *
 *************************************************************
 */

package com.bluecubs.xinco.client;

import com.bluecubs.xinco.core.XincoSetting;

/**
 *
 * @author ortizbj
 */
public class XincoClientSetting extends XincoSetting{
    
    /** Creates a new instance of XincoClientSetting */
    public XincoClientSetting() {
    }
    
    private XincoSetting getSetting(int i){
        return (XincoSetting)getXinco_settings().get(i);
    }
    
    public XincoSetting getSetting(String s){
        for(int i=0;i<getXinco_settings().size();i++){
            if(((XincoSetting)getXinco_settings().get(i)).getDescription().equals(s))
                return (XincoSetting)getXinco_settings().get(i);
        }
        return null;
    }
}
