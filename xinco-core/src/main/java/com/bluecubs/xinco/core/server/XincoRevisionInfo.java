package com.bluecubs.xinco.core.server;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = "xinco_revisioninfo")
@RevisionEntity(XincoRevisionListener.class)
public class XincoRevisionInfo extends DefaultRevisionEntity {

  @Getter
  @Setter
  @Column(name = "modifier_id")
  private int modifierId = 1;

  @Getter
  @Setter
  @Column(name = "mod_reason")
  private String modReason;
}
