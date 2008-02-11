/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.tools.dbUpgrade;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz
 */
public class DatabaseComparator {

    private Connection connection;

    public String[] usedKeywords() {
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            return dbmd.getSQLKeywords().split(",\\s*");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseComparator.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public String[] stringFunctions() {
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            return dbmd.getStringFunctions().split(",\\s*");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseComparator.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public String[] numericFunctions() {
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            return dbmd.getNumericFunctions().split(",\\s*");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseComparator.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public String[] systemFunctions() {
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            return dbmd.getSystemFunctions().split(",\\s*");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseComparator.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public String[] timeDateFunctions() {
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            return dbmd.getTimeDateFunctions().split(",\\s*");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseComparator.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public int maxTableLength() {
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            return dbmd.getMaxTableNameLength();
        } catch (SQLException e) {
            Logger.getLogger(DatabaseComparator.class.getName()).log(Level.SEVERE, null, e);
            return -1;
        }
    }

    // This method returns the name of a JDBC type.
    // Returns null if jdbcType is not recognized.
    @SuppressWarnings("unchecked")
    public static String getJdbcTypeName(int jdbcType) {
        HashMap map = null;
        // Use reflection to populate a map of int values to names
        if (map == null) {
            map = new HashMap();

            // Get all field in java.sql.Types
            Field[] fields = java.sql.Types.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                try {
                    // Get field name
                    String name = fields[i].getName();

                    // Get field value
                    Integer value = (Integer) fields[i].get(null);

                    // Add to map
                    map.put(value, name);
                } catch (IllegalAccessException e) {
                }
            }
        }

        // Return the JDBC type name
        return (String) map.get(new Integer(jdbcType));
    }
    
    /**
     * Returns the column names of the query
     * @param rs
     * @return String
     */
    public String getColumnNames(ResultSet rs) {
        String header = "";
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            // Get the column names; column indices start from 1
            for (int i = 1; i < numColumns + 1; i++) {
                header += rsmd.getColumnName(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return header;
    }
}
