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
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoCoreAuditTypeServer extends XincoCoreAuditType{
    private int changerID;
    private XincoCoreAuditTrailManager audit= new XincoCoreAuditTrailManager();
    /**
     * Creates a new instance of XincoCoreAuditTypeServer
     */
    public XincoCoreAuditTypeServer() {
    }
    
    public XincoCoreAuditTypeServer(int id, XincoCoreUser user,XincoDBManager DBM) {
        ResultSet rs=null;
        try {
            rs=DBM.con.createStatement().executeQuery("select * from xinco_audit_type where id="+id);
            while(rs.next()){
                setId(id);
                setDays(rs.getInt("days"));
                setWeeks(rs.getInt("weeks"));
                setMonths(rs.getInt("months"));
                setYears(rs.getInt("years"));
                setDescription(rs.getString("description"));
                setDue_same_day(rs.getBoolean("due_same_day"));
                setDue_same_week(rs.getBoolean("due_same_week"));
                setDue_same_month(rs.getBoolean("due_same_month"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
    
    public Vector getAuditTypes(){
        XincoDBManager DBM=null;
        XincoCoreAuditType temp=null;
        try {
            DBM = new XincoDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Vector types=new Vector();
        ResultSet rs=null;
        
        try {
            rs=DBM.con.createStatement().executeQuery("select distinct description from xinco_audit_type order by description");
            while(rs.next()){
                temp = new XincoCoreAuditType(rs.getInt("id"),rs.getInt("days"),rs.getInt("weeks"),
                        rs.getInt("months"),rs.getInt("years"),rs.getString("description"),rs.getBoolean("due_same_day"),
                        rs.getBoolean("due_same_week"),rs.getBoolean("due_same_month"));
                types.addElement(temp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return types;
    }
    
    public int getChangerID() {
        return changerID;
    }
    
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }
}
