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
/**
 *Copyright 2004 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            XincoCoreACEServer
 *
 * Description:     access control entry
 *
 * Original Author: Javier A. Ortiz (based on work by Sudhir Ancha)
 * Date:            2006
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server.email;

import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.core.server.persistence.XincoSettingServer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Send email based on DB settings
 * @author Javier A. Ortiz
 */
public class XincoMailer {

    private String SMTP_HOST_NAME;
    private String SMTP_AUTH_USER;
    private String SMTP_AUTH_PWD;
    private static final String emailMsgTxt = "Test";
    private static final String emailSubjectTxt = "Test Subject";
    private static final String emailFromAddress = "info@bluecubs.com";
    private String port;
    // Add List of Email address to who email needs to be sent to
    private static final String[] emailList = {"info@bluecubs.com"};

    public static void main(String args[]) throws Exception {
        XincoMailer Xmailer = new XincoMailer();
        Xmailer.postMail(emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress);
    }

    public XincoMailer() {
        try {
            SMTP_HOST_NAME = XincoSettingServer.getSetting("setting.email.host").getStringValue();
            SMTP_AUTH_USER = XincoSettingServer.getSetting("setting.email.user").getStringValue();
            SMTP_AUTH_PWD = XincoSettingServer.getSetting("setting.email.password").getStringValue();
            port = XincoSettingServer.getSetting("setting.email.port").getStringValue();
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoMailer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Post mail
     * @param recipients
     * @param subject
     * @param message
     * @param from
     * @throws javax.mail.MessagingException
     */
    public void postMail(String recipients[], String subject,
            String message, String from) throws MessagingException {
        boolean debug = false;
        //Set the host smtp address and related properties
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", this.port);
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
        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }

    /**
     * SimpleAuthenticator is used to do simple authentication
     * when the SMTP server requires it.
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }
}



