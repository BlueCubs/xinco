package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoAction;
import com.bluecubs.xinco.workflow.persistence.controller.XincoActionJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
public class XincoActionServer extends XincoAction {

    private static final long serialVersionUID = 5584044017037257878L;

    public XincoActionServer(String name) {
        super(name);
    }

    public XincoActionServer(String name, String implementationClass) {
        super(name);
        setImplementationClass(implementationClass);
    }

    public XincoActionServer(int id) {
        super();
        if (id > 0) {
            //Read from database
            XincoAction action = new XincoActionJpaController().findXincoAction(getId());
            setImplementationClass(action.getImplementationClass());
            setName(action.getName());
        }
    }

    XincoActionServer(XincoAction action) {
        setImplementationClass(action.getImplementationClass());
        setName(action.getName());
    }

    public int write2DB() throws XincoWorkflowException {
        XincoAction action = null;
        try {
            if (getId() != null) {
                action = new XincoActionJpaController().findXincoAction(getId());
                action.setImplementationClass(getImplementationClass());
                action.setName(getName());
                new XincoActionJpaController().edit(action);
            } else {
                action = new XincoAction();
                action.setImplementationClass(getImplementationClass());
                action.setName(getName());
                new XincoActionJpaController().create(action);
            }
            return action.getId();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getMessages());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
    }

    public static int removeFromDB(XincoAction action) throws XincoWorkflowException {
        //Check if action is being used somewhere else
        action = new XincoActionJpaController().findXincoAction(action.getId());
        try {
            new XincoActionJpaController().destroy(action.getId());
            XincoWorkflowDBManager.deleteEntity(action);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
        return 0;
    }
}
