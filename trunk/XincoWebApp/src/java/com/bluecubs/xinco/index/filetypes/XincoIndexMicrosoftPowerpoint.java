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
 * Name: XincoIndexMicrosoftPowerpoint
 *
 * Description: indexing Microsoft Powerpoint files
 *
 * Original Author: Alexander Manes Date: 2005/02/06
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.index.filetypes;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

public class XincoIndexMicrosoftPowerpoint implements XincoIndexFileType {

    private static final Logger logger = Logger.getLogger(XincoIndexMicrosoftPowerpoint.class.getSimpleName());

    public XincoIndexMicrosoftPowerpoint() {
        super();
    }

    @Override
    public Reader getFileContentReader(File f) {
        Reader reader = null;
        return reader;
    }

    @Override
    public String getFileContentString(File f) {
        String text;
        try {
            POIFSReader r = new POIFSReader();
            XincoIndexMicrosoftPowerpointPOIFSReaderListener ximpprl = new XincoIndexMicrosoftPowerpointPOIFSReaderListener();
            r.registerListener(ximpprl);
            r.read(new FileInputStream(f));
            text = ximpprl.getEventText();
        } catch (Exception e) {
            text = null;
        }
        return text;
    }

    static class XincoIndexMicrosoftPowerpointPOIFSReaderListener implements POIFSReaderListener {

        String EventText = "";

        public String getEventText() {
            return EventText;
        }

        @Override
        public void processPOIFSReaderEvent(POIFSReaderEvent event) {
            try {
                DocumentInputStream dis;
                dis = event.getStream();
                Reader EventReader = new BufferedReader(new InputStreamReader(dis));
                int l;
                char ca[] = new char[1024];
                while (true) {
                    l = EventReader.read(ca, 0, 1024);
                    if (!(l > 0)) {
                        break;
                    }
                    EventText = EventText + String.copyValueOf(ca, 0, l);
                }
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
                EventText = "";
            }
        }
    }
}
