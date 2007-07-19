<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.util.Locale,
java.util.ResourceBundle,
java.util.Vector,
com.bluecubs.xinco.client.*"
%>
<%@page import="com.bluecubs.xinco.core.server.XincoSettingServer"%>
<%@page import="com.bluecubs.xinco.core.*"%>
<%@page import="com.bluecubs.xinco.service.*"%>
<%
XincoSettingServer xss= new XincoSettingServer();
String setting = ((XincoSetting)(xss.getSettings().elementAt(7))).getString_value();
Locale loc = null;
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
XincoClientSession xincoClientSession = new XincoClientSession();
XincoCoreDataType xcdt= new XincoCoreDataType();
//String xinco_service_endpoint = request.getRequestURL().toString();
//xinco_service_endpoint = xinco_service_endpoint.substring(0, xinco_service_endpoint.indexOf("/search.jsp"));
//xinco_service_endpoint = xinco_service_endpoint + "/services/Xinco";
//xincoClientSession.service_endpoint=xinco_service_endpoint;
//xincoClientSession.user.setUsername("system");
//xincoClientSession.user.setUserpassword(((XincoSetting)(xss.getXinco_settings().elementAt(11))).getString_value());
//xincoClientSession.status=1;
//xincoClientSession.xinco = new XincoService().getXinco(new java.net.URL(xincoClientSession.service_endpoint));

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%out.println(rb.getString("window.search"));%></title>
    </head>
    <body>
        
        <h1><%out.println(rb.getString("window.search"));%></h1>
        <form action="searchresult.jsp" method="POST" enctype="multipart/form-data">
            <select name="operator">
                <option></option>
                <option>AND</option>
                <option>OR</option>
                <option>NOT</option>
                <option>+</option>
                <option>-</option>
            </select>
            <%
//            out.println("<select name='options'><option></option><option><"+
//                    rb.getString("window.search.filecontent") +
//                    " (file)></option>");
//            for (int i=0;i<xincoClientSession.server_datatypes.size();i++) {
//                xcdt = (XincoCoreDataType)xincoClientSession.server_datatypes.elementAt(i);
//                for (int j=0;j<xcdt.getXinco_core_data_type_attributes().size();j++) {
//                    out.println("<option>"+
//                            ((XincoCoreDataTypeAttribute)xcdt.getXinco_core_data_type_attributes().elementAt(j)).getDesignation()+
//                            "</option>");
//                }
//            }
//            out.println("</select>");
            %>
        </form>
    </body>
</html>
