package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLanguageJpaController;
import jakarta.persistence.EntityManager;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.hibernate.envers.AuditReaderFactory;

/**
 * Verifies that Hibernate Envers auditing is active: persisting a XincoCoreLanguage produces at
 * least one revision entry queryable via AuditReader.
 */
public class XincoEnversAuditTest extends AbstractXincoDataBaseTestCase {

  public XincoEnversAuditTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoEnversAuditTest.class);
  }

  public void testLanguageAuditRevisionCreated() throws Exception {
    XincoCoreLanguageJpaController ctrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreLanguage lang = new XincoCoreLanguage();
    lang.setDesignation("EnversTestLanguage");
    lang.setSign("et");
    XincoRevisionListener.MOD_REASON.set("envers.test.create");
    XincoRevisionListener.MODIFIER_ID.set(1);
    try {
      ctrl.create(lang);
    } finally {
      XincoRevisionListener.MOD_REASON.remove();
      XincoRevisionListener.MODIFIER_ID.remove();
    }
    int createdId = lang.getId();
    assertTrue("created id should be > 0", createdId > 0);

    try (EntityManager em = getEntityManagerFactory().createEntityManager()) {
      org.hibernate.envers.AuditReader reader = AuditReaderFactory.get(em);
      List<Number> revisions = reader.getRevisions(XincoCoreLanguage.class, createdId);
      assertNotNull("revisions should not be null", revisions);
      assertFalse("at least one revision should exist", revisions.isEmpty());
    }

    ctrl.destroy(createdId);
  }
}
