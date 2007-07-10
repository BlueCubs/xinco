/**
 * XincoWorkflow.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.service;

public interface Workflow extends java.rmi.Remote {
    public com.bluecubs.xinco.workflow.Activity getActivity(com.bluecubs.xinco.workflow.Activity in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public void setActivity(com.bluecubs.xinco.workflow.holders.ActivityHolder activity, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.Node getNode(com.bluecubs.xinco.workflow.Node in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.Property getProperty(com.bluecubs.xinco.workflow.Property in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public void setProperty(com.bluecubs.xinco.workflow.holders.PropertyHolder property, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.Resource getResource(com.bluecubs.xinco.workflow.Resource resourceReqested, com.bluecubs.xinco.workflow.Resource resourceRequesting) throws java.rmi.RemoteException;
    public void setResource(com.bluecubs.xinco.workflow.holders.ResourceHolder resource, com.bluecubs.xinco.workflow.Resource resourceRequesting) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.Transaction getTransaction(com.bluecubs.xinco.workflow.Transaction in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public void setTransaction(com.bluecubs.xinco.workflow.holders.TransactionHolder transaction, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.WorkflowInstance getWorkflowInstance(com.bluecubs.xinco.workflow.WorkflowInstance in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public void setWorkflowInstance(com.bluecubs.xinco.workflow.holders.WorkflowInstanceHolder instance, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.WorkflowTemplate getWorkflowTemplate(com.bluecubs.xinco.workflow.WorkflowTemplate in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.Node setNode(com.bluecubs.xinco.workflow.Node in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.WorkflowSetting setWorkflowSetting(com.bluecubs.xinco.workflow.WorkflowSetting in0, com.bluecubs.xinco.workflow.Resource in1) throws java.rmi.RemoteException;
    public java.util.Vector getWorkflowSetting(com.bluecubs.xinco.workflow.Resource in0) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.WorkflowVersion getWorkflowServerVersion() throws java.rmi.RemoteException;
    public void getWorkflowLog(com.bluecubs.xinco.workflow.holders.WorkflowLogHolder workflow_log, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public void setWorkflowLog(com.bluecubs.xinco.workflow.holders.WorkflowLogHolder workflow_log, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.InstanceProperty getWorkflowInstanceProperty(com.bluecubs.xinco.workflow.InstanceProperty instanceProperty, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
    public void setWorkflowInstanceProperty(com.bluecubs.xinco.workflow.holders.InstancePropertyHolder property, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException;
}
