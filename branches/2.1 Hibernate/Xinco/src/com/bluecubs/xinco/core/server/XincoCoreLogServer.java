/**
 *Copyright 2004 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            XincoCoreLogServer
 *
 * Description:     log
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoVersion;
import java.util.Vector;
import java.util.GregorianCalendar;
import com.bluecubs.xinco.core.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.persistence.XincoCoreUser;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import com.dreamer.Hibernate.PersistenceManager;
import com.dreamer.Hibernate.conf.ConfigSingletonServer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XincoCoreLogServer extends XincoCoreLog implements PersistenceServerObject {

    private static final long serialVersionUID = -8734701061727738206L;
    private XincoCoreUser user;
    private static PersistenceManager pm = ConfigSingletonServer.getPersistenceManager();
    private static HashMap parameters = new HashMap();
    private static List result;
    private XincoVersion version;
    //create single log object for data structures
    public XincoCoreLogServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreLog.findById", parameters);
            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoCoreLog xcl = (XincoCoreLog) result.get(0);
                setId(xcl.getId());
                setXincoCoreDataId(xcl.getXincoCoreDataId());
                setXincoCoreUserId(xcl.getXincoCoreUserId());
                setOpCode(xcl.getOpCode());
                setOpDatetime(xcl.getOpDatetime());
                setOpDescription(xcl.getOpDescription());
                setVersion(new XincoVersion());
                getVersion().setVersionHigh(xcl.getVersionHigh());
                getVersion().setVersionMid(xcl.getVersionMid());
                getVersion().setVersionLow(xcl.getVersionLow());
                getVersion().setVersionPostfix(xcl.getVersionPostfix());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException();
        }

    }

    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }
    //create single log object for data structures
    public XincoCoreLogServer(int attrID, int attrCDID, int attrUID, int attrOC, Date attrODT, String attrOD, int attrVH, int attrVM, int attrVL, String attrVP) throws XincoException {

        setId(attrID);
        setXincoCoreDataId(attrCDID);
        setXincoCoreUserId(attrUID);
        setOpCode(attrOC);
        setOpDatetime(attrODT);
        setOpDescription(attrOD);
        setVersion(new XincoVersion());
        getVersion().setVersionHigh(attrVH);
        getVersion().setVersionMid(attrVM);
        getVersion().setVersionLow(attrVL);
        getVersion().setVersionPostfix(attrVP);

    }
    //create complete log list for data
    public static Vector getXincoCoreLogs(int attrID) {

        Vector core_log = new Vector();
        GregorianCalendar cal = new GregorianCalendar();
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreLog x WHERE x.xincoCoreDataId=" + attrID, null);
            while (!result.isEmpty()) {
                core_log.addElement((XincoCoreLog) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            core_log.removeAllElements();
        }
        return core_log;
    }
    
     @SuppressWarnings("unchecked")
    public int getNewID() {
        return new XincoIDServer("XincoCoreLog").getNewTableID();
    }

    public boolean write2DB() {
        try {
            if (getId() > 0) {
                pm.persist(this, true, true);
            } else {
                setId(getNewID());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public boolean deleteFromDB() {
        return pm.delete(this, true);
    }

    public XincoVersion getVersion() {
        return version;
    }

    public void setVersion(XincoVersion version) {
        this.version = version;
    }
}
