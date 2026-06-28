package com.bluecubs.xinco.tools;

import static org.assertj.core.api.Assertions.assertThat;

import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreDataTypeAttribute;
import org.junit.jupiter.api.Test;

class ToolAddAttributeTest {

  @Test
  void addDefaultAddAttributes_populatesAttributeList() {
    XincoCoreData data = new XincoCoreData();
    data.setId(1);

    XincoCoreDataType dataType = new XincoCoreDataType();
    dataType.setId(1);

    XincoCoreDataTypeAttribute attr1 = new XincoCoreDataTypeAttribute();
    attr1.setAttributeId(1);
    attr1.setDesignation("attr1");
    attr1.setDataType("varchar");

    XincoCoreDataTypeAttribute attr2 = new XincoCoreDataTypeAttribute();
    attr2.setAttributeId(2);
    attr2.setDesignation("attr2");
    attr2.setDataType("text");

    dataType.getXincoCoreDataTypeAttributes().add(attr1);
    dataType.getXincoCoreDataTypeAttributes().add(attr2);
    data.setXincoCoreDataType(dataType);

    Tool.addDefaultAddAttributes(data);

    assertThat(data.getXincoAddAttributes()).hasSize(2);
    assertThat(data.getXincoAddAttributes().get(0).getAttributeId()).isEqualTo(1);
    assertThat(data.getXincoAddAttributes().get(1).getAttributeId()).isEqualTo(2);
  }

  @Test
  void addDefaultAddAttributes_emptyDataType_clearsAttributes() {
    XincoCoreData data = new XincoCoreData();
    data.setId(1);

    XincoCoreDataType dataType = new XincoCoreDataType();
    data.setXincoCoreDataType(dataType);

    // Pre-populate to verify clear happens
    data.getXincoAddAttributes().add(new com.bluecubs.xinco.server.service.XincoAddAttribute());

    Tool.addDefaultAddAttributes(data);

    assertThat(data.getXincoAddAttributes()).isEmpty();
  }
}
