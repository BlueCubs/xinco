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

    private com.bluecubs.xinco.workflow.Property[] properties;

    private com.bluecubs.xinco.workflow.Activity[] activities;

    public Node() {
    }

    public Node(
           int id,
           java.lang.String description,
           com.bluecubs.xinco.workflow.Property[] properties,
           com.bluecubs.xinco.workflow.Activity[] activities) {
           this.id = id;
           this.description = description;
           this.properties = properties;
           this.activities = activities;
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
    public com.bluecubs.xinco.workflow.Property[] getProperties() {
        return properties;
    }


    /**
     * Sets the properties value for this Node.
     * 
     * @param properties
     */
    public void setProperties(com.bluecubs.xinco.workflow.Property[] properties) {
        this.properties = properties;
    }

    public com.bluecubs.xinco.workflow.Property getProperties(int i) {
        return this.properties[i];
    }

    public void setProperties(int i, com.bluecubs.xinco.workflow.Property _value) {
        this.properties[i] = _value;
    }


    /**
     * Gets the activities value for this Node.
     * 
     * @return activities
     */
    public com.bluecubs.xinco.workflow.Activity[] getActivities() {
        return activities;
    }


    /**
     * Sets the activities value for this Node.
     * 
     * @param activities
     */
    public void setActivities(com.bluecubs.xinco.workflow.Activity[] activities) {
        this.activities = activities;
    }

    public com.bluecubs.xinco.workflow.Activity getActivities(int i) {
        return this.activities[i];
    }

    public void setActivities(int i, com.bluecubs.xinco.workflow.Activity _value) {
        this.activities[i] = _value;
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
              java.util.Arrays.equals(this.properties, other.getProperties()))) &&
            ((this.activities==null && other.getActivities()==null) || 
             (this.activities!=null &&
              java.util.Arrays.equals(this.activities, other.getActivities())));
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProperties());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProperties(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getActivities() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getActivities());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActivities(), i);
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Property"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activities");
        elemField.setXmlName(new javax.xml.namespace.QName("", "activities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Activity"));
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
