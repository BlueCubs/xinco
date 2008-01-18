/**
 * XincoVersion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoVersion  implements java.io.Serializable {
    private int version_high;

    private int version_low;

    private int version_mid;

    private java.lang.String version_postfix;

    public XincoVersion() {
    }

    public XincoVersion(
           int version_high,
           int version_low,
           int version_mid,
           java.lang.String version_postfix) {
           this.version_high = version_high;
           this.version_low = version_low;
           this.version_mid = version_mid;
           this.version_postfix = version_postfix;
    }


    /**
     * Gets the version_high value for this XincoVersion.
     * 
     * @return version_high
     */
    public int getVersionHigh() {
        return version_high;
    }


    /**
     * Sets the version_high value for this XincoVersion.
     * 
     * @param version_high
     */
    public void setVersionHigh(int version_high) {
        this.version_high = version_high;
    }


    /**
     * Gets the version_low value for this XincoVersion.
     * 
     * @return version_low
     */
    public int getVersionLow() {
        return version_low;
    }


    /**
     * Sets the version_low value for this XincoVersion.
     * 
     * @param version_low
     */
    public void setVersionLow(int version_low) {
        this.version_low = version_low;
    }


    /**
     * Gets the version_mid value for this XincoVersion.
     * 
     * @return version_mid
     */
    public int getVersionMid() {
        return version_mid;
    }


    /**
     * Sets the version_mid value for this XincoVersion.
     * 
     * @param version_mid
     */
    public void setVersionMid(int version_mid) {
        this.version_mid = version_mid;
    }


    /**
     * Gets the version_postfix value for this XincoVersion.
     * 
     * @return version_postfix
     */
    public java.lang.String getVersionPostfix() {
        return version_postfix;
    }


    /**
     * Sets the version_postfix value for this XincoVersion.
     * 
     * @param version_postfix
     */
    public void setVersionPostfix(java.lang.String version_postfix) {
        this.version_postfix = version_postfix;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoVersion)) return false;
        XincoVersion other = (XincoVersion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.version_high == other.getVersionHigh() &&
            this.version_low == other.getVersionLow() &&
            this.version_mid == other.getVersionMid() &&
            ((this.version_postfix==null && other.getVersionPostfix()==null) || 
             (this.version_postfix!=null &&
              this.version_postfix.equals(other.getVersionPostfix())));
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
        _hashCode += getVersionHigh();
        _hashCode += getVersionLow();
        _hashCode += getVersionMid();
        if (getVersionPostfix() != null) {
            _hashCode += getVersionPostfix().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoVersion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoVersion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version_high");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version_high"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version_low");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version_low"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version_mid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version_mid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version_postfix");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version_postfix"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
