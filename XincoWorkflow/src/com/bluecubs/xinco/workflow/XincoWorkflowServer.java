package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoWorkflow;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowPK;
import com.bluecubs.xinco.workflow.persistence.controller.UserLinkJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.XincoWorkflowJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowServer extends XincoWorkflow {

    private static final long serialVersionUID = 1L;

    public XincoWorkflowServer() {

    }

    public XincoWorkflowServer(XincoWorkflowPK XincoWorkflowPK, String name) {
        super(XincoWorkflowPK, name);
    }

    public XincoWorkflowServer(int id, int version, int userLinkId, String name) {
        super();
        if (id > 0 && version > 0 && userLinkId > 0) {
            //Read from database
            XincoWorkflow xw = new XincoWorkflowJpaController().findXincoWorkflow(new XincoWorkflowPK(version, userLinkId));
            setXincoWorkflowPK(xw.getXincoWorkflowPK());
            setName(xw.getName());
            setDescription(xw.getDescription());
            setUserLink(new UserLinkJpaController().findUserLink(getXincoWorkflowPK().getUserLinkId()));
            setXincoWorkflowStateList(xw.getXincoWorkflowStateList());
        } else if (id == 0 && version > 0 && userLinkId > 0) {
            setXincoWorkflowPK(new XincoWorkflowPK(version, userLinkId));
        }
        setName(name);
    }

    public XincoWorkflowServer(XincoWorkflow xw) {
        setXincoWorkflowPK(xw.getXincoWorkflowPK());
        setName(xw.getName());
        setDescription(xw.getDescription());
        setUserLink(new UserLinkJpaController().findUserLink(getXincoWorkflowPK().getUserLinkId()));
        setXincoWorkflowStateList(xw.getXincoWorkflowStateList());
    }

    public XincoWorkflowPK write2DB() throws XincoWorkflowException {
        XincoWorkflow xw = null;
        try {
            if (getXincoWorkflowPK() != null && getXincoWorkflowPK().getId() != 0
                    && getXincoWorkflowPK().getUserLinkId() != 0) {
                xw = new XincoWorkflowJpaController().findXincoWorkflow(getXincoWorkflowPK());
                xw.setXincoWorkflowPK(getXincoWorkflowPK());
                xw.setName(getName());
                xw.setDescription(getDescription());
                xw.setUserLink(new UserLinkJpaController().findUserLink(getXincoWorkflowPK().getUserLinkId()));
                xw.setXincoWorkflowStateList(getXincoWorkflowStateList());
                new XincoWorkflowJpaController().edit(xw);
            } else {
                xw = new XincoWorkflow();
                xw.setXincoWorkflowPK(getXincoWorkflowPK());
                xw.setName(getName());
                xw.setDescription(getDescription());
                xw.setUserLink(new UserLinkJpaController().findUserLink(getXincoWorkflowPK().getUserLinkId()));
                xw.setXincoWorkflowStateList(getXincoWorkflowStateList());
                new XincoWorkflowJpaController().create(xw);
            }
            return xw.getXincoWorkflowPK();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoWorkflowServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getMessages());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoWorkflowServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(XincoWorkflowServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
    }

    public static int removeFromDB(XincoWorkflow xp) throws XincoWorkflowException {
        //Check if action is being used somewhere else
        xp = new XincoWorkflowJpaController().findXincoWorkflow(xp.getXincoWorkflowPK());
        try {
            new XincoWorkflowJpaController().destroy(xp.getXincoWorkflowPK());
            XincoWorkflowDBManager.deleteEntity(xp);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoWorkflowServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException ex) {
            Logger.getLogger(XincoWorkflowServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
        return 0;
    }
}
