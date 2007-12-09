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

import java.sql.*;
import java.io.*;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.index.XincoIndexThread;
import com.bluecubs.xinco.index.XincoIndexer;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * @author javydreamercsw
 */
public class XincoAdminServlet extends HttpServlet {

    private ResourceBundle rb;
    private String credentialID = "";
    private XincoCoreUserServer login_user = null;
    private XincoDBManager DBM = null;

    /** Initializes the servlet.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /** Destroys the servlet.
     */
    @Override
    public void destroy() {
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
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
            loc = Locale.getDefault();
        }
        rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
        String global_error_message = "";
        int i = 0,
                j = 0;
        XincoCoreUserServer temp_user = null;
        XincoCoreGroupServer temp_group;
        XincoCoreLanguageServer temp_language;
        XincoCoreDataTypeAttributeServer temp_attribute;
        String current_location = "";
        String current_location_desc = "";
        int current_user_selection = 0;
        int current_group_selection = 0;
        int current_datatype_selection = 0;
        int status = 0;
        String error_message = "";
        HttpSession session = request.getSession(true);
        //start output
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        //connect to db
        try {
            DBM = new XincoDBManager();
        } catch (Throwable e) {
            global_error_message = "" + e.toString() + rb.getString("error.configurationfile.incorrect.deployment");
            out.println(global_error_message);
            return;
        }
        //do processing of requests

