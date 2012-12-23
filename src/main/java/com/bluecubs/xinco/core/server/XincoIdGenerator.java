package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.XincoId;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PrePersist;

/**
 * Work around issue with sequencing on H2 databases on Linux
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoIdGenerator {

    private static final Logger LOG = Logger.getLogger(XincoIdGenerator.class.getSimpleName());

    /**
     * Just before persisting an entity.
     *
     * @param o object to be persisted
     * @throws Exception if something goes wrong
     */
    @PrePersist
    public void persistEntity(Object o) throws Exception {
        LOG.log(Level.FINE, "Pre-persist: {0}", o);
        if (o instanceof XincoId) {
            XincoId xincoId = (XincoId) o;
            if (xincoId.getId() == null || xincoId.getId() <= 0) {
                LOG.log(Level.FINE, "Detected: {0}, fixing id...", xincoId);
                HashMap<String, Object> parameters = new HashMap<String, Object>();
                int id = XincoDBManager.namedQuery("XincoId.findAll").size() + 1;
                while (xincoId.getId() <= 0) {
                    parameters.put("id", id);
                    if (XincoDBManager.namedQuery("XincoId.findById", parameters).isEmpty()) {
                        xincoId.setId(id);
                        LOG.log(Level.FINE, "Assigned id: {0} to {1}", new Object[]{id, xincoId.getTablename()});
                    } else {
                        id++;
                    }
                }
            }
        }
    }
}
