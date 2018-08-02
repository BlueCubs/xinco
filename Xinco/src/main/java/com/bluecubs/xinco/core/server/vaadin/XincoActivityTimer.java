package com.bluecubs.xinco.core.server.vaadin;

import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Javier A. Ortiz Bultronjavier.ortiz.78@gmail.com
 */
public final class XincoActivityTimer {

    private Timer activityTimer = null;

    /**
     * Creates a new instance of XincoActivityTimer
     *
     * @param delay Delay time in minutes.
     */
    public XincoActivityTimer(int delay) {
        ActionListener lockExplorer = new XincoActivityActionListener();
        //Use delay as minute value
        activityTimer = new Timer(delay * 1000 * 60, lockExplorer);
        getActivityTimer().start();
    }

    /**
     * Get the activity timer.
     *
     * @return activity timer time.
     */
    public Timer getActivityTimer() {
        return activityTimer;
    }
}
