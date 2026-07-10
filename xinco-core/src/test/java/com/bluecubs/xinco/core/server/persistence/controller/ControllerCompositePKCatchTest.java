package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupPK;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers create() catch blocks (PK violation → PreexistingEntityException), edit() catch blocks
 * (em.find=null → NPE → rethrow), and destroy() orphan-check loop bodies for composite-PK and
 * orphan-checked JPA controllers.
 */
public class ControllerCompositePKCatchTest extends AbstractXincoDataBaseTestCase {

  public ControllerCompositePKCatchTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerCompositePKCatchTest.class);
  }

  // =========================================================================
  // XincoCoreUserHasXincoCoreGroupJpaController
  // =========================================================================

  /** Covers UHG create() catch — PK violation → PreexistingEntityException (~16 instr). */
  public void testUhg_createDuplicate() throws Exception {
    XincoCoreUserHasXincoCoreGroupJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreUser user = buildUser(userCtrl, "uhg.dup");
    XincoCoreGroup group = buildGroup(groupCtrl, "test.uhg.dup.grp");

    XincoCoreUserHasXincoCoreGroup uhg = buildUhg(ctrl, user, group);
    XincoCoreUserHasXincoCoreGroupPK pk = uhg.getXincoCoreUserHasXincoCoreGroupPK();

    try {
      ctrl.create(uhg);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException e) {
      // expected
    }

    ctrl.destroy(pk);
    getEntityManagerFactory().getCache().evictAll();
    userCtrl.destroy(user.getId());
    groupCtrl.destroy(group.getId());
  }

  /** Covers UHG edit() catch — em.find=null (destroyed) → NPE → rethrow (~11 instr). */
  public void testUhg_editCatch() throws Exception {
    XincoCoreUserHasXincoCoreGroupJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreUser user = buildUser(userCtrl, "uhg.edit");
    XincoCoreGroup group = buildGroup(groupCtrl, "test.uhg.edit.grp");
    XincoCoreUserHasXincoCoreGroup uhg = buildUhg(ctrl, user, group);
    XincoCoreUserHasXincoCoreGroupPK pk = uhg.getXincoCoreUserHasXincoCoreGroupPK();

    ctrl.destroy(pk);
    getEntityManagerFactory().getCache().evictAll();

    try {
      ctrl.edit(uhg);
      fail("Expected exception from edit on destroyed UHG");
    } catch (Exception e) {
      // expected: em.find returns null → NPE caught → rethrow
    }

    userCtrl.destroy(user.getId());
    groupCtrl.destroy(group.getId());
  }

  /** Covers UHG destroy() EntityNotFoundException catch (~9 instr). */
  public void testUhg_destroyNonExistent() {
    XincoCoreUserHasXincoCoreGroupJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupPK fakePK = new XincoCoreUserHasXincoCoreGroupPK();
    fakePK.setXincoCoreGroupId(99999);
    fakePK.setXincoCoreUserId(99999);
    try {
      ctrl.destroy(fakePK);
      fail("Expected exception for non-existent UHG pk");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreDataTypeAttributeJpaController
  // =========================================================================

  /** Covers DTA create() catch — PK violation → PreexistingEntityException (~16 instr). */
  public void testDta_createDuplicate() throws Exception {
    XincoCoreDataTypeAttributeJpaController ctrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());

    XincoCoreDataType dt = buildDataType(dtCtrl, "test.dup.dta.dt");
    XincoCoreDataTypeAttribute dta = new XincoCoreDataTypeAttribute();
    XincoCoreDataTypeAttributePK pk = new XincoCoreDataTypeAttributePK();
    pk.setAttributeId(901);
    dta.setXincoCoreDataTypeAttributePK(pk);
    dta.setXincoCoreDataType(dt);
    dta.setDesignation("DupDTA");
    dta.setDataType("String");
    dta.setAttrSize(10);
    ctrl.create(dta);
    XincoCoreDataTypeAttributePK createdPK = dta.getXincoCoreDataTypeAttributePK();

    try {
      ctrl.create(dta);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException e) {
      // expected
    }

    ctrl.destroy(createdPK);
    getEntityManagerFactory().getCache().evictAll();
    dtCtrl.destroy(dt.getId());
  }

  /** Covers DTA edit() catch — em.find=null → NPE → rethrow (~11 instr). */
  public void testDta_editCatch() throws Exception {
    XincoCoreDataTypeAttributeJpaController ctrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());

    XincoCoreDataType dt = buildDataType(dtCtrl, "test.edit.dta.dt");
    XincoCoreDataTypeAttribute dta = new XincoCoreDataTypeAttribute();
    XincoCoreDataTypeAttributePK pk = new XincoCoreDataTypeAttributePK();
    pk.setAttributeId(902);
    dta.setXincoCoreDataTypeAttributePK(pk);
    dta.setXincoCoreDataType(dt);
    dta.setDesignation("EditDTA");
    dta.setDataType("String");
    dta.setAttrSize(10);
    ctrl.create(dta);
    XincoCoreDataTypeAttributePK createdPK = dta.getXincoCoreDataTypeAttributePK();

    ctrl.destroy(createdPK);
    getEntityManagerFactory().getCache().evictAll();

    try {
      ctrl.edit(dta);
      fail("Expected exception from edit on destroyed DTA");
    } catch (Exception e) {
      // expected: em.find returns null → NPE caught → rethrow
    }

    dtCtrl.destroy(dt.getId());
  }

  // =========================================================================
  // XincoAddAttributeJpaController
  // =========================================================================

  /** Covers AddAttr create() catch — PK violation → PreexistingEntityException (~16 instr). */
  public void testAddAttr_createDuplicate() throws Exception {
    XincoAddAttributeJpaController ctrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.addattr.dup");
    XincoAddAttribute aa = buildAddAttr(ctrl, data, 901);
    XincoAddAttributePK aaPK = aa.getXincoAddAttributePK();

    try {
      ctrl.create(aa);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException e) {
      // expected
    }

    ctrl.destroy(aaPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(data.getId());
  }

  /** Covers AddAttr edit() catch — em.find=null → NPE → rethrow (~11 instr). */
  public void testAddAttr_editCatch() throws Exception {
    XincoAddAttributeJpaController ctrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.addattr.edit");
    XincoAddAttribute aa = buildAddAttr(ctrl, data, 902);
    XincoAddAttributePK aaPK = aa.getXincoAddAttributePK();

    ctrl.destroy(aaPK);
    getEntityManagerFactory().getCache().evictAll();

    try {
      ctrl.edit(aa);
      fail("Expected exception from edit on destroyed AddAttr");
    } catch (Exception e) {
      // expected: em.find returns null → NPE caught → rethrow
    }

    dataCtrl.destroy(data.getId());
  }

  /** Covers AddAttr destroy() EntityNotFoundException catch (~9 instr). */
  public void testAddAttr_destroyNonExistent() {
    XincoAddAttributeJpaController ctrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());
    XincoAddAttributePK fakePK = new XincoAddAttributePK();
    fakePK.setXincoCoreDataId(99999);
    fakePK.setAttributeId(99999);
    try {
      ctrl.destroy(fakePK);
      fail("Expected exception for non-existent AddAttr pk");
    } catch (Exception e) {
      // expected
    }
  }

  // =========================================================================
  // XincoCoreDataHasDependencyJpaController
  // =========================================================================

  /** Covers Dep create() catch — PK violation → PreexistingEntityException (~16 instr). */
  public void testDep_createDuplicate() throws Exception {
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController dtypeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData child = buildData(dataCtrl, "test.dep.dup.child");
    XincoCoreData parent = buildData(dataCtrl, "test.dep.dup.parent");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(child.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(parent.getId()));
    dep.setXincoDependencyType(dtypeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    try {
      depCtrl.create(dep);
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException e) {
      // expected
    }

    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(child.getId());
    dataCtrl.destroy(parent.getId());
  }

  /** Covers Dep edit() catch — em.find=null → NPE → rethrow (~11 instr). */
  public void testDep_editCatch() throws Exception {
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController dtypeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData child = buildData(dataCtrl, "test.dep.edit.child");
    XincoCoreData parent = buildData(dataCtrl, "test.dep.edit.parent");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(child.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(parent.getId()));
    dep.setXincoDependencyType(dtypeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();

    try {
      depCtrl.edit(dep);
      fail("Expected exception from edit on destroyed Dep");
    } catch (Exception e) {
      // expected: em.find returns null → NPE caught → rethrow
    }

    dataCtrl.destroy(child.getId());
    dataCtrl.destroy(parent.getId());
  }

  // =========================================================================
  // XincoDependencyBehaviorJpaController
  // =========================================================================

  /**
   * Covers DepBehavior edit() catch (IllegalOrphanException) — edit behavior(1) with empty
   * DependencyTypeList → orphan check loop fires → IllegalOrphanException caught (~11 instr).
   */
  public void testDepBehavior_editOrphanCatch() throws Exception {
    XincoDependencyBehaviorJpaController ctrl =
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory());
    XincoDependencyBehavior behavior = ctrl.findXincoDependencyBehavior(1);
    behavior.setXincoDependencyTypeList(new ArrayList<>());
    try {
      ctrl.edit(behavior);
      fail("Expected IllegalOrphanException from orphan check");
    } catch (Exception e) {
      // expected: orphan messages added → IllegalOrphanException thrown inside try → caught
    }
  }

  /**
   * Covers DepBehavior destroy() orphan-check loop body — behavior(1) has DependencyTypes → loop
   * fires → illegalOrphanMessages list populated → IllegalOrphanException thrown (~15 instr).
   */
  public void testDepBehavior_destroyOrphanLoop() {
    XincoDependencyBehaviorJpaController ctrl =
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory());
    try {
      ctrl.destroy(1);
      fail("Expected IllegalOrphanException for behavior with dependent types");
    } catch (Exception e) {
      // expected: seed behavior(1) has DependencyTypes → orphan loop fires → exception thrown
    }
  }

  // =========================================================================
  // Helpers
  // =========================================================================

  private XincoCoreUser buildUser(XincoCoreUserJpaController ctrl, String suffix) throws Exception {
    XincoCoreUser user = new XincoCoreUser();
    user.setUsername("usr6." + suffix);
    user.setUserpassword("pw6_" + suffix);
    user.setLastName("Last");
    user.setFirstName("First");
    user.setEmail(suffix + "@ex6.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new Date());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(user);
    return user;
  }

  private XincoCoreGroup buildGroup(XincoCoreGroupJpaController ctrl, String designation)
      throws Exception {
    XincoCoreGroup group = new XincoCoreGroup();
    group.setDesignation(designation);
    group.setStatusNumber(1);
    group.setXincoCoreAceList(new ArrayList<>());
    group.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(group);
    return group;
  }

  private XincoCoreUserHasXincoCoreGroup buildUhg(
      XincoCoreUserHasXincoCoreGroupJpaController ctrl, XincoCoreUser user, XincoCoreGroup group)
      throws Exception {
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUser(user);
    uhg.setXincoCoreGroup(group);
    uhg.setStatusNumber(1);
    ctrl.create(uhg);
    return uhg;
  }

  private XincoCoreDataType buildDataType(XincoCoreDataTypeJpaController ctrl, String designation)
      throws Exception {
    XincoCoreDataType dt = new XincoCoreDataType();
    dt.setDesignation(designation);
    dt.setDescription("test");
    dt.setXincoCoreDataList(new ArrayList<>());
    dt.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    ctrl.create(dt);
    return dt;
  }

  private XincoCoreData buildData(XincoCoreDataJpaController dataCtrl, String designation)
      throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreData data = new XincoCoreData();
    data.setDesignation(designation);
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(data);
    return data;
  }

  private XincoAddAttribute buildAddAttr(
      XincoAddAttributeJpaController ctrl, XincoCoreData data, int attributeId) throws Exception {
    XincoAddAttribute aa = new XincoAddAttribute();
    XincoAddAttributePK pk = new XincoAddAttributePK();
    pk.setAttributeId(attributeId);
    aa.setXincoAddAttributePK(pk);
    aa.setXincoCoreData(data);
    aa.setAttribVarchar("test");
    aa.setAttribInt(0);
    aa.setAttribUnsignedint(0L);
    aa.setAttribDouble(0.0);
    ctrl.create(aa);
    return aa;
  }
}
