package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers create() / edit() / destroy() catch blocks in all 14 audit-trail (T) JPA controllers by:
 * (a) re-persisting an entity with its already-assigned manual PK → PK violation →
 * PreexistingEntityException, (b) calling edit(null) → em.merge(null) → IllegalArgumentException
 * with non-null message → catch → rethrow, and (c) calling destroy(99999) → EntityNotFoundException
 * → NonexistentEntityException.
 */
public class ControllerAuditTrailCatchTest extends AbstractXincoDataBaseTestCase {

  public ControllerAuditTrailCatchTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerAuditTrailCatchTest.class);
  }

  // =========================================================================
  // XincoCoreGroupT
  // =========================================================================

  public void testXincoCoreGroupT_createDuplicate() throws Exception {
    XincoCoreGroupTJpaController ctrl = new XincoCoreGroupTJpaController(getEntityManagerFactory());
    XincoCoreGroupT e = new XincoCoreGroupT();
    e.setRecordId(100001);
    e.setId(1);
    e.setDesignation("AuditGroup");
    e.setStatusNumber(1);
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100001);
  }

  public void testXincoCoreGroupT_editNull() {
    XincoCoreGroupTJpaController ctrl = new XincoCoreGroupTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected: em.merge(null) → non-null msg → catch → rethrow
    }
  }

  public void testXincoCoreGroupT_destroyNonExistent() {
    XincoCoreGroupTJpaController ctrl = new XincoCoreGroupTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreNodeT
  // =========================================================================

  public void testXincoCoreNodeT_createDuplicate() throws Exception {
    XincoCoreNodeTJpaController ctrl = new XincoCoreNodeTJpaController(getEntityManagerFactory());
    XincoCoreNodeT e = new XincoCoreNodeT();
    e.setRecordId(100002);
    e.setId(1);
    e.setXincoCoreNodeId(1);
    e.setXincoCoreLanguageId(1);
    e.setDesignation("AuditNode");
    e.setStatusNumber(1);
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100002);
  }

  public void testXincoCoreNodeT_editNull() {
    XincoCoreNodeTJpaController ctrl = new XincoCoreNodeTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoCoreNodeT_destroyNonExistent() {
    XincoCoreNodeTJpaController ctrl = new XincoCoreNodeTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreLanguageT
  // =========================================================================

  public void testXincoCoreLanguageT_createDuplicate() throws Exception {
    XincoCoreLanguageTJpaController ctrl =
        new XincoCoreLanguageTJpaController(getEntityManagerFactory());
    XincoCoreLanguageT e = new XincoCoreLanguageT();
    e.setRecordId(100003);
    e.setId(1);
    e.setSign("en");
    e.setDesignation("AuditLang");
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100003);
  }

  public void testXincoCoreLanguageT_editNull() {
    XincoCoreLanguageTJpaController ctrl =
        new XincoCoreLanguageTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoCoreLanguageT_destroyNonExistent() {
    XincoCoreLanguageTJpaController ctrl =
        new XincoCoreLanguageTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreDataTypeT
  // =========================================================================

  public void testXincoCoreDataTypeT_createDuplicate() throws Exception {
    XincoCoreDataTypeTJpaController ctrl =
        new XincoCoreDataTypeTJpaController(getEntityManagerFactory());
    XincoCoreDataTypeT e = new XincoCoreDataTypeT();
    e.setRecordId(100004);
    e.setId(1);
    e.setDesignation("AuditDT");
    e.setDescription("audit");
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100004);
  }

  public void testXincoCoreDataTypeT_editNull() {
    XincoCoreDataTypeTJpaController ctrl =
        new XincoCoreDataTypeTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoCoreDataTypeT_destroyNonExistent() {
    XincoCoreDataTypeTJpaController ctrl =
        new XincoCoreDataTypeTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreDataT
  // =========================================================================

  public void testXincoCoreDataT_createDuplicate() throws Exception {
    XincoCoreDataTJpaController ctrl = new XincoCoreDataTJpaController(getEntityManagerFactory());
    XincoCoreDataT e = new XincoCoreDataT();
    e.setRecordId(100005);
    e.setId(1);
    e.setXincoCoreNodeId(1);
    e.setXincoCoreLanguageId(1);
    e.setXincoCoreDataTypeId(1);
    e.setDesignation("AuditData");
    e.setStatusNumber(1);
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100005);
  }

  public void testXincoCoreDataT_editNull() {
    XincoCoreDataTJpaController ctrl = new XincoCoreDataTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoCoreDataT_destroyNonExistent() {
    XincoCoreDataTJpaController ctrl = new XincoCoreDataTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreAceT
  // =========================================================================

  public void testXincoCoreAceT_createDuplicate() throws Exception {
    XincoCoreAceTJpaController ctrl = new XincoCoreAceTJpaController(getEntityManagerFactory());
    XincoCoreAceT e = new XincoCoreAceT();
    e.setRecordId(100006);
    e.setId(1);
    e.setXincoCoreNodeId(1);
    e.setReadPermission(true);
    e.setWritePermission(false);
    e.setExecutePermission(false);
    e.setAdminPermission(false);
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100006);
  }

  public void testXincoCoreAceT_editNull() {
    XincoCoreAceTJpaController ctrl = new XincoCoreAceTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoCoreAceT_destroyNonExistent() {
    XincoCoreAceTJpaController ctrl = new XincoCoreAceTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoAddAttributeT
  // =========================================================================

  public void testXincoAddAttributeT_createDuplicate() throws Exception {
    XincoAddAttributeTJpaController ctrl =
        new XincoAddAttributeTJpaController(getEntityManagerFactory());
    XincoAddAttributeT e = new XincoAddAttributeT();
    e.setRecordId(100007);
    e.setXincoCoreDataId(1);
    e.setAttributeId(1);
    e.setAttribInt(0);
    e.setAttribUnsignedint(0L);
    e.setAttribDouble(0.0);
    e.setAttribVarchar("audit");
    e.setAttribText("audit");
    e.setAttribDatetime(new Date());
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100007);
  }

  public void testXincoAddAttributeT_editNull() {
    XincoAddAttributeTJpaController ctrl =
        new XincoAddAttributeTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoAddAttributeT_destroyNonExistent() {
    XincoAddAttributeTJpaController ctrl =
        new XincoAddAttributeTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreDataHasDependencyT
  // =========================================================================

  public void testXincoCoreDataHasDependencyT_createDuplicate() throws Exception {
    XincoCoreDataHasDependencyTJpaController ctrl =
        new XincoCoreDataHasDependencyTJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyT e = new XincoCoreDataHasDependencyT();
    e.setRecordId(100008);
    e.setXincoCoreDataParentId(1);
    e.setXincoCoreDataChildrenId(2);
    e.setDependencyTypeId(1);
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100008);
  }

  public void testXincoCoreDataHasDependencyT_editNull() {
    XincoCoreDataHasDependencyTJpaController ctrl =
        new XincoCoreDataHasDependencyTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoCoreDataHasDependencyT_destroyNonExistent() {
    XincoCoreDataHasDependencyTJpaController ctrl =
        new XincoCoreDataHasDependencyTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreUserT
  // =========================================================================

  public void testXincoCoreUserT_createDuplicate() throws Exception {
    XincoCoreUserTJpaController ctrl = new XincoCoreUserTJpaController(getEntityManagerFactory());
    XincoCoreUserT e = new XincoCoreUserT();
    e.setRecordId(100009);
    e.setId(1);
    e.setUsername("audituser");
    e.setUserpassword("pass");
    e.setLastName("Last");
    e.setFirstName("First");
    e.setEmail("audit@test.com");
    e.setStatusNumber(1);
    e.setAttempts(0);
    e.setLastModified(new Date());
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100009);
  }

  public void testXincoCoreUserT_editNull() {
    XincoCoreUserTJpaController ctrl = new XincoCoreUserTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoCoreUserT_destroyNonExistent() {
    XincoCoreUserTJpaController ctrl = new XincoCoreUserTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreUserHasXincoCoreGroupT
  // =========================================================================

  public void testXincoCoreUserHasXincoCoreGroupT_createDuplicate() throws Exception {
    XincoCoreUserHasXincoCoreGroupTJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupTJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupT e = new XincoCoreUserHasXincoCoreGroupT();
    e.setRecordId(100010);
    e.setXincoCoreUserId(1);
    e.setXincoCoreGroupId(1);
    e.setStatusNumber(1);
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100010);
  }

  public void testXincoCoreUserHasXincoCoreGroupT_editNull() {
    XincoCoreUserHasXincoCoreGroupTJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoCoreUserHasXincoCoreGroupT_destroyNonExistent() {
    XincoCoreUserHasXincoCoreGroupTJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoDependencyTypeT
  // =========================================================================

  public void testXincoDependencyTypeT_createDuplicate() throws Exception {
    XincoDependencyTypeTJpaController ctrl =
        new XincoDependencyTypeTJpaController(getEntityManagerFactory());
    XincoDependencyTypeT e = new XincoDependencyTypeT();
    e.setRecordId(100011);
    e.setId(1);
    e.setXincoDependencyBehaviorId(1);
    e.setDesignation("AuditDepType");
    e.setDescription("audit");
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100011);
  }

  public void testXincoDependencyTypeT_editNull() {
    XincoDependencyTypeTJpaController ctrl =
        new XincoDependencyTypeTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoDependencyTypeT_destroyNonExistent() {
    XincoDependencyTypeTJpaController ctrl =
        new XincoDependencyTypeTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoDependencyBehaviorT
  // =========================================================================

  public void testXincoDependencyBehaviorT_createDuplicate() throws Exception {
    XincoDependencyBehaviorTJpaController ctrl =
        new XincoDependencyBehaviorTJpaController(getEntityManagerFactory());
    XincoDependencyBehaviorT e = new XincoDependencyBehaviorT();
    e.setRecordId(100012);
    e.setId(1);
    e.setDesignation("AuditDepBehav");
    e.setDescription("audit");
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100012);
  }

  public void testXincoDependencyBehaviorT_editNull() {
    XincoDependencyBehaviorTJpaController ctrl =
        new XincoDependencyBehaviorTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoDependencyBehaviorT_destroyNonExistent() {
    XincoDependencyBehaviorTJpaController ctrl =
        new XincoDependencyBehaviorTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoSettingT
  // =========================================================================

  public void testXincoSettingT_createDuplicate() throws Exception {
    XincoSettingTJpaController ctrl = new XincoSettingTJpaController(getEntityManagerFactory());
    XincoSettingT e = new XincoSettingT();
    e.setRecordId(100013);
    e.setId(1);
    e.setDescription("audit");
    e.setLongValue(0L);
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100013);
  }

  public void testXincoSettingT_editNull() {
    XincoSettingTJpaController ctrl = new XincoSettingTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoSettingT_destroyNonExistent() {
    XincoSettingTJpaController ctrl = new XincoSettingTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreDataTypeAttributeT
  // =========================================================================

  public void testXincoCoreDataTypeAttributeT_createDuplicate() throws Exception {
    XincoCoreDataTypeAttributeTJpaController ctrl =
        new XincoCoreDataTypeAttributeTJpaController(getEntityManagerFactory());
    XincoCoreDataTypeAttributeT e = new XincoCoreDataTypeAttributeT();
    e.setRecordId(100014);
    e.setXincoCoreDataTypeId(1);
    e.setAttributeId(1);
    e.setDesignation("AuditDTA");
    e.setDataType("String");
    e.setAttrSize(100);
    ctrl.create(e);
    try {
      ctrl.create(e);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException ex) {
      // expected
    }
    ctrl.destroy(100014);
  }

  public void testXincoCoreDataTypeAttributeT_editNull() {
    XincoCoreDataTypeAttributeTJpaController ctrl =
        new XincoCoreDataTypeAttributeTJpaController(getEntityManagerFactory());
    try {
      ctrl.edit(null);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }

  public void testXincoCoreDataTypeAttributeT_destroyNonExistent() {
    XincoCoreDataTypeAttributeTJpaController ctrl =
        new XincoCoreDataTypeAttributeTJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(99999);
      fail("Expected exception");
    } catch (Exception e) {
      // expected
    }
  }
}
