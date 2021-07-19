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
 * Name: XincoSettingFieldFactory
 *
 * Description: Custom field factory for the setting form
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Dec 19, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.vaadin.setting;

import static com.bluecubs.xinco.core.server.vaadin.Xinco.getInstance;

import com.vaadin.ui.DefaultFieldFactory;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
public class XincoSettingFieldFactory extends DefaultFieldFactory {

  /**
   * If name follows method naming conventions, convert the name to spaced upper case text. For
   * example, convert "firstName" to "First Name"
   *
   * @param propertyId property id to use
   * @return the formatted caption string
   */
  public static String createCaptionByPropertyId(Object propertyId) {
    String name = propertyId.toString();
    if (name.length() > 0 && "description".equals(name)) {
      name = getInstance().getResource().getString("general.description");
    }
    return name;
  }
}
