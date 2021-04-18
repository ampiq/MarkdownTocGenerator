package org.jb.toc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jb.toc.model.HeaderTree;

public class MarkdownTocGenerator {
  private static final Logger LOGGER = LogManager.getLogger(MarkdownTocGenerator.class.getName());

  private static final String HEADER_SYMBOL = "#";
  private static final String LINK_SEPARATOR = "-";

  private String path;

  private Map<String, Integer> linksCache;

  public MarkdownTocGenerator(String path) {
	this.path = path;
	linksCache = new HashMap<>();
  }

  public void generateToc() {
	try (InputStreamReader in = new InputStreamReader(new FileInputStream(path)); BufferedReader buffer = new BufferedReader(in)) {
	  String line;
	  HeaderTree tree = new HeaderTree();
	  while ((line = buffer.readLine()) != null) {
		HeaderType currHeader = getHeaderType(line);
		if (currHeader != null) {
		  String tocLine = getTocLine(line, currHeader);
		  tree.add(currHeader, tocLine);
		}
	  }
	  tree.traverse();
	  printContent();
	} catch (FileNotFoundException e) {
	  LOGGER.error("Incorrect path to markdown file");
	} catch (Exception e) {
	  LOGGER.error("Something went wrong while processing your markdown file");
	}
  }

  private String getTocLine(String line, HeaderType type) {
    String withoutHeaderSymbols = line.substring(type.starts().length()).trim();
	String preparedLink = simplifyContent(withoutHeaderSymbols);
	String resultLink;
	if (linksCache.containsKey(preparedLink)) {
	  int currNumberOfLinks = linksCache.get(preparedLink);
	  resultLink = preparedLink + LINK_SEPARATOR +currNumberOfLinks;
	  linksCache.put(preparedLink, ++currNumberOfLinks);
	} else {
	  resultLink = preparedLink;
	  linksCache.put(resultLink, 1);
	}
	return "["
		+ withoutHeaderSymbols
		+ "]"
		+ "(#"
		+ resultLink
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
	  LOGGER.error("Something went wrong while processing your markdown file");
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
  private HeaderType getHeaderType(String line) {
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
