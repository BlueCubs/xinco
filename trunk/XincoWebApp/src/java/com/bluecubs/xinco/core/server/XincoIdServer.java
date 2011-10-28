package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.XincoId;
import com.bluecubs.xinco.core.server.persistence.controller.XincoIdJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoIdServer extends XincoId {

    public XincoIdServer(Integer id) throws XincoException {
        XincoIdJpaController controller = new XincoIdJpaController(
                XincoDBManager.getEntityManagerFactory());
        XincoId xincoId = controller.findXincoId(id);
        if (xincoId != null) {
            fill(xincoId);
        } else {
            throw new XincoException("XincoId with id: " + id + " not found!");
        }
    }

    public XincoIdServer(String tablename, int lastId) {
        super(tablename, lastId);
        setId(0);
    }

    private void fill(XincoId xincoId) {
        setId(xincoId.getId());
        setLastId(xincoId.getLastId());
        setTablename(xincoId.getTablename());
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            XincoIdJpaController controller = new XincoIdJpaController(
                    XincoDBManager.getEntityManagerFactory());
            XincoId xincoId;
            if (getId() > 0) {
                xincoId = controller.findXincoId(getId());
                xincoId.setId(getId());
                xincoId.setLastId(getLastId());
                xincoId.setTablename(getTablename());
                controller.edit(xincoId);
            } else {
                xincoId = new XincoId();
                xincoId.setId(getId());
                xincoId.setLastId(getLastId());
                xincoId.setTablename(getTablename());
                controller.create(xincoId);
            }
            return getId();
        } catch (Exception ex) {
            Logger.getLogger(XincoIdServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex);
        }
    }
    
    public static int deleteFromDB(XincoId id) throws XincoException {
        XincoIdJpaController controller = new XincoIdJpaController(
                XincoDBManager.getEntityManagerFactory());
        if (id != null) {
            try {
                controller.destroy(id.getId());
            } catch (NonexistentEntityException ex) {
                throw new XincoException(ex);
            }
        }
        return 0;
    }
    
    public static XincoIdServer getXincoId(String table) throws XincoException{
        HashMap parameters = new HashMap();
        parameters.put("tablename", table);
        List result = XincoDBManager.namedQuery("XincoId.findByTablename", parameters);
        if (!result.isEmpty()) {
            return new XincoIdServer(((com.bluecubs.xinco.core.server.persistence.XincoId) result.get(0)).getId());
        } else {
            throw new XincoException("Unable to find xinco id for: " + table);
        }
    }
    
    public static int getNextId(String table) throws XincoException{
        XincoIdServer xincoId = getXincoId(table);
        xincoId.setLastId(xincoId.getLastId()+1);
        xincoId.write2DB();
        return xincoId.getLastId();
    }
    
    public static List<XincoIdServer> getIds() throws XincoException{
        ArrayList<XincoIdServer> ids = new ArrayList<XincoIdServer>();
        List result = XincoDBManager.namedQuery("XincoId.findAll");
        if (!result.isEmpty()) {
            for(Object o:result){
                ids.add(new XincoIdServer(((XincoId)o).getId()));
            }
        } else {
            throw new XincoException("No ids found!");
        }
        return ids;
    }
}