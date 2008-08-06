/**
 *Copyright 2007 blueCubs.com
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

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAuditableDAO;
import java.util.HashMap;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bluecubs.xinco.core.persistence.XincoAddAttribute;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.Date;
import java.util.List;

public class XincoAddAttributeServer extends XincoAddAttribute implements XincoAuditableDAO, PersistenceServerObject {
    //create add attribute object for data structures
    private static List result;
    private static final long serialVersionUID = -2238466076051443225L;

    @SuppressWarnings("unchecked")
    public XincoAddAttributeServer(int attrID1, int attrID2) throws XincoException {
        try {
            parameters.clear();
            parameters.put("xincoCoreDataId", attrID1);
            parameters.put("attributeId", attrID2);
            result = pm.createdQuery("SELECT x FROM XincoAddAttribute x WHERE " +
                    "x.xincoAddAttributePK.xincoCoreDataId = :xincoCoreDataId and " +
                    "x.xincoAddAttributePK.attributeId = :attributeId", parameters);
            if (!result.isEmpty()) {
                XincoAddAttribute xaa = (XincoAddAttribute) result.get(0);
                getXincoAddAttributePK().setXincoCoreDataId(xaa.getXincoAddAttributePK().getXincoCoreDataId());
                getXincoAddAttributePK().setAttributeId(xaa.getXincoAddAttributePK().getAttributeId());
                setAttribInt(xaa.getAttribInt());
                setAttribUnsignedint(xaa.getAttribUnsignedint());
                setAttribDouble(xaa.getAttribDouble());
                setAttribVarchar(xaa.getAttribVarchar());
                setAttribText(xaa.getAttribVarchar());
                setAttribDatetime(xaa.getAttribDatetime());
            }
        } catch (Exception e) {
            throw new XincoException();
        }
    }

    //create add attribute object for data structures
    public XincoAddAttributeServer(int attrID1, int attrID2, int attrI, long attrUI,
            double attrD, String attrVC, String attrT, Date attrDT) throws XincoException {
        getXincoAddAttributePK().setXincoCoreDataId(attrID1);
        getXincoAddAttributePK().setAttributeId(attrID2);
        setAttribInt(attrI);
        setAttribUnsignedint(attrUI);
        setAttribDouble(attrD);
        setAttribVarchar(attrVC);
        setAttribText(attrT);
        setAttribDatetime(attrDT);
    }

    public boolean write2DB() {
        String attrT = "";
        String attrVC = "";
        Date attrDT = null;
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
            Calendar calO = (Calendar) getAttribDatetime().clone();
            Calendar cal = (Calendar) getAttribDatetime().clone();
            Calendar ngc = new GregorianCalendar();
            cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - calO.get(Calendar.ZONE_OFFSET)) -
                    (ngc.get(Calendar.DST_OFFSET) + calO.get(Calendar.DST_OFFSET)));
            attrDT = new Date(cal.getTimeInMillis());
        }
        XincoAddAttribute xaa = new XincoAddAttribute();
        xaa.getXincoAddAttributePK().setXincoCoreDataId(getXincoAddAttributePK().getXincoCoreDataId());
        xaa.getXincoAddAttributePK().setAttributeId(getXincoAddAttributePK().getAttributeId());
        xaa.setAttribInt(getAttribInt());
        xaa.setAttribUnsignedint(getAttribUnsignedint());
        xaa.setAttribDouble(getAttribDouble());
        xaa.setAttribVarchar(attrVC);
        xaa.setAttribText(attrT);
        xaa.setAttribDatetime(attrDT);
        pm.persist(xaa, false, true);
        return true;
    }
    //create complete list of add attributes
    @SuppressWarnings("unchecked")
    public static Vector getXincoAddAttributes(int attrID) {
        Vector addAttributes = new Vector();
        try {
            parameters.clear();
            parameters.put("xincoCoreDataId", attrID);
            result = pm.createdQuery("SELECT x FROM XincoAddAttribute x WHERE " +
                    "x.xincoAddAttributePK.xincoCoreDataId = :xincoCoreDataId order by x.xincoAddAttributePK.attributeId", parameters);
            while (!result.isEmpty()) {
                addAttributes.addElement((XincoAddAttributeServer) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            addAttributes.removeAllElements();
        }

        return addAttributes;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AbstractAuditableObject create(AbstractAuditableObject value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delete(AbstractAuditableObject value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public HashMap getParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNewID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean deleteFromDB() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
