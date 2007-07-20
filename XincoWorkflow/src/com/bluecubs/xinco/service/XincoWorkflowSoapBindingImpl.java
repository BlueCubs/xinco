/**
 * WorkflowSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.core.server.email.Mailer;
import com.bluecubs.xinco.workflow.Email;
import com.bluecubs.xinco.workflow.InstanceProperty;
import com.bluecubs.xinco.workflow.Resource;
import com.bluecubs.xinco.workflow.WorkflowSetting;
import com.bluecubs.xinco.workflow.WorkflowVersion;
import com.bluecubs.xinco.workflow.WorkflowException;
import com.bluecubs.xinco.workflow.holders.InstancePropertyHolder;
import com.bluecubs.xinco.workflow.server.ActivityServer;
import com.bluecubs.xinco.workflow.server.NodeServer;
import com.bluecubs.xinco.workflow.server.PropertyServer;
import com.bluecubs.xinco.workflow.server.ResourceServer;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.mail.MessagingException;

public class XincoWorkflowSoapBindingImpl implements com.bluecubs.xinco.service.XincoWorkflow{
    private WorkflowDBManager DBM;
    public com.bluecubs.xinco.workflow.Activity getActivity(com.bluecubs.xinco.workflow.Activity in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
//        try {
//            ResourceServer r = new ResourceServer(resource.getUsername(),resource.getUserpassword(),new WorkflowDBManager());
//        } catch (WorkflowException ex) {
//            ex.printStackTrace();
//            return null;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        ActivityServer a=null;
//        try {
//            a = new ActivityServer(in.getId(), new WorkflowDBManager());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return (com.bluecubs.xinco.core.workflow.Activity)a;
        return null;
    }
    
    public void setActivity(com.bluecubs.xinco.workflow.holders.ActivityHolder activity, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
    }
    
    public com.bluecubs.xinco.workflow.Node getNode(com.bluecubs.xinco.workflow.Node in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        try {
            ResourceServer r = new ResourceServer(resource.getUsername(),resource.getUserpassword(),new WorkflowDBManager());
        } catch (WorkflowException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        NodeServer n=null;
        try {
            n = new NodeServer(in.getId(), new WorkflowDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return n;
    }
    
    public com.bluecubs.xinco.workflow.Property getProperty(com.bluecubs.xinco.workflow.Property in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        try {
            ResourceServer r = new ResourceServer(resource.getUsername(),resource.getUserpassword(),new WorkflowDBManager());
        } catch (WorkflowException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        PropertyServer p=null;
        try {
            p = new PropertyServer(in.getId(), new WorkflowDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p;
    }
    
    public void setProperty(com.bluecubs.xinco.workflow.holders.PropertyHolder property, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
    }
    
    public com.bluecubs.xinco.workflow.Resource getResource(com.bluecubs.xinco.workflow.Resource resourceReqested, com.bluecubs.xinco.workflow.Resource resourceRequesting) throws java.rmi.RemoteException {
        return null;
    }
    
    public void setResource(com.bluecubs.xinco.workflow.holders.ResourceHolder resource, com.bluecubs.xinco.workflow.Resource resourceRequesting) throws java.rmi.RemoteException {
    }
    
    public com.bluecubs.xinco.workflow.Transaction getTransaction(com.bluecubs.xinco.workflow.Transaction in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        return null;
    }
    
    public void setTransaction(com.bluecubs.xinco.workflow.holders.TransactionHolder transaction, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
    }
    
    public com.bluecubs.xinco.workflow.WorkflowInstance getWorkflowInstance(com.bluecubs.xinco.workflow.WorkflowInstance in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        return null;
    }
    
    public void setWorkflowInstance(com.bluecubs.xinco.workflow.holders.WorkflowInstanceHolder instance, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
    }
    
    public com.bluecubs.xinco.workflow.WorkflowTemplate getWorkflowTemplate(com.bluecubs.xinco.workflow.WorkflowTemplate in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        return null;
    }
    
    public com.bluecubs.xinco.workflow.Node setNode(com.bluecubs.xinco.workflow.Node in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        return null;
    }
    
    public WorkflowSetting setWorkflowSetting(WorkflowSetting in0, Resource in1) throws RemoteException {
        return null;
    }
    
    public Vector getWorkflowSetting(Resource in0) throws RemoteException {
        Vector setting=null;
        try {
            setting = new WorkflowDBManager().getWorkflowSettingServer().getWorkflow_settings();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return setting;
    }

    public WorkflowVersion getWorkflowServerVersion() throws RemoteException {
        try {
            DBM=new WorkflowDBManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //return current version of server
        WorkflowVersion version = new WorkflowVersion();
        version.setVersion_high(DBM.getWorkflowSettingServer().getSetting("version.high").getInt_value());
        version.setVersion_mid(DBM.getWorkflowSettingServer().getSetting("version.med").getInt_value());
        version.setVersion_low(DBM.getWorkflowSettingServer().getSetting("version.low").getInt_value());
        version.setVersion_postfix(DBM.getWorkflowSettingServer().getSetting("version.postfix").getString_value());
        return version;
    }
    
    public void getWorkflowLog(com.bluecubs.xinco.workflow.holders.WorkflowLogHolder workflow_log, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException{
        
    }
    public void setWorkflowLog(com.bluecubs.xinco.workflow.holders.WorkflowLogHolder workflow_log, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException{
        
    }

    public InstanceProperty getWorkflowInstanceProperty(InstanceProperty instanceProperty, Resource resource) throws RemoteException {
        return null;
    }

    public void setWorkflowInstanceProperty(InstancePropertyHolder property, Resource resource) throws RemoteException {
    }

    public boolean sendEmail(Email email, Resource from) throws RemoteException {
        Mailer mailer=null;
        try {
            mailer = new Mailer(new WorkflowDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String [] rec=new String[email.getRecipients().size()];
        for(int i=0;i<email.getRecipients().size();i++)
            rec[i]=((Resource)email.getRecipients().get(i)).getEmail();
        try {
            mailer.postMail(rec,email.getSubject(),email.getMessage(),from.getEmail());
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public Email getEmail(int id, Resource user) throws RemoteException {
        return null;
    }
}