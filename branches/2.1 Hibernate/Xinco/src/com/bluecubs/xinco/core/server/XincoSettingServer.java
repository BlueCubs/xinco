package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.persistence.XincoSetting;
import com.bluecubs.xinco.core.XincoException;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.List;

/**
 *
 * @author Javier
 */
class XincoSettingServer extends XincoSetting implements AuditableDAO, PersistenceServerObject {

    private static List result;

    @SuppressWarnings("unchecked")
    static XincoSetting getSetting(String desc) {
        parameters.clear();
        parameters.put("description", desc);
        result=pm.namedQuery("XincoSetting.findByDescription", parameters);
        if(result.isEmpty())
            throw new XincoException("setting.exception.notfound");
        return (XincoSetting)result.get(0);
    }
}
