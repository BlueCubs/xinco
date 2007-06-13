/**
 * Node.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class Node  implements java.io.Serializable {
    private int id;

    private java.lang.String description;

    private java.util.Vector properties;

    private java.util.Vector activities;

    private boolean startNode;

    private boolean endNode;

    private java.lang.Integer changerID;

    public Node() {
    }

    public Node(
           int id,
           java.lang.String description,
           java.util.Vector properties,
           java.util.Vector activities,
           boolean startNode,
           boolean endNode,
           java.lang.Integer changerID) {
           this.id = id;
           this.description = description;
           this.properties = properties;
           this.activities = activities;
           this.startNode = startNode;
           this.endNode = endNode;
           this.changerID = changerID;
    }


    /**
     * Gets the id value for this Node.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this Node.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the description value for this Node.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Node.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the properties value for this Node.
     * 
     * @return properties
     */
    public java.util.Vector getProperties() {
        return properties;
    }


    /**
     * Sets the properties value for this Node.
     * 
     * @param properties
     */
    public void setProperties(java.util.Vector properties) {
        this.properties = properties;
    }


    /**
     * Gets the activities value for this Node.
     * 
     * @return activities
     */
    public java.util.Vector getActivities() {
        return activities;
    }


    /**
     * Sets the activities value for this Node.
     * 
     * @param activities
     */
    public void setActivities(java.util.Vector activities) {
        this.activities = activities;
    }


    /**
     * Gets the startNode value for this Node.
     * 
     * @return startNode
     */
    public boolean isStartNode() {
        return startNode;
    }


    /**
     * Sets the startNode value for this Node.
     * 
     * @param startNode
     */
    public void setStartNode(boolean startNode) {
        this.startNode = startNode;
    }


    /**
     * Gets the endNode value for this Node.
     * 
     * @return endNode
     */
    public boolean isEndNode() {
        return endNode;
    }


    /**
     * Sets the endNode value for this Node.
     * 
     * @param endNode
     */
    public void setEndNode(boolean endNode) {
        this.endNode = endNode;
    }


    /**
     * Gets the changerID value for this Node.
     * 
     * @return changerID
     */
    public java.lang.Integer getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this Node.
     * 
     * @param changerID
     */
    public void setChangerID(java.lang.Integer changerID) {
        this.changerID = changerID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Node)) return false;
        Node other = (Node) obj;
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
            ((this.properties==null && other.getProperties()==null) || 
             (this.properties!=null &&
              this.properties.equals(other.getProperties()))) &&
            ((this.activities==null && other.getActivities()==null) || 
             (this.activities!=null &&
              this.activities.equals(other.getActivities()))) &&
            this.startNode == other.isStartNode() &&
            this.endNode == other.isEndNode() &&
            ((this.changerID==null && other.getChangerID()==null) || 
             (this.changerID!=null &&
              this.changerID.equals(other.getChangerID())));
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
        if (getProperties() != null) {
            _hashCode += getProperties().hashCode();
        }
        if (getActivities() != null) {
            _hashCode += getActivities().hashCode();
        }
        _hashCode += (isStartNode() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isEndNode() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getChangerID() != null) {
            _hashCode += getChangerID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Node.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"));
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
        elemField.setFieldName("properties");
        elemField.setXmlName(new javax.xml.namespace.QName("", "properties"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activities");
        elemField.setXmlName(new javax.xml.namespace.QName("", "activities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startNode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startNode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endNode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endNode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
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
