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
 *
 * Name: XincoCoreLanguageServer
 *
 * Description: language
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLanguageJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import com.bluecubs.xinco.server.service.XincoCoreLanguage;

public final class XincoCoreLanguageServer extends XincoCoreLanguage
{

  private HashMap parameters = new HashMap();
  private static List result;
  //create language object for data structures

  public XincoCoreLanguageServer(int attrID) throws XincoException
  {
    try
    {
      parameters.clear();
      parameters.put("id", attrID);
      result = namedQuery("XincoCoreLanguage.findById", parameters);
      //throw exception if no result found
      if (result.size() > 0)
      {
        com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage xcl
                = (com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage) result.get(0);
        setId(xcl.getId());
        setSign(xcl.getSign());
        setDesignation(xcl.getDesignation());
      }
      else
      {
        throw new XincoException("XincoCoreLanguage with id: " + attrID + " doesn't exist!");
      }
    }
    catch (Exception ex)
    {
      getLogger(XincoCoreLanguageServer.class.getName()).log(SEVERE, null, ex);
      throw new XincoException(ex.getLocalizedMessage());
    }
  }

  //create language object for data structures
  public XincoCoreLanguageServer(int attrID, String attrS, String attrD) throws XincoException
  {
    setId(attrID);
    setSign(attrS);
    setDesignation(attrD);
  }

  public XincoCoreLanguageServer(com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage xcl)
  {
    setId(xcl.getId());
    setSign(xcl.getSign());
    setDesignation(xcl.getDesignation());
  }

  //write to db
  public int write2DB() throws XincoException
  {
    try
    {
      XincoCoreLanguageJpaController controller = new XincoCoreLanguageJpaController(getEntityManagerFactory());
      com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage xcl;
      if (getId() > 0)
      {
        xcl = controller.findXincoCoreLanguage(getId());
        xcl.setSign(getSign().replaceAll("'", "\\\\'"));
        xcl.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
        xcl.setModificationReason("audit.language.change");
        xcl.setModifierId(getChangerID());
        xcl.setModificationTime(new Timestamp(new Date().getTime()));
        controller.edit(xcl);
      }
      else
      {
        xcl = new com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage();
        xcl.setSign(getSign().replaceAll("'", "\\\\'"));
        xcl.setDesignation(getDesignation().replaceAll("'", "\\\\'"));
        xcl.setModificationReason("audit.general.create");
        xcl.setModifierId(getChangerID());
        xcl.setModificationTime(new Timestamp(new Date().getTime()));
        controller.create(xcl);
        setId(xcl.getId());
      }
    }
    catch (Exception e)
    {
      getLogger(XincoCoreLanguageServer.class.getName()).log(SEVERE, null, e);
      throw new XincoException(e.getMessage());
    }
    return getId();
  }

  //delete from db
  public static int deleteFromDB(XincoCoreLanguage attrCL, int userID) throws XincoException
  {
    try
    {
      new XincoCoreLanguageJpaController(getEntityManagerFactory()).destroy(attrCL.getId());
    }
    catch (IllegalOrphanException | NonexistentEntityException ex)
    {
      getLogger(XincoCoreLanguageServer.class.getName()).log(SEVERE, null, ex);
      throw new XincoException();
    }
    return 0;
  }

  //create complete list of languages
  public static ArrayList<XincoCoreLanguageServer> getXincoCoreLanguages()
  {
    ArrayList<XincoCoreLanguageServer> coreLanguages = new ArrayList<>();
    try
    {
      result = createdQuery("SELECT xcl FROM XincoCoreLanguage xcl ORDER BY xcl.designation");
      result.forEach((lang) ->
      {
        coreLanguages.add(new XincoCoreLanguageServer((com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage) lang));
      });
    }
    catch (XincoException ex)
    {
      getLogger(XincoCoreLanguageServer.class.getName()).log(SEVERE, null, ex);
      coreLanguages.clear();
    }
    return coreLanguages;
  }

  //check if language is in use by other objects
  public static boolean isLanguageUsed(XincoCoreLanguage xcl)
  {
    boolean is_used;
    try
    {
      is_used = ((Long) createdQuery("select count(xcn) from XincoCoreNode xcn where xcn.xincoCoreLanguage.id = " + xcl.getId()).get(0)) > 0;
      if (!is_used)
      {
        is_used = ((Long) createdQuery("select count(xcd) from XincoCoreData xcd where xcd.xincoCoreLanguage.id = " + xcl.getId()).get(0)) > 0;
      }
    }
    catch (Exception ex)
    {
      getLogger(XincoCoreLanguageServer.class.getSimpleName()).log(SEVERE, null, ex);
      is_used = true; // rather lock language in case of error!
    }
    return is_used;
  }
}
