/**
 *Copyright June 8, 2007 blueCubs.com
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
 * Name:            PropertyServer.java
 *
 * Description:     PropertyServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 8, 2007, 2:17 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.workflow.Property;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PropertyServer extends Property{
    private ResultSet rs;
    private final int NODE=1,TRANSACTION=2,ACTIVITY=3;
    /** Creates a new instance of PropertyServer */
    public PropertyServer(int id, WorkflowDBManager DBM) {
        if(id>0){
            try {
                String sql="select * from " +
                        "property where id="+id;
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                rs=DBM.getStatement().executeQuery(sql);
                if(rs.next()){
                    setId(rs.getInt("id"));
                    setDescription(rs.getString("description"));
                    setStringProperty(rs.getString("propertyString"));
                    setBoolProperty(rs.getBoolean("propertyBool"));
                    setIntProperty(rs.getInt("propertyInt"));
                    setLongProperty(rs.getLong("propertyLong"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public PropertyServer(int id, String description, String stringProperty,
            int intProperty, long longProperty, boolean boolProperty) {
        setId(id);
        setDescription(description);
        setStringProperty(stringProperty);
        setIntProperty(intProperty);
        setLongProperty(longProperty);
        setBoolProperty(boolProperty);
    }
    
    public PropertyServer(){
        
    }
    
    public Vector getPropertiesForNode(int id, WorkflowDBManager DBM){
        return getProperties(id,this.NODE,DBM);
    }
    
    public Vector getPropertiesForTransaction(int id, WorkflowDBManager DBM){
        return getProperties(id,this.TRANSACTION,DBM);
    }
    
    public Vector getPropertiesForActivity(int id, WorkflowDBManager DBM){
        return getProperties(id,this.ACTIVITY,DBM);
    }
    
    private Vector getProperties(int id,int type, WorkflowDBManager DBM){
        Vector properties = new Vector();
        String sql = "select property_id from ";
        switch(type){
            case NODE:sql+="node_has_property where node_id="+id;
            break;
            case TRANSACTION:sql+="transaction_has_property where transaction_id="+id;
            break;
            case ACTIVITY:sql+="activity_has_property where activity_id="+id;
            break;
        }
        try {
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println(sql);
            rs=DBM.getStatement().executeQuery(sql);
            while(rs.next()){
                PropertyServer temp=new PropertyServer(rs.getInt("property_id"),DBM);
                if(temp.getDescription()!=null){
                    properties.add(temp);
                    if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                        System.out.println("Added property: \n"+temp.toString());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
    
    public boolean compare(Object o){
        try {
            if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                System.out.println("Comparing properties...");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        boolean equals=false;
        PropertyServer temp=((PropertyServer)o);
        equals= String.valueOf(temp.getIntProperty()).equals(String.valueOf(this.getIntProperty())) &&
                String.valueOf(temp.getLongProperty()).equals(String.valueOf(this.getLongProperty())) &&
                temp.getStringProperty()==null && this.getStringProperty()== null || temp.getStringProperty().equals(this.getStringProperty()) &&
                temp.isBoolProperty()==this.isBoolProperty();
        try {
            if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                System.out.println("Comparing: \n1: ");
                System.out.println(o.toString()+"\nand 2:\n");
                System.out.println(this.toString());
                System.out.println("Result: "+equals);
            }
        } catch (Exception ex) {
            try {
                if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return equals;
    }
    
    /**
     * Verify if all properties from vector o are in vector o2 with the same values
     * @param o Vector containing the properties that must be fulfilled
     * @param o2 Vector containing the properties to match
     * @return True if all properties in vector o exist in 02 with the same values
     */
    public boolean compare(Vector o, Vector o2){
        try {
            if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                System.out.println("Comparing vectors...");
                System.out.println("Vector 1 size: "+o.size());
                System.out.println("Vector 2 size: "+o2.size());
            }
            
            boolean found = false;
            if(o==null || o.size()==0 || o2.size()==0 || o2==null){
                if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                    System.out.println("Vector 1 is null?: "+o==null);
                    System.out.println("Vector 2 is null?: "+o2==null);
                    System.out.println("Vector 1 size is 0?: "+(o.size()==0));
                    System.out.println("Vector 2 size is 0?: "+(o2.size()==0));
                }
                return false;
            } else{
                if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                    System.out.println("Valid vectors to compare...");
                }
                for(int i=0;i<o.size();i++){
                    for(int j=0;j<o2.size();j++) {
                        if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                            System.out.println("Comparing properties\n1. "+((PropertyServer)o.get(i)).getDescription()+
                                    "\n2. "+((PropertyServer)o2.get(j)).getDescription());
                        }
                        if(((PropertyServer)o.get(i)).getDescription().equals(((PropertyServer)o2.get(j)).getDescription())){
                            //Found a matching property.
                            if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                                System.out.println("Found a matching property!");
                            }
                            found=true;
                            if(!((PropertyServer)o.get(i)).compare(((PropertyServer)o2.get(j)))){
                                if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                                    System.out.println("Properties are diffrent:\n1. \n"+((PropertyServer)o.get(j)).toString()+"\n2. \n"+
                                            ((PropertyServer)o2.get(j)).toString());
                                }
                                return false;
                            }
                        }
                    }
                    if(!found){
                        if(new WorkflowDBManager().getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value()){
                            System.out.println("Matching property not found");
                        }
                        return false;
                    }
                    found=false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public String toString(){
        String s="";
        s+="Description: "+this.getDescription()+"\n";
        s+="ID: "+this.getId()+"\n";
        s+="Int property: "+this.getIntProperty()+"\n";
        s+="Long property: "+this.getLongProperty()+"\n";
        s+="String Property: "+this.getStringProperty()+"\n";
        s+="Boolean property: "+this.isBoolProperty()+"\n";
        return s;
    }
}
