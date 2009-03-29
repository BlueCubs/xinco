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
import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.service.Xinco;
import com.bluecubs.xinco.service.XincoService;
import java.util.Vector;

/**
 * XincoClientSession
 */
public class XincoClientSession {

    /**
     * Service Endpoint
     */
    private String serviceEndpoint = "";
    /**
     * User
     */
    private XincoCoreUser user = null;
    //web service
    /**
     * Xinco Service
     */
    private XincoService xincoService = null;
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
    private XincoVersion serverVersion = null;
    /**
     * Server groups
     */
    private Vector serverGroups = null;
    /**
     * Server languages
     */
    private Vector serverLanguages = null;
    /**
     * Server data type
     */
    private Vector serverDatatypes = null;
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
        serviceEndpoint = "";
        user = new XincoCoreUser();
        //init repository
        xincoClientRepository = new XincoClientRepository(e);
        serverVersion = new XincoVersion();
        serverGroups = new Vector();
        serverLanguages = new Vector();
        serverDatatypes = new Vector();
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
    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    /**
     * 
     * @param serviceEndpoint
     */
    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
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
    public XincoService getXincoService() {
        return xincoService;
    }

    /**
     * 
     * @param xincoService
     */
    public void setXincoService(XincoService xincoService) {
        this.xincoService = xincoService;
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
    public XincoVersion getServerVersion() {
        return serverVersion;
    }

    /**
     * 
     * @param serverVersion
     */
    public void setServerVersion(XincoVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getServerGroups() {
        return serverGroups;
    }

    /**
     * 
     * @param serverGroups
     */
    public void setServerGroups(Vector serverGroups) {
        this.serverGroups = serverGroups;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getServerLanguages() {
        return serverLanguages;
    }

    /**
     * 
     * @param serverLanguages
     */
    public void setServerLanguages(Vector serverLanguages) {
        this.serverLanguages = serverLanguages;
    }

    /**
     * 
     * @return Vector
     */
    public Vector getServerDatatypes() {
        return serverDatatypes;
    }

    /**
     * 
     * @param serverDatatypes
     */
    public void setServerDatatypes(Vector serverDatatypes) {
        this.serverDatatypes = serverDatatypes;
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
