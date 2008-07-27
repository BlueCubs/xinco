<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : XincoExplorer
    Created on : Mar 25, 2008, 3:38:30 PM
    Author     : Javier A. Ortiz <javier.ortiz.78@gmail.com>
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page id="page1">
            <webuijsf:html id="html1">
                <webuijsf:head id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body id="body1" style="-rave-layout: grid">
                    <webuijsf:form id="form1">
                        <webuijsf:tree clientSide="true" id="tree1" selected="treeNode1"
                            style="height: 430px; left: 0px; top: 120px; position: absolute; width: 382px" text="Xinco DMS">
                            <webuijsf:treeNode id="treeNode1" text="root">
                                <f:facet name="image">
                                    <webuijsf:image icon="TREE_DOCUMENT" id="image1"/>
                                </f:facet>
                            </webuijsf:treeNode>
                        </webuijsf:tree>
                        <div style="height: 48px; left: 0px; top: 600px; position: absolute; width: 144px">
                            <jsp:directive.include file="footer.jspf"/>
                        </div>
                        <div style="height: 120px; left: 0px; top: 0px; position: absolute; width: 768px">
                            <jsp:directive.include file="Header.jspf"/>
                        </div>
                        <webuijsf:table augmentTitle="false" id="table1" style="height: 221px; left: 384px; top: 120px; position: absolute; width: 240px"
                            title="Table" width="120">
                            <webuijsf:tableRowGroup id="tableRowGroup1" rows="10" sourceData="#{XincoExplorer.defaultTableDataProvider}" sourceVar="currentRow">
                                <webuijsf:tableColumn align="center" headerText="Attribute" id="attribute" sort="column1" spacerColumn="true">
                                    <webuijsf:staticText id="staticText1" text="#{currentRow.value['column1']}"/>
                                </webuijsf:tableColumn>
                                <webuijsf:tableColumn align="center" headerText="Details" id="details" sort="column2">
                                    <webuijsf:staticText id="staticText2" text="#{currentRow.value['column2']}"/>
                                </webuijsf:tableColumn>
                            </webuijsf:tableRowGroup>
                        </webuijsf:table>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
