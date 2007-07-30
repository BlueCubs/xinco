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
 * -Javier A. Ortiz 02/20/2007        Add XincoSetting vector to client session
 *
 *************************************************************
 */

package com.bluecubs.xinco.client;

import java.util.Vector;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.service.*;

/**
 * XincoClientSession
 */
public class XincoClientSession {
    private XincoExplorer explorer;
    /**
     * Service Endpoint
     */
    public String service_endpoint = "";
    /**
     * User
     */
    public XincoCoreUser user = null;
    //web service
    /**
     * Xinco Service
     */
    XincoService xinco_service = null;
    /**
     * Xinco object
     */
    public Xinco xinco = null;
    //repository
    /**
     * XincoClientRepository object
     */
    public XincoClientRepository xincoClientRepository = null;
    /**
     * Server version
     */
    XincoVersion server_version = null;
    /**
     * Server groups
     */
    public Vector server_groups = null;
    /**
     * Server users
     */
    public Vector server_users = null;
    /**
     * Server settings
     */
    private Vector server_settings = null;
    /**
     * Server languages
     */
    public Vector server_languages = null;
    /**
     * Server data type
     */
    public Vector server_datatypes = null;
    /**
     * Current tree node selection
     */
    public XincoMutableTreeNode currentTreeNodeSelection = null;
    /**
     * Clipboard tree node selection
     */
    public Vector clipboardTreeNodeSelection = null;
    /**
     * Current search result
     */
    public Vector currentSearchResult = null;
    /**
     * Status
     */
    public int status = 0;	//0 = not connected
    
    /**
     * XincoClientSession
     * 1 = connecting...
     * 2 = connected
     * 3 = disconnecting
     */
    public XincoClientSession(XincoExplorer e) {
        setExplorer(e);
        service_endpoint = "";
        user = new XincoCoreUser();
        //init repository
        xincoClientRepository = new XincoClientRepository(getExplorer());
        server_version = new XincoVersion();
        server_groups = new Vector();
        server_users = new Vector();
        server_languages = new Vector();
        server_datatypes = new Vector();
        clipboardTreeNodeSelection = new Vector();
        currentSearchResult = new Vector();
        server_settings = new Vector();
        status = 0;
    }

    public XincoExplorer getExplorer() {
        return explorer;
    }

    public void setExplorer(XincoExplorer explorer) {
        this.explorer = explorer;
    }
    
}
