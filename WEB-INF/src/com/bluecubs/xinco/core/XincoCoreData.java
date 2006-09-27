/**
 * XincoCoreData.java
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
 * Name:            XincoCoreData
 *
 * Description:     data object 
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

public class XincoCoreData  implements java.io.Serializable {
    private java.lang.String designation;
    private java.util.Vector xinco_core_acl;
    private com.bluecubs.xinco.core.XincoCoreDataType xinco_core_data_type;
    private com.bluecubs.xinco.core.XincoCoreLanguage xinco_core_language;
    private int xinco_core_node_id;
    private int id;
    private int status_number;
    private java.util.Vector xinco_add_attributes;
    private java.util.Vector xinco_core_logs;

    public XincoCoreData() {
    }

    public java.lang.String getDesignation() {
        return designation;
    }

    public void setDesignation(java.lang.String designation) {
        this.designation = designation;
    }

    public java.util.Vector getXinco_core_acl() {
        return xinco_core_acl;
    }

    public void setXinco_core_acl(java.util.Vector xinco_core_acl) {
        this.xinco_core_acl = xinco_core_acl;
    }

    public com.bluecubs.xinco.core.XincoCoreDataType getXinco_core_data_type() {
        return xinco_core_data_type;
    }

    public void setXinco_core_data_type(com.bluecubs.xinco.core.XincoCoreDataType xinco_core_data_type) {
        this.xinco_core_data_type = xinco_core_data_type;
    }

    public com.bluecubs.xinco.core.XincoCoreLanguage getXinco_core_language() {
        return xinco_core_language;
    }

    public void setXinco_core_language(com.bluecubs.xinco.core.XincoCoreLanguage xinco_core_language) {
        this.xinco_core_language = xinco_core_language;
    }

    public int getXinco_core_node_id() {
        return xinco_core_node_id;
    }

    public void setXinco_core_node_id(int xinco_core_node_id) {
        this.xinco_core_node_id = xinco_core_node_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus_number() {
        return status_number;
    }

    public void setStatus_number(int status_number) {
        this.status_number = status_number;
    }

    public java.util.Vector getXinco_add_attributes() {
        return xinco_add_attributes;
    }

    public void setXinco_add_attributes(java.util.Vector xinco_add_attributes) {
        this.xinco_add_attributes = xinco_add_attributes;
    }

    public java.util.Vector getXinco_core_logs() {
        return xinco_core_logs;
    }

    public void setXinco_core_logs(java.util.Vector xinco_core_logs) {
        this.xinco_core_logs = xinco_core_logs;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreData)) return false;
        XincoCoreData other = (XincoCoreData) obj;
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
            ((this.xinco_core_acl==null && other.getXinco_core_acl()==null) || 
             (this.xinco_core_acl!=null &&
              this.xinco_core_acl.equals(other.getXinco_core_acl()))) &&
            ((this.xinco_core_data_type==null && other.getXinco_core_data_type()==null) || 
             (this.xinco_core_data_type!=null &&
              this.xinco_core_data_type.equals(other.getXinco_core_data_type()))) &&
            ((this.xinco_core_language==null && other.getXinco_core_language()==null) || 
             (this.xinco_core_language!=null &&
              this.xinco_core_language.equals(other.getXinco_core_language()))) &&
            this.xinco_core_node_id == other.getXinco_core_node_id() &&
            this.id == other.getId() &&
            this.status_number == other.getStatus_number() &&
            ((this.xinco_add_attributes==null && other.getXinco_add_attributes()==null) || 
             (this.xinco_add_attributes!=null &&
              this.xinco_add_attributes.equals(other.getXinco_add_attributes()))) &&
            ((this.xinco_core_logs==null && other.getXinco_core_logs()==null) || 
             (this.xinco_core_logs!=null &&
              this.xinco_core_logs.equals(other.getXinco_core_logs())));
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
        if (getXinco_core_acl() != null) {
            _hashCode += getXinco_core_acl().hashCode();
        }
        if (getXinco_core_data_type() != null) {
            _hashCode += getXinco_core_data_type().hashCode();
        }
        if (getXinco_core_language() != null) {
            _hashCode += getXinco_core_language().hashCode();
        }
        _hashCode += getXinco_core_node_id();
        _hashCode += getId();
        _hashCode += getStatus_number();
        if (getXinco_add_attributes() != null) {
            _hashCode += getXinco_add_attributes().hashCode();
        }
        if (getXinco_core_logs() != null) {
            _hashCode += getXinco_core_logs().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreData.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("designation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "designation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_acl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_acl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_data_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_data_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreDataType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_language");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_language"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLanguage"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_node_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_node_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status_number");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status_number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_add_attributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_add_attributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_logs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_logs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
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
