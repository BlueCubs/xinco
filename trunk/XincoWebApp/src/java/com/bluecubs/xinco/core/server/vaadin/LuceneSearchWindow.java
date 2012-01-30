package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.core.server.service.XincoCoreACE;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
    private final CheckBox allLanguages;
    private final Xinco xinco;

    public LuceneSearchWindow(final Xinco xinco) {
        this.xinco = xinco;
        allLanguages = new CheckBox(xinco.getResource().getString("window.search.alllanguages"), new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                lang.setEnabled(!(Boolean) allLanguages.getValue());
                lang.setValue(null);
            }
        });
        table.setWidth(50, Sizeable.UNITS_PERCENTAGE);
        setContent(new VerticalLayout());
        com.vaadin.ui.Button addToQuery = new com.vaadin.ui.Button(xinco.getResource().getString("window.search.addtoquery"), new com.vaadin.ui.Button.ClickListener() {

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
                queryField.setValue(queryField.getValue().toString() + (operator.getValue() == null ? "" : operator.getValue()) + field + variableField.getValue().toString() + " ");
                variableField.setValue("");
            }
        });
        com.vaadin.ui.Button reset = new com.vaadin.ui.Button(xinco.getResource().getString("general.reset"), new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                queryField.setValue("");
            }
        });
        com.vaadin.ui.Button searchButton = new com.vaadin.ui.Button(xinco.getResource().getString("window.search"), new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                String query = queryField.getValue().toString();
                if (query == null || query.length() == 0) {
                    query = "*";
                }
                table.setContainerDataSource(null);
                long st = System.currentTimeMillis();
                dataSource.search(query, null, false);
                long time = System.currentTimeMillis() - st;
                int hits = dataSource.size();
                info.setValue("Found " + hits + " docs in " + time + "ms");
                table.setContainerDataSource(dataSource);
                fixTable();
            }
        });
        dataSource = new LuceneContainer(XincoDBManager.config.fileIndexPath);
        table.setContainerDataSource(dataSource);
        //Collapse some columns by default
        table.setColumnCollapsingAllowed(true);
        fixTable();
        table.setSelectable(true);
        table.setImmediate(true);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        final com.vaadin.ui.Button cancel = new com.vaadin.ui.Button(xinco.getResource().getString("general.cancel"), new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                getApplication().getMainWindow().removeWindow(LuceneSearchWindow.this);
            }
        });
        final com.vaadin.ui.Button goToSelection = getToSelectionButton(xinco);
        table.addListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                goToSelection.setEnabled(table.getValue() != null);
            }
        });
        goToSelection.setEnabled(false);
        operator.addItem("AND");
        operator.addItem("OR");
        operator.addItem("NOT");
        operator.addItem("+");
        operator.addItem("-");
        options.addItem("file");
        options.setItemCaption("file", xinco.getResource().getString("window.search.filecontent") + " (file)");
        String text;
        ArrayList alldatatypes = XincoCoreDataTypeServer.getXincoCoreDataTypes();
        for (Iterator<XincoCoreDataTypeServer> it = alldatatypes.iterator(); it.hasNext();) {
            XincoCoreDataTypeServer type = it.next();
            for (Iterator<com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute> it2 = type.getXincoCoreDataTypeAttributes().iterator(); it2.hasNext();) {
                com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute attr = it2.next();
                text = attr.getDesignation();
                options.addItem(text);
                options.setItemCaption(text, xinco.getResource().containsKey(text) ? xinco.getResource().getString(text) : text);
            }
        }
        form.getLayout().addComponent(new com.vaadin.ui.Label(xinco.getResource().getString("window.search.querybuilder") + ":"));
        form.getLayout().addComponent(new com.vaadin.ui.Label(xinco.getResource().getString("window.search.querybuilderhints")));
        form.getLayout().addComponent(new com.vaadin.ui.Label(xinco.getResource().getString("window.search.querybuilderhintslabel")));
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
        hl4.addComponent(new com.vaadin.ui.Label(xinco.getResource().getString("general.language") + ":"));
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
        form.getFooter().addComponent(goToSelection);
        addComponent(form);
        // Set the size as undefined at all levels
        getContent().setSizeUndefined();
        setSizeUndefined();
    }

    protected final com.vaadin.ui.Button getToSelectionButton(final Xinco xinco) {
        return new com.vaadin.ui.Button(xinco.getResource().getString("window.search.gotoselection"), new com.vaadin.ui.Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                boolean haveAccess;
                boolean loggedIn = xinco.getLoggedUser() != null;
                //Get selected data
                XincoCoreDataServer data = new XincoCoreDataServer(Integer.valueOf(table.getItem(table.getValue()).getItemProperty("id").toString()));
                //Now check permissions on the file for the user
                data.loadACL();
                if (loggedIn) {
                    XincoCoreACE ace = XincoCoreACEServer.checkAccess(xinco.getLoggedUser(), (ArrayList) data.getXincoCoreAcl());
                    haveAccess = ace.isReadPermission();
                } else {
                    haveAccess = canRead(data.getXincoCoreAcl());
                }
                //Store path of ids
                LinkedList<Integer> parents = new LinkedList<Integer>();
                if (haveAccess) {
                    //Now have to make sure there is access to all parent folders of data
                    XincoCoreNodeServer parent = null;
                    if (data.getXincoCoreNodeId() > 0) {
                        parent = new XincoCoreNodeServer(data.getXincoCoreNodeId());
                    }
                    while (parent != null) {
                        if ((loggedIn && XincoCoreACEServer.checkAccess(xinco.getLoggedUser(), (ArrayList) data.getXincoCoreAcl()).isReadPermission()) || canRead(parent.getXincoCoreAcl())) {
                            parents.add(parent.getId());
                        }
                        if (parent.getXincoCoreNodeId() > 0) {
                            parent = new XincoCoreNodeServer(parent.getXincoCoreNodeId());
                        } else {
                            parent = null;
                        }
                    }
                    //If the last in the list is the root (1) we can access it!
                    haveAccess = parents.getLast() == 1;
                }
                close();
                if (!(haveAccess && xinco.expandTreeNodes(parents))) {
                    //Able to expand nodes
                    getApplication().getMainWindow().showNotification(xinco.getResource().getString("error.data.sufficientrights"), Notification.TYPE_WARNING_MESSAGE);
                } else {
                    xinco.getXincoTree().setValue("data-" + data.getId());
                }
            }
        });
    }

    private void fixTable() {
        for (Object id : table.getContainerPropertyIds()) {
            table.setColumnCollapsed(id, !id.equals("id") && !id.equals("designation"));
            //Fix headers
            table.setColumnHeader(id, xinco.getResource().containsKey(id.toString()) ? xinco.getResource().getString(id.toString()) : id.toString());
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
}
