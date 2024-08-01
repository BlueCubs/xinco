/**
 * Copyright 2012 blueCubs.com
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
 * <p>Name: XincoCoreGroupServer
 *
 * <p>Description: group
 *
 * <p>Original Author: Alexander Manes Date: 2004
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreGroupJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.server.service.XincoCoreGroup;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class XincoCoreGroupServer extends XincoCoreGroup {

  private static List result;
  private static HashMap parameters = new HashMap();
  // create group object for data structures

  public XincoCoreGroupServer(int attrID) throws XincoException {
    try {
      parameters.clear();
      parameters.put("id", attrID);
      result = namedQuery("XincoCoreGroup.findById", parameters);
      // throw exception if no result found
      if (result.size() > 0) {
        com.bluecubs.xinco.core.server.persistence.XincoCoreGroup xcg =
            (com.bluecubs.xinco.core.server.persistence.XincoCoreGroup) result.get(0);
        setId(xcg.getId());
        setDesignation(xcg.getDesignation());
        setStatusNumber(xcg.getStatusNumber());
      } else {
        throw new XincoException();
      }
    } catch (Exception e) {
      throw new XincoException(e.getMessage());
    }
  }

  // create group object for data structures
  public XincoCoreGroupServer(int attrID, String attrD, int attrSN) throws XincoException {
    setId(attrID);
    setDesignation(attrD);
    setStatusNumber(attrSN);
  }

  public XincoCoreGroupServer(com.bluecubs.xinco.core.server.persistence.XincoCoreGroup xcg) {
    setId(xcg.getId());
    setDesignation(xcg.getDesignation());
    setStatusNumber(xcg.getStatusNumber());
  }

  // write to db
  public int write2DB() throws XincoException {
    try {
      XincoCoreGroupJpaController controller =
          new XincoCoreGroupJpaController(getEntityManagerFactory());
      com.bluecubs.xinco.core.server.persistence.XincoCoreGroup xcg;
      if (getId() > 0) {
        xcg = controller.findXincoCoreGroup(getId());
        xcg.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
        xcg.setStatusNumber(getStatusNumber());
        xcg.setModificationReason("audit.general.modified");
        xcg.setModifierId(getChangerID());
        xcg.setModificationTime(new Timestamp(new Date().getTime()));
        controller.edit(xcg);
      } else {
        xcg = new com.bluecubs.xinco.core.server.persistence.XincoCoreGroup(getId());
        xcg.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
        xcg.setStatusNumber(getStatusNumber());
        xcg.setModificationReason("audit.general.create");
        xcg.setModifierId(getChangerID());
        xcg.setModificationTime(new Timestamp(new Date().getTime()));
        controller.create(xcg);
      }
      setId(xcg.getId());
    } catch (Exception e) {
      throw new XincoException(e.getMessage());
    }
    return getId();
  }

  public static int deleteFromDB(XincoCoreGroup group) {
    try {
      new XincoCoreGroupJpaController(getEntityManagerFactory()).destroy(group.getId());
      return 0;
    } catch (XincoException | IllegalOrphanException | NonexistentEntityException ex) {
      getLogger(XincoCoreGroupServer.class.getName()).log(SEVERE, null, ex);
    }
    return -1;
  }

  // create complete list of groups
  public static ArrayList getXincoCoreGroups() {
    ArrayList coreGroups = new ArrayList();
    try {
      result = createdQuery("SELECT xcg FROM XincoCoreGroup xcg ORDER BY xcg.designation");
      result.forEach(
          (o) -> {
            coreGroups.add(
                new XincoCoreGroupServer(
                    (com.bluecubs.xinco.core.server.persistence.XincoCoreGroup) o));
          });
    } catch (Exception e) {
      coreGroups.clear();
    }
    return coreGroups;
  }

  protected static List<XincoCoreUserServer> getMembersOfGroup(int id) {
    parameters.clear();
    parameters.put("xincoCoreGroupId", id);
    result = namedQuery("XincoCoreUserHasXincoCoreGroup.findByXincoCoreGroupId", parameters);
    ArrayList<XincoCoreUserServer> users = new ArrayList<>();
    for (Iterator it = result.iterator(); it.hasNext(); ) {
      XincoCoreUserHasXincoCoreGroup xcuhg = (XincoCoreUserHasXincoCoreGroup) it.next();
      users.add(new XincoCoreUserServer(xcuhg.getXincoCoreUser()));
    }
    return users;
  }
}