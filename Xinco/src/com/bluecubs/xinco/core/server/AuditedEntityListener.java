package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.*;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class AuditedEntityListener {

    private int record_ID;
    private XincoCoreUser modifier;
    private static boolean enabled = true;
    private AuditedObject last;

    @PrePersist
    public void persistEntity(Object o) throws Exception {
        if (!XincoDBManager.isLocked()) {
            Logger.getLogger(AuditedEntityListener.class.getSimpleName()).log(Level.FINEST, "Pre-persist: {0}", o);
            if (o instanceof AuditedObject && isEnabled()) {
                if (((AuditedObject) o).getModificationReason() == null || ((AuditedObject) o).getModificationReason().isEmpty()) {
                    ((AuditedObject) o).setModificationReason("audit.general.create");
                }
                performAuditTrail(o);
            }
        }
    }

    @PreRemove
    public void removeEntity(Object o) throws Exception {
        if (!XincoDBManager.isLocked()) {
            Logger.getLogger(AuditedEntityListener.class.getSimpleName()).log(Level.FINEST, "Pre-remove: {0}", o);
            if (o instanceof AuditedObject) {
                if (((AuditedObject) o).getModificationReason() == null || ((AuditedObject) o).getModificationReason().isEmpty()) {
                    ((AuditedObject) o).setModificationReason("audit.general.delete");
                }
                performAuditTrail(o);
            }
        }
    }

    @PreUpdate
    public void updateEntity(Object o) throws Exception {
        if (!XincoDBManager.isLocked()) {
            if (o instanceof AuditedObject) {
                Logger.getLogger(AuditedEntityListener.class.getSimpleName()).log(Level.FINEST, "Pre-update: {0}", o);
                if (((AuditedObject) o).getModificationReason() == null || ((AuditedObject) o).getModificationReason().isEmpty()) {
                    ((AuditedObject) o).setModificationReason("audit.general.modified");
                }
                performAuditTrail(o);
            }
        }
    }

    private void performAuditTrail(Object o) throws Exception {
        auditTrail(o);
        if (modifier == null) {
            //Default to admin
            modifier = new XincoCoreUserJpaController().findXincoCoreUser(1);
        }
        XincoCoreUserModifiedRecord mod = new XincoCoreUserModifiedRecord(new XincoCoreUserModifiedRecordPK(
                modifier.getId(), record_ID));
        mod.setModReason(((AuditedObject) o).getModificationReason());
        mod.setXincoCoreUser(modifier);
        mod.setModTime(new Date());
        new XincoCoreUserModifiedRecordJpaController().create(mod);
        Logger.getLogger(AuditedEntityListener.class.getSimpleName()).log(Level.FINEST, "Done!");
    }

    private void auditTrail(Object o) throws Exception {
        try {
            if (!XincoDBManager.isLocked()) {
                if (o instanceof AuditedObject) {
                    if (last != null && last.equals(o)) {
                        setEnabled(false);
                    }
                    if (isEnabled()) {
                        last = (AuditedObject) o;
                        Logger.getLogger(AuditedEntityListener.class.getSimpleName()).log(Level.FINEST, "Creating audit trail for {0}", o);
                        modifier = new XincoCoreUserJpaController().findXincoCoreUser(((AuditedObject) o).getModifierId());
                        boolean updated = false;
                        if (modifier == null) {
                            //Default to admin
                            modifier = new XincoCoreUserJpaController().findXincoCoreUser(1);
                        }
                        if (o instanceof XincoCoreUser) {
                            XincoCoreUser user = (XincoCoreUser) o;
                            XincoCoreUserT temp = new XincoCoreUserT(user.getId(), user.getUsername(), user.getUserpassword(),
                                    user.getName(), user.getFirstname(), user.getEmail(), user.getStatusNumber(),
                                    user.getAttempts(), new Timestamp(user.getLastModified().getTime()));
                            new XincoCoreUserTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoCoreNode) {
                            XincoCoreNode node = (XincoCoreNode) o;
                            XincoCoreNodeT temp = new XincoCoreNodeT(node.getId(),
                                    node.getXincoCoreNodeList().isEmpty() ? 0 : node.getXincoCoreNodeList().get(0).getId(),
                                    node.getXincoCoreLanguage() == null ? 1 : node.getXincoCoreLanguage().getId(),
                                    node.getDesignation(), node.getStatusNumber());
                            new XincoCoreNodeTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoCoreLanguage) {
                            XincoCoreLanguage lang = (XincoCoreLanguage) o;
                            XincoCoreLanguageT temp = new XincoCoreLanguageT(lang.getId(),
                                    lang.getSign(), lang.getDesignation());
                            new XincoCoreLanguageTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoCoreGroup) {
                            XincoCoreGroup group = (XincoCoreGroup) o;
                            XincoCoreGroupT temp = new XincoCoreGroupT(group.getId(),
                                    group.getDesignation(), group.getStatusNumber());
                            new XincoCoreGroupTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoCoreDataType) {
                            XincoCoreDataType group = (XincoCoreDataType) o;
                            XincoCoreDataTypeT temp = new XincoCoreDataTypeT(group.getId(),
                                    group.getDesignation(), group.getDescription());
                            new XincoCoreDataTypeTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoCoreDataTypeAttribute) {
                            XincoCoreDataTypeAttribute attr = (XincoCoreDataTypeAttribute) o;
                            XincoCoreDataTypeAttributeT temp = new XincoCoreDataTypeAttributeT(
                                    attr.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId(),
                                    attr.getXincoCoreDataTypeAttributePK().getAttributeId(),
                                    attr.getDesignation(), attr.getDataType(), attr.getAttrSize());
                            new XincoCoreDataTypeAttributeTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoCoreData) {
                            XincoCoreData data = (XincoCoreData) o;
                            XincoCoreDataT temp = new XincoCoreDataT(
                                    data.getId(),
                                    data.getXincoCoreNode().getId(),
                                    data.getXincoCoreLanguage().getId(),
                                    data.getXincoCoreDataType().getId(),
                                    data.getDesignation(), data.getStatusNumber());
                            new XincoCoreDataTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoCoreAce) {
                            XincoCoreAce ace = (XincoCoreAce) o;
                            XincoCoreAceT temp = new XincoCoreAceT(
                                    ace.getId(),
                                    ace.getReadPermission(),
                                    ace.getWritePermission(),
                                    ace.getExecutePermission(),
                                    ace.getAdminPermission());
                            new XincoCoreAceTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoAddAttribute) {
                            XincoAddAttribute attr = (XincoAddAttribute) o;
                            XincoAddAttributeT temp = new XincoAddAttributeT(
                                    attr.getXincoAddAttributePK().getAttributeId(),
                                    attr.getXincoAddAttributePK().getAttributeId(),
                                    attr.getAttribInt(),
                                    attr.getAttribUnsignedint(),
                                    attr.getAttribDouble(),
                                    attr.getAttribVarchar(),
                                    attr.getAttribText(),
                                    attr.getAttribDatetime());
                            new XincoAddAttributeTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoSetting) {
                            XincoSetting setting = (XincoSetting) o;
                            XincoSettingT temp = new XincoSettingT(
                                    setting.getId(),
                                    setting.getDescription());
                            temp.setBoolValue(setting.getBoolValue());
                            temp.setIntValue(setting.getIntValue());
                            temp.setLongValue(setting.getLongValue());
                            temp.setStringValue(setting.getStringValue());
                            new XincoSettingTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        }
                        if (o instanceof XincoDependencyType) {
                            XincoDependencyType dep = (XincoDependencyType) o;
                            XincoDependencyTypeT temp = new XincoDependencyTypeT(
                                    dep.getId(),
                                    dep.getXincoDependencyBehavior().getId(),
                                    dep.getDesignation());
                            if (dep.getDescription() != null && !dep.getDescription().isEmpty()) {
                                temp.setDescription(dep.getDescription());
                            }
                            new XincoDependencyTypeTJpaController().create(temp);
                            record_ID = temp.getRecordId();
                            updated = true;
                        } else if (!updated) {
                            throw new XincoException(o + " is an Auditable Object but it's processing logic is not implemented yet!");
                        }
                    }

                }


            }
        } catch (PreexistingEntityException e) {
            //Probably trying to reuse the recordID (this can happen in nested)
            throw new XincoException(e.getLocalizedMessage());


        }
    }

    /**
     * @return the enabled
     */
    public static boolean isEnabled() {
        return enabled;


    }

    /**
     * @param e the enabled to set
     */
    public static void setEnabled(boolean e) {
        enabled = e;

    }
}
