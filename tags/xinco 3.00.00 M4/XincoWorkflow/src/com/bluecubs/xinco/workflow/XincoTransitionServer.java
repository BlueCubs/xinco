package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoState;
import com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoStatePK;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowState;
import com.bluecubs.xinco.workflow.persistence.controller.XincoStateTransitionsToXincoStateJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoTransitionServer extends XincoStateTransitionsToXincoState {

    private static final long serialVersionUID = 1L;

    public XincoTransitionServer(XincoWorkflowState source, XincoWorkflowState dest) {
        super(source.getXincoWorkflowStatePK().getId(),
                source.getXincoWorkflowStatePK().getXincoWorkflowId(),
                source.getXincoWorkflowStatePK().getXincoWorkflowVersion(),
                dest.getXincoWorkflowStatePK().getId(),
                dest.getXincoWorkflowStatePK().getXincoWorkflowId(),
                dest.getXincoWorkflowStatePK().getXincoWorkflowVersion());
    }

    public XincoTransitionServer(XincoStateTransitionsToXincoStatePK xincoStateTransitionsToXincoStatePK) {
        super(xincoStateTransitionsToXincoStatePK);
    }

    public XincoTransitionServer(XincoStateTransitionsToXincoState x) {
        setXincoStateTransitionsToXincoStatePK(x.getXincoStateTransitionsToXincoStatePK());
        setSourceXincoWorkflowState(x.getSourceXincoWorkflowState());
        setDestXincoWorkflowState(x.getDestXincoWorkflowState());
        setUserLinkList(x.getUserLinkList());
        setXincoActionList(x.getXincoActionList());
    }

    public XincoStateTransitionsToXincoStatePK write2DB() throws XincoWorkflowException {
        XincoStateTransitionsToXincoState x = null;
        try {
            if (getXincoStateTransitionsToXincoStatePK() != null) {
                x = new XincoStateTransitionsToXincoStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoStateTransitionsToXincoState(getXincoStateTransitionsToXincoStatePK());
                setXincoStateTransitionsToXincoStatePK(x.getXincoStateTransitionsToXincoStatePK());
                x.setSourceXincoWorkflowState(getSourceXincoWorkflowState());
                x.setDestXincoWorkflowState(getDestXincoWorkflowState());
                x.setUserLinkList(getUserLinkList());
                x.setXincoActionList(getXincoActionList());
                new XincoStateTransitionsToXincoStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).edit(x);
            } else {
                x = new XincoStateTransitionsToXincoState();
                x.setXincoStateTransitionsToXincoStatePK(getXincoStateTransitionsToXincoStatePK());
                x.setSourceXincoWorkflowState(getSourceXincoWorkflowState());
                x.setDestXincoWorkflowState(getDestXincoWorkflowState());
                x.setUserLinkList(getUserLinkList());
                x.setXincoActionList(getXincoActionList());
                new XincoStateTransitionsToXincoStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).create(x);
            }
            return x.getXincoStateTransitionsToXincoStatePK();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoTransitionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getMessages());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoTransitionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(XincoTransitionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
    }

    public static int removeFromDB(XincoStateTransitionsToXincoState x) throws XincoWorkflowException {
        try {
            //Check if action is being used somewhere else
            x = new XincoStateTransitionsToXincoStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoStateTransitionsToXincoState(x.getXincoStateTransitionsToXincoStatePK());
            new XincoStateTransitionsToXincoStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).destroy(x.getXincoStateTransitionsToXincoStatePK());
            XincoWorkflowDBManager.deleteEntity(x);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoTransitionServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
        return 0;
    }
}
