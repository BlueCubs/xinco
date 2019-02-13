package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.UserLink;
import com.bluecubs.xinco.workflow.persistence.controller.UserLinkJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoUserLinkServer extends UserLink {

    private static final long serialVersionUID = 5584044017037257878L;

    public XincoUserLinkServer(int id, String system_id) {
        super(system_id);
        if (id > 0) {
            setId(id);
            //Read from database
            UserLink user = new UserLinkJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findUserLink(getId());
            setSystemId(user.getSystemId());
            setId(user.getId());
        }
    }

    public XincoUserLinkServer(UserLink user) {
        setSystemId(user.getSystemId());
        setId(user.getId());
    }

    public int write2DB() throws XincoWorkflowException {
        UserLink user = null;
        try {
            if (getId() != null) {
                user = new UserLinkJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findUserLink(getId());
                user.setSystemId(getSystemId());
                user.setId(getId());
                new UserLinkJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).edit(user);
            } else {
                user = new UserLink();
                user.setSystemId(getSystemId());
                new UserLinkJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).create(user);
            }
            return user.getId();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoUserLinkServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getMessages());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoUserLinkServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(XincoUserLinkServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
    }

    public static int removeFromDB(UserLink user) throws XincoWorkflowException {
        //Check if action is being used somewhere else
        user = new UserLinkJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findUserLink(user.getId());
        try {
            new UserLinkJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).destroy(user.getId());
            XincoWorkflowDBManager.deleteEntity(user);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoUserLinkServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException ex) {
            Logger.getLogger(XincoUserLinkServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
        return 0;
    }
}
