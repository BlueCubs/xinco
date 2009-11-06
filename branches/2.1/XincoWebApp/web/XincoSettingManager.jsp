<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- 
    Document   : XincoSettingManager
    Created on : Nov 21, 2007, 8:35:26 AM
    Author     : ortizbj
-->
<jsp:root version="1.2" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:ui="http://www.sun.com/web/ui">
    <jsp:directive.page contentType="text/html;charset=windows-1252" pageEncoding="ISO-8859-1"/>
    <f:view>
        <ui:page binding="#{XincoSettingManager.page1}" id="page1">
            <ui:html binding="#{XincoSettingManager.html1}" id="html1">
                <ui:head binding="#{XincoSettingManager.head1}" id="head1">
                    <ui:link binding="#{XincoSettingManager.link1}" id="link1" url="/resources/stylesheet.css"/>
                </ui:head>
                <ui:body binding="#{XincoSettingManager.body1}" id="body1" style="-rave-layout: grid">
                    <ui:form binding="#{XincoSettingManager.form1}" id="form1" virtualFormsConfig="save | settings_table:tableRowGroup1:tableColumn5:textField5 settings_table:tableRowGroup1:tableColumn7:textField2 settings_table:tableRowGroup1:tableColumn8:textField3 settings_table:tableRowGroup1:tableColumn6:textField1 settings_table:tableRowGroup1:tableColumn9:textField4 | save settings_table:tableRowGroup1:tableColumn5:textField5 settings_table:tableRowGroup1:tableColumn7:textField2 settings_table:tableRowGroup1:tableColumn8:textField3 settings_table:tableRowGroup1:tableColumn6:textField1 settings_table:tableRowGroup1:tableColumn9:textField4">
                        <div style="left: 0px; top: 0px; position: absolute">
                            <jsp:directive.include file="XincoAdminHeader.jspf"/>
                        </div>
                        <ui:table augmentTitle="false" binding="#{XincoSettingManager.settings_table}" clearSortButton="true" deselectMultipleButton="true"
                            id="settings_table" paginateButton="true" paginationControls="true" selectMultipleButton="true"
                            style="left: 24px; top: 144px; position: absolute; width: 0px" title="Xinco Settings" width="0">
                            <ui:tableRowGroup binding="#{XincoSettingManager.tableRowGroup1}" id="tableRowGroup1" rows="10"
                                sourceData="#{XincoSettingManager.xinco_settingDataProvider}" sourceVar="currentRow">
                                <ui:tableColumn binding="#{XincoSettingManager.tableColumn4}" headerText="id" id="tableColumn4" sort="xinco_setting.id">
                                    <ui:staticText binding="#{XincoSettingManager.staticText4}" id="staticText4" text="#{currentRow.value['xinco_setting.id']}"/>
                                </ui:tableColumn>
                                <ui:tableColumn binding="#{XincoSettingManager.tableColumn5}" headerText="description" id="tableColumn5" sort="xinco_setting.description">
                                    <ui:message binding="#{XincoSettingManager.message5}" for="textField5" id="message5" showDetail="false" showSummary="true"/>
                                    <ui:textField binding="#{XincoSettingManager.textField5}" id="textField5" text="#{currentRow.value['xinco_setting.description']}"/>
                                </ui:tableColumn>
                                <ui:tableColumn binding="#{XincoSettingManager.tableColumn6}" headerText="int_value" id="tableColumn6">
                                    <ui:message binding="#{XincoSettingManager.message1}" for="textField1" id="message1" showDetail="false" showSummary="true"/>
                                    <ui:textField binding="#{XincoSettingManager.textField1}" id="textField1" text="#{currentRow.value['xinco_setting.int_value']}"/>
                                </ui:tableColumn>
                                <ui:tableColumn binding="#{XincoSettingManager.tableColumn7}" headerText="string_value" id="tableColumn7" sort="xinco_setting.string_value">
                                    <ui:message binding="#{XincoSettingManager.message2}" for="textField2" id="message2" showDetail="false" showSummary="true"/>
                                    <ui:textField binding="#{XincoSettingManager.textField2}" id="textField2" text="#{currentRow.value['xinco_setting.string_value']}"/>
                                </ui:tableColumn>
                                <ui:tableColumn binding="#{XincoSettingManager.tableColumn8}" headerText="bool_value" id="tableColumn8" sort="xinco_setting.bool_value">
                                    <ui:textField binding="#{XincoSettingManager.textField3}" id="textField3" text="#{currentRow.value['xinco_setting.bool_value']}"/>
                                    <ui:message binding="#{XincoSettingManager.message3}" for="textField3" id="message3" showDetail="false" showSummary="true"/>
                                </ui:tableColumn>
                                <ui:tableColumn binding="#{XincoSettingManager.tableColumn9}" headerText="long_value" id="tableColumn9" sort="xinco_setting.long_value">
                                    <ui:textField binding="#{XincoSettingManager.textField4}" id="textField4" text="#{currentRow.value['xinco_setting.long_value']}"/>
                                    <ui:message binding="#{XincoSettingManager.message4}" for="textField4" id="message4" showDetail="false" showSummary="true"/>
                                </ui:tableColumn>
                            </ui:tableRowGroup>
                        </ui:table>
                        <ui:button action="#{XincoSettingManager.add_action}" binding="#{XincoSettingManager.add}" id="add"
                            style="left: 767px; top: 144px; position: absolute" text="#{XincoSessionBean.xincoSettingAddButtonLabel}"/>
                        <ui:button action="#{XincoSettingManager.save_action}" binding="#{XincoSettingManager.save}" id="save"
                            style="left: 815px; top: 144px; position: absolute; z-index: 500" text="#{XincoSessionBean.xincoSettingSaveButtonLabel}"/>
                    </ui:form>
                </ui:body>
            </ui:html>
        </ui:page>
    </f:view>
</jsp:root>
