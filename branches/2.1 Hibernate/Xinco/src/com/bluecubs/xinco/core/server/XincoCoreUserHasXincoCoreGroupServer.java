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
 * Name:            XincoCoreUserServer
 *
 * Description:     user
 *
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 *************************************************************
 */

package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.hibernate.audit.AbstractAuditableObject;
import com.bluecubs.xinco.core.hibernate.audit.AuditableDAO;
import com.bluecubs.xinco.core.hibernate.audit.PersistenceServerObject;
import java.util.HashMap;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserHasXincoCoreGroupServer extends XincoCoreUserHasXincoCoreGroup implements AuditableDAO, PersistenceServerObject{

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

    public boolean write2DB() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean deleteFromDB() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
