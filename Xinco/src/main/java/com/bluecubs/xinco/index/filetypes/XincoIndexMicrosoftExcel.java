/**
 * Copyright 2010 blueCubs.com
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
 * <p>Name: XincoIndexMicrosoftExcel
 *
 * <p>Description: indexing Microsoft Excel files
 *
 * <p>Original Author: Alexander Manes Date: 2005/02/05
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.index.filetypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class XincoIndexMicrosoftExcel implements XincoIndexFileType {

  public XincoIndexMicrosoftExcel() {
    super();
  }

  public Reader getFileContentReader(File f) {
    return null;
  }

  public String getFileContentString(File f) {
    int i, j, j2, k;
    short k2;
    HSSFWorkbook wb = null;
    HSSFSheet sheet = null;
    HSSFRow row = null;
    HSSFCell cell = null;
    InputStream is = null;
    StringBuilder cell_string = new StringBuilder();
    try {
      is = new FileInputStream(f);
      POIFSFileSystem fs = new POIFSFileSystem(is);
      wb = new HSSFWorkbook(fs);
      for (i = 0; i < wb.getNumberOfSheets(); i++) {
        sheet = wb.getSheetAt(i);
        j2 = 0;
        for (j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
          while ((row = sheet.getRow(j2)) == null) {
            j2++;
          }
          j2++;
          k2 = 0;
          for (k = 0; k < row.getPhysicalNumberOfCells(); k++) {
            while ((cell = row.getCell(k2)) == null) {
              k2++;
            }
            k2++;
            switch (cell.getCellType()) {
              case FORMULA:
                break;
              case NUMERIC:
                cell_string.append(cell.getNumericCellValue()).append("\t");
                break;
              case STRING:
                cell_string.append(cell.getStringCellValue()).append("\t");
                break;
              default:
            }
          }
          cell_string.append("\n");
        }
        cell_string.append("\n\n\n");
      }
      is.close();
    } catch (Exception fe) {
      cell_string = null;
      if (is != null) {
        try {
          is.close();
        } catch (Exception ise) {
        }
      }
    }
    assert cell_string != null;
    return cell_string.toString();
  }
}
