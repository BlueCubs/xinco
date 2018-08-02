/**
 * Copyright 2012 blueCubs.com
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
 * Name: XincoException
 *
 * Description: custom exception
 *
 * Original Author: Alexander Manes Date: 2004
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core;

import java.util.List;

/**
 * Xinco exception
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoException extends Error {

    String xinco_message = "";

    public XincoException() {
        super();
        xinco_message = "";
    }

    public XincoException(String s) {
        super(s);
        xinco_message = s;
    }

    public XincoException(List<String> messages) {
        for (String s : messages) {
            xinco_message += s + "\n";
        }
    }

    public XincoException(Throwable cause) {
        super(cause);
        xinco_message = cause.getLocalizedMessage();
    }

    @Override
    public String toString() {
        return xinco_message;
    }
}
