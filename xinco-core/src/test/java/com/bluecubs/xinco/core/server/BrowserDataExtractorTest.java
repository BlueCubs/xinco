package com.bluecubs.xinco.core.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;

/** Tests for BrowserDataExtractor using Mockito to supply the request/session. */
public class BrowserDataExtractorTest {

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(BrowserDataExtractorTest.class);
  }

  private static final HttpSession SESSION = mock(HttpSession.class);

  private static BrowserDataExtractor build(String userAgent, String acceptLanguage) {
    HttpServletRequest req = mock(HttpServletRequest.class);
    when(req.getHeader("User-Agent")).thenReturn(userAgent);
    when(req.getHeader("Accept-Language")).thenReturn(acceptLanguage);
    return new BrowserDataExtractor(req, SESSION);
  }

  // Long Chrome/Mozilla UA — version parsing is safe because the string is long enough
  private static final String CHROME_UA =
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
          + " Chrome/120.0.0.0 Safari/537.36";

  @Test
  public void testMozillaUserAgent_windowsNt() {
    BrowserDataExtractor e = build(CHROME_UA, null);
    assertEquals("Netscape Communications", e.getCompany());
    assertEquals("Netscape Navigator", e.getName());
    assertEquals("Windows NT", e.getOs());
    assertEquals("en", e.getLanguage());
    assertNotNull(e.getLocale());
  }

  @Test
  public void testMsieUserAgent_windowsNt() {
    BrowserDataExtractor e =
        build("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)", null);
    assertEquals("Microsoft", e.getCompany());
    assertEquals("Microsoft Internet Explorer", e.getName());
    assertEquals("9.0", e.getVersion());
    assertEquals("9", e.getMainVersion());
    assertEquals("0", e.getMinorVersion());
  }

  @Test
  public void testMsieUserAgent_windows95() {
    BrowserDataExtractor e = build("Mozilla/4.0 (compatible; MSIE 5.5; Windows 95)", null);
    assertEquals("Microsoft", e.getCompany());
    assertEquals("Windows 95", e.getOs());
  }

  @Test
  public void testMsieUserAgent_windows98() {
    BrowserDataExtractor e = build("Mozilla/4.0 (compatible; MSIE 5.5; Windows 98)", null);
    assertEquals("Microsoft", e.getCompany());
    assertEquals("Windows 98", e.getOs());
  }

  @Test
  public void testMsieUserAgent_windowsNt_winnt() {
    BrowserDataExtractor e = build("Mozilla/4.0 (compatible; MSIE 6.0; WinNT)", null);
    assertEquals("Microsoft", e.getCompany());
    assertEquals("Windows NT", e.getOs());
  }

  @Test
  public void testOperaUserAgent() {
    BrowserDataExtractor e =
        build("Opera/9.80 (Windows NT 6.1; WOW64) Presto/2.12.388 Version/12.18", null);
    assertEquals("Opera Software", e.getCompany());
    assertNotNull(e.getVersion());
  }

  @Test
  public void testAcceptLanguage_null_defaultsToEnglish() {
    BrowserDataExtractor e = build(CHROME_UA, null);
    assertEquals("en", e.getLanguage());
  }

  @Test
  public void testAcceptLanguage_hyphenated() {
    BrowserDataExtractor e = build(CHROME_UA, "en-US,en;q=0.9");
    assertNotNull(e.getLanguage());
    assertNotNull(e.getLocale());
  }

  @Test
  public void testAcceptLanguage_plainLocale() {
    BrowserDataExtractor e = build(CHROME_UA, "en,de;q=0.8");
    assertNotNull(e.getLanguage());
  }

  @Test
  public void testIsLanguageSupported_english_true() {
    BrowserDataExtractor e = build(CHROME_UA, null);
    assertTrue(e.isLanguageSupported("en"));
  }

  @Test
  public void testIsLanguageSupported_unknown_false() {
    BrowserDataExtractor e = build(CHROME_UA, null);
    assertFalse(e.isLanguageSupported("zzz"));
  }
}
