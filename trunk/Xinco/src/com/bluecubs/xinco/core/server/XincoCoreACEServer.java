/**
 *Copyright 2009 blueCubs.com
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
 * Name:            XincoCoreACEServer
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
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoCoreACE;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreUser;
import java.util.Vector;

import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreAceJpaController;
import java.util.HashMap;
import java.util.List;

public class XincoCoreACEServer extends XincoCoreACE {

    private int userID = 1;
    private HashMap parameters = new HashMap();
    private static List result;
    //create single ace object for data structures

    public XincoCoreACEServer(int attrID) throws XincoException {

        try {
            XincoCoreAceJpaController controller = new XincoCoreAceJpaController();
            com.bluecubs.xinco.core.server.persistence.XincoCoreAce ace = controller.findXincoCoreAce(attrID);
            if (controller.findXincoCoreAce(attrID) != null) {
                setId(ace.getId());
                setXinco_core_user_id(ace.getXincoCoreUserId().getId());
                setXinco_core_group_id(ace.getXincoCoreGroupId().getId());
                setXinco_core_node_id(ace.getXincoCoreNodeId().getId());
                setXinco_core_data_id(ace.getXincoCoreDataId().getId());
                setRead_permission(ace.getReadPermission());
                setWrite_permission(ace.getWritePermission());
                setExecute_permission(ace.getExecutePermission());
                setAdmin_permission(ace.getAdminPermission());
            } else {
                throw new XincoException();
            }

        } catch (Exception e) {
            throw new XincoException();
        }

    }

    public XincoCoreACEServer(com.bluecubs.xinco.core.server.persistence.XincoCoreAce ace) {
        setId(ace.getId());
        if (ace.getXincoCoreUserId() != null) {
            setXinco_core_user_id(ace.getXincoCoreUserId().getId());
        }
        if (ace.getXincoCoreGroupId() != null) {
            setXinco_core_group_id(ace.getXincoCoreGroupId().getId());
        }
        if (ace.getXincoCoreNodeId() != null) {
            setXinco_core_node_id(ace.getXincoCoreNodeId().getId());
        }
        if (ace.getXincoCoreDataId() != null) {
            setXinco_core_data_id(ace.getXincoCoreDataId().getId());
        }
        setRead_permission(ace.getReadPermission());
        setWrite_permission(ace.getWritePermission());
        setExecute_permission(ace.getExecutePermission());
        setAdmin_permission(ace.getAdminPermission());
    }

    //create single ace object for data structures
    public XincoCoreACEServer(int attrID, int attrUID, int attrGID, int attrNID, int attrDID, boolean attrRP, boolean attrWP, boolean attrEP, boolean attrAP) throws XincoException {

        setId(attrID);
        setXinco_core_user_id(attrUID);
        setXinco_core_group_id(attrGID);
        setXinco_core_node_id(attrNID);
        setXinco_core_data_id(attrDID);
        setRead_permission(attrRP);
        setWrite_permission(attrWP);
        setExecute_permission(attrEP);
        setAdmin_permission(attrAP);

    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            com.bluecubs.xinco.core.server.persistence.XincoCoreAce ace;
            boolean create = false;
            if (getId() > 0) {
                parameters.clear();
                parameters.put("id", getId());
                result = XincoDBManager.namedQuery("XincoCoreAce.findById", parameters);
                ace = (com.bluecubs.xinco.core.server.persistence.XincoCoreAce) result.get(0);
            } else {
                setId(XincoDBManager.getNewID("xinco_core_ace"));
                ace = new com.bluecubs.xinco.core.server.persistence.XincoCoreAce(getId());
                create = true;
            }
            if (getXinco_core_user_id() != 0) {
                ace.setXincoCoreUserId(new com.bluecubs.xinco.core.server.persistence.XincoCoreUser(getXinco_core_user_id()));
            }
            if (getXinco_core_group_id() != 0) {
                ace.setXincoCoreGroupId(new com.bluecubs.xinco.core.server.persistence.XincoCoreGroup(getXinco_core_group_id()));
            }
            if (getXinco_core_node_id() != 0) {
                ace.setXincoCoreNodeId(new com.bluecubs.xinco.core.server.persistence.XincoCoreNode(getXinco_core_node_id()));
            }
            if (getXinco_core_data_id() != 0) {
                ace.setXincoCoreDataId(new com.bluecubs.xinco.core.server.persistence.XincoCoreData(getXinco_core_data_id()));
            }
            ace.setReadPermission(isRead_permission());
            ace.setWritePermission(isWrite_permission());
            ace.setExecutePermission(isExecute_permission());
            ace.setAdminPermission(isAdmin_permission());
            if (create) {
                new XincoCoreAceJpaController().create(ace);
            } else {
                new XincoCoreAceJpaController().edit(ace);
            }
        } catch (Exception e) {
            throw new XincoException(e.getMessage());
        }

        return getId();

    }

    //remove from db
    public static int removeFromDB(XincoCoreACE attrCACE, int userID) throws XincoException {
        try {
            XincoCoreAceJpaController controller = new XincoCoreAceJpaController();
            controller.destroy(attrCACE.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException(e.getMessage());
        }
        return 0;
    }

    //create complete ACL for node or data
    public static Vector getXincoCoreACL(int attrID, String attrT) {
        Vector core_acl = new Vector();
        try {
            result = XincoDBManager.createdQuery("SELECT xca FROM XincoCoreAce xca WHERE xca." + attrT
                    + "=" + attrID + "");
            //TODO: Uncomment when bug is fixed
            /**
             * This is an eclipselink bug (https://bugs.eclipse.org/bugs/show_bug.cgi?id=294092)
             * Leave commented while its fixed.
             */
            // ORDER BY xca.xincoCoreUserId.id, xca.xincoCoreGroupId.id, "
            //                    + "xca.xincoCoreNodeId.id, xca.xincoCoreDataId.id
            for (Object o : result) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreAce ace =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreAce) o;
                core_acl.addElement(new XincoCoreACEServer(ace));
            }
        } catch (Exception e) {
            e.printStackTrace();
            core_acl.removeAllElements();
        }
        return core_acl;
    }

    //check access by comparing user / user groups to ACL and return permissions
    public static XincoCoreACE checkAccess(XincoCoreUser attrU, Vector attrACL) {

        int i = 0;
        int j = 0;
        boolean match_ace = false;
        XincoCoreACE core_ace = new XincoCoreACE();

        for (i = 0; i < attrACL.size(); i++) {
            //reset match_ace
            match_ace = false;
            //check if user is mentioned in ACE
            if (((XincoCoreACE) attrACL.elementAt(i)).getXinco_core_user_id() == attrU.getId()) {
                match_ace = true;
            }
            //check if group of user is mentioned in ACE
            if (!match_ace) {
                for (j = 0; j < attrU.getXinco_core_groups().size(); j++) {
                    if (((XincoCoreACE) attrACL.elementAt(i)).getXinco_core_group_id() == ((XincoCoreGroup) attrU.getXinco_core_groups().elementAt(j)).getId()) {
                        match_ace = true;
                        break;
                    }
                }
            }
            //add to rights
            if (match_ace) {
                //modify read permission
                if (!core_ace.isRead_permission()) {
                    core_ace.setRead_permission(((XincoCoreACE) attrACL.elementAt(i)).isRead_permission());
                }
                //modify write permission
                if (!core_ace.isWrite_permission()) {
                    core_ace.setWrite_permission(((XincoCoreACE) attrACL.elementAt(i)).isWrite_permission());
                }
                //modify execute permission
                if (!core_ace.isExecute_permission()) {
                    core_ace.setExecute_permission(((XincoCoreACE) attrACL.elementAt(i)).isExecute_permission());
                }
                //modify admin permission
                if (!core_ace.isAdmin_permission()) {
                    core_ace.setAdmin_permission(((XincoCoreACE) attrACL.elementAt(i)).isAdmin_permission());
                }
            }
        }
        return core_ace;
    }

    public void setUserId(int i) {
        this.userID = i;
    }
}
