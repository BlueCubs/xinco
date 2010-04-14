/**
 *Copyright 2010 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            AbstractDialog
 *
 * Description:     Abstract class with common methods for Dialogs within Xinco Explorer.
 *
 * Original Author: Javier A. Ortiz
 * Date:            2010
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.client.object.abstractObject;

import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Javier A. Ortiz
 */
public abstract class AbstractDialog extends javax.swing.JDialog {

    private HashMap<JTextField, String> textFields = new HashMap<JTextField, String>();
    private HashMap<JTextArea, String> textAreas = new HashMap<JTextArea, String>();
    private HashMap<JTable, DefaultTableModel> tables = new HashMap<JTable, DefaultTableModel>();

    /**
     * Constructor
     * @param parent
     * @param modal
     */
    public AbstractDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    /**
     * Get a HashMap<JTextField, String> containing the fields of this Dialog
     * and their default values.
     *
     * @return HashMap<JTextField, String> containing the fields of this Dialog
     * and their default values
     */
    public HashMap<JTextField, String> getTextFields() {
        return textFields;
    }

    /**
     * Set text fields
     * @param textFields 
     */
    public void setTextFields(HashMap<JTextField, String> textFields) {
        this.textFields = textFields;
    }

    /**
     * Get a HashMap<JTextArea, String> containing the fields of this Dialog
     * and their default values.
     *
     * @return HashMap<JTextArea, String> containing the fields of this Dialog
     * and their default values
     */
    public HashMap<JTextArea, String> getTextAreas() {
        return textAreas;
    }

    /**
     * Set text areas
     * @param textAreas
     */
    public void setTextAreas(HashMap<JTextArea, String> textAreas) {
        this.textAreas = textAreas;
    }

    /**
     * Get a HashMap<JTable, DefaultTableModel> containing the fields of this Dialog
     * and their default values.
     *
     * @return HashMap<JTable, DefaultTableModel> containing the fields of this Dialog
     * and their default values
     */
    public HashMap<JTable, DefaultTableModel> getTables() {
        return tables;
    }

    /**
     * Set tables
     * @param tables
     */
    public void setTables(HashMap<JTable, DefaultTableModel> tables) {
        this.tables = tables;
    }

    /**
     * Add a text field or password field
     * @param field
     * @param defaultValue
     */
    @SuppressWarnings("unchecked")
    public void addTextField(JTextField field, String defaultValue) {
        getTextFields().put(field, defaultValue);
    }

    /**
     * Add a text area
     * @param area
     * @param defaultValue
     */
    @SuppressWarnings("unchecked")
    public void addTextArea(JTextArea area, String defaultValue) {
        getTextAreas().put(area, defaultValue);
    }

    /**
     * Add a table
     * @param table
     * @param defaultValue
     */
    @SuppressWarnings("unchecked")
    public void addTable(JTable table, DefaultTableModel defaultValue) {
        getTables().put(table, defaultValue);
    }

    /**
     * Clear contents of all fields and/or areas in the dialog
     */
    public void clearDialog() {
        for (Entry<JTextField, String> e : getTextFields().entrySet()) {
            e.getKey().setText("");
        }
        for (Entry<JTextArea, String> e : getTextAreas().entrySet()) {
            e.getKey().setText("");
        }
        for (Entry<JTable, DefaultTableModel> e : getTables().entrySet()) {
            DefaultTableModel dm = (DefaultTableModel) e.getKey().getModel();
            dm.getDataVector().removeAllElements();
        }
    }

    public void setToDefaults() {
        for (Entry<JTextField, String> e : getTextFields().entrySet()) {
            e.getKey().setText(e.getValue());
        }
        for (Entry<JTextArea, String> e : getTextAreas().entrySet()) {
            e.getKey().setText(e.getValue());
        }
        for (Entry<JTable, DefaultTableModel> e : getTables().entrySet()) {
            e.getKey().setModel(e.getValue());
        }
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            clearDialog();
            setToDefaults();
        }
        super.setVisible(b);
    }
}
