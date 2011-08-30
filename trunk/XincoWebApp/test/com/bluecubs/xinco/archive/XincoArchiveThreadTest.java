package com.bluecubs.xinco.archive;

import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.server.XincoAddAttributeServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.XincoException;
import com.bluecubs.xinco.core.server.XincoTestCase;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLanguageJpaController;
import com.bluecubs.xinco.core.server.service.XincoAddAttribute;
import com.bluecubs.xinco.core.server.service.XincoCoreLog;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoArchiveThreadTest extends XincoTestCase {

    public XincoArchiveThreadTest(String testName) {
        super(testName);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of archiveData method, of class XincoArchiveThread.
     */
    @Test
    public void testArchiveData() {
        try {
            System.out.println("archiveData");
            //Nothing in database
            assertTrue(XincoArchiveThread.archiveData());
            //Add data
            XincoCoreDataServer xcds = new XincoCoreDataServer(0, 1, 1, 1, "Test Data", 1);
            assertTrue(xcds.write2DB() > 0);
            assertTrue(XincoArchiveThread.archiveData());
            assertFalse(isArchived(xcds));
            //Make data old enough to archive
            System.out.println("Datetime check");
            XincoAddAttributeServer attr = getAttribute(xcds, 6);
            GregorianCalendar c = new GregorianCalendar();
            DatatypeFactory factory = DatatypeFactory.newInstance();
            if (attr == null) {
                System.out.println("Creating attribute for the test...");
                c.setTime(new Date());
                attr = new XincoAddAttributeServer(xcds.getId(), 6, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                attr.setChangerID(1);
                attr.write2DB();
                System.out.println("Done!");
            }
            c.setTime(new Date());
            c.add(GregorianCalendar.DAY_OF_MONTH, 1);
            attr.setAttribDatetime(factory.newXMLGregorianCalendar(c));
            attr.write2DB();
            xcds.getXincoAddAttributes().add(new XincoAddAttributeServer(
                    attr.getXincoCoreDataId(), attr.getAttributeId()));
            xcds.write2DB();
            attr = getAttribute(xcds, 5);
            if (attr == null) {
                System.out.println("Creating attribute for the test...");
                c.setTime(new Date());
                attr = new XincoAddAttributeServer(xcds.getId(), 5, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                attr.write2DB();
                System.out.println("Done!");
                System.out.println("Done!");
            }
            attr.setAttribUnsignedint(1);
            attr.write2DB();
            xcds.getXincoAddAttributes().add(new XincoAddAttributeServer(
                    attr.getXincoCoreDataId(), attr.getAttributeId()));
            xcds.write2DB();
            show(xcds);
            assertTrue(XincoArchiveThread.archiveData());
            assertTrue(isArchived(xcds));
        } catch (XincoException ex) {
            Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    private void show(XincoCoreDataServer xcds) {
        try {
            System.out.println(new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreData(xcds.getId()).toString());
            System.out.println("Designation: " + xcds.getDesignation());
            System.out.println("Status Number: " + xcds.getStatusNumber());
            System.out.println(new XincoCoreDataTypeJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreDataType(xcds.getXincoCoreDataType().getId()));
            System.out.println(new XincoCoreLanguageJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreLanguage(xcds.getXincoCoreLanguage().getId()));
            System.out.println("Attributes: ");
            for (XincoAddAttribute attr : xcds.getXincoAddAttributes()) {
                System.out.println("Attribute ID: " + attr.getAttributeId());
                System.out.println("unsigned int: " + attr.getAttribUnsignedint());
                System.out.println("date time: " + attr.getAttribDatetime());
                System.out.println("data id: " + attr.getXincoCoreDataId());
            }
        } catch (XincoException ex) {
            Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isArchived(XincoCoreDataServer xcds) {
        for (XincoCoreLog log : xcds.getXincoCoreLogs()) {
            if (log.getOpCode() == OPCode.ARCHIVED.ordinal() + 1) {
                return true;
            }
        }
        return false;
    }

    private XincoAddAttributeServer getAttribute(XincoCoreDataServer xcds, int id) {
        for (XincoAddAttribute attr : xcds.getXincoAddAttributes()) {
            if (attr.getAttributeId() == id) {
                try {
                    return new XincoAddAttributeServer(attr.getXincoCoreDataId(), attr.getAttributeId());
                } catch (XincoException ex) {
                    Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
        }
        return null;
    }
}
