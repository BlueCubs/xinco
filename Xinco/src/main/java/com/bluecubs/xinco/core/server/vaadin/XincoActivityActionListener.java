/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.vaadin;

import static com.bluecubs.xinco.core.server.vaadin.Xinco.getInstance;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoActivityActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (getInstance() != null) {
                getInstance().setLock();
            }
        } catch (Throwable ex) {
            getLogger(XincoActivityActionListener.class.getName()).log(SEVERE, null, ex);
        }
    }
}
