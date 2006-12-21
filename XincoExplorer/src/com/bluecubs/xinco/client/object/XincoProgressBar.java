/*
 * XincoProgressBar.java
 *
 * Created on December 19, 2006, 4:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.client.object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author ortizbj
 */
public class XincoProgressBar extends JFrame{
    private	JProgressBar    progress;
    private	JPanel          topPanel;
    
    
    public XincoProgressBar() {
        this.setAlwaysOnTop(true);
        ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
        setTitle(xerb.getString("message.progressbar.title"));
        setSize( 210, 65 );
        setBackground( Color.gray );
        
        topPanel = new JPanel();
        topPanel.setPreferredSize( new Dimension( 310, 40 ) );
        getContentPane().add( topPanel );
        
        // Create progress bar
        
        progress = new JProgressBar();
        progress.setPreferredSize( new Dimension( 300, 20 ) );
        progress.setMinimum( 0 );
        progress.setMaximum( 100 );
        progress.setValue( 0 );
        progress.setBounds( 20, 35, 260, 20 );
        topPanel.add( progress );
    }
    
    
    public void setProgress(int p){
        // Update the progress indicator 
        
        progress.setValue( p );
        progress.setStringPainted(true);
        progress.setString(p + "%");
        Rectangle progressRect = progress.getBounds();
        progressRect.x = 0;
        progressRect.y = 0;
        progress.paintImmediately( progressRect );
        if(p==progress.getMaximum())
            this.setVisible(false);
    }
    
    public static void main( String args[] ) {
        // Create an instance of the test application
        XincoProgressBar mainFrame= new XincoProgressBar();
        mainFrame.setVisible( true );
        mainFrame.pack();
        mainFrame.setProgress(50);
    }
}
