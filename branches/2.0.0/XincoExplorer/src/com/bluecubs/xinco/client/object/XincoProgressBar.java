/**
 *Copyright 2006 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            XincoProgressBar
 *
 * Description:     XincoProgressBar
 *
 * Original Author: Javier A. Ortiz
 * Date:            2006
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *
 *************************************************************
 * XincoProgressBar.java
 *
 * Created on December 19, 2006, 4:16 PM
 */

package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;
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
    private XincoExplorer explorer;
    
    public XincoProgressBar(XincoExplorer explorer) {
        this.setAlwaysOnTop(true);
        this.explorer=explorer;
        ResourceBundle xerb = this.explorer.getResourceBundle();
        setTitle(xerb.getString("message.progressbar.title"));
        setSize( 700, 65 );
        setBackground( Color.gray );      
        topPanel = new JPanel();
        topPanel.setPreferredSize( new Dimension( 700, 40 ) );
        getContentPane().add( topPanel );
        
        // Create progress bar
        
        progress = new JProgressBar();
        progress.setPreferredSize( new Dimension(700, 20 ) );
        progress.setMinimum( 0 );
        progress.setMaximum( 100 );
        progress.setValue( 0 );
        progress.setBounds( 20, 35, 260, 20 );
        progress.setIndeterminate(true);
        topPanel.add( progress );
    }
    
    
    private void setProgress(int p){
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
}
