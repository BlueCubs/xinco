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
 * <p>Name: XincoCoreDataTypeServer
 *
 * <p>Description: data type
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
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreDataTypeAttribute;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class XincoCoreDataTypeServer extends XincoCoreDataType {

  private static List result;
  private static HashMap parameters = new HashMap();
  // create data type object for data structures

  public XincoCoreDataTypeServer(int attrID) throws XincoException {
    try {
      parameters.clear();
      parameters.put("id", attrID);
      result = namedQuery("XincoCoreDataType.findById", parameters);
      // throw exception if no result found
      if (result.size() > 0) {
        com.bluecubs.xinco.core.server.persistence.XincoCoreDataType xcdt =
            (com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) result.get(0);
        setId(xcdt.getId());
        setDesignation(xcdt.getDesignation());
        setDescription(xcdt.getDescription());
        getXincoCoreDataTypeAttributes().clear();
        getXincoCoreDataTypeAttributes()
            .addAll(XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(getId()));
      } else {
        throw new XincoException();
      }
    } catch (Exception e) {
      throw new XincoException();
    }
  }

  private XincoCoreDataTypeServer(
      com.bluecubs.xinco.core.server.persistence.XincoCoreDataType xcdt) {
    setId(xcdt.getId());
    setDesignation(xcdt.getDesignation());
    setDescription(xcdt.getDescription());
    getXincoCoreDataTypeAttributes().clear();
    getXincoCoreDataTypeAttributes()
        .addAll(XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(getId()));
  }

  // create data type object for data structures
  public XincoCoreDataTypeServer(
      int attrID, String attrD, String attrDESC, ArrayList<XincoCoreDataTypeAttribute> attrA)
      throws XincoException {
    setId(attrID);
    setDesignation(attrD);
    setDescription(attrDESC);
    getXincoCoreDataTypeAttributes().clear();
    getXincoCoreDataTypeAttributes().addAll(attrA);
  }

  // create complete list of data types
  public static ArrayList getXincoCoreDataTypes() {
    ArrayList<XincoCoreDataTypeServer> coreDataTypes = new ArrayList<>();
    try {
      result = createdQuery("SELECT xcdt FROM XincoCoreDataType xcdt ORDER BY xcdt.id");
      for (Iterator it = result.iterator(); it.hasNext(); ) {
        com.bluecubs.xinco.core.server.persistence.XincoCoreDataType xcdt =
            (com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) it.next();
        coreDataTypes.add(
            new XincoCoreDataTypeServer(
                (com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) xcdt));
      }
    } catch (Exception e) {
      getLogger(XincoCoreDataTypeAttributeServer.class.getSimpleName()).log(SEVERE, null, e);
      coreDataTypes.clear();
    }
    return coreDataTypes;
  }

  public static XincoCoreDataTypeServer getXincoCoreDataType(int id) {
    try {
      parameters.clear();
      parameters.put("id", id);
      result = namedQuery("XincoCoreDataType.findById", parameters);
      if (!result.isEmpty()) {
        return new XincoCoreDataTypeServer(
            (com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) result.get(0));
      }
    } catch (Exception e) {
      getLogger(XincoCoreDataTypeAttributeServer.class.getSimpleName()).log(SEVERE, null, e);
    }
    return null;
  }

  // write to db
  public int write2DB() throws XincoException {
    try {
      XincoCoreDataTypeJpaController controller =
          new XincoCoreDataTypeJpaController(getEntityManagerFactory());
      com.bluecubs.xinco.core.server.persistence.XincoCoreDataType xcdt;
      if (getId() > 0) {
        xcdt = controller.findXincoCoreDataType(getId());
        xcdt.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
        xcdt.setDescription(getDescription().replaceAll("'", "\\\\'"));
        xcdt.setId(getId());
        xcdt.setModificationReason("audit.general.modified");
        xcdt.setModifierId(getChangerID());
        xcdt.setModificationTime(new Timestamp(new Date().getTime()));
        controller.edit(xcdt);
      } else {
        xcdt = new com.bluecubs.xinco.core.server.persistence.XincoCoreDataType();
        xcdt.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
        xcdt.setDescription(getDescription().replaceAll("'", "\\\\'"));
        xcdt.setModificationReason("audit.general.created");
        xcdt.setModifierId(getChangerID());
        xcdt.setModificationTime(new Timestamp(new Date().getTime()));
        controller.create(xcdt);
      }
      setId(xcdt.getId());
    } catch (Exception e) {
      getLogger(XincoCoreDataTypeAttributeServer.class.getSimpleName()).log(SEVERE, null, e);
      throw new XincoException(e.getMessage());
    }
    return getId();
  }

  public static int deleteFromDB(XincoCoreDataType xcdt) {
    try {
      new XincoCoreDataTypeJpaController(getEntityManagerFactory()).destroy(xcdt.getId());
      return 0;
    } catch (IllegalOrphanException | NonexistentEntityException ex) {
      getLogger(XincoCoreDataTypeAttributeServer.class.getSimpleName()).log(SEVERE, null, ex);
      return -1;
    }
  }
}
