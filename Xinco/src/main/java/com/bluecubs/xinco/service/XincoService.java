/**
 * XincoService.java
 *
 * <p>This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT)
 * WSDL2Java emitter.
 */
package com.bluecubs.xinco.service;

public interface XincoService extends javax.xml.rpc.Service {
  java.lang.String getXincoAddress();

  com.bluecubs.xinco.service.Xinco getXinco() throws javax.xml.rpc.ServiceException;

  com.bluecubs.xinco.service.Xinco getXinco(java.net.URL portAddress)
      throws javax.xml.rpc.ServiceException;
}
