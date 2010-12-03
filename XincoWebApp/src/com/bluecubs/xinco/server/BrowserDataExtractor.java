package com.bluecubs.xinco.server;

import com.bluecubs.xinco.tools.LocalizationTool;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Extracts information from browser
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public final class BrowserDataExtractor extends HttpServlet {

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
        supportedLanguages = new HashMap(2);
        String[] locales = LocalizationTool.getLocales();
        for (String loc : locales) {
            if (!loc.trim().isEmpty()) {
                supportedLanguages.put(loc, LocalizationTool.getLocaleCode(loc));
            } else {
                supportedLanguages.put("en", "");
            }
        }
    }

    public void setUserAgent(String httpUserAgent) {
        this.userAgent = httpUserAgent.toLowerCase();
    }

    private void setCompany() {
        if (userAgent.indexOf("msie") > -1) {
            company = "Microsoft";
        } else if (userAgent.indexOf("opera") > -1) {
            company = "Opera Software";
        } else if (userAgent.indexOf("mozilla") > -1) {
            company = "Netscape Communications";
        } else {
            company = "unknown";
        }
    }

    /**
     * Get company.
     */
    public String getCompany() {
        return company;
    }

    private void setName() {
        if (company.equals("Microsoft")) {
            name = "Microsoft Internet Explorer";
        } else if (company.equals("Netscape Communications")) {
            name = "Netscape Navigator";
        } else if (company.equals("Operasoftware")) {
            name = "Operasoftware Opera";
        } else {
            name = "unknown";
        }
    }

    /**
     * Get name.
     */
    public String getName() {
        return name;
    }

    private void setVersion() {
        int tmpPos;
        String tmpString;

        if (company.equals("Microsoft")) {
            String str = userAgent.substring(userAgent.indexOf("msie") + 5);
            version = str.substring(0, str.indexOf(";"));
        } else {
            tmpString = (userAgent.substring(tmpPos = (userAgent.indexOf("/")) + 1,
                    tmpPos + userAgent.indexOf(" "))).trim();
            version = tmpString.substring(0, tmpString.indexOf(" "));
        }
    }

    /**
     * Get version.
     */
    public String getVersion() {
        return version;
    }

    private void setMainVersion() {
        mainVersion = version.substring(0, version.indexOf("."));
    }

    /**
     * Get main version.
     */
    public String getMainVersion() {
        return mainVersion;
    }

    private void setMinorVersion() {
        minorVersion = version.substring(version.indexOf(".") + 1).trim();
    }

    /**
     * Get minor version.
     */
    public String getMinorVersion() {
        return minorVersion;
    }

    private void setOs() {
        if (userAgent.indexOf("win") > -1) {
            if (userAgent.indexOf("windows 95") > -1
                    || userAgent.indexOf("win95") > -1) {
                os = "Windows 95";
            } else if (userAgent.indexOf("windows 98") > -1
                    || userAgent.indexOf("win98") > -1) {
                os = "Windows 98";
            } else if (userAgent.indexOf("windows nt") > -1
                    || userAgent.indexOf("winnt") > -1) {
                os = "Windows NT";
            } else if (userAgent.indexOf("win16") > -1
                    || userAgent.indexOf("windows 3.") > -1) {
                os = "Windows 3.x";
            }

        } else if (userAgent.indexOf("Mac") > -1) {
            if (userAgent.indexOf("Mac_PowerPC") > -1
                    || userAgent.indexOf("Mac_PPC") > -1) {
                os = "Macintosh Power PC";
            } else if (userAgent.indexOf("Macintosh") > -1) {
                os = "Macintosh";
            } else {
                os = "Unknown Mac";
            }
        }
    }

    /**
     * Get OS.
     */
    public String getOs() {
        return os;
    }

    private void setLanguage() {
        String prefLanguage = request.getHeader("Accept-Language");

        if (prefLanguage != null) {
            String lang = null;
            StringTokenizer st = new StringTokenizer(prefLanguage, ",");

            while (st.hasMoreTokens()) {
                if (supportedLanguages.containsKey((lang = st.nextToken()))) {
                    lang = parseLocale(lang);
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

    /**
     * Get language.
     */
    public String getLanguage() {
        return language;
    }

    private void setLocale() {
        locale = new Locale(language, "");
    }

    /**
     * Get locale
     */
    public Locale getLocale() {
        return locale;
    }
}
