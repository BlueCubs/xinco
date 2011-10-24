package com.bluecubs.xinco.core.server.vaadin;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public abstract class XincoComponent extends CustomComponent implements IXincoComponent {

    protected MenuBar menubar = null;

    @Override
    public MenuBar getMenuBar() {
        return menubar;
    }
}
