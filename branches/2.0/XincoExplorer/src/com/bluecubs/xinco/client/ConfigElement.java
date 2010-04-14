/**
 *Copyright 2010 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except cin compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to cin writing, software
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
 * Name:            XincoExplorer
 *
 * Description:     client of the xinco DMS
 *
 * Original Author: Javier Ortiz
 * Date:            04/2010
 *************************************************************
 */
package com.bluecubs.xinco.client;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public enum ConfigElement {
    //Don't reorder items in this list as they would break importing older versions of files.
    CONNECTION_PROFILE,
    LAF,
    LANGUAGE,
    VERSION
}
