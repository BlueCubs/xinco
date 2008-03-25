/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence.audit;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class: XincoCoreLanguageT
 * Created: Mar 24, 2008 2:44:22 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_language_t")
@NamedQueries({@NamedQuery(name = "XincoCoreLanguageT.findByRecordId", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreLanguageT.findByDesignation", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreLanguageT.findById", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreLanguageT.findBySign", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.sign = :sign")})
public class XincoCoreLanguageT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "sign", nullable = false)
    private String sign;

    public XincoCoreLanguageT() {
    }

    public XincoCoreLanguageT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreLanguageT(Integer recordId, String designation, int id, String sign) {
        this.recordId = recordId;
        this.designation = designation;
        this.id = id;
        this.sign = sign;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreLanguageT)) {
            return false;
        }
        XincoCoreLanguageT other = (XincoCoreLanguageT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.persistence.audit.XincoCoreLanguageT[recordId=" + recordId + "]";
    }

}
