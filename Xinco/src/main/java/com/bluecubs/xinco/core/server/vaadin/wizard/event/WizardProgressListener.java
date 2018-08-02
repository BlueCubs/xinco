package com.bluecubs.xinco.core.server.vaadin.wizard.event;

public interface WizardProgressListener {

    /**
     * Called when the currently active WizardStep is changed and this
     * {@code WizardProgressListener} is expected to update itself accordingly.
     *
     * @param event {@link WizardStepActivationEvent} object containing details
     * about the event
     */
    void activeStepChanged(WizardStepActivationEvent event);

    /**
     * Called when collection WizardSteps is changed (i.e. a step is added or
     * removed) and this {@code WizardProgressListener} is expected to update
     * itself accordingly.
     *
     * @param event {@link WizardStepSetChangedEvent} object containing details
     * about the event
     */
    void stepSetChanged(WizardStepSetChangedEvent event);

    /**
     * Called when a Wizard is completed.
     *
     * @param event {@link WizardCompletedEvent} object containing details about
     * the event
     */
    void wizardCompleted(WizardCompletedEvent event);

    /**
     * Called when a Wizard is cancelled by the user.
     *
     * @param event {@link WizardCancelledEvent} object containing details about
     * the event
     */
    void wizardCancelled(WizardCancelledEvent event);
}
