package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.*;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class AuditedEntityListener {

    private XincoCoreUser modifier;

    @PrePersist
    public void persistEntity(Object o) throws Exception {
        synchronized (this) {
            Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Pre-persist: {0}", o);
            if (o instanceof AuditedObject) {
                XincoAuditedObject auditedObject = (XincoAuditedObject) o;
                if (auditedObject.getModificationReason() == null || auditedObject.getModificationReason().isEmpty()) {
                    auditedObject.setModificationReason("audit.general.create");
                }
                auditTrail(o);
                Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Done!");
            }
        }
    }

    @PreRemove
    public void removeEntity(Object o) throws Exception {
        synchronized (this) {
            Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Pre-remove: {0}", o);
            if (o instanceof AuditedObject) {
                XincoAuditedObject auditedObject = (XincoAuditedObject) o;
                if (auditedObject.getModificationReason() == null || auditedObject.getModificationReason().isEmpty()) {
                    auditedObject.setModificationReason("audit.general.delete");
                }
                auditTrail(o);
                Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Done!");
            }
        }
    }

    @PreUpdate
    public void updateEntity(Object o) throws Exception {
        synchronized (this) {
            if (o instanceof AuditedObject) {
                XincoAuditedObject auditedObject = (XincoAuditedObject) o;
                Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Pre-update: {0}", o);
                if (auditedObject.getModificationReason() == null || auditedObject.getModificationReason().isEmpty()) {
                    auditedObject.setModificationReason("audit.general.modified");
                }
                auditTrail(o);
                Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Done!");
            }
        }
    }

    private void auditTrail(Object o) throws Exception {
        synchronized (this) {
            if (o instanceof AuditedObject) {
                XincoAuditedObject auditedObject = (XincoAuditedObject) o;
                if (auditedObject.isAuditable()) {
                    Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Creating audit trail for {0}", o);
                    modifier = new XincoCoreUserJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreUser(auditedObject.getModifierId());
                    if (modifier == null) {
                        //Default to admin
                        modifier = new XincoCoreUserJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreUser(1);
                    }
                    //Get an id from database
                    boolean used = true;
                    HashMap parameters = new HashMap();
                    int record_ID = -1;
                    while (used) {
                        record_ID = XincoIdServer.getNextId("xinco_core_user_modified_record");
                        parameters.clear();
                        parameters.put("recordId", record_ID);
                        used = !XincoDBManager.namedQuery("XincoCoreUserModifiedRecord.findByRecordId", parameters).isEmpty();
                    }
                    XincoCoreUserModifiedRecord mod = new XincoCoreUserModifiedRecord(new XincoCoreUserModifiedRecordPK(
                            modifier.getId(), record_ID));
                    mod.setModReason(auditedObject.getModificationReason());
                    mod.setXincoCoreUser(modifier);
                    mod.setModTime(new Date());
                    new XincoCoreUserModifiedRecordJpaController(XincoDBManager.getEntityManagerFactory()).create(mod);
                    Logger.getLogger(AuditedEntityListener.class.getName()).log(Level.FINEST, "Created XincoCoreUserModifiedRecord record");
                    boolean updated = false;
                    if (o instanceof XincoCoreNode) {
                        XincoCoreNode node = (XincoCoreNode) o;
                        new XincoCoreNodeTJpaController(XincoDBManager.getEntityManagerFactory()).create(new XincoCoreNodeT(record_ID, node.getId(),
                                node.getXincoCoreNode() == null ? 0 : node.getXincoCoreNode().getId(),
                                node.getXincoCoreLanguage() == null ? 1 : node.getXincoCoreLanguage().getId(),
                                node.getDesignation(), node.getStatusNumber()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreLanguage) {
                        XincoCoreLanguage lang = (XincoCoreLanguage) o;
                        new XincoCoreLanguageTJpaController(XincoDBManager.getEntityManagerFactory()).create(new XincoCoreLanguageT(record_ID, lang.getId(),
                                lang.getSign(), lang.getDesignation()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreGroup) {
                        XincoCoreGroup group = (XincoCoreGroup) o;
                        new XincoCoreGroupTJpaController(XincoDBManager.getEntityManagerFactory()).create(new XincoCoreGroupT(record_ID, group.getId(),
                                group.getDesignation(), group.getStatusNumber()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreDataType) {
                        XincoCoreDataType type = (XincoCoreDataType) o;
                        new XincoCoreDataTypeTJpaController(XincoDBManager.getEntityManagerFactory()).create(new XincoCoreDataTypeT(record_ID, type.getId(),
                                type.getDesignation(), type.getDescription()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreDataTypeAttribute) {
                        XincoCoreDataTypeAttribute attr = (XincoCoreDataTypeAttribute) o;
                        new XincoCoreDataTypeAttributeTJpaController(XincoDBManager.getEntityManagerFactory()).create(new XincoCoreDataTypeAttributeT(record_ID,
                                attr.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId(),
                                attr.getXincoCoreDataTypeAttributePK().getAttributeId(),
                                attr.getDesignation(), attr.getDataType(), attr.getAttrSize()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreData) {
                        XincoCoreData data = (XincoCoreData) o;
                        new XincoCoreDataTJpaController(XincoDBManager.getEntityManagerFactory()).create(new XincoCoreDataT(record_ID,
                                data.getId(),
                                data.getXincoCoreNode().getId(),
                                data.getXincoCoreLanguage().getId(),
                                data.getXincoCoreDataType().getId(),
                                data.getDesignation(), data.getStatusNumber()));
                        updated = true;
                    }
                    if (o instanceof XincoCoreAce) {
                        XincoCoreAce ace = (XincoCoreAce) o;
                        new XincoCoreAceTJpaController(XincoDBManager.getEntityManagerFactory()).create(new XincoCoreAceT(record_ID,
                                ace.getId(),
                                ace.getReadPermission(),
                                ace.getWritePermission(),
                                ace.getExecutePermission(),
                                ace.getAdminPermission()));
                        updated = true;
                    }
                    if (o instanceof XincoAddAttribute) {
                        XincoAddAttribute attr = (XincoAddAttribute) o;
                        new XincoAddAttributeTJpaController(XincoDBManager.getEntityManagerFactory()).create(new XincoAddAttributeT(record_ID,
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
                        new XincoSettingTJpaController(XincoDBManager.getEntityManagerFactory()).create(new XincoSettingT(record_ID,
                                setting.getId(),
                                setting.getDescription(),
                                setting.getIntValue(),
                                setting.getStringValue(),
                                setting.getBoolValue(),
                                setting.getLongValue()));
                        updated = true;
                    }
                    if (!updated) {
                        throw new XincoException(o + " is an Auditable "
                                + "Object but it's processing logic is not "
                                + "implemented yet!");
                    }
                }
            }
        }
    }
}
