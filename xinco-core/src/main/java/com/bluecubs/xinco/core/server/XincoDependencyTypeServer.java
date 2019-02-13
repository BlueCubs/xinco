package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.util.HashMap;
import java.util.List;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyBehaviorJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyTypeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public final class XincoDependencyTypeServer extends XincoDependencyType
{
  private static final long serialVersionUID = 6505374676088261630L;

  private HashMap parameters = new HashMap();
  private static List result;

  public XincoDependencyTypeServer()
  {
  }

  public XincoDependencyTypeServer(int id, int behaviorId, String designation) throws XincoException
  {
    XincoDependencyBehavior behavior = new XincoDependencyBehaviorJpaController(getEntityManagerFactory()).findXincoDependencyBehavior(behaviorId);
    setDesignation(designation);
    setId(id);
    if (behavior != null)
    {
      setXincoDependencyBehavior(behavior);
    }
    else
    {
      throw new XincoException();
    }
  }

  public XincoDependencyTypeServer(int behaviorId) throws XincoException
  {
    XincoDependencyType dependency = new XincoDependencyTypeJpaController(getEntityManagerFactory()).findXincoDependencyType(behaviorId);
    if (dependency != null)
    {
      setDescription(dependency.getDescription());
      setDesignation(dependency.getDesignation());
      setDesignation(dependency.getDesignation());
    }
    else
    {
      throw new XincoException();
    }
  }

  //write to db
  public int write2DB() throws XincoException
  {
    try
    {
      com.bluecubs.xinco.core.server.persistence.XincoDependencyType dep;
      boolean create = false;
      if (getId() > 0)
      {
        parameters.clear();
        parameters.put("id", getId());
        result = namedQuery("XincoDependencyType.findById", parameters);
        dep = (com.bluecubs.xinco.core.server.persistence.XincoDependencyType) result.get(0);
      }
      else
      {
        dep = new com.bluecubs.xinco.core.server.persistence.XincoDependencyType();
        create = true;
      }
      dep.setDescription(getDescription());
      dep.setDesignation(getDesignation());
      dep.setXincoDependencyBehavior(getXincoDependencyBehavior());
      if (create)
      {
        new XincoDependencyTypeJpaController(getEntityManagerFactory()).create(dep);
        setId(dep.getId());
      }
      else
      {
        new XincoDependencyTypeJpaController(getEntityManagerFactory()).edit(dep);
      }
    }
    catch (Exception e)
    {
      getLogger(XincoDependencyTypeServer.class.getSimpleName()).log(SEVERE, null, e);
      throw new XincoException(e.getMessage());
    }
    return getId();
  }

  public static int deleteFromDB(int id) throws XincoException
  {
    try
    {
      new XincoDependencyTypeJpaController(getEntityManagerFactory()).destroy(id);
    }
    catch (IllegalOrphanException | NonexistentEntityException ex)
    {
      getLogger(XincoDependencyTypeServer.class.getName()).log(SEVERE, null, ex);
      return -1;
    }
    return 0;
  }
}
