package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_user_has_xinco_core_group_t")
@NamedQueries({
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findAll", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByRecordId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.xincoCoreUserId = :xincoCoreUserId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByXincoCoreGroupId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.xincoCoreGroupId = :xincoCoreGroupId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByStatusNumber", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreUserHasXincoCoreGroupT implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XCUHXCGRECORDIDKEYGEN")
    @TableGenerator(name = "XCUHXCGRECORDIDKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_core_user_modified_record", initialValue = 1, allocationSize = 1)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "xinco_core_user_id", nullable = false)
    private int xincoCoreUserId;
    @Basic(optional = false)
    @Column(name = "xinco_core_group_id", nullable = false)
    private int xincoCoreGroupId;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreUserHasXincoCoreGroupT() {
    }

    public XincoCoreUserHasXincoCoreGroupT(int xincoCoreUserId, int xincoCoreGroupId, int statusNumber) {
        this.xincoCoreUserId = xincoCoreUserId;
        this.xincoCoreGroupId = xincoCoreGroupId;
        this.statusNumber = statusNumber;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    public void setXincoCoreUserId(int xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
    }

    public int getXincoCoreGroupId() {
        return xincoCoreGroupId;
    }

    public void setXincoCoreGroupId(int xincoCoreGroupId) {
        this.xincoCoreGroupId = xincoCoreGroupId;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof XincoCoreUserHasXincoCoreGroupT)) {
            return false;
        }
        XincoCoreUserHasXincoCoreGroupT other = (XincoCoreUserHasXincoCoreGroupT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupT[recordId=" + recordId + "]";
    }
}
