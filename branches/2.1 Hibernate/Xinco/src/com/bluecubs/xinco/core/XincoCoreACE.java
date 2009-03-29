/**
 * XincoCoreACE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreACE  implements java.io.Serializable {
    private boolean adminPermission;

    private int changerID;

    private boolean executePermission;

    private int xincoCoreDataId;

    private int xincoCoreGroupId;

    private int xincoCoreNodeId;

    private int xincoCoreUserId;

    private int id;

    private boolean readPermission;

    private boolean writePermission;

    private int userId;

    public XincoCoreACE() {
    }

    public XincoCoreACE(
           boolean adminPermission,
           int changerID,
           boolean executePermission,
           int xincoCoreDataId,
           int xincoCoreGroupId,
           int xincoCoreNodeId,
           int xincoCoreUserId,
           int id,
           boolean readPermission,
           boolean writePermission,
           int userId) {
           this.adminPermission = adminPermission;
           this.changerID = changerID;
           this.executePermission = executePermission;
           this.xincoCoreDataId = xincoCoreDataId;
           this.xincoCoreGroupId = xincoCoreGroupId;
           this.xincoCoreNodeId = xincoCoreNodeId;
           this.xincoCoreUserId = xincoCoreUserId;
           this.id = id;
           this.readPermission = readPermission;
           this.writePermission = writePermission;
           this.userId = userId;
    }


    /**
     * Gets the adminPermission value for this XincoCoreACE.
     * 
     * @return adminPermission
     */
    public boolean isAdminPermission() {
        return adminPermission;
    }


    /**
     * Sets the adminPermission value for this XincoCoreACE.
     * 
     * @param adminPermission
     */
    public void setAdminPermission(boolean adminPermission) {
        this.adminPermission = adminPermission;
    }


    /**
     * Gets the changerID value for this XincoCoreACE.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this XincoCoreACE.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }


    /**
     * Gets the executePermission value for this XincoCoreACE.
     * 
     * @return executePermission
     */
    public boolean isExecutePermission() {
        return executePermission;
    }


    /**
     * Sets the executePermission value for this XincoCoreACE.
     * 
     * @param executePermission
     */
    public void setExecutePermission(boolean executePermission) {
        this.executePermission = executePermission;
    }


    /**
     * Gets the xincoCoreDataId value for this XincoCoreACE.
     * 
     * @return xincoCoreDataId
     */
    public int getXincoCoreDataId() {
        return xincoCoreDataId;
    }


    /**
     * Sets the xincoCoreDataId value for this XincoCoreACE.
     * 
     * @param xincoCoreDataId
     */
    public void setXincoCoreDataId(int xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
    }


    /**
     * Gets the xincoCoreGroupId value for this XincoCoreACE.
     * 
     * @return xincoCoreGroupId
     */
    public int getXincoCoreGroupId() {
        return xincoCoreGroupId;
    }


    /**
     * Sets the xincoCoreGroupId value for this XincoCoreACE.
     * 
     * @param xincoCoreGroupId
     */
    public void setXincoCoreGroupId(int xincoCoreGroupId) {
        this.xincoCoreGroupId = xincoCoreGroupId;
    }


    /**
     * Gets the xincoCoreNodeId value for this XincoCoreACE.
     * 
     * @return xincoCoreNodeId
     */
    public int getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }


    /**
     * Sets the xincoCoreNodeId value for this XincoCoreACE.
     * 
     * @param xincoCoreNodeId
     */
    public void setXincoCoreNodeId(int xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
    }


    /**
     * Gets the xincoCoreUserId value for this XincoCoreACE.
     * 
     * @return xincoCoreUserId
     */
    public int getXincoCoreUserId() {
        return xincoCoreUserId;
    }


    /**
     * Sets the xincoCoreUserId value for this XincoCoreACE.
     * 
     * @param xincoCoreUserId
     */
    public void setXincoCoreUserId(int xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
    }


    /**
     * Gets the id value for this XincoCoreACE.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoCoreACE.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the readPermission value for this XincoCoreACE.
     * 
     * @return readPermission
     */
    public boolean isReadPermission() {
        return readPermission;
    }


    /**
     * Sets the readPermission value for this XincoCoreACE.
     * 
     * @param readPermission
     */
    public void setReadPermission(boolean readPermission) {
        this.readPermission = readPermission;
    }


    /**
     * Gets the writePermission value for this XincoCoreACE.
     * 
     * @return writePermission
     */
    public boolean isWritePermission() {
        return writePermission;
    }


    /**
     * Sets the writePermission value for this XincoCoreACE.
     * 
     * @param writePermission
     */
    public void setWritePermission(boolean writePermission) {
        this.writePermission = writePermission;
    }


    /**
     * Gets the userId value for this XincoCoreACE.
     * 
     * @return userId
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this XincoCoreACE.
     * 
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreACE)) return false;
        XincoCoreACE other = (XincoCoreACE) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.adminPermission == other.isAdminPermission() &&
            this.changerID == other.getChangerID() &&
            this.executePermission == other.isExecutePermission() &&
            this.xincoCoreDataId == other.getXincoCoreDataId() &&
            this.xincoCoreGroupId == other.getXincoCoreGroupId() &&
            this.xincoCoreNodeId == other.getXincoCoreNodeId() &&
            this.xincoCoreUserId == other.getXincoCoreUserId() &&
            this.id == other.getId() &&
            this.readPermission == other.isReadPermission() &&
            this.writePermission == other.isWritePermission() &&
            this.userId == other.getUserId();
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
        _hashCode += (isAdminPermission() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getChangerID();
        _hashCode += (isExecutePermission() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getXincoCoreDataId();
        _hashCode += getXincoCoreGroupId();
        _hashCode += getXincoCoreNodeId();
        _hashCode += getXincoCoreUserId();
        _hashCode += getId();
        _hashCode += (isReadPermission() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isWritePermission() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getUserId();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreACE.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreACE"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adminPermission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adminPermission"));
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
        elemField.setFieldName("executePermission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "executePermission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreDataId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreDataId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreGroupId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreGroupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreNodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreNodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreUserId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreUserId"));
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
        elemField.setFieldName("readPermission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "readPermission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("writePermission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "writePermission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userId"));
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
