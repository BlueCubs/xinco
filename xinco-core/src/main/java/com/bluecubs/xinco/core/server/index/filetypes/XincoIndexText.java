/**
 * Copyright 2005 blueCubs.com
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>************************************************************ This project supports the
 * blueCubs vision of giving back to the community in exchange for free software! More information
 * on: http://www.bluecubs.org ************************************************************
 *
 * <p>Name: XincoIndexText
 *
 * <p>Description: indexing text files
 *
 * <p>Original Author: Alexander Manes Date: 2005/02/05
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.core.server.index.filetypes;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.io.*;
import java.util.logging.Logger;

public class XincoIndexText implements XincoIndexFileType {

  private static final Logger logger = getLogger(XincoIndexText.class.getSimpleName());

  public XincoIndexText() {
    super();
  }

  @Override
  public Reader getFileContentReader(File f) {
    Reader reader = null;
    FileInputStream is = null;
    try {
      is = new FileInputStream(f);
      reader = new BufferedReader(new InputStreamReader(is));
    } catch (FileNotFoundException fe) {
      logger.log(SEVERE, null, fe);
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException re) {
          logger.log(SEVERE, null, re);
        }
      }
      if (is != null) {
        try {
          is.close();
        } catch (IOException ise) {
          logger.log(SEVERE, null, ise);
        }
      }
      reader = null;
    }
    return reader;
  }

  @Override
  public String getFileContentString(File f) {
    return null;
  }
}
