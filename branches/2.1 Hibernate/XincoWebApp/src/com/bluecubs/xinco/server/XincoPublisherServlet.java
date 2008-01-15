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
 * Name:            XincoPublisherServlet
 *
 * Description:     publisher servlet
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.server;

import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreACE;
import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.add.XincoAddAttribute;
import java.io.*;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.server.*;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.axis.encoding.Base64;

/**
 * 
 * @author Alexander Manes
 */
public class XincoPublisherServlet extends HttpServlet {

    private ResourceBundle rb;
    private XincoDBManager DBM;

    /** Initializes the servlet.
     * @param config
     * @throws javax.servlet.ServletException 
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
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException 
     */
    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        int i = 0;
        int j = 0;
        String request_path;
        String[] request_path_array;
        boolean fileDownload = false;
        int core_data_id = 0;
        XincoCoreDataServer xcd = null;
        XincoCoreDataTypeAttribute xcdta = null;
        XincoAddAttribute xaa = null;
        boolean printList = false;
        boolean browseFolder = false;
        String temp_url = "";
        String temp_server_url = "";
        boolean isPublic = false;

        //connect to db
        try {
            DBM = new XincoDBManager();
        } catch (Throwable e) {
            //start output
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(e.toString());
            return;
        }

        //get requested data
        if (request.getParameter("MainMenu") == null) {
            request_path = request.getPathInfo();
            if (request_path != null) {
                request_path_array = request_path.split("/");
                if (!(request_path_array.length > 1)) {
                    core_data_id = 0;
                } else {
                    try {
                        core_data_id = Integer.parseInt(request_path_array[1]);
                        xcd = new XincoCoreDataServer(core_data_id, DBM);
                        isPublic = false;
                        //check status (5 = published)
                        if (xcd.getStatus_number() == 5) {
                            isPublic = true;
                        } else {
                            //check read permission for group "public"
                            for (i = 0; i < xcd.getXinco_core_acl().size(); i++) {
                                if ((((XincoCoreACE) xcd.getXinco_core_acl().elementAt(i)).getXinco_core_group_id() == 3) && ((XincoCoreACE) xcd.getXinco_core_acl().elementAt(i)).isRead_permission()) {
                                    isPublic = true;
                                    break;
                                }
                            }
                        }
                        if (!isPublic) {
                            core_data_id = -1;
                        }
                    } catch (Throwable e) {
                        core_data_id = -1;
                    }
                }
            } else {
                core_data_id = 0;
            }
        } else {
            core_data_id = 0;
            if (request.getParameter("MainMenu").compareTo("list") == 0) {
                printList = true;
            } else if (request.getParameter("MainMenu").compareTo("browse") == 0) {
                browseFolder = true;
            }
        }
        //check data type
        if (core_data_id > 0) {
            if (xcd.getXinco_core_data_type().getId() == 1) {
                fileDownload = true;
            } else {
                fileDownload = false;
            }
        }

