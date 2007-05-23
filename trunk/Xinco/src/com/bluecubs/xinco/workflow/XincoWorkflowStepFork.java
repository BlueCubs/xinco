/**
 * XincoWorkflowStepFork.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class XincoWorkflowStepFork  implements java.io.Serializable {
    private int step_id;

    private int workflow_id;

    private int optionNumber;

    private java.lang.String description;

    private java.util.Vector forks;

    public XincoWorkflowStepFork() {
    }

    public XincoWorkflowStepFork(
           int step_id,
           int workflow_id,
           int optionNumber,
           java.lang.String description,
           java.util.Vector forks) {
           this.step_id = step_id;
           this.workflow_id = workflow_id;
           this.optionNumber = optionNumber;
           this.description = description;
           this.forks = forks;
    }


    /**
     * Gets the step_id value for this XincoWorkflowStepFork.
     * 
     * @return step_id
     */
    public int getStep_id() {
        return step_id;
    }


    /**
     * Sets the step_id value for this XincoWorkflowStepFork.
     * 
     * @param step_id
     */
    public void setStep_id(int step_id) {
        this.step_id = step_id;
    }


    /**
     * Gets the workflow_id value for this XincoWorkflowStepFork.
     * 
     * @return workflow_id
     */
    public int getWorkflow_id() {
        return workflow_id;
    }


    /**
     * Sets the workflow_id value for this XincoWorkflowStepFork.
     * 
     * @param workflow_id
     */
    public void setWorkflow_id(int workflow_id) {
        this.workflow_id = workflow_id;
    }


    /**
     * Gets the optionNumber value for this XincoWorkflowStepFork.
     * 
     * @return optionNumber
     */
    public int getOptionNumber() {
        return optionNumber;
    }


    /**
     * Sets the optionNumber value for this XincoWorkflowStepFork.
     * 
     * @param optionNumber
     */
    public void setOptionNumber(int optionNumber) {
        this.optionNumber = optionNumber;
    }


    /**
     * Gets the description value for this XincoWorkflowStepFork.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this XincoWorkflowStepFork.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the forks value for this XincoWorkflowStepFork.
     * 
     * @return forks
     */
    public java.util.Vector getForks() {
        return forks;
    }


    /**
     * Sets the forks value for this XincoWorkflowStepFork.
     * 
     * @param forks
     */
    public void setForks(java.util.Vector forks) {
        this.forks = forks;
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
            this.step_id == other.getStep_id() &&
            this.workflow_id == other.getWorkflow_id() &&
            this.optionNumber == other.getOptionNumber() &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.forks==null && other.getForks()==null) || 
             (this.forks!=null &&
              this.forks.equals(other.getForks())));
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
        _hashCode += getStep_id();
        _hashCode += getWorkflow_id();
        _hashCode += getOptionNumber();
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getForks() != null) {
            _hashCode += getForks().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoWorkflowStepFork.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "XincoWorkflowStepFork"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("step_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "step_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflow_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "workflow_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("optionNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "optionNumber"));
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
        elemField.setFieldName("forks");
        elemField.setXmlName(new javax.xml.namespace.QName("", "forks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
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
