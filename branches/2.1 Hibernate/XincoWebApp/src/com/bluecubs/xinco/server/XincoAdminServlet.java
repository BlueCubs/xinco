/**
 *Copyright 2006 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            XincoAdminServlet
 *
 * Description:     administration servlet
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 * Alexander Manes  11/01/2006        Bugfix: Remove user from selected group (not from all groups!)
 *
 * Javier A. Ortiz  09/20/2006        Modified processRequest in order to reflect
 *                                    changes to XincoCoreUser and XincoCoreUserServer
 *                                    for FDA 21 CFR regulation compliance.
 *
 * Javier A. Ortiz  09/22/2006        Changed the text saved into the audit trail to the actual
 *                                    key entry name in the XincoExplorer.properties file so the
 *                                    reason can be displayed in the selected language!
 *
 * Javier A. Ortiz  11/06/2006        Bugfix: java.lang.NumberFormatException: null when account is locked,
 *                                    requesting a null parameter.
 *
 * Javier A. Ortiz  01/08/2007        Bugfix: java.lang.NullPointerException: null when changing password
 *************************************************************
 */
package com.bluecubs.xinco.server;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroupPK;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributeServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroupServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguageServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.persistence.XincoPersistenceManager;
import com.bluecubs.xinco.core.server.persistence.XincoSettingServer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.bluecubs.xinco.index.XincoIndexThread;
import com.bluecubs.xinco.index.XincoIndexer;
import com.mysql.jdbc.ResultSet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author Alexander Manes
 */
public class XincoAdminServlet extends HttpServlet {

    private ResourceBundle rb;
    private XincoCoreUserServer login_user = null;
    private XincoPersistenceManager pm;
    private List result;
    private HashMap parameters = new HashMap();
    private static XincoConfigSingletonServer config;

    /** Initializes the servlet.
     * @param configS ServletConfig
     * @throws javax.servlet.ServletException 
     */
    @Override
    public void init(ServletConfig configS) throws ServletException {
        super.init(configS);
        config = XincoConfigSingletonServer.getInstance();
    }

