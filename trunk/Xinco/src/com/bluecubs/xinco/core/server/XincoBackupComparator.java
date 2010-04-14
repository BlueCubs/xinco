package com.bluecubs.xinco.core.server;

import java.util.Comparator;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoBackupComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        assert o1 instanceof XincoBackupFile && o2 instanceof XincoBackupFile;
        XincoBackupFile file1 = (XincoBackupFile) o1, file2 = (XincoBackupFile) o2;
        if (file1.getBackupDate().before(file1.getBackupDate())) {
            return -1;
        }
        if (file1.getBackupDate().equals(file1.getBackupDate())) {
            return 0;
        }
        if (file1.getBackupDate().after(file1.getBackupDate())) {
            return 1;
        } else {
            throw new XincoException("Unable to compare backup files: " + file1 + " and " + file2);
        }
    }
}
