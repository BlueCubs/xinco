/**
 * Copyright 2011 blueCubs.com
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
 * Name: XincoCoreDataTypeAttributeServer
 *
 * Description: data type attribute
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.XincoAddAttributeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeAttributeJpaController;
import com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;

public class XincoCoreDataTypeAttributeServer extends XincoCoreDataTypeAttribute {

    private static List result;
    //create data type attribute object for data structures

    public XincoCoreDataTypeAttributeServer(int attrID1, int attrID2) throws XincoException {
        try {
            result = XincoDBManager.createdQuery("SELECT xcdta FROM XincoCoreDataTypeAttribute xcdta "
                    + "WHERE xcdta.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId=" + attrID1
                    + " AND xcdta.xincoCoreDataTypeAttributePK.attributeId=" + attrID2);
            //throw exception if no result found
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute xcdta =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute) result.get(0);
                setXincoCoreDataTypeId(xcdta.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
                setAttributeId(xcdta.getXincoCoreDataTypeAttributePK().getAttributeId());
                setDesignation(xcdta.getDesignation());
                setDataType(xcdta.getDataType());
                setSize(xcdta.getAttrSize());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException();
        }

    }

    //create data type attribute object for data structures
    public XincoCoreDataTypeAttributeServer(int attrID1, int attrID2, String attrD, String attrDT, int attrS) throws XincoException {
        setXincoCoreDataTypeId(attrID1);
        setAttributeId(attrID2);
        setDesignation(attrD);
        setDataType(attrDT);
        setSize(attrS);
    }

    public XincoCoreDataTypeAttributeServer(com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute xcdta) {
        setXincoCoreDataTypeId(xcdta.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
        setAttributeId(xcdta.getXincoCoreDataTypeAttributePK().getAttributeId());
        setDesignation(xcdta.getDesignation());
        setDataType(xcdta.getDataType());
        setSize(xcdta.getAttrSize());
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute xcdta =
                    new com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute(new XincoCoreDataTypeAttributePK(getXincoCoreDataTypeId(), getAttributeId()));
            xcdta.setDesignation(getDesignation());
            xcdta.setDataType(getDataType());
            xcdta.setAttrSize(getSize());
            xcdta.setModificationReason("audit.general.created");
            xcdta.setModifierId(getChangerID());
            xcdta.setModificationTime(new Timestamp(new Date().getTime()));
            xcdta.setXincoCoreDataType(new com.bluecubs.xinco.core.server.persistence.XincoCoreDataType(getXincoCoreDataTypeId()));
            new XincoCoreDataTypeAttributeJpaController(XincoDBManager.getEntityManagerFactory()).create(xcdta);
            result = XincoDBManager.createdQuery("Select xcd from XincoCoreData xcd where xcd.xincoCoreDataType.id=" + getXincoCoreDataTypeId());
            for (Object o : result) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreData) o;
                DatatypeFactory factory = DatatypeFactory.newInstance();
                for (XincoAddAttribute attr : xcd.getXincoAddAttributeList()) {
                    new XincoAddAttributeServer(xcd.getId(), attr.getXincoAddAttributePK().getAttributeId(), 0, 0, 0.0, "", "", factory.newXMLGregorianCalendar()).write2DB();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex.getMessage());
        }
        return getAttributeId();
    }

    //delete from db
    public static int deleteFromDB(XincoCoreDataTypeAttribute attrCDTA, int userID) throws XincoException {
        try {
            result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE x.xincoAddAttributePK.attributeId =" + attrCDTA.getAttributeId()
                    + " and x.xincoAddAttributePK.xincoCoreDataId IN (Select xcd.id from XincoCoreData xcd where xcd.xincoCoreDataType.id=" + attrCDTA.getXincoCoreDataTypeId() + ")");
            for (Object o : result) {
                com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                        (com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) o;
                new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).destroy(xaa.getXincoAddAttributePK());
            }
            result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId =" + attrCDTA.getXincoCoreDataTypeId()
                    + " and x.xincoCoreDataTypeAttributePK.attributeId =" + attrCDTA.getAttributeId());
            for (Object o : result) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute xcdta =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute) o;
                new XincoCoreDataTypeAttributeJpaController(XincoDBManager.getEntityManagerFactory()).destroy(xcdta.getXincoCoreDataTypeAttributePK());
            }
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
            throw new XincoException();
        }
        return 0;
    }

    //create complete list of data type attributes
    public static ArrayList getXincoCoreDataTypeAttributes(int attrID) {
        ArrayList coreDataTypeAttributes = new ArrayList();
        try {
            result = XincoDBManager.createdQuery("SELECT xcdta FROM XincoCoreDataTypeAttribute xcdta "
                    + "WHERE xcdta.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId =" + attrID
                    + " ORDER BY xcdta.xincoCoreDataTypeAttributePK.attributeId");
            for (Iterator it = result.iterator(); it.hasNext();) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute attr =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute) it.next();
                coreDataTypeAttributes.add(new XincoCoreDataTypeAttributeServer(attr));
            }
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
            coreDataTypeAttributes.clear();
        }
        return coreDataTypeAttributes;
    }
}
