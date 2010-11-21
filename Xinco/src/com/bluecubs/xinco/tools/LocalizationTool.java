package com.bluecubs.xinco.tools;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class LocalizationTool {

    //TODO: Manage changing locale from webpage
    //static HashMap<String, String> localeFlagMap = new HashMap<String, String>();
    static ResourceBundle lrb = ResourceBundle.getBundle(
            "com.bluecubs.xinco.messages.XincoMessagesLocale",
            Locale.getDefault());

    static public String[] getLocales() {
        return lrb.getString("AvailableLocales").split(",");
    }

    static public String getLocaleCode(String loc) {
        return lrb.getString("Locale." + loc);
    }

//    static public String getPathToFlag(String loc) {
//
//    }
}
