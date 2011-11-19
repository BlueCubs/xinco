package com.bluecubs.xinco.core.server.vaadin.custom;

import com.bluecubs.xinco.core.server.service.XincoVersion;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.*;
import java.util.Locale;
import java.util.ResourceBundle;
import org.vaadin.risto.stepper.IntStepper;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class VersionSelector extends CustomComponent {

    private final String caption;
    private final XincoVersion version;
    private final CheckBox minor = new CheckBox(getString("general.minor"));
    private final IntStepper high = new IntStepper();
    private final IntStepper mid = new IntStepper();
    private final IntStepper low = new IntStepper();

    public VersionSelector(String caption, XincoVersion version) {
        this.caption = caption;
        this.version = version;
        // The composition root MUST be set
        setCompositionRoot(setup());
    }

    public VersionSelector() {
        caption = "";
        version = new XincoVersion();
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
        high.setMaxValue(Integer.MAX_VALUE);
        high.setMinValue(version.getVersionHigh());
        high.setStepAmount(1);
        panel.addComponent(high);
        mid.setValue(version.getVersionMid());
        mid.setMaxValue(Integer.MAX_VALUE);
        mid.setMinValue(version.getVersionMid());
        mid.setStepAmount(1);
        panel.addComponent(mid);
        low.setValue(version.getVersionLow());
        low.setMaxValue(Integer.MAX_VALUE);
        low.setMinValue(version.getVersionLow());
        low.setStepAmount(1);
        panel.addComponent(low);
        TextField postfix = new TextField(getString("general.version.postfix"));
        postfix.setValue(version.getVersionPostfix());
        panel.addComponent(postfix);
        minor.addListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if ((Boolean) minor.getValue()) {
                    high.setValue(version.getVersionHigh());
                    mid.setValue(version.getVersionMid() + 1);
                    low.setValue(version.getVersionLow());
                } else {
                    high.setValue(version.getVersionHigh() + 1);
                    mid.setValue(0);
                    low.setValue(0);
                }
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
        return ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", Locale.getDefault()).getString(key);
        //TODO: Use lookup
//        return Lookup.getDefault().lookup(XincoVaadinApplication.class).getResource().getString(key);
    }

    public void increaseHigh() {
        high.setValue((Integer) high.getValue() + 1);
    }

    public XincoVersion getVersion() {
        version.setVersionHigh((Integer) high.getValue());
        version.setVersionMid((Integer) mid.getValue());
        version.setVersionLow((Integer) low.getValue());
        return version;
    }
}
