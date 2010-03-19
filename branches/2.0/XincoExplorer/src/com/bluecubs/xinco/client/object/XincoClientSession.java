/**
 *Copyright 2009 blueCubs.com
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
import java.util.Vector;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.service.*;

/**
 * XincoClientSession
 */
public class XincoClientSession {

    /**
     * Server data type
     */
    private String serviceEndpoint = "";
    private XincoCoreUser user = null;
    private //web service
            /**
             * Xinco Service
             */
            XincoService xincoService = null;
    private Xinco xinco = null;
    private XincoClientRepository xincoClientRepository = null;
    private /**
             * Server version
             */
            XincoVersion serverVersion = null;
    private Vector serverGroups = null;
    private Vector serverLanguages = null;
    private Vector serverDatatypes = null;
    /**
     * Current tree node selection
     */
    private XincoMutableTreeNode currentTreeNodeSelection = null;
    /**
     * Status
     */
    private Vector clipboardTreeNodeSelection = null;	//0 = not connected
    private Vector currentSearchResult = null;
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

    public XincoMutableTreeNode getCurrentTreeNodeSelection() {
        return currentTreeNodeSelection;
    }

    public void setCurrentTreeNodeSelection(XincoMutableTreeNode currentTreeNodeSelection) {
        this.currentTreeNodeSelection = currentTreeNodeSelection;
    }

    /**
     * @return the service_endpoint
     */
    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    /**
     * @return the user
     */
    public XincoCoreUser getUser() {
        return user;
    }

    /**
     * @return the xinco_service
     */
    public XincoService getXincoService() {
        return xincoService;
    }

    /**
     * @return the xinco
     */
    public Xinco getXinco() {
        return xinco;
    }

    /**
     * @return the xincoClientRepository
     */
    public XincoClientRepository getXincoClientRepository() {
        return xincoClientRepository;
    }

    /**
     * @return the server_version
     */
    public XincoVersion getServerVersion() {
        return serverVersion;
    }

    /**
     * @return the server_groups
     */
    public Vector getServerGroups() {
        return serverGroups;
    }

    /**
     * @return the server_languages
     */
    public Vector getServerLanguages() {
        return serverLanguages;
    }

    /**
     * @return the server_datatypes
     */
    public Vector getServerDatatypes() {
        return serverDatatypes;
    }

    /**
     * @return the clipboardTreeNodeSelection
     */
    public Vector getClipboardTreeNodeSelection() {
        return clipboardTreeNodeSelection;
    }

    /**
     * @return the currentSearchResult
     */
    public Vector getCurrentSearchResult() {
        return currentSearchResult;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param xinco_service the xinco_service to set
     */
    public void setXincoService( //web service
            /**
             * Xinco Service
             */
            XincoService xinco_service) {
        this.xincoService = xinco_service;
    }

    /**
     * @param xinco the xinco to set
     */
    public void setXinco(Xinco xinco) {
        this.xinco = xinco;
    }

    /**
     * @param xincoClientRepository the xincoClientRepository to set
     */
    public void setXincoClientRepository(XincoClientRepository xincoClientRepository) {
        this.xincoClientRepository = xincoClientRepository;
    }

    /**
     * @param server_version the server_version to set
     */
    public void setServeVersion( /**
             * Server version
             */
            XincoVersion server_version) {
        this.setServerVersion(server_version);
    }

    /**
     * @param server_groups the server_groups to set
     */
    public void setServeGroups(Vector server_groups) {
        this.setServerGroups(server_groups);
    }

    /**
     * @param server_languages the server_languages to set
     */
    public void setServerLanguages(Vector server_languages) {
        this.serverLanguages = server_languages;
    }

    /**
     * @param clipboardTreeNodeSelection the clipboardTreeNodeSelection to set
     */
    public void setClipboardTreeNodeSelection(Vector clipboardTreeNodeSelection) {
        this.clipboardTreeNodeSelection = clipboardTreeNodeSelection;
    }

    /**
     * @param currentSearchResult the currentSearchResult to set
     */
    public void setCurrentSearchResult(Vector currentSearchResult) {
        this.currentSearchResult = currentSearchResult;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @param user the user to set
     */
    public void setUser(XincoCoreUser user) {
        this.user = user;
    }

    /**
     * @param server_datatypes the server_datatypes to set
     */
    public void setServerDatatypes(Vector server_datatypes) {
        this.serverDatatypes = server_datatypes;
    }

    /**
     * @param serviceEndpoint the serviceEndpoint to set
     */
    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    /**
     * @param serverVersion the serverVersion to set
     */
    public void setServerVersion( /**
             * Server version
             */
            XincoVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    /**
     * @param serverGroups the serverGroups to set
     */
    public void setServerGroups(Vector serverGroups) {
        this.serverGroups = serverGroups;
    }
}
