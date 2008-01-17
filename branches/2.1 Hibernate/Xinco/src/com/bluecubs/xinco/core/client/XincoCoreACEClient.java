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

import com.bluecubs.xinco.core.XincoCoreACE;
import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreUser;
import java.util.Vector;

/**
 * XincoCoreACEClient
 * @author Alexander Manes
 */
public class XincoCoreACEClient extends XincoCoreACE {

    public XincoCoreACEClient() throws XincoException {
    }
    //check access by comparing user / user groups to ACL and return permissions
    public static XincoCoreACE checkAccess(XincoCoreUser attrU, Vector attrACL) {
        int i = 0;
        int j = 0;
        boolean match_ace = false;
        XincoCoreACE core_ace = new XincoCoreACE();
        try {
            for (i = 0; i < attrACL.size(); i++) {
                //reset match_ace
                match_ace = false;
                //check if user is mentioned in ACE
                if (((XincoCoreACE) attrACL.elementAt(i)).getXincoCoreUserId() == attrU.getId()) {
                    match_ace = true;
                }
                //check if group of user is mentioned in ACE
                if (!match_ace) {
                    for (j = 0; j < ((XincoCoreUserClient) attrU).getXincoCoreGroups().size(); j++) {
                        if (((XincoCoreACE) attrACL.elementAt(i)).getXincoCoreGroupId() ==
                                ((XincoCoreGroup) ((XincoCoreUserClient) attrU).getXincoCoreGroups().elementAt(j)).getId()) {
                            match_ace = true;
                            break;
                        }
                    }
                }
                //add to rights
                if (match_ace) {
                    //modify read permission
                    if (!core_ace.getReadPermission()) {
                        core_ace.setReadPermission(((XincoCoreACE) attrACL.elementAt(i)).getReadPermission());
                    }
                    //modify write permission
                    if (!core_ace.getWritePermission()) {
                        core_ace.setWritePermission(((XincoCoreACE) attrACL.elementAt(i)).getWritePermission());
                    }
                    //modify execute permission
                    if (!core_ace.getExecutePermission()) {
                        core_ace.setExecutePermission(((XincoCoreACE) attrACL.elementAt(i)).getExecutePermission());
                    }
                    //modify admin permission
                    if (!core_ace.getAdminPermission()) {
                        core_ace.setAdminPermission(((XincoCoreACE) attrACL.elementAt(i)).getAdminPermission());
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return core_ace;
    }
}
