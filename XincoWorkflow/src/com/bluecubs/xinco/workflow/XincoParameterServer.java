package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoParameter;
import com.bluecubs.xinco.workflow.persistence.XincoParameterPK;
import com.bluecubs.xinco.workflow.persistence.controller.XincoActionJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.XincoParameterJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoParameterServer extends XincoParameter {

    private static final long serialVersionUID = 1L;

    public XincoParameterServer() {
    }

    public XincoParameterServer(XincoParameterPK xincoParameterPK, String name, String valueType, String value) {
        super(xincoParameterPK, name, valueType, value);
    }

    public XincoParameterServer(int id, int action_id) {
        super();
        if (id > 0 && action_id > 0) {
            //Read from database
            XincoParameter xp = new XincoParameterJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoParameter(new XincoParameterPK(action_id));
            setXincoParameterPK(xp.getXincoParameterPK());
            setName(xp.getName());
            setValue(xp.getValue());
            setValueType(xp.getValueType());
            setXincoAction(new XincoActionJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoAction(xp.getXincoParameterPK().getXincoActionId()));
        } else if (id == 0 && action_id > 0) {
            setXincoParameterPK(new XincoParameterPK(action_id));
        }
    }

    public XincoParameterServer(XincoParameter xp) {
        setXincoParameterPK(xp.getXincoParameterPK());
        setName(xp.getName());
        setValue(xp.getValue());
        setValueType(xp.getValueType());
        setXincoAction(new XincoActionJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoAction(xp.getXincoParameterPK().getXincoActionId()));
    }

    public XincoParameterPK write2DB() throws XincoWorkflowException {
        XincoParameter xp = null;
        try {
            if (getXincoParameterPK() != null && getXincoParameterPK().getId() != 0
                    && getXincoParameterPK().getXincoActionId() != 0) {
                xp = new XincoParameterJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoParameter(getXincoParameterPK());
                setXincoParameterPK(xp.getXincoParameterPK());
                xp.setName(getName());
                xp.setValue(getValue());
                xp.setValueType(getValueType());
                xp.setXincoAction(new XincoActionJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoAction(getXincoParameterPK().getXincoActionId()));
                new XincoParameterJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).edit(xp);
            } else {
                xp = new XincoParameter();
                xp.setName(getName());
                xp.setValue(getValue());
                xp.setValueType(getValueType());
                xp.setXincoAction(new XincoActionJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoAction(getXincoParameterPK().getXincoActionId()));
                new XincoParameterJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).create(xp);
            }
            return xp.getXincoParameterPK();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoParameterServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getMessages());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoParameterServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(XincoParameterServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
    }

    public static int removeFromDB(XincoParameter xp) throws XincoWorkflowException {
        //Check if action is being used somewhere else
        xp = new XincoParameterJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoParameter(xp.getXincoParameterPK());
        try {
            new XincoParameterJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).destroy(xp.getXincoParameterPK());
            XincoWorkflowDBManager.deleteEntity(xp);
        } catch (com.bluecubs.xinco.workflow.persistence.controller.exceptions.NonexistentEntityException ex) {
            Logger.getLogger(XincoParameterServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new XincoWorkflowException(ex.getLocalizedMessage());
        }
        return 0;
    }
}
