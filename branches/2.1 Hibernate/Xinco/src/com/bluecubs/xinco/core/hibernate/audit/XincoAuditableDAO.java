package com.bluecubs.xinco.core.hibernate.audit;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.PersistenceManager;
import java.util.ResourceBundle;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface XincoAuditableDAO extends AuditableDAO {
    ResourceBundle rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
    PersistenceManager pm = XincoConfigSingletonServer.getPersistenceManager();
}
