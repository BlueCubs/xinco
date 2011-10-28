package com.bluecubs.xinco.core.server.vaadin.wizard;

import com.bluecubs.xinco.core.server.vaadin.wizard.event.*;
import com.vaadin.ui.ProgressIndicator;

/**
 * Based on code from Teemu PÃ¶ntelin / Vaadin Ltd
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class WizardProgressBar extends ProgressIndicator implements
        WizardProgressListener {

    private final Wizard wizard;

    public WizardProgressBar(Wizard wizard) {
        super(new Float(0.0));
        setIndeterminate(false);
        setPollingInterval(500);
        this.wizard = wizard;
        setSizeFull();
    }

    private void recalculateProgress() {
        setValue(new Float(new Float(wizard.getProgress() + 1)
                / new Float(wizard.getSteps().size())));
        requestRepaint();
    }

    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        setCaption(event.getActivatedStep().getCaption());
        recalculateProgress();
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {
        recalculateProgress();
    }

    @Override
    public void wizardCompleted(WizardCompletedEvent event) {
        recalculateProgress();
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        // NOP, no need to react to cancellation
    }
}
