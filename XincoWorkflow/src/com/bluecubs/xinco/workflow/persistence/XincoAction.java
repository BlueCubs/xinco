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
@Table(name = "xinco_action", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
@NamedQueries({
    @NamedQuery(name = "XincoAction.findAll", query = "SELECT x FROM XincoAction x"),
    @NamedQuery(name = "XincoAction.findById", query = "SELECT x FROM XincoAction x WHERE x.id = :id"),
    @NamedQuery(name = "XincoAction.findByName", query = "SELECT x FROM XincoAction x WHERE x.name = :name"),
    @NamedQuery(name = "XincoAction.findByImplementationClass", query = "SELECT x FROM XincoAction x WHERE x.implementationClass = :implementationClass")})
public class XincoAction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "ACTIONKEYGEN")
    @TableGenerator(name = "ACTIONKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_action", initialValue = 1001, allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Column(name = "implementation_class", length = 45)
    private String implementationClass;
    @ManyToMany(mappedBy = "xincoActionList", fetch = FetchType.LAZY)
    private List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateList;
    @ManyToMany(mappedBy = "xincoActionList", fetch = FetchType.LAZY)
    private List<XincoWorkflowState> xincoWorkflowStateList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoAction", fetch = FetchType.LAZY)
    private List<XincoParameter> xincoParameterList;

    public XincoAction() {
    }

    public XincoAction(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImplementationClass() {
        return implementationClass;
    }

    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
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

    public List<XincoParameter> getXincoParameterList() {
        return xincoParameterList;
    }

    public void setXincoParameterList(List<XincoParameter> xincoParameterList) {
        this.xincoParameterList = xincoParameterList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof XincoAction)) {
            return false;
        }
        XincoAction other = (XincoAction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.persistence.XincoAction[id=" + id + "]";
    }

}
