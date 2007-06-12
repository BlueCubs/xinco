/**
 * XincoWorkflowSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.service;

public class XincoWorkflowSoapBindingSkeleton implements com.bluecubs.xinco.service.XincoWorkflow, org.apache.axis.wsdl.Skeleton {
    private com.bluecubs.xinco.service.XincoWorkflow impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Activity"), com.bluecubs.xinco.workflow.Activity.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getActivity", _params, new javax.xml.namespace.QName("", "activity"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Activity"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getActivity"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getActivity") == null) {
            _myOperations.put("getActivity", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getActivity")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "activity"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Activity"), com.bluecubs.xinco.workflow.Activity.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setActivity", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("", "setActivity"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("setActivity") == null) {
            _myOperations.put("setActivity", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setActivity")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"), com.bluecubs.xinco.workflow.Node.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getNode", _params, new javax.xml.namespace.QName("", "node"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getNode"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getNode") == null) {
            _myOperations.put("getNode", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getNode")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Property"), com.bluecubs.xinco.workflow.Property.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getProperty", _params, new javax.xml.namespace.QName("", "property"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Property"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getProperty"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getProperty") == null) {
            _myOperations.put("getProperty", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getProperty")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "property"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Property"), com.bluecubs.xinco.workflow.Property.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setProperty", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("", "setProperty"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("setProperty") == null) {
            _myOperations.put("setProperty", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setProperty")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resourceReqested"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resourceRequesting"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getResource", _params, new javax.xml.namespace.QName("", "resource"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getResource"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getResource") == null) {
            _myOperations.put("getResource", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getResource")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resourceRequesting"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setResource", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("", "setResource"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("setResource") == null) {
            _myOperations.put("setResource", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setResource")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Transaction"), com.bluecubs.xinco.workflow.Transaction.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTransaction", _params, new javax.xml.namespace.QName("", "transaction"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Transaction"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getTransaction"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTransaction") == null) {
            _myOperations.put("getTransaction", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTransaction")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "transaction"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Transaction"), com.bluecubs.xinco.workflow.Transaction.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setTransaction", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("", "setTransaction"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("setTransaction") == null) {
            _myOperations.put("setTransaction", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setTransaction")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowInstance"), com.bluecubs.xinco.workflow.WorkflowInstance.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getWorkflowInstance", _params, new javax.xml.namespace.QName("", "workflow_instance"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowInstance"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getWorkflowInstance"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getWorkflowInstance") == null) {
            _myOperations.put("getWorkflowInstance", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getWorkflowInstance")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "instance"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowInstance"), com.bluecubs.xinco.workflow.WorkflowInstance.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setWorkflowInstance", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("", "setWorkflowInstance"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("setWorkflowInstance") == null) {
            _myOperations.put("setWorkflowInstance", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setWorkflowInstance")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowTemplate"), com.bluecubs.xinco.workflow.WorkflowTemplate.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getWorkflowTemplate", _params, new javax.xml.namespace.QName("", "template"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowTemplate"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getWorkflowTemplate"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getWorkflowTemplate") == null) {
            _myOperations.put("getWorkflowTemplate", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getWorkflowTemplate")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"), com.bluecubs.xinco.workflow.Node.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setNode", _params, new javax.xml.namespace.QName("", "node"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "setNode"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("setNode") == null) {
            _myOperations.put("setNode", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setNode")).add(_oper);
    }

    public XincoWorkflowSoapBindingSkeleton() {
        this.impl = new com.bluecubs.xinco.service.XincoWorkflowSoapBindingImpl();
    }

    public XincoWorkflowSoapBindingSkeleton(com.bluecubs.xinco.service.XincoWorkflow impl) {
        this.impl = impl;
    }
    public com.bluecubs.xinco.workflow.Activity getActivity(com.bluecubs.xinco.workflow.Activity in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        com.bluecubs.xinco.workflow.Activity ret = impl.getActivity(in, resource);
        return ret;
    }

    public void setActivity(com.bluecubs.xinco.workflow.holders.ActivityHolder activity, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        impl.setActivity(activity, resource);
    }

    public com.bluecubs.xinco.workflow.Node getNode(com.bluecubs.xinco.workflow.Node in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        com.bluecubs.xinco.workflow.Node ret = impl.getNode(in, resource);
        return ret;
    }

    public com.bluecubs.xinco.workflow.Property getProperty(com.bluecubs.xinco.workflow.Property in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        com.bluecubs.xinco.workflow.Property ret = impl.getProperty(in, resource);
        return ret;
    }

    public void setProperty(com.bluecubs.xinco.workflow.holders.PropertyHolder property, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        impl.setProperty(property, resource);
    }

    public com.bluecubs.xinco.workflow.Resource getResource(com.bluecubs.xinco.workflow.Resource resourceReqested, com.bluecubs.xinco.workflow.Resource resourceRequesting) throws java.rmi.RemoteException
    {
        com.bluecubs.xinco.workflow.Resource ret = impl.getResource(resourceReqested, resourceRequesting);
        return ret;
    }

    public void setResource(com.bluecubs.xinco.workflow.holders.ResourceHolder resource, com.bluecubs.xinco.workflow.Resource resourceRequesting) throws java.rmi.RemoteException
    {
        impl.setResource(resource, resourceRequesting);
    }

    public com.bluecubs.xinco.workflow.Transaction getTransaction(com.bluecubs.xinco.workflow.Transaction in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        com.bluecubs.xinco.workflow.Transaction ret = impl.getTransaction(in, resource);
        return ret;
    }

    public void setTransaction(com.bluecubs.xinco.workflow.holders.TransactionHolder transaction, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        impl.setTransaction(transaction, resource);
    }

    public com.bluecubs.xinco.workflow.WorkflowInstance getWorkflowInstance(com.bluecubs.xinco.workflow.WorkflowInstance in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        com.bluecubs.xinco.workflow.WorkflowInstance ret = impl.getWorkflowInstance(in, resource);
        return ret;
    }

    public void setWorkflowInstance(com.bluecubs.xinco.workflow.holders.WorkflowInstanceHolder instance, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        impl.setWorkflowInstance(instance, resource);
    }

    public com.bluecubs.xinco.workflow.WorkflowTemplate getWorkflowTemplate(com.bluecubs.xinco.workflow.WorkflowTemplate in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        com.bluecubs.xinco.workflow.WorkflowTemplate ret = impl.getWorkflowTemplate(in, resource);
        return ret;
    }

    public com.bluecubs.xinco.workflow.Node setNode(com.bluecubs.xinco.workflow.Node in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException
    {
        com.bluecubs.xinco.workflow.Node ret = impl.setNode(in, resource);
        return ret;
    }

}
