package Lexer;

import Token.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Lexer {
  private String code;
  private int position = 0;
  private List<Token> tokenList = new ArrayList<>();;

  public Lexer(String code) {
    this.code = code;
  }

  public void analys() {
    try {
      this.nextToken();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    this.filterSpaces();
  }

  private boolean nextToken() throws Exception {
    if (isPositionMoreTokenList())
      return true;
    for (TokenTypeList token : TokenTypeList.values()) {
      Pattern regExp = Pattern.compile("^" + token.getType().getRegex());
      String subText = this.code.substring(this.position);
      Matcher matcher = regExp.matcher(subText);
      if (matcher.find()) {
        String result = subText.substring(matcher.start(), matcher.end());
        tokenList.add(new Token(this.position, result, token.getType()));
        this.position += result.length();
        return this.nextToken();
      }

    }
    throw new Exception("На позиции " + this.position + " обнаружена ошибка");
  }

  private boolean isPositionMoreTokenList() {
    return this.position >= this.code.length();
  }

  private void filterSpaces() {
    this.tokenList = this.tokenList.stream()
        .filter(token -> token.getToken().getName() != TokenTypeList.SPACE.getType().getName())
        .collect(Collectors.toList());
  }

  public List<Token> getTokenList() {
    return tokenList;
  }

}
