/**
 * XincoSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.bluecubs.xinco.service;

import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.add.holders.XincoAddAttributeHolder;
import com.bluecubs.xinco.core.XincoCoreACE;
import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.XincoSetting;
import com.bluecubs.xinco.core.XincoSettingException;
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
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;

public class XincoSoapBindingImpl implements com.bluecubs.xinco.service.Xinco {

    private XincoDBManager DBM = null;
    private String sql;
    private ResultSet rs;

    public com.bluecubs.xinco.core.XincoVersion getXincoServerVersion() throws java.rmi.RemoteException {
        try {
            DBM = new XincoDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //return current version of server
        XincoVersion version = new XincoVersion();
        version.setVersion_high(DBM.getSetting("version.high").getInt_value());
        version.setVersion_mid(DBM.getSetting("version.med").getInt_value());
        version.setVersion_low(DBM.getSetting("version.low").getInt_value());
        version.setVersion_postfix(DBM.getSetting("version.postfix").getString_value());
        try {
            DBM.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return version;
    }

    public com.bluecubs.xinco.core.XincoCoreUser getCurrentXincoCoreUser(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        //login
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreUserServer user = new XincoCoreUserServer(in0, in1, DBM);
            DBM.finalize();
            return (XincoCoreUser) user;
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public java.util.Vector getAllXincoCoreGroups(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            //check if user exists
            XincoCoreUserServer user = new XincoCoreUserServer(in0.getUsername(), in0.getUserpassword(), DBM);
            java.util.Vector v = XincoCoreGroupServer.getXincoCoreGroups(DBM);
            DBM.finalize();
            return v;
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public java.util.Vector getAllXincoCoreLanguages(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            //check if user exists
            XincoCoreUserServer user = new XincoCoreUserServer(in0.getUsername(), in0.getUserpassword(), DBM);
            java.util.Vector v = XincoCoreLanguageServer.getXincoCoreLanguages(DBM);
            DBM.finalize();
            return v;
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public java.util.Vector getAllXincoCoreDataTypes(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            //check if user exists
            XincoCoreUserServer user = new XincoCoreUserServer(in0.getUsername(), in0.getUserpassword(), DBM);
            java.util.Vector v = XincoCoreDataTypeServer.getXincoCoreDataTypes(DBM);
            DBM.finalize();
            return v;
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreNode getXincoCoreNode(com.bluecubs.xinco.core.XincoCoreNode in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            int i = 0;
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            XincoCoreNodeServer node = new XincoCoreNodeServer(in0.getId(), DBM);
            XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, node.getXinco_core_acl());
            if (ace.isRead_permission()) {
                boolean show_children = false;
                // show content of TRASH to admins ONLY!
                if (node.getId() == 2) {
                    for (i = 0; i < user.getXinco_core_groups().size(); i++) {
                        if (((XincoCoreGroup) user.getXinco_core_groups().elementAt(i)).getId() == 1) {
                            show_children = true;
                            break;
                        }
                    }
                } else {
                    show_children = true;
                }
                if (show_children) {
                    node.fillXincoCoreNodes(DBM);
                    node.fillXincoCoreData(DBM);
                }
                DBM.finalize();
                return (XincoCoreNode) node;
            } else {
                DBM.finalize();
                return null;
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreData getXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            XincoCoreDataServer data = new XincoCoreDataServer(in0.getId(), DBM);
            XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, data.getXinco_core_acl());
            if (ace.isRead_permission()) {
                DBM.finalize();
                return (XincoCoreData) data;
            } else {
                DBM.finalize();
                return null;
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreData doXincoCoreDataCheckout(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        in0.setStatus_number(4);
        return setXincoCoreData(in0, in1);
    }

    public com.bluecubs.xinco.core.XincoCoreData undoXincoCoreDataCheckout(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        in0.setStatus_number(1);
        return setXincoCoreData(in0, in1);
    }

    public com.bluecubs.xinco.core.XincoCoreData doXincoCoreDataCheckin(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        in0.setStatus_number(1);
        return setXincoCoreData(in0, in1);
    }

    public byte[] downloadXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreDataServer data;
            XincoCoreACE ace;
            byte[] byte_array = null;
            String revision = "";
            long total_len = 0;
            boolean useSAAJ = false;
            MessageContext mc = null;
            Message m = null;
            AttachmentPart ap = null;
            InputStream in = null;
            ByteArrayOutputStream out = null;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            //load data
            data = new XincoCoreDataServer(in0.getId(), DBM);
            ace = XincoCoreACEServer.checkAccess(user, data.getXinco_core_acl());
            if (ace.isRead_permission()) {
                //determine requested revision if data with only one specific log object is requested
                if ((data.getXinco_core_logs().size() > 1) && (in0.getXinco_core_logs().size() == 1)) {
                    //find id of log
                    int LogId = 0;
                    if ((((XincoCoreLog) in0.getXinco_core_logs().elementAt(0)).getOp_code() == 1) || (((XincoCoreLog) in0.getXinco_core_logs().elementAt(0)).getOp_code() == 5)) {
                        LogId = ((XincoCoreLog) in0.getXinco_core_logs().elementAt(0)).getId();
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
                in = new CheckedInputStream(new FileInputStream(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), data.getId(), data.getId() + revision)), new CRC32());
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
                    total_len = 0;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                        total_len = total_len + len;
                    }
                    in.close();
                    byte_array = out.toByteArray();
                    out.close();
                }
                DBM.finalize();
                return byte_array;
            } else {
                DBM.finalize();
                return null;
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public int uploadXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, byte[] in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreDataServer data;
            XincoCoreACE ace;
            int i = 0;
            int len = 0;
            long total_len = 0;
            boolean useSAAJ = false;
            MessageContext mc = null;
            Message m = null;
            AttachmentPart ap = null;
            InputStream in = null;
            XincoCoreUserServer user = new XincoCoreUserServer(in2.getUsername(), in2.getUserpassword(), DBM);
            //load data
            data = new XincoCoreDataServer(in0.getId(), DBM);
            ace = XincoCoreACEServer.checkAccess(user, data.getXinco_core_acl());
            if (ace.isWrite_permission()) {
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
                CheckedOutputStream out = new CheckedOutputStream(new FileOutputStream(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), data.getId(), "" + data.getId())), new CRC32());
                byte[] buf = new byte[4096];
                len = 0;
                total_len = 0;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                    total_len = total_len + len;
                }
                in.close();
                out.close();
                //dupicate file to preserve current revision
                if (((XincoAddAttribute) data.getXinco_add_attributes().elementAt(3)).getAttrib_unsignedint() == 1) {
                    //find id of latest log
                    int MaxLogId = 0;
                    for (i = 0; i < data.getXinco_core_logs().size(); i++) {
                        if ((((XincoCoreLog) data.getXinco_core_logs().elementAt(i)).getId() > MaxLogId) && ((((XincoCoreLog) data.getXinco_core_logs().elementAt(i)).getOp_code() == 1) || (((XincoCoreLog) data.getXinco_core_logs().elementAt(i)).getOp_code() == 5))) {
                            MaxLogId = ((XincoCoreLog) data.getXinco_core_logs().elementAt(i)).getId();
                        }
                    }
                    if (MaxLogId > 0) {
                        //copy file
                        FileInputStream fcis = new FileInputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), data.getId(), "" + data.getId())));
                        FileOutputStream fcos = new FileOutputStream(new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), data.getId(), data.getId() + "-" + MaxLogId)));
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
                try {
                    XincoIndexThread xit = new XincoIndexThread(data, true);
                    xit.start();
                    index_success = true;
                } catch (Exception xite) {
                    index_success = false;
                }
                //close connection if indexing thread failed
                if (!index_success) {
                    DBM.finalize();
                }
                return (int) total_len;
            } else {
                DBM.finalize();
                return 0;
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    public java.util.Vector findXincoCoreNodes(java.lang.String in0, com.bluecubs.xinco.core.XincoCoreLanguage in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException {
        /*
        try {
        XincoDBManager DBM = new XincoDBManager();
        XincoCoreUserServer user = new XincoCoreUserServer(in2.getUsername(), in2.getUserpassword(), DBM);
        java.util.Vector v = XincoCoreNodeServer.findXincoCoreNodes(in0, in1.getId(), DBM);
        return v;
        } catch (Throwable e) {
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
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreUserServer user = new XincoCoreUserServer(in2.getUsername(), in2.getUserpassword(), DBM);
            //search on database
            //java.util.Vector tv = XincoCoreDataServer.findXincoCoreData(in0, in1.getId(), true, true, DBM);
            //search on index
            java.util.Vector tv = XincoIndexer.findXincoCoreData(in0, in1.getId(), DBM);
            java.util.Vector tv2 = new java.util.Vector();
            //check immediate permissions
            for (int i = 0; i < tv.size(); i++) {
                XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, ((XincoCoreData) tv.elementAt(i)).getXinco_core_acl());
                if (ace.isRead_permission()) {
                    tv2.add(tv.elementAt(i));
                }
            }
            //check permissions on parents
            java.util.Vector tvp;
            for (int i = 0; i < tv2.size(); i++) {
                tvp = XincoCoreNodeServer.getXincoCoreNodeParents(((XincoCoreData) tv2.elementAt(i)).getXinco_core_node_id(), DBM);
                rp = true;
                for (int j = 0; j < tvp.size(); j++) {
                    XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, ((XincoCoreNode) tvp.elementAt(j)).getXinco_core_acl());
                    if (!ace.isRead_permission()) {
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
            DBM.finalize();
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return v;
    }

    public com.bluecubs.xinco.core.XincoCoreNode setXincoCoreNode(com.bluecubs.xinco.core.XincoCoreNode in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            int i = 0;
            boolean insertnewnode = false;
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreNodeServer node;
            XincoCoreNodeServer parent_node = new XincoCoreNodeServer(0, 0, 1, "", 1, DBM);
            XincoCoreACE ace;
            XincoCoreACE parent_ace = new XincoCoreACE();
            parent_ace.setWrite_permission(true);
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            if (in0.getId() <= 0) {
                //insert new node
                insertnewnode = true;
                parent_node = new XincoCoreNodeServer(in0.getXinco_core_node_id(), DBM);
                ace = XincoCoreACEServer.checkAccess(user, parent_node.getXinco_core_acl());
                node = new XincoCoreNodeServer(0, 0, 1, "", 1, DBM);
            } else {
                //update existing node
                node = new XincoCoreNodeServer(in0.getId(), DBM);
                //moving node requires write permission to target node
                if (in0.getXinco_core_node_id() != node.getXinco_core_node_id()) {
                    parent_node = new XincoCoreNodeServer(in0.getXinco_core_node_id(), DBM);
                    parent_node.setChangerID(in1.getId());
                    parent_ace = XincoCoreACEServer.checkAccess(user, parent_node.getXinco_core_acl());
                }
                ace = XincoCoreACEServer.checkAccess(user, node.getXinco_core_acl());
            }
            if ((ace.isWrite_permission()) && (parent_ace.isWrite_permission())) {
                //update information
                node.setXinco_core_node_id(in0.getXinco_core_node_id());
                node.setChangerID(in1.getId());
                node.setDesignation(in0.getDesignation());
                node.setXinco_core_language(in0.getXinco_core_language());
                node.setStatus_number(in0.getStatus_number());
                node.write2DB(DBM);
                //insert default ACL when inserting new node
                if (insertnewnode) {
                    XincoCoreACEServer newace;
                    //owner
                    newace = new XincoCoreACEServer(0, user.getId(), 0, node.getId(), 0, true, true, true, true);
                    newace.write2DB(DBM);
                    //inherit all group ACEs
                    for (i = 0; i < parent_node.getXinco_core_acl().size(); i++) {
                        newace = (XincoCoreACEServer) parent_node.getXinco_core_acl().elementAt(i);
                        if (newace.getXinco_core_group_id() > 0) {
                            newace.setId(0);
                            newace.setXinco_core_node_id(node.getId());
                            newace.write2DB(DBM);
                        }
                    }
                    //load new ACL
                    node.setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(node.getId(), "xinco_core_node_id", DBM));
                }
                DBM.finalize();
                return (XincoCoreNode) node;
            } else {
                DBM.finalize();
                return null;
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreData setXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            int i = 0;
            boolean insertnewdata = false;
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreDataServer data;
            XincoCoreNodeServer parent_node = new XincoCoreNodeServer(0, 0, 1, "", 1, DBM);
            XincoCoreACE ace;
            XincoCoreACE parent_ace = new XincoCoreACE();
            parent_ace.setWrite_permission(true);
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            if (in0.getId() <= 0) {
                //insert new node
                insertnewdata = true;
                parent_node = new XincoCoreNodeServer(in0.getXinco_core_node_id(), DBM);
                ace = XincoCoreACEServer.checkAccess(user, parent_node.getXinco_core_acl());
                data = new XincoCoreDataServer(0, 0, 1, 1, "", 1, DBM);
            } else {
                //update existing data
                data = new XincoCoreDataServer(in0.getId(), DBM);
                //moving node requires write permission to target node
                if (in0.getXinco_core_node_id() != data.getXinco_core_node_id()) {
                    parent_node = new XincoCoreNodeServer(in0.getXinco_core_node_id(), DBM);
                    parent_node.setChangerID(in1.getId());
                    parent_ace = XincoCoreACEServer.checkAccess(user, parent_node.getXinco_core_acl());
                }
                ace = XincoCoreACEServer.checkAccess(user, data.getXinco_core_acl());
            }
            if ((ace.isWrite_permission()) && (parent_ace.isWrite_permission())) {
                //update information
                data.setXinco_core_node_id(in0.getXinco_core_node_id());
                data.setChangerID(in1.getId());
                data.setDesignation(in0.getDesignation());
                data.setXinco_core_language(in0.getXinco_core_language());
                data.setXinco_core_data_type(in0.getXinco_core_data_type());
                data.setXinco_add_attributes(in0.getXinco_add_attributes());
                data.setStatus_number(in0.getStatus_number());
                data.setUser(user);
                data.write2DB(DBM);

                //index data (not on checkout, only when status = open = 1)
                if (data.getStatus_number() == 1) {
                    XincoIndexer.indexXincoCoreData(data, false, DBM);
                }

                //insert default ACL when inserting new node
                if (insertnewdata) {
                    XincoCoreACEServer newace;
                    //owner
                    newace = new XincoCoreACEServer(0, user.getId(), 0, 0, data.getId(), true, true, true, true);
                    newace.setUserId(user.getId());
                    newace.write2DB(DBM);
                    //inherit all group ACEs
                    for (i = 0; i < parent_node.getXinco_core_acl().size(); i++) {
                        newace = (XincoCoreACEServer) parent_node.getXinco_core_acl().elementAt(i);
                        if (newace.getXinco_core_group_id() > 0) {
                            newace.setId(0);
                            newace.setXinco_core_node_id(0);
                            newace.setXinco_core_data_id(data.getId());
                            newace.write2DB(DBM);
                        }
                    }
                    //load new ACL
                    data.setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(data.getId(), "xinco_core_data_id", DBM));
                }
                DBM.finalize();
                return (XincoCoreData) data;
            } else {
                DBM.finalize();
                return null;
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreACE setXincoCoreACE(com.bluecubs.xinco.core.XincoCoreACE in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreNodeServer node;
            XincoCoreDataServer data;
            XincoCoreACE ace = new XincoCoreACE();
            XincoCoreACEServer newace;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            if (in0.getXinco_core_node_id() > 0) {
                node = new XincoCoreNodeServer(in0.getXinco_core_node_id(), DBM);
                node.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, node.getXinco_core_acl());
            }
            if (in0.getXinco_core_data_id() > 0) {
                data = new XincoCoreDataServer(in0.getXinco_core_data_id(), DBM);
                data.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, data.getXinco_core_acl());
            }
            if (ace.isAdmin_permission()) {
                //load ACE or create new one
                if (in0.getId() > 0) {
                    newace = new XincoCoreACEServer(in0.getId(), DBM);
                } else {
                    newace = new XincoCoreACEServer(0, 0, 0, 0, 0, false, false, false, false);
                }
                //update ACE
                newace.setXinco_core_node_id(in0.getXinco_core_node_id());
                newace.setChangerID(in1.getId());
                newace.setXinco_core_data_id(in0.getXinco_core_data_id());
                newace.setXinco_core_user_id(in0.getXinco_core_user_id());
                newace.setXinco_core_group_id(in0.getXinco_core_group_id());
                newace.setRead_permission(in0.isRead_permission());
                newace.setWrite_permission(in0.isWrite_permission());
                newace.setExecute_permission(in0.isExecute_permission());
                newace.setAdmin_permission(in0.isAdmin_permission());
                newace.setUserId(user.getId());
                newace.write2DB(DBM);
                DBM.finalize();
                return (XincoCoreACE) newace;
            } else {
                DBM.finalize();
                return null;
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public boolean removeXincoCoreACE(com.bluecubs.xinco.core.XincoCoreACE in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreNodeServer node;
            XincoCoreDataServer data;
            XincoCoreACE ace = new XincoCoreACE();
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            if (in0.getXinco_core_node_id() > 0) {
                node = new XincoCoreNodeServer(in0.getXinco_core_node_id(), DBM);
                node.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, node.getXinco_core_acl());
            }
            if (in0.getXinco_core_data_id() > 0) {
                data = new XincoCoreDataServer(in0.getXinco_core_data_id(), DBM);
                data.setChangerID(in1.getId());
                ace = XincoCoreACEServer.checkAccess(user, data.getXinco_core_acl());
            }
            if (ace.isAdmin_permission()) {
                XincoCoreACEServer.removeFromDB(in0, DBM, user.getId());
                DBM.finalize();
                return true;
            } else {
                DBM.finalize();
                return false;
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreLog setXincoCoreLog(com.bluecubs.xinco.core.XincoCoreLog in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreLogServer log;
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            //load log or create new one
            if (in0.getId() > 0) {
                log = new XincoCoreLogServer(in0.getId(), DBM);
            } else {
                log = new XincoCoreLogServer(0, 0, 0, 0, null, "", 0, 0, 0, "");
            }
            //update log
            log.setXinco_core_data_id(in0.getXinco_core_data_id());
            log.setChangerID(in1.getId());
            log.setXinco_core_user_id(in0.getXinco_core_user_id());
            log.setOp_code(in0.getOp_code());
            log.setOp_description(in0.getOp_description());
            log.setOp_datetime(in0.getOp_datetime());
            log.setVersion(in0.getVersion());
            log.setUser(user);
            log.write2DB(DBM);
            DBM.finalize();
            return (XincoCoreLog) log;
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public com.bluecubs.xinco.core.XincoCoreUser setXincoCoreUser(com.bluecubs.xinco.core.XincoCoreUser in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            XincoCoreUserServer user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            //Update audit trail
            user.setChange(in0.isChange());
            user.setReason(in0.getReason());
            user.write2DB(DBM);
            //update user
            user.setUsername(in0.getUsername());
            user.setUserpassword(in0.getUserpassword());
            user.setFirstname(in0.getFirstname());
            user.setName(in0.getName());
            user.setEmail(in0.getEmail());
            user.setStatus_number(in0.getStatus_number());
            user.setChange(false);
            user.setAttempts(0);
            user.write2DB(DBM);
            DBM.finalize();
            return (XincoCoreUser) user;
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
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

    public com.bluecubs.xinco.add.XincoAddAttribute getXincoAddAttribute(com.bluecubs.xinco.add.XincoAddAttribute in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException {
        return null;
    }

    /*in1 is the original XincoUser, in0 is the new XincoUser with the proposed new password**/
    public boolean checkXincoCoreUserNewPassword(java.lang.String in0, com.bluecubs.xinco.core.XincoCoreUser in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException {
        XincoCoreUserServer user = null;
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
        } catch (Throwable e) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return user.isPasswordUsable(in0);
    }

    @SuppressWarnings("unchecked")
    public Vector getAllXincoUsers(XincoCoreUser in0) throws RemoteException {
        Vector temp = null;
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            sql = "select distinct id from xinco_core_user";
            rs = DBM.executeQuery(sql);
            temp = new Vector();
            while (rs.next()) {
                temp.add(new XincoCoreUserServer(rs.getInt("id"), DBM));
            }
            DBM.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

    public Vector getXincoSetting(XincoCoreUser in0) throws RemoteException {
        Vector settings = new Vector();
        try {
            DBM = new XincoDBManager();
            settings = DBM.getXincoSettingServer().getXinco_settings();
        } catch (Exception ex) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return settings;
    }

    public XincoSetting setXincoSetting(XincoSetting in0, XincoCoreUser in1) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setXincoAddAttribute(XincoAddAttributeHolder in0, XincoCoreUser in1) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean indexFiles(Vector in0, XincoCoreUser in1) throws RemoteException {
        XincoCoreUserServer user = null;
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            user = new XincoCoreUserServer(in1.getUsername(), in1.getUserpassword(), DBM);
            XincoIndexThread xit = new XincoIndexThread();
            xit.setNodes(in0);
            xit.start();
            DBM.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                    ex.printStackTrace();
                }
                return false;
            } catch (XincoSettingException ex1) {
                Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return true;
    }

    public String localizeString(String key, String locale) throws RemoteException {
        String tempS = key;
        try {
            if (DBM == null) {
                DBM = new XincoDBManager();
            }
            Locale temp = DBM.getLocale();
            DBM.setLocale(new Locale(locale));
            tempS = DBM.localizeString(key);
            DBM.setLocale(temp);
            DBM.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(XincoSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tempS;
    }
}
