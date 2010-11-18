package com.bluecubs.xinco.tools;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class LocalizationTool {

    static HashMap<String, String> localeFlagMap = new HashMap<String, String>();
    static ResourceBundle lrb = ResourceBundle.getBundle(
            "com.bluecubs.xinco.messages.XincoMessagesLocale",
            Locale.getDefault());
    static{

    }

    static public String[] getLocales() {
        return lrb.getString("AvailableLocales").split(",");
    }

    static public String getLocaleCode(String loc) {
        return lrb.getString("Locale." + loc);
    }

    static public String getPathToFlag(String loc) {
    }
}
