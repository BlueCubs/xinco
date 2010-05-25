package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoAddAttributePK implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "xinco_core_data_id", nullable = false)
    private int xincoCoreDataId;
    @Basic(optional = false)
    @Column(name = "attribute_id", nullable = false)
    private int attributeId;

    public XincoAddAttributePK() {
    }

    public XincoAddAttributePK(int xincoCoreDataId, int attributeId) {
        this.xincoCoreDataId = xincoCoreDataId;
        this.attributeId = attributeId;
    }

    public int getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    public void setXincoCoreDataId(int xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += xincoCoreDataId;
        hash += attributeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof XincoAddAttributePK)) {
            return false;
        }
        XincoAddAttributePK other = (XincoAddAttributePK) object;
        if (this.xincoCoreDataId != other.xincoCoreDataId) {
            return false;
        }
        if (this.attributeId != other.attributeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK[xincoCoreDataId=" + xincoCoreDataId + ", attributeId=" + attributeId + "]";
    }
}