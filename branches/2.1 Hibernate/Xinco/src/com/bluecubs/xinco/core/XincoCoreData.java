/**
 * XincoCoreData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreData  extends com.bluecubs.xinco.core.persistence.XincoCoreData implements java.io.Serializable {
    private java.lang.String designation;

    private java.util.Vector xinco_core_acl;

    private int changerID;

    private com.bluecubs.xinco.core.XincoCoreDataType xinco_core_data_type;

    private com.bluecubs.xinco.core.XincoCoreLanguage xinco_core_language;

    private int xinco_core_node_id;

    private int id;

    private int status_number;

    private java.util.Vector xinco_add_attributes;

    private java.util.Vector xinco_core_logs;

    public XincoCoreData() {
    }

    public XincoCoreData(
           java.lang.String designation,
           java.util.Vector xinco_core_acl,
           int changerID,
           com.bluecubs.xinco.core.XincoCoreDataType xinco_core_data_type,
           com.bluecubs.xinco.core.XincoCoreLanguage xinco_core_language,
           int xinco_core_node_id,
           int id,
           int status_number,
           java.util.Vector xinco_add_attributes,
           java.util.Vector xinco_core_logs) {
           this.designation = designation;
           this.xinco_core_acl = xinco_core_acl;
           this.changerID = changerID;
           this.xinco_core_data_type = xinco_core_data_type;
           this.xinco_core_language = xinco_core_language;
           this.xinco_core_node_id = xinco_core_node_id;
           this.id = id;
           this.status_number = status_number;
           this.xinco_add_attributes = xinco_add_attributes;
           this.xinco_core_logs = xinco_core_logs;
    }
    
    /**
     * Gets the xinco_core_acl value for this XincoCoreData.
     * 
     * @return xinco_core_acl
     */
    public java.util.Vector getXincoCoreACL() {
        return xinco_core_acl;
    }


    /**
     * Sets the xinco_core_acl value for this XincoCoreData.
     * 
     * @param xinco_core_acl
     */
    public void setXincoCoreACL(java.util.Vector xinco_core_acl) {
        this.xinco_core_acl = xinco_core_acl;
    }


    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("designation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "designation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_acl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_acl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_data_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreDataType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_language");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_language"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLanguage"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_node_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_node_id"));
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
        elemField.setFieldName("status_number");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status_number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_add_attributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_add_attributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_logs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_logs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
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
