/**
 * Copyright 2012 blueCubs.com
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
 * <p>Name: XincoIndexThread
 *
 * <p>Description: handle document indexing in thread
 *
 * <p>Original Author: Alexander Manes Date: 2004/12/18
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.core.server.index;

import com.bluecubs.xinco.server.service.XincoCoreData;

/** This class starts document indexing in a separate thread */
public class XincoIndexThread extends Thread {

  private XincoCoreData d = null;
  private boolean index_content = false;

  @Override
  public void run() {
    XincoIndexer.indexXincoCoreData(d, index_content);
  }

  public XincoIndexThread(XincoCoreData d, boolean index_content) {
    this.d = d;
    this.index_content = index_content;
  }
}
