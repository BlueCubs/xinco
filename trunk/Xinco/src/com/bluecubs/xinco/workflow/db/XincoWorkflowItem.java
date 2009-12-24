/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_workflow_item")
@NamedQueries({
    @NamedQuery(name = "XincoWorkflowItem.findAll", query = "SELECT x FROM XincoWorkflowItem x"),
    @NamedQuery(name = "XincoWorkflowItem.findById", query = "SELECT x FROM XincoWorkflowItem x WHERE x.id = :id"),
    @NamedQuery(name = "XincoWorkflowItem.findByCreationDate", query = "SELECT x FROM XincoWorkflowItem x WHERE x.creationDate = :creationDate"),
    @NamedQuery(name = "XincoWorkflowItem.findByCompletionDate", query = "SELECT x FROM XincoWorkflowItem x WHERE x.completionDate = :completionDate")})
public class XincoWorkflowItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "completion_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoWorkflowItem", fetch = FetchType.LAZY)
    private List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateList;

    public XincoWorkflowItem() {
    }

    public XincoWorkflowItem(Integer id) {
        this.id = id;
    }

    public XincoWorkflowItem(Integer id, Date creationDate) {
        this.id = id;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public List<XincoWorkItemHasXincoState> getXincoWorkItemHasXincoStateList() {
        return xincoWorkItemHasXincoStateList;
    }

    public void setXincoWorkItemHasXincoStateList(List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateList) {
        this.xincoWorkItemHasXincoStateList = xincoWorkItemHasXincoStateList;
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
        if (!(object instanceof XincoWorkflowItem)) {
            return false;
        }
        XincoWorkflowItem other = (XincoWorkflowItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.db.XincoWorkflowItem[id=" + id + "]";
    }

}
