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

import com.bluecubs.xinco.core.server.XincoAddAttributeServer;
import com.bluecubs.xinco.core.server.service.XincoCoreData;
import com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute;
import com.vaadin.data.Item;
import com.vaadin.ui.*;
import java.util.Iterator;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
class AddAttributeDialog extends CustomComponent {

    private Table attrTable = new Table("attributes");
    private final Xinco xinco;

    AddAttributeDialog(XincoCoreData data, final Xinco xinco) {
        this.xinco = xinco;
        com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel(xinco.getResource().getString("window.addattributesuniversal"));
        panel.setContent(new VerticalLayout());
        /*
         * Define the names and data types of columns. The "default value"
         * parameter is meaningless here.
         */
        attrTable.addContainerProperty(xinco.getResource().getString("general.attribute"), String.class, null);
        attrTable.addContainerProperty(xinco.getResource().getString("general.details"), String.class, null);
        //prevent editing of fixed attributes for certain data types
        //prevent editing of fixed attributes for certain data types
        //prevent editing of fixed attributes for certain data types
        //prevent editing of fixed attributes for certain data types
        int start = 0;
        //file = 1
        if (data.getXincoCoreDataType().getId() == 1) {
            start = 8;
        }
        //text = 2
        if (data.getXincoCoreDataType().getId() == 2) {
            start = 1;
        }
        java.util.List<com.bluecubs.xinco.core.server.service.XincoAddAttribute> attributes;
        if (data.getId() == 0) {
            // Is a new data, there's nothing yet in the database.
            // Load local values.
            attributes = data.getXincoAddAttributes();
        } else {
            attributes = XincoAddAttributeServer.getXincoAddAttributes(data.getId());
        }
        java.util.List<XincoCoreDataTypeAttribute> dataTypeAttributes = data.getXincoCoreDataType().getXincoCoreDataTypeAttributes();
        int count = 0;
        for (int i = start; i < attributes.size(); i++) {
            String value = "";
            if (dataTypeAttributes.get(i).getDataType().equals("datetime")) {
                value = "" + ((XMLGregorianCalendar) (attributes.get(i)).getAttribDatetime()).toGregorianCalendar().getTime().toString();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("double")) {
                value = "" + (attributes.get(i)).getAttribDouble();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("int")) {
                value = "" + (attributes.get(i)).getAttribInt();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("text")) {
                value = "" + (attributes.get(i)).getAttribText();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("unsignedint")) {
                value = "" + (attributes.get(i)).getAttribUnsignedint();
            }
            if (dataTypeAttributes.get(i).getDataType().equals("varchar")) {
                value = "" + (xinco.getResource().containsKey((attributes.get(i)).getAttribVarchar()) ? xinco.getResource().getString((attributes.get(i)).getAttribVarchar()) : (attributes.get(i)).getAttribVarchar());
            }
            String type = xinco.getResource().containsKey(dataTypeAttributes.get(i).getDesignation()) ? xinco.getResource().getString(dataTypeAttributes.get(i).getDesignation()) : dataTypeAttributes.get(i).getDesignation();
            attrTable.addItem(new Object[]{type, value}, count++);
        }
        attrTable.setTableFieldFactory(new TableFieldFactory() {

            @Override
            public Field createField(com.vaadin.data.Container container, Object itemId, Object propertyId, com.vaadin.ui.Component uiContext) {
                if (propertyId.equals(xinco.getResource().getString("general.attribute"))) {
                    com.vaadin.ui.TextField textField = new com.vaadin.ui.TextField();
                    textField.setEnabled(false);
                    return textField;
                } else {
                    return new com.vaadin.ui.TextField();
                }
            }
        });
        attrTable.setEditable(true);
        panel.addComponent(attrTable);
        // Set the size as undefined at all levels
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();
        // The composition root MUST be set
        setCompositionRoot(panel);
    }

    public void updateAttributes() {
        for (Iterator<?> it = attrTable.getItemIds().iterator(); it.hasNext();) {
            int id = (Integer) it.next();
            Item item = attrTable.getItem(id);
            xinco.getXincoCoreData().getXincoAddAttributes().get(id).setAttribVarchar(item.getItemProperty(xinco.getResource().getString("general.details")).getValue().toString());
        }
    }
}
