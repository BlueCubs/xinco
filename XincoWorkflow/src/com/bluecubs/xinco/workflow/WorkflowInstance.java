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

    private java.util.Vector nodes;

    private java.util.Vector transactions;

    private java.lang.Integer changerID;

    private int currentNode;

    public WorkflowInstance() {
    }

    public WorkflowInstance(
           int id,
           int templateId,
           java.util.Calendar creationTime,
           java.util.Vector nodes,
           java.util.Vector transactions,
           java.lang.Integer changerID,
           int currentNode) {
           this.id = id;
           this.templateId = templateId;
           this.creationTime = creationTime;
           this.nodes = nodes;
           this.transactions = transactions;
           this.changerID = changerID;
           this.currentNode = currentNode;
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
    public java.util.Vector getNodes() {
        return nodes;
    }


    /**
     * Sets the nodes value for this WorkflowInstance.
     * 
     * @param nodes
     */
    public void setNodes(java.util.Vector nodes) {
        this.nodes = nodes;
    }


    /**
     * Gets the transactions value for this WorkflowInstance.
     * 
     * @return transactions
     */
    public java.util.Vector getTransactions() {
        return transactions;
    }


    /**
     * Sets the transactions value for this WorkflowInstance.
     * 
     * @param transactions
     */
    public void setTransactions(java.util.Vector transactions) {
        this.transactions = transactions;
    }


    /**
     * Gets the changerID value for this WorkflowInstance.
     * 
     * @return changerID
     */
    public java.lang.Integer getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this WorkflowInstance.
     * 
     * @param changerID
     */
    public void setChangerID(java.lang.Integer changerID) {
        this.changerID = changerID;
    }


    /**
     * Gets the currentNode value for this WorkflowInstance.
     * 
     * @return currentNode
     */
    public int getCurrentNode() {
        return currentNode;
    }


    /**
     * Sets the currentNode value for this WorkflowInstance.
     * 
     * @param currentNode
     */
    public void setCurrentNode(int currentNode) {
        this.currentNode = currentNode;
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
              this.nodes.equals(other.getNodes()))) &&
            ((this.transactions==null && other.getTransactions()==null) || 
             (this.transactions!=null &&
              this.transactions.equals(other.getTransactions()))) &&
            ((this.changerID==null && other.getChangerID()==null) || 
             (this.changerID!=null &&
              this.changerID.equals(other.getChangerID()))) &&
            this.currentNode == other.getCurrentNode();
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
            _hashCode += getNodes().hashCode();
        }
        if (getTransactions() != null) {
            _hashCode += getTransactions().hashCode();
        }
        if (getChangerID() != null) {
            _hashCode += getChangerID().hashCode();
        }
        _hashCode += getCurrentNode();
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "transactions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentNode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "currentNode"));
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
