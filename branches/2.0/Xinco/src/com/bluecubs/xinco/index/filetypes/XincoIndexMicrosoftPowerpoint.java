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
* Name:            XincoIndexMicrosoftPowerpoint
*
* Description:     indexing Microsoft Powerpoint files 
*
* Original Author: Alexander Manes
* Date:            2005/02/06
*
* Modifications:
* 
* Who?             When?             What?
* -                -                 -
*
*************************************************************
*/

package com.bluecubs.xinco.index.filetypes;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File;
import java.io.BufferedReader;

import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

public class XincoIndexMicrosoftPowerpoint implements XincoIndexFileType {

	public XincoIndexMicrosoftPowerpoint() {
		super();
	}

	public Reader getFileContentReader(File f) {
		Reader reader = null;
		return reader;
	}

	public String getFileContentString(File f) {
        String text = null;
		try {
			POIFSReader r = new POIFSReader();
			XincoIndexMicrosoftPowerpointPOIFSReaderListener ximpprl = new XincoIndexMicrosoftPowerpointPOIFSReaderListener();
			r.registerListener(ximpprl);
	        r.read(new FileInputStream(f));
	        text = ximpprl.getEventText();
		} catch (Throwable e) {
			text = null;
		}
		return text;
	}

	static class XincoIndexMicrosoftPowerpointPOIFSReaderListener implements POIFSReaderListener {

		String EventText = "";
		
		public String getEventText() {
			return EventText;
		}
		
		public void processPOIFSReaderEvent(POIFSReaderEvent event) {
			PropertySet ps = null;
			try {
				DocumentInputStream dis = null;
				dis = event.getStream();
				Reader EventReader = new BufferedReader(new InputStreamReader(dis));
				int l = 0;
				char ca[] = new char[1024];
				while (true) {
					l = EventReader.read(ca, 0, 1024);
					if (!(l>0)) {
						break;
					}
					EventText = EventText + String.copyValueOf(ca, 0 , l);
				}
			} catch (Exception ex) {
				EventText = "";
			}
		}
	}
}
