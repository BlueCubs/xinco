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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import scriptella.execution.EtlExecutor;
import scriptella.execution.EtlExecutorException;
import scriptella.execution.ExecutionStatistics;
import scriptella.execution.ExecutionStatistics.ElementInfo;

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
 * the related tables should be populated already (if we have identifying relationships).
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoBackupManager {

    private static XincoBackupManager instance;
    private static EntityManagerFactory liveEMF;
    private static EntityManagerFactory backupEMF;
    private static EntityManager live, backup;
    private static XincoBackupFile last;
    private static String backupPath;

    public static XincoBackupManager get() {
        if (instance == null) {
            instance = new XincoBackupManager();
        }
        return instance;
    }

    protected static void setDBSystemDir(String systemDir) {
        // Set the db system directory.
        System.setProperty("derby.system.home", systemDir);
        Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                "Derby home set at: {0}", systemDir);
        try {
            //Start the embeded DB
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initConnections() {
        try {
            liveEMF = XincoDBManager.getEntityManagerFactory();
        } catch (XincoException ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
        try {
            backupEMF = Persistence.createEntityManagerFactory("XincoBackup");
        } catch (Exception ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static HashMap<String, Object> getScriptellaProperties(EntityManagerFactory source, EntityManagerFactory dest) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.putAll(getScriptellaProperties(source, 1));
        properties.putAll(getScriptellaProperties(dest, 2));
        return properties;
    }

    protected static HashMap<String, Object> getScriptellaProperties(EntityManagerFactory emf, int id) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("url" + id,
                emf.getProperties().get("javax.persistence.jdbc.url").toString().replaceAll(";create=true", ""));
        properties.put("user" + id, emf.getProperties().get("javax.persistence.jdbc.user"));
        properties.put("pass" + id, emf.getProperties().get("javax.persistence.jdbc.password"));
        return properties;
    }

    @SuppressWarnings("unchecked")
    protected static List<Object> namedQuery(EntityManager em, String query, HashMap<String, Object> parameters) throws XincoException {
        Query q = null;
        q = em.createNamedQuery(query);
        if (parameters != null) {
            Iterator<Map.Entry<String, Object>> entries = parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> e = entries.next();
                q.setParameter(e.getKey().toString(), e.getValue());
            }
        }
        return q.getResultList();
    }

    protected static boolean backup() throws XincoException {
        HashMap<String, Integer> stats = new HashMap<String, Integer>(),
                afterStats = new HashMap<String, Integer>();
        try {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Initializing connections...");
            initConnections();
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
            /*
             * Make sure there's no derby database stuff in the folder.
             * Any previous interrupted backup might left corrupted database files.
             */
            File tempDir = new File(backupNewDir.getAbsolutePath()
                    + System.getProperty("file.separator") + "xinco");
            if (tempDir.exists()) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.WARNING,
                        "Deleting potentially corrupted database files at: {0}", tempDir);
                FileUtils.deleteDirectory(tempDir);
                //Delete Derby log file
                FileUtils.forceDelete(new File(backupNewDir.getAbsolutePath()
                        + System.getProperty("file.separator") + "derby.log"));
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINE,
                        "Done!");
            }
            /**
             * Prepare system to use derby
             */
            setDBSystemDir(backupNewDir.getAbsolutePath());
            backup = backupEMF.createEntityManager();
            try {
                ExecutionStatistics executionStats = EtlExecutor.newExecutor(XincoBackupManager.class.getResource("db/scripts/backup.xml"), XincoBackupManager.getScriptellaProperties(liveEMF, backupEMF)).execute();
                int bad = 0;
                for (ElementInfo ei : executionStats.getElements()) {
                    bad += ei.getFailedExecutionCount();
                }
                if (bad > 0) {
                    throw new XincoException("Error during backup");
                }
            } catch (EtlExecutorException ex) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
                throw new XincoException(ex.getLocalizedMessage());
            }
            //Check the copy integrity
            for (EntityType et : liveEMF.getMetamodel().getEntities()) {
                stats.put(et.getName(), namedQuery(live, et.getName() + ".findAll", null).size());
            }
            for (EntityType et : backupEMF.getMetamodel().getEntities()) {
                afterStats.put(et.getName(), namedQuery(backup, et.getName() + ".findAll", null).size());
            }
            if (stats.size() != afterStats.size()) {
                throw new XincoException("Incomplete copy");
            }
            for (Entry e : stats.entrySet()) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINE,
                        "{0}: {1}", new Object[]{e.getKey(), e.getValue()});
                if (e.getValue() != afterStats.get(e.getKey().toString())) {
                    throw new XincoException("Record amount mismatch! Before: "
                            + e.getKey() + ", Now: " + afterStats.get((String) e.getKey())
                            + " Then: " + e.getValue() + " " + (e.getValue()
                            == afterStats.get((String) e.getKey())));
                } else {
                    Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINE,
                            "Record amount match! Before: {0}, Now: {1} Then: {2} {3}",
                            new Object[]{e.getKey(), afterStats.get((String) e.getKey()),
                                e.getValue(), e.getValue() == afterStats.get((String) e.getKey())});
                }
            }
            /**
             * At this point we should have a <Backup Database name> folder in
             * <Backup Path>/<Date>.
             * Lets zip them for storage.
             */
            format = new SimpleDateFormat("MM dd yyyy hh-mm-ss");
            zipBackupFiles(backupDir, backupNewDir.getAbsolutePath()
                    + System.getProperty("file.separator") + "Xinco Backup " + format.format(new Date()));
            //Stop Derby database in order to delete
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException e) {
                //When the database shuts down it'll throw an exception
            }
            //Delete backed up files
            String dbName = (String) backup.getProperties().get("javax.persistence.jdbc.url");
            dbName = dbName.substring(dbName.lastIndexOf(":") + 1, dbName.indexOf(";") > -1 ? dbName.indexOf(";") : dbName.length());
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Deleting temp folder: {0}", dbName);
            FileUtils.deleteDirectory(new File(backupNewDir.getAbsolutePath()
                    + System.getProperty("file.separator") + dbName));
            //Delete Derby log file
            FileUtils.forceDelete(new File(backupNewDir.getAbsolutePath()
                    + System.getProperty("file.separator") + "derby.log"));
        } catch (XincoException ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            XincoDBManager.setLocked(false);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            XincoDBManager.setLocked(false);
            return false;
        } finally {
            if (live != null && live.isOpen()) {
                live.close();
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

    @SuppressWarnings({"unchecked"})
    public static ArrayList<XincoBackupFile> getBackupFiles() throws XincoException {
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
        ArrayList<XincoBackupFile> backupFiles = new ArrayList<XincoBackupFile>();
        for (File f : files) {
            backupFiles.add(new XincoBackupFile(f));
        }
        //Sort
        Collections.sort(backupFiles, new XincoBackupComparator());
        //Sorted from oldest to newer so we need to invert the list.
        Collections.reverse(backupFiles);
        return backupFiles;
    }

    protected static boolean restoreFromBackup(XincoBackupFile backupFile) throws XincoException {
        try {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Restoring database from: {0}", backupFile.getName());
            //First make a backup of current database just in case
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Creating a restore point for your current database...");
            backup();
            //We need to make sure that there's no one in the database
            XincoDBManager.setLocked(true);
            //Load database from the provided backup
            loadDatabaseFromBackup(backupFile);
            XincoDBManager.setLocked(false);
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Restore complete!");
            try {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                        "Deleting restore point...");
                FileUtils.forceDelete(last);
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                        "Done!");
            } catch (IOException ex) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            }
            return true;
        } catch (XincoException ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            //Recover from last backup
            loadDatabaseFromBackup(getLast());
            XincoDBManager.setLocked(false);
            throw new XincoException("Unable to load backup! Database reverted to original state. \n" + ex.getMessage());
        }
    }

    /**
     * Get the execution order from the EntityManager meta data model.
     *
     * This will fail if the EntityManager is not JP2 compliant
     * @param em EntityManager to get the metadata from
     * @return ArrayList containing the order to process tables
     */
    protected static ArrayList<String> getProcessingOrder(EntityManager em) {
        ArrayList<String> tables = new ArrayList<String>();
        //This holds the amount of relationships and the tables with that same amount
        HashMap<Integer, ArrayList<String>> tableStats = new HashMap<Integer, ArrayList<String>>();
        //This holds the table and the tables referenced by it
        HashMap<String, ArrayList<String>> references = new HashMap<String, ArrayList<String>>();
        for (EntityType et : em.getMetamodel().getEntities()) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINER, et.getName());
            int amount = 0;
            Iterator<SingularAttribute> sIterator = et.getSingularAttributes().iterator();
            while (sIterator.hasNext()) {
                SingularAttribute next = sIterator.next();
                switch (next.getPersistentAttributeType()) {
                    case BASIC:
                    case ELEMENT_COLLECTION:
                    case EMBEDDED:
                    case ONE_TO_MANY:
                    case ONE_TO_ONE:
                        Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINER,
                                "Ignoring: {0}", next.getName());
                        break;
                    case MANY_TO_MANY:
                    case MANY_TO_ONE:
                        Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.INFO,
                                "{3} has a {2} relationship: {0} with: {1}",
                                new Object[]{next.getName(), next.getBindableJavaType(),
                                    next.getPersistentAttributeType().name(), et.getName()});
                        if (!references.containsKey(et.getName())) {
                            references.put(et.getName(), new ArrayList<String>());
                        }
                        references.get(et.getName()).add(next.getBindableJavaType().getSimpleName());
                        amount++;
                        break;
                    default:
                        Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE,
                                "Unexpected value: {0}", next.getName());
                        break;
                }
            }
            Iterator<PluralAttribute> pIterator = et.getPluralAttributes().iterator();
            while (pIterator.hasNext()) {
                PluralAttribute next = pIterator.next();
                switch (next.getPersistentAttributeType()) {
                    case BASIC:
                    case ELEMENT_COLLECTION:
                    case EMBEDDED:
                    case ONE_TO_MANY:
                    case MANY_TO_MANY:
                        Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINER,
                                "Ignoring: {0}", next.getName());
                        break;
                    case MANY_TO_ONE:
                    case ONE_TO_ONE:
                        Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.INFO,
                                "{3} has a {2} relationship: {0} with: {1}",
                                new Object[]{next.getName(), next.getBindableJavaType(),
                                    next.getPersistentAttributeType().name(), et.getName()});
                        if (!references.containsKey(et.getName())) {
                            references.put(et.getName(), new ArrayList<String>());
                        }
                        references.get(et.getName()).add(next.getBindableJavaType().getSimpleName());
                        amount++;
                        break;
                    default:
                        Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE,
                                "Unexpected value: {0}", next.getName());
                        break;
                }
            }
            if (!tableStats.containsKey(amount)) {
                tableStats.put(amount, new ArrayList<String>());
            }
            tableStats.get(amount).add(et.getName());
        }
        Iterator<String> iterator = references.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            Iterator<String> iterator1 = references.get(next).iterator();
            StringBuilder refs = new StringBuilder();
            while (iterator1.hasNext()) {
                refs.append(iterator1.next()).append("\n");
            }
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINER, "References for {0}:\n{1}", new Object[]{next, refs.toString()});
        }
        //Need to sort entities with relationships even further
        ArrayList<String> temp = new ArrayList<String>();
        for (Entry<Integer, ArrayList<String>> e : tableStats.entrySet()) {
            if (e.getKey() > 0) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.INFO, "Tables with {0} references", e.getKey());
                for (String t : e.getValue()) {
                    //Check the relationships of the tables
                    boolean ready = true;
                    for (String ref : references.get(t)) {
                        if (!temp.contains(ref)) {
                            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.INFO,
                                    "{0} is not ready. Referenced table {1} is not ready yet", new Object[]{t, ref});
                            ready = false;
                        }
                    }
                    if (ready) {
                        Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.INFO, "{0} is ready.", t);
                        temp.add(t);
                    }
                }
            } else {
                temp.addAll(e.getValue());
            }
        }
        tables.addAll(temp);
        for (Entry<Integer, ArrayList<String>> e : tableStats.entrySet()) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINER,
                    "Amount of relationships: {0}", e.getKey());
            StringBuilder list = new StringBuilder();
            for (String t : e.getValue()) {
                list.append(t).append("\n");
            }
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINER, list.toString());
        }
        return tables;
    }

    protected static void loadDatabaseFromBackup(XincoBackupFile backupFile) throws XincoException {
        EntityManager backupEM = null;
        try {
            initConnections();
            live = liveEMF.createEntityManager();
            XincoDBManager.setLocked(true);
            //Unzip backup
            unzipBackup(backupFile);
            //Delete current database (inverse order than writing)
            ArrayList<String> tables = getProcessingOrder(live);
            for (String s : tables) {
                clearTable(s, live);
            }
            //Make derby start where the backup is
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Connecting to backup data...");
            setDBSystemDir(backupPath + "Temp"
                    + System.getProperty("file.separator"));
            //Connect to backup database
            backupEM = Persistence.createEntityManagerFactory("XincoBackup").createEntityManager();
            //Start copying
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Starting loading entities...");
            try {
                //The same thing but backwards...
                ExecutionStatistics executionStats = EtlExecutor.newExecutor(XincoBackupManager.class.getResource("db/scripts/backup.xml"),
                        XincoBackupManager.getScriptellaProperties(backupEMF, liveEMF)).execute();
                int bad = 0;
                for (ElementInfo ei : executionStats.getElements()) {
                    bad += ei.getFailedExecutionCount();
                }
                if (bad > 0) {
                    throw new XincoException("Error during backup");
                }
            } catch (EtlExecutorException ex) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Load complete!");
            try {
                //Stop Derby database in order to delete
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException e) {
                //When the database shuts down it'll throw an exception
            }
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Delete temp folder!");
            try {
                FileUtils.deleteDirectory(new File(System.getProperty("derby.system.home")));
            } catch (IOException ex) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            }
            XincoDBManager.setLocked(false);
        } catch (Exception e) {
            XincoDBManager.setLocked(false);
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, e);
            throw new XincoException(e.getLocalizedMessage());
        } finally {
            if (live != null && live.isOpen()) {
                live.close();
            }
            if (backupEM != null && backupEM.isOpen()) {
                backupEM.close();
            }
        }
    }

    private static void unzipBackup(XincoBackupFile backup) {
        try {
            //Make sure that the temp directory is empty before unzipping
            FileUtils.deleteDirectory(new File(backupPath
                    + System.getProperty("file.separator") + "Temp"));
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(
                    new FileInputStream(backup.getBackupFile()));
            zipentry = zipinputstream.getNextEntry();
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Unzipping backup file: {0}", backup.getName());
            while (zipentry != null) {
                //for each entry to be extracted
                String entryName = zipentry.getName();
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                        "Extracting file: {0}", entryName);
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
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Unzipping complete!");
        } catch (Exception e) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE,
                    "Error unzipping file!", e);
        }
    }

    private static void clearTable(String table, EntityManager target) throws XincoException {
        try {
            List<Object> result;
            result = target.createNamedQuery(table + ".findAll").getResultList();
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Cleaning table: {0}", table);
            int i = 0;
            Class<?> serverClass = null;
            boolean special = false;
            try {
                serverClass = Class.forName("com.bluecubs.xinco.core.server." + table + "Server");
                special = serverClass.newInstance() instanceof XincoCRUDSpecialCase;
            } catch (ClassNotFoundException ex) {
                try {
                    //Class doesn't exist, try in the add folder
                    serverClass = Class.forName("com.bluecubs.xinco.add.server." + table + "Server");
                    special = serverClass.newInstance() instanceof XincoCRUDSpecialCase;
                } catch (ClassNotFoundException ex1) {
                } catch (InstantiationException ex1) {
                } catch (NoClassDefFoundError ex1) {
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
                throw new XincoException(ex.getLocalizedMessage());
            } catch (NoClassDefFoundError ex) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
                throw new XincoException(ex.getLocalizedMessage());
            }
            if (serverClass != null && special) {
                Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.INFO,
                        "Instance of XincoCRUDSpecialCase: {0}. Deleting in special way", table);
                ((XincoCRUDSpecialCase) serverClass.newInstance()).clearTable();
                special = false;
            } else {
                for (Object o : result) {
                    i++;
                    try {
                        Class<?> persistenceClass = Class.forName("com.bluecubs.xinco.core.server.persistence." + table);
                        target.getTransaction().begin();
                        target.remove(persistenceClass.cast(o));
                        target.getTransaction().commit();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
                        throw new XincoException(ex.getLocalizedMessage());
                    }
                }
            }
            result = target.createNamedQuery(table + ".findAll").getResultList();
            if (!result.isEmpty()) {
                throw new IllegalStateException("Unable to delete entities: " + result.size());
            }
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.FINEST,
                    "Cleaning table: {0} completed! Amount of records removed: {1}", new Object[]{table, i});
        } catch (IllegalAccessException ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex.getLocalizedMessage());
        } catch (InstantiationException ex) {
            Logger.getLogger(XincoBackupManager.class.getSimpleName()).log(Level.SEVERE, null, ex);
            throw new XincoException(ex.getLocalizedMessage());
        }
    }

    /**
     * @return the last
     */
    public static XincoBackupFile getLast() {
        return last;
    }
}
