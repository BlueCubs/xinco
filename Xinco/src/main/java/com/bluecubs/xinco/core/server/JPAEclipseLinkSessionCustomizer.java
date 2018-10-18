/**
 * Copyright 2012 blueCubs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 */
package com.bluecubs.xinco.core.server;

import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;

/**
 * See http://wiki.eclipse.org/Customizing_the_EclipseLink_Application_(ELUG)
 * Use for clients that would like to use a JTA SE pu instead of a
 * RESOURCE_LOCAL SE pu.
 */
public class JPAEclipseLinkSessionCustomizer implements SessionCustomizer {

    public static final String JNDI_DATASOURCE_NAME
            = "java:comp/env/jdbc/XincoDB";
    private static final Logger LOG
            = getLogger(JPAEclipseLinkSessionCustomizer.class.getSimpleName());

    /**
     * Get a dataSource connection and set it on the session with
     * lookupType=STRING_LOOKUP
     *
     * @param session Session to customize
     * @throws java.lang.Exception on error.
     */
    @Override
    public void customize(Session session) throws Exception {
        JNDIConnector connector;
        // Initialize session customizer
        DataSource dataSource;
        try {
            Context context = new InitialContext();
            if (null == context) {
                throw new Exception("Context is null");
            }
            connector = (JNDIConnector) session.getLogin().getConnector(); // possible CCE
            // Lookup this new dataSource
            dataSource = (DataSource) context.lookup(JNDI_DATASOURCE_NAME);
            connector.setDataSource(dataSource);

            // Set the new connection on the session
            session.getLogin().setConnector(connector);
        }
        catch (Exception e) {
            LOG.log(SEVERE, JNDI_DATASOURCE_NAME, e);
        }
    }
}
