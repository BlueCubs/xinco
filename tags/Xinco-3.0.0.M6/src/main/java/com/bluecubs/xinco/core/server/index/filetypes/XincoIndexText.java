/**
 * Copyright 2005 blueCubs.com
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
 * Name: XincoIndexText
 *
 * Description: indexing text files
 *
 * Original Author: Alexander Manes Date: 2005/02/05
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server.index.filetypes;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XincoIndexText implements XincoIndexFileType {

    private static final Logger logger =
            Logger.getLogger(XincoIndexText.class.getSimpleName());

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
        } catch (Exception fe) {
            logger.log(Level.SEVERE, null, fe);
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception re) {
                    logger.log(Level.SEVERE, null, re);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ise) {
                    logger.log(Level.SEVERE, null, ise);
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
