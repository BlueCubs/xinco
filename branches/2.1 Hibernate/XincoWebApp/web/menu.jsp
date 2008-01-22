<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Locale"%>
<%@page import="com.bluecubs.xinco.core.server.persistence.XincoPersistenceManager"%>
<%@page import="com.bluecubs.xinco.core.server.persistence.XincoSettingServer"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
Locale loc = null;
XincoPersistenceManager pm=new XincoPersistenceManager();
try {
    String list = request.getParameter("list");
    String[] locales;
    locales = list.split("_");
    switch(locales.length){
        case 1: loc = new Locale(locales[0]);break;
        case 2: loc = new Locale(locales[0],locales[1]);break;
        case 3: loc = new
                Locale(locales[0],locales[1],locales[2]);break;
        default: loc = Locale.getDefault();
    }
} catch (Exception e) {
    loc = Locale.getDefault();
}
ResourceBundle rb=ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages",loc);
rb.getLocale();
out.println("<html>");
out.println("<head>");
out.println("<title>"+rb.getString("message.admin.main.title")+"</title>");
out.println("<link rel='stylesheet' href='xincostyle.css' type='text/css'/>");
if(!XincoSettingServer.getSetting("setting.allowoutsidelinks").getBoolValue())
    out.println(pm.getWebBlockRightClickScript());
out.println("</head>");
out.println("<body>");
if(!XincoSettingServer.getSetting("setting.allowoutsidelinks").getBoolValue())
    out.println(pm.getWebBlockRightClickScript());
out.println("<center>");
out.println("<span class='text'>");
out.println("<br><img src='resources/images/blueCubs.gif' border='0'/>");
out.println("<br><span class='bigtext'>"+rb.getString("message.admin.main.description")+"</span><br><br>");
out.println("<table border='0' cellspacing='10' cellpadding='0'>");
out.println("<tr>");
out.println("<td class='text'><a href='client/XincoExplorer.jnlp' class='link'>"+rb.getString("message.admin.main.webstart.link")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.webstart")+"</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'>"+rb.getString("message.admin.main.endpoint.label")+"</td>");
String xinco_service_endpoint = request.getRequestURL().toString();
xinco_service_endpoint = xinco_service_endpoint.substring(0, xinco_service_endpoint.indexOf("/menu.jsp"));
xinco_service_endpoint = xinco_service_endpoint + "/services/Xinco";
out.println("<td class='text'><b>"+xinco_service_endpoint+"</b></td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'><a href='XincoPublisher?list="+request.getParameter("list")+"' class='link'>"+rb.getString("message.admin.main.publisher.label")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.publisherdesc")+"</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'>"+(XincoSettingServer.getSetting("setting.allowoutsidelinks").getBoolValue()? "<a href='http://java.sun.com' class='link'>"+
        rb.getString("message.admin.main.java.label")+"</a>":"http://java.sun.com")+"</td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.javadesc")+"</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'>&nbsp;</td>");
out.println("<td class='text'>&nbsp;</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'><a href='XincoAdmin?list="+request.getParameter("list")+"' class='link'>"+rb.getString("message.admin.main.admin.label")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.admindesc"));
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'><a href='XincoCron?list="+request.getParameter("list")+"' class='link'>"+rb.getString("message.admin.main.xincocron.label")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.xincocrondesc")+"</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'><a href='happyaxis.jsp'  class='link'>"+rb.getString("message.admin.main.validate.label")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.validatedesc"));
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'>&nbsp;</td>");
out.println("<td class='text'>&nbsp;</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'>&nbsp;</td>");
out.println("<td class='text'>&copy; "+XincoSettingServer.getSetting("general.copyright.date").getStringValue()+", "+(XincoSettingServer.getSetting("setting.allowoutsidelinks").getBoolValue()? rb.getString("message.admin.main.footer"):"blueCubs.com and xinco.org")+"</a></td>");
out.println("</tr>");
out.println("</table>");
out.println("</span>");
out.println("</center>");
out.println("</body>");
out.println("</html>");
%>
