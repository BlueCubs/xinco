/**
 *Copyright 2008 blueCubs.com
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
 * Name:            XincoAuditable
 *
 * Description:     Helper for Auditable Data Access Objects. Adapted from 
 *                  AbstractAuditableObject within oness package. 
 *
 * Original Author: Javier A. Ortiz
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.dreamer.Hibernate.audit;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.server.persistence.XincoIDServer;

import com.bluecubs.xinco.persistence.XincoCoreUserModifiedRecord;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.PersistenceManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Based on @link net.sf.oness.common.model.auditing.AbstractAuditableObject
 * @author Javier A. Ortiz
 */
public abstract class XincoAbstractAuditableObject extends AbstractAuditableObject {

    private XincoCoreUserModifiedRecord xcumr;
    @SuppressWarnings("static-access")
    private static PersistenceManager pm = XincoConfigSingletonServer.getInstance().getPersistenceManager();

    /**
     * @see net.sf.oness.common.model.auditing.Auditable#getRecordId()
     */
    public Integer getRecordId() {
        if (id == 0) {
            XincoIDServer xis = new XincoIDServer("xinco_core_user_modified_record");
            id = xis.getNewTableID();
        }
        return id;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#getXincoCoreUserModifiedRecord()
     */
    public XincoCoreUserModifiedRecord getXincoCoreUserModifiedRecord() {
        return xcumr;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#setXincoCoreUserModifiedRecord()
     */
    public void setXincoCoreUserModifiedRecord(XincoCoreUserModifiedRecord xcumr) {
        this.xcumr = xcumr;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#saveAuditData()
     */
    public boolean saveAuditData(PersistenceManager pm) {
        pm.persist(getXincoCoreUserModifiedRecord(), false, false);
        return pm.isTransactionOk();
    }

    /**
     * Remove from DB (static)
     * @param o AbstractAuditableObject
     * @param userID User ID
     * @return int
     * @throws com.bluecubs.xinco.core.exception.XincoException
     */
    public static boolean removeFromDB(AuditableDAO o, int userID) throws XincoException {
        try {
            AuditingDAOHelper.delete(o, userID);
        } catch (Throwable e) {
            Logger.getLogger(AbstractAuditableObject.class.getName()).log(Level.SEVERE, null, e);
            throw new XincoException();
        }
        return true;
    }
}
