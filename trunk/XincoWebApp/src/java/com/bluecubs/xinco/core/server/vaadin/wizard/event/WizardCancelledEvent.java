package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.Wizard;

@SuppressWarnings("serial")
public class WizardCancelledEvent extends AbstractWizardEvent {

    public WizardCancelledEvent(Wizard source) {
        super(source);
    }
}
