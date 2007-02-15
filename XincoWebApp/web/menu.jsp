<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Locale"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
Locale loc=new Locale(request.getParameter("list"));
ResourceBundle rb=ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages",loc);
rb.getLocale();
out.println("<html>");
out.println("<head>");
out.println("<title>"+rb.getString("message.admin.main.title")+"</title>");
out.println("<link rel='stylesheet' href='xincostyle.css' type='text/css'/>");
out.println("</head>");
out.println("<body>");
out.println("<center>");
out.println("<span class='text'>");
out.println("<br><img src='blueCubs.gif' border='0'/>");
out.println("<br><span class='bigtext'>"+rb.getString("message.admin.main.description")+"</span><br><br>");
out.println("<table border='0' cellspacing='10' cellpadding='0'>");
out.println("<tr>");
out.println("<td class='text'><a href='client/XincoExplorer.jnlp' class='link'  icon='out'>"+rb.getString("message.admin.main.webstart.link")+"</a></td>");
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
out.println("<td class='text'><a href='XincoPublisher?list="+request.getParameter("list")+"' class='link'  icon='out'>"+rb.getString("message.admin.main.publisher.label")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.publisherdesc")+"</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'><a href='http://java.sun.com' " +
"target='_blank' class='link'  icon='out'>"+rb.getString("message.admin.main.java.label")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.javadesc")+"</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'>&nbsp;</td>");
out.println("<td class='text'>&nbsp;</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'><a href='XincoAdmin?list="+request.getParameter("list")+"' class='link'  icon='out'>"+rb.getString("message.admin.main.admin.label")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.admindesc"));
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'><a href='XincoCron?list="+request.getParameter("list")+"' class='link'  icon='out'>"+rb.getString("message.admin.main.xincocron.label")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.xincocrondesc")+"</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'><a href='happyaxis.jsp' target='_blank' class='link'  icon='out'>"+rb.getString("message.admin.main.validate.label")+"</a></td>");
out.println("<td class='text'>"+rb.getString("message.admin.main.validatedesc"));
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'>&nbsp;</td>");
out.println("<td class='text'>&nbsp;</td>");
out.println("</tr>");
out.println("<tr>");
out.println("<td class='text'>&nbsp;</td>");
out.println("<td class='text'>&copy; "+rb.getString("general.copyright.date")+", "+rb.getString("message.admin.main.footer")+"</a></td>");
out.println("</tr>");
out.println("</table>");
out.println("</span>");
out.println("</center>");
out.println("</body>");
out.println("</html>");
%>
