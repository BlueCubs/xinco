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
 * Name:            XincoCoreGroupServer
 *
 * Description:     group
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

import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreGroupJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XincoCoreGroupServer extends XincoCoreGroup {

    private static final long serialVersionUID = 1L;
    private static List result;
    private static HashMap parameters = new HashMap();
    //create group object for data structures

    public XincoCoreGroupServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = XincoDBManager.namedQuery("XincoCoreGroup.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreGroup xcg =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreGroup) result.get(0);
                setId(xcg.getId());
                setDesignation(xcg.getDesignation());
                setStatus_number(xcg.getStatusNumber());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException(e.getMessage());
        }
    }

    //create group object for data structures
    public XincoCoreGroupServer(int attrID, String attrD, int attrSN) throws XincoException {
        setId(attrID);
        setDesignation(attrD);
        setStatus_number(attrSN);
    }

    public XincoCoreGroupServer(com.bluecubs.xinco.core.server.persistence.XincoCoreGroup xcg) {
        setId(xcg.getId());
        setDesignation(xcg.getDesignation());
        setStatus_number(xcg.getStatusNumber());
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            XincoCoreGroupJpaController controller = new XincoCoreGroupJpaController();
            if (getId() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreGroup xcg =
                        controller.findXincoCoreGroup(getId());
                xcg.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcg.setStatusNumber(getStatus_number());
                xcg.setModificationReason("audit.general.modified");
                xcg.setModifierId(getChangerID());
                xcg.setModificationTime(new Timestamp(new Date().getTime()));
                controller.edit(xcg);
            } else {
                com.bluecubs.xinco.core.server.persistence.XincoCoreGroup xcg =
                        new com.bluecubs.xinco.core.server.persistence.XincoCoreGroup();
                xcg.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcg.setStatusNumber(getStatus_number());
                xcg.setModificationReason("audit.general.create");
                xcg.setModifierId(getChangerID());
                xcg.setModificationTime(new Timestamp(new Date().getTime()));
                controller.create(xcg);
                setId(xcg.getId());
            }
        } catch (Exception e) {
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    public static int deleteFromDB(XincoCoreGroup group) {
        try {
            new XincoCoreGroupJpaController().destroy(group.getId());
            return 0;
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    //create complete list of groups
    public static Vector getXincoCoreGroups() {
        Vector coreGroups = new Vector();
        try {
            result = XincoDBManager.createdQuery("SELECT xcg FROM XincoCoreGroup xcg ORDER BY xcg.designation");
            for (Object o : result) {
                coreGroups.addElement(new XincoCoreGroupServer((com.bluecubs.xinco.core.server.persistence.XincoCoreGroup) o));
            }
        } catch (Exception e) {
            coreGroups.removeAllElements();
        }
        return coreGroups;
    }
}
