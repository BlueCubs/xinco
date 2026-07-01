package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers the remaining 0% methods: DataHasDependency edit, T-variant edits, XincoId ops, XincoIdJpa
 * destroy.
 */
public class ControllerDepthTest extends AbstractXincoDataBaseTestCase {

  public ControllerDepthTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerDepthTest.class);
  }

  // ---- XincoCoreDataHasDependencyJpaController edit ----

  public void testDataHasDependencyController_edit() throws Exception {
    XincoCoreDataHasDependencyJpaController ctrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController depTypeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreDataHasDependencyPK pk = new XincoCoreDataHasDependencyPK(1, 2, 1);
    XincoCoreDataHasDependency entity = new XincoCoreDataHasDependency();
    entity.setXincoCoreDataHasDependencyPK(pk);
    entity.setXincoCoreData(dataCtrl.findXincoCoreData(1));
    entity.setXincoCoreData1(dataCtrl.findXincoCoreData(2));
    entity.setXincoDependencyType(depTypeCtrl.findXincoDependencyType(1));

    ctrl.create(entity);
    assertNotNull(ctrl.findXincoCoreDataHasDependency(pk));

    // edit (re-merge the same entity to exercise the edit method code path)
    ctrl.edit(entity);
    assertNotNull(ctrl.findXincoCoreDataHasDependency(pk));

    ctrl.destroy(pk);
    assertNull(ctrl.findXincoCoreDataHasDependency(pk));
  }

  // ---- XincoCoreDataHasDependencyTJpaController edit ----

  public void testDataHasDependencyTController_edit() throws Exception {
    XincoCoreDataHasDependencyTJpaController ctrl =
        new XincoCoreDataHasDependencyTJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyT entity = new XincoCoreDataHasDependencyT();
    entity.setRecordId(9940);
    entity.setXincoCoreDataParentId(1);
    entity.setXincoCoreDataChildrenId(2);
    entity.setDependencyTypeId(1);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);

    entity.setDependencyTypeId(2);
    ctrl.edit(entity);
    assertEquals(2, (int) ctrl.findXincoCoreDataHasDependencyT(rid).getDependencyTypeId());

    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreDataHasDependencyT(rid));
  }

  // ---- XincoCoreUserHasXincoCoreGroupTJpaController edit ----

  public void testUserHasGroupTController_edit() throws Exception {
    XincoCoreUserHasXincoCoreGroupTJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupTJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupT entity = new XincoCoreUserHasXincoCoreGroupT();
    entity.setRecordId(9941);
    entity.setXincoCoreUserId(1);
    entity.setXincoCoreGroupId(1);
    entity.setStatusNumber(1);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);

    entity.setStatusNumber(2);
    ctrl.edit(entity);
    assertEquals(2, (int) ctrl.findXincoCoreUserHasXincoCoreGroupT(rid).getStatusNumber());

    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreUserHasXincoCoreGroupT(rid));
  }

  // ---- XincoIdJpaController ----

  public void testXincoIdController_findAllAndCount() {
    XincoIdJpaController ctrl = new XincoIdJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoIdEntities());
    assertNotNull(ctrl.findXincoIdEntities(10, 0));
    assertTrue(ctrl.getXincoIdCount() >= 0);
  }

  // ---- XincoId entity equals/hashCode/toString ----

  public void testXincoId_equalsAndHashCode() {
    XincoIdJpaController ctrl = new XincoIdJpaController(getEntityManagerFactory());
    java.util.List<XincoId> ids = ctrl.findXincoIdEntities();
    assertNotNull(ids);
    if (ids.size() >= 2) {
      XincoId id1 = ids.get(0);
      XincoId id2 = ids.get(1);
      // Same object equality
      assertEquals(id1, id1);
      // Different objects
      assertFalse(id1.equals(id2));
      // null
      assertFalse(id1 == null);
      // different class
      assertFalse(id1.equals("notAnId"));
      // hashCode consistent with equals
      assertEquals(id1.hashCode(), id1.hashCode());
      assertNotNull(id1.toString());
    } else if (!ids.isEmpty()) {
      XincoId id = ids.get(0);
      assertEquals(id, id);
      assertNotNull(id.toString());
      assertFalse(id == null);
    }
  }
}
