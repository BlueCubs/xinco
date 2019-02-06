/**
 * XincoCoreData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreData  implements java.io.Serializable {
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
     * Gets the designation value for this XincoCoreData.
     * 
     * @return designation
     */
    public java.lang.String getDesignation() {
        return designation;
    }


    /**
     * Sets the designation value for this XincoCoreData.
     * 
     * @param designation
     */
    public void setDesignation(java.lang.String designation) {
        this.designation = designation;
    }


    /**
     * Gets the xinco_core_acl value for this XincoCoreData.
     * 
     * @return xinco_core_acl
     */
    public java.util.Vector getXinco_core_acl() {
        return xinco_core_acl;
    }


    /**
     * Sets the xinco_core_acl value for this XincoCoreData.
     * 
     * @param xinco_core_acl
     */
    public void setXinco_core_acl(java.util.Vector xinco_core_acl) {
        this.xinco_core_acl = xinco_core_acl;
    }


    /**
     * Gets the changerID value for this XincoCoreData.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this XincoCoreData.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }


    /**
     * Gets the xinco_core_data_type value for this XincoCoreData.
     * 
     * @return xinco_core_data_type
     */
    public com.bluecubs.xinco.core.XincoCoreDataType getXinco_core_data_type() {
        return xinco_core_data_type;
    }


    /**
     * Sets the xinco_core_data_type value for this XincoCoreData.
     * 
     * @param xinco_core_data_type
     */
    public void setXinco_core_data_type(com.bluecubs.xinco.core.XincoCoreDataType xinco_core_data_type) {
        this.xinco_core_data_type = xinco_core_data_type;
    }


    /**
     * Gets the xinco_core_language value for this XincoCoreData.
     * 
     * @return xinco_core_language
     */
    public com.bluecubs.xinco.core.XincoCoreLanguage getXinco_core_language() {
        return xinco_core_language;
    }


    /**
     * Sets the xinco_core_language value for this XincoCoreData.
     * 
     * @param xinco_core_language
     */
    public void setXinco_core_language(com.bluecubs.xinco.core.XincoCoreLanguage xinco_core_language) {
        this.xinco_core_language = xinco_core_language;
    }


    /**
     * Gets the xinco_core_node_id value for this XincoCoreData.
     * 
     * @return xinco_core_node_id
     */
    public int getXinco_core_node_id() {
        return xinco_core_node_id;
    }


    /**
     * Sets the xinco_core_node_id value for this XincoCoreData.
     * 
     * @param xinco_core_node_id
     */
    public void setXinco_core_node_id(int xinco_core_node_id) {
        this.xinco_core_node_id = xinco_core_node_id;
    }


    /**
     * Gets the id value for this XincoCoreData.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoCoreData.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the status_number value for this XincoCoreData.
     * 
     * @return status_number
     */
    public int getStatus_number() {
        return status_number;
    }


    /**
     * Sets the status_number value for this XincoCoreData.
     * 
     * @param status_number
     */
    public void setStatus_number(int status_number) {
        this.status_number = status_number;
    }


    /**
     * Gets the xinco_add_attributes value for this XincoCoreData.
     * 
     * @return xinco_add_attributes
     */
    public java.util.Vector getXinco_add_attributes() {
        return xinco_add_attributes;
    }


    /**
     * Sets the xinco_add_attributes value for this XincoCoreData.
     * 
     * @param xinco_add_attributes
     */
    public void setXinco_add_attributes(java.util.Vector xinco_add_attributes) {
        this.xinco_add_attributes = xinco_add_attributes;
    }


    /**
     * Gets the xinco_core_logs value for this XincoCoreData.
     * 
     * @return xinco_core_logs
     */
    public java.util.Vector getXinco_core_logs() {
        return xinco_core_logs;
    }


    /**
     * Sets the xinco_core_logs value for this XincoCoreData.
     * 
     * @param xinco_core_logs
     */
    public void setXinco_core_logs(java.util.Vector xinco_core_logs) {
        this.xinco_core_logs = xinco_core_logs;
    }

    private java.lang.Object __equalsCalc = null;
    @Override
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreData)) return false;
        XincoCoreData other = (XincoCoreData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.designation==null && other.getDesignation()==null) || 
             (this.designation!=null &&
              this.designation.equals(other.getDesignation()))) &&
            ((this.xinco_core_acl==null && other.getXinco_core_acl()==null) || 
             (this.xinco_core_acl!=null &&
              this.xinco_core_acl.equals(other.getXinco_core_acl()))) &&
            this.changerID == other.getChangerID() &&
            ((this.xinco_core_data_type==null && other.getXinco_core_data_type()==null) || 
             (this.xinco_core_data_type!=null &&
              this.xinco_core_data_type.equals(other.getXinco_core_data_type()))) &&
            ((this.xinco_core_language==null && other.getXinco_core_language()==null) || 
             (this.xinco_core_language!=null &&
              this.xinco_core_language.equals(other.getXinco_core_language()))) &&
            this.xinco_core_node_id == other.getXinco_core_node_id() &&
            this.id == other.getId() &&
            this.status_number == other.getStatus_number() &&
            ((this.xinco_add_attributes==null && other.getXinco_add_attributes()==null) || 
             (this.xinco_add_attributes!=null &&
              this.xinco_add_attributes.equals(other.getXinco_add_attributes()))) &&
            ((this.xinco_core_logs==null && other.getXinco_core_logs()==null) || 
             (this.xinco_core_logs!=null &&
              this.xinco_core_logs.equals(other.getXinco_core_logs())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    @Override
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDesignation() != null) {
            _hashCode += getDesignation().hashCode();
        }
        if (getXinco_core_acl() != null) {
            _hashCode += getXinco_core_acl().hashCode();
        }
        _hashCode += getChangerID();
        if (getXinco_core_data_type() != null) {
            _hashCode += getXinco_core_data_type().hashCode();
        }
        if (getXinco_core_language() != null) {
            _hashCode += getXinco_core_language().hashCode();
        }
        _hashCode += getXinco_core_node_id();
        _hashCode += getId();
        _hashCode += getStatus_number();
        if (getXinco_add_attributes() != null) {
            _hashCode += getXinco_add_attributes().hashCode();
        }
        if (getXinco_core_logs() != null) {
            _hashCode += getXinco_core_logs().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
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
