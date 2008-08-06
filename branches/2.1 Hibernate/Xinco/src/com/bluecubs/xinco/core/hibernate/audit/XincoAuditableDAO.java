/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.hibernate.audit;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.dreamer.Hibernate.Audit.AuditableDAO;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface XincoAuditableDAO extends AuditableDAO {

    XincoDBManager pm = XincoConfigSingletonServer.getPersistenceManager();
}
