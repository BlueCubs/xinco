package com.bluecubs.xinco.tools;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ToolComprehensiveTest {

  @Test
  void compareNumberStrings_equalVersions_returnsTrue() {
    assertThat(Tool.compareNumberStrings("2.1.0", "2.01.00")).isTrue();
    assertThat(Tool.compareNumberStrings("1.0.0", "1.0.0")).isTrue();
  }

  @Test
  void compareNumberStrings_differentValues_returnsFalse() {
    assertThat(Tool.compareNumberStrings("2.1.0", "2.2.0")).isFalse();
    assertThat(Tool.compareNumberStrings("1.0.0", "2.0.0")).isFalse();
  }

  @Test
  void compareNumberStrings_differentTokenCount_returnsFalse() {
    assertThat(Tool.compareNumberStrings("2.1", "2.1.1")).isFalse();
  }

  @Test
  void compareNumberStrings_nonNumeric_returnsFalse() {
    assertThat(Tool.compareNumberStrings("a.b.c", "1.2.3")).isFalse();
  }

  @Test
  void compareNumberStrings_customSeparator_works() {
    assertThat(Tool.compareNumberStrings("2-1-0", "2-01-00", "-")).isTrue();
    assertThat(Tool.compareNumberStrings("1-0", "1-1", "-")).isFalse();
  }

  @Test
  void isPortAvailable_invalidPort_throwsIllegalArgument() {
    assertThatThrownBy(() -> Tool.isPortAvaialble(0)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> Tool.isPortAvaialble(65536))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void isPortAvailable_validPort_returnsBooleanWithoutException() {
    // Port 1 requires root privileges so will return false; port in high range should be free
    boolean result = Tool.isPortAvaialble(65530);
    assertThat(result).isIn(true, false);
  }

  @Test
  void isValidEmailAddress_validAddresses_returnsTrue() {
    assertThat(Tool.isValidEmailAddress("user@example.com")).isTrue();
    assertThat(Tool.isValidEmailAddress("test.name+tag@sub.domain.org")).isTrue();
  }

  @Test
  void isValidEmailAddress_invalidAddresses_returnsFalse() {
    assertThat(Tool.isValidEmailAddress(null)).isFalse();
    assertThat(Tool.isValidEmailAddress("")).isFalse();
    assertThat(Tool.isValidEmailAddress("notanemail")).isFalse();
    assertThat(Tool.isValidEmailAddress("@nodomain.com")).isFalse();
  }
}
