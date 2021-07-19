package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataHasDependencyJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
public final class XincoCoreDataHasDependencyServer extends XincoCoreDataHasDependency {

  private static HashMap parameters = new HashMap();
  private static List result;
  private static final long serialVersionUID = -8904380832604427983L;

  public XincoCoreDataHasDependencyServer(
      int xincoCoreDataParentId, int xincoCoreDataChildrenId, int dependencyTypeId)
      throws XincoException {
    parameters.clear();
    parameters.put("xincoCoreDataParentId", xincoCoreDataParentId);
    parameters.put("xincoCoreDataChildrenId", xincoCoreDataChildrenId);
    parameters.put("dependencyTypeId", dependencyTypeId);
    result =
        createdQuery(
            "SELECT x FROM XincoCoreDataHasDependency x WHERE "
                + "x.xincoCoreDataHasDependencyPK.xincoCoreDataParentId = :xincoCoreDataParentId "
                + "and x.xincoCoreDataHasDependencyPK.xincoCoreDataChildrenId = :xincoCoreDataChildrenId "
                + "and x.xincoCoreDataHasDependencyPK.dependencyTypeId = :dependencyTypeId",
            parameters);
    if (!result.isEmpty()) {
      // Existing one
      XincoCoreDataHasDependency xcdhd = (XincoCoreDataHasDependency) result.get(0);
      setXincoCoreData(xcdhd.getXincoCoreData());
      setXincoCoreData1(xcdhd.getXincoCoreData1());
      setXincoDependencyType(xcdhd.getXincoDependencyType());
      setXincoCoreDataHasDependencyPK(xcdhd.getXincoCoreDataHasDependencyPK());
    } else {
      throw new XincoException();
    }
  }

  public static List<XincoCoreData> getRenderings(int id) throws XincoException {
    ArrayList<XincoCoreData> renderings = new ArrayList<>();
    parameters.clear();
    parameters.put("xincoCoreDataParentId", id);
    for (Iterator<Object> it =
            namedQuery("XincoCoreDataHasDependency.findByXincoCoreDataParentId", parameters)
                .iterator();
        it.hasNext(); ) {
      XincoCoreDataHasDependency dep = (XincoCoreDataHasDependency) it.next();
      renderings.add(dep.getXincoCoreData());
    }
    return renderings;
  }

  public static boolean isRendering(int id) throws XincoException {
    parameters.clear();
    parameters.put("xincoCoreDataChildrenId", id);
    return !namedQuery("XincoCoreDataHasDependency.findByXincoCoreDataChildrenId", parameters)
        .isEmpty();
  }

  public XincoCoreDataHasDependencyServer(
      XincoCoreData parent, XincoCoreData child, XincoDependencyType type) {
    setXincoCoreData(child);
    setXincoCoreData1(parent);
    setXincoDependencyType(type);
    setXincoCoreDataHasDependencyPK(
        new XincoCoreDataHasDependencyPK(parent.getId(), child.getId(), type.getId()));
  }

  // write to db
  public int write2DB() throws XincoException {
    try {
      XincoCoreDataHasDependency xcdhd =
          new XincoCoreDataHasDependency(
              getXincoCoreData1().getId(),
              getXincoCoreData().getId(),
              getXincoDependencyType().getId());
      xcdhd.setXincoCoreData(getXincoCoreData());
      xcdhd.setXincoCoreData1(getXincoCoreData1());
      xcdhd.setXincoDependencyType(getXincoDependencyType());
      new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory()).create(xcdhd);
    } catch (PreexistingEntityException | Exception ex) {
      getLogger(XincoCoreDataHasDependencyServer.class.getName()).log(SEVERE, null, ex);
      throw new XincoException(ex.getLocalizedMessage());
    }
    return 0;
  }

  public static int deleteFromDB(
      int xincoCoreDataParentId, int xincoCoreDataChildrenId, int dependencyTypeId)
      throws XincoException {
    try {
      parameters.clear();
      parameters.put("xincoCoreDataParentId", xincoCoreDataParentId);
      parameters.put("xincoCoreDataChildrenId", xincoCoreDataChildrenId);
      parameters.put("dependencyTypeId", dependencyTypeId);
      result =
          createdQuery(
              "SELECT x FROM XincoCoreDataHasDependency x WHERE "
                  + "x.xincoCoreDataHasDependencyPK.xincoCoreDataParentId = :xincoCoreDataParentId "
                  + "and x.xincoCoreDataHasDependencyPK.xincoCoreDataChildrenId = :xincoCoreDataChildrenId "
                  + "and x.xincoCoreDataHasDependencyPK.dependencyTypeId = :dependencyTypeId",
              parameters);
      if (!result.isEmpty()) {
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory())
            .destroy(
                ((XincoCoreDataHasDependency) result.get(0)).getXincoCoreDataHasDependencyPK());
      }
    } catch (NonexistentEntityException ex) {
      getLogger(XincoCoreDataHasDependencyServer.class.getName()).log(SEVERE, null, ex);
      throw new XincoException(ex.getLocalizedMessage());
    }
    return 0;
  }

  public void deleteFromDB() {
    try {
      new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory())
          .destroy(getXincoCoreDataHasDependencyPK());
    } catch (NonexistentEntityException ex) {
      getLogger(XincoCoreDataHasDependencyServer.class.getName()).log(SEVERE, null, ex);
    }
  }
}
