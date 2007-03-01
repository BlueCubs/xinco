<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="com.bluecubs.xinco.core.server.XincoSettingServer"%>
<%@page import="com.bluecubs.xinco.core.XincoSetting"%>

<html>
    <%int i = 0;
    ResourceBundle lrb = null,rb=null;
    String[] locales;
    String text = "";
    //load locales
    lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
    rb=ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
    %>
    <head>
        <title><%out.println(rb.getString("message.admin.main.title"));%></title>
        <link rel="stylesheet" href="xincostyle.css" type="text/css"/>
    </head>
    <body>
        <center>
            
            <span class="text"><br><img src="blueCubs.gif" border="0"/>
                <br><span class="bigtext"><%out.println(rb.getString("message.admin.main.description"));%></span>
                <br><br>
                
                <form name='language' action='menu.jsp'>
                    
                    <table border='0'>
                        <tbody>
                        <tr>
                            <td class="text">Please choose a language:&nbsp;</td>
                            <td class="text">
                                <%
                                
                                locales = lrb.getString("AvailableLocales").split(",");
                                out.println("" +
                                        "" +
                                        "");
                                out.println("<select name='list'>");
                                for (i=0;i<locales.length;i++)
                                    out.println("<option value='"+locales[i]+"'>"+lrb.getString("Locale." + locales[i]) + "</option>");
                                out.println("</select>&nbsp;");
                                %>
                            </td>
                            <td class="text">
                                <input type='submit' value='Submit' />
                            </td>
                        </tr>
                    </table>
                    
                </form>
                
                <br>
                <span class="text" style="font-size: 10px;">
                    <%
                    //load settings
                    XincoSettingServer xss= new XincoSettingServer();
                    String version="[Version " + ((XincoSetting)(xss.getXinco_settings().elementAt(0))).getInt_value() 
                    + "." + ((XincoSetting)(xss.getXinco_settings().elementAt(1))).getInt_value() + "." + 
                            ((XincoSetting)(xss.getXinco_settings().elementAt(2))).getInt_value();
                    if(((XincoSetting)(xss.getXinco_settings().elementAt(3))).getString_value()!=null &&
                            !((XincoSetting)(xss.getXinco_settings().elementAt(3))).getString_value().trim().equals(""))
                        version+=" " + ((XincoSetting)(xss.getXinco_settings().elementAt(3))).getString_value();
                    version+="]";
                    out.println(version);
                    %>
                </span>
                
            </span>
        </center>
    </body>
</html>
