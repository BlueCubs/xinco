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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_state_type", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
@NamedQueries({
    @NamedQuery(name = "XincoStateType.findAll", query = "SELECT x FROM XincoStateType x"),
    @NamedQuery(name = "XincoStateType.findById", query = "SELECT x FROM XincoStateType x WHERE x.id = :id"),
    @NamedQuery(name = "XincoStateType.findByName", query = "SELECT x FROM XincoStateType x WHERE x.name = :name"),
    @NamedQuery(name = "XincoStateType.findByDescription", query = "SELECT x FROM XincoStateType x WHERE x.description = :description")})
public class XincoStateType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "STATETYPEKEYGEN")
    @TableGenerator(name = "STATETYPEKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "user_link", initialValue = 1001, allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Column(name = "description", length = 45)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoStateTypeId", fetch = FetchType.LAZY)
    private List<XincoWorkflowState> xincoWorkflowStateList;

    public XincoStateType() {
    }

    public XincoStateType(Integer id) {
        this.id = id;
    }

    public XincoStateType(String name) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<XincoWorkflowState> getXincoWorkflowStateList() {
        return xincoWorkflowStateList;
    }

    public void setXincoWorkflowStateList(List<XincoWorkflowState> xincoWorkflowStateList) {
        this.xincoWorkflowStateList = xincoWorkflowStateList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof XincoStateType)) {
            return false;
        }
        XincoStateType other = (XincoStateType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.persistence.XincoStateType[id=" + id + "]";
    }
}
