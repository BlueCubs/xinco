package com.bluecubs.xinco.core.server.vaadin;

import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoActivityTimer {
    private Xinco explorer = null;
    private Timer activityTimer = null;
    /** Creates a new instance of XincoActivityTimer
     * @param e
     * @param delay 
     */
    public XincoActivityTimer(Xinco e, int delay) {
        this.explorer = e;
        ActionListener lockExplorer = new XincoActivityActionListener(explorer);
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
