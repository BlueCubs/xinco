/**
 * Copyright 2012 blueCubs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 *
 * Name: XincoCoreACEServer
 *
 * Description: access control entry
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreAceJpaController;
import com.bluecubs.xinco.core.server.service.XincoCoreACE;
import com.bluecubs.xinco.core.server.service.XincoCoreGroup;
import com.bluecubs.xinco.core.server.service.XincoCoreUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

public class XincoCoreACEServer extends XincoCoreACE {

    private HashMap parameters = new HashMap();
    private static List result;
    private static final Logger LOG =
            getLogger(XincoCoreACEServer.class.getSimpleName());
    //create single ace object for data structures

    public XincoCoreACEServer(int attrID) throws XincoException {
        XincoCoreAceJpaController controller = new XincoCoreAceJpaController(getEntityManagerFactory());
        com.bluecubs.xinco.core.server.persistence.XincoCoreAce ace = controller.findXincoCoreAce(attrID);
        if (ace != null) {
            setId(ace.getId());
            if (ace.getXincoCoreUser() != null) {
                setXincoCoreUserId(ace.getXincoCoreUser().getId());
            }
            if (ace.getXincoCoreGroup() != null) {
                setXincoCoreGroupId(ace.getXincoCoreGroup().getId());
            }
            if (ace.getXincoCoreNode() != null) {
                setXincoCoreNodeId(ace.getXincoCoreNode().getId());
            }
            if (ace.getXincoCoreData() != null) {
                setXincoCoreDataId(ace.getXincoCoreData().getId());
            }
            setReadPermission(ace.getReadPermission());
            setWritePermission(ace.getWritePermission());
            setExecutePermission(ace.getExecutePermission());
            setAdminPermission(ace.getAdminPermission());
        } else {
            throw new XincoException("Unable to find ACE with id: " + attrID);
        }
    }

    public XincoCoreACEServer(com.bluecubs.xinco.core.server.persistence.XincoCoreAce ace) {
        setId(ace.getId());
        if (ace.getXincoCoreUser() != null) {
            setXincoCoreUserId(ace.getXincoCoreUser().getId());
        }
        if (ace.getXincoCoreGroup() != null) {
            setXincoCoreGroupId(ace.getXincoCoreGroup().getId());
        }
        if (ace.getXincoCoreNode() != null) {
            setXincoCoreNodeId(ace.getXincoCoreNode().getId());
        }
        if (ace.getXincoCoreData() != null) {
            setXincoCoreDataId(ace.getXincoCoreData().getId());
        }
        setReadPermission(ace.getReadPermission());
        setWritePermission(ace.getWritePermission());
        setExecutePermission(ace.getExecutePermission());
        setAdminPermission(ace.getAdminPermission());
    }

    //create single ace object for data structures
    public XincoCoreACEServer(int attrID, int attrUID, int attrGID, int attrNID, int attrDID, boolean attrRP, boolean attrWP, boolean attrEP, boolean attrAP) throws XincoException {

        setId(attrID);
        setXincoCoreUserId(attrUID);
        setXincoCoreGroupId(attrGID);
        setXincoCoreNodeId(attrNID);
        setXincoCoreDataId(attrDID);
        setReadPermission(attrRP);
        setWritePermission(attrWP);
        setExecutePermission(attrEP);
        setAdminPermission(attrAP);

    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            com.bluecubs.xinco.core.server.persistence.XincoCoreAce ace;
            boolean create = false;
            if (getId() > 0) {
                parameters.clear();
                parameters.put("id", getId());
                result = namedQuery("XincoCoreAce.findById", parameters);
                ace = (com.bluecubs.xinco.core.server.persistence.XincoCoreAce) result.get(0);
            } else {
                ace = new com.bluecubs.xinco.core.server.persistence.XincoCoreAce(getId());
                create = true;
            }
            if (getXincoCoreUserId() != 0) {
                ace.setXincoCoreUser(new com.bluecubs.xinco.core.server.persistence.XincoCoreUser(getXincoCoreUserId()));
            }
            if (getXincoCoreGroupId() != 0) {
                ace.setXincoCoreGroup(new com.bluecubs.xinco.core.server.persistence.XincoCoreGroup(getXincoCoreGroupId()));
            }
            if (getXincoCoreNodeId() != 0) {
                ace.setXincoCoreNode(new com.bluecubs.xinco.core.server.persistence.XincoCoreNode(getXincoCoreNodeId()));
            }
            if (getXincoCoreDataId() != 0) {
                ace.setXincoCoreData(new com.bluecubs.xinco.core.server.persistence.XincoCoreData(getXincoCoreDataId()));
            }
            ace.setReadPermission(isReadPermission());
            ace.setWritePermission(isWritePermission());
            ace.setExecutePermission(isExecutePermission());
            ace.setAdminPermission(isAdminPermission());
            if (create) {
                new XincoCoreAceJpaController(getEntityManagerFactory()).create(ace);
            } else {
                new XincoCoreAceJpaController(getEntityManagerFactory()).edit(ace);
            }
            setId(ace.getId());
        } catch (Exception e) {
            getLogger(XincoCoreACEServer.class.getSimpleName()).log(SEVERE, null, e);
            throw new XincoException(e.getMessage());
        }

        return getId();

    }

    //remove from db
    public static int removeFromDB(XincoCoreACE attrCACE, int userID) throws XincoException {
        try {
            XincoCoreAceJpaController controller = new XincoCoreAceJpaController(getEntityManagerFactory());
            controller.destroy(attrCACE.getId());
        } catch (Exception ex) {
            getLogger(XincoCoreACEServer.class.getSimpleName()).log(SEVERE, null, ex);
            throw new XincoException(ex.getMessage());
        }
        return 0;
    }

    //create complete ACL for node or data
    public static ArrayList<XincoCoreACEServer> getXincoCoreACL(int attrID, String attrT) {
        ArrayList<XincoCoreACEServer> core_acl = new ArrayList<>();
        try {
            result = createdQuery("SELECT xca FROM XincoCoreAce xca WHERE xca." + attrT
                    + "=" + attrID + "");
            //TODO: Uncomment when bug is fixed
            /**
             * This is an eclipselink bug
             * (https://bugs.eclipse.org/bugs/show_bug.cgi?id=294092) Leave
             * commented while its fixed.
             */
            // ORDER BY xca.xincoCoreUserId.id, xca.xincoCoreGroupId.id, "
            //                    + "xca.xincoCoreNodeId.id, xca.xincoCoreDataId.id
            for (Object o : result) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreAce ace =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreAce) o;
                core_acl.add(new XincoCoreACEServer(ace));
            }
        } catch (Exception ex) {
            getLogger(XincoCoreACEServer.class.getSimpleName()).log(SEVERE, null, ex);
            core_acl.clear();
        }
        return core_acl;
    }

    //check access by comparing user / user groups to ACL and return permissions
    public static XincoCoreACE checkAccess(XincoCoreUser attrU, List attrACL) {

        int i;
        int j;
        boolean match_ace;
        XincoCoreACE core_ace = new XincoCoreACE();

        for (i = 0; i < attrACL.size(); i++) {
            //reset match_ace
            match_ace = false;
            //check if user is mentioned in ACE
            if (attrU != null && ((XincoCoreACE) attrACL.get(i)).getXincoCoreUserId() == attrU.getId()) {
                LOG.fine("User has permission!");
                match_ace = true;
            }
            //check if group of user is mentioned in ACE
            if (!match_ace && attrU != null) {
                for (j = 0; j < attrU.getXincoCoreGroups().size(); j++) {
                    if (((XincoCoreACE) attrACL.get(i)).getXincoCoreGroupId()
                            == ((XincoCoreGroup) attrU.getXincoCoreGroups().get(j)).getId()) {
                        match_ace = true;
                        LOG.log(FINE,
                                "User has permission as member of group: {0}",
                                ((XincoCoreGroup) attrU.getXincoCoreGroups().get(j)).getDesignation());
                        break;
                    }
                }
            }
            //Check to see its public permissions
            if (!match_ace && ((XincoCoreACE) attrACL.get(i)).getXincoCoreGroupId() == 3) {
                LOG.fine("Data/Folder has public permission!");
                match_ace = true;
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
        return core_ace;
    }

    @Override
    public String toString() {
        return "XincoCoreACE{id =" + getId() + ", admin= " + isAdminPermission()
                + ", execute= " + isExecutePermission() + ", read= "
                + isReadPermission() + ", write= " + isWritePermission() + "}";
    }
}
