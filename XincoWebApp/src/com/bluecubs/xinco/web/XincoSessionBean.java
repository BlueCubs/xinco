/**
 *Copyright 2007 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            XincoSessionBean
 *
 * Description:     XincoSessionBean
 *
 * Original Author: Javier A. Ortiz
 * Date:            2007
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.web;

import com.sun.rave.web.ui.appbase.AbstractSessionBean;
import com.sun.sql.rowset.CachedRowSetXImpl;
import javax.faces.FacesException;

/**
 * <p>Session scope data bean for your application.  Create properties
 *  here to represent cached data that should be made available across
 *  multiple HTTP requests for an individual user.</p>
 *
 * <p>An instance of this class will be created for you automatically,
 * the first time your application evaluates a value binding expression
 * or method binding expression that references a managed bean using
 * this class.</p>
 *
 * @author ortizbj
 */
public class XincoSessionBean extends AbstractSessionBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">
    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        xinco_settingRowSet1.setDataSourceName("java:comp/env/jdbc/xinco_MySQL");
        xinco_settingRowSet1.setCommand("SELECT * FROM xinco_setting");
        xinco_settingRowSet1.setTableName("xinco_setting");
    }
    // </editor-fold>
    private String title;
    private String XincoSettingAddButtonLabel;
    private String XincoSettingSaveButtonLabel;
    private CachedRowSetXImpl cachedRowSetXImpl1 = new CachedRowSetXImpl();

    public CachedRowSetXImpl getCachedRowSetXImpl1() {
        return cachedRowSetXImpl1;
    }

    public void setCachedRowSetXImpl1(CachedRowSetXImpl crsxi) {
        this.cachedRowSetXImpl1 = crsxi;
    }
    private CachedRowSetXImpl xinco_settingRowSet1 = new CachedRowSetXImpl();

    public CachedRowSetXImpl getXinco_settingRowSet1() {
        return xinco_settingRowSet1;
    }

    public void setXinco_settingRowSet1(CachedRowSetXImpl crsxi) {
        this.xinco_settingRowSet1 = crsxi;
    }

    /**
     * <p>Construct a new session data bean instance.</p>
     */
    public XincoSessionBean() {
    }

    /**
     * <p>This method is called when this bean is initially added to
     * session scope.  Typically, this occurs as a result of evaluating
     * a value binding or method binding expression, which utilizes the
     * managed bean facility to instantiate this bean and store it into
     * session scope.</p>
     * 
     * <p>You may customize this method to initialize and cache data values
     * or resources that are required for the lifetime of a particular
     * user session.</p>
     */
    @Override
    public void init() {
        // Perform initializations inherited from our superclass
        super.init();
        // Perform application initialization that must complete
        // *before* managed components are initialized
        // TODO - add your own initialiation code here

        // <editor-fold defaultstate="collapsed" desc="Managed Component Initialization">
        // Initialize automatically managed components
        // *Note* - this logic should NOT be modified
        try {
            _init();
        } catch (Exception e) {
            log("SessionBean1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }

    // </editor-fold>
    // Perform application initialization that must complete
    // *after* managed components are initialized
    // TODO - add your own initialization code here
    }

    /**
     * <p>This method is called when the session containing it is about to be
     * passivated.  Typically, this occurs in a distributed servlet container
     * when the session is about to be transferred to a different
     * container instance, after which the <code>activate()</code> method
     * will be called to indicate that the transfer is complete.</p>
     * 
     * <p>You may customize this method to release references to session data
     * or resources that can not be serialized with the session itself.</p>
     */
    @Override
    public void passivate() {
    }

    /**
     * <p>This method is called when the session containing it was
     * reactivated.</p>
     * 
     * <p>You may customize this method to reacquire references to session
     * data or resources that could not be serialized with the
     * session itself.</p>
     */
    @Override
    public void activate() {
    }

    /**
     * <p>This method is called when this bean is removed from
     * session scope.  Typically, this occurs as a result of
     * the session timing out or being terminated by the application.</p>
     * 
     * <p>You may customize this method to clean up resources allocated
     * during the execution of the <code>init()</code> method, or
     * at any later time during the lifetime of the application.</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected XincoApplicationBean getXincoApplicationBean() {
        return (XincoApplicationBean) getBean("XincoApplicationBean");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getXincoSettingAddButtonLabel() {
        return XincoSettingAddButtonLabel;
    }

    public void setXincoSettingAddButtonLabel(String XincoSettingAddButtonLabel) {
        this.XincoSettingAddButtonLabel = XincoSettingAddButtonLabel;
    }

    public String getXincoSettingSaveButtonLabel() {
        return XincoSettingSaveButtonLabel;
    }

    public void setXincoSettingSaveButtonLabel(String XincoSettingSaveButtonLabel) {
        this.XincoSettingSaveButtonLabel = XincoSettingSaveButtonLabel;
    }
}