        //generate specific output
        if (fileDownload) {
            // begin FILE output
            try {
                response.setContentType("unknown/unknown");
                OutputStream out = response.getOutputStream();

                FileInputStream in = new FileInputStream(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), core_data_id, "" + core_data_id));
                byte[] buf = new byte[4096];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
            } catch (Throwable e) {
                System.out.println(e);
            }

        //end FILE output
        } else {
            // begin HTML output
            //start output
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            //show header
            out.println("<html>");
            out.println("<head>");
            if (core_data_id == 0) {
                out.println("<title>XincoPublisher</title>");
                out.println("<link rel=\"stylesheet\" href=\"xincostyle.css\" type=\"text/css\"/>");
            }
            if (core_data_id > 0) {
                out.println("<title>" + xcd.getDesignation() + "</title>");
                out.println("<link rel=\"stylesheet\" href=\"../../xincostyle.css\" type=\"text/css\"/>");
            }
            if (!DBM.config.isAllowOutsideLinks()) {
                out.println(DBM.getWebBlockRightClickScript());
            }
            out.println("</head>");
            out.println("<body>");

            //Avoid external links if setting.allowoutsidelinks is set to false
            //Security bug
            if (!DBM.config.isAllowOutsideLinks()) {
                out.println(DBM.getWebBlockRightClickScript());
            }
            out.println("<center>");
            out.println("");

            //show main menu
            if (core_data_id == 0) {
                out.println("<br>");
                out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                out.println("<td class=\"text\" width=\"100\"><img src=\"blueCubsSmall.gif\" border=\"0\"/></td>");
                out.println("<td class=\"bigtext\" width=\"650\">" + rb.getString("message.admin.main.publisher.label") + "</td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("<br>");
                out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                if (printList) {
                    try {
                        XincoCoreDataServer xdata_temp = null;
                        ResultSet rs = DBM.executeQuery("SELECT DISTINCT xcd.id, xcd.designation FROM xinco_core_data xcd, " + 
                                "xinco_core_ace xca WHERE xcd.id=xca.xinco_core_data_id AND (xcd.status_number=5 OR " + 
                                "(xca.xinco_core_group_id=3 AND xca.read_permission=1)) ORDER BY xcd.designation");
                        while (rs.next()) {
                            xdata_temp = new XincoCoreDataServer(rs.getInt("id"), DBM);
                            temp_server_url = request.getRequestURL().toString();
                            temp_url = "";
                            //file = 1
                            if (xdata_temp.getXinco_core_data_type().getId() == 1) {
                                temp_url = ((XincoAddAttribute) xdata_temp.getXinco_add_attributes().elementAt(0)).getAttrib_varchar();
                            } else {
                                temp_url = xdata_temp.getDesignation();
                            }
                            out.println("<tr>");
                            out.println("<td class=\"text\">" + xdata_temp.getDesignation() + " (" + rb.getString(xdata_temp.getXinco_core_data_type().getDesignation()) + " | " + xdata_temp.getXinco_core_language().getSign() + ")" + "</td>");
                            out.println("<td class=\"text\"><a href=\"" + "XincoPublisher/" + xdata_temp.getId() + "/" + temp_url + "?list=" + request.getParameter("list") + " target='_blank'\">" + temp_server_url + "/" + xdata_temp.getId() + "/" + temp_url + "</a></td>");
                            out.println("</tr>");
                            out.flush();
                        }
                    } catch (Throwable sqle) {
                        try {
                            if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                                sqle.printStackTrace();
                            }
                        } catch (XincoSettingException ex) {
                            Logger.getLogger(XincoPublisherServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else if (browseFolder) {
                    try {
                        XincoCoreNodeServer xnode_temp = null;
                        XincoCoreNodeServer xnode_temp2 = null;
                        XincoCoreDataServer xdata_temp = null;
                        String temp_path = null;
                        String temp_path2 = null;
                        int temp_xcn_id = 0;

                        if (!(request.getParameter("FolderId") == null)) {
                            temp_xcn_id = Integer.parseInt(request.getParameter("FolderId"));
                            xnode_temp = new XincoCoreNodeServer(temp_xcn_id, DBM);
                            //check read permission for group "public"
                            isPublic = false;
                            for (i = 0; i < xnode_temp.getXinco_core_acl().size(); i++) {
                                if ((((XincoCoreACE) xnode_temp.getXinco_core_acl().elementAt(i)).getXinco_core_group_id() == 3) && ((XincoCoreACE) xnode_temp.getXinco_core_acl().elementAt(i)).isRead_permission()) {
                                    isPublic = true;
                                    break;
                                }
                            }
                            if (isPublic) {
                                xnode_temp.fillXincoCoreNodes(DBM);
                                xnode_temp.fillXincoCoreData(DBM);
                                // print current path
                                if (!(request.getParameter("Path") == null)) {
                                    temp_path = request.getParameter("Path");
                                    temp_path = new String(Base64.decode(temp_path));
                                    out.println("<tr>");
                                    out.println("<td colspan=\"2\" class=\"text\"><b>" + rb.getString("general.path") + "</b> " + temp_path + "</td>");
                                    out.println("</tr>");
                                    out.println("<tr>");
                                    out.println("<td colspan=\"2\" class=\"text\">&nbsp;</td>");
                                    out.println("</tr>");
                                    out.flush();
                                } else {
                                    temp_path = null;
                                }
                                // list public folders
                                out.println("<tr>");
                                out.println("<td colspan=\"2\" class=\"text\"><b>" + rb.getString("message.xincopublisher.subfolders") + "</b></td>");
                                out.println("</tr>");
                                out.flush();
                                for (i = 0; i < xnode_temp.getXinco_core_nodes().size(); i++) {
                                    xnode_temp2 = new XincoCoreNodeServer(((XincoCoreNodeServer) xnode_temp.getXinco_core_nodes().elementAt(i)).getId(), DBM);
                                    isPublic = false;
                                    //check read permission for group "public"
                                    for (j = 0; j < xnode_temp2.getXinco_core_acl().size(); j++) {
                                        if ((((XincoCoreACE) xnode_temp2.getXinco_core_acl().elementAt(j)).getXinco_core_group_id() == 3) && ((XincoCoreACE) xnode_temp2.getXinco_core_acl().elementAt(j)).isRead_permission()) {
                                            isPublic = true;
                                            break;
                                        }
                                    }
                                    if (isPublic) {
                                        if (temp_path != null) {
                                            temp_path2 = temp_path + " / " + xnode_temp2.getDesignation() + " (" + xnode_temp2.getXinco_core_language().getSign() + ")";
                                            temp_path2 = Base64.encode(temp_path2.getBytes());
                                            temp_path2 = "&Path=" + temp_path2;
                                        } else {
                                            temp_path2 = "";
                                        }
                                        out.println("<tr>");
                                        out.println("<td class=\"text\">&nbsp;</td>");
                                        out.println("<td class=\"text\"><a href=\"" + "XincoPublisher?MainMenu=browse&FolderId=" + xnode_temp2.getId() + temp_path2 + "&list=" + request.getParameter("list") + "\">[" + xnode_temp2.getDesignation() + " (" + xnode_temp2.getXinco_core_language().getSign() + ")" + "]</a></td>");
                                        out.println("</tr>");
                                        out.flush();
                                    }
                                }
                                out.println("<tr>");
                                out.println("<td colspan=\"2\" class=\"text\">&nbsp;</td>");
                                out.println("</tr>");
                                out.flush();
                                // list public data
                                out.println("<tr>");
                                out.println("<td colspan=\"2\" class=\"text\"><b>" + rb.getString("message.xincopublisher.publicdata") + "</b></td>");
                                out.println("</tr>");
                                out.flush();
                                for (i = 0; i < xnode_temp.getXinco_core_data().size(); i++) {
                                    xdata_temp = new XincoCoreDataServer(((XincoCoreDataServer) xnode_temp.getXinco_core_data().elementAt(i)).getId(), DBM);
                                    isPublic = false;
                                    //check status (5 = published)
                                    if (xdata_temp.getStatus_number() == 5) {
                                        isPublic = true;
                                    } else {
                                        //check read permission for group "public"
                                        for (j = 0; j < xdata_temp.getXinco_core_acl().size(); j++) {
                                            if ((((XincoCoreACE) xdata_temp.getXinco_core_acl().elementAt(j)).getXinco_core_group_id() == 3) && ((XincoCoreACE) xdata_temp.getXinco_core_acl().elementAt(j)).isRead_permission()) {
                                                isPublic = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (isPublic) {
                                        temp_server_url = request.getRequestURL().toString();
                                        temp_url = "";
                                        //file = 1
                                        if (xdata_temp.getXinco_core_data_type().getId() == 1) {
                                            temp_url = ((XincoAddAttribute) xdata_temp.getXinco_add_attributes().elementAt(0)).getAttrib_varchar();
                                        } else {
                                            temp_url = xdata_temp.getDesignation();
                                        }
                                        out.println("<tr>");
                                        out.println("<td class=\"text\">" + xdata_temp.getDesignation() + " (" + xdata_temp.getXinco_core_data_type().getDesignation() + " | " + xdata_temp.getXinco_core_language().getSign() + ")" + "</td>");
                                        out.println("<td class=\"text\"><a href=\"" + "XincoPublisher/" + xdata_temp.getId() + "/" + temp_url + "?list=" + request.getParameter("list") + "\">" + temp_server_url + "/" + xdata_temp.getId() + "/" + temp_url + "</a></td>");
                                        out.println("</tr>");
                                        out.flush();
                                    }
                                }
                            }
                        }
                    } catch (Exception sqle) {
                    }
                } else {
                    out.println("<tr>");
                    out.println("<td class=\"text\" colspan=\"2\"><a href=\"XincoPublisher?MainMenu=list&list=" + request.getParameter("list") + "\" class=\"link\">" + rb.getString("message.xincopublisher.list") + "</td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td class=\"text\" colspan=\"2\"><a href=\"XincoPublisher?MainMenu=browse&FolderId=1&Path=" + (Base64.encode((new String("xincoRoot")).getBytes())) + "&list=" + request.getParameter("list") + "\" class=\"link\">" + rb.getString("message.xincopublisher.browse") + "</td>");
                    out.println("</tr>");
                }
                out.println("<tr>");
                out.println("<td class=\"text\" colspan=\"2\">&nbsp;<br><br></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\" colspan=\"2\">" + rb.getString("message.xincopublisher.howto") + "</td>");
                out.println("</tr>");
                out.println("</table>");
            }
            if (core_data_id > 0) {

                out.println("<br>");
                out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                //out.println("<td class=\"text\">Designation:</td>");
                out.println("<td class=\"bigtext\" colspan=\"2\">" + xcd.getDesignation() + "</td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td class=\"text\" colspan=\"2\">&nbsp;</td>");
                out.println("</tr>");

                //print additional attributes
                for (i = 0; i < xcd.getXinco_add_attributes().size(); i++) {
                    xcdta = ((XincoCoreDataTypeAttribute) xcd.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i));
                    xaa = ((XincoAddAttribute) xcd.getXinco_add_attributes().elementAt(i));
                    out.println("<tr>");
                    out.println("<td class=\"text\" valign=\"top\"><b>" + xcdta.getDesignation() + ":</b></td>");
                    if (xcdta.getData_type().toLowerCase().compareTo("int") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttrib_int() + "</pre></td>");
                    }
                    if (xcdta.getData_type().toLowerCase().compareTo("unsignedint") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttrib_unsignedint() + "</pre></td>");
                    }
                    if (xcdta.getData_type().toLowerCase().compareTo("double") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttrib_double() + "</pre></td>");
                    }
                    if (xcdta.getData_type().toLowerCase().compareTo("varchar") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttrib_varchar() + "</pre></td>");
                    }
                    if (xcdta.getData_type().toLowerCase().compareTo("text") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttrib_text() + "</pre></td>");
                    }
                    if (xcdta.getData_type().toLowerCase().compareTo("datetime") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttrib_datetime() + "</pre></td>");
                    }
                    out.println("</tr>");
                }

                out.println("</table>");
            }

            //show footer
            if (core_data_id == 0) {
                out.println("<br><br><br>");
                out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                out.println("<td class=\"text\">&nbsp;</td>");
                out.println("<td class=\"text\">&copy; " + DBM.getSetting("general.copyright.date").getString_value() + ", " + (DBM.config.isAllowOutsideLinks() ? rb.getString("message.admin.main.footer") : "blueCubs.com and xinco.org"));
                out.println("</tr>");
                out.println("</table><tr><form action='menu.jsp'><input type='submit' value='" + rb.getString("message.admin.main.backtomain") + "' />" + "<input type='hidden' name='list' value='" + request.getParameter("list") + "'/></form></tr>" + "<tr><FORM><INPUT TYPE='button' VALUE='" + rb.getString("message.admin.main.back") + "' onClick='history.go(-1);return true;'><input type='hidden' name='list' value='" + request.getParameter("list") + "'/></FORM></tr>");
            }

            out.println("</span>");
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");

            out.close();
        } //end HTML output
        //close db connection
        try {
            DBM.finalize();
        } catch (Throwable e) {
        }
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     * @return String
     */
    @Override
    public String getServletInfo() {
        return "Servlet of xinco";
    }
}
