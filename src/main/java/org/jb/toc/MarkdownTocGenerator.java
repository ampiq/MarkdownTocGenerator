package org.jb.toc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MarkdownTocGenerator {
  private static final Logger LOGGER = LogManager.getLogger(MarkdownTocGenerator.class.getName());

  private static final String HEADER_SYMBOL = "#";
  private static final char SHIFT = '\t';

  private String path;

  public MarkdownTocGenerator(String path) {
	this.path = path;
  }

  public void generateToc() {
	try (InputStreamReader in = new InputStreamReader(new FileInputStream(path)); BufferedReader buffer = new BufferedReader(in)) {
	  String line;
	  while ((line = buffer.readLine()) != null) {
		HeaderType currHeader = getHeaderType(line);
		if (currHeader != null) {
		  for (int i = 0; i < currHeader.starts().length() - 1; i++) {
			System.out.print(SHIFT);
		  }
		  System.out.println(getTocLine(line, currHeader));
		}
	  }
	  printContent();
	} catch (FileNotFoundException e) {
	  LOGGER.error("Incorrect path to markdown file");
	} catch (Exception e) {
	  LOGGER.error("Something went wrond wile processing your markdown file");
	}
  }

  private String getTocLine(String line, HeaderType type) {
    String withoutHeaderSymbols = line.substring(type.starts().length()).trim();
    return "["
		+ withoutHeaderSymbols
		+ "]"
		+ "(#"
		+ simplifyContent(withoutHeaderSymbols)
		+ ")";

  }

  private void printContent() {
	System.out.println();
	try (InputStreamReader in = new InputStreamReader(new FileInputStream(path)); BufferedReader buffer = new BufferedReader(in)) {
	  String line;
	  while ((line = buffer.readLine()) != null) {
		System.out.println(line);
	  }
	} catch (Exception e) {
	  LOGGER.error("Something went wrond wile processing your markdown file");
	}
  }

  private String simplifyContent(String line) {
    return line.toLowerCase().replaceAll("[\\W]|_", "-");
  }

  /**
   *
   * @param line notNull markdown file line
   * @return @see(HeaderType) type of header or Null if the line does not contain any header
   */
  private static HeaderType getHeaderType(String line) {
    if (!line.startsWith(HEADER_SYMBOL)) {
      return null;
	}
    int currentType = 1;
	for (int i = 1; i < line.length() && i <= HeaderType.MAXIMUM_LENGTH; i++) {
	  if (line.charAt(i) != '#') {
	    break;
	  } else {
	    currentType++;
	  }
	}
    return HeaderType.of(currentType);
  }

}
