package com.bluecubs.xinco.core.server.vaadin.wizard.event;

import com.bluecubs.xinco.core.server.vaadin.wizard.XincoWizard;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class AbstractWizardEvent extends Component.Event {

  protected AbstractWizardEvent(XincoWizard source) {
    super(source);
  }

  /**
   * Returns the {@link XincoWizard} component that was the source of this event.
   *
   * @return the source {@link XincoWizard} of this event.
   */
  public XincoWizard getWizard() {
    return (XincoWizard) getSource();
  }
}
