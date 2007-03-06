/**
 *Copyright 2007 blueCubs.com
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
 * Name:            XincoFileIconManager
 *
 * Description:     XincoFileIconManager
 *
 * Original Author: Javier A. Ortiz
 * Date:            February 16, 2007, 12:48 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *
 *************************************************************
 */

package com.bluecubs.xinco.tools;

import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoFileIconManager {
    private File file = null;
    /** Creates a new instance of XincoFileIconManager */
    public XincoFileIconManager() {
    }
    
    public Icon getIcon16(String extension){
        if(extension == null)
            return null;
        if(extension.indexOf('.')>-1)
            extension=extension.substring(extension.indexOf('.')+1,extension.length());
        Icon icon=null;
        try {
            //Create a temporary file with the specified extension
            file = File.createTempFile("icon", "." + extension);
            
            FileSystemView view = FileSystemView.getFileSystemView();
            icon = view.getSystemIcon(file);
            
            //Delete the temporary file
            file.delete();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return icon;
    }
    
    public Icon getIcon32(String extension){
        if(extension.indexOf('.')>-1)
            extension=extension.substring(extension.indexOf('.')+1,extension.length());
        Icon icon=null;
        try {
            //Create a temporary file with the specified extension
            file = File.createTempFile("icon", "." + extension);
            
            sun.awt.shell.ShellFolder shellFolder = sun.awt.shell.ShellFolder.getShellFolder(file);
            icon = new ImageIcon(shellFolder.getIcon(true));
            
            //Delete the temporary file
            file.delete();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return icon;
    }
}
