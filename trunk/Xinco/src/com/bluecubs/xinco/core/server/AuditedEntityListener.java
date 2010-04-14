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

    @PrePersist
    public void persistEntity(Object o) throws Exception {
        if (!XincoDBManager.isLocked()) {
            Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Pre-persist: " + o);
            if (o instanceof AuditedObject) {
                if (((AuditedObject) o).getModificationReason() == null || ((AuditedObject) o).getModificationReason().isEmpty()) {
                    ((AuditedObject) o).setModificationReason("audit.general.create");
                }
                auditTrail(o);
                XincoCoreUserModifiedRecord mod = new XincoCoreUserModifiedRecord(new XincoCoreUserModifiedRecordPK(
                        modifier.getId(), record_ID));
                mod.setModReason(((AuditedObject) o).getModificationReason());
                mod.setXincoCoreUser(modifier);
                mod.setModTime(new Date());
                new XincoCoreUserModifiedRecordJpaController().create(mod);
                Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Done!");
            }
        }
    }

    @PreRemove
    public void removeEntity(Object o) throws Exception {
        if (!XincoDBManager.isLocked()) {
            Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Pre-remove: " + o);
            if (o instanceof AuditedObject) {
                if (((AuditedObject) o).getModificationReason() == null || ((AuditedObject) o).getModificationReason().isEmpty()) {
                    ((AuditedObject) o).setModificationReason("audit.general.delete");
                }
                auditTrail(o);
                XincoCoreUserModifiedRecord mod = new XincoCoreUserModifiedRecord(new XincoCoreUserModifiedRecordPK(
                        modifier.getId(), record_ID));
                mod.setModReason(((AuditedObject) o).getModificationReason());
                mod.setXincoCoreUser(modifier);
                mod.setModTime(new Date());
                new XincoCoreUserModifiedRecordJpaController().create(mod);
                Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Done!");
            }
        }
    }

    @PreUpdate
    public void updateEntity(Object o) throws Exception {
        if (!XincoDBManager.isLocked()) {
            if (o instanceof AuditedObject) {
                Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Pre-update: " + o);
                if (((AuditedObject) o).getModificationReason() == null || ((AuditedObject) o).getModificationReason().isEmpty()) {
                    ((AuditedObject) o).setModificationReason("audit.general.modified");
                }
                auditTrail(o);
                XincoCoreUserModifiedRecord mod = new XincoCoreUserModifiedRecord(new XincoCoreUserModifiedRecordPK(
                        modifier.getId(), record_ID));
                mod.setModReason(((AuditedObject) o).getModificationReason());
                mod.setXincoCoreUser(modifier);
                mod.setModTime(new Date());
                new XincoCoreUserModifiedRecordJpaController().create(mod);
                Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Done!");
            }
        }
    }

    private void auditTrail(Object o) throws Exception {
        try {
            if (!XincoDBManager.isLocked()) {
                if (o instanceof AuditedObject) {
                    Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Creating audit trail for " + o);
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
                                node.getXincoCoreNodeId() == null ? 0 : node.getXincoCoreNodeId().getId(),
                                node.getXincoCoreLanguageId() == null ? 1 : node.getXincoCoreLanguageId().getId(),
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
                                data.getXincoCoreNodeId().getId(),
                                data.getXincoCoreLanguageId().getId(),
                                data.getXincoCoreDataTypeId().getId(),
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
                    } else if (!updated) {
                        throw new XincoException(o + " is an Auditable Object but it's processing logic is not implemented yet!");
                    }
                }
            }
        } catch (PreexistingEntityException e) {
            //Probably trying to reuse the recordID (this can happen in nested)
            e.printStackTrace();
            throw new XincoException(e.getLocalizedMessage());
        }
    }
}
