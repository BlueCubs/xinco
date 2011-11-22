/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server;

import java.util.List;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public interface ConfigurationManager {

    /**
     * 
     * @return FileIndexOptimizerPeriod
     */
    long getFileIndexOptimizerPeriod();

    /**
     * @return the OOPort
     */
    int getOOPort();

    /**
     * 
     * @return Outside links allowed?
     */
    boolean isAllowOutsideLinks();

    /**
     * 
     * @return Publisher list allowed?
     */
    boolean isAllowPublisherList();

    /**
     * @return the guessLanguage
     */
    boolean isGuessLanguage();

    /**
     * Load settings from DB
     */
    void loadSettings();
    
    /**
     * @return the JNDIDB
     */
    public String getJNDIDB();
    
    /**
     * 
     * @return FileArchivePeriod
     */
    public long getFileArchivePeriod();
    
    /**
     * @return the FileArchivePath
     */
    public String getFileArchivePath();

    /**
     * @return the FileIndexerCount
     */
    public long getFileIndexerCount();

    /**
     * @return the IndexFileTypesClass
     */
    public List getIndexFileTypesClass();

    /**
     * @return the IndexFileTypesExt
     */
    public List getIndexFileTypesExt();

    /**
     * @return the IndexNoIndex
     */
    public String[] getIndexNoIndex();

    /**
     * @return the MaxSearchResult
     */
    public int getMaxSearchResult();

    /**
     * @return the loadDefault
     */
    public boolean isLoadDefault();
    
    /**
     * @return the FileRepositoryPath
     */
    public String getFileRepositoryPath();

    /**
     * @return the FileIndexPath
     */
    public String getFileIndexPath();
}
