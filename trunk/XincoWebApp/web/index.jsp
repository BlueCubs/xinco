<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.ResourceBundle"%>

<html>
<head>
    <title>xinco DMS - Open Source Document Management</title>
    <link rel="stylesheet" href="xincostyle.css" type="text/css"/>
</head>
<body>
<center>
<span class="text"><br><img src="blueCubs.gif" border="0"/>
<br><span class="bigtext">xinco DMS - the Core of Information and Document Management</span><br><br>
<%
    int i = 0;
    ResourceBundle lrb = null;
    String[] locales;
    String text = "";
    //load locales
    lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
    locales = lrb.getString("AvailableLocales").split(",");
    out.println("<form name='language' action='menu.jsp'>" +
    "<table border='0'>" +
    "<tbody><td>Please Choose a language</td><td>");
    out.println("<select name='list'>");
    for (i=0;i<locales.length;i++)
        out.println("<option value='"+locales[i]+"'>"+lrb.getString("Locale." + locales[i]) + "</option>");
    out.println("</select></td></tbody></table><input type='submit' value='Submit' /></form>");
    %></td>

</table>

</form>
</span>
</center>
</body>
</html>
