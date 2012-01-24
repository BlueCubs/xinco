/**
 * Copyright 2007 blueCubs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 *
 * Name: XincoFileIconManager
 *
 * Description: XincoFileIconManager
 *
 * Original Author: Javier A. Ortiz Date: February 16, 2007, 12:48 PM
 *
 * Modifications:
 *
 * Who? When? What?
 *
 *
 *************************************************************
 */
package com.bluecubs.xinco.tools;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoFileIconManager {

    private File file = null;

    /**
     * Creates a new instance of XincoFileIconManager
     */
    public XincoFileIconManager() {
    }

    public Icon getIcon(String extension) {
        if (extension == null) {
            return null;
        }
        if (extension.indexOf('.') > -1) {
            extension = extension.substring(extension.lastIndexOf('.') + 1, extension.length());
        }
        if (extension.length() < 3) {
            return null;
        }
        Icon icon;
        try {
            if (extension.length() < 3) {
                return null;
            }
            if (FileSystemView.getFileSystemView() != null) {
                //Create a temporary file with the specified extension
                file = File.createTempFile("icon", "." + extension);
                if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    //For some reason the method above doesn't work on Mac. 
                    //Use alternate method.
                    icon = new JFileChooser().getIcon(file);
                } else {
                    icon = FileSystemView.getFileSystemView().getSystemIcon(file);
                }

                //Delete the temporary file
                file.delete();
            } else {
                return null;
            }
        } catch (Exception ioe) {
            return null;
        }
        return icon;
    }
}
