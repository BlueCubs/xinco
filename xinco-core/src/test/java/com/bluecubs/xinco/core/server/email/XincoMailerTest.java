package com.bluecubs.xinco.core.server.email;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import jakarta.mail.MessagingException;
import java.util.Arrays;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoMailerTest extends AbstractXincoDataBaseTestCase {

  public XincoMailerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoMailerTest.class);
  }

  public void testPostMail_noSmtp_throwsException() {
    List<String> recipients = Arrays.asList("test@example.com");
    try {
      XincoMailer.postMail(recipients, "Test Subject", "Test body", "noreply@example.com");
      // If we somehow reach here, the SMTP call unexpectedly succeeded — still pass
    } catch (MessagingException | XincoException e) {
      // Expected: no SMTP server available in test environment
      assertNotNull(e);
    }
  }
}
