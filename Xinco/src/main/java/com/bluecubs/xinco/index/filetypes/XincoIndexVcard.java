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
 * <p>Name: XincoIndexVcard
 *
 * <p>Description: convert XincoCoreData to Lucene Documents
 *
 * <p>Original Author: Alexander Manes Date: 2004/10/31
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.index.filetypes;

import java.io.File;
import java.io.Reader;

/**
 * Class: VCardIndexer <br>
 * This Indexer reads .vcf and .vcard files as exported by various email and PIM applications (e.g.
 * Mozilla Mail, Evolution, KMail). Fields which can be indexed are:
 *
 * <ul>
 *   <li><code>name</code>: The contact's full name
 *   <li><code>title</code>: Title (e.g. 'Dr.')
 *   <li><code>nickname</code>: nickname
 *   <li><code>birthday</code>: user notes
 *   <li><code>email</code>: email addresses
 *   <li><code>phone</code>: all available phone numbers
 *   <li><code>homephone</code>: home phone number
 *   <li><code>workphone</code>: work phone number
 *   <li><code>cellphone</code>: cell phone number
 *   <li><code>categories</code>: categories as used by the creator-application
 *   <li><code>address</code>: all available addresses
 *   <li><code>homeaddress</code>: the home address
 *   <li><code>workaddress</code>: the work address
 *   <li><code>url</code>: the URL (usually an http address)
 *   <li><code>organization</code>: the organization
 * </ul>
 *
 * <br>
 * Changelog:
 *
 * <ul>
 *   <li>02.06.2005: Initial implementation (jf)
 * </ul>
 *
 * @author <a href="mailto:jf@teamskill.de">Jens Fendler </a>
 */
/** Adapted by Javier Ortiz */
public class XincoIndexVcard implements XincoIndexFileType {

  public Reader getFileContentReader(File f) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getFileContentString(File f) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
