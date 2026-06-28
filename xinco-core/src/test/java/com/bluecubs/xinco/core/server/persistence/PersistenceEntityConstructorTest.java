package com.bluecubs.xinco.core.server.persistence;

import static org.junit.Assert.*;

import java.util.Date;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;

/** Tests for parameterized constructors that were previously at 0% coverage. */
public class PersistenceEntityConstructorTest {

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(PersistenceEntityConstructorTest.class);
  }

  @Test
  public void testXincoCoreLog_fullConstructor() {
    XincoCoreLog log = new XincoCoreLog(42, 1, new Date(), "op description");
    assertEquals(42, (int) log.getId());
    assertEquals(1, log.getOpCode());
    assertEquals("op description", log.getOpDescription());
  }

  @Test
  public void testXincoCoreLog_idConstructor() {
    XincoCoreLog log = new XincoCoreLog(99);
    assertEquals(99, (int) log.getId());
  }

  @Test
  public void testXincoCoreDataTypeAttribute_pkConstructor() {
    XincoCoreDataTypeAttributePK pk = new XincoCoreDataTypeAttributePK(2, 5);
    XincoCoreDataTypeAttribute attr = new XincoCoreDataTypeAttribute(pk);
    assertEquals(pk, attr.getXincoCoreDataTypeAttributePK());
  }

  @Test
  public void testXincoCoreDataTypeAttribute_intConstructor() {
    XincoCoreDataTypeAttribute attr = new XincoCoreDataTypeAttribute(2, 6);
    assertEquals(2, attr.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
    assertEquals(6, attr.getXincoCoreDataTypeAttributePK().getAttributeId());
  }

  @Test
  public void testXincoCoreDataHasDependency_hashCode() {
    XincoCoreDataHasDependency dep1 = new XincoCoreDataHasDependency();
    XincoCoreDataHasDependency dep2 = new XincoCoreDataHasDependency();
    dep1.setXincoCoreDataHasDependencyPK(new XincoCoreDataHasDependencyPK(1, 2, 1));
    dep2.setXincoCoreDataHasDependencyPK(new XincoCoreDataHasDependencyPK(1, 2, 1));
    assertEquals(dep1.hashCode(), dep2.hashCode());
    assertEquals(dep1, dep2);
    assertFalse(dep1.equals(null));
    assertFalse(dep1.equals("other"));
    assertNotNull(dep1.toString());
  }

  @Test
  public void testXincoCoreUserHasXincoCoreGroup_pkConstructor() {
    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(1, 2);
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup(pk);
    assertEquals(pk, uhg.getXincoCoreUserHasXincoCoreGroupPK());
  }

  @Test
  public void testXincoCoreUserHasXincoCoreGroup_statusConstructor() {
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup(1, 2);
    assertNotNull(uhg.getXincoCoreUserHasXincoCoreGroupPK());
    assertEquals(1, uhg.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId());
  }

  @Test
  public void testXincoCoreUserHasXincoCoreGroup_hashCode() {
    XincoCoreUserHasXincoCoreGroup uhg1 = new XincoCoreUserHasXincoCoreGroup(1, 2);
    XincoCoreUserHasXincoCoreGroup uhg2 = new XincoCoreUserHasXincoCoreGroup(1, 2);
    assertEquals(uhg1.hashCode(), uhg2.hashCode());
    assertEquals(uhg1, uhg2);
    assertFalse(uhg1.equals(null));
    assertFalse(uhg1.equals("other"));
    assertNotNull(uhg1.toString());
  }

  @Test
  public void testXincoDependencyTypeT_recordIdConstructor() {
    XincoDependencyTypeT dt = new XincoDependencyTypeT(55);
    assertEquals(55, (int) dt.getRecordId());
  }

  @Test
  public void testXincoDependencyBehaviorT_recordIdConstructor() {
    XincoDependencyBehaviorT bt = new XincoDependencyBehaviorT(66);
    assertEquals(66, (int) bt.getRecordId());
  }

  @Test
  public void testXincoDependencyBehaviorT_fullConstructor() {
    XincoDependencyBehaviorT bt = new XincoDependencyBehaviorT(77, 100, "TestBehavior");
    assertEquals(77, (int) bt.getRecordId());
    assertEquals(100, (int) bt.getId());
    assertEquals("TestBehavior", bt.getDesignation());
  }

  @Test
  public void testXincoCoreNode_idConstructor() {
    XincoCoreNode node = new XincoCoreNode(99);
    assertEquals(99, (int) node.getId());
  }

  @Test
  public void testXincoCoreLanguage_idConstructor() {
    XincoCoreLanguage lang = new XincoCoreLanguage(5);
    assertEquals(5, (int) lang.getId());
  }

  @Test
  public void testXincoCoreGroup_idConstructor() {
    XincoCoreGroup group = new XincoCoreGroup(3);
    assertEquals(3, (int) group.getId());
  }

  @Test
  public void testXincoCoreUserModifiedRecord_pkConstructor() {
    XincoCoreUserModifiedRecordPK pk = new XincoCoreUserModifiedRecordPK(1, 100);
    XincoCoreUserModifiedRecord record = new XincoCoreUserModifiedRecord(pk);
    assertEquals(pk, record.getXincoCoreUserModifiedRecordPK());
  }

  @Test
  public void testXincoCoreUserHasXincoCoreGroupT_recordIdConstructor() {
    XincoCoreUserHasXincoCoreGroupT t = new XincoCoreUserHasXincoCoreGroupT(88);
    assertEquals(88, (int) t.getRecordId());
  }

  @Test
  public void testXincoCoreDataHasDependencyT_recordIdConstructor() {
    XincoCoreDataHasDependencyT t = new XincoCoreDataHasDependencyT(99);
    assertEquals(99, (int) t.getRecordId());
  }

  @Test
  public void testXincoDependencyBehavior_constructors() {
    XincoDependencyBehavior b1 = new XincoDependencyBehavior(10);
    assertEquals(10, (int) b1.getId());

    XincoDependencyBehavior b2 = new XincoDependencyBehavior(11, "TestBehavior2");
    assertEquals(11, (int) b2.getId());
    assertEquals("TestBehavior2", b2.getDesignation());

    assertEquals(b1.hashCode(), b1.hashCode());
    assertFalse(b1.equals(b2));
    assertFalse(b1.equals(null));
    assertFalse(b1.equals("string"));
    assertNotNull(b1.toString());
  }
}
