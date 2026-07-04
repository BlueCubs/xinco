package com.bluecubs.xinco.core.server;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoAuditedObject implements AuditedObject {

  @Getter @Setter private boolean auditable = true;
  private String reason;
  // Default to Admin
  @Getter @Setter private int modifierId = 1;
  private Timestamp modDate;

  @Override
  public void setModificationReason(String reason) {
    this.reason = reason;
  }

  @Override
  public String getModificationReason() {
    return reason;
  }

  @Override
  public Timestamp getModificationTime() {
    return modDate;
  }

  @Override
  public void setModificationTime(Timestamp d) {
    this.modDate = d;
  }
}
