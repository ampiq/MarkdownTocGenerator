package org.jb.toc;

public enum HeaderType {

  FIRST_LEVEL("#"),
  SECOND_LEVEL("##"),
  THIRD_LEVEL("###"),
  FOURTH_LEVEL("####"),
  FIFTH_LEVEL("#####"),
  SIXTH_LEVEL("######");

  public static final int MAXIMUM_LENGTH = 6;

  private final String level;

  HeaderType(String level) {
    this.level = level;
  }

  public String starts() {
    return level;
  }

  public static HeaderType of(int level) {
    switch (level) {
	  case 1 : return FIRST_LEVEL;
	  case 2 : return SECOND_LEVEL;
	  case 3 : return THIRD_LEVEL;
	  case 4 : return FOURTH_LEVEL;
	  case 5 : return FIFTH_LEVEL;
	  case 6 : return SIXTH_LEVEL;
	  default: throw new IllegalArgumentException("Incompatible level type");
	}
  }

  public int diff(HeaderType that) {
	return that.starts().length() - this.starts().length();
  }
}