    /** Destroys the servlet.
     */
    @Override
    public void destroy() {
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @throws java.lang.Exception 
     */
    @SuppressWarnings("unchecked")
    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        Locale loc = null;
        try {
            String list = request.getParameter("list");
            String[] locales;
            locales = list.split("_");
            switch (locales.length) {
                case 1:
                    loc = new Locale(locales[0]);
                    break;
                case 2:
                    loc = new Locale(locales[0], locales[1]);
                    break;
                case 3:
                    loc = new Locale(locales[0], locales[1], locales[2]);
                    break;
                default:
                    loc = Locale.getDefault();
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.INFO, null, e);
            loc = Locale.getDefault();
        }
        rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
        String global_error_message = "";
        int i = 0,
                j = 0;
        XincoCoreUserServer temp_user = null;
        XincoCoreGroupServer tempGroup;
        XincoCoreLanguageServer temp_language;
        XincoCoreDataTypeAttributeServer tempAttribute;
        String current_location = "";
        String current_locationDesc = "";
        int current_userSelection = 0;
        int currentGroupSelection = 0;
        int currentDatatypeSelection = 0;
        int status = 0;
        String error_message = "";
        HttpSession session = request.getSession(true);
        //start output
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        //connect to db
        try {
            pm = new XincoPersistenceManager();
        } catch (Throwable e) {
            global_error_message = "" + e.toString() + rb.getString("error.configurationfile.incorrect.deployment");
            out.println(global_error_message);
            Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        //do processing of requests

        //check login status
        if (session.getAttribute("XincoAdminServlet.status") == null) {
            status = 0;
            session.setAttribute("XincoAdminServlet.status", new Integer(status));
            current_location = "MainLogin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.login");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
            current_userSelection = 0;
            session.setAttribute("XincoAdminServlet.current_userSelection", new Integer(current_userSelection));
            currentGroupSelection = 0;
            session.setAttribute("XincoAdminServlet.currentGroupSelection", new Integer(currentGroupSelection));
            currentDatatypeSelection = 0;
            session.setAttribute("XincoAdminServlet.currentDatatypeSelection", new Integer(currentDatatypeSelection));
        } else {
            status = ((Integer) session.getAttribute("XincoAdminServlet.status")).intValue();
            current_location = ((String) session.getAttribute("XincoAdminServlet.current_location"));
            current_locationDesc = ((String) session.getAttribute("XincoAdminServlet.current_locationDesc"));
            current_userSelection = ((Integer) session.getAttribute("XincoAdminServlet.current_userSelection")).intValue();
            currentGroupSelection = ((Integer) session.getAttribute("XincoAdminServlet.currentGroupSelection")).intValue();
            currentDatatypeSelection = ((Integer) session.getAttribute("XincoAdminServlet.currentDatatypeSelection")).intValue();
            if (status == 0) {
                current_location = "MainLogin";
                session.setAttribute("XincoAdminServlet.current_location", current_location);
                current_locationDesc = rb.getString("message.location.desc.login");
                session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
                current_userSelection = 0;
                session.setAttribute("XincoAdminServlet.current_userSelection", new Integer(current_userSelection));
                currentGroupSelection = 0;
                session.setAttribute("XincoAdminServlet.currentGroupSelection", new Integer(currentGroupSelection));
                currentDatatypeSelection = 0;
                session.setAttribute("XincoAdminServlet.currentDatatypeSelection", new Integer(currentDatatypeSelection));
            }
        }

        //do login
        if (request.getParameter("DialogLoginSubmit") != null) {
            try {
                try {
                    temp_user = new XincoCoreUserServer(request.getParameter("DialogLoginUsername"),
                            request.getParameter("DialogLoginPassword"));
                    temp_user.setChange(false);
                    //Know who's logged in as administrator since temp_user might be used for other purposes later
                    login_user = temp_user;
                } catch (Exception loginex) {
                    //Wrong password or username
                    result = pm.executeQuery("SELECT p FROM XincoCoreUser p WHERE p.username='" +
                            request.getParameter("DialogLoginUsername") + "' AND p.statusNumber<>2");
                    //Check if the username is correct if not just throw the wrong login message
                    if (result.size() == 0) {
                        throw new XincoException(rb.getString("password.login.fail"));
                    }
                    parameters.clear();
                    parameters.put("username", request.getParameter("DialogLoginUsername"));
                    result = pm.namedQuery("XincoCoreUser.findByUsername", parameters);
                    if (result.size() > 0) {
                        try {
                            XincoCoreUser t = (XincoCoreUser) result.get(0);
                            temp_user = new XincoCoreUserServer(t.getId());
                            long attempts = XincoSettingServer.getSetting("password.attempts").getIntValue();
                            //If user exists increase the atempt tries in the db. If limit reached lock account
                            if (temp_user.getAttempts() >= attempts && t.getId() != 1) {
                                try {

                                    int adminId = 1;
                                    //If no administrator is logged in change is made by default administrator.
                                    if (login_user != null) {
                                        adminId = login_user.getId();
                                    }
                                    temp_user.setChangerID(adminId);
                                    temp_user.setWriteGroups(true);
                                    //Register change in audit trail
                                    temp_user.setChange(true);
                                    //Reason for change
                                    temp_user.setReason(rb.getString("password.attempt.limitReached"));
                                    //the password retrieved when you logon is already hashed...
                                    temp_user.setHashPassword(false);
                                    temp_user.setIncreaseAttempts(true);
                                    temp_user.write2DB();
                                    throw new XincoException(rb.getString("password.attempt.limitReached"));
                                } catch (Throwable ex) {
                                    Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            throw new XincoException(rb.getString("password.login.fail"));
                        } catch (Throwable ex) {
                            Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                //check for admin group
                for (i = 0; i < temp_user.getXincoCoreGroups().size(); i++) {
                    if (((com.bluecubs.xinco.core.persistence.XincoCoreGroup) temp_user.getXincoCoreGroups().elementAt(i)).getId() == 1) {
                        break;
                    }
                }
                if (i == temp_user.getXincoCoreGroups().size()) {
                    throw new XincoException(rb.getString("password.login.notAdminGroup"));
                }
                current_userSelection = temp_user.getId();
                session.setAttribute("XincoAdminServlet.current_userSelection", new Integer(current_userSelection));
                status = 1;
                //Check for password aging
                if (temp_user.getStatusNumber() == 3) {
                    session.setAttribute("XincoAdminServlet.status", new Integer(status - 1));
                }
                session.setAttribute("XincoAdminServlet.status", new Integer(status));
                current_location = "MainMenu";
                session.setAttribute("XincoAdminServlet.current_location", current_location);
                current_locationDesc = rb.getString("message.location.desc.mainmenu");
                session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
            } catch (Throwable e) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
                error_message = "[" + global_error_message + " | " + e.toString() + "]";
                status = 0;
                session.setAttribute("XincoAdminServlet.status", new Integer(status));
            }
        }
        //switch to overview
        if (request.getParameter("MenuMainOverview") != null) {
            current_location = "MainMenu";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.mainmenu");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to user admin
        if (request.getParameter("MenuMainAdminUsers") != null) {
            current_location = "UserAdmin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.useradmin");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to group admin
        if (request.getParameter("MenuMainAdminGroups") != null) {
            current_location = "GroupAdmin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.groupadmin");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to user profile modification
        if (request.getParameter("MenuMainEditUserProfile") != null) {
            current_location = "UserProfileEdit";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.userprofile");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to user profile modification
        if (request.getParameter("DialogAdminUsersEdit") != null) {
            current_location = "AdminUserProfileEdit";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.userprofile");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to language admin
        if (request.getParameter("MenuMainAdminLanguages") != null) {
            current_location = "LanguageAdmin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.languageadmin");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to atrributes admin
        if (request.getParameter("MenuMainAdminAttributes") != null) {
            current_location = "AttributesAdmin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.attributeadmin");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to empty trash
        if (request.getParameter("MenuMainEmptyTrash") != null) {
            current_location = "MainMenu";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.mainmenu");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to reset DB
        if (request.getParameter("MenuMainResetDB") != null) {
            current_location = "MainMenu";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.admin.main.resetDB.label");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to index rebuilt
        if (request.getParameter("MenuMainRebuildIndex") != null) {
            current_location = "RebuildIndex";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.rebuild");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to Audit Menu
        if (request.getParameter("MenuAudit") != null &&
                request.getParameter("MenuAudit").equals("AuditMenu")) {
            current_location = "AuditMenu";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.auditmenu");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to Audit Query
        if (request.getParameter("MenuAudit") != null &&
                request.getParameter("MenuAudit").equals("AuditQuery")) {
            current_location = "AuditQuery";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.auditquery");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to Audit Table
        if (request.getParameter("MenuAudit") != null &&
                request.getParameter("MenuAudit").equals("AuditTable")) {
            current_location = "AuditTable";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.auditresult");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //switch to group modification
        if (request.getParameter("DialogAdminGroupsSelect") != null) {
            currentGroupSelection = Integer.parseInt(request.getParameter("DialogAdminGroupsSelect"));
            session.setAttribute("XincoAdminServlet.currentGroupSelection", new Integer(currentGroupSelection));
            current_location = "GroupAdminSingle";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.specificgroupadmin");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            out.println("Current location: " + current_location);
            out.println("Current location desc: " + current_locationDesc);
        }
        //lock user
        if (request.getParameter("DialogAdminUsersLock") != null) {
            //main admin cannot be locked
            if (!(Integer.parseInt(request.getParameter("DialogAdminUsersLock")) == 1)) {
                try {
                    i = Integer.parseInt(request.getParameter("DialogAdminUsersLock"));
                    temp_user = new XincoCoreUserServer(i);
                    temp_user.setStatusNumber(2);
                    //The logged in admin does the locking
                    if (login_user == null) {
                        temp_user.setChangerID(1);
                    } else {
                        temp_user.setChangerID(login_user.getId());
                    }
                    temp_user.setWriteGroups(true);
                    //Register change in audit trail
                    temp_user.setChange(true);
                    //Reason for change
                    temp_user.setReason("audit.user.account.lock");
                    temp_user.write2DB();
                } catch (Throwable e) {
                    Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
                }
            } else {
                error_message = rb.getString("error.user.account.lock");
            }
        }
        //unlock user
        if (request.getParameter("DialogAdminUsersUnlock") != null) {
            try {
                i = Integer.parseInt(request.getParameter("DialogAdminUsersUnlock"));
                temp_user = new XincoCoreUserServer(i);
                //Prompt the user for new password
                temp_user.setStatusNumber(4);
                //Reset login attempts
                temp_user.setAttempts(0);
                //The logged in admin does the unlocking
                if (login_user == null) {
                    temp_user.setChangerID(1);
                } else {
                    temp_user.setChangerID(login_user.getId());
                }
                temp_user.setWriteGroups(true);
                //Register change in audit trail
                temp_user.setChange(true);
                //Reason for change
                temp_user.setReason("audit.user.account.unlock");
                temp_user.write2DB();
            } catch (Throwable e) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        //reset user's password
        if (request.getParameter("DialogAdminUsersResetPW") != null) {
            try {
                i = Integer.parseInt(request.getParameter("DialogAdminUsersResetPW"));
                temp_user = new XincoCoreUserServer(i);
                temp_user.setUserpassword("123456");
                //The logged in admin does the locking
                if (login_user == null) {
                    temp_user.setChangerID(1);
                } else {
                    temp_user.setChangerID(login_user.getId());
                }
                temp_user.setWriteGroups(true);
                //Register change in audit trail
                temp_user.setChange(true);
                //Reason for change
                temp_user.setReason("audit.user.account.password.reset");
                //Prompt for new password next login - 21 CFR related
                temp_user.setStatusNumber(3);
                temp_user.write2DB();
            } catch (Throwable e) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        //create new user
        if (request.getParameter("DialogNewUserSubmit") != null) {
            try {
                //System.out.println("Creating new user...");
                temp_user = new XincoCoreUserServer(0,
                        request.getParameter("DialogNewUserUsername"),
                        request.getParameter("DialogNewUserPassword"),
                        request.getParameter("DialogNewUserLastname"),
                        request.getParameter("DialogNewUserFirstname"),
                        request.getParameter("DialogNewUserEmail"), 1, 0,
                        new Timestamp(System.currentTimeMillis()));
                tempGroup = new XincoCoreGroupServer(2);
                temp_user.getXincoCoreGroups().addElement(tempGroup);
                //The logged in admin does the locking
                if (login_user == null) {
                    temp_user.setChangerID(temp_user.getId());
                } else {
                    temp_user.setChangerID(login_user.getId());
                }
                temp_user.setWriteGroups(true);
                //Register change in audit trail
                temp_user.setChange(true);
                //Reason for change
                temp_user.setReason("audit.user.account.create");
                temp_user.write2DB();
            } catch (Throwable e) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        //create new group
        if (request.getParameter("DialogNewGroupSubmit") != null) {
            try {
                tempGroup = new XincoCoreGroupServer(0, request.getParameter("DialogNewGroupName"), 1);
                tempGroup.setChangerID(login_user.getId());
                tempGroup.write2DB();
            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //modify group
        if (request.getParameter("DialogEditGroupSubmit") != null) {
            try {
                tempGroup = new XincoCoreGroupServer(currentGroupSelection);
                tempGroup.setDesignation(request.getParameter("DialogEditGroupName"));
                tempGroup.setChangerID(login_user.getId());
                tempGroup.write2DB();

            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //remove user from group
        if (request.getParameter("DialogEditGroupRemoveUser") != null) {
            //main admin always is admin and everyone is a regular user
            if (!(((currentGroupSelection == 1) && (Integer.parseInt(request.getParameter("DialogEditGroupRemoveUser")) == 1)) || (currentGroupSelection == 2))) {
                try {
                    parameters.put("xincoCoreUserId", Integer.parseInt(request.getParameter("DialogEditGroupRemoveUser")));
                    parameters.put("xincoCoreGroupId", currentGroupSelection);
                    result = pm.createdQuery("SELECT x FROM XincoCoreUserHasXincoCoreGroup x " +
                            "WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId and " +
                            "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId", parameters);
                    XincoCoreUserHasXincoCoreGroup.removeFromDB((XincoCoreUserHasXincoCoreGroupServer) result.get(0), login_user.getId());
                } catch (Throwable e) {
                    Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
                }
            } else {
                error_message = rb.getString("error.user.remove.mainUserGroup");
            }
        }
        //add user to group
        if (request.getParameter("DialogEditGroupAddUser") != null) {
            try {
                XincoCoreUserHasXincoCoreGroupServer xcuhxcg =
                        new XincoCoreUserHasXincoCoreGroupServer(
                        new XincoCoreUserHasXincoCoreGroupPK(
                        Integer.parseInt(request.getParameter("DialogEditGroupAddUser")),
                        currentGroupSelection), 1);
                xcuhxcg.setChangerID(login_user.getId());
                xcuhxcg.write2DB();
            } catch (Throwable e) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        //modify user profile
        if (request.getParameter("DialogEditUserProfileSubmit") != null) {
            try {
                temp_user = new XincoCoreUserServer(Integer.parseInt(request.getParameter("DialogEditUserProfileID")));
                temp_user.setUsername(request.getParameter("DialogEditUserProfileUsername"));
                if (request.getParameter("DialogEditUserProfilePassword") != null) {
                    temp_user.setUserpassword(request.getParameter("DialogEditUserProfilePassword"));
                }
                temp_user.setName(request.getParameter("DialogEditUserProfileLastname"));
                temp_user.setFirstname(request.getParameter("DialogEditUserProfileFirstname"));
                temp_user.setEmail(request.getParameter("DialogEditUserProfileEmail"));
                //The logged in admin does the locking
                if (login_user == null) {
                    temp_user.setChangerID(temp_user.getId());
                } else {
                    temp_user.setChangerID(login_user.getId());
                }
                temp_user.setWriteGroups(true);
                //Register change in audit trail
                temp_user.setChange(true);
                //Reason for change
                temp_user.setReason("audit.user.account.modified");
                temp_user.setHashPassword(true);
                temp_user.write2DB();
                //return to same screen
                current_location = "AdminUserProfileEdit";
            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //create new language
        if (request.getParameter("DialogNewLanguageSubmit") != null) {
            try {
                temp_language = new XincoCoreLanguageServer(0,
                        request.getParameter("DialogNewLanguageSign"),
                        request.getParameter("DialogNewLanguageDesignation"));
                temp_language.write2DB();
            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //delete language
        if (request.getParameter("DialogAdminLanguagesDelete") != null) {
            try {
                temp_language = new XincoCoreLanguageServer(Integer.parseInt(request.getParameter("DialogAdminLanguagesDelete")));
                XincoCoreLanguageServer.removeFromDB(temp_language, login_user.getId());
            } catch (Throwable e) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        //switch to attributes modification
        if (request.getParameter("DialogAdminDataTypeSelect") != null) {
            currentDatatypeSelection = Integer.parseInt(request.getParameter("DialogAdminDataTypeSelect"));
            session.setAttribute("XincoAdminServlet.currentDatatypeSelection", new Integer(currentDatatypeSelection));
            current_location = "AttributesAdminSingle";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_locationDesc = rb.getString("message.location.desc.specificattributeadmin");
            session.setAttribute("XincoAdminServlet.current_locationDesc", current_locationDesc);
        }
        //create new attribute
        if (request.getParameter("DialogNewAttributeSubmit") != null) {
            try {
                tempAttribute = new XincoCoreDataTypeAttributeServer(currentDatatypeSelection,
                        Integer.parseInt(request.getParameter("DialogNewAttributeAttributeId")),
                        request.getParameter("DialogNewAttributeDesignation"),
                        request.getParameter("DialogNewAttributeDataType"),
                        Integer.parseInt(request.getParameter("DialogNewAttributeSize")));
                tempAttribute.write2DB();
            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //delete attribute and attribute values
        if (request.getParameter("DialogEditAttributesRemoveAttributeId") != null) {
            try {
                tempAttribute = new XincoCoreDataTypeAttributeServer(new XincoCoreDataTypeAttributePK(currentDatatypeSelection,
                        Integer.parseInt(request.getParameter("DialogEditAttributesRemoveAttributeId"))));
                XincoCoreDataTypeAttributeServer.removeFromDB(tempAttribute, login_user.getId());
            } catch (Throwable e) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        //empty trash
        if (request.getParameter("MenuMainEmptyTrash") != null) {
            if (login_user != null) {
                try {
                    XincoCoreNodeServer.removeFromDB(new XincoCoreNodeServer(2), login_user.getId());
                } catch (Throwable e) {
                    Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        //Reset DB (keeping standard inserts
        if (request.getParameter("MenuMainResetDB") != null) {
            try {
                pm.resetDB(this.login_user);
            } catch (Throwable e) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
            }
            out.println(rb.getString("message.location.desc.resetDBDone"));
        }
        //do logout
        if (request.getParameter("MenuMainLogout") != null) {
            try {
                session.removeAttribute("XincoAdminServlet.user");
                status = 0;
                session.setAttribute("XincoAdminServlet.status", new Integer(status));
            } catch (Throwable e) {
                status = 0;
                session.setAttribute("XincoAdminServlet.status", new Integer(status));
            }
        }
        //Password changed due to aging
        if (request.getParameter("changePassword") != null) {
            try {
                ResultSet rs = null;
                String sql = null;
                int id = 0;
                try {
                    parameters.clear();
                    parameters.put("username", request.getParameter("user").substring(0, request.getParameter("user").length() - 1));
                    result = pm.namedQuery("XincoCoreUser.findByUsername", parameters);
                    temp_user = new XincoCoreUserServer(((XincoCoreUser) result.get(0)).getId());
                } catch (XincoException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                boolean passwordIsUsable = temp_user.isPasswordUsable(request.getParameter("confirm"));
                if (!request.getParameter("new").equals(request.getParameter("confirm"))) {
                    //show welcome message
                    out.println("<br><center><img src=\"resources/images/blueCubs.gif\" border=\"0\"/>");
                    out.println("<br><span class=\"bigtext\">XincoAdmin</span><br><br>");
                    out.println("<form name='changePassword' action='changePassword.jsp' method='post'>");
                    out.println(rb.getString("password.noMatch") + "<br><br>" + "<input type='submit' value='" + rb.getString("general.continue") + "' name='changePassword' />");
                    out.println("<input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type='hidden' name='user' value=" + request.getParameter("id") + "/>");
                    out.println("</form></center>");
                    return;
                }
                if (!passwordIsUsable) {
                    //show welcome message
                    out.println("<br><center><img src=\"resources/images/blueCubs.gif\" border=\"0\"/>");
                    out.println("<br><span class=\"bigtext\">XincoAdmin</span><br><br>");
                    out.println("<form name='changePassword' action='changePassword.jsp' method='post'>");
                    out.println(rb.getString("password.unusable") + "<br><br>" + "<input type='submit' value='" + rb.getString("general.continue") + "' name='changePassword' />");
                    out.println("<input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type='hidden' name='user' value=" + request.getParameter("user") + "/>");
                    out.println("</form></center>");
                    return;
                } else {
                    try {
                        temp_user = new XincoCoreUserServer(id);
                        temp_user.setUserpassword(request.getParameter("new"));
                        temp_user.setLastModified(new Timestamp(System.currentTimeMillis()));
                        //The logged in admin does the locking if none loged in the default admin does the locking
                        if (login_user == null) {
                            temp_user.setChangerID(1);
                        } else {
                            temp_user.setChangerID(login_user.getId());
                        }
                        temp_user.setWriteGroups(true);
                        //Register change in audit trail
                        temp_user.setChange(true);
                        //Reason for change
                        temp_user.setReason("audit.user.account.password.change");
                        temp_user.write2DB();
                        out.println(rb.getString("password.changed"));
                        status = 1;
                    } catch (XincoException ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //show header
        out.println("<html>");
        out.println("<head>");
        out.println("<title>XincoAdmin</title>");
        out.println("<link rel=\"stylesheet\" href=\"xincostyle.css\" type=\"text/css\"/>");
        //Avoid external links if general.setting.allowoutsidelinks is set to false
        //Security bug (must appear in both head and body, is duplicated on purpose)
        if (!config.isAllowOutsideLinks()) {
            out.println(pm.getWebBlockRightClickScript());
        }
        out.println("</head>");
        out.println("<body onload=\"if (document.forms[0] != null) { if (document.forms[0].elements[0] != null) { document.forms[0].elements[0].focus(); } }\">");

        //Avoid external links if general.setting.allowoutsidelinks is set to false
        //Security bug (must appear in both head and body, is duplicated on purpose)
        if (!config.isAllowOutsideLinks()) {
            out.println(pm.getWebBlockRightClickScript());
        }
        out.println("<center>");
        out.println("<span class=\"text\">");

        out.println("");

        //if not logged in
        if (status == 0) {

            //show welcome message
            out.println("<br><img src=\"resources/images/blueCubs.gif\" border=\"0\"/>");
            out.println("<br><span class=\"bigtext\">XincoAdmin</span><br><br>");
            if (error_message.compareTo("") != 0) {
                out.println("<center>" + error_message + "</center>");
            }
            //show login dialog
            out.println("<form action=\"XincoAdmin\" method=\"post\">");
            out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
            out.println("<tr>");
            out.println("<td class=\"text\">" + rb.getString("general.user") + ":</td>");
            out.println("<td class=\"text\"><input type=\"text\" name=\"DialogLoginUsername\" size=\"40\"/></td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"text\">" + rb.getString("general.password") + ":</td>");
            out.println("<td class=\"text\"><input type=\"password\" name=\"DialogLoginPassword\" size=\"40\"/></td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"text\">&nbsp;</td>");
            out.println("<td class=\"text\"><center><input type=\"submit\" name=\"DialogLoginSubmit\" value=\"" + rb.getString("general.login") + "\"/>" +
                    "<input type='hidden' name='list' value='" + request.getParameter("list") + "'/></center></td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("</form>");
        } else if (status == 3) {
            //Password must be changed
            out.println("<br><img src=\"resources/images/blueCubs.gif\" border=\"0\"/>");
            out.println("<br><span class=\"bigtext\">XincoAdmin</span><br><br>");
            out.println("<form name='changePassword' action='changePassword.jsp' method='post'>");
            out.println(rb.getString("password.aged") + "<br><br>" +
                    "<input type='submit' value='" + rb.getString("general.continue") + "' name='changePassword' />");
            out.println("<input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type='hidden' name='user' value=" +
                    request.getParameter("DialogLoginUsername") + "/>");
            out.println("</form>");
        } else {
            //show main menu
            out.println("<br>");
            out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
            out.println("<tr>");
            out.println("<td class=\"text\" rowspan=\"3\"><img src=\"resources/images/blueCubsSmall.gif\" border=\"0\"/></td>");
            out.println("<td class=\"bigtext\" colspan=\"5\">XincoAdmin</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"text\" colspan=\"5\">" + rb.getString("general.location") + ": " + current_locationDesc + "</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainOverview=Overview&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("message.admin.overview") + "</a></td>");
            out.println("<td></td><td class=\"text\">|</td>");
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainAdminUsers=AdminUsers&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("message.admin.userAdmin") + "</a></td>");
            out.println("<td></td><td class=\"text\">|</td>");
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainAdminGroups=AdminGroups&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("message.admin.groupAdmin") + "</a></td>");
            out.println("<td></td><td class=\"text\">|</td>");
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainEditUserProfile=EditProfile&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("message.admin.userProfile") + "</a></td>");
            out.println("<td></td><td class=\"text\">|</td>");
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainAdminLanguages=AdminLanguages&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("message.admin.language") + "</a></td>");
            out.println("<td></td><td class=\"text\">|</td>");
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainAdminAttributes=AdminAttributes&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("message.admin.attribute") + "</a></td>");
            out.println("<td></td><td class=\"text\">|</td>");
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainEmptyTrash=EmptyTrash&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("message.admin.trash") + "</a></td>");
            out.println("<td></td><td class=\"text\">|</td>");
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainRebuildIndex=RebuildIndex&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("message.admin.index") + "</a></td>");
            out.println("<td></td><td class=\"text\">|</td>");
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuAudit=AuditMenu&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("general.audit.menu") + "</a></td>");
            out.println("<td></td><td class=\"text\">|</td>");
            //For now developer's only function. Too dangerous to use at the moment. Maybe add confirmation screens before executing if found as an usefull tool.
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainResetDB=Reset&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("message.admin.main.resetDB.label") + "</a></td>");
                out.println("<td></td><td class=\"text\">|</td>");
            }
            out.println("<td class=\"text\"><a href=\"XincoAdmin?MenuMainLogout=Logout&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">" + rb.getString("general.logout") + "</a></td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<br>");

            if (current_location.compareTo("MainMenu") == 0) {
                //show overview
                out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("message.admin.overview") + "</td>");
                out.println("<td class=\"text\">" + rb.getString("message.admin.overview.message") + "</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("message.admin.userAdmin") + "</td>");
                out.println("<td class=\"text\">" + rb.getString("message.admin.userAdmin.message") + "</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("message.admin.groupAdmin") + "</td>");
                out.println("<td class=\"text\">" + rb.getString("message.admin.groupAdmin.message") + "</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("message.admin.userProfile") + "</td>");
                out.println("<td class=\"text\">" + rb.getString("message.admin.userProfile.message") + "</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("message.admin.language") + "</td>");
                out.println("<td class=\"text\">" + rb.getString("message.admin.language.message") + "</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("message.admin.attribute") + "</td>");
                out.println("<td class=\"text\">" + rb.getString("message.admin.attribute.message") + "</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("message.admin.trash") + "</td>");
                out.println("<td class=\"text\">" + rb.getString("message.admin.trash.message") + "</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("message.admin.index") + "</td>");
                out.println("<td class=\"text\">" + rb.getString("message.admin.index.message") + "</td>");
                out.println("</tr>");
                out.println("<tr>");
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    out.println("<td class=\"bigtext\">" + rb.getString("message.admin.main.resetDB.label") + "</td>");
                    out.println("<td class=\"text\">" + rb.getString("message.admin.main.resetdesc") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");

            }

            if (current_location.compareTo("UserAdmin") == 0) {

                //show new user dialog
                out.println("<form action=\"XincoAdmin\" method=\"post\">");
                out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                out.println("<td class=\"text\">" + rb.getString("general.username") + ":</td>");
                out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewUserUsername\" size=\"40\"/></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\">" + rb.getString("general.password") + ":</td>");
                out.println("<td class=\"text\"><input type=\"password\" name=\"DialogNewUserPassword\" size=\"40\"/></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\">" + rb.getString("general.firstname") + ":</td>");
                out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewUserFirstname\" size=\"40\"/></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\">" + rb.getString("general.lastname") + ":</td>");
                out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewUserLastname\" size=\"40\"/></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\">" + rb.getString("general.email") + ":</td>");
                out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewUserEmail\" size=\"40\"/></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\">&nbsp;</td>");
                out.println("<td class=\"text\"><input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type=\"submit\" name=\"DialogNewUserSubmit\" value=\"" +
                        rb.getString("general.add.user") + "\"/></td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("</form>");

                //show user list
                out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
                if (error_message.compareTo("") != 0) {
                    out.println("<tr>");
                    out.println("<td class=\"text\" colspan=\"6\">" + error_message + "</td>");
                    out.println("</tr>");
                }
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.id") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.username") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.firstname") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.lastname") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.email") + ":</td>");
                out.println("<td class=\"bigtext\">&nbsp;</td>");
                out.println("</tr>");

                Vector allusers = XincoCoreUserServer.getXincoCoreUsers();
                for (i = 0; i < allusers.size(); i++) {
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getId() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getUsername() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getFirstname() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getName() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getEmail() + "</td>");
                    if (((XincoCoreUser) allusers.elementAt(i)).getStatusNumber() <= 3) {
                        String temp = "<td class=\"text\"><a href=\"XincoAdmin?DialogAdminUsers";
                        if (((XincoCoreUser) allusers.elementAt(i)).getStatusNumber() == 2) {
                            temp += "Unlock=" +
                                    ((XincoCoreUser) allusers.elementAt(i)).getId() +
                                    "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.unlock");
                        } else {
                            temp += "Lock=" + ((XincoCoreUser) allusers.elementAt(i)).getId() +
                                    "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.lock");
                        }
                        temp += "]</a>&nbsp;<a href=\"XincoAdmin?DialogAdminUsersResetPW=" +
                                ((XincoCoreUser) allusers.elementAt(i)).getId() +
                                "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.password.reset") + "*]</a>" +
                                "&nbsp;<a href=\"XincoAdmin?DialogAdminUsersEdit=" +
                                ((XincoCoreUser) allusers.elementAt(i)).getId() +
                                "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.edit") + "]</a></td>";
                        out.println(temp);
                    }
                    out.println("</tr>");
                }
                out.println("<tr>");
                out.println("<td colspan=\"6\" class=\"text\">&nbsp;</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td colspan=\"6\" class=\"text\">* " + rb.getString("general.password.note") + "</td>");
                out.println("</tr>");
                out.println("</table>");
            }

            if (current_location.compareTo("GroupAdmin") == 0) {

                //show new group dialog
                out.println("<form action=\"XincoAdmin\" method=\"post\">");
                out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                out.println("<td class=\"text\">" + rb.getString("general.name") + ":</td>");
                out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewGroupName\" size=\"40\"/></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\">&nbsp;</td>");
                out.println("<td class=\"text\"><input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type=\"submit\" name=\"DialogNewGroupSubmit\" value=\"" +
                        rb.getString("general.add.group") + "\"/></td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("</form>");

                //show group list
                out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.id") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.name") + ":</td>");
                out.println("<td class=\"bigtext\">&nbsp;</td>");
                out.println("</tr>");

                Vector allgroups = XincoCoreGroupServer.getXincoCoreGroups();
                for (i = 0; i < allgroups.size(); i++) {
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + ((XincoCoreGroup) allgroups.elementAt(i)).getId() + "</td>");
                    //Default groups
                    if (((XincoCoreGroup) allgroups.elementAt(i)).getId() <= 1000) {
                        out.println("<td class=\"text\">" + rb.getString(((XincoCoreGroup) allgroups.elementAt(i)).getDesignation()) + "</td>");
                    } else {
                        out.println("<td class=\"text\">" + ((XincoCoreGroup) allgroups.elementAt(i)).getDesignation() + "</td>");
                    }
                    out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogAdminGroupsSelect=" +
                            ((XincoCoreGroup) allgroups.elementAt(i)).getId() +
                            "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" +
                            rb.getString("general.edit") + "]</a></td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }

            if (current_location.compareTo("GroupAdminSingle") == 0) {

                //show group modification dialog
                try {
                    tempGroup = new XincoCoreGroupServer(currentGroupSelection);
                    out.println("<form action=\"XincoAdmin\" method=\"post\">");
                    out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.name") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditGroupName\" size=\"40\" value=\"" + pm.localizeString(tempGroup.getDesignation()) + "\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">&nbsp;</td>");
                    out.println("<td class=\"text\"><input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type=\"hidden\" name=\"DialogEditGroupID\" value=\"" + currentGroupSelection + "\"/><input type=\"submit\" name=\"DialogEditGroupSubmit\" value=\"" +
                            rb.getString("general.save") + "!\"/></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                } catch (Throwable e) {
                }

                //show user list
                Vector allusers = XincoCoreUserServer.getXincoCoreUsers();
                boolean member_ofGroup = false;

                out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");

                if (error_message.compareTo("") != 0) {
                    out.println("<tr>");
                    out.println("<td class=\"text\" colspan=\"6\">" + error_message + "</td>");
                    out.println("</tr>");
                }
                out.println("<tr>");
                out.println("<td class=\"bigtext\" colspan=\"6\">" + rb.getString("general.group.member") + ":</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.id") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.username") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.firstname") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.lastname") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.email") + ":</td>");
                out.println("<td class=\"bigtext\">&nbsp;</td>");
                out.println("</tr>");
                for (i = 0; i < allusers.size(); i++) {
                    member_ofGroup = false;
                    for (j = 0; j < ((XincoCoreUserServer) allusers.elementAt(i)).getXincoCoreGroups().size(); j++) {
                        if (((XincoCoreGroup) ((XincoCoreUserServer) allusers.elementAt(i)).getXincoCoreGroups().elementAt(j)).getId() == currentGroupSelection) {
                            member_ofGroup = true;
                            break;
                        }
                    }
                    if (member_ofGroup) {
                        out.println("<tr>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getId() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getUsername() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getFirstname() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getName() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getEmail() + "</td>");
                        out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogEditGroupRemoveUser=" +
                                ((XincoCoreUser) allusers.elementAt(i)).getId() +
                                "&list=" + request.getParameter("list") +
                                "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.group.removeuser") + "]</a></td>");
                        out.println("</tr>");
                    }
                }

                out.println("<tr>");
                out.println("<td class=\"bigtext\" colspan=\"6\">&nbsp;</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\" colspan=\"6\">" + rb.getString("general.group.notmember") + ":</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.id") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.username") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.firstname") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.lastname") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.email") + ":</td>");
                out.println("<td class=\"bigtext\">&nbsp;</td>");
                out.println("</tr>");
                for (i = 0; i < allusers.size(); i++) {
                    member_ofGroup = false;
                    for (j = 0; j < ((XincoCoreUserServer) allusers.elementAt(i)).getXincoCoreGroups().size(); j++) {
                        if (((XincoCoreGroup) ((XincoCoreUserServer) allusers.elementAt(i)).getXincoCoreGroups().elementAt(j)).getId() == currentGroupSelection) {
                            member_ofGroup = true;
                            break;
                        }
                    }
                    if (!member_ofGroup) {
                        out.println("<tr>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getId() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getUsername() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getFirstname() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getName() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUser) allusers.elementAt(i)).getEmail() + "</td>");
                        out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogEditGroupAddUser=" +
                                ((XincoCoreUser) allusers.elementAt(i)).getId() +
                                "&list=" + request.getParameter("list") +
                                "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.group.adduser") + "]</a></td>");
                        out.println("</tr>");
                    }
                }
                out.println("</table>");
            }

            if (current_location.compareTo("UserProfileEdit") == 0) {
                //show user profile modification dialog
                try {
                    temp_user = new XincoCoreUserServer(current_userSelection);
                    out.println("<form action=\"XincoAdmin\" method=\"post\">");
                    out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.username") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditUserProfileUsername\" value=\"" + temp_user.getUsername() + "\" size=\"40\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.password") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"password\" name=\"DialogEditUserProfilePassword\" value=\"" + temp_user.getUserpassword() + "\" size=\"40\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.firstname") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditUserProfileFirstname\" value=\"" + temp_user.getFirstname() + "\" size=\"40\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.lastname") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditUserProfileLastname\" value=\"" + temp_user.getName() + "\" size=\"40\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.email") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditUserProfileEmail\" value=\"" + temp_user.getEmail() + "\" size=\"40\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">&nbsp;</td>");
                    out.println("<td class=\"text\"><input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type=\"hidden\" name=\"DialogEditUserProfileID\" value=\"" +
                            current_userSelection + "\"/><input type=\"submit\" name=\"DialogEditUserProfileSubmit\" value=\"" + rb.getString("general.save") + "!\"/></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                } catch (Throwable e) {
                }

            }

            if (current_location.compareTo("AdminUserProfileEdit") == 0) {
                //show user profile modification dialog
                //Admin can change other user's info
                try {
                    temp_user = new XincoCoreUserServer(Integer.valueOf(request.getParameter("DialogAdminUsersEdit")));
                    out.println("<form action=\"XincoAdmin\" method=\"post\">");
                    out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.username") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditUserProfileUsername\" value=\"" + temp_user.getUsername() + "\" size=\"40\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.firstname") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditUserProfileFirstname\" value=\"" + temp_user.getFirstname() + "\" size=\"40\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.lastname") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditUserProfileLastname\" value=\"" + temp_user.getName() + "\" size=\"40\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.email") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditUserProfileEmail\" value=\"" + temp_user.getEmail() + "\" size=\"40\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">&nbsp;</td>");
                    out.println("<td class=\"text\"><input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type=\"hidden\" name=\"DialogEditUserProfileID\" value=\"" +//
                            request.getParameter("DialogAdminUsersEdit") + "\"/><input type=\"hidden\" name=\"DialogAdminUsersEdit\" value=\"" +//
                            request.getParameter("DialogAdminUsersEdit") + "\"/><input type=\"submit\" name=\"DialogEditUserProfileSubmit\" value=\"" + rb.getString("general.save") + "!\"/></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                } catch (Throwable e) {
                }

            }

            if (current_location.compareTo("LanguageAdmin") == 0) {

                //show new language dialog
                out.println("<form action=\"XincoAdmin\" method=\"post\">");
                out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                out.println("<td class=\"text\">" + rb.getString("general.designation") + ":</td>");
                out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewLanguageDesignation\" size=\"40\"/></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\">" + rb.getString("general.sign") + ":</td>");
                out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewLanguageSign\" size=\"5\"/></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\">&nbsp;</td>");
                out.println("<td class=\"text\"><input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type=\"submit\" name=\"DialogNewLanguageSubmit\" value=\"" +
                        rb.getString("general.add.language") + "\"/></td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("</form>");

                //show language list
                out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
                if (error_message.compareTo("") != 0) {
                    out.println("<tr>");
                    out.println("<td class=\"text\" colspan=\"4\">" + error_message + "</td>");
                    out.println("</tr>");
                }
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.id") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.sign") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.designation") + ":</td>");
                out.println("<td class=\"bigtext\">&nbsp;</td>");
                out.println("</tr>");

                Vector alllanguages = XincoCoreLanguageServer.getXincoCoreLanguages();
                boolean is_used = true;
                for (i = 0; i < alllanguages.size(); i++) {
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + ((XincoCoreLanguageServer) alllanguages.elementAt(i)).getId() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreLanguageServer) alllanguages.elementAt(i)).getSign() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreLanguageServer) alllanguages.elementAt(i)).getDesignation() + "</td>");
                    is_used = XincoCoreLanguageServer.isLanguageUsed((XincoCoreLanguageServer) alllanguages.elementAt(i));
                    if (!is_used) {
                        out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogAdminLanguagesDelete=" +
                                ((XincoCoreLanguageServer) alllanguages.elementAt(i)).getId() +
                                "&list=" + request.getParameter("list") +
                                "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.delete") + "]</a></td>");
                    } else {
                        out.println("<td class=\"text\">&nbsp;</td>");
                    }
                    out.println("</tr>");
                }
                out.println("<tr>");
                out.println("<td colspan=\"4\" class=\"text\">&nbsp;</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td colspan=\"4\" class=\"text\">" +
                        rb.getString("error.language.delete.referenced") + "</td>");
                out.println("</tr>");
                out.println("</table>");
            }
            if (current_location.compareTo("AttributesAdmin") == 0) {
                //show group list
                out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.id") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.designation") + ":</td>");
                out.println("<td class=\"bigtext\">" + rb.getString("general.description") + ":</td>");
                out.println("<td class=\"bigtext\">&nbsp;</td>");
                out.println("</tr>");

                Vector alldatatypes = XincoCoreDataTypeServer.getXincoCoreDataTypes();
                for (i = 0; i < alldatatypes.size(); i++) {
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + ((XincoCoreDataTypeServer) alldatatypes.elementAt(i)).getId() + "</td>");
                    out.println("<td class=\"text\">" + pm.localizeString(((XincoCoreDataTypeServer) alldatatypes.elementAt(i)).getDesignation()) + "</td>");
                    out.println("<td class=\"text\">" + pm.localizeString(((XincoCoreDataTypeServer) alldatatypes.elementAt(i)).getDescription()) + "</td>");
                    out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogAdminDataTypeSelect=" +
                            ((XincoCoreDataTypeServer) alldatatypes.elementAt(i)).getId() +
                            "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.edit") + "]</a></td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }

            if (current_location.compareTo("AttributesAdminSingle") == 0) {
                //show add attribute dialog
                try {
                    out.println("<form action=\"XincoAdmin\" method=\"post\">");
                    out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.position") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewAttributeAttributeId\" size=\"5\" value=\"1\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.designation") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewAttributeDesignation\" size=\"40\" value=\"\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.datatype") + ":</td>");
                    out.println("<td class=\"text\"><select name=\"DialogNewAttributeDataType\" size=\"1\"><option value=\"int\">int (Integer)</option><option value=\"unsignedint\">unsignedint (Unsigned Integer)</option><option value=\"double\">double (Floating Point Number)</option><option value=\"varchar\">varchar (String)</option><option value=\"text\">text (Text)</option><option value=\"datetime\">datetime (Date + Time)</option></select></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.size") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogNewAttributeSize\" size=\"5\" value=\"0\"/> " + rb.getString("message.admin.attribute.req4string") + "</td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">&nbsp;</td>");
                    out.println("<td class=\"text\"><input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type=\"hidden\" name=\"DialogNewAttributeDataTypeID\" value=\"" +
                            currentDatatypeSelection + "\"/><input type=\"submit\" name=\"DialogNewAttributeSubmit\" value=\"" +
                            rb.getString("general.add.attribute") + "\"/></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                    out.flush();
                } catch (Throwable e) {
                }

                //show attributes list
                try {
                    XincoCoreDataTypeServer tempDatatype = new XincoCoreDataTypeServer(currentDatatypeSelection);

                    out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");

                    if (error_message.compareTo("") != 0) {
                        out.println("<tr>");
                        out.println("<td class=\"text\" colspan=\"5\">" + error_message + "</td>");
                        out.println("</tr>");
                    }
                    out.println("<tr>");
                    out.println("<td class=\"bigtext\" colspan=\"5\">" + rb.getString("general.attribute.ofthistype") + ":</td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"bigtext\">" + rb.getString("general.position") + ":</td>");
                    out.println("<td class=\"bigtext\">" + rb.getString("general.designation") + ":</td>");
                    out.println("<td class=\"bigtext\">" + rb.getString("general.position") + ":</td>");
                    out.println("<td class=\"bigtext\">" + rb.getString("general.size") + ":</td>");
                    out.println("<td class=\"bigtext\">&nbsp;</td>");
                    out.println("</tr>");
                    for (i = 0; i < tempDatatype.getXincoCoreDataTypeAttributes().size(); i++) {
                        out.println("<tr>");
                        out.println("<td class=\"text\">" + ((XincoCoreDataTypeAttributeServer) tempDatatype.getXincoCoreDataTypeAttributes().elementAt(i)).getXincoCoreDataTypeAttributePK().getAttributeId() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreDataTypeAttributeServer) tempDatatype.getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreDataTypeAttributeServer) tempDatatype.getXincoCoreDataTypeAttributes().elementAt(i)).getDataType() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreDataTypeAttributeServer) tempDatatype.getXincoCoreDataTypeAttributes().elementAt(i)).getSize() + "</td>");
                        if (((currentDatatypeSelection == 1) && (((XincoCoreDataTypeAttributeServer) tempDatatype.getXincoCoreDataTypeAttributes().elementAt(i)).getXincoCoreDataTypeAttributePK().getAttributeId() <= 8)) ||
                                ((currentDatatypeSelection == 2) && (((XincoCoreDataTypeAttributeServer) tempDatatype.getXincoCoreDataTypeAttributes().elementAt(i)).getXincoCoreDataTypeAttributePK().getAttributeId() <= 1)) ||
                                ((currentDatatypeSelection == 3) && (((XincoCoreDataTypeAttributeServer) tempDatatype.getXincoCoreDataTypeAttributes().elementAt(i)).getXincoCoreDataTypeAttributePK().getAttributeId() <= 1))) {
                            out.println("<td class=\"text\">&nbsp;</td>");
                        } else {
                            out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogEditAttributesRemoveAttributeId=" +
                                    ((XincoCoreDataTypeAttributeServer) tempDatatype.getXincoCoreDataTypeAttributes().elementAt(i)).getXincoCoreDataTypeAttributePK().getAttributeId() +
                                    "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.delete") + "*]</a></td>");
                        }
                        out.println("</tr>");
                    }
                    out.println("<tr>");
                    out.println("<td class=\"text\" colspan=\"5\">&nbsp;</td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\" colspan=\"5\">" + rb.getString("message.warning.attribute.remove") + "</td>");
                    out.println("</tr>");
                    out.println("</table>");
                } catch (Throwable e) {
                }
            }
            if (current_location.compareTo("AuditTable") == 0) {
                try {
                    out.write("\n");
                    out.write("\n");
                    out.write(" \n");
                    out.write(" \n");
                    out.write("\n");
                    out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n");
                    out.write("\"http://www.w3.org/TR/html4/loose.dtd\">\n");
                    out.write("\n");
                    out.write("<html>\n");
                    out.write("    <head>\n");
                    out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
                    out.write("        <title>");
                    out.println(rb.getString("general.audit.results").replaceAll("%i",
                            request.getParameter("id")).replaceAll("%t",
                            request.getParameter("table")));
                    out.write("</title>\n");
                    out.write("    </head>\n");
                    out.write("    <body>\n");
                    out.write("        <center>");
                    out.write("            ");

                    ResultSet rs;
                    pm.setLocale(loc);
                    String column = "id";
                    if (request.getParameter("table").equals("xincoAddAttribute")) {
                        column = "xincoCoreDataId";
                    }
                    if (request.getParameter("table").equals("xincoCoreDataTypeAttribute")) {
                        column = "xincoCoreDataTypeId";
                    }
//                    rs = .
//                    executeQuery("select * from " + request.getParameter("table") +
//                            "T a, (select a.firstname || ' ' || a.name as \"" +
//                            rb.getString("general.user") + "\" , b.modTime as \"" + rb.getString("general.audit.modtime") +
//                            "\" ,b.mod_reason as \"" + rb.getString("general.reason") + "\" ,b.recordId " +
//                            "from xincoCore_user a,xincoCore_user_modified_record b where a.id=b.id " +
//                            ") b where b.recordId =a.recordId and a." + column +
//                            " = '" + request.getParameter("id") + "' order by a.recordId desc");
//                    .
//                    drawTable(rs, response.getWriter(), .getColumnNames(rs)
//                    
//                    
//                    
//                    
//                    ,
//                            "<center>" + rb.getString("general.audit.results").replaceAll("%i",
//                            request.getParameter("id")).replaceAll("%t",
//                            request.getParameter("table")) + "<br>", 
//                    
//                    
//                    
//                    
//                    -1, false, -1);
                    out.write("\n");
                    out.write("        </center>\n");
                    out.write("    </body>\n");
                    out.write("</html>\n");
                } catch (Throwable e) {
                    global_error_message = global_error_message + e.toString();
                    Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if (current_location.compareTo("AuditQuery") == 0) {
                try {
                    out.write("\n");
                    out.write("\n");
                    out.write(" \n");
                    out.write(" \n");
                    out.write("\n");
                    out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n");
                    out.write("\"http://www.w3.org/TR/html4/loose.dtd\">\n");
                    out.write("\n");
                    out.write("<html>\n");
                    out.write("    <head>\n");
                    out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
                    out.write("        <title>");
                    out.println(rb.getString("general.audit.select.searchparameters"));
                    out.write("</title>\n");
                    out.write("    </head>\n");
                    out.write("    <body>\n");
                    out.write("        <center>");
                    out.write("            <h1>");
                    out.println(rb.getString("general.audit.select.record") + request.getParameter("table"));
                    out.write("</h1>\n");
                    out.write("            ");

                    ResultSet rs;
                    String column = "id";
                    if (request.getParameter("table").equals("xincoAddAttribute")) {
                        column = "xincoCoreDataId";
                    }
                    if (request.getParameter("table").equals("xincoCoreDataTypeAttribute")) {
                        column = "xincoCoreDataTypeId";
                    }
//                    rs = .
//                    executeQuery("select distinct * from " + request.getParameter("table") +
//                            " where " + column + " in (select distinct " + column + " from " + request.getParameter("table") + "T)");
//                    .
//                    drawTable(rs, response.getWriter(), .getColumnNames(rs)
//                      
//                      
//                      
//                      
//                      , "", -1, false, -1);
//                    rs = 
//                    
//                    
//                    
//                    
//                    .executeQuery("select distinct " + column + " from " + request.getParameter("table") + "T");
//                    out.println("<form action='XincoAdmin?MenuAudit=AuditTable' method='POST'>");
//                    rs = .
//                    executeQuery("select distinct " + column + " from " +
//                            request.getParameter("table") + "T");
                    out.println("Select record id: ");
                    out.println("<select name='id'>");
//                    while (rs.next()) {
//                        out.println("<option >" + rs.getString(1) + "</option>");
//                    }
                    out.println("</select>");
                    out.println("<br><br><input type='submit' value='" + rb.getString("menu.view") + "' />" +
                            "<input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type='hidden' name='table' value=" + request.getParameter("table") + " /></form>");

                    out.write("\n");
                    out.write("        </center>\n");
                    out.write("    </body>\n");
                    out.write("</html>\n");
                } catch (Throwable e) {
                    global_error_message = global_error_message + e.toString();
                    Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if (current_location.compareTo("AuditMenu") == 0) {
                try {
                    out.write("\n");
                    out.write("\n");
                    out.write(" \n");
                    out.write(" \n");
                    out.write("\n");
                    out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n");
                    out.write("\"http://www.w3.org/TR/html4/loose.dtd\">\n");
                    out.write("\n");
                    out.write("<html>\n");
                    out.write("    <head>\n");
                    out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
                    out.write("        <title>");
                    out.println(rb.getString("message.warning.attribute.remove"));
                    out.write("</title>\n");
                    out.write("    </head>\n");
                    out.write("    <body>\n");
                    out.write("        \n");
                    out.write("        <center><h1>");
                    out.write("        ");

                    ResultSet rs;
//                    =  new Xincoanager();
//                    DatabaseMetaData meta = .
//                    getConnection().getMetaData();
                    String[] types = {"TABLE"};
//                    rs = meta.getTables(null, null, null, types);
                    out.println("<center><table border='0'><tbody><thead><tr><th>" +
                            rb.getString("general.table") + "</th><th>" +
                            rb.getString("general.audit.action") + "</th></tr>");
//                    while (rs.next()) {
//                        if (!rs.getString("TABLE_NAME").endsWith("T") &&
//                                !rs.getString("TABLE_NAME").equals("xincoId") &&
//                                !rs.getString("TABLE_NAME").equals("xincoCore_log") &&
//                                !rs.getString("TABLE_NAME").equals("xincoCore_user_modified_record") &&
//                                rs.getString("TABLE_NAME").startsWith("xinco") &&
//                                !rs.getString("TABLE_NAME").equals("xincoCore_user_has_xincoCoreGroup")) {
//                            out.println("<form action='XincoAdmin?MenuAudit=AuditQuery' method='POST'>");
//                            out.println("<tr><td>" + rs.getString("TABLE_NAME") + "</td><td><center><input type='submit' value='" +
//                                    rb.getString("general.continue") + "'/></center></td></tr>" +
//                                    "<input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type='hidden' name='table' value='" + rs.getString("TABLE_NAME") + "' /></form>");
//                        }
//                    }
                    out.println("</tbody></table></center>");

                    out.write("\n");
                    out.write("    </body>\n");
                    out.write("</html>\n");
                } catch (Throwable e) {
                    global_error_message = global_error_message + e.toString();
                    Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if (current_location.compareTo("RebuildIndex") == 0) {
                //rebuild index and list status
                XincoSettingServer s2 = null;
                try {
                    s2 = new XincoSettingServer(XincoSettingServer.getSetting("setting.index.lock").getId());
                    out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
                    out.println("<tr>");
                    out.println("<td class=\"bigtext\" colspan=\"2\">" + rb.getString("message.index.rebuild") + "</td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\" colspan=\"2\">" + rb.getString("message.warning.index.rebuild") + "</td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\" colspan=\"2\">&nbsp;</td>");
                    out.println("</tr>");
                    s2.setBoolValue(true);
                    s2.setChangerID(login_user.getId());
                    s2.write2DB();
                    //delete existing index
                    File indexDirectory = null;
                    boolean indexDirectoryDeleted = false;
                    indexDirectory = new File(config.getFileIndexPath());
                    if (indexDirectory.exists()) {
                        indexDirectoryDeleted = XincoIndexThread.deleteIndex();
                        out.println("<tr>");
                        out.println("<td class=\"text\"><b>" + rb.getString("message.index.delete") + "</b></td>");
                        if (indexDirectoryDeleted) {
                            out.println("<td class=\"text\">" + rb.getString("general.ok") + "!</td>");
                        } else {
                            out.println("<td class=\"text\">" + rb.getString("general.fail") + "</td>");
                        }
                        out.println("</tr>");
                    }
                    out.println("<tr>");
                    out.println("<td class=\"text\">&nbsp;</td>");
                    out.println("<td class=\"text\">&nbsp;</td>");
                    out.println("</tr>");

                    //select all data
                    out.println("<tr>");
                    out.println("<td class=\"text\"><b>" +
                            rb.getString("message.data.sort.designation") + "</b></td>");
                    out.println("<td class=\"text\"><b>" +
                            rb.getString("message.indexing.status") + "</b></td>");
                    out.println("</tr>");
                    XincoCoreDataServer xdataTemp = null;
                    boolean index_result = false;
//                    ResultSet rs = .
//                    executeQuery("SELECT id FROM xincoCoreData ORDER BY designation");
//                    while (rs.next()) {
//                        xdataTemp = new XincoCoreDataServer(rs.getInt("id"));
//                        index_result = XincoIndexer.indexXincoCoreData(xdataTemp, true);
//                        out.println("<tr>");
//                        out.println("<td class=\"text\">" + xdataTemp.getDesignation() + "</td>");
//                        if (index_result) {
//                            out.println("<td class=\"text\">" + rb.getString("general.ok") + "!</td>");
//                        } else {
//                            out.println("<td class=\"text\">" + rb.getString("general.fail") + "</td>");
//                        }
//                        out.println("</tr>");
//                        out.flush();
//                    }

                    //optimize index
                    index_result = XincoIndexer.optimizeIndex();
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("message.index.optimize") + "</td>");
                    if (index_result) {
                        out.println("<td class=\"text\">" + rb.getString("general.ok") + "!</td>");
                    } else {
                        out.println("<td class=\"text\">" + rb.getString("general.fail") + "</td>");
                    }
                    out.println("</tr>");
                    out.flush();

                    out.println("</table>");
                    s2.setBoolValue(false);
                    s2.setChangerID(login_user.getId());
                    s2.write2DB();
                } catch (Throwable e) {
                    out.println("</table>");
                    if (s2 != null) {
                        s2.setBoolValue(false);
                        s2.setChangerID(login_user.getId());
                        s2.write2DB();
                    }
                }
            }
        }

        //show footer
        out.println("<br><br><br>");
        out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
        out.println("<tr>");
        out.println("<td class=\"text\">&nbsp;</td>");
        out.println("<td class=\"text\">&copy; " + XincoSettingServer.getSetting("general.copyright.date").getStringValue() + ", " +
                //Avoid external links if general.setting.allowoutsidelinks is set to false
                //Security bug
                (config.isAllowOutsideLinks() ? rb.getString("message.admin.main.footer") : "blueCubs.com and xinco.org"));
        out.println("</tr>");
        out.println("</table><tr><form action='menu.jsp'><input type='submit' value='" +
                rb.getString("message.admin.main.backtomain") + "' />" +
                "<input type='hidden' name='list' value='" + request.getParameter("list") + "'/></form></tr>" +
                "<tr><FORM><INPUT TYPE='button' VALUE='" + rb.getString("message.admin.main.back") +
                "' onClick='history.go(-1);return true;'><input type='hidden' name='list' value='" +
                request.getParameter("list") + "'/></FORM></tr>");
        out.println("</span>");
        out.println("</center>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Returns a short description of the servlet.
     * @return String
     */
    @Override
    public String getServletInfo() {
        return rb.getString("message.servlet.info");
    }
}
