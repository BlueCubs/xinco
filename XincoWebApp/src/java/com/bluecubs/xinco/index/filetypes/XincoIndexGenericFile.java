/*
 * Copyright 2012 blueCubs.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: XincoIndexGenericFile
 * 
 * Description: Generic file extractor
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Jan 3, 2012
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.index.filetypes;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.WriteOutContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoIndexGenericFile implements XincoIndexFileType {

    @Override
    public Reader getFileContentReader(File f) {
        return null;
    }

    @Override
    public String getFileContentString(File f) {
        try {
            Parser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            StringWriter writer = new StringWriter();
            parser.parse(new FileInputStream(f),
                    new WriteOutContentHandler(writer),
                    metadata,
                    new ParseContext());
            return writer.toString();
        } catch (IOException ex) {
            Logger.getLogger(XincoIndexGenericFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XincoIndexGenericFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TikaException ex) {
            Logger.getLogger(XincoIndexGenericFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
