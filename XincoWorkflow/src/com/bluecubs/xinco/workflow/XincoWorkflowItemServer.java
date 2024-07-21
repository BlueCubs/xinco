package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoWorkflowItem;
import com.bluecubs.xinco.workflow.persistence.controller.XincoWorkflowItemJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowItemServer extends XincoWorkflowItem {

    private static final long serialVersionUID = 1L;

    public XincoWorkflowItemServer(Date creationDate) {
        super(creationDate);
    }

    public XincoWorkflowItemServer() {
    }

    public XincoWorkflowItemServer(int id) {
        super();
        if (id > 0) {
            //Read from database
            XincoWorkflowItem xwi = new XincoWorkflowItemJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflowItem(getId());
            setId(xwi.getId());
            setCompletionDate(xwi.getCompletionDate());
            setCreationDate(xwi.getCreationDate());
            setXincoWorkItemHasXincoStateList(xwi.getXincoWorkItemHasXincoStateList());
        }
    }

    public XincoWorkflowItemServer(XincoWorkflowItem xwi) {
        setId(xwi.getId());
        setCompletionDate(xwi.getCompletionDate());
        setCreationDate(xwi.getCreationDate());
        setXincoWorkItemHasXincoStateList(xwi.getXincoWorkItemHasXincoStateList());
    }

    public int write2DB() throws XincoWorkflowException {
        XincoWorkflowItem xwi = null;
        try {
            if (getId() != null) {
                xwi = new XincoWorkflowItemJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflowItem(getId());
                setId(xwi.getId());
                xwi.setCompletionDate(getCompletionDate());
                xwi.setCreationDate(getCreationDate());
                xwi.setXincoWorkItemHasXincoStateList(getXincoWorkItemHasXincoStateList());
                new XincoWorkflowItemJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).edit(xwi);
            } else {
                xwi = new XincoWorkflowItem();
                xwi.setCompletionDate(getCompletionDate());
                xwi.setCreationDate(getCreationDate());
                xwi.setXincoWorkItemHasXincoStateList(getXincoWorkItemHasXincoStateList());
                new XincoWorkflowItemJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).create(xwi);
            }
            return xwi.getId();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoWorkflowItemServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getMessages());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoWorkflowItemServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(XincoWorkflowItemServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
    }

    public static int removeFromDB(XincoWorkflowItem xwi) throws XincoWorkflowException {
        //Check if action is being used somewhere else
        xwi = new XincoWorkflowItemJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflowItem(xwi.getId());
        if (xwi.getXincoWorkItemHasXincoStateList() == null
                || xwi.getXincoWorkItemHasXincoStateList().isEmpty()) {
            try {
                new XincoWorkflowItemJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).destroy(xwi.getId());
                XincoWorkflowDBManager.deleteEntity(xwi);
            } catch (com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException ex) {
                Logger.getLogger(XincoWorkflowItemServer.class.getName()).log(Level.SEVERE, null, ex);
                throw new XincoWorkflowException(ex.getMessages());
            } catch (com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException ex) {
                Logger.getLogger(XincoWorkflowItemServer.class.getName()).log(Level.SEVERE, null, ex);
                throw new XincoWorkflowException(ex.getLocalizedMessage());
            }
        } else {
            throw new XincoWorkflowException("XincoWorkflowItem " + xwi + " is being used and can't be deleted.");
        }
        return 0;
    }
}
