package com.bluecubs.xinco.tools;

import static com.bluecubs.xinco.tools.Tool.copyFile;
import static com.bluecubs.xinco.tools.Tool.getFileSuffix;
import static com.bluecubs.xinco.tools.Tool.getImageDim;
import static com.bluecubs.xinco.tools.Tool.textHasContent;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ToolAdditionalTest {

  @TempDir File tempDir;

  @Test
  void textHasContent_nullReturnsFalse() {
    assertThat(textHasContent(null)).isFalse();
  }

  @Test
  void textHasContent_emptyStringReturnsFalse() {
    assertThat(textHasContent("")).isFalse();
  }

  @Test
  void textHasContent_blankStringReturnsFalse() {
    assertThat(textHasContent("   ")).isFalse();
  }

  @Test
  void textHasContent_nonEmptyReturnsTrue() {
    assertThat(textHasContent("hello")).isTrue();
    assertThat(textHasContent(" x ")).isTrue();
  }

  @Test
  void getFileSuffix_returnsExtension() {
    assertThat(getFileSuffix("document.pdf")).isEqualTo("pdf");
    assertThat(getFileSuffix("archive.tar.gz")).isEqualTo("gz");
  }

  @Test
  void getFileSuffix_noExtensionReturnsEmpty() {
    assertThat(getFileSuffix("README")).isEqualTo("");
  }

  @Test
  void getFileSuffix_nullReturnsNull() {
    assertThat(getFileSuffix(null)).isNull();
  }

  @Test
  void getFileSuffix_dotOnlyReturnsEmpty() {
    assertThat(getFileSuffix("file.")).isEqualTo("");
  }

  @Test
  void getImageDim_returnsNullForNonImagePath() {
    assertThat(getImageDim("/no/such/file.xyz")).isNull();
  }

  @Test
  void getImageDim_returnsNullForNullSuffix() {
    assertThat(getImageDim("filewithoutext")).isNull();
  }

  @Test
  void copyFile_copiesContents() throws IOException {
    File src = new File(tempDir, "src.txt");
    File dst = new File(tempDir, "dst.txt");
    Files.writeString(src.toPath(), "copy me");
    copyFile(src, dst);
    assertThat(Files.readString(dst.toPath())).isEqualTo("copy me");
  }
}
