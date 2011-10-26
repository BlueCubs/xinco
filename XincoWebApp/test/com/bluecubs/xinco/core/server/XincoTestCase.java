package com.bluecubs.xinco.core.server;

import junit.framework.TestCase;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoTestCase extends TestCase {

    public XincoTestCase(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        XincoDBManager.setPU("XincoTest");
    }
}
