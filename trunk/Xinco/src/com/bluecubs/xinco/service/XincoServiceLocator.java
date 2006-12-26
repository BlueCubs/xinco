/**
 * XincoServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.service; 

public class XincoServiceLocator extends org.apache.axis.client.Service implements com.bluecubs.xinco.service.XincoService {

    public XincoServiceLocator() {
    }


    public XincoServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public XincoServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Xinco
    private java.lang.String Xinco_address = "http://localhost:8084/xinco/services/Xinco";

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

    public void setXincoEndpointAddress(java.lang.String address) {
        Xinco_address = address;
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
        java.lang.String inputPortName = portName.getLocalPart();
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
            ports.add(new javax.xml.namespace.QName("urn:Xinco", "Xinco"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Xinco".equals(portName)) {
            setXincoEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
