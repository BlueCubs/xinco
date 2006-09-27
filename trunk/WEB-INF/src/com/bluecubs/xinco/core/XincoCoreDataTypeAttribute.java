/**
 * XincoCoreDataTypeAttribute.java
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
 * Name:            XincoCoreDataTypeAttribute
 *
 * Description:     data type attribute
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

package com.bluecubs.xinco.core;

public class XincoCoreDataTypeAttribute  implements java.io.Serializable {
    private java.lang.String designation;
    private int attribute_id;
    private java.lang.String data_type;
    private int size;
    private int xinco_core_data_type_id;

    public XincoCoreDataTypeAttribute() {
    }

    public java.lang.String getDesignation() {
        return designation;
    }

    public void setDesignation(java.lang.String designation) {
        this.designation = designation;
    }

    public int getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(int attribute_id) {
        this.attribute_id = attribute_id;
    }

    public java.lang.String getData_type() {
        return data_type;
    }

    public void setData_type(java.lang.String data_type) {
        this.data_type = data_type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getXinco_core_data_type_id() {
        return xinco_core_data_type_id;
    }

    public void setXinco_core_data_type_id(int xinco_core_data_type_id) {
        this.xinco_core_data_type_id = xinco_core_data_type_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreDataTypeAttribute)) return false;
        XincoCoreDataTypeAttribute other = (XincoCoreDataTypeAttribute) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.designation==null && other.getDesignation()==null) || 
             (this.designation!=null &&
              this.designation.equals(other.getDesignation()))) &&
            this.attribute_id == other.getAttribute_id() &&
            ((this.data_type==null && other.getData_type()==null) || 
             (this.data_type!=null &&
              this.data_type.equals(other.getData_type()))) &&
            this.size == other.getSize() &&
            this.xinco_core_data_type_id == other.getXinco_core_data_type_id();
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
        if (getDesignation() != null) {
            _hashCode += getDesignation().hashCode();
        }
        _hashCode += getAttribute_id();
        if (getData_type() != null) {
            _hashCode += getData_type().hashCode();
        }
        _hashCode += getSize();
        _hashCode += getXinco_core_data_type_id();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreDataTypeAttribute.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreDataTypeAttribute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("designation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "designation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attribute_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attribute_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_data_type_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_type_id"));
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
