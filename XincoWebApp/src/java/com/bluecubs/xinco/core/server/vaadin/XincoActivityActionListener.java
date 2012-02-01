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

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (Xinco.getInstance() != null) {
                Xinco.getInstance().setLock();
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoActivityActionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
