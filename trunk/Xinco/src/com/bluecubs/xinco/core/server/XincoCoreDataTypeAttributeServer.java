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
 * Name:            XincoCoreDataTypeAttributeServer
 *
 * Description:     data type attribute
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

import com.bluecubs.xinco.add.server.XincoAddAttributeServer;
import java.util.Vector;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.XincoAddAttributeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeAttributeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeJpaController;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class XincoCoreDataTypeAttributeServer extends XincoCoreDataTypeAttribute {

    private static final long serialVersionUID = 1L;
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
                setXinco_core_data_type_id(xcdta.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
                setAttribute_id(xcdta.getXincoCoreDataTypeAttributePK().getAttributeId());
                setDesignation(xcdta.getDesignation());
                setData_type(xcdta.getDataType());
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
        setXinco_core_data_type_id(attrID1);
        setAttribute_id(attrID2);
        setDesignation(attrD);
        setData_type(attrDT);
        setSize(attrS);
    }

    public XincoCoreDataTypeAttributeServer(com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute xcdta) {
        setXinco_core_data_type_id(xcdta.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
        setAttribute_id(xcdta.getXincoCoreDataTypeAttributePK().getAttributeId());
        setDesignation(xcdta.getDesignation());
        setData_type(xcdta.getDataType());
        setSize(xcdta.getAttrSize());
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute xcdta =
                    new com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute(new XincoCoreDataTypeAttributePK(getXinco_core_data_type_id(), getAttribute_id()));
            xcdta.setDesignation(getDesignation());
            xcdta.setDataType(getData_type());
            xcdta.setAttrSize(getSize());
            xcdta.setModificationReason("audit.general.created");
            xcdta.setModifierId(getChangerID());
            xcdta.setModificationTime(new Timestamp(new Date().getTime()));
            xcdta.setXincoCoreDataType(new XincoCoreDataTypeJpaController().findXincoCoreDataType(getXinco_core_data_type_id()));
            new XincoCoreDataTypeAttributeJpaController().create(xcdta);
            result = XincoDBManager.createdQuery("Select xcd from XincoCoreData xcd where xcd.xincoCoreDataTypeId.id=" + getXinco_core_data_type_id());
            for (Object o : result) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreData xcd =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreData) o;
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(new Date());
                for (XincoAddAttribute attr : xcd.getXincoAddAttributeList()) {
                    new XincoAddAttributeServer(xcd.getId(), attr.getXincoAddAttributePK().getAttributeId(), 0, 0, 0.0, "", "", cal).write2DB();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException();
        }
        return getAttribute_id();
    }

    //delete from db
    public static int deleteFromDB(XincoCoreDataTypeAttribute attrCDTA, int userID) throws XincoException {
        try {
            result = XincoDBManager.createdQuery("SELECT x FROM XincoAddAttribute x WHERE x.xincoAddAttributePK.attributeId =" + attrCDTA.getAttribute_id()
                    + " and x.xincoAddAttributePK.xincoCoreDataId IN (Select xcd.id from XincoCoreData xcd where xcd.xincoCoreDataTypeId.id=" + attrCDTA.getXinco_core_data_type_id() + ")");
            for (Object o : result) {
                com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                        (com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) o;
                new XincoAddAttributeJpaController().destroy(xaa.getXincoAddAttributePK());
            }
            result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId =" + attrCDTA.getXinco_core_data_type_id()
                    + " and x.xincoCoreDataTypeAttributePK.attributeId =" + attrCDTA.getAttribute_id());
            for (Object o : result) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute xcdta =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute) o;
                new XincoCoreDataTypeAttributeJpaController().destroy(xcdta.getXincoCoreDataTypeAttributePK());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException();
        }
        return 0;
    }

    //create complete list of data type attributes
    public static Vector getXincoCoreDataTypeAttributes(int attrID) {
        Vector coreDataTypeAttributes = new Vector();
        try {
            result = XincoDBManager.createdQuery("SELECT xcdta FROM XincoCoreDataTypeAttribute xcdta "
                    + "WHERE xcdta.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId =" + attrID
                    + " ORDER BY xcdta.xincoCoreDataTypeAttributePK.attributeId");
            while (result.size() > 0) {
                coreDataTypeAttributes.addElement(new XincoCoreDataTypeAttributeServer((com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute) result.get(0)));
                result.remove(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            coreDataTypeAttributes.removeAllElements();
        }
        return coreDataTypeAttributes;
    }
}
