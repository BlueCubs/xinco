/**
 * Property.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class Property  implements java.io.Serializable {
    private int id;

    private java.lang.String description;

    private java.lang.String stringProperty;

    private int intProperty;

    private long longProperty;

    private boolean boolProperty;

    private java.lang.Integer changerID;

    public Property() {
    }

    public Property(
           int id,
           java.lang.String description,
           java.lang.String stringProperty,
           int intProperty,
           long longProperty,
           boolean boolProperty,
           java.lang.Integer changerID) {
           this.id = id;
           this.description = description;
           this.stringProperty = stringProperty;
           this.intProperty = intProperty;
           this.longProperty = longProperty;
           this.boolProperty = boolProperty;
           this.changerID = changerID;
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


    /**
     * Gets the changerID value for this Property.
     * 
     * @return changerID
     */
    public java.lang.Integer getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this Property.
     * 
     * @param changerID
     */
    public void setChangerID(java.lang.Integer changerID) {
        this.changerID = changerID;
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
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.stringProperty==null && other.getStringProperty()==null) || 
             (this.stringProperty!=null &&
              this.stringProperty.equals(other.getStringProperty()))) &&
            this.intProperty == other.getIntProperty() &&
            this.longProperty == other.getLongProperty() &&
            this.boolProperty == other.isBoolProperty() &&
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
        if (getStringProperty() != null) {
            _hashCode += getStringProperty().hashCode();
        }
        _hashCode += getIntProperty();
        _hashCode += new Long(getLongProperty()).hashCode();
        _hashCode += (isBoolProperty() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getChangerID() != null) {
            _hashCode += getChangerID().hashCode();
        }
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
