package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoWorkflow;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowState;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowStatePK;
import com.bluecubs.xinco.workflow.persistence.controller.XincoWorkflowStateJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowStateServer extends XincoWorkflowState {

    private static final long serialVersionUID = 1L;

    public XincoWorkflowStateServer(int xincoWorkflowId, int xincoWorkflowVersion, String name,
            XincoWorkflow wf) {
        super(xincoWorkflowId, xincoWorkflowVersion, name);
        setXincoWorkflow(wf);
    }

    public XincoWorkflowStateServer(XincoWorkflowStatePK xincoWorkflowStatePK) {
        super(xincoWorkflowStatePK);
        XincoWorkflowState s = new XincoWorkflowStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflowState(xincoWorkflowStatePK);
        setName(s.getName());
        setUserLinkList(s.getUserLinkList());
        setSourceXincoStateTransitionsToXincoStateList(s.getSourceXincoStateTransitionsToXincoStateList());
        setDestXincoStateTransitionsToXincoStateList(s.getDestXincoStateTransitionsToXincoStateList());
        setXincoActionList(s.getXincoActionList());
        setXincoStateTypeId(s.getXincoStateTypeId());
        setXincoWorkItemHasXincoStateList(s.getXincoWorkItemHasXincoStateList());
        setXincoWorkflow(s.getXincoWorkflow());
    }

    public XincoWorkflowStatePK write2DB() throws XincoWorkflowException {
        XincoWorkflowState s = null;
        try {
            if (getXincoWorkflowStatePK() != null && getXincoWorkflowStatePK().getId() != 0) {
                s = new XincoWorkflowStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflowState(getXincoWorkflowStatePK());
                s.setName(getName());
                s.setUserLinkList(getUserLinkList());
                s.setSourceXincoStateTransitionsToXincoStateList(getSourceXincoStateTransitionsToXincoStateList());
                s.setDestXincoStateTransitionsToXincoStateList(getDestXincoStateTransitionsToXincoStateList());
                s.setXincoActionList(getXincoActionList());
                s.setXincoStateTypeId(getXincoStateTypeId());
                s.setXincoWorkItemHasXincoStateList(getXincoWorkItemHasXincoStateList());
                s.setXincoWorkflow(getXincoWorkflow());
                new XincoWorkflowStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).edit(s);
            } else {
                s = new XincoWorkflowState();
                s.setName(getName());
                s.setUserLinkList(getUserLinkList());
                s.setSourceXincoStateTransitionsToXincoStateList(getSourceXincoStateTransitionsToXincoStateList());
                s.setDestXincoStateTransitionsToXincoStateList(getDestXincoStateTransitionsToXincoStateList());
                s.setXincoActionList(getXincoActionList());
                s.setXincoStateTypeId(getXincoStateTypeId());
                s.setXincoWorkItemHasXincoStateList(getXincoWorkItemHasXincoStateList());
                s.setXincoWorkflow(getXincoWorkflow());
                new XincoWorkflowStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).create(s);
            }
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoWorkflowStateServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoWorkflowStateServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XincoWorkflowStateServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s.getXincoWorkflowStatePK();
    }

    public static int removeFromDB(XincoWorkflowState s) throws XincoWorkflowException {
        //Check if action is being used somewhere else
        s = new XincoWorkflowStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflowState(s.getXincoWorkflowStatePK());
        try {
            new XincoWorkflowStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).destroy(s.getXincoWorkflowStatePK());
            XincoWorkflowDBManager.deleteEntity(s);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException ex) {
            Logger.getLogger(XincoActionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
        return 0;
    }
}
