package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.Wizard;

@SuppressWarnings("serial")
public class WizardCompletedEvent extends AbstractWizardEvent {

    public WizardCompletedEvent(Wizard source) {
        super(source);
    }
}
