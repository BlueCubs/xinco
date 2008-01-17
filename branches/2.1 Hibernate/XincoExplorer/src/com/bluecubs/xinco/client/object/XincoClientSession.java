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
package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.service.Xinco;
import java.util.Vector;


/**
 * XincoClientSession
 */
public class XincoClientSession {

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
    private int status = 0;

    /**
     * XincoClientSession
     * 1 = connecting...
     * 2 = connected
     * 3 = disconnecting
     * @param e 
     */
    public XincoClientSession(XincoExplorer e) {
        service_endpoint = "";
        user = new XincoCoreUser();
        //init repository
        xincoClientRepository = new XincoClientRepository(e);
        server_version = new XincoVersion();
        server_groups = new Vector();
        server_languages = new Vector();
        server_datatypes = new Vector();
        clipboardTreeNodeSelection = new Vector();
        currentSearchResult = new Vector();
        status = 0;
    }

    /**
     * Constructor
     */
    protected XincoClientSession() {

    }

    /**
     * 
     * @return String
     */
    public String getService_endpoint() {
        return service_endpoint;
    }

    /**
     * 
     * @param service_endpoint
     */
    public void setService_endpoint(String service_endpoint) {
        this.service_endpoint = service_endpoint;
    }

    /**
     * 
     * @return XincoCoreUser
     */
    public XincoCoreUser getUser() {
        return user;
    }

    /**
     * 
     * @param user
     */
    public void setUser(XincoCoreUser user) {
        this.user = user;
    }

    /**
     * Xinco Service
     * @return XincoService
     */
    public XincoService getXinco_service() {
        return xinco_service;
    }

    /**
     * 
     * @param xinco_service
     */
    public void setXinco_service(XincoService xinco_service) {
        this.xinco_service = xinco_service;
    }

    /**
     * 
     * @return Xinco
     */
    public Xinco getXinco() {
        return xinco;
    }

    /**
     * 
     * @param xinco
     */
    public void setXinco(Xinco xinco) {
        this.xinco = xinco;
    }

    /**
     * 
     * @return XincoClientRepository
     */
    public XincoClientRepository getXincoClientRepository() {
        return xincoClientRepository;
    }

    /**
     * 
     * @param xincoClientRepository
     */
    public void setXincoClientRepository(XincoClientRepository xincoClientRepository) {
        this.xincoClientRepository = xincoClientRepository;
    }

    /**
     * Server version
     * @return XincoVersion
     */
    public XincoVersion getServer_version() {
        return server_version;
    }

    /**
     * 
     * @param server_version
     */
    public void setServer_version(XincoVersion server_version) {
        this.server_version = server_version;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getServer_groups() {
        return server_groups;
    }

    /**
     * 
     * @param server_groups
     */
    public void setServer_groups(Vector server_groups) {
        this.server_groups = server_groups;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getServer_languages() {
        return server_languages;
    }

    /**
     * 
     * @param server_languages
     */
    public void setServer_languages(Vector server_languages) {
        this.server_languages = server_languages;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getServer_datatypes() {
        return server_datatypes;
    }

    /**
     * 
     * @param server_datatypes
     */
    public void setServer_datatypes(Vector server_datatypes) {
        this.server_datatypes = server_datatypes;
    }

    /**
     * 
     * @return XincoMutableTreeNode
     */
    public XincoMutableTreeNode getCurrentTreeNodeSelection() {
        return currentTreeNodeSelection;
    }

    /**
     * 
     * @param currentTreeNodeSelection
     */
    public void setCurrentTreeNodeSelection(XincoMutableTreeNode currentTreeNodeSelection) {
        this.currentTreeNodeSelection = currentTreeNodeSelection;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getClipboardTreeNodeSelection() {
        return clipboardTreeNodeSelection;
    }

    /**
     * 
     * @param clipboardTreeNodeSelection
     */
    public void setClipboardTreeNodeSelection(Vector clipboardTreeNodeSelection) {
        this.clipboardTreeNodeSelection = clipboardTreeNodeSelection;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getCurrentSearchResult() {
        return currentSearchResult;
    }

    /**
     * 
     * @param currentSearchResult
     */
    public void setCurrentSearchResult(Vector currentSearchResult) {
        this.currentSearchResult = currentSearchResult;
    }

    /**
     * 
     * @return int
     */
    public int getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
