/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.vaadin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoActivityActionListener implements ActionListener {

    private Xinco explorer = null;

    /** Creates a new instance of XincoActivityActionListener
     * @param e
     * @param xat 
     */
    public XincoActivityActionListener(Xinco e) {
        this.explorer = e;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            explorer.setLock();
        } catch (Throwable ex) {
            Logger.getLogger(XincoActivityActionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
