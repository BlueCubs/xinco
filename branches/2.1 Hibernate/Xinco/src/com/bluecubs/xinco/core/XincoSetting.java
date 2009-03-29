/**
 * XincoSetting.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoSetting  implements java.io.Serializable {
    private int id;

    private java.lang.String description;

    private int intValue;

    private java.lang.String stringValue;

    private boolean boolValue;

    private int changerID;

    private long longValue;

    private java.util.Vector xincoSettings;

    public XincoSetting() {
    }

    public XincoSetting(
           int id,
           java.lang.String description,
           int intValue,
           java.lang.String stringValue,
           boolean boolValue,
           int changerID,
           long longValue,
           java.util.Vector xincoSettings) {
           this.id = id;
           this.description = description;
           this.intValue = intValue;
           this.stringValue = stringValue;
           this.boolValue = boolValue;
           this.changerID = changerID;
           this.longValue = longValue;
           this.xincoSettings = xincoSettings;
    }


    /**
     * Gets the id value for this XincoSetting.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoSetting.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the description value for this XincoSetting.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this XincoSetting.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the intValue value for this XincoSetting.
     * 
     * @return intValue
     */
    public int getIntValue() {
        return intValue;
    }


    /**
     * Sets the intValue value for this XincoSetting.
     * 
     * @param intValue
     */
    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }


    /**
     * Gets the stringValue value for this XincoSetting.
     * 
     * @return stringValue
     */
    public java.lang.String getStringValue() {
        return stringValue;
    }


    /**
     * Sets the stringValue value for this XincoSetting.
     * 
     * @param stringValue
     */
    public void setStringValue(java.lang.String stringValue) {
        this.stringValue = stringValue;
    }


    /**
     * Gets the boolValue value for this XincoSetting.
     * 
     * @return boolValue
     */
    public boolean isBoolValue() {
        return boolValue;
    }


    /**
     * Sets the boolValue value for this XincoSetting.
     * 
     * @param boolValue
     */
    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }


    /**
     * Gets the changerID value for this XincoSetting.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this XincoSetting.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }


    /**
     * Gets the longValue value for this XincoSetting.
     * 
     * @return longValue
     */
    public long getLongValue() {
        return longValue;
    }


    /**
     * Sets the longValue value for this XincoSetting.
     * 
     * @param longValue
     */
    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }


    /**
     * Gets the xincoSettings value for this XincoSetting.
     * 
     * @return xincoSettings
     */
    public java.util.Vector getXincoSettings() {
        return xincoSettings;
    }


    /**
     * Sets the xincoSettings value for this XincoSetting.
     * 
     * @param xincoSettings
     */
    public void setXincoSettings(java.util.Vector xincoSettings) {
        this.xincoSettings = xincoSettings;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoSetting)) return false;
        XincoSetting other = (XincoSetting) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.intValue == other.getIntValue() &&
            ((this.stringValue==null && other.getStringValue()==null) || 
             (this.stringValue!=null &&
              this.stringValue.equals(other.getStringValue()))) &&
            this.boolValue == other.isBoolValue() &&
            this.changerID == other.getChangerID() &&
            this.longValue == other.getLongValue() &&
            ((this.xincoSettings==null && other.getXincoSettings()==null) || 
             (this.xincoSettings!=null &&
              this.xincoSettings.equals(other.getXincoSettings())));
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
        _hashCode += getId();
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += getIntValue();
        if (getStringValue() != null) {
            _hashCode += getStringValue().hashCode();
        }
        _hashCode += (isBoolValue() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getChangerID();
        _hashCode += new Long(getLongValue()).hashCode();
        if (getXincoSettings() != null) {
            _hashCode += getXincoSettings().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoSetting.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoSetting"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("intValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "intValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stringValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stringValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boolValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "boolValue"));
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
        elemField.setFieldName("longValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "longValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoSettings");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoSettings"));
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
