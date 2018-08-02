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
 * Name: SettingEditor
 * 
 * Description: Edit settings
 * 
 * Original Author: Javier A. Ortiz Bultron javier.ortiz.78@gmail.com Date: Dec 14, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin.setting;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import com.bluecubs.xinco.core.server.persistence.XincoSetting;
import com.bluecubs.xinco.core.server.persistence.controller.XincoSettingJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import static com.bluecubs.xinco.core.server.vaadin.Xinco.getInstance;
import com.vaadin.data.Item;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.io.Serializable;
import static java.lang.Integer.valueOf;
import static java.lang.String.format;
import java.lang.reflect.Method;
import static java.util.Arrays.asList;
import java.util.ResourceBundle;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
class SettingEditor extends Window implements Button.ClickListener {

    private final Item settingItem;
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;

    public SettingEditor(Item item) {
        this.settingItem = item;
        editorForm = new Form();
        editorForm.setWriteThrough(false);
        editorForm.setImmediate(true);
        editorForm.setItemDataSource(settingItem,
                asList("id", "description",
                "stringValue", "longValue", "boolValue", "intValue"));
        saveButton = new Button(getResource().getString("general.save"), this);
        cancelButton = new Button(getResource().getString("general.cancel"), this);
        editorForm.getField("id").setEnabled(false);
        editorForm.getField("description").setCaption(getResource().getString("general.description"));
        editorForm.getField("description").setEnabled(false);
        ((TextField) editorForm.getField("description")).setNullRepresentation("null");
        editorForm.getField("description").setRequired(editorForm.getField("description").isEnabled());
        editorForm.getField("description").setRequiredError(getResource().getString("message.missing.description"));
        ((TextField) editorForm.getField("stringValue")).setNullRepresentation("null");
        editorForm.getField("stringValue").setRequired(editorForm.getField("stringValue").isEnabled());
        editorForm.getField("stringValue").setRequiredError(getResource().getString("message.missing.value"));
        ((TextField) editorForm.getField("longValue")).setNullRepresentation("null");
        editorForm.getField("longValue").setRequired(editorForm.getField("longValue").isEnabled());
        editorForm.getField("longValue").setRequiredError(getResource().getString("message.missing.value"));
        editorForm.getField("boolValue").setRequired(editorForm.getField("boolValue").isEnabled());
        editorForm.getField("boolValue").setRequiredError(getResource().getString("message.missing.value"));
        ((TextField) editorForm.getField("intValue")).setNullRepresentation("null");
        editorForm.getField("intValue").setRequired(editorForm.getField("intValue").isEnabled());
        editorForm.getField("intValue").setRequiredError(getResource().getString("message.missing.value"));
        editorForm.getFooter().addComponent(saveButton);
        editorForm.getFooter().addComponent(cancelButton);
        getContent().setSizeUndefined();
        addComponent(editorForm);
        setCaption(buildCaption());
    }

    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
        return format("%s",
                settingItem.getItemProperty("description").getValue());
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
            XincoSetting setting =
                    new XincoSettingJpaController(getEntityManagerFactory()).findXincoSetting(valueOf(editorForm.getField("id").getValue().toString()));
            boolean modified = false;
            if (setting.getLongValue() != ((Long) editorForm.getField("longValue").getValue())) {
                modified = true;
                setting.setLongValue((Long) editorForm.getField("longValue").getValue());
            }
            if (setting.getIntValue().equals((Integer) editorForm.getField("intValue").getValue())) {
                modified = true;
                setting.setIntValue((Integer) editorForm.getField("intValue").getValue());
            }
            if (!setting.getStringValue().equals(editorForm.getField("stringValue").getValue().toString())) {
                modified = true;
                setting.setStringValue(editorForm.getField("stringValue").getValue().toString());
            }
            if (setting.getBoolValue() != editorForm.getField("boolValue").getValue()) {
                modified = true;
                setting.setBoolValue((Boolean) editorForm.getField("boolValue").getValue());
            }
            if (modified) {
                try {
                    new XincoSettingJpaController(getEntityManagerFactory()).edit(setting);
                } catch (NonexistentEntityException ex) {
                    getLogger(SettingEditor.class.getName()).log(SEVERE, null, ex);
                } catch (Exception ex) {
                    getLogger(SettingEditor.class.getName()).log(SEVERE, null, ex);
                }
            }
            fireEvent(new EditorSavedEvent(this, settingItem));
        } else if (event.getButton() == cancelButton) {
            editorForm.discard();
        }
        close();
    }

    public static class EditorSavedEvent extends Component.Event {

        private Item savedItem;

        public EditorSavedEvent(Component source, Item savedItem) {
            super(source);
            this.savedItem = savedItem;
        }

        public Item getSavedItem() {
            return savedItem;
        }
    }

    public interface EditorSavedListener extends Serializable {

        public void editorSaved(EditorSavedEvent event);
    }

    public void addListener(EditorSavedListener listener) {
        try {
            Method method = EditorSavedListener.class.getDeclaredMethod(
                    "editorSaved", new Class[]{EditorSavedEvent.class});
            addListener(EditorSavedEvent.class, listener, method);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, editor saved method not found");
        }
    }

    private ResourceBundle getResource() {
        return getInstance().getResource();
    }
}
