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
 * Name: XincoIndexMicrosoftWord
 *
 * Description: indexing Microsoft Word files
 *
 * Original Author: Alexander Manes Date: 2005/02/05
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.index.filetypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.textmining.extraction.TextExtractor;
import org.textmining.extraction.word.WordTextExtractorFactory;

public class XincoIndexMicrosoftWord implements XincoIndexFileType {

    private static final Logger logger =
            Logger.getLogger(XincoIndexMicrosoftWord.class.getSimpleName());

    public XincoIndexMicrosoftWord() {
        super();
    }

    @Override
    public Reader getFileContentReader(File f) {
        return null;
    }

    @Override
    public String getFileContentString(File f) {
        String word_string;
        FileInputStream is = null;
        try {
            is = new FileInputStream(f);
            WordTextExtractorFactory extractor = new WordTextExtractorFactory();
            TextExtractor textExtractor = extractor.textExtractor(is);
            word_string = textExtractor.getText();
            is.close();
        } catch (Exception fe) {
            logger.log(Level.SEVERE, null, fe);
            word_string = null;
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ise) {
                    logger.log(Level.SEVERE, null, ise);
                }
            }
        }
        return word_string;
    }
}