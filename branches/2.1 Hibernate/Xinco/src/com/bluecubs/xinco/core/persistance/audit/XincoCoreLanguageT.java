/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistance.audit;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author javydreamercsw
 */
@Entity
@Table(name = "xinco_core_language_t")
@NamedQueries({@NamedQuery(name = "XincoCoreLanguageT.findByRecordId", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreLanguageT.findById", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreLanguageT.findBySign", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.sign = :sign"), @NamedQuery(name = "XincoCoreLanguageT.findByDesignation", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.designation = :designation")})
public class XincoCoreLanguageT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "sign", nullable = false)
    private String sign;
    @Column(name = "designation", nullable = false)
    private String designation;

    public XincoCoreLanguageT() {
    }

    public XincoCoreLanguageT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreLanguageT(Integer recordId, int id, String sign, String designation) {
        this.recordId = recordId;
        this.id = id;
        this.sign = sign;
        this.designation = designation;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
        return "com.bluecubs.xinco.core.persistance.XincoCoreLanguageT[recordId=" + recordId + "]";
    }

}
