package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.OPCode.CREATION;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.findXincoCoreData;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.xml.datatype.DatatypeFactory.newInstance;

import com.bluecubs.xinco.core.XincoException;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import junit.framework.Test;
import junit.framework.TestSuite;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
public class XincoCoreDataServerTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreDataServerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(XincoCoreDataServerTest.class);
    return suite;
  }

  /** Test of write2DB method, of class XincoCoreDataServer. */
  public void testWrite2DB() {
    try {
      out.println("write2DB");
      XincoCoreDataServer instance = new XincoCoreDataServer(0, 1, 1, 1, "Test Data", 1);
      assertTrue(instance.write2DB() > 0);
      XincoAddAttributeServer xaa;
      for (int i = 0;
          i < instance.getXincoCoreDataType().getXincoCoreDataTypeAttributes().size();
          i++) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        newInstance().newXMLGregorianCalendar(calendar);
        xaa =
            new XincoAddAttributeServer(
                instance.getId(),
                (instance.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(i))
                    .getAttributeId(),
                0,
                0,
                0,
                "",
                "",
                newInstance().newXMLGregorianCalendar(calendar));
        xaa.write2DB();
        instance
            .getXincoAddAttributes()
            .add(new XincoAddAttributeServer(xaa.getXincoCoreDataId(), xaa.getAttributeId()));
      }
      instance.write2DB();
      XincoAddAttributeServer attr;
      GregorianCalendar c = new GregorianCalendar();
      DatatypeFactory factory = newInstance();
      for (int i = 1; i < 8; i++) {
        switch (i) {
          case 5:
            c.setTime(new Date());
            attr =
                new XincoAddAttributeServer(
                    instance.getId(), 5, 1, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
            break;
          case 6:
            c.setTime(new Date());
            c.add(DAY_OF_YEAR, -1);
            attr =
                new XincoAddAttributeServer(
                    instance.getId(), 6, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
            break;
          default:
            c.setTime(new Date());
            attr =
                new XincoAddAttributeServer(
                    instance.getId(), i, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
            break;
        }
        addAttribute(instance, attr);
      }
      // Need to add at least one log (should be there by default (creation)
      c.setTimeInMillis(currentTimeMillis());
      XincoCoreLogServer log =
          new XincoCoreLogServerBuilder()
              .setXincoCoreDataId(instance.getId())
              .setXincoCoreUserId(1)
              .setOpCode(CREATION.ordinal() + 1)
              .setOperationDate(c)
              .setOperationDescription("")
              .setVersionHigh(1)
              .setVersionMid(0)
              .setVersionLow(0)
              .setVersionPostFix("")
              .createXincoCoreLogServer();
      log.write2DB();
      instance.getXincoCoreLogs().add(log);
      instance.write2DB();
      assertTrue(instance.write2DB() > 0);
      assertTrue(instance.deleteFromDB() == 0);
    } catch (DatatypeConfigurationException ex) {
      getLogger(XincoCoreDataServerTest.class.getName()).log(SEVERE, null, ex);
    } catch (XincoException ex) {
      getLogger(XincoCoreDataServerTest.class.getSimpleName()).log(SEVERE, null, ex);
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

  /** Test of findXincoCoreData method, of class XincoCoreDataServer. */
  public void testFindXincoCoreData() {
    String attrS = "xinco.org";
    int attrLID = 2;
    boolean attrSA = false;
    boolean attrSFD = false;
    assertTrue(findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size() > 0);
    attrSA = true;
    assertTrue(findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size() > 0);
    attrSFD = true;
    assertTrue(findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size() > 0);
    attrS = "none";
    assertTrue(findXincoCoreData(attrS, attrLID, attrSA, attrSFD).isEmpty());
  }
}
