package com.bluecubs.xinco.core.server.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PropertyDTOTest {

  @Test
  void constructor_setsKeyAndValue() {
    PropertyDTO dto = new PropertyDTO("Name", "Alice");
    assertThat(dto.key()).isEqualTo("Name");
    assertThat(dto.value()).isEqualTo("Alice");
  }

  @Test
  void recordEquality_twoEqualRecords() {
    PropertyDTO a = new PropertyDTO("k", "v");
    PropertyDTO b = new PropertyDTO("k", "v");
    assertThat(a).isEqualTo(b);
    assertThat(a.hashCode()).isEqualTo(b.hashCode());
  }

  @Test
  void recordEquality_differentValues_notEqual() {
    PropertyDTO a = new PropertyDTO("k", "v1");
    PropertyDTO b = new PropertyDTO("k", "v2");
    assertThat(a).isNotEqualTo(b);
  }

  @Test
  void toString_containsKeyAndValue() {
    PropertyDTO dto = new PropertyDTO("Status", "Open");
    assertThat(dto.toString()).contains("Status").contains("Open");
  }
}
