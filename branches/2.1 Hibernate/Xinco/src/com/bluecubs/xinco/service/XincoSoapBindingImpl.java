/**
 * XincoSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.bluecubs.xinco.service;

import com.bluecubs.xinco.core.XincoAddAttribute;
import com.bluecubs.xinco.core.XincoCoreACE;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.core.server.XincoCoreACEServer;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreDataTypeServer;
import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import com.bluecubs.xinco.core.server.XincoCoreLanguageServer;
import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.index.XincoIndexThread;
import com.bluecubs.xinco.index.XincoIndexer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;

public class XincoSoapBindingImpl implements com.bluecubs.xinco.service.Xinco {

    public com.bluecubs.xinco.core.XincoVersion getXincoServerVersion() throws java.rmi.RemoteException {
        ResourceBundle settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
        //return current version of server
        XincoVersion version = new XincoVersion();
        version.setVersionHigh(Integer.parseInt(settings.getString("version.high")));
        version.setVersionMid(Integer.parseInt(settings.getString("version.mid")));
        version.setVersionLow(Integer.parseInt(settings.getString("version.low")));
        version.setVersionPostfix(settings.getString("version.postfix"));
        return version;
    }

    public com.bluecubs.xinco.core.XincoCoreUser getCurrentXincoCoreUser(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        //login
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(in0, in1);
            return (XincoCoreUser) (com.bluecubs.xinco.core.persistence.XincoCoreUser) user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public java.util.Vector getAllXincoCoreGroups(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException {
        try {
            //check if user exists
            new XincoCoreUserServer(in0.getUsername(), in0.getUserpassword());
            java.util.Vector v = XincoCoreGroupServer.getXincoCoreGroups();
            return v;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public java.util.Vector getAllXincoCoreLanguages(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException {
        try {
            //check if user exists
            new XincoCoreUserServer(in0.getUsername(), in0.getUserpassword());
            java.util.Vector v = XincoCoreLanguageServer.getXincoCoreLanguages();
            return v;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public java.util.Vector getAllXincoCoreDataTypes(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException {
        try {
            //check if user exists
            new XincoCoreUserServer(in0.getUsername(), in0.getUserpassword());
            java.util.Vector v = XincoCoreDataTypeServer.getXincoCoreDataTypes();

            return v;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreNode getXincoCoreNode(com.bluecubs.xinco.core.XincoCoreNode in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            int i = 0;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            XincoCoreNodeServer node = new XincoCoreNodeServer(in0.getId());
            com.bluecubs.xinco.core.persistence.XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, node.getXincoCoreACL());
            if (ace.getReadPermission()) {
                boolean showChildren = false;
                // show content of TRASH to admins ONLY!
                if (node.getId() == 2) {
                    for (i = 0; i < user.getXincoCoreGroups().size(); i++) {
                        if (((XincoCoreGroup) user.getXincoCoreGroups().elementAt(i)).getId() == 1) {
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

                return (XincoCoreNode) (com.bluecubs.xinco.core.persistence.XincoCoreNode) node;
            } else {

                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreData getXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            XincoCoreDataServer data = new XincoCoreDataServer(in0.getId());
            com.bluecubs.xinco.core.persistence.XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, data.getXincoCoreACL());
            if (ace.getReadPermission()) {
                return (XincoCoreData) (com.bluecubs.xinco.core.persistence.XincoCoreData) data;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreData doXincoCoreDataCheckout(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        in0.setStatusNumber(4);
        return setXincoCoreData(in0, in1);
    }

    public com.bluecubs.xinco.core.XincoCoreData undoXincoCoreDataCheckout(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        in0.setStatusNumber(1);
        return setXincoCoreData(in0, in1);
    }

    public com.bluecubs.xinco.core.XincoCoreData doXincoCoreDataCheckin(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        in0.setStatusNumber(1);
        return setXincoCoreData(in0, in1);
    }

    public byte[] downloadXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            XincoCoreDataServer data;
            com.bluecubs.xinco.core.persistence.XincoCoreACE ace;
            byte[] byteArray = null;
            String revision = "";
            long totalLen = 0;
            boolean useSAAJ = false;
            MessageContext mc = null;
            Message m = null;
            AttachmentPart ap = null;
            InputStream in = null;
            ByteArrayOutputStream out = null;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            //load data
            data = new XincoCoreDataServer(in0.getId());
            ace = XincoCoreACEServer.checkAccess(user, data.getXincoCoreACL());
            if (ace.getReadPermission()) {
                //determine requested revision if data with only one specific log object is requested
                if ((data.getXincoCoreLogs().size() > 1) && (data.getXincoCoreLogs().size() == 1)) {
                    //find id of log
                    int LogId = 0;
                    if ((((XincoCoreLog) data.getXincoCoreLogs().elementAt(0)).getOpCode() == 1) ||
                            (((XincoCoreLog) data.getXincoCoreLogs().elementAt(0)).getOpCode() == 5)) {
                        LogId = ((XincoCoreLog) data.getXincoCoreLogs().elementAt(0)).getId();
                    }
                    if (LogId > 0) {
                        revision = "-" + LogId;
                    }
                }

                // decide whether to read from SOAP attachments or byte array
                mc = MessageContext.getCurrentContext();
                m = mc.getRequestMessage();
                if (m.getAttachments().hasNext()) {
                    useSAAJ = true;
                } else {
                    useSAAJ = false;
                }
                in = new CheckedInputStream(new FileInputStream(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.getFileRepositoryPath(), data.getId(), data.getId() + revision)), new CRC32());
                if (useSAAJ) {
                    //attach file to SOAP message
                    m = mc.getResponseMessage();
                    ap = new AttachmentPart();
                    ap.setContent(in, "unknown/unknown");
                    m.addAttachmentPart(ap);
                } else {
                    out = new ByteArrayOutputStream();
                    byte[] buf = new byte[4096];
                    int len = 0;
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
            } else {

                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public int uploadXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, byte[] in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException {
        try {
            XincoCoreDataServer data;
            com.bluecubs.xinco.core.persistence.XincoCoreACE ace;
            int i = 0;
            int len = 0;
            long totalLen = 0;
            boolean useSAAJ = false;
            MessageContext mc = null;
            Message m = null;
            AttachmentPart ap = null;
            InputStream in = null;
            XincoCoreUserServer user = new XincoCoreUserServer(in2.getUsername(), in2.getUserpassword());
            //load data
            data = new XincoCoreDataServer(in0.getId());
            ace = XincoCoreACEServer.checkAccess(user, data.getXincoCoreACL());
            if (ace.getWritePermission()) {

                // decide whether to read from SOAP attachments or byte array
                mc = MessageContext.getCurrentContext();
                m = mc.getCurrentMessage();
                if (m.getAttachments().hasNext()) {
                    useSAAJ = true;
                } else {
                    useSAAJ = false;
                }
                if (useSAAJ) {
                    ap = (AttachmentPart) m.getAttachments().next();
                    in = (InputStream) ap.getContent();
                } else {
                    in = new ByteArrayInputStream(in1);
                }
                CheckedOutputStream out =
                        new CheckedOutputStream(new FileOutputStream(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.getFileRepositoryPath(), data.getId(), "" + data.getId())), new CRC32());
                byte[] buf = new byte[4096];
                len = 0;
                totalLen = 0;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                    totalLen = totalLen + len;
                }
                in.close();
                out.close();

                //dupicate file to preserve current revision
                if (((XincoAddAttribute) data.getXincoAddAttributes().elementAt(3)).getAttribUnsignedint() == 1) {
                    //find id of latest log
                    int MaxLogId = 0;
                    for (i = 0; i < data.getXincoCoreLogs().size(); i++) {
                        if ((((XincoCoreLog) data.getXincoCoreLogs().elementAt(i)).getId() > MaxLogId) && ((((XincoCoreLog) data.getXincoCoreLogs().elementAt(i)).getOpCode() == 1) || (((XincoCoreLog) data.getXincoCoreLogs().elementAt(i)).getOpCode() == 5))) {
                            MaxLogId = ((XincoCoreLog) data.getXincoCoreLogs().elementAt(i)).getId();
                        }
                    }
                    if (MaxLogId > 0) {
                        //copy file
                        FileInputStream fcis = new FileInputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.getFileRepositoryPath(), data.getId(), "" + data.getId())));
                        FileOutputStream fcos = new FileOutputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.getFileRepositoryPath(), data.getId(), data.getId() + "-" + MaxLogId)));
                        byte[] fcbuf = new byte[4096];
                        len = 0;
                        while ((len = fcis.read(fcbuf)) != -1) {
                            fcos.write(fcbuf, 0, len);
                        }
                        fcis.close();
                        fcos.close();
                    }
                }

                //index data and file content
                boolean index_success = false;
                //index_success = XincoIndexer.indexXincoCoreData(data, true);
                try {
                    XincoIndexThread xit = new XincoIndexThread(data, true);
                    xit.start();
                    index_success = true;
                } catch (Exception xite) {
                    index_success = false;
                }

                //close connection if indexing thread failed
                if (!index_success) {
                }
                return (int) totalLen;
            } else {

                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public java.util.Vector findXincoCoreNodes(java.lang.String in0, com.bluecubs.xinco.core.XincoCoreLanguage in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException {
        /*
        try {
        XincoDBManager DBM = new XincoDBManager();
        XincoCoreUserServer user = new XincoCoreUserServer(in2.getUsername(), in2.getUserpassword());
        java.util.Vector v = XincoCoreNodeServer.findXincoCoreNodes(in0, in1.getId());
        return v;
        } catch (Exception e) {
        return null;
        }
         */
        return null;
    }

    @SuppressWarnings(value = "unchecked")
    public java.util.Vector findXincoCoreData(java.lang.String in0, com.bluecubs.xinco.core.XincoCoreLanguage in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException {
        boolean rp = false;
        java.util.Vector v = new java.util.Vector();
        java.util.Vector v2 = new java.util.Vector();

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
            //search on database
            //java.util.Vector tv = XincoCoreDataServer.findXincoCoreData(in0, in1.getId(), true, true);
            //search on index
            java.util.Vector tv = XincoIndexer.findXincoCoreData(in0, in1.getId());
            java.util.Vector tv2 = new java.util.Vector();
            //check immediate permissions
            for (int i = 0; i < tv.size(); i++) {
                com.bluecubs.xinco.core.persistence.XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, ((XincoCoreDataServer) tv.elementAt(i)).getXincoCoreACL());
                if (ace.getReadPermission()) {
                    tv2.add(tv.elementAt(i));
                }
            }
            //check permissions on parents
            java.util.Vector tvp;
            for (int i = 0; i < tv2.size(); i++) {
                tvp = XincoCoreNodeServer.getXincoCoreNodeParents(((XincoCoreData) tv2.elementAt(i)).getXincoCoreNodeId());
                rp = true;
                for (int j = 0; j < tvp.size(); j++) {
                    com.bluecubs.xinco.core.persistence.XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, ((XincoCoreNodeServer) tvp.elementAt(j)).getXincoCoreACL());
                    if (!ace.getReadPermission()) {
                        rp = false;
                        break;
                    }
                }
                //for complete read permission, add Data + Parent Nodes to result!
                if (rp) {
                    v2 = new java.util.Vector();
                    v2.add(tv2.elementAt(i));
                    for (int j = tvp.size() - 1; j >= 0; j--) {
                        v2.add(tvp.elementAt(j));
                    }
                    v.add(v2);
                }
            }

        } catch (Exception e) {
            return null;
        }
        return v;
    }

    public com.bluecubs.xinco.core.XincoCoreNode setXincoCoreNode(com.bluecubs.xinco.core.XincoCoreNode in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            int i = 0;
            boolean insertnewnode = false;
            XincoCoreNodeServer node;
            XincoCoreNodeServer parentNode = new XincoCoreNodeServer(0, 0, 1, "", 1);
            com.bluecubs.xinco.core.persistence.XincoCoreACE ace;
            com.bluecubs.xinco.core.persistence.XincoCoreACE parentAce = new XincoCoreACE();
            parentAce.setWritePermission(true);
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            if (in0.getId() <= 0) {
                //insert new node
                insertnewnode = true;
                parentNode = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                ace = XincoCoreACEServer.checkAccess(user, parentNode.getXincoCoreACL());
                node = new XincoCoreNodeServer(0, 0, 1, "", 1);
            } else {
                //update existing node
                node = new XincoCoreNodeServer(in0.getId());
                //moving node requires write permission to target node
                if (in0.getXincoCoreNodeId() != node.getXincoCoreNodeId()) {
                    parentNode = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                    parentNode.setChangerID(in1.getId());
                    parentAce = XincoCoreACEServer.checkAccess(user, parentNode.getXincoCoreACL());
                }
                ace = XincoCoreACEServer.checkAccess(user, node.getXincoCoreACL());
            }
            if ((ace.getWritePermission()) && (parentAce.getWritePermission())) {
                //update information
                node.setXincoCoreNodeId(in0.getXincoCoreNodeId());
                node.setChangerID(in1.getId());
                node.setDesignation(in0.getDesignation());
                node.setXincoCoreLanguageId(in0.getXincoCoreLanguageId());
                node.setStatusNumber(in0.getStatusNumber());
                node.write2DB();
                //insert default ACL when inserting new node
                if (insertnewnode) {
                    XincoCoreACEServer newace;
                    //owner
                    newace = new XincoCoreACEServer(0, user.getId(), 0, node.getId(), 0, true, true, true, true);
                    newace.write2DB();
                    //inherit all group ACEs
                    for (i = 0; i < parentNode.getXincoCoreACL().size(); i++) {
                        newace = (XincoCoreACEServer) parentNode.getXincoCoreACL().elementAt(i);
                        if (newace.getXincoCoreGroupId() > 0) {
                            newace.setId(0);
                            newace.setXincoCoreNodeId(node.getId());
                            newace.write2DB();
                        }
                    }
                    //load new ACL
                    node.setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(node.getId(), "xincoCoreNodeId"));
                }

                return (XincoCoreNode) (com.bluecubs.xinco.core.persistence.XincoCoreNode) node;
            } else {

                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreData setXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            int i = 0;
            boolean insertnewdata = false;
            XincoCoreDataServer data;
            XincoCoreNodeServer parentNode = new XincoCoreNodeServer(0, 0, 1, "", 1);
            com.bluecubs.xinco.core.persistence.XincoCoreACE ace;
            com.bluecubs.xinco.core.persistence.XincoCoreACE parentAce = new XincoCoreACE();
            parentAce.setWritePermission(true);
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            if (in0.getId() <= 0) {
                //insert new node
                insertnewdata = true;
                parentNode = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                ace = XincoCoreACEServer.checkAccess(user, parentNode.getXincoCoreACL());
                data = new XincoCoreDataServer(0, 0, 1, 1, "", 1);
            } else {
                //update existing data
                data = new XincoCoreDataServer(in0.getId());
                //moving node requires write permission to target node
                if (in0.getXincoCoreNodeId() != data.getXincoCoreNodeId()) {
                    parentNode = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                    parentNode.setChangerID(in1.getId());
                    parentAce = XincoCoreACEServer.checkAccess(user, parentNode.getXincoCoreACL());
                }
                ace = XincoCoreACEServer.checkAccess(user, data.getXincoCoreACL());
            }
            if ((ace.getWritePermission()) && (parentAce.getWritePermission())) {
                //update information
                data.setXincoCoreNodeId(in0.getXincoCoreNodeId());
                data.setChangerID(in1.getId());
                data.setDesignation(in0.getDesignation());
                data.setXincoCoreLanguageId(in0.getXincoCoreLanguageId());
                data.setXincoCoreDataTypeId(in0.getXincoCoreDataTypeId());
                data.setXincoAddAttributes(data.getXincoAddAttributes());
                data.setStatusNumber(in0.getStatusNumber());
                data.setUser(user);
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
                    newace.setXincoCoreUserId(user.getId());
                    newace.write2DB();
                    //inherit all group ACEs
                    for (i = 0; i < parentNode.getXincoCoreACL().size(); i++) {
                        newace = (XincoCoreACEServer) parentNode.getXincoCoreACL().elementAt(i);
                        if (newace.getXincoCoreGroupId() > 0) {
                            newace.setId(0);
                            newace.setXincoCoreNodeId(0);
                            newace.setXincoCoreDataId(data.getId());
                            newace.write2DB();
                        }
                    }
                    //load new ACL
                    data.setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(data.getId(), "xincoCoreDataId"));
                }

                return (XincoCoreData) (com.bluecubs.xinco.core.persistence.XincoCoreData) data;
            } else {

                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreACE setXincoCoreACE(com.bluecubs.xinco.core.XincoCoreACE in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            XincoCoreNodeServer node;
            XincoCoreDataServer data;
            com.bluecubs.xinco.core.persistence.XincoCoreACE ace = new XincoCoreACE();
            XincoCoreACEServer newace;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            if (in0.getXincoCoreNodeId() > 0) {
                node = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                node.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, node.getXincoCoreACL());
            }
            if (in0.getXincoCoreDataId() > 0) {
                data = new XincoCoreDataServer(in0.getXincoCoreDataId());
                data.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, data.getXincoCoreACL());
            }
            if (ace.getAdminPermission()) {
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
                newace.setReadPermission(in0.getReadPermission());
                newace.setWritePermission(in0.getWritePermission());
                newace.setExecutePermission(in0.getExecutePermission());
                newace.setAdminPermission(in0.getAdminPermission());
                newace.setXincoCoreUserId(user.getId());
                newace.write2DB();

                return (XincoCoreACE) (com.bluecubs.xinco.core.persistence.XincoCoreACE) newace;
            } else {

                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean removeXincoCoreACE(com.bluecubs.xinco.core.XincoCoreACE in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            XincoCoreNodeServer node;
            XincoCoreDataServer data;
            com.bluecubs.xinco.core.persistence.XincoCoreACE ace = new XincoCoreACE();
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            if (in0.getXincoCoreNodeId() > 0) {
                node = new XincoCoreNodeServer(in0.getXincoCoreNodeId());
                node.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, node.getXincoCoreACL());
            }
            if (in0.getXincoCoreDataId() > 0) {
                data = new XincoCoreDataServer(in0.getXincoCoreDataId());
                data.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, data.getXincoCoreACL());
            }
            if (ace.getAdminPermission()) {
                XincoCoreACEServer.deleteFromDB(ace, user.getId());
                return true;
            } else {

                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreLog setXincoCoreLog(com.bluecubs.xinco.core.XincoCoreLog in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            XincoCoreLogServer log;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
            //load log or create new one
            if (in0.getId() > 0) {
                log = new XincoCoreLogServer(in0.getId());
            } else {
                log = new XincoCoreLogServer(0, 0, 0, 0, null, "", 0, 0, 0, "");
            }
            //update log
            log.setXincoCoreDataId(in0.getXincoCoreDataId());
            log.setXincoCoreUserId(in1.getId());
            log.setXincoCoreUserId(in0.getXincoCoreUserId());
            log.setOpCode(in0.getOpCode());
            log.setOpDescription(in0.getOpDescription());
            log.setOpDatetime(in0.getOpDatetime());
            log.setVersion(in0.getVersion());
            log.setUser(user);
            log.write2DB();

            return (XincoCoreLog) (com.bluecubs.xinco.core.persistence.XincoCoreLog) log;
        } catch (Exception e) {
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreUser setXincoCoreUser(com.bluecubs.xinco.core.XincoCoreUser in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
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

            return (XincoCoreUser) (com.bluecubs.xinco.core.persistence.XincoCoreUser) user;
        } catch (Exception e) {
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreGroup setXincoCoreGroup(com.bluecubs.xinco.core.XincoCoreGroup in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        //not to be implemented yet: advanced administration feature!
        return null;
    }

    public com.bluecubs.xinco.core.XincoCoreLanguage setXincoCoreLanguage(com.bluecubs.xinco.core.XincoCoreLanguage in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        //not to be implemented yet: advanced administration feature!
        return null;
    }

    public com.bluecubs.xinco.core.XincoCoreDataTypeAttribute getXincoCoreDataTypeAttribute(com.bluecubs.xinco.core.XincoCoreDataTypeAttribute in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        //dummy: not to be implemented!
        return null;
    }

    public XincoAddAttribute getXincoAddAttribute(XincoAddAttribute in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        return null;
    }

    /*in1 is the original XincoUser, in0 is the new XincoUser with the proposed new password**/
    public boolean checkXincoCoreUserNewPassword(java.lang.String in0, com.bluecubs.xinco.core.XincoCoreUser in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException {
        XincoCoreUserServer user = null;
        try {
            user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword());
        } catch (XincoException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user.isPasswordUsable(in0);
    }
}
