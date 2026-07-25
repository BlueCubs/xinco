package com.bluecubs.xinco.core.server;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name = "xinco_revisioninfo")
@RevisionEntity(XincoRevisionListener.class)
public class XincoRevisionInfo {

  @Id @GeneratedValue @RevisionNumber @Getter @Setter private int id;

  @RevisionTimestamp @Getter @Setter private long timestamp;

  @Getter
  @Setter
  @Column(name = "modifier_id")
  private int modifierId = 1;

  @Getter
  @Setter
  @Column(name = "mod_reason")
  private String modReason;
}
