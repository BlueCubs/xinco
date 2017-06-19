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
 * exchange for free software! More FINErmation on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: XincoRenderingThread
 * 
 * Description: Thread in charge of generating pdf rendering of files when uploaded (if needed)
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Dec 21, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.rendering;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.*;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.getXincoCoreDataPath;
import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoSettingServer.getSetting;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyTypeJpaController;
import com.bluecubs.xinco.core.server.service.XincoCoreData;
import com.bluecubs.xinco.core.server.service.XincoCoreUser;
import com.bluecubs.xinco.core.server.service.XincoWebService;
import static com.bluecubs.xinco.tools.Tool.MIN_PORT_NUMBER;
import static com.bluecubs.xinco.tools.Tool.addDefaultAddAttributes;
import static com.bluecubs.xinco.tools.Tool.copyFile;
import static com.bluecubs.xinco.tools.Tool.isPortAvaialble;
import java.io.File;
import java.io.FileInputStream;
import static java.lang.System.getProperty;
import static java.util.Locale.getDefault;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoRenderingThread extends Thread {

    private XincoCoreData original;
    private XincoCoreDataServer rendering = null;
    private XincoCoreDataHasDependencyServer dependency = null;
    private final XincoCoreUser user;
    private XincoWebService service = new XincoWebService();
    private File rendition;
    private File xincoFile;
    private static int port;
    private static final Logger LOG =
            getLogger(XincoRenderingThread.class.getName());

    public XincoRenderingThread(XincoCoreData original, XincoCoreUser user) {
        this.original = original;
        this.user = user;
    }

    @Override
    public void run() {
        port = getSetting(new XincoCoreUserServer(1),
                "setting.OOPort").getIntValue();
        if (port >= 0 && !original.getXincoAddAttributes().isEmpty()
                && !original.getXincoAddAttributes().get(0).getAttribVarchar()
                .toLowerCase(getDefault()).endsWith(".pdf")) {
            try {
                xincoFile = new File(getXincoCoreDataPath(CONFIG.fileRepositoryPath,
                        original.getId(),
                        "" + original.getId()));
                LOG.log(FINE, "Creating PDF rendering");
                //Enter the rendering into the system
                rendering = new XincoCoreDataServer(0,
                        original.getXincoCoreNodeId(),
                        original.getXincoCoreLanguage().getId(),
                        original.getXincoCoreDataType().getId(),
                        original.getDesignation(), original.getStatusNumber());
                rendering.setDesignation(original.getDesignation().substring(0,
                        original.getDesignation().lastIndexOf(".")) + ".pdf");
                rendering.setStatusNumber(original.getStatusNumber());
                rendering.setXincoCoreDataType(original.getXincoCoreDataType());
                rendering.setXincoCoreLanguage(original.getXincoCoreLanguage());
                rendering.setXincoCoreNodeId(original.getXincoCoreNodeId());
                rendering = (XincoCoreDataServer) service.setXincoCoreData(
                        rendering, user);
                LOG.log(FINE, "Rendering id: {0}", rendering.getId());
                //Mark as a rendering
                dependency =
                        new XincoCoreDataHasDependencyServer(
                        new XincoCoreDataJpaController(getEntityManagerFactory()).findXincoCoreData(
                        original.getId()),
                        new XincoCoreDataJpaController(getEntityManagerFactory()).findXincoCoreData(
                        rendering.getId()),
                        new XincoDependencyTypeJpaController(getEntityManagerFactory()).findXincoDependencyType(5));//rendering
                dependency.write2DB();
                LOG.log(FINE, "Marked as rendering");
                //Now create the pdf rendering
                //First save a temporary file
                String renderingFileName = rendering.getId() + ".pdf";
                rendition = new File(getXincoCoreDataPath(CONFIG.fileRepositoryPath,
                        rendering.getId(),
                        renderingFileName));
                //Convert to pdf
                getPDFRendering(xincoFile, rendition);
                addDefaultAddAttributes(rendering);
                // update attributes
                rendering.getXincoAddAttributes().get(0)
                        .setAttribVarchar(rendering.getDesignation());
                rendering.getXincoAddAttributes().get(1)
                        .setAttribUnsignedint(rendition.length());
                rendering.getXincoAddAttributes().get(2).setAttribVarchar(""
                        + new CheckedInputStream(new FileInputStream(rendition),
                        new CRC32()).getChecksum().getValue());
                rendering.getXincoAddAttributes().get(3)
                        .setAttribUnsignedint(1);
                rendering.getXincoAddAttributes().get(4)
                        .setAttribUnsignedint(0);
                service.setXincoCoreData(rendering, user);
                //Rename the file (remove extension) to meet Xinco file system
                copyFile(rendition, new File(
                        rendition.getParentFile().getAbsolutePath()
                        + getProperty("file.separator")
                        + rendering.getId()));
                rendition.delete();
                LOG.log(FINE, "Converted the file!");
            } catch (Exception ex) {
                LOG.log(WARNING, null, ex);
                //Cleanup
                if (dependency != null) {
                    dependency.deleteFromDB();
                }
                XincoCoreDataServer render =
                        new XincoCoreDataServer(rendering.getId());
                if (render != null) {
                    render.deleteFromDB();
                }
            }
        }
    }

    public static boolean getPDFRendering(File source, File dest)
            throws IllegalStateException {
        OfficeManager officeManager = null;
        try {
            //Make sure the port is available
            boolean valid = false;
            while (!valid) {
                valid = isPortAvaialble(port);
                if (!valid) {
                    //Use another port
                    port++;
                    port = port % MIN_PORT_NUMBER;
                    if (port == 0) {
                        port++;
                    }
                }
            }
            // Connect to an OpenOffice.org instance running on available port
            try {
                officeManager = new DefaultOfficeManagerConfiguration()
                        .setPortNumber(port).buildOfficeManager();
                officeManager.start();

                OfficeDocumentConverter converter =
                        new OfficeDocumentConverter(officeManager);
                converter.convert(source, dest);
                // close the connection
                officeManager.stop();
                return true;
            } catch (IllegalStateException ise) {
                //Looks like OpenOffice or LibreOffice is not installed
                LOG.warning("Unable to find OpenOffice and/or LibreOffice "
                        + "installation. Disabling rendering generation!");
                disable();
                throw ise;
            }
        } catch (OfficeException e) {
            officeManager.stop();
            throw new XincoException(e);
        }
    }

    private static void disable() {
        XincoSettingServer setting = 
                getSetting(new XincoCoreUserServer(1), 
                "setting.OOPort");
        setting.setIntValue(-1);
        setting.write2DB();
    }
}