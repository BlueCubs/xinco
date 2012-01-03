/*
 * Copyright 2012 blueCubs.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: XincoExceptionNotifier
 * 
 * Description: Listener that notifies admin of exceptions
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Jan 3, 2012
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoExceptionListener;
import com.bluecubs.xinco.core.server.email.XincoMailer;
import com.bluecubs.xinco.tools.Tool;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@ServiceProvider(service = XincoExceptionListener.class)
public class XincoExceptionNotifier implements XincoExceptionListener {

    public void onException(XincoException e) {
        try {
            ArrayList<String> recipients = new ArrayList<String>();
            for (Iterator<XincoCoreUserServer> it = XincoCoreGroupServer.getMembersOfGroup(1).iterator(); it.hasNext();) {
                XincoCoreUserServer admin = it.next();
                String email = admin.getEmail();
                if (Tool.isValidEmailAddress(email)) {
                    recipients.add(email);
                }
            }
            XincoMailer.postMail(recipients, "XincoException", e.toString(), "xinco.system@xinco.org");
        } catch (MessagingException ex) {
            Logger.getLogger(XincoExceptionNotifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XincoException ex) {
            Logger.getLogger(XincoExceptionNotifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
