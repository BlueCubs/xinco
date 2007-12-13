/**
 *Copyright 2005 blueCubs.com
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
 * Name:            XincoIndexMicrosoftWord
 *
 * Description:     indexing Microsoft Word files 
 *
 * Original Author: Alexander Manes
 * Date:            2005/02/05
 *
 * Modifications:
 * 
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.index.filetypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import org.textmining.text.extraction.WordExtractor;

/**
 * 
 * @author Alexander Manes
 */
public class XincoIndexMicrosoftWord implements XincoIndexFileType {

    public XincoIndexMicrosoftWord() {
        super();
    }

    /**
     * @link XincoIndexFileType#getFileContentReader
     */
    public Reader getFileContentReader(File f) {
        return null;
    }

    /**
     * @link XincoIndexFileType#getFileContentString
     */
    public String getFileContentString(File f) {
        String word_string = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(f);
            WordExtractor extractor = new WordExtractor();
            word_string = extractor.extractText(is);
            is.close();
        } catch (Exception fe) {
            word_string = null;
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ise) {
                }
            }
        }
        return word_string;
    }
}
