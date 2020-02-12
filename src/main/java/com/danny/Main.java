package com.danny;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

public class Main {
  public static void main(String[] args) throws IOException {
    System.out.println("Hello World");
    String mode = args[0];
    int level = Integer.parseInt(args[1]);
    Path source = Paths.get(args[2]);
    Path target = Paths.get(args[3]);
    int bufferSize = Integer.parseInt(args[4]);

    if (mode.equalsIgnoreCase("buffer")) {
      bufferedGZip(source, target, level, bufferSize);
    } else if (mode.equalsIgnoreCase("gzip")) {
      gZip(source, target, level);
    } else if (mode.equalsIgnoreCase("move")) {
      Files.move(source, target, ATOMIC_MOVE, REPLACE_EXISTING);
    }
  }

  private static void gZip(Path source, Path target, int level) throws IOException {
    try (
        FileOutputStream fos = new FileOutputStream(target.toAbsolutePath().toString());
        OutputStream gZIPOut = new GZIPOutputStream(fos) {
          {
            def.setLevel(level);
          }
        }
    ) {
      Files.copy(source, gZIPOut);
    }
  }

  private static void bufferedGZip(Path source, Path target, int level, int bufferSize) throws IOException {
    try (
        FileOutputStream fos = new FileOutputStream(target.toAbsolutePath().toString());
        BufferedOutputStream bos = new BufferedOutputStream(fos, bufferSize);
        OutputStream gZIPOut = new GZIPOutputStream(bos) {
          {
            def.setLevel(level);
          }
        }
    ) {
      Files.copy(source, gZIPOut);
    }
  }
}
