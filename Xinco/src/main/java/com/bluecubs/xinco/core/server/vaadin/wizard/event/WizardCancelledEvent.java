package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;

@SuppressWarnings("serial")
/** Event fired when the wizard is cancelled. */
public class WizardCancelledEvent extends AbstractWizardEvent {

  /** Creates a wizard-cancelled event. */
  public WizardCancelledEvent(XincoWizard source) {
    super(source);
  }
}
