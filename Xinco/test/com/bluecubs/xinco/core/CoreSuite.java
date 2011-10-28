/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({com.bluecubs.xinco.core.XincoExceptionTest.class, com.bluecubs.xinco.core.XincoDataStatusTest.class, com.bluecubs.xinco.core.OPCodeTest.class})
public class CoreSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
