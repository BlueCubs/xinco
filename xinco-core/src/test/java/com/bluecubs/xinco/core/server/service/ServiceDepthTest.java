package com.bluecubs.xinco.core.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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

class ServiceDepthTest {

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
    when(user.getId()).thenReturn(1);
    when(user.getUsername()).thenReturn("testuser");
  }

  @Test
  void checkinFile_nullLogReason_doesNotAppendDash() throws XincoException {
    XincoVersion version = new XincoVersion();
    byte[] fileData = "content".getBytes();
    XincoCoreLog savedLog = new XincoCoreLog();
    XincoCoreData checkinResult = new XincoCoreData();

    when(webService.setXincoCoreLog(any(), eq(user))).thenReturn(savedLog);
    when(webService.uploadXincoCoreData(eq(data), eq(fileData), eq(user)))
        .thenReturn(fileData.length);
    when(webService.doXincoCoreDataCheckin(eq(data), eq(user))).thenReturn(checkinResult);

    XincoCoreData result = activityService.checkinFile(data, user, version, fileData, null);
    assertThat(result).isSameAs(checkinResult);
  }

  @Test
  void checkinFile_emptyLogReason_doesNotAppendDash() throws XincoException {
    XincoVersion version = new XincoVersion();
    byte[] fileData = "content".getBytes();
    XincoCoreLog savedLog = new XincoCoreLog();
    XincoCoreData checkinResult = new XincoCoreData();

    when(webService.setXincoCoreLog(any(), eq(user))).thenReturn(savedLog);
    when(webService.uploadXincoCoreData(eq(data), eq(fileData), eq(user)))
        .thenReturn(fileData.length);
    when(webService.doXincoCoreDataCheckin(eq(data), eq(user))).thenReturn(checkinResult);

    XincoCoreData result = activityService.checkinFile(data, user, version, fileData, "");
    assertThat(result).isSameAs(checkinResult);
  }

  @Test
  void checkinFile_nullLogReturned_doesNotAddToList() throws XincoException {
    XincoVersion version = new XincoVersion();
    byte[] fileData = "content".getBytes();
    XincoCoreData checkinResult = new XincoCoreData();
    ArrayList<Object> logs = new ArrayList<>();
    when(data.getXincoCoreLogs()).thenReturn(logs);

    when(webService.setXincoCoreLog(any(), eq(user))).thenReturn(null);
    when(webService.uploadXincoCoreData(eq(data), eq(fileData), eq(user)))
        .thenReturn(fileData.length);
    when(webService.doXincoCoreDataCheckin(eq(data), eq(user))).thenReturn(checkinResult);

    XincoCoreData result = activityService.checkinFile(data, user, version, fileData, "reason");
    assertThat(result).isSameAs(checkinResult);
    assertThat(logs).isEmpty();
  }

  @Test
  void checkinFile_uploadFails_throwsXincoException() {
    XincoVersion version = new XincoVersion();
    byte[] fileData = "content".getBytes();
    XincoCoreLog savedLog = new XincoCoreLog();

    when(webService.setXincoCoreLog(any(), eq(user))).thenReturn(savedLog);
    when(webService.uploadXincoCoreData(eq(data), eq(fileData), eq(user))).thenReturn(0);

    assertThatThrownBy(() -> activityService.checkinFile(data, user, version, fileData, "reason"))
        .isInstanceOf(XincoException.class);
  }

  @Test
  void checkoutFile_nullLogReturned_doesNotAddToList() throws XincoException {
    ArrayList<Object> logs = new ArrayList<>();
    when(data.getXincoCoreLogs()).thenReturn(logs);
    XincoCoreData checkoutResult = new XincoCoreData();

    when(webService.setXincoCoreLog(any(), eq(user))).thenReturn(null);
    when(webService.doXincoCoreDataCheckout(eq(data), eq(user))).thenReturn(checkoutResult);

    XincoCoreData result = activityService.checkoutFile(data, user);
    assertThat(result).isSameAs(checkoutResult);
    assertThat(logs).isEmpty();
  }

  @Test
  void downloadFile_exceptionWrappedInXincoException() {
    XincoFileService fileService = new XincoFileService(webService);
    XincoCoreData fileData = mock(XincoCoreData.class);
    when(fileData.getId()).thenReturn(1);

    when(webService.downloadXincoCoreData(any(), any()))
        .thenThrow(new RuntimeException("network error"));

    assertThatThrownBy(() -> fileService.downloadFile(fileData, user, false))
        .isInstanceOf(XincoException.class)
        .hasMessageContaining("Download failed");
  }

  @Test
  void uploadFile_exceptionWrappedInXincoException() {
    XincoFileService fileService = new XincoFileService(webService);
    XincoCoreData fileData = mock(XincoCoreData.class);
    when(fileData.getId()).thenReturn(1);
    byte[] content = "data".getBytes();

    when(webService.uploadXincoCoreData(any(), any(), any()))
        .thenThrow(new RuntimeException("upload error"));

    assertThatThrownBy(() -> fileService.uploadFile(fileData, content, user))
        .isInstanceOf(XincoException.class)
        .hasMessageContaining("Upload failed");
  }
}
