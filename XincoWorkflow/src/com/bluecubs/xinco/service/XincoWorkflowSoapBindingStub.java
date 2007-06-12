/**
 * XincoWorkflowSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bluecubs.xinco.service;

public class XincoWorkflowSoapBindingStub extends org.apache.axis.client.Stub implements com.bluecubs.xinco.service.XincoWorkflow {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[16];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Activity"), com.bluecubs.xinco.workflow.Activity.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Activity"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.Activity.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "activity"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "activity"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Activity"), com.bluecubs.xinco.workflow.Activity.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getNode");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"), com.bluecubs.xinco.workflow.Node.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.Node.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getProperty");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Property"), com.bluecubs.xinco.workflow.Property.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Property"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.Property.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "property"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setProperty");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "property"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Property"), com.bluecubs.xinco.workflow.Property.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getResource");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resourceReqested"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resourceRequesting"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.Resource.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "resource"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setResource");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resourceRequesting"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTransaction");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Transaction"), com.bluecubs.xinco.workflow.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Transaction"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.Transaction.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "transaction"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setTransaction");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "transaction"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Transaction"), com.bluecubs.xinco.workflow.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getWorkflowInstance");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowInstance"), com.bluecubs.xinco.workflow.WorkflowInstance.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowInstance"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.WorkflowInstance.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "workflow_instance"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setWorkflowInstance");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "instance"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowInstance"), com.bluecubs.xinco.workflow.WorkflowInstance.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getWorkflowTemplate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowTemplate"), com.bluecubs.xinco.workflow.WorkflowTemplate.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowTemplate"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.WorkflowTemplate.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "template"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setNode");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"), com.bluecubs.xinco.workflow.Node.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.Node.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setWorkflowSetting");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowSetting"), com.bluecubs.xinco.workflow.WorkflowSetting.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowSetting"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.WorkflowSetting.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "setXincoSettingReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getWorkflowSetting");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource"), com.bluecubs.xinco.workflow.Resource.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        oper.setReturnClass(java.util.Vector.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getXincoSettingReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getWorkflowServerVersion");
        oper.setReturnType(new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowVersion"));
        oper.setReturnClass(com.bluecubs.xinco.workflow.WorkflowVersion.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getWorkflowServerVersionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[15] = oper;

    }

    public XincoWorkflowSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public XincoWorkflowSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public XincoWorkflowSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Activity");
            cachedSerQNames.add(qName);
            cls = com.bluecubs.xinco.workflow.Activity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Node");
            cachedSerQNames.add(qName);
            cls = com.bluecubs.xinco.workflow.Node.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Property");
            cachedSerQNames.add(qName);
            cls = com.bluecubs.xinco.workflow.Property.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Resource");
            cachedSerQNames.add(qName);
            cls = com.bluecubs.xinco.workflow.Resource.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "Transaction");
            cachedSerQNames.add(qName);
            cls = com.bluecubs.xinco.workflow.Transaction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowInstance");
            cachedSerQNames.add(qName);
            cls = com.bluecubs.xinco.workflow.WorkflowInstance.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowSetting");
            cachedSerQNames.add(qName);
            cls = com.bluecubs.xinco.workflow.WorkflowSetting.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowTemplate");
            cachedSerQNames.add(qName);
            cls = com.bluecubs.xinco.workflow.WorkflowTemplate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://workflow.xinco.bluecubs.com", "WorkflowVersion");
            cachedSerQNames.add(qName);
            cls = com.bluecubs.xinco.workflow.WorkflowVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.bluecubs.xinco.workflow.Activity getActivity(com.bluecubs.xinco.workflow.Activity in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getActivity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.Activity) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.Activity) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.Activity.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void setActivity(com.bluecubs.xinco.workflow.holders.ActivityHolder activity, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "setActivity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {activity.value, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                activity.value = (com.bluecubs.xinco.workflow.Activity) _output.get(new javax.xml.namespace.QName("", "activity"));
            } catch (java.lang.Exception _exception) {
                activity.value = (com.bluecubs.xinco.workflow.Activity) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "activity")), com.bluecubs.xinco.workflow.Activity.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bluecubs.xinco.workflow.Node getNode(com.bluecubs.xinco.workflow.Node in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getNode"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.Node) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.Node) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.Node.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bluecubs.xinco.workflow.Property getProperty(com.bluecubs.xinco.workflow.Property in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getProperty"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.Property) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.Property) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.Property.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void setProperty(com.bluecubs.xinco.workflow.holders.PropertyHolder property, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "setProperty"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {property.value, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                property.value = (com.bluecubs.xinco.workflow.Property) _output.get(new javax.xml.namespace.QName("", "property"));
            } catch (java.lang.Exception _exception) {
                property.value = (com.bluecubs.xinco.workflow.Property) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "property")), com.bluecubs.xinco.workflow.Property.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bluecubs.xinco.workflow.Resource getResource(com.bluecubs.xinco.workflow.Resource resourceReqested, com.bluecubs.xinco.workflow.Resource resourceRequesting) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getResource"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {resourceReqested, resourceRequesting});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.Resource) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.Resource) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.Resource.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void setResource(com.bluecubs.xinco.workflow.holders.ResourceHolder resource, com.bluecubs.xinco.workflow.Resource resourceRequesting) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "setResource"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {resource.value, resourceRequesting});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resource.value = (com.bluecubs.xinco.workflow.Resource) _output.get(new javax.xml.namespace.QName("", "resource"));
            } catch (java.lang.Exception _exception) {
                resource.value = (com.bluecubs.xinco.workflow.Resource) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resource")), com.bluecubs.xinco.workflow.Resource.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bluecubs.xinco.workflow.Transaction getTransaction(com.bluecubs.xinco.workflow.Transaction in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getTransaction"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.Transaction) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.Transaction) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.Transaction.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void setTransaction(com.bluecubs.xinco.workflow.holders.TransactionHolder transaction, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "setTransaction"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {transaction.value, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                transaction.value = (com.bluecubs.xinco.workflow.Transaction) _output.get(new javax.xml.namespace.QName("", "transaction"));
            } catch (java.lang.Exception _exception) {
                transaction.value = (com.bluecubs.xinco.workflow.Transaction) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "transaction")), com.bluecubs.xinco.workflow.Transaction.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bluecubs.xinco.workflow.WorkflowInstance getWorkflowInstance(com.bluecubs.xinco.workflow.WorkflowInstance in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getWorkflowInstance"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.WorkflowInstance) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.WorkflowInstance) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.WorkflowInstance.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void setWorkflowInstance(com.bluecubs.xinco.workflow.holders.WorkflowInstanceHolder instance, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "setWorkflowInstance"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {instance.value, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                instance.value = (com.bluecubs.xinco.workflow.WorkflowInstance) _output.get(new javax.xml.namespace.QName("", "instance"));
            } catch (java.lang.Exception _exception) {
                instance.value = (com.bluecubs.xinco.workflow.WorkflowInstance) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "instance")), com.bluecubs.xinco.workflow.WorkflowInstance.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bluecubs.xinco.workflow.WorkflowTemplate getWorkflowTemplate(com.bluecubs.xinco.workflow.WorkflowTemplate in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getWorkflowTemplate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.WorkflowTemplate) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.WorkflowTemplate) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.WorkflowTemplate.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bluecubs.xinco.workflow.Node setNode(com.bluecubs.xinco.workflow.Node in, com.bluecubs.xinco.workflow.Resource resource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "setNode"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in, resource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.Node) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.Node) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.Node.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bluecubs.xinco.workflow.WorkflowSetting setWorkflowSetting(com.bluecubs.xinco.workflow.WorkflowSetting in0, com.bluecubs.xinco.workflow.Resource in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "setWorkflowSetting"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.WorkflowSetting) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.WorkflowSetting) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.WorkflowSetting.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.util.Vector getWorkflowSetting(com.bluecubs.xinco.workflow.Resource in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getWorkflowSetting"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.util.Vector) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.util.Vector) org.apache.axis.utils.JavaUtils.convert(_resp, java.util.Vector.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.bluecubs.xinco.workflow.WorkflowVersion getWorkflowServerVersion() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getWorkflowServerVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.bluecubs.xinco.workflow.WorkflowVersion) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.bluecubs.xinco.workflow.WorkflowVersion) org.apache.axis.utils.JavaUtils.convert(_resp, com.bluecubs.xinco.workflow.WorkflowVersion.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
