/**
 * Copyright 2009 blueCubs.com
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>************************************************************ This project supports the
 * blueCubs vision of giving back to the community in exchange for free software! More information
 * on: http://www.bluecubs.org ************************************************************
 *
 * <p>Name: XincoCronServlet
 *
 * <p>Description: cronjob servlet
 *
 * <p>Original Author: Alexander Manes Date: 2005/01/17
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.server;

import com.bluecubs.xinco.archive.XincoArchiveThread;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.index.XincoIndexOptimizeThread;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public class XincoCronServlet extends HttpServlet {

  ResourceBundle lrb;
  // single instance of archiving thread
  XincoArchiveThread xat = null;
  // single instance of index optimizing thread
  XincoIndexOptimizeThread xiot = null;
  private XincoDBManager db = null;

  /**
   * Initializes the servlet.
   *
   * @param config
   * @throws jakarta.servlet.ServletException
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    // init archiving thread
    xat = XincoArchiveThread.getInstance();
    xat.start();

    // init index optimizing thread
    xiot = XincoIndexOptimizeThread.getInstance();
    xiot.start();
  }

  /** Destroys the servlet. */
  @Override
  public void destroy() {}

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws java.io.IOException
   */
  protected synchronized void processRequest(
      HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      db = new XincoDBManager();
    } catch (Exception e) {
    }
    Locale loc = null;
    try {
      String list = request.getParameter("list");
      String[] locales;
      locales = list.split("_");
      loc =
          switch (locales.length) {
            case 1 -> new Locale(locales[0]);
            case 2 -> new Locale(locales[0], locales[1]);
            case 3 -> new Locale(locales[0], locales[1], locales[2]);
            default -> Locale.getDefault();
          };
    } catch (Exception e) {
      loc = Locale.getDefault();
    }
    lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
    // start output
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    // show header
    out.println("<html>");
    out.println("<head>");
    out.println("<title>" + lrb.getString("message.admin.main.xincocron.label") + "</title>");
    out.println("<link rel=\"stylesheet\" href=\"xincostyle.css\" type=\"text/css\"/>");
    out.println(
        "<link rel='shortcut icon' href='resources/images/favicon.ico' type='image/x-icon'>");
    out.println("<link rel='icon' href='resources/images/favicon.ico' type='image/x-icon'> ");
    out.println("</head>");

    out.println(
        "<body "
            + (!db.config.isAllowOutsideLinks() ? "oncontextmenu='return false;' " : " ")
            + ">");
    out.println("<center>");
    out.println("<span class=\"text\">");

    out.println("");

    // show info
    out.println("<br>");
    out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
    out.println("<tr>");
    out.println(
        "<td class=\"text\" width=\"100\"><img src='resources/images/blueCubsSmall.gif'"
            + " border=\"0\"/></td>");
    out.println(
        "<td class=\"bigtext\" width=\"650\">"
            + lrb.getString("message.admin.main.xincocron.label")
            + "</td>");
    out.println("</tr>");
    out.println("</table>");
    out.println("<br><br>");
    out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
    out.println("<tr>");
    out.println(
        "<td class=\"text\" colspan=\"3\">"
            + lrb.getString("message.admin.main.xincocrondesc")
            + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td class=\"text\" colspan=\"3\">&nbsp;</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println(
        "<td class=\"text\" colspan=\"3\"><b>"
            + lrb.getString("message.xincocron.status")
            + "</b></td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td class=\"text\">" + lrb.getString("message.xincocron.openDB") + "</td>");
    out.println("<td class=\"text\">" + XincoDBManager.count + "</td>");
    out.println("<td class=\"text\">&nbsp;</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td class=\"text\" colspan=\"3\">&nbsp;</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println(
        "<td class=\"text\"><b>" + lrb.getString("message.xincocron.servicename") + "</b></td>");
    out.println(
        "<td class=\"text\"><b>" + lrb.getString("message.xincocron.service.first") + "</b></td>");
    out.println(
        "<td class=\"text\"><b>" + lrb.getString("message.xincocron.service.last") + "</b></td>");
    out.println("</tr>");

    out.println("<tr>");
    out.println(
        "<td class=\"text\">" + lrb.getString("message.xincocron.service.archiver") + "</td>");
    String xat_first_run = "";
    String xat_last_run = "";
    if (xat != null) {
      if (xat.firstRun != null) {
        xat_first_run = xat.firstRun.getTime().toString();
      }
      if (xat.lastRun != null) {
        xat_last_run = xat.lastRun.getTime().toString();
      }
    }
    out.println("<td class=\"text\">" + xat_first_run + "</td>");
    out.println("<td class=\"text\">" + xat_last_run + "</td>");
    out.println("</tr>");

    out.println("<tr>");
    out.println(
        "<td class=\"text\">"
            + lrb.getString("message.xincocron.service.indexOptimizer")
            + "</td>");
    String xiot_first_run = "";
    String xiot_last_run = "";
    if (xat != null) {
      if (xiot.firstRun != null) {
        xiot_first_run = xiot.firstRun.getTime().toString();
      }
      if (xiot.lastRun != null) {
        xiot_last_run = xiot.lastRun.getTime().toString();
      }
    }
    out.println("<td class=\"text\">" + xiot_first_run + "</td>");
    out.println("<td class=\"text\">" + xiot_last_run + "</td>");
    out.println("</tr>");

    out.println("</table>");

    // show footer
    out.println("<br><br><br>");
    out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
    out.println("<tr>");
    out.println("<td class=\"text\">&nbsp;</td>");
    out.println(
        "<td class=\"text\">&copy; "
            + lrb.getString("general.copyright.date")
            + ", "
            +
            // Avoid external links if general.setting.allowoutsidelinks is set to false
            // Security bug
            (db.config.isAllowOutsideLinks()
                ? lrb.getString("message.admin.main.footer")
                : "blueCubs.com and xinco.org"));
    out.println("</tr>");
    out.println(
        "</table><tr><form action='menu.jsp'><input type='submit' value='"
            + lrb.getString("message.admin.main.backtomain")
            + "' />"
            + "<input type='hidden' name='list' value='"
            + request.getParameter("list")
            + "'/></form></tr>"
            + "<tr><FORM><INPUT TYPE='button' VALUE='"
            + lrb.getString("message.admin.main.back")
            + "' onClick='history.go(-1);return true;'><input type='hidden' name='list' value='"
            + request.getParameter("list")
            + "'/></FORM></tr>");

    out.println("</span>");
    out.println("</center>");
    out.println("</body>");
    out.println("</html>");

    out.close();
  }

  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws java.io.IOException
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws java.io.IOException
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return Servlet info.
   */
  @Override
  public String getServletInfo() {
    return "Servlet of Xinco";
  }
}
