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
 * Name: SettingEditor
 * 
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Dec 14, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin.setting;

import com.bluecubs.xinco.core.server.persistence.XincoSetting;
import com.bluecubs.xinco.core.server.vaadin.XincoVaadinApplication;
import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ResourceBundle;
import org.openide.util.Lookup;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
class SettingEditor extends Window implements Button.ClickListener, FormFieldFactory {

    private final Item settingItem;
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;

    public SettingEditor(Item item) {
        this.settingItem = item;
        editorForm = new BeanValidationForm<XincoSetting>(XincoSetting.class);
        editorForm.setFormFieldFactory(SettingEditor.this);
        editorForm.setWriteThrough(false);
        editorForm.setImmediate(true);
        editorForm.setItemDataSource(settingItem,
                Arrays.asList("id", "description",
                "stringValue", "longValue", "boolValue", "intValue"));
        saveButton = new Button(getResource().getString("general.save"), this);
        cancelButton = new Button(getResource().getString("general.cancel"), this);

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
        return String.format("%s",
                settingItem.getItemProperty("description").getValue());
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
            editorForm.commit();
            fireEvent(new EditorSavedEvent(this, settingItem));
        } else if (event.getButton() == cancelButton) {
            editorForm.discard();
        }
        close();
    }

    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
        Field field = XincoSettingFieldFactory.get().createField(item, propertyId,
                uiContext);
        if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("null");
        }
        field.setEnabled(!"id".equals(propertyId.toString().toLowerCase())
                && !"description".equals(propertyId.toString().toLowerCase()));// id and description are not modifiable.
        return field;
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
        return Lookup.getDefault().lookup(XincoVaadinApplication.class).getResource();
    }
}
