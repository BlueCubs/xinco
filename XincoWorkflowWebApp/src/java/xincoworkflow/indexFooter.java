/*
 * indexFooter.java
 *
 * Created on June 12, 2007, 3:39 PM
 * Copyright Javier A. Ortiz
 */
package xincoworkflow;

import com.bluecubs.xinco.workflow.WorkflowSetting;
import com.bluecubs.xinco.workflow.server.WorkflowSettingServer;
import com.sun.rave.web.ui.appbase.AbstractFragmentBean;
import com.sun.rave.web.ui.component.Label;
import javax.faces.FacesException;

/**
 * <p>Fragment bean that corresponds to a similarly named JSP page
 * fragment.  This class contains component definitions (and initialization
 * code) for all components that you have defined on this fragment, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 */
public class indexFooter extends AbstractFragmentBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">
    private int __placeholder;
    
    /**
     * <p>Automatically managed component initialization. <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private Label label1 = new Label();

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label l) {
        this.label1 = l;
    }
    // </editor-fold>
    
    public indexFooter() {
    }
    
    /**
     * <p>Callback method that is called whenever a page containing
     * this page fragment is navigated to, either directly via a URL,
     * or indirectly via page navigation.  Override this method to acquire
     * resources that will be needed for event handlers and lifecycle methods.</p>
     *
     * <p>The default implementation does nothing.</p>
     */
    public void init() {
        // Perform initializations inherited from our superclass
        super.init();
        // Perform application initialization that must complete
        // *before* managed components are initialized
        // TODO - add your own initialiation code here
        
        
        // <editor-fold defaultstate="collapsed" desc="Visual-Web-managed Component Initialization">
        // Initialize automatically managed components
        // *Note* - this logic should NOT be modified
        try {
            _init();
        } catch (Exception e) {
            log("Page1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e: new FacesException(e);
        }
        
        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
        
        WorkflowSettingServer xss= new WorkflowSettingServer();
        String setting = ((WorkflowSetting)(xss.getWorkflow_settings().elementAt(7))).getString_value();
        getSession().setLicenseString("&copy; "+setting+", "+
                getSession().getResourceBundle().getString("message.admin.main.footer"));
    }
    
    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called.  Override this
     * method to release resources acquired in the <code>init()</code>
     * resources that will be needed for event handlers and lifecycle methods.</p>
     *
     * <p>The default implementation does nothing.</p>
     */
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected ApplicationBean getApplicationBean() {
        return (ApplicationBean)getBean("ApplicationBean");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected Request getRequest() {
        return (Request)getBean("Request");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected Session getSession() {
        return (Session)getBean("Session");
    }
}
