package com.bluecubs.xinco.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
  public static void zipFolder(String sourceFolderPath, String zipFilePath) throws IOException {
    Path zipFile = Paths.get(zipFilePath);
    Files.createDirectories(zipFile.getParent());
    Files.createFile(zipFile);
    try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
      Path sourceFolder = Paths.get(sourceFolderPath);
      Files.walk(sourceFolder)
          .filter(path -> !Files.isDirectory(path))
          .forEach(
              path -> {
                ZipEntry zipEntry = new ZipEntry(sourceFolder.relativize(path).toString());
                try {
                  zipOutputStream.putNextEntry(zipEntry);
                  Files.copy(path, zipOutputStream);
                  zipOutputStream.closeEntry();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
    }
  }
}
