/**
 *Copyright 2010 blueCubs.com
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

import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLogJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreUserJpaController;
import java.util.Vector;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XincoCoreLogServer extends XincoCoreLog {

    private static final long serialVersionUID = 1L;
    private static List result;
    private static HashMap parameters = new HashMap();
    private XincoCoreUser user;

    protected XincoCoreLogServer() {
    }
    //create single log object for data structures

    public XincoCoreLogServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = XincoDBManager.namedQuery("XincoCoreLog.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreLog xcl =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreLog) result.get(0);
                setId(xcl.getId());
                setXinco_core_data_id(xcl.getXincoCoreData().getId());
                setXinco_core_user_id(xcl.getXincoCoreUser().getId());
                setOp_code(xcl.getOpCode());
                setOp_datetime(new GregorianCalendar());
                getOp_datetime().setTime(xcl.getOpDatetime());
                setOp_description(xcl.getOpDescription());
                setVersion(new XincoVersion());
                getVersion().setVersion_high(xcl.getVersionHigh());
                getVersion().setVersion_mid(xcl.getVersionMid());
                getVersion().setVersion_low(xcl.getVersionLow());
                getVersion().setVersion_postfix(xcl.getVersionPostfix());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException(e.getMessage());
        }
    }

    private XincoCoreLogServer(com.bluecubs.xinco.core.server.persistence.XincoCoreLog xcl) {
        setId(xcl.getId());
        setXinco_core_data_id(xcl.getXincoCoreData().getId());
        setXinco_core_user_id(xcl.getXincoCoreUser().getId());
        setOp_code(xcl.getOpCode());
        setOp_datetime(new GregorianCalendar());
        getOp_datetime().setTime(xcl.getOpDatetime());
        setOp_description(xcl.getOpDescription());
        setVersion(new XincoVersion());
        getVersion().setVersion_high(xcl.getVersionHigh());
        getVersion().setVersion_mid(xcl.getVersionMid());
        getVersion().setVersion_low(xcl.getVersionLow());
        getVersion().setVersion_postfix(xcl.getVersionPostfix());
    }

    public void setUser(XincoCoreUserServer user) {
        this.user = user;
        setXinco_core_user_id(user.getId());
    }

    //create single log object for data structures
    public XincoCoreLogServer(int attrID, int attrCDID, int attrUID, int attrOC, Calendar attrODT, String attrOD, int attrVH, int attrVM, int attrVL, String attrVP) throws XincoException {
        setId(attrID);
        setXinco_core_data_id(attrCDID);
        setXinco_core_user_id(attrUID);
        setOp_code(attrOC);
        setOp_datetime(attrODT);
        setOp_description(attrOD);
        setVersion(new XincoVersion());
        getVersion().setVersion_high(attrVH);
        getVersion().setVersion_mid(attrVM);
        getVersion().setVersion_low(attrVL);
        getVersion().setVersion_postfix(attrVP);
    }

    //write to db
    public int write2DB() throws XincoException {
        try {
            XincoCoreLogJpaController controller = new XincoCoreLogJpaController();
            com.bluecubs.xinco.core.server.persistence.XincoCoreLog xcl;
            if (getId() > 0) {
                xcl = controller.findXincoCoreLog(getId());
                xcl.setXincoCoreData(new XincoCoreDataJpaController().findXincoCoreData(getXinco_core_data_id()));
                xcl.setXincoCoreUser(new XincoCoreUserJpaController().findXincoCoreUser(getXinco_core_user_id()));
                xcl.setOpCode(getOp_code());
                xcl.setOpDatetime(new Date());
                xcl.setOpDescription(getOp_description().replaceAll("'", "\\\\'"));
                xcl.setVersionHigh(getVersion().getVersion_high());
                xcl.setVersionMid(getVersion().getVersion_mid());
                xcl.setVersionLow(getVersion().getVersion_low());
                xcl.setVersionPostfix(getVersion().getVersion_postfix().replaceAll("'", "\\\\'"));
                controller.edit(xcl);
            } else {
                xcl = new com.bluecubs.xinco.core.server.persistence.XincoCoreLog();
                xcl.setXincoCoreData(new XincoCoreDataJpaController().findXincoCoreData(getXinco_core_data_id()));
                xcl.setXincoCoreUser(new XincoCoreUserJpaController().findXincoCoreUser(getXinco_core_user_id()));
                xcl.setOpCode(getOp_code());
                xcl.setOpDatetime(new Date());
                xcl.setOpDescription(getOp_description().replaceAll("'", "\\\\'"));
                xcl.setVersionHigh(getVersion().getVersion_high());
                xcl.setVersionMid(getVersion().getVersion_mid());
                xcl.setVersionLow(getVersion().getVersion_low());
                xcl.setVersionPostfix(getVersion().getVersion_postfix().replaceAll("'", "\\\\'"));
                controller.create(xcl);
                setId(xcl.getId());
            }
        } catch (Exception e) {
            throw new XincoException(e.getMessage());
        }
        return getId();
    }

    //create complete log list for data
    public static Vector<XincoCoreLogServer> getXincoCoreLogs(int attrID) {

        Vector<XincoCoreLogServer> core_log = new Vector<XincoCoreLogServer>();
        GregorianCalendar cal = new GregorianCalendar();

        try {
            result = XincoDBManager.createdQuery("SELECT xcl FROM XincoCoreLog xcl WHERE xcl.xincoCoreData.id=" + attrID);

            for (Object o:result) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreLog xcl =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreLog) o;
                cal = new GregorianCalendar();
                cal.setTime(xcl.getOpDatetime());
                core_log.addElement(new XincoCoreLogServer(xcl));
            }
        } catch (Exception e) {
            Logger.getLogger(XincoCoreLogServer.class.getSimpleName()).log(Level.SEVERE, null, e);
            core_log.removeAllElements();
        }
        return core_log;
    }
}
