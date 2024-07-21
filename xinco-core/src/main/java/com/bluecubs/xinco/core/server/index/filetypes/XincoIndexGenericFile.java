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
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Jan 3, 2012
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.index.filetypes;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.io.*;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.WriteOutContentHandler;
import org.xml.sax.SAXException;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
public class XincoIndexGenericFile implements XincoIndexFileType {

  @Override
  public Reader getFileContentReader(File f) {
    return null;
  }

  @Override
  public String getFileContentString(File f) {
    String result;
    try {
      Parser parser = new AutoDetectParser();
      Metadata metadata = new Metadata();
      StringWriter writer = new StringWriter();
      try (FileInputStream fis = new FileInputStream(f)) {
        WriteOutContentHandler woch = new WriteOutContentHandler(writer);
        parser.parse(fis, woch, metadata, new ParseContext());
        result = writer.toString();
      }
    } catch (IOException | SAXException | TikaException ex) {
      getLogger(XincoIndexGenericFile.class.getName()).log(SEVERE, null, ex);
      result = null;
    }
    return result;
  }
}
