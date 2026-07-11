package com.bluecubs.xinco.core.server;

import static java.util.Locale.getDefault;
import static java.util.ResourceBundle.getBundle;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import lombok.Getter;

/**
 * Extracts information from browser
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public final class BrowserDataExtractor extends HttpServlet {
  private static final long serialVersionUID = 8403308041839164132L;

  HttpServletRequest request;
  HttpSession session;
  String userAgent;

  public void setUserAgent(String ua) {
    this.userAgent = ua != null ? ua.toLowerCase(getDefault()) : "";
  }

  @Getter String company;
  @Getter String name;
  @Getter String version;
  @Getter String mainVersion;
  @Getter String minorVersion;
  @Getter String os;
  @Getter String language = "en";
  @Getter Locale locale;
  // Spracheinstellungen
  private HashMap<String, String> supportedLanguages;

  public BrowserDataExtractor(HttpServletRequest request, HttpSession session) {
    initialize();
    this.request = request;
    this.session = session;

    setUserAgent(request.getHeader("User-Agent"));
    setCompany();
    setName();
    setVersion();
    setMainVersion();
    setMinorVersion();
    setOs();
    setLanguage();
    setLocale();
  }

  public void initialize() {
    supportedLanguages = new HashMap<>();
    ResourceBundle lrb = getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", getDefault());
    String[] locales = lrb.getString("AvailableLocales").split(",");
    for (String loc : locales) {
      if (!loc.trim().isEmpty()) {
        supportedLanguages.put(loc, lrb.getString("Locale." + loc));
      } else {
        supportedLanguages.put("en", "");
      }
    }
  }

  private void setCompany() {
    if (userAgent.contains("msie")) {
      company = "Microsoft";
    } else if (userAgent.contains("opera")) {
      company = "Opera Software";
    } else if (userAgent.contains("mozilla")) {
      company = "Netscape Communications";
    } else {
      company = "unknown";
    }
  }

  private void setName() {
    switch (company) {
      case "Microsoft":
        name = "Microsoft Internet Explorer";
        break;
      case "Netscape Communications":
        name = "Netscape Navigator";
        break;
      case "Operasoftware":
        name = "Operasoftware Opera";
        break;
      default:
        name = "unknown";
        break;
    }
  }

  private void setVersion() {
    int tmpPos;
    String tmpString;

    if (company.equals("Microsoft")) {
      String str = userAgent.substring(userAgent.indexOf("msie") + 5);
      version = str.substring(0, str.indexOf(';'));
    } else {
      tmpString =
          userAgent
              .substring(tmpPos = (userAgent.indexOf('/')) + 1, tmpPos + userAgent.indexOf(' '))
              .trim();
      version = tmpString.substring(0, tmpString.indexOf(' '));
    }
  }

  private void setMainVersion() {
    mainVersion = version.substring(0, version.indexOf('.'));
  }

  private void setMinorVersion() {
    minorVersion = version.substring(version.indexOf('.') + 1).trim();
  }

  private void setOs() {
    if (userAgent.contains("win")) {
      if (userAgent.contains("windows 95") || userAgent.contains("win95")) {
        os = "Windows 95";
      } else if (userAgent.contains("windows 98") || userAgent.contains("win98")) {
        os = "Windows 98";
      } else if (userAgent.contains("windows nt") || userAgent.contains("winnt")) {
        os = "Windows NT";
      } else if (userAgent.contains("win16") || userAgent.contains("windows 3.")) {
        os = "Windows 3.x";
      }

    } else if (userAgent.contains("mac")) {
      if (userAgent.contains("mac_powerppc") || userAgent.contains("mac_ppc")) {
        os = "Macintosh Power PC";
      } else if (userAgent.contains("macintosh")) {
        os = "Macintosh";
      } else {
        os = "Unknown Mac";
      }
    }
  }

  private void setLanguage() {
    String prefLanguage = request.getHeader("Accept-Language");

    if (prefLanguage != null) {
      language = null;
      StringTokenizer st = new StringTokenizer(prefLanguage, ",");

      while (st.hasMoreTokens()) {
        if (supportedLanguages.containsKey(language = st.nextToken())) {
          language = parseLocale(language);
        }
      }
    }
  }

  public boolean isLanguageSupported(String lang) {
    return supportedLanguages.containsKey(lang);
  }

  /*
   * Parse locale.
   */
  private String parseLocale(String language) {
    StringTokenizer st = new StringTokenizer(language, "-");

    if (st.countTokens() == 2) {
      return st.nextToken();
    } else {
      return language;
    }
  }

  private void setLocale() {
    locale = new Locale(language, "");
  }
}
