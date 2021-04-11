package org.jb.toc;

import java.io.FileNotFoundException;
import picocli.CommandLine;

public class Launcher implements Runnable {

  @CommandLine.Option(names={"-from"}, description="Markdown file location", required=true)
  String from;

  public static void main(String[] args) {
	int exitCode = new CommandLine(new Launcher()).execute(args);
  }

  @Override
  public void run(){
      MarkdownTocGenerator generator = new MarkdownTocGenerator(from);
      generator.generateToc();
  }
}
