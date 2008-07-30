<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Main
    Created on : Mar 24, 2008, 3:36:10 PM
    Author     : Javier A. Ortiz <javier.ortiz.78@gmail.com>
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page binding="#{Main.page1}" id="page1">
            <webuijsf:html id="html1">
                <webuijsf:head id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body id="body1" style="-rave-layout: grid">
                    <webuijsf:form id="form1">
                        <webuijsf:image height="312" id="image1" style="left: 288px; top: 24px; position: absolute" url="/resources/blueCubs.gif" width="312"/>
                        <webuijsf:label id="label1"
                            style="font-size: medium; height: 24px; left: 168px; top: 360px; position: absolute; text-align: center; width: 504px" text="xinco DMS - the Core of Information and Document Management"/>
                        <webuijsf:label for="dropDown1" id="label2" labelLevel="3" style="left: 192px; top: 408px; position: absolute" text="Please choose a language"/>
                        <webuijsf:dropDown id="dropDown1" items="#{Main.dropDown1DefaultOptions.options}"
                            style="height: 24px; left: 360px; top: 408px; position: absolute" width="168"/>
                        <webuijsf:button id="button1" style="height: 24px; left: 551px; top: 408px; position: absolute; width: 96px" text="Submit"/>
                        <webuijsf:label binding="#{Main.label3}" id="label3" style="position: absolute; left: 384px; top: 456px; width: 120px; height: 24px" text="Label"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
