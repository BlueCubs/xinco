/*
 * Copyright 2012 blueCubs.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: XincoExceptionListener
 * 
 * Description: Interface to listen for XincoExceptions
 * 
 * Original Author: Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com> Date: Jan 3, 2012
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core;

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
public interface XincoExceptionListener {

    /**
     * Do something when a XincoException is thrown.
     * @param e Exception thrown
     */
    public void onException(XincoException e);
}