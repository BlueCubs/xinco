<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.ResourceBundle"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Password Aging</title>
    <link rel="stylesheet" href="xincostyle.css" type="text/css"/>
</head>
<body class="text">
<center>
    <br/><img src="blueCubs.gif" width="356" height="400" alt="blueCubs"/><br/>
    <%
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
    ResourceBundle rb=ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages",loc),
            settings=ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings",loc);
    out.println("<span class='bigtext'>"+rb.getString("password.aged")+"</span><br/><br/>");
    out.print(rb.getString("password.change.warning").replaceAll("%n",settings.getString("password.unusable_period")));%>
    <br/><br/>
    <form name="password" action="XincoAdmin" method="post">
        <table border="0">
            <tbody>
                <tr>
                    <td class="text"><%out.println(rb.getString("general.password")+":");%></td>
                    <td class="text"><input type="password" name="new" value="" /></td>
                </tr>
                <tr>
                    <td class="text"><%out.println(rb.getString("password.confirm")+":");%></td>
                    <td class="text"><input type="password" name="confirm" value="" /></td>
                </tr>
            </tbody>
        </table>
        <br/>
        <input type="submit" value="<%out.println(rb.getString("password.change"));%>" name="changePassword" />
        <input type="hidden" name="user" value="<% out.print(request.getParameter("user")); %>"/>
    </form>
</center>
</body>
</html>
