/**
 * XincoAddAttribute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.add;

public class XincoAddAttribute  implements java.io.Serializable {
    private java.util.Calendar attribDatetime;

    private double attribDouble;

    private int attribInt;

    private java.lang.String attribText;

    private long attribUnsignedint;

    private java.lang.String attribVarchar;

    private int attributeId;

    private int xincoCoreDataId;

    private int changerID;

    private boolean change;

    public XincoAddAttribute() {
    }

    public XincoAddAttribute(
           java.util.Calendar attribDatetime,
           double attribDouble,
           int attribInt,
           java.lang.String attribText,
           long attribUnsignedint,
           java.lang.String attribVarchar,
           int attributeId,
           int xincoCoreDataId,
           int changerID,
           boolean change) {
           this.attribDatetime = attribDatetime;
           this.attribDouble = attribDouble;
           this.attribInt = attribInt;
           this.attribText = attribText;
           this.attribUnsignedint = attribUnsignedint;
           this.attribVarchar = attribVarchar;
           this.attributeId = attributeId;
           this.xincoCoreDataId = xincoCoreDataId;
           this.changerID = changerID;
           this.change = change;
    }


    /**
     * Gets the attribDatetime value for this XincoAddAttribute.
     * 
     * @return attribDatetime
     */
    public java.util.Calendar getAttribDatetime() {
        return attribDatetime;
    }


    /**
     * Sets the attribDatetime value for this XincoAddAttribute.
     * 
     * @param attribDatetime
     */
    public void setAttribDatetime(java.util.Calendar attribDatetime) {
        this.attribDatetime = attribDatetime;
    }


    /**
     * Gets the attribDouble value for this XincoAddAttribute.
     * 
     * @return attribDouble
     */
    public double getAttribDouble() {
        return attribDouble;
    }


    /**
     * Sets the attribDouble value for this XincoAddAttribute.
     * 
     * @param attribDouble
     */
    public void setAttribDouble(double attribDouble) {
        this.attribDouble = attribDouble;
    }


    /**
     * Gets the attribInt value for this XincoAddAttribute.
     * 
     * @return attribInt
     */
    public int getAttribInt() {
        return attribInt;
    }


    /**
     * Sets the attribInt value for this XincoAddAttribute.
     * 
     * @param attribInt
     */
    public void setAttribInt(int attribInt) {
        this.attribInt = attribInt;
    }


    /**
     * Gets the attribText value for this XincoAddAttribute.
     * 
     * @return attribText
     */
    public java.lang.String getAttribText() {
        return attribText;
    }


    /**
     * Sets the attribText value for this XincoAddAttribute.
     * 
     * @param attribText
     */
    public void setAttribText(java.lang.String attribText) {
        this.attribText = attribText;
    }


    /**
     * Gets the attribUnsignedint value for this XincoAddAttribute.
     * 
     * @return attribUnsignedint
     */
    public long getAttribUnsignedint() {
        return attribUnsignedint;
    }


    /**
     * Sets the attribUnsignedint value for this XincoAddAttribute.
     * 
     * @param attribUnsignedint
     */
    public void setAttribUnsignedint(long attribUnsignedint) {
        this.attribUnsignedint = attribUnsignedint;
    }


    /**
     * Gets the attribVarchar value for this XincoAddAttribute.
     * 
     * @return attribVarchar
     */
    public java.lang.String getAttribVarchar() {
        return attribVarchar;
    }


    /**
     * Sets the attribVarchar value for this XincoAddAttribute.
     * 
     * @param attribVarchar
     */
    public void setAttribVarchar(java.lang.String attribVarchar) {
        this.attribVarchar = attribVarchar;
    }


    /**
     * Gets the attributeId value for this XincoAddAttribute.
     * 
     * @return attributeId
     */
    public int getAttributeId() {
        return attributeId;
    }


    /**
     * Sets the attributeId value for this XincoAddAttribute.
     * 
     * @param attributeId
     */
    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }


    /**
     * Gets the xincoCoreDataId value for this XincoAddAttribute.
     * 
     * @return xincoCoreDataId
     */
    public int getXincoCoreDataId() {
        return xincoCoreDataId;
    }


    /**
     * Sets the xincoCoreDataId value for this XincoAddAttribute.
     * 
     * @param xincoCoreDataId
     */
    public void setXincoCoreDataId(int xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
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


    /**
     * Gets the change value for this XincoAddAttribute.
     * 
     * @return change
     */
    public boolean isChange() {
        return change;
    }


    /**
     * Sets the change value for this XincoAddAttribute.
     * 
     * @param change
     */
    public void setChange(boolean change) {
        this.change = change;
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
            ((this.attribDatetime==null && other.getAttribDatetime()==null) || 
             (this.attribDatetime!=null &&
              this.attribDatetime.equals(other.getAttribDatetime()))) &&
            this.attribDouble == other.getAttribDouble() &&
            this.attribInt == other.getAttribInt() &&
            ((this.attribText==null && other.getAttribText()==null) || 
             (this.attribText!=null &&
              this.attribText.equals(other.getAttribText()))) &&
            this.attribUnsignedint == other.getAttribUnsignedint() &&
            ((this.attribVarchar==null && other.getAttribVarchar()==null) || 
             (this.attribVarchar!=null &&
              this.attribVarchar.equals(other.getAttribVarchar()))) &&
            this.attributeId == other.getAttributeId() &&
            this.xincoCoreDataId == other.getXincoCoreDataId() &&
            this.changerID == other.getChangerID() &&
            this.change == other.isChange();
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
        if (getAttribDatetime() != null) {
            _hashCode += getAttribDatetime().hashCode();
        }
        _hashCode += new Double(getAttribDouble()).hashCode();
        _hashCode += getAttribInt();
        if (getAttribText() != null) {
            _hashCode += getAttribText().hashCode();
        }
        _hashCode += new Long(getAttribUnsignedint()).hashCode();
        if (getAttribVarchar() != null) {
            _hashCode += getAttribVarchar().hashCode();
        }
        _hashCode += getAttributeId();
        _hashCode += getXincoCoreDataId();
        _hashCode += getChangerID();
        _hashCode += (isChange() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoAddAttribute.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://add.xinco.bluecubs.com", "XincoAddAttribute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribDatetime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribDatetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribDouble");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribDouble"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribInt");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribInt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribUnsignedint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribUnsignedint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribVarchar");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribVarchar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xincoCoreDataId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xincoCoreDataId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("change");
        elemField.setXmlName(new javax.xml.namespace.QName("", "change"));
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