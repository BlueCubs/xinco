/**
 * XincoCoreUser.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

public class XincoCoreUser  implements java.io.Serializable {
    private int attempts;

    private boolean change;

    private int changerID;

    private java.lang.String email;

    private java.lang.String firstname;

    private int id;

    private java.lang.Object lastModified;

    private java.lang.String name;

    private java.lang.String reason;

    private int status_number;

    private java.lang.String username;

    private java.lang.String userpassword;

    private boolean writeGroups;

    private java.util.Vector xinco_core_groups;

    public XincoCoreUser() {
    }

    public XincoCoreUser(
           int attempts,
           boolean change,
           int changerID,
           java.lang.String email,
           java.lang.String firstname,
           int id,
           java.lang.Object lastModified,
           java.lang.String name,
           java.lang.String reason,
           int status_number,
           java.lang.String username,
           java.lang.String userpassword,
           boolean writeGroups,
           java.util.Vector xinco_core_groups) {
           this.attempts = attempts;
           this.change = change;
           this.changerID = changerID;
           this.email = email;
           this.firstname = firstname;
           this.id = id;
           this.lastModified = lastModified;
           this.name = name;
           this.reason = reason;
           this.status_number = status_number;
           this.username = username;
           this.userpassword = userpassword;
           this.writeGroups = writeGroups;
           this.xinco_core_groups = xinco_core_groups;
    }


    /**
     * Gets the attempts value for this XincoCoreUser.
     * 
     * @return attempts
     */
    public int getAttempts() {
        return attempts;
    }


    /**
     * Sets the attempts value for this XincoCoreUser.
     * 
     * @param attempts
     */
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }


    /**
     * Gets the change value for this XincoCoreUser.
     * 
     * @return change
     */
    public boolean isChange() {
        return change;
    }


    /**
     * Sets the change value for this XincoCoreUser.
     * 
     * @param change
     */
    public void setChange(boolean change) {
        this.change = change;
    }


    /**
     * Gets the changerID value for this XincoCoreUser.
     * 
     * @return changerID
     */
    public int getChangerID() {
        return changerID;
    }


    /**
     * Sets the changerID value for this XincoCoreUser.
     * 
     * @param changerID
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }


    /**
     * Gets the email value for this XincoCoreUser.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this XincoCoreUser.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the firstname value for this XincoCoreUser.
     * 
     * @return firstname
     */
    public java.lang.String getFirstname() {
        return firstname;
    }


    /**
     * Sets the firstname value for this XincoCoreUser.
     * 
     * @param firstname
     */
    public void setFirstname(java.lang.String firstname) {
        this.firstname = firstname;
    }


    /**
     * Gets the id value for this XincoCoreUser.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this XincoCoreUser.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the lastModified value for this XincoCoreUser.
     * 
     * @return lastModified
     */
    public java.lang.Object getLastModified() {
        return lastModified;
    }


    /**
     * Sets the lastModified value for this XincoCoreUser.
     * 
     * @param lastModified
     */
    public void setLastModified(java.lang.Object lastModified) {
        this.lastModified = lastModified;
    }


    /**
     * Gets the name value for this XincoCoreUser.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this XincoCoreUser.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the reason value for this XincoCoreUser.
     * 
     * @return reason
     */
    public java.lang.String getReason() {
        return reason;
    }


    /**
     * Sets the reason value for this XincoCoreUser.
     * 
     * @param reason
     */
    public void setReason(java.lang.String reason) {
        this.reason = reason;
    }


    /**
     * Gets the status_number value for this XincoCoreUser.
     * 
     * @return status_number
     */
    public int getStatus_number() {
        return status_number;
    }


    /**
     * Sets the status_number value for this XincoCoreUser.
     * 
     * @param status_number
     */
    public void setStatus_number(int status_number) {
        this.status_number = status_number;
    }


    /**
     * Gets the username value for this XincoCoreUser.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this XincoCoreUser.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the userpassword value for this XincoCoreUser.
     * 
     * @return userpassword
     */
    public java.lang.String getUserpassword() {
        return userpassword;
    }


    /**
     * Sets the userpassword value for this XincoCoreUser.
     * 
     * @param userpassword
     */
    public void setUserpassword(java.lang.String userpassword) {
        this.userpassword = userpassword;
    }


    /**
     * Gets the writeGroups value for this XincoCoreUser.
     * 
     * @return writeGroups
     */
    public boolean isWriteGroups() {
        return writeGroups;
    }


    /**
     * Sets the writeGroups value for this XincoCoreUser.
     * 
     * @param writeGroups
     */
    public void setWriteGroups(boolean writeGroups) {
        this.writeGroups = writeGroups;
    }


    /**
     * Gets the xinco_core_groups value for this XincoCoreUser.
     * 
     * @return xinco_core_groups
     */
    public java.util.Vector getXinco_core_groups() {
        return xinco_core_groups;
    }


    /**
     * Sets the xinco_core_groups value for this XincoCoreUser.
     * 
     * @param xinco_core_groups
     */
    public void setXinco_core_groups(java.util.Vector xinco_core_groups) {
        this.xinco_core_groups = xinco_core_groups;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XincoCoreUser)) return false;
        XincoCoreUser other = (XincoCoreUser) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.attempts == other.getAttempts() &&
            this.change == other.isChange() &&
            this.changerID == other.getChangerID() &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.firstname==null && other.getFirstname()==null) || 
             (this.firstname!=null &&
              this.firstname.equals(other.getFirstname()))) &&
            this.id == other.getId() &&
            ((this.lastModified==null && other.getLastModified()==null) || 
             (this.lastModified!=null &&
              this.lastModified.equals(other.getLastModified()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.reason==null && other.getReason()==null) || 
             (this.reason!=null &&
              this.reason.equals(other.getReason()))) &&
            this.status_number == other.getStatus_number() &&
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.userpassword==null && other.getUserpassword()==null) || 
             (this.userpassword!=null &&
              this.userpassword.equals(other.getUserpassword()))) &&
            this.writeGroups == other.isWriteGroups() &&
            ((this.xinco_core_groups==null && other.getXinco_core_groups()==null) || 
             (this.xinco_core_groups!=null &&
              this.xinco_core_groups.equals(other.getXinco_core_groups())));
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
        _hashCode += getAttempts();
        _hashCode += (isChange() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getChangerID();
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getFirstname() != null) {
            _hashCode += getFirstname().hashCode();
        }
        _hashCode += getId();
        if (getLastModified() != null) {
            _hashCode += getLastModified().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getReason() != null) {
            _hashCode += getReason().hashCode();
        }
        _hashCode += getStatus_number();
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getUserpassword() != null) {
            _hashCode += getUserpassword().hashCode();
        }
        _hashCode += (isWriteGroups() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getXinco_core_groups() != null) {
            _hashCode += getXinco_core_groups().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XincoCoreUser.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attempts");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attempts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("change");
        elemField.setXmlName(new javax.xml.namespace.QName("", "change"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changerID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firstname");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firstname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastModified");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lastModified"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reason");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reason"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status_number");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status_number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("", "username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userpassword");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userpassword"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("writeGroups");
        elemField.setXmlName(new javax.xml.namespace.QName("", "writeGroups"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xinco_core_groups");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xinco_core_groups"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
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
