package com.bluecubs.xinco.server;

import static com.bluecubs.xinco.tools.LocalizationTool.getLocaleCode;
import static com.bluecubs.xinco.tools.LocalizationTool.getLocales;
import static java.lang.System.out;

import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Extracts information from browser
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public final class BrowserDataExtractor extends HttpServlet
{
  private static final long serialVersionUID = -3242536927010769667L;

  protected HttpServletRequest request;
  protected HttpSession session;
  protected String userAgent;
  protected String company;
  protected String name;
  protected String version;
  protected String mainVersion;
  protected String minorVersion;
  protected String os;
  protected String language = "en";
  protected Locale locale;
  private HashMap supportedLanguages;

  public BrowserDataExtractor(HttpServletRequest request, HttpSession session)
  {
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

  public void initialize()
  {
    supportedLanguages = new HashMap(2);
    String[] locales = getLocales();
    for (String loc : locales)
    {
      if (!loc.trim().isEmpty())
      {
        supportedLanguages.put(loc, getLocaleCode(loc));
      }
      else
      {
        supportedLanguages.put("en", "");
      }
    }
  }

  public void setUserAgent(String httpUserAgent)
  {
    this.userAgent = httpUserAgent.toLowerCase();
  }

  private void setCompany()
  {
    if (userAgent.contains("msie"))
    {
      company = "Microsoft";
    }
    else if (userAgent.contains("opera"))
    {
      company = "Opera Software";
    }
    else if (userAgent.contains("mozilla"))
    {
      company = "Netscape Communications";
    }
    else
    {
      company = "unknown";
    }
  }

  /**
   * Get company.
   *
   * @return company
   */
  public String getCompany()
  {
    return company;
  }

  private void setName()
  {
    switch (company)
    {
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

  /**
   * Get name.
   * @return name
   */
  public String getName()
  {
    return name;
  }

  private void setVersion()
  {
    int tmpPos;
    String tmpString;
    try
    {
      if (company.equals("Microsoft"))
      {
        String str = userAgent.substring(userAgent.indexOf("msie") + 5);
        version = str.substring(0, str.indexOf(";"));
      }
      else
      {
        tmpString = (userAgent.substring(tmpPos = (userAgent.indexOf("/")) + 1,
                tmpPos + userAgent.indexOf(" "))).trim();
        version = tmpString.substring(0, tmpString.indexOf(" "));
      }
    }
    catch (Exception e)
    {
      out.println("Error getting browser information:\n"
              + "Company: " + company + "\n"
              + "Name: " + name + "\n"
              + "User Agent: " + userAgent);
    }
  }

  /**
   * Get version.
   * @return version
   */
  public String getVersion()
  {
    return version;
  }

  private void setMainVersion()
  {
    mainVersion = version.substring(0, version.indexOf("."));
  }

  /**
   * Get main version.
   * @return main version
   */
  public String getMainVersion()
  {
    return mainVersion;
  }

  private void setMinorVersion()
  {
    minorVersion = version.substring(version.indexOf(".") + 1).trim();
  }

  /**
   * Get minor version.
   * @return minor version
   */
  public String getMinorVersion()
  {
    return minorVersion;
  }

  private void setOs()
  {
    if (userAgent.contains("win"))
    {
      if (userAgent.contains("windows 95")
              || userAgent.contains("win95"))
      {
        os = "Windows 95";
      }
      else if (userAgent.contains("windows 98")
              || userAgent.contains("win98"))
      {
        os = "Windows 98";
      }
      else if (userAgent.contains("windows nt")
              || userAgent.contains("winnt"))
      {
        os = "Windows NT";
      }
      else if (userAgent.contains("win16")
              || userAgent.contains("windows 3."))
      {
        os = "Windows 3.x";
      }

    }
    else if (userAgent.contains("Mac"))
    {
      if (userAgent.contains("Mac_PowerPC")
              || userAgent.contains("Mac_PPC"))
      {
        os = "Macintosh Power PC";
      }
      else if (userAgent.contains("Macintosh"))
      {
        os = "Macintosh";
      }
      else
      {
        os = "Unknown Mac";
      }
    }
  }

  /**
   * Get OS.
   * @return OS
   */
  public String getOs()
  {
    return os;
  }

  private void setLanguage()
  {
    String prefLanguage = request.getHeader("Accept-Language");

    if (prefLanguage != null)
    {
      String lang;
      StringTokenizer st = new StringTokenizer(prefLanguage, ",");

      while (st.hasMoreTokens())
      {
        if (supportedLanguages.containsKey((lang = st.nextToken())))
        {
          language = parseLocale(lang);
          break;
        }
      }
    }
  }

  public boolean isLanguageSupported(String lang)
  {
    return supportedLanguages.containsKey(lang);
  }

  /*
     * Parse locale.
   */
  private String parseLocale(String language)
  {
    StringTokenizer st = new StringTokenizer(language, "-");

    if (st.countTokens() == 2)
    {
      return st.nextToken();
    }
    else
    {
      return language;
    }
  }

  /**
   * Get language.
   * @return language
   */
  public String getLanguage()
  {
    return language;
  }

  private void setLocale()
  {
    locale = new Locale(language, "");
  }

  /**
   * Get locale
   * @return locale
   */
  public Locale getLocale()
  {
    return locale;
  }
}
