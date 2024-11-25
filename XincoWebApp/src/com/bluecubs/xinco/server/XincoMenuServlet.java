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
 * Name:            XincoMenuServlet
 *
 * Description:     menu servlet
 *
 * Original Author: Javier Ortiz
 * Date:            2024
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */
package com.bluecubs.xinco.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XincoMenuServlet extends HttpServlet {

    private ResourceBundle rb;
    private ResourceBundle settings;
    private XincoCoreUserServer login_user = null;
    private String servletContext;

    /**
     * Initializes the servlet.
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        servletContext = config.getServletContext().getRealPath(".");
    }

    /**
     * Destroys the servlet.
     */
    @Override
    public void destroy() {
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
    @SuppressWarnings("unchecked")
    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            XincoDBManager db = new XincoDBManager();
            Locale loc = null;
            PrintWriter out = response.getWriter();
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
            } catch (Exception e) {
                loc = Locale.getDefault();
            }
            rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + rb.getString("message.admin.main.title") + "</title>");
            out.println("<link rel='stylesheet' href='xincostyle.css' type='text/css'/>");
            out.println("<link rel='shortcut icon' href='resources/images/favicon.ico' type='image/x-icon'>");
            out.println("<link rel='icon' href='resources/images/favicon.ico' type='image/x-icon'>");
            out.println("</head>");
            out.println("<body " + (!db.config.isAllowOutsideLinks() ? "oncontextmenu='return false;' " : " ") + ">");
            out.println("<center>");
            out.println("<span class='text'>");
            out.println("<br><img src='resources/images/blueCubs.gif' border='0'/>");
            out.println("<br><span class='bigtext'>" + rb.getString("message.admin.main.description") + "</span><br><br>");
            out.println("<table border='0' cellspacing='10' cellpadding='0'>");
            out.println("<tr>");
            File jnlp = null;
            File backup = null;
            String protocol = "http://";
            String url = request.getRequestURL().toString();
            url = url.substring(url.indexOf(protocol) + protocol.length(), url.indexOf("/xinco/menu"));
            File last = new File(servletContext + "/client/" + url + ".xinco");
            if (!last.exists()) {
                File dir = new File(servletContext + "/client/");
                String[] list = dir.list(new ExtensionFilter(".xinco"));

                if (list.length != 0) {
                  for (String list1 : list) {
                    new File(dir.getAbsolutePath(), list1).delete();
                  }
                }
                try {
                    jnlp = new File(servletContext + "/client/XincoExplorer.jnlp");
                    backup = new File(servletContext + "/client/XincoExplorer.jnlp.bak");
                    backup.createNewFile();
                    if (jnlp.exists()) {
                        FileChannel source = null;
                        FileChannel destination = null;
                        try {
                            source = new FileInputStream(jnlp).getChannel();
                            destination = new FileOutputStream(backup).getChannel();
                            destination.transferFrom(source, 0, source.size());
                        } finally {
                            if (source != null) {
                                source.close();
                            }
                            if (destination != null) {
                                destination.close();
                            }
                        }
                        try {
                            StringBuilder contents = new StringBuilder();
                            //use buffering, reading one line at a time
                            //FileReader always assumes default encoding is OK!
                            BufferedReader input = new BufferedReader(new FileReader(jnlp));
                            try {
                                String line = null; //not declared within while loop
                                /*
                                    * readLine is a bit quirky :
                                    * it returns the content of a line MINUS the newline.
                                    * it returns null only for the END of the stream.
                                    * it returns an empty String if two newlines appear in a row.
                                 */
                                while ((line = input.readLine()) != null) {
                                    if (line.contains("codebase") && !line.startsWith("<!")) {
                                        String start = line.substring(0,
                                                line.indexOf(protocol) + protocol.length());
                                        String end = null;
                                        end = line.substring(line.indexOf("/xinco"));
                                        line = start + url + end;
                                    }
                                    contents.append(line);
                                    contents.append(System.getProperty("line.separator"));
                                }
                                //use buffering to update jnlp
                                Writer output = new BufferedWriter(new FileWriter(jnlp));
                                try {
                                    //FileWriter always assumes default encoding is OK!
                                    output.write(contents.toString());
                                } finally {
                                    output.close();
                                }
                            } finally {
                                input.close();
                                backup.delete();
                                last.createNewFile();
                            }
                        } catch (IOException ex) {
                            try {
                                source = new FileInputStream(backup).getChannel();
                                destination = new FileOutputStream(jnlp).getChannel();
                                destination.transferFrom(source, 0, source.size());
                                backup.delete();
                            } finally {
                                if (source != null) {
                                    source.close();
                                }
                                if (destination != null) {
                                    destination.close();
                                }
                            }
                        }
                    } else {
                        throw new XincoException("Missing XincoExplorer.jnlp!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.println("<td class='text'><a href='client/XincoExplorer.jnlp' class='link'>" + rb.getString("message.admin.main.webstart.link") + "</a></td>");
            out.println("<td class='text'>" + rb.getString("message.admin.main.webstart") + "</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class='text'>" + rb.getString("message.admin.main.endpoint.label") + "</td>");
            String xinco_service_endpoint = request.getRequestURL().toString();
            xinco_service_endpoint = xinco_service_endpoint.substring(0, xinco_service_endpoint.indexOf("/menu"));
            xinco_service_endpoint = xinco_service_endpoint + "/services/Xinco";
            out.println("<td class='text'><b>" + xinco_service_endpoint + "</b></td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class='text'><a href='XincoPublisher?list=" + request.getParameter("list") + "' class='link'>" + rb.getString("message.admin.main.publisher.label") + "</a></td>");
            out.println("<td class='text'>" + rb.getString("message.admin.main.publisherdesc") + "</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class='text'>" + (db.config.isAllowOutsideLinks() ? "<a href='http://java.sun.com' class='link'>"
                    + rb.getString("message.admin.main.java.label") + "</a>" : "http://java.sun.com") + "</td>");
            out.println("<td class='text'>" + rb.getString("message.admin.main.javadesc") + "</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class='text'>&nbsp;</td>");
            out.println("<td class='text'>&nbsp;</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class='text'><a href='XincoAdmin?list=" + request.getParameter("list") + "' class='link'>" + rb.getString("message.admin.main.admin.label") + "</a></td>");
            out.println("<td class='text'>" + rb.getString("message.admin.main.admindesc"));
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class='text'><a href='XincoCron?list=" + request.getParameter("list") + "' class='link'>" + rb.getString("message.admin.main.xincocron.label") + "</a></td>");
            out.println("<td class='text'>" + rb.getString("message.admin.main.xincocrondesc") + "</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class='text'><a href='happyaxis.jsp'  class='link'>" + rb.getString("message.admin.main.validate.label") + "</a></td>");
            out.println("<td class='text'>" + rb.getString("message.admin.main.validatedesc"));
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class='text'>&nbsp;</td>");
            out.println("<td class='text'>&nbsp;</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td class='text'>&nbsp;</td>");
            out.println("<td class='text'>&copy; " + rb.getString("general.copyright.date") + ", "
                    + //Avoid external links if general.setting.allowoutsidelinks is set to false
                    //Security bug
                    (db.config.isAllowOutsideLinks() ? rb.getString("message.admin.main.footer") : "blueCubs.com and xinco.org") + "</a></td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("</span>");
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception ex) {
            Logger.getLogger(XincoMenuServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            throws ServletException, IOException {
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
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return
     */
    @Override
    public String getServletInfo() {
        return rb.getString("message.servlet.info");
    }
}
