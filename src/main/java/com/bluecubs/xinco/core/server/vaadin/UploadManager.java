/*
 * Copyright 2012 blueCubs.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: UploadManager
 * 
 * Description: Upload Manager
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Jan 26, 2012
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Window.Notification;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
class UploadManager extends CustomComponent implements Upload.SucceededListener, Upload.FailedListener, Upload.Receiver {

    private File file; // File to write to.
    private boolean success = false;

    // Callback method to begin receiving the upload.
    @Override
    public OutputStream receiveUpload(String filename, String MIMEType) {
        FileOutputStream fos; // Output stream to write to
        Xinco.getInstance().setFileName(filename);
        try {
            //Create upload folder if needed
            File uploads = new File(XincoDBManager.config.fileRepositoryPath + System.getProperty("file.separator"));
            uploads.mkdirs();
            uploads.deleteOnExit();
            file = File.createTempFile("xinco", ".xtf", uploads);
        } catch (IOException ex) {
            return null;
        }
        getFile().deleteOnExit();
        try {
            // Open the file for writing.
            fos = new FileOutputStream(getFile());
        } catch (final java.io.FileNotFoundException e) {
            return null;
        }
        return fos; // Return the output stream to write to
    }

    @Override
    public void uploadSucceeded(SucceededEvent event) {
        Xinco.getInstance().setFileToLoad(file);
        success = true;
    }

    @Override
    public void uploadFailed(FailedEvent event) {
        getApplication().getMainWindow().showNotification(Xinco.getInstance().getResource().getString("datawizard.unabletoloadfile"), Notification.TYPE_ERROR_MESSAGE);
        file = null;
        Xinco.getInstance().setFileName(null);
        success = false;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return Xinco.getInstance().getFileName();
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }
}
