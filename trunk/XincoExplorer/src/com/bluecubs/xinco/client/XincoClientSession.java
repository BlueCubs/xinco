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
    private String service_endpoint = "";
    /**
     * User
     */
    private XincoCoreUser user = null;
    //web service
    /**
     * Xinco Service
     */
    private XincoService xinco_service = null;
    /**
     * Xinco object
     */
    private Xinco xinco = null;
    //repository
    /**
     * XincoClientRepository object
     */
    private XincoClientRepository xincoClientRepository = null;
    /**
     * Server version
     */
    private XincoVersion server_version = null;
    /**
     * Server groups
     */
    private Vector server_groups = null;
    /**
     * Server users
     */
    private Vector server_users = null;
    /**
     * Server settings
     */
    private Vector server_settings = null;
    /**
     * Server languages
     */
    private Vector server_languages = null;
    /**
     * Server data type
     */
    private Vector server_datatypes = null;
    /**
     * Current tree node selection
     */
    private XincoMutableTreeNode currentTreeNodeSelection = null;
    /**
     * Clipboard tree node selection
     */
    private Vector clipboardTreeNodeSelection = null;
    /**
     * Current search result
     */
    private Vector currentSearchResult = null;
    /**
     * Status
     */
    private int status = 0;	//0 = not connected
    
    /**
     * XincoClientSession
     * 1 = connecting...
     * 2 = connected
     * 3 = disconnecting
     */
    public XincoClientSession(XincoExplorer e) {
        setService_endpoint("");
        setUser(new XincoCoreUser());
        //init repository
        setXincoClientRepository(new XincoClientRepository(getExplorer()));
        setServer_version(new XincoVersion());
        setServer_groups(new Vector());
        setServer_users(new Vector());
        setServer_languages(new Vector());
        setServer_datatypes(new Vector());
        setClipboardTreeNodeSelection(new Vector());
        setCurrentSearchResult(new Vector());
        setServer_settings(new Vector());
        setStatus(0);
        setExplorer(e);
    }
    
    public XincoClientSession() {
        setService_endpoint("");
        setUser(new XincoCoreUser());
        //init repository
        setXincoClientRepository(new XincoClientRepository(getExplorer()));
        setServer_version(new XincoVersion());
        setServer_groups(new Vector());
        setServer_users(new Vector());
        setServer_languages(new Vector());
        setServer_datatypes(new Vector());
        setClipboardTreeNodeSelection(new Vector());
        setCurrentSearchResult(new Vector());
        setServer_settings(new Vector());
        setStatus(0);
    }

    public XincoExplorer getExplorer() {
        return explorer;
    }

    public void setExplorer(XincoExplorer explorer) {
        this.explorer = explorer;
    }

    public String getService_endpoint() {
        return service_endpoint;
    }

    public void setService_endpoint(String service_endpoint) {
        this.service_endpoint = service_endpoint;
    }

    public XincoCoreUser getUser() {
        return user;
    }

    public void setUser(XincoCoreUser user) {
        this.user = user;
    }

    public XincoService getXinco_service() {
        return xinco_service;
    }

    public void setXinco_service(XincoService xinco_service) {
        this.xinco_service = xinco_service;
    }

    public Xinco getXinco() {
        return xinco;
    }

    public void setXinco(Xinco xinco) {
        this.xinco = xinco;
    }

    public XincoClientRepository getXincoClientRepository() {
        return xincoClientRepository;
    }

    public void setXincoClientRepository(XincoClientRepository xincoClientRepository) {
        this.xincoClientRepository = xincoClientRepository;
    }

    public XincoVersion getServer_version() {
        return server_version;
    }

    public void setServer_version(XincoVersion server_version) {
        this.server_version = server_version;
    }

    public Vector getServer_groups() {
        return server_groups;
    }

    public void setServer_groups(Vector server_groups) {
        this.server_groups = server_groups;
    }

    public Vector getServer_users() {
        return server_users;
    }

    public void setServer_users(Vector server_users) {
        this.server_users = server_users;
    }

    public Vector getServer_settings() {
        return server_settings;
    }

    public void setServer_settings(Vector server_settings) {
        this.server_settings = server_settings;
    }

    public Vector getServer_languages() {
        return server_languages;
    }

    public void setServer_languages(Vector server_languages) {
        this.server_languages = server_languages;
    }

    public Vector getServer_datatypes() {
        return server_datatypes;
    }

    public void setServer_datatypes(Vector server_datatypes) {
        this.server_datatypes = server_datatypes;
    }

    public XincoMutableTreeNode getCurrentTreeNodeSelection() {
        return currentTreeNodeSelection;
    }

    public void setCurrentTreeNodeSelection(XincoMutableTreeNode currentTreeNodeSelection) {
        this.currentTreeNodeSelection = currentTreeNodeSelection;
    }

    public Vector getClipboardTreeNodeSelection() {
        return clipboardTreeNodeSelection;
    }

    public void setClipboardTreeNodeSelection(Vector clipboardTreeNodeSelection) {
        this.clipboardTreeNodeSelection = clipboardTreeNodeSelection;
    }

    public Vector getCurrentSearchResult() {
        return currentSearchResult;
    }

    public void setCurrentSearchResult(Vector currentSearchResult) {
        this.currentSearchResult = currentSearchResult;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
