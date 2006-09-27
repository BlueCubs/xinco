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

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.bluecubs.xinco.archive.*;
import com.bluecubs.xinco.index.*;
import com.bluecubs.xinco.core.server.XincoDBManager;

public class XincoCronServlet extends HttpServlet {
    
	//single instance of archiving thread
	XincoArchiveThread xat = null; 
	
	//single instance of index optimizing thread
	XincoIndexOptimizeThread xiot = null; 
	
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        //init archiving thread
		xat = XincoArchiveThread.getInstance();
		xat.start();
        
        //init index optimizing thread
		xiot = XincoIndexOptimizeThread.getInstance();
		xiot.start();
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
			
		//start output
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        //show header
        out.println("<html>");
        out.println("<head>");
    	out.println("<title>XincoCron</title>");
		out.println("<link rel=\"stylesheet\" href=\"xincostyle.css\" type=\"text/css\"/>");
        out.println("</head>");

        out.println("<body>");
		out.println("<center>");
		out.println("<span class=\"text\">");
                
		out.println("");

		//show info
		out.println("<br>");
		out.println("<table border=\"0\" width=\"750\" cellspacing=\"10\" cellpadding=\"0\">");
		out.println("<tr>");
		out.println("<td class=\"text\" width=\"100\"><img src=\"blueCubsSmall.gif\" border=\"0\"/></td>");
		out.println("<td class=\"bigtext\" width=\"650\">XincoCron</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<br><br>");
		out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
		out.println("<tr>");
		out.println("<td class=\"text\" colspan=\"3\">XincoCron initializes and runs services for the xinco DMS web application.</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td class=\"text\" colspan=\"3\">&nbsp;</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td class=\"text\" colspan=\"3\"><b>General Status:</b></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td class=\"text\">Open DB-Connections:</td>");
		out.println("<td class=\"text\">" + XincoDBManager.count + "</td>");
		out.println("<td class=\"text\">&nbsp;</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td class=\"text\" colspan=\"3\">&nbsp;</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td class=\"text\"><b>Service Name:</b></td>");
		out.println("<td class=\"text\"><b>First Run:</b></td>");
		out.println("<td class=\"text\"><b>Last Run:</b></td>");
		out.println("</tr>");

		out.println("<tr>");
		out.println("<td class=\"text\">Archiver</td>");
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
		out.println("<td class=\"text\">Index Optimizer</td>");
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

		//show footer
		out.println("<br><br><br>");
		out.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
		out.println("<tr>");
		out.println("<td class=\"text\">&nbsp;</td>");
		out.println("<td class=\"text\">&copy; 2004-2006, <a href=\"http://www.bluecubs.com\" target=\"_blank\" class=\"link\">blueCubs.com</a> and <a href=\"http://www.xinco.org\" target=\"_blank\" class=\"link\">xinco.org</a></td>");
		out.println("</tr>");
		out.println("</table>");
	
		out.println("</span>");
		out.println("</center>");
		out.println("</body>");
        out.println("</html>");

        out.close();
        
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Servlet of xinco";
    }
    
}
