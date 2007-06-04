/**
 * XincoCoreAuditType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreAuditType  implements java.io.Serializable {
    private int id;

    private int days;

    private int weeks;

    private int months;

    private int years;

    private java.lang.String description;

    private boolean due_same_day;

    private boolean due_same_week;

    private boolean due_same_month;

    public XincoCoreAuditType() {
    }

    public XincoCoreAuditType(
           int id,
           int days,
           int weeks,
           int months,
           int years,
           java.lang.String description,
           boolean due_same_day,
           boolean due_same_week,
           boolean due_same_month) {
           this.id = id;
           this.days = days;
           this.weeks = weeks;
           this.months = months;
           this.years = years;
           this.description = description;
           this.due_same_day = due_same_day;
           this.due_same_week = due_same_week;
           this.due_same_month = due_same_month;
    }


    /**
     * Gets the id value for this XincoCoreAuditType.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoCoreAuditType.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the days value for this XincoCoreAuditType.
     * 
     * @return days
     */
    public int getDays() {
        return days;
    }


    /**
     * Sets the days value for this XincoCoreAuditType.
     * 
     * @param days
     */
    public void setDays(int days) {
        this.days = days;
    }


    /**
     * Gets the weeks value for this XincoCoreAuditType.
     * 
     * @return weeks
     */
    public int getWeeks() {
        return weeks;
    }


    /**
     * Sets the weeks value for this XincoCoreAuditType.
     * 
     * @param weeks
     */
    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }


    /**
     * Gets the months value for this XincoCoreAuditType.
     * 
     * @return months
     */
    public int getMonths() {
        return months;
    }


    /**
     * Sets the months value for this XincoCoreAuditType.
     * 
     * @param months
     */
    public void setMonths(int months) {
        this.months = months;
    }


    /**
     * Gets the years value for this XincoCoreAuditType.
     * 
     * @return years
     */
    public int getYears() {
        return years;
    }


    /**
     * Sets the years value for this XincoCoreAuditType.
     * 
     * @param years
     */
    public void setYears(int years) {
        this.years = years;
    }


    /**
     * Gets the description value for this XincoCoreAuditType.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this XincoCoreAuditType.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the due_same_day value for this XincoCoreAuditType.
     * 
     * @return due_same_day
     */
    public boolean isDue_same_day() {
        return due_same_day;
    }


    /**
     * Sets the due_same_day value for this XincoCoreAuditType.
     * 
     * @param due_same_day
     */
    public void setDue_same_day(boolean due_same_day) {
        this.due_same_day = due_same_day;
    }


    /**
     * Gets the due_same_week value for this XincoCoreAuditType.
     * 
     * @return due_same_week
     */
    public boolean isDue_same_week() {
        return due_same_week;
    }


    /**
     * Sets the due_same_week value for this XincoCoreAuditType.
     * 
     * @param due_same_week
     */
    public void setDue_same_week(boolean due_same_week) {
        this.due_same_week = due_same_week;
    }


    /**
     * Gets the due_same_month value for this XincoCoreAuditType.
     * 
     * @return due_same_month
     */
    public boolean isDue_same_month() {
        return due_same_month;
    }


    /**
     * Sets the due_same_month value for this XincoCoreAuditType.
     * 
     * @param due_same_month
     */
    public void setDue_same_month(boolean due_same_month) {
        this.due_same_month = due_same_month;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreAuditType)) return false;
        XincoCoreAuditType other = (XincoCoreAuditType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.days == other.getDays() &&
            this.weeks == other.getWeeks() &&
            this.months == other.getMonths() &&
            this.years == other.getYears() &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.due_same_day == other.isDue_same_day() &&
            this.due_same_week == other.isDue_same_week() &&
            this.due_same_month == other.isDue_same_month();
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
        _hashCode += getDays();
        _hashCode += getWeeks();
        _hashCode += getMonths();
        _hashCode += getYears();
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += (isDue_same_day() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDue_same_week() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDue_same_month() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreAuditType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreAuditType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("days");
        elemField.setXmlName(new javax.xml.namespace.QName("", "days"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("weeks");
        elemField.setXmlName(new javax.xml.namespace.QName("", "weeks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("months");
        elemField.setXmlName(new javax.xml.namespace.QName("", "months"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("years");
        elemField.setXmlName(new javax.xml.namespace.QName("", "years"));
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
        elemField.setFieldName("due_same_day");
        elemField.setXmlName(new javax.xml.namespace.QName("", "due_same_day"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("due_same_week");
        elemField.setXmlName(new javax.xml.namespace.QName("", "due_same_week"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("due_same_month");
        elemField.setXmlName(new javax.xml.namespace.QName("", "due_same_month"));
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
