/**
 *Copyright 2007 blueCubs.com
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
 * Name:            XincoMailer
 *
 * Description:     XincoMailer
 *
 * Original Author: Javier Ortiz
 * Date:            2007
 *
 * Modifications:
 *
 * Who?             When?             What?
 *************************************************************
 */
package com.bluecubs.xinco.core.server.email;

import com.bluecubs.xinco.core.XincoEmail;
import com.bluecubs.xinco.core.server.XincoDBManager;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class XincoMailer extends XincoEmail{
    private String host_name = "";
    private String User = "";
    private String Password  = "";
    
    private String emailMsgTxt      = "";
    private String emailSubjectTxt  = "";
    private String emailFromAddress = "";
    private String port = "";
    
    // Add List of Email address to who email needs to be sent to
    private String[] emailList = {""};
    
    public XincoMailer(XincoDBManager DBM){
        setHostName(DBM.getSetting("general.setting.email.host").getString_value());
        setUsername(DBM.getSetting("general.setting.email.user").getString_value());
        setPassword(DBM.getSetting("general.setting.email.password").getString_value());
        setPort(DBM.getSetting("general.setting.email.port").getString_value());
    }
    
    public void postMail( String recipients[ ], String subject,
            String message , String from) throws MessagingException {
        boolean debug = false;
        
        //Set the host smtp address and related properties
        Properties props = new Properties();
        props.put("mail.smtp.host", host_name);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", this.port);
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth ", "true ");
        
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", this.port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        
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
        return;
    }
    
    
    /**
     * SimpleAuthenticator is used to do simple authentication
     * when the SMTP server requires it.
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        
        public PasswordAuthentication getPasswordAuthentication() {
            String username = User;
            String password = Password;
            return new PasswordAuthentication(username, password);
        }
    }
    
    public void setHostName(String SMTP_HOST_NAME) {
        this.host_name = SMTP_HOST_NAME;
    }
    
    public void setUsername(String SMTP_AUTH_USER) {
        this.User = SMTP_AUTH_USER;
    }
    
    public void setPassword(String SMTP_AUTH_PWD) {
        this.Password = SMTP_AUTH_PWD;
    }
    
    public void setEmailMsgTxt(String emailMsgTxt) {
        this.emailMsgTxt = emailMsgTxt;
    }
    
    public void setEmailSubjectTxt(String emailSubjectTxt) {
        this.emailSubjectTxt = emailSubjectTxt;
    }
    
    public void setEmailFromAddress(String emailFromAddress) {
        this.emailFromAddress = emailFromAddress;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public void setEmailList(String[] emailList) {
        this.emailList = emailList;
    }
}