        //check login status
        if (session.getAttribute("XincoAdminServlet.status") == null) {
            status = 0;
            session.setAttribute("XincoAdminServlet.status", new Integer(status));
            current_location = "MainLogin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.login");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
            current_user_selection = 0;
            session.setAttribute("XincoAdminServlet.current_user_selection", new Integer(current_user_selection));
            current_group_selection = 0;
            session.setAttribute("XincoAdminServlet.current_group_selection", new Integer(current_group_selection));
            current_datatype_selection = 0;
            session.setAttribute("XincoAdminServlet.current_datatype_selection", new Integer(current_datatype_selection));
        } else {
            status = ((Integer) session.getAttribute("XincoAdminServlet.status")).intValue();
            current_location = ((String) session.getAttribute("XincoAdminServlet.current_location"));
            current_location_desc = ((String) session.getAttribute("XincoAdminServlet.current_location_desc"));
            current_user_selection = ((Integer) session.getAttribute("XincoAdminServlet.current_user_selection")).intValue();
            current_group_selection = ((Integer) session.getAttribute("XincoAdminServlet.current_group_selection")).intValue();
            current_datatype_selection = ((Integer) session.getAttribute("XincoAdminServlet.current_datatype_selection")).intValue();
            if (status == 0) {
                current_location = "MainLogin";
                session.setAttribute("XincoAdminServlet.current_location", current_location);
                current_location_desc = rb.getString("message.location.desc.login");
                session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
                current_user_selection = 0;
                session.setAttribute("XincoAdminServlet.current_user_selection", new Integer(current_user_selection));
                current_group_selection = 0;
                session.setAttribute("XincoAdminServlet.current_group_selection", new Integer(current_group_selection));
                current_datatype_selection = 0;
                session.setAttribute("XincoAdminServlet.current_datatype_selection", new Integer(current_datatype_selection));
            }
        }

        //do login
        if (request.getParameter("DialogLoginSubmit") != null) {
            try {
                try {
                    temp_user = new XincoCoreUserServer(request.getParameter("DialogLoginUsername"), request.getParameter("DialogLoginPassword"), DBM);
                    temp_user.setChange(false);
                    //Know who's logged in as administrator since temp_user might be used for other purposes later
                    login_user = temp_user;
                } catch (Exception loginex) {
                    //Wrong password or username
                    ResultSet rs = DBM.executeQuery("SELECT id FROM xinco_core_user WHERE username='" +
                            request.getParameter("DialogLoginUsername") + "' AND status_number<>2");
                    //Check if the username is correct if not just throw the wrong login message
                    if (!rs.next()) {
                        throw new XincoException(rb.getString("password.login.fail"));
                    }
                    rs = DBM.executeQuery("SELECT id FROM xinco_core_user WHERE username='" +
                            request.getParameter("DialogLoginUsername") + "'");
                    if (rs.next()) {
                        try {
                            temp_user = new XincoCoreUserServer(rs.getInt("id"), DBM);
                            long attempts = DBM.getSetting("password.attempts").getInt_value();
                            //If user exists increase the atempt tries in the db. If limit reached lock account
                            if (temp_user.getAttempts() >= attempts && rs.getInt("id") != 1) {
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
                                    temp_user.write2DB(DBM);
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
                for (i = 0; i < temp_user.getXinco_core_groups().size(); i++) {
                    if (((XincoCoreGroup) temp_user.getXinco_core_groups().elementAt(i)).getId() == 1) {
                        break;
                    }
                }
                if (i == temp_user.getXinco_core_groups().size()) {
                    throw new XincoException(rb.getString("password.login.notAdminGroup"));
                }
                current_user_selection = temp_user.getId();
                session.setAttribute("XincoAdminServlet.current_user_selection", new Integer(current_user_selection));
                status = 1;
                //Check for password aging
                if (temp_user.getStatus_number() == 3) {
                    session.setAttribute("XincoAdminServlet.status", new Integer(status - 1));
                }
                session.setAttribute("XincoAdminServlet.status", new Integer(status));
                current_location = "MainMenu";
                session.setAttribute("XincoAdminServlet.current_location", current_location);
                current_location_desc = rb.getString("message.location.desc.mainmenu");
                session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
            } catch (Throwable e) {
                e.printStackTrace();
                error_message = "[" + global_error_message + " | " + e.toString() + "]";
                status = 0;
                session.setAttribute("XincoAdminServlet.status", new Integer(status));
            }
        }
        //switch to overview
        if (request.getParameter("MenuMainOverview") != null) {
            current_location = "MainMenu";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.mainmenu");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to user admin
        if (request.getParameter("MenuMainAdminUsers") != null) {
            current_location = "UserAdmin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.useradmin");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to group admin
        if (request.getParameter("MenuMainAdminGroups") != null) {
            current_location = "GroupAdmin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.groupadmin");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to user profile modification
        if (request.getParameter("MenuMainEditUserProfile") != null) {
            current_location = "UserProfileEdit";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.userprofile");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to user profile modification
        if (request.getParameter("DialogAdminUsersEdit") != null) {
            current_location = "AdminUserProfileEdit";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.userprofile");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to language admin
        if (request.getParameter("MenuMainAdminLanguages") != null) {
            current_location = "LanguageAdmin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.languageadmin");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to atrributes admin
        if (request.getParameter("MenuMainAdminAttributes") != null) {
            current_location = "AttributesAdmin";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.attributeadmin");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to empty trash
        if (request.getParameter("MenuMainEmptyTrash") != null) {
            current_location = "MainMenu";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.mainmenu");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to reset DB
        if (request.getParameter("MenuMainResetDB") != null) {
            current_location = "MainMenu";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.admin.main.resetDB.label");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to index rebuilt
        if (request.getParameter("MenuMainRebuildIndex") != null) {
            current_location = "RebuildIndex";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.rebuild");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to Audit Menu
        if (request.getParameter("MenuAudit") != null &&
                request.getParameter("MenuAudit").equals("AuditMenu")) {
            current_location = "AuditMenu";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.auditmenu");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to Audit Query
        if (request.getParameter("MenuAudit") != null &&
                request.getParameter("MenuAudit").equals("AuditQuery")) {
            current_location = "AuditQuery";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.auditquery");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to Audit Table
        if (request.getParameter("MenuAudit") != null &&
                request.getParameter("MenuAudit").equals("AuditTable")) {
            current_location = "AuditTable";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.auditresult");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //switch to group modification
        if (request.getParameter("DialogAdminGroupsSelect") != null) {
            current_group_selection = Integer.parseInt(request.getParameter("DialogAdminGroupsSelect"));
            session.setAttribute("XincoAdminServlet.current_group_selection", new Integer(current_group_selection));
            current_location = "GroupAdminSingle";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.specificgroupadmin");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
            out.println("Current location: " + current_location);
            out.println("Current location desc: " + current_location_desc);
        }
        //lock user
        if (request.getParameter("DialogAdminUsersLock") != null) {
            //main admin cannot be locked
            if (!(Integer.parseInt(request.getParameter("DialogAdminUsersLock")) == 1)) {
                try {
                    i = Integer.parseInt(request.getParameter("DialogAdminUsersLock"));
                    temp_user = new XincoCoreUserServer(i, DBM);
                    temp_user.setStatus_number(2);
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
                    temp_user.write2DB(DBM);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                error_message = rb.getString("error.user.account.lock");
            }
        }
        //unlock user
        if (request.getParameter("DialogAdminUsersUnlock") != null) {
            try {
                i = Integer.parseInt(request.getParameter("DialogAdminUsersUnlock"));
                temp_user = new XincoCoreUserServer(i, DBM);
                //Prompt the user for new password
                temp_user.setStatus_number(4);
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
                temp_user.write2DB(DBM);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        //reset user's password
        if (request.getParameter("DialogAdminUsersResetPW") != null) {
            try {
                i = Integer.parseInt(request.getParameter("DialogAdminUsersResetPW"));
                temp_user = new XincoCoreUserServer(i, DBM);
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
                temp_user.setStatus_number(3);
                temp_user.write2DB(DBM);
            } catch (Throwable e) {
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
                        new Timestamp(System.currentTimeMillis()), DBM);
                temp_group = new XincoCoreGroupServer(2, DBM);
                temp_user.getXinco_core_groups().addElement(temp_group);
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
                temp_user.write2DB(DBM);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        //create new group
        if (request.getParameter("DialogNewGroupSubmit") != null) {
            try {
                temp_group = new XincoCoreGroupServer(0, request.getParameter("DialogNewGroupName"), 1);
                temp_group.setChangerID(login_user.getId());
                temp_group.write2DB(DBM);
            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //modify group
        if (request.getParameter("DialogEditGroupSubmit") != null) {
            try {
                temp_group = new XincoCoreGroupServer(current_group_selection, DBM);
                temp_group.setDesignation(request.getParameter("DialogEditGroupName"));
                temp_group.setChangerID(login_user.getId());
                temp_group.write2DB(DBM);

            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //remove user from group
        if (request.getParameter("DialogEditGroupRemoveUser") != null) {
            //main admin always is admin and everyone is a regular user
            if (!(((current_group_selection == 1) && (Integer.parseInt(request.getParameter("DialogEditGroupRemoveUser")) == 1)) || (current_group_selection == 2))) {
                try {
                    DBM.executeUpdate("DELETE FROM xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + Integer.parseInt(request.getParameter("DialogEditGroupRemoveUser")) + " AND xinco_core_group_id=" + current_group_selection);
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
                DBM.executeUpdate("INSERT INTO xinco_core_user_has_xinco_core_group VALUES (" + Integer.parseInt(request.getParameter("DialogEditGroupAddUser")) + ", " + current_group_selection + ", " + "1)");
            } catch (Throwable e) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        //modify user profile
        if (request.getParameter("DialogEditUserProfileSubmit") != null) {
            try {
                temp_user = new XincoCoreUserServer(Integer.parseInt(request.getParameter("DialogEditUserProfileID")), DBM);
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
                temp_user.write2DB(DBM);

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
                temp_language.write2DB(DBM);

            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //delete language
        if (request.getParameter("DialogAdminLanguagesDelete") != null) {
            try {
                temp_language = new XincoCoreLanguageServer(Integer.parseInt(request.getParameter("DialogAdminLanguagesDelete")), DBM);
                XincoCoreLanguageServer.deleteFromDB(temp_language, DBM, login_user.getId());
            } catch (Throwable e) {
            }
        }
        //switch to attributes modification
        if (request.getParameter("DialogAdminDataTypeSelect") != null) {
            current_datatype_selection = Integer.parseInt(request.getParameter("DialogAdminDataTypeSelect"));
            session.setAttribute("XincoAdminServlet.current_datatype_selection", new Integer(current_datatype_selection));
            current_location = "AttributesAdminSingle";
            session.setAttribute("XincoAdminServlet.current_location", current_location);
            current_location_desc = rb.getString("message.location.desc.specificattributeadmin");
            session.setAttribute("XincoAdminServlet.current_location_desc", current_location_desc);
        }
        //create new attribute
        if (request.getParameter("DialogNewAttributeSubmit") != null) {
            try {
                temp_attribute = new XincoCoreDataTypeAttributeServer(current_datatype_selection, Integer.parseInt(request.getParameter("DialogNewAttributeAttributeId")), request.getParameter("DialogNewAttributeDesignation"), request.getParameter("DialogNewAttributeDataType"), Integer.parseInt(request.getParameter("DialogNewAttributeSize")));
                temp_attribute.write2DB(DBM);

            } catch (Throwable ex) {
                Logger.getLogger(XincoAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //delete attribute and attribute values
        if (request.getParameter("DialogEditAttributesRemoveAttributeId") != null) {
            try {
                temp_attribute = new XincoCoreDataTypeAttributeServer(current_datatype_selection, Integer.parseInt(request.getParameter("DialogEditAttributesRemoveAttributeId")), DBM);
                XincoCoreDataTypeAttributeServer.deleteFromDB(temp_attribute, DBM, login_user.getId());
            } catch (Throwable e) {
            }
        }
        //empty trash
        if (request.getParameter("MenuMainEmptyTrash") != null) {
            if (login_user != null) {
                try {
                    DBM = new XincoDBManager();
                    (new XincoCoreNodeServer(2, DBM)).deleteFromDB(false, DBM, login_user.getId());
                    DBM.finalize();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        //Reset DB (keeping standard inserts
        if (request.getParameter("MenuMainResetDB") != null) {
            try {
                (new XincoDBManager()).resetDB(this.login_user);
            } catch (Throwable e) {
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
                DBM = null;
                try {
                    DBM = new XincoDBManager();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    sql = "select id from xinco_core_user where username='" + request.getParameter("user").substring(0, request.getParameter("user").length() - 1) + "'";
                    rs = DBM.executeQuery(sql);
                    rs.next();
                    id = rs.getInt(1);
                    temp_user = new XincoCoreUserServer(id, DBM);
                } catch (XincoException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                boolean passwordIsUsable = temp_user.isPasswordUsable(request.getParameter("confirm"));
                if (!request.getParameter("new").equals(request.getParameter("confirm"))) {
                    //show welcome message
                    out.println("<br><center><img src=\"blueCubs.gif\" border=\"0\"/>");
                    out.println("<br><span class=\"bigtext\">XincoAdmin</span><br><br>");
                    out.println("<form name='changePassword' action='changePassword.jsp' method='post'>");
                    out.println(rb.getString("password.noMatch") + "<br><br>" + "<input type='submit' value='" + rb.getString("general.continue") + "' name='changePassword' />");
                    out.println("<input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type='hidden' name='user' value=" + request.getParameter("id") + "/>");
                    out.println("</form></center>");
                    return;
                }
                if (!passwordIsUsable) {
                    //show welcome message
                    out.println("<br><center><img src=\"blueCubs.gif\" border=\"0\"/>");
                    out.println("<br><span class=\"bigtext\">XincoAdmin</span><br><br>");
                    out.println("<form name='changePassword' action='changePassword.jsp' method='post'>");
                    out.println(rb.getString("password.unusable") + "<br><br>" + "<input type='submit' value='" + rb.getString("general.continue") + "' name='changePassword' />");
                    out.println("<input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type='hidden' name='user' value=" + request.getParameter("user") + "/>");
                    out.println("</form></center>");
                    return;
                } else {
                    try {
                        temp_user = new XincoCoreUserServer(id, DBM);
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
                        temp_user.write2DB(DBM);
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
        //Security bug
        if (!DBM.config.isAllowOutsideLinks()) {
            out.println(DBM.getWebBlockRightClickScript());
        }
        out.println("</head>");
        out.println("<body onload=\"if (document.forms[0] != null) { if (document.forms[0].elements[0] != null) { document.forms[0].elements[0].focus(); } }\">");

        //Avoid external links if general.setting.allowoutsidelinks is set to false
        //Security bug
        if (!DBM.config.isAllowOutsideLinks()) {
            out.println(DBM.getWebBlockRightClickScript());
        }

        out.println("<center>");
        out.println("<span class=\"text\">");

        out.println("");

        //if not logged in
        if (status == 0) {

            //show welcome message
            out.println("<br><img src=\"blueCubs.gif\" border=\"0\"/>");
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
            out.println("<br><img src=\"blueCubs.gif\" border=\"0\"/>");
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
            out.println("<td class=\"text\" rowspan=\"3\"><img src=\"blueCubsSmall.gif\" border=\"0\"/></td>");
            out.println("<td class=\"bigtext\" colspan=\"5\">XincoAdmin</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class=\"text\" colspan=\"5\">" + rb.getString("general.location") + ": " + current_location_desc + "</td>");
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
            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
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
                if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
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

                Vector allusers = XincoCoreUserServer.getXincoCoreUsers(DBM);
                for (i = 0; i < allusers.size(); i++) {
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getId() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getUsername() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getFirstname() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getName() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getEmail() + "</td>");
                    if (((XincoCoreUserServer) allusers.elementAt(i)).getStatus_number() <= 3) {
                        String temp = "<td class=\"text\"><a href=\"XincoAdmin?DialogAdminUsers";
                        if (((XincoCoreUserServer) allusers.elementAt(i)).getStatus_number() == 2) {
                            temp += "Unlock=" +
                                    ((XincoCoreUserServer) allusers.elementAt(i)).getId() +
                                    "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.unlock");
                        } else {
                            temp += "Lock=" + ((XincoCoreUserServer) allusers.elementAt(i)).getId() +
                                    "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.lock");
                        }
                        temp += "]</a>&nbsp;<a href=\"XincoAdmin?DialogAdminUsersResetPW=" +
                                ((XincoCoreUserServer) allusers.elementAt(i)).getId() +
                                "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" + rb.getString("general.password.reset") + "*]</a>" +
                                "&nbsp;<a href=\"XincoAdmin?DialogAdminUsersEdit=" +
                                ((XincoCoreUserServer) allusers.elementAt(i)).getId() +
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

                Vector allgroups = XincoCoreGroupServer.getXincoCoreGroups(DBM);
                for (i = 0; i < allgroups.size(); i++) {
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + ((XincoCoreGroupServer) allgroups.elementAt(i)).getId() + "</td>");
                    //Default groups
                    if (((XincoCoreGroupServer) allgroups.elementAt(i)).getId() <= 1000) {
                        out.println("<td class=\"text\">" + rb.getString(((XincoCoreGroupServer) allgroups.elementAt(i)).getDesignation()) + "</td>");
                    } else {
                        out.println("<td class=\"text\">" + ((XincoCoreGroupServer) allgroups.elementAt(i)).getDesignation() + "</td>");
                    }
                    out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogAdminGroupsSelect=" +
                            ((XincoCoreGroupServer) allgroups.elementAt(i)).getId() +
                            "&list=" + request.getParameter("list") + "\" class=\"link\"  icon=\"xinco\">[" +
                            rb.getString("general.edit") + "]</a></td>");
                    out.println("</tr>");
                }

                out.println("</table>");

            }

            if (current_location.compareTo("GroupAdminSingle") == 0) {

                //show group modification dialog
                try {
                    temp_group = new XincoCoreGroupServer(current_group_selection, DBM);
                    out.println("<form action=\"XincoAdmin\" method=\"post\">");
                    out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + rb.getString("general.name") + ":</td>");
                    out.println("<td class=\"text\"><input type=\"text\" name=\"DialogEditGroupName\" size=\"40\" value=\"" + temp_group.getDesignation() + "\"/></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\">&nbsp;</td>");
                    out.println("<td class=\"text\"><input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type=\"hidden\" name=\"DialogEditGroupID\" value=\"" + current_group_selection + "\"/><input type=\"submit\" name=\"DialogEditGroupSubmit\" value=\"" +
                            rb.getString("general.save") + "!\"/></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                } catch (Throwable e) {
                }

                //show user list
                Vector allusers = XincoCoreUserServer.getXincoCoreUsers(DBM);
                boolean member_of_group = false;

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
                    member_of_group = false;
                    for (j = 0; j < ((XincoCoreUserServer) allusers.elementAt(i)).getXinco_core_groups().size(); j++) {
                        if (((XincoCoreGroupServer) ((XincoCoreUserServer) allusers.elementAt(i)).getXinco_core_groups().elementAt(j)).getId() == current_group_selection) {
                            member_of_group = true;
                            break;
                        }
                    }
                    if (member_of_group) {
                        out.println("<tr>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getId() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getUsername() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getFirstname() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getName() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getEmail() + "</td>");
                        out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogEditGroupRemoveUser=" +
                                ((XincoCoreUserServer) allusers.elementAt(i)).getId() +
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
                    member_of_group = false;
                    for (j = 0; j < ((XincoCoreUserServer) allusers.elementAt(i)).getXinco_core_groups().size(); j++) {
                        if (((XincoCoreGroupServer) ((XincoCoreUserServer) allusers.elementAt(i)).getXinco_core_groups().elementAt(j)).getId() == current_group_selection) {
                            member_of_group = true;
                            break;
                        }
                    }
                    if (!member_of_group) {
                        out.println("<tr>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getId() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getUsername() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getFirstname() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getName() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreUserServer) allusers.elementAt(i)).getEmail() + "</td>");
                        out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogEditGroupAddUser=" +
                                ((XincoCoreUserServer) allusers.elementAt(i)).getId() +
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
                    temp_user = new XincoCoreUserServer(current_user_selection, DBM);
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
                            current_user_selection + "\"/><input type=\"submit\" name=\"DialogEditUserProfileSubmit\" value=\"" + rb.getString("general.save") + "!\"/></td>");
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
                    temp_user = new XincoCoreUserServer(Integer.valueOf(request.getParameter("DialogAdminUsersEdit")), DBM);
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
                    out.println("<td class=\"text\"><input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type=\"hidden\" name=\"DialogAdminUsersEdit\" value=\"" +//DialogEditUserProfileID
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

                Vector alllanguages = XincoCoreLanguageServer.getXincoCoreLanguages(DBM);
                boolean is_used = true;
                for (i = 0; i < alllanguages.size(); i++) {
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + ((XincoCoreLanguageServer) alllanguages.elementAt(i)).getId() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreLanguageServer) alllanguages.elementAt(i)).getSign() + "</td>");
                    out.println("<td class=\"text\">" + ((XincoCoreLanguageServer) alllanguages.elementAt(i)).getDesignation() + "</td>");
                    is_used = XincoCoreLanguageServer.isLanguageUsed((XincoCoreLanguageServer) alllanguages.elementAt(i), DBM);
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

                Vector alldatatypes = XincoCoreDataTypeServer.getXincoCoreDataTypes(DBM);
                for (i = 0; i < alldatatypes.size(); i++) {
                    out.println("<tr>");
                    out.println("<td class=\"text\">" + ((XincoCoreDataTypeServer) alldatatypes.elementAt(i)).getId() + "</td>");
                    out.println("<td class=\"text\">" + DBM.localizeString(((XincoCoreDataTypeServer) alldatatypes.elementAt(i)).getDesignation()) + "</td>");
                    out.println("<td class=\"text\">" + DBM.localizeString(((XincoCoreDataTypeServer) alldatatypes.elementAt(i)).getDescription()) + "</td>");
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
                            current_datatype_selection + "\"/><input type=\"submit\" name=\"DialogNewAttributeSubmit\" value=\"" +
                            rb.getString("general.add.attribute") + "\"/></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                    out.flush();
                } catch (Throwable e) {
                }

                //show attributes list
                try {
                    XincoCoreDataTypeServer temp_datatype = new XincoCoreDataTypeServer(current_datatype_selection, DBM);

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
                    for (i = 0; i < temp_datatype.getXinco_core_data_type_attributes().size(); i++) {
                        out.println("<tr>");
                        out.println("<td class=\"text\">" + ((XincoCoreDataTypeAttributeServer) temp_datatype.getXinco_core_data_type_attributes().elementAt(i)).getAttribute_id() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreDataTypeAttributeServer) temp_datatype.getXinco_core_data_type_attributes().elementAt(i)).getDesignation() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreDataTypeAttributeServer) temp_datatype.getXinco_core_data_type_attributes().elementAt(i)).getData_type() + "</td>");
                        out.println("<td class=\"text\">" + ((XincoCoreDataTypeAttributeServer) temp_datatype.getXinco_core_data_type_attributes().elementAt(i)).getSize() + "</td>");
                        if (((current_datatype_selection == 1) && (((XincoCoreDataTypeAttributeServer) temp_datatype.getXinco_core_data_type_attributes().elementAt(i)).getAttribute_id() <= 8)) || ((current_datatype_selection == 2) && (((XincoCoreDataTypeAttributeServer) temp_datatype.getXinco_core_data_type_attributes().elementAt(i)).getAttribute_id() <= 1)) || ((current_datatype_selection == 3) && (((XincoCoreDataTypeAttributeServer) temp_datatype.getXinco_core_data_type_attributes().elementAt(i)).getAttribute_id() <= 1))) {
                            out.println("<td class=\"text\">&nbsp;</td>");
                        } else {
                            out.println("<td class=\"text\"><a href=\"XincoAdmin?DialogEditAttributesRemoveAttributeId=" +
                                    ((XincoCoreDataTypeAttributeServer) temp_datatype.getXinco_core_data_type_attributes().elementAt(i)).getAttribute_id() +
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
                    DBM = new XincoDBManager();
                    DBM.setLocale(loc);
                    String column = "id";
                    if (request.getParameter("table").equals("xinco_add_attribute")) {
                        column = "xinco_core_data_id";
                    }
                    if (request.getParameter("table").equals("xinco_core_data_type_attribute")) {
                        column = "xinco_core_data_type_id";
                    }
                    rs = DBM.executeQuery("select * from " + request.getParameter("table") +
                            "_t a, (select a.firstname || ' ' || a.name as \"" +
                            rb.getString("general.user") + "\" , b.mod_time as \"" + rb.getString("general.audit.modtime") +
                            "\" ,b.mod_reason as \"" + rb.getString("general.reason") + "\" ,b.record_id " +
                            "from xinco_core_user a,xinco_core_user_modified_record b where a.id=b.id " +
                            ") b where b.record_id =a.record_id and a." + column +
                            " = '" + request.getParameter("id") + "' order by a.record_id desc");
                    DBM.drawTable(rs, response.getWriter(), DBM.getColumnNames(rs),
                            "<center>" + rb.getString("general.audit.results").replaceAll("%i",
                            request.getParameter("id")).replaceAll("%t",
                            request.getParameter("table")) + "<br>", -1, false, -1);
                    out.write("\n");
                    out.write("        </center>\n");
                    out.write("    </body>\n");
                    out.write("</html>\n");
                } catch (Throwable e) {
                    global_error_message = global_error_message + e.toString();
                    e.printStackTrace();
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
                    DBM = new XincoDBManager();
                    String column = "id";
                    if (request.getParameter("table").equals("xinco_add_attribute")) {
                        column = "xinco_core_data_id";
                    }
                    if (request.getParameter("table").equals("xinco_core_data_type_attribute")) {
                        column = "xinco_core_data_type_id";
                    }
                    rs = DBM.executeQuery("select distinct * from " + request.getParameter("table") +
                            " where " + column + " in (select distinct " + column + " from " + request.getParameter("table") + "_t)");
                    DBM.drawTable(rs, response.getWriter(), DBM.getColumnNames(rs), "", -1, false, -1);
                    rs = DBM.executeQuery("select distinct " + column + " from " + request.getParameter("table") + "_t");
                    out.println("<form action='XincoAdmin?MenuAudit=AuditTable' method='POST'>");
                    rs = DBM.executeQuery("select distinct " + column + " from " +
                            request.getParameter("table") + "_t");
                    out.println("Select record id: ");
                    out.println("<select name='id'>");
                    while (rs.next()) {
                        out.println("<option >" + rs.getString(1) + "</option>");
                    }
                    out.println("</select>");
                    out.println("<br><br><input type='submit' value='" + rb.getString("menu.view") + "' />" +
                            "<input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type='hidden' name='table' value=" + request.getParameter("table") + " /></form>");

                    out.write("\n");
                    out.write("        </center>\n");
                    out.write("    </body>\n");
                    out.write("</html>\n");
                } catch (Throwable e) {
                    global_error_message = global_error_message + e.toString();
                    e.printStackTrace();
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
                    DBM = new XincoDBManager();
                    DatabaseMetaData meta = DBM.getConnection().getMetaData();
                    String[] types = {"TABLE"};
                    rs = meta.getTables(null, null, null, types);
                    out.println("<center><table border='0'><tbody><thead><tr><th>" +
                            rb.getString("general.table") + "</th><th>" +
                            rb.getString("general.audit.action") + "</th></tr>");
                    while (rs.next()) {
                        if (!rs.getString("TABLE_NAME").endsWith("_t") &&
                                !rs.getString("TABLE_NAME").equals("xinco_id") &&
                                !rs.getString("TABLE_NAME").equals("xinco_core_log") &&
                                !rs.getString("TABLE_NAME").equals("xinco_core_user_modified_record") &&
                                rs.getString("TABLE_NAME").startsWith("xinco") &&
                                !rs.getString("TABLE_NAME").equals("xinco_core_user_has_xinco_core_group")) {
                            out.println("<form action='XincoAdmin?MenuAudit=AuditQuery' method='POST'>");
                            out.println("<tr><td>" + rs.getString("TABLE_NAME") + "</td><td><center><input type='submit' value='" +
                                    rb.getString("general.continue") + "'/></center></td></tr>" +
                                    "<input type='hidden' name='list' value='" + request.getParameter("list") + "'/><input type='hidden' name='table' value='" + rs.getString("TABLE_NAME") + "' /></form>");
                        }
                    }
                    out.println("</tbody></table></center>");

                    out.write("\n");
                    out.write("    </body>\n");
                    out.write("</html>\n");
                } catch (Throwable e) {
                    global_error_message = global_error_message + e.toString();
                    e.printStackTrace();
                }
            }
            if (current_location.compareTo("RebuildIndex") == 0) {
                //rebuild index and list status
                DBM = new XincoDBManager();
                XincoSettingServer s2 = null;
                try {
                    s2 = new XincoSettingServer(DBM.getSetting("setting.index.lock").getId(), DBM);
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
                    s2.setBool_value(true);
                    s2.setChangerID(login_user.getId());
                    s2.write2DB(DBM);
                    //delete existing index
                    File indexDirectory = null;
                    File indexDirectoryFile = null;
                    String[] indexDirectoryFileList = null;
                    boolean index_directory_deleted = false;
                    indexDirectory = new File(DBM.config.getFileIndexPath());
                    if (indexDirectory.exists()) {
                        index_directory_deleted = XincoIndexThread.deleteIndex(DBM);
                        out.println("<tr>");
                        out.println("<td class=\"text\"><b>" + rb.getString("message.index.delete") + "</b></td>");
                        if (index_directory_deleted) {
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
                    XincoCoreDataServer xdata_temp = null;
                    boolean index_result = false;
                    ResultSet rs = DBM.executeQuery("SELECT id FROM xinco_core_data ORDER BY designation");
                    while (rs.next()) {
                        xdata_temp = new XincoCoreDataServer(rs.getInt("id"), DBM);
                        index_result = XincoIndexer.indexXincoCoreData(xdata_temp, true, DBM);
                        out.println("<tr>");
                        out.println("<td class=\"text\">" + xdata_temp.getDesignation() + "</td>");
                        if (index_result) {
                            out.println("<td class=\"text\">" + rb.getString("general.ok") + "!</td>");
                        } else {
                            out.println("<td class=\"text\">" + rb.getString("general.fail") + "</td>");
                        }
                        out.println("</tr>");
                        out.flush();
                    }

                    //optimize index
                    index_result = XincoIndexer.optimizeIndex(DBM);
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
                    s2.setBool_value(false);
                    s2.setChangerID(login_user.getId());
                    s2.write2DB(DBM);
                } catch (Throwable e) {
                    out.println("</table>");
                    if (s2 != null) {
                        s2.setBool_value(false);
                        s2.setChangerID(login_user.getId());
                        try {
                            s2.write2DB(DBM);
                        } catch (XincoException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        //show footer
        out.println("<br><br><br>");
        out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
        out.println("<tr>");
        out.println("<td class=\"text\">&nbsp;</td>");
        out.println("<td class=\"text\">&copy; " + DBM.getSetting("general.copyright.date").getString_value() + ", " +
                //Avoid external links if general.setting.allowoutsidelinks is set to false
                //Security bug
                (DBM.config.isAllowOutsideLinks() ? rb.getString("message.admin.main.footer") : "blueCubs.com and xinco.org"));
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
        //close db connection
        try {
            DBM.finalize();
        } catch (Throwable e) {
            global_error_message = global_error_message + e.toString();
        }

    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
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
     */
    @Override
    public String getServletInfo() {
        return rb.getString("message.servlet.info");
    }
}
