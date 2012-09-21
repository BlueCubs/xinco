package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.db.DBState;
import junit.framework.TestCase;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public abstract class AbstractXincoDataBaseTestCase extends TestCase {

    public AbstractXincoDataBaseTestCase(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        XincoDBManager.setPU("XincoTest");
        assertTrue(XincoDBManager.getState().equals(DBState.VALID));
    }
}
