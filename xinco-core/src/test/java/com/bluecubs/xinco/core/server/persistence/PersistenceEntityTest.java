package com.bluecubs.xinco.core.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.Test;

/** Covers getters/setters on JPA entity POJOs — no database required. */
class PersistenceEntityTest {

  @Test
  void xincoCoreGroup_settersAndGetters() {
    XincoCoreGroup g = new XincoCoreGroup();
    g.setId(1);
    g.setDesignation("test.group");
    g.setStatusNumber(1);
    g.setXincoCoreAceList(new ArrayList<>());
    g.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    assertThat(g.getId()).isEqualTo(1);
    assertThat(g.getDesignation()).isEqualTo("test.group");
    assertThat(g.getStatusNumber()).isEqualTo(1);
    assertThat(g.getXincoCoreAceList()).isEmpty();
    assertThat(g.toString()).isNotNull();
    assertThat(g.hashCode()).isNotZero();
    assertThat(g.equals(g)).isTrue();
    assertThat(g == null).isFalse();
  }

  @Test
  void xincoCoreLanguage_settersAndGetters() {
    XincoCoreLanguage l = new XincoCoreLanguage();
    l.setId(2);
    l.setSign("en");
    l.setDesignation("language.en");
    l.setXincoCoreDataList(new ArrayList<>());
    l.setXincoCoreNodeList(new ArrayList<>());
    assertThat(l.getId()).isEqualTo(2);
    assertThat(l.getSign()).isEqualTo("en");
    assertThat(l.getDesignation()).isEqualTo("language.en");
    assertThat(l.toString()).isNotNull();
    assertThat(l.hashCode()).isNotZero();
    assertThat(l.equals(l)).isTrue();
  }

  @Test
  void xincoCoreDataType_settersAndGetters() {
    XincoCoreDataType dt = new XincoCoreDataType();
    dt.setId(1);
    dt.setDesignation("general.data.type.file");
    dt.setDescription("File type");
    dt.setXincoCoreDataList(new ArrayList<>());
    dt.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    assertThat(dt.getId()).isEqualTo(1);
    assertThat(dt.getDesignation()).isEqualTo("general.data.type.file");
    assertThat(dt.toString()).isNotNull();
    assertThat(dt.hashCode()).isNotZero();
    assertThat(dt.equals(dt)).isTrue();
  }

  @Test
  void xincoCoreNode_settersAndGetters() {
    XincoCoreNode n = new XincoCoreNode();
    n.setId(1);
    n.setDesignation("xincoRoot");
    n.setStatusNumber(1);
    n.setXincoCoreNodeList(new ArrayList<>());
    n.setXincoCoreAceList(new ArrayList<>());
    n.setXincoCoreDataList(new ArrayList<>());
    assertThat(n.getId()).isEqualTo(1);
    assertThat(n.getDesignation()).isEqualTo("xincoRoot");
    assertThat(n.toString()).isNotNull();
    assertThat(n.hashCode()).isNotZero();
    assertThat(n.equals(n)).isTrue();
  }

  @Test
  void xincoCoreUser_settersAndGetters() {
    XincoCoreUser u = new XincoCoreUser();
    u.setId(1);
    u.setUsername("admin");
    u.setUserpassword("hashed");
    u.setLastName("Admin");
    u.setFirstName("Xinco");
    u.setEmail("admin@xinco.org");
    u.setStatusNumber(1);
    u.setAttempts(0);
    u.setLastModified(new Date());
    u.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    u.setXincoCoreAceList(new ArrayList<>());
    u.setXincoCoreLogList(new ArrayList<>());
    u.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    assertThat(u.getId()).isEqualTo(1);
    assertThat(u.getUsername()).isEqualTo("admin");
    assertThat(u.getEmail()).isEqualTo("admin@xinco.org");
    assertThat(u.toString()).isNotNull();
    assertThat(u.hashCode()).isNotZero();
    assertThat(u.equals(u)).isTrue();
    assertThat(u == null).isFalse();
  }

