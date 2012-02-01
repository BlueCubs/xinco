package com.bluecubs.xinco.workflow;

import javax.persistence.PersistenceException;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowManager {

    private static XincoWorkflowManager instance = null;

    private XincoWorkflowManager() {
    }

    public static XincoWorkflowManager get() throws PersistenceException {
        if (instance == null) {
            instance = new XincoWorkflowManager();
        }
        return instance;
    }
}
