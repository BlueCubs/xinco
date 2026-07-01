package com.bluecubs.xinco.core.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.Test;

/** Getter/setter coverage for the audit-trail T-variant entity POJOs. */
class PersistenceTEntityTest {

  @Test
  void xincoCoreGroupT_settersAndGetters() {
    XincoCoreGroupT e = new XincoCoreGroupT();
    e.setRecordId(1);
    e.setId(2);
    e.setDesignation("test.group.t");
    e.setStatusNumber(1);
    assertThat(e.getRecordId()).isEqualTo(1);
    assertThat(e.getId()).isEqualTo(2);
    assertThat(e.getDesignation()).isEqualTo("test.group.t");
    assertThat(e.getStatusNumber()).isEqualTo(1);
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
    assertThat(e == null).isFalse();
  }

  @Test
  void xincoCoreLanguageT_settersAndGetters() {
    XincoCoreLanguageT e = new XincoCoreLanguageT();
    e.setRecordId(1);
    e.setId(2);
    e.setSign("en");
    e.setDesignation("language.en");
    assertThat(e.getRecordId()).isEqualTo(1);
    assertThat(e.getSign()).isEqualTo("en");
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreDataTypeT_settersAndGetters() {
    XincoCoreDataTypeT e = new XincoCoreDataTypeT();
    e.setRecordId(1);
    e.setId(2);
    e.setDesignation("general.data.type.file");
    e.setDescription("desc");
    assertThat(e.getDesignation()).isEqualTo("general.data.type.file");
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreNodeT_settersAndGetters() {
    XincoCoreNodeT e = new XincoCoreNodeT();
    e.setRecordId(1);
    e.setId(2);
    e.setXincoCoreNodeId(1);
    e.setXincoCoreLanguageId(1);
    e.setDesignation("TestNode");
    e.setStatusNumber(1);
    assertThat(e.getDesignation()).isEqualTo("TestNode");
    assertThat(e.getXincoCoreNodeId()).isEqualTo(1);
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreAceT_settersAndGetters() {
    XincoCoreAceT e = new XincoCoreAceT();
    e.setRecordId(1);
    e.setId(2);
    e.setXincoCoreUserId(1);
    e.setXincoCoreGroupId(2);
    e.setXincoCoreNodeId(1);
    e.setXincoCoreDataId(1);
    e.setReadPermission(true);
    e.setWritePermission(false);
    e.setExecutePermission(true);
    e.setAdminPermission(false);
    assertThat(e.getReadPermission()).isTrue();
    assertThat(e.getWritePermission()).isFalse();
    assertThat(e.getXincoCoreUserId()).isEqualTo(1);
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoSettingT_settersAndGetters() {
    XincoSettingT e = new XincoSettingT();
    e.setRecordId(1);
    e.setId(2);
    e.setDescription("test.setting.t");
    e.setIntValue(42);
    e.setStringValue("hello");
    e.setBoolValue(true);
    e.setLongValue(100L);
    assertThat(e.getIntValue()).isEqualTo(42);
    assertThat(e.getStringValue()).isEqualTo("hello");
    assertThat(e.getBoolValue()).isTrue();
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreDataT_settersAndGetters() {
    XincoCoreDataT e = new XincoCoreDataT();
    e.setRecordId(1);
    e.setId(2);
    e.setXincoCoreNodeId(1);
    e.setXincoCoreLanguageId(1);
    e.setXincoCoreDataTypeId(1);
    e.setDesignation("TestDataT");
    e.setStatusNumber(1);
    assertThat(e.getDesignation()).isEqualTo("TestDataT");
    assertThat(e.getXincoCoreNodeId()).isEqualTo(1);
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoAddAttributeT_settersAndGetters() {
    XincoAddAttributeT e = new XincoAddAttributeT();
    e.setRecordId(1);
    e.setXincoCoreDataId(1);
    e.setAttributeId(1);
    e.setAttribInt(7);
    e.setAttribUnsignedint(99L);
    e.setAttribDouble(3.14);
    e.setAttribVarchar("test");
    e.setAttribText("text");
    e.setAttribDatetime(new Date());
    assertThat(e.getAttribInt()).isEqualTo(7);
    assertThat(e.getAttribVarchar()).isEqualTo("test");
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreUserHasXincoCoreGroupT_settersAndGetters() {
    XincoCoreUserHasXincoCoreGroupT e = new XincoCoreUserHasXincoCoreGroupT();
    e.setRecordId(1);
    e.setXincoCoreUserId(1);
    e.setXincoCoreGroupId(2);
    e.setStatusNumber(1);
    assertThat(e.getXincoCoreUserId()).isEqualTo(1);
    assertThat(e.getXincoCoreGroupId()).isEqualTo(2);
    assertThat(e.getStatusNumber()).isEqualTo(1);
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreDataHasDependencyT_settersAndGetters() {
    XincoCoreDataHasDependencyT e = new XincoCoreDataHasDependencyT();
    e.setRecordId(1);
    e.setXincoCoreDataParentId(1);
    e.setXincoCoreDataChildrenId(2);
    e.setDependencyTypeId(1);
    assertThat(e.getXincoCoreDataParentId()).isEqualTo(1);
    assertThat(e.getXincoCoreDataChildrenId()).isEqualTo(2);
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoDependencyBehaviorT_settersAndGetters() {
    XincoDependencyBehaviorT e = new XincoDependencyBehaviorT();
    e.setRecordId(1);
    e.setId(2);
    e.setDesignation("test.dep.behavior.t");
    e.setDescription("desc");
    assertThat(e.getDesignation()).isEqualTo("test.dep.behavior.t");
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoDependencyTypeT_settersAndGetters() {
    XincoDependencyTypeT e = new XincoDependencyTypeT();
    e.setRecordId(1);
    e.setId(2);
    e.setXincoDependencyBehaviorId(1);
    e.setDesignation("test.dep.type.t");
    e.setDescription("desc t");
    assertThat(e.getDesignation()).isEqualTo("test.dep.type.t");
    assertThat(e.getXincoDependencyBehaviorId()).isEqualTo(1);
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreUserT_settersAndGetters() {
    XincoCoreUserT e = new XincoCoreUserT();
    e.setRecordId(1);
    e.setId(2);
    e.setUsername("testuser");
    e.setUserpassword("hash");
    e.setLastName("User");
    e.setFirstName("Test");
    e.setEmail("test@example.com");
    e.setStatusNumber(1);
    e.setAttempts(0);
    e.setLastModified(new Date());
    assertThat(e.getUsername()).isEqualTo("testuser");
    assertThat(e.getEmail()).isEqualTo("test@example.com");
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreDataTypeAttributeT_settersAndGetters() {
    XincoCoreDataTypeAttributeT e = new XincoCoreDataTypeAttributeT();
    e.setRecordId(1);
    e.setXincoCoreDataTypeId(1);
    e.setAttributeId(1);
    e.setDesignation("test.attr.t");
    e.setDataType("varchar");
    e.setAttrSize(255);
    assertThat(e.getDesignation()).isEqualTo("test.attr.t");
    assertThat(e.getDataType()).isEqualTo("varchar");
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreUserModifiedRecord_settersAndGetters() {
    XincoCoreUserModifiedRecord e = new XincoCoreUserModifiedRecord();
    XincoCoreUserModifiedRecordPK pk = new XincoCoreUserModifiedRecordPK(1, 1);
    e.setXincoCoreUserModifiedRecordPK(pk);
    e.setModTime(new Date());
    e.setModReason("test reason");
    assertThat(e.getModReason()).isEqualTo("test reason");
    assertThat(e.getModTime()).isNotNull();
    assertThat(e.toString()).isNotNull();
    assertThat(e.hashCode()).isNotZero();
    assertThat(e.equals(e)).isTrue();
  }

  @Test
  void xincoCoreUserModifiedRecordPK_settersAndGetters() {
    XincoCoreUserModifiedRecordPK pk = new XincoCoreUserModifiedRecordPK(1, 2);
    assertThat(pk.getId()).isEqualTo(1);
    assertThat(pk.getRecordId()).isEqualTo(2);
    pk.setId(3);
    pk.setRecordId(4);
    assertThat(pk.getId()).isEqualTo(3);
    assertThat(pk.hashCode()).isNotZero();
    assertThat(pk.equals(pk)).isTrue();
    assertThat(pk == null).isFalse();
    assertThat(pk.toString()).isNotNull();
  }
}
