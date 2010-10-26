<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.nio.channels.FileChannel"%>
<%@page import="com.bluecubs.xinco.core.XincoException"%>
<%@page import="java.io.Writer"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.net.URISyntaxException"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Locale"%>
<%@page import="com.bluecubs.xinco.core.server.XincoDBManager"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            XincoDBManager db = new XincoDBManager();
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
            } catch (Exception e) {
                loc = Locale.getDefault();
            }
            ResourceBundle rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
            rb.getLocale();
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
            url = url.substring(url.indexOf(protocol) + protocol.length(), url.indexOf("/xinco/menu.jsp"));
            File last = new File(getServletContext().getRealPath("/client/" + url));
            if (!last.exists()) {
                try {
                    jnlp = new File(getServletContext().getRealPath("/client/XincoExplorer.jnlp"));
                    backup = new File(getServletContext().getRealPath("/client/XincoExplorer.jnlp.bak"));
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
            xinco_service_endpoint = xinco_service_endpoint.substring(0, xinco_service_endpoint.indexOf("/menu.jsp"));
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
%>
