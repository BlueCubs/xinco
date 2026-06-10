package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoCoreLanguageControllerTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreLanguageControllerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreLanguageControllerTest.class);
  }

  private XincoCoreLanguageJpaController controller() throws XincoException {
    return new XincoCoreLanguageJpaController(getEntityManagerFactory());
  }

  public void testFindAll_returnsNonEmpty() {
    try {
      List<XincoCoreLanguage> all = controller().findXincoCoreLanguageEntities();
      assertTrue(all.size() > 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testFindById_returnsKnownLanguage() {
    try {
      XincoCoreLanguage lang = controller().findXincoCoreLanguage(1);
      assertNotNull(lang);
      assertNotNull(lang.getDesignation());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCount_greaterThanZero() {
    try {
      assertTrue(controller().getXincoCoreLanguageCount() > 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testFindAll_paged() {
    try {
      List<XincoCoreLanguage> page = controller().findXincoCoreLanguageEntities(1, 0);
      assertNotNull(page);
      assertTrue(page.size() <= 1);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCreateAndDestroy() {
    try {
      XincoCoreLanguage lang = new XincoCoreLanguage();
      lang.setSign("tt");
      lang.setDesignation("Test Language");
      controller().create(lang);
      assertTrue(lang.getId() > 0);

      XincoCoreLanguage found = controller().findXincoCoreLanguage(lang.getId());
      assertNotNull(found);
      assertEquals("tt", found.getSign());

      controller().destroy(lang.getId());
      assertNull(controller().findXincoCoreLanguage(lang.getId()));
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
