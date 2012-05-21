/**
 * Copyright 2011 blueCubs.com
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
 * Name: OpCode
 *
 * Description: OPCode enumeration
 *
 * Original Author: Javier A. Ortiz Date: 2009
 *
 * Modifications:
 *
 * Who? When? What?
 *
 *
 *************************************************************
 * LogDialog.java
 *
 * Created on November 22, 2006, 10:09 AM
 */
package com.bluecubs.xinco.core;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public enum OPCode {

    CREATION("datawizard.logging.creation"),
    MODIFICATION("datawizard.logging.modification"),
    CHECKOUT("datawizard.logging.checkoutchangesplanned"),
    CHECKOUT_UNDONE("datawizard.logging.checkoutundone"),
    CHECKIN("datawizard.logging.checkinchangesmade"),
    PUBLISH_COMMENT("datawizard.logging.publishcomment"),
    LOCK_COMMENT("datawizard.logging.lockcomment"),
    ARCHIVED("datawizard.logging.archivecomment"),
    COMMENT("datawizard.logging.commentcomment"),
    DATA_MOVE("datawizard.logging.move");
    private String name;

    OPCode(String name) {
        this.name = name;
    }

    public static OPCode getOPCode(int op_code) {
        OPCode[] fields = OPCode.values();
        if (op_code - 1 < fields.length && op_code - 1 >= 0) {
            return fields[op_code - 1];
        } else {
            return null;
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
