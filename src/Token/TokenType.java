package Token;

public class TokenType {
  private String name;
  private String regex;

  TokenType(String name, String regex) {
    this.name = name;
    this.regex = regex;
  }

  public String getName() {
    return name;
  }

  public String getRegex() {
    return regex;
  }
}

