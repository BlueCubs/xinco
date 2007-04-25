/**
 * XincoCoreACE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreACE  implements java.io.Serializable {
    private boolean admin_permission;

    private int changerID;

    private boolean execute_permission;

    private int xinco_core_data_id;

    private int xinco_core_group_id;

    private int xinco_core_node_id;

    private int xinco_core_user_id;

    private int id;

    private boolean read_permission;

    private boolean write_permission;

    public XincoCoreACE() {
    }

    public XincoCoreACE(
           boolean admin_permission,
           int changerID,
           boolean execute_permission,
           int xinco_core_data_id,
           int xinco_core_group_id,
           int xinco_core_node_id,
           int xinco_core_user_id,
           int id,
           boolean read_permission,
           boolean write_permission) {
           this.admin_permission = admin_permission;
           this.changerID = changerID;
           this.execute_permission = execute_permission;
           this.xinco_core_data_id = xinco_core_data_id;
           this.xinco_core_group_id = xinco_core_group_id;
           this.xinco_core_node_id = xinco_core_node_id;
           this.xinco_core_user_id = xinco_core_user_id;
           this.id = id;
           this.read_permission = read_permission;
           this.write_permission = write_permission;
    }


    /**
     * Gets the admin_permission value for this XincoCoreACE.
     * 
     * @return admin_permission
     */
    public boolean isAdmin_permission() {
        return admin_permission;
    }


    /**
     * Sets the admin_permission value for this XincoCoreACE.
     * 
     * @param admin_permission
     */
    public void setAdmin_permission(boolean admin_permission) {
        this.admin_permission = admin_permission;
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
     * Gets the execute_permission value for this XincoCoreACE.
     * 
     * @return execute_permission
     */
    public boolean isExecute_permission() {
        return execute_permission;
    }


    /**
     * Sets the execute_permission value for this XincoCoreACE.
     * 
     * @param execute_permission
     */
    public void setExecute_permission(boolean execute_permission) {
        this.execute_permission = execute_permission;
    }


    /**
     * Gets the xinco_core_data_id value for this XincoCoreACE.
     * 
     * @return xinco_core_data_id
     */
    public int getXinco_core_data_id() {
        return xinco_core_data_id;
    }


    /**
     * Sets the xinco_core_data_id value for this XincoCoreACE.
     * 
     * @param xinco_core_data_id
     */
    public void setXinco_core_data_id(int xinco_core_data_id) {
        this.xinco_core_data_id = xinco_core_data_id;
    }


    /**
     * Gets the xinco_core_group_id value for this XincoCoreACE.
     * 
     * @return xinco_core_group_id
     */
    public int getXinco_core_group_id() {
        return xinco_core_group_id;
    }


    /**
     * Sets the xinco_core_group_id value for this XincoCoreACE.
     * 
     * @param xinco_core_group_id
     */
    public void setXinco_core_group_id(int xinco_core_group_id) {
        this.xinco_core_group_id = xinco_core_group_id;
    }


    /**
     * Gets the xinco_core_node_id value for this XincoCoreACE.
     * 
     * @return xinco_core_node_id
     */
    public int getXinco_core_node_id() {
        return xinco_core_node_id;
    }


    /**
     * Sets the xinco_core_node_id value for this XincoCoreACE.
     * 
     * @param xinco_core_node_id
     */
    public void setXinco_core_node_id(int xinco_core_node_id) {
        this.xinco_core_node_id = xinco_core_node_id;
    }


    /**
     * Gets the xinco_core_user_id value for this XincoCoreACE.
     * 
     * @return xinco_core_user_id
     */
    public int getXinco_core_user_id() {
        return xinco_core_user_id;
    }


    /**
     * Sets the xinco_core_user_id value for this XincoCoreACE.
     * 
     * @param xinco_core_user_id
     */
    public void setXinco_core_user_id(int xinco_core_user_id) {
        this.xinco_core_user_id = xinco_core_user_id;
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
     * Gets the read_permission value for this XincoCoreACE.
     * 
     * @return read_permission
     */
    public boolean isRead_permission() {
        return read_permission;
    }


    /**
     * Sets the read_permission value for this XincoCoreACE.
     * 
     * @param read_permission
     */
    public void setRead_permission(boolean read_permission) {
        this.read_permission = read_permission;
    }


    /**
     * Gets the write_permission value for this XincoCoreACE.
     * 
     * @return write_permission
     */
    public boolean isWrite_permission() {
        return write_permission;
    }


    /**
     * Sets the write_permission value for this XincoCoreACE.
     * 
     * @param write_permission
     */
    public void setWrite_permission(boolean write_permission) {
        this.write_permission = write_permission;
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
            this.admin_permission == other.isAdmin_permission() &&
            this.changerID == other.getChangerID() &&
            this.execute_permission == other.isExecute_permission() &&
            this.xinco_core_data_id == other.getXinco_core_data_id() &&
            this.xinco_core_group_id == other.getXinco_core_group_id() &&
            this.xinco_core_node_id == other.getXinco_core_node_id() &&
            this.xinco_core_user_id == other.getXinco_core_user_id() &&
            this.id == other.getId() &&
            this.read_permission == other.isRead_permission() &&
            this.write_permission == other.isWrite_permission();
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
        _hashCode += (isAdmin_permission() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getChangerID();
        _hashCode += (isExecute_permission() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getXinco_core_data_id();
        _hashCode += getXinco_core_group_id();
        _hashCode += getXinco_core_node_id();
        _hashCode += getXinco_core_user_id();
        _hashCode += getId();
        _hashCode += (isRead_permission() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isWrite_permission() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreACE.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreACE"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("admin_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "admin_permission"));
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
        elemField.setFieldName("execute_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "execute_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_data_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_group_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_group_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_node_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_node_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_user_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_user_id"));
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
        elemField.setFieldName("read_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "read_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("write_permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "write_permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
