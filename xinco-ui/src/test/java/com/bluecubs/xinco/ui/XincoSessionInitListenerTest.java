package com.bluecubs.xinco.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.SessionInitEvent;
import com.vaadin.flow.server.SessionInitListener;
import com.vaadin.flow.server.SystemMessages;
import com.vaadin.flow.server.SystemMessagesInfo;
import com.vaadin.flow.server.SystemMessagesProvider;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class XincoSessionInitListenerTest {

  private XincoSessionInitListener listener;

  @BeforeEach
  void setup() throws Exception {
    listener = new XincoSessionInitListener();
    setTimeoutMinutes(5);
  }

  @Test
  void defaultTimeoutMinutes_isFive() {
    assertEquals(5, listener.getTimeoutMinutes());
  }

  @Test
  void serviceInit_setsSessionMaxInactiveInterval() throws Exception {
    VaadinService service = mock(VaadinService.class);
    ServiceInitEvent event = mock(ServiceInitEvent.class);
    when(event.getSource()).thenReturn(service);

    listener.serviceInit(event);

    ArgumentCaptor<SessionInitListener> captor =
        ArgumentCaptor.forClass(SessionInitListener.class);
    verify(service).addSessionInitListener(captor.capture());

    WrappedSession wrapped = mock(WrappedSession.class);
    VaadinSession session = mock(VaadinSession.class);
    when(session.getSession()).thenReturn(wrapped);
    SessionInitEvent initEvent = mock(SessionInitEvent.class);
    when(initEvent.getSession()).thenReturn(session);

    captor.getValue().sessionInit(initEvent);

    verify(wrapped).setMaxInactiveInterval(5 * 60);
  }

  @Test
  void serviceInit_customTimeout_setsCorrectInterval() throws Exception {
    setTimeoutMinutes(15);

    VaadinService service = mock(VaadinService.class);
    ServiceInitEvent event = mock(ServiceInitEvent.class);
    when(event.getSource()).thenReturn(service);

    listener.serviceInit(event);

    ArgumentCaptor<SessionInitListener> captor =
        ArgumentCaptor.forClass(SessionInitListener.class);
    verify(service).addSessionInitListener(captor.capture());

    WrappedSession wrapped = mock(WrappedSession.class);
    VaadinSession session = mock(VaadinSession.class);
    when(session.getSession()).thenReturn(wrapped);
    SessionInitEvent initEvent = mock(SessionInitEvent.class);
    when(initEvent.getSession()).thenReturn(session);

    captor.getValue().sessionInit(initEvent);

    verify(wrapped).setMaxInactiveInterval(15 * 60);
  }

  @Test
  void serviceInit_configuresSessionExpiredRedirectToRoot() {
    VaadinService service = mock(VaadinService.class);
    ServiceInitEvent event = mock(ServiceInitEvent.class);
    when(event.getSource()).thenReturn(service);

    listener.serviceInit(event);

    ArgumentCaptor<SystemMessagesProvider> captor =
        ArgumentCaptor.forClass(SystemMessagesProvider.class);
    verify(service).setSystemMessagesProvider(captor.capture());

    SystemMessages msgs = captor.getValue().getSystemMessages(mock(SystemMessagesInfo.class));
    assertFalse(msgs.isSessionExpiredNotificationEnabled());
    assertEquals("/", msgs.getSessionExpiredURL());
  }

  private void setTimeoutMinutes(int minutes) throws Exception {
    Field f = XincoSessionInitListener.class.getDeclaredField("timeoutMinutes");
    f.setAccessible(true);
    f.set(listener, minutes);
  }
}
