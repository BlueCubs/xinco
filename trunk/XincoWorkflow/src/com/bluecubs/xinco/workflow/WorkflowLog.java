/**
 * WorkflowLog.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class WorkflowLog  implements java.io.Serializable {
    private int id;

    private int resource_id;

    private int workflow_template_id;

    private int workflow_instance_id;

    private int op_code;

    private java.util.Calendar op_datetime;

    private java.lang.String op_description;

    private int xinco_core_user_id;

    public WorkflowLog() {
    }

    public WorkflowLog(
           int id,
           int resource_id,
           int workflow_template_id,
           int workflow_instance_id,
           int op_code,
           java.util.Calendar op_datetime,
           java.lang.String op_description,
           int xinco_core_user_id) {
           this.id = id;
           this.resource_id = resource_id;
           this.workflow_template_id = workflow_template_id;
           this.workflow_instance_id = workflow_instance_id;
           this.op_code = op_code;
           this.op_datetime = op_datetime;
           this.op_description = op_description;
           this.xinco_core_user_id = xinco_core_user_id;
    }


    /**
     * Gets the id value for this WorkflowLog.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this WorkflowLog.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the resource_id value for this WorkflowLog.
     * 
     * @return resource_id
     */
    public int getResource_id() {
        return resource_id;
    }


    /**
     * Sets the resource_id value for this WorkflowLog.
     * 
     * @param resource_id
     */
    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }


    /**
     * Gets the workflow_template_id value for this WorkflowLog.
     * 
     * @return workflow_template_id
     */
    public int getWorkflow_template_id() {
        return workflow_template_id;
    }


    /**
     * Sets the workflow_template_id value for this WorkflowLog.
     * 
     * @param workflow_template_id
     */
    public void setWorkflow_template_id(int workflow_template_id) {
        this.workflow_template_id = workflow_template_id;
    }


    /**
     * Gets the workflow_instance_id value for this WorkflowLog.
     * 
     * @return workflow_instance_id
     */
    public int getWorkflow_instance_id() {
        return workflow_instance_id;
    }


    /**
     * Sets the workflow_instance_id value for this WorkflowLog.
     * 
     * @param workflow_instance_id
     */
    public void setWorkflow_instance_id(int workflow_instance_id) {
        this.workflow_instance_id = workflow_instance_id;
    }


    /**
     * Gets the op_code value for this WorkflowLog.
     * 
     * @return op_code
     */
    public int getOp_code() {
        return op_code;
    }


    /**
     * Sets the op_code value for this WorkflowLog.
     * 
     * @param op_code
     */
    public void setOp_code(int op_code) {
        this.op_code = op_code;
    }


    /**
     * Gets the op_datetime value for this WorkflowLog.
     * 
     * @return op_datetime
     */
    public java.util.Calendar getOp_datetime() {
        return op_datetime;
    }


    /**
     * Sets the op_datetime value for this WorkflowLog.
     * 
     * @param op_datetime
     */
    public void setOp_datetime(java.util.Calendar op_datetime) {
        this.op_datetime = op_datetime;
    }


    /**
     * Gets the op_description value for this WorkflowLog.
     * 
     * @return op_description
     */
    public java.lang.String getOp_description() {
        return op_description;
    }


    /**
     * Sets the op_description value for this WorkflowLog.
     * 
     * @param op_description
     */
    public void setOp_description(java.lang.String op_description) {
        this.op_description = op_description;
    }


    /**
     * Gets the xinco_core_user_id value for this WorkflowLog.
     * 
     * @return xinco_core_user_id
     */
    public int getXinco_core_user_id() {
        return xinco_core_user_id;
    }


    /**
     * Sets the xinco_core_user_id value for this WorkflowLog.
     * 
     * @param xinco_core_user_id
     */
    public void setXinco_core_user_id(int xinco_core_user_id) {
        this.xinco_core_user_id = xinco_core_user_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WorkflowLog)) return false;
        WorkflowLog other = (WorkflowLog) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.resource_id == other.getResource_id() &&
            this.workflow_template_id == other.getWorkflow_template_id() &&
            this.workflow_instance_id == other.getWorkflow_instance_id() &&
            this.op_code == other.getOp_code() &&
            ((this.op_datetime==null && other.getOp_datetime()==null) || 
             (this.op_datetime!=null &&
              this.op_datetime.equals(other.getOp_datetime()))) &&
            ((this.op_description==null && other.getOp_description()==null) || 
             (this.op_description!=null &&
              this.op_description.equals(other.getOp_description()))) &&
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
        _hashCode += getResource_id();
        _hashCode += getWorkflow_template_id();
        _hashCode += getWorkflow_instance_id();
        _hashCode += getOp_code();
        if (getOp_datetime() != null) {
            _hashCode += getOp_datetime().hashCode();
        }
        if (getOp_description() != null) {
            _hashCode += getOp_description().hashCode();
        }
        _hashCode += getXinco_core_user_id();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WorkflowLog.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowLog"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resource_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resource_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("op_code");
        elemField.setXmlName(new javax.xml.namespace.QName("", "op_code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("op_datetime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "op_datetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("op_description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "op_description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
