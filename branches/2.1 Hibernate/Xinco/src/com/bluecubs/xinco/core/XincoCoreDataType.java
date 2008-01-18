/**
 * XincoCoreDataType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreDataType  implements java.io.Serializable {
    private java.lang.String designation;

    private int id;

    private int changerID;

    private java.lang.String description;

    private java.util.Vector xincoCoreDataTypeAttributes;

    public XincoCoreDataType() {
    }

    public XincoCoreDataType(
           java.lang.String designation,
           int id,
           int changerID,
           java.lang.String description,
           java.util.Vector xincoCoreDataTypeAttributes) {
           this.designation = designation;
           this.id = id;
           this.changerID = changerID;
           this.description = description;
           this.xincoCoreDataTypeAttributes = xincoCoreDataTypeAttributes;
    }


    /**
     * Gets the designation value for this XincoCoreDataType.
     * 
     * @return designation
     */
    public java.lang.String getDesignation() {
        return designation;
    }


    /**
     * Sets the designation value for this XincoCoreDataType.
     * 
     * @param designation
     */
    public void setDesignation(java.lang.String designation) {
        this.designation = designation;
    }


    /**
     * Gets the id value for this XincoCoreDataType.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoCoreDataType.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the changerID value for this XincoCoreDataType.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this XincoCoreDataType.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }


    /**
     * Gets the description value for this XincoCoreDataType.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this XincoCoreDataType.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the xincoCoreDataTypeAttributes value for this XincoCoreDataType.
     * 
     * @return xincoCoreDataTypeAttributes
     */
    public java.util.Vector getXincoCoreDataTypeAttributes() {
        return xincoCoreDataTypeAttributes;
    }


    /**
     * Sets the xincoCoreDataTypeAttributes value for this XincoCoreDataType.
     * 
     * @param xincoCoreDataTypeAttributes
     */
    public void setXincoCoreDataTypeAttributes(java.util.Vector xincoCoreDataTypeAttributes) {
        this.xincoCoreDataTypeAttributes = xincoCoreDataTypeAttributes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreDataType)) return false;
        XincoCoreDataType other = (XincoCoreDataType) obj;
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
            this.id == other.getId() &&
            this.changerID == other.getChangerID() &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.xincoCoreDataTypeAttributes==null && other.getXincoCoreDataTypeAttributes()==null) || 
             (this.xincoCoreDataTypeAttributes!=null &&
              this.xincoCoreDataTypeAttributes.equals(other.getXincoCoreDataTypeAttributes())));
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
        _hashCode += getId();
        _hashCode += getChangerID();
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getXincoCoreDataTypeAttributes() != null) {
            _hashCode += getXincoCoreDataTypeAttributes().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreDataType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreDataType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("designation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "designation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreDataTypeAttributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreDataTypeAttributes"));
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
