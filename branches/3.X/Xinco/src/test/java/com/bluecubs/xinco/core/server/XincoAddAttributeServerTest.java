package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoAddAttributeServer.getXincoAddAttributes;
import java.util.Date;
import java.util.GregorianCalendar;
import static javax.xml.datatype.DatatypeFactory.newInstance;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoAddAttributeServerTest extends AbstractXincoDataBaseTestCase {

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
        XincoAddAttributeServer xaa;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        newInstance().newXMLGregorianCalendar(calendar);
        xaa = new XincoAddAttributeServer(xcds.getId(),
                (xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(0)).getAttributeId(),
                0, 0, 0, "", "",
                newInstance().newXMLGregorianCalendar(calendar));
        xaa.write2DB();
        assertTrue(new XincoAddAttributeServer(xcds.getId(),
                (xcds.getXincoCoreDataType().getXincoCoreDataTypeAttributes().get(0)).getAttributeId()) != null);
        assertTrue(getXincoAddAttributes(xaa.getAttributeId()) != null);
    }
}
