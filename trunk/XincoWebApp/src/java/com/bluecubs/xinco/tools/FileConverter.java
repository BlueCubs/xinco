package com.bluecubs.xinco.tools;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

/**
 * Name  Value Type Default Value Possible Values
 * EncryptFile Boolean false
 * DocumentOpenPassword String
 * RestrictPermissions Boolean false
 * PermissionPassword String
 * Printing Integer 2 0: Not permitted
 * 1: Low resolution (150 DPI)
 * 2: High resolution
 * Changes Integer 4 0: Not permitted
 * 1: Inserting, deleting and rotating pages
 * 2: Filling in form fields
 * 3: Commenting, filling in form fields
 * 4: Any except extracting pages
 * EnableCopyingOfContent Boolean true
 * EnableTextAccessForAccessibilityTools Boolean true
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class FileConverter {

    public static void main(String[] args) {
        OfficeManager officeManager = new DefaultOfficeManagerConfiguration().buildOfficeManager();
        officeManager.start();

        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(new File("C:\\document.docx"), new File("C:\\document.pdf"));

        officeManager.stop();
    }

    public static boolean getPDFRendering(int id) {
        // connect to an OpenOffice.org instance running on port 8100
        OfficeManager officeManager = new DefaultOfficeManagerConfiguration().setPortNumber(XincoSettingServer.getSetting(new XincoCoreUserServer(1),
                "setting.OOPort").getIntValue()).buildOfficeManager();
        officeManager.start();

        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(
                new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.fileRepositoryPath, id, "" + id)),
                new File(XincoDBManager.config.fileRepositoryPath + id + ".pdf"));
        Logger.getLogger(FileConverter.class.getName()).log(Level.INFO, "Rendering saved at: {0}{1}-pdf",
                new Object[]{XincoDBManager.config.fileRepositoryPath, id});
        // close the connection
        officeManager.stop();
        return true;
    }

    private FileConverter() {
    }
}
