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

    private int changerID,xinco_core_data_type_id,xinco_core_language_id;

    private com.bluecubs.xinco.core.XincoCoreDataType xincoCoreDataType;

    private com.bluecubs.xinco.core.XincoCoreLanguage xincoCoreLanguage;

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
           this.xincoCoreDataType = xinco_core_data_type;
           this.xincoCoreLanguage = xinco_core_language;
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
     * Gets the xinco_core_data_type_id value for this XincoCoreData.
     * 
     * @return xinco_core_data_type_id
     */
    public com.bluecubs.xinco.core.XincoCoreDataType getXincoCoreDataType() {
        return getXincoCoreDataType();
    }


    /**
     * Sets the xinco_core_data_type_id value for this XincoCoreData.
     * 
     * @param xinco_core_data_type_id
     */
    public void setXincoCoreDataTypeId(int xinco_core_data_type_id) {
        this.setXinco_core_data_type_id(xinco_core_data_type_id);
    }


    /**
     * Gets the xincoCoreLanguage value for this XincoCoreData.
     * 
     * @return xincoCoreLanguage
     */
    public com.bluecubs.xinco.core.XincoCoreLanguage getXincoCoreLanguage() {
        return xincoCoreLanguage;
    }


    /**
     * Sets the xincoCoreLanguage value for this XincoCoreData.
     * 
     * @param xincoCoreLanguage
     */
    public void setXincoCoreLanguage(com.bluecubs.xinco.core.XincoCoreLanguage xinco_core_language) {
        this.xincoCoreLanguage = xinco_core_language;
    }


    /**
     * Gets the xinco_core_node_id value for this XincoCoreData.
     * 
     * @return xinco_core_node_id
     */
    public int getXincoCoreNodeId() {
        return xinco_core_node_id;
    }


    /**
     * Sets the xinco_core_node_id value for this XincoCoreData.
     * 
     * @param xinco_core_node_id
     */
    public void setXincoCoreNodeId(int xinco_core_node_id) {
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
    public int getStatusNumber() {
        return status_number;
    }


    /**
     * Sets the status_number value for this XincoCoreData.
     * 
     * @param status_number
     */
    public void setStatusNumber(int status_number) {
        this.status_number = status_number;
    }


    /**
     * Gets the xinco_add_attributes value for this XincoCoreData.
     * 
     * @return xinco_add_attributes
     */
    public java.util.Vector getXincoAddAttributes() {
        return xinco_add_attributes;
    }


    /**
     * Sets the xinco_add_attributes value for this XincoCoreData.
     * 
     * @param xinco_add_attributes
     */
    public void setXincoAddAttributes(java.util.Vector xinco_add_attributes) {
        this.xinco_add_attributes = xinco_add_attributes;
    }


    /**
     * Gets the xinco_core_logs value for this XincoCoreData.
     * 
     * @return xinco_core_logs
     */
    public java.util.Vector getXincoCoreLogs() {
        return xinco_core_logs;
    }


    /**
     * Sets the xinco_core_logs value for this XincoCoreData.
     * 
     * @param xinco_core_logs
     */
    public void setXincoCoreLogs(java.util.Vector xinco_core_logs) {
        this.xinco_core_logs = xinco_core_logs;
    }

    private java.lang.Object __equalsCalc = null;
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
            ((this.xinco_core_acl==null && other.getXincoCoreACL()==null) || 
             (this.xinco_core_acl!=null &&
              this.xinco_core_acl.equals(other.getXincoCoreACL()))) &&
            this.changerID == other.getChangerID() &&
            ((this.getXincoCoreDataType()==null && other.getXincoCoreDataType()==null) ||
             (this.getXincoCoreDataType()!=null &&
              this.getXincoCoreDataType().equals(other.getXincoCoreDataType()))) &&
            ((this.getXincoCoreLanguage()==null && other.getXincoCoreLanguage()==null) || 
             (this.getXincoCoreLanguage()!=null &&
              this.getXincoCoreLanguage().equals(other.getXincoCoreLanguage()))) &&
            this.xinco_core_node_id == other.getXincoCoreNodeId() &&
            this.id == other.getId() &&
            this.status_number == other.getStatusNumber() &&
            ((this.xinco_add_attributes==null && other.getXincoAddAttributes()==null) || 
             (this.xinco_add_attributes!=null &&
              this.xinco_add_attributes.equals(other.getXincoAddAttributes()))) &&
            ((this.xinco_core_logs==null && other.getXincoCoreLogs()==null) || 
             (this.xinco_core_logs!=null &&
              this.xinco_core_logs.equals(other.getXincoCoreLogs())));
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
        if (getDesignation() != null) {
            _hashCode += getDesignation().hashCode();
        }
        if (getXincoCoreACL() != null) {
            _hashCode += getXincoCoreACL().hashCode();
        }
        _hashCode += getChangerID();
        if (getXincoCoreDataType() != null) {
            _hashCode += getXincoCoreDataType().hashCode();
        }
        if (getXincoCoreLanguage() != null) {
            _hashCode += getXincoCoreLanguage().hashCode();
        }
        _hashCode += getXincoCoreNodeId();
        _hashCode += getId();
        _hashCode += getStatusNumber();
        if (getXincoAddAttributes() != null) {
            _hashCode += getXincoAddAttributes().hashCode();
        }
        if (getXincoCoreLogs() != null) {
            _hashCode += getXincoCoreLogs().hashCode();
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

    public int getXincoCoreDataTypeId() {
        return xinco_core_data_type_id;
    }

    public void setXinco_core_data_type_id(int xinco_core_data_type_id) {
        this.xinco_core_data_type_id = xinco_core_data_type_id;
    }

    public int getXincoCoreLanguageId() {
        return xinco_core_language_id;
    }

    public void setXincoCoreLanguageId(int xinco_core_language_id) {
        this.xinco_core_language_id = xinco_core_language_id;
    }

    public void setXincoCoreDataType(com.bluecubs.xinco.core.XincoCoreDataType xincoCoreDataType) {
        this.xincoCoreDataType = xincoCoreDataType;
    }

}
