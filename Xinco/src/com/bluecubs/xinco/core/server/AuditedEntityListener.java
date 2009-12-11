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
                    record_ID = XincoDBManager.getNewID("xinco_core_user_modified_record");
                    modifier = new XincoCoreUserJpaController().findXincoCoreUser(((AuditedObject) o).getModifierId());
                    boolean updated = false;
                    if (modifier == null) {
                        //Default to admin
                        modifier = new XincoCoreUserJpaController().findXincoCoreUser(1);
                    }
                    if (o instanceof XincoCoreUser) {
                        XincoCoreUser user = (XincoCoreUser) o;
                        new XincoCoreUserTJpaController().create(new XincoCoreUserT(record_ID, user.getId(), user.getUsername(), user.getUserpassword(),
                                user.getName(), user.getFirstname(), user.getEmail(), user.getStatusNumber(),
                                user.getAttempts(), new Timestamp(user.getLastModified().getTime())));
                        updated = true;
                    }
                    if (o instanceof XincoCoreNode) {
                        XincoCoreNode node = (XincoCoreNode) o;
                        new XincoCoreNodeTJpaController().create(new XincoCoreNodeT(record_ID, node.getId(),
                                node.getXincoCoreNodeId() == null ? 0 : node.getXincoCoreNodeId().getId(),
                                node.getXincoCoreLanguageId() == null ? 1 : node.getXincoCoreLanguageId().getId(),
                                node.getDesignation(), node.getStatusNumber()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreLanguage) {
                        XincoCoreLanguage lang = (XincoCoreLanguage) o;
                        new XincoCoreLanguageTJpaController().create(new XincoCoreLanguageT(record_ID, lang.getId(),
                                lang.getSign(), lang.getDesignation()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreGroup) {
                        XincoCoreGroup group = (XincoCoreGroup) o;
                        new XincoCoreGroupTJpaController().create(new XincoCoreGroupT(record_ID, group.getId(),
                                group.getDesignation(), group.getStatusNumber()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreDataType) {
                        XincoCoreDataType group = (XincoCoreDataType) o;
                        new XincoCoreDataTypeTJpaController().create(new XincoCoreDataTypeT(record_ID, group.getId(),
                                group.getDesignation(), group.getDescription()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreDataTypeAttribute) {
                        XincoCoreDataTypeAttribute attr = (XincoCoreDataTypeAttribute) o;
                        new XincoCoreDataTypeAttributeTJpaController().create(new XincoCoreDataTypeAttributeT(record_ID,
                                attr.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId(),
                                attr.getXincoCoreDataTypeAttributePK().getAttributeId(),
                                attr.getDesignation(), attr.getDataType(), attr.getAttrSize()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreData) {
                        XincoCoreData data = (XincoCoreData) o;
                        new XincoCoreDataTJpaController().create(new XincoCoreDataT(record_ID,
                                data.getId(),
                                data.getXincoCoreNodeId().getId(),
                                data.getXincoCoreLanguageId().getId(),
                                data.getXincoCoreDataTypeId().getId(),
                                data.getDesignation(), data.getStatusNumber()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreAce) {
                        XincoCoreAce ace = (XincoCoreAce) o;
                        new XincoCoreAceTJpaController().create(new XincoCoreAceT(record_ID,
                                ace.getId(),
                                ace.getReadPermission(),
                                ace.getWritePermission(),
                                ace.getExecutePermission(),
                                ace.getAdminPermission()));
                        updated = true;
                    }
                    if (o instanceof XincoAddAttribute) {
                        XincoAddAttribute attr = (XincoAddAttribute) o;
                        new XincoAddAttributeTJpaController().create(new XincoAddAttributeT(record_ID,
                                attr.getXincoAddAttributePK().getAttributeId(),
                                attr.getXincoAddAttributePK().getAttributeId(),
                                attr.getAttribInt(),
                                attr.getAttribUnsignedint(),
                                attr.getAttribDouble(),
                                attr.getAttribVarchar(),
                                attr.getAttribText(),
                                attr.getAttribDatetime()));
                        updated = true;
                    }
                    if (o instanceof XincoSetting) {
                        XincoSetting setting = (XincoSetting) o;
                        XincoSettingT temp = new XincoSettingT(record_ID,
                                setting.getId(),
                                setting.getDescription());
                        temp.setBoolValue(setting.getBoolValue());
                        temp.setIntValue(setting.getIntValue());
                        temp.setLongValue(setting.getLongValue());
                        temp.setStringValue(setting.getStringValue());
                        new XincoSettingTJpaController().create(temp);
                        updated = true;
                    } else if (!updated) {
                        throw new XincoException(o + " is an Auditable Object but it's processing logic is not implemented yet!");
                    }
                }
            }
        } catch (PreexistingEntityException e) {
            //Probably trying to reuse the recordID (this can happen in nested
            e.printStackTrace();
            throw new XincoException(e.getLocalizedMessage());
        }
    }
}
