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
 * Name: DataTypeDialog
 * 
 * Description: Data Type Dialog
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Jan 26, 2012
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.XincoCoreDataTypeServer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import java.util.Iterator;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
class DataTypeDialog extends CustomComponent {

    private final Select types;

    DataTypeDialog() {
        types = new Select(Xinco.getInstance().getResource().getString("window.datatype.datatype") + ":");
        com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel(Xinco.getInstance().getResource().getString("window.datatype"));
        panel.setContent(new VerticalLayout());
        //Data Type selection
        for (Iterator it = XincoCoreDataTypeServer.getXincoCoreDataTypes().iterator(); it.hasNext();) {
            XincoCoreDataTypeServer type = (XincoCoreDataTypeServer) it.next();
            String designation = type.getDesignation();
            if (Xinco.getInstance().getResource().containsKey(designation)) {
                String value = Xinco.getInstance().getResource().getString(designation);
                types.addItem(type.getId());
                types.setItemCaption(type.getId(), value);
            }
        }
        panel.addComponent(types);
        // Set the size as undefined at all levels
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();
        // The composition root MUST be set
        setCompositionRoot(panel);
    }

    /**
     * @return the types
     */
    /**
     * @return the types
     */
    public Select getTypes() {
        return types;
    }
}
