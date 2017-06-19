package com.bluecubs.xinco.core.server.vaadin;

import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Javier A. Ortiz Bultron<javier.ortiz.78@gmail.com>
 */
public final class XincoActivityTimer {

    private Timer activityTimer = null;

    /**
     * Creates a new instance of XincoActivityTimer
     *
     * @param e
     * @param delay
     */
    public XincoActivityTimer(int delay) {
        ActionListener lockExplorer = new XincoActivityActionListener();
        //Use delay as minute value
        activityTimer = new Timer(delay * 1000 * 60, lockExplorer);
        getActivityTimer().start();
    }

    /**
     *
     * @return
     */
    public Timer getActivityTimer() {
        return activityTimer;
    }
}
