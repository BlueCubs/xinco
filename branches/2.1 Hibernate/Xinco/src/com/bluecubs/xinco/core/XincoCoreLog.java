/**
 * XincoCoreLog.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.bluecubs.xinco.core;

import java.util.Date;

public class XincoCoreLog implements java.io.Serializable {

    private int id;
    private int opCode;
    private int changerID;
    private Date opDatetime;
    private java.lang.String opDescription;
    private com.bluecubs.xinco.core.XincoVersion version;
    private int xincoCoreDataId;
    private int xincoCoreUserId;

    public XincoCoreLog() {
    }

    public XincoCoreLog(
            int id,
            int opCode,
            int changerID,
            Date opDatetime,
            java.lang.String opDescription,
            com.bluecubs.xinco.core.XincoVersion version,
            int xincoCoreDataId,
            int xincoCoreUserId) {
        this.id = id;
        this.opCode = opCode;
        this.changerID = changerID;
        this.opDatetime = opDatetime;
        this.opDescription = opDescription;
        this.version = version;
        this.xincoCoreDataId = xincoCoreDataId;
        this.xincoCoreUserId = xincoCoreUserId;
    }

    /**
     * Gets the id value for this XincoCoreLog.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id value for this XincoCoreLog.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the opCode value for this XincoCoreLog.
     * 
     * @return opCode
     */
    public int getOpCode() {
        return opCode;
    }

    /**
     * Sets the opCode value for this XincoCoreLog.
     * 
     * @param opCode
     */
    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    /**
     * Gets the changerID value for this XincoCoreLog.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
    }

    /**
     * Sets the changerID value for this XincoCoreLog.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }

    /**
     * Gets the opDatetime value for this XincoCoreLog.
     * 
     * @return opDatetime
     */
    public Date getOpDatetime() {
        return opDatetime;
    }

    /**
     * Sets the opDatetime value for this XincoCoreLog.
     * 
     * @param opDatetime
     */
    public void setOpDatetime(Date opDatetime) {
        this.opDatetime = opDatetime;
    }

    /**
     * Gets the opDescription value for this XincoCoreLog.
     * 
     * @return opDescription
     */
    public java.lang.String getOpDescription() {
        return opDescription;
    }

    /**
     * Sets the opDescription value for this XincoCoreLog.
     * 
     * @param opDescription
     */
    public void setOpDescription(java.lang.String opDescription) {
        this.opDescription = opDescription;
    }

    /**
     * Gets the version value for this XincoCoreLog.
     * 
     * @return version
     */
    public com.bluecubs.xinco.core.XincoVersion getVersion() {
        return version;
    }

    /**
     * Sets the version value for this XincoCoreLog.
     * 
     * @param version
     */
    public void setVersion(com.bluecubs.xinco.core.XincoVersion version) {
        this.version = version;
    }

    /**
     * Gets the xincoCoreDataId value for this XincoCoreLog.
     * 
     * @return xincoCoreDataId
     */
    public int getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    /**
     * Sets the xincoCoreDataId value for this XincoCoreLog.
     * 
     * @param xincoCoreDataId
     */
    public void setXincoCoreDataId(int xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
    }

    /**
     * Gets the xincoCoreUserId value for this XincoCoreLog.
     * 
     * @return xincoCoreUserId
     */
    public int getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    /**
     * Sets the xincoCoreUserId value for this XincoCoreLog.
     * 
     * @param xincoCoreUserId
     */
    public void setXincoCoreUserId(int xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
    }
    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreLog)) {
            return false;
        }
        XincoCoreLog other = (XincoCoreLog) obj;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
                this.id == other.getId() &&
                this.opCode == other.getOpCode() &&
                this.changerID == other.getChangerID() &&
                ((this.opDatetime == null && other.getOpDatetime() == null) ||
                (this.opDatetime != null &&
                this.opDatetime.equals(other.getOpDatetime()))) &&
                ((this.opDescription == null && other.getOpDescription() == null) ||
                (this.opDescription != null &&
                this.opDescription.equals(other.getOpDescription()))) &&
                ((this.version == null && other.getVersion() == null) ||
                (this.version != null &&
                this.version.equals(other.getVersion()))) &&
                this.xincoCoreDataId == other.getXincoCoreDataId() &&
                this.xincoCoreUserId == other.getXincoCoreUserId();
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
        _hashCode += getOpCode();
        _hashCode += getChangerID();
        if (getOpDatetime() != null) {
            _hashCode += getOpDatetime().hashCode();
        }
        if (getOpDescription() != null) {
            _hashCode += getOpDescription().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        _hashCode += getXincoCoreDataId();
        _hashCode += getXincoCoreUserId();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
            new org.apache.axis.description.TypeDesc(XincoCoreLog.class, true);
    

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLog"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "opCode"));
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
        elemField.setFieldName("opDatetime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "opDatetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "opDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoVersion"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreDataId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreDataId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreUserId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreUserId"));
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
        return new org.apache.axis.encoding.ser.BeanSerializer(
                _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
            java.lang.String mechType,
            java.lang.Class _javaType,
            javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(
                _javaType, _xmlType, typeDesc);
    }
}
