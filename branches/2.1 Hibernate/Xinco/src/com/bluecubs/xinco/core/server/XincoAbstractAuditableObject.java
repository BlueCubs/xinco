package com.bluecubs.xinco.core.server;

import com.dreamer.Hibernate.Audit.AbstractAuditableObject;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class XincoAbstractAuditableObject extends AbstractAuditableObject {

    private static final long serialVersionUID = 3791544578403758791L;

    public Integer getRecordId() {
        if (id == 0) {
            XincoIDServer pis = new XincoIDServer("xinco_user_modified_record");
            id = pis.getNewID();
        }
        return id;
    }
}
