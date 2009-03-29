package com.bluecubs.xinco.core.hibernate.audit;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.dreamer.hibernate.audit.AuditableDAO;
import com.dreamer.hibernate.PersistenceManager;
import java.util.ResourceBundle;

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
public interface XincoAuditableDAO extends AuditableDAO {

    ResourceBundle rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
    PersistenceManager pm = XincoConfigSingletonServer.getPersistenceManager();
}
