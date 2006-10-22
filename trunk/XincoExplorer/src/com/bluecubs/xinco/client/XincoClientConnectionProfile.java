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
 * Name:            XincoClientConnectionProfile
 *
 * Description:     connection profiles for client 
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

package com.bluecubs.xinco.client;

public class XincoClientConnectionProfile implements java.io.Serializable {
	
	public String profile_name = null;
	public String service_endpoint = null;
	public String username = null;
	public String password = null;
	public boolean save_password = false;
	
	public XincoClientConnectionProfile() {
		profile_name  = "";
		service_endpoint  = "";
		username  = "";
		password  = "";
		save_password = false;
	}
	
	public String toString() {
		return profile_name;
	}

}
