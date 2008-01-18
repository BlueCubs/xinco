/**
 * XincoCoreDataTypeAttribute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreDataTypeAttribute  implements java.io.Serializable {
    private java.lang.String designation;

    private int changerID;

    private int attributeId;

    private java.lang.String dataType;

    private int size;

    private int xincoCoreDataTypeId;

    public XincoCoreDataTypeAttribute() {
    }

    public XincoCoreDataTypeAttribute(
           java.lang.String designation,
           int changerID,
           int attributeId,
           java.lang.String dataType,
           int size,
           int xincoCoreDataTypeId) {
           this.designation = designation;
           this.changerID = changerID;
           this.attributeId = attributeId;
           this.dataType = dataType;
           this.size = size;
           this.xincoCoreDataTypeId = xincoCoreDataTypeId;
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
     * Gets the changerID value for this XincoCoreDataTypeAttribute.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
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
     * Gets the attributeId value for this XincoCoreDataTypeAttribute.
     * 
     * @return attributeId
     */
    public int getAttributeId() {
        return attributeId;
    }


    /**
     * Sets the attributeId value for this XincoCoreDataTypeAttribute.
     * 
     * @param attributeId
     */
    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }


    /**
     * Gets the dataType value for this XincoCoreDataTypeAttribute.
     * 
     * @return dataType
     */
    public java.lang.String getDataType() {
        return dataType;
    }


    /**
     * Sets the dataType value for this XincoCoreDataTypeAttribute.
     * 
     * @param dataType
     */
    public void setDataType(java.lang.String dataType) {
        this.dataType = dataType;
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
     * Gets the xincoCoreDataTypeId value for this XincoCoreDataTypeAttribute.
     * 
     * @return xincoCoreDataTypeId
     */
    public int getXincoCoreDataTypeId() {
        return xincoCoreDataTypeId;
    }


    /**
     * Sets the xincoCoreDataTypeId value for this XincoCoreDataTypeAttribute.
     * 
     * @param xincoCoreDataTypeId
     */
    public void setXincoCoreDataTypeId(int xincoCoreDataTypeId) {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
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
            this.attributeId == other.getAttributeId() &&
            ((this.dataType==null && other.getDataType()==null) || 
             (this.dataType!=null &&
              this.dataType.equals(other.getDataType()))) &&
            this.size == other.getSize() &&
            this.xincoCoreDataTypeId == other.getXincoCoreDataTypeId();
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
        _hashCode += getAttributeId();
        if (getDataType() != null) {
            _hashCode += getDataType().hashCode();
        }
        _hashCode += getSize();
        _hashCode += getXincoCoreDataTypeId();
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
        elemField.setFieldName("attributeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataType"));
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
        elemField.setFieldName("xincoCoreDataTypeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreDataTypeId"));
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
