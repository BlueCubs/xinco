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

import com.bluecubs.xinco.core.server.XincoDBManager;
import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class XincoRepository implements Repository{
    private XincoDBManager DBM;
    
    public XincoRepository(){
        try {
            DBM=new XincoDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        DBM
    }
    
    public String[] getDescriptorKeys() {
    }

    public String getDescriptor(String string) {
    }

    public Session login(Credentials credentials, String string) throws LoginException, NoSuchWorkspaceException, RepositoryException {
    }

    public Session login(Credentials credentials) throws LoginException, RepositoryException {
    }

    public Session login(String string) throws LoginException, NoSuchWorkspaceException, RepositoryException {
    }

    public Session login() throws LoginException, RepositoryException {
    }
    
}