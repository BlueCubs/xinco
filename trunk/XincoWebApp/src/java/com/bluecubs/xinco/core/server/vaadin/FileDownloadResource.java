package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.XincoConfigSingletonServer;
import com.vaadin.Application;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.FileResource;
import java.io.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class FileDownloadResource extends FileResource {

    private final String fileName;
    private File download;
    private File newFile;

    public FileDownloadResource(File sourceFile, String fileName,
            Application application) {
        super(sourceFile, application);
        this.fileName = fileName;
    }

    protected void cleanup() {
        if (newFile != null && newFile.exists()) {
            newFile.delete();
        }
        if (download != null && download.exists() && download.listFiles().length == 0) {
            download.delete();
        }
    }

    @Override
    public DownloadStream getStream() {
        try {
            //Copy file to directory for downloading
            InputStream in = new CheckedInputStream(new FileInputStream(getSourceFile()),
                    new CRC32());
            download = new File(XincoConfigSingletonServer.getInstance().FileRepositoryPath
                    + System.getProperty("file.separator") + UUID.randomUUID().toString());
            newFile = new File(download.getAbsolutePath() + System.getProperty("file.separator") + fileName);
            download.mkdirs();
            OutputStream out = new FileOutputStream(newFile);
            newFile.deleteOnExit();
            download.deleteOnExit();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            final DownloadStream ds = new DownloadStream(
                    new FileInputStream(newFile), getMIMEType(), fileName);
            ds.setParameter("Content-Disposition", "attachment; filename="+ fileName);
            ds.setCacheTime(getCacheTime());
            return ds;
        } catch (final FileNotFoundException ex) {
            Logger.getLogger(FileDownloadResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(FileDownloadResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
