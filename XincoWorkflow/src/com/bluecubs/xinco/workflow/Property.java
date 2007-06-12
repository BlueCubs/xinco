/**
 * Property.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class Property  implements java.io.Serializable {
    private int id;

    private java.lang.Integer transactionId;

    private java.lang.Integer nodeId;

    private java.lang.Integer activityId;

    private java.lang.String description;

    private java.lang.String stringProperty;

    private int intProperty;

    private long longProperty;

    private boolean boolProperty;

    public Property() {
    }

    public Property(
           int id,
           java.lang.Integer transactionId,
           java.lang.Integer nodeId,
           java.lang.Integer activityId,
           java.lang.String description,
           java.lang.String stringProperty,
           int intProperty,
           long longProperty,
           boolean boolProperty) {
           this.id = id;
           this.transactionId = transactionId;
           this.nodeId = nodeId;
           this.activityId = activityId;
           this.description = description;
           this.stringProperty = stringProperty;
           this.intProperty = intProperty;
           this.longProperty = longProperty;
           this.boolProperty = boolProperty;
    }


    /**
     * Gets the id value for this Property.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this Property.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the transactionId value for this Property.
     * 
     * @return transactionId
     */
    public java.lang.Integer getTransactionId() {
        return transactionId;
    }


    /**
     * Sets the transactionId value for this Property.
     * 
     * @param transactionId
     */
    public void setTransactionId(java.lang.Integer transactionId) {
        this.transactionId = transactionId;
    }


    /**
     * Gets the nodeId value for this Property.
     * 
     * @return nodeId
     */
    public java.lang.Integer getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this Property.
     * 
     * @param nodeId
     */
    public void setNodeId(java.lang.Integer nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the activityId value for this Property.
     * 
     * @return activityId
     */
    public java.lang.Integer getActivityId() {
        return activityId;
    }


    /**
     * Sets the activityId value for this Property.
     * 
     * @param activityId
     */
    public void setActivityId(java.lang.Integer activityId) {
        this.activityId = activityId;
    }


    /**
     * Gets the description value for this Property.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Property.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the stringProperty value for this Property.
     * 
     * @return stringProperty
     */
    public java.lang.String getStringProperty() {
        return stringProperty;
    }


    /**
     * Sets the stringProperty value for this Property.
     * 
     * @param stringProperty
     */
    public void setStringProperty(java.lang.String stringProperty) {
        this.stringProperty = stringProperty;
    }


    /**
     * Gets the intProperty value for this Property.
     * 
     * @return intProperty
     */
    public int getIntProperty() {
        return intProperty;
    }


    /**
     * Sets the intProperty value for this Property.
     * 
     * @param intProperty
     */
    public void setIntProperty(int intProperty) {
        this.intProperty = intProperty;
    }


    /**
     * Gets the longProperty value for this Property.
     * 
     * @return longProperty
     */
    public long getLongProperty() {
        return longProperty;
    }


    /**
     * Sets the longProperty value for this Property.
     * 
     * @param longProperty
     */
    public void setLongProperty(long longProperty) {
        this.longProperty = longProperty;
    }


    /**
     * Gets the boolProperty value for this Property.
     * 
     * @return boolProperty
     */
    public boolean isBoolProperty() {
        return boolProperty;
    }


    /**
     * Sets the boolProperty value for this Property.
     * 
     * @param boolProperty
     */
    public void setBoolProperty(boolean boolProperty) {
        this.boolProperty = boolProperty;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Property)) return false;
        Property other = (Property) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            ((this.transactionId==null && other.getTransactionId()==null) || 
             (this.transactionId!=null &&
              this.transactionId.equals(other.getTransactionId()))) &&
            ((this.nodeId==null && other.getNodeId()==null) || 
             (this.nodeId!=null &&
              this.nodeId.equals(other.getNodeId()))) &&
            ((this.activityId==null && other.getActivityId()==null) || 
             (this.activityId!=null &&
              this.activityId.equals(other.getActivityId()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.stringProperty==null && other.getStringProperty()==null) || 
             (this.stringProperty!=null &&
              this.stringProperty.equals(other.getStringProperty()))) &&
            this.intProperty == other.getIntProperty() &&
            this.longProperty == other.getLongProperty() &&
            this.boolProperty == other.isBoolProperty();
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
        if (getTransactionId() != null) {
            _hashCode += getTransactionId().hashCode();
        }
        if (getNodeId() != null) {
            _hashCode += getNodeId().hashCode();
        }
        if (getActivityId() != null) {
            _hashCode += getActivityId().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getStringProperty() != null) {
            _hashCode += getStringProperty().hashCode();
        }
        _hashCode += getIntProperty();
        _hashCode += new Long(getLongProperty()).hashCode();
        _hashCode += (isBoolProperty() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Property.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Property"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "transactionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activityId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "activityId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stringProperty");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stringProperty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("intProperty");
        elemField.setXmlName(new javax.xml.namespace.QName("", "intProperty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longProperty");
        elemField.setXmlName(new javax.xml.namespace.QName("", "longProperty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boolProperty");
        elemField.setXmlName(new javax.xml.namespace.QName("", "boolProperty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
