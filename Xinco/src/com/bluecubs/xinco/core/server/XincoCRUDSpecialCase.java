package com.bluecubs.xinco.core.server;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public interface XincoCRUDSpecialCase {

    /**
     * Unusual way of clearing the table.
     * Example: references to same table (cyclical references)
     */
    public void clearTable();
}
