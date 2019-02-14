package com.bluecubs.xinco.core.server.vaadin.custom;

import static com.bluecubs.xinco.core.server.vaadin.Xinco.getInstance;
import com.bluecubs.xinco.server.service.XincoVersion;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.*;
import static java.lang.Integer.MAX_VALUE;
import org.vaadin.risto.stepper.IntStepper;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public final class VersionSelector extends CustomComponent {

    private static final long serialVersionUID = 6980240938236771282L;

    private final String caption;
    private final XincoVersion version;
    private final CheckBox minor = new CheckBox(getString("general.minor"));
    private final IntStepper high = new IntStepper();
    private final IntStepper mid = new IntStepper();
    private final IntStepper low = new IntStepper();
    private TextField postfix;

    public VersionSelector(String caption, XincoVersion version) {
        this.caption = caption;
        this.version = version;
        // The composition root MUST be set
        setCompositionRoot(setup());
    }

    public VersionSelector() {
        caption = "";
        version = new XincoVersion();
        // The composition root MUST be set
        setCompositionRoot(setup());
    }

    public void setMinorEnabled(boolean enable) {
        minor.setEnabled(enable);
    }

    private Component setup() {
        // A layout structure used for composition
        Panel panel = new Panel(caption);
        panel.setContent(new VerticalLayout());
        panel.addComponent(new Label(getString("general.version.postfix") + ":"));
        high.setValue(version.getVersionHigh());
        high.setMaxValue(MAX_VALUE);
        high.setMinValue(version.getVersionHigh());
        high.setStepAmount(1);
        panel.addComponent(high);
        mid.setValue(version.getVersionMid());
        mid.setMaxValue(MAX_VALUE);
        mid.setMinValue(version.getVersionMid());
        mid.setStepAmount(1);
        panel.addComponent(mid);
        low.setValue(version.getVersionLow());
        low.setMaxValue(MAX_VALUE);
        low.setMinValue(version.getVersionLow());
        low.setStepAmount(1);
        panel.addComponent(low);
        postfix = new TextField(getString("general.version.postfix"));
        postfix.setValue(version.getVersionPostfix() == null ? "" : version.getVersionPostfix());
        panel.addComponent(postfix);
        minor.addListener((ValueChangeEvent event) -> {
            if ((Boolean) minor.getValue()) {
                high.setValue(version.getVersionHigh());
                mid.setValue(version.getVersionMid() + 1);
                low.setValue(version.getVersionLow());
            } else {
                high.setValue(version.getVersionHigh() + 1);
                mid.setValue(0);
                low.setValue(0);
            }
        });
        panel.addComponent(minor);
        // Set the size as undefined at all levels
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();
        return panel;
    }

    private String getString(String key) {
        return getInstance().getResource().getString(key);
    }

    public void increaseHigh() {
        high.setValue((Integer) high.getValue() + 1);
    }

    public void increaseMid() {
        mid.setValue((Integer) mid.getValue() + 1);
    }

    public void increaseLow() {
        low.setValue((Integer) low.getValue() + 1);
    }

    /**
     * Get the version currently displayed in the component
     *
     * @return version
     */
    public XincoVersion getVersion() {
        version.setVersionHigh((Integer) high.getValue());
        version.setVersionMid((Integer) mid.getValue());
        version.setVersionLow((Integer) low.getValue());
        return version;
    }

    /**
     * Enable/disable the version fields (only modifiable with the minor check
     * box)
     *
     * @param enable enable the version fields
     */
    public void setVersionEnabled(boolean enable) {
        high.setEnabled(enable);
        mid.setEnabled(enable);
        low.setEnabled(enable);
        postfix.setEnabled(enable);
    }

    public void setVersion(XincoVersion version) {
        this.version.setVersionHigh(version.getVersionHigh());
        this.version.setVersionMid(version.getVersionMid());
        this.version.setVersionLow(version.getVersionLow());
        this.version.setVersionPostfix(version.getVersionPostfix());
        update();
    }

    private void update() {
        high.setValue(version.getVersionHigh());
        high.setMaxValue(MAX_VALUE);
        high.setMinValue(version.getVersionHigh());
        high.setStepAmount(1);
        mid.setValue(version.getVersionMid());
        mid.setMaxValue(MAX_VALUE);
        mid.setMinValue(version.getVersionMid());
        mid.setStepAmount(1);
        low.setValue(version.getVersionLow());
        low.setMaxValue(MAX_VALUE);
        low.setMinValue(version.getVersionLow());
        low.setStepAmount(1);
        postfix.setValue(version.getVersionPostfix() == null ? ""
                : version.getVersionPostfix());
    }
}
