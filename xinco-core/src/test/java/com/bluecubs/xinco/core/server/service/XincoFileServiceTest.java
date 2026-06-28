package com.bluecubs.xinco.core.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XincoFileServiceTest {

  private XincoWebService webService;
  private XincoFileService fileService;
  private XincoCoreData data;
  private XincoCoreUserServer user;

  @BeforeEach
  void setUp() {
    webService = mock(XincoWebService.class);
    fileService = new XincoFileService(webService);
    data = mock(XincoCoreData.class);
    user = mock(XincoCoreUserServer.class);
    when(data.getId()).thenReturn(123);
  }

  @Test
  void testDownloadFileNoRendering() throws XincoException {
    byte[] expectedBytes = "content".getBytes();
    when(webService.downloadXincoCoreData(eq(data), eq(user))).thenReturn(expectedBytes);

    byte[] actualBytes = fileService.downloadFile(data, user, false);

    assertThat(actualBytes).isEqualTo(expectedBytes);
    verify(webService).downloadXincoCoreData(data, user);
  }

  @Test
  void testUploadFile() throws XincoException {
    byte[] content = "new content".getBytes();
    when(webService.uploadXincoCoreData(eq(data), eq(content), eq(user)))
        .thenReturn(content.length);

    int uploaded = fileService.uploadFile(data, content, user);

    assertThat(uploaded).isEqualTo(content.length);
    verify(webService).uploadXincoCoreData(data, content, user);
  }
}
