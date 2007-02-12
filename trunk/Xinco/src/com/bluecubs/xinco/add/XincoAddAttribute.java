/**
 * XincoAddAttribute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.add;

public class XincoAddAttribute  implements java.io.Serializable {
    private java.util.Calendar attrib_datetime;

    private double attrib_double;

    private int attrib_int;

    private java.lang.String attrib_text;

    private long attrib_unsignedint;

    private java.lang.String attrib_varchar;

    private int attribute_id;

    private int xinco_core_data_id;

    private int changerID;

    public XincoAddAttribute() {
    }

    public XincoAddAttribute(
           java.util.Calendar attrib_datetime,
           double attrib_double,
           int attrib_int,
           java.lang.String attrib_text,
           long attrib_unsignedint,
           java.lang.String attrib_varchar,
           int attribute_id,
           int xinco_core_data_id,
           int changerID) {
           this.attrib_datetime = attrib_datetime;
           this.attrib_double = attrib_double;
           this.attrib_int = attrib_int;
           this.attrib_text = attrib_text;
           this.attrib_unsignedint = attrib_unsignedint;
           this.attrib_varchar = attrib_varchar;
           this.attribute_id = attribute_id;
           this.xinco_core_data_id = xinco_core_data_id;
           this.changerID = changerID;
    }


    /**
     * Gets the attrib_datetime value for this XincoAddAttribute.
     * 
     * @return attrib_datetime
     */
    public java.util.Calendar getAttrib_datetime() {
        return attrib_datetime;
    }


    /**
     * Sets the attrib_datetime value for this XincoAddAttribute.
     * 
     * @param attrib_datetime
     */
    public void setAttrib_datetime(java.util.Calendar attrib_datetime) {
        this.attrib_datetime = attrib_datetime;
    }


    /**
     * Gets the attrib_double value for this XincoAddAttribute.
     * 
     * @return attrib_double
     */
    public double getAttrib_double() {
        return attrib_double;
    }


    /**
     * Sets the attrib_double value for this XincoAddAttribute.
     * 
     * @param attrib_double
     */
    public void setAttrib_double(double attrib_double) {
        this.attrib_double = attrib_double;
    }


    /**
     * Gets the attrib_int value for this XincoAddAttribute.
     * 
     * @return attrib_int
     */
    public int getAttrib_int() {
        return attrib_int;
    }


    /**
     * Sets the attrib_int value for this XincoAddAttribute.
     * 
     * @param attrib_int
     */
    public void setAttrib_int(int attrib_int) {
        this.attrib_int = attrib_int;
    }


    /**
     * Gets the attrib_text value for this XincoAddAttribute.
     * 
     * @return attrib_text
     */
    public java.lang.String getAttrib_text() {
        return attrib_text;
    }


    /**
     * Sets the attrib_text value for this XincoAddAttribute.
     * 
     * @param attrib_text
     */
    public void setAttrib_text(java.lang.String attrib_text) {
        this.attrib_text = attrib_text;
    }


    /**
     * Gets the attrib_unsignedint value for this XincoAddAttribute.
     * 
     * @return attrib_unsignedint
     */
    public long getAttrib_unsignedint() {
        return attrib_unsignedint;
    }


    /**
     * Sets the attrib_unsignedint value for this XincoAddAttribute.
     * 
     * @param attrib_unsignedint
     */
    public void setAttrib_unsignedint(long attrib_unsignedint) {
        this.attrib_unsignedint = attrib_unsignedint;
    }


    /**
     * Gets the attrib_varchar value for this XincoAddAttribute.
     * 
     * @return attrib_varchar
     */
    public java.lang.String getAttrib_varchar() {
        return attrib_varchar;
    }


    /**
     * Sets the attrib_varchar value for this XincoAddAttribute.
     * 
     * @param attrib_varchar
     */
    public void setAttrib_varchar(java.lang.String attrib_varchar) {
        this.attrib_varchar = attrib_varchar;
    }


    /**
     * Gets the attribute_id value for this XincoAddAttribute.
     * 
     * @return attribute_id
     */
    public int getAttribute_id() {
        return attribute_id;
    }


    /**
     * Sets the attribute_id value for this XincoAddAttribute.
     * 
     * @param attribute_id
     */
    public void setAttribute_id(int attribute_id) {
        this.attribute_id = attribute_id;
    }


    /**
     * Gets the xinco_core_data_id value for this XincoAddAttribute.
     * 
     * @return xinco_core_data_id
     */
    public int getXinco_core_data_id() {
        return xinco_core_data_id;
    }


    /**
     * Sets the xinco_core_data_id value for this XincoAddAttribute.
     * 
     * @param xinco_core_data_id
     */
    public void setXinco_core_data_id(int xinco_core_data_id) {
        this.xinco_core_data_id = xinco_core_data_id;
    }


    /**
     * Gets the changerID value for this XincoAddAttribute.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this XincoAddAttribute.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoAddAttribute)) return false;
        XincoAddAttribute other = (XincoAddAttribute) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attrib_datetime==null && other.getAttrib_datetime()==null) || 
             (this.attrib_datetime!=null &&
              this.attrib_datetime.equals(other.getAttrib_datetime()))) &&
            this.attrib_double == other.getAttrib_double() &&
            this.attrib_int == other.getAttrib_int() &&
            ((this.attrib_text==null && other.getAttrib_text()==null) || 
             (this.attrib_text!=null &&
              this.attrib_text.equals(other.getAttrib_text()))) &&
            this.attrib_unsignedint == other.getAttrib_unsignedint() &&
            ((this.attrib_varchar==null && other.getAttrib_varchar()==null) || 
             (this.attrib_varchar!=null &&
              this.attrib_varchar.equals(other.getAttrib_varchar()))) &&
            this.attribute_id == other.getAttribute_id() &&
            this.xinco_core_data_id == other.getXinco_core_data_id() &&
            this.changerID == other.getChangerID();
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
        if (getAttrib_datetime() != null) {
            _hashCode += getAttrib_datetime().hashCode();
        }
        _hashCode += new Double(getAttrib_double()).hashCode();
        _hashCode += getAttrib_int();
        if (getAttrib_text() != null) {
            _hashCode += getAttrib_text().hashCode();
        }
        _hashCode += new Long(getAttrib_unsignedint()).hashCode();
        if (getAttrib_varchar() != null) {
            _hashCode += getAttrib_varchar().hashCode();
        }
        _hashCode += getAttribute_id();
        _hashCode += getXinco_core_data_id();
        _hashCode += getChangerID();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoAddAttribute.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://add.xinco.bluecubs.com", "XincoAddAttribute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_datetime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_datetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_double");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_double"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_int");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_int"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_text");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_unsignedint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_unsignedint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_varchar");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_varchar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribute_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribute_id"));
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
        elemField.setFieldName("changerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changerID"));
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
