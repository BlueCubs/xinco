package com.bluecubs.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class LoginTest extends SeleneseTestBase {

    @Before
    public void beforeClass() {
        selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8084");
        selenium.start();
    }

    @Test
    public void testSimple() throws Exception {
        selenium.open("/xinco");
        selenium.click("selectLang");
    }
}
