package com.bluecubs.xinco.archive;

import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLanguageJpaController;
import com.bluecubs.xinco.core.server.service.XincoAddAttribute;
import com.bluecubs.xinco.core.server.service.XincoCoreLog;
import java.util.Calendar;
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
    public void testArchiveData1() {
        try {
            System.out.println("archiveData1");
            //Nothing in database
            assertTrue(XincoArchiveThread.archiveData());
            //Add data
            XincoCoreDataServer xcds = new XincoCoreDataServer(0, 1, 1, 1, "Test Data", 1);
            assertTrue(xcds.write2DB() > 0);
            XincoAddAttributeServer xaa;
            for (int i = 0; i < xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().size(); i++) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
                xaa = new XincoAddAttributeServer(xcds.getId(),
                        (xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(i)).getAttributeId(),
                        0, 0, 0, "", "",
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
                xaa.write2DB();
                xcds.getXincoAddAttributes().add(new XincoAddAttributeServer(
                        xaa.getXincoCoreDataId(), xaa.getAttributeId()));
            }
            xcds.write2DB();
            assertTrue(XincoArchiveThread.archiveData());
            assertFalse(XincoCoreDataServer.isArchived(xcds));
            //Make data old enough to archive
            System.out.println("Datetime check");
            XincoAddAttributeServer attr;
            GregorianCalendar c = new GregorianCalendar();
            DatatypeFactory factory = DatatypeFactory.newInstance();
            System.out.println("Creating attributes for the test...");
            for (int i = 1; i < 8; i++) {
                switch (i) {
                    case 5:
                        c.setTime(new Date());
                        attr = new XincoAddAttributeServer(xcds.getId(),
                                5, 1, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                        break;
                    case 6:
                        c.setTime(new Date());
                        c.add(Calendar.DAY_OF_YEAR, -1);
                        attr = new XincoAddAttributeServer(xcds.getId(),
                                6, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                        break;
                    default:
                        c.setTime(new Date());
                        attr = new XincoAddAttributeServer(xcds.getId(),
                                i, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                        break;
                }
                addAttribute(xcds, attr);
            }
            //Need to add at least one log (should be there by default (creation)
            c.setTimeInMillis(System.currentTimeMillis());
            XincoCoreLogServer log = new XincoCoreLogServer(xcds.getId(), 1,
                    OPCode.CREATION.ordinal() + 1, c, "", 1, 0, 0, "");
            log.write2DB();
            xcds.getXincoCoreLogs().add(log);
            xcds.write2DB();
            assertTrue(XincoArchiveThread.archiveData());
            xcds.loadLogs();
            show(xcds);
            assertTrue(XincoCoreDataServer.isArchived(xcds));
        } catch (XincoException ex) {
            Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of archiveData method, of class XincoArchiveThread.
     */
    @Test
    public void testArchiveData2() {
        try {
            System.out.println("archiveData2");
            //Nothing in database
            assertTrue(XincoArchiveThread.archiveData());
            //Add data
            XincoCoreDataServer xcds = new XincoCoreDataServer(0, 1, 1, 1, "Test Data", 1);
            assertTrue(xcds.write2DB() > 0);
            XincoAddAttributeServer xaa;
            for (int i = 0; i < xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().size(); i++) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
                xaa = new XincoAddAttributeServer(xcds.getId(),
                        (xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(i)).getAttributeId(),
                        0, 0, 0, "", "",
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
                xaa.write2DB();
                xcds.getXincoAddAttributes().add(new XincoAddAttributeServer(
                        xaa.getXincoCoreDataId(), xaa.getAttributeId()));
            }
            xcds.write2DB();
            assertTrue(XincoArchiveThread.archiveData());
            assertFalse(XincoCoreDataServer.isArchived(xcds));
            //Make data old enough to archive
            System.out.println("Datetime check");
            XincoAddAttributeServer attr;
            GregorianCalendar c = new GregorianCalendar();
            DatatypeFactory factory = DatatypeFactory.newInstance();
            System.out.println("Creating attributes for the test...");
            for (int i = 1; i < 8; i++) {
                switch (i) {
                    case 5:
                        c.setTime(new Date());
                        attr = new XincoAddAttributeServer(xcds.getId(),
                                5, 2, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                        break;
                    case 6:
                        c.setTime(new Date());
                        c.add(Calendar.DAY_OF_YEAR, -1);
                        attr = new XincoAddAttributeServer(xcds.getId(),
                                6, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                        break;
                    default:
                        c.setTime(new Date());
                        attr = new XincoAddAttributeServer(xcds.getId(),
                                i, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                        break;
                }
                addAttribute(xcds, attr);
            }
            //Need to add at least one log (should be there by default (creation)
            c.setTimeInMillis(System.currentTimeMillis());
            XincoCoreLogServer log = new XincoCoreLogServer(xcds.getId(), 1,
                    OPCode.CREATION.ordinal() + 1, c, "", 1, 0, 0, "");
            log.write2DB();
            xcds.getXincoCoreLogs().add(log);
            xcds.write2DB();
            assertTrue(XincoArchiveThread.archiveData());
            xcds.loadLogs();
            show(xcds);
            assertTrue(XincoCoreDataServer.isArchived(xcds));
        } catch (XincoException ex) {
            Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    private void addAttribute(XincoCoreDataServer xcds, XincoAddAttributeServer attr) throws XincoException {
        attr.write2DB();
        xcds.getXincoAddAttributes().add(new XincoAddAttributeServer(
                attr.getXincoCoreDataId(), attr.getAttributeId()));
        xcds.write2DB();
    }

    private void show(XincoCoreDataServer xcds) {
        try {
            System.out.println(new XincoCoreDataJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreData(xcds.getId()).toString());
            System.out.println("Designation: " + xcds.getDesignation());
            System.out.println("Status Number: " + xcds.getStatusNumber());
            System.out.println(new XincoCoreDataTypeJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreDataType(xcds.getXincoCoreDataType().getId()));
            System.out.println(new XincoCoreLanguageJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreLanguage(xcds.getXincoCoreLanguage().getId()));
            System.out.println("Attributes:");
            for (XincoAddAttribute attr : xcds.getXincoAddAttributes()) {
                System.out.println("Attribute ID: " + attr.getAttributeId());
                System.out.println("unsigned int: " + attr.getAttribUnsignedint());
                System.out.println("date time: " + attr.getAttribDatetime());
                System.out.println("data id: " + attr.getXincoCoreDataId());
            }
            System.out.println("Logs:");
            for (XincoCoreLog log : xcds.getXincoCoreLogs()) {
                System.out.println("ID: " + log.getId());
                System.out.println("Code: " + log.getOpCode());
                System.out.println("Description: " + log.getOpDescription());
                System.out.println("Data ID: " + log.getXincoCoreDataId());
                System.out.println("Time: " + log.getOpDatetime());
            }
        } catch (XincoException ex) {
            Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
