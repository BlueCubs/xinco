package com.bluecubs.xinco.core.server.vaadin;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoException;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import com.bluecubs.xinco.core.server.service.XincoCoreUser;
import com.bluecubs.xinco.core.server.service.XincoVersion;
import com.bluecubs.xinco.tools.XincoFileIconManager;
import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;

/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
public class Xinco extends Application {

    private Window mainWindow = null;
    //client version
    private XincoVersion xincoClientVersion = null;
    //TODO: use selected language
    private ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", Locale.getDefault());
    private XincoCoreUser loggedUser;
    private XincoExplorer explorer = null;
    private XincoFileIconManager xfm = new XincoFileIconManager();

    @Override
    public void init() {
        //Default user (System)
        try {
            loggedUser=new XincoCoreUserServer(1);
        } catch (XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }
        xincoClientVersion = new XincoVersion();
        try {
            xincoClientVersion.setVersionHigh(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.high").getIntValue());
            xincoClientVersion.setVersionMid(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.mid").getIntValue());
            xincoClientVersion.setVersionLow(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.low").getIntValue());
            xincoClientVersion.setVersionPostfix(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.postfix").getStringValue());
        } catch (com.bluecubs.xinco.core.server.XincoException ex) {
            Logger.getLogger(Xinco.class.getName()).log(Level.SEVERE, null, ex);
        }

        explorer = new XincoExplorer(this);
        mainWindow = new Window(xerb.getString("general.clienttitle") + " - "
                + xerb.getString("general.version") + " "
                + xincoClientVersion.getVersionHigh() + "."
                + xincoClientVersion.getVersionMid() + "."
                + xincoClientVersion.getVersionLow() + " "
                + xincoClientVersion.getVersionPostfix());
        setMainWindow(mainWindow);
        setComponent(explorer);
    }
    
    private void setComponent(XincoComponent comp){
        mainWindow.removeAllComponents();
        if(comp.getMenuBar()!=null && !comp.getMenuBar().getItems().isEmpty()){
            mainWindow.addComponent(comp.getMenuBar());
        }
        mainWindow.addComponent(comp);
    }

    protected ThemeResource getIcon(String extension) throws IOException {
        WebApplicationContext context = (WebApplicationContext) getContext();
        File iconsFolder = new File(context.getHttpSession().getServletContext().getRealPath("/WEB-INF") + System.getProperty("file.separator") + "icons");
        if (!iconsFolder.exists()) {
            //Create it
            iconsFolder.mkdirs();
        }
        Image image = iconToImage(xfm.getIcon(extension));
        BufferedImage buffered = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffered.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        if (extension.indexOf('.') > -1) {
            extension = extension.substring(extension.lastIndexOf('.') + 1,
                    extension.length());
        }
        File icon = new File(iconsFolder.getAbsolutePath()
                + System.getProperty("file.separator") + extension + ".png");
        ImageIO.write(buffered, "PNG", icon);
        ThemeResource resource = new ThemeResource(".." + System.getProperty("file.separator")
                + ".." + System.getProperty("file.separator")
                + "icons" + System.getProperty("file.separator") + extension + ".png");
        getMainWindow().showNotification(resource.getResourceId());
        return resource;
    }

    static Image iconToImage(Icon icon) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        Graphics2D g = image.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return image;
    }

    /**
     * @return the xerb
     */
    public ResourceBundle getResource() {
        return xerb;
    }

    /**
     * @return the loggedUser
     */
    public XincoCoreUser getLoggedUser() {
        return loggedUser;
    }
}
