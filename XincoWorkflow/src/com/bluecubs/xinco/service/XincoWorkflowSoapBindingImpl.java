/**
 * XincoWorkflowSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.XincoWorkflowException;
import com.bluecubs.xinco.workflow.server.ActivityServer;
import com.bluecubs.xinco.workflow.server.NodeServer;
import com.bluecubs.xinco.workflow.server.PropertyServer;
import com.bluecubs.xinco.workflow.server.ResourceServer;

public class XincoWorkflowSoapBindingImpl implements com.bluecubs.xinco.service.XincoWorkflow{
    public com.bluecubs.xinco.workflow.Activity getActivity(com.bluecubs.xinco.workflow.Activity in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        try {
            ResourceServer r = new ResourceServer(resource.getUsername(),resource.getPassword(),new XincoDBManager());
        } catch (XincoWorkflowException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        ActivityServer a=null;
        try {
            a = new ActivityServer(in.getId(), new XincoDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return a;
    }
    
    public void setActivity(com.bluecubs.xinco.workflow.holders.ActivityHolder activity, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
    }
    
    public com.bluecubs.xinco.workflow.Node getNode(com.bluecubs.xinco.workflow.Node in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        try {
            ResourceServer r = new ResourceServer(resource.getUsername(),resource.getPassword(),new XincoDBManager());
        } catch (XincoWorkflowException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        NodeServer n=null;
        try {
            n = new NodeServer(in.getId(), new XincoDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return n;
    }
    
    public com.bluecubs.xinco.workflow.Property getProperty(com.bluecubs.xinco.workflow.Property in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        try {
            ResourceServer r = new ResourceServer(resource.getUsername(),resource.getPassword(),new XincoDBManager());
        } catch (XincoWorkflowException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        PropertyServer p=null;
        try {
            p = new PropertyServer(in.getId(), new XincoDBManager());
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
    
}