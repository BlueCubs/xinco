package com.bluecubs.xinco.tools;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
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
        System.out.println("Testing with extension: " + extension);
        Icon result = instance.getIcon(extension);
        assertTrue(FileSystemView.getFileSystemView() == null ? result == null : result != null);
        extension = null;
        System.out.println("Testing with extension: " + extension);
        result = instance.getIcon(extension);
        assertTrue(result == null);
        extension = "txt";
        System.out.println("Testing with extension: " + extension);
        result = instance.getIcon(extension);
        assertTrue(FileSystemView.getFileSystemView() == null ? result == null : result != null);
        extension = "tx";
        System.out.println("Testing with extension: " + extension);
        result = instance.getIcon(extension);
        assertTrue(result != null);
    }
}