  @Test
  void xincoCoreAce_settersAndGetters() {
    XincoCoreAce ace = new XincoCoreAce();
    ace.setId(1);
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(true);
    ace.setAdminPermission(false);
    assertThat(ace.getId()).isEqualTo(1);
    assertThat(ace.getReadPermission()).isTrue();
    assertThat(ace.getWritePermission()).isFalse();
    assertThat(ace.getExecutePermission()).isTrue();
    assertThat(ace.getAdminPermission()).isFalse();
    assertThat(ace.toString()).isNotNull();
    assertThat(ace.hashCode()).isNotZero();
    assertThat(ace.equals(ace)).isTrue();
  }

  @Test
  void xincoCoreLog_settersAndGetters() {
    XincoCoreLog log = new XincoCoreLog();
    log.setId(1);
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("test log");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("SNAPSHOT");
    assertThat(log.getId()).isEqualTo(1);
    assertThat(log.getOpCode()).isEqualTo(1);
    assertThat(log.getOpDescription()).isEqualTo("test log");
    assertThat(log.getVersionHigh()).isEqualTo(1);
    assertThat(log.getVersionPostfix()).isEqualTo("SNAPSHOT");
    assertThat(log.toString()).isNotNull();
    assertThat(log.hashCode()).isNotZero();
    assertThat(log.equals(log)).isTrue();
  }

  @Test
  void xincoCoreData_settersAndGetters() {
    XincoCoreData d = new XincoCoreData();
    d.setId(1);
    d.setDesignation("TestData");
    d.setStatusNumber(1);
    d.setXincoCoreLogList(new ArrayList<>());
    d.setXincoCoreAceList(new ArrayList<>());
    d.setXincoAddAttributeList(new ArrayList<>());
    d.setXincoCoreDataHasDependencyList(new ArrayList<>());
    d.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    assertThat(d.getId()).isEqualTo(1);
    assertThat(d.getDesignation()).isEqualTo("TestData");
    assertThat(d.toString()).isNotNull();
    assertThat(d.hashCode()).isNotZero();
    assertThat(d.equals(d)).isTrue();
  }

  @Test
  void xincoCoreDataHasDependency_pkSettersAndGetters() {
    XincoCoreDataHasDependencyPK pk = new XincoCoreDataHasDependencyPK(1, 2, 1);
    assertThat(pk.getXincoCoreDataParentId()).isEqualTo(1);
    assertThat(pk.getXincoCoreDataChildrenId()).isEqualTo(2);
    assertThat(pk.getDependencyTypeId()).isEqualTo(1);
    pk.setXincoCoreDataParentId(3);
    pk.setXincoCoreDataChildrenId(4);
    pk.setDependencyTypeId(2);
    assertThat(pk.getXincoCoreDataParentId()).isEqualTo(3);
    assertThat(pk.hashCode()).isNotZero();
    assertThat(pk.equals(pk)).isTrue();
    assertThat(pk == null).isFalse();
    assertThat(pk.toString()).isNotNull();
  }

  @Test
  void xincoSetting_settersAndGetters() {
    XincoSetting s = new XincoSetting();
    s.setId(1);
    s.setDescription("test.setting");
    s.setIntValue(42);
    s.setLongValue(100L);
    s.setStringValue("hello");
    s.setBoolValue(true);
    assertThat(s.getId()).isEqualTo(1);
    assertThat(s.getDescription()).isEqualTo("test.setting");
    assertThat(s.getIntValue()).isEqualTo(42);
    assertThat(s.getLongValue()).isEqualTo(100L);
    assertThat(s.getStringValue()).isEqualTo("hello");
    assertThat(s.getBoolValue()).isTrue();
    assertThat(s.toString()).isNotNull();
    assertThat(s.hashCode()).isNotZero();
    assertThat(s.equals(s)).isTrue();
  }

