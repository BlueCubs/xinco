/*
 * Header.java
 *
 * Created on Mar 25, 2008, 4:10:09 PM
 */
 
package xincowebapp;

import com.sun.rave.web.ui.appbase.AbstractFragmentBean;
import com.sun.webui.jsf.model.DefaultOptionsList;
import javax.faces.FacesException;

/**
 * <p>Fragment bean that corresponds to a similarly named JSP page
 * fragment.  This class contains component definitions (and initialization
 * code) for all components that you have defined on this fragment, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class Header extends AbstractFragmentBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    /**
     * <p>Automatically managed component initialization. <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    private DefaultOptionsList menu1DefaultOptions = new DefaultOptionsList();

    public DefaultOptionsList getMenu1DefaultOptions() {
        return menu1DefaultOptions;
    }

    public void setMenu1DefaultOptions(DefaultOptionsList dol) {
        this.menu1DefaultOptions = dol;
    }
    private DefaultOptionsList menu2DefaultOptions = new DefaultOptionsList();

    public DefaultOptionsList getMenu2DefaultOptions() {
        return menu2DefaultOptions;
    }

    public void setMenu2DefaultOptions(DefaultOptionsList dol) {
        this.menu2DefaultOptions = dol;
    }
    private DefaultOptionsList menu1DefaultOptions1 = new DefaultOptionsList();

    public DefaultOptionsList getMenu1DefaultOptions1() {
        return menu1DefaultOptions1;
    }

    public void setMenu1DefaultOptions1(DefaultOptionsList dol) {
        this.menu1DefaultOptions1 = dol;
    }
    // </editor-fold>

    public Header() {
    }

    /**
     * <p>Callback method that is called whenever a page containing
     * this page fragment is navigated to, either directly via a URL,
     * or indirectly via page navigation.  Override this method to acquire
     * resources that will be needed for event handlers and lifecycle methods.</p>
     * 
     * <p>The default implementation does nothing.</p>
     */
    @Override
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
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called.  Override this
     * method to release resources acquired in the <code>init()</code>
     * resources that will be needed for event handlers and lifecycle methods.</p>
     * 
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected XincoSessionBean getXincoSessionBean() {
        return (XincoSessionBean) getBean("XincoSessionBean");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected XincoRequestBean getXincoRequestBean() {
        return (XincoRequestBean) getBean("XincoRequestBean");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected XincoApplicationBean getXincoApplicationBean() {
        return (XincoApplicationBean) getBean("XincoApplicationBean");
    }

}
