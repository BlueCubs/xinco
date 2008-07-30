/**
 * Xinco.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.bluecubs.xinco.service;

import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.core.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreACE;
import com.bluecubs.xinco.core.persistence.XincoCoreData;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.persistence.XincoCoreUser;

public interface Xinco extends java.rmi.Remote {

    public XincoVersion getXincoServerVersion() throws java.rmi.RemoteException;

    public XincoCoreUser getCurrentXincoCoreUser(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;

    public java.util.Vector getAllXincoCoreGroups(XincoCoreUser in0) throws java.rmi.RemoteException;

    public java.util.Vector getAllXincoCoreLanguages(XincoCoreUser in0) throws java.rmi.RemoteException;

    public java.util.Vector getAllXincoCoreDataTypes(XincoCoreUser in0) throws java.rmi.RemoteException;

    public XincoCoreNode getXincoCoreNode(XincoCoreNode in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreData getXincoCoreData(XincoCoreData in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreData doXincoCoreDataCheckout(XincoCoreData in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreData undoXincoCoreDataCheckout(XincoCoreData in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreData doXincoCoreDataCheckin(XincoCoreData in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public byte[] downloadXincoCoreData(XincoCoreData in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public int uploadXincoCoreData(XincoCoreData in0, byte[] in1, XincoCoreUser in2) throws java.rmi.RemoteException;

    public java.util.Vector findXincoCoreNodes(java.lang.String in0, XincoCoreLanguage in1, XincoCoreUser in2) throws java.rmi.RemoteException;

    public java.util.Vector findXincoCoreData(java.lang.String in0, XincoCoreLanguage in1, XincoCoreUser in2) throws java.rmi.RemoteException;

    public XincoCoreNode setXincoCoreNode(XincoCoreNode in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreData setXincoCoreData(XincoCoreData in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreACE setXincoCoreACE(XincoCoreACE in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public boolean removeXincoCoreACE(XincoCoreACE in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreLog setXincoCoreLog(XincoCoreLog in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreUser setXincoCoreUser(XincoCoreUser in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreGroup setXincoCoreGroup(XincoCoreGroup in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreLanguage setXincoCoreLanguage(XincoCoreLanguage in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoCoreDataTypeAttribute getXincoCoreDataTypeAttribute(XincoCoreDataTypeAttribute in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public XincoAddAttribute getXincoAddAttribute(XincoAddAttribute in0, XincoCoreUser in1) throws java.rmi.RemoteException;

    public boolean checkXincoCoreUserNewPassword(java.lang.String in0, XincoCoreUser in1, XincoCoreUser in2) throws java.rmi.RemoteException;
}
