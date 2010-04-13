/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.client;

import com.bluecubs.xinco.client.object.XincoClientConnectionProfile;
import java.util.Vector;
import junit.framework.TestCase;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoExplorerTest extends TestCase {

    public XincoExplorerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConfigFileVersioning() {
        //Create file with newer version
        Vector xincoClientConfig = new Vector();
        xincoClientConfig.add(new Vector());
        //insert default connection profiles
        ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "xinco Demo User";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://xinco.org:8080/xinco_demo/services/Xinco";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "user";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "user";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
        ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "xinco Demo Admin";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://xinco.org:8080/xinco_demo/services/Xinco";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "admin";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "admin";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
        ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "Template Profile";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://[server_domain]:8080/xinco/services/Xinco";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "your_username";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "your_password";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
        ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "Admin (localhost)";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "admin";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "admin";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
        ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "User (localhost)";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "user";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "user";
        ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = true;
        //add Pluggable Look and Feel
        xincoClientConfig.addElement(new String("javax.swing.plaf.metal.MetalLookAndFeel"));
        //add locale
        xincoClientConfig.addElement("x");
        //add version
        xincoClientConfig.addElement(XincoExplorer.ConfigFileVersion + 1);
        try {
            java.io.FileOutputStream fout =
                    new java.io.FileOutputStream(System.getProperty("user.home")
                    + System.getProperty("file.separator") + "xincoClientConfig.dat");
            java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(fout);
            os.writeObject(xincoClientConfig);
            os.close();
            fout.close();
        } catch (Exception ioe) {
            //error handling
            System.out.println("Unable to write Profiles-File!");
        }
    }
}
