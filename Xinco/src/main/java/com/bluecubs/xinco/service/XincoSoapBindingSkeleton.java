/**
 * XincoSoapBindingSkeleton.java
 *
 * <p>This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT)
 * WSDL2Java emitter.
 */
package com.bluecubs.xinco.service;

public class XincoSoapBindingSkeleton
    implements com.bluecubs.xinco.service.Xinco, org.apache.axis.wsdl.Skeleton {
  private com.bluecubs.xinco.service.Xinco impl;
  private static java.util.Map _myOperations = new java.util.Hashtable();
  private static java.util.Collection _myOperationsList = new java.util.ArrayList();

  /** Returns List of OperationDesc objects with this name */
  public static java.util.List getOperationDescByName(java.lang.String methodName) {
    return (java.util.List) _myOperations.get(methodName);
  }

  /** Returns Collection of OperationDescs */
  public static java.util.Collection getOperationDescs() {
    return _myOperationsList;
  }

  static {
    org.apache.axis.description.OperationDesc _oper;
    org.apache.axis.description.FaultDesc _fault;
    org.apache.axis.description.ParameterDesc[] _params;
    _params = new org.apache.axis.description.ParameterDesc[] {};
    _oper =
        new org.apache.axis.description.OperationDesc(
            "getXincoServerVersion",
            _params,
            new javax.xml.namespace.QName("", "getXincoServerVersionReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoVersion"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "getXincoServerVersion"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("getXincoServerVersion", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("getXincoServerVersion")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
              java.lang.String.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
              java.lang.String.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "getCurrentXincoCoreUser",
            _params,
            new javax.xml.namespace.QName("", "getCurrentXincoCoreUserReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "getCurrentXincoCoreUser"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("getCurrentXincoCoreUser", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("getCurrentXincoCoreUser")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "getAllXincoCoreGroups",
            _params,
            new javax.xml.namespace.QName("", "getAllXincoCoreGroupsReturn"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "getAllXincoCoreGroups"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("getAllXincoCoreGroups", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("getAllXincoCoreGroups")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "getAllXincoCoreLanguages",
            _params,
            new javax.xml.namespace.QName("", "getAllXincoCoreLanguagesReturn"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "getAllXincoCoreLanguages"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("getAllXincoCoreLanguages", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("getAllXincoCoreLanguages")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "getAllXincoCoreDataTypes",
            _params,
            new javax.xml.namespace.QName("", "getAllXincoCoreDataTypesReturn"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "getAllXincoCoreDataTypes"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("getAllXincoCoreDataTypes", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("getAllXincoCoreDataTypes")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreNode"),
              com.bluecubs.xinco.core.XincoCoreNode.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "getXincoCoreNode",
            _params,
            new javax.xml.namespace.QName("", "getXincoCoreNodeReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreNode"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "getXincoCoreNode"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("getXincoCoreNode", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("getXincoCoreNode")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"),
              com.bluecubs.xinco.core.XincoCoreData.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "getXincoCoreData",
            _params,
            new javax.xml.namespace.QName("", "getXincoCoreDataReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "getXincoCoreData"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("getXincoCoreData", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("getXincoCoreData")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"),
              com.bluecubs.xinco.core.XincoCoreData.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "doXincoCoreDataCheckout",
            _params,
            new javax.xml.namespace.QName("", "doXincoCoreDataCheckoutReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "doXincoCoreDataCheckout"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("doXincoCoreDataCheckout", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("doXincoCoreDataCheckout")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"),
              com.bluecubs.xinco.core.XincoCoreData.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "undoXincoCoreDataCheckout",
            _params,
            new javax.xml.namespace.QName("", "undoXincoCoreDataCheckoutReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "undoXincoCoreDataCheckout"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("undoXincoCoreDataCheckout", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("undoXincoCoreDataCheckout")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"),
              com.bluecubs.xinco.core.XincoCoreData.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "doXincoCoreDataCheckin",
            _params,
            new javax.xml.namespace.QName("", "doXincoCoreDataCheckinReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "doXincoCoreDataCheckin"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("doXincoCoreDataCheckin", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("doXincoCoreDataCheckin")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"),
              com.bluecubs.xinco.core.XincoCoreData.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "downloadXincoCoreData",
            _params,
            new javax.xml.namespace.QName("", "downloadXincoCoreDataReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "base64Binary"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "downloadXincoCoreData"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("downloadXincoCoreData", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("downloadXincoCoreData")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"),
              com.bluecubs.xinco.core.XincoCoreData.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName(
                  "http://schemas.xmlsoap.org/soap/encoding/", "base64Binary"),
              byte[].class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in2"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "uploadXincoCoreData",
            _params,
            new javax.xml.namespace.QName("", "uploadXincoCoreDataReturn"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "uploadXincoCoreData"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("uploadXincoCoreData", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("uploadXincoCoreData")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
              java.lang.String.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLanguage"),
              com.bluecubs.xinco.core.XincoCoreLanguage.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in2"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "findXincoCoreNodes",
            _params,
            new javax.xml.namespace.QName("", "findXincoCoreNodesReturn"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "findXincoCoreNodes"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("findXincoCoreNodes", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("findXincoCoreNodes")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
              java.lang.String.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLanguage"),
              com.bluecubs.xinco.core.XincoCoreLanguage.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in2"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "findXincoCoreData",
            _params,
            new javax.xml.namespace.QName("", "findXincoCoreDataReturn"));
    _oper.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "findXincoCoreData"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("findXincoCoreData", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("findXincoCoreData")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreNode"),
              com.bluecubs.xinco.core.XincoCoreNode.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "setXincoCoreNode",
            _params,
            new javax.xml.namespace.QName("", "setXincoCoreNodeReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreNode"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "setXincoCoreNode"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("setXincoCoreNode", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("setXincoCoreNode")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"),
              com.bluecubs.xinco.core.XincoCoreData.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "setXincoCoreData",
            _params,
            new javax.xml.namespace.QName("", "setXincoCoreDataReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreData"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "setXincoCoreData"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("setXincoCoreData", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("setXincoCoreData")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreACE"),
              com.bluecubs.xinco.core.XincoCoreACE.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "setXincoCoreACE", _params, new javax.xml.namespace.QName("", "setXincoCoreACEReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreACE"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "setXincoCoreACE"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("setXincoCoreACE", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("setXincoCoreACE")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreACE"),
              com.bluecubs.xinco.core.XincoCoreACE.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "removeXincoCoreACE",
            _params,
            new javax.xml.namespace.QName("", "removeXincoCoreACEReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "removeXincoCoreACE"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("removeXincoCoreACE", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("removeXincoCoreACE")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLog"),
              com.bluecubs.xinco.core.XincoCoreLog.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "setXincoCoreLog", _params, new javax.xml.namespace.QName("", "setXincoCoreLogReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLog"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "setXincoCoreLog"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("setXincoCoreLog", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("setXincoCoreLog")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "setXincoCoreUser",
            _params,
            new javax.xml.namespace.QName("", "setXincoCoreUserReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "setXincoCoreUser"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("setXincoCoreUser", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("setXincoCoreUser")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreGroup"),
              com.bluecubs.xinco.core.XincoCoreGroup.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "setXincoCoreGroup",
            _params,
            new javax.xml.namespace.QName("", "setXincoCoreGroupReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreGroup"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "setXincoCoreGroup"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("setXincoCoreGroup", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("setXincoCoreGroup")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLanguage"),
              com.bluecubs.xinco.core.XincoCoreLanguage.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "setXincoCoreLanguage",
            _params,
            new javax.xml.namespace.QName("", "setXincoCoreLanguageReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreLanguage"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "setXincoCoreLanguage"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("setXincoCoreLanguage", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("setXincoCoreLanguage")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName(
                  "http://core.xinco.bluecubs.com", "XincoCoreDataTypeAttribute"),
              com.bluecubs.xinco.core.XincoCoreDataTypeAttribute.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "getXincoCoreDataTypeAttribute",
            _params,
            new javax.xml.namespace.QName("", "getXincoCoreDataTypeAttributeReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName(
            "http://core.xinco.bluecubs.com", "XincoCoreDataTypeAttribute"));
    _oper.setElementQName(
        new javax.xml.namespace.QName("urn:Xinco", "getXincoCoreDataTypeAttribute"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent(
        "getXincoCoreDataTypeAttribute", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("getXincoCoreDataTypeAttribute")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://add.xinco.bluecubs.com", "XincoAddAttribute"),
              com.bluecubs.xinco.add.XincoAddAttribute.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "getXincoAddAttribute",
            _params,
            new javax.xml.namespace.QName("", "getXincoAddAttributeReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://add.xinco.bluecubs.com", "XincoAddAttribute"));
    _oper.setElementQName(new javax.xml.namespace.QName("urn:Xinco", "getXincoAddAttribute"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent("getXincoAddAttribute", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("getXincoAddAttribute")).add(_oper);
    _params =
        new org.apache.axis.description.ParameterDesc[] {
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in0"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
              java.lang.String.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in1"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
          new org.apache.axis.description.ParameterDesc(
              new javax.xml.namespace.QName("", "in2"),
              org.apache.axis.description.ParameterDesc.IN,
              new javax.xml.namespace.QName("http://core.xinco.bluecubs.com", "XincoCoreUser"),
              com.bluecubs.xinco.core.XincoCoreUser.class,
              false,
              false),
        };
    _oper =
        new org.apache.axis.description.OperationDesc(
            "checkXincoCoreUserNewPassword",
            _params,
            new javax.xml.namespace.QName("", "checkXincoCoreUserNewPasswordReturn"));
    _oper.setReturnType(
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
    _oper.setElementQName(
        new javax.xml.namespace.QName("urn:Xinco", "checkXincoCoreUserNewPassword"));
    _oper.setSoapAction("");
    _myOperationsList.add(_oper);
    _myOperations.computeIfAbsent(
        "checkXincoCoreUserNewPassword", k -> new java.util.ArrayList<>());
    ((java.util.List) _myOperations.get("checkXincoCoreUserNewPassword")).add(_oper);
  }

  public XincoSoapBindingSkeleton() {
    this.impl = new com.bluecubs.xinco.service.XincoSoapBindingImpl();
  }

  public XincoSoapBindingSkeleton(com.bluecubs.xinco.service.Xinco impl) {
    this.impl = impl;
  }

  public com.bluecubs.xinco.core.XincoVersion getXincoServerVersion()
      throws java.rmi.RemoteException {
    com.bluecubs.xinco.core.XincoVersion ret = impl.getXincoServerVersion();
    return ret;
  }

  public com.bluecubs.xinco.core.XincoCoreUser getCurrentXincoCoreUser(
      java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
    return impl.getCurrentXincoCoreUser(in0, in1);
  }

  public java.util.Vector getAllXincoCoreGroups(com.bluecubs.xinco.core.XincoCoreUser in0)
      throws java.rmi.RemoteException {
    return impl.getAllXincoCoreGroups(in0);
  }

  public java.util.Vector getAllXincoCoreLanguages(com.bluecubs.xinco.core.XincoCoreUser in0)
      throws java.rmi.RemoteException {
    return impl.getAllXincoCoreLanguages(in0);
  }

  public java.util.Vector getAllXincoCoreDataTypes(com.bluecubs.xinco.core.XincoCoreUser in0)
      throws java.rmi.RemoteException {
    return impl.getAllXincoCoreDataTypes(in0);
  }

  public com.bluecubs.xinco.core.XincoCoreNode getXincoCoreNode(
      com.bluecubs.xinco.core.XincoCoreNode in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.getXincoCoreNode(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreData getXincoCoreData(
      com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.getXincoCoreData(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreData doXincoCoreDataCheckout(
      com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.doXincoCoreDataCheckout(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreData undoXincoCoreDataCheckout(
      com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.undoXincoCoreDataCheckout(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreData doXincoCoreDataCheckin(
      com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.doXincoCoreDataCheckin(in0, in1);
  }

  public byte[] downloadXincoCoreData(
      com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.downloadXincoCoreData(in0, in1);
  }

  public int uploadXincoCoreData(
      com.bluecubs.xinco.core.XincoCoreData in0,
      byte[] in1,
      com.bluecubs.xinco.core.XincoCoreUser in2)
      throws java.rmi.RemoteException {
    return impl.uploadXincoCoreData(in0, in1, in2);
  }

  public java.util.Vector findXincoCoreNodes(
      java.lang.String in0,
      com.bluecubs.xinco.core.XincoCoreLanguage in1,
      com.bluecubs.xinco.core.XincoCoreUser in2)
      throws java.rmi.RemoteException {
    return impl.findXincoCoreNodes(in0, in1, in2);
  }

  public java.util.Vector findXincoCoreData(
      java.lang.String in0,
      com.bluecubs.xinco.core.XincoCoreLanguage in1,
      com.bluecubs.xinco.core.XincoCoreUser in2)
      throws java.rmi.RemoteException {
    return impl.findXincoCoreData(in0, in1, in2);
  }

  public com.bluecubs.xinco.core.XincoCoreNode setXincoCoreNode(
      com.bluecubs.xinco.core.XincoCoreNode in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.setXincoCoreNode(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreData setXincoCoreData(
      com.bluecubs.xinco.core.XincoCoreData in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.setXincoCoreData(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreACE setXincoCoreACE(
      com.bluecubs.xinco.core.XincoCoreACE in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.setXincoCoreACE(in0, in1);
  }

  public boolean removeXincoCoreACE(
      com.bluecubs.xinco.core.XincoCoreACE in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.removeXincoCoreACE(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreLog setXincoCoreLog(
      com.bluecubs.xinco.core.XincoCoreLog in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.setXincoCoreLog(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreUser setXincoCoreUser(
      com.bluecubs.xinco.core.XincoCoreUser in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.setXincoCoreUser(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreGroup setXincoCoreGroup(
      com.bluecubs.xinco.core.XincoCoreGroup in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.setXincoCoreGroup(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreLanguage setXincoCoreLanguage(
      com.bluecubs.xinco.core.XincoCoreLanguage in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.setXincoCoreLanguage(in0, in1);
  }

  public com.bluecubs.xinco.core.XincoCoreDataTypeAttribute getXincoCoreDataTypeAttribute(
      com.bluecubs.xinco.core.XincoCoreDataTypeAttribute in0,
      com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.getXincoCoreDataTypeAttribute(in0, in1);
  }

  public com.bluecubs.xinco.add.XincoAddAttribute getXincoAddAttribute(
      com.bluecubs.xinco.add.XincoAddAttribute in0, com.bluecubs.xinco.core.XincoCoreUser in1)
      throws java.rmi.RemoteException {
    return impl.getXincoAddAttribute(in0, in1);
  }

  public boolean checkXincoCoreUserNewPassword(
      java.lang.String in0,
      com.bluecubs.xinco.core.XincoCoreUser in1,
      com.bluecubs.xinco.core.XincoCoreUser in2)
      throws java.rmi.RemoteException {
    return impl.checkXincoCoreUserNewPassword(in0, in1, in2);
  }
}
