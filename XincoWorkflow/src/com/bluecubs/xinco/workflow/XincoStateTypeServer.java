package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoStateType;
import com.bluecubs.xinco.workflow.persistence.controller.XincoStateTypeJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoStateTypeServer extends XincoStateType {

    private static final long serialVersionUID = 1L;

    public XincoStateTypeServer(String name) {
        super(name);
    }

    public XincoStateTypeServer() {
    }

    public XincoStateTypeServer(int id) {
        super();
        if (id > 0) {
            //Read from database
            XincoStateType xst = new XincoStateTypeJpaController().findXincoStateType(getId());
            setId(xst.getId());
            setDescription(xst.getDescription());
            setName(xst.getName());
            setXincoWorkflowStateList(xst.getXincoWorkflowStateList());
        }
    }

    public XincoStateTypeServer(XincoStateType xst) {
        setId(xst.getId());
        setDescription(xst.getDescription());
        setName(xst.getName());
        setXincoWorkflowStateList(xst.getXincoWorkflowStateList());
    }

    public int write2DB() throws XincoWorkflowException {
        XincoStateType xst = null;
        try {
            if (getId() != null) {
                xst = new XincoStateTypeJpaController().findXincoStateType(getId());
                setId(xst.getId());
                xst.setDescription(getDescription());
                xst.setName(getName());
                xst.setXincoWorkflowStateList(getXincoWorkflowStateList());
                new XincoStateTypeJpaController().edit(xst);
            } else {
                xst = new XincoStateType();
                xst.setDescription(getDescription());
                xst.setName(getName());
                xst.setXincoWorkflowStateList(getXincoWorkflowStateList());
                new XincoStateTypeJpaController().create(xst);
            }
            return xst.getId();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoStateTypeServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getMessages());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoStateTypeServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(XincoStateTypeServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
    }

    public static int removeFromDB(XincoStateType xst) throws XincoWorkflowException {
        //Check if action is being used somewhere else
        xst = new XincoStateTypeJpaController().findXincoStateType(xst.getId());
        if (xst.getXincoWorkflowStateList() == null
                || xst.getXincoWorkflowStateList().isEmpty()) {
            try {
                new XincoStateTypeJpaController().destroy(xst.getId());
                XincoWorkflowDBManager.deleteEntity(xst);
            } catch (com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException ex) {
                Logger.getLogger(XincoStateTypeServer.class.getName()).log(Level.SEVERE, null, ex);
                throw new XincoWorkflowException(ex.getMessages());
            } catch (com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException ex) {
                Logger.getLogger(XincoStateTypeServer.class.getName()).log(Level.SEVERE, null, ex);
                throw new XincoWorkflowException(ex.getLocalizedMessage());
            }
        } else {
            throw new XincoWorkflowException("XincoState " + xst + " is being used and can't be deleted.");
        }
        return 0;
    }
}
