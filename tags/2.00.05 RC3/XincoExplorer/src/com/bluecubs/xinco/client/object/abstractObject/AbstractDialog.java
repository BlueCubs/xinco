/**
 *Copyright 2008 blueCubs.com
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
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.client.object.abstractObject;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Javier A. Ortiz
 */
public abstract class AbstractDialog extends javax.swing.JDialog {

    private Vector textFields = new Vector();
    private Vector textAreas = new Vector();
    private Vector tables = new Vector();

    /**
     * Constructor
     * @param parent
     * @param modal
     */
    public AbstractDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    /**
     * Get a vector containing the fields of this Dialog
     * @return Vector containing the fields of this Dialog
     */
    public Vector getTextFields() {
        return textFields;
    }

    /**
     * Set text fields
     * @param textFields 
     */
    public void setTextFields(Vector textFields) {
        this.textFields = textFields;
    }

    /**
     * Get a vector containing the text areas of this Dialog
     * @return Vector containing the text areas of this Dialog
     */
    public Vector getTextAreas() {
        return textAreas;
    }

    /**
     * Set text areas
     * @param textAreas
     */
    public void setTextAreas(Vector textAreas) {
        this.textAreas = textAreas;
    }

    /**
     * Get a vector containing the tables of this Dialog
     * @return Vector containing the tables of this Dialog
     */
    public Vector getTables() {
        return tables;
    }

    /**
     * Set tables
     * @param tables
     */
    public void setTables(Vector tables) {
        this.tables = tables;
    }

    /**
     * Add a text field or password field
     * @param field
     */
    @SuppressWarnings("unchecked")
    public void addTextField(JTextField field) {
        getTextFields().add(field);
    }

    /**
     * Add a text area
     * @param area
     */
    @SuppressWarnings("unchecked")
    public void addTextArea(JTextArea area) {
        getTextAreas().add(area);
    }

    /**
     * Add a table
     * @param table 
     */
    @SuppressWarnings("unchecked")
    public void addTable(JTable table) {
        getTables().add(table);
    }

    /**
     * Clear contents of all fields and/or areas in the dialog
     */
    @SuppressWarnings("unchecked")
    public void clearDialog() {
        for (int i = 0; i < getTextFields().size(); i++) {
            ((JTextField) getTextFields().get(i)).setText("");
        }
        for (int i = 0; i < getTextAreas().size(); i++) {
            ((JTextArea) getTextAreas().get(i)).setText("");
        }
        for (int i = 0; i < getTables().size(); i++) {
            DefaultTableModel dm = (DefaultTableModel) ((JTable) getTables().get(i)).getModel();
            dm.getDataVector().removeAllElements();
        }
    }
}
