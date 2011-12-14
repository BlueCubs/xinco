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
 * Name: LuceneSearchWindow
 * 
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Dec 13, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.XincoCoreDataTypeServer;
import com.bluecubs.xinco.core.server.XincoCoreLanguageServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import org.openide.util.Lookup;
import org.vaadin.lucenecontainer.LuceneContainer;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class LuceneSearchWindow extends Window {

    private final com.vaadin.ui.TextField variableField = new com.vaadin.ui.TextField();
    private final com.vaadin.ui.TextField queryField = new com.vaadin.ui.TextField();
    private HorizontalLayout hl = new HorizontalLayout();
    private HorizontalLayout hl2 = new HorizontalLayout();
    private HorizontalLayout hl3 = new HorizontalLayout();
    private HorizontalLayout hl4 = new HorizontalLayout();
    private HorizontalLayout hl5 = new HorizontalLayout();
    private LuceneContainer dataSource = null;
    private final Form form = new Form();
    private final com.vaadin.ui.Label info = new com.vaadin.ui.Label();
    private final Table table = new Table();
    private final Select lang = new Select();
    private Select operator = new Select();
    private Select options = new Select();
    private final CheckBox allLanguages = new CheckBox(getResource().getString("window.search.alllanguages"),
            new com.vaadin.ui.Button.ClickListener() {

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                    lang.setEnabled(!(Boolean) allLanguages.getValue());
                    lang.setValue(null);
                }
            });

    public LuceneSearchWindow() {
        table.setWidth(50, Sizeable.UNITS_PERCENTAGE);
        setContent(new VerticalLayout());
        com.vaadin.ui.Button addToQuery = new com.vaadin.ui.Button(
                getResource().getString("window.search.addtoquery"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        String field;
                        //create field string
                        if (options.getValue() != null) {
                            field = ((String) options.getValue()) + ":";
                        } else {
                            field = "";
                        }
                        // append to query
                        queryField.setValue(queryField.getValue().toString()
                                + (operator.getValue() == null ? "" : operator.getValue())
                                + field + variableField.getValue().toString() + " ");
                        variableField.setValue("");
                    }
                });
        com.vaadin.ui.Button reset = new com.vaadin.ui.Button(
                getResource().getString("general.reset"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        queryField.setValue("");
                    }
                });
        com.vaadin.ui.Button searchButton = new com.vaadin.ui.Button(
                getResource().getString("window.search"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        String query = queryField.getValue().toString();
                        if (query == null || query.length() == 0) {
                            query = "*";
                        }
                        if (dataSource == null) {
                            dataSource = new LuceneContainer(XincoDBManager.config.FileIndexPath);
                        }
                        table.setContainerDataSource(null);
                        long st = System.currentTimeMillis();
                        dataSource.search(query, null, false);
                        long time = (System.currentTimeMillis() - st);
                        int hits = dataSource.size();
                        info.setValue("Found " + hits + " docs in " + time + "ms");
                        table.setContainerDataSource(dataSource);
                        for (Object id : table.getContainerPropertyIds()) {
                            table.setColumnCollapsed(id, !id.equals("id") && !id.equals("designation"));
                            //Fix headers
                            table.setColumnHeader(id, getResource().containsKey(id.toString())
                                    ? getResource().getString(id.toString()) : id.toString());
                        }
                    }
                });
        //Collapse some columns by default
        table.setColumnCollapsingAllowed(true);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(
                getResource().getString("general.cancel"),
                new com.vaadin.ui.Button.ClickListener() {

                    @Override
                    public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                        LuceneSearchWindow.this.close();
                    }
                });
        operator.addItem("AND");
        operator.addItem("OR");
        operator.addItem("NOT");
        operator.addItem("+");
        operator.addItem("-");
        options.addItem("file");
        options.setItemCaption("file", getResource().getString("window.search.filecontent") + " (file)");
        String text;
        ArrayList alldatatypes = XincoCoreDataTypeServer.getXincoCoreDataTypes();
        for (Iterator<XincoCoreDataTypeServer> it = alldatatypes.iterator(); it.hasNext();) {
            XincoCoreDataTypeServer type = it.next();
            for (Iterator<com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute> it2 = type.getXincoCoreDataTypeAttributes().iterator(); it2.hasNext();) {
                com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute attr = it2.next();
                text = attr.getDesignation();
                options.addItem(text);
                options.setItemCaption(text, getResource().containsKey(text)
                        ? getResource().getString(text) : text);
            }
        }
        form.getLayout().addComponent(new com.vaadin.ui.Label(
                getResource().getString("window.search.querybuilder") + ":"));
        form.getLayout().addComponent(new com.vaadin.ui.Label(
                getResource().getString("window.search.querybuilderhints")));
        form.getLayout().addComponent(new com.vaadin.ui.Label(
                getResource().getString("window.search.querybuilderhintslabel")));
        form.getLayout().addComponent(info);
        hl.addComponent(operator);
        hl.addComponent(options);
        form.getLayout().addComponent(hl);
        hl2.addComponent(variableField);
        hl2.addComponent(addToQuery);
        form.getLayout().addComponent(hl2);
        hl3.addComponent(queryField);
        hl3.addComponent(reset);
        form.getLayout().addComponent(hl3);
        hl4.addComponent(new com.vaadin.ui.Label(
                getResource().getString("general.language") + ":"));
        for (Iterator<XincoCoreLanguageServer> it = XincoCoreLanguageServer.getXincoCoreLanguages().iterator(); it.hasNext();) {
            XincoCoreLanguageServer language = it.next();
            text = language.getDesignation() + " (" + language.getSign() + ")";
            lang.addItem(language.getId());
            lang.setItemCaption(language.getId(), text);
        }
        hl4.addComponent(lang);
        form.getLayout().addComponent(hl4);
        hl5.addComponent(allLanguages);
        hl5.addComponent(searchButton);
        form.getLayout().addComponent(hl5);
        form.getLayout().addComponent(table);
        form.getFooter().setSizeUndefined();
        form.getFooter().addComponent(cancel);
        addComponent(form);
        // Set the size as undefined at all levels
        getContent().setSizeUndefined();
        setSizeUndefined();
    }

    private ResourceBundle getResource() {
        return Lookup.getDefault().lookup(XincoVaadinApplication.class).getResource();
    }
}
