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

import com.bluecubs.xinco.core.Vector;
import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoVersion;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.util.ResourceBundle;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class XincoCoreLogServer extends XincoCoreLog {

    private static DatatypeFactory df;
    private XincoCoreUser user;
    //create single log object for data structures
    public XincoCoreLogServer(int attrID, XincoDBManager DBM, DatatypeFactory df) throws XincoException {

        try {

            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_log WHERE id=" + attrID);

            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setXincoCoreDataId(rs.getInt("xinco_core_data_id"));
                setXincoCoreUserId(rs.getInt("xinco_core_user_id"));
                setOpCode(rs.getInt("op_code"));
                df = DatatypeFactory.newInstance();
                Calendar cal = (Calendar) rs.getDate("op_datetime").clone();
                XMLGregorianCalendar xgc = df.newXMLGregorianCalendar();
                xgc.setYear(cal.get(Calendar.YEAR) - 1900);
                xgc.setMonth(cal.get(Calendar.MONTH));
                xgc.setDay(cal.get(Calendar.DAY_OF_YEAR));
                xgc.setHour(cal.get(Calendar.HOUR_OF_DAY));
                xgc.setMinute(cal.get(Calendar.MINUTE));
                xgc.setSecond(cal.get(Calendar.SECOND));
                xgc.setMillisecond(cal.get(Calendar.MILLISECOND));
                setOpDatetime(xgc);
                setOpDescription(rs.getString("op_description"));
                setVersion(new XincoVersion());
                getVersion().setVersionHigh(rs.getInt("version_high"));
                getVersion().setVersionMid(rs.getInt("version_mid"));
                getVersion().setVersionLow(rs.getInt("version_low"));
                getVersion().setVersionPostfix(rs.getString("version_postfix"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }

            stmt.close();

        } catch (Exception e) {
            throw new XincoException();
        }

    }

    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }

    //create single log object for data structures
    public XincoCoreLogServer(int attrID, int attrCDID, int attrUID, int attrOC, XMLGregorianCalendar attrODT, String attrOD, int attrVH, int attrVM, int attrVL, String attrVP) throws XincoException {

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

    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {

        try {

            if (getId() > 0) {
                Statement stmt = DBM.getConnection().createStatement();
                XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
                ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
                audit.updateAuditTrail("xinco_core_log", new String[]{"id =" + getId()},
                        DBM, xerb.getString("audit.log.change"), this.getChangerID());
                stmt.executeUpdate("UPDATE xinco_core_log SET xinco_core_data_id=" + getXincoCoreDataId() + ", xinco_core_user_id=" + getXincoCoreUserId() + ", op_code=" + getOpCode() + ", op_datetime=now(), op_description='" + getOpDescription().replaceAll("'", "\\\\'") + "', version_high=" + getVersion().getVersionHigh() + ", version_mid=" + getVersion().getVersionMid() + ", version_low=" + getVersion().getVersionLow() + ", version_postfix='" + getVersion().getVersionPostfix().replaceAll("'", "\\\\'") + "' WHERE id=" + getId());
                stmt.close();
            } else {
                setId(DBM.getNewID("xinco_core_log"));

                Statement stmt = DBM.getConnection().createStatement();
                stmt.executeUpdate("INSERT INTO xinco_core_log VALUES (" + getId() + ", " + getXincoCoreDataId() + ", " + getXincoCoreUserId() + ", " + getOpCode() + ", now(), '" + getOpDescription().replaceAll("'", "\\\\'") + "', " + getVersion().getVersionHigh() + ", " + getVersion().getVersionMid() + ", " + getVersion().getVersionLow() + ", '" + getVersion().getVersionPostfix().replaceAll("'", "\\\\'") + "')");
                stmt.close();
            }

            DBM.getConnection().commit();

        } catch (Exception e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }

        return getId();

    }

    //create complete log list for data
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreLogs(int attrID, XincoDBManager DBM) {

        Vector core_log = new Vector();

        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_log WHERE xinco_core_data_id=" + attrID);

            while (rs.next()) {
                df = DatatypeFactory.newInstance();
                Calendar cal = (Calendar) rs.getDate("op_datetime").clone();
                XMLGregorianCalendar xgc = df.newXMLGregorianCalendar();
                xgc.setYear(cal.get(Calendar.YEAR) - 1900);
                xgc.setMonth(cal.get(Calendar.MONTH));
                xgc.setDay(cal.get(Calendar.DAY_OF_YEAR));
                xgc.setHour(cal.get(Calendar.HOUR_OF_DAY));
                xgc.setMinute(cal.get(Calendar.MINUTE));
                xgc.setSecond(cal.get(Calendar.SECOND));
                xgc.setMillisecond(cal.get(Calendar.MILLISECOND));
                cal = new GregorianCalendar();
                cal.setTime(rs.getTimestamp("op_datetime"));
                core_log.getItem().add(new XincoCoreLogServer(rs.getInt("id"), rs.getInt("xinco_core_data_id"), rs.getInt("xinco_core_user_id"), rs.getInt("op_code"), xgc, rs.getString("op_description"), rs.getInt("version_high"), rs.getInt("version_mid"), rs.getInt("version_low"), rs.getString("version_postfix")));
            }
            stmt.close();
        } catch (Exception e) {
            core_log.removeAllElements();
        }
        return core_log;
    }
}
