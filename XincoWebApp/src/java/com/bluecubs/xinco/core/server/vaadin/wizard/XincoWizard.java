package com.bluecubs.xinco.core.server.vaadin.wizard;

import com.bluecubs.xinco.core.server.vaadin.wizard.event.*;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.*;
import java.util.*;

/**
 * Component for displaying multi-step wizard style user interface.
 *
 * <p> The steps of the wizard must be implementations of the {@link WizardStep}
 * interface. Use the {@link #addStep(WizardStep)} method to add these steps in
 * the same order they are supposed to be displayed. </p>
 *
 * <p> The wizard also supports navigation through URI fragments. This feature
 * is disabled by default, but you can enable it using
 * {@link #setUriFragmentEnabled(boolean)} method. Each step will get a
 * generated identifier that is used as the URI fragment. If you wish to
 * override these with your own identifiers, you can add the steps using the
 * overloaded {@link #addStep(WizardStep, String)} method. </p>
 *
 * <p> To react on the progress, cancellation or completion of this {@code XincoWizard}
 * you should add one or more listeners that implement the
 * {@link WizardProgressListener} interface. These listeners are added using the
 * {@link #addListener(WizardProgressListener)} method and removed with the
 * {@link #removeListener(WizardProgressListener)}. </p>
 *
 * Based on code from Teemu PÃ¶ntelin / Vaadin Ltd
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class XincoWizard extends CustomComponent {

    private final List<WizardStep> steps = new ArrayList<WizardStep>();
    private final List<WizardProgressListener> listeners =
            new ArrayList<WizardProgressListener>();
    private final Map<String, WizardStep> idMap =
            new HashMap<String, WizardStep>();
    private VerticalLayout mainLayout;
    private Panel contentPanel;
    private Button nextButton;
    private Button backButton;
    private Button finishButton;
    private Button cancelButton;
    private WizardStep currentStep;
    private WizardStep lastCompletedStep;
    private Component header;
    private int lastCompleted, progress;

    public XincoWizard() {
        init();
    }

    private void init() {
        mainLayout = new VerticalLayout();
        setCompositionRoot(mainLayout);
        setSizeFull();

        contentPanel = new Panel();
        contentPanel.setSizeFull();

        initControlButtons();

        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(cancelButton);
        footer.addComponent(backButton);
        footer.addComponent(nextButton);
        footer.addComponent(finishButton);

        mainLayout.addComponent(contentPanel);
        mainLayout.addComponent(footer);
        mainLayout.setComponentAlignment(footer, Alignment.BOTTOM_RIGHT);

        mainLayout.setExpandRatio(contentPanel, 1.0f);
        mainLayout.setSizeFull();

        initDefaultHeader();
    }

    private void initControlButtons() {
        nextButton = new Button("Next");
        nextButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                next();
            }
        });

        backButton = new Button("Back");
        backButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                back();
            }
        });

        finishButton = new Button("Finish");
        finishButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                finish();
            }
        });
        finishButton.setEnabled(false);

        cancelButton = new Button("Cancel");
        cancelButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                cancel();
            }
        });
    }

    /**
     * Cancels this XincoWizard triggering a {@link WizardCancelledEvent}. This
     * method is called when user clicks the cancel button.
     */
    public void cancel() {
        fireEvent(new WizardCancelledEvent(this));
    }

    /**
     * Triggers a {@link WizardCompletedEvent} if the current step is the last
     * step and it allows advancing (see {@link WizardStep#onAdvance()}). This
     * method is called when user clicks the finish button.
     */
    public void finish() {
        if (isLastStep(currentStep) && currentStep.onAdvance()) {
            // next (finish) allowed -> fire complete event
            fireEvent(new WizardCompletedEvent(this));
        }
    }

    /**
     * Activates the next {@link WizardStep} if the current step allows
     * advancing (see {@link WizardStep#onAdvance()}) or calls the
     * {@link #finish()} method the current step is the last step. This method
     * is called when user clicks the next button.
     */
    public void next() {
        progress++;
        if (isLastStep(currentStep)) {
            finish();
        } else {
            lastCompleted = steps.indexOf(currentStep);
            activateStep(steps.get(lastCompleted + 1));
        }
    }

    /**
     * Activates the previous {@link WizardStep} if the current step allows
     * going back (see {@link WizardStep#onBack()}) and the current step is not
     * the first step. This method is called when user clicks the back button.
     */
    public void back() {
        progress--;
        if (lastCompleted > 0) {
            activateStep(steps.get(lastCompleted - 1));
            lastCompleted = steps.indexOf(currentStep);
        }
    }

    private void initDefaultHeader() {
        WizardProgressBar progressBar = new WizardProgressBar(this);
        addListener(progressBar);
        setHeader(progressBar);
    }

    /**
     * Sets a {@link Component} that is displayed on top of the actual content.
     * Set to {@code null} to remove the header altogether.
     *
     * @param newHeader
     *            {@link Component} to be displayed on top of the actual content or {@code null}
     * to remove the header.
     */
    public void setHeader(Component newHeader) {
        if (header != null) {
            if (newHeader == null) {
                mainLayout.removeComponent(header);
            } else {
                mainLayout.replaceComponent(header, newHeader);
            }
        } else {
            if (newHeader != null) {
                mainLayout.addComponentAsFirst(newHeader);
            }
        }
        this.header = newHeader;
    }

    /**
     * Returns a {@link Component} that is displayed on top of the actual
     * content or {@code null} if no header is specified.
     *
     * <p> By default the header is a {@link WizardProgressBar} component that
     * is also registered as a {@link WizardProgressListener} to this
     * XincoWizard. </p>
     *
     * @return {@link Component} that is displayed on top of the actual content
     * or {@code null}.
     */
    public Component getHeader() {
        return header;
    }

    /**
     * Method for adding steps not at the end of the wizard steps. If you are
     * trying to add one at the end use use addStep(WizardStep step) instead.
     *
     * @param step Step to add
     * @param pos Position to add the step
     */
    public void addStep(WizardStep step, int pos) {
        synchronized (steps) {
            if (pos < 0 || pos <= steps.indexOf(currentStep)) {
                throw new RuntimeException("Invalid position: " + pos);
            }
            if (pos >= steps.size()) {
                //Add normally at the end instead
                addStep(step);
                return;
            }
            //Recreate list and id map
            idMap.clear();
            List<WizardStep> newSteps = new ArrayList<WizardStep>();
            for (WizardStep ws : steps) {
                if (newSteps.size() == pos) {
                    //Insert new step
                    newSteps.add(step);
                    idMap.put("wizard-step-" + (newSteps.size()), step);
                }
                newSteps.add(ws);
                idMap.put("wizard-step-" + (newSteps.size()), ws);
            }
            steps.clear();
            steps.addAll(newSteps);
            updateButtons();

            // notify listeners
            fireEvent(new WizardStepSetChangedEvent(this));
        }
    }

    public void addStep(WizardStep step) {
        addStep(step, "wizard-step-" + (steps.size() + 1));
    }

    public void addStep(WizardStep step, String id) {
        steps.add(step);
        idMap.put(id, step);
        updateButtons();

        // notify listeners
        fireEvent(new WizardStepSetChangedEvent(this));
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        // make sure there is always a step selected
        if (currentStep == null && !steps.isEmpty()) {
            // activate the first step
            activateStep(steps.get(0));
        }

        super.paintContent(target);
    }

    public void addListener(WizardProgressListener listener) {
        listeners.add(listener);
    }

    public void removeListener(WizardProgressListener listener) {
        listeners.remove(listener);
    }

    public List<WizardStep> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    public boolean removeStep(WizardStep step) {
        boolean remove = steps.remove(step);
        if (remove) {
            updateButtons();
            // notify listeners
            fireEvent(new WizardStepSetChangedEvent(this));
        }
        return remove;
    }

    public boolean isCompleted(WizardStep step) {
        return steps.indexOf(step) < steps.indexOf(currentStep);
    }

    public boolean isActive(WizardStep step) {
        return (step == currentStep);
    }

    private void updateButtons() {
        if (isLastStep(currentStep)) {
            finishButton.setEnabled(true);
            nextButton.setEnabled(false);
        } else {
            finishButton.setEnabled(false);
            nextButton.setEnabled(true);
        }
        backButton.setEnabled(!isFirstStep(currentStep));
    }

    private boolean isFirstStep(WizardStep step) {
        if (step != null) {
            return steps.indexOf(step) == 0;
        }
        return false;
    }

    private boolean isLastStep(WizardStep step) {
        if (step != null && !steps.isEmpty()) {
            return steps.indexOf(step) == (steps.size() - 1);
        }
        return false;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getFinishButton() {
        return finishButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    private void activateStep(WizardStep step) {
        if (step == null) {
            return;
        }

        if (currentStep != null) {
            if (currentStep.equals(step)) {
                // already active
                return;
            }

            // ask if we're allowed to move
            boolean advancing = steps.indexOf(step) > steps.indexOf(currentStep);
            if (advancing) {
                if (!currentStep.onAdvance()) {
                    // not allowed to advance
                    return;
                }
            } else {
                if (!currentStep.onBack()) {
                    // not allowed to go back
                    return;
                }
            }

            // keep track of the last step that was completed
            lastCompleted = steps.indexOf(currentStep);
            if (lastCompletedStep == null
                    || steps.indexOf(lastCompletedStep) < lastCompleted) {
                lastCompletedStep = currentStep;
            }
        }

        contentPanel.removeAllComponents();
        contentPanel.addComponent(step.getContent());
        currentStep = step;

        updateButtons();
        fireEvent(new WizardStepActivationEvent(this, step));
    }

    @Override
    protected void fireEvent(Event event) {
        for (WizardProgressListener listener : listeners) {
            if (event instanceof WizardCancelledEvent) {
                listener.wizardCancelled((WizardCancelledEvent) event);
            }
            if (event instanceof WizardCompletedEvent) {
                listener.wizardCompleted((WizardCompletedEvent) event);
            }
            if (event instanceof WizardStepActivationEvent) {
                listener.activeStepChanged((WizardStepActivationEvent) event);
            }
            if (event instanceof WizardStepSetChangedEvent) {
                listener.stepSetChanged((WizardStepSetChangedEvent) event);
            }
        }
    }

    public void activateStep(String id) {
        WizardStep step = idMap.get(id);
        if (step != null) {
            // check that we don't go past the lastCompletedStep by using the id
            int lastCompletedIndex = lastCompletedStep == null ? -1 : steps.indexOf(lastCompletedStep);
            int stepIndex = steps.indexOf(step);

            if (lastCompletedIndex < stepIndex) {
                activateStep(lastCompletedStep);
            } else {
                activateStep(step);
            }
        }
    }

    public String getId(WizardStep step) {
        for (Map.Entry<String, WizardStep> entry : idMap.entrySet()) {
            if (entry.getValue().equals(step)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * @return the currentIndex
     */
    public int getLastCompleted() {
        return lastCompleted;
    }

    /**
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }
}
