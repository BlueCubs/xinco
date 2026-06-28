package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;

@SuppressWarnings("serial")
/** Event fired when the set of wizard steps changes. */
public class WizardStepSetChangedEvent extends AbstractWizardEvent {

  /**
   * Creates a step-set-changed event.
   *
   * @param source the wizard that generated this event
   */
  public WizardStepSetChangedEvent(XincoWizard source) {
    super(source);
  }
}
