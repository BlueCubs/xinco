package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;

@SuppressWarnings("serial")
public class WizardStepSetChangedEvent extends AbstractWizardEvent {

  public WizardStepSetChangedEvent(XincoWizard source) {
    super(source);
  }
}
