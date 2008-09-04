/**
 * XincoVersion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoVersion  implements java.io.Serializable {
    private int versionHigh;

    private int versionLow;

    private int versionMid;

    private java.lang.String versionPostfix;

    public XincoVersion() {
    }

    public XincoVersion(
           int versionHigh,
           int versionLow,
           int versionMid,
           java.lang.String versionPostfix) {
           this.versionHigh = versionHigh;
           this.versionLow = versionLow;
           this.versionMid = versionMid;
           this.versionPostfix = versionPostfix;
    }


    /**
     * Gets the versionHigh value for this XincoVersion.
     * 
     * @return versionHigh
     */
    public int getVersionHigh() {
        return versionHigh;
    }


    /**
     * Sets the versionHigh value for this XincoVersion.
     * 
     * @param versionHigh
     */
    public void setVersionHigh(int versionHigh) {
        this.versionHigh = versionHigh;
    }


    /**
     * Gets the versionLow value for this XincoVersion.
     * 
     * @return versionLow
     */
    public int getVersionLow() {
        return versionLow;
    }


    /**
     * Sets the versionLow value for this XincoVersion.
     * 
     * @param versionLow
     */
    public void setVersionLow(int versionLow) {
        this.versionLow = versionLow;
    }


    /**
     * Gets the versionMid value for this XincoVersion.
     * 
     * @return versionMid
     */
    public int getVersionMid() {
        return versionMid;
    }


    /**
     * Sets the versionMid value for this XincoVersion.
     * 
     * @param versionMid
     */
    public void setVersionMid(int versionMid) {
        this.versionMid = versionMid;
    }


    /**
     * Gets the versionPostfix value for this XincoVersion.
     * 
     * @return versionPostfix
     */
    public java.lang.String getVersionPostfix() {
        return versionPostfix;
    }


    /**
     * Sets the versionPostfix value for this XincoVersion.
     * 
     * @param versionPostfix
     */
    public void setVersionPostfix(java.lang.String versionPostfix) {
        this.versionPostfix = versionPostfix;
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
            this.versionHigh == other.getVersionHigh() &&
            this.versionLow == other.getVersionLow() &&
            this.versionMid == other.getVersionMid() &&
            ((this.versionPostfix==null && other.getVersionPostfix()==null) || 
             (this.versionPostfix!=null &&
              this.versionPostfix.equals(other.getVersionPostfix())));
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
        elemField.setFieldName("versionHigh");
        elemField.setXmlName(new javax.xml.namespace.QName("", "versionHigh"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versionLow");
        elemField.setXmlName(new javax.xml.namespace.QName("", "versionLow"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versionMid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "versionMid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versionPostfix");
        elemField.setXmlName(new javax.xml.namespace.QName("", "versionPostfix"));
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
