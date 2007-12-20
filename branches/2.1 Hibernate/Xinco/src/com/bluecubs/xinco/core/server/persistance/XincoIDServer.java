/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.persistance.XincoId;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoIDServer extends XincoId{

    private XincoPersistanceManager pm = new XincoPersistanceManager();
    private List result;

    @SuppressWarnings("unchecked")
    public XincoIDServer(String table) {
        XincoId temp;
        HashMap p = new HashMap();
        p.put("tablename", table);
        result = pm.namedQuery("XincoId.findByTablename", p);
        if (result.size() > 0) {
            temp = ((XincoId) result.get(0));
            setLastId(temp.getLastId());
            setTablename(temp.getTablename());
        } else {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, "Parameter table name is incorrect: " + table);
        }
    }
    
    /**
     * Get a new id
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewID() {
        HashMap p = new HashMap();
        p.put("tablename", getTablename());
        result = pm.namedQuery("XincoId.findByTablename", p);
        if (result.size() > 0) {
            XincoId temp = ((XincoId) result.get(0));
            setLastId(temp.getLastId() + 1);
            setTablename(temp.getTablename());
        }else
            return -1;
        write2DB(true);
        return getLastId();
    }

    public boolean delete(boolean atomic) {
        return pm.delete(new XincoId(getTablename()), atomic);
    }

    public boolean write2DB(boolean atomic) {
        return pm.persist(new XincoId(getTablename(), getLastId()), !getTablename().equals(null), atomic);
    }

    public int getChangerID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setChangerID(int changerID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
