<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
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
            <span class="bigtext">Change your password</span><br/><br/>
            <span class="text">Your password has exeeded the aging period.</span><br/>
            <span class="text">Please provide a new one.</span><br/>
            <span class="text">The password can't be one used in previous <%
            ResourceBundle rb=ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
            out.print(rb.getString("password.unusable_period"));%> days.</span><br/><br/>
            <form name="password" action="XincoAdmin" method="post">
            <table border="0">
               <!--
                <thead>
                    <tr>
                        <th class="text">Description</th>
                        <th class="text">Value</th>
                    </tr>
                </thead>
                -->
                <tbody>
                    <tr>
                        <td class="text">New password:</td>
                        <td class="text"><input type="password" name="new" value="" /></td>
                    </tr>
                    <tr>
                        <td class="text">Confirm password:</td>
                        <td class="text"><input type="password" name="confirm" value="" /></td>
                    </tr>
                </tbody>
            </table>
                <br/>
                <input type="submit" value="Change Password" name="changePassword" />
                <input type="hidden" name="user" value="<% out.print(request.getParameter("user")); %>"/>
            </form>
        </center>
    </body>
</html>
