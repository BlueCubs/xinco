package com.bluecubs.xinco.core.server;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This represents a Xinco backup Zip file
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoBackupFile extends File {

  private Date backupDate;
  private File backupFile;

  public XincoBackupFile(File backupFile) {
    super(backupFile.getAbsolutePath());
    this.backupFile = backupFile;
    SimpleDateFormat format = new SimpleDateFormat("MM dd yyyy hh-mm-ss");
    String fileName=backupFile.getName();
    fileName=fileName.substring("Xinco Backup".length()+1);
    try {
      backupDate = format.parse(fileName);
    } catch (ParseException ex) {
      Logger.getLogger(XincoBackupFile.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * @return the backupDate
   */
  public Date getBackupDate() {
    return backupDate;
  }

  /**
   * @return the backupFile
   */
  public File getBackupFile() {
    return backupFile;
  }

  @Override
  public String toString() {
    return "File name: "+backupFile.getName()+"\n"
            +"Backup time: "+backupDate;
  }
}
