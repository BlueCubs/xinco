/**
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
 * Name: SimplifiedSearchComponent
 *
 * Description: Simplified Search Component
 *
 * $Author$:
 * $Date$:
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin;

import static com.bluecubs.xinco.core.server.XincoCoreACEServer.checkAccess;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import com.bluecubs.xinco.core.server.service.XincoCoreACE;
import static com.bluecubs.xinco.core.server.vaadin.Xinco.getInstance;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import static com.vaadin.ui.Window.Notification.TYPE_WARNING_MESSAGE;
import static java.lang.Integer.valueOf;
import static java.lang.System.currentTimeMillis;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import org.vaadin.lucenecontainer.LuceneContainer;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
public final class SimplifiedSearchComponent extends CustomComponent {

    private LuceneContainer dataSource = null;
    private final Table table = new Table();
    private final Button goToSelection, search;
    private Window results;
    private final com.vaadin.ui.Panel panel;

    public SimplifiedSearchComponent() {
        goToSelection = new Button(getInstance().getResource()
                .getString("window.search.gotoselection"));
        dataSource = new LuceneContainer(CONFIG.fileIndexPath);
        table.setWidth(50, UNITS_PERCENTAGE);
        table.setColumnCollapsingAllowed(true);
        table.setSelectable(true);
        panel = new com.vaadin.ui.Panel(getInstance().getResource()
                .getString("menu.search"));
        panel.setContent(new HorizontalLayout());
        final TextField criteria = new TextField();
        panel.addComponent(criteria);
        search = new Button(getInstance().getResource()
                .getString("menu.search"),
                new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                String query = criteria.getValue().toString();
                if (query == null || query.length() == 0) {
                    query = "*";
                } else {
                    //Search file name and content
                    query = "general.filename:" + query + "  OR file:"
                            + query + " ";
                }
                table.setContainerDataSource(null);
                long st = currentTimeMillis();
                dataSource.search(query, null, false);
                long time = currentTimeMillis() - st;
                int hits = dataSource.size();
                table.setContainerDataSource(dataSource);
                fixTable();
                if (hits > 0) {
                    results = new Window();
                    results.addComponent(table);
                    results.addComponent(goToSelection);
                    results.setWidth(table.getWidth(), table.getWidthUnits());
                    getInstance().getMainWindow().addWindow(results);
                    results.center();
                }
            }
        });
        table.addListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                goToSelection.setEnabled(
                        table.getValue() != null);
            }
        });
        goToSelection.setEnabled(false);
        goToSelection.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                boolean haveAccess;
                boolean loggedIn = getInstance().getLoggedUser() != null;
                //Get selected data
                XincoCoreDataServer data = new XincoCoreDataServer(
                        valueOf(table.getItem(table.getValue())
                                .getItemProperty("id").toString()));
                //Now check permissions on the file for the user
                data.loadACL();
                if (loggedIn) {
                    XincoCoreACE ace = checkAccess(getInstance().getLoggedUser(),
                            (ArrayList) data.getXincoCoreAcl());
                    haveAccess = ace.isReadPermission();
                } else {
                    haveAccess = canRead(data.getXincoCoreAcl());
                }
                //Store path of ids
                LinkedList<Integer> parents = new LinkedList<>();
                if (haveAccess) {
                    //Now have to make sure there is access to all parent folders of data
                    XincoCoreNodeServer parent = null;
                    if (data.getXincoCoreNodeId() > 0) {
                        parent = new XincoCoreNodeServer(
                                data.getXincoCoreNodeId());
                    }
                    while (parent != null) {
                        if ((loggedIn && checkAccess(getInstance().getLoggedUser(),
                                (ArrayList) data.getXincoCoreAcl())
                                .isReadPermission())
                                || canRead(parent.getXincoCoreAcl())) {
                            parents.add(parent.getId());
                        }
                        if (parent.getXincoCoreNodeId() > 0) {
                            parent = new XincoCoreNodeServer(
                                    parent.getXincoCoreNodeId());
                        } else {
                            parent = null;
                        }
                    }
                    //If the last in the list is the root (1) we can access it!
                    haveAccess = parents.getLast() == 1;
                }
                if (!(haveAccess && getInstance()
                        .expandTreeNodes(parents))) {
                    //Able to expand nodes
                    getApplication().getMainWindow().showNotification(getInstance().getResource()
                                    .getString("error.data.sufficientrights"), TYPE_WARNING_MESSAGE);
                } else {
                    getInstance().getXincoTree().setValue("data-"
                            + data.getId());
                }
                getInstance().getMainWindow().removeWindow(results);
            }
        });
        panel.addComponent(search);
        // Set the size as undefined at all levels
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();
        // The composition root MUST be set
        setCompositionRoot(panel);
    }

    private void fixTable() {
        for (Object id : table.getContainerPropertyIds()) {
            table.setColumnCollapsed(id, !id.equals("id")
                    && !id.equals("designation"));
            //Fix headers
            table.setColumnHeader(id, getInstance().getResource()
                    .containsKey(id.toString())
                    ? getInstance().getResource()
                            .getString(id.toString()) : id.toString());
        }
    }

    private boolean canRead(java.util.List list) {
        boolean haveAccess = false;
        for (Iterator<XincoCoreACE> it = list.iterator(); it.hasNext();) {
            XincoCoreACE ace = it.next();
            if (ace.isReadPermission() && ace.getXincoCoreGroupId() == 3) {
                //Public
                haveAccess = ace.isReadPermission();
                break;
            }
        }
        return haveAccess;
    }

    void refresh() {
        panel.setCaption(getInstance().getResource()
                .getString("menu.search"));
        search.setCaption(getInstance().getResource()
                .getString("menu.search"));
    }
}
