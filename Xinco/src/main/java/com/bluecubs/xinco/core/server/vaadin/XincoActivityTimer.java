package com.bluecubs.xinco.core.server.vaadin;

import java.awt.event.ActionListener;
import javax.swing.Timer;
import lombok.Getter;

/**
 * Timer that tracks user activity and triggers session timeout.
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public final class XincoActivityTimer {

  @Getter private Timer activityTimer = null;

  /**
   * Creates a new instance of XincoActivityTimer
   *
   * @param delay Timer delay
   */
  public XincoActivityTimer(int delay) {
    ActionListener lockExplorer = new XincoActivityActionListener();
    // Use delay as minute value
    activityTimer = new Timer(delay * 1_000 * 60, lockExplorer);
    getActivityTimer().start();
  }
}
