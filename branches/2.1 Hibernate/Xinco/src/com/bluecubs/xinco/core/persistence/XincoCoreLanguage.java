/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.server.persistence.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz
 */
@Entity
@Table(name = "xinco_core_language")
@NamedQueries({@NamedQuery(name = "XincoCoreLanguage.findById", query = "SELECT x FROM XincoCoreLanguage x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreLanguage.findBySign", query = "SELECT x FROM XincoCoreLanguage x WHERE x.sign = :sign"), @NamedQuery(name = "XincoCoreLanguage.findByDesignation", query = "SELECT x FROM XincoCoreLanguage x WHERE x.designation = :designation")})
public class XincoCoreLanguage extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "sign", nullable = false)
    private String sign;
    @Column(name = "designation", nullable = false)
    private String designation;

    public XincoCoreLanguage() {
    }

    public XincoCoreLanguage(Integer id) {
        this.id = id;
    }

    public XincoCoreLanguage(Integer id, String sign, String designation) {
        this.id = id;
        this.sign = sign;
        this.designation = designation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreLanguage)) {
            return false;
        }
        XincoCoreLanguage other = (XincoCoreLanguage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreLanguage[id=" + id + "]";
    }

}
