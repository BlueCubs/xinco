package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import static java.util.logging.Level.FINE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.server.persistence.XincoId;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.persistence.PrePersist;

/**
 * Work around issue with sequencing on H2 databases on Linux
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoIdGenerator {

  private static final Logger LOG = getLogger(XincoIdGenerator.class.getSimpleName());

  /**
   * Just before persisting an entity.
   *
   * @param o object to be persisted
   * @throws Exception if something goes wrong
   */
  @PrePersist
  public void persistEntity(Object o) throws Exception {
    LOG.log(FINE, "Pre-persist: {0}", o);
    if (o instanceof XincoId) {
      XincoId xincoId = (XincoId) o;
      if (xincoId.getId() == null || xincoId.getId() <= 0) {
        LOG.log(FINE, "Detected: {0}, fixing id...", xincoId);
        HashMap<String, Object> parameters = new HashMap<>();
        int id = namedQuery("XincoId.findAll").size() + 1;
        while (xincoId.getId() <= 0) {
          parameters.put("id", id);
          if (namedQuery("XincoId.findById", parameters).isEmpty()) {
            xincoId.setId(id);
            LOG.log(FINE, "Assigned id: {0} to {1}", new Object[] {id, xincoId.getTablename()});
          } else {
            id++;
          }
        }
      }
    }
  }
}
