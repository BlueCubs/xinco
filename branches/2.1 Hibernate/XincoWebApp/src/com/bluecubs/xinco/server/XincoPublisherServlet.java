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
import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import java.io.*;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.persistence.XincoPersistenceManager;
import com.bluecubs.xinco.core.server.persistence.XincoSettingServer;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.axis.encoding.Base64;

/**
 * 
 * @author Alexander Manes
 */
public class XincoPublisherServlet extends HttpServlet {

    private XincoPersistenceManager pm;
    private ResourceBundle rb;
    private static XincoConfigSingletonServer config = XincoConfigSingletonServer.getInstance();

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
    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, XincoSettingException {
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
        String requestPath;
        String[] requestPathArray;
        boolean fileDownload = false;
        int coreDataId = 0;
        XincoCoreDataServer xcd = null;
        XincoCoreDataTypeAttribute xcdta = null;
        XincoAddAttribute xaa = null;
        boolean printList = false;
        boolean browseFolder = false;
        String tempUrl = "";
        String temp_serverUrl = "";
        boolean isPublic = false;

        //connect to db
        try {
            pm = new XincoPersistenceManager();
        } catch (Throwable e) {
            //start output
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(e.toString());
            return;
        }

        //get requested data
        if (request.getParameter("MainMenu") == null) {
            requestPath = request.getPathInfo();
            if (requestPath != null) {
                requestPathArray = requestPath.split("/");
                if (!(requestPathArray.length > 1)) {
                    coreDataId = 0;
                } else {
                    try {
                        coreDataId = Integer.parseInt(requestPathArray[1]);
                        xcd = new XincoCoreDataServer(coreDataId);
                        isPublic = false;
                        //check status (5 = published)
                        if (xcd.getStatusNumber() == 5) {
                            isPublic = true;
                        } else {
                            //check read permission for group "public"
                            for (i = 0; i < xcd.getXincoCoreACL().size(); i++) {
                                if ((((XincoCoreACE) xcd.getXincoCoreACL().elementAt(i)).getXincoCoreGroupId() == 3) && ((XincoCoreACE) xcd.getXincoCoreACL().elementAt(i)).getReadPermission()) {
                                    isPublic = true;
                                    break;
                                }
                            }
                        }
                        if (!isPublic) {
                            coreDataId = -1;
                        }
                    } catch (Throwable e) {
                        coreDataId = -1;
                    }
                }
            } else {
                coreDataId = 0;
            }
        } else {
            coreDataId = 0;
            if (request.getParameter("MainMenu").compareTo("list") == 0) {
                printList = true;
            } else if (request.getParameter("MainMenu").compareTo("browse") == 0) {
                browseFolder = true;
            }
        }
        //check data type
        if (coreDataId > 0) {
            if (xcd.getXincoCoreDataType().getId() == 1) {
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

                FileInputStream in = new FileInputStream(XincoCoreDataServer.getXincoCoreDataPath(config.getFileRepositoryPath(), coreDataId, "" + coreDataId));
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
            if (coreDataId == 0) {
                out.println("<title>XincoPublisher</title>");
                out.println("<link rel=\"stylesheet\" href=\"xincostyle.css\" type=\"text/css\"/>");
            }
            if (coreDataId > 0) {
                out.println("<title>" + xcd.getDesignation() + "</title>");
                out.println("<link rel=\"stylesheet\" href=\"../../xincostyle.css\" type=\"text/css\"/>");
            }
            if (!config.isAllowOutsideLinks()) {
                out.println(pm.getWebBlockRightClickScript());
            }
            out.println("</head>");
            out.println("<body>");

            //Avoid external links if setting.allowoutsidelinks is set to false
            //Security bug
            if (!config.isAllowOutsideLinks()) {
                out.println(pm.getWebBlockRightClickScript());
            }
            out.println("<center>");
            out.println("");

            //show main menu
            if (coreDataId == 0) {
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
                        XincoCoreDataServer xdataTemp = null;
//                        ResultSet rs = pm.executeQuery("SELECT DISTINCT xcd.id, xcd.designation FROM xincoCoreData xcd, " +
//                                "xincoCoreAce xca WHERE xcd.id=xca.xincoCoreDataId AND (xcd.statusNumber=5 OR " +
//                                "(xca.xincoCoreGroupId=3 AND xca.readPermission=1)) ORDER BY xcd.designation");
//                        while (rs.next()) {
//                            xdataTemp = new XincoCoreDataServer(rs.getInt("id"));
//                            temp_serverUrl = request.getRequestURL().toString();
//                            tempUrl = "";
//                            //file = 1
//                            if (xdataTemp.getXincoCoreDataType().getId() == 1) {
//                                tempUrl = ((XincoAddAttribute) xdataTemp.getXincoAddAttributes().elementAt(0)).getAttribVarchar();
//                            } else {
//                                tempUrl = xdataTemp.getDesignation();
//                            }
//                            out.println("<tr>");
//                            out.println("<td class=\"text\">" + xdataTemp.getDesignation() + " (" + rb.getString(xdataTemp.getXincoCoreDataType().getDesignation()) + " | " + xdataTemp.getXincoCoreLanguage().getSign() + ")" + "</td>");
//                            out.println("<td class=\"text\"><a href=\"" + "XincoPublisher/" + xdataTemp.getId() + "/" + tempUrl + "?list=" + request.getParameter("list") + " target='_blank'\">" + temp_serverUrl + "/" + xdataTemp.getId() + "/" + tempUrl + "</a></td>");
//                            out.println("</tr>");
//                            out.flush();
//                        }
                    } catch (Throwable ex) {
                        Logger.getLogger(XincoPublisherServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (browseFolder) {
                    try {
                        XincoCoreNodeServer xnodeTemp = null;
                        XincoCoreNodeServer xnodeTemp2 = null;
                        XincoCoreDataServer xdataTemp = null;
                        String tempPath = null;
                        String tempPath2 = null;
                        int temp_xcnId = 0;

                        if (!(request.getParameter("FolderId") == null)) {
                            temp_xcnId = Integer.parseInt(request.getParameter("FolderId"));
                            xnodeTemp = new XincoCoreNodeServer(temp_xcnId);
                            //check read permission for group "public"
                            isPublic = false;
                            for (i = 0; i < xnodeTemp.getXincoCoreACL().size(); i++) {
                                if ((((XincoCoreACE) xnodeTemp.getXincoCoreACL().elementAt(i)).getXincoCoreGroupId() == 3) && ((XincoCoreACE) xnodeTemp.getXincoCoreACL().elementAt(i)).getReadPermission()) {
                                    isPublic = true;
                                    break;
                                }
                            }
                            if (isPublic) {
                                xnodeTemp.fillXincoCoreNodes();
                                xnodeTemp.fillXincoCoreData();
                                // print current path
                                if (!(request.getParameter("Path") == null)) {
                                    tempPath = request.getParameter("Path");
                                    tempPath = new String(Base64.decode(tempPath));
                                    out.println("<tr>");
                                    out.println("<td colspan=\"2\" class=\"text\"><b>" + rb.getString("general.path") + "</b> " + tempPath + "</td>");
                                    out.println("</tr>");
                                    out.println("<tr>");
                                    out.println("<td colspan=\"2\" class=\"text\">&nbsp;</td>");
                                    out.println("</tr>");
                                    out.flush();
                                } else {
                                    tempPath = null;
                                }
                                // list public folders
                                out.println("<tr>");
                                out.println("<td colspan=\"2\" class=\"text\"><b>" + rb.getString("message.xincopublisher.subfolders") + "</b></td>");
                                out.println("</tr>");
                                out.flush();
                                for (i = 0; i < xnodeTemp.getXincoCoreNodes().size(); i++) {
                                    xnodeTemp2 = new XincoCoreNodeServer(((XincoCoreNodeServer) xnodeTemp.getXincoCoreNodes().elementAt(i)).getId());
                                    isPublic = false;
                                    //check read permission for group "public"
                                    for (j = 0; j < xnodeTemp2.getXincoCoreACL().size(); j++) {
                                        if ((((XincoCoreACE) xnodeTemp2.getXincoCoreACL().elementAt(j)).getXincoCoreGroupId() == 3) && ((XincoCoreACE) xnodeTemp2.getXincoCoreACL().elementAt(j)).getReadPermission()) {
                                            isPublic = true;
                                            break;
                                        }
                                    }
                                    if (isPublic) {
                                        if (tempPath != null) {
                                            tempPath2 = tempPath + " / " + xnodeTemp2.getDesignation() + " (" + xnodeTemp2.getXincoCoreLanguage().getSign() + ")";
                                            tempPath2 = Base64.encode(tempPath2.getBytes());
                                            tempPath2 = "&Path=" + tempPath2;
                                        } else {
                                            tempPath2 = "";
                                        }
                                        out.println("<tr>");
                                        out.println("<td class=\"text\">&nbsp;</td>");
                                        out.println("<td class=\"text\"><a href=\"" + "XincoPublisher?MainMenu=browse&FolderId=" + xnodeTemp2.getId() + tempPath2 + "&list=" + request.getParameter("list") + "\">[" + xnodeTemp2.getDesignation() + " (" + xnodeTemp2.getXincoCoreLanguage().getSign() + ")" + "]</a></td>");
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
                                for (i = 0; i < xnodeTemp.getXincoCoreData().size(); i++) {
                                    xdataTemp = new XincoCoreDataServer(((XincoCoreDataServer) xnodeTemp.getXincoCoreData().elementAt(i)).getId());
                                    isPublic = false;
                                    //check status (5 = published)
                                    if (xdataTemp.getStatusNumber() == 5) {
                                        isPublic = true;
                                    } else {
                                        //check read permission for group "public"
                                        for (j = 0; j < xdataTemp.getXincoCoreACL().size(); j++) {
                                            if ((((XincoCoreACE) xdataTemp.getXincoCoreACL().elementAt(j)).getXincoCoreGroupId() == 3) && ((XincoCoreACE) xdataTemp.getXincoCoreACL().elementAt(j)).getReadPermission()) {
                                                isPublic = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (isPublic) {
                                        temp_serverUrl = request.getRequestURL().toString();
                                        tempUrl = "";
                                        //file = 1
                                        if (xdataTemp.getXincoCoreDataType().getId() == 1) {
                                            tempUrl = ((XincoAddAttribute) xdataTemp.getXincoAddAttributes().elementAt(0)).getAttribVarchar();
                                        } else {
                                            tempUrl = xdataTemp.getDesignation();
                                        }
                                        out.println("<tr>");
                                        out.println("<td class=\"text\">" + xdataTemp.getDesignation() + " (" + xdataTemp.getXincoCoreDataType().getDesignation() + " | " + xdataTemp.getXincoCoreLanguage().getSign() + ")" + "</td>");
                                        out.println("<td class=\"text\"><a href=\"" + "XincoPublisher/" + xdataTemp.getId() + "/" + tempUrl + "?list=" + request.getParameter("list") + "\">" + temp_serverUrl + "/" + xdataTemp.getId() + "/" + tempUrl + "</a></td>");
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
            if (coreDataId > 0) {

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
                for (i = 0; i < xcd.getXincoAddAttributes().size(); i++) {
                    xcdta = ((XincoCoreDataTypeAttribute) xcd.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i));
                    xaa = ((XincoAddAttribute) xcd.getXincoAddAttributes().elementAt(i));
                    out.println("<tr>");
                    out.println("<td class=\"text\" valign=\"top\"><b>" + xcdta.getDesignation() + ":</b></td>");
                    if (xcdta.getDataType().toLowerCase().compareTo("int") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttribInt() + "</pre></td>");
                    }
                    if (xcdta.getDataType().toLowerCase().compareTo("unsignedint") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttribUnsignedint() + "</pre></td>");
                    }
                    if (xcdta.getDataType().toLowerCase().compareTo("double") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttribDouble() + "</pre></td>");
                    }
                    if (xcdta.getDataType().toLowerCase().compareTo("varchar") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttribVarchar() + "</pre></td>");
                    }
                    if (xcdta.getDataType().toLowerCase().compareTo("text") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttribText() + "</pre></td>");
                    }
                    if (xcdta.getDataType().toLowerCase().compareTo("datetime") == 0) {
                        out.println("<td class=\"text\"><pre>" + xaa.getAttribDatetime() + "</pre></td>");
                    }
                    out.println("</tr>");
                }

                out.println("</table>");
            }

            //show footer
            if (coreDataId == 0) {
                out.println("<br><br><br>");
                out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
                out.println("<tr>");
                out.println("<td class=\"text\">&nbsp;</td>");
                out.println("<td class=\"text\">&copy; " + XincoSettingServer.getSetting("general.copyright.date").getStringValue() + ", " + (config.isAllowOutsideLinks() ? rb.getString("message.admin.main.footer") : "blueCubs.com and xinco.org"));
                out.println("</tr>");
                out.println("</table><tr><form action='menu.jsp'><input type='submit' value='" + rb.getString("message.admin.main.backtomain") + "' />" + "<input type='hidden' name='list' value='" + request.getParameter("list") + "'/></form></tr>" + "<tr><FORM><INPUT TYPE='button' VALUE='" + rb.getString("message.admin.main.back") + "' onClick='history.go(-1);return true;'><input type='hidden' name='list' value='" + request.getParameter("list") + "'/></FORM></tr>");
            }

            out.println("</span>");
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");

            out.close();
        } //end HTML output
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoPublisherServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoPublisherServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Returns a short description of the servlet.
     * @return String
     */
    @Override
    public String getServletInfo() {
        return "Servlet of xinco";
    }
    }
