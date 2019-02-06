/**
 *Copyright 2009 blueCubs.com
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
 * Name:            XincoCronServlet
 *
 * Description:     cronjob servlet
 *
 * Original Author: Alexander Manes
 * Date:            2005/01/17
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.count;
import static java.util.Calendar.getInstance;
import static java.util.Locale.getDefault;
import static java.util.ResourceBundle.getBundle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluecubs.xinco.archive.XincoArchiveTimerTask;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.index.XincoIndexOptimizeTimerTask;

public class XincoCronServlet extends HttpServlet
{

  ResourceBundle lrb;
  //single instance of archiving thread
  XincoArchiveTimerTask xatt = null;
  //single instance of index optimizing thread
  XincoIndexOptimizeTimerTask xiott = null;
  private XincoDBManager db = null;
  private static Timer timer = null;

  /**
   * Initializes the servlet.
   *
   * @param config
   * @throws javax.servlet.ServletException
   */
  @Override
  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);

    timer = new Timer("xincoTimer");

    //init archiving thread
    xatt = new XincoArchiveTimerTask();
    timer.schedule(xatt, getInstance().getTime(), 14_400_000); // Every 4 hours

    //init index optimizing thread
    xiott = new XincoIndexOptimizeTimerTask();
    timer.schedule(xiott, getInstance().getTime(), 604_800_000); //Weekly
  }

  /**
   * Destroys the servlet.
   */
  @Override
  public void destroy()
  {
    timer.cancel();
    timer = null;
  }

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws javax.servlet.ServletException
   * @throws java.io.IOException
   */
  protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    try
    {
      db = new XincoDBManager();
    }
    catch (Exception e)
    {
    }
    Locale loc;
    try
    {
      String list = request.getParameter("list");
      String[] locales;
      locales = list.split("_");
      switch (locales.length)
      {
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
          loc = getDefault();
      }
    }
    catch (Exception e)
    {
      loc = getDefault();
    }
    lrb = getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
    //start output
    response.setContentType("text/html");
    //show header
    try (PrintWriter out = response.getWriter())
    {
      //show header
      out.println("<html>");
      out.println("<head>");
      out.println("<title>" + lrb.getString("message.admin.main.xincocron.label") + "</title>");
      out.println("<link rel=\"stylesheet\" href=\"xincostyle.css\" type=\"text/css\"/>");
      out.println("<link rel='shortcut icon' href='resources/images/favicon.ico' type='image/x-icon'>");
      out.println("<link rel='icon' href='resources/images/favicon.ico' type='image/x-icon'> ");
      out.println("</head>");
      out.println("<body " + (!db.config.isAllowOutsideLinks() ? "oncontextmenu='return false;' " : " ") + ">");
      out.println("<center>");
      out.println("<span class=\"text\">");
      out.println("");
      //show info
      out.println("<br>");
      out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
      out.println("<tr>");
      out.println("<td class=\"text\" width=\"100\"><img src='resources/images/blueCubsSmall.gif' border=\"0\"/></td>");
      out.println("<td class=\"bigtext\" width=\"650\">" + lrb.getString("message.admin.main.xincocron.label") + "</td>");
      out.println("</tr>");
      out.println("</table>");
      out.println("<br><br>");
      out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
      out.println("<tr>");
      out.println("<td class=\"text\" colspan=\"3\">" + lrb.getString("message.admin.main.xincocrondesc") + "</td>");
      out.println("</tr>");
      out.println("<tr>");
      out.println("<td class=\"text\" colspan=\"3\">&nbsp;</td>");
      out.println("</tr>");
      out.println("<tr>");
      out.println("<td class=\"text\" colspan=\"3\"><b>" + lrb.getString("message.xincocron.status") + "</b></td>");
      out.println("</tr>");
      out.println("<tr>");
      out.println("<td class=\"text\">" + lrb.getString("message.xincocron.openDB") + "</td>");
      out.println("<td class=\"text\">" + count + "</td>");
      out.println("<td class=\"text\">&nbsp;</td>");
      out.println("</tr>");
      out.println("<tr>");
      out.println("<td class=\"text\" colspan=\"3\">&nbsp;</td>");
      out.println("</tr>");
      out.println("<tr>");
      out.println("<td class=\"text\"><b>" + lrb.getString("message.xincocron.servicename") + "</b></td>");
      out.println("<td class=\"text\"><b>" + lrb.getString("message.xincocron.service.first") + "</b></td>");
      out.println("<td class=\"text\"><b>" + lrb.getString("message.xincocron.service.last") + "</b></td>");
      out.println("</tr>");
      out.println("<tr>");
      out.println("<td class=\"text\">" + lrb.getString("message.xincocron.service.archiver") + "</td>");
      String xat_first_run = "";
      String xat_last_run = "";
      if (xatt != null)
      {
        if (xatt.firstRun != null)
        {
          xat_first_run = xatt.firstRun.getTime().toString();
        }
        if (xatt.lastRun != null)
        {
          xat_last_run = xatt.lastRun.getTime().toString();
        }
      }
      out.println("<td class=\"text\">" + xat_first_run + "</td>");
      out.println("<td class=\"text\">" + xat_last_run + "</td>");
      out.println("</tr>");
      out.println("<tr>");
      out.println("<td class=\"text\">" + lrb.getString("message.xincocron.service.indexOptimizer") + "</td>");
      String xiot_first_run = "";
      String xiot_last_run = "";
      if (xatt != null)
      {
        if (xiott.firstRun != null)
        {
          xiot_first_run = xiott.firstRun.getTime().toString();
        }
        if (xiott.lastRun != null)
        {
          xiot_last_run = xiott.lastRun.getTime().toString();
        }
      }
      out.println("<td class=\"text\">" + xiot_first_run + "</td>");
      out.println("<td class=\"text\">" + xiot_last_run + "</td>");
      out.println("</tr>");
      out.println("</table>");
      //show footer
      out.println("<br><br><br>");
      out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
      out.println("<tr>");
      out.println("<td class=\"text\">&nbsp;</td>");
      out.println("<td class=\"text\">&copy; " + lrb.getString("general.copyright.date") + ", "
              + //Avoid external links if general.setting.allowoutsidelinks is set to false
              //Security bug
              (db.config.isAllowOutsideLinks() ? lrb.getString("message.admin.main.footer") : "blueCubs.com and xinco.org"));
      out.println("</tr>");
      out.println("</table><tr><form action='menu.jsp'><input type='submit' value='"
              + lrb.getString("message.admin.main.backtomain") + "' />"
              + "<input type='hidden' name='list' value='" + request.getParameter("list") + "'/></form></tr>"
              + "<tr><FORM><INPUT TYPE='button' VALUE='" + lrb.getString("message.admin.main.back")
              + "' onClick='history.go(-1);return true;'><input type='hidden' name='list' value='"
              + request.getParameter("list") + "'/></FORM></tr>");
      out.println("</span>");
      out.println("</center>");
      out.println("</body>");
      out.println("</html>");
    }

  }

  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws javax.servlet.ServletException
   * @throws java.io.IOException
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws javax.servlet.ServletException
   * @throws java.io.IOException
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return
   */
  @Override
  public String getServletInfo()
  {
    return "Servlet of xinco";
  }
}
