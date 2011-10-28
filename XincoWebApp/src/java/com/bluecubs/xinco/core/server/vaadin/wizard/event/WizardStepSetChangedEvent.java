package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.Wizard;

@SuppressWarnings("serial")
public class WizardStepSetChangedEvent extends AbstractWizardEvent {

    public WizardStepSetChangedEvent(Wizard source) {
        super(source);
    }
}
