package com.bluecubs.xinco.core.server;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoAddAttributeServerTest extends XincoTestCase {

    private static XincoCoreDataServer xcds;

    public XincoAddAttributeServerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        xcds = new XincoCoreDataServer(0, 1, 1, 1, "Test Data", 1);
        assertTrue(xcds.write2DB() > 0);
    }

    @Override
    protected void tearDown() throws Exception {
        xcds.deleteFromDB();
        super.tearDown();
    }

    /**
     * Test of write2DB method, of class XincoAddAttributeServer.
     */
    @Test
    public void testWrite2DB() throws Exception {
        System.out.println("write2DB");
        XincoAddAttributeServer xaa;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        xaa = new XincoAddAttributeServer(xcds.getId(),
                (xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(0)).getAttributeId(),
                0, 0, 0, "", "",
                DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
        xaa.write2DB();
        assertTrue(new XincoAddAttributeServer(xcds.getId(),
                (xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(0)).getAttributeId()) != null);
        assertTrue(XincoAddAttributeServer.getXincoAddAttributes(xaa.getAttributeId())!=null);
    }
}
