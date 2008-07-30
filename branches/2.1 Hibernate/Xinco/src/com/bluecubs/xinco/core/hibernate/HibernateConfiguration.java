/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.hibernate;

import java.util.Properties;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public abstract class HibernateConfiguration {

    private Properties props;
    
    /**
     * Get properties
     * @return Properties
     */
    public Properties getProperties() {
        return props;
    }

    /**
     * Set the properties
     * @param props Properties
     */
    public void setProperties(Properties props) {
        this.props = props;
    }
    
    /**
     * Get property value
     * @param name Property name
     * @return
     */
    public String get(String name){
        return getProperties().getProperty(name);
    }
    
    public abstract void loadProperties() throws Exception;
}
