/**
 * XincoAddAttribute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

/**
 *Copyright 2004 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            XincoAddAttribute
 *
 * Description:     additional attributes of a data object 
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 * 
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
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

    public XincoAddAttribute() {
    }

    public java.util.Calendar getAttrib_datetime() {
        return attrib_datetime;
    }

    public void setAttrib_datetime(java.util.Calendar attrib_datetime) {
        this.attrib_datetime = attrib_datetime;
    }

    public double getAttrib_double() {
        return attrib_double;
    }

    public void setAttrib_double(double attrib_double) {
        this.attrib_double = attrib_double;
    }

    public int getAttrib_int() {
        return attrib_int;
    }

    public void setAttrib_int(int attrib_int) {
        this.attrib_int = attrib_int;
    }

    public java.lang.String getAttrib_text() {
        return attrib_text;
    }

    public void setAttrib_text(java.lang.String attrib_text) {
        this.attrib_text = attrib_text;
    }

    public long getAttrib_unsignedint() {
        return attrib_unsignedint;
    }

    public void setAttrib_unsignedint(long attrib_unsignedint) {
        this.attrib_unsignedint = attrib_unsignedint;
    }

    public java.lang.String getAttrib_varchar() {
        return attrib_varchar;
    }

    public void setAttrib_varchar(java.lang.String attrib_varchar) {
        this.attrib_varchar = attrib_varchar;
    }

    public int getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(int attribute_id) {
        this.attribute_id = attribute_id;
    }

    public int getXinco_core_data_id() {
        return xinco_core_data_id;
    }

    public void setXinco_core_data_id(int xinco_core_data_id) {
        this.xinco_core_data_id = xinco_core_data_id;
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
            this.xinco_core_data_id == other.getXinco_core_data_id();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoAddAttribute.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://add.xinco.bluecubs.com", "XincoAddAttribute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_datetime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_datetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_double");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_double"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_int");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_int"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_text");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_unsignedint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_unsignedint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attrib_varchar");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attrib_varchar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribute_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribute_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_data_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
