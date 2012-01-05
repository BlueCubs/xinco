package com.bluecubs.xinco.tools;

import javax.swing.Icon;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
public class XincoFileIconManagerTest {

    public XincoFileIconManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getIcon method, of class XincoFileIconManager.
     */
    @Test
    public void testGetIcon() {
        System.out.println("getIcon");
        String extension = ".txt";
        XincoFileIconManager instance = new XincoFileIconManager();
        System.out.println("testing with extension: " + extension);
        Icon result = instance.getIcon(extension);
        assertTrue(result != null);
        extension = null;
        System.out.println("testing with extension: " + extension);
        result = instance.getIcon(extension);
        assertTrue(result == null);
        extension = "txt";
        System.out.println("testing with extension: " + extension);
        result = instance.getIcon(extension);
        assertTrue(result != null);
        extension = "tx";
        System.out.println("testing with extension: " + extension);
        result = instance.getIcon(extension);
        assertTrue(result == null);
    }
}
