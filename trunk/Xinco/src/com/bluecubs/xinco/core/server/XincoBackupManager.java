package com.bluecubs.xinco.core.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * This is a complex task and is heavily dependant on the architecture
 * of the database.
 * 
 * Data needs to be stored in a particular order into the database to comply 
 * with database constraints. This order can be observed in a dump file or
 * create script like the ones generated from MySQL Workbench. Using that 
 * should be enough. In case that tool is not available basically the logic is
 * populating tables from the outside inwards. From the tables with no relationships
 * or only one working to the more complex ones. As summary before a table is populated all
 * the related tables should be populated already (if we have identifying relationships.
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoBackupManager {

    private static XincoBackupManager instance;
    private static EntityManagerFactory liveEMF;
    private static EntityManagerFactory backupEMF;
    private static EntityManager live, backup;
    private static Vector<String> tables = new Vector<String>();
    private static XincoBackupFile last;
    private static String backupPath;
    public static HashMap<String, Integer> stats = new HashMap<String, Integer>();

    static {
        //Non-order-critical tables
        tables.add("XincoCoreAceT");
        tables.add("XincoCoreDataT");
        tables.add("XincoCoreDataTypeAttributeT");
        tables.add("XincoCoreGroupT");
        tables.add("XincoCoreLanguageT");
        tables.add("XincoCoreNodeT");
        tables.add("XincoCoreUserHasXincoCoreGroupT");
        tables.add("XincoCoreUserT");
        tables.add("XincoSettingT");
        tables.add("XincoSetting");
        tables.add("XincoId");
        //Order critical tables
        tables.add("XincoCoreLanguage");
        tables.add("XincoCoreNode");
        tables.add("XincoCoreDataType");
        tables.add("XincoCoreData");
        tables.add("XincoCoreUser");
        tables.add("XincoCoreUserModifiedRecord");
        tables.add("XincoCoreAce");
        tables.add("XincoAddAttribute");
        tables.add("XincoCoreDataTypeAttribute");
        tables.add("XincoCoreLog");
    }

    public static XincoBackupManager get() {
        if (instance == null) {
            instance = new XincoBackupManager();
        }
        return instance;
    }

    private static void setDBSystemDir(String systemDir) {
        // Set the db system directory.
        System.setProperty("derby.system.home", systemDir);
        Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO, "Derby home set as: " + systemDir);
        try {
            //Start the embeded DB
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initConnections() {
        try {
            liveEMF = XincoDBManager.getEntityManagerFactory();
        } catch (XincoException ex) {
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        backupEMF = Persistence.createEntityManagerFactory("XincoBackup");
    }

    protected static boolean backup() throws XincoException {
        try {
            initConnections();
            stats.clear();
            backupPath = XincoSettingServer.getSetting("setting.backup.path").getString_value();
            //We need to make sure that there's no one in the database
            XincoDBManager.setLocked(true);
            live = liveEMF.createEntityManager();
            //Prepare the backup repository. Create dirs if needed.
            File backupDir = new File(backupPath);
            backupDir.mkdirs();
            //Create folder for this backup
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            File backupNewDir = new File(backupPath + System.getProperty("file.separator")
                    + format.format(new Date()));
            backupNewDir.mkdirs();
            /**
             * Prepare system to use derby
             */
            setDBSystemDir(backupNewDir.getAbsolutePath());
            backup = backupEMF.createEntityManager();
            for (String s : tables) {
                copyEntities(s, live, backup);
            }
            /**
             * At this point we should have a <Backup Database name> folder in
             * <Backup Path>/<Date>.
             * Lets zip them for storage.
             */
            format = new SimpleDateFormat("MM dd yyyy hh-mm-ss");
            zipBackupFiles(backupNewDir, backupNewDir.getAbsolutePath()
                    + System.getProperty("file.separator") + "Xinco Backup " + format.format(new Date()));
            //Stop Derby database in order to delete
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException e) {
                //When the database shuts down it'll throw an exception
            }
            //Delete backed up files
            String dbName = (String) backup.getProperties().get("javax.persistence.jdbc.url");
            dbName = dbName.substring(dbName.lastIndexOf(":") + 1, dbName.indexOf(";"));
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO, "Deleting temp folder: " + dbName);
            FileUtils.deleteDirectory(new File(backupNewDir.getAbsolutePath()
                    + System.getProperty("file.separator") + dbName));
            //Delete Derby log file
            FileUtils.forceDelete(new File(backupNewDir.getAbsolutePath()
                    + System.getProperty("file.separator") + "derby.log"));
        } catch (XincoException ex) {
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
            XincoDBManager.setLocked(false);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
            XincoDBManager.setLocked(false);
            return false;
        } finally {
            if (live != null && live.isOpen()) {
                live.close();
            }
            if (liveEMF != null && liveEMF.isOpen()) {
                liveEMF.close();
            }
            if (backup != null && backup.isOpen()) {
                backup.close();
            }
            if (backupEMF != null && backupEMF.isOpen()) {
                backupEMF.close();
            }
        }
        XincoDBManager.setLocked(false);
        return true;
    }

    private static void zipBackupFiles(File path, String zipName) throws XincoException {
        if (!zipName.endsWith(".zip")) {
            zipName += ".zip";
        }
        // These are the files to include in the ZIP file
        IOFileFilter filter = new IOFileFilter() {

            @Override
            public boolean accept(File file) {

                if (file.isDirectory()) {
                    return true;
                }
                //Ignore other backup files
                if (file.isFile() && !file.getName().endsWith(".zip")) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean accept(File file, String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        @SuppressWarnings("unchecked")
        Collection<File> fileList = FileUtils.listFiles(path, filter, TrueFileFilter.INSTANCE);
        Object[] files = fileList.toArray();

        // Create a buffer for reading the files
        byte[] buf = new byte[1024];

        try {
            // Create the ZIP file
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipName));

            // Compress the files
            for (int i = 0; i < files.length; i++) {
                FileInputStream in = new FileInputStream((File) files[i]);
                String fileName = ((File) files[i]).getPath();
                //Remove not needed folders
                fileName = fileName.substring(fileName.indexOf(path.getAbsolutePath()) + path.getAbsolutePath().length() + 1);
                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(fileName));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
                last = new XincoBackupFile(new File(zipName));
            }
            // Complete the ZIP file
            out.close();
        } catch (IOException e) {
            throw new XincoException("Error zipping backup: " + e.getLocalizedMessage());
        }
    }

    private static void copyEntities(String table, EntityManager source, EntityManager dest) {
        List<Object> result, result2;
        result = source.createNamedQuery(table + ".findAll").getResultList();
        Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO, "Copying from table: " + table);
        int i = 0;
        source.clear();
        for (Object o : result) {
            i++;
            Class<?> persistenceClass = null;
            try {
                persistenceClass = Class.forName("com.bluecubs.xinco.core.server.persistence." + table);
                dest.getTransaction().begin();
                if (dest.contains(persistenceClass.cast(o))) {
                    //If no exception do a merge because it exists already
                    dest.merge(persistenceClass.cast(o));
                } else {
                    dest.persist(persistenceClass.cast(o));
                }
                dest.getTransaction().commit();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        stats.put(table, i);
        result2 = dest.createNamedQuery(table + ".findAll").getResultList();
        assert result2.size() == result.size();
        Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO, "Copying for table: "
                + table + " completed! Amount of records: " + i);
    }

    @SuppressWarnings({"unchecked"})
    public static Vector<XincoBackupFile> getBackupFiles() throws XincoException {
        // These are the files to include in the ZIP file
        IOFileFilter filter = new IOFileFilter() {

            @Override
            public boolean accept(File file) {
                //Only zip files
                if (file.isFile() && file.getName().endsWith(".zip")
                        && file.getName().startsWith("Xinco Backup")) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean accept(File file, String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        Collection<File> files = FileUtils.listFiles(
                new File(backupPath), filter, TrueFileFilter.INSTANCE);
        Vector<XincoBackupFile> backupFiles = new Vector<XincoBackupFile>();
        for (File f : files) {
            backupFiles.add(new XincoBackupFile(f));
        }
        return backupFiles;
    }

    protected static boolean restoreFromBackup(XincoBackupFile backupFile) throws XincoException {
        try {
            stats.clear();
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                    "Restoring database from: " + backupFile.getName());
            //First make a backup of current database just in case
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                    "Creating a restore point for your current database...");
            backup();
            //We need to make sure that there's no one in the database
            XincoDBManager.setLocked(true);
            //Load database from the provided backup
            loadDatabaseFromBackup(backupFile);
            XincoDBManager.setLocked(false);
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                    "Restore complete!");
            try {
                Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                        "Deleting restore point...");
                FileUtils.forceDelete(last);
                Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                        "Done!");
            } catch (IOException ex) {
                Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        } catch (XincoException ex) {
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
            //Recover from last backup
            loadDatabaseFromBackup(getLast());
            XincoDBManager.setLocked(false);
            throw new XincoException("Unable to load backup! Database reverted to original state. \n" + ex.getMessage());
        }
    }

    private static void loadDatabaseFromBackup(XincoBackupFile backupFile) throws XincoException {
        EntityManager backupEM = null;
        try {
            initConnections();
            live = liveEMF.createEntityManager();
            //Unzip backup
            unzipBackup(backupFile);
            //Delete current database (inverse order than writing)
            Collections.reverse(tables);
            for (String s : tables) {
                clearTable(s, live);
            }
            //Get back to original order
            Collections.reverse(tables);
            //Make derby start where the backup is
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                    "Connecting to backup data...");
            setDBSystemDir(backupPath + "Temp"
                    + System.getProperty("file.separator"));
            //Connect to backup database
            backupEM = Persistence.createEntityManagerFactory("XincoBackup").createEntityManager();
            //Start copying
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                    "Starting loading entities...");
            for (String s : tables) {
                //Copy values from backup
                copyEntities(s, backupEM, live);
            }
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                    "Load complete!");
            //Stop Derby database in order to delete
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            //When the database shuts down it'll throw an exception
        } finally {
            if (live != null && live.isOpen()) {
                live.close();
            }
            if (liveEMF != null && liveEMF.isOpen()) {
                liveEMF.close();
            }
            if (backupEM != null && backupEM.isOpen()) {
                backupEM.close();
            }
        }
    }

    private static void unzipBackup(XincoBackupFile backup) {
        try {
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(
                    new FileInputStream(backup.getBackupFile()));
            zipentry = zipinputstream.getNextEntry();
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                    "Unzipping backup file: " + backup.getName());
            while (zipentry != null) {
                //for each entry to be extracted
                String entryName = zipentry.getName();
                Logger.getLogger(XincoBackupManager.class.getName()).log(Level.FINE,
                        "Extracting file: " + entryName);
                int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(entryName);
                String directory = newFile.getParent();

                if (directory == null) {
                    if (newFile.isDirectory()) {
                        break;
                    }
                }
                if (entryName.contains(System.getProperty("file.separator"))) {
                    //Create any internal folders required
                    new File(backupPath
                            + System.getProperty("file.separator") + "Temp"
                            + System.getProperty("file.separator") + entryName.substring(
                            0, entryName.lastIndexOf(
                            System.getProperty("file.separator")))).mkdirs();
                } else {
                    File tempDir = new File(backupPath
                            + System.getProperty("file.separator") + "Temp"
                            + System.getProperty("file.separator"));
                    tempDir.mkdirs();
                }
                fileoutputstream = new FileOutputStream(backupPath
                        + System.getProperty("file.separator") + "Temp"
                        + System.getProperty("file.separator") + entryName);

                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                    fileoutputstream.write(buf, 0, n);
                }

                fileoutputstream.close();
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();

            }//while
            zipinputstream.close();
            Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO,
                    "Unzipping complete!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clearTable(String table, EntityManager target) throws XincoException {
        List<Object> result;
        result = target.createNamedQuery(table + ".findAll").getResultList();
        Logger.getLogger(XincoBackupManager.class.getName()).log(Level.INFO, "Cleaning table: " + table);
        int i = 0;
        for (Object o : result) {
            i++;
            try {
                Class<?> persistenceClass = Class.forName("com.bluecubs.xinco.core.server.persistence." + table);
                target.getTransaction().begin();
                target.remove(persistenceClass.cast(o));
                target.getTransaction().commit();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(XincoBackupManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        result = target.createNamedQuery(table + ".findAll").getResultList();
        assert result.size() == 0 : "Unable to delete entities: " + result.size();
        stats.put(table, i);
        Logger.getLogger(XincoBackupManager.class.getName()).log(Level.FINE, "Cleaning table: "
                + table + " completed! Amount of records removed: " + i);
    }

    /**
     * @return the last
     */
    public static XincoBackupFile getLast() {
        return last;
    }
}
