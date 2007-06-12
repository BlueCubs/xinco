<?xml version="1.0" encoding="UTF-8"?>
<jsp:root version="1.2" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:ui="http://www.sun.com/web/ui">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <ui:page binding="#{index.page1}" id="page1">
            <ui:html binding="#{index.html1}" id="html1">
                <ui:head binding="#{index.head1}" id="head1">
                    <ui:link binding="#{index.link1}" id="link1" url="/resources/stylesheet.css"/>
                </ui:head>
                <ui:body binding="#{index.body1}" focus="form1:languages" id="body1" style="-rave-layout: grid">
                    <center>
                        <ui:form binding="#{index.form1}" id="form1" virtualFormsConfig="languageSelect | languages | languages button1">
                            <ui:image binding="#{index.image1}" id="image1" style="left: 216px; top: 0px; position: absolute" url="/resources/blueCubs.gif"/>
                            <ui:label binding="#{index.label1}" id="label1" style="left: 216px; top: 408px; position: absolute" text="Please choose a language:"/>
                            <ui:dropDown binding="#{index.languages}" id="languages" items="#{SessionBean1.languageOptions}" required="true"
                                style="left: 384px; top: 408px; position: absolute; width: 192px" valueChangeListener="#{index.languages_processValueChange}"/>
                            <ui:button binding="#{index.button1}" id="button1" style="left: 383px; top: 456px; position: absolute" text="Ok"/>
                            <div style="height: 22px; left: 216px; top: 480px; position: absolute">
                                <jsp:directive.include file="indexFooter.jspf"/>
                            </div>
                        </ui:form>
                    </center>
                </ui:body>
            </ui:html>
        </ui:page>
    </f:view>
</jsp:root>
