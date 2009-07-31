package com.bluecubs.xinco.core.client;

import com.bluecubs.xinco.core.XincoCoreSetting;
import java.util.Vector;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoSettingClient extends XincoCoreSetting {

    public XincoSettingClient() {
    }

    public XincoSettingClient getSetting(String string) {
        Vector<XincoCoreSetting> temp = getXincoSettings();
        for (XincoCoreSetting s : temp) {
            if (s.getDescription().equals(string)) {
                return (XincoSettingClient) s;
            }
        }
        return null;
    }
}
