/**
 * XincoWorkflowInstance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class XincoWorkflowInstance  implements java.io.Serializable {
    private int id;

    private int xinco_workflow_id;

    private int xinco_core_data_id;

    private int current_step;

    private java.util.Calendar start_date;

    private java.util.Calendar end_date;

    private java.util.Vector workflow_instance_step_instances;

    public XincoWorkflowInstance() {
    }

    public XincoWorkflowInstance(
           int id,
           int xinco_workflow_id,
           int xinco_core_data_id,
           int current_step,
           java.util.Calendar start_date,
           java.util.Calendar end_date,
           java.util.Vector workflow_instance_step_instances) {
           this.id = id;
           this.xinco_workflow_id = xinco_workflow_id;
           this.xinco_core_data_id = xinco_core_data_id;
           this.current_step = current_step;
           this.start_date = start_date;
           this.end_date = end_date;
           this.workflow_instance_step_instances = workflow_instance_step_instances;
    }


    /**
     * Gets the id value for this XincoWorkflowInstance.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoWorkflowInstance.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the xinco_workflow_id value for this XincoWorkflowInstance.
     * 
     * @return xinco_workflow_id
     */
    public int getXinco_workflow_id() {
        return xinco_workflow_id;
    }


    /**
     * Sets the xinco_workflow_id value for this XincoWorkflowInstance.
     * 
     * @param xinco_workflow_id
     */
    public void setXinco_workflow_id(int xinco_workflow_id) {
        this.xinco_workflow_id = xinco_workflow_id;
    }


    /**
     * Gets the xinco_core_data_id value for this XincoWorkflowInstance.
     * 
     * @return xinco_core_data_id
     */
    public int getXinco_core_data_id() {
        return xinco_core_data_id;
    }


    /**
     * Sets the xinco_core_data_id value for this XincoWorkflowInstance.
     * 
     * @param xinco_core_data_id
     */
    public void setXinco_core_data_id(int xinco_core_data_id) {
        this.xinco_core_data_id = xinco_core_data_id;
    }


    /**
     * Gets the current_step value for this XincoWorkflowInstance.
     * 
     * @return current_step
     */
    public int getCurrent_step() {
        return current_step;
    }


    /**
     * Sets the current_step value for this XincoWorkflowInstance.
     * 
     * @param current_step
     */
    public void setCurrent_step(int current_step) {
        this.current_step = current_step;
    }


    /**
     * Gets the start_date value for this XincoWorkflowInstance.
     * 
     * @return start_date
     */
    public java.util.Calendar getStart_date() {
        return start_date;
    }


    /**
     * Sets the start_date value for this XincoWorkflowInstance.
     * 
     * @param start_date
     */
    public void setStart_date(java.util.Calendar start_date) {
        this.start_date = start_date;
    }


    /**
     * Gets the end_date value for this XincoWorkflowInstance.
     * 
     * @return end_date
     */
    public java.util.Calendar getEnd_date() {
        return end_date;
    }


    /**
     * Sets the end_date value for this XincoWorkflowInstance.
     * 
     * @param end_date
     */
    public void setEnd_date(java.util.Calendar end_date) {
        this.end_date = end_date;
    }


    /**
     * Gets the workflow_instance_step_instances value for this XincoWorkflowInstance.
     * 
     * @return workflow_instance_step_instances
     */
    public java.util.Vector getWorkflow_instance_step_instances() {
        return workflow_instance_step_instances;
    }


    /**
     * Sets the workflow_instance_step_instances value for this XincoWorkflowInstance.
     * 
     * @param workflow_instance_step_instances
     */
    public void setWorkflow_instance_step_instances(java.util.Vector workflow_instance_step_instances) {
        this.workflow_instance_step_instances = workflow_instance_step_instances;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoWorkflowInstance)) return false;
        XincoWorkflowInstance other = (XincoWorkflowInstance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.xinco_workflow_id == other.getXinco_workflow_id() &&
            this.xinco_core_data_id == other.getXinco_core_data_id() &&
            this.current_step == other.getCurrent_step() &&
            ((this.start_date==null && other.getStart_date()==null) || 
             (this.start_date!=null &&
              this.start_date.equals(other.getStart_date()))) &&
            ((this.end_date==null && other.getEnd_date()==null) || 
             (this.end_date!=null &&
              this.end_date.equals(other.getEnd_date()))) &&
            ((this.workflow_instance_step_instances==null && other.getWorkflow_instance_step_instances()==null) || 
             (this.workflow_instance_step_instances!=null &&
              this.workflow_instance_step_instances.equals(other.getWorkflow_instance_step_instances())));
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
        _hashCode += getXinco_workflow_id();
        _hashCode += getXinco_core_data_id();
        _hashCode += getCurrent_step();
        if (getStart_date() != null) {
            _hashCode += getStart_date().hashCode();
        }
        if (getEnd_date() != null) {
            _hashCode += getEnd_date().hashCode();
        }
        if (getWorkflow_instance_step_instances() != null) {
            _hashCode += getWorkflow_instance_step_instances().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoWorkflowInstance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "XincoWorkflowInstance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_workflow_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_workflow_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_data_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("current_step");
        elemField.setXmlName(new javax.xml.namespace.QName("", "current_step"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("start_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "start_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("end_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "end_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflow_instance_step_instances");
        elemField.setXmlName(new javax.xml.namespace.QName("", "workflow_instance_step_instances"));
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
