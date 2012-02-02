/**
 *Copyright 2011 blueCubs.com
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
 * Name:            XincoCoreACEClient
 *
 * Description:     access control entry 
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
package com.bluecubs.xinco.core.client;

import com.bluecubs.xinco.client.service.XincoCoreACE;
import com.bluecubs.xinco.client.service.XincoCoreGroup;
import com.bluecubs.xinco.client.service.XincoCoreUser;
import com.bluecubs.xinco.core.XincoException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XincoCoreACEClient extends XincoCoreACE {

    public XincoCoreACEClient() throws XincoException {
    }

    //check access by comparing user / user groups to ACL and return permissions
    public static XincoCoreACE checkAccess(XincoCoreUser attrU, List attrACL) {

        int i = 0;
        int j = 0;
        boolean match_ace = false;
        XincoCoreACE core_ace = new XincoCoreACE();

        try {
            for (i = 0; i < attrACL.size(); i++) {
                //reset match_ace
                match_ace = false;
                //check if user is mentioned in ACE
                if (((XincoCoreACE) attrACL.get(i)).getXincoCoreUserId() == attrU.getId()) {
                    match_ace = true;
                }
                //check if group of user is mentioned in ACE
                if (!match_ace) {
                    for (j = 0; j < attrU.getXincoCoreGroups().size(); j++) {
                        if (((XincoCoreACE) attrACL.get(i)).getXincoCoreGroupId() == ((XincoCoreGroup) attrU.getXincoCoreGroups().get(j)).getId()) {
                            match_ace = true;
                            break;
                        }
                    }
                }
                //add to rights
                if (match_ace) {
                    //modify read permission
                    if (!core_ace.isReadPermission()) {
                        core_ace.setReadPermission(((XincoCoreACE) attrACL.get(i)).isReadPermission());
                    }
                    //modify write permission
                    if (!core_ace.isWritePermission()) {
                        core_ace.setWritePermission(((XincoCoreACE) attrACL.get(i)).isWritePermission());
                    }
                    //modify execute permission
                    if (!core_ace.isExecutePermission()) {
                        core_ace.setExecutePermission(((XincoCoreACE) attrACL.get(i)).isExecutePermission());
                    }
                    //modify admin permission
                    if (!core_ace.isAdminPermission()) {
                        core_ace.setAdminPermission(((XincoCoreACE) attrACL.get(i)).isAdminPermission());
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(XincoCoreACEClient.class.getSimpleName()).log(Level.SEVERE, null, e);
        }
        return core_ace;
    }
}
