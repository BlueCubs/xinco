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
 * Name:            XincoClientSession
 *
 * Description:     session on client side
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */

package com.bluecubs.xinco.client;

import java.util.Vector;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.service.*;

public class XincoClientSession {
    
    public String service_endpoint = "";
    public XincoCoreUser user = null;
    //web service
    public XincoService xinco_service = null;
    public Xinco xinco = null;
    //repository
    public XincoClientRepository xincoClientRepository = null;
    public XincoVersion server_version = null;
    public Vector server_groups = null;
    public Vector server_languages = null;
    public Vector server_datatypes = null;
    public XincoMutableTreeNode currentTreeNodeSelection = null;
    public Vector clipboardTreeNodeSelection = null;
    public Vector currentSearchResult = null;
    public int status = 0;	//0 = not connected
    //1 = connecting...
    //2 = connected
    //3 = disconnecting
    
    public XincoClientSession() {
        service_endpoint = "";
        user = new XincoCoreUser();
        //init repository
        xincoClientRepository = new XincoClientRepository();
        server_version = new XincoVersion();
        server_groups = new Vector();
        server_languages = new Vector();
        server_datatypes = new Vector();
        clipboardTreeNodeSelection = new Vector();
        currentSearchResult = new Vector();
        status = 0;
    }
    
}
