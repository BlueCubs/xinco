/**
 * XincoCoreUser.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.core;

import java.sql.Timestamp;

public class XincoCoreUser  implements java.io.Serializable {
    private java.lang.String email;

    private java.util.Vector xinco_core_groups;

    private java.lang.String firstname;

    private int id;

    private java.lang.String name;

    private int status_number;

    private java.lang.String username;

    private java.lang.String userpassword;

    private boolean change;

    private int changerID;

    private java.lang.String reason;

    private boolean writeGroups;

    private int attempts;

    private java.sql.Timestamp lastModified;

    private boolean hashPassword;

    private boolean increaseAttempts;

    public XincoCoreUser() {
    }

    public XincoCoreUser(
           java.lang.String email,
           java.util.Vector xinco_core_groups,
           java.lang.String firstname,
           int id,
           java.lang.String name,
           int status_number,
           java.lang.String username,
           java.lang.String userpassword,
           boolean change,
           int changerID,
           java.lang.String reason,
           boolean writeGroups,
           int attempts,
           java.sql.Timestamp lastModified,
           boolean hashPassword,
           boolean increaseAttempts) {
           this.email = email;
           this.xinco_core_groups = xinco_core_groups;
           this.firstname = firstname;
           this.id = id;
           this.name = name;
           this.status_number = status_number;
           this.username = username;
           this.userpassword = userpassword;
           this.change = change;
           this.changerID = changerID;
           this.reason = reason;
           this.writeGroups = writeGroups;
           this.attempts = attempts;
           this.lastModified = lastModified;
           this.hashPassword = hashPassword;
           this.increaseAttempts = increaseAttempts;
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

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }
    
    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }
    
    public boolean isHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(boolean hashPassword) {
        this.hashPassword = hashPassword;
    }
    
    public boolean isIncreaseAttempts() {
        return increaseAttempts;
    }

    public void setIncreaseAttempts(boolean increaseAttempts) {
        this.increaseAttempts = increaseAttempts;
    }
    

}
