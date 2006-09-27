/**
 * XincoServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

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
 * Name:            XincoServiceLocator
 *
 * Description:     -
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

package com.bluecubs.xinco.service;

public class XincoServiceLocator extends org.apache.axis.client.Service implements com.bluecubs.xinco.service.XincoService {

    // Use to get a proxy class for Xinco
    private final java.lang.String Xinco_address = "http://localhost:8080/xinco/services/Xinco";

    public java.lang.String getXincoAddress() {
        return Xinco_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String XincoWSDDServiceName = "Xinco";

    public java.lang.String getXincoWSDDServiceName() {
        return XincoWSDDServiceName;
    }

    public void setXincoWSDDServiceName(java.lang.String name) {
        XincoWSDDServiceName = name;
    }

    public com.bluecubs.xinco.service.Xinco getXinco() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Xinco_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getXinco(endpoint);
    }

    public com.bluecubs.xinco.service.Xinco getXinco(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.bluecubs.xinco.service.XincoSoapBindingStub _stub = new com.bluecubs.xinco.service.XincoSoapBindingStub(portAddress, this);
            _stub.setPortName(getXincoWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.bluecubs.xinco.service.Xinco.class.isAssignableFrom(serviceEndpointInterface)) {
                com.bluecubs.xinco.service.XincoSoapBindingStub _stub = new com.bluecubs.xinco.service.XincoSoapBindingStub(new java.net.URL(Xinco_address), this);
                _stub.setPortName(getXincoWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("Xinco".equals(inputPortName)) {
            return getXinco();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:Xinco", "XincoService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("Xinco"));
        }
        return ports.iterator();
    }

}
