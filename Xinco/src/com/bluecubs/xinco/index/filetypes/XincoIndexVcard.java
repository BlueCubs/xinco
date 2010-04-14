/**
 *Copyright 2010 blueCubs.com
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
 * Name:            XincoIndexVcard
 *
 * Description:     convert XincoCoreData to Lucene Documents
 *
 * Original Author: Alexander Manes
 * Date:            2004/10/31
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
import java.io.Reader;
import lius.index.application.VCardIndexer;

/**
 * Class: VCardIndexer <br>
 * 
 * This Indexer reads .vcf and .vcard files as exported by various email and PIM
 * applications (e.g. Mozilla Mail, Evolution, KMail). Fields which can be
 * indexed are:
 * 
 * <ul>
 * <li><code>name</code>: The contact's full name</li>
 * <li><code>title</code>: Title (e.g. 'Dr.')</li>
 * <li><code>nickname</code>: nickname</li>
 * <li><code>birthday</code>: user notes</li>
 * <li><code>email</code>: email addresses</li>
 * <li><code>phone</code>: all available phone numbers</li>
 * <li><code>homephone</code>: home phone number</li>
 * <li><code>workphone</code>: work phone number</li>
 * <li><code>cellphone</code>: cell phone number</li>
 * <li><code>categories</code>: categories as used by the creator-application
 * </li>
 * <li><code>address</code>: all available addresses</li>
 * <li><code>homeaddress</code>: the home address</li>
 * <li><code>workaddress</code>: the work address</li>
 * <li><code>url</code>: the URL (usually an http address)</li>
 * <li><code>organization</code>: the organization</li>
 * </ul>
 * <br/>
 * 
 * Changelog:
 * <ul>
 * <li>02.06.2005: Initial implementation (jf)</li>
 * </ul>
 * 
 * @author <a href="mailto:jf@teamskill.de">Jens Fendler </a>
 */
/**
 *Adapted by Javier Ortiz
 */
public class XincoIndexVcard extends VCardIndexer implements XincoIndexFileType{

    public Reader getFileContentReader(File f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getFileContentString(File f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
