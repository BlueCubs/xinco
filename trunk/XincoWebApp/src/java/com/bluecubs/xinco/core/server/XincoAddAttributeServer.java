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
 * Name: XincoAddAttributeServer
 *
 * Description: additional attributes of a data object
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
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.XincoAddAttributeJpaController;
import com.bluecubs.xinco.core.server.service.XincoAddAttribute;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class XincoAddAttributeServer extends XincoAddAttribute {

    private static List result;
    //create add attribute object for data structures

    public XincoAddAttributeServer(int xincoCoreDataId, int attributeId) throws XincoException {
        try {
            result = XincoDBManager.createdQuery("SELECT xaa FROM XincoAddAttribute xaa"
                    + " WHERE xaa.xincoAddAttributePK.xincoCoreDataId=" + xincoCoreDataId
                    + " AND xaa.xincoAddAttributePK.attributeId=" + attributeId);
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                        (com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) result.get(0);
                setXincoCoreDataId(xaa.getXincoAddAttributePK().getXincoCoreDataId());
                setAttributeId(xaa.getXincoAddAttributePK().getAttributeId());
                setAttribInt(xaa.getAttribInt());
                setAttribUnsignedint(xaa.getAttribUnsignedint());
                setAttribDouble(xaa.getAttribDouble());
                setAttribVarchar(xaa.getAttribVarchar());
                setAttribText(xaa.getAttribText());
                DatatypeFactory factory = DatatypeFactory.newInstance();
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(xaa.getAttribDatetime());
                setAttribDatetime(factory.newXMLGregorianCalendar(cal));
            }
        } catch (Exception e) {
            throw new XincoException(e.fillInStackTrace());
        }
    }

    //create add attribute object for data structures
    public XincoAddAttributeServer(int attrID1, int attrID2, int attrI, long attrUI, double attrD, String attrVC, String attrT, XMLGregorianCalendar attrDT) throws XincoException {
        setXincoCoreDataId(attrID1);
        setAttributeId(attrID2);
        setAttribInt(attrI);
        setAttribUnsignedint(attrUI);
        setAttribDouble(attrD);
        setAttribVarchar(attrVC);
        setAttribText(attrT);
        setAttribDatetime(attrDT);
    }

    public XincoAddAttributeServer(com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa) {
        try {
            setXincoCoreDataId(xaa.getXincoAddAttributePK().getXincoCoreDataId());
            setAttributeId(xaa.getXincoAddAttributePK().getAttributeId());
            setAttribInt(xaa.getAttribInt());
            setAttribUnsignedint(xaa.getAttribUnsignedint());
            setAttribDouble(xaa.getAttribDouble());
            setAttribVarchar(xaa.getAttribVarchar());
            setAttribText(xaa.getAttribText());
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(xaa.getAttribDatetime());
            setAttribDatetime(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(XincoAddAttributeServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    //write to db
    public int write2DB() throws XincoException {
        boolean create = false;
        try {
            String attrT;
            String attrVC;
            String attrDT;
            if (getAttribText() != null) {
                attrT = getAttribText();
                attrT = attrT.replaceAll("'", "\\\\'");
            } else {
                attrT = "NULL";
            }
            if (getAttribVarchar() != null) {
                attrVC = getAttribVarchar();
                attrVC = attrVC.replaceAll("'", "\\\\'");
            } else {
                attrVC = "NULL";
            }
            if (getAttribDatetime() != null) {
                //convert clone from remote time to local time
                Calendar cal = getAttribDatetime().toGregorianCalendar();
                Calendar ngc = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET)
                        - cal.get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET)
                        + cal.get(Calendar.DST_OFFSET)));
                attrDT = "" + cal.get(Calendar.YEAR) + "-"
                        + (cal.get(Calendar.MONTH) + 1) + "-"
                        + cal.get(Calendar.DAY_OF_MONTH) + " "
                        + cal.get(Calendar.HOUR_OF_DAY) + ":"
                        + cal.get(Calendar.MINUTE) + ":"
                        + cal.get(Calendar.SECOND);
            } else {
                attrDT = "0000-00-00 00:00:00";
            }
            com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                    new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory()).findXincoAddAttribute(new XincoAddAttributePK(getXincoCoreDataId(), getAttributeId()));
            if (xaa == null) {
                create = true;
                xaa = new com.bluecubs.xinco.core.server.persistence.XincoAddAttribute();
                xaa.setXincoCoreData(new com.bluecubs.xinco.core.server.persistence.XincoCoreData(getXincoCoreDataId()));
                xaa.setXincoAddAttributePK(new XincoAddAttributePK());
                xaa.getXincoAddAttributePK().setXincoCoreDataId(xaa.getXincoCoreData().getId());
                xaa.getXincoAddAttributePK().setAttributeId(getAttributeId());
            }
            xaa.setAttribInt(getAttribInt());
            xaa.setAttribUnsignedint(getAttribUnsignedint());
            xaa.setAttribDouble(getAttribDouble());
            xaa.setAttribVarchar(attrVC);
            xaa.setAttribText(attrT);
            if (!attrDT.equals("NULL")) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                xaa.setAttribDatetime(df.parse(attrDT));
            }
            XincoAddAttributeJpaController controller = new XincoAddAttributeJpaController(XincoDBManager.getEntityManagerFactory());
            if (create) {
                controller.create(xaa);
            } else {
                controller.edit(xaa);
            }
        } catch (Exception ex) {
            //no commit or rollback -> CoreData manages exceptions!
            Logger.getLogger(XincoAddAttributeServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
            throw new XincoException();
        }
        return 1;
    }
    //create complete list of add attributes

    public static ArrayList getXincoAddAttributes(int attrID) {
        ArrayList<XincoAddAttribute> addAttributes =
                new ArrayList<XincoAddAttribute>();
        try {
            result = XincoDBManager.createdQuery("SELECT xaa FROM XincoAddAttribute xaa "
                    + "WHERE xaa.xincoAddAttributePK.xincoCoreDataId=" + attrID
                    + " ORDER BY xaa.xincoAddAttributePK.attributeId");

            GregorianCalendar cal;
            for (Iterator it = result.iterator(); it.hasNext();) {
                cal = new GregorianCalendar();
                com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                        (com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) it.next();
                if (xaa.getAttribDatetime() != null) {
                    cal.setTime(xaa.getAttribDatetime());
                }
                addAttributes.add(new XincoAddAttributeServer(xaa));
            }
        } catch (Exception e) {
            Logger.getLogger(
                    XincoAddAttributeServer.class.getSimpleName()).log(
                    Level.SEVERE, null, e);
            addAttributes.clear();
        }

        return addAttributes;
    }
}
