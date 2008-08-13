/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.filter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class SetCharacterEncodingFilterTest extends TestCase {
    
    public SetCharacterEncodingFilterTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SetCharacterEncodingFilterTest.class);
        return suite;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of destroy method, of class SetCharacterEncodingFilter.
     */
    public void testDestroy() {
        System.out.println("destroy");
        SetCharacterEncodingFilter instance = new SetCharacterEncodingFilter();
        instance.destroy();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doFilter method, of class SetCharacterEncodingFilter.
     */
    public void testDoFilter() throws Exception {
        System.out.println("doFilter");
        ServletRequest request = null;
        ServletResponse response = null;
        FilterChain chain = null;
        SetCharacterEncodingFilter instance = new SetCharacterEncodingFilter();
        instance.doFilter(request, response, chain);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of init method, of class SetCharacterEncodingFilter.
     */
    public void testInit() throws Exception {
        System.out.println("init");
        FilterConfig filterConfig = null;
        SetCharacterEncodingFilter instance = new SetCharacterEncodingFilter();
        instance.init(filterConfig);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selectEncoding method, of class SetCharacterEncodingFilter.
     */
    public void testSelectEncoding() {
        System.out.println("selectEncoding");
        ServletRequest request = null;
        SetCharacterEncodingFilter instance = new SetCharacterEncodingFilter();
        String expResult = "";
        String result = instance.selectEncoding(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
