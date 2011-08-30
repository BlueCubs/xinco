package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyBehaviorJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyTypeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoDependencyTypeServer extends XincoDependencyType {

    private HashMap parameters = new HashMap();
    private static List result;

    public XincoDependencyTypeServer() {
    }

    public XincoDependencyTypeServer(int id, int behaviorId, String designation) throws XincoException {
        XincoDependencyBehavior behavior = new XincoDependencyBehaviorJpaController(XincoDBManager.getEntityManagerFactory()).findXincoDependencyBehavior(behaviorId);
        setDesignation(designation);
        setId(id);
        if (behavior != null) {
            setXincoDependencyBehavior(behavior);
        } else {
            throw new XincoException();
        }
    }

    public XincoDependencyTypeServer(int behaviorId) throws XincoException {
        XincoDependencyType dependency = new XincoDependencyTypeJpaController(XincoDBManager.getEntityManagerFactory()).findXincoDependencyType(behaviorId);
        if (dependency != null) {
            setDescription(dependency.getDescription());
            setDesignation(dependency.getDesignation());
            setDesignation(dependency.getDesignation());
        } else {
            throw new XincoException();
        }
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            com.bluecubs.xinco.core.server.persistence.XincoDependencyType dep;
            boolean create = false;
            if (getId() > 0) {
                parameters.clear();
                parameters.put("id", getId());
                result = XincoDBManager.namedQuery("XincoDependencyType.findById", parameters);
                dep = (com.bluecubs.xinco.core.server.persistence.XincoDependencyType) result.get(0);
            } else {
                dep = new com.bluecubs.xinco.core.server.persistence.XincoDependencyType();
                create = true;
            }
            dep.setDescription(getDescription());
            dep.setDesignation(getDesignation());
            dep.setXincoDependencyBehavior(getXincoDependencyBehavior());
            if (create) {
                new XincoDependencyTypeJpaController(XincoDBManager.getEntityManagerFactory()).create(dep);
                setId(dep.getId());
            } else {
                new XincoDependencyTypeJpaController(XincoDBManager.getEntityManagerFactory()).edit(dep);
            }
        } catch (Exception e) {
            Logger.getLogger(XincoDependencyTypeServer.class.getSimpleName()).log(Level.SEVERE, null, e);
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    public static int deleteFromDB(int id) throws XincoException {
        try {
            new XincoDependencyTypeJpaController(XincoDBManager.getEntityManagerFactory()).destroy(id);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoDependencyTypeServer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoDependencyTypeServer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return 0;
    }
}
