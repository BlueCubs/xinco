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
 * Name: Class
 * 
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Jan 26, 2012
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.XincoCoreLanguageServer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
class DataDialog extends CustomComponent {

    private com.vaadin.ui.TextField idField;
    private com.vaadin.ui.TextField designationField;
    private com.vaadin.ui.TextField statusField;
    private final Select languages;

    DataDialog(boolean newData) {
        languages = new Select(Xinco.getInstance().getResource().getString("general.language") + ":");
        com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel(Xinco.getInstance().getResource().getString("window.datadetails"));
        panel.setContent(new VerticalLayout());
        //ID
        idField = new com.vaadin.ui.TextField(Xinco.getInstance().getResource().getString("general.id") + ":");
        panel.addComponent(idField);
        if (newData) {
            idField.setValue("0");
        } else {
            idField.setValue(Xinco.getInstance().getXincoCoreData().getId());
        }
        //Not editable
        idField.setEnabled(false);
        //Designation
        designationField = new com.vaadin.ui.TextField(Xinco.getInstance().getResource().getString("general.designation") + ":");
        panel.addComponent(designationField);
        designationField.setValue(Xinco.getInstance().getXincoCoreData().getDesignation());
        //Language selection
        int i = 0;
        for (Object language : XincoCoreLanguageServer.getXincoCoreLanguages()) {
            String designation = ((XincoCoreLanguageServer) language).getDesignation();
            if (Xinco.getInstance().getResource().containsKey(designation)) {
                String value = Xinco.getInstance().getResource().getString(designation);
                languages.addItem(((XincoCoreLanguageServer) language).getId());
                languages.setItemCaption(((XincoCoreLanguageServer) language).getId(), value);
                if (newData && ((XincoCoreLanguageServer) language).getSign().equals("en")) {
                    languages.setValue(((XincoCoreLanguageServer) language).getId());
                } else if (!newData && ((XincoCoreLanguageServer) language).getId() == Xinco.getInstance().getXincoCoreData().getXincoCoreLanguage().getId()) {
                    languages.setValue(((XincoCoreLanguageServer) language).getId());
                }
                i++;
            } else {
                Logger.getLogger(DataDialog.class.getName()).log(Level.WARNING, "{0} not defined in com.bluecubs.xinco.messages.XincoMessagesLocale", "Locale." + designation);
            }
            if (Xinco.getInstance().getXincoCoreData() != null && Xinco.getInstance().getXincoCoreData().getXincoCoreLanguage() != null && Xinco.getInstance().getXincoCoreData().getXincoCoreLanguage().getSign().equals(designation)) {
                languages.setValue("Locale." + designation);
            }
        }
        panel.addComponent(languages);
        //Status
        statusField = new com.vaadin.ui.TextField(Xinco.getInstance().getResource().getString("general.status") + ":");
        //Not editable
        statusField.setEnabled(false);
        String text = "";
        if (newData || Xinco.getInstance().getXincoCoreData().getStatusNumber() == 1) {
            text = Xinco.getInstance().getResource().getString("general.status.open");
        }
        if (Xinco.getInstance().getXincoCoreData() != null && Xinco.getInstance().getXincoCoreData().getStatusNumber() == 2) {
            text = Xinco.getInstance().getResource().getString("general.status.locked") + " (-)";
        }
        if (Xinco.getInstance().getXincoCoreData() != null && Xinco.getInstance().getXincoCoreData().getStatusNumber() == 3) {
            text = Xinco.getInstance().getResource().getString("general.status.archived") + " (->)";
        }
        if (Xinco.getInstance().getXincoCoreData() != null && Xinco.getInstance().getXincoCoreData().getStatusNumber() == 4) {
            text = Xinco.getInstance().getResource().getString("general.status.checkedout") + " (X)";
        }
        if (Xinco.getInstance().getXincoCoreData() != null && Xinco.getInstance().getXincoCoreData().getStatusNumber() == 5) {
            text = Xinco.getInstance().getResource().getString("general.status.published") + " (WWW)";
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
