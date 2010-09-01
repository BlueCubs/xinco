package com.bluecubs.xinco.core.server.service;

import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoException;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import com.bluecubs.xinco.server.service.XincoCoreUser;
import com.bluecubs.xinco.server.service.XincoVersion;
import javax.jws.WebService;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@WebService(serviceName = "Xinco", portName = "XincoPort", endpointInterface = "com.bluecubs.xinco.server.service.Xinco", targetNamespace = "http://service.server.xinco.bluecubs.com/", wsdlLocation = "WEB-INF/wsdl/Xinco/Xinco.wsdl")
public class Xinco {

    public com.bluecubs.xinco.server.service.XincoCoreDataType getXincoCoreDataType(com.bluecubs.xinco.server.service.XincoCoreDataType in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<com.bluecubs.xinco.server.service.XincoAddAttribute> getXincoAddAttributes(com.bluecubs.xinco.server.service.XincoCoreData in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<com.bluecubs.xinco.server.service.XincoCoreNode> getXincoCoreNodes(com.bluecubs.xinco.server.service.XincoCoreNode in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreData getXincoCoreData(com.bluecubs.xinco.server.service.XincoCoreData in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoVersion getXincoServerVersion() throws XincoException {
        //return current version of server
        XincoVersion version = new XincoVersion();
        version.setVersionHigh(XincoSettingServer.getSetting("version.high").getIntValue());
        version.setVersionMid(XincoSettingServer.getSetting("version.mid").getIntValue());
        version.setVersionLow(XincoSettingServer.getSetting("version.low").getIntValue());
        version.setVersionPostfix(XincoSettingServer.getSetting("version.postfix").getStringValue());
        return version;
    }

    public com.bluecubs.xinco.server.service.XincoCoreUser getCurrentXincoCoreUser(java.lang.String in0, java.lang.String in1) {
        //login
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(in0, in1);
            return (XincoCoreUser) user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public java.util.List<com.bluecubs.xinco.server.service.XincoCoreGroup> getAllXincoCoreGroups(com.bluecubs.xinco.server.service.XincoCoreUser in0) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<com.bluecubs.xinco.server.service.XincoCoreLanguage> getAllXincoCoreLanguages(com.bluecubs.xinco.server.service.XincoCoreUser in0) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<com.bluecubs.xinco.server.service.XincoCoreDataType> getAllXincoCoreDataTypes(com.bluecubs.xinco.server.service.XincoCoreUser in0) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreNode getXincoCoreNode(com.bluecubs.xinco.server.service.XincoCoreNode in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreData doXincoCoreDataCheckout(com.bluecubs.xinco.server.service.XincoCoreData in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreData doXincoCoreDataCheckin(com.bluecubs.xinco.server.service.XincoCoreData in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreData undoXincoCoreDataCheckout(com.bluecubs.xinco.server.service.XincoCoreData in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public byte[] downloadXincoCoreData(com.bluecubs.xinco.server.service.XincoCoreData in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int uploadXincoCoreData(com.bluecubs.xinco.server.service.XincoCoreData in0, byte[] in1, com.bluecubs.xinco.server.service.XincoCoreUser in2) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<java.lang.Object> findXincoCoreData(java.lang.String in0, com.bluecubs.xinco.server.service.XincoCoreLanguage in1, com.bluecubs.xinco.server.service.XincoCoreUser in2) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreNode setXincoCoreNode(com.bluecubs.xinco.server.service.XincoCoreNode in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreData setXincoCoreData(com.bluecubs.xinco.server.service.XincoCoreData in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreACE setXincoCoreACE(com.bluecubs.xinco.server.service.XincoCoreACE in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean removeXincoCoreACE(com.bluecubs.xinco.server.service.XincoCoreACE in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreLog setXincoCoreLog(com.bluecubs.xinco.server.service.XincoCoreLog in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreUser setXincoCoreUser(com.bluecubs.xinco.server.service.XincoCoreUser in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreGroup setXincoCoreGroup(com.bluecubs.xinco.server.service.XincoCoreGroup in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoCoreLanguage setXincoCoreLanguage(com.bluecubs.xinco.server.service.XincoCoreLanguage in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean checkXincoCoreUserNewPassword(java.lang.String in0, com.bluecubs.xinco.server.service.XincoCoreUser in1, com.bluecubs.xinco.server.service.XincoCoreUser in2) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public com.bluecubs.xinco.server.service.XincoSetting getXincoSetting(java.lang.String in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<com.bluecubs.xinco.server.service.XincoCoreDataTypeAttribute> getXincoCoreDataTypeAttribute(com.bluecubs.xinco.server.service.XincoCoreDataType in0, com.bluecubs.xinco.server.service.XincoCoreUser in1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
