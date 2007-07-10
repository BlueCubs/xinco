<?xml version="1.0" encoding="UTF-8"?>
<jsp:root version="1.2" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:ui="http://www.sun.com/web/ui">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <ui:page binding="#{WorkflowTest.page1}" id="page1">
            <ui:html binding="#{WorkflowTest.html1}" id="html1">
                <ui:head binding="#{WorkflowTest.head1}" id="head1">
                    <ui:link binding="#{WorkflowTest.link1}" id="link1" url="/resources/xincostyle.css"/>
                </ui:head>
                <ui:body binding="#{WorkflowTest.body1}" id="body1" style="-rave-layout: grid">
                    <center>
                        <ui:form binding="#{WorkflowTest.form1}" id="form1" virtualFormsConfig="virtualForm1 | |">
                            <ui:label binding="#{WorkflowTest.label1}" id="label1" style="left: 384px; top: 408px; position: absolute" text="Welcome!"/>
                            <ui:button action="#{WorkflowTest.load_action}" binding="#{WorkflowTest.load}" id="load"
                                style="left: 455px; top: 408px; position: absolute" text="Load Workflow"/>
                            <ui:textArea binding="#{WorkflowTest.result}" id="result"
                                style="height: 110px; left: 384px; top: 432px; position: absolute; width: 264px" valueChangeListener="#{WorkflowTest.result_processValueChange}"/>
                            <ui:image align="middle" binding="#{WorkflowTest.image1}" id="image1" style="left: 336px; top: 0px; position: absolute" url="/resources/blueCubs.gif"/>
                        </ui:form>
                    </center>
                </ui:body>
            </ui:html>
        </ui:page>
    </f:view>
</jsp:root>
