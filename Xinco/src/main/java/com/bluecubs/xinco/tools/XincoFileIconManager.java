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
import static java.io.File.createTempFile;
import static java.lang.System.getProperty;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import static javax.swing.filechooser.FileSystemView.getFileSystemView;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoFileIconManager {

    private File file = null;

    public Icon getIcon(final String extension) {
        if (extension == null) {
            return null;
        }
        String ext = extension;
        if (extension.indexOf('.') > -1) {
            ext = extension.substring(extension.lastIndexOf('.') + 1, extension.length());
        }
        if (ext.length() < 3) {
            return null;
        }
        Icon icon;
        try {
            if (getFileSystemView() == null) {
                return null;
            } else {
                //Create a temporary file with the specified extension
                file = createTempFile("icon", "." + ext);
                if (getProperty("os.name").toLowerCase().contains("mac")) {
                    //For some reason the method above doesn't work on Mac. 
                    //Use alternate method.
                    icon = new JFileChooser().getIcon(file);
                } else {
                    icon = getFileSystemView().getSystemIcon(file);
                }
                //Delete the temporary file
                file.delete();
            }
        } catch (Exception ioe) {
            getLogger(XincoFileIconManager.class.getSimpleName()).log(SEVERE, null, ioe);
            return null;
        }
        return icon;
    }
}
