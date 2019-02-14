package com.bluecubs.xinco.core.server.vaadin.wizard;

import com.bluecubs.xinco.core.server.vaadin.wizard.event.*;
import com.vaadin.ui.ProgressIndicator;

/**
 * Based on code from Teemu PÃ¶ntelin / Vaadin Ltd
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public final class WizardProgressBar extends ProgressIndicator implements
        WizardProgressListener {

    private static final long serialVersionUID = 4019670885155445960L;

    private final XincoWizard wizard;

    public WizardProgressBar(XincoWizard wizard) {
        super(0.0f);
        setIndeterminate(false);
        setPollingInterval(500);
        this.wizard = wizard;
        setSizeFull();
    }

    private void recalculateProgress() {
        setValue(Float.valueOf(wizard.getProgress() + 1)
                / Float.valueOf(wizard.getSteps().size()));
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
