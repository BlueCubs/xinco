/**
 * XincoCoreLog.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreLog  implements java.io.Serializable {
    private int id;

    private int op_code;

    private int changerID;

    private java.util.Calendar op_datetime;

    private java.lang.String op_description;

    private com.bluecubs.xinco.core.XincoVersion version;

    private int xinco_core_data_id;

    private int xinco_core_user_id;

    public XincoCoreLog() {
    }

    public XincoCoreLog(
           int id,
           int op_code,
           int changerID,
           java.util.Calendar op_datetime,
           java.lang.String op_description,
           com.bluecubs.xinco.core.XincoVersion version,
           int xinco_core_data_id,
           int xinco_core_user_id) {
           this.id = id;
           this.op_code = op_code;
           this.changerID = changerID;
           this.op_datetime = op_datetime;
           this.op_description = op_description;
           this.version = version;
           this.xinco_core_data_id = xinco_core_data_id;
           this.xinco_core_user_id = xinco_core_user_id;
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
     * Gets the op_code value for this XincoCoreLog.
     * 
     * @return op_code
     */
    public int getOp_code() {
        return op_code;
    }


    /**
     * Sets the op_code value for this XincoCoreLog.
     * 
     * @param op_code
     */
    public void setOp_code(int op_code) {
        this.op_code = op_code;
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
     * Gets the op_datetime value for this XincoCoreLog.
     * 
     * @return op_datetime
     */
    public java.util.Calendar getOp_datetime() {
        return op_datetime;
    }


    /**
     * Sets the op_datetime value for this XincoCoreLog.
     * 
     * @param op_datetime
     */
    public void setOp_datetime(java.util.Calendar op_datetime) {
        this.op_datetime = op_datetime;
    }


    /**
     * Gets the op_description value for this XincoCoreLog.
     * 
     * @return op_description
     */
    public java.lang.String getOp_description() {
        return op_description;
    }


    /**
     * Sets the op_description value for this XincoCoreLog.
     * 
     * @param op_description
     */
    public void setOp_description(java.lang.String op_description) {
        this.op_description = op_description;
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
     * Gets the xinco_core_data_id value for this XincoCoreLog.
     * 
     * @return xinco_core_data_id
     */
    public int getXinco_core_data_id() {
        return xinco_core_data_id;
    }


    /**
     * Sets the xinco_core_data_id value for this XincoCoreLog.
     * 
     * @param xinco_core_data_id
     */
    public void setXinco_core_data_id(int xinco_core_data_id) {
        this.xinco_core_data_id = xinco_core_data_id;
    }


    /**
     * Gets the xinco_core_user_id value for this XincoCoreLog.
     * 
     * @return xinco_core_user_id
     */
    public int getXinco_core_user_id() {
        return xinco_core_user_id;
    }


    /**
     * Sets the xinco_core_user_id value for this XincoCoreLog.
     * 
     * @param xinco_core_user_id
     */
    public void setXinco_core_user_id(int xinco_core_user_id) {
        this.xinco_core_user_id = xinco_core_user_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreLog)) return false;
        XincoCoreLog other = (XincoCoreLog) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.op_code == other.getOp_code() &&
            this.changerID == other.getChangerID() &&
            ((this.op_datetime==null && other.getOp_datetime()==null) || 
             (this.op_datetime!=null &&
              this.op_datetime.equals(other.getOp_datetime()))) &&
            ((this.op_description==null && other.getOp_description()==null) || 
             (this.op_description!=null &&
              this.op_description.equals(other.getOp_description()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            this.xinco_core_data_id == other.getXinco_core_data_id() &&
            this.xinco_core_user_id == other.getXinco_core_user_id();
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
        _hashCode += getOp_code();
        _hashCode += getChangerID();
        if (getOp_datetime() != null) {
            _hashCode += getOp_datetime().hashCode();
        }
        if (getOp_description() != null) {
            _hashCode += getOp_description().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        _hashCode += getXinco_core_data_id();
        _hashCode += getXinco_core_user_id();
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
        elemField.setFieldName("op_code");
        elemField.setXmlName(new javax.xml.namespace.QName("", "op_code"));
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
        elemField.setFieldName("op_datetime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "op_datetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("op_description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "op_description"));
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
        elemField.setFieldName("xinco_core_data_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_user_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_user_id"));
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
