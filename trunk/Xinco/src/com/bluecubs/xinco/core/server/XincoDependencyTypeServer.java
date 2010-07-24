package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyBehaviorJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyTypeJpaController;
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

    public XincoDependencyTypeServer(int id,int behaviorId, String designation) {
        XincoDependencyBehavior behavior = new XincoDependencyBehaviorJpaController().findXincoDependencyBehavior(behaviorId);
        setDesignation(designation);
        setId(id);
        if (behavior != null) {
            setXincoDependencyBehavior(behavior);
        } else {
            throw new XincoException();
        }
    }

    public XincoDependencyTypeServer(int id, int behaviorId) {
        XincoDependencyType dependency = new XincoDependencyTypeJpaController().findXincoDependencyType(behaviorId);
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
                new XincoDependencyTypeJpaController().create(dep);
                setId(dep.getId());
            } else {
                new XincoDependencyTypeJpaController().edit(dep);
            }
        } catch (Exception e) {
            Logger.getLogger(XincoDependencyTypeServer.class.getSimpleName()).log(Level.SEVERE, null, e);
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    public static int deleteFromDB(int id) {
        new XincoDependencyTypeJpaController().destroy(id);
        return 0;
    }
}
