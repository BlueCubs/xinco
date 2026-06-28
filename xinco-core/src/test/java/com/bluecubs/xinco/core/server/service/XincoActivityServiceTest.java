package com.bluecubs.xinco.core.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreLog;
import com.bluecubs.xinco.server.service.XincoVersion;
import com.bluecubs.xinco.server.service.XincoWebService;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class XincoActivityServiceTest {

  private XincoWebService webService;
  private XincoActivityService activityService;
  private XincoCoreDataServer data;
  private XincoCoreUserServer user;

  @BeforeEach
  void setUp() {
    webService = mock(XincoWebService.class);
    activityService = new XincoActivityService(webService);
    data = mock(XincoCoreDataServer.class);
    user = mock(XincoCoreUserServer.class);
    when(data.getXincoCoreLogs()).thenReturn(new ArrayList<>());
  }

  @Test
  public void testCheckoutFile() throws XincoException {
    XincoCoreLog existingLog = new XincoCoreLog();
    existingLog.setVersion(new XincoVersion());
    existingLog.getVersion().setVersionHigh(1);

    ArrayList<Object> logs = new ArrayList<>();
    logs.add(existingLog);

    when(data.getId()).thenReturn(123);
    when(data.getXincoCoreLogs()).thenReturn(logs);
    when(user.getId()).thenReturn(456);
    when(user.getUsername()).thenReturn("testuser");

    XincoCoreLog savedLog = new XincoCoreLog();
    when(webService.setXincoCoreLog(any(XincoCoreLog.class), eq(user))).thenReturn(savedLog);

    XincoCoreData checkoutResult = new XincoCoreData();
    when(webService.doXincoCoreDataCheckout(eq(data), eq(user))).thenReturn(checkoutResult);

    XincoCoreData result = activityService.checkoutFile(data, user);

    assertThat(result).isSameAs(checkoutResult);
    verify(webService).doXincoCoreDataCheckout(eq(data), eq(user));
    assertThat(logs).contains(savedLog);
  }

  @Test
  public void testUndoCheckoutFile() throws XincoException {
    ArrayList<Object> logs = new ArrayList<>();
    when(data.getXincoCoreLogs()).thenReturn(logs);
    when(user.getUsername()).thenReturn("testuser");

    XincoCoreLog savedLog = new XincoCoreLog();
    when(webService.setXincoCoreLog(any(XincoCoreLog.class), eq(user))).thenReturn(savedLog);

    XincoCoreData undoResult = new XincoCoreData();
    when(webService.undoXincoCoreDataCheckout(eq(data), eq(user))).thenReturn(undoResult);

    XincoCoreData result = activityService.undoCheckoutFile(data, user);

    assertThat(result).isSameAs(undoResult);
    verify(webService).undoXincoCoreDataCheckout(eq(data), eq(user));
  }

  @Test
  public void testCheckinFile() throws XincoException {
    XincoVersion newVersion = new XincoVersion();
    byte[] fileData = "test content".getBytes();

    ArrayList<Object> logs = new ArrayList<>();
    when(data.getXincoCoreLogs()).thenReturn(logs);
    when(user.getUsername()).thenReturn("testuser");

    XincoCoreLog savedLog = new XincoCoreLog();
    when(webService.setXincoCoreLog(any(XincoCoreLog.class), eq(user))).thenReturn(savedLog);
    when(webService.uploadXincoCoreData(eq(data), eq(fileData), eq(user)))
        .thenReturn(fileData.length);

    XincoCoreData checkinResult = new XincoCoreData();
    when(webService.doXincoCoreDataCheckin(eq(data), eq(user))).thenReturn(checkinResult);

    XincoCoreData result = activityService.checkinFile(data, user, newVersion, fileData, "reason");

    assertThat(result).isSameAs(checkinResult);
    verify(webService).uploadXincoCoreData(eq(data), eq(fileData), eq(user));
    verify(webService).doXincoCoreDataCheckin(eq(data), eq(user));
  }
}
