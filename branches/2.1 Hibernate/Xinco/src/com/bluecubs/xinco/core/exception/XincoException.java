/**
 *Copyright 2004 blueCubs.com
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
 * Name:            XincoException
 *
 * Description:     custom exception
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 * 
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.exception;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoException extends Exception {

    String xinco_message = "";

    /**
     * Creates a new instance of <code>XincoException</code> without detail message.
     */
    public XincoException() {
        super();
        xinco_message = "";
    }

    /**
     * Constructs an instance of <code>XincoException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public XincoException(String msg) {
        super(msg);
    }
    
    @Override
    public String toString() {
        return xinco_message;
    }
}
