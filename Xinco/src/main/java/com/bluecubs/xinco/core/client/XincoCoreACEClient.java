/**
 * Copyright 2010 blueCubs.com
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>************************************************************ This project supports the
 * blueCubs vision of giving back to the community in exchange for free software! More information
 * on: http://www.bluecubs.org ************************************************************
 *
 * <p>Name: XincoCoreACEClient
 *
 * <p>Description: access control entry
 *
 * <p>Original Author: Alexander Manes Date: 2004
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.core.client;

import com.bluecubs.xinco.core.XincoCoreACE;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoException;
import java.util.Vector;

public class XincoCoreACEClient extends XincoCoreACE {

  public XincoCoreACEClient() throws XincoException {}

  // check access by comparing user / user groups to ACL and return permissions
  public static XincoCoreACE checkAccess(XincoCoreUser attrU, Vector attrACL) {

    int i = 0;
    int j = 0;
    boolean match_ace = false;
    XincoCoreACE core_ace = new XincoCoreACE();

    try {
      for (i = 0; i < attrACL.size(); i++) {
        // reset match_ace
        match_ace = false;
        // check if user is mentioned in ACE
        if (((XincoCoreACE) attrACL.elementAt(i)).getXinco_core_user_id() == attrU.getId()) {
          match_ace = true;
        }
        // check if group of user is mentioned in ACE
        if (!match_ace) {
          for (j = 0; j < attrU.getXinco_core_groups().size(); j++) {
            if (((XincoCoreACE) attrACL.elementAt(i)).getXinco_core_group_id()
                == ((XincoCoreGroup) attrU.getXinco_core_groups().elementAt(j)).getId()) {
              match_ace = true;
              break;
            }
          }
        }
        // add to rights
        if (match_ace) {
          // modify read permission
          if (!core_ace.isRead_permission()) {
            core_ace.setRead_permission(((XincoCoreACE) attrACL.elementAt(i)).isRead_permission());
          }
          // modify write permission
          if (!core_ace.isWrite_permission()) {
            core_ace.setWrite_permission(
                ((XincoCoreACE) attrACL.elementAt(i)).isWrite_permission());
          }
          // modify execute permission
          if (!core_ace.isExecute_permission()) {
            core_ace.setExecute_permission(
                ((XincoCoreACE) attrACL.elementAt(i)).isExecute_permission());
          }
          // modify admin permission
          if (!core_ace.isAdmin_permission()) {
            core_ace.setAdmin_permission(
                ((XincoCoreACE) attrACL.elementAt(i)).isAdmin_permission());
          }
        }
      }
    } catch (Exception e) {
    }

    return core_ace;
  }
}
