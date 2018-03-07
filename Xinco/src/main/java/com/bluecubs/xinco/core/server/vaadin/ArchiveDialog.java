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
 * Name: ArchiveDialog
 * 
 * Description: Archive Dialog
 * 
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Jan 26, 2012
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin;

import static com.bluecubs.xinco.core.server.XincoAddAttributeServer.getXincoAddAttributes;
import com.bluecubs.xinco.core.server.service.XincoAddAttribute;
import static com.bluecubs.xinco.core.server.vaadin.Xinco.getInstance;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.*;
import de.essendi.vaadin.ui.component.numberfield.NumberField;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.valueOf;
import java.util.Calendar;
import static java.util.Calendar.DST_OFFSET;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.ZONE_OFFSET;
import java.util.Date;
import java.util.GregorianCalendar;
import static java.util.logging.Level.FINE;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.datatype.DatatypeConfigurationException;
import static javax.xml.datatype.DatatypeFactory.newInstance;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
class ArchiveDialog extends CustomComponent {

    private final Select archiveModel;
    private final DateField date = new DateField();
    private final NumberField days = new NumberField();
    private static final Logger LOG =
            getLogger(ArchiveDialog.class.getName());

    ArchiveDialog() {
        days.setCaption(getInstance().getResource()
                .getString("window.archive.archivedays") + ":");
        days.setDecimalAllowed(false);
        days.setGroupingUsed(false);
        days.setMinValue(1);
        days.setMaxValue(MAX_VALUE);
        archiveModel = new Select(getInstance().getResource()
                .getString("window.archive.archivingmodel") + ":");
        com.vaadin.ui.Panel panel = new com.vaadin.ui.Panel(getInstance().getResource().getString("window.archive"));
        panel.setContent(new VerticalLayout());
        CheckBox revisionModelCheckbox = new CheckBox(getInstance()
                .getResource().getString("window.archive.revisionmodel"));
        revisionModelCheckbox.setValue(true);
        panel.addComponent(revisionModelCheckbox);
        java.util.List<XincoAddAttribute> attributes;
        if (getInstance().getXincoCoreData().getId() == 0) {
            // Is a new data, there's nothing yet in the database.
            // Load local values.
            attributes =
                    getInstance().getXincoCoreData()
                    .getXincoAddAttributes();
        } else {
            attributes = getXincoAddAttributes(getInstance().getXincoCoreData().getId());
        }
        int i = 0;
        archiveModel.addItem(i);
        archiveModel.setItemCaption(i, getInstance().getResource()
                .getString("window.archive.archivingmodel.none"));
        //Set as default
        archiveModel.setValue(i);
        i++;
        archiveModel.addItem(i);
        archiveModel.setItemCaption(i, getInstance().getResource()
                .getString("window.archive.archivingmodel.archivedate"));
        i++;
        archiveModel.addItem(i);
        archiveModel.setItemCaption(i, getInstance().getResource()
                .getString("window.archive.archivingmodel.archivedays"));
        i++;
        panel.addComponent(archiveModel);
        //processing independent of creation
        // Set the date and time to present
        date.setValue(new Date());
        date.setDateFormat("dd-MM-yyyy");
        date.setCaption(getInstance().getResource()
                .getString("window.archive.archivedate") + ":");
        panel.addComponent(date);
        //Disabled by default
        date.setEnabled(false);
        //set date / days
        //convert clone from remote time to local time
        Calendar cal =
                (Calendar) (attributes.get(5))
                .getAttribDatetime().toGregorianCalendar().clone();
        Calendar realcal = (attributes.get(5))
                .getAttribDatetime().toGregorianCalendar();
        Calendar ngc = new GregorianCalendar();
        cal.add(MILLISECOND, (ngc.get(ZONE_OFFSET)
                - realcal.get(ZONE_OFFSET))
                - (ngc.get(DST_OFFSET)
                + realcal.get(DST_OFFSET)));
        date.setValue(cal.getTime());

        panel.addComponent(days);
        //Disabled by default
        days.setEnabled(false);
        archiveModel.addListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                switch (valueOf(event.getProperty().toString())) {
                    case 1:
                        //Enable date
                        date.setEnabled(true);
                        days.setEnabled(false);
                        break;
                    case 2:
                        //Enable days
                        days.setEnabled(true);
                        date.setEnabled(false);
                        break;
                    default:
                        //Disable both
                        date.setEnabled(false);
                        days.setEnabled(false);
                        break;
                }
            }
        });
        // Set the size as undefined at all levels
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();
        // The composition root MUST be set
        setCompositionRoot(panel);
    }

    /**
     * @return the archiveModel
     */
    private Select getArchiveModel() {
        return archiveModel;
    }

    /**
     * @return the date
     */
    private Date getDate() {
        return (Date) date.getValue();
    }

    private int getDays() {
        return days.isEnabled()
                && !days.getValue().toString().isEmpty()
                ? valueOf(days.getValue().toString())
                : 0;
    }

    public void updateAttributes() throws DatatypeConfigurationException {
        //Archive model
        String model = getArchiveModel().getValue().toString();
        LOG.log(FINE, "Archive model: {0}", model);
        getInstance().getXincoCoreData().getXincoAddAttributes().get(4)
                .setAttribUnsignedint(valueOf(model));
        //Archieve date
        Date archiveDate = getDate();
        LOG.log(FINE, "Archive date: {0}", archiveDate);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(archiveDate);
        getInstance().getXincoCoreData().getXincoAddAttributes()
                .get(5).setAttribDatetime(newInstance()
                .newXMLGregorianCalendar(c));
        //Archieve dayss 
        int tempDays = getDays();
        LOG.log(FINE, "Archive days: {0}", tempDays);
        getInstance().getXincoCoreData().getXincoAddAttributes().get(6)
                .setAttribUnsignedint(tempDays);
    }
}
