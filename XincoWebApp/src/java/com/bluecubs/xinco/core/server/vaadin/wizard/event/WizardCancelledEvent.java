package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;

@SuppressWarnings("serial")
public class WizardCancelledEvent extends AbstractWizardEvent {

    public WizardCancelledEvent(XincoWizard source) {
        super(source);
    }
}
