package com.bluecubs.xinco.core.server.vaadin.wizard;

import com.vaadin.ui.Component;

/**
 * Based on code from Teemu PÃ¶ntelin / Vaadin Ltd
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public interface WizardStep {

    /**
     * Returns the caption of this WizardStep.
     *
     * @return the caption of this WizardStep.
     */
    public String getCaption();

    /**
     * Returns the {@link Component} that is to be used as the actual content of
     * this WizardStep.
     *
     * @return the content of this WizardStep as a Component.
     */
    public Component getContent();

    /**
     * Returns true if user is allowed to navigate forward past this WizardStep.
     * Typically this method is called when user clicks the Next button of the
     * {@link Wizard}.
     *
     * @return true if user is allowed to navigate past this WizardStep.
     */
    public boolean onAdvance();

    /**
     * Returns true if user is allowed to navigate backwards from this
     * WizardStep. Typically this method is called when user clicks the Back
     * button of the {@link Wizard}.
     *
     * @return true if user is allowed to navigate backwards from this
     * WizardStep.
     */
    public boolean onBack();
}
