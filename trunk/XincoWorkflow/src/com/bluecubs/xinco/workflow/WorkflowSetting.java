/**
 * WorkflowSetting.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class WorkflowSetting  implements java.io.Serializable {
    private int id;

    private java.lang.String description;

    private int int_value;

    private java.lang.String string_value;

    private boolean bool_value;

    private int changerID;

    private long long_value;

    private java.util.Vector workflow_settings;

    private java.lang.Integer changerID2;

    public WorkflowSetting() {
    }

    public WorkflowSetting(
           int id,
           java.lang.String description,
           int int_value,
           java.lang.String string_value,
           boolean bool_value,
           int changerID,
           long long_value,
           java.util.Vector workflow_settings,
           java.lang.Integer changerID2) {
           this.id = id;
           this.description = description;
           this.int_value = int_value;
           this.string_value = string_value;
           this.bool_value = bool_value;
           this.changerID = changerID;
           this.long_value = long_value;
           this.workflow_settings = workflow_settings;
           this.changerID2 = changerID2;
    }


    /**
     * Gets the id value for this WorkflowSetting.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this WorkflowSetting.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the description value for this WorkflowSetting.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this WorkflowSetting.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the int_value value for this WorkflowSetting.
     * 
     * @return int_value
     */
    public int getInt_value() {
        return int_value;
    }


    /**
     * Sets the int_value value for this WorkflowSetting.
     * 
     * @param int_value
     */
    public void setInt_value(int int_value) {
        this.int_value = int_value;
    }


    /**
     * Gets the string_value value for this WorkflowSetting.
     * 
     * @return string_value
     */
    public java.lang.String getString_value() {
        return string_value;
    }


    /**
     * Sets the string_value value for this WorkflowSetting.
     * 
     * @param string_value
     */
    public void setString_value(java.lang.String string_value) {
        this.string_value = string_value;
    }


    /**
     * Gets the bool_value value for this WorkflowSetting.
     * 
     * @return bool_value
     */
    public boolean isBool_value() {
        return bool_value;
    }


    /**
     * Sets the bool_value value for this WorkflowSetting.
     * 
     * @param bool_value
     */
    public void setBool_value(boolean bool_value) {
        this.bool_value = bool_value;
    }


    /**
     * Gets the changerID value for this WorkflowSetting.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this WorkflowSetting.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }


    /**
     * Gets the long_value value for this WorkflowSetting.
     * 
     * @return long_value
     */
    public long getLong_value() {
        return long_value;
    }


    /**
     * Sets the long_value value for this WorkflowSetting.
     * 
     * @param long_value
     */
    public void setLong_value(long long_value) {
        this.long_value = long_value;
    }


    /**
     * Gets the workflow_settings value for this WorkflowSetting.
     * 
     * @return workflow_settings
     */
    public java.util.Vector getWorkflow_settings() {
        return workflow_settings;
    }


    /**
     * Sets the workflow_settings value for this WorkflowSetting.
     * 
     * @param workflow_settings
     */
    public void setWorkflow_settings(java.util.Vector workflow_settings) {
        this.workflow_settings = workflow_settings;
    }


    /**
     * Gets the changerID2 value for this WorkflowSetting.
     * 
     * @return changerID2
     */
    public java.lang.Integer getChangerID2() {
        return changerID2;
    }


    /**
     * Sets the changerID2 value for this WorkflowSetting.
     * 
     * @param changerID2
     */
    public void setChangerID2(java.lang.Integer changerID2) {
        this.changerID2 = changerID2;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WorkflowSetting)) return false;
        WorkflowSetting other = (WorkflowSetting) obj;
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
            this.int_value == other.getInt_value() &&
            ((this.string_value==null && other.getString_value()==null) || 
             (this.string_value!=null &&
              this.string_value.equals(other.getString_value()))) &&
            this.bool_value == other.isBool_value() &&
            this.changerID == other.getChangerID() &&
            this.long_value == other.getLong_value() &&
            ((this.workflow_settings==null && other.getWorkflow_settings()==null) || 
             (this.workflow_settings!=null &&
              this.workflow_settings.equals(other.getWorkflow_settings()))) &&
            ((this.changerID2==null && other.getChangerID2()==null) || 
             (this.changerID2!=null &&
              this.changerID2.equals(other.getChangerID2())));
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
        _hashCode += getInt_value();
        if (getString_value() != null) {
            _hashCode += getString_value().hashCode();
        }
        _hashCode += (isBool_value() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getChangerID();
        _hashCode += new Long(getLong_value()).hashCode();
        if (getWorkflow_settings() != null) {
            _hashCode += getWorkflow_settings().hashCode();
        }
        if (getChangerID2() != null) {
            _hashCode += getChangerID2().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WorkflowSetting.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowSetting"));
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
        elemField.setFieldName("int_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "int_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("string_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "string_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bool_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bool_value"));
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
        elemField.setFieldName("long_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "long_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflow_settings");
        elemField.setXmlName(new javax.xml.namespace.QName("", "workflow_settings"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
