/*
 * XincoApplicationBean.java
 *
 * Created on Mar 19, 2008, 4:51:34 PM
 */
package xincowebapp;

import com.bluecubs.xinco.core.server.persistence.XincoSettingServer;
import com.sun.rave.web.ui.appbase.AbstractApplicationBean;
import java.util.ResourceBundle;
import javax.faces.FacesException;

/**
 * <p>Application scope data bean for your application.  Create properties
 *  here to represent cached data that should be made available to all users
 *  and pages in the application.</p>
 *
 * <p>An instance of this class will be created for you automatically,
 * the first time your application evaluates a value binding expression
 * or method binding expression that references a managed bean using
 * this class.</p>
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class XincoApplicationBean extends AbstractApplicationBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    // </editor-fold>
    private XincoSettingServer settings;
    private String version;
    private ResourceBundle rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");

    /**
     * <p>Construct a new application data bean instance.</p>
     */
    public XincoApplicationBean() {
    }

    /**
     * <p>This method is called when this bean is initially added to
     * application scope.  Typically, this occurs as a result of evaluating
     * a value binding or method binding expression, which utilizes the
     * managed bean facility to instantiate this bean and store it into
     * application scope.</p>
     * 
     * <p>You may customize this method to initialize and cache application wide
     * data values (such as the lists of valid options for dropdown list
     * components), or to allocate resources that are required for the
     * lifetime of the application.</p>
     */
    @Override
    public void init() {
        // Perform initializations inherited from our superclass
        super.init();
        // Perform application initialization that must complete
        // *before* managed components are initialized
        // TODO - add your own initialiation code here
        //Get settings from server
        settings = new XincoSettingServer();
        //Get server version
        version = "["+rb.getString("general.version") + rb.getString("version.high")
                + rb.getString("version.med") + rb.getString("version.low")+"]";
        // <editor-fold defaultstate="collapsed" desc="Managed Component Initialization">
        // Initialize automatically managed components
        // *Note* - this logic should NOT be modified
        try {
            _init();
        } catch (Exception e) {
            log(java.util.ResourceBundle.getBundle("com/bluecubs/xinco/messages/XincoMessages").getString("ApplicationBean1_Initialization_Failure"), e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }

    // </editor-fold>
    // Perform application initialization that must complete
    // *after* managed components are initialized
    // TODO - add your own initialization code here
    }

    /**
     * <p>This method is called when this bean is removed from
     * application scope.  Typically, this occurs as a result of
     * the application being shut down by its owning container.</p>
     * 
     * <p>You may customize this method to clean up resources allocated
     * during the execution of the <code>init()</code> method, or
     * at any later time during the lifetime of the application.</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return an appropriate character encoding based on the
     * <code>Locale</code> defined for the current JavaServer Faces
     * view.  If no more suitable encoding can be found, return
     * "UTF-8" as a general purpose default.</p>
     *
     * <p>The default implementation uses the implementation from
     * our superclass, <code>AbstractApplicationBean</code>.</p>
     */
    @Override
    public String getLocaleCharacterEncoding() {
        return super.getLocaleCharacterEncoding();
    }

    public XincoSettingServer getSettings() {
        return settings;
    }

    public void setSettings(XincoSettingServer settings) {
        this.settings = settings;
    }

    public String getVersion() {
        return version;
    }

    public ResourceBundle getResourceBundle() {
        return rb;
    }
}
