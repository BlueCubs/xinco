package com.bluecubs.xinco.core.server.service;

import com.bluecubs.xinco.core.OPCode;
import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.index.XincoIndexThread;
import com.bluecubs.xinco.index.XincoIndexer;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import javax.jws.WebService;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@WebService(serviceName = "Xinco", portName = "XincoPort",
endpointInterface = "com.bluecubs.xinco.core.server.service.Xinco",
targetNamespace = "http://service.server.core.xinco.bluecubs.com/",
wsdlLocation = "WEB-INF/wsdl/XincoWebService/Xinco.wsdl")
public class XincoWebService {

    public XincoCoreDataType getXincoCoreDataType(XincoCoreDataType in0, XincoCoreUser in1) {
        //dummy: not to be implemented!
        return null;
    }

    public java.util.List<XincoAddAttribute> getXincoAddAttributes(XincoCoreData in0, XincoCoreUser in1) {
        //dummy: not to be implemented!
        return null;
    }

    public java.util.List<XincoCoreNode> getXincoCoreNodes(XincoCoreNode in0, XincoCoreUser in1) {
        //dummy: not to be implemented!
        return null;
    }

    public XincoCoreData getXincoCoreData(XincoCoreData in0, XincoCoreUser in1) {
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            XincoCoreDataServer data = new XincoCoreDataServer(in0.getId());
            XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, (ArrayList) data.getXincoCoreAcl());
            if (ace.isReadPermission()) {
                return (XincoCoreData) data;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public XincoVersion getXincoServerVersion() throws XincoException {
        //return current version of server
        XincoVersion version = new XincoVersion();
        version.setVersionHigh(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.high").getIntValue());
        version.setVersionMid(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.mid").getIntValue());
        version.setVersionLow(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.low").getIntValue());
        version.setVersionPostfix(XincoSettingServer.getSetting(new XincoCoreUserServer(1), "version.postfix").getStringValue());
        return version;
    }

    public XincoCoreUser getCurrentXincoCoreUser(java.lang.String in0, java.lang.String in1) {
        //login
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(in0, in1);
            return (XincoCoreUser) user;
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public java.util.List<XincoCoreGroup> getAllXincoCoreGroups(XincoCoreUser in0) {
        ArrayList<XincoCoreGroup> list = null;
        try {
            //check if user exists
            if (XincoCoreUserServer.validCredentials(in0.getUsername(), in0.getUserpassword(), true)) {
                list = XincoCoreGroupServer.getXincoCoreGroups();
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public java.util.List<XincoCoreLanguage> getAllXincoCoreLanguages(XincoCoreUser in0) {
        ArrayList<XincoCoreLanguage> list = null;
        try {
            //check if user exists
            if (XincoCoreUserServer.validCredentials(in0.getUsername(), in0.getUserpassword(), true)) {
                list = XincoCoreLanguageServer.getXincoCoreLanguages();
            } else {
                Logger.getLogger(XincoWebService.class.getName()).log(Level.WARNING,
                        "User {0} doesn't exist or provided wrong credentials.",
                        in0.getUsername());
                Logger.getLogger(XincoWebService.class.getName()).log(Level.WARNING,
                        "password {0}",
                        in0.getUserpassword());
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public java.util.List<XincoCoreDataType> getAllXincoCoreDataTypes(XincoCoreUser in0) {
        ArrayList<XincoCoreDataType> list = null;
        try {
            //check if user exists
            if (XincoCoreUserServer.validCredentials(in0.getUsername(), in0.getUserpassword(), true)) {
                list = XincoCoreDataTypeServer.getXincoCoreDataTypes();
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public XincoCoreNode getXincoCoreNode(XincoCoreNode in0, XincoCoreUser in1) {
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            XincoCoreNodeServer node = new XincoCoreNodeServer(in0.getId());
            XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, (ArrayList) node.getXincoCoreAcl());
            if (ace.isReadPermission()) {
                boolean showChildren = false;
                // show content of TRASH to admins ONLY!
                if (node.getId() == 2) {
                    for (int i = 0; i < user.getXincoCoreGroups().size(); i++) {
                        if (((XincoCoreGroup) user.getXincoCoreGroups().get(i)).getId() == 1) {
                            showChildren = true;
                            break;
                        }
                    }
                } else {
                    showChildren = true;
                }
                if (showChildren) {
                    node.fillXincoCoreNodes();
                    node.fillXincoCoreData();
                }
                return (XincoCoreNode) node;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public XincoCoreData doXincoCoreDataCheckout(XincoCoreData in0, XincoCoreUser in1) {
        in0.setStatusNumber(4);
        return setXincoCoreData(in0, in1);
    }

    public XincoCoreData doXincoCoreDataCheckin(XincoCoreData in0, XincoCoreUser in1) {
        in0.setStatusNumber(1);
        return setXincoCoreData(in0, in1);
    }

    public XincoCoreData undoXincoCoreDataCheckout(XincoCoreData in0, XincoCoreUser in1) {
        in0.setStatusNumber(1);
        return setXincoCoreData(in0, in1);
    }

    public byte[] downloadXincoCoreData(XincoCoreData in0, XincoCoreUser in1) {
        try {
            XincoCoreDataServer data;
            XincoCoreACE ace;
            byte[] byteArray = null;
            String revision = "";
            long totalLen;
            InputStream in;
            ByteArrayOutputStream out;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            //load data
            data = new XincoCoreDataServer(in0.getId());
            ace = XincoCoreACEServer.checkAccess(user, (ArrayList) data.getXincoCoreAcl());
            if (ace.isReadPermission()) {
                //determine requested revision if data with only one specific log object is requested
                if ((data.getXincoCoreLogs().size() > 1) && (in0.getXincoCoreLogs().size() == 1)) {
                    //find id of log
                    int LogId = 0;
                    if ((((XincoCoreLog) in0.getXincoCoreLogs().get(0)).getOpCode() == OPCode.CREATION.ordinal())
                            || (((XincoCoreLog) in0.getXincoCoreLogs().get(0)).getOpCode() == OPCode.CHECKIN.ordinal())) {
                        LogId = ((XincoCoreLog) in0.getXincoCoreLogs().get(0)).getId();
                    }
                    if (LogId > 0) {
                        revision = "-" + LogId;
                    }
                }
                in = new CheckedInputStream(new FileInputStream(
                        XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath,
                        data.getId(), data.getId() + revision)), new CRC32());
                out = new ByteArrayOutputStream();
                byte[] buf = new byte[4096];
                int len;
                totalLen = 0;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                    totalLen = totalLen + len;
                }
                in.close();
                byteArray = out.toByteArray();
                out.close();
            }
            return byteArray;
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public int uploadXincoCoreData(XincoCoreData in0, byte[] in1, XincoCoreUser in2) {
        try {
            XincoCoreDataServer data;
            XincoCoreACE ace;
            int i;
            int len;
            long totalLen;
            InputStream in;
            XincoCoreUserServer user = new XincoCoreUserServer(in2.getUsername(), in2.getUserpassword());
            //load data
            data = new XincoCoreDataServer(in0.getId());
            ace = XincoCoreACEServer.checkAccess(user, (ArrayList) data.getXincoCoreAcl());
            if (ace.isWritePermission()) {
                in = new ByteArrayInputStream(in1);
                CheckedOutputStream out = new CheckedOutputStream(new FileOutputStream(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, data.getId(), "" + data.getId())), new CRC32());
                byte[] buf = new byte[4096];
                totalLen = 0;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                    totalLen = totalLen + len;
                }
                in.close();
                out.close();

                //dupicate file to preserve current revision
                if (((XincoAddAttribute) data.getXincoAddAttributes().get(3)).getAttribUnsignedint() == 1) {
                    //find id of latest log
                    int MaxLogId = 0;
                    for (i = 0; i < data.getXincoCoreLogs().size(); i++) {
                        if ((((XincoCoreLog) in0.getXincoCoreLogs().get(0)).getOpCode() == OPCode.CREATION.ordinal())
                                || (((XincoCoreLog) in0.getXincoCoreLogs().get(0)).getOpCode() == OPCode.CHECKIN.ordinal())) {
                            MaxLogId = ((XincoCoreLog) data.getXincoCoreLogs().get(i)).getId();
                        }
                    }
                    if (MaxLogId > 0) {
                        //copy file
                        FileInputStream fcis = new FileInputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, data.getId(), "" + data.getId())));
                        FileOutputStream fcos = new FileOutputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.FileRepositoryPath, data.getId(), data.getId() + "-" + MaxLogId)));
                        byte[] fcbuf = new byte[4096];
                        while ((len = fcis.read(fcbuf)) != -1) {
                            fcos.write(fcbuf, 0, len);
                        }
                        fcis.close();
                        fcos.close();
                    }
                }
                try {
                    XincoIndexThread xit = new XincoIndexThread(data, true);
                    xit.start();
                } catch (Exception xite) {
                    Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, xite);
                }
                return (int) totalLen;
            } else {
                return 0;
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    public java.util.List<java.lang.Object> findXincoCoreData(java.lang.String in0, XincoCoreLanguage in1, XincoCoreUser in2) {
        boolean rp;
        ArrayList v = new ArrayList();
        ArrayList v2;

        //check size of keyword string
        if (in0.length() < 1) {
            return null;
        }

        //truncate % from start and end of search query
        if (in0.startsWith("%") && in0.endsWith("%")) {
            in0 = in0.substring(1, in0.length() - 1);
        }

        try {
            XincoCoreUserServer user = new XincoCoreUserServer(in2.getUsername(), in2.getUserpassword());
            //search on index
            ArrayList tv = XincoIndexer.findXincoCoreData(in0, in1.getId());
            ArrayList tv2 = new ArrayList();
            //check immediate permissions
            for (int i = 0; i < tv.size(); i++) {
                XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, (ArrayList) ((XincoCoreData) tv.get(i)).getXincoCoreAcl());
                if (ace.isReadPermission()) {
                    tv2.add(tv.get(i));
                }
            }
            //check permissions on parents
            ArrayList tvp;
            for (int i = 0; i < tv2.size(); i++) {
                tvp = XincoCoreNodeServer.getXincoCoreNodeParents(((XincoCoreData) tv2.get(i)).getXincoCoreNodeId());
                rp = true;
                for (int j = 0; j < tvp.size(); j++) {
                    XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, (ArrayList) ((XincoCoreNode) tvp.get(j)).getXincoCoreAcl());
                    if (!ace.isReadPermission()) {
                        rp = false;
                        break;
                    }
                }
                //for complete read permission, add Data + Parent Nodes to result!
                if (rp) {
                    v2 = new ArrayList();
                    v2.add(tv2.get(i));
                    for (int j = tvp.size() - 1; j >= 0; j--) {
                        v2.add(tvp.get(j));
                    }
                    v.add(v2);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return v;
    }

    public XincoCoreNode setXincoCoreNode(XincoCoreNode in0, XincoCoreUser in1) {
        try {
            boolean insertnewnode = false;
            XincoCoreNodeServer node;
            XincoCoreNodeServer parentNode = new XincoCoreNodeServer(0, 0, 1, "", 1);
            XincoCoreACE ace;
            XincoCoreACE parentAce = new XincoCoreACE();
            parentAce.setWritePermission(true);
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            if (in0.getId() <= 0) {
                //insert new node
                insertnewnode = true;
                parentNode = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                ace = XincoCoreACEServer.checkAccess(user, (ArrayList) parentNode.getXincoCoreAcl());
                node = new XincoCoreNodeServer(0, 0, 1, "", 1);
            } else {
                //update existing node
                node = new XincoCoreNodeServer(in0.getId());
                //moving node requires write permission to target node
                if (in0.getXincoCoreNodeId() != node.getXincoCoreNodeId()) {
                    parentNode = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                    parentNode.setChangerID(in1.getId());
                    parentAce = XincoCoreACEServer.checkAccess(user, (ArrayList) parentNode.getXincoCoreAcl());
                }
                ace = XincoCoreACEServer.checkAccess(user, (ArrayList) node.getXincoCoreAcl());
            }
            if ((ace.isWritePermission()) && (parentAce.isWritePermission())) {
                //update information
                node.setXincoCoreNodeId(in0.getXincoCoreNodeId());
                node.setChangerID(in1.getId());
                node.setDesignation(in0.getDesignation());
                node.setXincoCoreLanguage(in0.getXincoCoreLanguage());
                node.setStatusNumber(in0.getStatusNumber());
                node.write2DB();
                //insert default ACL when inserting new node
                if (insertnewnode) {
                    XincoCoreACEServer newace;
                    //owner
                    newace = new XincoCoreACEServer(0, user.getId(), 0, node.getId(), 0, true, true, true, true);
                    newace.write2DB();
                    //inherit all group ACEs
                    for (int i = 0; i < parentNode.getXincoCoreAcl().size(); i++) {
                        newace = (XincoCoreACEServer) parentNode.getXincoCoreAcl().get(i);
                        if (newace.getXincoCoreGroupId() > 0) {
                            newace.setId(0);
                            newace.setXincoCoreNodeId(node.getId());
                            newace.write2DB();
                        }
                    }
                    //load new ACL
                    node.getXincoCoreAcl().clear();
                    node.getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(node.getId(), "xincoCoreNodeId"));
                }
                return (XincoCoreNode) node;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public XincoCoreData setXincoCoreData(XincoCoreData in0, XincoCoreUser in1) {
        try {
            boolean insertnewdata = false;
            XincoCoreDataServer data;
            XincoCoreNodeServer parentNode = new XincoCoreNodeServer(0, 0, 1, "", 1);
            XincoCoreACE ace;
            XincoCoreACE parentAce = new XincoCoreACE();
            parentAce.setWritePermission(true);
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            if (in0.getId() <= 0) {
                //insert new node
                insertnewdata = true;
                parentNode = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                ace = XincoCoreACEServer.checkAccess(user, (ArrayList) parentNode.getXincoCoreAcl());
                data = new XincoCoreDataServer(0, 0, 1, 1, "", 1);
            } else {
                //update existing data
                data = new XincoCoreDataServer(in0.getId());
                //moving node requires write permission to target node
                if (in0.getXincoCoreNodeId() != data.getXincoCoreNodeId()) {
                    parentNode = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                    parentNode.setChangerID(in1.getId());
                    parentAce = XincoCoreACEServer.checkAccess(user, (ArrayList) parentNode.getXincoCoreAcl());
                }
                ace = XincoCoreACEServer.checkAccess(user, (ArrayList) data.getXincoCoreAcl());
            }
            if ((ace.isWritePermission()) && (parentAce.isWritePermission())) {
                //update information
                data.setXincoCoreNodeId(in0.getXincoCoreNodeId());
                data.setChangerID(in1.getId());
                data.setDesignation(in0.getDesignation());
                data.setXincoCoreLanguage(in0.getXincoCoreLanguage());
                data.setXincoCoreDataType(in0.getXincoCoreDataType());
                data.getXincoAddAttributes().clear();
                data.getXincoAddAttributes().addAll(in0.getXincoAddAttributes());
                data.setStatusNumber(in0.getStatusNumber());
                data.write2DB();

                //index data (not on checkout, only when status = open = 1)
                if (data.getStatusNumber() == 1) {
                    XincoIndexer.indexXincoCoreData(data, false);
                }

                //insert default ACL when inserting new node
                if (insertnewdata) {
                    XincoCoreACEServer newace;
                    //owner
                    newace = new XincoCoreACEServer(0, user.getId(), 0, 0, data.getId(), true, true, true, true);
                    newace.write2DB();
                    //inherit all group ACEs
                    for (int i = 0; i < parentNode.getXincoCoreAcl().size(); i++) {
                        newace = (XincoCoreACEServer) parentNode.getXincoCoreAcl().get(i);
                        if (newace.getXincoCoreGroupId() > 0) {
                            newace.setId(0);
                            newace.setXincoCoreNodeId(0);
                            newace.setXincoCoreDataId(data.getId());
                            newace.write2DB();
                        }
                    }
                    //load new ACL
                    data.getXincoCoreAcl().clear();
                    data.getXincoCoreAcl().addAll(XincoCoreACEServer.getXincoCoreACL(data.getId(), "xincoCoreDataId"));
                }
                return (XincoCoreData) data;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public XincoCoreACE setXincoCoreACE(XincoCoreACE in0, XincoCoreUser in1) {
        try {
            XincoCoreNodeServer node;
            XincoCoreDataServer data;
            XincoCoreACE ace = new XincoCoreACE();
            XincoCoreACEServer newace;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            if (in0.getXincoCoreNodeId() > 0) {
                node = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                node.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, (ArrayList) node.getXincoCoreAcl());
            }
            if (in0.getXincoCoreDataId() > 0) {
                data = new XincoCoreDataServer(in0.getXincoCoreDataId());
                data.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, (ArrayList) data.getXincoCoreAcl());
            }
            if (ace.isAdminPermission()) {
                //load ACE or create new one
                if (in0.getId() > 0) {
                    newace = new XincoCoreACEServer(in0.getId());
                } else {
                    newace = new XincoCoreACEServer(0, 0, 0, 0, 0, false, false, false, false);
                }
                //update ACE
                newace.setXincoCoreNodeId(in0.getXincoCoreNodeId());
                newace.setChangerID(in1.getId());
                newace.setXincoCoreDataId(in0.getXincoCoreDataId());
                newace.setXincoCoreUserId(in0.getXincoCoreUserId());
                newace.setXincoCoreGroupId(in0.getXincoCoreGroupId());
                newace.setReadPermission(in0.isReadPermission());
                newace.setWritePermission(in0.isWritePermission());
                newace.setExecutePermission(in0.isExecutePermission());
                newace.setAdminPermission(in0.isAdminPermission());
                newace.write2DB();
                return (XincoCoreACE) newace;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public boolean removeXincoCoreACE(XincoCoreACE in0, XincoCoreUser in1) {
        try {
            XincoCoreNodeServer node;
            XincoCoreDataServer data;
            XincoCoreACE ace = new XincoCoreACE();
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            if (in0.getXincoCoreNodeId() > 0) {
                node = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                node.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, (ArrayList) node.getXincoCoreAcl());
            }
            if (in0.getXincoCoreDataId() > 0) {
                data = new XincoCoreDataServer(in0.getXincoCoreDataId());
                data.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, (ArrayList) data.getXincoCoreAcl());
            }
            if (ace.isAdminPermission()) {
                XincoCoreACEServer.removeFromDB(in0, user.getId());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public XincoCoreLog setXincoCoreLog(XincoCoreLog in0, XincoCoreUser in1) {
        try {
            XincoCoreLogServer log;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            //load log or create new one
            if (in0.getId() > 0) {
                log = new XincoCoreLogServer(in0.getId());
            } else {
                log = new XincoCoreLogServer(0, 0, 0, null, "", 0, 0, 0, "");
            }
            //update log
            log.setXincoCoreDataId(in0.getXincoCoreDataId());
            log.setChangerID(in1.getId());
            log.setXincoCoreUserId(in0.getXincoCoreUserId());
            log.setOpCode(in0.getOpCode());
            log.setOpDescription(in0.getOpDescription());
            log.setOpDatetime(in0.getOpDatetime());
            log.setVersion(in0.getVersion());
            log.setUser(user);
            log.write2DB();
            return (XincoCoreLog) log;
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public XincoCoreUser setXincoCoreUser(XincoCoreUser in0, XincoCoreUser in1) {
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            //Update audit trail
            user.setChange(in0.isChange());
            user.setReason(in0.getReason());
            user.write2DB();
            //update user
            user.setUsername(in0.getUsername());
            user.setUserpassword(in0.getUserpassword());
            user.setFirstname(in0.getFirstname());
            user.setName(in0.getName());
            user.setEmail(in0.getEmail());
            user.setStatusNumber(in0.getStatusNumber());
            user.setChange(false);
            user.setAttempts(0);
            user.write2DB();
            return (XincoCoreUser) user;
        } catch (Exception e) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public XincoCoreGroup setXincoCoreGroup(XincoCoreGroup in0, XincoCoreUser in1) {
        //not to be implemented yet: advanced administration feature!
        return null;
    }

    public XincoCoreLanguage setXincoCoreLanguage(XincoCoreLanguage in0, XincoCoreUser in1) {
        //not to be implemented yet: advanced administration feature!
        return null;
    }

    public boolean checkXincoCoreUserNewPassword(java.lang.String in0, XincoCoreUser in1, XincoCoreUser in2) {
        try {
            return XincoCoreUserServer.validCredentials(in1.getUsername(), in1.getUserpassword(), true) ? 
                    new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword()).isPasswordUsable(in0) : false;
        } catch (XincoException ex) {
            Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public XincoSetting getXincoSetting(java.lang.String in0, XincoCoreUser in1) {
        XincoSetting setting = null;
        if (XincoCoreUserServer.validCredentials(in1.getUsername(), in1.getUserpassword(), true)) {
            try {
                setting = XincoSettingServer.getSetting(
                        new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword()), in0);
            } catch (XincoException ex) {
                Logger.getLogger(XincoWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return setting;
    }

    //TODO: Add a set setting method
    public java.util.List<XincoCoreDataTypeAttribute> getXincoCoreDataTypeAttribute(XincoCoreDataType in0, XincoCoreUser in1) {
        //dummy: not to be implemented!
        return null;
    }
}
