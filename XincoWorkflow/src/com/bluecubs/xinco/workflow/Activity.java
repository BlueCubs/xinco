/**
 * Activity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.workflow;

public class Activity  implements java.io.Serializable {
    private int id;

    private java.lang.String description;

    private java.lang.String classname;

    private com.bluecubs.xinco.workflow.Resource assignedTo;

    private java.util.Calendar assignedOn;

    private java.util.Calendar completedOn;

    private java.util.Vector properties;

    private java.lang.Integer changerID;

    public Activity() {
    }

    public Activity(
           int id,
           java.lang.String description,
           java.lang.String classname,
           com.bluecubs.xinco.workflow.Resource assignedTo,
           java.util.Calendar assignedOn,
           java.util.Calendar completedOn,
           java.util.Vector properties,
           java.lang.Integer changerID) {
           this.id = id;
           this.description = description;
           this.classname = classname;
           this.assignedTo = assignedTo;
           this.assignedOn = assignedOn;
           this.completedOn = completedOn;
           this.properties = properties;
           this.changerID = changerID;
    }


    /**
     * Gets the id value for this Activity.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this Activity.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the description value for this Activity.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Activity.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the classname value for this Activity.
     * 
     * @return classname
     */
    public java.lang.String getClassname() {
        return classname;
    }


    /**
     * Sets the classname value for this Activity.
     * 
     * @param classname
     */
    public void setClassname(java.lang.String classname) {
        this.classname = classname;
    }


    /**
     * Gets the assignedTo value for this Activity.
     * 
     * @return assignedTo
     */
    public com.bluecubs.xinco.workflow.Resource getAssignedTo() {
        return assignedTo;
    }


    /**
     * Sets the assignedTo value for this Activity.
     * 
     * @param assignedTo
     */
    public void setAssignedTo(com.bluecubs.xinco.workflow.Resource assignedTo) {
        this.assignedTo = assignedTo;
    }


    /**
     * Gets the assignedOn value for this Activity.
     * 
     * @return assignedOn
     */
    public java.util.Calendar getAssignedOn() {
        return assignedOn;
    }


    /**
     * Sets the assignedOn value for this Activity.
     * 
     * @param assignedOn
     */
    public void setAssignedOn(java.util.Calendar assignedOn) {
        this.assignedOn = assignedOn;
    }


    /**
     * Gets the completedOn value for this Activity.
     * 
     * @return completedOn
     */
    public java.util.Calendar getCompletedOn() {
        return completedOn;
    }


    /**
     * Sets the completedOn value for this Activity.
     * 
     * @param completedOn
     */
    public void setCompletedOn(java.util.Calendar completedOn) {
        this.completedOn = completedOn;
    }


    /**
     * Gets the properties value for this Activity.
     * 
     * @return properties
     */
    public java.util.Vector getProperties() {
        return properties;
    }


    /**
     * Sets the properties value for this Activity.
     * 
     * @param properties
     */
    public void setProperties(java.util.Vector properties) {
        this.properties = properties;
    }


    /**
     * Gets the changerID value for this Activity.
     * 
     * @return changerID
     */
    public java.lang.Integer getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this Activity.
     * 
     * @param changerID
     */
    public void setChangerID(java.lang.Integer changerID) {
        this.changerID = changerID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Activity)) return false;
        Activity other = (Activity) obj;
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
            ((this.classname==null && other.getClassname()==null) || 
             (this.classname!=null &&
              this.classname.equals(other.getClassname()))) &&
            ((this.assignedTo==null && other.getAssignedTo()==null) || 
             (this.assignedTo!=null &&
              this.assignedTo.equals(other.getAssignedTo()))) &&
            ((this.assignedOn==null && other.getAssignedOn()==null) || 
             (this.assignedOn!=null &&
              this.assignedOn.equals(other.getAssignedOn()))) &&
            ((this.completedOn==null && other.getCompletedOn()==null) || 
             (this.completedOn!=null &&
              this.completedOn.equals(other.getCompletedOn()))) &&
            ((this.properties==null && other.getProperties()==null) || 
             (this.properties!=null &&
              this.properties.equals(other.getProperties()))) &&
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
        if (getClassname() != null) {
            _hashCode += getClassname().hashCode();
        }
        if (getAssignedTo() != null) {
            _hashCode += getAssignedTo().hashCode();
        }
        if (getAssignedOn() != null) {
            _hashCode += getAssignedOn().hashCode();
        }
        if (getCompletedOn() != null) {
            _hashCode += getCompletedOn().hashCode();
        }
        if (getProperties() != null) {
            _hashCode += getProperties().hashCode();
        }
        if (getChangerID() != null) {
            _hashCode += getChangerID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Activity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Activity"));
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
        elemField.setFieldName("classname");
        elemField.setXmlName(new javax.xml.namespace.QName("", "classname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assignedTo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "assignedTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assignedOn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "assignedOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("completedOn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "completedOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("properties");
        elemField.setXmlName(new javax.xml.namespace.QName("", "properties"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
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
