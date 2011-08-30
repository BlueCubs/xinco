package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.OPCode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataServerTest extends XincoTestCase {

    public XincoCoreDataServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreDataServerTest.class);
        return suite;
    }

    /**
     * Test of write2DB method, of class XincoCoreDataServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreDataServer instance = new XincoCoreDataServer(0, 1, 1, 1, "Test Data", 1);
            assertTrue(instance.write2DB() > 0);
            XincoAddAttributeServer xaa;
            for (int i = 0; i < instance.getXincoCoreDataType().getXincoCoreDataTypeAttributes().size(); i++) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
                xaa = new XincoAddAttributeServer(instance.getId(),
                        (instance.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(i)).getAttributeId(),
                        0, 0, 0, "", "",
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
                xaa.write2DB();
                instance.getXincoAddAttributes().add(new XincoAddAttributeServer(
                        xaa.getXincoCoreDataId(), xaa.getAttributeId()));
            }
            instance.write2DB();
            XincoAddAttributeServer attr = null;
            GregorianCalendar c = new GregorianCalendar();
            DatatypeFactory factory = DatatypeFactory.newInstance();
            for (int i = 1; i < 8; i++) {
                switch (i) {
                    case 5:
                        c.setTime(new Date());
                        attr = new XincoAddAttributeServer(instance.getId(),
                                5, 1, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                        break;
                    case 6:
                        c.setTime(new Date());
                        c.add(Calendar.DAY_OF_YEAR, -1);
                        attr = new XincoAddAttributeServer(instance.getId(),
                                6, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                        break;
                    default:
                        c.setTime(new Date());
                        attr = new XincoAddAttributeServer(instance.getId(),
                                i, 0, 0, 0, "", "", factory.newXMLGregorianCalendar(c));
                        break;
                }
                addAttribute(instance, attr);
            }
            //Need to add at least one log (should be there by default (creation)
            c.setTimeInMillis(System.currentTimeMillis());
            XincoCoreLogServer log = new XincoCoreLogServer(instance.getId(), 1,
                    OPCode.CREATION.ordinal(), c, "", 1, 0, 0, "");
            log.write2DB();
            instance.getXincoCoreLogs().add(log);
            instance.write2DB();
            assertTrue(instance.write2DB() > 0);
            System.out.println("deleteFromDB");
            assertTrue(instance.deleteFromDB() == 0);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    private void addAttribute(XincoCoreDataServer xcds, XincoAddAttributeServer attr) throws XincoException {
        attr.write2DB();
        xcds.getXincoAddAttributes().add(new XincoAddAttributeServer(
                attr.getXincoCoreDataId(), attr.getAttributeId()));
        xcds.write2DB();
    }

    /**
     * Test of findXincoCoreData method, of class XincoCoreDataServer.
     */
    public void testFindXincoCoreData() {
        System.out.println("findXincoCoreData");
        String attrS = "xinco.org";
        int attrLID = 2;
        boolean attrSA = false;
        boolean attrSFD = false;
        assertTrue(XincoCoreDataServer.findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size() > 0);
        attrSA = true;
        assertTrue(XincoCoreDataServer.findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size() > 0);
        attrSFD = true;
        assertTrue(XincoCoreDataServer.findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size() > 0);
        attrS = "none";
        assertTrue(XincoCoreDataServer.findXincoCoreData(attrS, attrLID, attrSA, attrSFD).isEmpty());
    }
}
