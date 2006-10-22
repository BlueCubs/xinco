<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

   <%
out.println("<html>" +
        "<head>" +
        "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>" +
        "        <title>Password Aging</title>" +
        "    </head>" +
        "    <body>" +
        "        <center>" +
        "            <br><img src='blueCubs.gif' width='356' height='400' alt='blueCubs'/>" +
        "            <h1>Change your password</h1>" +
        "            Your password have exeeded the aging periood.<br>" +
        "            Please provide a new one.<br> The password can't be one used in the previos year.<br>" +
        "            <form name='password' action='XincoAdmin' method='post'><table border='1'>" +
        "               <thead>" +
        "                    <tr>" +
        "                        <th>Description</th>" +
        "                        <th>Value</th>" +
        "                    </tr>" +
        "                </thead>" +
        "                <tbody>" +
        "                    <tr>" +
        "                        <td>New password:</td>" +
        "                        <td><input type='password' name='new' value='' /></td>" +
        "                    </tr>" +
        "                    <tr>" +
        "                        <td>Confirm password:</td>" +
        "                        <td><input type='password' name='confirm' value='' /></td>" +
        "                    </tr>" +
        "                </tbody>" +
        "            </table>" +
        "                <br>" +
        "                <input type='submit' value='Change Password' name='changePassword' />" +
        "                <input type='hidden' name='user' value="+request.getParameter("user")+"/>"+
        "            </form>" +
        "        </center>" +
        "        </body>" +
        "</html>");
   %>