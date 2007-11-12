/*
 * XincoSettingManagerView.java
 */
package com.bluecubs.xinco.tools.SettingManager;

import com.bluecubs.xinco.client.XincoClientConnectionProfile;
import com.bluecubs.xinco.client.XincoMutableTreeNode;
import com.bluecubs.xinco.client.XincoSettingManagerSession;
import com.bluecubs.xinco.client.dialog.SettingManagerConnectionDialog;
import com.bluecubs.xinco.client.object.timer.XincoActivityTimer;
import com.bluecubs.xinco.core.XincoCoreDataType;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.persistance.XincoSetting;
import com.bluecubs.xinco.service.XincoServiceLocator;
import com.bluecubs.xinco.tools.SettingManager.*;
import java.rmi.RemoteException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.Task;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.persistence.Query;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;

/**
 * The application's main frame.
 */
public class XincoSettingManagerView extends FrameView {

    private com.bluecubs.xinco.persistance.XincoSetting x = null;
    private XincoSettingManagerSession xincoClientSession;
    private SettingManagerConnectionDialog dialog;
    private ResourceBundle xerb = null;
    private Vector xincoClientConfig = null;
    private Locale loc = null;
    private XincoSettingManagerView.loginThread loginT;
    private XincoCoreUser temp,  newuser = null;
    //client version

    private XincoVersion xincoClientVersion = null;

