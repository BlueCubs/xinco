package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;

@SuppressWarnings("serial")
public class WizardCompletedEvent extends AbstractWizardEvent {

    public WizardCompletedEvent(XincoWizard source) {
        super(source);
    }
}
