package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.server.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier
 */
@Entity
@Table(name = "xinco_core_language", catalog = "xinco", schema = "", 
uniqueConstraints = {@UniqueConstraint(columnNames = {"designation"})})
@NamedQueries({@NamedQuery(name = "XincoCoreLanguage.findAll",
    query = "SELECT x FROM XincoCoreLanguage x"),
    @NamedQuery(name = "XincoCoreLanguage.findById",
    query = "SELECT x FROM XincoCoreLanguage x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreLanguage.findBySign",
    query = "SELECT x FROM XincoCoreLanguage x WHERE x.sign = :sign"),
    @NamedQuery(name = "XincoCoreLanguage.findByDesignation",
    query = "SELECT x FROM XincoCoreLanguage x WHERE x.designation = :designation")
})
public class XincoCoreLanguage extends XincoAbstractAuditableObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer lId;
    @Basic(optional = false)
    @Column(name = "sign", nullable = false, length = 255)
    private String sign;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;

    public XincoCoreLanguage() {
    }

    public XincoCoreLanguage(Integer id) {
        this.lId = id;
    }

    public XincoCoreLanguage(Integer id, String sign, String designation) {
        this.lId = id;
        this.sign = sign;
        this.designation = designation;
    }

    public Integer getId() {
        return lId;
    }

    public void setId(Integer id) {
        this.lId = id;
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
        hash += (lId != null ? lId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreLanguage)) {
            return false;
        }
        XincoCoreLanguage other = (XincoCoreLanguage) object;
        if ((this.lId == null && other.lId != null) || (this.lId != null && !this.lId.equals(other.lId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreLanguage[id=" + lId + "]";
    }
}
