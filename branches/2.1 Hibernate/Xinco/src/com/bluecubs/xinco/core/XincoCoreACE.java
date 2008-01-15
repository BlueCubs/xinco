/**
 * XincoCoreACE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.bluecubs.xinco.core;

public class XincoCoreACE extends com.bluecubs.xinco.core.persistence.XincoCoreACE implements java.io.Serializable {

    private boolean admin_permission;
    private int changerID;
    private boolean execute_permission;
    private int xincoCoreDataId;
    private int xincoCore_groupId;
    private int xincoCoreNodeId;
    private int xincoCoreUserId;
    private int id;
    private boolean read_permission;
    private boolean write_permission;

    public XincoCoreACE() {
    }

    public XincoCoreACE(
            boolean admin_permission,
            int changerID,
            boolean execute_permission,
            int xincoCoreDataId,
            int xincoCore_groupId,
            int xincoCoreNodeId,
            int xincoCoreUserId,
            int id,
            boolean read_permission,
            boolean write_permission) {
        this.admin_permission = admin_permission;
        this.changerID = changerID;
        this.execute_permission = execute_permission;
        this.xincoCoreDataId = xincoCoreDataId;
        this.xincoCore_groupId = xincoCore_groupId;
        this.xincoCoreNodeId = xincoCoreNodeId;
        this.xincoCoreUserId = xincoCoreUserId;
        this.id = id;
        this.read_permission = read_permission;
        this.write_permission = write_permission;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
            new org.apache.axis.description.TypeDesc(XincoCoreACE.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreACE"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("admin_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "admin_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("execute_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "execute_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreDataId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreDataId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCore_groupId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCore_groupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreNodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreNodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreUserId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreUserId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("read_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "read_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("write_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "write_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     * @return 
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     * @param mechType 
     * @param _javaType 
     * @param _xmlType 
     * @return
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
            java.lang.String mechType,
            java.lang.Class _javaType,
            javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(
                _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     * @param mechType 
     * @param _javaType 
     * @param _xmlType 
     * @return
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
            java.lang.String mechType,
            java.lang.Class _javaType,
            javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(
                _javaType, _xmlType, typeDesc);
    }
}
