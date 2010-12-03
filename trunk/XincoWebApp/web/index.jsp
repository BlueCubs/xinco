<%@page import="com.bluecubs.xinco.core.server.XincoDBManager"%>
<%@page import="com.bluecubs.xinco.core.server.BrowserDataExtractor"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.ResourceBundle"%>

<%
            BrowserDataExtractor extractor = new BrowserDataExtractor(request, session);
            XincoDBManager db = new XincoDBManager();
            if (db.config.isGuessLanguage()
                    && !extractor.getLanguage().isEmpty()
                    && extractor.isLanguageSupported(extractor.getLanguage())) {
                pageContext.forward("menu.jsp?list=" + extractor.getLanguage());
            }
%>
<html>
    <head>
        <title>xinco DMS - Open Source Document Management</title>
        <link rel="stylesheet" href="xincostyle.css" type="text/css"/>
        <link rel='shortcut icon' href='resources/images/favicon.ico' type='image/x-icon'>
        <link rel='icon' href='resources/images/favicon.ico' type='image/x-icon'>
    </head>
    <body>
        <center>
            <span class="text"><br><img src="resources/images/blueCubs.gif" border="0" alt="Blue Cubs"/>
                <br><span class="bigtext">xinco DMS - the Core of Information and Document Management</span>
                <br><br>

                <form name='language' action='menu.jsp'>

                    <table border='0'>
                        <tbody>
                            <tr>
                                <td class="text">Please choose a language:&nbsp;</td>
                                <td class="text">
                                    <%
                                                int i = 0;
                                                ResourceBundle lrb = null;
                                                String[] locales;
                                                String text = "";
                                                //load locales
                                                lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
                                                locales = lrb.getString("AvailableLocales").split(",");
                                                out.println(""
                                                        + ""
                                                        + "");
                                                out.println("<select name='list'>");
                                                for (i = 0; i < locales.length; i++) {
                                                    out.println("<option value='" + locales[i] + "'>" + lrb.getString("Locale." + locales[i]) + "</option>");
                                                }
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
                                ResourceBundle settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
                                out.println("[Version " + settings.getString("version.high") + "." + settings.getString("version.mid") + "." + settings.getString("version.low") + (settings.getString("version.postfix").isEmpty() ? "" : " " + settings.getString("version.postfix")) + "]");
                    %>
                </span>

            </span>
        </center>
    </body>
</html>
