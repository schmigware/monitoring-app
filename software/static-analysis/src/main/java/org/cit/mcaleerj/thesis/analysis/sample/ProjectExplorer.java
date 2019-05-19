package org.cit.mcaleerj.thesis.analysis.sample;

import java.io.File;

public class ProjectExplorer {

  private static final String EXT_JAVA = ".java";

  private final FileProcessor processor;

  public ProjectExplorer(final FileProcessor processor) {
    this.processor = processor;
  }

  public void process(File file) {
    handleFile(file);
  }

  private void handleFile(final File file) {
    if (file.isDirectory()) {
      for (File child : file.listFiles()) {
        handleFile(child);
      }
    } else {
      if (file.getName().endsWith(EXT_JAVA))
        this.processor.process(file);
    }
  }

  public interface FileProcessor {
    void process(File file);
  }

}
