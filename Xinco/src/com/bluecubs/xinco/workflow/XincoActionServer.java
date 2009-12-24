package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.XincoException;
import com.bluecubs.xinco.workflow.db.XincoAction;
import com.bluecubs.xinco.workflow.db.XincoParameter;
import com.bluecubs.xinco.workflow.db.XincoStateTransitionsToXincoState;
import com.bluecubs.xinco.workflow.db.XincoWorkflowState;
import com.bluecubs.xinco.workflow.db.controller.XincoActionJpaController;
import com.bluecubs.xinco.workflow.db.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.db.controller.exceptions.NonexistentEntityException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoActionServer extends XincoAction {

    public XincoActionServer(int id, String name) {
        super(id, name);
    }

    public XincoActionServer(int id, String name, String implementationClass) {
        super(id, name);
        setImplementationClass(implementationClass);
        setXincoParameterList(new Vector<XincoParameter>());
        setXincoStateTransitionsToXincoStateList(new Vector<XincoStateTransitionsToXincoState>());
        setXincoWorkflowStateList(new Vector<XincoWorkflowState>());
    }

    public XincoActionServer(int id) {
        super(id);
        setXincoParameterList(new Vector<XincoParameter>());
        setXincoStateTransitionsToXincoStateList(new Vector<XincoStateTransitionsToXincoState>());
        setXincoWorkflowStateList(new Vector<XincoWorkflowState>());
        if (id > 0) {
            //Read from database
            XincoAction action = new XincoActionJpaController().findXincoAction(getId());
            setImplementationClass(action.getImplementationClass());
            setName(action.getName());
            setXincoParameterList(action.getXincoParameterList());
            setXincoStateTransitionsToXincoStateList(action.getXincoStateTransitionsToXincoStateList());
            setXincoWorkflowStateList(action.getXincoWorkflowStateList());
        }
    }

    public int write2DB() throws XincoException {
        com.bluecubs.xinco.workflow.db.XincoAction action = null;
        try {
            if (getId() > 0) {
                action = new XincoActionJpaController().findXincoAction(getId());
                action.setImplementationClass(getImplementationClass());
                action.setName(getName());
                action.setXincoParameterList(this.getXincoParameterList());
                action.setXincoStateTransitionsToXincoStateList(getXincoStateTransitionsToXincoStateList());
                action.setXincoWorkflowStateList(getXincoWorkflowStateList());
                new XincoActionJpaController().edit(action);
            } else {
                setId(XincoDBManager.getNewID("xinco_action"));
                action = new com.bluecubs.xinco.workflow.db.XincoAction(getId());
                action.setId(getId());
                action.setImplementationClass(getImplementationClass());
                action.setName(getName());
                action.setXincoParameterList(this.getXincoParameterList());
                action.setXincoStateTransitionsToXincoStateList(getXincoStateTransitionsToXincoStateList());
                action.setXincoWorkflowStateList(getXincoWorkflowStateList());
                new XincoActionJpaController().create(action);
            }
            return getId();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex.getMessages());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex.getLocalizedMessage());
        }
    }

    public static int removeFromDB(XincoAction action) {
        //Check if action is being used somewhere else
        action = new XincoActionJpaController().findXincoAction(action.getId());
        if ((action.getXincoStateTransitionsToXincoStateList() == null
                && action.getXincoWorkflowStateList() == null)
                || (action.getXincoStateTransitionsToXincoStateList() != null
                && action.getXincoWorkflowStateList() != null
                && action.getXincoStateTransitionsToXincoStateList().size() == 0
                && action.getXincoWorkflowStateList().size() == 0)) {
            try {
                new XincoActionJpaController().destroy(action.getId());
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
                throw new XincoException(ex.getMessages());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
                throw new XincoException(ex.getLocalizedMessage());
            }
        } else {
            throw new XincoException("XincoAction " + action + " is being used and can't be deleted.");
        }
        return 0;
    }
}
