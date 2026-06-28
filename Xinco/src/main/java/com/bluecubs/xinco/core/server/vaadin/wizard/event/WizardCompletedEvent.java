package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;

@SuppressWarnings("serial")
/** Event fired when the wizard completes successfully. */
public class WizardCompletedEvent extends AbstractWizardEvent {

  /**
   * Creates a wizard-completed event.
   *
   * @param source the wizard that generated this event
   */
  public WizardCompletedEvent(XincoWizard source) {
    super(source);
  }
}
