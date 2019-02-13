/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.workflow.persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "user_link", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"system_id"})})
@NamedQueries({
    @NamedQuery(name = "UserLink.findAll", query = "SELECT u FROM UserLink u"),
    @NamedQuery(name = "UserLink.findById", query = "SELECT u FROM UserLink u WHERE u.id = :id"),
    @NamedQuery(name = "UserLink.findBySystemId", query = "SELECT u FROM UserLink u WHERE u.systemId = :systemId")})
public class UserLink implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "USERKEYGEN")
    @TableGenerator(name = "USERKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "user_link", initialValue = 1001, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "system_id", length = 45)
    private String systemId;
    @ManyToMany(mappedBy = "userLinkList", fetch = FetchType.LAZY)
    private List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateList;
    @ManyToMany(mappedBy = "userLinkList", fetch = FetchType.LAZY)
    private List<XincoWorkflowState> xincoWorkflowStateList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userLink", fetch = FetchType.LAZY)
    private List<XincoWorkflow> xincoWorkflowList;

    public UserLink() {
    }

    public UserLink(String id) {
        this.systemId = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public List<XincoStateTransitionsToXincoState> getXincoStateTransitionsToXincoStateList() {
        return xincoStateTransitionsToXincoStateList;
    }

    public void setXincoStateTransitionsToXincoStateList(List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateList) {
        this.xincoStateTransitionsToXincoStateList = xincoStateTransitionsToXincoStateList;
    }

    public List<XincoWorkflowState> getXincoWorkflowStateList() {
        return xincoWorkflowStateList;
    }

    public void setXincoWorkflowStateList(List<XincoWorkflowState> xincoWorkflowStateList) {
        this.xincoWorkflowStateList = xincoWorkflowStateList;
    }

    public List<XincoWorkflow> getXincoWorkflowList() {
        return xincoWorkflowList;
    }

    public void setXincoWorkflowList(List<XincoWorkflow> xincoWorkflowList) {
        this.xincoWorkflowList = xincoWorkflowList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof UserLink)) {
            return false;
        }
        UserLink other = (UserLink) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.persistence.UserLink[id=" + id + "]";
    }
}
