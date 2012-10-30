package com.bluecubs.xinco.archive;

import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLanguageJpaController;
import com.bluecubs.xinco.core.server.service.XincoAddAttribute;
import com.bluecubs.xinco.core.server.service.XincoCoreLog;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoArchiveThreadTest extends AbstractXincoDataBaseTestCase {

    public XincoArchiveThreadTest(String testName) {
        super(testName);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private void addAttributes(XincoCoreDataServer xcds) throws DatatypeConfigurationException {
        System.out.println("Adding default attributes...");
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
        System.out.println("Making data old enough to archive");
        XincoAddAttributeServer attr;
        GregorianCalendar c = new GregorianCalendar();
        DatatypeFactory factory = DatatypeFactory.newInstance();
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
    }

    /**
     * Test of archiveData method, of class XincoArchiveThread.
     */
    @Test
    public void testArchiveData() {
        try {
            System.out.println("archiveData");
            //Add data
            XincoCoreDataServer xcds = new XincoCoreDataServer(0, 1, 1, 1, "Test Data", 1);
            assertTrue(xcds.write2DB() > 0);
            System.out.println("Archiving");
            assertFalse(XincoArchiver.archiveData(new XincoCoreDataServer(xcds.getId()),
                    XincoCoreNodeServer.getXincoCoreNodeParents(xcds.getXincoCoreNodeId())));
            System.out.println("Done!");
            assertFalse(XincoCoreDataServer.isArchived(xcds));
            addAttributes(xcds);
            System.out.println("Archiving");
            try {
                XincoArchiver.archiveData(new XincoCoreDataServer(xcds.getId()),
                        XincoCoreNodeServer.getXincoCoreNodeParents(xcds.getXincoCoreNodeId()));
                fail();
            } catch (XincoException ex) {
                //This should happen
            }
            //Add the missing log
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            XincoCoreLogServer xcls =
                    new XincoCoreLogServerBuilder().setXincoCoreDataId(xcds.getId())
                    .setOpCode(OPCode.CREATION.ordinal() + 1)
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
            assertTrue(XincoArchiver.archiveData(new XincoCoreDataServer(xcds.getId()),
                    XincoCoreNodeServer.getXincoCoreNodeParents(xcds.getXincoCoreNodeId())));
            System.out.println("Done!");
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
            System.out.println(new XincoCoreDataJpaController(
                    XincoDBManager.getEntityManagerFactory()).findXincoCoreData(
                    xcds.getId()).getDesignation());
            System.out.println("Designation: " + xcds.getDesignation());
            System.out.println("Status Number: " + xcds.getStatusNumber());
            System.out.println(new XincoCoreDataTypeJpaController(
                    XincoDBManager.getEntityManagerFactory()).findXincoCoreDataType(
                    xcds.getXincoCoreDataType().getId()).getDescription());
            System.out.println(new XincoCoreLanguageJpaController(
                    XincoDBManager.getEntityManagerFactory()).findXincoCoreLanguage(
                    xcds.getXincoCoreLanguage().getId()).getDesignation());
            System.out.println("Attributes:");
            for (XincoAddAttribute attr : xcds.getXincoAddAttributes()) {
                System.out.println("Attribute ID: " + attr.getAttributeId());
                System.out.println("unsigned int: " + attr.getAttribUnsignedint());
                System.out.println("date time: " + attr.getAttribDatetime());
            }
            System.out.println("Logs:");
            for (Iterator<Object> it = xcds.getXincoCoreLogs().iterator(); it.hasNext();) {
                XincoCoreLog log = (XincoCoreLog) it.next();
                System.out.println("ID: " + log.getId());
                System.out.println("Code: " + log.getOpCode() + " Desc: "
                        + OPCode.getOPCode(log.getOpCode()).name());
                System.out.println("Description: " + log.getOpDescription());
                System.out.println("Data ID: " + log.getXincoCoreDataId());
            }
        } catch (XincoException ex) {
            Logger.getLogger(XincoArchiveThreadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
