/**
 * XincoWorkflowStepFork.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class XincoWorkflowStepFork  implements java.io.Serializable {
    private int id;

    private int yesStep;

    private int noStep;

    public XincoWorkflowStepFork() {
    }

    public XincoWorkflowStepFork(
           int id,
           int yesStep,
           int noStep) {
           this.id = id;
           this.yesStep = yesStep;
           this.noStep = noStep;
    }


    /**
     * Gets the id value for this XincoWorkflowStepFork.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoWorkflowStepFork.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the yesStep value for this XincoWorkflowStepFork.
     * 
     * @return yesStep
     */
    public int getYesStep() {
        return yesStep;
    }


    /**
     * Sets the yesStep value for this XincoWorkflowStepFork.
     * 
     * @param yesStep
     */
    public void setYesStep(int yesStep) {
        this.yesStep = yesStep;
    }


    /**
     * Gets the noStep value for this XincoWorkflowStepFork.
     * 
     * @return noStep
     */
    public int getNoStep() {
        return noStep;
    }


    /**
     * Sets the noStep value for this XincoWorkflowStepFork.
     * 
     * @param noStep
     */
    public void setNoStep(int noStep) {
        this.noStep = noStep;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoWorkflowStepFork)) return false;
        XincoWorkflowStepFork other = (XincoWorkflowStepFork) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.yesStep == other.getYesStep() &&
            this.noStep == other.getNoStep();
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
        _hashCode += getYesStep();
        _hashCode += getNoStep();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoWorkflowStepFork.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "XincoWorkflowStepFork"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("yesStep");
        elemField.setXmlName(new javax.xml.namespace.QName("", "yesStep"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noStep");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noStep"));
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
