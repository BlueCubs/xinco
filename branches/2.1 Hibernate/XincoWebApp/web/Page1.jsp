<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Page1
    Created on : Apr 10, 2008, 1:04:17 PM
    Author     : Javier A. Ortiz <javier.ortiz.78@gmail.com>
-->

<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page binding="#{Page1.page1}" id="page1">
            <webuijsf:html binding="#{Page1.html1}" id="html1">
                <webuijsf:head binding="#{Page1.head1}" id="head1">
                    <webuijsf:link binding="#{Page1.link1}" id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body binding="#{Page1.body1}" id="body1" style="-rave-layout: grid">
                    <webuijsf:form binding="#{Page1.form1}" id="form1">
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
