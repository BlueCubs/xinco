/**
 * XincoCoreAudit.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreAudit  implements java.io.Serializable {
    private int schedule_id;

    private int data_id;

    private int schedule_type_id;

    private java.util.Date scheduled_date;

    private java.util.Date completion_date;

    private int completedBy;

    public XincoCoreAudit() {
    }

    public XincoCoreAudit(
           int schedule_id,
           int data_id,
           int schedule_type_id,
           java.util.Date scheduled_date,
           java.util.Date completion_date,
           int completedBy) {
           this.schedule_id = schedule_id;
           this.data_id = data_id;
           this.schedule_type_id = schedule_type_id;
           this.scheduled_date = scheduled_date;
           this.completion_date = completion_date;
           this.completedBy = completedBy;
    }


    /**
     * Gets the schedule_id value for this XincoCoreAudit.
     * 
     * @return schedule_id
     */
    public int getSchedule_id() {
        return schedule_id;
    }


    /**
     * Sets the schedule_id value for this XincoCoreAudit.
     * 
     * @param schedule_id
     */
    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }


    /**
     * Gets the data_id value for this XincoCoreAudit.
     * 
     * @return data_id
     */
    public int getData_id() {
        return data_id;
    }


    /**
     * Sets the data_id value for this XincoCoreAudit.
     * 
     * @param data_id
     */
    public void setData_id(int data_id) {
        this.data_id = data_id;
    }


    /**
     * Gets the schedule_type_id value for this XincoCoreAudit.
     * 
     * @return schedule_type_id
     */
    public int getSchedule_type_id() {
        return schedule_type_id;
    }


    /**
     * Sets the schedule_type_id value for this XincoCoreAudit.
     * 
     * @param schedule_type_id
     */
    public void setSchedule_type_id(int schedule_type_id) {
        this.schedule_type_id = schedule_type_id;
    }


    /**
     * Gets the scheduled_date value for this XincoCoreAudit.
     * 
     * @return scheduled_date
     */
    public java.util.Date getScheduled_date() {
        return scheduled_date;
    }


    /**
     * Sets the scheduled_date value for this XincoCoreAudit.
     * 
     * @param scheduled_date
     */
    public void setScheduled_date(java.util.Date scheduled_date) {
        this.scheduled_date = scheduled_date;
    }


    /**
     * Gets the completion_date value for this XincoCoreAudit.
     * 
     * @return completion_date
     */
    public java.util.Date getCompletion_date() {
        return completion_date;
    }


    /**
     * Sets the completion_date value for this XincoCoreAudit.
     * 
     * @param completion_date
     */
    public void setCompletion_date(java.util.Date completion_date) {
        this.completion_date = completion_date;
    }


    /**
     * Gets the completedBy value for this XincoCoreAudit.
     * 
     * @return completedBy
     */
    public int getCompletedBy() {
        return completedBy;
    }


    /**
     * Sets the completedBy value for this XincoCoreAudit.
     * 
     * @param completedBy
     */
    public void setCompletedBy(int completedBy) {
        this.completedBy = completedBy;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreAudit)) return false;
        XincoCoreAudit other = (XincoCoreAudit) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.schedule_id == other.getSchedule_id() &&
            this.data_id == other.getData_id() &&
            this.schedule_type_id == other.getSchedule_type_id() &&
            ((this.scheduled_date==null && other.getScheduled_date()==null) || 
             (this.scheduled_date!=null &&
              this.scheduled_date.equals(other.getScheduled_date()))) &&
            ((this.completion_date==null && other.getCompletion_date()==null) || 
             (this.completion_date!=null &&
              this.completion_date.equals(other.getCompletion_date()))) &&
            this.completedBy == other.getCompletedBy();
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
        _hashCode += getSchedule_id();
        _hashCode += getData_id();
        _hashCode += getSchedule_type_id();
        if (getScheduled_date() != null) {
            _hashCode += getScheduled_date().hashCode();
        }
        if (getCompletion_date() != null) {
            _hashCode += getCompletion_date().hashCode();
        }
        _hashCode += getCompletedBy();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreAudit.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreAudit"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("schedule_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "schedule_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("schedule_type_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "schedule_type_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduled_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "scheduled_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("completion_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "completion_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("completedBy");
        elemField.setXmlName(new javax.xml.namespace.QName("", "completedBy"));
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
