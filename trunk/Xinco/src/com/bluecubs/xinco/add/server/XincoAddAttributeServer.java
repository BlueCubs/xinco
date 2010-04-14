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
 * Name:            XincoAddAttributeServer
 *
 * Description:     additional attributes of a data object 
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
package com.bluecubs.xinco.add.server;

import com.bluecubs.xinco.add.XincoAddAttribute;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.XincoAddAttributeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class XincoAddAttributeServer extends XincoAddAttribute {

    private static final long serialVersionUID = 1L;
    private static List result;
    //create add attribute object for data structures

    public XincoAddAttributeServer(int attrID1, int attrID2) throws XincoException {
        try {
            result = XincoDBManager.createdQuery("SELECT xaa FROM XincoAddAttribute xaa"
                    + "WHERE xaa.xincoAddAttributePK.xincoCoreDataId=" + attrID1
                    + " AND xaa.xincoAddAttributePK.attributeId=" + attrID2);
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                        (com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) result.get(0);
                setXinco_core_data_id(xaa.getXincoAddAttributePK().getAttributeId());
                setAttribute_id(xaa.getXincoAddAttributePK().getAttributeId());
                setAttrib_int(xaa.getAttribInt());
                setAttrib_unsignedint(xaa.getAttribUnsignedint());
                setAttrib_double(xaa.getAttribDouble());
                setAttrib_varchar(xaa.getAttribVarchar());
                setAttrib_text(xaa.getAttribText());
                setAttrib_datetime(new GregorianCalendar());
                getAttrib_datetime().setTime(xaa.getAttribDatetime());
            }
        } catch (Exception e) {
            throw new XincoException();
        }
    }

    //create add attribute object for data structures
    public XincoAddAttributeServer(int attrID1, int attrID2, int attrI, long attrUI, double attrD, String attrVC, String attrT, Calendar attrDT) throws XincoException {
        setXinco_core_data_id(attrID1);
        setAttribute_id(attrID2);
        setAttrib_int(attrI);
        setAttrib_unsignedint(attrUI);
        setAttrib_double(attrD);
        setAttrib_varchar(attrVC);
        setAttrib_text(attrT);
        setAttrib_datetime(attrDT);
    }

    public XincoAddAttributeServer(com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa) {
        setXinco_core_data_id(xaa.getXincoAddAttributePK().getAttributeId());
        setAttribute_id(xaa.getXincoAddAttributePK().getAttributeId());
        setAttrib_int(xaa.getAttribInt());
        setAttrib_unsignedint(xaa.getAttribUnsignedint());
        setAttrib_double(xaa.getAttribDouble());
        setAttrib_varchar(xaa.getAttribVarchar());
        setAttrib_text(xaa.getAttribText());
        setAttrib_datetime(new GregorianCalendar());
        getAttrib_datetime().setTime(xaa.getAttribDatetime());
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            String attrT = "";
            String attrVC = "";
            String attrDT = "";
            if (getAttrib_text() != null) {
                attrT = getAttrib_text();
                attrT = attrT.replaceAll("'", "\\\\'");
            } else {
                attrT = "NULL";
            }
            if (getAttrib_varchar() != null) {
                attrVC = getAttrib_varchar();
                attrVC = attrVC.replaceAll("'", "\\\\'");
            } else {
                attrVC = "NULL";
            }
            if (getAttrib_datetime() != null) {
                //convert clone from remote time to local time
                Calendar cal = (Calendar) getAttrib_datetime().clone();
                Calendar ngc = new GregorianCalendar();
                cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - getAttrib_datetime().get(Calendar.ZONE_OFFSET)) - (ngc.get(Calendar.DST_OFFSET) + getAttrib_datetime().get(Calendar.DST_OFFSET)));
                attrDT = "" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
            } else {
                attrDT = "NULL";
            }
            com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                    new com.bluecubs.xinco.core.server.persistence.XincoAddAttribute();
            xaa.setXincoCoreData(new XincoCoreDataJpaController().findXincoCoreData(getXinco_core_data_id()));
            xaa.setXincoAddAttributePK(new XincoAddAttributePK());
            xaa.getXincoAddAttributePK().setXincoCoreDataId(xaa.getXincoCoreData().getId());
            xaa.getXincoAddAttributePK().setAttributeId(getAttribute_id());
            xaa.setAttribInt(getAttrib_int());
            xaa.setAttribUnsignedint(getAttrib_unsignedint());
            xaa.setAttribDouble(getAttrib_double());
            xaa.setAttribVarchar(attrVC);
            xaa.setAttribText(attrT);
            if (!attrDT.equals("NULL")) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                xaa.setAttribDatetime(df.parse(attrDT));
            }
            XincoAddAttributeJpaController controller = new XincoAddAttributeJpaController();
            controller.create(xaa);
        } catch (Exception e) {
            //no commit or rollback -> CoreData manages exceptions!
            e.printStackTrace();
            throw new XincoException();
        }
        return 1;
    }
    //create complete list of add attributes

    public static Vector getXincoAddAttributes(int attrID) {
        Vector addAttributes = new Vector();
        try {
            result = XincoDBManager.createdQuery("SELECT xaa FROM XincoAddAttribute xaa "
                    + "WHERE xaa.xincoAddAttributePK.xincoCoreDataId=" + attrID
                    + " ORDER BY xaa.xincoAddAttributePK.attributeId");

            GregorianCalendar cal;
            while (result.size() > 0) {
                cal = new GregorianCalendar();
                com.bluecubs.xinco.core.server.persistence.XincoAddAttribute xaa =
                        (com.bluecubs.xinco.core.server.persistence.XincoAddAttribute) result.get(0);
                if (xaa.getAttribDatetime() != null) {
                    cal.setTime(xaa.getAttribDatetime());
                }
                addAttributes.addElement(new XincoAddAttributeServer(xaa));
                result.remove(0);
            }
        } catch (Exception e) {
            addAttributes.removeAllElements();
        }

        return addAttributes;
    }
}
