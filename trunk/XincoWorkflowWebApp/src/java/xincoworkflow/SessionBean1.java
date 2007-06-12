/*
 * SessionBean1.java
 *
 * Created on June 12, 2007, 9:01 AM
 * Copyright Javier A. Ortiz
 */
package xincoworkflow;

import com.bluecubs.xinco.workflow.WorkflowSetting;
import com.bluecubs.xinco.workflow.server.WorkflowSettingServer;
import com.sun.rave.web.ui.appbase.AbstractSessionBean;
import com.sun.rave.web.ui.model.Option;
import java.util.Locale;
import java.util.ResourceBundle;
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
 */
public class SessionBean1 extends AbstractSessionBean {
    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">
    private int __placeholder;
    
    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }
    // </editor-fold>
    private int i = 0;
    private ResourceBundle lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
    private ResourceBundle rb=ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoWorkflowMessages");;
    private String[] locales;
    private String text = "";
    
    /**
     * <p>Construct a new session data bean instance.</p>
     */
    public SessionBean1() {
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
            throw e instanceof FacesException ? (FacesException) e: new FacesException(e);
        }
        
        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
        locales = lrb.getString("AvailableLocales").split(",");
        Option []options= new Option[locales.length];
        for (i=0;i<locales.length;i++)
            options[i]=new Option(locales[i], lrb.getString("Locale." + locales[i]));
        setLanguageOptions(options);
        WorkflowSettingServer xss= new WorkflowSettingServer();
        String version="[Version " + xss.getSetting("workflow.version.high").getInt_value()
        + "." + xss.getSetting("workflow.version.med").getInt_value() + "." +
                xss.getSetting("workflow.version.low").getInt_value();
        if(xss.getSetting("workflow.version.postfix").getString_value()!=null &&
                !xss.getSetting("workflow.version.postfix").getString_value().trim().equals(""))
            version+=" " + xss.getSetting("workflow.version.postfix").getString_value();
        version+="]";
        setFooterText(version);
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
    public void destroy() {
    }
    
    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1)getBean("ApplicationBean1");
    }
    
    static {
    }
    
    /**
     * Holds value of property languageOptions.
     */
    private Option[] languageOptions;
    
    /**
     * Getter for property languageOptions.
     * @return Value of property languageOptions.
     */
    public Option[] getLanguageOptions() {
        return this.languageOptions;
    }
    
    /**
     * Setter for property languageOptions.
     * @param languageOptions New value of property languageOptions.
     */
    public void setLanguageOptions(Option[] languageOptions) {
        this.languageOptions = languageOptions;
    }
    
    /**
     * Holds value of property footerText.
     */
    private String footerText;
    
    /**
     * Getter for property footerText.
     * @return Value of property footerText.
     */
    public String getFooterText() {
        return this.footerText;
    }
    
    /**
     * Setter for property footerText.
     * @param footerText New value of property footerText.
     */
    public void setFooterText(String footerText) {
    }
}
