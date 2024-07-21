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
 * <p>Name: XincoCoreLogServerBuilder
 *
 * <p>Description: log builder
 *
 * <p>Original Author: Javier A. Ortiz Bultron: 2012
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.core.server;

import java.util.Calendar;

public class XincoCoreLogServerBuilder {
  private int attrCDID;
  private int attrUID;
  private int attrOC;
  private Calendar attrODT;
  private String attrOD;
  private int attrVH;
  private int attrVM;
  private int attrVL;
  private String attrVP;

  public XincoCoreLogServerBuilder() {}

  public XincoCoreLogServerBuilder setXincoCoreDataId(int attrCDID) {
    this.attrCDID = attrCDID;
    return this;
  }

  public XincoCoreLogServerBuilder setXincoCoreUserId(int attrUID) {
    this.attrUID = attrUID;
    return this;
  }

  public XincoCoreLogServerBuilder setOpCode(int attrOC) {
    this.attrOC = attrOC;
    return this;
  }

  public XincoCoreLogServerBuilder setOperationDate(Calendar attrODT) {
    this.attrODT = attrODT;
    return this;
  }

  public XincoCoreLogServerBuilder setOperationDescription(String attrOD) {
    this.attrOD = attrOD;
    return this;
  }

  public XincoCoreLogServerBuilder setVersionHigh(int attrVH) {
    this.attrVH = attrVH;
    return this;
  }

  public XincoCoreLogServerBuilder setVersionMid(int attrVM) {
    this.attrVM = attrVM;
    return this;
  }

  public XincoCoreLogServerBuilder setVersionLow(int attrVL) {
    this.attrVL = attrVL;
    return this;
  }

  public XincoCoreLogServerBuilder setVersionPostFix(String attrVP) {
    this.attrVP = attrVP;
    return this;
  }

  public XincoCoreLogServer createXincoCoreLogServer() {
    return new XincoCoreLogServer(
        attrCDID, attrUID, attrOC, attrODT, attrOD, attrVH, attrVM, attrVL, attrVP);
  }
}
