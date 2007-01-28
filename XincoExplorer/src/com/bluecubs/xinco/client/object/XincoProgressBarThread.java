/*
 * XincoProgressBarThread.java
 *
 * Created on January 22, 2007, 3:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;

/**
 *
 * @author javydreamercsw
 */
public class XincoProgressBarThread extends Thread{
    private XincoExplorer explorer;
    private XincoProgressBar progressBar;
    @Override
    public void run() {
        progressBar=new XincoProgressBar(this.explorer);
        progressBar.pack();
        progressBar.setVisible(true);
    }
    /** Creates a new instance of XincoProgressBarThread */
    public XincoProgressBarThread(XincoExplorer e) {
        this.explorer=e;
    }
    public void hide() {
        progressBar.setVisible(false);
    }
    public void show() {
        progressBar.setVisible(true);
        progressBar.repaint();
    }
    public void setTitle(String t){
        progressBar.setTitle(t);
    }
}
