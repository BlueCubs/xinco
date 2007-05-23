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

import com.bluecubs.xinco.core.XincoEmail;
import com.bluecubs.xinco.core.server.XincoDBManager;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class XincoMailer extends XincoEmail{
    private XincoDBManager DBM;
    private String host_name = "";
    private String User = "";
    private String Password  = "";
    
    private String emailMsgTxt      = "";
    private String emailSubjectTxt  = "";
    private String emailFromAddress = "";
    private String port = "";
   
    // Add List of Email address to who email needs to be sent to
    private String[] emailList = {""};
    
    public XincoMailer(){
        try {
            DBM=new XincoDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        
        props.put("mail.smtp.debug", "true"); // if the user wants
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
        System.exit(0);
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

    public void setDBM(XincoDBManager DBM) {
        this.DBM = DBM;
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



