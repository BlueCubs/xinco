/**
 * Copyright 2012 blueCubs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 *
 * Name: XincoCoreLogServer
 *
 * Description: log
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLogJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreUserJpaController;
import com.bluecubs.xinco.core.server.service.XincoCoreLog;
import com.bluecubs.xinco.core.server.service.XincoVersion;
import java.util.*;
import java.util.logging.Level;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import static javax.xml.datatype.DatatypeFactory.newInstance;

public class XincoCoreLogServer extends XincoCoreLog {

    private static List result;
    private static HashMap parameters = new HashMap();
    //create single log object for data structures

    public XincoCoreLogServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = namedQuery("XincoCoreLog.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreLog xcl =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreLog) result.get(0);
                setId(xcl.getId());
                setXincoCoreDataId(xcl.getXincoCoreData().getId());
                setXincoCoreUserId(xcl.getXincoCoreUser().getId());
                setOpCode(xcl.getOpCode());
                DatatypeFactory factory = newInstance();
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(xcl.getOpDatetime());
                setOpDatetime(factory.newXMLGregorianCalendar(cal));
                setOpDescription(xcl.getOpDescription());
                setVersion(new XincoVersion());
                getVersion().setVersionHigh(xcl.getVersionHigh());
                getVersion().setVersionMid(xcl.getVersionMid());
                getVersion().setVersionLow(xcl.getVersionLow());
                getVersion().setVersionPostfix(xcl.getVersionPostfix());
            } else {
                throw new XincoException("Invalid id: " + attrID);
            }
        } catch (Exception ex) {
            getLogger(XincoCoreLogServer.class.getName()).log(SEVERE, null, ex);
            throw new XincoException(ex.getMessage());
        }
    }

    private XincoCoreLogServer(com.bluecubs.xinco.core.server.persistence.XincoCoreLog xcl) {
        try {
            setId(xcl.getId());
            setXincoCoreDataId(xcl.getXincoCoreData().getId());
            setXincoCoreUserId(xcl.getXincoCoreUser().getId());
            setOpCode(xcl.getOpCode());
            DatatypeFactory factory = newInstance();
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(xcl.getOpDatetime());
            setOpDatetime(factory.newXMLGregorianCalendar(cal));
            setOpDescription(xcl.getOpDescription());
            setVersion(new XincoVersion());
            getVersion().setVersionHigh(xcl.getVersionHigh());
            getVersion().setVersionMid(xcl.getVersionMid());
            getVersion().setVersionLow(xcl.getVersionLow());
            getVersion().setVersionPostfix(xcl.getVersionPostfix());
        } catch (DatatypeConfigurationException ex) {
            getLogger(XincoCoreLogServer.class.getName()).log(SEVERE, null, ex);
        }
    }

    public void setUser(XincoCoreUserServer user) {
        setXincoCoreUserId(user.getId());
    }

    //create single log object for data structures
    public XincoCoreLogServer(int attrCDID, int attrUID, int attrOC,
            Calendar attrODT, String attrOD, int attrVH, int attrVM,
            int attrVL, String attrVP) throws XincoException {
        try {
            setXincoCoreDataId(attrCDID);
            setXincoCoreUserId(attrUID);
            setOpCode(attrOC);
            DatatypeFactory factory = newInstance();
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(attrODT == null ? new Date() : attrODT.getTime());
            setOpDatetime(factory.newXMLGregorianCalendar(cal));
            setOpDescription(attrOD);
            setVersion(new XincoVersion());
            getVersion().setVersionHigh(attrVH);
            getVersion().setVersionMid(attrVM);
            getVersion().setVersionLow(attrVL);
            getVersion().setVersionPostfix(attrVP);
        } catch (DatatypeConfigurationException ex) {
            getLogger(XincoCoreLogServer.class.getName()).log(SEVERE, null, ex);
        }
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            XincoCoreLogJpaController controller = new XincoCoreLogJpaController(getEntityManagerFactory());
            com.bluecubs.xinco.core.server.persistence.XincoCoreLog xcl;
            if (getId() > 0) {
                xcl = controller.findXincoCoreLog(getId());
                xcl.setXincoCoreData(new XincoCoreDataJpaController(getEntityManagerFactory()).findXincoCoreData(getXincoCoreDataId()));
                xcl.setXincoCoreUser(new XincoCoreUserJpaController(getEntityManagerFactory()).findXincoCoreUser(getXincoCoreUserId()));
                xcl.setOpCode(getOpCode());
                xcl.setOpDatetime(new Date());
                xcl.setOpDescription(getOpDescription().replaceAll("'", "\\\\'"));
                xcl.setVersionHigh(getVersion().getVersionHigh());
                xcl.setVersionMid(getVersion().getVersionMid());
                xcl.setVersionLow(getVersion().getVersionLow());
                xcl.setVersionPostfix(getVersion().getVersionPostfix().replaceAll("'", "\\\\'"));
                controller.edit(xcl);
            } else {
                xcl = new com.bluecubs.xinco.core.server.persistence.XincoCoreLog();
                xcl.setXincoCoreData(new XincoCoreDataJpaController(getEntityManagerFactory()).findXincoCoreData(getXincoCoreDataId()));
                xcl.setXincoCoreUser(new XincoCoreUserJpaController(getEntityManagerFactory()).findXincoCoreUser(getXincoCoreUserId()));
                xcl.setOpCode(getOpCode());
                xcl.setOpDatetime(new Date());
                xcl.setOpDescription(getOpDescription().replaceAll("'", "\\\\'"));
                xcl.setVersionHigh(getVersion().getVersionHigh());
                xcl.setVersionMid(getVersion().getVersionMid());
                xcl.setVersionLow(getVersion().getVersionLow());
                xcl.setVersionPostfix(getVersion().getVersionPostfix().replaceAll("'", "\\\\'"));
                controller.create(xcl);
            }
            setId(xcl.getId());
        } catch (Exception e) {
            getLogger(XincoCoreLogServer.class.getSimpleName()).log(SEVERE, null, e);
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    //create complete log list for data
    public static List<XincoCoreLogServer> getXincoCoreLogs(int attrID) {

        ArrayList<XincoCoreLogServer> coreLog = 
                new ArrayList<>();
        GregorianCalendar cal;

        try {
            result = createdQuery(
                    "SELECT xcl FROM XincoCoreLog xcl WHERE "
                    + "xcl.xincoCoreData.id=" + attrID + " order by xcl.id");
            for (Iterator it = result.iterator(); it.hasNext();) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreLog xcl =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreLog) it.next();
                cal = new GregorianCalendar();
                cal.setTime(xcl.getOpDatetime());
                coreLog.add(new XincoCoreLogServer(xcl));
            }
        } catch (Exception e) {
            getLogger(XincoCoreLogServer.class.getSimpleName());
            coreLog.clear();
        }
        return coreLog;
    }
}
