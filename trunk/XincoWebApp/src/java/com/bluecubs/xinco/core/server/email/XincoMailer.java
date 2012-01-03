/*
 Some SMTP servers require a username and password authentication before you
 can use their Server for Sending mail. This is most common with couple
 of ISP's who provide SMTP Address to Send Mail.

 This Program gives any example on how to do SMTP Authentication
 (User and Password verification)

 This is a free source code and is provided as it is without any warranties and
 it can be used in any your code for free.

 Author : Sudhir Ancha
 */
package com.bluecubs.xinco.core.server.email;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class XincoMailer {

    public static void postMail(List<String> recipients, String subject,
            String message, String from) throws MessagingException, XincoException {
        boolean debug = false;

        //Set the host smtp address and related properties
        Properties props = new Properties();
        props.put("mail.smtp.host", XincoSettingServer.getSetting(
                new XincoCoreUserServer(1),
                "setting.email.host").getStringValue());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", XincoSettingServer.getSetting(
                new XincoCoreUserServer(1),
                "setting.email.port").getStringValue());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth ", "true ");

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);

        session.setDebug(debug);

        // create a message
        Message msg = new MimeMessage(session);

        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.size()];
        int i = 0;
        for (String email : recipients) {
            addressTo[i] = new InternetAddress(email);
            i++;
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);


        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }

    /**
     * SimpleAuthenticator is used to do simple authentication when the SMTP
     * server requires it.
     */
    private static class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            try {
                String username = XincoSettingServer.getSetting(
                        new XincoCoreUserServer(1),
                        "setting.email.user").getStringValue();
                String password = XincoSettingServer.getSetting(
                        new XincoCoreUserServer(1),
                        "setting.email.password").getStringValue();
                return new PasswordAuthentication(username, password);
            } catch (XincoException ex) {
                Logger.getLogger(XincoMailer.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
}
