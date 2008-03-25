/**
 * XincoCoreNode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.bluecubs.xinco.core;

public class XincoCoreNode implements java.io.Serializable {

    private java.lang.String designation;
    private int changerID;
    private java.util.Vector xincoCoreAcl;
    private java.util.Vector xincoCoreData;
    private com.bluecubs.xinco.core.XincoCoreLanguage xincoCoreLanguage;
    private int xincoCoreNodeId;
    private java.util.Vector xincoCoreNodes;
    private int id;
    private int statusNumber;

    public XincoCoreNode() {
    }

    public XincoCoreNode(
            java.lang.String designation,
            int changerID,
            java.util.Vector xincoCoreAcl,
            java.util.Vector xincoCoreData,
            com.bluecubs.xinco.core.XincoCoreLanguage xincoCoreLanguage,
            int xincoCoreNodeId,
            java.util.Vector xincoCoreNodes,
            int id,
            int statusNumber) {
        this.designation = designation;
        this.changerID = changerID;
        this.xincoCoreAcl = xincoCoreAcl;
        this.xincoCoreData = xincoCoreData;
        this.xincoCoreLanguage = xincoCoreLanguage;
        this.xincoCoreNodeId = xincoCoreNodeId;
        this.xincoCoreNodes = xincoCoreNodes;
        this.id = id;
        this.statusNumber = statusNumber;
    }

    /**
     * Gets the designation value for this XincoCoreNode.
     * 
     * @return designation
     */
    public java.lang.String getDesignation() {
        return designation;
    }

    /**
     * Sets the designation value for this XincoCoreNode.
     * 
     * @param designation
     */
    public void setDesignation(java.lang.String designation) {
        this.designation = designation;
    }

    /**
     * Gets the changerID value for this XincoCoreNode.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
    }

    /**
     * Sets the changerID value for this XincoCoreNode.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }

    /**
     * Gets the xincoCoreAcl value for this XincoCoreNode.
     * 
     * @return xincoCoreAcl
     */
    public java.util.Vector getXincoCoreACL() {
        return xincoCoreAcl;
    }

    /**
     * Sets the xincoCoreAcl value for this XincoCoreNode.
     * 
     * @param xincoCoreAcl
     */
    public void setXincoCoreACL(java.util.Vector xincoCoreAcl) {
        this.xincoCoreAcl = xincoCoreAcl;
    }

    /**
     * Gets the xincoCoreData value for this XincoCoreNode.
     * 
     * @return xincoCoreData
     */
    public java.util.Vector getXincoCoreData() {
        return xincoCoreData;
    }

    /**
     * Sets the xincoCoreData value for this XincoCoreNode.
     * 
     * @param xincoCoreData
     */
    public void setXincoCoreData(java.util.Vector xincoCoreData) {
        this.xincoCoreData = xincoCoreData;
    }

    /**
     * Gets the xincoCoreLanguage value for this XincoCoreNode.
     * 
     * @return xincoCoreLanguage
     */
    public com.bluecubs.xinco.core.XincoCoreLanguage getXincoCoreLanguage() {
        return xincoCoreLanguage;
    }

    /**
     * Sets the xincoCoreLanguage value for this XincoCoreNode.
     * 
     * @param xincoCoreLanguage
     */
    public void setXincoCoreLanguage(com.bluecubs.xinco.core.XincoCoreLanguage xincoCoreLanguage) {
        this.xincoCoreLanguage = xincoCoreLanguage;
    }

    /**
     * Gets the xincoCoreNodeId value for this XincoCoreNode.
     * 
     * @return xincoCoreNodeId
     */
    public int getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }

    /**
     * Sets the xincoCoreNodeId value for this XincoCoreNode.
     * 
     * @param xincoCoreNodeId
     */
    public void setXincoCoreNodeId(int xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
    }

    /**
     * Gets the xincoCoreNodes value for this XincoCoreNode.
     * 
     * @return xincoCoreNodes
     */
    public java.util.Vector getXincoCoreNodes() {
        return xincoCoreNodes;
    }

    /**
     * Sets the xincoCoreNodes value for this XincoCoreNode.
     * 
     * @param xincoCoreNodes
     */
    public void setXincoCoreNodes(java.util.Vector xincoCoreNodes) {
        this.xincoCoreNodes = xincoCoreNodes;
    }

    /**
     * Gets the id value for this XincoCoreNode.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id value for this XincoCoreNode.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the statusNumber value for this XincoCoreNode.
     * 
     * @return statusNumber
     */
    public int getStatusNumber() {
        return statusNumber;
    }

    /**
     * Sets the statusNumber value for this XincoCoreNode.
     * 
     * @param statusNumber
     */
    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }
    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreNode)) {
            return false;
        }
        XincoCoreNode other = (XincoCoreNode) obj;
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
                ((this.designation == null && other.getDesignation() == null) ||
                (this.designation != null &&
                this.designation.equals(other.getDesignation()))) &&
                this.changerID == other.getChangerID() &&
                ((this.xincoCoreAcl == null && other.getXincoCoreACL() == null) ||
                (this.xincoCoreAcl != null &&
                this.xincoCoreAcl.equals(other.getXincoCoreACL()))) &&
                ((this.xincoCoreData == null && other.getXincoCoreData() == null) ||
                (this.xincoCoreData != null &&
                this.xincoCoreData.equals(other.getXincoCoreData()))) &&
                ((this.xincoCoreLanguage == null && other.getXincoCoreLanguage() == null) ||
                (this.xincoCoreLanguage != null &&
                this.xincoCoreLanguage.equals(other.getXincoCoreLanguage()))) &&
                this.xincoCoreNodeId == other.getXincoCoreNodeId() &&
                ((this.xincoCoreNodes == null && other.getXincoCoreNodes() == null) ||
                (this.xincoCoreNodes != null &&
                this.xincoCoreNodes.equals(other.getXincoCoreNodes()))) &&
                this.id == other.getId() &&
                this.statusNumber == other.getStatusNumber();
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
        if (getXincoCoreACL() != null) {
            _hashCode += getXincoCoreACL().hashCode();
        }
        if (getXincoCoreData() != null) {
            _hashCode += getXincoCoreData().hashCode();
        }
        if (getXincoCoreLanguage() != null) {
            _hashCode += getXincoCoreLanguage().hashCode();
        }
        _hashCode += getXincoCoreNodeId();
        if (getXincoCoreNodes() != null) {
            _hashCode += getXincoCoreNodes().hashCode();
        }
        _hashCode += getId();
        _hashCode += getStatusNumber();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
            new org.apache.axis.description.TypeDesc(XincoCoreNode.class, true);
    

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreNode"));
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
        elemField.setFieldName("xincoCoreAcl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreAcl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
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
        elemField.setFieldName("xincoCoreNodes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreNodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
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