  @Test
  void xincoDependencyType_settersAndGetters() {
    XincoDependencyType dt = new XincoDependencyType();
    dt.setId(1);
    dt.setDescription("test.dep.type");
    dt.setXincoCoreDataHasDependencyList(new ArrayList<>());
    assertThat(dt.getId()).isEqualTo(1);
    assertThat(dt.getDescription()).isEqualTo("test.dep.type");
    assertThat(dt.toString()).isNotNull();
    assertThat(dt.hashCode()).isNotZero();
    assertThat(dt.equals(dt)).isTrue();
  }

  @Test
  void xincoAddAttribute_pkSettersAndGetters() {
    XincoAddAttributePK pk = new XincoAddAttributePK(1, 2);
    assertThat(pk.getXincoCoreDataId()).isEqualTo(1);
    assertThat(pk.getAttributeId()).isEqualTo(2);
    assertThat(pk.hashCode()).isNotZero();
    assertThat(pk.equals(pk)).isTrue();
    assertThat(pk == null).isFalse();
    assertThat(pk.toString()).isNotNull();

    XincoAddAttribute attr = new XincoAddAttribute();
    attr.setXincoAddAttributePK(pk);
    attr.setAttribInt(7);
    attr.setAttribUnsignedint(99L);
    attr.setAttribDouble(2.71);
    attr.setAttribVarchar("test");
    attr.setAttribText("text value");
    attr.setAttribDatetime(new Date());
    assertThat(attr.getAttribInt()).isEqualTo(7);
    assertThat(attr.getAttribVarchar()).isEqualTo("test");
    assertThat(attr.toString()).isNotNull();
    assertThat(attr.hashCode()).isNotZero();
    assertThat(attr.equals(attr)).isTrue();
  }

  @Test
  void xincoCoreUserHasXincoCoreGroupPK_settersAndGetters() {
    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(1, 2);
    assertThat(pk.getXincoCoreUserId()).isEqualTo(1);
    assertThat(pk.getXincoCoreGroupId()).isEqualTo(2);
    pk.setXincoCoreUserId(3);
    pk.setXincoCoreGroupId(4);
    assertThat(pk.getXincoCoreUserId()).isEqualTo(3);
    assertThat(pk.hashCode()).isNotZero();
    assertThat(pk.equals(pk)).isTrue();
    assertThat(pk == null).isFalse();
    assertThat(pk.toString()).isNotNull();
  }

  @Test
  void xincoCoreDataTypeAttributePK_settersAndGetters() {
    XincoCoreDataTypeAttributePK pk = new XincoCoreDataTypeAttributePK(1, 5);
    assertThat(pk.getXincoCoreDataTypeId()).isEqualTo(1);
    assertThat(pk.getAttributeId()).isEqualTo(5);
    pk.setXincoCoreDataTypeId(2);
    pk.setAttributeId(10);
    assertThat(pk.getXincoCoreDataTypeId()).isEqualTo(2);
    assertThat(pk.hashCode()).isNotZero();
    assertThat(pk.equals(pk)).isTrue();
    assertThat(pk.toString()).isNotNull();
  }

  @Test
  void xincoCoreDataTypeAttribute_settersAndGetters() {
    XincoCoreDataTypeAttribute attr = new XincoCoreDataTypeAttribute();
    attr.setXincoCoreDataTypeAttributePK(new XincoCoreDataTypeAttributePK(1, 1));
    attr.setDesignation("test.attr");
    attr.setDataType("varchar");
    attr.setAttrSize(255);
    assertThat(attr.getDesignation()).isEqualTo("test.attr");
    assertThat(attr.getDataType()).isEqualTo("varchar");
    assertThat(attr.getAttrSize()).isEqualTo(255);
    assertThat(attr.toString()).isNotNull();
    assertThat(attr.hashCode()).isNotZero();
    assertThat(attr.equals(attr)).isTrue();
  }
}
