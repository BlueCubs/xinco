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
 * Name:            XincoRepository
 *
 * Description:     JCR compliance object
 *
 * Original Author: Javier A. Ortiz
 * Date:            May 23, 2007, 3:25 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *************************************************************
 */

package com.bluecubs.xinco.jcr;

import EDU.oswego.cs.dl.util.concurrent.ReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.WriterPreferenceReadWriteLock;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import java.util.Arrays;
import java.util.Properties;
import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import org.apache.commons.collections.ReferenceMap;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.core.SessionImpl;
import org.apache.jackrabbit.core.SessionListener;
import org.apache.jackrabbit.core.config.RepositoryConfig;

public class XincoRepository implements JackrabbitRepository, SessionListener,
        EventListener{
    private XincoDBManager DBM=null;
    private Properties props=null,repProps=null;
    private RepositoryConfig repConfig=null;
    private boolean disposed=false;
    // misc. statistics
    private long nodesCount = 0;
    private long propsCount = 0;
    // names of well-known repository properties
    public static final String STATS_NODE_COUNT_PROPERTY = "jcr.repository.stats.nodes.count";
    public static final String STATS_PROP_COUNT_PROPERTY = "jcr.repository.stats.properties.count";
    /**
     * active sessions (weak references)
     */
    private final ReferenceMap activeSessions =
            new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.WEAK);
    /**
     * Shutdown lock for guaranteeing that no new sessions are started during
     * repository shutdown and that a repository shutdown is not initiated
     * during a login. Each session login acquires a read lock while the
     * repository shutdown requires a write lock. This guarantees that there
     * can be multiple concurrent logins when the repository is not shutting
     * down, but that only a single shutdown and no concurrent logins can
     * happen simultaneously.
     */
    private final ReadWriteLock shutdownLock = new WriterPreferenceReadWriteLock();
    
    public XincoRepository(RepositoryConfig repConfig){
        this.repConfig=repConfig;
        loadSettings();
    }
    
    private XincoDBManager getXincoDBManager(){
        if(DBM==null){
            try {
                DBM=new XincoDBManager();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return DBM;
    }
    
    private void loadSettings(){
        getXincoDBManager();
        XincoSettingServer settings = DBM.getXss();
        for(int i=0;i<settings.getXinco_settings().size();i++){
            if(settings.getSetting(i).getDescription().startsWith("jrc")){
                String value="";
                if(!settings.getSetting(i).getDescription().contains("specification") &&
                        !settings.getSetting(i).getDescription().contains("repository")){
                    //Repository features
                    if(props==null)
                        props=new Properties();
                    if(settings.getSetting(i).isBool_value())
                        value="true";
                    else
                        value="false";
                    props.setProperty(settings.getSetting(i).getDescription(),value);
                }else{
                    if(props==null)
                        repProps=new Properties();
                    repProps.setProperty(settings.getSetting(i).getDescription(),
                            settings.getSetting(i).getString_value());
                }
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String[] getDescriptorKeys() {
        String[] keys = (String[]) repProps.keySet().toArray(new String[repProps.keySet().size()]);
        Arrays.sort(keys);
        return keys;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDescriptor(String key) {
        return repProps.getProperty(key);
    }
    
    /**
     * {@inheritDoc}
     */
    public Session login(Credentials credentials, String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException {
        XincoCoreUser temp = new XincoCoreUser();
        temp.setUsername(((SimpleCredentials)credentials).getUserID());
        temp.setUserpassword(((SimpleCredentials)credentials).getPassword().toString());
        return XincoLogin(temp,workspaceName);
    }
    
    public Session XincoLogin(XincoCoreUser user, String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException {
        SimpleCredentials credentials=null;
        try {
            XincoCoreUserServer temp = new XincoCoreUserServer(user.getUsername(),user.getUserpassword(),new XincoDBManager());
            credentials = new SimpleCredentials(user.getUsername(),user.getUserpassword().toCharArray());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return login(credentials, workspaceName);
    }
    
    /**
     * {@inheritDoc}
     */
    public Session login(Credentials credentials) throws LoginException, RepositoryException {
        XincoCoreUser temp = new XincoCoreUser();
        temp.setUsername(((SimpleCredentials)credentials).getUserID());
        temp.setUserpassword(((SimpleCredentials)credentials).getPassword().toString());
        return XincoLogin(temp,null);
    }
    
    /**
     * {@inheritDoc}
     */
    public Session login(String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException {
        return XincoLogin(null,workspaceName);
    }
    
    /**
     * {@inheritDoc}
     */
    public Session login() throws LoginException, RepositoryException {
        return XincoLogin(null,null);
    }
    
    /**
     * Performs a sanity check on this repository instance.
     *
     * @throws IllegalStateException if this repository has been rendered
     *                               invalid for some reason (e.g. if it has
     *                               been shut down)
     */
    protected void sanityCheck() throws IllegalStateException {
        // check repository status
        if (disposed) {
            throw new IllegalStateException("Repository instance has been shut down");
        }
    }
    
    public void shutdown() {
    }
    
    public void loggingOut(SessionImpl sessionImpl) {
    }
    
    /**
     * {@inheritDoc}
     */
    public void loggedOut(SessionImpl session) {
        synchronized (activeSessions) {
            // remove session from active sessions
            activeSessions.remove(session);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void onEvent(EventIterator events) {
        // check status of this instance
        if (disposed) {
            // ignore, repository instance has been shut down
            return;
        }

        synchronized (repProps) {
            while (events.hasNext()) {
                Event event = events.nextEvent();
                long type = event.getType();
                if ((type & Event.NODE_ADDED) == Event.NODE_ADDED) {
                    nodesCount++;
                    repProps.setProperty(STATS_NODE_COUNT_PROPERTY, Long.toString(nodesCount));
                }
                if ((type & Event.NODE_REMOVED) == Event.NODE_REMOVED) {
                    nodesCount--;
                    repProps.setProperty(STATS_NODE_COUNT_PROPERTY, Long.toString(nodesCount));
                }
                if ((type & Event.PROPERTY_ADDED) == Event.PROPERTY_ADDED) {
                    propsCount++;
                    repProps.setProperty(STATS_PROP_COUNT_PROPERTY, Long.toString(propsCount));
                }
                if ((type & Event.PROPERTY_REMOVED) == Event.PROPERTY_REMOVED) {
                    propsCount--;
                    repProps.setProperty(STATS_PROP_COUNT_PROPERTY, Long.toString(propsCount));
                }
            }
        }
    }
}