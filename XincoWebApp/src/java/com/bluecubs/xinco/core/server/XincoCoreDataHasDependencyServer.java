package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataHasDependencyJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataHasDependencyServer extends XincoCoreDataHasDependency {

    private static HashMap parameters = new HashMap();
    private static List result;

    public XincoCoreDataHasDependencyServer(int xincoCoreDataParentId, int xincoCoreDataChildrenId, int dependencyTypeId) throws XincoException {
        parameters.clear();
        parameters.put("xincoCoreDataParentId", xincoCoreDataParentId);
        parameters.put("xincoCoreDataChildrenId", xincoCoreDataChildrenId);
        parameters.put("dependencyTypeId", dependencyTypeId);
        result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreDataHasDependency x WHERE "
                + "x.xincoCoreDataHasDependencyPK.xincoCoreDataParentId = :xincoCoreDataParentId "
                + "and x.xincoCoreDataHasDependencyPK.xincoCoreDataChildrenId = :xincoCoreDataChildrenId "
                + "and x.xincoCoreDataHasDependencyPK.dependencyTypeId = :dependencyTypeId", parameters);
        if (!result.isEmpty()) {
            //Existing one
            XincoCoreDataHasDependency xcdhd = (XincoCoreDataHasDependency) result.get(0);
            setXincoCoreData(xcdhd.getXincoCoreData());
            setXincoCoreData1(xcdhd.getXincoCoreData1());
            setXincoDependencyType(xcdhd.getXincoDependencyType());
            setXincoCoreDataHasDependencyPK(xcdhd.getXincoCoreDataHasDependencyPK());
        } else {
            throw new XincoException();
        }
    }

    public static List<XincoCoreDataServer> getRenderings(int id) throws XincoException {
        ArrayList<XincoCoreDataServer> renderings = new ArrayList<XincoCoreDataServer>();
        parameters.clear();
        parameters.put("xincoCoreDataParentId", id);
        for (Iterator<Object> it = XincoDBManager.namedQuery(
                "XincoCoreDataHasDependency.findByXincoCoreDataParentId", parameters).iterator(); it.hasNext();) {
            XincoCoreDataHasDependency dep = (XincoCoreDataHasDependency) it.next();
            renderings.add(new XincoCoreDataServer(dep.getXincoCoreData1()));
        }
        return renderings;
    }

    public XincoCoreDataHasDependencyServer(XincoCoreData parent,
            XincoCoreData child, XincoDependencyType type) {
        setXincoCoreData(child);
        setXincoCoreData1(parent);
        setXincoDependencyType(type);
        setXincoCoreDataHasDependencyPK(new XincoCoreDataHasDependencyPK(parent.getId(),
                child.getId(), type.getId()));
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            XincoCoreDataHasDependency xcdhd = new XincoCoreDataHasDependency(
                    getXincoCoreData1().getId(),
                    getXincoCoreData().getId(),
                    getXincoDependencyType().getId());
            xcdhd.setXincoCoreData(getXincoCoreData());
            xcdhd.setXincoCoreData1(getXincoCoreData1());
            xcdhd.setXincoDependencyType(getXincoDependencyType());
            new XincoCoreDataHasDependencyJpaController(XincoDBManager.getEntityManagerFactory()).create(xcdhd);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(XincoCoreDataHasDependencyServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataHasDependencyServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex.getLocalizedMessage());
        }
        return 0;
    }

    public static int deleteFromDB(int xincoCoreDataParentId, int xincoCoreDataChildrenId, int dependencyTypeId) throws XincoException {
        try {
            parameters.clear();
            parameters.put("xincoCoreDataParentId", xincoCoreDataParentId);
            parameters.put("xincoCoreDataChildrenId", xincoCoreDataChildrenId);
            parameters.put("dependencyTypeId", dependencyTypeId);
            result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreDataHasDependency x WHERE "
                    + "x.xincoCoreDataHasDependencyPK.xincoCoreDataParentId = :xincoCoreDataParentId "
                    + "and x.xincoCoreDataHasDependencyPK.xincoCoreDataChildrenId = :xincoCoreDataChildrenId "
                    + "and x.xincoCoreDataHasDependencyPK.dependencyTypeId = :dependencyTypeId", parameters);
            if (!result.isEmpty()) {
                new XincoCoreDataHasDependencyJpaController(XincoDBManager.getEntityManagerFactory()).destroy(
                        ((XincoCoreDataHasDependency) result.get(0)).getXincoCoreDataHasDependencyPK());
            }
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataHasDependencyServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex.getLocalizedMessage());
        }
        return 0;
    }
}
