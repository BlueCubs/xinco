/**
 * XincoVersion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.bluecubs.xinco.core;

public class XincoVersion implements java.io.Serializable {

    private static final long serialVersionUID = 4585756076441927061L;
    private int version_high;
    private int version_low;
    private int version_mid;
    private java.lang.String version_postfix;

    public XincoVersion() {
    }

    public XincoVersion(
            int version_high,
            int version_low,
            int version_mid,
            java.lang.String version_postfix) {
        this.version_high = version_high;
        this.version_low = version_low;
        this.version_mid = version_mid;
        this.version_postfix = version_postfix;
    }

    /**
     * Gets the version_high value for this XincoVersion.
     * 
     * @return version_high
     */
    public int getVersionHigh() {
        return version_high;
    }

    /**
     * Sets the version_high value for this XincoVersion.
     * 
     * @param version_high
     */
    public void setVersionHigh(int version_high) {
        this.version_high = version_high;
    }

    /**
     * Gets the version_low value for this XincoVersion.
     * 
     * @return version_low
     */
    public int getVersionLow() {
        return version_low;
    }

    /**
     * Sets the version_low value for this XincoVersion.
     * 
     * @param version_low
     */
    public void setVersionLow(int version_low) {
        this.version_low = version_low;
    }

    /**
     * Gets the version_mid value for this XincoVersion.
     * 
     * @return version_mid
     */
    public int getVersionMid() {
        return version_mid;
    }

    /**
     * Sets the version_mid value for this XincoVersion.
     * 
     * @param version_mid
     */
    public void setVersionMid(int version_mid) {
        this.version_mid = version_mid;
    }

    /**
     * Gets the version_postfix value for this XincoVersion.
     * 
     * @return version_postfix
     */
    public java.lang.String getVersionPostfix() {
        return version_postfix;
    }

    /**
     * Sets the version_postfix value for this XincoVersion.
     * 
     * @param version_postfix
     */
    public void setVersionPostfix(java.lang.String version_postfix) {
        this.version_postfix = version_postfix;
    }
}
