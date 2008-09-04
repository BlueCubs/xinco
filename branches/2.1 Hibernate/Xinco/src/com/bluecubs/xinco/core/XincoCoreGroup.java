/**
 * XincoCoreGroup.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreGroup  extends com.bluecubs.xinco.core.persistence.XincoCoreGroup implements java.io.Serializable {
    private java.lang.String designation;

    private int changerID;

    private int id;

    private int status_number;

    public XincoCoreGroup() {
    }

    public XincoCoreGroup(
           java.lang.String designation,
           int changerID,
           int id,
           int status_number) {
           this.designation = designation;
           this.changerID = changerID;
           this.id = id;
           this.status_number = status_number;
    }


    /**
     * Gets the designation value for this XincoCoreGroup.
     * 
     * @return designation
     */
    public java.lang.String getDesignation() {
        return designation;
    }


    /**
     * Sets the designation value for this XincoCoreGroup.
     * 
     * @param designation
     */
    public void setDesignation(java.lang.String designation) {
        this.designation = designation;
    }

    /**
     * Sets the changerID value for this XincoCoreGroup.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }


    /**
     * Gets the id value for this XincoCoreGroup.
     * 
     * @return id
     */
    public Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoCoreGroup.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the status_number value for this XincoCoreGroup.
     * 
     * @return status_number
     */
    public int getStatus_number() {
        return status_number;
    }


    /**
     * Sets the status_number value for this XincoCoreGroup.
     * 
     * @param status_number
     */
    public void setStatus_number(int status_number) {
        this.status_number = status_number;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreGroup)) return false;
        XincoCoreGroup other = (XincoCoreGroup) obj;
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
            this.changerID == other.getChangerID() &&
            this.id == other.getId() &&
            this.status_number == other.getStatus_number();
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
        _hashCode += getChangerID();
        _hashCode += getId();
        _hashCode += getStatus_number();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreGroup.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreGroup"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("designation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "designation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changerID"));
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
