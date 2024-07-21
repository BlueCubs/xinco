package com.bluecubs.xinco.archive;

import static com.bluecubs.xinco.archive.XincoArchiver.archiveData;
import static com.bluecubs.xinco.core.OPCode.CREATION;
import static com.bluecubs.xinco.core.OPCode.getOPCode;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.isArchived;
import static com.bluecubs.xinco.core.server.XincoCoreNodeServer.getXincoCoreNodeParents;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static java.lang.System.out;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.xml.datatype.DatatypeFactory.newInstance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.XincoAddAttributeServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.XincoCoreLogServerBuilder;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLanguageJpaController;
import com.bluecubs.xinco.server.service.XincoAddAttribute;
import com.bluecubs.xinco.server.service.XincoCoreLog;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
public class XincoArchiveThreadTest extends AbstractXincoDataBaseTestCase {

  public XincoArchiveThreadTest(String testName) {
    super(testName);
  }

  @BeforeClass
  public static void setUpClass() throws Exception {}

  @AfterClass
  public static void tearDownClass() throws Exception {}

  private void addAttributes(XincoCoreDataServer xcds) throws DatatypeConfigurationException {
    out.println("Adding default attributes...");
    XincoAddAttributeServer xaa;
    for (int i = 0; i < xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().size(); i++) {
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(new Date());
      newInstance().newXMLGregorianCalendar(calendar);
      xaa =
          new XincoAddAttributeServer(
              xcds.getId(),
              (xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(i))
                  .getAttributeId(),
              0,
              0,
              0,
              "",
              "",
              newInstance().newXMLGregorianCalendar(calendar));
      xaa.write2DB();
      xcds.getXincoAddAttributes()
          .add(new XincoAddAttributeServer(xaa.getXincoCoreDataId(), xaa.getAttributeId()));
    }
    xcds.write2DB();
    out.println("Making data old enough to archive");
    XincoAddAttributeServer attr;
    GregorianCalendar c = new GregorianCalendar();
    DatatypeFactory factory = newInstance();
    for (int i = 1; i < 8; i++) {
      switch (i) {
        case 5:
          c.setTime(new Date());
          attr =
              new XincoAddAttributeServer(
                  xcds.getId(), 5, 1, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
          break;
        case 6:
          c.setTime(new Date());
          c.add(DAY_OF_YEAR, -1);
          attr =
              new XincoAddAttributeServer(
                  xcds.getId(), 6, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
          break;
        default:
          c.setTime(new Date());
          attr =
              new XincoAddAttributeServer(
                  xcds.getId(), i, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
          break;
      }
      addAttribute(xcds, attr);
    }
  }

  /** Test of archiveData method, of class XincoArchiveThread. */
  @Test
  public void testArchiveData() {
    try {
      out.println("archiveData");
      // Add data
      XincoCoreDataServer xcds = new XincoCoreDataServer(0, 1, 1, 1, "Test Data", 1);
      assertTrue(xcds.write2DB() > 0);
      out.println("Archiving");
      assertFalse(
          archiveData(
              new XincoCoreDataServer(xcds.getId()),
              getXincoCoreNodeParents(xcds.getXincoCoreNodeId())));
      out.println("Done!");
      assertFalse(isArchived(xcds));
      addAttributes(xcds);
      out.println("Archiving");
      try {
        archiveData(
            new XincoCoreDataServer(xcds.getId()),
            getXincoCoreNodeParents(xcds.getXincoCoreNodeId()));
        fail();
      } catch (XincoException ex) {
        // This should happen
      }
      // Add the missing log
      GregorianCalendar c = new GregorianCalendar();
      c.setTime(new Date());
      XincoCoreLogServer xcls =
          new XincoCoreLogServerBuilder()
              .setXincoCoreDataId(xcds.getId())
              .setOpCode(CREATION.ordinal() + 1)
              .setOperationDate(c)
              .setOperationDescription("")
              .setVersionHigh(1)
              .setVersionMid(0)
              .setVersionLow(0)
              .setVersionPostFix("")
              .setXincoCoreUserId(1)
              .createXincoCoreLogServer();
      xcls.write2DB();
      xcds.getXincoCoreLogs().add((XincoCoreLog) xcls);
      xcds.write2DB();
      assertTrue(
          archiveData(
              new XincoCoreDataServer(xcds.getId()),
              getXincoCoreNodeParents(xcds.getXincoCoreNodeId())));
      out.println("Done!");
      xcds.loadLogs();
      show(xcds);
      assertTrue(isArchived(xcds));
    } catch (XincoException | Exception ex) {
      getLogger(XincoArchiveThreadTest.class.getName()).log(SEVERE, null, ex);
      fail();
    }
  }

  private void addAttribute(XincoCoreDataServer xcds, XincoAddAttributeServer attr)
      throws XincoException {
    attr.write2DB();
    xcds.getXincoAddAttributes()
        .add(new XincoAddAttributeServer(attr.getXincoCoreDataId(), attr.getAttributeId()));
    xcds.write2DB();
  }

  private void show(XincoCoreDataServer xcds) {
    try {
      out.println(
          new XincoCoreDataJpaController(getEntityManagerFactory())
              .findXincoCoreData(xcds.getId())
              .getDesignation());
      out.println("Designation: " + xcds.getDesignation());
      out.println("Status Number: " + xcds.getStatusNumber());
      out.println(
          new XincoCoreDataTypeJpaController(getEntityManagerFactory())
              .findXincoCoreDataType(xcds.getXincoCoreDataType().getId())
              .getDescription());
      out.println(
          new XincoCoreLanguageJpaController(getEntityManagerFactory())
              .findXincoCoreLanguage(xcds.getXincoCoreLanguage().getId())
              .getDesignation());
      out.println("Attributes:");
      for (XincoAddAttribute attr : xcds.getXincoAddAttributes()) {
        out.println("Attribute ID: " + attr.getAttributeId());
        out.println("unsigned int: " + attr.getAttribUnsignedint());
        out.println("date time: " + attr.getAttribDatetime());
      }
      out.println("Logs:");
      for (Iterator<Object> it = xcds.getXincoCoreLogs().iterator(); it.hasNext(); ) {
        XincoCoreLog log = (XincoCoreLog) it.next();
        out.println("ID: " + log.getId());
        out.println("Code: " + log.getOpCode() + " Desc: " + getOPCode(log.getOpCode()).name());
        out.println("Description: " + log.getOpDescription());
        out.println("Data ID: " + log.getXincoCoreDataId());
      }
    } catch (XincoException ex) {
      getLogger(XincoArchiveThreadTest.class.getName()).log(SEVERE, null, ex);
    }
  }
}
