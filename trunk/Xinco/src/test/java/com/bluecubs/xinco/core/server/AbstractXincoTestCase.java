package com.bluecubs.xinco.core.server;

import junit.framework.TestCase;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public abstract class AbstractXincoTestCase extends TestCase {

    public AbstractXincoTestCase(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        XincoDBManager.setPU("XincoTest");
    }
}
