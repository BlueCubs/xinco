package com.bluecubs.xinco.core.server.db;

import java.util.ResourceBundle;

/**
 * Enumeration to describe the database state
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public enum DBState {

    /*
     * Database is up to date
     */
    VALID("message.db.valid"),
    /*
     * Database needs a manual update
     * (previous to 2.1.x the database version is not stored within the database)
     */
    NEED_MANUAL_UPDATE("error.old.db"),
    /*
     * Database just updated
     */
    UPDATED("message.updated.db"),
    /*
     * Database needs to be updated
     */
    NEED_UPDATE("message.update.db"),
    /*
     * Database needs initialization
     */
    NEED_INIT("message.init.db"),
    /*
     * Error detected
     */
    ERROR("message.db.error"),
    /*
     * Start up
     */
    START_UP("message.db.startup"),
    /*
     * Updating
     */
    UPDATING("message.update.db");
    private final String mess;
    private static ResourceBundle lrb = ResourceBundle.getBundle(
            "com.bluecubs.xinco.messages.XincoMessages");

    DBState(String mess) {
        this.mess = mess;
    }

    /**
     * @return the mess
     */
    public String getMessage() {
        if (lrb.containsKey(mess)) {
            return lrb.getString(mess);
        } else {
            return mess;
        }
    }
}