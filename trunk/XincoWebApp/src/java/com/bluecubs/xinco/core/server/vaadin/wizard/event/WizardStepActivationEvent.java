package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.Wizard;
import com.bluecubs.xinco.core.server.vaadin.wizard.WizardStep;

@SuppressWarnings("serial")
public class WizardStepActivationEvent extends AbstractWizardEvent {

    private final WizardStep activatedStep;

    public WizardStepActivationEvent(Wizard source, WizardStep activatedStep) {
        super(source);
        this.activatedStep = activatedStep;
    }

    /**
     * Returns the {@link WizardStep} that was the activated.
     *
     * @return the activated {@link WizardStep}.
     */
    public WizardStep getActivatedStep() {
        return activatedStep;
    }
}