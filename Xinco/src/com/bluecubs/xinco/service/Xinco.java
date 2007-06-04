/**
 * Xinco.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.service;

public interface Xinco extends java.rmi.Remote {
    public com.bluecubs.xinco.core.XincoVersion getXincoServerVersion() throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreUser getCurrentXincoCoreUser(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.util.Vector getAllXincoCoreGroups(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException;
    public java.util.Vector getAllXincoCoreLanguages(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException;
    public java.util.Vector getAllXincoCoreDataTypes(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreNode getXincoCoreNode(com.bluecubs.xinco.core.XincoCoreNode in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreData getXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreData doXincoCoreDataCheckout(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreData undoXincoCoreDataCheckout(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreData doXincoCoreDataCheckin(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public byte[] downloadXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public int uploadXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, byte[] in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException;
    public java.util.Vector findXincoCoreNodes(java.lang.String in0, com.bluecubs.xinco.core.XincoCoreLanguage in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException;
    public java.util.Vector findXincoCoreData(java.lang.String in0, com.bluecubs.xinco.core.XincoCoreLanguage in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreNode setXincoCoreNode(com.bluecubs.xinco.core.XincoCoreNode in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreData setXincoCoreData(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreACE setXincoCoreACE(com.bluecubs.xinco.core.XincoCoreACE in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public boolean removeXincoCoreACE(com.bluecubs.xinco.core.XincoCoreACE in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreLog setXincoCoreLog(com.bluecubs.xinco.core.XincoCoreLog in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreUser setXincoCoreUser(com.bluecubs.xinco.core.XincoCoreUser in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreGroup setXincoCoreGroup(com.bluecubs.xinco.core.XincoCoreGroup in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreLanguage setXincoCoreLanguage(com.bluecubs.xinco.core.XincoCoreLanguage in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreDataTypeAttribute getXincoCoreDataTypeAttribute(com.bluecubs.xinco.core.XincoCoreDataTypeAttribute in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.add.XincoAddAttribute getXincoAddAttribute(com.bluecubs.xinco.add.XincoAddAttribute in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public boolean checkXincoCoreUserNewPassword(java.lang.String in0, com.bluecubs.xinco.core.XincoCoreUser in1, com.bluecubs.xinco.core.XincoCoreUser in2) throws java.rmi.RemoteException;
    public java.util.Vector getXincoCoreAudit(com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreAudit setXincoCoreAudit(com.bluecubs.xinco.core.XincoCoreAudit in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public java.util.Vector getAllXincoUsers(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException;
    public java.util.Vector getXincoSetting(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoSetting setXincoSetting(com.bluecubs.xinco.core.XincoSetting in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoCoreAuditType getXincoCoreAuditType(com.bluecubs.xinco.core.XincoCoreUser in0, int in1) throws java.rmi.RemoteException;
    public java.util.Vector getXincoCoreAuditTypes(com.bluecubs.xinco.core.XincoCoreUser in0) throws java.rmi.RemoteException;
    public void setXincoAddAttribute(com.bluecubs.xinco.add.holders.XincoAddAttributeHolder in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public boolean indexFiles(java.util.Vector in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public void setWorkflow(com.bluecubs.xinco.workflow.holders.XincoWorkflowHolder in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.XincoWorkflow getWorkflow(int in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.XincoWorkflow getWorkflowInstance(int in0, int in1, int in2, com.bluecubs.xinco.core.XincoCoreUser in3) throws java.rmi.RemoteException;
    public java.util.Vector getWorkflowAttributes(int in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public void setXincoWorkflowInstance(com.bluecubs.xinco.workflow.holders.XincoWorkflowInstanceHolder in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public void setXincoWorkflowStep(com.bluecubs.xinco.workflow.holders.XincoWorkflowStepHolder in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public void setWorkflowInstanceStep(com.bluecubs.xinco.workflow.holders.XincoWorkflowStepInstanceHolder in0, com.bluecubs.xinco.core.XincoCoreUser in1) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.workflow.XincoWorkflowStepFork setXincoWorkflowStepFork(int id, int yesStep, int noStep, com.bluecubs.xinco.core.XincoCoreUser user) throws java.rmi.RemoteException;
    public boolean sendEmail(com.bluecubs.xinco.core.XincoEmail email, com.bluecubs.xinco.core.XincoCoreUser from) throws java.rmi.RemoteException;
    public com.bluecubs.xinco.core.XincoEmail getEmail(int id, com.bluecubs.xinco.core.XincoCoreUser user) throws java.rmi.RemoteException;
}
