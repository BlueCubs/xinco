package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.WizardStep;
import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;

@SuppressWarnings("serial")
/** Event fired when a wizard step is activated. */
public class WizardStepActivationEvent extends AbstractWizardEvent {

  private final WizardStep activatedStep;

  /**
   * Creates a step-activation event for the given step.
   *
   * @param source the wizard that generated this event
   * @param activatedStep the step that was activated
   */
  public WizardStepActivationEvent(XincoWizard source, WizardStep activatedStep) {
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
