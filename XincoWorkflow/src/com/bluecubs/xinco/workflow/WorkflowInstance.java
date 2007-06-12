/**
 * WorkflowInstance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class WorkflowInstance  implements java.io.Serializable {
    private int id;

    private int templateId;

    private java.util.Calendar creationTime;

    private com.bluecubs.xinco.workflow.Node[] nodes;

    private com.bluecubs.xinco.workflow.Transaction[] transactions;

    public WorkflowInstance() {
    }

    public WorkflowInstance(
           int id,
           int templateId,
           java.util.Calendar creationTime,
           com.bluecubs.xinco.workflow.Node[] nodes,
           com.bluecubs.xinco.workflow.Transaction[] transactions) {
           this.id = id;
           this.templateId = templateId;
           this.creationTime = creationTime;
           this.nodes = nodes;
           this.transactions = transactions;
    }


    /**
     * Gets the id value for this WorkflowInstance.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this WorkflowInstance.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the templateId value for this WorkflowInstance.
     * 
     * @return templateId
     */
    public int getTemplateId() {
        return templateId;
    }


    /**
     * Sets the templateId value for this WorkflowInstance.
     * 
     * @param templateId
     */
    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }


    /**
     * Gets the creationTime value for this WorkflowInstance.
     * 
     * @return creationTime
     */
    public java.util.Calendar getCreationTime() {
        return creationTime;
    }


    /**
     * Sets the creationTime value for this WorkflowInstance.
     * 
     * @param creationTime
     */
    public void setCreationTime(java.util.Calendar creationTime) {
        this.creationTime = creationTime;
    }


    /**
     * Gets the nodes value for this WorkflowInstance.
     * 
     * @return nodes
     */
    public com.bluecubs.xinco.workflow.Node[] getNodes() {
        return nodes;
    }


    /**
     * Sets the nodes value for this WorkflowInstance.
     * 
     * @param nodes
     */
    public void setNodes(com.bluecubs.xinco.workflow.Node[] nodes) {
        this.nodes = nodes;
    }

    public com.bluecubs.xinco.workflow.Node getNodes(int i) {
        return this.nodes[i];
    }

    public void setNodes(int i, com.bluecubs.xinco.workflow.Node _value) {
        this.nodes[i] = _value;
    }


    /**
     * Gets the transactions value for this WorkflowInstance.
     * 
     * @return transactions
     */
    public com.bluecubs.xinco.workflow.Transaction[] getTransactions() {
        return transactions;
    }


    /**
     * Sets the transactions value for this WorkflowInstance.
     * 
     * @param transactions
     */
    public void setTransactions(com.bluecubs.xinco.workflow.Transaction[] transactions) {
        this.transactions = transactions;
    }

    public com.bluecubs.xinco.workflow.Transaction getTransactions(int i) {
        return this.transactions[i];
    }

    public void setTransactions(int i, com.bluecubs.xinco.workflow.Transaction _value) {
        this.transactions[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WorkflowInstance)) return false;
        WorkflowInstance other = (WorkflowInstance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.templateId == other.getTemplateId() &&
            ((this.creationTime==null && other.getCreationTime()==null) || 
             (this.creationTime!=null &&
              this.creationTime.equals(other.getCreationTime()))) &&
            ((this.nodes==null && other.getNodes()==null) || 
             (this.nodes!=null &&
              java.util.Arrays.equals(this.nodes, other.getNodes()))) &&
            ((this.transactions==null && other.getTransactions()==null) || 
             (this.transactions!=null &&
              java.util.Arrays.equals(this.transactions, other.getTransactions())));
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
        _hashCode += getTemplateId();
        if (getCreationTime() != null) {
            _hashCode += getCreationTime().hashCode();
        }
        if (getNodes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNodes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNodes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTransactions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransactions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransactions(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WorkflowInstance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowInstance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("templateId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "templateId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "creationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "transactions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Transaction"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
