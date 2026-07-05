package com.bluecubs.xinco.core.server;

import org.hibernate.envers.RevisionListener;

public class XincoRevisionListener implements RevisionListener {

  public static final ThreadLocal<Integer> MODIFIER_ID = ThreadLocal.withInitial(() -> 1);
  public static final ThreadLocal<String> MOD_REASON = ThreadLocal.withInitial(() -> "");

  @Override
  public void newRevision(Object revisionEntity) {
    XincoRevisionInfo rev = (XincoRevisionInfo) revisionEntity;
    rev.setModifierId(MODIFIER_ID.get());
    rev.setModReason(MOD_REASON.get());
  }
}
