/**
 * InstanceProperty.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class InstanceProperty  extends com.bluecubs.xinco.workflow.Property  implements java.io.Serializable {
    private int workflow_template_id;

    private int workflow_instance_id;

    public InstanceProperty() {
    }

    public InstanceProperty(
           int id,
           java.lang.String description,
           java.lang.String stringProperty,
           int intProperty,
           long longProperty,
           boolean boolProperty,
           java.lang.Integer changerID,
           int workflow_template_id,
           int workflow_instance_id) {
        super(
            id,
            description,
            stringProperty,
            intProperty,
            longProperty,
            boolProperty,
            changerID);
        this.workflow_template_id = workflow_template_id;
        this.workflow_instance_id = workflow_instance_id;
    }


    /**
     * Gets the workflow_template_id value for this InstanceProperty.
     * 
     * @return workflow_template_id
     */
    public int getWorkflow_template_id() {
        return workflow_template_id;
    }


    /**
     * Sets the workflow_template_id value for this InstanceProperty.
     * 
     * @param workflow_template_id
     */
    public void setWorkflow_template_id(int workflow_template_id) {
        this.workflow_template_id = workflow_template_id;
    }


    /**
     * Gets the workflow_instance_id value for this InstanceProperty.
     * 
     * @return workflow_instance_id
     */
    public int getWorkflow_instance_id() {
        return workflow_instance_id;
    }


    /**
     * Sets the workflow_instance_id value for this InstanceProperty.
     * 
     * @param workflow_instance_id
     */
    public void setWorkflow_instance_id(int workflow_instance_id) {
        this.workflow_instance_id = workflow_instance_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InstanceProperty)) return false;
        InstanceProperty other = (InstanceProperty) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.workflow_template_id == other.getWorkflow_template_id() &&
            this.workflow_instance_id == other.getWorkflow_instance_id();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        _hashCode += getWorkflow_template_id();
        _hashCode += getWorkflow_instance_id();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InstanceProperty.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "InstanceProperty"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflow_template_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "workflow_template_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflow_instance_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "workflow_instance_id"));
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