    @SuppressWarnings("unchecked")
    public XincoSettingManagerView(SingleFrameApplication app) {
        super(app);
        //load config
        System.out.println("Loading configuration...");
        loadConfig();
        System.out.println("Loaded!");
        saveConfig();
        try {
            if (((Locale) xincoClientConfig.elementAt(2)).toString().indexOf("_") == -1) {
                loc = new Locale(((Locale) xincoClientConfig.elementAt(2)).toString());
            } else {
                loc = new Locale(((Locale) xincoClientConfig.elementAt(2)).toString().substring(0, ((Locale) xincoClientConfig.elementAt(2)).toString().indexOf("_")),
                        ((Locale) xincoClientConfig.elementAt(2)).toString().substring(((Locale) xincoClientConfig.elementAt(2)).toString().indexOf("_") + 1,
                        ((Locale) xincoClientConfig.elementAt(2)).toString().length()));
            }
        } catch (Exception e) {
            loc = Locale.getDefault();
        }
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
        setLocale(xerb.getLocale());

        System.out.println("Connecting...");
        connect(true);
        System.out.println("Conected");

        Map props = new HashMap();
        props.put("toplink.jdbc.user", temp.getUsername());
        props.put("toplink.jdbc.url", xincoClientSession.getService_endpoint());
        props.put("toplink.jdbc.password", temp.getUserpassword());
        props.put("toplink.jdbc.driver", xincoClientSession.getJDBCDriver());
        entityManager=javax.persistence.Persistence.createEntityManagerFactory("xinco?autoReconnect=truePU", props).createEntityManager();
        System.out.println("Switched to servers information...");
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        statusMessageLabel.setText("");
                    }
                });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                        statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
                    }
                });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

                    public void propertyChange(java.beans.PropertyChangeEvent evt) {
                        String propertyName = evt.getPropertyName();
                        if ("started".equals(propertyName)) {
                            if (!busyIconTimer.isRunning()) {
                                statusAnimationLabel.setIcon(busyIcons[0]);
                                busyIconIndex = 0;
                                busyIconTimer.start();
                            }
                            progressBar.setVisible(true);
                            progressBar.setIndeterminate(true);
                        } else if ("done".equals(propertyName)) {
                            busyIconTimer.stop();
                            statusAnimationLabel.setIcon(idleIcon);
                            progressBar.setVisible(false);
                            progressBar.setValue(0);
                        } else if ("message".equals(propertyName)) {
                            String text = (String) (evt.getNewValue());
                            statusMessageLabel.setText((text == null) ? "" : text);
                            messageTimer.restart();
                        } else if ("progress".equals(propertyName)) {
                            int value = (Integer) (evt.getNewValue());
                            progressBar.setVisible(true);
                            progressBar.setIndeterminate(false);
                            progressBar.setValue(value);
                        }
                    }
                });

        // tracking table selection
        masterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                    public void valueChanged(ListSelectionEvent e) {
                        firePropertyChange("recordSelected", !isRecordSelected(), isRecordSelected());
                    }
                });

        // tracking changes to save
        bindingGroup.addBindingListener(new AbstractBindingListener() {

                    @Override
            public void targetEdited(Binding binding) {
                        // save action observes saveNeeded property
                        if (!saveNeeded) {
                            saveNeeded = true;
                            firePropertyChange("saveNeeded", false, true);
                        }
                    }
                });

        // have a transaction started
        entityManager.getTransaction().begin();
    }

    public Vector getConfig() {
        return xincoClientConfig;
    }

    public ResourceBundle getResourceBundle() {
        return xerb;
    }

    public boolean isSaveNeeded() {
        return saveNeeded;
    }

    public boolean isRecordSelected() {
        return masterTable.getSelectedRow() != -1;
    }

    @Action
    public void newRecord() {
        x = new XincoSetting();
        entityManager.persist(x);
        list.add(x);
        int row = list.size() - 1;
        masterTable.setRowSelectionInterval(row, row);
        masterTable.scrollRectToVisible(masterTable.getCellRect(row, 0, true));
    }

    @Action(enabledProperty = "recordSelected")
    public void deleteRecord() {
        int[] selected = masterTable.getSelectedRows();
        List<com.bluecubs.xinco.persistance.XincoSetting> toRemove = new ArrayList<com.bluecubs.xinco.persistance.XincoSetting>(selected.length);
        for (int idx = 0; idx < selected.length; idx++) {
            com.bluecubs.xinco.persistance.XincoSetting x = list.get(masterTable.convertRowIndexToModel(selected[idx]));
            toRemove.add(x);
            entityManager.remove(x);
        }
        list.removeAll(toRemove);
    }

    @Action(enabledProperty = "saveNeeded")
    public Task save() {
        return new Task(getApplication()) {

                    protected Void doInBackground() {
                        entityManager.getTransaction().commit();
                        entityManager.getTransaction().begin();
                        return null;
                    }

                    @Override
            protected void finished() {
                        saveNeeded = false;
                        XincoSettingManagerView.this.firePropertyChange("saveNeeded", true, false);
                    }
                };
    }

    /**
     * An example action method showing how to create asynchronous tasks
     * (running on background) and how to show their progress. Note the
     * artificial 'Thread.sleep' calls making the task long enough to see the
     * progress visualization - remove the sleeps for real application.
     */
    @Action
     public Task refresh() {
        return new Task(getApplication()) {

                    @SuppressWarnings("unchecked")
                    protected Void doInBackground() {

                        setProgress(0, 0, 4);
                        setMessage("Rolling back the current changes...");
                        setProgress(1, 0, 4);
                        entityManager.getTransaction().rollback();
                        setProgress(2, 0, 4);

                        setMessage("Starting a new transaction...");
                        entityManager.getTransaction().begin();
                        setProgress(3, 0, 4);

                        setMessage("Fetching new data...");
                        java.util.Collection data = query.getResultList();
                        setProgress(4, 0, 4);

                        list.clear();
                        list.addAll(data);
                        return null;
                    }

                    @Override
                    protected void finished() {
                        setMessage("Done.");
                    }
                };
    }

    @Action
    public void showAboutBox(ActionEvent e) {
        if (aboutBox == null) {
            JFrame mainFrame = XincoSettingManagerApp.getApplication().getMainFrame();
            aboutBox = new XincoSettingManagerAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        XincoSettingManagerApp.getApplication().show(aboutBox);
    }

    private SettingManagerConnectionDialog getJDialogConnection() {
        if (dialog == null) {
            dialog = new SettingManagerConnectionDialog(null, true, this);
        }
        return dialog;
    }

    /**
     * Verify connection settings from web server in case default settings are not the ones used
     */
    private void updateConnectionSettings() {

    }

    /**
     * Connect to Xinco
     */
    public void connect(boolean prompt) {
        int i = 0;
        if (prompt) {
            //init session
            setXincoClientSession(new XincoSettingManagerSession(this));
            getSession().setStatus(0);
            //open connection dialog
            getJDialogConnection().setVisible(true);
            DefaultListModel dlm = (DefaultListModel) dialog.getProfileList().getModel();
            dlm.removeAllElements();
            for (i = 0; i < ((Vector) xincoClientConfig.elementAt(0)).size(); i++) {
                dlm.addElement(new String(((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(i)).toString()));
            }
        }
        //establish connection and login
        if (getSession().getStatus() == 1) {
            loginT = new loginThread(this);
            getLoginT().start();
        }
    }

    XincoSettingManagerView.loginThread getLoginT() {
        return loginT;
    }

    public XincoSettingManagerSession getSession() {
        return xincoClientSession;
    }

    public void setXincoClientSession(XincoSettingManagerSession xincoClientSession) {
        this.xincoClientSession = xincoClientSession;
    }

    private class loginThread extends Thread {

        loginThread(XincoSettingManagerView m) {
            setMe(m);
            login();
        }
        private XincoSettingManagerView me;

        @Action
     public Task login() {
            return new Task(getApplication()) {

                        private XincoActivityTimer xat;
                        private String status_string_1,  status_string_2;

                        @SuppressWarnings("unchecked")
                    protected Void doInBackground() throws RemoteException {

                            int i = 0;
                            try {
                                getSession().setXinco_service(new XincoServiceLocator());
                                getSession().setXinco(getSession().getXinco_service().getXinco(new java.net.URL(getSession().getService_endpoint())));
                                getSession().setServer_version(getSession().getXinco().getXincoServerVersion());
                                //check if client and server versions match (high AND mid must match!)
                                if ((xincoClientVersion.getVersion_high() != getSession().getServer_version().getVersion_high()) || (xincoClientVersion.getVersion_mid() != getSession().getServer_version().getVersion_mid())) {
                                    throw new XincoException(xerb.getString("menu.connection.error.serverversion") + " " + getSession().getServer_version().getVersion_high() + "." + getSession().getServer_version().getVersion_mid() + ".x");
                                }
                                if ((temp = getSession().getXinco().getCurrentXincoCoreUser(getSession().getUser().getUsername(), getSession().getUser().getUserpassword())) == null) {
                                    throw new XincoException(xerb.getString("menu.connection.error.user"));
                                }
                                getJDialogConnection().updateProfile();
                                temp.setUserpassword(getSession().getUser().getUserpassword());
                                newuser.setEmail(temp.getEmail());
                                newuser.setFirstname(temp.getFirstname());
                                newuser.setId(temp.getId());
                                newuser.setName(temp.getName());
                                newuser.setStatus_number(temp.getStatus_number());
                                newuser.setUsername(temp.getUsername());
                                newuser.setUserpassword(temp.getUserpassword());
                                getSession().setUser(getSession().getXinco().getCurrentXincoCoreUser(newuser.getUsername(), newuser.getUserpassword()));
                                getSession().setServer_datatypes(getSession().getXinco().getAllXincoCoreDataTypes(getSession().getUser()));
                                getSession().setServer_groups(getSession().getXinco().getAllXincoCoreGroups(getSession().getUser()));
                                getSession().setServer_languages(getSession().getXinco().getAllXincoCoreLanguages(getSession().getUser()));
                                getSession().setServer_users(getSession().getXinco().getAllXincoUsers(getSession().getUser()));
                                String space = "      + ";
                                for (i = 0; i < getSession().getUser().getXinco_core_groups().size(); i++) {
                                    status_string_1 += space + ((XincoCoreGroup) getSession().getUser().getXinco_core_groups().elementAt(i)).getDesignation() + "\n";
                                }
                                for (i = 0; i < getSession().getServer_datatypes().size(); i++) {
                                    status_string_2 += space + getSession().getXinco().localizeString(((XincoCoreDataType) getSession().getServer_datatypes().elementAt(i)).getDesignation(), getLocale().toString()) + "\n";
                                }
                            } catch (java.rmi.RemoteException cone) {
                                setProgress(0, 0, 4);
                                setMessage(xerb.getString("menu.connection.failed") + " " +
                                        xerb.getString("general.reason") + ": " +
                                        cone.toString() + " " + xerb.getString("error.connection.incorrect.deployment"));
                                getSession().setStatus(0);
                                cone.printStackTrace();
                            } catch (Exception cone) {
                                getSession().setStatus(0);
                                cone.printStackTrace();
                                String exception = "";
                                if (cone.toString().equals("java.lang")) {
                                    exception = xerb.getString("error.connection.incorrect.deployment");
                                } else {
                                    exception = cone.toString();
                                }
                                setMessage(xerb.getString("menu.connection.failed") + " " +
                                        xerb.getString("general.reason") + ": " +
                                        cone.toString());
                                Query q = entityManager.createNamedQuery("XincoSetting.findByDescription").setParameter("description", "general.setting.enable.developermode");
                                x = (XincoSetting) q.getSingleResult();
                                if (x.getBoolValue()) {
                                    cone.printStackTrace();
                                }
                            }

                            String status_string = "";
                            temp = getSession().getXinco().getCurrentXincoCoreUser(getSession().getUser().getUsername(), getSession().getUser().getUserpassword());
                            status_string += xerb.getString("menu.connection.connectedto") + ": " + getSession().getService_endpoint() + "\n";
                            status_string += xerb.getString("general.serverversion") + ": ";
                            status_string += getSession().getServer_version().getVersion_high() + ".";
                            status_string += (getSession().getServer_version().getVersion_mid() < 9 ? "0" +
                                    getSession().getServer_version().getVersion_mid() : getSession().getServer_version().getVersion_mid()) + ".";
                            status_string += (getSession().getServer_version().getVersion_low() < 9 ? "0" +
                                    getSession().getServer_version().getVersion_low() : getSession().getServer_version().getVersion_low());
                            status_string += " " + getSession().getServer_version().getVersion_postfix() + "\n";
                            status_string += "\n";
                            status_string += xerb.getString("general.user") + ": " + getSession().getUser().getFirstname() + " " + getSession().getUser().getName() + " <" + getSession().getUser().getEmail() + ">\n";
                            status_string += xerb.getString("general.memberof") + ":\n";
                            status_string += status_string_1 + "\n";
                            status_string += xerb.getString("general.groupsonserver") + ": " + getSession().getServer_groups().size() + "\n";
                            status_string += xerb.getString("general.languagesonserver") + ": " + getSession().getServer_languages().size() + "\n";
                            status_string += xerb.getString("general.datatypesonserver") + ": " + getSession().getServer_datatypes().size() + "\n";
                            status_string += status_string_2 + "\n";
                            getSession().setCurrentSearchResult(new Vector());
                            getSession().setStatus(2);
                            setMessage(xerb.getString("menu.connection.established"));
                            //get root
                            XincoCoreNode xnode = new XincoCoreNode();
                            xnode.setId(1);
                            xnode = getSession().getXinco().getXincoCoreNode(xnode, getSession().getUser());
                            getSession().getXincoClientRepository().assignObject2TreeNode((XincoMutableTreeNode) (getSession().getXincoClientRepository().treemodel).getRoot(),
                                    xnode,
                                    getSession().getXinco(), getSession().getUser(), 2);
                            if (temp.getStatus_number() == 3) {
                                setMessage(xerb.getString("password.aged"));
                                getJDialogUser(true);
                            }
                            resetStrings();
                            return null;
                        }

                        public void resetStrings() {
                            status_string_1 = "";
                            status_string_2 =
                                    "";
                        }
                    };
        }

        private void getJDialogUser(boolean b) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        public XincoSettingManagerView getMe() {
            return me;
        }

        public void setMe(XincoSettingManagerView me) {
            this.me = me;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        mainPanel = new javax.swing.JPanel();
        masterScrollPane = new javax.swing.JScrollPane();
        masterTable = new javax.swing.JTable();
        intValueLabel = new javax.swing.JLabel();
        stringValueLabel = new javax.swing.JLabel();
        boolValueLabel = new javax.swing.JLabel();
        longValueLabel = new javax.swing.JLabel();
        intValueField = new javax.swing.JTextField();
        stringValueField = new javax.swing.JTextField();
        boolValueField = new javax.swing.JTextField();
        longValueField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem newRecordMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem deleteRecordMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JMenuItem saveMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem refreshMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("xinco?autoReconnect=truePU").createEntityManager();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.bluecubs.xinco.tools.SettingManager.XincoSettingManagerApp.class).getContext().getResourceMap(XincoSettingManagerView.class);
        query = entityManager.createQuery(resourceMap.getString("query.query")); // NOI18N
        list = org.jdesktop.observablecollections.ObservableCollections.observableList(query.getResultList());

        mainPanel.setName("mainPanel"); // NOI18N

        masterScrollPane.setName("masterScrollPane"); // NOI18N

        masterTable.setName("masterTable"); // NOI18N

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, list, masterTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${id}"));
        columnBinding.setColumnName("Id");
        columnBinding.setColumnClass(Integer.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${description}"));
        columnBinding.setColumnName("Description");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);

        masterScrollPane.setViewportView(masterTable);

        intValueLabel.setText(resourceMap.getString("intValueLabel.text")); // NOI18N
        intValueLabel.setName("intValueLabel"); // NOI18N

        stringValueLabel.setText(resourceMap.getString("stringValueLabel.text")); // NOI18N
        stringValueLabel.setName("stringValueLabel"); // NOI18N

        boolValueLabel.setText(resourceMap.getString("boolValueLabel.text")); // NOI18N
        boolValueLabel.setName("boolValueLabel"); // NOI18N

        longValueLabel.setText(resourceMap.getString("longValueLabel.text")); // NOI18N
        longValueLabel.setName("longValueLabel"); // NOI18N

        intValueField.setName("intValueField"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.intValue}"), intValueField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), intValueField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        stringValueField.setName("stringValueField"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.stringValue}"), stringValueField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), stringValueField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        boolValueField.setName("boolValueField"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.boolValue}"), boolValueField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), boolValueField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        longValueField.setName("longValueField"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.longValue}"), longValueField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), longValueField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.bluecubs.xinco.tools.SettingManager.XincoSettingManagerApp.class).getContext().getActionMap(XincoSettingManagerView.class, this);
        saveButton.setAction(actionMap.get("save")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N

        refreshButton.setAction(actionMap.get("refresh")); // NOI18N
        refreshButton.setName("refreshButton"); // NOI18N

        newButton.setAction(actionMap.get("newRecord")); // NOI18N
        newButton.setName("newButton"); // NOI18N

        deleteButton.setAction(actionMap.get("deleteRecord")); // NOI18N
        deleteButton.setName("deleteButton"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(newButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(intValueLabel)
                            .addComponent(stringValueLabel)
                            .addComponent(boolValueLabel)
                            .addComponent(longValueLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(intValueField, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                            .addComponent(stringValueField, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                            .addComponent(boolValueField, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                            .addComponent(longValueField, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(masterScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)))
                .addContainerGap())
        );

        mainPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {deleteButton, newButton, refreshButton, saveButton});

        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(masterScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(intValueLabel)
                    .addComponent(intValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stringValueLabel)
                    .addComponent(stringValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boolValueLabel)
                    .addComponent(boolValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(longValueLabel)
                    .addComponent(longValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(refreshButton)
                    .addComponent(deleteButton)
                    .addComponent(newButton))
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newRecordMenuItem.setAction(actionMap.get("newRecord")); // NOI18N
        newRecordMenuItem.setName("newRecordMenuItem"); // NOI18N
        fileMenu.add(newRecordMenuItem);

        deleteRecordMenuItem.setAction(actionMap.get("deleteRecord")); // NOI18N
        deleteRecordMenuItem.setName("deleteRecordMenuItem"); // NOI18N
        fileMenu.add(deleteRecordMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        saveMenuItem.setAction(actionMap.get("save")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        fileMenu.add(saveMenuItem);

        refreshMenuItem.setAction(actionMap.get("refresh")); // NOI18N
        refreshMenuItem.setName("refreshMenuItem"); // NOI18N
        fileMenu.add(refreshMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField boolValueField;
    private javax.swing.JLabel boolValueLabel;
    private javax.swing.JButton deleteButton;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JTextField intValueField;
    private javax.swing.JLabel intValueLabel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private java.util.List<com.bluecubs.xinco.persistance.XincoSetting> list;
    private javax.swing.JTextField longValueField;
    private javax.swing.JLabel longValueLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane masterScrollPane;
    private javax.swing.JTable masterTable;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton newButton;
    private javax.swing.JProgressBar progressBar;
    private javax.persistence.Query query;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextField stringValueField;
    private javax.swing.JLabel stringValueLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private boolean saveNeeded;

    public Locale getLocale() {
        return loc;
    }

    public void setLocale(Locale loc) {
        this.loc = loc;
    }

    /**
     * This method loads the configuration
     *
     * @return void
     */
    @SuppressWarnings("unchecked")
    private void loadConfig() {
        Vector tmp_vector_old = new Vector();
        try {
            Vector tmp_vector;
            FileInputStream fin;
            ObjectInputStream ois;

            //get old settings
            try {
                fin = new FileInputStream("xincoClientConnectionProfiles.dat");
                ois = new ObjectInputStream(fin);
                try {
                    while ((tmp_vector = (Vector) ois.readObject()) != null) {
                        tmp_vector_old = tmp_vector;
                    }
                } catch (Exception ioe3) {
                }
                ois.close();
                fin.close();
            } catch (Exception ioe2) {
                tmp_vector_old = null;
            }

            fin = new FileInputStream(System.getProperty("user.home") +
                    System.getProperty("file.separator") + "xincoClientConfig.dat");
            ois = new ObjectInputStream(fin);

            try {
                while ((tmp_vector = (Vector) ois.readObject()) != null) {
                    xincoClientConfig = tmp_vector;
                }
            } catch (Exception ioe3) {
            }

            ois.close();
            fin.close();

            //insert old settings
            if (tmp_vector_old != null) {
                xincoClientConfig.setElementAt(tmp_vector_old, 0);
            }
            //delete old settings
            (new File("xincoClientConnectionProfiles.dat")).delete();

        } catch (Exception ioe) {
            //error handling
            //create config
            xincoClientConfig = new Vector();
            //add connection profiles
            xincoClientConfig.addElement(new Vector());
            //insert old settings
            if (tmp_vector_old != null) {
                xincoClientConfig.setElementAt(tmp_vector_old, 0);
                //delete old settings
                (new File("xincoClientConnectionProfiles.dat")).delete();
            } else {
                //insert connection profiles
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "xinco Demo User";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://xinco.org:8080/xinco_demo/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "user";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "user";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = false;
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "xinco Demo Admin";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://xinco.org:8080/xinco_demo/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "admin";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "admin";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = false;
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "Template Profile";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://[server_domain]:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "your_username";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = false;
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "Admin (localhost)";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "admin";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = false;
                ((Vector) xincoClientConfig.elementAt(0)).addElement(new XincoClientConnectionProfile());
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).profile_name = "User (localhost)";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).service_endpoint = "http://localhost:8080/xinco/services/Xinco";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).username = "user";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).password = "";
                ((XincoClientConnectionProfile) ((Vector) xincoClientConfig.elementAt(0)).elementAt(((Vector) xincoClientConfig.elementAt(0)).size() - 1)).save_password = false;
            }
            //add Pluggable Look and Feel
            xincoClientConfig.addElement(new String("javax.swing.plaf.metal.MetalLookAndFeel"));
            //add locale
            xincoClientConfig.addElement(Locale.getDefault());
        }
    }
    /**
     * This method saves the configuration
     *
     */

    public void saveConfig() {
        try {
            java.io.FileOutputStream fout = new java.io.FileOutputStream(System.getProperty("user.home") +
                    System.getProperty("file.separator") + "xincoClientConfig.dat");
            java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(fout);
            os.writeObject(xincoClientConfig);
            os.close();
            fout.close();
        } catch (Exception ioe) {
        }
    }
    }
