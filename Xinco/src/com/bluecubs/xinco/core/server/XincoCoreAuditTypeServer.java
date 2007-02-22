/**
 *Copyright February 19, 2007 blueCubs.com
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
 * Name:            XincoCoreAuditTypeServer
 *
 * Description:     XincoCoreAuditTypeServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            February 19, 2007, 11:34 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *
 *************************************************************
 */

package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoCoreAuditType;
import com.bluecubs.xinco.core.XincoException;
import java.sql.SQLException;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoCoreAuditTypeServer extends XincoCoreAuditType{
    XincoCoreAuditTrailManager audit= new XincoCoreAuditTrailManager();
    /**
     * Creates a new instance of XincoCoreAuditTypeServer
     */
    public XincoCoreAuditTypeServer(int id, XincoDBManager DBM) {
        
    }
    
    public XincoCoreAuditTypeServer(
            int id,
            int days,
            int weeks,
            int months,
            int years,
            java.lang.String description,
            boolean due_same_day,
            boolean due_same_week,
            boolean due_same_month,
            int changerID) {
        setId(id);
        setDays(days);
        setWeeks(weeks);
        setMonths(months);
        setYears(years);
        setDescription(description);
        setDue_same_day(due_same_day);
        setDue_same_week(due_same_week);
        setDue_same_month(due_same_month);
    }
    
    private void write2DB(XincoDBManager DBM) throws XincoException{
        if(getId()>0){
            try {
                DBM.con.createStatement().executeUpdate("update xinco_audit_type set id="+
                        getId()+", days="+getDays()+", weeks="+getWeeks()+", months="+getMonths()+
                        ", years="+getYears()+", description='"+getDescription()+"', due_same_day="+isDue_same_day()+
                        ", due_same_week="+isDue_same_week()+", due_same_month="+isDue_same_month());
                audit.updateAuditTrail("xinco_audit_type",new String [] {"id ="+getId()},
                        DBM,"audit.general.modified",this.getChangerID());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else{
            try {
                setId(DBM.getNewID("xinco_audit_type"));
                DBM.con.createStatement().executeUpdate("insert into xinco_audit_type values("+getId()+", "+
                        getDays()+", "+getWeeks()+", "+getMonths()+", "+getYears()+", "+
                        getDescription()+", "+isDue_same_day()+", "+isDue_same_week()
                        +", "+isDue_same_month()+")");
                audit.updateAuditTrail("xinco_audit_type",new String [] {"id ="+getId()},
                        DBM,"audit.general.create",this.getChangerID());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
