/**
 * XincoCoreACE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

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
 * Name:            XincoCoreACE
 *
 * Description:     access control entry 
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

package com.bluecubs.xinco.core;

public class XincoCoreACE  implements java.io.Serializable {
    private boolean admin_permission;
    private boolean execute_permission;
    private int xinco_core_data_id;
    private int xinco_core_group_id;
    private int xinco_core_node_id;
    private int xinco_core_user_id;
    private int id;
    private boolean read_permission;
    private boolean write_permission;

    public XincoCoreACE() {
    }

    public boolean isAdmin_permission() {
        return admin_permission;
    }

    public void setAdmin_permission(boolean admin_permission) {
        this.admin_permission = admin_permission;
    }

    public boolean isExecute_permission() {
        return execute_permission;
    }

    public void setExecute_permission(boolean execute_permission) {
        this.execute_permission = execute_permission;
    }

    public int getXinco_core_data_id() {
        return xinco_core_data_id;
    }

    public void setXinco_core_data_id(int xinco_core_data_id) {
        this.xinco_core_data_id = xinco_core_data_id;
    }

    public int getXinco_core_group_id() {
        return xinco_core_group_id;
    }

    public void setXinco_core_group_id(int xinco_core_group_id) {
        this.xinco_core_group_id = xinco_core_group_id;
    }

    public int getXinco_core_node_id() {
        return xinco_core_node_id;
    }

    public void setXinco_core_node_id(int xinco_core_node_id) {
        this.xinco_core_node_id = xinco_core_node_id;
    }

    public int getXinco_core_user_id() {
        return xinco_core_user_id;
    }

    public void setXinco_core_user_id(int xinco_core_user_id) {
        this.xinco_core_user_id = xinco_core_user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRead_permission() {
        return read_permission;
    }

    public void setRead_permission(boolean read_permission) {
        this.read_permission = read_permission;
    }

    public boolean isWrite_permission() {
        return write_permission;
    }

    public void setWrite_permission(boolean write_permission) {
        this.write_permission = write_permission;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreACE)) return false;
        XincoCoreACE other = (XincoCoreACE) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.admin_permission == other.isAdmin_permission() &&
            this.execute_permission == other.isExecute_permission() &&
            this.xinco_core_data_id == other.getXinco_core_data_id() &&
            this.xinco_core_group_id == other.getXinco_core_group_id() &&
            this.xinco_core_node_id == other.getXinco_core_node_id() &&
            this.xinco_core_user_id == other.getXinco_core_user_id() &&
            this.id == other.getId() &&
            this.read_permission == other.isRead_permission() &&
            this.write_permission == other.isWrite_permission();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += new Boolean(isAdmin_permission()).hashCode();
        _hashCode += new Boolean(isExecute_permission()).hashCode();
        _hashCode += getXinco_core_data_id();
        _hashCode += getXinco_core_group_id();
        _hashCode += getXinco_core_node_id();
        _hashCode += getXinco_core_user_id();
        _hashCode += getId();
        _hashCode += new Boolean(isRead_permission()).hashCode();
        _hashCode += new Boolean(isWrite_permission()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreACE.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreACE"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("admin_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "admin_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("execute_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "execute_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_data_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_group_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_group_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_node_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_node_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_user_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_user_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("read_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "read_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("write_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "write_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
