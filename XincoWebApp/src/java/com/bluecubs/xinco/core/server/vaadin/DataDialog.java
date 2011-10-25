package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.XincoCoreLanguageServer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class DataDialog extends Window {

    public DataDialog(String caption, Xinco xinco) {
        super(caption, xinco.getMainWindow());
        //ID
        TextField idField = new TextField(xinco.getResource().getString("general.id") + ":");
        addComponent(idField);
        //Designation
        TextField designationField = new TextField(xinco.getResource().getString("general.designation") + ":");
        addComponent(designationField);
        //Language selection
        Select languages = new Select(xinco.getResource().getString("general.language") + ":");
        for (Object language : XincoCoreLanguageServer.getXincoCoreLanguages()) {
            languages.addItem(((XincoCoreLanguageServer)language).getDesignation());
        }
        addComponent(languages);
        //Status
        TextField statusField = new TextField(xinco.getResource().getString("general.status") + ":");
        addComponent(statusField);
        //Buttons
        addComponent(new Button(
                xinco.getResource().getString("general.save"),
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                }));
        addComponent(new Button(
                xinco.getResource().getString("general.cancel"),
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                }));
    }
}
