/**
 * XincoWorkflowStepInstance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class XincoWorkflowStepInstance  implements java.io.Serializable {
    private int xinco_core_data_id;

    private int xinco_workflow_id;

    private int id;

    private int xinco_core_user_id;

    private int xinco_workflow_step_id;

    private java.util.Calendar assign_date;

    private java.util.Calendar completion_date;

    public XincoWorkflowStepInstance() {
    }

    public XincoWorkflowStepInstance(
           int xinco_core_data_id,
           int xinco_workflow_id,
           int id,
           int xinco_core_user_id,
           int xinco_workflow_step_id,
           java.util.Calendar assign_date,
           java.util.Calendar completion_date) {
           this.xinco_core_data_id = xinco_core_data_id;
           this.xinco_workflow_id = xinco_workflow_id;
           this.id = id;
           this.xinco_core_user_id = xinco_core_user_id;
           this.xinco_workflow_step_id = xinco_workflow_step_id;
           this.assign_date = assign_date;
           this.completion_date = completion_date;
    }


    /**
     * Gets the xinco_core_data_id value for this XincoWorkflowStepInstance.
     * 
     * @return xinco_core_data_id
     */
    public int getXinco_core_data_id() {
        return xinco_core_data_id;
    }


    /**
     * Sets the xinco_core_data_id value for this XincoWorkflowStepInstance.
     * 
     * @param xinco_core_data_id
     */
    public void setXinco_core_data_id(int xinco_core_data_id) {
        this.xinco_core_data_id = xinco_core_data_id;
    }


    /**
     * Gets the xinco_workflow_id value for this XincoWorkflowStepInstance.
     * 
     * @return xinco_workflow_id
     */
    public int getXinco_workflow_id() {
        return xinco_workflow_id;
    }


    /**
     * Sets the xinco_workflow_id value for this XincoWorkflowStepInstance.
     * 
     * @param xinco_workflow_id
     */
    public void setXinco_workflow_id(int xinco_workflow_id) {
        this.xinco_workflow_id = xinco_workflow_id;
    }


    /**
     * Gets the id value for this XincoWorkflowStepInstance.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoWorkflowStepInstance.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the xinco_core_user_id value for this XincoWorkflowStepInstance.
     * 
     * @return xinco_core_user_id
     */
    public int getXinco_core_user_id() {
        return xinco_core_user_id;
    }


    /**
     * Sets the xinco_core_user_id value for this XincoWorkflowStepInstance.
     * 
     * @param xinco_core_user_id
     */
    public void setXinco_core_user_id(int xinco_core_user_id) {
        this.xinco_core_user_id = xinco_core_user_id;
    }


    /**
     * Gets the xinco_workflow_step_id value for this XincoWorkflowStepInstance.
     * 
     * @return xinco_workflow_step_id
     */
    public int getXinco_workflow_step_id() {
        return xinco_workflow_step_id;
    }


    /**
     * Sets the xinco_workflow_step_id value for this XincoWorkflowStepInstance.
     * 
     * @param xinco_workflow_step_id
     */
    public void setXinco_workflow_step_id(int xinco_workflow_step_id) {
        this.xinco_workflow_step_id = xinco_workflow_step_id;
    }


    /**
     * Gets the assign_date value for this XincoWorkflowStepInstance.
     * 
     * @return assign_date
     */
    public java.util.Calendar getAssign_date() {
        return assign_date;
    }


    /**
     * Sets the assign_date value for this XincoWorkflowStepInstance.
     * 
     * @param assign_date
     */
    public void setAssign_date(java.util.Calendar assign_date) {
        this.assign_date = assign_date;
    }


    /**
     * Gets the completion_date value for this XincoWorkflowStepInstance.
     * 
     * @return completion_date
     */
    public java.util.Calendar getCompletion_date() {
        return completion_date;
    }


    /**
     * Sets the completion_date value for this XincoWorkflowStepInstance.
     * 
     * @param completion_date
     */
    public void setCompletion_date(java.util.Calendar completion_date) {
        this.completion_date = completion_date;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoWorkflowStepInstance)) return false;
        XincoWorkflowStepInstance other = (XincoWorkflowStepInstance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.xinco_core_data_id == other.getXinco_core_data_id() &&
            this.xinco_workflow_id == other.getXinco_workflow_id() &&
            this.id == other.getId() &&
            this.xinco_core_user_id == other.getXinco_core_user_id() &&
            this.xinco_workflow_step_id == other.getXinco_workflow_step_id() &&
            ((this.assign_date==null && other.getAssign_date()==null) || 
             (this.assign_date!=null &&
              this.assign_date.equals(other.getAssign_date()))) &&
            ((this.completion_date==null && other.getCompletion_date()==null) || 
             (this.completion_date!=null &&
              this.completion_date.equals(other.getCompletion_date())));
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
        _hashCode += getXinco_core_data_id();
        _hashCode += getXinco_workflow_id();
        _hashCode += getId();
        _hashCode += getXinco_core_user_id();
        _hashCode += getXinco_workflow_step_id();
        if (getAssign_date() != null) {
            _hashCode += getAssign_date().hashCode();
        }
        if (getCompletion_date() != null) {
            _hashCode += getCompletion_date().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoWorkflowStepInstance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "XincoWorkflowStepInstance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_data_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_id"));
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
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
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
        elemField.setFieldName("xinco_workflow_step_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_workflow_step_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assign_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "assign_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("completion_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "completion_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
