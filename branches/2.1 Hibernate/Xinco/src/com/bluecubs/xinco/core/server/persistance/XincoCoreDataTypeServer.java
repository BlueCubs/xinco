/**
 *Copyright 2004 blueCubs.com
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

package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoCoreDataType;
import com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Create data type object for data structures
 * @author Alexander Manes
 */
public class XincoCoreDataTypeServer extends XincoCoreDataType implements XincoAuditableDAO, XincoPersistanceServerObject{
    private static XincoPersistanceManager pm = new XincoPersistanceManager();
    private static List result;
    private static HashMap parameters;
    private Vector xincoCoreDataTypeAttributes;
    /**
     * Create data type object for data structures
     * @param attrID 
     * @throws com.bluecubs.xinco.core.XincoException
     */
    @SuppressWarnings("unchecked")
    public XincoCoreDataTypeServer(int attrID) throws XincoException {
        try {
            parameters = new HashMap();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreData.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreDataType temp = (XincoCoreDataType) result.get(0);
                setId(temp.getId());
                setDesignation(temp.getDesignation());
                setDescription(temp.getDescription());
//                setXincoCoreDataTypeAttributes(XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(getId()));
            }
            else {
                throw new XincoException();
            }
        } catch (Throwable e) {
            throw new XincoException();
        }
    }

    /**
     * Create data type object for data structures
     * @param id
     * @param attrD
     * @param attrDESC
     * @param attrA
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreDataTypeServer(int id, String attrD, String attrDESC, Vector attrA) throws XincoException {
        setId(id);
        setDesignation(attrD);
        setDescription(attrDESC);
//        setXinco_core_data_type_attributes(attrA);
    }

    /**
     * Create complete list of data types
     * @param DBM
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreDataTypes(XincoDBManager DBM) {
        Vector coreDataTypes = new Vector();
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_data_type ORDER BY designation");
            while (rs.next()) {
                coreDataTypes.add(new XincoCoreDataTypeServer(rs.getInt("id"), rs.getString("designation"), rs.getString("description"), XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(rs.getInt("id"), DBM)));
            }
        } catch (Throwable e) {
            coreDataTypes.removeAllElements();
        }
        return coreDataTypes;
    }

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XincoAbstractAuditableObject create(XincoAbstractAuditableObject value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public HashMap getParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNewID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean deleteFromDB() throws XincoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean write2DB() throws XincoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector getXincoCoreDataTypeAttributes() {
        return xincoCoreDataTypeAttributes;
    }

    public void setXincoCoreDataTypeAttributes(Vector xincoCoreDataTypeAttributes) {
        this.xincoCoreDataTypeAttributes = xincoCoreDataTypeAttributes;
    }
}
