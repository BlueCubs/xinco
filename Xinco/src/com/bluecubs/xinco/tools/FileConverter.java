package com.bluecubs.xinco.tools;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import java.io.File;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static void main(String[] a) {
        try {
            File inputFile = new File("C:\\document.doc");
            File outputFile = new File("C:\\document.pdf");
            // connect to an OpenOffice.org instance running on port 8100
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
            connection.connect();
            // convert
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(inputFile, outputFile);
            // close the connection
            connection.disconnect();
        } catch (ConnectException ex) {
            Logger.getLogger(FileConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static OpenOfficeConnection connectToOpenOffice() {
        OpenOfficeConnection connection = null;
        try {
            // connect to an OpenOffice.org instance running on port 8100
            connection = new SocketOpenOfficeConnection(8100);
            connection.connect();
        } catch (ConnectException ex) {
            Logger.getLogger(FileConverter.class.getName()).log(Level.SEVERE, null, ex);
            connection = null;
        }
        return connection;
    }

    public static boolean createPDFRendering(int id) {
        // connect to an OpenOffice.org instance running on port 8100
        OpenOfficeConnection connection = connectToOpenOffice();
        DocumentFormat customPdfFormat =
                new DocumentFormat("Portable Document Format", "application/pdf", "pdf");
        customPdfFormat.setExportFilter(DocumentFamily.TEXT, "writer_pdf_Export");
        HashMap pdfOptions = new HashMap();
        pdfOptions.put("EnableCopyingOfContent", Boolean.FALSE);
        customPdfFormat.setExportOption(DocumentFamily.TEXT, "FilterData", pdfOptions);
        // convert
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        converter.convert(
                new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, id, "" + id)),
                new File(XincoDBManager.config.FileRepositoryPath + id + "-pdf"), customPdfFormat);
        Logger.getLogger(FileConverter.class.getName()).log(Level.INFO, "Rendering saved at: {0}{1}-pdf", 
                new Object[]{XincoDBManager.config.FileRepositoryPath, id});
        // close the connection
        connection.disconnect();
        return true;
    }
}
