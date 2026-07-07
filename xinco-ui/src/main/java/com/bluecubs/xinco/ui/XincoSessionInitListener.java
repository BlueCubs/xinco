package com.bluecubs.xinco.ui;

import com.vaadin.flow.server.CustomizedSystemMessages;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Ports XincoActivityTimer behaviour to Vaadin Flow / Spring Boot.
 *
 * <p>Sets the servlet-level session max-inactive interval so the container expires idle sessions,
 * and configures Vaadin to redirect the browser to the login page (/) when the session expires
 * rather than showing the built-in "session expired" dialog.
 */
@Component
public class XincoSessionInitListener implements VaadinServiceInitListener {

  @Value("${xinco.session.timeout.minutes:5}")
  private int timeoutMinutes;

  @Override
  public void serviceInit(ServiceInitEvent event) {
    event
        .getSource()
        .setSystemMessagesProvider(
            info -> {
              CustomizedSystemMessages msgs = new CustomizedSystemMessages();
              msgs.setSessionExpiredNotificationEnabled(false);
              msgs.setSessionExpiredURL("/");
              return msgs;
            });

    event
        .getSource()
        .addSessionInitListener(
            initEvent ->
                initEvent.getSession().getSession().setMaxInactiveInterval(timeoutMinutes * 60));
  }

  int getTimeoutMinutes() {
    return timeoutMinutes;
  }
}
