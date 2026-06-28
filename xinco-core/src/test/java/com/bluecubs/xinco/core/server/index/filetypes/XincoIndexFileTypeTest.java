package com.bluecubs.xinco.core.server.index.filetypes;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class XincoIndexFileTypeTest {

  @TempDir File tempDir;

  @Test
  void xincoIndexText_getFileContentReader_returnsReaderForTextFile() throws Exception {
    File f = new File(tempDir, "hello.txt");
    Files.writeString(f.toPath(), "hello world");

    XincoIndexText indexer = new XincoIndexText();
    try (Reader r = indexer.getFileContentReader(f)) {
      assertThat(r).isNotNull();
      char[] buf = new char[11];
      r.read(buf);
      assertThat(new String(buf)).isEqualTo("hello world");
    }
  }

  @Test
  void xincoIndexText_getFileContentReader_returnsNullForMissingFile() {
    XincoIndexText indexer = new XincoIndexText();
    Reader r = indexer.getFileContentReader(new File(tempDir, "nonexistent.txt"));
    assertThat(r).isNull();
  }

  @Test
  void xincoIndexText_getFileContentString_returnsNull() throws Exception {
    File f = new File(tempDir, "test.txt");
    Files.writeString(f.toPath(), "content");
    XincoIndexText indexer = new XincoIndexText();
    assertThat(indexer.getFileContentString(f)).isNull();
  }

  @Test
  void xincoIndexGenericFile_getFileContentReader_returnsNull() throws Exception {
    File f = new File(tempDir, "generic.bin");
    Files.write(f.toPath(), new byte[] {1, 2, 3});
    XincoIndexGenericFile indexer = new XincoIndexGenericFile();
    assertThat(indexer.getFileContentReader(f)).isNull();
  }

  @Test
  void xincoIndexGenericFile_getFileContentString_returnsStringForExistingFile() throws Exception {
    File f = new File(tempDir, "sample.txt");
    Files.writeString(f.toPath(), "extractable content");
    XincoIndexGenericFile indexer = new XincoIndexGenericFile();
    // Tika returns non-null for a readable file (content may vary by Tika version)
    String result = indexer.getFileContentString(f);
    assertThat(result).isNotNull();
  }

  @Test
  void xincoIndexGenericFile_getFileContentString_returnsNullForMissingFile() {
    XincoIndexGenericFile indexer = new XincoIndexGenericFile();
    // FileInputStream throws IOException → caught → returns null
    String result = indexer.getFileContentString(new File(tempDir, "missing.txt"));
    assertThat(result).isNull();
  }

  @Test
  void xincoIndexHTML_getFileContentReader_returnsReaderForHtmlFile() throws Exception {
    File f = new File(tempDir, "page.html");
    Files.writeString(f.toPath(), "<html><body>test</body></html>");
    XincoIndexHTML indexer = new XincoIndexHTML();
    try (Reader r = indexer.getFileContentReader(f)) {
      assertThat(r).isNotNull();
    }
  }

  @Test
  void xincoIndexHTML_getFileContentReader_returnsNullForMissingFile() {
    XincoIndexHTML indexer = new XincoIndexHTML();
    Reader r = indexer.getFileContentReader(new File(tempDir, "nonexistent.html"));
    assertThat(r).isNull();
  }
}
