/*
 * Copyright 2012 blueCubs.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: DataDialog
 * 
 * Description: Data Dialog
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Jan 26, 2012
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.XincoCoreLanguageServer;
import static com.bluecubs.xinco.core.server.XincoCoreLanguageServer.getXincoCoreLanguages;
import static com.bluecubs.xinco.core.server.vaadin.Xinco.getInstance;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
class DataDialog extends CustomComponent {

    private com.vaadin.ui.TextField idField;
    private com.vaadin.ui.TextField designationField;
    private com.vaadin.ui.TextField statusField;
    private final Select languages;

    DataDialog(boolean newData) {
        languages = new Select(getInstance().getResource().getString(
                "general.language") + ":");
        com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel(getInstance()
                .getResource().getString("window.datadetails"));
        panel.setContent(new VerticalLayout());
        //ID
        idField = new com.vaadin.ui.TextField(getInstance().getResource()
                .getString("general.id") + ":");
        panel.addComponent(idField);
        if (newData) {
            idField.setValue("0");
        } else {
            idField.setValue(getInstance().getXincoCoreData().getId());
        }
        //Not editable
        idField.setEnabled(false);
        //Designation
        designationField = new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.designation") + ":");
        panel.addComponent(designationField);
        designationField.setValue(getInstance().getXincoCoreData()
                .getDesignation());
        //Language selection
        for (Object language : getXincoCoreLanguages()) {
            String designation = 
                    ((XincoCoreLanguageServer) language).getDesignation();
            if (getInstance().getResource().containsKey(designation)) {
                String value = 
                        getInstance().getResource()
                        .getString(designation);
                languages.addItem(((XincoCoreLanguageServer) language).getId());
                languages.setItemCaption(((XincoCoreLanguageServer) language)
                        .getId(), value);
                if (newData && ((XincoCoreLanguageServer) language).getSign()
                        .equals("en")) {
                    languages.setValue(((XincoCoreLanguageServer) language)
                            .getId());
                } else if (!newData && ((XincoCoreLanguageServer) language)
                        .getId() == getInstance().getXincoCoreData()
                        .getXincoCoreLanguage().getId()) {
                    languages.setValue(((XincoCoreLanguageServer) language)
                            .getId());
                }
            } else {
                getLogger(DataDialog.class.getName()).log(WARNING, 
                        "{0} not defined in XincoMessagesLocale", "Locale." 
                        + designation);
            }
            if (getInstance().getXincoCoreData() != null 
                    && getInstance().getXincoCoreData()
                    .getXincoCoreLanguage() != null 
                    && getInstance().getXincoCoreData()
                    .getXincoCoreLanguage().getSign().equals(designation)) {
                languages.setValue("Locale." + designation);
            }
        }
        panel.addComponent(languages);
        //Status
        statusField = new com.vaadin.ui.TextField(getInstance()
                .getResource().getString("general.status") + ":");
        //Not editable
        statusField.setEnabled(false);
        String text = "";
        if (newData || getInstance().getXincoCoreData()
                .getStatusNumber() == 1) {
            text = getInstance().getResource()
                    .getString("general.status.open");
        }
        if (getInstance().getXincoCoreData() != null 
                && getInstance().getXincoCoreData()
                .getStatusNumber() == 2) {
            text = getInstance().getResource()
                    .getString("general.status.locked") + " (-)";
        }
        if (getInstance().getXincoCoreData() != null 
                && getInstance().getXincoCoreData()
                .getStatusNumber() == 3) {
            text = getInstance().getResource()
                    .getString("general.status.archived") + " (->)";
        }
        if (getInstance().getXincoCoreData() != null 
                && getInstance().getXincoCoreData()
                .getStatusNumber() == 4) {
            text = getInstance().getResource()
                    .getString("general.status.checkedout") + " (X)";
        }
        if (getInstance().getXincoCoreData() != null 
                && getInstance().getXincoCoreData()
                .getStatusNumber() == 5) {
            text = getInstance().getResource()
                    .getString("general.status.published") + " (WWW)";
        }
        statusField.setValue(text);
        panel.addComponent(statusField);
        // Set the size as undefined at all levels
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();
        // The composition root MUST be set
        setCompositionRoot(panel);
    }

    /**
     * @return the idField
     */
    public com.vaadin.ui.TextField getIdField() {
        return idField;
    }

    /**
     * @return the designationField
     */
    public com.vaadin.ui.TextField getDesignationField() {
        return designationField;
    }

    /**
     * @return the statusField
     */
    public com.vaadin.ui.TextField getStatusField() {
        return statusField;
    }

    /**
     * @return the languages
     */
    public Select getLanguages() {
        return languages;
    }
}
