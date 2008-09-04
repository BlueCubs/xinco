/**
 * XincoCoreDataTypeAttribute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreDataTypeAttribute  extends com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute implements java.io.Serializable {
    private java.lang.String designation;

    private int changerID;

    private int attribute_id;

    private java.lang.String data_type;

    private int size;

    private int xinco_core_data_type_id;

    public XincoCoreDataTypeAttribute() {
    }

    public XincoCoreDataTypeAttribute(
           java.lang.String designation,
           int changerID,
           int attribute_id,
           java.lang.String data_type,
           int size,
           int xinco_core_data_type_id) {
           this.designation = designation;
           this.changerID = changerID;
           this.attribute_id = attribute_id;
           this.data_type = data_type;
           this.size = size;
           this.xinco_core_data_type_id = xinco_core_data_type_id;
    }


    /**
     * Gets the designation value for this XincoCoreDataTypeAttribute.
     * 
     * @return designation
     */
    public java.lang.String getDesignation() {
        return designation;
    }


    /**
     * Sets the designation value for this XincoCoreDataTypeAttribute.
     * 
     * @param designation
     */
    public void setDesignation(java.lang.String designation) {
        this.designation = designation;
    }

    /**
     * Sets the changerID value for this XincoCoreDataTypeAttribute.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }


    /**
     * Gets the attribute_id value for this XincoCoreDataTypeAttribute.
     * 
     * @return attribute_id
     */
    public int getAttribute_id() {
        return attribute_id;
    }


    /**
     * Sets the attribute_id value for this XincoCoreDataTypeAttribute.
     * 
     * @param attribute_id
     */
    public void setAttribute_id(int attribute_id) {
        this.attribute_id = attribute_id;
    }


    /**
     * Gets the data_type value for this XincoCoreDataTypeAttribute.
     * 
     * @return data_type
     */
    public java.lang.String getData_type() {
        return data_type;
    }


    /**
     * Sets the data_type value for this XincoCoreDataTypeAttribute.
     * 
     * @param data_type
     */
    public void setData_type(java.lang.String data_type) {
        this.data_type = data_type;
    }


    /**
     * Gets the size value for this XincoCoreDataTypeAttribute.
     * 
     * @return size
     */
    public int getSize() {
        return size;
    }


    /**
     * Sets the size value for this XincoCoreDataTypeAttribute.
     * 
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }


    /**
     * Gets the xinco_core_data_type_id value for this XincoCoreDataTypeAttribute.
     * 
     * @return xinco_core_data_type_id
     */
    public int getXinco_core_data_type_id() {
        return xinco_core_data_type_id;
    }


    /**
     * Sets the xinco_core_data_type_id value for this XincoCoreDataTypeAttribute.
     * 
     * @param xinco_core_data_type_id
     */
    public void setXinco_core_data_type_id(int xinco_core_data_type_id) {
        this.xinco_core_data_type_id = xinco_core_data_type_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreDataTypeAttribute)) return false;
        XincoCoreDataTypeAttribute other = (XincoCoreDataTypeAttribute) obj;
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
            this.attribute_id == other.getAttribute_id() &&
            ((this.data_type==null && other.getData_type()==null) || 
             (this.data_type!=null &&
              this.data_type.equals(other.getData_type()))) &&
            this.size == other.getSize() &&
            this.xinco_core_data_type_id == other.getXinco_core_data_type_id();
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
        _hashCode += getAttribute_id();
        if (getData_type() != null) {
            _hashCode += getData_type().hashCode();
        }
        _hashCode += getSize();
        _hashCode += getXinco_core_data_type_id();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreDataTypeAttribute.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreDataTypeAttribute"));
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
        elemField.setFieldName("attribute_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribute_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_data_type_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_type_id"));
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
