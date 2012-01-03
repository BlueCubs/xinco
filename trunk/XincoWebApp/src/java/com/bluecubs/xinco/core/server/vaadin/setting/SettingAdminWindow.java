/*
 * Copyright 2011 blueCubs.com.
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
 * Name: SettingAdminWindow
 * 
 * Description: Admin window for managing settings
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Dec 14, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin.setting;

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.vaadin.XincoVaadinApplication;
import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import java.util.ResourceBundle;
import org.openide.util.Lookup;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class SettingAdminWindow extends Panel implements ComponentContainer {

    private Table table;
    private EntityContainer<com.bluecubs.xinco.core.server.persistence.XincoSetting> container;

    public SettingAdminWindow() {
        buildMainArea();
    }

    private void buildMainArea() {
        VerticalLayout vl = new VerticalLayout();
        addComponent(vl);
        HorizontalLayout toolbar = new HorizontalLayout();
        container = JPAContainerFactory.make(com.bluecubs.xinco.core.server.persistence.XincoSetting.class,
                XincoDBManager.getEntityManagerFactory().createEntityManager());
        table = new Table(null, container);
        table.addStyleName("striped");
        com.vaadin.ui.Button newButton =
                new com.vaadin.ui.Button(getResource().getString("general.add"));
        newButton.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                final BeanItem<com.bluecubs.xinco.core.server.persistence.XincoSetting> newSettingItem =
                        new BeanItem<com.bluecubs.xinco.core.server.persistence.XincoSetting>(new com.bluecubs.xinco.core.server.persistence.XincoSetting());
                SettingEditor settingEditor =
                        new SettingEditor(newSettingItem);
                settingEditor.addListener(new SettingEditor.EditorSavedListener() {

                    @Override
                    public void editorSaved(SettingEditor.EditorSavedEvent event) {
                        container.addEntity(newSettingItem.getBean());
                    }
                });
            }
        });
        final com.vaadin.ui.Button editButton = new com.vaadin.ui.Button(getResource().getString("general.edit"));
        editButton.addListener(new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                if (table.getValue() != null) {
                    SettingEditor settingEditor = new SettingEditor(
                            table.getItem(table.getValue()));
                    settingEditor.center();
                    settingEditor.setModal(true);
                    getApplication().getMainWindow().addWindow(settingEditor);
                }
            }
        });
        table.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setModificationsEnabled(event.getProperty().getValue() != null);
            }

            private void setModificationsEnabled(boolean b) {
                editButton.setEnabled(b);
            }
        });
        table.setSizeFull();
        table.setSelectable(true);
        toolbar.addComponent(newButton);
        toolbar.addComponent(editButton);
        toolbar.setWidth("100%");
        vl.addComponent(toolbar);
        vl.addComponent(table);
        vl.setExpandRatio(table, 1);
        vl.setSizeFull();
    }

    private ResourceBundle getResource() {
        return Lookup.getDefault().lookup(XincoVaadinApplication.class).getResource();
    }
}
