package com.bluecubs.xinco.core.server.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreLanguage;
import com.bluecubs.xinco.server.service.XincoCoreNode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XincoTreeServiceTest {

  private XincoTreeService treeService;

  @BeforeEach
  void setUp() {
    treeService = new XincoTreeService();
  }

  @Test
  void getItemProperties_node_noLanguage_hasIdDesignationStatus() {
    XincoCoreNode node = new XincoCoreNode();
    node.setId(7);
    node.setDesignation("RootNode");
    node.setStatusNumber(1);

    List<PropertyDTO> props = treeService.getItemProperties(node, null);

    assertThat(props).isNotEmpty();
    assertThat(props.stream().map(PropertyDTO::value)).contains("7", "RootNode", "Open");
  }

  @Test
  void getItemProperties_node_withLanguage_includesLanguageProperty() {
    XincoCoreLanguage lang = new XincoCoreLanguage();
    lang.setDesignation("language.en");
    lang.setSign("en");

    XincoCoreNode node = new XincoCoreNode();
    node.setId(2);
    node.setDesignation("NodeWithLang");
    node.setStatusNumber(2);
    node.setXincoCoreLanguage(lang);

    List<PropertyDTO> props = treeService.getItemProperties(node, null);

    assertThat(props.stream().map(PropertyDTO::value))
        .anyMatch(v -> v.contains("English") && v.contains("en"));
    assertThat(props.stream().map(PropertyDTO::value)).contains("Locked");
  }

  @Test
  void getItemProperties_node_statusArchived() {
    XincoCoreNode node = new XincoCoreNode();
    node.setId(3);
    node.setDesignation("Archived");
    node.setStatusNumber(3);

    List<PropertyDTO> props = treeService.getItemProperties(node, null);
    assertThat(props.stream().map(PropertyDTO::value)).contains("Archived");
  }

  @Test
  void getItemProperties_node_statusCheckedOut() {
    XincoCoreNode node = new XincoCoreNode();
    node.setId(4);
    node.setDesignation("CheckedOut");
    node.setStatusNumber(4);

    List<PropertyDTO> props = treeService.getItemProperties(node, null);
    assertThat(props.stream().map(PropertyDTO::value)).contains("Checked out");
  }

  @Test
  void getItemProperties_node_statusPublished() {
    XincoCoreNode node = new XincoCoreNode();
    node.setId(5);
    node.setDesignation("Published");
    node.setStatusNumber(5);

    List<PropertyDTO> props = treeService.getItemProperties(node, null);
    assertThat(props.stream().map(PropertyDTO::value)).contains("Published");
  }

  @Test
  void getItemProperties_node_unknownStatus_includesFallback() {
    XincoCoreNode node = new XincoCoreNode();
    node.setId(6);
    node.setDesignation("Unknown");
    node.setStatusNumber(99);

    List<PropertyDTO> props = treeService.getItemProperties(node, null);
    assertThat(props.stream().map(PropertyDTO::value)).anyMatch(v -> v.contains("Unknown"));
  }

  @Test
  void getItemProperties_data_noLanguageNoDataType_hasIdDesignationStatus() {
    XincoCoreData data = new XincoCoreData();
    data.setId(10);
    data.setDesignation("MyDocument");
    data.setStatusNumber(1);

    List<PropertyDTO> props = treeService.getItemProperties(data, null);

    assertThat(props).isNotEmpty();
    assertThat(props.stream().map(PropertyDTO::value)).contains("10", "MyDocument", "Open");
  }

  @Test
  void getItemProperties_data_withLanguageAndDataType() {
    XincoCoreLanguage lang = new XincoCoreLanguage();
    lang.setDesignation("language.en");
    lang.setSign("en");

    XincoCoreDataType dataType = new XincoCoreDataType();
    dataType.setDesignation("general.data.type.file");

    XincoCoreData data = new XincoCoreData();
    data.setId(11);
    data.setDesignation("Report.pdf");
    data.setStatusNumber(5);
    data.setXincoCoreLanguage(lang);
    data.setXincoCoreDataType(dataType);

    List<PropertyDTO> props = treeService.getItemProperties(data, null);

    assertThat(props.stream().map(PropertyDTO::value))
        .anyMatch(v -> v.contains("English"))
        .anyMatch(v -> v.contains("File"));
    assertThat(props.stream().map(PropertyDTO::value)).contains("Published");
  }

  @Test
  void getItemProperties_data_statusLocked() {
    XincoCoreData data = new XincoCoreData();
    data.setId(12);
    data.setDesignation("Locked");
    data.setStatusNumber(2);

    List<PropertyDTO> props = treeService.getItemProperties(data, null);
    assertThat(props.stream().map(PropertyDTO::value)).contains("Locked");
  }

  @Test
  void getItemProperties_unknownType_returnsEmptyList() {
    List<PropertyDTO> props = treeService.getItemProperties("some string", null);
    assertThat(props).isEmpty();
  }
}
