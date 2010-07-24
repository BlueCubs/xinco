/**
 *Copyright 2010 blueCubs.com
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
 * Name:            XincoCoreDataTypeServer
 *
 * Description:     data type 
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

import com.bluecubs.xinco.core.XincoCoreDataType;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XincoCoreDataTypeServer extends XincoCoreDataType {

    private static final long serialVersionUID = 1L;
    private static List result;
    private static HashMap parameters = new HashMap();

    protected XincoCoreDataTypeServer() {
    }
    //create data type object for data structures

    public XincoCoreDataTypeServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = XincoDBManager.namedQuery("XincoCoreDataType.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreDataType xcdt =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) result.get(0);
                setId(xcdt.getId());
                setDesignation(xcdt.getDesignation());
                setDescription(xcdt.getDescription());
                setXinco_core_data_type_attributes(XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(getId()));
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException();
        }
    }

    private XincoCoreDataTypeServer(com.bluecubs.xinco.core.server.persistence.XincoCoreDataType xcdt) {
        setId(xcdt.getId());
        setDesignation(xcdt.getDesignation());
        setDescription(xcdt.getDescription());
        setXinco_core_data_type_attributes(XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(getId()));
    }

    //create data type object for data structures
    public XincoCoreDataTypeServer(int attrID, String attrD, String attrDESC, Vector attrA) throws XincoException {
        setId(attrID);
        setDesignation(attrD);
        setDescription(attrDESC);
        setXinco_core_data_type_attributes(attrA);
    }

    //create complete list of data types
    public static Vector getXincoCoreDataTypes() {
        Vector coreDataTypes = new Vector();
        try {
            result = XincoDBManager.createdQuery("SELECT xcdt FROM XincoCoreDataType xcdt ORDER BY xcdt.designation");
            while (result.size() > 0) {
                coreDataTypes.addElement(new XincoCoreDataTypeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreDataType) result.get(0)));
                result.remove(0);
            }
        } catch (Exception e) {
            coreDataTypes.removeAllElements();
        }
        return coreDataTypes;
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            XincoCoreDataTypeJpaController controller = new XincoCoreDataTypeJpaController();
            if (getId() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreDataType xcdt =
                        controller.findXincoCoreDataType(getId());
                xcdt.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcdt.setDescription(getDescription().replaceAll("'", "\\\\'"));
                xcdt.setId(getId());
                xcdt.setModificationReason("audit.general.modified");
                xcdt.setModifierId(getChangerID());
                xcdt.setModificationTime(new Timestamp(new Date().getTime()));
                controller.edit(xcdt);
            } else {
                com.bluecubs.xinco.core.server.persistence.XincoCoreDataType xcdt =
                        new com.bluecubs.xinco.core.server.persistence.XincoCoreDataType();
                xcdt.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
                xcdt.setDescription(getDescription().replaceAll("'", "\\\\'"));
                xcdt.setModificationReason("audit.general.created");
                xcdt.setModifierId(getChangerID());
                xcdt.setModificationTime(new Timestamp(new Date().getTime()));
                controller.create(xcdt);
                setId(xcdt.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    public static int deleteFromDB(XincoCoreDataType xcdt) {
        try {
            new XincoCoreDataTypeJpaController().destroy(xcdt.getId());
            return 0;
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoCoreGroupServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoCoreGroupServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}
