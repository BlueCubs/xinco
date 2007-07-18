/**
 *Copyright July 16, 2007 blueCubs.com
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
 * Name:            Notify.java
 *
 * Description:     Notify
 *
 * Original Author: Javier A. Ortiz Bultrón
 * Date:            July 16, 2007, 2:29 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server.activity;

import com.bluecubs.xinco.core.server.email.Mailer;
import com.bluecubs.xinco.general.DBManager;
import com.bluecubs.xinco.workflow.server.ActivityServer;
import java.util.Vector;

public class Notify extends ActivityServer{
    private Mailer mailer;
    /** Creates a new instance of Notify */
    public Notify(int id,DBManager DBM) {
        super(id,DBM);
        mailer = new Mailer(DBM);
        Vector properties= new Vector();
    }
    
}
