/**
 * XincoCoreData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreData  implements java.io.Serializable {
    private java.lang.String designation;

    private java.util.Vector xincoCoreACL;

    private int changerID;

    private com.bluecubs.xinco.core.XincoCoreDataType xincoCoreDataType;

    private com.bluecubs.xinco.core.XincoCoreLanguage xincoCoreLanguage;

    private int xincoCoreNodeId;

    private int id;

    private int statusNumber;

    private java.util.Vector xincoAddAttributes;

    private java.util.Vector xincoCoreLogs;

    public XincoCoreData() {
    }

    public XincoCoreData(
           java.lang.String designation,
           java.util.Vector xincoCoreACL,
           int changerID,
           com.bluecubs.xinco.core.XincoCoreDataType xincoCoreDataType,
           com.bluecubs.xinco.core.XincoCoreLanguage xincoCoreLanguage,
           int xincoCoreNodeId,
           int id,
           int statusNumber,
           java.util.Vector xincoAddAttributes,
           java.util.Vector xincoCoreLogs) {
           this.designation = designation;
           this.xincoCoreACL = xincoCoreACL;
           this.changerID = changerID;
           this.xincoCoreDataType = xincoCoreDataType;
           this.xincoCoreLanguage = xincoCoreLanguage;
           this.xincoCoreNodeId = xincoCoreNodeId;
           this.id = id;
           this.statusNumber = statusNumber;
           this.xincoAddAttributes = xincoAddAttributes;
           this.xincoCoreLogs = xincoCoreLogs;
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
     * Gets the xincoCoreACL value for this XincoCoreData.
     * 
     * @return xincoCoreACL
     */
    public java.util.Vector getXincoCoreACL() {
        return xincoCoreACL;
    }


    /**
     * Sets the xincoCoreACL value for this XincoCoreData.
     * 
     * @param xincoCoreACL
     */
    public void setXincoCoreACL(java.util.Vector xincoCoreACL) {
        this.xincoCoreACL = xincoCoreACL;
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
     * Gets the xincoCoreDataType value for this XincoCoreData.
     * 
     * @return xincoCoreDataType
     */
    public com.bluecubs.xinco.core.XincoCoreDataType getXincoCoreDataType() {
        return xincoCoreDataType;
    }


    /**
     * Sets the xincoCoreDataType value for this XincoCoreData.
     * 
     * @param xincoCoreDataType
     */
    public void setXincoCoreDataType(com.bluecubs.xinco.core.XincoCoreDataType xincoCoreDataType) {
        this.xincoCoreDataType = xincoCoreDataType;
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
    public void setXincoCoreLanguage(com.bluecubs.xinco.core.XincoCoreLanguage xincoCoreLanguage) {
        this.xincoCoreLanguage = xincoCoreLanguage;
    }


    /**
     * Gets the xincoCoreNodeId value for this XincoCoreData.
     * 
     * @return xincoCoreNodeId
     */
    public int getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }


    /**
     * Sets the xincoCoreNodeId value for this XincoCoreData.
     * 
     * @param xincoCoreNodeId
     */
    public void setXincoCoreNodeId(int xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
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
     * Gets the statusNumber value for this XincoCoreData.
     * 
     * @return statusNumber
     */
    public int getStatusNumber() {
        return statusNumber;
    }


    /**
     * Sets the statusNumber value for this XincoCoreData.
     * 
     * @param statusNumber
     */
    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }


    /**
     * Gets the xincoAddAttributes value for this XincoCoreData.
     * 
     * @return xincoAddAttributes
     */
    public java.util.Vector getXincoAddAttributes() {
        return xincoAddAttributes;
    }


    /**
     * Sets the xincoAddAttributes value for this XincoCoreData.
     * 
     * @param xincoAddAttributes
     */
    public void setXincoAddAttributes(java.util.Vector xincoAddAttributes) {
        this.xincoAddAttributes = xincoAddAttributes;
    }


    /**
     * Gets the xincoCoreLogs value for this XincoCoreData.
     * 
     * @return xincoCoreLogs
     */
    public java.util.Vector getXincoCoreLogs() {
        return xincoCoreLogs;
    }


    /**
     * Sets the xincoCoreLogs value for this XincoCoreData.
     * 
     * @param xincoCoreLogs
     */
    public void setXincoCoreLogs(java.util.Vector xincoCoreLogs) {
        this.xincoCoreLogs = xincoCoreLogs;
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
            ((this.xincoCoreACL==null && other.getXincoCoreACL()==null) || 
             (this.xincoCoreACL!=null &&
              this.xincoCoreACL.equals(other.getXincoCoreACL()))) &&
            this.changerID == other.getChangerID() &&
            ((this.xincoCoreDataType==null && other.getXincoCoreDataType()==null) || 
             (this.xincoCoreDataType!=null &&
              this.xincoCoreDataType.equals(other.getXincoCoreDataType()))) &&
            ((this.xincoCoreLanguage==null && other.getXincoCoreLanguage()==null) || 
             (this.xincoCoreLanguage!=null &&
              this.xincoCoreLanguage.equals(other.getXincoCoreLanguage()))) &&
            this.xincoCoreNodeId == other.getXincoCoreNodeId() &&
            this.id == other.getId() &&
            this.statusNumber == other.getStatusNumber() &&
            ((this.xincoAddAttributes==null && other.getXincoAddAttributes()==null) || 
             (this.xincoAddAttributes!=null &&
              this.xincoAddAttributes.equals(other.getXincoAddAttributes()))) &&
            ((this.xincoCoreLogs==null && other.getXincoCoreLogs()==null) || 
             (this.xincoCoreLogs!=null &&
              this.xincoCoreLogs.equals(other.getXincoCoreLogs())));
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
        elemField.setFieldName("xincoCoreACL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreACL"));
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
        elemField.setFieldName("xincoCoreDataType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreDataType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreDataType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreLanguage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreLanguage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLanguage"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreNodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreNodeId"));
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
        elemField.setFieldName("statusNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "statusNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoAddAttributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoAddAttributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreLogs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreLogs"));
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
