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
 * Name:            XincoCoreLogServer
 *
 * Description:     log
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
import com.bluecubs.xinco.core.persistance.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import java.util.HashMap;
import java.util.Vector;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

public class XincoCoreLogServer extends XincoCoreLog implements XincoAuditableDAO, XincoPersistanceServerObject{

    /**
     * Create single log object for data structures
     * @param id
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreLogServer(int id) throws XincoException {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_log WHERE id=" + id);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setXinco_core_data_id(rs.getInt("xinco_core_data_id"));
                setXinco_core_user_id(rs.getInt("xinco_core_user_id"));
                setOp_code(rs.getInt("op_code"));
                setOp_datetime(new GregorianCalendar());
                getOp_datetime().setTime(rs.getTimestamp("op_datetime"));
                setOp_description(rs.getString("op_description"));
                setVersion(new XincoVersion());
                getVersion().setVersion_high(rs.getInt("version_high"));
                getVersion().setVersion_mid(rs.getInt("version_mid"));
                getVersion().setVersion_low(rs.getInt("version_low"));
                getVersion().setVersion_postfix(rs.getString("version_postfix"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
        } catch (Throwable e) {
            throw new XincoException();
        }
    }

    /**
     * Set user
     * @param user
     */
    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }

    /**
     * Create single log object for data structures
     * @param attrID
     * @param attrCDID
     * @param attrUID
     * @param attrOC
     * @param attrODT
     * @param attrOD
     * @param attrVH
     * @param attrVM
     * @param attrVL
     * @param attrVP
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreLogServer(int attrID, int attrCDID, int attrUID, int attrOC, Calendar attrODT, String attrOD, int attrVH, int attrVM, int attrVL, String attrVP) throws XincoException {
        setId(attrID);
        setXinco_core_data_id(attrCDID);
        setXinco_core_user_id(attrUID);
        setOp_code(attrOC);
        setOp_datetime(attrODT);
        setOp_description(attrOD);
        setVersion(new XincoVersion());
        getVersion().setVersion_high(attrVH);
        getVersion().setVersion_mid(attrVM);
        getVersion().setVersion_low(attrVL);
        getVersion().setVersion_postfix(attrVP);
    }
    
    /**
     * Create complete log list for data
     * @param attrID
     * @param DBM
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreLogs(int attrID) {
        Vector core_log = new Vector();
        GregorianCalendar cal = new GregorianCalendar();
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_log WHERE xinco_core_data_id=" + attrID);
            while (rs.next()) {
                cal = new GregorianCalendar();
                cal.setTime(rs.getTimestamp("op_datetime"));
                core_log.addElement(new XincoCoreLogServer(rs.getInt("id"), rs.getInt("xinco_core_data_id"), rs.getInt("xinco_core_user_id"), rs.getInt("op_code"), cal, rs.getString("op_description"), rs.getInt("version_high"), rs.getInt("version_mid"), rs.getInt("version_low"), rs.getString("version_postfix")));
            }
        } catch (Throwable e) {
            core_log.removeAllElements();
        }
        return core_log;
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

}
